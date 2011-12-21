package utils; 

import com.ctb.bean.testAdmin.TestProduct;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import data.TestRosterVO;
import data.TestAdminVO;
import java.io.IOException;
import java.util.List;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTable;
import com.lowagie.text.pdf.PdfWriter;
import data.SubtestVO;
import data.TableVO;
import data.TestSummaryVO;
import data.TestVO;
import java.awt.Color;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import javax.servlet.ServletOutputStream;
//import weblogic.webservice.tools.pagegen.result;

public class SummaryTestTicketsReportUtils extends ReportUtils
{ 
    // static x page coordinates
    private static final float LEFT_X = 32f;
    private static final float TITLE_VALUE_X = 190f;
    private static final float INFO_VALUE_X = 180f;
    private static final float HALF_X = 421f;
    private static final float ACCOMM_1_LABEL_X = LEFT_X;
    private static final float ACCOMM_1_VALUE_X = 147f;
    private static final float ACCOMM_2_LABEL_X = 268f;
    private static final float ACCOMM_2_VALUE_X = 367f;
    private static final float ACCOMM_3_LABEL_X = 525f;
    private static final float ACCOMM_3_VALUE_X = 620f;
    private static final float TOTAL_STUDENTS_VALUE_X = 150f;
    private static final float TOTAL_STUDENTS_WITH_ACCOMMODATIONS_VALUE_X = 280f;
    private static final float PAGE_NUMBER_X = 700f;
    private static final float WATERMARK_X = 725f;
    
    // static y page coordinates
    private static final float TITLE_Y = 580f;
    private static final float PAGE_MIN_Y = 80f;
    private static final float FOOTER_Y = 73f;
    private static final float PAGE_NUMBER_Y = 70f;
    private static final float WATERMARK_Y = 50f;

    // y axis spacing
    private static final float TITLE_LINE_SPACING = 5f;
    private static final float LINE_TEST_NAME_SPACING = 10f;
    private static final float TEST_NAME_LEVEL_SPACING = 10f;
    private static final float SUPPORT_CONTACT_SPACING = 15f;
    private static final float LEVEL_SESSION_INFO_SPACING = 22f;
    private static final float SESSION_INFO_DYNAMIC_PAGE_SPACING = 80f;
    private static final float DYNAMIC_PAGE_TAC_TITLE_SPACING = 10f;
    private static final float TAC_TITLE_TAC_INFO_SPACING = 20f;
    private static final float TAC_INFO_TAC_TABLE_SPACING = 20f;
    private static final float TAC_TABLE_TOTAL_STUDENTS_SPACING = 15f; // For MQC defect 66844
    private static final float STUDENTS_TABLE_SPACING = 10f;
    private static final float TOTAL_STUDENTS_TOTAL_ACCOMMODATIONS_SPACING = 20f;
    private static final float TOTAL_ACCOMMODATIONS_ACCOM_1_SPACING = 15f; // For MQC defect 66844
    private static final float ACCOMM_1_ACCOMM_2_SPACING = 15f; // For MQC defect 66844
    private static final float DYNAMIC_PAGE_STUDENT_TABLE_SPACING = 10f;

    private static final float SUBTEST_HEADER_HEIGHT = 20f;
    private static final float STUDENT_ROW_HEIGHT = 16f;
    private static final float STUDENT_SUMMARY_HEIGHT = 150f;
    
    // table widths
    private static final float PAGE_WIDTH = 715f;
    private static final float TITLE_LABEL_WIDTH = 200f;
    private static final float TITLE_VALUE_WIDTH = 500f;
    private static final float INFO_LABEL_WIDTH = 100f;
    private static final float INFO_VALUE_WIDTH = 500f;
    private static final float LINE_WIDTH = 715f;
    private static final float SESSION_WIDTH = 333f;
    private static final float TAC_WIDTH = 600f;
    private static final float TOTAL_STUDENTS_WIDTH = 150f;
    private static final float TOTAL_STUDENTS_WTIH_ACCOMMODATIONS_WIDTH = 250f;
    private static final float FOOTER_WIDTH = 630f;
    private static final float SUBTEST_NAME_WIDTH = 300f;
    private static final float TAC_NAME_WIDTH = 200f;
    private static final float WATERMARK_WIDTH = 50f;

    // table column width ratios
    private static final float[] SESSION_WIDTHS = new float[] {47f, 63f};
    private static final float[] THREE_COLUMN_TAC_WIDTHS = new float[] {6f, 2f, 3f};
    private static final float[] FOUR_COLUMN_TAC_WIDTHS = new float[] {1.5f, 6f, 2f, 3f};
    private static final float[] STUDENT_WIDTHS = new float[] {50f, 32f, 72f, 21f, 13f, 21f, 31f};
    private static final float[] STUDENT_WIDTHS_FOR_TABE = new float[] {50f, 32f, 72f, 21f, 21f, 44f};

    private static final float SESSION_VALUE_WIDTH = 170f;
    // maximum number of student lines on a page
    private int maxStudentLines = 0;
    
