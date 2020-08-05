package stock.master.app.constant;

public class ConstantKey {
	// directory
	public static final String data_dir = "data//";
	public static final String conf_dir = data_dir + "config//";
	public static final String template_dir = conf_dir + "template//";
	
	// stock list
	public static final String otc_list = template_dir + "otc_list.csv";
	public static final String tse_list = template_dir + "tse_list.csv";
	public static final String daniel_fine = conf_dir + "DANIEL_FINE.txt";
	public static final String daniel_rough = conf_dir + "DANIEL_ROUGH.txt";
	public static final String otc_rough = conf_dir + "OTC_ROUGH_INDUSTRY.txt";
	public static final String tse_rough = conf_dir + "TSE_ROUGH_INDUSTRY.txt";
	public static final String fine_industry = conf_dir + "FINE_INDUSTRY.txt";
	public static final String detail_industry = conf_dir + "DETAIL_INDUSTRY.txt";
	public static final String stock_list = conf_dir + "StockList.csv";

	
	// 
	public static final String API_PREFIX = "/stock/v1";
}
