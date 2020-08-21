package stock.master.app.service;

import org.apache.log4j.Logger;

public class logService {
	
	private static Logger logger = Logger.getLogger(logService.class);
	
	public static String debug(String message) {
		
		String callerName = new Exception().getStackTrace()[1].getMethodName();
		Integer callerLine = new Exception().getStackTrace()[1].getLineNumber();
		String msg = "( " + callerName + ":" + callerLine.toString() +" ) " + message;

		logger.debug(msg);
		return msg;
	}
	
	public static String error(String message) {
		
		String callerName = new Exception().getStackTrace()[1].getMethodName();
		Integer callerLine = new Exception().getStackTrace()[1].getLineNumber();
		String msg = "( " + callerName + ":" + callerLine.toString() +" ) " + message;

		logger.error(msg);
		return msg;
	}
}
