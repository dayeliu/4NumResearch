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
import stock.master.app.resource.vo.updateStockListResult;
import stock.master.app.util.Log;

@RestController
@RequestMapping(ConstantKey.API_PREFIX + "/update")
public class UpdateController extends BaseController {

	@GetMapping("/updateStockList")
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
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@GetMapping("/InitDb")
	public ResponseEntity<String> InitialDb() throws Exception {
		Log.debug("===== InitialDb begin =====");

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
			throw new Exception(Log.error(e.toString()));
		}

		Log.debug("===== InitialDb end =====");
		
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
