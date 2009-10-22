package com.ctb.widgets.taglib.table2; 

import com.ctb.widgets.taglib.BaseTag;
import com.ctb.widgets.taglib.utils.TagLibUtils;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

public class FilterTag extends BaseTag
{ 
    // Member variables --------------------------------------------------------
    public static final String DIV_PREFIX = "filterTagDiv_";


    // Tag attributes --------------------------------------------------------
    private String visibleDataSource; 
    
    // Tag attribute setters -------------------------------------------------
    public void setVisibleDataSource( String visibleDataSource ) {
        this.visibleDataSource = visibleDataSource;
    }



    // Life-cycle --------------------------------------------------------------

    public int doStartTag() {
        
        try {
            TagLibUtils tlu = new TagLibUtils( (HttpServletRequest) pageContext.getRequest(), this, this.pageContext );
            Object visibleObject = tlu.resolveVariable( visibleDataSource );
            boolean visibleValue;

            String divId = DIV_PREFIX + visibleDataSource;
            String divStyle;
    
            if( visibleObject instanceof Boolean ) {
                visibleValue = ((Boolean) visibleObject).booleanValue();
            } else if( visibleObject instanceof String ) {
                visibleValue = Boolean.valueOf( visibleObject.toString() ).booleanValue();
            } else {
                visibleValue = false;
            }
            
            if( visibleValue)
                divStyle = "display: block; ";
            else
                divStyle = "display: none; ";

            JspWriter out = pageContext.getOut();
            out.print( hiddenElement(visibleDataSource, String.valueOf(visibleValue) ) );
            out.print( "<div id=\"" + divId + "\" style=\"" + divStyle + "\" >" );
            
        } catch( IOException ioe ) {
            System.err.println("IOException caught within doStartTag of FilterTag handler!");
            System.err.println(ioe);
        } catch( JspException jspe ) {
            System.err.println("JspException caught within doStartTag of FilterTag handler!");
            System.err.println(jspe);
        }
        
        return this.EVAL_BODY_INCLUDE;
    }
    
    
    
    public int doEndTag() {

        try {
            JspWriter out = pageContext.getOut();
            out.print( "</div>" );
        }
        catch( IOException ioe ) {
            System.err.println("IOException caught within doEndTag of FilterTag handler!");
            System.err.println(ioe);
        }
        
        return this.SKIP_BODY;

    }

} 
