package util;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import util.PDFTableVO;
import util.PDFUtils;
import util.ImmediateReport;

import com.ctb.bean.testAdmin.ScoreDetails;
import com.ctb.bean.testAdmin.ScoreDetails.ResponseResultDetails;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

public class StudentResponseReportPdfUtils implements ImmediateReport {
	private Document document;
	private PdfWriter writer;
	private float scoreTableY = TITLE_Y;
	private OutputStream out;
	List<ScoreDetails> sdForAllSubtests;
	List<ResponseResultDetails> crResponseList;
	String studentName;
	String testSessionName;
	String tascId;
	int itemCount = 0;
	int totalItemInPage;

	public void setup(OutputStream outputStream, List<ScoreDetails> sdForAllSubtests, String studentName, 
			String testSessionName, String tascId) {
		this.out = outputStream;
		this.sdForAllSubtests = sdForAllSubtests;
		this.studentName = studentName;
		this.testSessionName = testSessionName;
		this.tascId = tascId;
	}
	
	void close() {
		document.close();
	}
	
	// Creates a blank PDF document and initializes it for writing
	void initialize() throws DocumentException {
		this.document = new Document();
		this.document.setPageSize( PageSize.LETTER );
		this.writer = PDFUtils.getWriter(document, getOut());
		this.document.open();
	}
	
	public void generateReport() throws Exception {
		initialize();
		generateResponseReport();
		close();
	}

	// Generates Response Result PDF in proper format
	private void generateResponseReport() throws Exception{
		
		addHeaderRow(this.studentName, this.testSessionName, this.tascId);
		
		totalItemInPage = TOTAL_ITEM_IN_FIRST_PAGE;
		
		for(ScoreDetails sd : this.sdForAllSubtests) {
			itemCount = 0;
			crResponseList = new ArrayList<ResponseResultDetails>();
			
			addTitleText(sd.getItemSetName());
			
			ResponseResultDetails[] responseList = sd.getResponseList();
			
			addResponseTableHeader();
			
			for(ResponseResultDetails resResultDetail : responseList) {
				
				if(!"CR".equals(resResultDetail.getItemType())) {
					if( ++itemCount == totalItemInPage ) {
						itemCount = 0;
						totalItemInPage = TOTAL_ITEM_IN_PAGE;
						initializeNewPdfPage(sd.getItemSetName());
					}
					PDFTableVO table1 = PDFUtils.getTableResponseForBody(
							new String[]{resResultDetail.getItemOrder().toString(),
									resResultDetail.getItemType(),
									resResultDetail.getResponse().trim(),
									resResultDetail.getRawScore().toString(),
									resResultDetail.getContentDomain()}, 
							TOTAL_TABLE_WIDTH,
							FIVE_COLUMN_TABLE_WIDTHS,
							LEFT_X,
			                scoreTableY,
			                SCORE_BORDER);
					PDFUtils.write(writer,table1.getTable(), 0, table1.getEnd(), table1.getX(), table1.getY());
					
					scoreTableY = scoreTableY - 18f;
				}
				else if("CR".equals(resResultDetail.getItemType())) {
					crResponseList.add(resResultDetail);
				}
			}
			if(crResponseList.size() > 0) {
				float constructedResponseHeight = 0f;
				initializeNewPdfPageConstructedResponse(sd.getItemSetName());
				
				for(ResponseResultDetails resResultDetail : crResponseList) {
					if(null == resResultDetail.getPdfResponse()) {
						resResultDetail.setPdfResponse("");
					}
					int totalCharLength = resResultDetail.getPdfResponse().length();
					int beginIndex = 0;
					if(totalCharLength > CHAR_PER_PDF_PAGE) {
						while (beginIndex <= totalCharLength) {
							String arr = "";
							if(totalCharLength - beginIndex < CHAR_PER_PDF_PAGE) {
								arr = resResultDetail.getPdfResponse().substring(beginIndex, totalCharLength);
							} else {
								arr = resResultDetail.getPdfResponse().substring(beginIndex, beginIndex + CHAR_PER_PDF_PAGE);
								arr += " ...";
							}
							PDFTableVO table;
							if( beginIndex == 0 ) {
								table = PDFUtils.getTableResponseForBody(
										new String[]{resResultDetail.getItemOrder().toString(),
												resResultDetail.getItemType(),
												arr,
												resResultDetail.getRawScore().toString(),
												resResultDetail.getContentDomain()}, 
										TOTAL_TABLE_WIDTH,
										FIVE_COLUMN_TABLE_WIDTHS_CR,
										LEFT_X,
						                scoreTableY,
						                SCORE_BORDER);
							} else {
								constructedResponseHeight = 0f;
								initializeNewPdfPageConstructedResponse(sd.getItemSetName());
								table = PDFUtils.getTableResponseForBody(
										new String[]{"",
												"",
												arr,
												"",
												""}, 
										TOTAL_TABLE_WIDTH,
										FIVE_COLUMN_TABLE_WIDTHS_CR,
										LEFT_X,
						                scoreTableY,
						                SCORE_BORDER);
							}
							PDFUtils.write(writer,table.getTable(), 0, table.getEnd(), table.getX(), table.getY());
							constructedResponseHeight += table.getHeight();
							beginIndex += CHAR_PER_PDF_PAGE;
						}
					}
					else {
						PDFTableVO table = PDFUtils.getTableResponseForBody(
								new String[]{resResultDetail.getItemOrder().toString(),
										resResultDetail.getItemType(),
										resResultDetail.getPdfResponse(),
										resResultDetail.getRawScore().toString(),
										resResultDetail.getContentDomain()}, 
								TOTAL_TABLE_WIDTH,
								FIVE_COLUMN_TABLE_WIDTHS_CR,
								LEFT_X,
				                scoreTableY,
				                SCORE_BORDER);
						PDFUtils.write(writer,table.getTable(), 0, table.getEnd(), table.getX(), table.getY());
						constructedResponseHeight += table.getHeight();
					}
				}
				scoreTableY = scoreTableY - constructedResponseHeight;
			}
			scoreTableY = scoreTableY - 35f;
			addScoreText(sd.getResponseStatus());
			document.newPage();
			scoreTableY = TITLE_Y;
		}
	}
	
