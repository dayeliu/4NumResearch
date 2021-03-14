package stock.master.app.service.Impl;

import java.io.File;
import java.io.FileWriter;

import stock.master.app.constant.ConstantKey;
import stock.master.app.util.Log;

public class WriteFile{

	private File file = null;
	private FileWriter writer = null;
	
	public WriteFile(String fileName) {
		super();

		try {
			file = new File(ConstantKey.strategy_dir + fileName);
			file.createNewFile();
			writer = new FileWriter(file); 
		} catch (Exception e) {
			Log.error("Create File Failed. file : " + fileName);
		} finally {
			//writer.close();
		}
	}

	public void write(String msg) {
		if (writer == null) {
			Log.error("Null writer handle");
		}
	
		try {
			writer.write(msg + "\n");
			writer.flush();
		} catch (Exception e) {
			Log.error("Write File Failed. msg : " + msg);
		}
	}
}
