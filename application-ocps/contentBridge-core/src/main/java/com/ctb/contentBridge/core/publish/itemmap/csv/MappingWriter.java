package com.ctb.contentBridge.core.publish.itemmap.csv;

public interface MappingWriter {
	public void writeLine(String line);
	public void close();
}
