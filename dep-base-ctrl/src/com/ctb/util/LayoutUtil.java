package com.ctb.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.ctb.bean.testAdmin.LiteracyProExportData;

public class LayoutUtil {
	private static final String windowsEOL = "\r\n";
	private static final String headerWithoutQuotes = "Student Login ID, Last Name, Middle Name, First Name, Date of Birth, Gender, Assessment Date, Instrument, Form, Level, Subtest, Scaled Score, GLE, Session Name";
	
    public static byte[] getLiteracyProExportDataBytes(LiteracyProExportData[] tableData) {
	if (tableData != null && tableData.length > 0) {
	    CharArrayWriter out = new CharArrayWriter();
	    try {
		out.write(getHeader());
		for (LiteracyProExportData rowData : tableData) {
		    out.write(wrap(rowData.getStudentID()));
		    out.write(",");
		    out.write(wrap(rowData.getLastName()));
		    out.write(",");
		    out.write(wrap(rowData.getMiddleName()));
		    out.write(",");
		    out.write(wrap(rowData.getFirstName()));
		    out.write(",");
		    out.write(wrap(rowData.getDateofBirth()));
		    out.write(",");
		    out.write(wrap(rowData.getGender()));
		    out.write(",");
		    out.write(wrap(rowData.getAssessmentDate()));
		    out.write(",");
		    out.write(wrap(rowData.getInstrument()));
		    out.write(",");
		    out.write(wrap(rowData.getForm()));
		    out.write(",");
		    out.write(wrap(rowData.getLvl()));
		    out.write(",");
		    out.write(wrap(rowData.getSubtest()));
		    out.write(",");
		    out.write(wrap(rowData.getScaledScore()));
		    out.write(",");
		    out.write(wrap(rowData.getGLE()));
		    out.write(",");
		    out.write(wrap(rowData.getSessionName()));
		    out.write(windowsEOL);
		}
		out.flush();
		out.close();
		System.out.println("Table Data bytes created. Rows = " + tableData.length);
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	    return out.toString().getBytes();
	} else {
	    System.out.println("0 Records. Returning header only.");
	    return getHeader().getBytes();
	}
    }

  /*  private static String getHeader() {
	return "\"Student Login ID\",\"Last Name\",\"Middle Name\",\"First Name\",\"Date of Birth\",\"Gender\",\"Assessment Date\",\"Instrument\",\"Form\",\"Level\",\"Subtest\",\"Scaled Score\",\"GLE\",\"Session Name\"\n";
    }*/
    
    private static String getHeader() {
    	return headerWithoutQuotes + windowsEOL;
    }

    public static String wrap(String s) {
	StringBuffer sb = new StringBuffer();
	if (s != null) {
	    sb.append(s);
	}
	sb.insert(0, "\"").append("\"");
	return sb.toString();
    }
    
    /**
	 * Creates a zip byte array from a byte array. The zip byte array can be used to create a zip file.
	 * 
	 * @param filePath
	 * @param input
	 * @return
	 * @throws IOException
	 */
	public static byte[] zipBytes(String fileName, byte[] input) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(baos);
		if (fileName == null) {
		    	System.out.println("Filename is null");
			return "".getBytes();
		}
		ZipEntry entry = new ZipEntry(fileName);
		entry.setSize(input.length);
		zos.putNextEntry(entry);
		zos.write(input);
		zos.closeEntry();
		zos.close();
		return baos.toByteArray();
	}
	
	
    /**
     * Creates a byte array from a zip byte array.
     * @param zipBytes
     * @return
     */
    public static byte[] unZippedBytes(byte[] zipBytes) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		try {
			ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(
					zipBytes));
			ZipEntry ze = zis.getNextEntry();
			while (ze != null) {
				int len;
				while ((len = zis.read(buffer)) > 0) {
					baos.write(buffer, 0, len);
				}
				baos.close();
				ze = zis.getNextEntry();
			}
			zis.closeEntry();
			zis.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return baos.toByteArray();
	}
}
