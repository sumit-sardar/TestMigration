package util; 

import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import data.TestRosterVO;
import data.TestAdminVO;
import java.io.IOException;
import java.util.List;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTable;
import com.lowagie.text.pdf.PdfWriter;
import data.ImageVO;
import data.TableVO;
import java.awt.Color;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import javax.servlet.ServletOutputStream;
//import weblogic.webservice.tools.pagegen.result;

public class TableUtils 
{ 
    protected int currentPage = 0;
    protected double PAGE_SIZE = 0.0;
    protected Document document = null;
    private Font largeBoldBlueFont = null;
    private Font smallBlueFont = null;
    private Font normalBoldBlueFont = null;
    private Font normalBlueFont = null;
    private Font normalFont = null;
    private Font dataEntryFont = null;
    private Font normalBoldFont = null;
    private Font tenBoldBlueFont = null;
    private Font elevenBoldBlueFont = null;
    private Font tenFont = null;
    private Font dataEntryTenFont = null;
    private Font nineFont = null;
    private Font mediumFont = null;
    private Font mediumGreyFont = null;
	private Color blueColor = null;
	private Color blackColor = null;
    private Color greyColor = null;
    private Color lightGreyColor = null;
    private Color darkBlueColor = null;
    private Color whiteColor = null;
    private PdfWriter writer = null;
    private ServletOutputStream out = null;
    private String server = null;
    private int port = 0;
    
	private static final String COPYWRITE = "Developed and published by CTB/McGraw-Hill LLC, a subsidiary of the McGraw-Hill Companies, Inc., 20 Ryan Ranch Road, Monterey, California, 93940-5703. Copyright © 2006 by CTB/McGraw-Hill LLC. All rights reserved. Only authorized customers may copy, download and/or print the document, located online at ctb.com. Any other use or reproduction of this document, in whole or in part, requires written permission of the publisher.";
    private static final String ARIAL = "ARIAL";
    private static final String TIMES_NEW_ROMAN = FontFactory.TIMES_ROMAN;
    private static final float SMALL_FONT = 2f;
    private static final float MEDIUM_FONT = 8f;
    private static final float LARGE_FONT = 14f;
    private static final float NORMAL_FONT = 12f;
    private static final float ELEVEN_POINT_FONT = 11f;
    private static final float TEN_POINT_FONT = 10f;
    private static final float NINE_POINT_FONT = 9f;
    private static final String BOLD = "bold";
    private static final String ELLIPSIS = "...";
    private static final float DEFAULT_CELL_PADDING = 5f;
    private static final float STUDENT_CELL_PADDING = 2.5f;
    
    private static final float STUDENT_NAME_WIDTH = 170f;
    private static final float STUDENT_ID_WIDTH = 100f;
    private static final float LOGIN_ID_WIDTH = 300f;

    /**
     * This method returns a table displaying the title in large bold blue font
     */
    protected TableVO getTitleTable(String title,
                                    float width,
                                    float x,
                                    float y) throws DocumentException{
        ArrayList cells = new ArrayList();
        cells.add(getTableCell(title, getLargeBoldBlueFont()));
        PdfPTable table = getTable(1, cells);
        table.setTotalWidth(width);
        return new TableVO(table, 0, 1, x, y, 1);
    }
    /**
     * This method returns a table displaying a horizontal line
     * haven't been able to get it to do a rounded line style - maybe using PdfPTable instead of cell
     */
    protected TableVO getLineTable(float width,
                                   float x,
                                   float y) throws DocumentException{
        ArrayList cells = new ArrayList();
        cells.add(getTableCell(getBlueColor(), " ", getSmallBlueFont(), false, true, false, false, 3f));
        PdfPTable table = getTable(1, cells);
        table.setTotalWidth(width);
        return new TableVO(table, 0, 1, x, y, 1);
    }
    //START - Added For CR ISTEP2011CR007 (Multiple Test Ticket)
    /**
     * This method returns a table displaying a horizontal dotted line
     * haven't been able to get it to do a rounded line style - maybe using PdfPTable instead of cell
     */
    protected TableVO getDottedLineTable(float width,
                                   float x,
                                   float y) throws DocumentException{
        ArrayList cells = new ArrayList();
        String text="";
    	for (int i=0; i< 64; i++){
    		text = text + "- ";
    	}
    	cells.add(getTableCell(getBlueColor(),text, getNormalBlueFont(), false, false, false, false, 0f));
        PdfPTable table = getTable(1, cells);
        table.setTotalWidth(width);
        return new TableVO(table, 0, 1, x, y, 1);
    }
    //END - Added For CR ISTEP2011CR007 (Multiple Test Ticket)
    
