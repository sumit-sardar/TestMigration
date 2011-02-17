package com.ctb.itemmap.csv;

public interface MappingWriter {
	public void writeLine(String line);
	public void close();
}
