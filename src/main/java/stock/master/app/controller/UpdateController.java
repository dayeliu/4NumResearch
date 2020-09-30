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
import stock.master.app.service.logService;

@RestController
@RequestMapping(ConstantKey.API_PREFIX + "/update")
public class UpdateController extends BaseController {

	@GetMapping("/updateStockList")
	public ResponseEntity<String> UpdateStockList() throws Exception {
		logService.debug("===== UpdateStockList begin =====");

		int count = 0; 
		try {
			count = stockList_service.updateList();
		} catch (Exception e) {
			throw new Exception (logService.error(e.toString()));
		}

		String msg = "Total count : " + count;
		logService.debug(msg);

		logService.debug("===== UpdateStockList end =====");
		return new ResponseEntity<>(msg, HttpStatus.OK);
	}
	
	@GetMapping("/InitDb")
	public ResponseEntity<String> InitialDb() throws Exception {
		logService.debug("===== InitialDb begin =====");

		try {
			List<String> stockIds = new ArrayList<String>();
			stockIds.add("2330");
			stockIds.add("3017");
			
			//List<Revenue> list = findByStockIdIn(stockIds);
			
			/*List<BasicInfo> basicInfoList = stockList_service.getAllStockInfo();
			int idx = 0;
			for (BasicInfo info : basicInfoList) {
				logService.debug("[Index : " + idx + "]");

				crawl_Service.updateRevenue(info.getStockId());
				idx++;
			}
			*/	

		} catch (Exception e) {
			throw new Exception(logService.error(e.toString()));
		}

		logService.debug("===== InitialDb end =====");
		
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
