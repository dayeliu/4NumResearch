package stock.master.app.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import stock.master.app.constant.ConstantKey;
import stock.master.app.entity.BasicInfo;
import stock.master.app.entity.Revenue;
import stock.master.app.util.Log;

@RestController
@RequestMapping(ConstantKey.API_PREFIX + "/getinfo")
public class GetInfoController extends BaseController {

	@GetMapping("/{sid}")
	public ResponseEntity<String> UpdateStockList() throws Exception {
		Log.debug("===== UpdateStockList begin =====");

		int count = 0; 
		try {
			//count = stockList_service.updateList();
		} catch (Exception e) {
			throw new Exception (Log.error(e.toString()));
		}

		String msg = "Total count : " + count;
		Log.debug(msg);

		Log.debug("===== UpdateStockList end =====");
		return new ResponseEntity<>(msg, HttpStatus.OK);
	}
}
