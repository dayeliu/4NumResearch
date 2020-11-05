package stock.master.app.service.Impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Service;
import stock.master.app.service.BaseService;


@Service
public class CrawlCmoney extends BaseService {

	final private String appid = "20201027171244144";
	final private String appsecret = "92938700183411ebba92000c29beef84";
	
	final private String token_url = "https://owl.cmoney.com.tw/OwlApi/auth";
	final private String token_param = "appId=" + appid + "&appSecret=" + appsecret;
	
	/*
	 * https://owl.cmoney.com.tw/Owl/tutorial/list.do?id=4f44a640131911e9befb000c29e493f4
	 * https://dotblogs.com.tw/grayyin/2016/04/07/100152
	 * 
	 * */
	public void testing () throws Exception {
		
		URL obj = new URL(token_url);
		HttpURLConnection urlConnection = (HttpURLConnection) obj.openConnection();
		urlConnection.setDoOutput(true);
		urlConnection.setDoInput(true);
		urlConnection.setRequestMethod("POST");
		urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

		OutputStream os = urlConnection.getOutputStream();
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
		writer.write(token_param);
		writer.flush();
		writer.close();
		os.close();

		urlConnection.connect();

		BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),"UTF-8"));

		StringBuffer sb = new StringBuffer("");
		String lines="";
		while ((lines = reader.readLine()) != null) {
			lines = new String(lines.getBytes(), "utf-8");
			sb.append( lines);
		}
		System.out.println(sb);
		reader.close();
		urlConnection.disconnect();
		
		
	}

}
