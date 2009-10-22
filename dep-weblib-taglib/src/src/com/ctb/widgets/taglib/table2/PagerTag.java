package com.ctb.widgets.taglib.table2; 

import com.ctb.widgets.taglib.BaseTag;
import com.ctb.widgets.taglib.utils.TagLibUtils;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

public class PagerTag extends BaseTag
{ 
    // Member variables --------------------------------------------------------
    private static final String STYLE_CLASS = "pager";
    private static final String STYLE_CLASS_ALIGN_LEFT = "alignLeft";
    private static final String STYLE_CLASS_ALIGN_RIGHT = "alignRight";
    private static final String STYLE_CLASS_ALIGN_CENTER = "alignCenter";
    private static final String STYLE_CLASS_BUTTON = "button";
    private static final String STYLE_CLASS_INPUT_FIELD = "textField";
    
    private static final String RESOURCE_BUTTON_FIRST = "{bundle.widgets['pager.button.first']}";
    private static final String RESOURCE_BUTTON_PREV  = "{bundle.widgets['pager.button.prev']}";
    private static final String RESOURCE_BUTTON_NEXT  = "{bundle.widgets['pager.button.next']}";
    private static final String RESOURCE_BUTTON_LAST  = "{bundle.widgets['pager.button.last']}";
    private static final String RESOURCE_BUTTON_GO    = "{bundle.widgets['pager.button.go']}";
    private static final String RESOURCE_TOTAL        = "{bundle.widgets['pager.total']}";
    private static final String RESOURCE_FILTERED     = "{bundle.widgets['pager.filtered']}";
    private static final String RESOURCE_LOCATION     = "{bundle.widgets['pager.location']}";
    
    private TagLibUtils tlu;
    private ComplexTableContainerTag containerTag;
    private Integer totalFound;
    private Integer totalFiltered;
    private Integer totalPages;
    private String objectLabel;
    
        
    // Tag attributes --------------------------------------------------------
    private String totalFoundDataSource;
    private String totalFilteredDataSource;
    private String totalPagesDataSource;
    private String objectLabelDataSource;

    // Tag attribute setters -------------------------------------------------
    public void setTotalFoundDataSource(String totalFoundDataSource) {
        this.totalFoundDataSource = totalFoundDataSource;
    }

    public void setTotalFilteredDataSource(String totalFilteredDataSource) {
        this.totalFilteredDataSource = totalFilteredDataSource;
    }

    public void setTotalPagesDataSource(String totalPagesDataSource) {
        this.totalPagesDataSource = totalPagesDataSource;
    }

    public void setObjectLabelDataSource(String objectLabelDataSource) {
        this.objectLabelDataSource = objectLabelDataSource;
    }    
    
    
    // Life-cycle --------------------------------------------------------------
    
    public int doStartTag() {
        try {
            // Confirm we are within a container
            containerTag = (ComplexTableContainerTag) this.findAncestorWithClass(this, ComplexTableContainerTag.class);
            if( containerTag == null )
                throw new JspException("PagerTag not within any table container tags and will not render!");
            else if( containerTag.getPageRequested() == null )
                throw new JspException("PagerTag found null for pageRequested within ancestor table container tag!");

            // Resolve variables to values            
            tlu = new TagLibUtils( (HttpServletRequest) pageContext.getRequest(), this, this.pageContext );

            totalFound = (Integer) tlu.resolveVariable( totalFoundDataSource );
            totalPages = (Integer) tlu.resolveVariable( totalPagesDataSource );
            if( totalFilteredDataSource != null )
                totalFiltered = (Integer) tlu.resolveVariable( totalFilteredDataSource );
            if( objectLabelDataSource != null )
                objectLabel = (String) tlu.resolveVariable( objectLabelDataSource );
            
            
            JspWriter out = pageContext.getOut();
            out.print( "<table class=\"" + STYLE_CLASS + "\">" );
            out.print( "<tr>" );
            
            out.print( "<td class=\"" + STYLE_CLASS_ALIGN_LEFT + "\">" );
            out.print( pagerButtons() );
            out.print( "</td>" );
            
            out.print( "<td class=\"" + STYLE_CLASS_ALIGN_CENTER + "\">" );
            out.print( pagerSummary() );
            out.print( "</td>" );

            out.print( "<td class=\"" + STYLE_CLASS_ALIGN_RIGHT + "\">" );
            out.print( pagerLocation() );
            out.print( "</td>" );

            out.print( "</tr>" );
            out.print( "</table>" );
                        
        } catch( JspException jspe ) {
            System.err.println("JspException caught within doStartTag of PagerTag handler!");
            System.err.println(jspe);
        } catch( IOException ioe ) {
            System.err.println("IOException caught within doStartTag of PagerTag handler!");
            System.err.println(ioe);
        }
        
        return this.SKIP_BODY;
    }
    
    
    // Private methods ---------------------------------------------------------

