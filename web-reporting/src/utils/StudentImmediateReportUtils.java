package utils;

import java.io.OutputStream;
import java.io.PrintWriter;
import com.ctb.bean.studentManagement.StudentScoreReport;
import com.ctb.bean.testAdmin.StudentReportIrsScore;
import com.itextpdf.text.DocumentException;

public abstract class StudentImmediateReportUtils {

	
	private String studentName;
	private String studentExtPin;
	private String formRe;
	private String grade;
	private String district;
	private String school;
	private String testAdminName;
	private StudentReportIrsScore[] irsScores;
	private String testAdminStartDateString;
	private OutputStream out;
	private String titleText = "LAS Links Online Student Report";
	private String testName;
	private PrintWriter outWriter;
	

	abstract void generateReport() throws Exception ;
	abstract void initialize() throws DocumentException ;
	abstract void close() ;
	
	public void setup(OutputStream outputStream, StudentScoreReport stuReport, String testAdminStartDateString) {
		this.studentName=  stuReport.getStudentName();
		this.studentExtPin =stuReport.getStudentExtPin1();
		this.formRe= stuReport.getForm();
		this.grade= stuReport.getGrade();
		this.district= stuReport.getDistrict();
		this.school= stuReport.getSchool();
		this.irsScores=stuReport.getStudentReportIrsScore();
		this.testAdminStartDateString = testAdminStartDateString;
		this.testAdminName = stuReport.getTestAdminName();
		this.testName = stuReport.getTestName();
		this.out = outputStream;
	}
	
	public void setupCSV(PrintWriter outputStream, StudentScoreReport stuReport, String testAdminStartDateString) {
		this.studentName=  stuReport.getStudentName();
		this.studentExtPin =stuReport.getStudentExtPin1();
		this.formRe= stuReport.getForm();
		this.grade= stuReport.getGrade();
		this.district= stuReport.getDistrict();
		this.school= stuReport.getSchool();
		this.irsScores=stuReport.getStudentReportIrsScore();
		this.testAdminStartDateString = testAdminStartDateString;
		this.testAdminName = stuReport.getTestAdminName();
		this.testName = stuReport.getTestName();
		this.outWriter = outputStream;
	}

	/**
	 * @return the studentName
	 */
	public String getStudentName() {
		return studentName;
	}

	/**
	 * @return the studentExtPin
	 */
	public String getStudentExtPin() {
			//return '--' if there is no data, to maintain report layout. 
			if("".equalsIgnoreCase(studentExtPin) || studentExtPin == null) {
				return "--";
			}
		return studentExtPin;
	}

	/**
	 * @return the formRe
	 */
	public String getFormRe() {
		return formRe;
	}

	/**
	 * @return the grade
	 */
	public String getGrade() {
		return grade;
	}

	/**
	 * @return the district
	 */
	public String getDistrict() {
		return district;
	}

	/**
	 * @return the school
	 */
	public String getSchool() {
		return school;
	}

	/**
	 * @return the irsScores
	 */
	public StudentReportIrsScore[] getIrsScores() {
		return irsScores;
	}

	/**
	 * @return the testAdminStartDateString
	 */
	public String getTestAdminStartDateString() {
		return testAdminStartDateString;
	}

	/**
	 * @return the out
	 */
	public OutputStream getOut() {
		return out;
	}
	
	public String getTitleText() {
		return titleText;
	}
	/**
	 * @return the testAdminName
	 */
	public String getTestAdminName() {
		return testAdminName;
	}
	/**
	 * @param testAdminName the testAdminName to set
	 */
	public void setTestAdminName(String testAdminName) {
		this.testAdminName = testAdminName;
	}
	/**
	 * @return the testName
	 */
	public String getTestName() {
		return testName;
	}
	/**
	 * @param testName the testName to set
	 */
	public void setTestName(String testName) {
		this.testName = testName;
	}
	/**
	 * @return the outWriter
	 */
	public PrintWriter getOutWriter() {
		return outWriter;
	}
	/**
	 * @param outWriter the outWriter to set
	 */
	public void setOutWriter(PrintWriter outWriter) {
		this.outWriter = outWriter;
	}

	

}
