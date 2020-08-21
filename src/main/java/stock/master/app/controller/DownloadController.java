package stock.master.app.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import stock.master.app.constant.ConstantKey;
import stock.master.app.service.logService;
import stock.master.app.util.fileOperation;

@RestController
@RequestMapping(ConstantKey.API_PREFIX + "/download")
public class DownloadController {

	@GetMapping("/getDatabase")
	public ResponseEntity<String> GetDatabase() throws Exception {
		
		//fileOperation.compressDirectory(ConstantKey.conf_dir, ConstantKey.data_dir, "config.zip");
		
		try {
			fileOperation.compressFile(ConstantKey.data_dir + "text.txt", ConstantKey.data_dir, "text.zip");
		} catch (Exception e) {
			throw new Exception(logService.error("Exception: " + e.toString()));
		}
		
		return new ResponseEntity<>("Done", HttpStatus.OK);
	}
}
