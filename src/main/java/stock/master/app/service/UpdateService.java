package stock.master.app.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import stock.master.app.entity.BasicInfo;
import stock.master.app.entity.Daily;
import stock.master.app.entity.Monthly;
import stock.master.app.entity.Quarterly;
import stock.master.app.entity.Weekly;
import stock.master.app.resource.vo.updateStockListResult;
import stock.master.app.service.Impl.CrawlNorwayWeb;
import stock.master.app.service.Impl.CrawlWearnWeb;
import stock.master.app.service.Impl.ExportToCsv;
import stock.master.app.service.Impl.HandleStockList;
import stock.master.app.util.Log;

@Service
public class UpdateService extends BaseService {

	@Autowired
	protected HandleStockList handleStockListImpl;

	@Autowired
	protected CrawlNorwayWeb crawNorwayImpl;
	
	@Autowired
	protected CrawlWearnWeb crawWearnyImpl;
	
	@Autowired
	protected ExportToCsv exportToCsvImpl;
	
	public updateStockListResult UpdateStockList() throws Exception {

		Log.debug("===== UpdateStockList =====");

		updateStockListResult result = null;

		result = handleStockListImpl.updateList();
		Log.debug(result.toString());
		
		exportToCsvImpl.exportStockList();
		Log.debug("===== UpdateStockList done =====");

		return result;
	}

	/*
	 * to determine when to update db
	 * update database everyday at 7:00 pm
	 * */
	@Async
	public void UpdateDb () throws Exception {
		Log.debug("===== UpdateDb =====");

		LocalDate date = LocalDate.now();
		DayOfWeek dow = date.getDayOfWeek();
		String dayName = dow.getDisplayName(TextStyle.FULL, Locale.ENGLISH);
		System.out.println(dayName);

		// update daily info everyday
		HandlyDaily();

		// update weekly info every weekend
		if (dayName.equals("Sunday")) {
			System.out.println("update weekly info");
		}
		HandlyWeekly();

		// update monthly info when ...
		HandlyMonthly();

		// update quarterly info when ...
		//HandlyQuarterly();

		Log.debug("===== UpdateDb done =====");
	}

	private void HandlyDaily () throws Exception {
		Log.debug("===== HandlyDaily =====");

		Date curDate = new Date();
		if (curDate.getHours() < 19) {
			Log.debug("Invalid update time : " + curDate.toString());
			//return;
		}

		List<BasicInfo> stockIds = basicInfoRepository.findTop1ByOrderByStockIdAsc();

		int idx = 0;
		for (BasicInfo stock : stockIds) {
			Log.debug("[Index : " + idx + "] " + stock.getStockId());

			Daily fromDb = dailyRepository.findTop1ByStockIdOrderByDateDesc(stock.getStockId());
			if (fromDb == null) {
				Log.debug("Need to initial daily db. sid = " + stock.getStockId());
				InitDaily(stock.getStockId());
			} else {
				Date dateFromDb = fromDb.getDate();
				if ((curDate.getYear()*1000 + curDate.getMonth()*50 + curDate.getDay()) > (dateFromDb.getYear()*1000 + dateFromDb.getMonth()*50 + dateFromDb.getDay())) {
					UpdateDaily(stock.getStockId());
				}
			}

			idx++;
		}

		Log.debug("===== HandlyDaily done =====");
	}

	/*
	 * handle daily data
	 * 
	 * start from Jan. 1 last year
	 * 
	 * */
	private void InitDaily (String sid) throws Exception {
		if (dailyRepository.existsByStockId(sid)) {
			dailyRepository.deleteByStockId(sid);
		}

		List<Daily> list = new ArrayList<Daily>();
		
		int yearsAgo = 1;	// inital from one year ago
		LocalDate currentDate = LocalDate.now();
		int year = currentDate.getYear();
		int month = currentDate.getMonthValue();

		for (int i = year - 1911 ; i >= year - 1911 - yearsAgo ; i--) {
			// last year
			if (i != year - 1911) {
				month = 13;
			}

			for (int j = month - 1 ; j >= 1 ; j--) {
				String strMonth = "";
				if (j >= 10) {
					strMonth = String.valueOf(j);
				} else {
					strMonth = "0" + String.valueOf(j);
				}
				
				list.addAll(crawWearnyImpl.getDailylyInfo(sid, String.valueOf(i), strMonth));
			}
		}
		crawWearnyImpl.CalculateAverage(list);

		int count = list.size();
		String msg = "Count:" + count + " Add From " + list.get(0).getDate().toString() + " to " + list.get(count - 1).getDate().toString();
		Log.debug(msg);
		
		dailyRepository.saveAll(list);

		UpdateDaily(sid);

		exportToCsvImpl.exportDaily(sid);
		Log.debug("InitDaily done");
	}
	
