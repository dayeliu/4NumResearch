package stock.master.app.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import stock.master.app.entity.BasicInfo;
import stock.master.app.entity.Daily;
import stock.master.app.entity.Monthly;
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
	 * initial database
	 * */
	public void initDb () throws Exception {

		Log.debug("===== initDb =====");

		List<BasicInfo> stockIds = basicInfoRepository.findTop10ByOrderByStockIdAsc();

		int idx = 0;
		for (BasicInfo stock : stockIds) {
			Log.debug("[Index : " + idx + "] " + stock.getStockId());

			initDbBySid(stock.getStockId());

			idx++;
		}

		Log.debug("===== initDb done =====");
	}

	public void initDbBySid (String sid) throws Exception {

		Log.debug("===== initDbBySid : " + sid + " =====");

		if (!basicInfoRepository.existsById(sid)) {
			throw new Exception(Log.error("stock id not exist. id = " + sid));
		}
		
		InitDaily(sid);
		//InitWeekly(sid);
		//InitMonthly(sid);

		Log.debug("===== initDbBySid : " + sid + "  done =====");
	}

	/*
	 * update database
	 * */
	public void updateDb () throws Exception {
		Log.debug("===== updateDb =====");

		List<BasicInfo> stockIds = basicInfoRepository.findTop10ByOrderByStockIdAsc();

		int idx = 0;
		for (BasicInfo stock : stockIds) {
			Log.debug("[Index : " + idx + "] " + stock.getStockId());

			updateDbBySid(stock.getStockId());

			idx++;
		}

		Log.debug("===== updateDb done =====");
	}

	public void updateDbBySid (String sid) throws Exception {

		Log.debug("===== updateDbBySid : " + sid + " =====");

		if (!basicInfoRepository.existsById(sid)) {
			throw new Exception(Log.error("stock id not exist. id = " + sid));
		}

		UpdateDaily(sid);
		UpdateWeekly(sid);
		UpdateMonthly(sid);
		
		Log.debug("===== updateDbBySid : " + sid + " done =====");
	}

	/*
	 * handle daily data
	 * 
	 * 從去年1/1開始
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
}
