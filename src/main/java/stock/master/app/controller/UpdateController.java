package stock.master.app.controller;

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

		updateStockListResult result = updateService.UpdateStockList();

		return new ResponseEntity<updateStockListResult>(result, HttpStatus.OK);
	}

	@GetMapping("/initdb/{sid}")
	public ResponseEntity<String> InitialDbBySid(@PathVariable(value = "sid") String sid) {

		updateService.initDbBySid(sid);

		return new ResponseEntity<String>("InitialDbBySid:" + sid, HttpStatus.OK);
	}
	
	@GetMapping("/InitDb")
	public ResponseEntity<String> InitialDb() {

		updateService.initDb();

		return new ResponseEntity<String>("InitDb", HttpStatus.OK);
	}

	@GetMapping("/updateDb/{sid}")
	public ResponseEntity<String> UpdateDbBySid(@PathVariable(value = "sid") String sid) {

		updateService.updateDbBySid(sid);

		return new ResponseEntity<String>("UpdateDbBySid:" + sid, HttpStatus.OK);
	}
	
	@GetMapping("/updateDb")
	public ResponseEntity<String> UpdateDb() {

		updateService.updateDb();

		return new ResponseEntity<String>("UpdateDb", HttpStatus.OK);
	}

}
