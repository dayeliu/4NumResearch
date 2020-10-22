package stock.master.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import stock.master.app.entity.BasicInfo;
import stock.master.app.entity.Weekly;
import stock.master.app.resource.vo.updateStockListResult;
import stock.master.app.service.Impl.CrawlNorwayWeb;
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
	protected ExportToCsv exportToCsvImpl;
	
	public updateStockListResult UpdateStockList() throws Exception {

		Log.debug("===== UpdateStockList =====");

		updateStockListResult result = null;

		try {
			result = handleStockListImpl.updateList();
		} catch (Exception e) {
			throw new Exception(Log.error(e.toString()));
		}

		Log.debug(result.toString());
		Log.debug("===== UpdateStockList done =====");

		return result;
	}

	/*
	 * initial database
	 * */
	public void initDb () {

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

	public void initDbBySid (String sid) {

		Log.debug("===== initDbBySid : " + sid + " =====");

		if (!basicInfoRepository.existsById(sid)) {
			Log.error("stock id not exist. id = " + sid);
			return;
		}
		
		try {

			InitWeekly(sid);

		} catch (Exception e) {
			Log.error(e.toString());
			return;
		}

		Log.debug("===== initDbBySid : " + sid + "  done =====");
	}

	/*
	 * update database
	 * */
	public void updateDb () {
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

	public void updateDbBySid (String sid) {

		Log.debug("===== updateDbBySid : " + sid + " =====");

		if (!basicInfoRepository.existsById(sid)) {
			Log.error("stock id not exist. id = " + sid);
			return;
		}

		try {

			UpdateWeekly(sid);
						
		} catch (Exception e) {
			Log.error(e.toString());
			return;
		}
		
		Log.debug("===== updateDbBySid : " + sid + " done =====");
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
			Log.error("stock not init yet. sid = " + sid);
			return;
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
		if (weeklyRepository.existsByStockId(sid)) {
			weeklyRepository.deleteByStockId(sid);
		}

		List<Weekly> list = crawNorwayImpl.getWeeklyInfo(sid, 10);
		int count = list.size();
		String msg = "Count:" + count + " Add From " + list.get(0).getDate().toString() + " to " + list.get(count - 1).getDate().toString();
		Log.debug(msg);
		
		weeklyRepository.saveAll(list);

		exportToCsvImpl.exportMonthly(sid);
		Log.debug("InitMonthly done");
	}
	
	private void UpdateMonthly (String sid) throws Exception {
		if (!weeklyRepository.existsByStockId(sid)) {
			Log.error("stock not init yet. sid = " + sid);
			return;
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

		exportToCsvImpl.exportMonthly(sid);
		Log.debug("UpdateMonthly done");
	}
}