    /**
     * This method returns a table displaying the copywrite text in medium font
    */
     protected TableVO getCopywriteTable(float width,
                                         float x,
                                         float y) throws DocumentException{
        return getFooterTable(COPYWRITE, width ,x, y);
    }

    /**
     * This method returns a table displaying the copywrite text in medium font
    */
     protected TableVO getWatermarkTable(String text,
                                         float width,
                                         float x,
                                         float y) throws DocumentException{
        ArrayList cells = new ArrayList();
        cells.add(getTableCell(text, getMediumGreyFont()));
        PdfPTable table = getTable(1, cells);
        table.setTotalWidth(width);
        return new TableVO(table, 0, 1, x, y, 1);
    }

    /**
     * This method returns a table displaying the copywrite text in medium font
    */
     protected TableVO getFooterTable(String text,
                                      float width,
                                      float x,
                                      float y) throws DocumentException{
        ArrayList cells = new ArrayList();
        cells.add(getTableCell(text, getMediumFont()));
        PdfPTable table = getTable(1, cells);
        table.setTotalWidth(width);
        return new TableVO(table, 0, 1, x, y, 1);
    }

    /**
     * This method returns a table displaying the text in normal bold blue font
    */
     protected TableVO getLabelTable(String text,
                                     float width,
                                     float x,
                                     float y) throws DocumentException{
        ArrayList cells = new ArrayList();
        cells.add(getTableCell(text, getNormalBoldBlueFont()));
        PdfPTable table = getTable(1, cells);
        table.setTotalWidth(width);
        return new TableVO(table, 0, 1, x, y, 1);
    }


    /**
     * This method returns a table displaying the text in normal blue font
     */
     protected TableVO getBlueTable(String text,
                                     float width,
                                     float x,
                                     float y) throws DocumentException{
        ArrayList cells = new ArrayList();
        cells.add(getTableCell(text, getNormalBlueFont()));
        PdfPTable table = getTable(1, cells);
        table.setTotalWidth(width);
        return new TableVO(table, 0, 1, x, y, 1);
    }

    /**
     * This method returns a table displaying the text in normal font
     */
      protected TableVO getInfoTable(String text,
                                    float width,
                                    float x,
                                    float y) throws DocumentException{
        ArrayList cells = new ArrayList();
        cells.add(getTableCell(text, getNormalFont()));
        PdfPTable table = getTable(1, cells);
        table.setTotalWidth(width);
        return new TableVO(table, 0, 1, x, y, 1);
    }

    /**
     * This method returns a table displaying the first cell of each row in normal blue font,
     * the rest of the cells in the table in normal font, with a border around the outside of 
     * the table and between columns.
    */
     protected TableVO getLoginTable(String[] texts,
                                     float width,
                                     float[] columnWidths,
                                     float x,
                                     float y,
                                     float borderWidth) throws DocumentException {
        ArrayList cells = new ArrayList();
        int cols = columnWidths.length;
        int rows = texts.length/cols;
        for(int i=0; i<texts.length; i++){
        	cells.add(getTableCell(texts[i], 
                getLoginFont(i, cols), 
                getColumnOuterBorder(i, cols, rows),  
                borderWidth));
        }
        PdfPTable table = getTable(cols, cells);
        table.setTotalWidth(width);
        table.setWidths(columnWidths);
        return new TableVO(table, 0, rows, x, y, rows);
    }

