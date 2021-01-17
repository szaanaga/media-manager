package com.zanaga.mediamanager.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Timestamp implements Comparable<Timestamp> {
	
	public static Timestamp choose(Timestamp... timestamps) {
		Timestamp ret = null;
		for(Timestamp timestamp : timestamps) {
			if(ret == null) {
				ret = timestamp;
			}
			else {
				ret = ret.getMin(timestamp);
			}
		}
		return ret;
	}
	
	
	private Date date;
	
	
	public Timestamp(String text, SimpleDateFormat timestampFormat) {
		if(text != null && text.trim().length() > 0) {
			try {
				this.date = timestampFormat.parse(text);
			}
			catch(Exception exception) {
				exception.printStackTrace();
			}
		}
	}
	
	
	private Timestamp getMin(Timestamp t) {
		Long l1 = getTimeInMillis();
		Long l2 = t.getTimeInMillis();
		if(l1 != null) {
			if(l2 != null) {
				return l2 < l1 ? t : this;
			}
			else {
				return this;
			}
		}
		else {
			if(l2 != null) {
				return t;
			}
			else {
				return this;
			}
		}
	}
	
	public Long getTimeInMillis() {
		if(this.date != null && this.date.getTime() > 0) {
			return this.date.getTime();
		}
		return null;
	}

	@Override
	public int compareTo(Timestamp o) {
		Long l1 = getTimeInMillis();
		Long l2 = o.getTimeInMillis();
		int ret = Boolean.compare(l1 != null, l2 != null);
		if(ret == 0 && l1 != null) {
			ret = l1.compareTo(l2);
		}
		return ret;
	}
}
