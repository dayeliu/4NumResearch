package stock.master.app.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import stock.master.app.entity.Weekly;
import stock.master.app.resource.vo.updateStockListResult;
import stock.master.app.service.Impl.CrawlNorwayService_Impl;
import stock.master.app.service.Impl.StockListService_Impl;
import stock.master.app.util.Log;

@Service
public class UpdateService extends BaseService {

	@Autowired
	protected StockListService_Impl stockList_impl;

	@Autowired
	protected CrawlNorwayService_Impl norwayService_impl;
	
	public updateStockListResult UpdateStockList() throws Exception {

		updateStockListResult result = null;

		try {
			result = stockList_impl.updateList();
		} catch (Exception e) {
			throw new Exception(Log.error(e.toString()));
		}

		return result;
	}

	public String initDbBySid (String sid) {
		
		if (!basicInfoRepository.existsById(sid)) {
			return Log.error("stock id not exist. id = " + sid);
		}
		
		// daily
		
		// weekly
		try {
			List<Weekly> list = norwayService_impl.getWeeklyInfo(sid);
			System.out.println("safdas");
		} catch (Exception e) {
			
		}
		
		// monthly
		
		return "initDbBySid done";
	}

	
	
	
	
	public String updateDbBySid (String sid) {
		
		return "updateDbBySid done";
	}
}
