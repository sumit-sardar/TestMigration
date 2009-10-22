package com.ctb.widgets.taglib; 

import com.ctb.widgets.bean.ColumnSortEntry;
import com.ctb.widgets.taglib.utils.TagLibUtils;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;
import org.apache.struts.action.ActionForm;

public class TableColumnSortTag extends WidgetsBaseTag
{ 
    private static final String IMG_SORT_ASC  = "resources/images/columnsort/up.gif";
    private static final String IMG_SORT_DESC = "resources/images/columnsort/down.gif";    
    private static final String IMG_SORT_NONE = "resources/images/columnsort/none.gif";
    private static final String STYLE_CLASS_SORTED = "currentSort";
    private static final String STYLE_CLASS_NOT_SORTED = "notCurrentSort";

     
    private ColumnSortEntry currentColumnSort;

    private String value;
    
    public void setValue( String value ) {
        this.value = value;
    }
    
    
    
    public int doStartTag() {
        
        try {
            TagLibUtils tlu = new TagLibUtils( (HttpServletRequest) pageContext.getRequest(), this, this.pageContext );
            
            Tag tag = this.getParent();
            while (! tag.getClass().isInstance(new TableColumnSortGroupTag())) 
                tag = tag.getParent();
            
            TableColumnSortGroupTag parentTag = (TableColumnSortGroupTag )tag;                    

            String expressionCDS = parentTag.getColumnDataSource();
            String expressionOBDS = parentTag.getOrderByDataSource();
            
            this.currentColumnSort = new ColumnSortEntry();
            
            if (isOldStyle(expressionCDS)) {            
            	this.currentColumnSort.setValue( (String) tlu.resolveVariable(expressionCDS) );
            	this.currentColumnSort.setOrderBy( (String) tlu.resolveVariable(expressionOBDS) );
            }
            else {
            	this.currentColumnSort.setValue( (String) tlu.performResolveVariable(expressionCDS) );
            	this.currentColumnSort.setOrderBy( (String) tlu.performResolveVariable(expressionOBDS) );            	
                expressionCDS = addBrackets(parentTag.getColumnDataSource());
                expressionOBDS = addBrackets(parentTag.getOrderByDataSource());
            }
            
            // change the sort order and style when this is the current sort
            String newOrderByValue = ColumnSortEntry.ASCENDING;
            String style = this.STYLE_CLASS_NOT_SORTED;

            if( this.currentColumnSort.getValue() != null && this.currentColumnSort.getValue().equals( this.value ) ) {

                style = this.STYLE_CLASS_SORTED;

                if( this.currentColumnSort.getOrderBy().equals( ColumnSortEntry.ASCENDING ) ) 
                    newOrderByValue = ColumnSortEntry.DESCENDING;

            }

            
            String js = "";
            js += "setElementValue('" + expressionCDS + "', '" + this.value + "');";
            if (parentTag.getAnchorName() != null)            
                js += "setElementValueAndSubmitWithAnchor('" + expressionOBDS + "', '" +  newOrderByValue + "', '" + parentTag.getAnchorName() + "');";
            else
                js += "setElementValueAndSubmit('" + expressionOBDS + "', '" +  newOrderByValue + "');";


            JspWriter out = pageContext.getOut();
            out.print( "<div class=\"" + style + "\"><a href=\"#\" onclick=\"" + js + " return false;\" >" );

        } catch( IOException ioe ) {
            System.err.println("IOException caught within doStartTag of TableColumnSortTag handler!");
            System.err.println(ioe);
        } catch( JspException jspe) {
            System.err.println("JspException caught within doStartTag of TableColumnSortTag handler!");
            System.err.println(jspe);
        }
        
        return this.EVAL_BODY_INCLUDE;
    }
    
    public int doEndTag() {
        
        try {
            JspWriter out = pageContext.getOut();
            out.print( "</a> ");
            
            if( this.currentColumnSort.getValue().equals( this.value ) ) {
                // Add icon to this column if its the active sort
                if( this.currentColumnSort.getOrderBy().equals( ColumnSortEntry.ASCENDING )  )
                    out.print( sortImage(IMG_SORT_ASC) );
                else
                    out.print( sortImage(IMG_SORT_DESC) );
            } else {
                out.print( sortImage(IMG_SORT_NONE) );
            }
            
            
            out.print("</div>");
            
        } catch( IOException ioe ) {
            System.err.println("IOException caught within doEndTag of TableColumnSortTag handler!");
            System.err.println(ioe);
        }
        
        return this.SKIP_BODY;
    }
    
    private String sortImage(String imageResource) {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        String contextPath = request.getContextPath();
    
        return "<img src=\"" + contextPath + "/" + imageResource + "\" border=\"0\" width=\"10\" height=\"10\" >";
    }
} 
