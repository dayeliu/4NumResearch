package stock.master.app.controller;

import org.springframework.beans.factory.annotation.Autowired;

import stock.master.app.service.stockListService;
import stock.master.app.service.strategyService;
import stock.master.app.service.wearnCrawlerService;

public class BaseController {
	@Autowired
	stockListService stockList_service;
	
	@Autowired
	wearnCrawlerService crawl_Service;
	
	@Autowired
	strategyService strategy_service;
}
