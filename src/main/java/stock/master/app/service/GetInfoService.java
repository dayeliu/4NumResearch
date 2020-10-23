package stock.master.app.service;

import org.springframework.stereotype.Service;

import stock.master.app.resource.vo.getStockInfoResult;
import stock.master.app.util.Log;

@Service
public class GetInfoService extends BaseService {

	public getStockInfoResult GetStockInfo(String sid) throws Exception {

		Log.debug("===== GetStockInfo Sid : " + sid + "=====");

		if (!basicInfoRepository.existsById(sid)) {
			Log.error("stock id not exist. id = " + sid);
			return null;
		}
		
		getStockInfoResult result = new getStockInfoResult();
		result.setBasicInfo(basicInfoRepository.findByStockId(sid));
		result.setWeekly(weeklyRepository.findByStockIdOrderByDateDesc(sid));
		result.setMonthly(monthlyRepository.findByStockIdOrderByDateDesc(sid));

		Log.debug("===== GetStockInfo done =====");

		return result;
	}
}
