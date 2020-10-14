package stock.master.app.service.Impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import stock.master.app.entity.Weekly;
import stock.master.app.service.BaseService;
import stock.master.app.util.Log;


@Service
public class CrawlNorwayService_Impl extends BaseService {

	private final String basic_url = "https://norway.twsthr.info/StockHolders.aspx?stock=";
	private SimpleDateFormat inputDateFormat = new SimpleDateFormat ("yyyyMMdd");

	/*
	 * 大戶籌碼
	 * 
	 * output : 返回20筆資料, 從近到遠存放至 Map<String:date(20201008), Weekly>
	 * 
	 * */
	public List<Weekly> getWeeklyInfo (String sid) throws Exception {
		Log.debug("===== getWeeklyInfo begin ===== [" + sid + "]");
		String url = basic_url + sid;
		final int maxCount = 10;
		
		Map<String, Weekly> result = new HashMap<String, Weekly>();
		try {
			Connection connect = Jsoup.connect(url);
			Document doc = connect.timeout(5000).get();

			//String[] classList = {"tr[class=lDS]", "tr[class=1LS]"};
			String[] classList = {"tr[class=lDS]"};
			for (String className : classList) {

				Elements mainElements = doc.select(className);
				int count = mainElements.size();
				for (int i = 0 ; i < count ; i++) {
					if (i >= maxCount) {
						break;
					}

					Elements elements = mainElements.get(i).select("td");
					debugPrint(elements);

					if (elements.get(2).text().equals("資料日期")) {
						continue;
					}

					String dateStr = elements.get(2).text();
					System.out.println(dateStr);

					Weekly info = new Weekly();
					info.setStockId(sid);
					info.setAverage(Double.valueOf(elements.get(5).text()));
					info.setOver_400_amount(Long.valueOf(elements.get(6).text().replace(",", "")));
					info.setOver_400_percent(Double.valueOf(elements.get(7).text()));
					info.setOver_400_people(Integer.valueOf(elements.get(8).text().replace(",", "")));
					info.setBet_400_600_people(Integer.valueOf(elements.get(9).text().replace(",", "")));
					info.setBet_600_800_people(Integer.valueOf(elements.get(10).text().replace(",", "")));
					info.setBet_800_1000_people(Integer.valueOf(elements.get(11).text().replace(",", "")));
					info.setOver_1000_people(Integer.valueOf(elements.get(12).text().replace(",", "")));
					info.setOver_1000_percent(Double.valueOf(elements.get(13).text().replace(",", "")));

					// date
					Date t = inputDateFormat.parse(dateStr);
					info.setDate(t);
					
					result.put(dateStr, info);
				}
			}

		} catch (Exception e) {
			throw new Exception(Log.error("[" + sid + "] " + e.toString()));
		} finally {
			
		}
		
		Log.debug("===== getWeeklyInfo end =====");
		return MapSortByKey(result);
	}
	
	private void debugPrint (Elements elements) {
		int ccc = elements.size();
		for (int j = 0 ; j < ccc ; j++) {
			System.out.println(elements.get(j).text());
		}
	}
	
	private List<Weekly> MapSortByKey(Map<String, Weekly> list) {
		
		List<Weekly> result = new ArrayList<Weekly>();
		
		Set set= list.keySet();
        Object[] arr = set.toArray();
        Arrays.sort(arr);

        for(Object key : arr){
        	result.add(list.get(key));
        }
        
        return result;
	}
}
