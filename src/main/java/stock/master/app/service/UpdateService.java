package stock.master.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import stock.master.app.repository.BasicInfoRepository;
import stock.master.app.service.Impl.UpdateServiceImpl_StockList;
import stock.master.app.util.Log;

@Service
public class UpdateService extends BaseService {

	@Autowired
	protected UpdateServiceImpl_StockList stockList;

	public int UpdateStockList() {
		Log.debug("===== UpdateStockList begin =====");
		
		int count = 0;
		try {
			count = stockList.updateList();
		} catch (Exception e) {
			Log.error("Exception: " + e.toString());
		}
		
		Log.debug("===== UpdateStockList end ===== Count : " + count);
		return count;
	}

}
