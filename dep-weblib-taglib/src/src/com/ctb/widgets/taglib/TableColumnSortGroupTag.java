package com.ctb.widgets.taglib; 

import com.ctb.widgets.bean.ColumnSortEntry;
import com.ctb.widgets.taglib.utils.TagLibUtils;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
 
public class TableColumnSortGroupTag extends WidgetsBaseTag
{ 
    private String columnDataSource = null;
    private String orderByDataSource = null;
    private String anchorName = null;
    
    
    public void setColumnDataSource( String columnDataSource ) {
        this.columnDataSource = columnDataSource;
    }

    public String getColumnDataSource( ) {
        return this.columnDataSource;
    }

    public void setOrderByDataSource( String orderByDataSource ) {
        this.orderByDataSource = orderByDataSource;
    }

    public String getOrderByDataSource( ) {
        return this.orderByDataSource;
    }

    public void setAnchorName( String anchorName ) {
        this.anchorName = anchorName;
    }

    public String getAnchorName( ) {
        return this.anchorName;
    }

    public int doStartTag() {
        
        try {
            TagLibUtils tlu = new TagLibUtils( (HttpServletRequest) pageContext.getRequest(), this, this.pageContext );
            
            String expressionCDS = this.getColumnDataSource();
            String expressionOBDS = this.getOrderByDataSource();
            String columnValue;
            String orderByValue;
            
            if (isOldStyle(expressionCDS)) {            
            	columnValue = (String) tlu.resolveVariable( expressionCDS );
            	orderByValue = (String) tlu.resolveVariable( expressionOBDS );
            }
            else {
            	columnValue = (String) tlu.performResolveVariable( expressionCDS );
            	orderByValue = (String) tlu.performResolveVariable( expressionOBDS );            	
                expressionCDS = addBrackets(this.getColumnDataSource());
                expressionOBDS = addBrackets(this.getOrderByDataSource());
            }
            
            JspWriter out = pageContext.getOut();
            
            out.print( hiddenElement( expressionCDS, columnValue ) );
            out.print( hiddenElement( expressionOBDS, orderByValue ) );
        } catch( IOException ioe ) {
            System.err.println("IOException caught within doStartTag of TableSortColumnGroupTag handler!");
            System.err.println(ioe);
        } catch( JspException jspe ) {
            System.err.println("JspException caught within doStartTag of TableSortColumnGroupTag handler!");
            System.err.println(jspe);
        }
        
        return this.EVAL_BODY_INCLUDE;
    }
    
    
    
} 
