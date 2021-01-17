package com.zanaga.mediamanager.reportparser;

import com.zanaga.mediamanager.*;
import com.zanaga.mediamanager.util.Counter;
import com.zanaga.mediamanager.util.Logger;
import com.zanaga.mediamanager.util.Timestamp;
import com.zanaga.mediamanager.util.Util;
import com.zanaga.mediamanager.util.file.filter.FileFilter;
import com.zanaga.mediamanager.util.file.operations.FileManager;
import com.zanaga.mediamanager.util.file.structure.FileGroup;
import com.zanaga.mediamanager.util.file.structure.FileGroupKey;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ReportParser {

	
	
	public static void execute() {
		try {
			ReportParser reportParser = new ReportParser();
			reportParser.read(new File("Video.txt"));
//			reportParser.writeIncomplete(new HashSet<>(Arrays.asList("jpg")));
//			reportParser.writeComplete(true);
//			reportParser.move();
		}
		catch(Exception exception) {
			exception.printStackTrace();
		}
	}
	
	
	private HashMap<FileGroupKey, FileGroup> map = new HashMap<>();
	private List<FileGroup> incompleteList = new ArrayList<>();
	
	
	/*
	 * READ
	 */
	
	private void read(File inputFile) throws Exception {
		
		Counter countRead = new Counter("Read");
		Counter countParsed = new Counter("Parsed");
		Counter sizeParsed = new Counter("Parsed (size)");
		Counter countCollapsed = new Counter("Collapsed");
		Counter sizeCollapsed = new Counter("Collapsed (size)");
		
		SimpleDateFormat timestampFormat = Util.createSimpleDateFormat(Settings.PATTERN_SCAN);
		
		BufferedReader reader = new BufferedReader(new FileReader(inputFile));
		String line;
		while((line = reader.readLine()) != null) {
			countRead.incr();
			
			String[] items = Util.split(line);
			File file = new File(items[0]);
			String hash = items[1];
			Long size = Util.parseLong(items[2]);
//			Timestamp exifTime = new Timestamp(items[3], timestampFormat);
//			Timestamp parsedTime = new Timestamp(items[4], timestampFormat);
//			Timestamp timestamp = Timestamp.choose(exifTime, parsedTime);
			Timestamp timestamp = new Timestamp(items[5], timestampFormat);
			
			
//			FileGroupKey key = new FileGroupKey(hash, size);
			FileGroupKey key = null;
			if(FileFilter.isImage(file)) {
				key = new FileGroupKey(hash);
			}
			else if(FileFilter.isVideo(file)) {
				key = new FileGroupKey(hash);
//				key = new FileGroupKey(hash, size);
			}
			if(key.isValid()) {
				
				countParsed.incr();
				sizeParsed.incr(size);
				if(!this.map.containsKey(key)) {
					countCollapsed.incr();
					sizeCollapsed.incr(size);
					this.map.put(key, new FileGroup(file, hash, size, timestamp));
				}
				else {
					this.map.get(key).add(file, timestamp);
				}
			}
			else {
				this.incompleteList.add(new FileGroup(file, hash, size, timestamp));
			}
		}
		reader.close();
		
		countRead.logInfo(getClass());
		countParsed.logInfo(getClass());
		sizeParsed.logInfo(getClass());
		countCollapsed.logInfo(getClass());
		sizeCollapsed.logInfo(getClass());
	}

	
	/*
	 * ACTIONS
	 */
	
	private void writeComplete(boolean valid) {
		AtomicInteger count = new AtomicInteger(0);
		this.map.values().stream().filter(structure -> structure.isValid() == valid && hasFilesWithDifferentNames(structure)).sorted().forEach(structure -> {
			structure.logComplete();
			count.incrementAndGet();
		});
		Logger.info(getClass(), "COUNT_COMPLETE_" + (valid ? "VALID" : "NOTVALID"), Integer.toString(count.get()));
	}
	
	private boolean hasFilesWithDifferentNames(FileGroup fileGroup) {
		if(fileGroup.getFileSet().size() == 1) {
			if(fileGroup.getFileSet().stream().map(file -> file.getName()).collect(Collectors.toSet()).size() == 1) {
				return true;
			}
		}
		return false;
	}
	
	private void writeIncomplete(HashSet<String> suffixSet) {

		// Print the suffix
		this.incompleteList.stream().map(structure -> Util.getSuffix(structure.getFileSet().stream().findFirst().orElse(null))).collect(Collectors.toSet()).forEach(suffix -> {
			Logger.info(getClass(), "INCOMPLETE_SUFFIX", suffix);
		});
		
		// Print all incomplete
		AtomicInteger count = new AtomicInteger(0);
		this.incompleteList.stream().filter(structure -> suffixSet.size() == 0 || suffixSet.contains(Util.getSuffix(structure.getFileSet().stream().findFirst().orElse(null)))).forEach(structure -> {
			structure.logIncomplete();
			count.incrementAndGet();
		});
		Logger.info(getClass(), "COUNT_INCOMPLETE", Integer.toString(count.get()));
	}
	
	private void move() {
		FileManager fileManager = new FileManager();
		AtomicInteger count = new AtomicInteger(0);
		this.map.values().stream().filter(structure -> structure.isValid()).sorted().forEach(structure -> {
			fileManager.move(
					structure.getTimeInMillis(), 
					structure.getFileName(), 
					structure.getFileSet()
			);
			count.incrementAndGet();
		});
		fileManager.printInfo();
	}
}