    // table borders
    private static final float SESSION_BORDER = 1f;
    private static final float TAC_BORDER = 1f;
    private static final float STUDENT_BORDER = 1f;
    
    // page text
    private static final String PAGE_NAME_LABEL = "Summary Test Ticket:";
	private static final String TEST_NAME_LABEL = "Test Name:";
	private static final String START_DATE_LABEL = "Start Date:";
    private static final String END_DATE_LABEL = "End Date:";
	private static final String LOGIN_WINDOW_LABEL = "Login Window:";
	private static final String TIME_ZONE_LABEL = "Time Zone:";
    private static final String TIME_LIMIT_LABEL = "Time Limit:";
    private static final String ALLOW_ENFORCE_BREAKS_LABEL = "Allow/Enforce Breaks:";
    private static final String LOCATION_LABEL = "Location:";
    private static final String TAC_LABEL = "Test Access Codes";
    private static final String TAC_INFO = "Test access codes prevent early logins.  Distribute test access codes just before starting each test.";
    private static final String SEQUENCE_LABEL = "Sequence";
    private static final String TEST_NAME_TABLE_LABEL = "Test Name";
    private static final String SUBTEST_NAME = "Subtest Name";
    private static final String TEST_NAME = "Test Name";
    private static final String DURATION_LABEL = "Duration";
    private static final String TOTAL_STUDENTS_LABEL = "Total Students:";
    private static final String TOTAL_STUDENTS_WITH_ACCOMMODATIONS_LABEL = "Total Students with Accommodations:";
    private static final String CALCULATOR_LABEL = "Calculator:";
    private static final String SCREEN_READER_LABEL = "Screen Reader:";
    private static final String COLOR_FONT_LABEL = "Color/Font:";
    private static final String PAUSE_LABEL = "Pause:";
    private static final String UNTIMED_LABEL = "Untimed:";
    private static final String STUDENT_LABEL = "Student";
    private static final String STUDENT_ID_LABEL = "Student ID";
    private static final String LOGIN_ID_LABEL = "Login ID";
    private static final String PASSWORD_LABEL = "Password";
    private static final String FORM_LABEL = "Form";
    private static final String STATUS_LABEL = "Status";
    private static final String ACCOMMODATION_LABEL = "Accommodation";
    private static final String ACCESS_CODE_LABEL = "Test Access Code";
    private static final String WATERMARK_TEXT = "TTS";
    private static final String SUPPORT_CONTACT_LABEL = "For customer support, please call ";
    private static final String HIGHLIGHTER_LABEL = "Highlighter:"; /* 51931 Deferred Defect For HighLighter*//* 55815 solution for defect*/
   // Start: For MQC defect 66844
    private static final String MASKING_RULAR_LABEL = "Blocking Ruler:";
    private static final String MASKING_TOOL_LABEL = "Masking Tool:";
    private static final String MAGNIFYING_GLASS_LABEL = "Magnifying Glass";
    private static final String MUSIC_PLAYER_LABEL = "Music Player";
    private static final String EXTENDED_TIME_LABEL = "Extended Time";
    // End: For MQC defect 66844
    // variable y page coordinates
    private float lineY = 0f;
    private float testNameY = 0f;
    private float supportContactY = 0f;
    private float levelY = 0f;
    private float sessionInfoY = 0f;
    private float dynamicPageStart = 0f;
    private float tacTitleY = 0f;
    private float tacInfoY = 0f;
    private float tacTableY = 0f;
    private float studentTableY = 0f;
    
    private float totalStudentsY = 0f;  
    private float totalAccommY = 0f;    
    private float accomm1Y = 0f;      
    // Start:For MQC defect 66844
    private float accomm2Y = 0f;
    private float accomm3Y = 0f;
    private float accomm4Y = 0f;
    // End: For MQC defect 66844
    
    // global variables
	private Collection rosterList = null;
	private TestAdminVO testAdmin = null;
    private TestSummaryVO testSummary = null;
    private TestVO test = null;
    private float tacTableHeight = 0f;
    private Boolean isTabeProduct = Boolean.FALSE;
    private TestProduct testProduct;
    //START - Changed for CR GA2011CR001
    private Boolean isStudentIdConfigurable = Boolean.FALSE;
    private String studentIdLabelName = "Student ID";
    //END - Changed for CR GA2011CR001 
    /**
     * initialize globals passed into method
     * create static tables
     * set the variable y coordinates that we can
     */
     //START - Changed For CR ISTEP2011CR007 (Multiple Test Ticket)
    protected void setup(Object[] args) throws DocumentException{
        super.initializeGlobals(new Object[] {args[5], args[6], args[7], PageSize.LETTER.rotate(), args[8]});
        this.rosterList = (Collection)args[0];
        this.testAdmin = (TestAdminVO)args[1];
        this.testSummary = (TestSummaryVO)args[3];
        this.test = (TestVO)args[4];
        this.setDynamicGlobals();
        this.isTabeProduct = (Boolean)args[9];
        this.testProduct = (TestProduct)args[10];
        //START - Changed for CR GA2011CR001
        this.isStudentIdConfigurable = (Boolean)args[11];
        this.studentIdLabelName = (String)args[12];
        //END - Changed for CR GA2011CR001
        addStaticTables();
        createPages();
    }
    //END - Added For CR ISTEP2011CR007 (Multiple Test Ticket)
    
