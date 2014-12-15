package com.mhe.ctb.oas.BMTSync;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

public class TestUtils {
	
	private TestUtils()
	{
		// Blank since this is a pure static class
	}
	
	public static String readFileFromClasspath(Class<?> rootClassPath, String filename) throws IOException
	{
		String canonical = rootClassPath.getCanonicalName();
		String rootPath = canonical
							.substring(0, canonical.lastIndexOf('.'))
							.replace('.', File.separatorChar);
		
		String fullPath = rootPath + File.separatorChar + filename;
		
		InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream(fullPath);
		if (stream == null)
		{
			throw new FileNotFoundException(fullPath);
		}
		
		String result = IOUtils.toString(stream);
		IOUtils.closeQuietly(stream);
		
		return result;
	}
	

}
