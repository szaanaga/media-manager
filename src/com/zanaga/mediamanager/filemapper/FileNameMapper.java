package com.zanaga.mediamanager.filemapper;

import com.zanaga.mediamanager.util.file.iterators.FileIterator;
import com.zanaga.mediamanager.Settings;
import com.zanaga.mediamanager.util.Util;
import com.zanaga.mediamanager.util.file.filter.FileFilter;
import com.zanaga.mediamanager.util.file.parser.ParserFactory;
import com.zanaga.mediamanager.util.file.structure.FileStructure;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.function.Consumer;

public class FileNameMapper {

	public static void execute() {
		new FileNameMapper();
	}
	
	
	private final FileFilter filter;
	private final ParserFactory dateTimeUtil;
	private final SimpleDateFormat dateFormat;
	private final ArrayList<String> lineList;
	
	private int count = 0;
	private int parsed = 0;
	private HashMap<Long, HashSet<File>> fileMap = new HashMap<>();
	
	private FileNameMapper() {
		
		this.filter = new FileFilter();
		this.dateTimeUtil = new ParserFactory();
		this.dateFormat = (SimpleDateFormat)SimpleDateFormat.getInstance();
		this.dateFormat.applyPattern("yyyyMMddHHmmssSSS");
		this.lineList = new ArrayList<>();
		
		FileIterator fileIterator = new FileIterator(createEvaluator());
		
		fileIterator.iterate(new File(Settings.FOLDER_DISCOESTERNO));
		fileIterator.iterate(new File(Settings.FOLDER_DISCOESTERNO_LAPTOP));
		fileIterator.iterate(new File(Settings.FOLDER_LAPTOP));
		
		System.err.println(this.count);
		System.err.println(this.parsed);
		System.err.println(this.fileMap.size());

		this.lineList.forEach(System.out::println);
	}
	
	private Consumer<File> createEvaluator() {
		return file -> {
			this.count++;
			if(this.count % 250 == 0) {
				System.err.println(this.count);
			}
			String suffix = Util.getSuffix(file);
			if(suffix != null && this.filter.filter(suffix)) {
				FileStructure fileStructure = this.dateTimeUtil.getFileStructure(file, Util.getName(file));
				if(fileStructure != null) {
					String line = fileStructure.toCSV(this.dateFormat);
					System.out.println(line);
					this.lineList.add(line);
				}
				else {
					System.err.println("Unknown name pattern: '" + file.getAbsolutePath() + "'");
					System.err.println(this.count);
					System.exit(-2);
				}
			}
			else if(this.filter.isUnknown(suffix)) {
				System.err.println("Unknown suffix: '" + suffix + "'");
				System.err.println(this.count);
				System.exit(-1);
			}
		};
	}
}
