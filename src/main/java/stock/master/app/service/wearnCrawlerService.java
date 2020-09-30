package stock.master.app.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
	private SimpleDateFormat inputDateFormat = new SimpleDateFormat ("yyyy-MM-dd");
	
	public void InitDb() {
		
	}
	
	public void UpdateDb() {
		
	}
	
	/*
	 * 營收
	 * */
	public void updateRevenue(String sid) throws Exception {
		logService.debug("===== updateRevenue begin ===== [" + sid + "]");
		
		String url = basic_url + "asale.asp?kind=" + sid;
		String[] classList = {"tr[class=stockalllistbg1]", "tr[class=stockalllistbg2]"};
		
		try {
			Connection connect = Jsoup.connect(url);
			Document doc = connect.timeout(5000).get();

			boolean bImportAllToDb = false;
			// get all from db
			List<Revenue> revenueList = revenueRepository.findByStockIdOrderByDateDesc(sid);
			if (revenueList.size() == 0) {  // initial db
				bImportAllToDb = true;
			}
			
			for (String className : classList) {

				Elements mainElements = doc.select(className);
				int count = mainElements.size();
				for (int i = 0 ; i < count ; i++) {
					Elements elements = mainElements.get(i).select("td");

					// date
					String str = "";
					str = elements.select("td").get(0).text();
					String[] split = str.split("/");
					String newDateStr = (Integer.parseInt(split[0]) + 1911) + "-" + split[1] + "-01";
					Date t = inputDateFormat.parse(newDateStr);
					
					// initial db or last date newer than latest date in db
					if (bImportAllToDb || t.after(revenueList.get(0).getDate())) {
						Revenue info = new Revenue();

						info.setDate(t);
						info.setStockId(sid);
						info.setIncome(elements.select("td").get(1).text());
						info.setMom(elements.select("td").get(3).text());
						info.setIncomeLastYear(elements.select("td").get(4).text());
						info.setYoy(elements.select("td").get(5).text());
						info.setAccurate(elements.select("td").get(6).text());
						info.setAccurateLastYear(elements.select("td").get(7).text());
						info.setAcccurate_yoy(elements.select("td").get(8).text());
						
						revenueRepository.save(info);
					}
				}
			}
			
			logService.debug("===== updateRevenue end =====");
		} catch (Exception e) {
			throw new Exception(logService.error("[" + sid + "] " + e.toString()));
		} finally {
			
		}

		return;
	}
}
