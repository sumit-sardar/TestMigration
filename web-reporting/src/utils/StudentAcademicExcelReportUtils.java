package utils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.TreeMap;

import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.ctb.bean.studentManagement.StudentScoreReport;
import com.ctb.bean.testAdmin.StudentReportIrsScore;
import com.itextpdf.text.DocumentException;

public class StudentAcademicExcelReportUtils extends StudentImmediateReportUtils implements ImmediateReport{
	
	StudentScoreReport stuReport = null;
	String testAdminStartDateString = null;
	 private TreeMap<Integer,String> studentHeaderMap;
	
	public void generateExcelReport(Object[] args)  {
        try{
        	setupForExcel(args);
        	OutputStream out= (OutputStream) args[4];
        	HSSFWorkbook wb = new HSSFWorkbook();
    		Sheet reportSheet = wb.createSheet();
    		wb.setSheetName(0, (String)args[0]);

    		prepareReportSheet(reportSheet);

    		HSSFPalette palette =  wb.getCustomPalette();

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

	private void prepareReportSheet(Sheet reportSheet) throws IOException {
		int rowno = 0;
		String[] headings;
		headings = populateStudentHeader();
		Row row = null;
		row = reportSheet.createRow(rowno++);
		int cellCount = 0;
		for (int i = 0; i < headings.length; i++) {
			addCell(row, cellCount++ , headings[i]);
		}
		int cellno = 0;
		row = reportSheet.createRow(rowno++);
		
		addCell(row, cellno++ , getStudentName());
		addCell(row, cellno++ , getStudentExtPin());
		addCell(row, cellno++ , getTestAdminStartDateString());
		addCell(row, cellno++ , getFormRe());
		addCell(row, cellno++ , getDistrict());
		addCell(row, cellno++ , getSchool());
		addCell(row, cellno++ , getGrade());
		addCell(row, cellno++ , getTestName());
		reportSheet.addMergedRegion(new CellRangeAddress(rowno-1,rowno-1, (short)cellno-1,(short)cellno+3));
		/*for(int i=0; i<cellno; i++){
			reportSheet.autoSizeColumn(i);
		}*/
		
		row = reportSheet.createRow(rowno++);
		row = reportSheet.createRow(rowno++);
		row = reportSheet.createRow(rowno++);
		addCell(row, 1 , ACADEMIC_LANGUAGE_REPORT);
		reportSheet.addMergedRegion(new CellRangeAddress(rowno-1,rowno-1, (short)1,(short)3));
		row = reportSheet.createRow(rowno++);
		row = reportSheet.createRow(rowno++);
		
		appendContentAreaNames(reportSheet,rowno++, (short)0, false);
		appendScoresTitle(reportSheet,rowno++, (short)0, false);
		
		for(int i=0; i<getIrsScores().length; i++){
			if(getIrsScores()[i] != null && !getIrsScores()[i].getContentAreaName().contains("Total Score")){
				appendObjectiveScores(reportSheet,getIrsScores()[i], rowno++, (short)0, false);
			}
		}
		for(int i=0; i<getIrsScores().length; i++){
			if(getIrsScores()[i] != null && getIrsScores()[i].getContentAreaName().contains("Total Score")){
				appendTotalScore(reportSheet, getIrsScores()[i], rowno++, (short)0, false);
			}
		}
		row = reportSheet.createRow(rowno++);
		row = reportSheet.createRow(rowno++);
		reportSheet.addMergedRegion(new CellRangeAddress(rowno-1,rowno-1, (short)1,(short)13));
		addCell(row, 1 , ACADEMICTOTALSCORECONDITION);

		//addThckBoarderInaSheet(reportSheet, rowno++, 0, 5, HSSFColor.TEAL.index);
		
	}

	private void setupForExcel(Object[] args) {
		this.stuReport = (StudentScoreReport)args[1];
		this.testAdminStartDateString = (String)args[2];
		super.setupCSV((PrintWriter)args[3], stuReport, testAdminStartDateString);
		
	}

	@Override
	void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	void generateReport() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	void initialize() throws DocumentException {
		// TODO Auto-generated method stub
		
	}
	
	private static Row getRow(Sheet sheet, int rowno) {
		return sheet.createRow(rowno);

	}
	private static Cell addCell(Row row, int cellNo, String cellValue) {
		Cell cell0 = row.createCell(cellNo);
		cell0.setCellValue(cellValue);
		return cell0;
		
	}
	private static Cell addCell(Row row, int cellNo, String cellValue, CellStyle cs) {
		Cell cell0 = addCell( row,  cellNo,  cellValue);
		cell0.setCellStyle(cs);
		return cell0;
	}
	
	private static Font getFont(Workbook workbook, boolean isBold, short color,
			short testSize) {
		Font f = workbook.createFont();
		if (isBold) {
			f.setBoldweight(Font.BOLDWEIGHT_BOLD);
		}
		if (testSize > 0) {
			f.setFontHeightInPoints( testSize);
		}

		f.setColor(color);
		return f;
	}
	private static void appendObjectiveScores(Sheet summarySheet, StudentReportIrsScore irsScore, int rowno,short textSize, Boolean isBold) {
		Row row = getRow(summarySheet, rowno);
		int count = 1;
		org.apache.poi.ss.usermodel.Font normalBlackFont = getFont(summarySheet.getWorkbook(), isBold, HSSFColor.BLACK.index, textSize);
		CellStyle c0s = summarySheet.getWorkbook().createCellStyle();
		c0s.setFont(normalBlackFont);
		c0s.setBorderLeft(CellStyle.BORDER_THIN);
		c0s.setBorderRight(CellStyle.BORDER_THIN);
		c0s.setBorderTop(CellStyle.BORDER_THIN);
		
		CellStyle objArea = summarySheet.getWorkbook().createCellStyle();
		objArea.setFont(normalBlackFont);
		objArea.setWrapText(true);
		objArea.setBorderLeft(CellStyle.BORDER_MEDIUM);
		objArea.setBorderRight(CellStyle.BORDER_THIN);
		objArea.setBorderTop(CellStyle.BORDER_THIN);
		
		CellStyle rightBar = summarySheet.getWorkbook().createCellStyle();
		rightBar.setFont(normalBlackFont);
		rightBar.setBorderLeft(CellStyle.BORDER_THIN);
		rightBar.setBorderRight(CellStyle.BORDER_MEDIUM);
		rightBar.setBorderTop(CellStyle.BORDER_THIN);
		
		CellStyle blackCell = summarySheet.getWorkbook().createCellStyle();
		blackCell.setFillPattern((short) CellStyle.SOLID_FOREGROUND);
		blackCell.setFillForegroundColor(IndexedColors.BLACK.getIndex());
		blackCell.setFont(normalBlackFont);
		summarySheet.addMergedRegion(new CellRangeAddress(rowno,rowno, (short)count,(short)count+1));
		addCell(row, count++, irsScore.getContentAreaName(), objArea);
		addCell(row, count++, "", rightBar);
		if(irsScore.getContentAreaName().contains("Foundational")){
			addCell(row, count++, irsScore.getSpPtsObtained(), blackCell);
			addCell(row, count++, irsScore.getSpPtsObtained(), blackCell);
			addCell(row, count++, irsScore.getSpPerCorrect(), blackCell);
			addCell(row, count++, irsScore.getLnPtsPossible(), blackCell);
			addCell(row, count++, irsScore.getLnPtsObtained(), blackCell);
			addCell(row, count++, irsScore.getLnPerCorrect(), blackCell);
		}else{
			addCell(row, count++, irsScore.getSpPtsObtained(), c0s);
			addCell(row, count++, irsScore.getSpPtsObtained(), c0s);
			addCell(row, count++, irsScore.getSpPerCorrect(), rightBar);
			addCell(row, count++, irsScore.getLnPtsPossible(), c0s);
			addCell(row, count++, irsScore.getLnPtsObtained(), c0s);
			addCell(row, count++, irsScore.getLnPerCorrect(), rightBar);
		}
		addCell(row, count++, irsScore.getRdPtsPossible(), c0s);
		addCell(row, count++, irsScore.getRdPtsObtained(), c0s);
		addCell(row, count++, irsScore.getRdPerCorrect(), rightBar);
		addCell(row, count++, irsScore.getWrPtsPossible(), c0s);
		addCell(row, count++, irsScore.getWrPtsObtained(), c0s);
		addCell(row, count++, irsScore.getWrPerCorrect(), rightBar);
	}
	
	private static void appendTotalScore(Sheet summarySheet, StudentReportIrsScore irsScore, int rowno,short textSize, Boolean isBold){
		Row row = getRow(summarySheet, rowno);
		int count = 1;
		org.apache.poi.ss.usermodel.Font normalBlackFont = getFont(summarySheet.getWorkbook(), isBold, HSSFColor.BLACK.index, textSize);
		CellStyle c0s = summarySheet.getWorkbook().createCellStyle();
		c0s.setFont(normalBlackFont);
		c0s.setBorderLeft(CellStyle.BORDER_THIN);
		c0s.setBorderRight(CellStyle.BORDER_THIN);
		c0s.setBorderTop(CellStyle.BORDER_THIN);
		c0s.setBorderBottom(CellStyle.BORDER_MEDIUM);
		
		CellStyle rightBar = summarySheet.getWorkbook().createCellStyle();
		rightBar.setFont(normalBlackFont);
		rightBar.setBorderLeft(CellStyle.BORDER_THIN);
		rightBar.setBorderRight(CellStyle.BORDER_MEDIUM);
		rightBar.setBorderTop(CellStyle.BORDER_THIN);
		rightBar.setBorderBottom(CellStyle.BORDER_MEDIUM);
		
		CellStyle objArea = summarySheet.getWorkbook().createCellStyle();
		objArea.setFont(normalBlackFont);
		objArea.setWrapText(true);
		objArea.setBorderLeft(CellStyle.BORDER_MEDIUM);
		objArea.setBorderRight(CellStyle.BORDER_THIN);
		objArea.setBorderTop(CellStyle.BORDER_THIN);
		objArea.setBorderBottom(CellStyle.BORDER_MEDIUM);
		
		summarySheet.addMergedRegion(new CellRangeAddress(rowno,rowno, (short)1,(short)2));
		summarySheet.addMergedRegion(new CellRangeAddress(rowno,rowno, (short)3,(short)5));
		summarySheet.addMergedRegion(new CellRangeAddress(rowno,rowno, (short)6,(short)8));
		summarySheet.addMergedRegion(new CellRangeAddress(rowno,rowno, (short)9,(short)11));
		summarySheet.addMergedRegion(new CellRangeAddress(rowno,rowno, (short)12,(short)14));
		
		addCell(row, count++, irsScore.getContentAreaName(), objArea);
		addCell(row, count++, "", rightBar);
		addCell(row, count++, irsScore.getSpTotalScore(), c0s);
		addCell(row, count++, "", c0s);
		addCell(row, count++, "", rightBar);
		addCell(row, count++, irsScore.getLnTotalScore(), c0s);
		addCell(row, count++, "", c0s);
		addCell(row, count++, "", rightBar);
		addCell(row, count++, irsScore.getRdTotalScore(), c0s);
		addCell(row, count++, "", c0s);
		addCell(row, count++, "", rightBar);
		addCell(row, count++, irsScore.getWrTotalScore(), c0s);
		addCell(row, count++, "", c0s);
		addCell(row, count++, "", rightBar);
	}
	
	private static void appendContentAreaNames(Sheet summarySheet, int rowno,short textSize, Boolean isBold){
		Row header = getRow(summarySheet, rowno);
		int counter = 1;
		org.apache.poi.ss.usermodel.Font normalBlackFont = getFont(summarySheet.getWorkbook(), isBold, HSSFColor.BLACK.index, textSize);
		CellStyle c0s = summarySheet.getWorkbook().createCellStyle();
		c0s.setFont(normalBlackFont);
		
		CellStyle c0sStart = summarySheet.getWorkbook().createCellStyle();
		CellStyle c0sEnd = summarySheet.getWorkbook().createCellStyle();
		
		c0sStart.setBorderLeft(CellStyle.BORDER_MEDIUM);
		c0sStart.setBorderTop(CellStyle.BORDER_MEDIUM);
		
		c0sEnd.setBorderRight(CellStyle.BORDER_MEDIUM);
		c0sEnd.setBorderTop(CellStyle.BORDER_MEDIUM);
		
		c0s.setBorderTop(CellStyle.BORDER_MEDIUM);
		
		summarySheet.addMergedRegion(new CellRangeAddress(rowno,rowno, (short)1,(short)2));
		summarySheet.addMergedRegion(new CellRangeAddress(rowno,rowno, (short)3,(short)5));
		summarySheet.addMergedRegion(new CellRangeAddress(rowno,rowno, (short)6,(short)8));
		summarySheet.addMergedRegion(new CellRangeAddress(rowno,rowno, (short)9,(short)11));
		summarySheet.addMergedRegion(new CellRangeAddress(rowno,rowno, (short)12,(short)14));
		
		addCell(header, counter++, "", c0sStart);
		addCell(header, counter++, "", c0sEnd);
		addCell(header, counter++, SPEAKING, c0s);
		addCell(header, counter++, "", c0s);
		addCell(header, counter++, "", c0sEnd);
		addCell(header, counter++, LISTENING, c0s);
		addCell(header, counter++, "", c0s);
		addCell(header, counter++, "", c0sEnd);
		addCell(header, counter++, READING, c0s);
		addCell(header, counter++, "", c0s);
		addCell(header, counter++, "", c0sEnd);
		addCell(header, counter++, WRITING, c0s);
		addCell(header, counter++, "", c0s);
		addCell(header, counter++, "", c0sEnd);
	}
	
	private static void appendScoresTitle(Sheet summarySheet, int rowno,short textSize, Boolean isBold){
		Row row = getRow(summarySheet, rowno);
		int counter = 1;
		org.apache.poi.ss.usermodel.Font normalBlackFont = getFont(summarySheet.getWorkbook(), isBold, HSSFColor.BLACK.index, textSize);
		CellStyle c0s = summarySheet.getWorkbook().createCellStyle();
		c0s.setFont(normalBlackFont);
		c0s.setBorderLeft(CellStyle.BORDER_THIN);
		c0s.setBorderRight(CellStyle.BORDER_THIN);
		c0s.setBorderTop(CellStyle.BORDER_THIN);
		
		CellStyle c0sLeft = summarySheet.getWorkbook().createCellStyle();
		CellStyle c0sRight = summarySheet.getWorkbook().createCellStyle();
		c0sLeft.setFont(normalBlackFont);
		c0sLeft.setBorderLeft(CellStyle.BORDER_MEDIUM);
		c0sLeft.setBorderRight(CellStyle.BORDER_THIN);
		c0sLeft.setBorderTop(CellStyle.BORDER_THIN);
		
		
		c0sRight.setFont(normalBlackFont);
		c0sRight.setBorderLeft(CellStyle.BORDER_THIN);
		c0sRight.setBorderTop(CellStyle.BORDER_THIN);
		c0sRight.setBorderRight(CellStyle.BORDER_MEDIUM);
		summarySheet.addMergedRegion(new CellRangeAddress(rowno,rowno, (short)1,(short)2));
		
		addCell(row, counter++, "", c0sLeft);
		addCell(row, counter++, "", c0s);
		addCell(row, counter++, PtsPossible, c0sLeft);
		addCell(row, counter++, PtsObtained, c0s);
		addCell(row, counter++, PerCorrect, c0sRight);
		addCell(row, counter++, PtsPossible, c0s);
		addCell(row, counter++, PtsObtained, c0s);
		addCell(row, counter++, PerCorrect, c0sRight);
		addCell(row, counter++, PtsPossible, c0s);
		addCell(row, counter++, PtsObtained, c0s);
		addCell(row, counter++, PerCorrect, c0sRight);
		addCell(row, counter++, PtsPossible, c0s);
		addCell(row, counter++, PtsObtained, c0s);
		addCell(row, counter++, PerCorrect, c0sRight);
	}

	private String[] populateStudentHeader() {
		String[] result = new String[8];
		result[0] = STUDENT_NAME_LABEL_CSV;
		result[1] = STUDENT_Id_LABEL_CSV;
		result[2] = TEST_DATE_LABEL_CSV;
		result[3] = FORM_LABEL_CSV;
		result[4] = DISTRICT_LABEL_CSV;
		result[5] = SCHOOL_LABEL_CSV;
		result[6] = GRADE_LABEL_CSV;
		result[7] = TEST_NAME_LABEL_CSV;
		
		return result;
	}
	
}
