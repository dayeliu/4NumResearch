package stock.master.app.service;

import org.springframework.beans.factory.annotation.Autowired;

import stock.master.app.repository.BasicInfoRepository;

public class repositoryService {
	@Autowired
	protected BasicInfoRepository basicInfoRepository;
}
