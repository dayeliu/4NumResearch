package stock.master.app.service;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import stock.master.app.constant.ConstantKey;
import stock.master.app.resource.basicInfo;

public class stockListService {
	
	/*
	 * source url : https://mops.twse.com.tw/mops/web/t51sb01
	 * 需先用 notepad++ 將csv file 轉成 UTF-8 encoding
	 *
	 * input : 從證交所下載的股票清單檔案
	 * output : StockList.csv
	 * 
	 * */
	public static boolean updateList() {
		
		Map<String, basicInfo> stockList = new HashMap<String, basicInfo>();
		
		getCompanyList(ConstantKey.otc_list, stockList);
		getCompanyList(ConstantKey.tse_list, stockList);
		
		getClassification(ConstantKey.daniel_fine, stockList);
		getClassification(ConstantKey.daniel_rough, stockList);
		getClassification(ConstantKey.otc_rough, stockList);
		getClassification(ConstantKey.tse_rough, stockList);
		getClassification(ConstantKey.fine_industry, stockList);
		getClassification(ConstantKey.detail_industry, stockList);
		
		exportStockListFile(stockList);
		
		return true;
	}
	
	public static String show(String sid) {
		
		Map<String, basicInfo> lists = new HashMap<String, basicInfo>();
		importStockListFile(lists);
		
		String str = lists.get(sid).toString();
		
		System.out.println(str);
		return str;
	}
	
	private static boolean exportStockListFile(Map<String, basicInfo> lists) {
		
		CSVWriter writer = null;
		try {
			writer = new CSVWriter(new FileWriter(ConstantKey.stock_list), ','); 
			
			for (basicInfo info : lists.values()) {
				
				String [] line = { info.getSid(), info.getName(), info.getProperty(), info.getClassification(), info.getAmount().toString()};
				writer.writeNext(line);
			}
			
		} catch (IOException e) {
			System.out.println(e);
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				System.out.println(e);
			}
		}
		
		return true;
	}
	
	private static boolean hasImportStockListFile = false;
	private static boolean importStockListFile(Map<String, basicInfo> lists) {
		
		
		CSVReader reader = null; 
		DataInputStream in = null;
		
		String property = "";
		
		try {
			String [] nextLine; 
			
			in = new DataInputStream(new FileInputStream(ConstantKey.stock_list));
			reader = new CSVReader(new InputStreamReader(in, "UTF-8"));
			while ((nextLine = reader.readNext()) != null) {
				basicInfo info = new basicInfo();
				info.setSid(nextLine[0]);
				info.setName(nextLine[1]);
				info.setClassification(nextLine[3]);
				info.setAmount((Long.valueOf(nextLine[4])));
				info.setProperty(nextLine[2]);
				
				lists.put(nextLine[0], info);
			}
			
		} catch (IOException e) {
			System.out.println(e);
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				System.out.println(e);
			}
			
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				System.out.println(e);
			}
		}
		
		hasImportStockListFile = true;
		return true;
	}
	
	private static boolean getClassification(String classificationList, Map<String, basicInfo> lists) {
		
		BufferedReader reader = null;
		
		try {
			reader = new BufferedReader(new FileReader(classificationList));
			String line = "";
			String name = "";
			while((line = reader.readLine()) != null) {
				line = line.replace("\n", "");
				line = line.replace("\r", "");
				line = line.replace("\t", "");
				line = line.replace("skip", "");
				line = line.replace(" ", "");
				
				if (line.contains("[") && line.contains("]")) {
					line = line.replace("[", "");
					line = line.replace("]", "");
					name = line;
				} else if (line.isEmpty() == false) {
					String[] sids = line.split(",");
					
					for (String sid : sids) {
						if (lists.containsKey(sid) == false) {
							System.out.println("File : " + classificationList + " Stock id : " + sid + " not exist in StockList.csv");
							continue;
						}
						
						basicInfo info = lists.get(sid);
						String classification = info.getClassification();
						classification += "," + name;
						info.setClassification(classification);
						lists.put(sid, info);
					}
				}
			}
		} catch (IOException e) {
			System.out.println(e);
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				System.out.println(e);
			}
		}
		
		return true;
	}
	
	private static boolean getCompanyList(String company, Map<String, basicInfo> lists) {
		
		CSVReader reader = null; 
		DataInputStream in = null;
		
		String property = "";
		if (company.contains("tse")) {
			property = "tse";
		} else if (company.contains("otc")) {
			property = "otc";
		}
		
		try {
			String [] nextLine; 
			int idx = -1;
			
			in = new DataInputStream(new FileInputStream(company));
			reader = new CSVReader(new InputStreamReader(in, "UTF-8"));
			while ((nextLine = reader.readNext()) != null) {
				idx++;
				if (idx == 0) {
					continue;
				}
				
				basicInfo info = new basicInfo();
				info.setSid(nextLine[0]);
				info.setName(nextLine[2]);
				info.setClassification(nextLine[3]);
				info.setAmount((Long.valueOf(nextLine[17])/1000));
				info.setProperty(property);
				
				lists.put(nextLine[0], info);
			}
			
		} catch (IOException e) {
			System.out.println(e);
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				System.out.println(e);
			}
			
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				System.out.println(e);
			}
		}
		
		return true;
	} 
}
