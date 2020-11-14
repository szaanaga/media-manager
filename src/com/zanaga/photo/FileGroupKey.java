package com.zanaga.photo;

public class FileGroupKey {

	private boolean valid;
	private String key;
	
	
	public FileGroupKey(String hash) {
		this.valid = false;
		if(hash != null && hash.trim().length() > 0) {
			this.valid = true;
		}
		this.key = hash;
	}

	public FileGroupKey(String hash, Long size) {
		this.valid = false;
		if(hash != null && hash.trim().length() > 0) {
			if(size != null && size > 0) {
				this.valid = true;
			}
		}
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
