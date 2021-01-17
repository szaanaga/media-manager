package com.zanaga.mediamanager.util.file.structure;

public class FileGroupKey {

	private final boolean valid;
	private final String key;
	
	
	public FileGroupKey(String hash) {
		boolean valid = false;
		if(hash != null && hash.trim().length() > 0) {
			valid = true;
		}
		this.valid = valid;
		this.key = hash;
	}

	public FileGroupKey(String hash, Long size) {
		boolean valid = false;
		if(hash != null && hash.trim().length() > 0) {
			if(size != null && size > 0) {
				valid = true;
			}
		}
		this.valid = valid;
		this.key = hash + "@" + size;
	}

	
	public boolean isValid() {
		return this.valid;
	}

	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return toString().equals(obj.toString());
	}

	@Override
	public String toString() {
		return this.key;
	}
}
