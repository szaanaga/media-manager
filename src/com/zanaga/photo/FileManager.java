package com.zanaga.photo;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Calendar;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class FileManager {

	private final String ACTION_FOLDERCREATED 	= "FOLDER_CREATED";
	private final String ACTION_FILEMOVED 		= "FILE_MOVED";
	private final String ACTION_FILEDELETED 	= "FILE_DELETED";
	
	private final boolean ENABLE_FOLDERCREATE 	= true;
	private final boolean ENABLE_FILEMOVE 		= ENABLE_FOLDERCREATE && true;
	private final boolean ENABLE_FILEDELETE 	= ENABLE_FILEMOVE && true;
	
	private Counter countFolderCreated = new Counter(ACTION_FOLDERCREATED);
	private Counter countFileMoved = new Counter(ACTION_FILEMOVED);
	private Counter countFileDeleted = new Counter(ACTION_FILEDELETED);
	
	
	public boolean move(long timestamp, String fileName, Set<File> fileSet) {
		
		File folder = getOrCreateFolder(timestamp);

		File source = fileSet.stream().findFirst().orElse(null);
		
		File target = new File(folder.getPath() + File.separator + fileName);
		
		if(move(source, target)) {
			
			AtomicBoolean ret = new AtomicBoolean(true);
			fileSet.stream().filter(file -> file.exists()).forEach(file -> {
				if(!delete(file)) {
					ret.set(false);
				}
			});
			return ret.get();
		}

		return false;
	}
	
	public void printInfo() {
		this.countFolderCreated.logInfo(getClass());
		this.countFileMoved.logInfo(getClass());
		this.countFileDeleted.logInfo(getClass());
	}
	
	
	private File getOrCreateFolder(long timestamp) {
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timestamp);
		
		// Base
		File folder = getOrCreateFolder(Settings.BASE_FOLDER_SORTED);
		
		// Year
		folder = getOrCreateFolder(folder.getPath() + File.separator + calendar.get(Calendar.YEAR));
		
		// Month
		folder = getOrCreateFolder(folder.getPath() + File.separator + Util.format(calendar.get(Calendar.MONTH)+1, 2));

		return folder;
	}
	
	private File getOrCreateFolder(String folderPath) {
		File folder = new File(folderPath);
		if(!folder.exists()) {
			mkdir(folder);
		}
		return folder;

	}
	
	
	/*
	 * COMMANDS
	 */
	
	private boolean mkdir(File folder) {
		boolean ret = true;
		if(ENABLE_FOLDERCREATE) {
			ret = folder.mkdir();
			this.countFolderCreated.incr();
			Logger.info(getClass(), ACTION_FOLDERCREATED, folder.getAbsolutePath());
		}
		return ret;
	}
	
	private boolean move(File fileSource, File fileTarget) {
		try {
			if(ENABLE_FILEMOVE) {
				Files.move(fileSource.toPath(), fileTarget.toPath(), StandardCopyOption.ATOMIC_MOVE);
				this.countFileMoved.incr();
				Logger.info(getClass(), ACTION_FILEMOVED, fileSource.getAbsolutePath(), fileTarget.getAbsolutePath());
			}
			return true;
		}
		catch(Exception exception) {
			Logger.error(getClass(), exception, ACTION_FILEMOVED, fileSource.getAbsolutePath(), fileTarget.getAbsolutePath());
			return false;
		}
	}
	
	private boolean delete(File fileSource) {
		try {
			if(ENABLE_FILEDELETE) {
				Files.delete(fileSource.toPath());
				this.countFileDeleted.incr();
				Logger.info(getClass(), ACTION_FILEDELETED, fileSource.getAbsolutePath());
			}
			return true;
		}
		catch(Exception exception) {
			Logger.error(getClass(), exception, ACTION_FILEDELETED, fileSource.getAbsolutePath());
			return false;
		}
	}
}
