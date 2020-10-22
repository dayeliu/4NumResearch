package stock.master.app.service.Impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.stereotype.Service;

import stock.master.app.constant.ConstantKey;
import stock.master.app.entity.BasicInfo;
import stock.master.app.entity.Weekly;
import stock.master.app.service.BaseService;
import stock.master.app.util.Log;
import stock.master.app.util.fileOperation;

@Service
public class ExportToCsv extends BaseService {

	public boolean exportWeekly(String sid) throws Exception {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		final String fileName = sid + "_" + "weekly.csv";
		final String filePath = ConstantKey.export_dir + sid + "//" + fileName;
		
		if (!fileOperation.checkExist(filePath)) {
			fileOperation.copyFile(ConstantKey.export_template_dir + "weekly.csv", filePath);
		}
		
		File file = new File(filePath);
		FileWriter fr = new FileWriter(file, true);
		BufferedWriter br = new BufferedWriter(fr);

		List<Weekly> list = weeklyRepository.findByStockIdOrderByDateDesc(sid);
		for (Weekly info : list) {
			String strDate = formatter.format(info.getDate());
			String str = strDate + "," + info.getAverage() + "," + info.getOver_400_percent() + "," + info.getOver_1000_percent() + ",";
			br.write(str + "\n");
			br.flush();
		}

		br.close();
		fr.close();
		
		return true;
	}

	public boolean exportMonthly(String sid) throws Exception {
		
		return true;
	}

	public boolean exportStockList() throws Exception {
		
		File file = new File(ConstantKey.stock_list);
		FileWriter fr = null;
		try {
            fr = new FileWriter(file);
            
            List<BasicInfo> basicInfoList = basicInfoRepository.findAllByOrderByStockIdAsc();
            for (BasicInfo info : basicInfoList) {
            	String data = info.toString();
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
}