    private void setDynamicGlobals() throws DocumentException{
        float titleHeight = getTitleHeight();
        this.lineY = TITLE_Y - titleHeight - TITLE_LINE_SPACING;
        this.testNameY = this.lineY - LINE_TEST_NAME_SPACING;
        this.supportContactY = this.testNameY - SUPPORT_CONTACT_SPACING;
        float testNameHeight = getTestNameHeight();
        this.levelY = testNameY - testNameHeight - TEST_NAME_LEVEL_SPACING;
        this.sessionInfoY = this.levelY - LEVEL_SESSION_INFO_SPACING;
        this.dynamicPageStart = this.sessionInfoY - SESSION_INFO_DYNAMIC_PAGE_SPACING;
        this.tacTitleY = this.dynamicPageStart - DYNAMIC_PAGE_TAC_TITLE_SPACING;
        this.tacInfoY = this.tacTitleY - TAC_TITLE_TAC_INFO_SPACING;
        this.tacTableY = this.tacInfoY - TAC_INFO_TAC_TABLE_SPACING;
        this.studentTableY = this.dynamicPageStart - DYNAMIC_PAGE_STUDENT_TABLE_SPACING;
        float spaceForStudentTable = this.studentTableY - PAGE_MIN_Y;
        Float msl = new Float((this.studentTableY - PAGE_MIN_Y)/STUDENT_ROW_HEIGHT);
        this.maxStudentLines = msl.intValue();
    }
    
    private void addStaticTables() throws DocumentException{
        addTitle();
        addTestName();
        addSupportContact();
        if (! this.isTabeProduct.booleanValue()) { 
            addLevel();
        }
        addSessionInfo();
        addFooter();
        addWatermark();
    }
    protected boolean setDynamicTables() throws DocumentException, IOException{
        this.dynamicTables = (Collection)this.pages.get(this.currentPageIndex);
        return (this.dynamicTables != null && this.dynamicTables.size() > 0);
    }

    private float getTestNameHeight() throws DocumentException{
        String testName = getTestName();
        return tableUtils.getInfoHeight(testName, INFO_VALUE_WIDTH);
    }
    
    private float getTitleHeight() throws DocumentException{
        String sessionName = getTitleText();
        return tableUtils.getTitleHeight(sessionName, TITLE_VALUE_WIDTH);
    }
    
    protected ArrayList getStudentTable(ArrayList pageStudents) throws DocumentException{
        ArrayList result = new ArrayList();
        result.add(
            tableUtils.getStudentTable(pageStudents,
                                       getStudentHeadings(),
                                       PAGE_WIDTH,
                                       getStudentWidths(),
                                       LEFT_X,
                                       studentTableY,
                                       STUDENT_BORDER,
                                       this.isTabeProduct.booleanValue()));
        return result;
    }
    
    private float[] getStudentWidths() {
        if (this.isTabeProduct.booleanValue()) 
            return STUDENT_WIDTHS_FOR_TABE;
        else
            return STUDENT_WIDTHS;
    }
    
    private String[] getStudentHeadings(){
        String[] result = null;
        if (this.isTabeProduct.booleanValue()) {
            result =  new String[6];
            result[0] = STUDENT_LABEL;
            //START - Changed for CR GA2011CR001
            if(isStudentIdConfigurable)
            	result[1] = studentIdLabelName;
            else
                result[1] = STUDENT_ID_LABEL;
            //END - Changed for CR GA2011CR001
            result[2] = LOGIN_ID_LABEL;
            result[3] = PASSWORD_LABEL;
            result[4] = STATUS_LABEL;
            result[5] = ACCOMMODATION_LABEL;
        }            
        else {
            result =  new String[7];
            result[0] = STUDENT_LABEL;
            //START - Changed for CR GA2011CR001
            if(isStudentIdConfigurable)
            	result[1] = studentIdLabelName;
            else
                result[1] = STUDENT_ID_LABEL;
            //END - Changed for CR GA2011CR001
            result[2] = LOGIN_ID_LABEL;
            result[3] = PASSWORD_LABEL;
            result[4] = FORM_LABEL;
            result[5] = STATUS_LABEL;
            result[6] = ACCOMMODATION_LABEL;
        }
        return result;
    }
    
    protected boolean setImages() throws DocumentException, IOException{
        return false;
    }
    
    private TableVO getTestAccessCodesTitle() throws DocumentException{
        return tableUtils.getTitleTable(TAC_LABEL,
                                        PAGE_WIDTH,
                                        LEFT_X,
                                        tacTitleY);
    }
    
    private TableVO getTestAccessCodeInformation() throws DocumentException{
        return tableUtils.getInfoTable(TAC_INFO,
                                       PAGE_WIDTH,
                                       LEFT_X,
                                       tacInfoY);
    }
    
