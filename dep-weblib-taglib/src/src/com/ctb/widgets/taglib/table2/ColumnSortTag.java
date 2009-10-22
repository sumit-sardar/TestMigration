package com.ctb.widgets.taglib.table2; 

import com.ctb.widgets.taglib.BaseTag;
import com.ctb.widgets.taglib.utils.TagLibUtils;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

public class ColumnSortTag extends BaseTag
{ 
    // Member variables --------------------------------------------------------
    private static final String IMG_SORT_ASC  = "resources/images/columnsort/up.gif";
    private static final String IMG_SORT_DESC = "resources/images/columnsort/down.gif";    
    private static final String IMG_SORT_NONE = "resources/images/columnsort/none.gif";
    private static final String ASCENDING = "ASC";
    private static final String DESCENDING = "DESC";
    private static final String STYLE_CLASS_SORTED = "currentSort";
    private static final String STYLE_CLASS_NOT_SORTED = "notCurrentSort";
        
    private ComplexTableContainerTag containerTag;
    private String sortOrderImg;
    private String sortStyleClass;

        
    // Tag attributes --------------------------------------------------------
    private String value;
    private String styleClass;
    
    // Tag attribute setters -------------------------------------------------
    public void setValue( String value ) {
        this.value = value;
    }
    
    public void setStyleClass( String styleClass ) {
        this.styleClass = styleClass;
    }


    
    public int doStartTag() {
        
        try {
            containerTag = (ComplexTableContainerTag) this.findAncestorWithClass(this, ComplexTableContainerTag.class);
            
            if( containerTag == null ) 
                throw new JspException("ColumnSortTag not within any table container tags and will not render!");
            else if( containerTag.getColumnSort() == null )
                throw new JspException("ColumnSortTag within a table container tag without non-null column sort!");
            else if( containerTag.getColumnOrderBy() == null )
                throw new JspException("ColumnSortTag within a table container tag without non-null column orderBy!");
        
            
            TagLibUtils tlu = new TagLibUtils( (HttpServletRequest) pageContext.getRequest(), this, this.pageContext );
            
            // Set the default style, image, and new orderBy.
            String newOrderBy = ASCENDING;
            sortOrderImg = IMG_SORT_NONE;
            sortStyleClass = STYLE_CLASS_NOT_SORTED;

            // Change style, image, and new order based on container settings
            if( containerTag.getColumnSort() != null ) {
                
                if( containerTag.getColumnSort().equalsIgnoreCase( value ) ) {
                    sortStyleClass = STYLE_CLASS_SORTED;
                    
                    if( containerTag.getColumnOrderBy().equalsIgnoreCase( ASCENDING ) ) {
                        sortOrderImg = IMG_SORT_ASC;
                        newOrderBy = DESCENDING;
                    } else if( containerTag.getColumnOrderBy().equalsIgnoreCase( DESCENDING ) ){
                        sortOrderImg = IMG_SORT_DESC;
                        newOrderBy = ASCENDING;
                    }
                    
                }
            }
            
            
            String js = containerTag.getPageResetJavaScript();
            js += "setElementValue('" + containerTag.getColumnSortDataSource() + "', '" + this.value + "');";
            js += "setElementValueAndSubmit('" + containerTag.getColumnOrderByDataSource() + "', '" +  newOrderBy + "');";

            JspWriter out = pageContext.getOut();
            out.print( "<th class=\"" + sortStyleClass + " " + styleClass + "\"><a href=\"#\" onclick=\"" + js + " return false;\" >" );

        } catch( IOException ioe ) {
            System.err.println("IOException caught within doStartTag of ColumnSortTag handler!");
            System.err.println(ioe);
        } catch( JspException jspe) {
            System.err.println("JspException caught within doStartTag of ColumnSortTag handler!");
            System.err.println(jspe);
        }
        
        return this.EVAL_BODY_INCLUDE;
    }
    
    public int doEndTag() {
        
        try {
            JspWriter out = pageContext.getOut();

            out.print( "</a> ");
            out.print( sortImage(sortOrderImg) );
            out.print("</th>");
            
        } catch( IOException ioe ) {
            System.err.println("IOException caught within doEndTag of ColumnSortTag handler!");
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
