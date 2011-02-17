package com.ctb.reporting;

public interface Report {
	boolean isSuccess();
	String toString(boolean isSubReport);
}