    private TableVO getTestAccessCodeTable(ArrayList subtests) throws DocumentException{
        TableVO tacTable = null;
        if(this.testAdmin.getSubtests().size() == 1){
            tacTable = tableUtils.getTacTable(getThreeColumnTacTexts(subtests),
                                                       PAGE_WIDTH,
                                                       THREE_COLUMN_TAC_WIDTHS,
                                                       LEFT_X,
                                                       tacTableY,
                                                       TAC_BORDER);
        }
        else{
            tacTable = tableUtils.getTacTable(getFourColumnTacTexts(subtests),
                                                       PAGE_WIDTH,
                                                       FOUR_COLUMN_TAC_WIDTHS,
                                                       LEFT_X,
                                                       tacTableY,
                                                       TAC_BORDER);
        }
        this.tacTableHeight = tacTable.getTable().getTotalHeight();
        return tacTable;
    }
    
     private String[] getFourColumnTacTexts(ArrayList subtests){
        int numSubtests = subtests.size();
        int total = 4 * (1 + numSubtests);
        String[] result = new String[total];
        int index = 0;
        result[index++] = SEQUENCE_LABEL;
        result[index++] = SUBTEST_NAME;
        result[index++] = DURATION_LABEL;
        result[index++] = ACCESS_CODE_LABEL;
        for(Iterator it = subtests.iterator(); it.hasNext();){
            SubtestVO subtest = (SubtestVO)it.next();
            result[index++] = getSequence(subtest);
            result[index++] = getTestName(subtest);
            result[index++] = getDuration(subtest);
            result[index++] = getAccessCode(subtest);
        }
        return result;
    }
    
     private String[] getThreeColumnTacTexts(ArrayList subtests){
        int numSubtests = subtests.size();
        int total = 3 * (1 + numSubtests);
        String[] result = new String[total];
        int index = 0;
        result[index++] = TEST_NAME;
        result[index++] = DURATION_LABEL;
        result[index++] = ACCESS_CODE_LABEL;
        for(Iterator it = subtests.iterator(); it.hasNext();){
            SubtestVO subtest = (SubtestVO)it.next();
            result[index++] = getTestName(subtest);
            result[index++] = getDuration(subtest);
            result[index++] = getAccessCode(subtest);
        }
        return result;
    }
    
    private String getSequence(SubtestVO subtest){
        String sequence = subtest.getSequence();
        return sequence == null ? " " : sequence;
    }
    
    private String getTestName(SubtestVO subtest){
        String testName = subtest.getSubtestName();
        return testName == null ? " " : testName;
    }
    
    private String getDuration(SubtestVO subtest){
        String duration = subtest.getDuration();
        return duration == null ? " " : duration;
    }
    
    private String getAccessCode(SubtestVO subtest){
        String accessCode = subtest.getTestAccessCode();
        return accessCode == null ? " " : accessCode;
    }
    
   private ArrayList getStudentSummaryInformation() throws DocumentException{
        ArrayList result = new ArrayList();
        setSummaryInfoYs();
        result.add(getTotalStudentsLabel());
        result.add(getTotalStudentsValue());
        
        Boolean supportAccommodations = this.testSummary.getSupportAccommodations();
        if ( supportAccommodations.booleanValue() ) {
            result.add(getTotalStudentsWithAccommodationsLabel());
            result.add(getTotalStudentsWithAccommodationsValue());
            if(hasAccommodatedStudents()){
                result.add(getCalculatorLabel());
                result.add(getCalculatorValue());
                result.add(getScreenReaderLabel());
                result.add(getScreenReaderValue());
                result.add(getColorFontLabel());
                result.add(getColorFontValue());
                result.add(getPauseLabel());
                result.add(getPauseValue());
                result.add(getUntimedLabel());
                result.add(getUntimedValue());
                result.add(getHighLighterLabel());  /* 51931 Deferred Defect For HighLighter*/
                result.add(getHighLighterValue());
                
                // Start: For MQC defect 66844
                result.add(getMaskingRularLabel());
                result.add(getMaskingRularValue());
                result.add(getMaskingToolLabel());
                result.add(getMaskingToolValue());
                result.add(getMagniFyingGlassLabel());
                result.add(getMagniFyingGlassValue());
                result.add(getMusicPlayerLabel());
                result.add(getMusicPlayerValue());
                result.add(getExtendedTimeLabel());
                result.add(getExtendedTimeValue());
				//End: For MQC defect 66844
                
            }
        }
        return result;
    }
    
