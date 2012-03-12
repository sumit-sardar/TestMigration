package utils;

import java.io.IOException;
import java.io.OutputStream;

import com.ctb.bean.studentManagement.StudentScoreReport;

public class StudentImmediateCSVReportUtils extends StudentImmediateReportUtils implements ImmediateReport {
	
	OutputStream cvsOutStream;
		
	@Override
	public void setup(OutputStream outputStream, StudentScoreReport stuReport, String testAdminStartDateString) {
		super.setup(outputStream, stuReport, testAdminStartDateString);		
	}
	
	@Override
	 void close() {
	}
	
	@Override
	void initialize(){
		cvsOutStream = getOut();
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
		cvsOutStream.write(headerRow.toString().getBytes());
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
		studentData.append(getFormRe());
		studentData.append(",");
		studentData.append(getDistrict());
		studentData.append(",");
		studentData.append(getSchool());
		studentData.append(",");
		studentData.append(getGrade());
		studentData.append(",");
		studentData.append(getTestAdminName());
		studentData.append(",");
		studentData.append("\n");
		cvsOutStream.write(studentData.toString().getBytes());
		
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
					studentScores.append(getIrsScores()[i].getContentAreaName());
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
			cvsOutStream.write(studentScores.toString().getBytes());
		}
	}

}
