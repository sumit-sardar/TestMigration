package utils;

import java.io.OutputStream;

import com.ctb.bean.studentManagement.StudentScoreReport;
import com.ctb.bean.testAdmin.StudentReportIrsScore;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;




public class StudentImmediatePdfReportUtils extends StudentImmediateReportUtils implements ImmediateReport {
	private Document document;
	private PdfWriter writer;
	private float lineY;
	private float studentNameY;
	private float idY;
	private float testDateY;
	private float formY;
	private float districtY;
	private float schoolY;
	private float gradeY;
	private float testNameY;
	private float scoreTableY;

	@Override
	public void setup(OutputStream outputStream, StudentScoreReport stuReport, String testAdminStartDateString) {
		super.setup(outputStream, stuReport, testAdminStartDateString);
	}
	
	@Override
	 void close() {
		document.close();
	}
	
	@Override
	void initialize() throws DocumentException {
		this.document = new Document();
		this.document.setPageSize( PageSize.LETTER.rotate());
		this.writer = PDFUtils.getWriter(document, getOut());
		this.document.open();

	}
	
	
	@Override
	public void generateReport() throws Exception {
		initialize();
		createHeaderTable();
		
		createScroreReportTable();
		addFooter();
		close();
		
	}
	

	private void createScroreReportTable() throws Exception{
		createRosterData();
		addRosterScoreData();
	}

	private void createHeaderTable() throws DocumentException {
		addTitle();
	}
	
	private void createRosterData() throws DocumentException {
		addStudentName();
		addStudentId();
		addTestDate();
		addForm();
		addDistrict();
		addSchool();
		addGrade();
		addTestName();
	}
	
	 private void addTitle() throws DocumentException{
		 	addTitleText();
		 	addTitleLine();
	 }
	 
	 private void addStudentName() throws DocumentException{
		 	addStudentNameLabel();
		 	addStudentNameValue();
	 }
	 
	 private void addStudentId() throws DocumentException{
		 	addStudentIdLabel();
		 	addStudentIdValue();
	 }
	 
	 private void addTestDate() throws DocumentException{
		 	addTestDateLabel();
		 	addTestDateValue();
	 }
	 
	 private void addForm() throws DocumentException{
		 	addFormLabel();
		 	addFormValue();
	 }
	 
	 private void addDistrict() throws DocumentException{
		 	addDistrictLabel();
		 	addDistrictValue();
	 }
	 
	 private void addSchool() throws DocumentException{
		 	addSchoolLabel();
		 	addSchoolValue();
	 }
	 
	 private void addGrade() throws DocumentException{
		 	addGradeLabel();
		 	addGradeValue();
	 }
	 
	 private void addTestName() throws DocumentException{
		 	addTestNameLabel();
		 	addTestNameValue();
	 }

	private void addTitleText() throws DocumentException {
		PDFTableVO table = PDFUtils.getTitleTable(getTitleText(),TITLE_VALUE_WIDTH,TITLE_VALUE_X, TITLE_Y);
		PDFUtils.write(writer,table.getTable(), 0, table.getEnd(), table.getX(), table.getY());
	}
	
	 private void addTitleLine() throws DocumentException{
		 float titleHeight = PDFUtils.getTitleHeight(getTitleText(), TITLE_VALUE_WIDTH);
		 lineY = TITLE_Y - titleHeight - TITLE_LINE_SPACING;
		 PDFTableVO table = PDFUtils.getLineTable(LINE_WIDTH, LEFT_X, lineY);
		 PDFUtils.write(writer,table.getTable(), 0, table.getEnd(), table.getX(), table.getY());
	 }

	 private void addFooter() throws DocumentException {
		 PDFTableVO table = PDFUtils.getFooterTable(COPYWRITE, FOOTER_WIDTH, LEFT_X, FOOTER_Y);
		 PDFUtils.write(writer,table.getTable(), 0, table.getEnd(), table.getX(), table.getY());
	 }

