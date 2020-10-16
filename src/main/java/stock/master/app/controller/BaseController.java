package stock.master.app.controller;

import org.springframework.beans.factory.annotation.Autowired;

import stock.master.app.service.GetInfoService;
import stock.master.app.service.StrategyService;
import stock.master.app.service.UpdateService;

public class BaseController {
	@Autowired
	UpdateService updateService;
	
	@Autowired
	GetInfoService getInfoService;
	
	@Autowired
	StrategyService strategyService;
}
