package stock.master.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import stock.master.app.repository.BasicInfoRepository;
import stock.master.app.resource.vo.updateStockListResult;
import stock.master.app.service.Impl.UpdateServiceImpl_StockList;
import stock.master.app.util.Log;

@Service
public class UpdateService extends BaseService {

	@Autowired
	protected UpdateServiceImpl_StockList stockList_impl;

	public updateStockListResult UpdateStockList() throws Exception {

		updateStockListResult result = null;

		try {
			result = stockList_impl.updateList();
		} catch (Exception e) {
			throw new Exception(Log.error(e.toString()));
		}

		return result;
	}

}
