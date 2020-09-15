package stock.master.app.service;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import au.com.bytecode.opencsv.CSVReader;
import stock.master.app.constant.ConstantKey;
import stock.master.app.entity.BasicInfo;

@Service
public class stockListService extends repositoryService {
	
	/*
	 * source url : https://mops.twse.com.tw/mops/web/t51sb01
	 * 需先用 notepad++ 將csv file 轉成 UTF-8 encoding
	 *
	 * input : 從證交所下載的股票清單檔案
	 * output : StockList.csv
	 * 
	 * */
	public int updateList() throws Exception {
		int count = 0;
		try {
			count += getCompanyList(ConstantKey.otc_list);
			count += getCompanyList(ConstantKey.tse_list);
			
			getClassification(ConstantKey.daniel_fine);
			getClassification(ConstantKey.daniel_rough);
			getClassification(ConstantKey.otc_rough);
			getClassification(ConstantKey.tse_rough);
			getClassification(ConstantKey.fine_industry);
			getClassification(ConstantKey.detail_industry);
			
			exportStockListFile(ConstantKey.stock_list);

		} catch (Exception e) {
			throw new Exception(logService.error(e.toString()));
		}

		return count;
	}
	
	public String show(String sid) {
		BasicInfo info = basicInfoRepository.findByStockId(sid);
		if (info == null) {
			logService.error("stock not exist in db " + sid);
			return "";
		}

		return showFormat(info);
	}
	
	private String showFormat(BasicInfo info) {
		if (info == null) {
			return "";
		}

		// EX : 2330 (台積電, 111/25930380) : 半導體*, 半導體指標, TSE-電子, TSE-半導體, APPLE, AMD
		String ret = "[" + info.getStockClass() + "] " + info.getStockId() + 
    			" (" + info.getName() + ", " + (int)(info.getAmount()*0.0015) + "/" + info.getAmount() + ") : " + info.getCategory();

		return ret;
	}

	private boolean exportStockListFile(String path) throws Exception {
		
		File file = new File(path);
		FileWriter fr = null;
		try {
            fr = new FileWriter(file);
            
            List<BasicInfo> basicInfoList = basicInfoRepository.findAll();
            for (BasicInfo info : basicInfoList) {
            	String data = showFormat(info);
            	fr.write(data + "\n");
            }

        } catch (IOException e) {
        	throw new Exception(logService.error(e.toString()));
        }finally{
            try {
                fr.close();
            } catch (IOException e) {
            	logService.error(e.toString());
            }
        }

		return true;
	}

	private boolean getClassification(String classificationList) throws Exception {
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
						BasicInfo info = basicInfoRepository.findByStockId(sid);
						if (info == null) {
							logService.error("stock not exist in db " + sid);
							continue;
						}

						String category = info.getCategory();
						category += "," + name;
						info.setCategory(category);
						basicInfoRepository.save(info);
					}
				}
			}
		} catch (IOException e) {
			throw new Exception(logService.error(e.toString()));
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				logService.error(e.toString());
			}
		}
		
		return true;
	}

	private int getCompanyList(String company) throws Exception{
		CSVReader reader = null; 
		DataInputStream in = null;
		
		String property = "";
		if (company.contains("tse")) {
			property = "tse";
		} else if (company.contains("otc")) {
			property = "otc";
		}
		
		int idx = -1;
		try {
			String [] nextLine; 
			
			in = new DataInputStream(new FileInputStream(company));
			reader = new CSVReader(new InputStreamReader(in, "UTF-8"));
			while ((nextLine = reader.readNext()) != null) {
				idx++;
				if (idx == 0) {
					continue;
				}
				
				BasicInfo info = new BasicInfo();
				info.setStockId(nextLine[0]);
				info.setName(nextLine[2]);
				info.setCategory(nextLine[3]);
				info.setAmount(Long.valueOf(nextLine[17])/1000);
				info.setStockClass(property);

				basicInfoRepository.save(info);
			}
			
		} catch (IOException e) {
			throw new Exception(logService.error(e.toString()));
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				logService.error(e.toString());
			}
			
			try {
				in.close();
			} catch (IOException e) {
				logService.error(e.toString());
			}
		}

		return idx;
	}
}