	// For MQC defect 66844
	private TableVO getExtendedTimeValue() throws DocumentException {
		return tableUtils.getInfoTable(getExtendedTime(),
                INFO_VALUE_WIDTH,
                ACCOMM_2_VALUE_X,
                accomm4Y);
}
// For MQC defect 66844
	private TableVO getExtendedTimeLabel() throws DocumentException {
		return tableUtils.getBlueTable(EXTENDED_TIME_LABEL,
                INFO_LABEL_WIDTH,
                ACCOMM_2_LABEL_X,
                accomm4Y);
}
// For MQC defect 66844
	private TableVO getMusicPlayerValue() throws DocumentException {
		return tableUtils.getInfoTable(getMusicPlayer(),
                INFO_VALUE_WIDTH,
                ACCOMM_1_VALUE_X,
                accomm4Y);
}
// For MQC defect 66844
	private TableVO getMusicPlayerLabel() throws DocumentException {
		return tableUtils.getBlueTable(MUSIC_PLAYER_LABEL,
                INFO_LABEL_WIDTH,
                ACCOMM_1_LABEL_X,
                accomm4Y);
}
// For MQC defect 66844
	private TableVO getMagniFyingGlassValue() throws DocumentException{
		return tableUtils.getInfoTable(getMagniFyingGlass(),
                INFO_VALUE_WIDTH,
                ACCOMM_3_VALUE_X,
                accomm3Y);
}
// For MQC defect 66844
	private TableVO getMagniFyingGlassLabel() throws DocumentException{
		return tableUtils.getBlueTable(MAGNIFYING_GLASS_LABEL,
                INFO_LABEL_WIDTH,
                ACCOMM_3_LABEL_X,
                accomm3Y);
}
// For MQC defect 66844
	private TableVO getMaskingToolValue() throws DocumentException{
		return tableUtils.getInfoTable(getMaskingTool(),
                INFO_VALUE_WIDTH,
                ACCOMM_2_VALUE_X,
                accomm3Y);
}
// For MQC defect 66844
	private TableVO getMaskingToolLabel() throws DocumentException{
		return tableUtils.getBlueTable(MASKING_TOOL_LABEL,
                INFO_LABEL_WIDTH,
                ACCOMM_2_LABEL_X,
                accomm3Y);
}
// For MQC defect 66844
    private TableVO getMaskingRularValue() throws DocumentException {
    	return tableUtils.getInfoTable(getMaskingRular(),
                INFO_VALUE_WIDTH,
                ACCOMM_1_VALUE_X,
                accomm3Y);
    }
// For MQC defect 66844
	private TableVO getMaskingRularLabel() throws DocumentException {
		  return tableUtils.getBlueTable(MASKING_RULAR_LABEL,
                  INFO_LABEL_WIDTH,
                  ACCOMM_1_LABEL_X,
                  accomm3Y);
}

    private boolean hasAccommodatedStudents(){
        return testSummary.getAccommodated().intValue() > 0;
    }
    
    private void setSummaryInfoYs(){
        this.totalStudentsY = this.tacTableY - this.tacTableHeight - TAC_TABLE_TOTAL_STUDENTS_SPACING;
        this.totalAccommY = this.totalStudentsY - TOTAL_STUDENTS_TOTAL_ACCOMMODATIONS_SPACING;
        this.accomm1Y = this.totalAccommY - TOTAL_ACCOMMODATIONS_ACCOM_1_SPACING;
        this.accomm2Y = this.accomm1Y - ACCOMM_1_ACCOMM_2_SPACING;
        
        this.accomm3Y = this.accomm2Y - TOTAL_ACCOMMODATIONS_ACCOM_1_SPACING; // For MQC defect 66844
        this.accomm4Y = this.accomm3Y - ACCOMM_1_ACCOMM_2_SPACING; // For MQC defect 66844
    }
    private TableVO getTotalStudentsLabel() throws DocumentException {
        return tableUtils.getTitleTable(TOTAL_STUDENTS_LABEL,
                                        TOTAL_STUDENTS_WIDTH,
                                        LEFT_X,
                                        totalStudentsY);
   }
    
    private TableVO getTotalStudentsValue() throws DocumentException {
         return tableUtils.getInfoTable(getTotalStudents(),
                                        INFO_VALUE_WIDTH,
                                        TOTAL_STUDENTS_VALUE_X,
                                        totalStudentsY);
   }
    
    private TableVO getTotalStudentsWithAccommodationsLabel() throws DocumentException {
        return tableUtils.getLabelTable(TOTAL_STUDENTS_WITH_ACCOMMODATIONS_LABEL,
                                        TOTAL_STUDENTS_WTIH_ACCOMMODATIONS_WIDTH,
                                        LEFT_X,
                                        totalAccommY);
   }
    
    private TableVO getTotalStudentsWithAccommodationsValue() throws DocumentException {
         return tableUtils.getInfoTable(getAccommodatedStudents(),
                                       INFO_VALUE_WIDTH,
                                       TOTAL_STUDENTS_WITH_ACCOMMODATIONS_VALUE_X,
                                       totalAccommY);
   }
    
    private TableVO getCalculatorLabel() throws DocumentException {
        return tableUtils.getBlueTable(CALCULATOR_LABEL,
                                       INFO_LABEL_WIDTH,
                                       ACCOMM_1_LABEL_X,
                                       accomm1Y);
   }
    
