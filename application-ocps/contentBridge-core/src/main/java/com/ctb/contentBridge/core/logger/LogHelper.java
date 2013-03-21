/**
 * 
 */
package com.ctb.contentBridge.core.logger;

import java.sql.Timestamp;

import org.apache.log4j.Logger;

import com.ctb.contentBridge.core.domain.BaseTO;

/**
 * @author TATA CONSULTANCY SERVICES
 * @version 1.0
 */
public class LogHelper {
	private static Logger vLog = Logger.getLogger("");
	private static final String sSeparator = "!";
	public static final int DEBUG = 1;
	public static final int INFO = 2;
	public static final int WARN = 3;
	public static final int ERROR = 4;
	public static final int FATAL = 5;

	/**
	 * It logs the related debug message to the log file depending on the
	 * LogDetails object and log level passed as parameters.
	 * 
	 * @param avDetails
	 *            The instance of the LogDetails class.
	 * @param aiLogType
	 *            The log level.
	 */
	
	/**
	 * This method will take an input TO and the type of the log level. this
	 * will check whether the input TO is null or not.
	 * 
	 * @param BaseTO
	 * @param int
	 */

	public static void log(BaseTO avBaseTO, int aiLogType) {
		if (avBaseTO == null) {
			print("", null, aiLogType);
		} else {
			print(avBaseTO.toString(), null, aiLogType);
		}

	}

	/**
	 * It logs the related debug message to the log file depending on the
	 * LogDetails object and log level passed as parameters.
	 * 
	 * @param avDetails
	 *            The instance of the LogDetails class.
	 * @param aiLogType
	 *            The log level.
	 */

	public static void log(String asMessage, int aiLogType) {
		print(asMessage, null, aiLogType);
	}

	/**
	 * This private method creates the original logging mesage.
	 * 
	 * @param String
	 *            asMessage
	 * @param Throwable
	 *            avThrowable
	 * @int int aiLogType
	 */
	private static void print(String asMessage, Throwable avThrowable,
			int aiLogType) {
		Throwable vThrowable = new Throwable();
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(vThrowable.getStackTrace()[2].getClassName())
				.append(sSeparator)
				.append(vThrowable.getStackTrace()[2].getMethodName())
				.append(sSeparator)
				.append(new Timestamp(System.currentTimeMillis()))
				.append(sSeparator).append(asMessage).append(sSeparator);
		if (avThrowable != null) {
			sbuf.append(getMessageFromException(avThrowable));
		}
		switch (aiLogType) {
		case LogHelper.DEBUG:
			vLog.debug(sbuf);
			break;
		case LogHelper.INFO:
			vLog.info(sbuf);
			break;
		case LogHelper.WARN:
			vLog.warn(sbuf);
			break;
		case LogHelper.ERROR:
			vLog.error(sbuf);
			break;
		case LogHelper.FATAL:
			vLog.fatal(sbuf);
		default:
			vLog.debug(sbuf);
		}
	}

	/**
	 * It logs the related debug message to the log file depending on the
	 * LogDetails object and log level passed as parameters.
	 * 
	 * @param avDetails
	 *            The instance of the LogDetails class.
	 * @param aiLogType
	 *            The log level.
	 */

	public static void log(String asMessage, Throwable avThrowable,
			int aiLogType) {
		print(asMessage, avThrowable, aiLogType);
	}

	/**
	 * This is another variation of the Log method. this will take
	 * 
	 * @param BaseTO
	 *            avBaseTO
	 * @param Throwable
	 *            The instance of the Throwable object.
	 * @param aiLogType
	 *            The log level.
	 * 
	 *            This will check whether the input TO is NULL or not, if it is
	 *            null, then it will invoke the log method with null as the
	 *            first argument, else TO.toString() will be passed as the first
	 *            argument.
	 */

	public static void log(BaseTO avBaseTO, Throwable avThrowable, int aiType) {
		if (avBaseTO == null) {
			print("", avThrowable, aiType);
		} else {
			print(avBaseTO.toString(), avThrowable, aiType);
		}
	}

	/**
	 * This private method finds out the Exception messages from the stack trace
	 * elemnts.
	 * 
	 * @param Exception
	 *            ex
	 * @retyurn String
	 */
	private static String getMessageFromException(Throwable avThrowable) {
		StringBuffer value = new StringBuffer("");
		if (avThrowable != null) {
			value.append(avThrowable.toString()).append("\n");
			for (int i = 0; avThrowable != null
					&& avThrowable.getStackTrace() != null
					&& i < avThrowable.getStackTrace().length; i++) {
				value.append(avThrowable.getStackTrace()[i]).append("\n");
			}
		}
		return value.toString();
	}
}
