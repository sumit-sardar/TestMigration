package util;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class PoiUtils {

	public static void addThckBoarderInaSheet(Sheet summarySheet, int rowno, int colStart,int colEnd,
			short color) {
		Workbook workbook = summarySheet.getWorkbook();
		CellStyle cs3 = workbook.createCellStyle();
		cs3.setBorderBottom(CellStyle.BORDER_THICK);
		cs3.setBottomBorderColor(color);

		Row r = summarySheet.createRow(rowno);
		Cell c = null;
		for (int cellnum =  colStart; cellnum <= colEnd; cellnum++) {
			// create a blank type cell (no value)
			c = r.createCell(cellnum);
			// set it to the thick black border style
			c.setCellStyle(cs3);
		}

	}


	public static Font getFont(Workbook workbook, boolean isBold, short color,
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


	public static Row getRow(Sheet sheet, int rowno) {
		return sheet.createRow(rowno);

	}


	public static Cell addCell(Row row, int cellNo, String cellValue) {
		Cell cell0 = row.createCell(cellNo);
		cell0.setCellValue(cellValue);
		return cell0;
		
	}
	public static Cell addCell(Row row, int cellNo, String cellValue, CellStyle cs) {
		Cell cell0 = addCell( row,  cellNo,  cellValue);
		cell0.setCellStyle(cs);
		return cell0;
		
	}
	
	
	public static void appendTitleNameValueWithSameColor(Sheet summarySheet, String title, String titleText, int rowno,short textSize, Boolean isBold) {
			Row header = getRow(summarySheet, rowno);

			CellStyle titleStyle = summarySheet.getWorkbook().createCellStyle();
			org.apache.poi.ss.usermodel.Font boldFont = getFont(summarySheet.getWorkbook(), true, HSSFColor.TEAL.index, textSize);
			if(isBold) {
				titleStyle.setFont(boldFont);
			}
			
			addCell(header, 0, title, titleStyle);
			addCell(header, 1, titleText, titleStyle);
		}
	
	public static  void appendTitleNameValueWithDiffColor(Sheet summarySheet,  String title, String titleText, int rowno,short textSize, Boolean isBold) {
    	Row row = getRow(summarySheet, rowno);
		org.apache.poi.ss.usermodel.Font normalFont = PoiUtils.getFont(summarySheet.getWorkbook(), true, HSSFColor.TEAL.index, textSize);
		org.apache.poi.ss.usermodel.Font normalBlackFont = PoiUtils.getFont(summarySheet.getWorkbook(), true, HSSFColor.BLACK.index, textSize);
		
		
		CellStyle c0s = summarySheet.getWorkbook().createCellStyle();
		c0s.setFont(normalFont);
		addCell(row, 0, title, c0s);

		c0s = summarySheet.getWorkbook().createCellStyle();
		c0s.setFont(normalBlackFont);
		addCell(row, 1, titleText, c0s);

		
		
		
    }

}