	 private void addStudentNameLabel() throws DocumentException {
		 studentNameY = lineY - LINE_TEST_NAME_SPACING;
		 PDFTableVO table = PDFUtils.getLabelTable(STUDENT_NAME_LABEL, INFO_LABEL_WIDTH, LEFT_X, studentNameY);
		 PDFUtils.write(writer,table.getTable(), 0, table.getEnd(), table.getX(), table.getY());
	 }
	    
	 private void addStudentNameValue() throws DocumentException {
	 	PDFTableVO table = PDFUtils.getInfoTable(getStudentName(), INFO_VALUE_WIDTH, INFO_VALUE_X, studentNameY);
		PDFUtils.write(writer,table.getTable(), 0, table.getEnd(), table.getX(), table.getY());
	 } 
	    
	private void addStudentIdLabel() throws DocumentException {
		idY = studentNameY - PDFUtils.getInfoHeight(getStudentName(), INFO_VALUE_WIDTH) - LINE_ROSTER_DATA_SPACING;
		PDFTableVO table = PDFUtils.getLabelTable(STUDENT_Id_LABEL, INFO_LABEL_WIDTH, LEFT_X, idY);
		PDFUtils.write(writer,table.getTable(), 0, table.getEnd(), table.getX(), table.getY());
	}
		    
	private void addStudentIdValue() throws DocumentException {
		PDFTableVO table = PDFUtils.getInfoTable(getStudentExtPin(), INFO_VALUE_WIDTH, INFO_VALUE_X, idY);
		PDFUtils.write(writer,table.getTable(), 0, table.getEnd(), table.getX(), table.getY());
	} 
		 
	private void addTestDateLabel() throws DocumentException {
		testDateY = idY - PDFUtils.getInfoHeight(getStudentExtPin(), INFO_VALUE_WIDTH) - LINE_ROSTER_DATA_SPACING;
		PDFTableVO table = PDFUtils.getLabelTable(TEST_DATE_LABEL, INFO_LABEL_WIDTH, LEFT_X, testDateY);
		PDFUtils.write(writer,table.getTable(), 0, table.getEnd(), table.getX(), table.getY());
	}
			    
	private void addTestDateValue() throws DocumentException {
		PDFTableVO table = PDFUtils.getInfoTable(getTestAdminStartDateString(), INFO_VALUE_WIDTH, INFO_VALUE_X, testDateY);
		PDFUtils.write(writer,table.getTable(), 0, table.getEnd(), table.getX(), table.getY());
	} 
			 
	private void addFormLabel() throws DocumentException {
		formY = testDateY - PDFUtils.getInfoHeight(getTestAdminStartDateString(), INFO_VALUE_WIDTH) - LINE_ROSTER_DATA_SPACING;
		PDFTableVO table = PDFUtils.getLabelTable(FORM_LABEL, INFO_LABEL_WIDTH, LEFT_X, formY);
		PDFUtils.write(writer,table.getTable(), 0, table.getEnd(), table.getX(), table.getY());
	}
				    
	private void addFormValue() throws DocumentException {
		PDFTableVO table = PDFUtils.getInfoTable(getFormRe(), INFO_VALUE_WIDTH, INFO_VALUE_X, formY);
		PDFUtils.write(writer,table.getTable(), 0, table.getEnd(), table.getX(), table.getY());
	} 
				 
	private void addDistrictLabel() throws DocumentException {
		districtY = formY - PDFUtils.getInfoHeight(getFormRe(), INFO_VALUE_WIDTH) - LINE_ROSTER_DATA_SPACING;
		PDFTableVO table = PDFUtils.getLabelTable(DISTRICT_LABEL, INFO_LABEL_WIDTH, LEFT_X, districtY);
		PDFUtils.write(writer,table.getTable(), 0, table.getEnd(), table.getX(), table.getY());
	}
					    
	private void addDistrictValue() throws DocumentException {
		PDFTableVO table = PDFUtils.getInfoTable(getDistrict(), INFO_VALUE_WIDTH, INFO_VALUE_X, districtY);
		PDFUtils.write(writer,table.getTable(), 0, table.getEnd(), table.getX(), table.getY());
	} 
					 
