package com.ctb.widgets.taglib; 

import com.ctb.widgets.taglib.utils.TagLibUtils;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import org.apache.struts.action.ActionForm;

public class TablePathEntryTag extends WidgetsBaseTag
{ 
    private String srcLabelDataSource;
    private String srcValueDataSource;
    private String dstLabelDataSource;
    private String dstValueDataSource;
    private String shownAsLink = "true";
    
    public void setSrcLabelDataSource( String srcLabelDataSource ) {
        this.srcLabelDataSource = srcLabelDataSource;
    }
    
    public void setSrcValueDataSource( String srcValueDataSource ) {
        this.srcValueDataSource = srcValueDataSource;
    }
    
    public void setDstLabelDataSource( String dstLabelDataSource ) {
        this.dstLabelDataSource = dstLabelDataSource;
    }
    
    public void setDstValueDataSource( String dstValueDataSource ) {
        this.dstValueDataSource = dstValueDataSource;
    }
    
    public void setShownAsLink( String shownAsLink ) {
        this.shownAsLink = shownAsLink;
    }
    
    
    public int doStartTag() {
        
        try {
            TagLibUtils tlu = new TagLibUtils( (HttpServletRequest) pageContext.getRequest(), this, this.pageContext );
            String srcLabel = (String) tlu.resolveVariable( srcLabelDataSource );
            Object srcValue =          tlu.resolveVariable( srcValueDataSource );
            String hasLink = (String) tlu.resolveVariable( shownAsLink ); 
            String js = "";
            
            String labelDS = "{" + this.dstLabelDataSource + "}";
            String valueDS = "{" + this.dstValueDataSource + "}";
            
            js += "setElementValue('" + labelDS + "', '" + tlu.escapeVariable(srcLabel) + "'); ";
            js += "setElementValueAndSubmit('" + valueDS + "', '" + srcValue.toString() + "'); ";
            
            JspWriter out = pageContext.getOut();
            if ( hasLink.equalsIgnoreCase("true") ) {
                out.print( "<a href=\"#\" onclick=\"" + js + " return false;\">" );
                out.print( srcLabel );
                out.print( "</a>" );
            }
            else {
                out.print( srcLabel );
            }

        } catch( JspException jspe ) {
            System.err.println("JSPException caught within doStartTag of TablePathEntryTag handler!");
            System.err.println(jspe);
        } catch( IOException ioe ) {
            System.err.println("IOException caught within doStartTag of TablePathEntryTag handler!");
            System.err.println(ioe);
        }
        
        return this.SKIP_BODY;
    }


} 