    private TableVO getCalculatorValue() throws DocumentException {
         return tableUtils.getInfoTable(getCalculator(),
                                        INFO_VALUE_WIDTH,
                                        ACCOMM_1_VALUE_X,
                                        accomm1Y);
   }
    private TableVO getScreenReaderLabel() throws DocumentException {
        return tableUtils.getBlueTable(SCREEN_READER_LABEL,
                                       INFO_LABEL_WIDTH,
                                       ACCOMM_2_LABEL_X,
                                       accomm1Y);
   }
    
    private TableVO getScreenReaderValue() throws DocumentException {
         return tableUtils.getInfoTable(getScreenReader(),
                                        INFO_VALUE_WIDTH,
                                        ACCOMM_2_VALUE_X,
                                        accomm1Y);
   }
    private TableVO getColorFontLabel() throws DocumentException {
        return tableUtils.getBlueTable(COLOR_FONT_LABEL,
                                       INFO_LABEL_WIDTH,
                                       ACCOMM_3_LABEL_X,
                                       accomm1Y);
   }
    
    private TableVO getColorFontValue() throws DocumentException {
         return tableUtils.getInfoTable(getColorFont(),
                                        INFO_VALUE_WIDTH,
                                        ACCOMM_3_VALUE_X,
                                        accomm1Y);
   }
    private TableVO getPauseLabel() throws DocumentException {
        return tableUtils.getBlueTable(PAUSE_LABEL,
                                       INFO_LABEL_WIDTH,
                                       ACCOMM_1_LABEL_X,
                                       accomm2Y);
   }
    
    private TableVO getPauseValue() throws DocumentException {
         return tableUtils.getInfoTable(getPause(),
                                        INFO_VALUE_WIDTH,
                                        ACCOMM_1_VALUE_X,
                                        accomm2Y);
   }
    private TableVO getUntimedLabel() throws DocumentException {
        return tableUtils.getBlueTable(UNTIMED_LABEL,
                                       INFO_LABEL_WIDTH,
                                       ACCOMM_2_LABEL_X,
                                       accomm2Y);
   }
    
    private TableVO getUntimedValue() throws DocumentException {
         return tableUtils.getInfoTable(getUntimed(),
                                        INFO_VALUE_WIDTH,
                                        ACCOMM_2_VALUE_X,
                                        accomm2Y);
   }
   /*
    * 51931 Deferred Defect For HighLighter
   */
   
    private TableVO getHighLighterLabel() throws DocumentException {
        return tableUtils.getBlueTable(HIGHLIGHTER_LABEL,
                                       INFO_LABEL_WIDTH,
                                       ACCOMM_3_LABEL_X,
                                       accomm2Y);
   }
   
    private TableVO getHighLighterValue() throws DocumentException {
        return tableUtils.getInfoTable(getHighLighter(),
                                       INFO_VALUE_WIDTH,
                                       ACCOMM_3_VALUE_X,
                                       accomm2Y);
   }
   
   
    private TableVO getPageNumber(int i) throws DocumentException {
        return tableUtils.getFooterTable(getPageNumberText(i),
                                         INFO_LABEL_WIDTH,
                                         PAGE_NUMBER_X,
                                         PAGE_NUMBER_Y);
   }
    
    private String getPageNumberText(int i){
        return "Page " + (i + 1) + " of " + this.totalPages;
    }
    
    private String getTotalStudents(){
        return String.valueOf(this.rosterList.size());
    }
    
    private String getCalculator(){
        return testSummary.getCalculator().toString();
    }
    
    private String getAccommodatedStudents(){
        return testSummary.getAccommodated().toString();
    }
    
    private String getScreenReader(){
        return testSummary.getScreenReader().toString();
    }
    
    private String getColorFont(){
        return testSummary.getColorFont().toString();
    }
    
    private String getPause(){
        return testSummary.getPause().toString();
    }
    
    private String getUntimed(){
        return testSummary.getUntimed().toString();
    }
    
    
    /* 51931 Deferred Defect For HighLighter*/
    private String getHighLighter(){
        return testSummary.getHighLighter().toString();
    }
    
    private String getMaskingRular() {
    	 return testSummary.getMaskingRular().toString();
	}
    private String getMaskingTool() {
    	return testSummary.getMaskingTool().toString();
	}
    private String getMagniFyingGlass() {
    	return testSummary.getMagnifyingGlass().toString();
	}
	private String getMusicPlayer() {
		return testSummary.getMusicPlayerAccom().toString();
	}
	private String getExtendedTime() {
		return testSummary.getExtendedTimeAccom().toString();
	}
    
    private void addSupportContact() throws DocumentException {
        this.staticTables.add( 
             tableUtils.getLabelTable(SUPPORT_CONTACT_LABEL + getSupportContact(),
                                      INFO_LABEL_WIDTH * 3,
                                      LEFT_X,
                                      supportContactY));
    }
     
     private void addTestName() throws DocumentException {
        addTestNameLabel();
        addTestNameValue();
    }
    
    private void addTestNameLabel() throws DocumentException {
        this.staticTables.add( 
             tableUtils.getLabelTable(TEST_NAME_LABEL,
                                      INFO_LABEL_WIDTH,
                                      LEFT_X,
                                      testNameY));
   }
    
