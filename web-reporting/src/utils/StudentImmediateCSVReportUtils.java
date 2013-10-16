package utils;

import java.io.IOException;
import java.io.PrintWriter;

import com.ctb.bean.studentManagement.StudentScoreReport;
import java.text.Normalizer;

public class StudentImmediateCSVReportUtils extends StudentImmediateReportUtils implements ImmediateReport {
	
	PrintWriter cvsOutStream;
		
	@Override
	public void setupCSV(PrintWriter writer, StudentScoreReport stuReport, String testAdminStartDateString) {
		super.setupCSV(writer, stuReport, testAdminStartDateString);		
	}
	
	@Override
	 void close() {
	}
	
	@Override
	void initialize(){
		cvsOutStream = getOutWriter();
	}
	
	
	@Override
	public void generateReport() throws Exception {
		initialize();
		writeStudentData();
		close();
	}
	
	private void writeStudentData() throws IOException {
		writeHeaderRow();
		writeStudentTestData();
		writeStudentScores();
	}
	
	private void writeHeaderRow() throws IOException {
		StringBuffer headerRow = new StringBuffer();
		headerRow.append(STUDENT_NAME_LABEL_CSV);
		headerRow.append(",");
		headerRow.append(STUDENT_Id_LABEL_CSV);
		headerRow.append(",");
		headerRow.append(TEST_DATE_LABEL_CSV);
		headerRow.append(",");
		headerRow.append(FORM_LABEL_CSV);
		headerRow.append(",");
		headerRow.append(DISTRICT_LABEL_CSV);
		headerRow.append(",");
		headerRow.append(SCHOOL_LABEL_CSV);
		headerRow.append(",");
		headerRow.append(GRADE_LABEL_CSV);
		headerRow.append(",");
		headerRow.append(TEST_NAME_LABEL_CSV);
		headerRow.append('\n');
		cvsOutStream.write(headerRow.toString());
	}
	
	private void writeStudentTestData() throws IOException {
		StringBuffer studentData = new StringBuffer();
		studentData.append("\"");
		studentData.append(getStudentName());
		studentData.append("\"");
		studentData.append(",");
		studentData.append(getStudentExtPin());
		studentData.append(",");
		studentData.append("\"");
		studentData.append(getTestAdminStartDateString());
		studentData.append("\"");
		studentData.append(",");
		studentData.append(removeDiacritics(getFormRe()));
		studentData.append(",");
		studentData.append(getDistrict());
		studentData.append(",");
		studentData.append(getSchool());
		studentData.append(",");
		studentData.append(getGrade());
		studentData.append(",");
		studentData.append(removeDiacritics(getTestName()));
		studentData.append(",");
		studentData.append("\n");
		cvsOutStream.write(studentData.toString());
		
	}
	
	private void writeStudentScores() throws IOException {
		StringBuffer studentScores = new StringBuffer();
		studentScores.append("\n");
		studentScores.append("\n");
		studentScores.append("\n");
		studentScores.append(",");
		studentScores.append(",");
		studentScores.append(RAW_SCORE_CSV);
		studentScores.append(",");
		studentScores.append(SCALE_SCORE_CSV);
		studentScores.append(",");
		studentScores.append(PROFICIENCY_LEVEL_CSV);
		studentScores.append("\n");
		if(getIrsScores() != null) {
			for(int i = 0; i < getIrsScores().length; i++) {
				if(getIrsScores()[i] != null) {
					studentScores.append(",");
					studentScores.append(removeDiacritics(getIrsScores()[i].getContentAreaName()));
					studentScores.append(",");
					studentScores.append(getIrsScores()[i].getRawScore());
					studentScores.append(",");
					studentScores.append(getIrsScores()[i].getScaleScore());
					studentScores.append(",");
					studentScores.append(getIrsScores()[i].getProficiencyLevel());
					studentScores.append("\n");
				}
			}
		}
		if(studentScores != null) {
			cvsOutStream.write(studentScores.toString());
		}
	}

	//** Convert all accented characters into their deAccented counterparts
	private String removeDiacritics(String stringWithSpanishChars)
	{
		String normal = Normalizer.normalize(stringWithSpanishChars, Normalizer.Form.NFD);
		StringBuilder stripped = new StringBuilder();
		for (int i=0;i<normal.length();++i)
		{
			if (Character.getType(normal.charAt(i)) != Character.NON_SPACING_MARK)
			{
				stripped.append(normal.charAt(i));
			}
		}
		return stripped.toString();
	}
}
