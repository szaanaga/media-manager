package com.zanaga.mediamanager;

import com.zanaga.mediamanager.filemapper.FileNameMapper;
import com.zanaga.mediamanager.foldercleaner.FolderCleaner;
import com.zanaga.mediamanager.reportparser.ReportParser;

public class Cleaner {

	public static void main(String[] args) {
		FolderCleaner.execute();
	}
}
