package stock.master.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	stockListService service;

	@GetMapping("/updateStockList")
	public ResponseEntity<String> UpdateStockList() throws Exception
	{
		Integer count = 0; 
		try {
			count = service.updateList();
		} catch (Exception e) {
			throw new Exception (e.toString());
		}

		return new ResponseEntity<>("Total count : " + count.toString(), HttpStatus.OK);
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
