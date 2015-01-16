package util; 

import com.itextpdf.text.pdf.PdfPTable;

public class PDFTableVO implements java.io.Serializable
{ 
    static final long serialVersionUID = 1L;

    private PdfPTable table = null;
    private int start = 0;
    private int end = 0;
    private float x = 0f;
    private float y = 0f;
    private int rows = 0;
    private float height = 0f;
        
    public PDFTableVO(PdfPTable table, 
                   int start, 
                   int end, 
                   float x, 
                   float y,
                   int rows) {
        this.table = table;
        this.start = start;
        this.end = end;
        this.x = x;
        this.y = y;
        this.rows = rows;
    }
    
    public PDFTableVO(PdfPTable table, 
	            int start, 
	            int end, 
	            float x, 
	            float y,
	            int rows,
	            float height) {
		 this.table = table;
		 this.start = start;
		 this.end = end;
		 this.x = x;
		 this.y = y;
		 this.rows = rows;
		 this.height = height;
	}

    public PDFTableVO(PDFTableVO src) {
        this.table = src.getTable();
        this.start = src.getStart();
        this.end = src.getEnd();
        this.x = src.getX();
        this.y = src.getY();
        this.rows = src.getRows();
   }

    public PdfPTable getTable() {
        return this.table;
    }
    public void setTxt(PdfPTable table) {
        this.table = table;
    }
    public int getStart() {
        return this.start;
    }
    public void setStart(int start) {
        this.start = start;
    }
    public int getEnd() {
        return this.end;
    }
    public void setEnd(int end) {
        this.end = end;
    }
    public float getX() {
        return this.x;
    }
    public void setX(float x) {
        this.x = x;
    }
    public float getY() {
        return this.y;
    }
    public void setY(float y) {
        this.y = y;
    }
    public int getRows() {
        return this.rows;
    }
    public void setRows(int rows) {
        this.rows = rows;
    }
	public float getHeight() {
		return height;
	}
	public void setHeight(float height) {
		this.height = height;
	}
} 
