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
import stock.master.app.entity.Quarterly;
import stock.master.app.util.Log;

@Service
public class CrawlWearnWeb {

	private final int retryTime = 3;
	private final String basic_url = "https://stock.wearn.com/";
	private SimpleDateFormat inputDateFormat = new SimpleDateFormat ("yyyy-MM-dd");

	/*
	 * 三率 & eps
	 * 
	 * */
	public List<Quarterly> getQuarterlyInfo (String sid) throws Exception {
		String url = basic_url + "income.asp?kind=" + sid;
System.out.println(url);
		Connection connect = null;
		Document doc = null;

		boolean connectSuccessful = false;
		int retry = 0;
		while (retry < retryTime) {
			try {
				connect = Jsoup.connect(url);
				doc = connect.timeout(5000).get();
				connectSuccessful = true;
				break;
			} catch (Exception e) {
				Log.error("[sid : " + sid + "] error : " + e.toString());
			}
			retry++;
		}

		if (connectSuccessful == false) {
			throw new Exception ("[sid : " + sid + "] connection failed.");
		}

		Map<String, Quarterly> data = new HashMap<String, Quarterly>();
		String[] classList = {"tr[class=stockalllistbg1]", "tr[class=stockalllistbg2]"};
		for (String className : classList) {

			Elements mainElements = doc.select(className);
			int count = mainElements.size();
			for (int i = 0 ; i < count ; i++) {
				Elements elements = mainElements.get(i).select("td");

				// date
				TimeZone gmtTimeZone = TimeZone.getTimeZone("GMT");
				inputDateFormat.setTimeZone(gmtTimeZone);

				Quarterly info = new Quarterly();
				info.setStockId(sid);
				info.setGm(elements.select("td").get(2).text());
				info.setOpr(elements.select("td").get(3).text());
				info.setNpat(elements.select("td").get(5).text());
				info.setEps(elements.select("td").get(6).text());

				Date t = null;
				String dateStr = elements.select("td").get(0).select("a").text();

				int index = dateStr.indexOf("年");
				String newDateStr = (Integer.parseInt(dateStr.substring(0, index)) + 1911) + "-" + dateStr.substring(index + 1, index + 3) + "-01";
				try {
					t = inputDateFormat.parse(newDateStr);
				} catch (Exception e) {
					throw new Exception (Log.error("[sid : " + sid + "] dateStr : " + dateStr + ", error : " + e.toString()));
				}
				info.setDate(t);

				data.put(dateStr, info);
			}
		}

		List<Quarterly> result = MapSortByKeyQuarterly(data);

		return result;
	}

