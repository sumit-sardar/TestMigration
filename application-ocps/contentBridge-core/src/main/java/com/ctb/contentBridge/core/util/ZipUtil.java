package com.ctb.contentBridge.core.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtil {

	public static ZipOutputStream createZipFile(String fileName)
			throws FileNotFoundException {
		new File(fileName).delete();
		ZipOutputStream out = new ZipOutputStream(
				new FileOutputStream(fileName));
		out.setLevel(Deflater.BEST_COMPRESSION);
		return out;

	}

	public static void appendFileInZipFile(ZipOutputStream output,
			String fileName, byte[] val) throws IOException {
		try {
			output.putNextEntry(new ZipEntry(fileName));
			output.write(val);

		} finally {
			try {
				output.closeEntry();
			} catch (Exception e) {

			}

		}

	}

	public static void closeZipFile(ZipOutputStream output) {
		try {
			output.close();
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

}
