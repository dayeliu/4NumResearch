package stock.master.app.service.Impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import stock.master.app.entity.Daily;
import stock.master.app.entity.Monthly;
import stock.master.app.entity.Weekly;
import stock.master.app.util.Log;

@Service
public class CrawlWearnWeb {

	private final String basic_url = "https://stock.wearn.com/";
	private SimpleDateFormat inputDateFormat = new SimpleDateFormat ("yyyy-MM-dd");

	/*
	 * 營收
	 * 聚財網上只有一頁，12筆資料
	 * */
	public List<Monthly> getMonthlyInfo (String sid) throws Exception {
		
		String url = basic_url + "asale.asp?kind=" + sid;
		String[] classList = {"tr[class=stockalllistbg1]", "tr[class=stockalllistbg2]"};

		Map<String, Monthly> data = new HashMap<String, Monthly>();
		
		Connection connect = Jsoup.connect(url);
		Document doc = connect.timeout(5000).get();

		for (String className : classList) {

			Elements mainElements = doc.select(className);
			int count = mainElements.size();
			for (int i = 0 ; i < count ; i++) {
				Elements elements = mainElements.get(i).select("td");

				// date
				TimeZone gmtTimeZone = TimeZone.getTimeZone("GMT");
				inputDateFormat.setTimeZone(gmtTimeZone);

				String dateStr = elements.select("td").get(0).text();
				String[] split = dateStr.split("/");
				String newDateStr = (Integer.parseInt(split[0]) + 1911) + "-" + split[1] + "-01";
				Date t = inputDateFormat.parse(newDateStr);

				Monthly info = new Monthly();
				info.setDate(t);
				info.setStockId(sid);
				info.setIncome(elements.select("td").get(1).text().replace(",", ""));
				info.setMom(elements.select("td").get(3).text());
				info.setIncomeLastYear(elements.select("td").get(4).text().replace(",", ""));
				info.setYoy(elements.select("td").get(5).text());
				info.setAccurate(elements.select("td").get(6).text().replace(",", ""));
				info.setAccurateLastYear(elements.select("td").get(7).text().replace(",", ""));
				info.setAcccurate_yoy(elements.select("td").get(8).text());

				data.put(newDateStr, info);
			}
		}
		
		List<Monthly> result = MapSortByKey(data);
		// debug
		/*
		for (Monthly tmp : result) {
			System.out.println(tmp.toString());
		}*/

		return result;
	}
	
	public List<Daily> getDailylyInfo (String sid, String year, String month) throws Exception { 

		Map<String, Daily> data = new HashMap<String, Daily>();
		
		GetCdata(sid, year, month, data);
		GetNetbuy(sid, year, month, data);
		GetAcredit(sid, year, month, data);
		
		
		
		List<Daily> result = MapSortByKeyDaily(data);
		// debug
		/*
		for (Monthly tmp : result) {
			System.out.println(tmp.toString());
		}*/

		return result;
	}
	
	/*
	 * https://stock.wearn.com/cdata.asp?Year=109&month=09&kind=1101
	 * 取得每日股價
	 * 
	 * year : 民國
	 * month : 2位數
	 * 
	 * */
	private void GetCdata(String sid, String year, String month, Map<String, Daily> data) throws Exception {
		String url = basic_url + "cdata.asp?Year=" + year + "&month=" + month + "&kind=" + sid;
		String[] classList = {"tr[class=stockalllistbg1]", "tr[class=stockalllistbg2]"};
		
		Connection connect = Jsoup.connect(url);
		Document doc = connect.timeout(5000).get();

		for (String className : classList) {

			Elements mainElements = doc.select(className);
			int count = mainElements.size();
			for (int i = 0 ; i < count ; i++) {
				Elements elements = mainElements.get(i).select("td");

				// date
				TimeZone gmtTimeZone = TimeZone.getTimeZone("GMT");
				inputDateFormat.setTimeZone(gmtTimeZone);

				String dateStr = elements.select("td").get(0).text();
				String[] split = dateStr.split("/");
				String newDateStr = (Integer.parseInt(split[0]) + 1911) + "-" + split[1] + "-" + split[2];
				Date t = inputDateFormat.parse(newDateStr);

				Daily info = null;
				if (data.containsKey(newDateStr)) {
					info = data.get(newDateStr);
				} else {
					info = new Daily();
					info.setDate(t);
					info.setStockId(sid);
				}

				info.setOpen(Float.parseFloat(elements.select("td").get(1).text()));
				info.setHigh(Float.parseFloat(elements.select("td").get(2).text()));
				info.setLow(Float.parseFloat(elements.select("td").get(3).text()));
				info.setClose(Float.parseFloat(elements.select("td").get(4).text()));
				info.setVolumn(Integer.parseInt(elements.select("td").get(5).text().replace(",", "")));

				data.put(newDateStr, info);
			}
		}
	}
	
	/*
	 * 法人籌碼
	 * */
	private void GetNetbuy(String sid, String year, String month, Map<String, Daily> data) throws Exception {
		
	}
	
	/*
	 * 融資融券
	 * */
	private void GetAcredit(String sid, String year, String month, Map<String, Daily> data) throws Exception {
		
	}

	private List<Monthly> MapSortByKey(Map<String, Monthly> list) {
		
        List<Monthly> result = new ArrayList<Monthly>();

        Set set= list.keySet();
        Object[] arr = set.toArray();
        Arrays.sort(arr);

        for(Object key : arr){
            result.add(0, list.get(key));
        }
        
        return result;
	}
	
	private List<Daily> MapSortByKeyDaily(Map<String, Daily> list) {
		
        List<Daily> result = new ArrayList<Daily>();

        Set set= list.keySet();
        Object[] arr = set.toArray();
        Arrays.sort(arr);

        for(Object key : arr){
            result.add(0, list.get(key));
        }
        
        return result;
	}
}