	private void addSchoolLabel() throws DocumentException {
		schoolY = districtY - PDFUtils.getInfoHeight(getDistrict(), INFO_VALUE_WIDTH) - LINE_ROSTER_DATA_SPACING;
		PDFTableVO table = PDFUtils.getLabelTable(SCHOOL_LABEL, INFO_LABEL_WIDTH, LEFT_X, schoolY);
		PDFUtils.write(writer,table.getTable(), 0, table.getEnd(), table.getX(), table.getY());
	}
						    
	private void addSchoolValue() throws DocumentException {
		PDFTableVO table = PDFUtils.getInfoTable(getSchool(), INFO_VALUE_WIDTH, INFO_VALUE_X, schoolY);
		PDFUtils.write(writer,table.getTable(), 0, table.getEnd(), table.getX(), table.getY());
	}
						 
	private void addGradeLabel() throws DocumentException {
		gradeY = schoolY - PDFUtils.getInfoHeight(getSchool(), INFO_VALUE_WIDTH) - LINE_ROSTER_DATA_SPACING;
		PDFTableVO table = PDFUtils.getLabelTable(GRADE_LABEL, INFO_LABEL_WIDTH, LEFT_X, gradeY);
		PDFUtils.write(writer,table.getTable(), 0, table.getEnd(), table.getX(), table.getY());
	}
							    
	private void addGradeValue() throws DocumentException {
		PDFTableVO table = PDFUtils.getInfoTable(getGrade(), INFO_VALUE_WIDTH, INFO_VALUE_X, gradeY);
		PDFUtils.write(writer,table.getTable(), 0, table.getEnd(), table.getX(), table.getY());
	}
	
	private void addTestNameLabel() throws DocumentException {
		testNameY = gradeY - PDFUtils.getInfoHeight(getGrade(), INFO_VALUE_WIDTH) - LINE_ROSTER_DATA_SPACING;
		PDFTableVO table = PDFUtils.getLabelTable(TEST_NAME_LABEL, INFO_LABEL_WIDTH, LEFT_X, testNameY);
		PDFUtils.write(writer,table.getTable(), 0, table.getEnd(), table.getX(), table.getY());
	}
							    
	private void addTestNameValue() throws DocumentException {
		PDFTableVO table = PDFUtils.getInfoTable(getTestAdminName(), INFO_VALUE_WIDTH, INFO_VALUE_X, testNameY);
		PDFUtils.write(writer,table.getTable(), 0, table.getEnd(), table.getX(), table.getY());
	}
	
	private void addRosterScoreData() throws DocumentException {
		scoreTableY = testNameY - SCORE_TABLE_SPACING;
		PDFTableVO table = PDFUtils.getTacTable(getRosterScoreData(getIrsScores()),
                PAGE_WIDTH,
                FOUR_COLUMN_TAC_WIDTHS,
                LEFT_X,
                scoreTableY,
                SCORE_BORDER);
		PDFUtils.write(writer,table.getTable(), 0, table.getEnd(), table.getX(), table.getY());
	}
	
	private String[] getRosterScoreData(StudentReportIrsScore[] irsScores){
        int numContentAreas = irsScores.length;
        int total = 4 * (1 + numContentAreas);
        String[] result = new String[total];
        int index = 0;
        result[index++] = "";
        result[index++] = RAW_SCORE;
        result[index++] = SCALE_SCORE;
        result[index++] = PROFICIENCY_LEVEL;
        for(int i = 0; i < numContentAreas; i++) {
        	StudentReportIrsScore contentAreaScores = irsScores[i];
        	if(contentAreaScores != null) {
	        	result[index++] = contentAreaScores.getContentAreaName();
	        	result[index++] = contentAreaScores.getRawScore().toString();
	        	result[index++] = contentAreaScores.getScaleScore().toString();
	        	result[index++] = contentAreaScores.getProficiencyLevel().toString();
        	}
        }
        
        return result;
    }

}
