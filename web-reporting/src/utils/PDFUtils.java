package utils;

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
	private static final float SMALL_FONT = 2f;
    private static final float DEFAULT_CELL_PADDING = 5f;
    private static final float LARGE_FONT = 14f;
	private static final String BOLD = "bold";
	private static final float MEDIUM_FONT = 8f;
	private static final float NORMAL_FONT = 12f;
	private static final String TIMES_NEW_ROMAN = FontFactory.TIMES_ROMAN;
    
	private static BaseColor blueColor;
	private static Font smallBlueFont;
	private static BaseColor darkBlueColor;
	private static Font largeBoldBlueFont;
	private static Font mediumFont = null;
	private static Font normalBoldBlueFont = null;
	private static Font normalFont = null;
	private static Font dataEntryFont = null;
	private static BaseColor greyColor;
	private static BaseColor whiteColor;
	private static BaseColor greenColor;
	


	public static PdfWriter getWriter(Document document, OutputStream out ) throws DocumentException{
		return PdfWriter.getInstance(document, out);
    }

	public static PDFTableVO getLineTable(float width, float leftX, float lineY) throws BadElementException {
		ArrayList<PdfPCell> cells = new ArrayList<PdfPCell>();
		cells.add( getTableCell(getBlueColor(), " ", getSmallBlueFont(), false, true, false, false, 3f));
		PdfPTable table = getTable(1, cells);
	    table.setTotalWidth(width);
	    return new PDFTableVO(table, 0, 1, leftX, lineY, 1);
	}
	
	
	 
	private static BaseColor getBlueColor(){
	        if(PDFUtils.blueColor == null){
	        	PDFUtils.blueColor = new BaseColor(0x336699);
	        }
	        return PDFUtils.blueColor;
	    }
	 
	 private static Font getSmallBlueFont(){
	        if(PDFUtils.smallBlueFont == null){
	        	PDFUtils.smallBlueFont = FontFactory.getFont(ARIAL, SMALL_FONT);
	        	PDFUtils.smallBlueFont.setColor(getDarkBlueColor());
	        }
	        return PDFUtils.smallBlueFont;
	    }
	 
	 private static BaseColor getDarkBlueColor(){
	        if(PDFUtils.darkBlueColor == null){
	        	PDFUtils.darkBlueColor = new BaseColor(0xC0C0C0);
	        }
	        return PDFUtils.blueColor;
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
	    private static PdfPCell getTableCell(BaseColor borderColor,
	                                  String text, 
	                                  Font font, 
	                                  boolean borderLeft,
	                                  boolean borderTop,
	                                  boolean borderRight,
	                                  boolean borderBottom,
	                                  float borderWidth) throws BadElementException {
	        PdfPCell result = getTableCell(text, font, borderLeft, borderTop, borderRight, borderBottom, borderWidth, null);
	        result.setBorderColor(borderColor);
	        return result;
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
	    	 cells.add(getTableCell(title, getLargeBoldBlueFont()));
	    	 PdfPTable table = getTable(1, cells);
	    	 table.setTotalWidth(width);
	    	 return new PDFTableVO(table, 0, 1, x, y, 1);
	     }
	     
	     private static Font getLargeBoldBlueFont(){
	         if(largeBoldBlueFont == null){
	             largeBoldBlueFont = FontFactory.getFont(ARIAL, LARGE_FONT);
	             largeBoldBlueFont.setStyle(BOLD);
	             largeBoldBlueFont.setColor(getBlueColor());
	         }
	         return largeBoldBlueFont;
	     }
	     
	     private static Font getMediumFont(){
	         if(mediumFont == null){
	             mediumFont = FontFactory.getFont(ARIAL, MEDIUM_FONT);
	         }
	         return mediumFont;
	     }
	     
	     private static Font getNormalBoldBlueFont(){
	         if(normalBoldBlueFont == null){
	             normalBoldBlueFont = FontFactory.getFont(ARIAL, NORMAL_FONT);
	             normalBoldBlueFont.setStyle(BOLD);
	             normalBoldBlueFont.setColor(getBlueColor());
	         }
	         return normalBoldBlueFont;
	     }
	     
	     private static Font getNormalFont(){
	         if(normalFont == null){
	             normalFont = FontFactory.getFont(ARIAL, NORMAL_FONT);
	         }
	         return normalFont;
	     }
	     
	     protected static float getTitleHeight(String text, float width) throws DocumentException{
	         ArrayList<PdfPCell> cells = new ArrayList<PdfPCell>();
	         cells.add(getTableCell(text, getLargeBoldBlueFont()));
	         PdfPTable table = getTable(1, cells);
	         table.setTotalWidth(width);
	         return table.getTotalHeight();
	     }
	      
	      protected static PDFTableVO getFooterTable(String text,
                  float width,
                  float x,
                  float y) throws DocumentException{
	    	  ArrayList<PdfPCell> cells = new ArrayList<PdfPCell>();
	          cells.add(getTableCell(text, getMediumFont()));
	          PdfPTable table = getTable(1, cells);
	          table.setTotalWidth(width);
	          return new PDFTableVO(table, 0, 1, x, y, 1);
	      	}
	      
	      protected static PDFTableVO getLabelTable(String text,
                  float width,
                  float x,
                  float y) throws DocumentException{
	    	  ArrayList<PdfPCell> cells = new ArrayList<PdfPCell>();
	          cells.add(getTableCell(text, getNormalBoldBlueFont()));
	          PdfPTable table = getTable(1, cells);
	          table.setTotalWidth(width);
	          return new PDFTableVO(table, 0, 1, x, y, 1);
	      	}
	      
	      protected static PDFTableVO getInfoTable(String text,
                  float width,
                  float x,
                  float y) throws DocumentException{
	    	  ArrayList<PdfPCell> cells = new ArrayList<PdfPCell>();
	          cells.add(getTableCell(text, getNormalFont()));
	          PdfPTable table = getTable(1, cells);
	          table.setTotalWidth(width);
	          return new PDFTableVO(table, 0, 1, x, y, 1);
	      	}
	      
	      protected static float getInfoHeight(String text, float width) throws DocumentException{
	          ArrayList<PdfPCell> cells = new ArrayList<PdfPCell>();
	          cells.add(getTableCell(text, getNormalFont()));
	          PdfPTable table = getTable(1, cells);
	          table.setTotalWidth(width);
	          return table.getTotalHeight();
	      }
	      
	      /**
	       * This method returns a table displaying the heading text in normal bold font and the rest of the
	       * text in normal font, with a border around each cell.
	      */
	       protected static PDFTableVO getTacTable(String[] texts,
	                                              float width,
	                                              float[] columnWidths,
	                                              float x,
	                                              float y,
	                                              float borderWidth) throws DocumentException {
	          ArrayList<PdfPCell> cells = new ArrayList<PdfPCell>();
	          int cols = columnWidths.length;
	          int rows = texts.length/cols;
	          for(int i = 0; i< texts.length; i++){
	              cells.add(
	                  getTableCell(texts[i], 
	                               getBlueHeaderBorderFont(i, cols), 
	                               getAllBorder(i, cols, rows), 
	                               borderWidth, 
	                               getHeaderBorderColor(i, cols)));
	          }
	          PdfPTable table = getTable(cols, cells);
	          table.setTotalWidth(width);
	          table.setWidths(columnWidths);
	          return new PDFTableVO(table, 0, rows, x, y, rows);
	      }
	       
	       
           //Added to remove the bottom border for academic language score

	       protected static PDFTableVO getTacTableAcademicForHeader(String[] texts, float width,
	    		   float[] columnWidths, float x, float y, float borderWidth) throws DocumentException {
	    	   
	    	   ArrayList<PdfPCell> cells = new ArrayList<PdfPCell>();
	    	   int cols = columnWidths.length;
	    	   int rows = texts.length / cols;
	    	   for (int i = 0; i < texts.length; i++) {
	    		   cells.add(getTableCell(texts[i], getBlueHeaderBorderFont(i, cols),
	    				   getAllBorderNew(i, cols, rows), borderWidth,
	    				   getHeaderBorderColorForAcademic(i, cols)));
	    	   }
	    	   PdfPTable table = getTable(cols, cells);
	    	   table.setTotalWidth(width);
	    	   table.setWidths(columnWidths);
	    	   return new PDFTableVO(table, 0, rows, x, y, rows);
	       }
	       
	       
	       
	       protected static PDFTableVO getTacTableAcademic(String[] texts,
				float width, float[] columnWidths, float x, float y,
				float borderWidth) throws DocumentException {
				ArrayList<PdfPCell> cells = new ArrayList<PdfPCell>();
				int cols = columnWidths.length;
				int rows = texts.length / cols;
				for (int i = 0; i < texts.length; i++) {
					cells.add(getTableCell(texts[i], getBlueHeaderBorderFont(i, cols),
							getAllBorder(i, cols, rows), borderWidth,
							getHeaderBorderColorForAcademic(i, cols)));
				}
				PdfPTable table = getTable(cols, cells);
				table.setTotalWidth(width);
				table.setWidths(columnWidths);
				return new PDFTableVO(table, 0, rows, x, y, rows);
	       }
	      
	       protected static PDFTableVO getTacTableForTotal(String[] texts,
				float width, float[] columnWidths, float x, float y,
				float borderWidth)throws DocumentException{
    	   		boolean[] result = new boolean[4];
    	   		result[0]=true;result[1]=true;result[2]=false;result[3]=true;
				ArrayList<PdfPCell> cells = new ArrayList<PdfPCell>();
				int cols = columnWidths.length;
				int rows = texts.length / cols;
				for (int i = 0; i < texts.length; i++) {
					cells.add(getTableCell(texts[i], getNormalFont(),
							(i==0)?result:getAllBorder(i, cols, rows), borderWidth,
							getWhiteColor()));
				}
				PdfPTable table = getTable(cols, cells);
				table.setTotalWidth(width);
				table.setWidths(columnWidths);
				return new PDFTableVO(table, 0, rows, x, y, rows);
	       }
	  //END
	       
	       
	       
	       private static Font getBlueHeaderBorderFont(int cell, int cols){
	           if(cell < cols){
	               return getNormalBoldBlueFont();
	           }
	           else if (cell%cols != 0){
	               return getDataEntryFont();
	           }
	           else{
	               return getNormalFont();
	           }
	       }
	       
	       private static Font getDataEntryFont(){
	           if(dataEntryFont == null){
	               dataEntryFont = FontFactory.getFont(TIMES_NEW_ROMAN, NORMAL_FONT);
	           }
	           return dataEntryFont;
	       }
	       
	    // left top right bottom
	       private static boolean[] getAllBorder(int cell, int cols, int rows){
	           boolean[] result = new boolean[4];
	           int mod = cell%cols;
	           int div = cell/cols;
	           if(cell == 0) {
	        	   result[0] = false;
		           result[1] = false;
	           } else {
		           result[0] = true;
		           result[1] = true;
	           }
	           if(mod == cols-1){ // show right border for right col
	               result[2] = true;
	           }
	           else{
	               result[2] = false;
	           }
	           if(div == rows - 1){ // show bottom border for bottom row
	               result[3] = true;
	           }
	           else{
	               result[3] = false;
	           }
	           return result;
	       }
	       
	    // left top right bottom
	       private static boolean[] getAllBorderNew(int cell, int cols, int rows){
	           boolean[] result = new boolean[4];
	           int mod = cell%cols;
	           int div = cell/cols;
	           if(cell == 0) {
	        	   result[0] = false;
		           result[1] = false;
	           } else {
		           result[0] = true;
		           result[1] = true;
	           }
	           if(mod == cols-1){ // show right border for right col
	               result[2] = true;
	           }
	           else{
	               result[2] = false;
	           }
	           /**if(div == rows - 1){ // show bottom border for bottom row
	               result[3] = true;
	           }
	           else{
	               result[3] = false;
	           }**/
	           result[3] = false;
	           return result;
	       }
	       
	       private static BaseColor getHeaderBorderColor(int cell, int cols){
	    	   if(cell == 0)
	    		   return getWhiteColor();
	           if(cell < cols){
	               return getGreyColor();
	           }
	           else{
	        	   if((cell >= 12 && cell <= 15) || (cell >= 24 && cell <= 31)) {
	        		   return getGreenColor();
	        	   } else {
	        		   return getWhiteColor();
	        	   }
	           }
	       }
	       
	       private static BaseColor getHeaderBorderColorForAcademic(int cell, int cols){
	    	   if(cell == 0)
	    		   return getWhiteColor();
	           if(cell < cols){
	               return getGreyColor();
	           }
	           else{
	        	   if(cell >= 40 && cell <= 45) {
	        		   return getGreenColor();
	        	   } else {
	        		   return getWhiteColor();
	        	   }
	           }
	       }
	       
	       private static BaseColor getGreyColor(){
	           if(greyColor == null){
	               greyColor = new BaseColor(0xC0C0C0);
	           }
	           return greyColor;
	       }
	       
	       private static BaseColor getWhiteColor(){
	           if(whiteColor == null){
	               whiteColor = new BaseColor(0xFFFFFF);
	           }
	           return whiteColor;
	       }
	       
	       private static BaseColor getGreenColor(){
	           if(greenColor == null){
	        	   greenColor = new BaseColor(0xC3D599);
	           }
	           return greenColor;
	       }
	     
	     protected static void write(PdfWriter writer, PdfPTable table, int start, int end, float x, float y) throws DocumentException{
	         table.writeSelectedRows(start, end, x, y, writer.getDirectContent());
	     }

}
