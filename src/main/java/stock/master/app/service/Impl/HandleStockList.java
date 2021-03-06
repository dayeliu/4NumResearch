package stock.master.app.service.Impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import stock.master.app.constant.ConstantKey;
import stock.master.app.entity.BasicInfo;
import stock.master.app.resource.vo.updateStockListResult;
import stock.master.app.service.BaseService;
import stock.master.app.util.Log;

@Service
public class HandleStockList extends BaseService {
	
	@Autowired
	protected ExportToCsv exportToCsvImpl;

	/*
	 * source url : https://mops.twse.com.tw/mops/web/t51sb01
	 * 需先用 notepad++ 將csv file 轉成 UTF-8 encoding
	 *
	 * input : 從證交所下載的股票清單檔案
	 * output : StockList.csv
	 * 
	 * */
	public updateStockListResult updateList() throws Exception {

		Map<String, BasicInfo> list = new HashMap<String, BasicInfo>();
		updateStockListResult ret = null;

		getCompanyList(ConstantKey.otc_list, list);
		getCompanyList(ConstantKey.tse_list, list);
		
		getClassification(ConstantKey.daniel_fine, list);
		getClassification(ConstantKey.daniel_rough, list);
		getClassification(ConstantKey.otc_rough, list);
		getClassification(ConstantKey.tse_rough, list);
		getClassification(ConstantKey.fine_industry, list);
		getClassification(ConstantKey.detail_industry, list);
		getNotes(list);

		ret = getUpdateResult(list);

		basicInfoRepository.deleteAll();
		basicInfoRepository.saveAll(list.values());

		return ret;
	}

	private boolean getNotes(Map<String, BasicInfo> list) throws Exception {
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(ConstantKey.notes));
			String line = "";
			while((line = reader.readLine()) != null) {
				line = line.replace("\n", "");
				line = line.replace("\r", "");
				line = line.replace("\t", "");
				line = line.replace(" ", "");

				if (line.isEmpty()) {
					continue;
				}

				String[] splitStr = line.split(":");
				String sid = splitStr[0];
				String note = splitStr[1];

				if (!list.containsKey(sid)) {
					Log.error("stock not exist in db " + sid);
					continue;
				}

				BasicInfo info = list.get(sid);
				info.setNotes(note);
				list.put(sid, info);
			}
		} catch (IOException e) {
			throw new Exception(Log.error("getNotes : " + e.toString()));
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				Log.error(e.toString());
			}
		}

		return true;
	}

	private updateStockListResult getUpdateResult(Map<String, BasicInfo> list) {
		updateStockListResult result = new updateStockListResult();
		List<BasicInfo> added = new ArrayList<BasicInfo>();
		List<BasicInfo> deleted = new ArrayList<BasicInfo>();
		
		for (String sid : list.keySet()) {
			if (basicInfoRepository.existsById(sid)) {
				basicInfoRepository.deleteById(sid);
			} else {
				added.add(list.get(sid));
			}
		}
		
		deleted = basicInfoRepository.findAll();

		result.setTotalCount(list.size());
		result.setAddedCount(added.size());
		result.setAddStockIds(added);
		result.setDeletedCount(deleted.size());
		result.setDeleteStockIds(deleted);
		return result;
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
			throw new Exception(Log.error("classificationList:" + classificationList +e.toString()));
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
			throw new Exception(Log.error("company:" + company + e.toString()));
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				Log.error(e.toString());
			}
		}
	}
}
