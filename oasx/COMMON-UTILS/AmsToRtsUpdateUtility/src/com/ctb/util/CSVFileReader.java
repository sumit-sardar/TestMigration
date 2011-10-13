package com.ctb.util;

import java.io.FileReader;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

public class CSVFileReader {

	@SuppressWarnings("unchecked")
	public static List getFileContent(String fileName) throws Exception {

		CSVReader reader = new CSVReader(new FileReader(fileName));
		List list = reader.readAll();// CSV file content.....
		return list;
	}

}
