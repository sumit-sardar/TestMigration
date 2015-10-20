package com.ctb.utils;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;

public class StudentResponseExcelUtils {

	// List<MosaicRequestExcelPojo> mosaicRequestCSVList = null;
	SimpleCache cache = null;
	FileOutputStream fileOut = null;
	private HSSFWorkbook wb;

	/**
	 * Generate the excel report with a single sheet Here 65k limit restriction
	 * is not present
	 * 
	 * @param args
	 * @throws Exception
	 */
	public void generateExcelReport(Object[] args) throws Exception {
		setupForExcel(args);
		if (cache != null && cache.getExtractKeys().size() > 0) {
			wb = new HSSFWorkbook();
			HSSFSheet reportSheet = wb.createSheet();
			wb.setSheetName(0, MSSConstantUtils.excelSheetName);

			prepareReportSheet(reportSheet);

			HSSFPalette palette = wb.getCustomPalette();

			palette.setColorAtIndex(HSSFColor.TEAL.index, (byte) 70, // RGB red
																		// (0-255)
					(byte) 130, // RGB green
					(byte) 180 // RGB blue
			);
			wb.write(fileOut);
		}
	}

	/**
	 * initialize populated student response collection
	 * 
	 * @param args
	 */
	private void setupForExcel(Object[] args) {
		// this.mosaicRequestCSVList = (List<MosaicRequestExcelPojo>)args[1];
		this.cache = (SimpleCache) args[1];
		this.fileOut = (FileOutputStream) args[2];
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
			addCell(row, (short) cellCount++, headings[i]);
		}
		reportSheet.addMergedRegion(new CellRangeAddress(rowno-1, rowno-1, cellCount-1, cellCount+5));

		for (String id : cache.getExtractKeys()) {
			MosaicRequestExcelPojo msReq = cache.getExtractResponse(id);
			if (msReq != null) {
				row = reportSheet.createRow(rowno++);
				int cellno = 0;
				addCell(row, (short) cellno++, msReq.getItemid());
				addCell(row, (short) cellno++, msReq.getItemsource());
				addCell(row, (short) cellno++, msReq.getItemBankid());
				addCell(row, (short) cellno++, msReq.getsResponse());
				addCell(row, (short) cellno++, msReq.getStudentrosterid());
				addCell(row, (short) cellno++, msReq.getOasItemId());
				addCell(row, (short) cellno++, msReq.getPEId());
				addCell(row, (short) cellno++, msReq.getAnswered());
				addCell(row, (short) cellno++, msReq.getJson());
				reportSheet.addMergedRegion(new CellRangeAddress(rowno-1, rowno-1, cellno-1, cellno+5));
				/*
				 * System.out.println(msReq.getItemid() + "\t||\t " +
				 * msReq.getItemsource() + "\t||\t" + msReq.getItemBankid() +
				 * "\t||\t" + msReq.getsResponse() + "\t||\t" +
				 * msReq.getStudentrosterid() + "\t||\t" + msReq.getOasItemId()
				 * + "\t||\t" + msReq.getPEId() + "\t||\t" + msReq.getJson());
				 */
			}
		}

	}
	
	/**
	 * Setting the header of excel file
	 * @return
	 */
	private String[] populateStudentHeader() {
		String[] result = new String[9];
		result[0] = MSSConstantUtils.COL1_DAS_ITEM_ID;
		result[1] = MSSConstantUtils.COL2_ITEM_SOURCE;
		result[2] = MSSConstantUtils.COL3_ITEM_BANK_ID;
		result[3] = MSSConstantUtils.COL4_S_RESPONSE;
		result[4] = MSSConstantUtils.COL5_STUDENT_ROSTER_ID;
		result[5] = MSSConstantUtils.COL6_OAS_ITEM_ID;
		result[6] = MSSConstantUtils.COL7_PEID;
		result[7] = MSSConstantUtils.COL8_ANSWERED;
		result[8] = MSSConstantUtils.COL9_OAS_JSON;
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
		while(cellValue.length() > 32767){
			HSSFCell cell0 = row.createCell(cellNo);
			cellValue = cellValue.substring(0, 32767 - 3);
			cell0.setCellValue(cellValue);
			cellNo++;
		}
		HSSFCell cell0 = row.createCell(cellNo);
		cell0.setCellValue(cellValue);
		return cell0;
		
	}
	
}
