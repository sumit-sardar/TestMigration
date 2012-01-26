package util; 

import com.ctb.bean.testAdmin.TestProduct;
import com.lowagie.text.Chunk;
import data.TestRosterVO;
import data.TestAdminVO;
import java.io.IOException;
import java.io.OutputStream;
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
import java.util.LinkedHashMap;
import java.util.TreeMap;

import javax.servlet.ServletOutputStream;
//import weblogic.webservice.tools.pagegen.result;

import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

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
    private TreeMap<Integer,String> rosterHeaderMap ;
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
             tableUtils.getSummaryTable(getSession2Texts(true),
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
    private String[] getSession2Texts(boolean isForpdf){
        String[] result = new String[8];
        boolean singleSubtest = this.testAdmin.getSubtests().size() == 1;
        result[0] = TIME_LIMIT_LABEL;
        result[1] = getTimeLimit();
        result[2] = singleSubtest ? " " : ALLOW_ENFORCE_BREAKS_LABEL;
        result[3] = singleSubtest ? " " : getAllowEnforceBreaks();
        result[4] = LOCATION_LABEL;
        result[5] = isForpdf? getLocation():getNonBlankString(testAdmin.getLocation());
        result[6] = " ";
        result[7] = " ";
        
        return result;
    }

   /* private void appendTitleNameValueWithSameColor(Sheet summarySheet, String title, String titleText, int rowno,short textSize, Boolean isBold) {
		Row header = PoiUtils.getRow(summarySheet, rowno);

		CellStyle titleStyle = summarySheet.getWorkbook().createCellStyle();
		org.apache.poi.ss.usermodel.Font boldFont = PoiUtils.getFont(
				summarySheet.getWorkbook(), true, HSSFColor.TEAL.index,
				 textSize);
		titleStyle.setFont(boldFont);
		
		PoiUtils.addCell(header, 0, title, titleStyle);
		PoiUtils.addCell(header, 1, titleText, titleStyle);
	}*/
    
    
    
    private void appendSupportContact(Sheet summarySheet, int rowno) {
    	Row row = null;

		CellStyle c0s = null;
		c0s = summarySheet.getWorkbook().createCellStyle();
		org.apache.poi.ss.usermodel.Font normalFont = PoiUtils.getFont(summarySheet.getWorkbook(), true, HSSFColor.TEAL.index, (short)0);
		
		
		row = PoiUtils.getRow(summarySheet, rowno);
		c0s.setFont(normalFont);
		PoiUtils.addCell(row, 0, SUPPORT_CONTACT_LABEL + getSupportContact(), c0s);
    	
    }
    
    private void appendLevel(Sheet summarySheet, int rowno) {
    	Row row = null;
		CellStyle c0s = null;
		org.apache.poi.ss.usermodel.Font normalFont = PoiUtils.getFont(summarySheet.getWorkbook(), true, HSSFColor.TEAL.index, (short)0);
		org.apache.poi.ss.usermodel.Font normalBlackFont = PoiUtils.getFont(summarySheet.getWorkbook(), true, HSSFColor.BLACK.index, (short)0);
		
		row = PoiUtils.getRow(summarySheet, rowno);
		c0s = summarySheet.getWorkbook().createCellStyle();
		c0s.setFont(normalFont);
		PoiUtils.addCell(row, 0, getLevelLabel(), c0s);
		c0s = summarySheet.getWorkbook().createCellStyle();
		c0s.setFont(normalBlackFont);

		PoiUtils.addCell(row, 1, getLevel(), c0s);

	}
    
    private int appendSessionInfo(Sheet summarySheet, int rowno) {
		Row row = null;
		Cell cell0 = null;
		Cell cell1 = null;
		Cell cell2 = null;
		Cell cell3 = null;
		Cell cell4 = null;
		org.apache.poi.ss.usermodel.Font normalFont = PoiUtils.getFont(	summarySheet.getWorkbook(), true, HSSFColor.TEAL.index,	(short) 0);

		CellStyle tableTopLeftCorner = summarySheet.getWorkbook().createCellStyle();
		tableTopLeftCorner.setBorderLeft(CellStyle.BORDER_MEDIUM);
		tableTopLeftCorner.setBorderRight(CellStyle.BORDER_MEDIUM);
		tableTopLeftCorner.setBorderTop(CellStyle.BORDER_MEDIUM);
		tableTopLeftCorner.setFont(normalFont);

		CellStyle tableTopRightCorner = summarySheet.getWorkbook().createCellStyle();
		tableTopRightCorner.setBorderTop(CellStyle.BORDER_MEDIUM);
		tableTopRightCorner.setBorderRight(CellStyle.BORDER_MEDIUM);

		CellStyle tableBottomLeftCorner = summarySheet.getWorkbook().createCellStyle();
		tableBottomLeftCorner.setBorderLeft(CellStyle.BORDER_MEDIUM);
		tableBottomLeftCorner.setBorderBottom(CellStyle.BORDER_MEDIUM);
		tableBottomLeftCorner.setBorderRight(CellStyle.BORDER_MEDIUM);
		tableBottomLeftCorner.setFont(normalFont);

		CellStyle tableBottomRightCorner = summarySheet.getWorkbook().createCellStyle();
		tableBottomRightCorner.setBorderBottom(CellStyle.BORDER_MEDIUM);
		tableBottomRightCorner.setBorderRight(CellStyle.BORDER_MEDIUM);

		CellStyle tableLeft = summarySheet.getWorkbook().createCellStyle();
		tableLeft.setBorderLeft(CellStyle.BORDER_MEDIUM);
		tableLeft.setBorderRight(CellStyle.BORDER_MEDIUM);
		tableLeft.setFont(normalFont);

		CellStyle tableRight = summarySheet.getWorkbook().createCellStyle();
		tableRight.setBorderRight(CellStyle.BORDER_MEDIUM);
		++rowno;

		String[] session1Texts = getSession1Texts();
		String[] session2Texts = getSession2Texts(false);
		int noOfrow = session1Texts.length / 2;

		for (int i = 0; i < noOfrow; i++) {
			row = PoiUtils.getRow(summarySheet, rowno + i);
			cell0 = row.createCell(0);
			cell1 = row.createCell(1);
			cell2 = row.createCell(2);
			cell3 = row.createCell(3);
			cell4 = row.createCell(4);

			String valAtCell0 = session1Texts[i * 2];
			String valAtCell1 = session1Texts[i * 2 + 1];
			String valAtCell2 = " ";
			String valAtCell3 = session2Texts[i * 2];
			
			String valAtCell4 = session2Texts[i * 2 + 1];

			if (i == 0) {
				cell0.setCellStyle(tableTopLeftCorner);
				cell1.setCellStyle(tableTopRightCorner);

				cell3.setCellStyle(tableTopLeftCorner);
				cell4.setCellStyle(tableTopRightCorner);
			} else if (noOfrow - 1 == i) {
				cell0.setCellStyle(tableBottomLeftCorner);
				cell1.setCellStyle(tableBottomRightCorner);

				cell3.setCellStyle(tableBottomLeftCorner);
				cell4.setCellStyle(tableBottomRightCorner);

			} else {
				cell0.setCellStyle(tableLeft);
				cell1.setCellStyle(tableRight);

				cell3.setCellStyle(tableLeft);
				cell4.setCellStyle(tableRight);
			}

			cell0.setCellValue(valAtCell0);
			cell1.setCellValue(valAtCell1);
			cell2.setCellValue(valAtCell2);
			cell3.setCellValue(valAtCell3);
			cell4.setCellValue(valAtCell4);

		}

		return rowno + noOfrow;
	}
    
    
    
    
    protected void setupForExcel(Object[] args) {

        this.rosterList = (Collection)args[0];
        this.testAdmin = (TestAdminVO)args[1];
        this.testSummary = (TestSummaryVO)args[3];
        this.test = (TestVO)args[4];
        this.isTabeProduct = (Boolean)args[9];
        this.testProduct = (TestProduct)args[10];
        this.isStudentIdConfigurable = (Boolean)args[11];
        this.studentIdLabelName = (String)args[12];
        populateRosterHeader();
    }
    
    public void prepareSummarySheet(Sheet summarySheet) throws IOException {
    	int rowno = 0;
    	PoiUtils.appendTitleNameValueWithSameColor(summarySheet,PAGE_NAME_LABEL,getTitleText(), rowno++, (short)0, true);
    	
    	summarySheet.addMergedRegion(new CellRangeAddress(rowno-1,rowno-1, (short)1,(short)5));
    	
    	PoiUtils.addThckBoarderInaSheet(summarySheet, rowno++, 0, 5, HSSFColor.TEAL.index);
    	rowno++;
    	PoiUtils.appendTitleNameValueWithDiffColor(summarySheet,TEST_NAME_LABEL, getTestName(), rowno++, (short)0, true);
    	appendSupportContact(summarySheet,rowno++ );
    	 
    	if (! this.isTabeProduct.booleanValue()) { 
    		appendLevel(summarySheet, rowno++);
        }
    	
    	rowno = appendSessionInfo(summarySheet,rowno );
    	//rowno ++;
    	
    	rowno = createTacRecords(summarySheet, rowno);
    	rowno ++;
    	createStudentSummaryInformation(summarySheet, rowno);
    	// setting column auto size
    	for( int i =0; i<5;i++) {
    		summarySheet.autoSizeColumn(i);
    	}
    	summarySheet.setColumnWidth(5, summarySheet.getColumnWidth(3));
    	
    }
    
  
	private void createStudentSummaryInformation(Sheet summarySheet, int rowno) {
		PoiUtils.appendTitleNameValueWithDiffColor(summarySheet,TOTAL_STUDENTS_LABEL , getTotalStudents(), ++rowno, (short)0, true); 
		String[] accomodation = null;
		Row row = null;

		CellStyle nameStyle = summarySheet.getWorkbook().createCellStyle();
		CellStyle valueStyle = summarySheet.getWorkbook().createCellStyle();
		org.apache.poi.ss.usermodel.Font boldFont = PoiUtils.getFont(summarySheet.getWorkbook(), true, HSSFColor.TEAL.index,(short) 10);
		org.apache.poi.ss.usermodel.Font boldFont1 = PoiUtils.getFont(summarySheet.getWorkbook(), true, HSSFColor.BLACK.index,(short) 10);
		
		nameStyle.setFont(boldFont);
		valueStyle.setFont(boldFont1);
		
		 Boolean supportAccommodations = this.testSummary.getSupportAccommodations();
        if ( supportAccommodations.booleanValue() ) {
        	PoiUtils.appendTitleNameValueWithDiffColor(summarySheet,TOTAL_STUDENTS_WITH_ACCOMMODATIONS_LABEL , getAccommodatedStudents(), ++rowno, (short) 0, true); 
        	if(hasAccommodatedStudents()){
        		accomodation = getAllAccomodation();
        		++rowno;
        		for(int i=0, columnCount = 0; i<accomodation.length; i+=2 ){
        			if(columnCount%6 == 0) {
        				row = PoiUtils.getRow(summarySheet, ++rowno);
        				 columnCount = 0;
        			}
        			
        			PoiUtils.addCell(row, columnCount++ , accomodation[i], nameStyle);
        			PoiUtils.addCell(row, columnCount++ , accomodation[i+1], valueStyle);
        			
        		}
        		
        	}
        	
        }
		
	}

	private String[] getAllAccomodation() {
		List<String> result = new ArrayList<String>();
		result.add(CALCULATOR_LABEL);
        result.add(getCalculator());
        result.add(SCREEN_READER_LABEL);
        result.add(getScreenReader());
        result.add(COLOR_FONT_LABEL);
        result.add(getColorFont());
        result.add(PAUSE_LABEL);
        result.add(getPause());
        result.add(UNTIMED_LABEL);
        result.add(getUntimed());
        result.add(HIGHLIGHTER_LABEL);  /* 51931 Deferred Defect For HighLighter*/
        result.add(getHighLighter());
        
        // Start: For MQC defect 66844
        result.add(MASKING_RULAR_LABEL);
        result.add(getMaskingRular());
        result.add(MASKING_TOOL_LABEL);
        result.add(getMaskingTool());
        result.add(MAGNIFYING_GLASS_LABEL);
        result.add(getMagniFyingGlass());
        result.add(MUSIC_PLAYER_LABEL);
        result.add(getMusicPlayer());
        result.add(EXTENDED_TIME_LABEL);
        result.add(getExtendedTime());
        
        
		return result.toArray( new String[result.size()] );
	}

	@SuppressWarnings("all")
	private int createTacRecords(Sheet summarySheet, int rowno) {
		ArrayList subtest = (ArrayList) this.testAdmin.getSubtests();
		int tacColumnLenth = subtest.size()>1 ? 4 : 3;
		
		// added TAC TITLE
		PoiUtils.appendTitleNameValueWithSameColor(summarySheet,TAC_LABEL , "", ++rowno, (short)12, true);
		
		// added TAC INFO
		Row header = PoiUtils.getRow(summarySheet, ++rowno);

		CellStyle titleStyle = summarySheet.getWorkbook().createCellStyle();
		org.apache.poi.ss.usermodel.Font boldFont = PoiUtils.getFont(summarySheet.getWorkbook(), true, HSSFColor.BLACK.index,(short) 0);
		titleStyle.setFont(boldFont);

		
		PoiUtils.addCell(header, 0 , TAC_INFO, titleStyle);
		
		summarySheet.addMergedRegion(new CellRangeAddress(rowno,rowno, (short)0,(short)5));
		++rowno;
		
		// added TAC Table
	
		String[] tacTableValues = getTacTableValues(subtest);

		Row row = null;
		Cell cell0 = null;

		// Header
		CellStyle tableHeaderStyle = summarySheet.getWorkbook()
				.createCellStyle();
		org.apache.poi.ss.usermodel.Font boldTableHeaderFont = PoiUtils.getFont(summarySheet.getWorkbook(), true, HSSFColor.TEAL.index,	(short) 10);
		tableHeaderStyle.setBorderLeft(CellStyle.BORDER_MEDIUM);
		tableHeaderStyle.setBorderRight(CellStyle.BORDER_MEDIUM);
		tableHeaderStyle.setBorderTop(CellStyle.BORDER_MEDIUM);
		tableHeaderStyle.setBorderBottom(CellStyle.BORDER_MEDIUM);
		tableHeaderStyle.setFont(boldTableHeaderFont);
		tableHeaderStyle.setFillPattern((short) CellStyle.SOLID_FOREGROUND);
		tableHeaderStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		
		CellStyle cellStyle = summarySheet.getWorkbook()
		.createCellStyle();
		cellStyle.setBorderLeft(CellStyle.BORDER_MEDIUM);
		cellStyle.setBorderRight(CellStyle.BORDER_MEDIUM);
		cellStyle.setBorderTop(CellStyle.BORDER_MEDIUM);
		cellStyle.setBorderBottom(CellStyle.BORDER_MEDIUM);

		row = summarySheet.createRow(++rowno);
		
		for (int i = 0 ; i < tacColumnLenth; i++) {
			PoiUtils.addCell(row, i , tacTableValues[i], tableHeaderStyle);
		}
		
		for (int i = tacColumnLenth ;  i< tacTableValues.length;  i=i+tacColumnLenth ){
			row = summarySheet.createRow(++rowno);
			for (int j = 0 ; j < tacColumnLenth; j++) {
				PoiUtils.addCell(row, j , tacTableValues[i+j], cellStyle);
			}
			
		}
		
	return rowno;	
	}

	
	@SuppressWarnings("all")
	private String[] getTacTableValues(ArrayList subtests) {
		String[] val = null;
		if(subtests.size() == 1){
			val = getThreeColumnTacTexts(subtests);
        }
        else{
        	val = getFourColumnTacTexts(subtests);
        }
		return val;
	}

	public void prepareRosterSheet(Sheet rosterSheet) throws IOException {
		int rowno = 0;
		String[] headings = getStudentHeadings();
		Row row = null;
		TableUtils tableUtils = new TableUtils();
		// Header
		CellStyle tableHeaderStyle = rosterSheet.getWorkbook()
				.createCellStyle();
		org.apache.poi.ss.usermodel.Font boldTableHeaderFont = PoiUtils.getFont(rosterSheet.getWorkbook(), true, HSSFColor.TEAL.index,(short) 10);
		tableHeaderStyle.setBorderLeft(CellStyle.BORDER_MEDIUM);
		tableHeaderStyle.setBorderRight(CellStyle.BORDER_MEDIUM);
		tableHeaderStyle.setBorderTop(CellStyle.BORDER_MEDIUM);
		tableHeaderStyle.setBorderBottom(CellStyle.BORDER_MEDIUM);
		tableHeaderStyle.setFont(boldTableHeaderFont);
		tableHeaderStyle.setFillPattern((short) CellStyle.SOLID_FOREGROUND);
		tableHeaderStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		
		CellStyle shededCellStyle = rosterSheet.getWorkbook()
		.createCellStyle();
		shededCellStyle.setFillPattern((short) CellStyle.SOLID_FOREGROUND);
		shededCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		

		row = rosterSheet.createRow(rowno++);
		int cellCount = 0;
		for (int i = 0; i < headings.length-1; i++) {
			PoiUtils.addCell(row, cellCount++ , headings[i], tableHeaderStyle);
		}
		for(String header : rosterHeaderMap.values()){
			PoiUtils.addCell(row, cellCount++ , header, tableHeaderStyle);
		}
        boolean isShaded = false;
		for (Iterator it = this.rosterList.iterator(); it.hasNext(); isShaded=!isShaded) {
			TestRosterVO student = (TestRosterVO) it.next();
			String studentName = tableUtils.getStudentName(student);
			String studentId = getStudentId(student);
			String loginId = getLoginId(student);
			String password = getPassword(student);
			String form = tableUtils.getForm(student);
			String status = tableUtils.getStatus(student);
			
			
			String[] accommodation = student.getAccommodations();
			if(accommodation.length==0){
				accommodation = new String []{" "};
			}
			// added
			int cellno = 0;
			row = rosterSheet.createRow(rowno++);
			PoiUtils.addCell(row,cellno++, studentName);
			PoiUtils.addCell(row,cellno++, studentId);
			PoiUtils.addCell(row,cellno++, loginId);
			PoiUtils.addCell(row,cellno++, password);
			if (!isTabeProduct){
				PoiUtils.addCell(row,cellno++, form);
			}
			PoiUtils.addCell(row,cellno++, status);
			
			for(String header : rosterHeaderMap.values()){
				String val = "No";
				if(student.getAccommodationsSet().contains(header)) {
					val = "Yes";
				}
				PoiUtils.addCell(row,cellno++, val);
			}
			// setting default column width
			for(int i=0; i<cellno; i++){
				rosterSheet.autoSizeColumn(i);

			}

		}

	}
	
	public void generateExcelReport(Object[] args)  {
	        try{
	        	setupForExcel(args);
	        	OutputStream out= (OutputStream) args[5];
	        	HSSFWorkbook wb = new HSSFWorkbook();
	    		Sheet summarySheet = wb.createSheet();
	    		Sheet rosterSheet = wb.createSheet();
	    		wb.setSheetName(0, TestTicketConstents.EXCEL_TEST_TICKET_SUMMARY_SHEET_NAME);
	    		wb.setSheetName(1, TestTicketConstents.EXCEL_TEST_TICKET_STUDENT_DETAIL_SHEET_NAME);

	    		prepareSummarySheet(summarySheet);

	    		prepareRosterSheet(rosterSheet);

	    		HSSFPalette palette =  wb.getCustomPalette();

			    //replacing the standard red with freebsd.org red
			    palette.setColorAtIndex(HSSFColor.TEAL.index,
			            (byte) 70,  //RGB red (0-255)
			            (byte) 130,    //RGB green
			            (byte) 180     //RGB blue
			    );

	    		wb.write(out);
	    		//out.close();
	         }
	        catch(Exception de){
	            System.err.println("document: " + de.getMessage());
	            de.printStackTrace();
	        }
	    }
	
	private void populateRosterHeader() {
		rosterHeaderMap = new TreeMap<Integer, String>();
		rosterHeaderMap.put(1, TestTicketConstents.TEST_TICKET_ACCOM_CALCULATOR);
		rosterHeaderMap.put(2, TestTicketConstents.TEST_TICKET_ACCOM_PAUSE);
		rosterHeaderMap.put(3,  TestTicketConstents.TEST_TICKET_ACCOM_UNTIMED);
		rosterHeaderMap.put(4, TestTicketConstents.TEST_TICKET_ACCOM_COLOR_FONT);
		rosterHeaderMap.put(5, TestTicketConstents.TEST_TICKET_ACCOM_SCREEN_READER);
		rosterHeaderMap.put(6, TestTicketConstents.TEST_TICKET_ACCOM_HIGHLIGHTER);
		rosterHeaderMap.put(7, TestTicketConstents.TEST_TICKET_ACCOM_EXTENDED_TIME);
		rosterHeaderMap.put(8, TestTicketConstents.TEST_TICKET_ACCOM_BLOCKING_RULER);
		rosterHeaderMap.put(9, TestTicketConstents.TEST_TICKET_ACCOM_MASKING_TOOL);
		rosterHeaderMap.put(10, TestTicketConstents.TEST_TICKET_ACCOM_MAGNIFYING_GLASS);
		rosterHeaderMap.put(11, TestTicketConstents.TEST_TICKET_ACCOM_MUSIC_PLAYER);
			
	}
    
} 
