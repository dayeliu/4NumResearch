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
import stock.master.app.entity.Daily;
import stock.master.app.entity.Monthly;
import stock.master.app.entity.Quarterly;
import stock.master.app.entity.Weekly;
import stock.master.app.service.BaseService;
import stock.master.app.util.Log;
import stock.master.app.util.fileOperation;

@Service
public class ExportToCsv extends BaseService {

	final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	java.text.DecimalFormat df = new java.text.DecimalFormat("#.##");

	public boolean exportDaily(String sid) throws Exception {

		final String fileName = sid + "_" + "daily.csv";
		final String filePath = ConstantKey.export_dir + sid + "//" + fileName;

		if (fileOperation.checkExist(filePath)) {
			fileOperation.delFile(filePath);
		}
		fileOperation.copyFile(ConstantKey.export_template_dir + "daily.csv", filePath);

		File file = new File(filePath);
		FileWriter fr = new FileWriter(file, true);
		BufferedWriter br = new BufferedWriter(fr);

		List<Daily> list = dailyRepository.findByStockIdOrderByDateDesc(sid);
		for (Daily info : list) {
			String strDate = formatter.format(info.getDate());
			String str = strDate + "," + info.getOpen() + "," + info.getClose() + "," + info.getHigh() + "," + info.getLow()
			+ "," + info.getVolumn() + "," + df.format(info.getMa_5()) + "," + info.getMa_10() + "," + info.getMa_20()
			+ "," + info.getMa_60() + "," + info.getTosin() + "," + info.getWize() + "," + info.getZyin()
			+ "," + info.getBuy() + "," + info.getSell() + ",";
			br.write(str + "\n");
			br.flush();
		}

		br.close();
		fr.close();

		return true;
	}

	public boolean exportWeekly(String sid) throws Exception {
		
		final String fileName = sid + "_" + "weekly.csv";
		final String filePath = ConstantKey.export_dir + sid + "//" + fileName;
		
		if (fileOperation.checkExist(filePath)) {
			fileOperation.delFile(filePath);
		}
		fileOperation.copyFile(ConstantKey.export_template_dir + "weekly.csv", filePath);
		
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
		
		final String fileName = sid + "_" + "monthly.csv";
		final String filePath = ConstantKey.export_dir + sid + "//" + fileName;
		
		if (fileOperation.checkExist(filePath)) {
			fileOperation.delFile(filePath);
		}
		fileOperation.copyFile(ConstantKey.export_template_dir + "monthly.csv", filePath);
		
		File file = new File(filePath);
		FileWriter fr = new FileWriter(file, true);
		BufferedWriter br = new BufferedWriter(fr);

		List<Monthly> list = monthlyRepository.findByStockIdOrderByDateDesc(sid);
		for (Monthly info : list) {
			String strDate = formatter.format(info.getDate());
			String str = strDate + "," + info.getIncome() + "," + info.getMom() + "," + info.getIncomeLastYear() +
					"," + info.getYoy() + "," + info.getAccurate() + "," + info.getAccurateLastYear() + "," + info.getAcccurate_yoy() + ",";
			br.write(str + "\n");
			br.flush();
		}

		br.close();
		fr.close();

		return true;
	}

	public boolean exportQuarterly(String sid) throws Exception {
		
		final String fileName = sid + "_" + "quarterly.csv";
		final String filePath = ConstantKey.export_dir + sid + "//" + fileName;
		
		if (fileOperation.checkExist(filePath)) {
			fileOperation.delFile(filePath);
		}
		fileOperation.copyFile(ConstantKey.export_template_dir + "quarterly.csv", filePath);
		
		File file = new File(filePath);
		FileWriter fr = new FileWriter(file, true);
		BufferedWriter br = new BufferedWriter(fr);

		List<Quarterly> list = quarterlyRepository.findByStockIdOrderByDateDesc(sid);
		for (Quarterly info : list) {
			String strDate = formatter.format(info.getDate());
			String str = strDate + "," + info.getGm() + "," + info.getOpr() + "," + info.getNpat() + "," + info.getEps() + ",";
			br.write(str + "\n");
			br.flush();
		}

		br.close();
		fr.close();

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
