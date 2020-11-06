package stock.master.app.service.Impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.stereotype.Service;
import stock.master.app.service.BaseService;
import com.google.gson.Gson;

@Service
public class CrawlCmoney extends BaseService {

	/*
	 * https://owl.cmoney.com.tw/Owl/tutorial/list.do?id=4f44a640131911e9befb000c29e493f4
	 * https://dotblogs.com.tw/grayyin/2016/04/07/100152
	 * 
	 * */
	public String getToken () throws Exception {
		String token_url = "https://owl.cmoney.com.tw/OwlApi/auth";
		URL obj = new URL(token_url);
		HttpURLConnection urlConnection = (HttpURLConnection) obj.openConnection();
		urlConnection.setDoOutput(true);
		urlConnection.setDoInput(true);
		urlConnection.setRequestMethod("POST");
		urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

		String appid = "20201027171244144";		// from cmoney
		String appsecret = "92938700183411ebba92000c29beef84";	// from comney
		String token_param = "appId=" + appid + "&appSecret=" + appsecret;
		OutputStream os = urlConnection.getOutputStream(); // send data
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
		
		String tok = sb.toString();
		Gson gson = new Gson();
		auth object = gson.fromJson(tok, auth.class);
		
		return object.token;
	}

	/*
	 * 
	 * */
	private void getData(String tok) throws Exception {
		String data_url = "https://owl.cmoney.com.tw/OwlApi/api/v2/json/PYPRI-14847a/2330/40";
		URL obj = new URL(data_url);
		HttpURLConnection urlConnection = (HttpURLConnection) obj.openConnection();
		urlConnection.setDoOutput(true);
		urlConnection.setDoInput(true);
		urlConnection.setRequestMethod("GET");
		urlConnection.setRequestProperty("authorization", "Bearer " + tok); // header
		urlConnection.setRequestProperty("Content-Type", "application/json");
		urlConnection.connect();

		BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),"UTF-8"));

		StringBuffer sb = new StringBuffer("");
		String lines="";
		while ((lines = reader.readLine()) != null) {
			lines = new String(lines.getBytes(), "utf-8");
			//sb.append( lines);
			System.out.println(lines);
		}
		//System.out.println(sb);
		reader.close();
		urlConnection.disconnect();
	}
}

class auth {
	String token;
}
