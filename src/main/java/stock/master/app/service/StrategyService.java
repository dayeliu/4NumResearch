package stock.master.app.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import stock.master.app.entity.BasicInfo;
import stock.master.app.entity.Monthly;
import stock.master.app.service.Impl.WriteFile;
import stock.master.app.util.Log;



@Service
public class StrategyService extends BaseService {
	
	public void strategy_income_increase (int month, Boolean checkMom, Boolean checkYoy) {
		Log.debug("===== strategy_income_increase =====");

		WriteFile writer = new WriteFile("strategy_income_increase.txt");
		List<BasicInfo> stockIds = basicInfoRepository.findAll();

		int count = 0;
		for (BasicInfo stock : stockIds) {

			Monthly fromDb = monthlyRepository.findTop1ByStockIdOrderByDateDesc(stock.getStockId());
			if (fromDb == null) {
				Log.debug("Need to initial monthly db. sid = " + stock.getStockId());
				continue;
			}
			
			if (fromDb.getDate().getMonth() != (month - 1)) {
				Log.debug("No required date. sid = " + stock.getStockId() + " date = " + fromDb.getDate().toString());
				continue;
			}

			Double mom = Double.valueOf(fromDb.getMom().replace("%", "").replace(",", ""));
			Double yoy = Double.valueOf(fromDb.getYoy().replace("%", "").replace(",", ""));
			if (mom > 0 && yoy > 0) {
				String str = stock.toString() + "(" + fromDb.getMom() + " / " + fromDb.getYoy() + ")";
				writer.write(str);
				count++;
			}
		}

		System.out.println("Count : " + count);
		Log.debug("===== strategy_income_increase done =====");
	}

	public boolean strategy_incomeOver12Month (String sid) {
		
		/*List<Revenue> revenueList = revenueRepository.findByStockIdOrderByDateDesc(sid);
		if (revenueList.size() == 0) {
			return false;
		}
		
		int count = 12;
		long latest_income = Long.parseLong(revenueList.get(0).getIncome().replace(",", "")); 
		for (Revenue info : revenueList) {
			if (count == 0) {
				break;
			}

			if (Long.parseLong(info.getIncome().replace(",", "")) > latest_income) {
				return false;
			}
			
			count--;
		}
		*/
		return true;
	}
}
