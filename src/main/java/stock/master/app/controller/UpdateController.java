package stock.master.app.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import stock.master.app.constant.ConstantKey;
import stock.master.app.resource.vo.updateStockListResult;

@RestController
@RequestMapping(ConstantKey.API_PREFIX + "/update")
public class UpdateController extends BaseController {

	@GetMapping("/stockList")
	public ResponseEntity<updateStockListResult> UpdateStockList() throws Exception {

		updateStockListResult result = updateService.UpdateStockList();

		return new ResponseEntity<updateStockListResult>(result, HttpStatus.OK);
	}

	@GetMapping("/updateDb")
	public ResponseEntity<String> UpdateDb() throws Exception {

		updateService.UpdateDb();

		return new ResponseEntity<String>("", HttpStatus.OK);
	}
}
