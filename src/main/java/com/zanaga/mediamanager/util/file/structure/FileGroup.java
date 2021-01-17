package com.zanaga.mediamanager.util.file.structure;

import com.zanaga.mediamanager.util.Logger;
import com.zanaga.mediamanager.Settings;
import com.zanaga.mediamanager.util.Timestamp;
import com.zanaga.mediamanager.util.Util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class FileGroup implements Comparable<FileGroup> {
	
	private static final SimpleDateFormat DATEFORMAT = Util.createSimpleDateFormat(Settings.PATTERN_FILENAME);
	private static final HashSet<String> fileNameSet = new HashSet<>();

	
	private String hash;
	private long size;
	private Timestamp timestamp;
	private Set<File> fileSet = new HashSet<>();
	private String fileName;
	
	
	public FileGroup(File file, String hash, long size, Timestamp timestamp) {
		this.hash = hash;
		this.size = size;
		this.timestamp = timestamp;
		getFileSet().add(file);
	}
	
	
	public void add(File file, Timestamp timestamp) {
		this.timestamp = Timestamp.choose(this.timestamp, timestamp);
		getFileSet().add(file);
	}

	
	public Long getTimeInMillis() {
		return this.timestamp.getTimeInMillis();
	}

	public Set<File> getFileSet() {
		return this.fileSet;
	}


	public boolean isValid() {
		return getTimeInMillis() != null;
	}
	
	public String getFileName() {
		if(this.fileName == null) {
			if(isValid()) {
				String namePrefix = DATEFORMAT.format(new Date(getTimeInMillis()));
				String suffix = Util.getSuffix(getFileSet().stream().findFirst().orElse(null));
				int index=0;
				while(!fileNameSet.add(this.fileName = (namePrefix + formatNameSuffix(index++) + "." + suffix)));
			}
			else {
				this.fileName = "NO_NAME";
			}
		}
		return this.fileName;
	}
	
	private String formatNameSuffix(int index) {
		String suffix = Integer.toString(index);
		while(suffix.length() < 4) {
			suffix = "0" + suffix;
		}
		return "_" + suffix;
	}
	
	public void logComplete() {
		StringBuffer buffer = new StringBuffer();
		getFileSet().stream().map(File::getName).sorted().forEach(name -> {
			if(buffer.length() > 0) {
				buffer.append(";");
			}
			buffer.append(name);
		});
		Logger.info(getClass(), getFileName(), buffer.toString());
	}
	
	public void logIncomplete() {
		StringBuffer buffer = new StringBuffer();
		getFileSet().stream().map(File::getName).sorted().forEach(name -> {
			if(buffer.length() > 0) {
				buffer.append(";");
			}
			buffer.append(name);
		});
		Logger.info(getClass(), this.hash, Long.toString(this.size), buffer.toString());
	}
	
	@Override
	public int compareTo(FileGroup o) {
		int ret = this.timestamp.compareTo(o.timestamp);
		if(ret == 0) {
			String f1 = getFileSet().stream().map(File::getName).sorted().findFirst().orElse(null);
			String f2 = o.getFileSet().stream().map(File::getName).sorted().findFirst().orElse(null);
			ret = f1.substring(0, f1.lastIndexOf(".")).compareToIgnoreCase(f2.substring(0, f2.lastIndexOf(".")));
			if(ret == 0) {
				ret = this.hash.compareTo(o.hash);
				if(ret == 0) {
					ret = Long.compare(this.size, o.size);
				}
			}
		}
		return ret;
	}
}