    /**
     * 
     */
    private String pagerButtons() {

        // Return nbsp if total pages = 1 or null
        if( totalPages == null || totalPages.intValue() == 1 )
            return "&nbsp;";

        int firstPage, prevPage, nextPage, lastPage;
        int current = containerTag.getPageRequested().intValue();
        int total = totalPages.intValue();

        firstPage = 1;
        prevPage  = (current <= 1) ? 1 : current - 1;
        nextPage  = (current >= total) ? current : current + 1;
        lastPage  = total;

        String html = "";
        String js;
        String firstButton = (String) tlu.resolveVariable(this.RESOURCE_BUTTON_FIRST);
        String prevButton  = (String) tlu.resolveVariable(this.RESOURCE_BUTTON_PREV);
        String nextButton  = (String) tlu.resolveVariable(this.RESOURCE_BUTTON_NEXT);
        String lastButton  = (String) tlu.resolveVariable(this.RESOURCE_BUTTON_LAST);

        if( current <= firstPage ) {
            html += buttonElement(null, firstButton, STYLE_CLASS_BUTTON, null, true) + " ";
            html += buttonElement(null, prevButton, STYLE_CLASS_BUTTON, null, true) + " ";
        } else {
            js = "setElementValueAndSubmit('" + containerTag.getPageRequestedDataSource() + "', '" + String.valueOf(firstPage) + "');";
            html += buttonElement(null, firstButton, STYLE_CLASS_BUTTON, js, false) + " ";

            js = "setElementValueAndSubmit('" + containerTag.getPageRequestedDataSource() + "', '" + String.valueOf(prevPage) + "');";
            html += buttonElement(null, prevButton, STYLE_CLASS_BUTTON, js, false) + " ";
        }

        if( current >= lastPage ) {
            html += buttonElement(null, nextButton, STYLE_CLASS_BUTTON, null, true) + " ";
            html += buttonElement(null, lastButton, STYLE_CLASS_BUTTON, null, true) + " ";
        } else {
            js = "setElementValueAndSubmit('" + containerTag.getPageRequestedDataSource() + "', '" + String.valueOf(nextPage) + "');";
            html += buttonElement(null, nextButton, STYLE_CLASS_BUTTON, js, false) + " ";

            js = "setElementValueAndSubmit('" + containerTag.getPageRequestedDataSource() + "', '" + String.valueOf(lastPage) + "');";
            html += buttonElement(null, lastButton, STYLE_CLASS_BUTTON, js, false) + " ";
        }        
        
        return html;
    }
    
    /**
     * 
     */
    private String pagerSummary() {
        String totalMsg = (String) tlu.resolveVariable( this.RESOURCE_TOTAL );
        String filteredMsg = (String) tlu.resolveVariable( this.RESOURCE_FILTERED );
        String html = totalMsg;
        
        if( objectLabel != null ) 
            html = html.replaceAll( "\\$x", objectLabel );
        else
            html = html.replaceAll( "\\$x", "" );
    
        html = html.replaceAll( "\\$y", totalFound.toString() );
        
        if( totalFiltered != null && totalFiltered.intValue() > 0 && totalFiltered.intValue() != totalFound.intValue() ) {
            html += filteredMsg;
            html = html.replaceAll( "\\$x", totalFiltered.toString() );
        }
        
        return "<span>" + html + "</span>";   
    }
    
    
    /**
     * 
     */
    private String pagerLocation() {
        String goButton = (String) tlu.resolveVariable( this.RESOURCE_BUTTON_GO );
        String html = (String) tlu.resolveVariable( this.RESOURCE_LOCATION );
        String inputFieldName = safeDataSourceName(containerTag.getPageRequestedDataSource() + "_input");
        String inputFieldValue;
        String js;

        // Just show the Page 1 of 1 by default, otherwise, show a text field.
        if( totalPages.intValue() > 1 ) {
            js = "return constrainNumericKeyEvent(event, this);";
            inputFieldValue = textElement( inputFieldName, containerTag.getPageRequested(), STYLE_CLASS_INPUT_FIELD, js, false );
        } else {
            inputFieldValue = "1";
        }
        html = html.replaceAll( "\\$x", inputFieldValue );
        html = html.replaceAll( "\\$y", totalPages.toString() );

        // Don't show the GO button if only one page
        if( totalPages.intValue() > 1 ) {
            js = "setElementValueAndSubmit('" + containerTag.getPageRequestedDataSource() + "', this.form." + inputFieldName + ".value);";
            html += "&nbsp;&nbsp;" + buttonElement(null, goButton, STYLE_CLASS_BUTTON, js, false);
        }
        
        // Wrap since NetUI wraps all labels with spans to be nice with css
        return "<span>" + html + "</span>"; 
    }
    
  
} 
