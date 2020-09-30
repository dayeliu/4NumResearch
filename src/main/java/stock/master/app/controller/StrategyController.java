package stock.master.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import stock.master.app.service.logService;

public class StrategyController extends BaseController {
	@GetMapping("/strategy")
	public ResponseEntity<String> strategy_over_12_month() throws Exception {
		logService.debug("===== strategy_over_12_month begin =====");

		Integer count = 0; 
		try {
			count = stockList_service.updateList();
		} catch (Exception e) {
			throw new Exception (e.toString());
		}

		String msg = "Total count : " + count.toString();
		logService.debug(msg);

		logService.debug("===== strategy_over_12_month end =====");
		return new ResponseEntity<>(msg, HttpStatus.OK);
	}
}
