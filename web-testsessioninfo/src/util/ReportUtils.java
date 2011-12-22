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
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import javax.servlet.ServletOutputStream;
//import weblogic.webservice.tools.pagegen.result;

/**
 * 
 * To create a report use a derived class:
 *  override initializeGlobals to set state, call super.initializeGlobals to complete setup
 *  implement hasNextPage to return true if there is another page, false otherwise
 *  implement nextPage to set up state for setStaticTables, setDynamicTables, and setImages
 *  implement setStaticTables to set staticTables to contain TableVOs for the page
 *  implement setDynamicTables to set dynamicTables to contain TableVOs for the page
 *  implement setImages to set images for the page
 *  use TableUtils to help create TableVOs for report
 *  generate report using generateReport
 *  
 */
public abstract class ReportUtils 
{ 
    protected Document document = null;
    private PdfWriter writer = null;
    private ServletOutputStream out = null;
    private String server = null;
    private Integer port = null;
    
    protected Collection staticTables = null;  
    protected Collection dynamicTables = null;
    protected Collection images = null;
    protected TableUtils tableUtils = null;
    protected int totalPages = 0;
    protected int currentPageIndex = 0;
    protected String scheme;
    protected ArrayList pages = new ArrayList();
    //START - Added For CR ISTEP2011CR007 (Multiple Test Ticket)
    protected Collection staticKeyboardTables = null;
    protected boolean lastStudent = true;
    private boolean isMultiIndividualTkt = false;
    //END - Added For CR ISTEP2011CR007 (Multiple Test Ticket)
       
    protected abstract void setup(Object[] args) throws DocumentException, IOException;
    protected abstract boolean setDynamicTables() throws DocumentException, IOException;
    protected abstract boolean setImages() throws DocumentException, IOException;
   
    public void generateReport(Object[] args) throws IOException {
        try{
            setup(args);
            //START - Added For CR ISTEP2011CR007 (Multiple Test Ticket)
            this.isMultiIndividualTkt = (Boolean)args[2];
            createPage();
            
           while(hasNextPage()){
        	   document.newPage();
               currentPageIndex++;
               createPage();
            }
            //END - Added For CR ISTEP2011CR007 (Multiple Test Ticket)
          /* if(isMultiIndividualTkt) {
        	   document.newPage();
        	   displayKeyBoardPage();
           }*/
           
            close();
         }
        catch(DocumentException de){
            de.printStackTrace();
            System.err.println("document: " + de.getMessage());
        }
    }
    
    protected void initializeGlobals(Object[] args) throws DocumentException{
        this.tableUtils = new TableUtils();
        this.dynamicTables = new ArrayList();
        this.staticTables = new ArrayList();
        this.staticKeyboardTables = new ArrayList(); // Added For CR ISTEP2011CR007 (Multiple Test Ticket)
        this.images = new ArrayList();
        this.out =    (ServletOutputStream)args[0];
        this.server = (String)args[1];
        this.port =   (Integer)args[2];
        this.document = new Document();
        this.document.setPageSize((Rectangle)args[3]);
        this.scheme = (String)args[4];
        this.getWriter();
        this.document.open();
   }
    
    private void displayPage() throws IOException, DocumentException {
        for(Iterator it=staticTables.iterator(); it.hasNext();){
            TableVO table = (TableVO)it.next();
            //START - Changed For CR ISTEP2011CR007 (Multiple Test Ticket)
            if((isMultiIndividualTkt && !lastStudent && table.getY()== 72.0f ) 
            		|| (isMultiIndividualTkt && !lastStudent && table.getY()== 30.0f ))
            	continue;
            if(!lastStudent && table.getY()== 400.0f )
            	break;
            write(table.getTable(), 0, table.getEnd(), table.getX(), table.getY());
            //END - Changed For CR ISTEP2011CR007 (Multiple Test Ticket)
        }
        
        if(setDynamicTables()){
            for(Iterator it=dynamicTables.iterator(); it.hasNext();){
                TableVO table = (TableVO)it.next();
                write(table.getTable(), 0, table.getEnd(), table.getX(), table.getY());
            }
        }
        if(!isMultiIndividualTkt && setImages()){
            for(Iterator it=images.iterator(); it.hasNext();){
                ImageVO image = (ImageVO)it.next();
                writeImage(image.getUrl(), image.getX(), image.getY());
            }
        }
    }
 	//START - Added For CR ISTEP2011CR007 (Multiple Test Ticket)
    private void displayKeyBoardPage() throws IOException, DocumentException {
        for(Iterator it=staticKeyboardTables.iterator(); it.hasNext();){
            TableVO table = (TableVO)it.next();
            write(table.getTable(), 0, table.getEnd(), table.getX(), table.getY());
        }
    }
    
    private void createPage() throws IOException, DocumentException {
    	  lastStudent = hasNextPage();
          if(!isMultiIndividualTkt) {
         	 displayKeyBoardPage();
          }
          displayPage();
          if(isMultiIndividualTkt) {
              if(hasNextPage()){
              	currentPageIndex++;
              	displayPage();
              }
          }
    }
    //END - Added For CR ISTEP2011CR007 (Multiple Test Ticket)
    
    private boolean hasNextPage(){
        return this.currentPageIndex < this.totalPages - 1;
    }
    
    protected String getNonBlankString(String in){
        return (in == null || in.length() == 0) ? " " : in;
    }
    
    protected PdfWriter getWriter() throws DocumentException{
        if(this.writer == null){
            this.writer = PdfWriter.getInstance(document, out);
        }
        return this.writer;
    }
    protected void write(PdfPTable table, int start, int end, float x, float y) throws DocumentException{
        table.writeSelectedRows(start, end, x, y, writer.getDirectContent());
    }
    
    protected void writeImage(String imageUrl, float x, float y) throws DocumentException, IOException{
        try {
            this.scheme = "http"; // hard code for SSL connection issue.
    //        URL url = new URL(this.scheme, server, port.intValue(), imageUrl);
            URL url = new URL(this.scheme, server, imageUrl);
            Image img = Image.getInstance(url);
            img.setAbsolutePosition(x, y);
            document.add(img);
        }
        catch (FileNotFoundException e) { // don't throw exception in case file not found
            e.printStackTrace();
            
        }
    }
    protected void close(){
        document.close();
    }
 } 
