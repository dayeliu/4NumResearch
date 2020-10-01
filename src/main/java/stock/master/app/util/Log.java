package stock.master.app.util;

import org.apache.log4j.Logger;

public class Log {
	
	private static Logger logger = Logger.getLogger(Log.class);
	
	public static String debug(String message) {
		
		String callerName = new Exception().getStackTrace()[1].getMethodName();
		Integer callerLine = new Exception().getStackTrace()[1].getLineNumber();
		String msg = "( " + callerName + ":" + callerLine.toString() +" ) " + message;

		logger.debug(msg);
		return message;
	}
	
	public static String error(String message) {
		
		String callerName = new Exception().getStackTrace()[1].getMethodName();
		Integer callerLine = new Exception().getStackTrace()[1].getLineNumber();
		String msg = "( " + callerName + ":" + callerLine.toString() +" ) " + message;

		logger.error(msg);
		return message;
	}
}
