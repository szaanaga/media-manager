package com.zanaga.mediamanager.util.file.iterators;

import java.io.File;
import java.util.function.Consumer;

public class FileIterator {

	private Consumer<File> evaluator;
	
	public FileIterator(Consumer<File> evaluator) {
		this.evaluator = evaluator;
	}
	
	public void iterate(File file) {
		if(file.isDirectory()) {
			for(File child : file.listFiles()) {
				iterate(child);
			}
		}
		else if(file.isFile()) {
			this.evaluator.accept(file);
		}
	}
}
