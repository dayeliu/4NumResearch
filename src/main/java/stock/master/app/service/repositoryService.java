package stock.master.app.service;

import org.springframework.beans.factory.annotation.Autowired;

import stock.master.app.repository.BasicInfoRepository;
import stock.master.app.repository.RevenueRepository;

public class repositoryService {
	@Autowired
	protected BasicInfoRepository basicInfoRepository;
	
	@Autowired
	protected RevenueRepository revenueRepository;
}
