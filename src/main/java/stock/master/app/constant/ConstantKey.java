package stock.master.app.constant;

public class ConstantKey {
	// directory
	public static final String data_dir = "data//";
	public static final String conf_dir = data_dir + "config//";
	public static final String template_dir = conf_dir + "template//";
	public static final String industry_dir = conf_dir + "industry//";
	
	// stock list
	public static final String otc_list = template_dir + "otc_list.csv";
	public static final String tse_list = template_dir + "tse_list.csv";
	public static final String daniel_fine = industry_dir + "DANIEL_FINE.txt";
	public static final String daniel_rough = industry_dir + "DANIEL_ROUGH.txt";
	public static final String otc_rough = industry_dir + "OTC_ROUGH_INDUSTRY.txt";
	public static final String tse_rough = industry_dir + "TSE_ROUGH_INDUSTRY.txt";
	public static final String fine_industry = industry_dir + "FINE_INDUSTRY.txt";
	public static final String detail_industry = industry_dir + "DETAIL_INDUSTRY.txt";
	public static final String stock_list = conf_dir + "StockList.csv";

	// common
	public static final int retryTime = 3;
	
	
	// system setting
	public static final String API_PREFIX = "/stock/v1";
}
