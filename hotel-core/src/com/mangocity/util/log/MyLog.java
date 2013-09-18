package com.mangocity.util.log;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

public class MyLog {
	private Logger dbLogger = Logger.getLogger("dataBase");
	private Log defLogger;
	private String className;

	protected MyLog(Class<?> clazz) {
		this.className = clazz.getName();
		this.defLogger = LogFactory.getLog(clazz);
	}

	public static MyLog getLogger(Class<?> clazz) {
		MyLog log = new MyLog(clazz);
		return log;
	}

	public void debug(Object message) {
		defLogger.debug(message);
	}

	public void debug(Object message, Throwable t) {
		defLogger.debug(message, t);
	}

	public void info(Object message) {
		defLogger.info(message);
	}

	public void info(Object message, Throwable t) {
		defLogger.info(message, t);
	}

	public void error(Object message) {
		defLogger.error(message);
	}

	public void error(Object message, Throwable t) {
		defLogger.error(message, t);
	}

	public void warn(Object message) {
		defLogger.warn(message);
	}

	public void warn(Object message, Throwable t) {
		defLogger.warn(message, t);
	}

	public void fatal(Object message) {
		defLogger.fatal(message);
	}

	public void fatal(Object message, Throwable t) {
		defLogger.fatal(message, t);
	}

	public void db(Object message) {
		dbLogger.log(LoggerLevel.DB, "[" + className + "]-" + message);
	}

	public void db(Object message, Throwable t) {
		dbLogger.log(LoggerLevel.DB, "[" + className + "]-" + message, t);
	}

	public void setDefLogger(Log defLogger) {
		this.defLogger = defLogger;
	}

	public Log getDefLogger() {
		return defLogger;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
}
