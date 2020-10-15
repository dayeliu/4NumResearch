package stock.master.app.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import stock.master.app.constant.ConstantKey;
import stock.master.app.resource.vo.updateStockListResult;
import stock.master.app.util.Log;

@RestController
@RequestMapping(ConstantKey.API_PREFIX + "/update")
public class UpdateController extends BaseController {

	@GetMapping("/stockList")
	public ResponseEntity<updateStockListResult> UpdateStockList() throws Exception {
		Log.debug("===== UpdateStockList begin =====");

		updateStockListResult result = null; 
		try {
			result = updateService.UpdateStockList();
		} catch (Exception e) {
			throw new Exception (Log.error(e.toString()));
		}


		Log.debug(result.toString());

		Log.debug("===== UpdateStockList end =====");
		return new ResponseEntity<updateStockListResult>(result, HttpStatus.OK);
	}

	@GetMapping("/initdb/{sid}")
	public ResponseEntity<String> InitialDbBySid(@PathVariable(value = "sid") String sid) throws Exception {
		Log.debug("===== InitialDbBySid begin ===== sid = " + sid);

		String ret = updateService.initDbBySid(sid);
		
		Log.debug("===== InitialDbBySid end =====");
		return new ResponseEntity<String>(ret, HttpStatus.OK);
	}
	
	@GetMapping("/InitDb")
	public ResponseEntity<String> InitialDb(){
		Log.debug("===== InitialDb begin =====");

		List<String> stockIds = new ArrayList<String>();
		stockIds.add("2330");
		stockIds.add("3017");

		int idx = 0;
		for (String sid : stockIds) {
			Log.debug("[Index : " + idx + "] " + sid);

			try {
				String ret = updateService.initDbBySid(sid);
			} catch (Exception e) {
				Log.error(e.toString());
			}

			idx++;
		}

		Log.debug("===== InitialDb end =====");

		return new ResponseEntity<String>("done", HttpStatus.OK);
	}

	@GetMapping("/updateDb/{sid}")
	public ResponseEntity<String> UpdateDbBySid(@PathVariable(value = "sid") String sid) {
		Log.debug("===== UpdateDbBySid begin ===== sid = " + sid);

		try {
			String ret = updateService.updateDbBySid(sid);
		} catch (Exception e) {
			Log.error(e.toString());
		}

		Log.debug("===== UpdateDbBySid end =====");
		return new ResponseEntity<String>("done", HttpStatus.OK);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public ResponseEntity<String> UpdateDb() {
		return new ResponseEntity<>("", HttpStatus.OK); 
	}
	
	
}