    /**
     * This method returns a table displaying the first cell of each row in normal blue font,
     * the rest of the cells in the table in normal font, with a border around the outside of 
     * the table and between columns.
    */
     protected TableVO getSummaryTable(String[] texts,
                                     float width,
                                     float[] columnWidths,
                                     float x,
                                     float y,
                                     float borderWidth) throws DocumentException {
        ArrayList cells = new ArrayList();
        int cols = columnWidths.length;
        int rows = texts.length/cols;
        for(int i=0; i<texts.length; i++){
            float height = i == texts.length-2 ? 30f : 0f;
            cells.add(getTableCell(texts[i], 
                                   getNormalBlueFont(i, cols), 
                                   getColumnOuterBorder(i, cols, rows),  
                                   borderWidth,
                                   height));
        }
        PdfPTable table = getTable(cols, cells);
        table.setTotalWidth(width);
        table.setWidths(columnWidths);
        return new TableVO(table, 0, rows, x, y, rows);
    }

    /**
     * This method returns a table displaying the heading cells in ten point bold blue font with a border
     * around each cell, and the rest of the cells in the table in normal font, with a border between columns.
    */
     protected TableVO getStudentTable(ArrayList students,
                                       String[] headings,
                                       float width,
                                       float[] columnWidths,
                                       float x,
                                       float y,
                                       float borderWidth,
                                       boolean isTabeProduct) throws DocumentException {
        ArrayList cells = new ArrayList();
        for(int i=0; i<headings.length; i++){
            int halign = getHalignForStudentTable(i);
            cells.add(getTableCell(headings[i],
                                   getTenBoldBlueFont(),
                                   getAllBorder(i, headings.length, 1), 
                                   borderWidth,
                                   getGreyColor(),
                                   this.getHalignForStudentTable(i))); 
        }
        int cols = headings.length;
        int rows = 1;
        int colorIndex = 0;
        for(Iterator it = students.iterator(); it.hasNext();){
            TestRosterVO student = (TestRosterVO)it.next();
            int numAccommodations = student.getAccommodations().length;
            if(numAccommodations >0){
                rows+= student.getAccommodations().length;
                Color color = getStudentColor(colorIndex);
                addStudentCells(cells, student, borderWidth, color, isTabeProduct);
                colorIndex++;
            }
        }
        PdfPTable table = getTable(cols, cells);
        table.setTotalWidth(width);
        table.setWidths(columnWidths);
        return new TableVO(table, 0, rows, x, y, rows);
    }

    /**
     * This method returns a table displaying the text in normal font with a border around the table.
    */
     protected TableVO getBorderedTable(String text,
                                        float width,
                                        float x,
                                        float y,
                                        float borderWidth) throws DocumentException {
        ArrayList cells = new ArrayList();
        cells.add(getTableCell(text, getNormalFont(), true, true, true, true, borderWidth));
        PdfPTable table = getTable(1, cells);
        table.setTotalWidth(width);
        return new TableVO(table, 0, 1, x, y, 1);
    }

    /**
     * This method returns a table displaying the heading text in normal bold font and the rest of the
     * text in normal font, with a border around each cell.
    */
     protected TableVO getHeaderBorderTable(String[] texts,
                                            float width,
                                            float[] columnWidths,
                                            float x,
                                            float y,
                                            float borderWidth) throws DocumentException {
        ArrayList cells = new ArrayList();
        int cols = columnWidths.length;
        int rows = texts.length/cols;
        for(int i = 0; i< texts.length; i++){
            cells.add(
                getTableCell(texts[i], 
                             getHeaderBorderFont(i, cols), 
                             getAllBorder(i, cols, rows), 
                             borderWidth, 
                             getHeaderBorderColor(i, cols)));
        }
        PdfPTable table = getTable(cols, cells);
        table.setTotalWidth(width);
        table.setWidths(columnWidths);
        return new TableVO(table, 0, rows, x, y, rows);
    }

    /**
     * This method returns a table displaying the heading text in normal bold font and the rest of the
     * text in normal font, with a border around each cell.
    */
     protected TableVO getTacTable(String[] texts,
                                            float width,
                                            float[] columnWidths,
                                            float x,
                                            float y,
                                            float borderWidth) throws DocumentException {
        ArrayList cells = new ArrayList();
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
        return new TableVO(table, 0, rows, x, y, rows);
    }

