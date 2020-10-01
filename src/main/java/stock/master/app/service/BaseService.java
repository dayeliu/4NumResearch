package stock.master.app.service;

import org.springframework.beans.factory.annotation.Autowired;

import stock.master.app.repository.BasicInfoRepository;
import stock.master.app.repository.DailyRepository;
import stock.master.app.repository.MonthlyRepository;
import stock.master.app.repository.RevenueRepository;
import stock.master.app.repository.WeeklyRepository;

public class BaseService {	
	@Autowired
	protected BasicInfoRepository basicInfoRepository;
	
	@Autowired
	protected RevenueRepository revenueRepository;
	
	@Autowired
	protected DailyRepository dailyRepository;

	@Autowired
	protected WeeklyRepository weeklyRepository;
	
	@Autowired
	protected MonthlyRepository monthlyRepository;
}
