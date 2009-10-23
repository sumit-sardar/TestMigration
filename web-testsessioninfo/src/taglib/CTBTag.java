/*
 * Created on Mar 31, 2004
 *
 */
package taglib;

import java.io.IOException;
import java.util.Date;

import javax.servlet.jsp.tagext.TagSupport;

 
/**
 * @author Tai Truong
 *
 */
 
public class CTBTag extends TagSupport {
	protected static final int NO_ROWSPAN = -1;
	protected static final int NO_COLSPAN = -1;
	protected static final int NO_HEIGHT = -1;
	protected static final int NO_WIDTH = -1;
	
	protected static final int NO_BORDER = 0;
	protected static final int NO_CELLPADDING = 0;
	protected static final int NO_CELLSPACING = 0;
	
	protected static final String ALIGN_RIGHT = "right";
	protected static final String ALIGN_LEFT = "left";
	protected static final String ALIGN_CENTER = "center";
	protected static final String VALIGN_TOP = "top";
	protected static final String VALIGN_MIDDLE = "middle";
	protected static final String VALIGN_BOTTOM = "bottom";
	protected static final String VALIGN_BASELINE = "baseline";
	
	protected static final String NO_CLASS = null;
	protected static final String DEFAULT_WIDTH = "100";
	protected static final String DEFAULT_HEIGHT = "20";
	protected static final String RADIO_SIZE = "5%";
	protected static final int DEFAULT_CELLSPACING = 0;




    // Table Tag - <table>
	protected void displayTableStart() throws IOException {
		displayTableStart("transparent");
	}
    
	protected void displayTableStart(String class_) throws IOException {
		StringBuffer buff = new StringBuffer();
		buff.append("<table");
		if(class_ != null){
			buff.append(" class=\"" + class_ + "\"");
		}
		buff.append(">");
		writeToPage(buff.toString());	
	}
    
	protected void displayTableStart(String class_, String height_, String align_) throws IOException {
		StringBuffer buff = new StringBuffer();
		buff.append("<table");
		if(class_ != null){
			buff.append(" class=\"" + class_ + "\"");
		}
		if(height_ != null){
			buff.append(" height=\"" + height_ + "\"");
		}
		if(align_ != null){
			buff.append(" align=\"" + align_ + "\"");
		}
		buff.append(">");
		writeToPage(buff.toString());	
	}
    
	protected void displayTableEnd() throws IOException {
		writeToPage("</table>");
		writeToPage("\n");
	}


	
    
    // Row Tag - <tr>
	protected void displayRowStart() throws IOException {
        displayRowStart("transparent");
    }
        
	protected void displayRowStart(String class_) throws IOException {
		StringBuffer buff = new StringBuffer();
		buff.append("<tr");
		if(class_ != null){
			buff.append(" class=\"" + class_ + "\"");
		}
		buff.append(">");
		writeToPage(buff.toString());	
	}

	protected void displayRowStart(String class_, String valign_) throws IOException {
		StringBuffer buff = new StringBuffer();
		buff.append("<tr");
		if(class_ != null){
			buff.append(" class=\"" + class_ + "\"");
		}
		if(valign_ != null){
			buff.append(" valign=\"" + valign_ + "\"");
		}
		buff.append(">");
		writeToPage(buff.toString());	
	}

	protected void displayRowStart(String class_, String height_, String valign_) throws IOException {
		StringBuffer buff = new StringBuffer();
		buff.append("<tr");
		if(class_ != null){
			buff.append(" class=\"" + class_ + "\"");
		}
		if(height_ != null){
			buff.append(" height=\"" + height_ + "\"");
		}
		if(valign_ != null){
			buff.append(" valign=\"" + valign_ + "\"");
		}
		buff.append(">");
		writeToPage(buff.toString());	
	}
    
	protected void displayRowEnd() throws IOException {
		writeToPage("</tr>");
		writeToPage("\n");
	}

    
    

    // Column Tag - <td>
	protected void displayCellStart() throws IOException {
	    displayCellStart("transparent");
	}

