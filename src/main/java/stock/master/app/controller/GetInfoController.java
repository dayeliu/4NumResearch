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
import stock.master.app.entity.BasicInfo;
import stock.master.app.resource.vo.getStockInfoResult;
import stock.master.app.util.Log;

@RestController
@RequestMapping(ConstantKey.API_PREFIX + "/getinfo")
public class GetInfoController extends BaseController {

	@GetMapping("stock/{sid}")
	public ResponseEntity<getStockInfoResult> getStockInfo(@PathVariable(value = "sid") String sid) throws Exception {

		getStockInfoResult result = getInfoService.GetStockInfo(sid);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}
}
