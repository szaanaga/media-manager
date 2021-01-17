package com.zanaga.mediamanager.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.imageio.ImageIO;

import com.drew.imaging.mp4.Mp4MetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.mp4.Mp4Directory;
import com.zanaga.mediamanager.util.file.filter.FileFilter;
import com.zanaga.mediamanager.util.file.structure.FileStructure;

public class Util {
	
	public static String getName(File file) {
		String name = file.getName();
		int endIndex = name.lastIndexOf(".");
		if(endIndex != -1) {
			return name.substring(0, endIndex);
		}
		else {
			return null;
		}
	}

	public static String getSuffix(File file) {
		String name = file.getName();
		int beginIndex = name.lastIndexOf(".");
		if(beginIndex != -1) {
			return name.substring(beginIndex+1);
		}
		else {
			return null;
		}
	}
	
	public static String format(int value, int length) {
		String ret = Integer.toString(value);
		while(ret.length() < length) {
			ret = "0" + ret;
		}
		return ret;
	}
	
	public static String[] split(String text) {
		String SEPARATOR = "\",\"";
		ArrayList<String> list = new ArrayList<>();
		text = text.substring(1, text.length()-1);
		int index;
		while((index = text.indexOf(SEPARATOR)) != -1) {
			list.add(text.substring(0, index));
			text = text.substring(index+SEPARATOR.length());
		}
		return list.toArray(count -> new String[count]);
	}

	public static Long parseLong(String value) {
		try {
			return Long.parseLong(value);
		}
		catch(Exception exception) {
			return null;
		}
	}
	
	public static SimpleDateFormat createSimpleDateFormat(String pattern) {
		SimpleDateFormat dateFormat = (SimpleDateFormat)SimpleDateFormat.getInstance();
		dateFormat.applyPattern(pattern);
		return dateFormat;
	}

	public static Long[] calculateTimestamp(FileStructure fileStructure) {
		
		Date exif = fileStructure.getExifCreatedTime();
		Date parsed = fileStructure.getParsedTimestamp();
		
		boolean rightExif = exif != null && exif.getTime() > 0;
		boolean rightParsed = parsed != null && parsed.getTime() > 0;
		
		if(rightExif) {
			if(FileFilter.isImage(fileStructure.getFile())) {
				fileStructure.setExifCreatedTime(exif = add(exif, exif.getTimezoneOffset()/60));
			}
			else if(FileFilter.isVideo(fileStructure.getFile())) {
				fileStructure.setExifCreatedTime(exif = add(exif, 1));
			}
		}
		
		boolean hasTime = false;
		if(rightParsed) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(parsed);
			int count = 
					calendar.get(Calendar.HOUR) + 
					calendar.get(Calendar.MINUTE) + 
					calendar.get(Calendar.SECOND) + 
					calendar.get(Calendar.MILLISECOND);
			hasTime = count > 0;
		}
		
		if(rightExif) {
			if(rightParsed) {
				if(hasTime) {
					long diff = Math.abs(exif.getTime() - parsed.getTime());
					if(diff > (1_000 * 60 * 30)) {
						return new Long[] {exif.getTime(), diff};
					}
					else {
						return new Long[] {exif.getTime(), 0l};
					}
				}
				else {
					return new Long[] {exif.getTime(), 0l};
				}
			}
			else {
				return new Long[] {exif.getTime(), 0l};
			}
		}
		else {
			if(rightParsed) {
				return new Long[] {parsed.getTime(), 0l};
			}
			else {
				return new Long[] {null, 0l};
			}
		}
	}
	
	private static Date add(Date date, int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR_OF_DAY, amount);
		return calendar.getTime();
	}
	
	public static String getChecksum(File file) {
		try {
			if(FileFilter.isImage(file)) {
	        	return getCheckSumPhoto(file);
			}
			else if(FileFilter.isVideo(file)) {
				return getCheckSumVideo(file);
//	        	return getDuration(file);
			}
		}
		catch(Exception exception) {

		}
		return "";
	}
	
	private static String getCheckSumPhoto(File file) throws Exception {

        MessageDigest md = MessageDigest.getInstance("MD5");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(ImageIO.read(file), Util.getSuffix(file), outputStream);

        md.update(outputStream.toByteArray());
        
        StringBuilder sb = new StringBuilder();
		for(byte b : md.digest()) {
			sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring( 1 ));
        }

    	return sb.toString();
	}
	
	private static String getCheckSumVideo(File file) throws Exception {
		InputStream fis =  new FileInputStream(file);

		byte[] buffer = new byte[1024];
		MessageDigest complete = MessageDigest.getInstance("MD5");
		int numRead;

		do {
			numRead = fis.read(buffer);
			if (numRead > 0) {
				complete.update(buffer, 0, numRead);
	           }
		} while (numRead != -1);
		
		fis.close();
		
		byte[] b = complete.digest();
		String result = "";

		for (int i=0; i < b.length; i++) {
			result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
		}
		return result;
	}
	
	private static String getDuration(File file) {
		try {
			for(Directory directory : Mp4MetadataReader.readMetadata(file).getDirectoriesOfType(Mp4Directory.class)) {
				if(directory.containsTag(Mp4Directory.TAG_DURATION)) {
					Long duration = directory.getLong(Mp4Directory.TAG_DURATION);
					if(duration != null) {
						return duration.toString();
					}
				}
			}
		}
		catch(Exception exception) {

		}
		return null;
	}
}
