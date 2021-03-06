package stock.master.app.service;

import org.springframework.beans.factory.annotation.Autowired;

import stock.master.app.repository.BasicInfoRepository;
import stock.master.app.repository.DailyRepository;
import stock.master.app.repository.MonthlyRepository;
import stock.master.app.repository.QuarterlyRepository;
import stock.master.app.repository.WeeklyRepository;

public class BaseService {	
	@Autowired
	protected BasicInfoRepository basicInfoRepository;
	
	@Autowired
	protected DailyRepository dailyRepository;

	@Autowired
	protected WeeklyRepository weeklyRepository;
	
	@Autowired
	protected MonthlyRepository monthlyRepository;

	@Autowired
	protected QuarterlyRepository quarterlyRepository;
}