    private void addTestNameValue() throws DocumentException{
        this.staticTables.add( 
             tableUtils.getInfoTable(getTestName(),
                                     INFO_VALUE_WIDTH,
                                     INFO_VALUE_X,
                                     testNameY));
     } 
     
    private void addLevel() throws DocumentException{
        addLevelLabel();
        addLevelValue();
    }
    
    private void addLevelLabel() throws DocumentException{
        this.staticTables.add( 
             tableUtils.getLabelTable(getLevelLabel(),
                                      INFO_LABEL_WIDTH,
                                      LEFT_X,
                                      levelY));
    }
    private String getLevelLabel(){
        return this.test.getLevelOrGrade() + ":";
    }
    private void addLevelValue() throws DocumentException{
        this.staticTables.add( 
             tableUtils.getInfoTable(getLevel(),
                                     INFO_VALUE_WIDTH,
                                     INFO_VALUE_X,
                                     levelY));
    }
    private void addSessionInfo() throws DocumentException{
        addSessionInfoTable1();
        addSessionInfoTable2();
    }

    private void addSessionInfoTable1() throws DocumentException{
        this.staticTables.add( 
             tableUtils.getSummaryTable(getSession1Texts(),
                                        SESSION_WIDTH,
                                        SESSION_WIDTHS,
                                        LEFT_X,
                                        sessionInfoY,
                                        SESSION_BORDER));
    }
    private void addSessionInfoTable2() throws DocumentException{
        this.staticTables.add( 
             tableUtils.getSummaryTable(getSession2Texts(),
                                        SESSION_WIDTH,
                                        SESSION_WIDTHS,
                                        HALF_X,
                                        sessionInfoY,
                                        SESSION_BORDER));
    }
    
    private void createPages() throws DocumentException{
        createTacPages();
        createRosterPages();
        this.totalPages = this.pages.size();
        addPageNumbers();
    }
    
    private void addPageNumbers() throws DocumentException{
        for (int i=0; i< this.pages.size(); i++){
            ArrayList page = (ArrayList)this.pages.get(i);
            page.add(this.getPageNumber(i));
        }
    }
    private void createTacPages() throws DocumentException{
        Iterator it = this.testAdmin.getSubtests().iterator();
        float pageEnd = getPageStart();
        ArrayList pageSubtests = new ArrayList();
        SubtestVO subtest = null;
        while(it.hasNext()){
            subtest = (SubtestVO)it.next();
            pageEnd = getNewPageEnd(subtest, pageEnd);
            if(pageEnd < PAGE_MIN_Y){ //need a new page
                this.pages.add(getTacTables(pageSubtests));
                pageSubtests = new ArrayList();
                pageEnd = getPageStart(); 
            }
            pageSubtests.add(subtest);
        }
        if(!canAddSummary(pageEnd)){
            this.pages.add(getTacTables(pageSubtests));
            this.tacTableHeight = 0f;
            ArrayList lastPageTables = new ArrayList();
            lastPageTables.addAll(this.getStudentSummaryInformation());
            this.pages.add(lastPageTables);
        }
        else{
            ArrayList lastPageTables = getTacTables(pageSubtests);
            lastPageTables.addAll(this.getStudentSummaryInformation());
            this.pages.add(lastPageTables);
        }
        
    }
    
    private ArrayList getTacTables(ArrayList subtests) throws DocumentException{
        ArrayList result = new ArrayList();
        result.add(getTestAccessCodesTitle());
        result.add(getTestAccessCodeInformation());
        result.add(getTestAccessCodeTable(subtests));
        return result;        
    }
    
    private boolean canAddSummary(float pageEnd){
        return pageEnd - STUDENT_SUMMARY_HEIGHT >= PAGE_MIN_Y;
    }
    private float getPageStart(){
        return this.dynamicPageStart - SUBTEST_HEADER_HEIGHT;
    }
    
    private float getNewPageEnd(SubtestVO subtest, float pageEnd) throws DocumentException{
        String subtestName = subtest.getSubtestName();
        String tac = subtest.getTestAccessCode();

        float stnHeight = tableUtils.getInfoHeight(subtestName, SUBTEST_NAME_WIDTH);
        float tacHeight = tableUtils.getInfoHeight(tac, TAC_NAME_WIDTH);
        float maxHeight = (stnHeight > tacHeight) ? stnHeight : tacHeight;
        pageEnd -= maxHeight;
        return pageEnd;
    }

    private void createRosterPages() throws DocumentException{
        ArrayList pageStudents = new ArrayList();
        int lines = 0;
        for(Iterator it = this.rosterList.iterator(); it.hasNext();){
            TestRosterVO student = (TestRosterVO)it.next();
            int currentStudentLines = student.getAccommodations().length;
            if((lines + currentStudentLines) < this.maxStudentLines){
                lines += currentStudentLines;
            }
            else{
                this.pages.add(getStudentTable(pageStudents));
                pageStudents = new ArrayList();
                lines = currentStudentLines;
            }
            pageStudents.add(student);
        }
        this.pages.add(getStudentTable(pageStudents));
    }
    