    protected String infoEllipsis(String in, float width){
        Font font = getNormalFont();
        return ellipsis(in, width, font);
    }
    
    private String ellipsis(String text, float maxWidth, Font font){
        float textWidth = getTextWidth(text, font);
        if(getTextWidth(text, font) <= maxWidth){
            return text;
        }
        else{
            StringBuffer buff = new StringBuffer(text);
            String result = null;
            while (textWidth > maxWidth){
                int end = buff.length();
                buff.delete(end - 1, end);
                result = buff.toString() + ELLIPSIS;
                textWidth = getTextWidth(result, font);
            }
            return result;
        }
    }
    
    private float getTextWidth(String text, Font font){
        Chunk chunk = new Chunk(text, font);
        return chunk.getWidthPoint();
    }
    
    private void addStudentCells(ArrayList cells, 
                                 TestRosterVO student,
                                 float borderWidth, 
                                 Color color,
                                 boolean isTabeProduct) throws DocumentException {
        for(int i=0; i<student.getAccommodations().length; i++){
            addStudentCellsForRow(i, cells, student, borderWidth, color, isTabeProduct);
        }
    }
    
    private void addStudentCellsForRow(int rowIndex,
                                       ArrayList cells, 
                                       TestRosterVO student,
                                       float borderWidth, 
                                       Color color,
                                       boolean isTabeProduct) throws DocumentException{
        String studentName = " ";
        String studentId = " ";
        String loginId = " ";
        String password = " ";
        String form = " ";
        String status = " ";
        String accommodation = student.getAccommodations()[rowIndex];
        if(rowIndex == 0){
            studentName = getStudentName(student);
            studentId = getStudentId(student);
            loginId = getLoginId(student);
            password = getPassword(student);
            form = getForm(student);
            status = getStatus(student);
            
        }
        cells.add(getTableCell(studentName,
                               getTenFont(),
                               false,
                               false,
                               false,
                               false,
                               borderWidth,
                               color,
                               STUDENT_CELL_PADDING,
                               Element.ALIGN_LEFT ));
        cells.add(getTableCell(studentId,
                               getTenFont(),
                               true,
                               false,
                               false,
                               false,
                               borderWidth,
                               color,
                               STUDENT_CELL_PADDING,
                               Element.ALIGN_CENTER));
        cells.add(getTableCell(loginId,
                               getDataEntryTenFont(),
                               true,
                               false,
                               false,
                               false,
                               borderWidth,
                               color,
                               STUDENT_CELL_PADDING,
                               Element.ALIGN_LEFT));
        cells.add(getTableCell(password,
        					   getDataEntryTenFont(),
                               true,
                               false,
                               false,
                               false,
                               borderWidth,
                               color,
                               STUDENT_CELL_PADDING,
                               Element.ALIGN_LEFT));
        if (! isTabeProduct) {                              
            cells.add(getTableCell(form,
                               getTenFont(),
                               true,
                               false,
                               false,
                               false,
                               borderWidth,
                               color,
                               STUDENT_CELL_PADDING,
                               Element.ALIGN_CENTER));
        }                              
        cells.add(getTableCell(status,
                               getTenFont(),
                               true,
                               false,
                               false,
                               false,
                               borderWidth,
                               color,
                               STUDENT_CELL_PADDING,
                               Element.ALIGN_CENTER));
        cells.add(getTableCell(accommodation,
                               getNineFont(),
                               true,
                               false,
                               false,
                               false,
                               borderWidth,
                               color,
                               STUDENT_CELL_PADDING,
                               Element.ALIGN_CENTER));
   }
   
   private int getHalignForStudentTable(int colIndex){
        return (colIndex == 0 || colIndex == 2 || colIndex == 3) ? Element.ALIGN_LEFT : Element.ALIGN_CENTER;
   }
    private String getStudentName(TestRosterVO student){
        String name = student.getLastName() + ", " + student.getFirstName();
        String middleName = student.getMiddleName();
        if(middleName != null && middleName.length() > 0){
            name = name + " " + middleName;
        }
        return infoEllipsis(name, STUDENT_NAME_WIDTH);
    }
 
