package com.ctb.contentcreator.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {

	public static void saveToFile(String dir, String filename, byte[] content)
			throws IOException {
		File ff;
		FileOutputStream out = null;
		if (dir != null) {
			ff = new File(dir, filename);
		} else {
			ff = new File(filename);
		}
		try {
			out = new FileOutputStream(ff);
			out.write(content);
		} finally {
			ColsableHelper.close(out);
		}

	}

	public static void deleteFile(String dir, String filename) {
		File ff;
		if (dir != null) {
			ff = new File(dir, filename);
		} else {
			ff = new File(filename);
		}
		ff.delete();

	}

	public static FileOutputStream getFileOutputStream(String dir, String fileName) throws FileNotFoundException {
		return new FileOutputStream(new File(dir, fileName));

	}

}
