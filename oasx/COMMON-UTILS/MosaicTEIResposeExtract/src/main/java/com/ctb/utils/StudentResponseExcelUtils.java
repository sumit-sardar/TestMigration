package com.ctb.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

public class StudentResponseExcelUtils {
	
	
	List<MosaicRequestExcelPojo> mosaicRequestCSVList = null;
	FileOutputStream fileOut = null;
	
	/**
	 * Generate the excel report with a single sheet
	 * Here 65k limit restriction is not present
	 * @param args
	 * @throws Exception
	 */
	public void generateExcelReport(Object[] args) throws Exception {
    	setupForExcel(args);
    	HSSFWorkbook wb = new HSSFWorkbook();
    	if(this.mosaicRequestCSVList!= null && !this.mosaicRequestCSVList.isEmpty()){
    		HSSFSheet reportSheet = wb.createSheet();
    		wb.setSheetName(0, MSSConstantUtils.excelSheetName);

    		prepareReportSheet(reportSheet);

    		HSSFPalette palette =  wb.getCustomPalette();

    	    palette.setColorAtIndex(HSSFColor.TEAL.index,
    	            (byte) 70,  //RGB red (0-255)
    	            (byte) 130,    //RGB green
    	            (byte) 180     //RGB blue
    	    );
    	    wb.write(fileOut);
    	}
    }
	
	/**
	 * initialize populated student response collection 
	 * @param args
	 */
	@SuppressWarnings("unchecked")
	private void setupForExcel(Object[] args) {
		this.mosaicRequestCSVList = (List<MosaicRequestExcelPojo>)args[1];
		this.fileOut = (FileOutputStream)args[2];
	}
	
	/**
	 * Preparing the header and other data of excel sheet
	 * @param reportSheet
	 * @param wb
	 * @throws IOException
	 */
	private void prepareReportSheet(HSSFSheet reportSheet) throws IOException {
		int rowno = 0;
		String[] headings;
		headings = populateStudentHeader();
		HSSFRow row = null;
		row = reportSheet.createRow(rowno++);
		int cellCount = 0;
		for (int i = 0; i < headings.length; i++) {
			addCell(row, (short)cellCount++ , headings[i]);
		}
		
		for (MosaicRequestExcelPojo msReq : mosaicRequestCSVList) {
			row = reportSheet.createRow(rowno++);
			int cellno = 0;
			addCell(row, (short)cellno++ , msReq.getItemid());
			addCell(row, (short)cellno++ , msReq.getItemsource());
			addCell(row, (short)cellno++ , msReq.getItemBankid());
			addCell(row, (short)cellno++ , msReq.getsResponse());
			addCell(row, (short)cellno++ , msReq.getStudentrosterid());
			addCell(row, (short)cellno++ , msReq.getOasItemId());
			addCell(row, (short)cellno++ , msReq.getPEId());
			addCell(row, (short)cellno++ , MSSConstantUtils.wrap(msReq.getJson()));
		}
	}
	
	/**
	 * Setting the header of excel file
	 * @return
	 */
	private String[] populateStudentHeader() {
		String[] result = new String[8];
		result[0] = MSSConstantUtils.COL1_DAS_ITEM_ID;
		result[1] = MSSConstantUtils.COL2_ITEM_SOURCE;
		result[2] = MSSConstantUtils.COL3_ITEM_BANK_ID;
		result[3] = MSSConstantUtils.COL4_S_RESPONSE;
		result[4] = MSSConstantUtils.COL5_STUDENT_ROSTER_ID;
		result[5] = MSSConstantUtils.COL6_OAS_ITEM_ID;
		result[6] = MSSConstantUtils.COL7_PEID;
		result[7] = MSSConstantUtils.COL8_OAS_JSON;
		return result;
	}
	
	/**
	 * Add cell value
	 * @param row
	 * @param cellNo
	 * @param cellValue
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private static HSSFCell addCell(HSSFRow row, short cellNo, String cellValue) {
		HSSFCell cell0 = row.createCell(cellNo);
		cell0.setCellValue(cellValue);
		return cell0;
		
	}
	
}
