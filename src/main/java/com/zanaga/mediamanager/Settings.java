package com.zanaga.mediamanager;

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

	public static final boolean FILE_FILTER_ALLOW_JPG = true;
	public static final boolean FILE_FILTER_ALLOW_VIDEO = true;

	public static final boolean FILE_MANAGER_ENABLE_FOLDER_CREATE = true;
	public static final boolean FILE_MANAGER_ENABLE_FILE_MOVE = FILE_MANAGER_ENABLE_FOLDER_CREATE && true;
	public static final boolean FILE_MANAGER_ENABLE_FILE_DELETE = FILE_MANAGER_ENABLE_FILE_MOVE && true;
}