    private String getStudentId(TestRosterVO student){
        String studentId = student.getStudentNumber();
        studentId = studentId == null ? " " : studentId;
        return infoEllipsis(studentId, STUDENT_ID_WIDTH);
    }
    private String getLoginId(TestRosterVO student){
        String loginId = student.getLoginName();
        loginId = loginId == null ? " " : loginId;
        return infoEllipsis(loginId, LOGIN_ID_WIDTH);
   }
    private String getPassword(TestRosterVO student){
        String password = student.getPassword();
    return password == null ? " " : password;
    }
    private String getForm(TestRosterVO student){
        return student.getForm();
    }
    private String getStatus(TestRosterVO student){
        String status = student.getTestStatus();
        return status == null ? " " : FilterSortPageUtils.testStatus_CodeToString(status);
    }
    
    private String[] getAccommodations(TestRosterVO student){
        return student.getAccommodations();
    }
    private Color getStudentColor(int rowIndex){
        if(rowIndex%2 == 0){
            return getWhiteColor();
        }
        else{
            return getLightGreyColor();
        }
    }
    
    private Color getLightGreyColor(){
        if(this.lightGreyColor == null){
            this.lightGreyColor = new Color(0xD0D0D0);
        }
        return this.lightGreyColor;
    }
    private Font getNormalBlueFont(int cell, int cols){
        int mod = cell%cols;
        if(mod == 0){
            return getNormalBlueFont();
        }
        else{
            return getNormalFont();
        }
    }
    
    private Font getLoginFont(int cell, int cols){
        int mod = cell%cols;
        if(mod == 0){
            return getNormalBlueFont();
        }
        else{
            return getDataEntryFont();
        }
    }
    
    private Font getSessionFont(int cell, int cols){
        int mod = cell%cols;
        if(mod == 0){
            return getNormalBoldFont();
        }
        else{
            return getNormalFont();
        }
    }
    
    private Font getHeaderBorderFont(int cell, int cols){
        if(cell < cols){
            return getNormalBoldFont();
        }
        else{
            return getNormalFont();
        }
    }
    
    private Font getBlueHeaderBorderFont(int cell, int cols){
        if(cell < cols){
            return getNormalBoldBlueFont();
        }
        else if ((cell+1)%cols == 0){  // tac 
            return getDataEntryFont();
        }
        else{
            return getNormalFont();
        }
    }
    
    private Color getHeaderBorderColor(int cell, int cols){
        if(cell < cols){
            return getGreyColor();
        }
        else{
            return getWhiteColor();
        }
    }
    