	/*
	 * 營收
	 * 聚財網上只有一頁，12筆資料
	 * */
	public List<Monthly> getMonthlyInfo (String sid) throws Exception {
		
		String url = basic_url + "asale.asp?kind=" + sid;

		Connection connect = null;
		Document doc = null;

		boolean connectSuccessful = false;
		int retry = 0;
		while (retry < retryTime) {
			try {
				connect = Jsoup.connect(url);
				doc = connect.timeout(5000).get();
				connectSuccessful = true;
				break;
			} catch (Exception e) {
				Log.error("[sid : " + sid + "] error : " + e.toString());
			}
			retry++;
		}
		
		if (connectSuccessful == false) {
			throw new Exception ("[sid : " + sid + "] connection failed.");
		}

		Map<String, Monthly> data = new HashMap<String, Monthly>();
		String[] classList = {"tr[class=stockalllistbg1]", "tr[class=stockalllistbg2]"};
		for (String className : classList) {

			Elements mainElements = doc.select(className);
			int count = mainElements.size();
			for (int i = 0 ; i < count ; i++) {
				Elements elements = mainElements.get(i).select("td");

				// date
				TimeZone gmtTimeZone = TimeZone.getTimeZone("GMT");
				inputDateFormat.setTimeZone(gmtTimeZone);

				Monthly info = new Monthly();
				info.setStockId(sid);
				info.setIncome(elements.select("td").get(1).text().replace(",", ""));
				info.setMom(elements.select("td").get(3).text());
				info.setIncomeLastYear(elements.select("td").get(4).text().replace(",", ""));
				info.setYoy(elements.select("td").get(5).text());
				info.setAccurate(elements.select("td").get(6).text().replace(",", ""));
				info.setAccurateLastYear(elements.select("td").get(7).text().replace(",", ""));
				info.setAcccurate_yoy(elements.select("td").get(8).text());

				Date t = null;
				String dateStr = elements.select("td").get(0).text();
				String[] split = dateStr.split("/");
				String newDateStr = (Integer.parseInt(split[0]) + 1911) + "-" + split[1] + "-01";
				try {
					t = inputDateFormat.parse(newDateStr);
				} catch (Exception e) {
					throw new Exception (Log.error("[sid : " + sid + "] dateStr : " + dateStr + ", error : " + e.toString()));
				}
				info.setDate(t);

				data.put(newDateStr, info);
			}
		}
		
		List<Monthly> result = MapSortByKey(data);
		// debug
		/*
		for (Monthly tmp : result) {
			Log.debug(tmp.toString());
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
		/*for (Daily tmp : result) {
			Log.debug(tmp.toString());
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
		
		Connection connect = null;
		Document doc = null;

		boolean connectSuccessful = false;
		int retry = 0;
		while (retry < retryTime) {
			try {
				connect = Jsoup.connect(url);
				doc = connect.timeout(5000).get();
				connectSuccessful = true;
				break;
			} catch (Exception e) {
				Log.error("[sid : " + sid + "] error : " + e.toString());
			}
			retry++;
		}
		
		if (connectSuccessful == false) {
			throw new Exception ("[sid : " + sid + "] connection failed.");
		}

		for (String className : classList) {

			Elements mainElements = doc.select(className);
			int count = mainElements.size();
			for (int i = 0 ; i < count ; i++) {
				Elements elements = mainElements.get(i).select("td");

				// date
				TimeZone gmtTimeZone = TimeZone.getTimeZone("GMT");
				inputDateFormat.setTimeZone(gmtTimeZone);

				Date t = null;
				String dateStr = elements.select("td").get(0).text();
				String[] split = dateStr.split("/");
				String newDateStr = (Integer.parseInt(split[0]) + 1911) + "-" + split[1] + "-" + split[2];
				try {
					t = inputDateFormat.parse(newDateStr);
				} catch (Exception e) {
					throw new Exception (Log.error("[sid : " + sid + "] dateStr : " + dateStr + ", error : " + e.toString()));
				}
				
				Daily info = null;
				if (data.containsKey(newDateStr)) {
					info = data.get(newDateStr);
				} else {
					info = new Daily();
					info.setDate(t);
					info.setStockId(sid);
				}

				info.setOpen(Float.parseFloat(elements.select("td").get(1).text().replace(",", "")));
				info.setHigh(Float.parseFloat(elements.select("td").get(2).text().replace(",", "")));
				info.setLow(Float.parseFloat(elements.select("td").get(3).text().replace(",", "")));
				info.setClose(Float.parseFloat(elements.select("td").get(4).text().replace(",", "")));
				info.setVolumn(Integer.parseInt(elements.select("td").get(5).text().replace(",", "")));

				data.put(newDateStr, info);
			}
		}
	}
	
	/*
	 * https://stock.wearn.com/netbuy.asp?Year=109&month=09&kind=3017
	 * 法人籌碼
	 * 
	 * year : 民國
	 * month : 2位數
	 * */
	private void GetNetbuy(String sid, String year, String month, Map<String, Daily> data) throws Exception {
		
		String url = basic_url + "netbuy.asp?Year=" + year + "&month=" + month + "&kind=" + sid;
		String[] classList = {"tr[class=stockalllistbg1]", "tr[class=stockalllistbg2]"};
		
		Connection connect = null;
		Document doc = null;

		boolean connectSuccessful = false;
		int retry = 0;
		while (retry < retryTime) {
			try {
				connect = Jsoup.connect(url);
				doc = connect.timeout(5000).get();
				connectSuccessful = true;
				break;
			} catch (Exception e) {
				Log.error("[sid : " + sid + "] error : " + e.toString());
			}
			retry++;
		}
		
		if (connectSuccessful == false) {
			throw new Exception ("[sid : " + sid + "] connection failed.");
		}

		for (String className : classList) {

			Elements mainElements = doc.select(className);
			int count = mainElements.size();
			for (int i = 0 ; i < count ; i++) {
				Elements elements = mainElements.get(i).select("td");

				// date
				TimeZone gmtTimeZone = TimeZone.getTimeZone("GMT");
				inputDateFormat.setTimeZone(gmtTimeZone);

				Date t = null;
				String dateStr = elements.select("td").get(0).text();
				String[] split = dateStr.split("/");
				String newDateStr = (Integer.parseInt(split[0]) + 1911) + "-" + split[1] + "-" + split[2];
				try {
					t = inputDateFormat.parse(newDateStr);
				} catch (Exception e) {
					throw new Exception (Log.error("[sid : " + sid + "] dateStr : " + dateStr + ", error : " + e.toString()));
				}
				
				Daily info = null;
				if (data.containsKey(newDateStr)) {
					info = data.get(newDateStr);
				} else {
					info = new Daily();
					info.setDate(t);
					info.setStockId(sid);
				}

				info.setTosin(Integer.parseInt(elements.select("td").get(1).text().replace(",", "")));
				info.setZyin(Integer.parseInt(elements.select("td").get(2).text().replace(",", "")));
				info.setWize(Integer.parseInt(elements.select("td").get(3).text().replace(",", "")));

				data.put(newDateStr, info);
			}
		}
	}
	
	/*
	 * https://stock.wearn.com/acredit.asp?Year=109&month=09&kind=3017
	 * 融資融券
	 * 
	 * year : 民國
	 * month : 2位數
	 * */
	private void GetAcredit(String sid, String year, String month, Map<String, Daily> data) throws Exception {
		
		String url = basic_url + "acredit.asp?Year=" + year + "&month=" + month + "&kind=" + sid;
		String[] classList = {"tr[class=stockalllistbg1]", "tr[class=stockalllistbg2]"};
		
		Connection connect = null;
		Document doc = null;

		boolean connectSuccessful = false;
		int retry = 0;
		while (retry < retryTime) {
			try {
				connect = Jsoup.connect(url);
				doc = connect.timeout(5000).get();
				connectSuccessful = true;
				break;
			} catch (Exception e) {
				Log.error("[sid : " + sid + "] error : " + e.toString());
			}
			retry++;
		}
		
		if (connectSuccessful == false) {
			throw new Exception ("[sid : " + sid + "] connection failed.");
		}

		for (String className : classList) {

			Elements mainElements = doc.select(className);
			int count = mainElements.size();
			for (int i = 0 ; i < count ; i++) {
				Elements elements = mainElements.get(i).select("td");

				// date
				TimeZone gmtTimeZone = TimeZone.getTimeZone("GMT");
				inputDateFormat.setTimeZone(gmtTimeZone);

				Date t = null;
				String dateStr = elements.select("td").get(0).text();
				String[] split = dateStr.split("/");
				String newDateStr = (Integer.parseInt(split[0]) + 1911) + "-" + split[1] + "-" + split[2];
				try {
					t = inputDateFormat.parse(newDateStr);
				} catch (Exception e) {
					throw new Exception (Log.error("[sid : " + sid + "] dateStr : " + dateStr + ", error : " + e.toString()));
				}

				Daily info = null;
				if (data.containsKey(newDateStr)) {
					info = data.get(newDateStr);
				} else {
					info = new Daily();
					info.setDate(t);
					info.setStockId(sid);
				}

				info.setBuy(Integer.parseInt(elements.select("td").get(1).text().replace(",", "")));
				info.setSell(Integer.parseInt(elements.select("td").get(3).text().replace(",", "")));

				data.put(newDateStr, info);
			}
		}
	}

	public void CalculateAverage(List<Daily> list) {
		int count = list.size();
		
		List<Float> ma_5 = new ArrayList<Float>();
		List<Float> ma_10 = new ArrayList<Float>();
		List<Float> ma_20 = new ArrayList<Float>();
		List<Float> ma_60 = new ArrayList<Float>();
		for (int i = count - 1 ; i >= 0 ; i--) {
			Daily daily = list.get(i);

			Float accurate = 0f;
			accurate = CalculateAverageImpl(list.get(i).getClose(), ma_5, 5);
			if (daily.getMa_5() == 0) {
				accurate = (float)Math.round(accurate*100)/100;
				daily.setMa_5(accurate);
			}
			accurate = CalculateAverageImpl(list.get(i).getClose(), ma_10, 10);
			if (daily.getMa_10() == 0) {
				accurate = (float)Math.round(accurate*100)/100;
				daily.setMa_10(accurate);
			}
			accurate = CalculateAverageImpl(list.get(i).getClose(), ma_20, 20);
			if (daily.getMa_20() == 0) {
				accurate = (float)Math.round(accurate*100)/100;
				daily.setMa_20(accurate);
			}
			accurate = CalculateAverageImpl(list.get(i).getClose(), ma_60, 60);
			if (daily.getMa_60() == 0) {
				accurate = (float)Math.round(accurate*100)/100;
				daily.setMa_60(accurate);
			}
			
			list.set(i, daily);
		}
	}
	
	private Float CalculateAverageImpl (Float close, List<Float> accuList, int maCount) {
		accuList.add(close);
		if (accuList.size() > maCount) {
			accuList.remove(0);
		}
		
		Float accurate = 0f;
		for (Float tmp : accuList) {
			accurate += tmp;
		}
		
		return accurate / maCount;
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

private List<Quarterly> MapSortByKeyQuarterly(Map<String, Quarterly> list) {

        List<Quarterly> result = new ArrayList<Quarterly>();

        Set set= list.keySet();
        Object[] arr = set.toArray();
        Arrays.sort(arr);

        for(Object key : arr){
            result.add(0, list.get(key));
        }

        return result;
	}
}
