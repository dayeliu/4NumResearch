package stock.master.app.controller;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import stock.master.app.constant.ConstantKey;
import stock.master.app.service.logService;

@RestController
@RequestMapping(ConstantKey.API_PREFIX + "/develop")
public class DevelopControler {
	
	@GetMapping("/function")
	public ResponseEntity<String> function() throws IOException {
		
		
		Connection connect = Jsoup.connect("https://norway.twsthr.info/StockHolders.aspx?stock=2448");
		
		Document doc = connect.timeout(5000).get();
		
		Element element = doc.select("div[id=D1]").get(0);
		Element t0 = element.select("tr[class=lDS]").get(0);
		Element t1 = element.select("tr[class=lDS]").get(1);
		
		System.out.println(t0);
		System.out.println(t1);
		
		
		
		return new ResponseEntity<>("Done", HttpStatus.OK);
	}
}
