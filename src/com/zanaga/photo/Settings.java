package com.zanaga.photo;

import java.time.ZoneId;
import java.util.TimeZone;

public class Settings {

	public static final String FOLDER_DISCOESTERNO = "D:\\Disco esterno";
	public static final String FOLDER_DISCOESTERNO_LAPTOP = "D:\\Disco esterno  e Laptop";
	public static final String FOLDER_LAPTOP = "D:\\Laptop";
	public static final String BASE_FOLDER_SORTED = "D:\\Sorted";
	
	public static final String PATTERN_SCAN = "yyyyMMddHHmmssSSS";
	public static final String PATTERN_FILENAME = "yyyy-MM-dd_HH-mm-ss";
	
	public static final TimeZone TIMEZONE = TimeZone.getTimeZone(ZoneId.of("Europe/Rome"));
}
