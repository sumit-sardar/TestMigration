package util;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFUtils {
	
	private static final String ARIAL = "ARIAL";
    private static final float DEFAULT_CELL_PADDING = 5f;
    private static final float LARGE_FONT = 14f;
	private static final String BOLD = "bold";
	private static final float NORMAL_FONT = 12f;
	private static final float NORMAL_BODY_FONT = 11f;
	private static Font largeBoldBlackFont;
	private static Font normalBoldBlackFont = null;
	private static Font normalBlackBodyFont = null;
	private static BaseColor whiteColor;
	private static BaseColor blackColor;
	
	
	public static PdfWriter getWriter(Document document, OutputStream out ) throws DocumentException{
		return PdfWriter.getInstance(document, out);
	}
	
	private static BaseColor getBlackColor(){
	    if(PDFUtils.blackColor == null){
	    	PDFUtils.blackColor = new BaseColor(0x000000);
	    }
	    return PDFUtils.blackColor;
	}
 
	@SuppressWarnings("unused")
	private static PdfPCell getTableCell(String text, Font font) throws BadElementException {
        return getTableCell(text, font, false,false,false,false,0f);
    }
    
    private static PdfPCell getTableCell(String text, 
                                  Font font, 
                                  boolean borderLeft,
                                  boolean borderTop,
                                  boolean borderRight,
                                  boolean borderBottom,
                                  float borderWidth,
                                  BaseColor bgcolor) throws BadElementException {
    	if (text == null)
    		text = "";                                    
    	Chunk chunk = new Chunk(text);
	    chunk.setFont(font);
	    Paragraph paragraph = new Paragraph();
	    paragraph.add(chunk);
	    PdfPCell result = new PdfPCell(paragraph);
	    if(borderWidth > 0f){
	        if(borderLeft){
	            result.setBorderWidthLeft(borderWidth);
	        }
	        else{
	            result.setBorderWidthLeft(0);
	        }
	        if(borderTop){
	            result.setBorderWidthTop(borderWidth);
	        }
	        else{
	            result.setBorderWidthTop(0);
	        }
	        if(borderRight){
	            result.setBorderWidthRight(borderWidth);
	        }
	        else{
	            result.setBorderWidthRight(0);
	        }
	        if(borderBottom){
	            result.setBorderWidthBottom(borderWidth);
	        }
	        else{
	            result.setBorderWidthBottom(0);
	        }
	    }
	    else{
	    	result.setBorder(0);
	    }
	    if(bgcolor != null){
	    	result.setBackgroundColor(bgcolor);
	    }
	    result.setPaddingLeft(DEFAULT_CELL_PADDING);
	    result.setPaddingBottom(DEFAULT_CELL_PADDING);
	    return result;			
    }

	@SuppressWarnings("unused")
	private PdfPCell getTableCell(String text, 
	                              Font font, 
	                              boolean borderLeft,
	                              boolean borderTop,
	                              boolean borderRight,
	                              boolean borderBottom,
	                              float borderWidth,
	                              BaseColor bgcolor,
	                              float padding,
	                              int halign) throws BadElementException {
	    PdfPCell result = getTableCell( text, 
	                                    font, 
	                                    borderLeft,
	                                    borderTop,
	                                    borderRight,
	                                    borderBottom,
	                                    borderWidth,
	                                    bgcolor);
	    result.setPadding(padding);
	    result.setHorizontalAlignment(halign);
	    return result;        
	}

	@SuppressWarnings("unused")
	private PdfPCell getTableCell(String text, 
	                                 Font font, 
	                                 boolean[] borders,
	                                 float borderWidth,
	                                 BaseColor bgcolor,
	                                 int halign) throws BadElementException {
	   PdfPCell result = getTableCell(text, font, borders, borderWidth, bgcolor);
	   result.setHorizontalAlignment(halign);
	   return result;
	}

	@SuppressWarnings("unused")
	private static PdfPCell getTableCell(String text, 
	                                 Font font, 
	                                 boolean[] borders,
	                                 float borderWidth,
	                                 BaseColor bgcolor) throws BadElementException {
	   return getTableCell(text, font, borders[0], borders[1], borders[2], borders[3], borderWidth, bgcolor);
	}

	private static PdfPCell getTableCell(String text, 
	                              Font font, 
	                              boolean borderLeft,
	                              boolean borderTop,
	                              boolean borderRight,
	                              boolean borderBottom,
	                              float borderWidth) throws BadElementException {
	    return getTableCell(text, font, borderLeft, borderTop, borderRight, borderBottom, borderWidth, null);
	}
	
	@SuppressWarnings("unused")
	private PdfPCell getTableCell(String text, 
	                              Font font, 
	                              boolean[] border,
	                              float borderWidth) throws BadElementException {
	    return getTableCell(text, font, border[0], border[1], border[2], border[3], borderWidth, null);
	}

	@SuppressWarnings("unused")
	private PdfPCell getTableCell(String text, 
                                  Font font, 
                                  boolean[] border,
                                  float borderWidth,
                                  float height) throws BadElementException {
        PdfPCell result = getTableCell(text, font, border[0], border[1], border[2], border[3], borderWidth, null);
        if(height != 0f){
            result.setFixedHeight(height);
        }
        return result;
    }
     
	private static PdfPTable getTable(int columns, ArrayList<PdfPCell> cells) throws BadElementException{
	     PdfPTable result = new PdfPTable(columns);
	     for(Iterator<PdfPCell> it = cells.iterator(); it.hasNext();){
	         PdfPCell cell = (PdfPCell)it.next();
	         result.addCell(cell);
	     }
	
	     return result;
	}
     
     protected static PDFTableVO getTitleTable(String title, float width, float x, float y)	throws DocumentException {
    	 ArrayList<PdfPCell> cells = new ArrayList<PdfPCell>();
    	 cells.add(getTableCell(title, getLargeBoldBlackFont()));
    	 PdfPTable table = getTable(1, cells);
    	 table.setTotalWidth(width);
    	 return new PDFTableVO(table, 0, 1, x, y, 1);
     }
     
     private static Font getLargeBoldBlackFont(){
         if(largeBoldBlackFont == null){
             largeBoldBlackFont = FontFactory.getFont(ARIAL, LARGE_FONT);
             largeBoldBlackFont.setStyle(BOLD);
             largeBoldBlackFont.setColor(getBlackColor());
         }
         return largeBoldBlackFont;
     }
     
     private static Font getNormalBoldBlackFont(){
         if(normalBoldBlackFont == null){
        	 normalBoldBlackFont = FontFactory.getFont(ARIAL, NORMAL_FONT);
        	 normalBoldBlackFont.setStyle(BOLD);
        	 normalBoldBlackFont.setColor(getBlackColor());
         }
         return normalBoldBlackFont;
     }
     
     private static Font getNormalBlackFont(){
         if(normalBlackBodyFont == null){
        	 normalBlackBodyFont = FontFactory.getFont(ARIAL, NORMAL_BODY_FONT);
        	 normalBlackBodyFont.setColor(getBlackColor());
         }
         return normalBlackBodyFont;
     }
	
    protected static PDFTableVO getTableResponseForHeader(String[] texts, float width,
		   float[] columnWidths, float x, float y, float borderWidth) throws DocumentException {
	   
	   ArrayList<PdfPCell> cells = new ArrayList<PdfPCell>();
	   int cols = columnWidths.length;
	   int rows = texts.length / cols;
	   for (int i = 0; i < texts.length; i++) {
		   cells.add(getTableCell(texts[i], getNormalBoldBlackFont(),
				   getAllResponseBorder(i, cols, rows), borderWidth,
				   getWhiteColor()));
	   }
	   PdfPTable table = getTable(cols, cells);
	   table.setTotalWidth(width);
	   table.setWidths(columnWidths);
	   return new PDFTableVO(table, 0, rows, x, y, rows);
   }
    
   protected static PDFTableVO getTableResponseForBody(String[] texts,
		float width, float[] columnWidths, float x, float y, float borderWidth) throws DocumentException {
	   
		ArrayList<PdfPCell> cells = new ArrayList<PdfPCell>();
		int cols = columnWidths.length;
		int rows = texts.length / cols;
		for (int i = 0; i < texts.length; i++) {
			cells.add(getTableCell(texts[i], getNormalBlackFont(),
					getAllResponseBorder(i, cols, rows), borderWidth,
					getWhiteColor()));
		}
		PdfPTable table = getTable(cols, cells);
		table.setTotalWidth(width);
		table.setWidths(columnWidths);
		return new PDFTableVO(table, 0, rows, x, y, rows, table.getTotalHeight());
   }
    
    protected static PDFTableVO getTableForStudentHeader(String[] texts,
		float width, float[] columnWidths, float x, float y, float borderWidth) throws DocumentException {
	   
		ArrayList<PdfPCell> cells = new ArrayList<PdfPCell>();
		int cols = columnWidths.length;
		int rows = texts.length / cols;
		for (int i = 0; i < texts.length; i++) {
			cells.add(getTableCell(texts[i], getNormalBlackFont(),
					getAllResponseBorder(i, cols, rows), borderWidth,
					getWhiteColor()));
		}
		PdfPTable table = getTable(cols, cells);
		table.setTotalWidth(width);
		table.setWidths(columnWidths);
		return new PDFTableVO(table, 0, rows, x, y, rows);
	}

	private static boolean[] getAllResponseBorder(int cell, int cols, int rows){
	    boolean[] result = new boolean[4];
	    result[0] = true;
	    result[1] = true;
	    result[2] = true;
	    result[3] = true;
	    return result;
	}

	private static BaseColor getWhiteColor(){
	    if(whiteColor == null){
	        whiteColor = new BaseColor(0xFFFFFF);
	    }
	    return whiteColor;
	}

	protected static void write(PdfWriter writer, PdfPTable table, int start, int end, float x, float y) throws DocumentException{
	    table.writeSelectedRows(start, end, x, y, writer.getDirectContent());
	}
}
