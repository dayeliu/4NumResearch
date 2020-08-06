package stock.master.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import stock.master.app.constant.ConstantKey;
import stock.master.app.util.fileOperation;

@RestController
@RequestMapping(ConstantKey.API_PREFIX + "/download")
public class DownloadController {

	@GetMapping("/getDatabase")
	public ResponseEntity<String> GetDatabase() {
		
		fileOperation.compressDirectory(ConstantKey.conf_dir, ConstantKey.data_dir, "config.zip");
		
		fileOperation.compressFile(ConstantKey.data_dir + "StockList.txt", ConstantKey.data_dir, "StockList.zip");
		
		return new ResponseEntity<>("Done", HttpStatus.OK);
	}
}
