package stock.master.app.service.Impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import stock.master.app.entity.Weekly;
import stock.master.app.service.BaseService;
import stock.master.app.util.Log;


@Service
public class CrawlServiceImpl_Norway extends BaseService {

	private final String basic_url = "https://norway.twsthr.info/StockHolders.aspx?stock=";
	private SimpleDateFormat inputDateFormat = new SimpleDateFormat ("yyyy-MM-dd");

	/*
	 * 大戶籌碼
	 * */
	public List<Weekly> getWeeklyInfo (String sid, int fromYear, int fromMonth) {
		Log.debug("===== getWeeklyInfo begin ===== [" + sid + "]");
		String url = basic_url + sid;
		
		List<Weekly> info = new ArrayList<Weekly>();
		
		Log.debug("===== getWeeklyInfo end =====");
		return info;
	}

}
