package data; 

import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPTable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Iterator;

public class ImageVO implements java.io.Serializable
{ 
    static final long serialVersionUID = 1L;

    private String url = null;
    private float x = 0f;
    private float y = 0f;
        
    public ImageVO(String url, float x, float y) {
        this.url = url;
        this.x = x;
        this.y = y;
   }

    public ImageVO(ImageVO src) {
        this.url = src.getUrl();
        this.x = src.getX();
        this.y = src.getY();
    }

    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
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

} 
