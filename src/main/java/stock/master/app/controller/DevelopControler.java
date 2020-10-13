package stock.master.app.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import stock.master.app.constant.ConstantKey;
import stock.master.app.entity.BasicInfo;
import stock.master.app.entity.Revenue;
import stock.master.app.repository.BasicInfoRepository;
import stock.master.app.repository.RevenueRepository;

@RestController
@RequestMapping(ConstantKey.API_PREFIX + "/develop")
public class DevelopControler extends BaseController {
	@Autowired
	protected BasicInfoRepository basicInfoRepository;
	
	@Autowired
	protected RevenueRepository revenueRepository;
	
	@GetMapping("/function")
	public ResponseEntity<String> function() throws IOException {
		
		
		Connection connect = Jsoup.connect("https://stock.wearn.com/asale.asp?kind=2330");
		
		Document doc = connect.timeout(5000).get();
		
		int count1 = doc.select("tr[class=stockalllistbg2]").size();
		int count2 = doc.select("tr[class=stockalllistbg2]").size();
		
		Elements elements = doc.select("tr[class=stockalllistbg2]").get(0).select("td");
		for (Element ele : elements) {
			System.out.println(ele.text());
		}

		
		//Element element = doc.select("div[id=D1]").get(0);
		//Element t0 = element.select("tr[class=lDS]").get(0);
		//Element t1 = element.select("tr[class=lDS]").get(1);
		
		//System.out.println(t0);
		//System.out.println(t1);
		
		
		
		return new ResponseEntity<>("Done", HttpStatus.OK);
	}
	
	@GetMapping("/function1")
	public ResponseEntity<String> function1() throws Exception {
		
		List<String> stockIds = new ArrayList<String>();
		stockIds.add("2330");
		stockIds.add("3017");
		
		List<Revenue> list = revenueRepository.findByStockIdIn(stockIds);
		System.out.println(list.size());
		
				
		
		
		return new ResponseEntity<>("function1 Done", HttpStatus.OK);
	}
}
