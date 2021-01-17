package com.zanaga.mediamanager.util.file.structure;

import com.zanaga.mediamanager.util.Util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileStructure {

	private final File file;
	private Date exifCreatedTime;
	private Date parsedTimestamp;
	private String hash;
	
	
	public FileStructure(File file) {
		this.file = file;
	}

	public File getFile() {
		return file;
	}

	public Date getExifCreatedTime() {
		return exifCreatedTime;
	}

	public void setExifCreatedTime(Date exifCreatedTime) {
		this.exifCreatedTime = exifCreatedTime;
	}

	public Date getParsedTimestamp() {
		return parsedTimestamp;
	}

	public void setParsedTimestamp(Date parsedTimestamp) {
		this.parsedTimestamp = parsedTimestamp;
	}
	
	private String getHash() {
		if(this.hash == null) {
			this.hash = Util.getChecksum(getFile());
		}
		return this.hash;
	}

	private long getSize() {
		return getFile().length();
	}
	
	public String toCSV(SimpleDateFormat dateFormat) {
		Long[] calculated = Util.calculateTimestamp(this);
		return new StringBuffer()
				.append("\"").append(getFile().getAbsolutePath()).append("\"")
				.append(",")
				.append("\"").append(getHash()).append("\"")
				.append(",")
				.append("\"").append(getSize()).append("\"")
				.append(",")
				.append("\"").append(getExifCreatedTime() != null ? dateFormat.format(getExifCreatedTime()) : "").append("\"")
				.append(",")
				.append("\"").append(getParsedTimestamp() != null ? dateFormat.format(getParsedTimestamp()) : "").append("\"")
				.append(",")
				.append("\"").append(calculated[0] != null ? dateFormat.format(new Date(calculated[0])) : "").append("\"")
				.append(",")
				.append("\"").append(calculated[1]).append("\"")
				.toString();
	}
}
