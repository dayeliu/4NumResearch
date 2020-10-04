package stock.master.app.service.Impl;

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

import stock.master.app.constant.ConstantKey;
import stock.master.app.entity.BasicInfo;
import stock.master.app.service.BaseService;
import stock.master.app.util.Log;

@Service
public class UpdateServiceImpl_StockList extends BaseService {
	
	/*
	 * source url : https://mops.twse.com.tw/mops/web/t51sb01
	 * 需先用 notepad++ 將csv file 轉成 UTF-8 encoding
	 *
	 * input : 從證交所下載的股票清單檔案
	 * output : StockList.csv
	 * 
	 * */
	public int updateList() throws Exception {

		Map<String, BasicInfo> list = new HashMap<String, BasicInfo>();

		try {
					
			getCompanyList(ConstantKey.otc_list, list);
			getCompanyList(ConstantKey.tse_list, list);
			
			getClassification(ConstantKey.daniel_fine, list);
			getClassification(ConstantKey.daniel_rough, list);
			getClassification(ConstantKey.otc_rough, list);
			getClassification(ConstantKey.tse_rough, list);
			getClassification(ConstantKey.fine_industry, list);
			getClassification(ConstantKey.detail_industry, list);

			basicInfoRepository.saveAll(list.values());
			
			exportStockListFile(ConstantKey.stock_list);

		} catch (Exception e) {
			throw new Exception(Log.error(e.toString()));
		}

		return list.size();
	}
	
	public List<BasicInfo> getAllStockInfo() {
		return basicInfoRepository.findAllByOrderByStockIdAsc();
	}

	public String show(String sid) {
		BasicInfo info = basicInfoRepository.findByStockId(sid);
		if (info == null) {
			Log.error("stock not exist in db " + sid);
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
            
            List<BasicInfo> basicInfoList = basicInfoRepository.findAllByOrderByStockIdAsc();
            for (BasicInfo info : basicInfoList) {
            	String data = showFormat(info);
            	fr.write(data + "\n");
            }

        } catch (IOException e) {
        	throw new Exception(Log.error(e.toString()));
        }finally{
            try {
                fr.close();
            } catch (IOException e) {
            	Log.error(e.toString());
            }
        }

		return true;
	}

	private boolean getClassification(String classificationList, Map<String, BasicInfo> list) throws Exception {
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
						
						if (!list.containsKey(sid)) {
							Log.error("stock not exist in db " + sid);
							continue;
						}

						BasicInfo info = list.get(sid);
						String category = info.getCategory();
						category += "," + name;
						info.setCategory(category);
						
						list.put(sid, info);
					}
				}
			}
		} catch (IOException e) {
			throw new Exception(Log.error(e.toString()));
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				Log.error(e.toString());
			}
		}
		
		return true;
	}
	
	private void getCompanyList(String company, Map<String, BasicInfo> list) throws Exception{

		String property = "";
		if (company.contains("tse")) {
			property = "tse";
		} else if (company.contains("otc")) {
			property = "otc";
		}

		int row_idx = -1;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(company));
			String line = "";
			while((line = reader.readLine()) != null) {
				row_idx++;
				if (row_idx == 0) {
					continue;
				}
				
				line = line.replace("\n", "");
				line = line.replace("\r", "");
				line = line.replace("\t", "");
				line = line.replace(" ", "");

				BasicInfo info = new BasicInfo();

				boolean bInDuration = false;
				String item = "";
				int items_idx = 0;
				
				char[] array = line.toCharArray();
				for (int i = 0; i < array.length; i++) {
					
					if (array[i] == '\"') {
						bInDuration = !bInDuration;
						continue;
					}

					if ((array[i] == ',') && bInDuration == false) {
						//System.out.println("item : " + item + ", items_idx : " + items_idx);

						if (items_idx == 0) {
							info.setStockId(item);
						} else if (items_idx == 2) {
							info.setName(item);
						} else if (items_idx == 3) {
							info.setCategory(item);
						} else if (items_idx == 17) {
							info.setAmount(Long.valueOf(item)/1000);
						}

						item = "";
						items_idx++;
						continue;
					}

					item += array[i];
		        }

				info.setStockClass(property);
				list.put(info.getStockId(), info);
			}
		} catch (IOException e) {
			throw new Exception(Log.error(e.toString()));
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				Log.error(e.toString());
			}
		}
	}
}
