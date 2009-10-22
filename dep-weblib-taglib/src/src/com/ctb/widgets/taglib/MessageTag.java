package com.ctb.widgets.taglib; 

import com.ctb.widgets.taglib.utils.TagLibUtils;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import org.apache.struts.action.ActionForm;

public class MessageTag extends WidgetsBaseTag
{ 
    private static final String STYLE_CLASS_INFO  = "informationMessage";
    private static final String STYLE_CLASS_ALERT = "alertMessage";
    private static final String STYLE_CLASS_ERROR = "errorMessage";
    private static final String STYLE_CLASS_TABLE = "tableMessage";
    
    private final String IMG_INFO  = "resources/images/messaging/icon_info.gif";
    private final String IMG_ALERT = "resources/images/messaging/icon_alert.gif";
    private final String IMG_ERROR = "resources/images/messaging/icon_error.gif";
    private final String IMG_TABLE = "resources/images/messaging/icon_info.gif";
    
    
    private String style;
    private String title;
    
    public void setStyle( String style ) {
        this.style = style;
    }
    
    public void setTitle( String title ) {
        this.title = title;
    }
    
    
    
    public int doStartTag() {
        
        try {
            TagLibUtils tlu = new TagLibUtils( (HttpServletRequest) pageContext.getRequest(), this, this.pageContext );
            String titleValue = (String) tlu.resolveVariable( this.title );
            String img;
            
            if( this.style.equals( this.STYLE_CLASS_INFO ) ) {
                img = messageImage( this.IMG_INFO, true );
            } else if( this.style.equals( this.STYLE_CLASS_TABLE) ) {
                img = messageImage( this.IMG_TABLE, true );
            } else if( this.style.equals( this.STYLE_CLASS_ALERT) ) {
                img = messageImage( this.IMG_ALERT, false );
            } else {
                // default to error to catch attention
                img = messageImage( this.IMG_ERROR, false );
            }
            
            
            JspWriter out = pageContext.getOut();
            out.print( "<div class=\"" + this.style + "\">" );
            out.print( "<table>" );
            out.print( "<tr>" );
            out.print( "<th rowspan=\"2\">" + img + "</th> " );
            out.print( "<th>" + titleValue + "</th>" );
            out.print( "</tr>" );
            out.print( "<tr>" );
            out.print( "<td>" );
            

        } catch( JspException jspe ) {
            System.err.println("JSPException caught within doStartTag of AnchorFormInput handler!");
            System.err.println(jspe);
        } catch( IOException ioe ) {
            System.err.println("IOException caught within doStartTag of AnchorFormInput handler!");
            System.err.println(ioe);
        }
        
        return this.EVAL_BODY_INCLUDE;
    }
    

    public int doEndTag() {
        
        try {
            JspWriter out = pageContext.getOut();
            out.print( "</td>");
            out.print( "</tr>");
            out.print( "</table>");
            out.print( "</div>");

        } catch( IOException ioe ) {
            System.err.println("IOException caught within doEndTag of AnchorFormInput handler!");
            System.err.println(ioe);
        }
        
        return this.SKIP_BODY;
    }


    private String messageImage(String image, boolean smallSize) {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        String contextPath = request.getContextPath();
    
        if (smallSize)       
            return "<img src=\"" + contextPath + "/" + image + "\" border=\"0\" width=\"23\" height=\"23\">";
        else
            return "<img src=\"" + contextPath + "/" + image + "\" border=\"0\" width=\"32\" height=\"32\">";
    }


} 
