package com.zanaga.photo;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class FileFilter {
	
	private final static boolean ALLOW_JPG = true;
	private final static boolean ALLOW_VIDEO = true;
	
	public static boolean isImage(File file) {
        switch(Util.getSuffix(file)) {
        case "jpg":
        case "JPG":
        case "jpeg":
        	return true;
        default:
        	return false;
        }
	}
	
	public static boolean isVideo(File file) {
        switch(Util.getSuffix(file)) {
        case "mp4":
        case "MP4":
        case "mov":
        case "MOV":
        case "mpg":
        case "3gp":
        	return true;
        default:
        	return false;
        }
	}

	private Set<String> evaluableSuffixSet;
	private Set<String> notEvaluableSuffixSet;

	public FileFilter() {
		
		this.evaluableSuffixSet = new HashSet<>();
		if(ALLOW_JPG) {
			this.evaluableSuffixSet.addAll(Arrays.asList(
					"jpg", 
					"JPG",
					"jpeg"
			));
		}
		if(ALLOW_VIDEO) {
			this.evaluableSuffixSet.addAll(Arrays.asList(
					"mp4",
					"MP4",
					"mov",
					"MOV",
					"mpg",
					"3gp"
			));
		}

		this.notEvaluableSuffixSet = new HashSet<>();
		if(!ALLOW_JPG) {
			this.notEvaluableSuffixSet.addAll(Arrays.asList(
					"jpg", 
					"JPG",
					"jpeg"
			));
		}
		if(!ALLOW_VIDEO) {
			this.notEvaluableSuffixSet.addAll(Arrays.asList(
					"mp4",
					"MP4",
					"mov",
					"MOV",
					"mpg",
					"3gp"
			));
		}
		this.notEvaluableSuffixSet.addAll(Arrays.asList(
				"db",
				"THM",
				"MTS",
				"gif",
				"bmp", 
				"txt",
				"png",
				"pdf",
				"PDF",
				"docx",
				"snb",
				"xls",
				"xlsx",
				"amr",
				"m4a",
				null
		));
	}
	
	public boolean filter(String suffix) {
		return this.evaluableSuffixSet.contains(suffix);
	}
	
	public boolean isUnknown(String suffix) {
		return this.notEvaluableSuffixSet.add(suffix);
	}
}