	// Add Header row in the first page of the document
	private void addHeaderRow(String studentName, String testSessionName, String tascId) throws DocumentException{
		PDFTableVO table = PDFUtils.getTableForStudentHeader(
				new String[]{EXAMINEE_NAME, studentName}, 
				TOTAL_TABLE_WIDTH,
				TWO_COLUMN_TABLE_WIDTHS,
				LEFT_X,
                scoreTableY,
                SCORE_BORDER);
		PDFUtils.write(writer,table.getTable(), 0, table.getEnd(), table.getX(), table.getY());
		
		scoreTableY = scoreTableY - 18f;
		
		PDFTableVO table1 = PDFUtils.getTableForStudentHeader(
				new String[]{SESSION_NAME, testSessionName}, 
				TOTAL_TABLE_WIDTH,
				TWO_COLUMN_TABLE_WIDTHS,
				LEFT_X,
                scoreTableY,
                SCORE_BORDER);
		PDFUtils.write(writer,table1.getTable(), 0, table.getEnd(), table1.getX(), table1.getY());
		
		scoreTableY = scoreTableY - 18f;
		
		PDFTableVO table2 = PDFUtils.getTableForStudentHeader(
				new String[]{TASC_ID, tascId}, 
				TOTAL_TABLE_WIDTH,
				TWO_COLUMN_TABLE_WIDTHS,
				LEFT_X,
                scoreTableY,
                SCORE_BORDER);
		PDFUtils.write(writer,table2.getTable(), 0, table2.getEnd(), table2.getX(), table2.getY());
		
		scoreTableY = scoreTableY - 40f;
	}

	public OutputStream getOut() {
		return out;
	}

	public void setOut(OutputStream out) {
		this.out = out;
	}
	
	// Add Subtest Name as Title to each PDF page
	private void addTitleText(String subTestName) throws DocumentException {
		PDFTableVO table = PDFUtils.getTitleTable(subTestName + SCORE_SHEET, TITLE_VALUE_WIDTH, TITLE_VALUE_X, scoreTableY);
		PDFUtils.write(writer,table.getTable(), 0, table.getEnd(), table.getX(), table.getY());
		
		scoreTableY = scoreTableY - 25f;
	}
	
	// Add Header row to each Response Table for SR/GR items
	private void addResponseTableHeader() throws DocumentException {
		PDFTableVO table = PDFUtils.getTableResponseForHeader(
				new String[]{QUES_NO, ITEM_TYPE, RESPONSE, RAW_SCORE, CONTENT_DOMAIN}, 
				TOTAL_TABLE_WIDTH,
				FIVE_COLUMN_TABLE_WIDTHS,
				LEFT_X,
                scoreTableY,
                SCORE_BORDER);
		PDFUtils.write(writer,table.getTable(), 0, table.getEnd(), table.getX(), table.getY());
		
		scoreTableY = scoreTableY - 31f;
	}
	
	// Add Header row to each Response Table for Constructed Response items
	private void addConstructedResponseTableHeader() throws DocumentException {
		PDFTableVO table = PDFUtils.getTableResponseForHeader(
				new String[]{QUES_NO, ITEM_TYPE, RESPONSE, RAW_SCORE, CONTENT_DOMAIN}, 
				TOTAL_TABLE_WIDTH,
				FIVE_COLUMN_TABLE_WIDTHS_CR,
				LEFT_X,
                scoreTableY,
                SCORE_BORDER);
		PDFUtils.write(writer,table.getTable(), 0, table.getEnd(), table.getX(), table.getY());
		
		scoreTableY = scoreTableY - 31f;
	}

	// Add Score text to the end of subtest end section
	private void addScoreText(String responseStatus)  throws DocumentException {
		PDFTableVO table = PDFUtils.getTitleTable(responseStatus, SCORE_TEXT_VALUE_WIDTH, TITLE_VALUE_X, scoreTableY);
		PDFUtils.write(writer,table.getTable(), 0, table.getEnd(), table.getX(), table.getY());
	}
	
	// Adds a new page to existing PDF document and initializes it for writing
	private void initializeNewPdfPage(String subTestName) throws DocumentException {
		document.newPage();
		scoreTableY = TITLE_Y;
		addTitleText(subTestName);
		addResponseTableHeader();
	}
	
	// Adds a new page to existing PDF document and initializes it for writing only for Constructed Response
	private void initializeNewPdfPageConstructedResponse(String subTestName) throws DocumentException {
		document.newPage();
		scoreTableY = TITLE_Y;
		addTitleText(subTestName);
		addConstructedResponseTableHeader();
	}
}
