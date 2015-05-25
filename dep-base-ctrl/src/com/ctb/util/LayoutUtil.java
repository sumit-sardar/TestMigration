package com.ctb.util;

import java.io.CharArrayWriter;
import java.io.IOException;

import com.ctb.bean.testAdmin.LiteracyProExportData;

public class LayoutUtil {
    
    public static byte[] getLiteracyProExportDataBytes(LiteracyProExportData[] tableData) {
	if (tableData != null && tableData.length > 0) {
		CharArrayWriter out = new CharArrayWriter();
		try {
		    out.write("StudentID");
		    out.write(",");
		    out.write("Last Name");
		    out.write(",");
		    out.write("Middle Name");
		    out.write(",");
		    out.write("First Name");
		    out.write(",");
		    out.write("Date of Birth");
		    out.write(",");
		    out.write("Gender");
		    out.write(",");
		    out.write("AssessmentDate");
		    out.write(",");
		    out.write("Instrument");
		    out.write(",");
		    out.write("Form");
		    out.write(",");
		    out.write("Level");
		    out.write(",");
		    out.write("Subtest");
		    out.write(",");
		    out.write("ScaledScore");
		    out.write(",");
		    out.write("GLE");
		    out.write(",");
		    out.write("Session Name");
		    out.write("\n");
		    for (LiteracyProExportData rowData : tableData) {
			out.write(rowData.getOasStudentId() == null ? "" : rowData.getOasStudentId());
			out.write(",");
			out.write(rowData.getLastName() == null ? "" : rowData.getLastName());
			out.write(",");
			out.write(rowData.getMiddleName() == null ? "" : rowData.getMiddleName());
			out.write(",");
			out.write(rowData.getFirstName() == null ? "" : rowData.getFirstName());
			out.write(",");
			out.write(rowData.getDateofBirth() == null ? "" : rowData.getDateofBirth());
			out.write(",");
			out.write(rowData.getGender() == null ? "" : rowData.getGender());
			out.write(",");
			out.write(rowData.getAssessmentDate() == null ? "" : rowData.getAssessmentDate());
			out.write(",");
			out.write(rowData.getInstrument() == null ? "" : rowData.getInstrument());
			out.write(",");
			out.write(rowData.getForm() == null ? "" : rowData.getForm());
			out.write(",");
			out.write(rowData.getLvl() == null ? "" : rowData.getLvl());
			out.write(",");
			out.write(rowData.getSubtest() == null ? "" : rowData.getSubtest());
			out.write(",");
			out.write(rowData.getScaledScore() == null ? "" : rowData.getScaledScore());
			out.write(",");
			out.write(rowData.getGLE() == null ? "" : rowData.getGLE());
			out.write(",");
			out.write(rowData.getSessionName() == null ? "" : rowData.getSessionName());
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
		return "No Records Found".getBytes();
	}
    }
	

}
