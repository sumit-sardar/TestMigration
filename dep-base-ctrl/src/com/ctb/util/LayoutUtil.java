package com.ctb.util;

import java.io.CharArrayWriter;
import java.io.IOException;

import com.ctb.bean.testAdmin.LiteracyProExportData;

public class LayoutUtil {

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
		    out.write("\n");
		}
		out.flush();
		out.close();
		System.out.println("Table Data bytes created");
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	    return out.toString().getBytes();
	} else {
	    return getHeader().getBytes();
	}
    }

    private static String getHeader() {
	return "\"StudentID\",\"Last Name\",\"Middle Name\",\"First Name\",\"Date of Birth\",\"Gender\",\"AssessmentDate\",\"Instrument\",\"Form\",\"Level\",\"Subtest\",\"ScaledScore\",\"GLE\",\"Session Name\"\n";
    }

    public static String wrap(String s) {
	StringBuffer sb = new StringBuffer();
	if (s != null) {
	    sb.append(s);
	}
	sb.insert(0, "\"").append("\"");
	return sb.toString();
    }
}
