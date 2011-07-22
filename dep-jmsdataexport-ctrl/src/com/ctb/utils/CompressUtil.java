package com.ctb.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

public class CompressUtil {
	
	public static void gzipFile(String from, String to) throws IOException {
	    FileInputStream in = new FileInputStream(from);
	    GZIPOutputStream out = new GZIPOutputStream(new FileOutputStream(to));
	    byte[] buffer = new byte[4096];
	    int bytesRead;
	    while ((bytesRead = in.read(buffer)) != -1)
	      out.write(buffer, 0, bytesRead);
	    in.close();
	    out.close();
	  }


}
