package stock.master.app.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import stock.master.app.entity.Revenue;

@Service
public class wearnCrawlerService extends repositoryService {
	
	private final String basic_url = "https://stock.wearn.com/";
	private SimpleDateFormat inputDateFormat = new SimpleDateFormat ("yyyy/MM");
	
	public void updateRevenue(String sid) throws Exception {
		String url = basic_url + "asale.asp?kind=" + sid;

		
		try {
			Connection connect = Jsoup.connect(url);
			Document doc = connect.timeout(5000).get();
			
			Revenue info = new Revenue();
			Elements elements = doc.select("tr[class=stockalllistbg2]").get(0).select("td");
			String str = "";

			// date
			str = elements.select("td").get(0).text();
			String[] split = str.split("/");
			String newDateStr = (Integer.parseInt(split[0]) + 1911) + "/" + split[1];
			Date t = inputDateFormat.parse(newDateStr);
			info.setDate(t);
			
			revenueRepository.save(info);
			
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new Exception(e.toString());
		} finally {
			
		}
		
		//int count1 = doc.select("tr[class=stockalllistbg2]").size();
		//int count2 = doc.select("tr[class=stockalllistbg2]").size();
		
		//Elements elements = doc.select("tr[class=stockalllistbg2]").get(0).select("td");
		//for (Element ele : elements) {
		//	System.out.println(ele.text());
		//}
		
		return;
	}
}
