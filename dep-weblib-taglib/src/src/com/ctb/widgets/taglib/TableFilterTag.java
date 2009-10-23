package com.ctb.widgets.taglib; 

import com.ctb.widgets.taglib.utils.TagLibUtils;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

public class TableFilterTag extends WidgetsBaseTag
{ 
    public static final String DIV_PREFIX = "filterTagDiv_";
    
    private String dataSource;
    
    
    public void setDataSource( String dataSource ) {
        this.dataSource = dataSource;
    }

    public String getDataSource( ) {
        return this.dataSource;
    }



    public int doStartTag() {
        
        try {
            TagLibUtils tlu = new TagLibUtils( (HttpServletRequest) pageContext.getRequest(), this, this.pageContext );
            
            Boolean filterVisible = Boolean.TRUE;
            String filterVisibleStr;
            String expression = this.getDataSource();
            
            if (isOldStyle(this.getDataSource())) {                        
            	filterVisibleStr = (String) tlu.resolveVariable( this.getDataSource() );
                filterVisible = new Boolean(filterVisibleStr);
            }
            else {
            	filterVisible = (Boolean) tlu.performResolveVariable( this.getDataSource() );            	
            	expression = addBrackets(this.getDataSource());
            }
            
            
            JspWriter out = pageContext.getOut();

            String divId = this.DIV_PREFIX + expression;
            String divStyle;
            
            if( filterVisible.booleanValue() )
                divStyle = "display: block; ";
            else
                divStyle = "display: none; ";


            out.print( hiddenElement(expression, filterVisible) );
            out.print( "<div id=\"" + divId + "\" style=\"" + divStyle + "\" >" );
        } catch( IOException ioe ) {
            System.err.println("IOException caught within doStartTag of TableFilterTag handler!");
            System.err.println(ioe);
        } catch( JspException jspe ) {
            System.err.println("JspException caught within doStartTag of TableFilterTag handler!");
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
            System.err.println("IOException caught within doEndTag of TableFilterTag handler!");
            System.err.println(ioe);
        }
        
        return this.SKIP_BODY;

    }

} 
