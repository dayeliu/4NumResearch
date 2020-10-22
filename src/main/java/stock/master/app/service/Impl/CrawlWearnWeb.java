package stock.master.app.service.Impl;

import java.text.SimpleDateFormat;
import java.util.List;

import stock.master.app.entity.Monthly;

public class CrawlWearnWeb {

	private final String basic_url = "https://stock.wearn.com/";
	private SimpleDateFormat inputDateFormat = new SimpleDateFormat ("yyyyMMdd");

	/*
	 * 
	 * */
	private final String asale_url = basic_url + "asale.asp?kind=";
	public List<Monthly> getMonthlyInfo (String sid, int count) throws Exception {
		
	}

}