	protected void displayTableCellStart() throws IOException {
		StringBuffer buff = new StringBuffer();
		buff.append("<td");
        buff.append(" class=\"dynamicFrame\" width=\"47%\" ");
		buff.append(">");
		writeToPage(buff.toString());
	}

	protected void displayCellStart(String class_) throws IOException {
		StringBuffer buff = new StringBuffer();
		buff.append("<td");
		if(class_ != null)
			buff.append(" class=\"" + class_ + "\"");
		buff.append(">");
		writeToPage(buff.toString());
	}

	protected void displayCellStart(String class_, String width_) throws IOException {
		StringBuffer buff = new StringBuffer();
		buff.append("<td");
		if(class_ != null)
			buff.append(" class=\"" + class_ + "\"");
		if(width_ != null)
			buff.append(" width=\"" + width_ + "\""); 
		buff.append(" valign=\"top\" >");
		writeToPage(buff.toString());
	}

	protected void displayCellStart(String class_, String width_, String align_) throws IOException {
		StringBuffer buff = new StringBuffer();
		buff.append("<td");
		if(class_ != null)
			buff.append(" class=\"" + class_ + "\"");
		if(width_ != null)
			buff.append(" width=\"" + width_ + "\""); 
		if(align_ != null)
			buff.append(" align=\"" + align_ + "\""); 
		buff.append(" >");
		writeToPage(buff.toString());
	}

	protected void displayCellStart(String class_, String width_, String align_, String height_) throws IOException {
		StringBuffer buff = new StringBuffer();
		buff.append("<td");
		if(class_ != null)
			buff.append(" class=\"" + class_ + "\"");
		if(width_ != null)
			buff.append(" width=\"" + width_ + "\""); 
		if(align_ != null)
			buff.append(" align=\"" + align_ + "\""); 
		if(height_ != null)
			buff.append(" height=\"" + height_ + "\""); 
		buff.append(" >");
		writeToPage(buff.toString());
	}

	protected void displayCellStart(String class_, String width_, String align_, String height_, String colspan_) throws IOException {
		StringBuffer buff = new StringBuffer();
		buff.append("<td");
		if(class_ != null)
			buff.append(" class=\"" + class_ + "\"");
		if(width_ != null)
			buff.append(" width=\"" + width_ + "\""); 
		if(align_ != null)
			buff.append(" align=\"" + align_ + "\""); 
		if(height_ != null)
			buff.append(" height=\"" + height_ + "\""); 
		if(colspan_ != null)
			buff.append(" colspan=\"" + colspan_ + "\""); 
		buff.append(" >");
		writeToPage(buff.toString());
	}

	protected void displayCellEnd() throws IOException {
		writeToPage("</td>");
	}

	protected void displayHeaderStart(String class_) throws IOException {
		StringBuffer buff = new StringBuffer();
		buff.append("<th");
		if(class_ != null)
			buff.append(" class=\"" + class_ + "\"");
		buff.append(" height=\"25\" >");
		writeToPage(buff.toString());
	}

	protected void displayHeaderStart(String class_, String width_) throws IOException {
		StringBuffer buff = new StringBuffer();
		buff.append("<th");
		if(class_ != null)
			buff.append(" class=\"" + class_ + "\"");
		if(width_ != null)
			buff.append(" width=\"" + width_ + "\"");
		buff.append(" height=\"25\" >");
		writeToPage(buff.toString());
	}

	protected void displayHeaderEnd() throws IOException {
		writeToPage("</th>");
	}

	// miscellance
	protected void writeToPage(String value) throws IOException {
		pageContext.getOut().print(value);
	}
    
	protected String checkBox(String name, String value, boolean isChecked, String onClick) {
		return "<input type=\"checkbox\" name=\"" + name + "\" id=\"" + name + "\" " + 
				"value=\""+ value + "\" " +
			(isChecked?"checked=\"true\"":"") + 
            " onClick=\"" + onClick + "\" />";
	}
		
	
}
