package com.ctb.testSessionInfo.utils; 

import com.ctb.testSessionInfo.dto.ProgramStatusReportVO;
import com.ctb.testSessionInfo.dto.TestStatusVO;
import java.io.OutputStream;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TimeZone;
import org.apache.struts.action.ActionError;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

public class ProgramStatusReportUtils {
	private OutputStream out;
	private ProgramStatusReportVO vo;
	private HSSFWorkbook hssfWorkbook;
	private HSSFSheet sheet;
    private HSSFCellStyle boldStyle;
    private HSSFCellStyle normalStyle;

	private static final String SHEET_NAME = "Program Status Report";
    private static final String PROGRAM_STATUS = "Program Status";
    private static final String CUSTOMER = "Customer";
    private static final String PROGRAM = "Program";
    private static final String ORGANIZATION = "Organization";
    private static final String TEST = "Test";
    private static final String SUBTEST = "Subtest";
    private static final String SUBTEST_STATUS = "Subtest Status";
	private static final String SESSION_NAME = "Session Name";
	private static final String SESSION_NUMBER = "Session ID";
	private static final String LOGIN_ID = "Login ID";
	private static final String PASSWORD = "Password";
	private static final String ACCESS_CODE = "Access Code";

	private static final short SESSION_NAME_COLUMN = (short)0;
	private static final short SESSION_NAME_COLUMN_WIDTH = (short)5550;
	private static final short SESSION_NUMBER_COLUMN = (short)1;
	private static final short SESSION_NUMBER_COLUMN_WIDTH = (short)5550;
	private static final short LOGIN_ID_COLUMN = (short)2;
	private static final short LOGIN_ID_COLUMN_WIDTH = (short)5550;
	private static final short PASSWORD_COLUMN = (short)3;
	private static final short PASSWORD_COLUMN_WIDTH = (short)5550;
	private static final short ACCESS_CODE_COLUMN = (short)4;
	private static final short ACCESS_CODE_COLUMN_WIDTH = (short)5550;
	
	
	private static final int FILTER_ROW = 0;
	private static final int STUDENT_HEADER_ROW = 13;
	private static final int STUDENT_DATA_ROW = 14;
	
	public ProgramStatusReportUtils(ProgramStatusReportVO vo, OutputStream out) {
		this.vo = vo;
		this.out = out;
	}

	public boolean generateReport() throws Exception {
		try {
            this.initialize();
			this.createFilterRows();
			this.createStudentHeaderRow();
			this.createStudentRows();
			this.hssfWorkbook.write(out);
			this.out.close();

		} catch (Exception exException) {
			exException.printStackTrace();
			throw exException;
		}
		return true;
	}

    private void initialize(){
        this.hssfWorkbook = new HSSFWorkbook();
		this.sheet = hssfWorkbook.createSheet(SHEET_NAME);
		sheet.setColumnWidth(SESSION_NAME_COLUMN, SESSION_NAME_COLUMN_WIDTH);
		sheet.setColumnWidth(SESSION_NUMBER_COLUMN, SESSION_NUMBER_COLUMN_WIDTH);
		sheet.setColumnWidth(LOGIN_ID_COLUMN, LOGIN_ID_COLUMN_WIDTH);
		sheet.setColumnWidth(PASSWORD_COLUMN, PASSWORD_COLUMN_WIDTH);
		sheet.setColumnWidth(ACCESS_CODE_COLUMN, ACCESS_CODE_COLUMN_WIDTH);
        this.boldStyle = hssfWorkbook.createCellStyle();
        this.normalStyle = hssfWorkbook.createCellStyle();
        this.boldStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        this.normalStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        this.boldStyle.setFillForegroundColor(HSSFColor.LIGHT_BLUE.index);
        HSSFFont boldFont = hssfWorkbook.createFont();
        boldFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        boldFont.setItalic(false);
        this.boldStyle.setFont(boldFont);
    }
    