    private void addTitle() throws DocumentException{
        addTitleText();
        addTitleLine();
    }
    
    private void addTitleText() throws DocumentException{
        addTitleLabel();
        addTitleValue();
    }
    
    private void addTitleLabel() throws DocumentException {
        this.staticTables.add( 
            tableUtils.getTitleTable(PAGE_NAME_LABEL,
                                     TITLE_LABEL_WIDTH,
                                     LEFT_X,
                                     TITLE_Y));
    }
    private void addTitleValue() throws DocumentException{
        this.staticTables.add( 
            tableUtils.getTitleTable(getTitleText(),
                                     TITLE_VALUE_WIDTH,
                                     TITLE_VALUE_X,
                                     TITLE_Y));
    }
    private String getTitleText(){
        return getNonBlankString(testAdmin.getSessionName());
    }
    private void addTitleLine() throws DocumentException{
        this.staticTables.add( 
            tableUtils.getLineTable(LINE_WIDTH,
                                    LEFT_X,
                                    lineY));
    }
    
    private void addFooter() throws DocumentException{
        this.staticTables.add( 
            tableUtils.getCopywriteTable(FOOTER_WIDTH,
                                         LEFT_X,
                                         FOOTER_Y));
     }
    
    private void addWatermark() throws DocumentException{
        this.staticTables.add( 
            tableUtils.getWatermarkTable(WATERMARK_TEXT,
                                         WATERMARK_WIDTH,
                                         WATERMARK_X,
                                         WATERMARK_Y));
     }
    
    private String getLocation(){
        return tableUtils.infoEllipsis(getNonBlankString(testAdmin.getLocation()), SESSION_VALUE_WIDTH);
    }
     
     private String getStudentId(TestRosterVO student){
        return getNonBlankString(student.getStudentNumber());
    }

   private String getPassword(TestRosterVO student){
        return getNonBlankString(student.getPassword());
   }   
   private String getLoginId(TestRosterVO student){
        return getNonBlankString(student.getLoginName());
   }   
   private String getTestName(){
        return getNonBlankString(testAdmin.getTestName());
   }   
   private String getSupportContact(){
        return testProduct.getSupportPhoneNumber();
   } 
   private String getLevel(){
        return getNonBlankString(test.getLevel());
   }   
    private String getStartDate(){
        return DateUtils.formatDateToReportDateString(testAdmin.getStartDate());
    }   
    private String getEndDate(){
        return DateUtils.formatDateToReportDateString(testAdmin.getEndDate());
    }   
    private String getLoginWindow(){
        String startTime = DateUtils.formatDateToTimeString(testAdmin.getStartTime());
        String endTime = DateUtils.formatDateToTimeString(testAdmin.getEndTime());
        
        return startTime + " - " + endTime;
    } 
    
    private String getTimeZone(){
        return DateUtils.getUITimeZone(testAdmin.getTimeZone());
    }   
    
    private String getTimeLimit(){
        StringBuffer result = new StringBuffer();
        String duration = test.getDuration();
        if(duration.indexOf("hour") == -1 && duration.indexOf(" minutes") != -1){
            Integer dur = new Integer(duration.substring(0, duration.indexOf(" minutes")));
            int time = dur.intValue();
            int hours = time/60;
            int minutes = time%60;
            if (hours > 1){
                result.append(String.valueOf(hours));
                result.append(" hours and ");
            }
            else if(hours == 1){
                result.append(String.valueOf(hours));
                result.append(" hour and ");
            }
            result.append(String.valueOf(minutes));
            result.append(" minutes");
        }
        else{
            result.append(duration);
        }
        return result.toString();
    }   
    
    private String getAllowEnforceBreaks(){
        String allowEnforceBreaks = testAdmin.getEnforceBreaks();
        return "T".equals(allowEnforceBreaks) ? "Yes" : "No";
    }   
  
 
    private String[] getSession1Texts(){
        String[] result = new String[8];
        
        result[0] = START_DATE_LABEL;
        result[1] = getStartDate();
        result[2] = END_DATE_LABEL;
        result[3] = getEndDate();
        result[4] = LOGIN_WINDOW_LABEL;
        result[5] = getLoginWindow();
        result[6] = TIME_ZONE_LABEL;
        result[7] = getTimeZone();
        
        return result;
    }
    private String[] getSession2Texts(){
        String[] result = new String[8];
        boolean singleSubtest = this.testAdmin.getSubtests().size() == 1;
        result[0] = TIME_LIMIT_LABEL;
        result[1] = getTimeLimit();
        result[2] = singleSubtest ? " " : ALLOW_ENFORCE_BREAKS_LABEL;
        result[3] = singleSubtest ? " " : getAllowEnforceBreaks();
        result[4] = LOCATION_LABEL;
        result[5] = getLocation();
        result[6] = " ";
        result[7] = " ";
        
        return result;
    }
} 
