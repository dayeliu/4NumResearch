package stock.master.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import stock.master.app.constant.ConstantKey;
import stock.master.app.service.stockListService;

@RestController
@RequestMapping(ConstantKey.API_PREFIX + "/update")
public class UpdateController {

	@GetMapping("/UpdateStockList")
	public ResponseEntity<String> UpdateStockList()
	{
		boolean ret = stockListService.updateList();
		
		String result = "";
		if (ret == true) {
			result = "Success";
		} else {
			result = "Fail";
		}
			
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@GetMapping("/InitDb")
	public ResponseEntity<String> InitialDb() {
		return new ResponseEntity<>("", HttpStatus.OK); 
	}
	
	public ResponseEntity<String> InitialDbBySid() {
		return new ResponseEntity<>("", HttpStatus.OK); 
	}
	
	public ResponseEntity<String> UpdateDb() {
		return new ResponseEntity<>("", HttpStatus.OK); 
	}
	
	public ResponseEntity<String> UpdateDbBySid() {
		return new ResponseEntity<>("", HttpStatus.OK); 
	}
}
