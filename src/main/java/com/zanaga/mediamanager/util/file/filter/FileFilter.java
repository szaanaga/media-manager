package com.zanaga.mediamanager.util.file.filter;

import com.zanaga.mediamanager.Settings;
import com.zanaga.mediamanager.util.Util;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class FileFilter {

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

	private final Set<String> evaluableSuffixSet = new HashSet<>();
	private final Set<String> notEvaluableSuffixSet = new HashSet<>();

	public FileFilter() {
		
		if(Settings.FILE_FILTER_ALLOW_JPG) {
			this.evaluableSuffixSet.addAll(Arrays.asList(
					"jpg", 
					"JPG",
					"jpeg"
			));
		}
		if(Settings.FILE_FILTER_ALLOW_VIDEO) {
			this.evaluableSuffixSet.addAll(Arrays.asList(
					"mp4",
					"MP4",
					"mov",
					"MOV",
					"mpg",
					"3gp"
			));
		}

		if(!Settings.FILE_FILTER_ALLOW_JPG) {
			this.notEvaluableSuffixSet.addAll(Arrays.asList(
					"jpg", 
					"JPG",
					"jpeg"
			));
		}
		if(!Settings.FILE_FILTER_ALLOW_VIDEO) {
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
