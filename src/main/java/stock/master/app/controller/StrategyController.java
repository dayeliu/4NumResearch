package stock.master.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import stock.master.app.constant.ConstantKey;
import stock.master.app.util.Log;

@RestController
@RequestMapping(ConstantKey.API_PREFIX + "/strategy")
public class StrategyController extends BaseController {
	
	@GetMapping("/strategy1")
	public ResponseEntity<String> strategy_over_12_month() throws Exception {
		Log.debug("===== strategy_over_12_month begin =====");

		Integer count = 0; 
		try {
			//count = stockList_service.updateList();
		} catch (Exception e) {
			throw new Exception (e.toString());
		}

		String msg = "Total count : " + count.toString();
		Log.debug(msg);

		Log.debug("===== strategy_over_12_month end =====");
		return new ResponseEntity<>(msg, HttpStatus.OK);
	}
}
