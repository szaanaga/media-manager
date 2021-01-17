package com.zanaga.mediamanager.foldercleaner;

import com.zanaga.mediamanager.util.Counter;
import com.zanaga.mediamanager.util.Logger;
import com.zanaga.mediamanager.Settings;

import java.io.File;

public class FolderCleaner {

	private static final boolean ENABLE_FOLDERDELETE = false;

	public static void execute() {
		FolderCleaner folderCleaner = new FolderCleaner();
		folderCleaner.iterate(new File(Settings.FOLDER_DISCOESTERNO));
		folderCleaner.iterate(new File(Settings.FOLDER_DISCOESTERNO_LAPTOP));
		folderCleaner.iterate(new File(Settings.FOLDER_LAPTOP));
		folderCleaner.counter.logInfo(FolderCleaner.class);
	}
	
	
	private Counter counter = new Counter("Deleted");
	
	private void iterate(File folder) {
	
		if(folder != null && folder.exists() && folder.isDirectory()) {
			
			for(File file : folder.listFiles()) {
				if(file.isFile()) {
					if("Thumbs.db".equals(file.getName())) {
						if(ENABLE_FOLDERDELETE) {
							file.delete();
						}
						Logger.info(getClass(), "FILEDELETED", file.getAbsolutePath());
					}
					else if(file.getName().endsWith(".THM")) {
						if(ENABLE_FOLDERDELETE) {
							file.delete();
						}
						Logger.info(getClass(), "FILEDELETED", file.getAbsolutePath());
					}
				}
				else if(file.isDirectory()) {
					iterate(file);
				}
			}
			
			if(folder.listFiles().length == 0) {
				if(ENABLE_FOLDERDELETE) {
					folder.delete();
				}
				Logger.info(getClass(), "FOLDERDELETED", folder.getAbsolutePath());
				this.counter.incr();
			}
		}
	}
}
