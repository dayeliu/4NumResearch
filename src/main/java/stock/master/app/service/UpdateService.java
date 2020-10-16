package stock.master.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import stock.master.app.entity.Weekly;
import stock.master.app.resource.vo.updateStockListResult;
import stock.master.app.service.Impl.CrawlNorwayService_Impl;
import stock.master.app.service.Impl.ExportToCsv;
import stock.master.app.service.Impl.StockListService_Impl;
import stock.master.app.util.Log;

@Service
public class UpdateService extends BaseService {

	@Autowired
	protected StockListService_Impl stockList_impl;

	@Autowired
	protected CrawlNorwayService_Impl norwayService_impl;
	
	@Autowired
	protected ExportToCsv exportToCsv;
	
	public updateStockListResult UpdateStockList() throws Exception {

		updateStockListResult result = null;

		try {
			result = stockList_impl.updateList();
		} catch (Exception e) {
			throw new Exception(Log.error(e.toString()));
		}

		return result;
	}

	public String initDbBySid (String sid) throws Exception {
		
		if (!basicInfoRepository.existsById(sid)) {
			return Log.error("stock id not exist. id = " + sid);
		}
		
		try {

			InitWeekly(sid);

		} catch (Exception e) {
			throw new Exception(Log.error(e.toString()));
		}

		return "initDbBySid done";
	}

	public String updateDbBySid (String sid) throws Exception {
		
		if (!basicInfoRepository.existsById(sid)) {
			return Log.error("stock id not exist. id = " + sid);
		}

		try {

			UpdateWeekly(sid);
						
		} catch (Exception e) {
			throw new Exception(Log.error(e.toString()));
		}
		
		return "updateDbBySid done";
	}

	private String InitWeekly (String sid) throws Exception {
		if (weeklyRepository.existsByStockId(sid)) {
			weeklyRepository.deleteByStockId(sid);
		}
		List<Weekly> list = norwayService_impl.getWeeklyInfo(sid, 20);
		weeklyRepository.saveAll(list);
		
		exportToCsv.exportMonthly(sid, list);

		return "InitWeekly done";
	}

	private String UpdateWeekly (String sid) throws Exception {
		if (!weeklyRepository.existsByStockId(sid)) {
			return Log.error("stock not init yet. sid = " + sid);
		}

		Weekly fromDb = weeklyRepository.findTop1ByStockIdOrderByDateDesc(sid);
		List<Weekly> newData = norwayService_impl.getWeeklyInfo(sid, 6);
		List<Weekly> readyToUpdate = new ArrayList<Weekly>();
		for (Weekly info : newData) {
			if (info.getDate().after(fromDb.getDate())) {
				readyToUpdate.add(info);
			}
		}
		weeklyRepository.saveAll(readyToUpdate);
		
		return "UpdateWeekly done";
	}
}
