package com.zanaga.photo;

public class Logger {

	public static void info(Class<?> classs, String... messageItems) {
		StringBuffer buffer = new StringBuffer();
		for(String messageItem : messageItems) {
			buffer.append("|").append(messageItem);
		}
		System.out.println(classs.getSimpleName() + buffer.toString());
	}

	public static void error(Class<?> classs, Exception exception, String... messageItems) {
		StringBuffer buffer = new StringBuffer();
		for(String messageItem : messageItems) {
			buffer.append("|").append(messageItem);
		}
		System.err.println(classs.getSimpleName() + buffer.toString());
		if(exception != null) {
			exception.printStackTrace();
		}
	}
}
