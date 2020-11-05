package stock.master.app.service.Impl;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Service;
import stock.master.app.service.BaseService;


@Service
public class CrawlCmoney extends BaseService {

	final private String appid = "";
	final private String appsecret = "";
	
	final private String token_url = "https://owl.cmoney.com.tw/OwlApi/auth";
	final private String token_param = "appId=" + appid + "&appSecret=" + appsecret;
	//final private String token_header = {'content-type':};
	
	/*
	 * https://owl.cmoney.com.tw/Owl/tutorial/list.do?id=4f44a640131911e9befb000c29e493f4
	 * https://dotblogs.com.tw/grayyin/2016/04/07/100152
	 * 
	 * */
	public void testing () throws Exception {
		
		URL obj = new URL(token_url);
		HttpURLConnection urlConnection = (HttpURLConnection) obj.openConnection();
		urlConnection.setDoOutput(true);
		urlConnection.setRequestMethod("POST");
		urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		urlConnection.setRequestProperty("charset", StandardCharsets.UTF_8.displayName());
		urlConnection.setRequestProperty("Content-Length", Integer.toString(postData.length()));
		urlConnection.setUseCaches(false);
		urlConnection.getOutputStream().write(postData.getBytes(StandardCharsets.UTF_8));
 
		JSONTokener jsonTokener = new JSONTokener(urlConnection.getInputStream());
		return new JSONObject(jsonTokener);
		
	}

}
