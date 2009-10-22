package com.ctb.widgets.taglib.table2; 

import com.ctb.widgets.taglib.BaseTag;
import com.ctb.widgets.taglib.utils.TagLibUtils;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

public class ComplexTableContainerTag extends BaseTableContainerTag
{ 
    // Member variables ------------------------------------------------------
    private static final String STYLE_CLASS = "c";
    private Integer pageRequested;    
    private String columnSort;
    private String columnOrderBy;

    // Methods ---------------------------------------------------------------
    public Integer getPageRequested() {
        return this.pageRequested;
    }
    
    public String getColumnSort() {
        return this.columnSort;
    }
    
    public String getColumnOrderBy() {
        return this.columnOrderBy;
    }
    
    public String getPageResetJavaScript() {
        if( pageRequested != null )
            return "setElementValue('" + pageRequestedDataSource + "', '1'); ";
        else
            return "";
    }
    
    
    // Tag attributes --------------------------------------------------------
    private String pageRequestedDataSource;
    private String columnSortDataSource;
    private String columnOrderByDataSource;

    // Tag attribute setters/getters -----------------------------------------
    public void setPageRequestedDataSource(String pageRequestedDataSource) {
        this.pageRequestedDataSource = pageRequestedDataSource;
    }

    public String getPageRequestedDataSource() {
        return this.pageRequestedDataSource;
    }

    public void setColumnSortDataSource(String columnSortDataSource) {
        this.columnSortDataSource = columnSortDataSource;
    }

    public String getColumnSortDataSource() {
        return this.columnSortDataSource;
    }

    public void setColumnOrderByDataSource(String columnOrderByDataSource) {
        this.columnOrderByDataSource = columnOrderByDataSource;
    }

    public String getColumnOrderByDataSource() {
        return this.columnOrderByDataSource;
    }

    
    // Lifecycle --------------------------------------------------------------

    public int doStartTag() {

        try {
            TagLibUtils tlu = new TagLibUtils( (HttpServletRequest) pageContext.getRequest(), this, this.pageContext );
            
            if( pageRequestedDataSource != null )
                pageRequested = (Integer) tlu.resolveVariable(pageRequestedDataSource);
            if( columnSortDataSource != null )
                columnSort    = (String)  tlu.resolveVariable(columnSortDataSource);
            if( columnOrderByDataSource != null )
                columnOrderBy = (String)  tlu.resolveVariable(columnOrderByDataSource);

            JspWriter out = pageContext.getOut();
            out.print( "<div class=\"" + STYLE_CLASS + "\">" );
            if( pageRequested != null )
                out.print( hiddenElement( pageRequestedDataSource, pageRequested ) );
            if( columnSort != null )
                out.print( hiddenElement( columnSortDataSource, columnSort ) );
            if( columnOrderBy != null )
                out.print( hiddenElement( columnOrderByDataSource, columnOrderBy ) );

        } catch( JspException jspe ) {
            System.err.println("JspException caught within doStartTag of " + this.getClass().getName() + " handler!");
            System.err.println(jspe);
        } catch( IOException ioe ) {
            System.err.println("IOException caught within doStartTag of " + this.getClass().getName() + " handler!");
            System.err.println(ioe);
        }

        return this.EVAL_BODY_INCLUDE;
    }


    public int doEndTag() {
        try {
            JspWriter out = pageContext.getOut();

            out.print( "</div>" );

        } catch( IOException ioe ) {
            System.err.println("IOException caught within doEndTag of " + this.getClass().getName() + " handler!");
            System.err.println(ioe);
        }

        return this.EVAL_BODY_INCLUDE;
    }
    
} 
