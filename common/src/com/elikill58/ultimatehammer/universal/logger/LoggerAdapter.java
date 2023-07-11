package com.elikill58.ultimatehammer.universal.logger;

public interface LoggerAdapter {

	void info(String msg);
	
	void warn(String msg);
	
	void error(String msg);
	
	void printError(String message, Throwable e);
}
