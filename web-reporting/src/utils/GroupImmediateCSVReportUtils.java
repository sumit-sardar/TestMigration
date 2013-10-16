package utils;


import java.io.IOException;
import java.io.PrintWriter;
import java.text.Normalizer;





public class GroupImmediateCSVReportUtils extends StudentImmediateReportUtils  implements ImmediateReport {
	
	PrintWriter cvsOutStream;
	
	@Override
	void close() {
	}
	
	@Override
	void initialize() {
		cvsOutStream = getOutWriter();
	}

	@Override
	public void generateReport() throws Exception {
		writeStudentTestData();
	}
	
	public void writeHeaderRow(PrintWriter outputStream,Integer productId) throws Exception {
		cvsOutStream = outputStream;
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
		headerRow.append(",");
		headerRow.append(LISTENING_RAW_SCORE_CSV);
		headerRow.append(",");
		headerRow.append(LISTENING_SCALE_SCORE_CSV);
		headerRow.append(",");
		headerRow.append(LISTENING_PROFICIENCY_LEVEL_CSV);
		headerRow.append(",");
		headerRow.append(SPEAKING_RAW_SCORE_CSV);
		headerRow.append(",");
		headerRow.append(SPEAKING_SCALE_SCORE_CSV);
		headerRow.append(",");
		headerRow.append(SPEAKING_PROFICIENCY_LEVEL_CSV);
		headerRow.append(",");
		headerRow.append(ORAL_RAW_SCORE_CSV);
		headerRow.append(",");
		headerRow.append(ORAL_SCALE_SCORE_CSV);
		headerRow.append(",");
		headerRow.append(ORAL_PROFICIENCY_LEVEL_CSV);
		headerRow.append(",");
		headerRow.append(READING_RAW_SCORE_CSV);
		headerRow.append(",");
		headerRow.append(READING_SCALE_SCORE_CSV);
		headerRow.append(",");
		headerRow.append(READING_PROFICIENCY_LEVEL_CSV);
		headerRow.append(",");
		headerRow.append(WRITING_RAW_SCORE_CSV);
		headerRow.append(",");
		headerRow.append(WRITING_SCALE_SCORE_CSV);
		headerRow.append(",");
		headerRow.append(WRITING_PROFICIENCY_LEVEL_CSV);
		headerRow.append(",");
		headerRow.append(COMPREHENSION_RAW_SCORE_CSV);
		headerRow.append(",");
		headerRow.append(COMPREHENSION_SCALE_SCORE_CSV);
		headerRow.append(",");
		headerRow.append(COMPREHENSION_PROFICIENCY_LEVEL_CSV);
		headerRow.append(",");
		if(productId == 7000){
			headerRow.append(OVERALL_RAW_SCORE_CSV);
			headerRow.append(",");
			headerRow.append(OVERALL_SCALE_SCORE_CSV);
			headerRow.append(",");
			headerRow.append(OVERALL_PROFICIENCY_LEVEL_CSV);
			headerRow.append('\n');
		}else{
			headerRow.append(PRODUCTIVE_RAW_SCORE_CSV);
			headerRow.append(",");
			headerRow.append(PRODUCTIVE_SCALE_SCORE_CSV);
			headerRow.append(",");
			headerRow.append(PRODUCTIVE_PROFICIENCY_LEVEL_CSV);
			headerRow.append(",");
			headerRow.append(LITERACY_RAW_SCORE_CSV);
			headerRow.append(",");
			headerRow.append(LITERACY_SCALE_SCORE_CSV);
			headerRow.append(",");
			headerRow.append(LITERACY_PROFICIENCY_LEVEL_CSV);
			headerRow.append(",");
			headerRow.append(OVERALL_RAW_SCORE_CSV);
			headerRow.append(",");
			headerRow.append(OVERALL_SCALE_SCORE_CSV);
			headerRow.append(",");
			headerRow.append(OVERALL_PROFICIENCY_LEVEL_CSV);
			headerRow.append('\n');
		}
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
		if(getIrsScores() != null) {
			for(int i = 0; i < getIrsScores().length; i++) {
				if(getIrsScores()[i] != null) {
					studentData.append(getIrsScores()[i].getRawScore());
					studentData.append(",");
					studentData.append(getIrsScores()[i].getScaleScore());
					studentData.append(",");
					studentData.append(getIrsScores()[i].getProficiencyLevel());
					studentData.append(",");
				}
			}
		}
		studentData.append('\n');
		if(studentData != null) {
			cvsOutStream.write(studentData.toString());
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
