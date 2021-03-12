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

import stock.master.app.entity.Weekly;
import stock.master.app.service.BaseService;
import stock.master.app.util.Log;


@Service
public class CrawlHistockWeb extends BaseService {

	private final int retryTime = 3;
	// url => https://norway.twsthr.info/StockHolders.aspx?stock=2337
	private final String basic_url = "https://histock.tw/stock/large.aspx?no=";
	private SimpleDateFormat inputDateFormat = new SimpleDateFormat ("yyyyMMdd");

	/*
	 * 大戶籌碼
	 * 
	 * input : sid
	 * input : weekCount 取得幾筆資料
	 * output : 返回20筆資料, 從近到遠存放至 Map<String:date(20201008), Weekly>
	 * 		  : [0] : 2020/10/10 -> [1] : 2020/10/09
	 * 
	 * */
	public List<Weekly> getWeeklyInfo (String sid, int count) throws Exception {

		String url = basic_url + sid;

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

		int weekCount = count/2;
		Map<String, Weekly> data = new HashMap<String, Weekly>();
		String[] classList = {"table[class=tb-stock tbChip]"};
		for (String className : classList) {

			Elements mainElements = doc.select(className);
			int elementCount = mainElements.size();
			if (elementCount == 0) {
				Log.error("No data. className = " + className + ", sid = " + sid);
			}

			//for (int i = 0 ; i < elementCount ; i++) {
				//if (i > weekCount) {
				//	break;
				//}

				Elements elements = mainElements.select("tr");
				int countc = elements.size();
				//debugPrint(elements);

				if (elements.get(2).text().equals("資料日期")) {
					continue;
				}
/*
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
				TimeZone gmtTimeZone = TimeZone.getTimeZone("GMT");
				inputDateFormat.setTimeZone(gmtTimeZone);

				Date t = null;
				String dateStr = elements.get(2).text();
				try {
					t = inputDateFormat.parse(dateStr);
				} catch (Exception e) {
					throw new Exception (Log.error("[sid : " + sid + "] dateStr : " + dateStr + ", error : " + e.toString()));
				}
				info.setDate(t);
				
				data.put(dateStr, info);
				
			}*/
		}
		
		List<Weekly> result = MapSortByKey(data);
		/* debug
		for (Weekly tmp : result) {
			System.out.println(tmp.toString());
		}*/
		
		return result;
	}

	private void debugPrint (Elements elements) {
		int ccc = elements.size();
		for (int j = 0 ; j < ccc ; j++) {
			System.out.println(elements.get(j).text());
		}
	}

	/*
	 * sort from new to old
	 * 
	 * */
	private List<Weekly> MapSortByKey(Map<String, Weekly> list) {
		
        List<Weekly> result = new ArrayList<Weekly>();

        Set set= list.keySet();
        Object[] arr = set.toArray();
        Arrays.sort(arr);

        for(Object key : arr){
            result.add(0, list.get(key));
        }
        
        return result;
	}
}