	private void createFilterRows(){
		int row = FILTER_ROW;
		HSSFRow hssfRow = sheet.createRow(row++);
        addCell(hssfRow, (short)0, boldStyle, PROGRAM_STATUS);
        this.createFilterRow(row++, CUSTOMER, vo.getCustomer());
        this.createFilterRow(row++, PROGRAM, vo.getProgram());
        this.createFilterRow(row++, ORGANIZATION, vo.getOrganization());
        this.createFilterRow(row++, TEST, vo.getTest());
        this.createFilterRow(row++, SUBTEST, vo.getSubtest());
        this.createFilterRow(row++, SUBTEST_STATUS, vo.getSubtestStatus());
//        this.createFilterRow(row++, SESSION_NAME, vo.getSessionNameFilter(), vo.getSessionNameValue());
//        this.createFilterRow(row++, SESSION_NUMBER, vo.getSessionNumberFilter(), vo.getSessionNumberValue());
//        this.createFilterRow(row++, LOGIN_ID, vo.getLoginIdFilter(), vo.getLoginIdValue());
//        this.createFilterRow(row++, PASSWORD, vo.getPasswordFilter(), vo.getPasswordValue());
//        this.createFilterRow(row++, ACCESS_CODE, vo.getAccessCodeFilter(), vo.getAccessCodeValue());
	}
	
    private void createFilterRow(int rowNumber,
                                 String key,
                                 String value){
        HSSFRow hssfRow = sheet.createRow(rowNumber);
        addCell(hssfRow, (short)0, boldStyle, key + ":");
        addCell(hssfRow, (short)1, normalStyle, value);
    }
    
    private void createFilterRow(int rowNumber, 
                                 String key,
                                 String filter, 
                                 String value){
        HSSFRow hssfRow = sheet.createRow(rowNumber);
        addCell(hssfRow, (short)0, boldStyle, key + ":");
        addCell(hssfRow, (short)1, normalStyle, filter + " \"" + value + "\"");
    }
	
    private void createStudentHeaderRow(){
		int row = STUDENT_HEADER_ROW;
		HSSFRow hssfRow = sheet.createRow(row);
		addCell(hssfRow, SESSION_NAME_COLUMN, boldStyle, SESSION_NAME);
		addCell(hssfRow, SESSION_NUMBER_COLUMN, boldStyle, SESSION_NUMBER);
		addCell(hssfRow, LOGIN_ID_COLUMN, boldStyle, LOGIN_ID);
		addCell(hssfRow, PASSWORD_COLUMN, boldStyle, PASSWORD);
		addCell(hssfRow, ACCESS_CODE_COLUMN, boldStyle, ACCESS_CODE);
    }
	
	private void createStudentRows(){
		List subtestStatusList = vo.getSubtestStatusList();
		HSSFRow hssfRow;
		int row = STUDENT_DATA_ROW;
        Iterator it = subtestStatusList.iterator();
		while(it.hasNext()){
            TestStatusVO testStatus = (TestStatusVO)it.next();
			hssfRow = sheet.createRow(row++);
			addCell(hssfRow, SESSION_NAME_COLUMN, normalStyle, testStatus.getSessionName());
			addCell(hssfRow, SESSION_NUMBER_COLUMN, normalStyle, testStatus.getSessionNumber());
			addCell(hssfRow, LOGIN_ID_COLUMN, normalStyle, testStatus.getLoginId());
			addCell(hssfRow, PASSWORD_COLUMN, normalStyle, testStatus.getPassword());
			addCell(hssfRow, ACCESS_CODE_COLUMN, normalStyle, testStatus.getAccessCode());
		}
	}
    
    private void addCell(HSSFRow hssfRow, short col, HSSFCellStyle style, String text){
        HSSFCell hssfCell = hssfRow.createCell(col);
        hssfCell.setCellStyle(style);
        hssfCell.setCellValue(new HSSFRichTextString(text));
    }
}
