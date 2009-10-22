package com.ctb.widgets.taglib.table2; 

import com.ctb.widgets.taglib.BaseTag;
import com.ctb.widgets.taglib.utils.TagLibUtils;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

public class SimpleTableContainerTag extends BaseTableContainerTag
{ 
    // Member variables 
    private static final String STYLE_CLASS = "s";
    
    // Tag attributes

    // Tag attribute getters/setters
    


    
    // Lifecycle --------------------------------------------------------------

    public int doStartTag() {

        try {
            JspWriter out = pageContext.getOut();

            out.print( "<div class=\"" + STYLE_CLASS + "\">" );

        } catch( IOException ioe ) {
            System.err.println("IOException caught within doStartTag of SimpleTableContainerTag handler!");
            System.err.println(ioe);
        }

        return this.EVAL_BODY_INCLUDE;
    }


    public int doEndTag() {
        try {
            JspWriter out = pageContext.getOut();

            out.print( "</div>" );

        } catch( IOException ioe ) {
            System.err.println("IOException caught within doEndTag of SimpleTableContainerTag handler!");
            System.err.println(ioe);
        }

        return this.EVAL_BODY_INCLUDE;
    }
    
} 
