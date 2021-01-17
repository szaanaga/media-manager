package com.zanaga.mediamanager.util;

public class Counter {

	private final String name;
	private long count;
	
	
	public Counter(String name) {
		this.name = name;
		this.count = 0;
	}
	
	
	public void incr() {
		this.count++;
	}
	
	public void incr(long count) {
		this.count+=count;
	}
	
	
	public void logInfo(Class<?> classs) {
		Logger.info(classs, this.name, Long.toString(this.count));
	}
}