	private void UpdateDaily (String sid) throws Exception {
		if (!dailyRepository.existsByStockId(sid)) {
			throw new Exception(Log.error("stock not init yet. sid = " + sid));
		}

		Daily fromDb = dailyRepository.findTop1ByStockIdOrderByDateDesc(sid);
		LocalDate currentDate = LocalDate.now();
		int year = currentDate.getYear();
		int month = currentDate.getMonthValue();
		String strMonth = "";
		if (month >= 10) {
			strMonth = String.valueOf(month);
		} else {
			strMonth = "0" + String.valueOf(month);
		}
		List<Daily> newData = crawWearnyImpl.getDailylyInfo(sid, String.valueOf(year - 1911), strMonth);
		List<Daily> readyToUpdate = new ArrayList<Daily>();
		for (Daily info : newData) {
			if (info.getDate().after(fromDb.getDate())) {
				readyToUpdate.add(info);
			}
		}

		String msg = "";
		int count = readyToUpdate.size();
		if (count == 0) {
			msg = "Already up to date";
		} else {
			msg = "Count:" + count + " Update From " + readyToUpdate.get(0).getDate().toString() + " to " + readyToUpdate.get(count - 1).getDate().toString();
		}
		Log.debug(msg);

		dailyRepository.saveAll(readyToUpdate);

		// calculate average
		List<Daily> fromDb2 = dailyRepository.findTop60ByStockIdOrderByDateDesc(sid);
		crawWearnyImpl.CalculateAverage(fromDb2);
		dailyRepository.saveAll(fromDb2);

		exportToCsvImpl.exportDaily(sid);
		Log.debug("UpdateDaily done");
	}

	private void HandlyWeekly() throws Exception {
		Log.debug("===== HandlyWeekly =====");

		Date curDate = new Date();
		if (curDate.getHours() < 19) {
			Log.debug("Invalid update time : " + curDate.toString());
			//return;
		}

		List<BasicInfo> stockIds = basicInfoRepository.findTop1ByOrderByStockIdAsc();

		int idx = 0;
		for (BasicInfo stock : stockIds) {
			Log.debug("[Index : " + idx + "] " + stock.getStockId());

			Weekly fromDb = weeklyRepository.findTop1ByStockIdOrderByDateDesc(stock.getStockId());
			if (fromDb == null) {
				Log.debug("Need to initial weekly db. sid = " + stock.getStockId());
				InitWeekly(stock.getStockId());
			} else {
				Date dateFromDb = fromDb.getDate();
				if (((curDate.getYear()*1000 + curDate.getMonth()*50 + curDate.getDay()) - (dateFromDb.getYear()*1000 + dateFromDb.getMonth()*50 + dateFromDb.getDay())) >= 7) {
					UpdateWeekly(stock.getStockId());
				}
			}

			idx++;
		}

		Log.debug("===== HandlyWeekly done =====");
	}
	/*
	 * handle weekly data
	 * 
	 * */
	private void InitWeekly (String sid) throws Exception {
		if (weeklyRepository.existsByStockId(sid)) {
			weeklyRepository.deleteByStockId(sid);
		}

		List<Weekly> list = crawNorwayImpl.getWeeklyInfo(sid, 10);
		int count = list.size();
		String msg = "Count:" + count + " Add From " + list.get(0).getDate().toString() + " to " + list.get(count - 1).getDate().toString();
		Log.debug(msg);
		
		weeklyRepository.saveAll(list);

		exportToCsvImpl.exportWeekly(sid);
		Log.debug("InitWeekly done");
	}

	private void UpdateWeekly (String sid) throws Exception {
		if (!weeklyRepository.existsByStockId(sid)) {
			throw new Exception(Log.error("stock not init yet. sid = " + sid));
		}

		Weekly fromDb = weeklyRepository.findTop1ByStockIdOrderByDateDesc(sid);
		List<Weekly> newData = crawNorwayImpl.getWeeklyInfo(sid, 4);
		List<Weekly> readyToUpdate = new ArrayList<Weekly>();
		for (Weekly info : newData) {
			if (info.getDate().after(fromDb.getDate())) {
				readyToUpdate.add(info);
			}
		}

		String msg = "";
		int count = readyToUpdate.size();
		if (count == 0) {
			msg = "Already up to date";
		} else {
			msg = "Count:" + count + " Update From " + readyToUpdate.get(0).getDate().toString() + " to " + readyToUpdate.get(count - 1).getDate().toString();
		}
		Log.debug(msg);

		weeklyRepository.saveAll(readyToUpdate);

		exportToCsvImpl.exportWeekly(sid);
		Log.debug("UpdateWeekly done");
	}
	
