package stock.master.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import stock.master.app.entity.Revenue;

@Service
public class strategyService extends repositoryService {
	
	public boolean strategy_incomeOver12Month (String sid) {
		
		List<Revenue> revenueList = revenueRepository.findByStockIdOrderByDateDesc(sid);
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
		
		return true;
	}
}