    // left top right bottom
    private boolean[] getAllBorder(int cell, int cols, int rows){
        boolean[] result = new boolean[4];
        int mod = cell%cols;
        int div = cell/cols;
        result[0] = true;  // show left border for all
        result[1] = true;  // show top border for al
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
    
    private boolean[] getColumnOuterBorder(int cell, int cols, int rows){
        boolean[] result = new boolean[4];
        int mod = cell%cols;
        int div = cell/cols;
        result[0] = true;  // show left border for all
        if(cell < cols){  // show top border for first row
            result[1] = true;
        }
        else{
            result[1] = false;
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

     protected TableVO getSummaryInfoTable(String text,
                                           float width,
                                           float[] colWidths,
                                           float x,
                                           float y,
                                           float borderWidth) throws DocumentException {
        ArrayList cells = new ArrayList();
        cells.add(getTableCell(text, getNormalFont(), true, true, true, true, borderWidth));
        PdfPTable table = getTable(1, cells);
        table.setTotalWidth(width);
        table.setWidths(colWidths);
        return new TableVO(table, 0, 1, x, y, 1);
    }

      protected TableVO getSessionTable(String[] texts,
                                        float width,
                                        float[] colWidths,
                                        float x,
                                        float y,
                                        float borderWidth) throws DocumentException {
        ArrayList cells = new ArrayList();
        int cols = colWidths.length;
        int rows = texts.length / cols;
        for(int i=0; i<texts.length; i++){
            cells.add(getTableCell(texts[i], 
                      getSessionFont(i, cols), 
                      this.getColumnOuterBorder(i, cols, rows), 
                      borderWidth));
        }
        PdfPTable table = getTable(cols, cells);
        table.setTotalWidth(width);
        table.setWidths(colWidths);
        return new TableVO(table, 0, rows, x, y, rows);
    }

    private PdfPCell getTableCell(String text, Font font) throws BadElementException {
        return getTableCell(text, font, false,false,false,false,0f);
    }
    
    private PdfPCell getTableCell(String text, 
                                  Font font, 
                                  boolean borderLeft,
                                  boolean borderTop,
                                  boolean borderRight,
                                  boolean borderBottom,
                                  float borderWidth,
                                  Color bgcolor) throws BadElementException {
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
    
    private PdfPCell getTableCell(String text, 
                                  Font font, 
                                  boolean borderLeft,
                                  boolean borderTop,
                                  boolean borderRight,
                                  boolean borderBottom,
                                  float borderWidth,
                                  Color bgcolor,
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
    
    private PdfPCell getTableCell(String text, 
                                     Font font, 
                                     boolean[] borders,
                                     float borderWidth,
                                     Color bgcolor,
                                     int halign) throws BadElementException {
       PdfPCell result = getTableCell(text, font, borders, borderWidth, bgcolor);
       result.setHorizontalAlignment(halign);
       return result;
    }

    private PdfPCell getTableCell(String text, 
                                     Font font, 
                                     boolean[] borders,
                                     float borderWidth,
                                     Color bgcolor) throws BadElementException {
       return getTableCell(text, font, borders[0], borders[1], borders[2], borders[3], borderWidth, bgcolor);
    }

    private PdfPCell getTableCell(String text, 
                                  Font font, 
                                  boolean borderLeft,
                                  boolean borderTop,
                                  boolean borderRight,
                                  boolean borderBottom,
                                  float borderWidth) throws BadElementException {
        return getTableCell(text, font, borderLeft, borderTop, borderRight, borderBottom, borderWidth, null);
    }
    private PdfPCell getTableCell(Color borderColor,
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
     private PdfPCell getTableCell(String text, 
                                  Font font, 
                                  boolean[] border,
                                  float borderWidth) throws BadElementException {
        return getTableCell(text, font, border[0], border[1], border[2], border[3], borderWidth, null);
    }

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

     private Font getLargeBoldBlueFont(){
        if(largeBoldBlueFont == null){
            this.largeBoldBlueFont = FontFactory.getFont(ARIAL, LARGE_FONT);
            this.largeBoldBlueFont.setStyle(BOLD);
            this.largeBoldBlueFont.setColor(getBlueColor());
        }
        return this.largeBoldBlueFont;
    }
    private Font getSmallBlueFont(){
        if(smallBlueFont == null){
            this.smallBlueFont = FontFactory.getFont(ARIAL, SMALL_FONT);
            this.smallBlueFont.setColor(getDarkBlueColor());
        }
        return this.smallBlueFont;
    }

    private Font getNormalBoldBlueFont(){
        if(normalBoldBlueFont == null){
            this.normalBoldBlueFont = FontFactory.getFont(ARIAL, NORMAL_FONT);
            this.normalBoldBlueFont.setStyle(BOLD);
            this.normalBoldBlueFont.setColor(getBlueColor());
        }
        return this.normalBoldBlueFont;
    }

    private Font getNormalBoldFont(){
        if(normalBoldFont == null){
            this.normalBoldFont = FontFactory.getFont(ARIAL, NORMAL_FONT);
            this.normalBoldFont.setStyle(BOLD);
        }
        return this.normalBoldFont;
    }
    
    private Font getElevenBoldBlueFont(){
        if(elevenBoldBlueFont == null){
            this.elevenBoldBlueFont = FontFactory.getFont(ARIAL, ELEVEN_POINT_FONT);
            this.elevenBoldBlueFont.setStyle(BOLD);
            this.elevenBoldBlueFont.setColor(getBlueColor());
        }
        return this.elevenBoldBlueFont;
    }
    
    private Font getTenBoldBlueFont(){
        if(tenBoldBlueFont == null){
            this.tenBoldBlueFont = FontFactory.getFont(ARIAL, TEN_POINT_FONT);
            this.tenBoldBlueFont.setStyle(BOLD);
            this.tenBoldBlueFont.setColor(getBlueColor());
        }
        return this.tenBoldBlueFont;
    }
    
    private Font getTenFont(){
        if(tenFont == null){
            this.tenFont = FontFactory.getFont(ARIAL, TEN_POINT_FONT);
        }
        return this.tenFont;
    }
    
    private Font getDataEntryTenFont(){
        if(dataEntryTenFont == null){
            this.dataEntryTenFont = FontFactory.getFont(TIMES_NEW_ROMAN, TEN_POINT_FONT);
        }
        return this.dataEntryTenFont;
    }
    
    private Font getNineFont(){
        if(nineFont == null){
            this.nineFont = FontFactory.getFont(ARIAL, NINE_POINT_FONT);
        }
        return this.nineFont;
    }
    
    private Font getNormalFont(){
        if(normalFont == null){
            this.normalFont = FontFactory.getFont(ARIAL, NORMAL_FONT);
        }
        return this.normalFont;
    }
    
    private Font getDataEntryFont(){
        if(dataEntryFont == null){
            this.dataEntryFont = FontFactory.getFont(TIMES_NEW_ROMAN, NORMAL_FONT);
        }
        return this.dataEntryFont;
    }
    
   private Font getNormalBlueFont(){
        if(normalBlueFont == null){
            this.normalBlueFont = FontFactory.getFont(ARIAL, NORMAL_FONT);
            this.normalBlueFont.setColor(getBlueColor());
        }
        return this.normalBlueFont;
    }
    
    private Font getMediumFont(){
        if(mediumFont == null){
            this.mediumFont = FontFactory.getFont(ARIAL, MEDIUM_FONT);
        }
        return this.mediumFont;
    }
    
    private Font getMediumGreyFont(){
        if(mediumGreyFont == null){
            this.mediumGreyFont = FontFactory.getFont(ARIAL, MEDIUM_FONT);
            this.mediumGreyFont.setColor(getGreyColor());
        }
        return this.mediumGreyFont;
    }
    
    private Color getBlueColor(){
        if(this.blueColor == null){
            this.blueColor = new Color(0x336699);
        }
        return this.blueColor;
    }
    private Color getDarkBlueColor(){
        if(this.darkBlueColor == null){
            this.darkBlueColor = new Color(0xC0C0C0);
        }
        return this.blueColor;
    }

    private Color getGreyColor(){
        if(this.greyColor == null){
            this.greyColor = new Color(0xC0C0C0);
        }
        return this.greyColor;
    }
    
    private Color getWhiteColor(){
        if(this.whiteColor == null){
            this.whiteColor = new Color(0xFFFFFF);
        }
        return this.whiteColor;
    }
    private PdfPTable getTable(int columns, ArrayList cells) throws BadElementException{
        PdfPTable result = new PdfPTable(columns);
        
        for(Iterator it = cells.iterator(); it.hasNext();){
            PdfPCell cell = (PdfPCell)it.next();
            result.addCell(cell);
        }

        return result;
    }

    protected float getTitleHeight(String text, float width) throws DocumentException{
        ArrayList cells = new ArrayList();
        cells.add(getTableCell(text, getLargeBoldBlueFont()));
        PdfPTable table = getTable(1, cells);
        table.setTotalWidth(width);
        return table.getTotalHeight();
    }
    
    protected float getInfoHeight(String text, float width) throws DocumentException{
        ArrayList cells = new ArrayList();
        cells.add(getTableCell(text, getNormalFont()));
        PdfPTable table = getTable(1, cells);
        table.setTotalWidth(width);
        return table.getTotalHeight();
    }
    
 } 