	private void HandlyMonthly() throws Exception {
		Log.debug("===== HandlyMonthly =====");

		Date curDate = new Date();
		if (curDate.getHours() < 19) {
			Log.debug("Invalid update time : " + curDate.toString());
			//return;
		}

		List<BasicInfo> stockIds = basicInfoRepository.findTop1ByOrderByStockIdAsc();

		int idx = 0;
		for (BasicInfo stock : stockIds) {
			Log.debug("[Index : " + idx + "] " + stock.getStockId());

			Monthly fromDb = monthlyRepository.findTop1ByStockIdOrderByDateDesc(stock.getStockId());
			if (fromDb == null) {
				Log.debug("Need to initial monthly db. sid = " + stock.getStockId());
				InitMonthly(stock.getStockId());
			} else {
				Date dateFromDb = fromDb.getDate();
				if ((curDate.getYear()*1000 + (curDate.getMonth()-1)*50) > (dateFromDb.getYear()*1000 + dateFromDb.getMonth()*50)) {
					UpdateMonthly(stock.getStockId());
				}
			}

			idx++;
		}

		Log.debug("===== HandlyMonthly done =====");
	}
	/*
	 * handle monthly data
	 * 
	 * */
	private void InitMonthly (String sid) throws Exception {
		if (monthlyRepository.existsByStockId(sid)) {
			monthlyRepository.deleteByStockId(sid);
		}

		List<Monthly> list = crawWearnyImpl.getMonthlyInfo(sid);
		int count = list.size();
		String msg = "Count:" + count + " Add From " + list.get(0).getDate().toString() + " to " + list.get(count - 1).getDate().toString();
		Log.debug(msg);
		
		monthlyRepository.saveAll(list);

		exportToCsvImpl.exportMonthly(sid);
		Log.debug("InitMonthly done");
	}
	
	private void UpdateMonthly (String sid) throws Exception {
		if (!monthlyRepository.existsByStockId(sid)) {
			throw new Exception(Log.error("stock not init yet. sid = " + sid));
		}

		Monthly fromDb = monthlyRepository.findTop1ByStockIdOrderByDateDesc(sid);
		List<Monthly> newData = crawWearnyImpl.getMonthlyInfo(sid);
		List<Monthly> readyToUpdate = new ArrayList<Monthly>();
		for (Monthly info : newData) {
			if (info.getDate().after(fromDb.getDate())) {
				readyToUpdate.add(info);
			}
		}

		String msg = "";
		int count = readyToUpdate.size();
		if (count == 0) {
			msg = "Already up to date";
		} else {
			msg = "Count:" + count + " Update From " + readyToUpdate.get(0).getDate().toString() + " to " + readyToUpdate.get(count - 1).getDate().toString();
		}
		Log.debug(msg);

		monthlyRepository.saveAll(readyToUpdate);

		exportToCsvImpl.exportMonthly(sid);
		Log.debug("UpdateMonthly done");
	}

	private void HandlyQuarterly() throws Exception {
		Log.debug("===== HandlyQuarterly =====");

		List<BasicInfo> stockIds = basicInfoRepository.findTop1ByOrderByStockIdAsc();

		int idx = 0;
		for (BasicInfo stock : stockIds) {
			Log.debug("[Index : " + idx + "] " + stock.getStockId());

			if (quarterlyRepository.existsByStockId(stock.getStockId())) {
				UpdateQuarterly(stock.getStockId());
			} else {
				Log.debug("Need to initial quarterly db. sid = " + stock.getStockId());
				InitQuarterly(stock.getStockId());
			}

			idx++;
		}

		Log.debug("===== HandlyQuarterly done =====");
	}
	/*
	 * handle quarterly data
	 * */
	private void InitQuarterly (String sid) throws Exception {
		if (quarterlyRepository.existsByStockId(sid)) {
			quarterlyRepository.deleteByStockId(sid);
		}

		List<Quarterly> list = crawWearnyImpl.getQuarterlyInfo(sid);
		int count = list.size();
		String msg = "Count:" + count + " Add From " + list.get(0).getDate().toString() + " to " + list.get(count - 1).getDate().toString();
		Log.debug(msg);

		quarterlyRepository.saveAll(list);

		exportToCsvImpl.exportQuarterly(sid);
		Log.debug("InitQuarterly done");
	}

	private void UpdateQuarterly (String sid) throws Exception {
		if (!quarterlyRepository.existsByStockId(sid)) {
			throw new Exception(Log.error("stock not init yet. sid = " + sid));
		}

		Quarterly fromDb = quarterlyRepository.findTop1ByStockIdOrderByDateDesc(sid);
		List<Quarterly> newData = crawWearnyImpl.getQuarterlyInfo(sid);
		List<Quarterly> readyToUpdate = new ArrayList<Quarterly>();
		for (Quarterly info : newData) {
			if (info.getDate().after(fromDb.getDate())) {
				readyToUpdate.add(info);
			}
		}

		String msg = "";
		int count = readyToUpdate.size();
		if (count == 0) {
			msg = "Already up to date";
		} else {
			msg = "Count:" + count + " Update From " + readyToUpdate.get(0).getDate().toString() + " to " + readyToUpdate.get(count - 1).getDate().toString();
		}
		Log.debug(msg);

		quarterlyRepository.saveAll(readyToUpdate);

		exportToCsvImpl.exportQuarterly(sid);
		Log.debug("UpdateQuarterly done");
	}
}
