package com.ctb.widgets.taglib; 
 
import com.ctb.widgets.bean.PagerSummary;
import com.ctb.widgets.taglib.utils.TagLibUtils;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

public class TablePagerTag extends WidgetsBaseTag
{ 
    private static final String STYLE_CLASS = "tablePaging";
    private static final String STYLE_CLASS_LEFTSIDE = "tablePagingLeftSide";
    private static final String STYLE_CLASS_RIGHTSIDE = "tablePagingRightSide";
    private static final String STYLE_CLASS_MIDDLE = "tablePagingMiddle";
    private static final String STYLE_CLASS_BUTTON = "button";
    private static final String STYLE_CLASS_INPUT_FIELD = "pagingField";
    
    private static final String RESOURCE_BUTTON_FIRST = "{bundle.widgets['pager.button.first']}";
    private static final String RESOURCE_BUTTON_PREV  = "{bundle.widgets['pager.button.prev']}";
    private static final String RESOURCE_BUTTON_NEXT  = "{bundle.widgets['pager.button.next']}";
    private static final String RESOURCE_BUTTON_LAST  = "{bundle.widgets['pager.button.last']}";
    private static final String RESOURCE_BUTTON_GO    = "{bundle.widgets['pager.button.go']}";
    private static final String RESOURCE_TOTAL        = "{bundle.widgets['pager.total']}";
    private static final String RESOURCE_FILTERED     = "{bundle.widgets['pager.filtered']}";
    private static final String RESOURCE_FOUND        = "{bundle.widgets['pager.found']}";
    private static final String RESOURCE_LOCATION     = "{bundle.widgets['pager.location']}";
    private static final String RESOURCE_SELECTED     = "{bundle.widgets['pager.selected']}";//Bulk Accommodation
        
    private String dataSource;
    private String summary;
    private String objectLabel;
    private String foundLabel;
    private String id = "unknown";
    private String anchorName = null;
    private Integer value= null;
    
    
    
    public void setDataSource( String dataSource ) {
        this.dataSource = dataSource;
    }

    public String getDataSource( ) {
        return this.dataSource;
    }
    
    public void setSummary( String summary ) {
        this.summary = summary;
    }
    
    public String getSummary() {
        return this.summary;
    }
        
    public void setObjectLabel( String objectLabel) {
        this.objectLabel = objectLabel;
    }
    
    public String getObjectLabel() {
        return this.objectLabel;
    }

    public void setFoundLabel( String foundLabel) {
        this.foundLabel = foundLabel;
    }
    
    public String getFoundLabel() {
        return this.foundLabel;
    }
    
    public void setAnchorName( String anchorName ) {
        this.anchorName = anchorName;
    }

    public String getAnchorName( ) {
        return this.anchorName;
    }
    
    public void setId( String id ) {
        this.id = id;
    }

    public String getId( ) {
        return this.id;
    }
    
    
    public int doStartTag() {
        try {
            JspWriter out = pageContext.getOut();
            TagLibUtils tlu = new TagLibUtils( (HttpServletRequest) pageContext.getRequest(), this, this.pageContext );

            String expression = this.getDataSource();
            Integer pageRequested;
            //System.out.println("pgetrequest in jsp..."+ this.value);
            if (isOldStyle(this.getDataSource())) {            
            	pageRequested= (Integer) tlu.resolveVariable( this.getDataSource() );
            	//System.out.println("pageRequested in old" + pageRequested);
            }
            else {
            	pageRequested= (Integer) tlu.performResolveVariable( this.getDataSource() );
            	//System.out.println("pageRequested" + pageRequested);
            	expression = addBrackets(this.getDataSource());            	
            }
            
            PagerSummary pagerSummary = (PagerSummary)tlu.performResolveVariable( this.getSummary() );

            if( pageRequested == null ) {
            	if (this.value != null) {
            		
            		pageRequested = this.value;
            		
            	} else {
            		throw new JspException("dataSource attribute not defined!");
            	}
                
            }
            if( pagerSummary == null ) {
                throw new JspException("summary attribute not defined!");
            }

            
            // Write out hidden element if only one page.
            if( pagerSummary.getTotalPages().intValue() <= 1 ) {
                out.print( hiddenElement(expression, pageRequested.toString()) );
            }
            
            out.print( startTable(this.STYLE_CLASS) );
            out.print( startTableRow(this.STYLE_CLASS) );
            
            out.print( startTableData(this.STYLE_CLASS_LEFTSIDE) );
            out.print( pagerButtons(tlu, pagerSummary) );
            out.print( endTableData() );
            
            out.print( startTableData(this.STYLE_CLASS_MIDDLE) );
            out.print( pagerSummary(tlu, pagerSummary) );
            out.print( endTableData() );

            out.print( startTableData(this.STYLE_CLASS_RIGHTSIDE) );
            out.print( pagerLocation(tlu, pagerSummary) );
            out.print( endTableData() );

            out.print( endTableRow() );
            out.print( endTable() );
        } catch( IOException ioe ) {
            System.err.println("IOException caught within doStartTag of TablePagerTag handler!");
            System.err.println(ioe);
        } catch( JspException jspe ) {
            System.err.println("JspException caught within doStartTag of TablePagerTag handler!");
            System.err.println(jspe);
        }
        
        return this.SKIP_BODY;
    }
    
    
    /**
     * 
     */
    private String pagerButtons(TagLibUtils tlu, PagerSummary pagerSummary) {
        int firstPage, prevPage, nextPage, lastPage, currentPage, totalPages;
        String html = "";
        String js;

        String firstButton = (String) tlu.resolveVariable(this.RESOURCE_BUTTON_FIRST);
        String prevButton  = (String) tlu.resolveVariable(this.RESOURCE_BUTTON_PREV);
        String nextButton  = (String) tlu.resolveVariable(this.RESOURCE_BUTTON_NEXT);
        String lastButton  = (String) tlu.resolveVariable(this.RESOURCE_BUTTON_LAST);
        
        
        if( pagerSummary.getCurrentPage() != null ) {
            currentPage = pagerSummary.getCurrentPage().intValue();
        } else {
            currentPage = 1;
        }
        
        if( pagerSummary.getTotalPages() != null ) {
            totalPages = pagerSummary.getTotalPages().intValue();
        } else {
            totalPages = 1;
        }
        
        
        // Return nbsp if total pages = 1
        if( totalPages == 1 )
            return "&nbsp;";
        
        
        
        firstPage = 1;
        prevPage  = (currentPage <= 1) ? 1 : currentPage - 1;
        nextPage  = (currentPage >= totalPages) ? currentPage : currentPage + 1;
        lastPage  = totalPages;

        String expression = addBrackets(this.getDataSource());            	
        
        //System.out.println("expression..." + expression);
        //System.out.println("currentPage..." + currentPage);
        //System.out.println("firstPage..." + firstPage);
        //System.out.println("this.anchorName..." + this.anchorName);
        if( currentPage <= firstPage ) {
            html += buttonElement(null, firstButton, STYLE_CLASS_BUTTON, null, null, null, null, true) + " ";
            html += buttonElement(null, prevButton, STYLE_CLASS_BUTTON, null, null, null, null, true) + " ";
        } else {
            if ( this.anchorName != null )
                js = "setElementValueAndSubmitWithAnchor('" + expression + "', '" + String.valueOf(firstPage) + "', '" + this.anchorName + "');";
            else
                js = "setElementValueAndSubmit('" + expression + "', '" + String.valueOf(firstPage) + "');";
            html += buttonElement(null, firstButton, STYLE_CLASS_BUTTON, null, null, js, null, false) + " ";

            if ( this.anchorName != null )
                js = "setElementValueAndSubmitWithAnchor('" + expression + "', '" + String.valueOf(prevPage) + "', '" + this.anchorName + "');";
            else
                js = "setElementValueAndSubmit('" + expression + "', '" + String.valueOf(prevPage) + "');";
            html += buttonElement(null, prevButton, STYLE_CLASS_BUTTON, null, null, js, null, false) + " ";
        }

        if( currentPage >= lastPage ) {
            html += buttonElement(null, nextButton, STYLE_CLASS_BUTTON, null, null, null, null, true) + " ";
            html += buttonElement(null, lastButton, STYLE_CLASS_BUTTON, null, null, null, null, true) + " ";
        } else {
            if ( this.anchorName != null )            
                js = "setElementValueAndSubmitWithAnchor('" + expression + "', '" + String.valueOf(nextPage) + "', '" + this.anchorName + "');";
            else
                js = "setElementValueAndSubmit('" + expression + "', '" + String.valueOf(nextPage) + "');";
            html += buttonElement(null, nextButton, STYLE_CLASS_BUTTON, null, null, js, null, false) + " ";

            if ( this.anchorName != null )            
                js = "setElementValueAndSubmitWithAnchor('" + expression + "', '" + String.valueOf(lastPage) + "', '" + this.anchorName + "');";
            else
                js = "setElementValueAndSubmit('" + expression + "', '" + String.valueOf(lastPage) + "');";
            html += buttonElement(null, lastButton, STYLE_CLASS_BUTTON, null, null, js, null, false) + " ";
        }        

        return html;
    }
    
        /**
     * 
     */
    private String pagerLocation(TagLibUtils tlu, PagerSummary pagerSummary) {
        
        String goButton = (String) tlu.resolveVariable( this.RESOURCE_BUTTON_GO );
        String html = (String) tlu.resolveVariable( this.RESOURCE_LOCATION );
        String js = "return constrainNumericKeyEvent(event, this, '" + this.id + "', '" + this.anchorName + "' );";

        if( pagerSummary.getCurrentPage() != null ) {
            String inputField;

            String expression = addBrackets(this.getDataSource());            	
            
            if( pagerSummary.getTotalPages().intValue() > 1 ) {
                inputField = textElement( expression, pagerSummary.getCurrentPage(), STYLE_CLASS_INPUT_FIELD, null, null, null, js, false );
            } else {
                inputField = "1";
            }

            html = html.replaceAll( "\\$x", inputField );
        }
        
        if( pagerSummary.getTotalPages() != null ) {
            html = html.replaceAll( "\\$y", pagerSummary.getTotalPages().toString() );
        }

        // Don't show the GO button if only one page
        if( pagerSummary.getTotalPages().intValue() > 1 ) {
            js = "return buttonGoInvoked('" + this.id + "', '" + this.anchorName + "');";
            html += "&nbsp;&nbsp;" + submitElement(null, goButton, STYLE_CLASS_BUTTON, null, null, js, null, false);
        }
            
        
        return "<span>" + html + "</span>";   
    }
    
    
    /**
     * 
     */
    private String pagerSummary(TagLibUtils tlu, PagerSummary pagerSummary) {
        
        String totalMsg = (String) tlu.resolveVariable( this.RESOURCE_TOTAL );
        String objectLabelValue = "";
        String html = "";
        int total = 0;
        
        if( this.getObjectLabel() != null )
            objectLabelValue = (String) tlu.resolveVariable( this.getObjectLabel() );

        if( pagerSummary.getTotalObjects() != null )
            total = pagerSummary.getTotalObjects().intValue();
        
        html = totalMsg;
        html = html.replaceAll( "\\$x", objectLabelValue );
        html = html.replaceAll( "\\$y", String.valueOf(total) );

        if( pagerSummary.getTotalFilteredObjects() != null ) {
            String filteredMsg = null;
            if( this.getFoundLabel() != null )
                filteredMsg = (String) tlu.resolveVariable( this.RESOURCE_FOUND );
            else 
                filteredMsg = (String) tlu.resolveVariable( this.RESOURCE_FILTERED );
            int filtered = pagerSummary.getTotalFilteredObjects().intValue();
            html += filteredMsg;
            html = html.replaceAll( "\\$x", String.valueOf(filtered) );
        }
        
        if(pagerSummary.getTotalSelectedObjects() != null) {
        	String js="toggleShowButton(element,totalStudents)";
        	String selectedMsg = null;
        	selectedMsg = (String) tlu.resolveVariable( this.RESOURCE_SELECTED );
        	html += selectedMsg;
        	html += "<span id=\"totalSelected\">"+pagerSummary.getTotalSelectedObjects()+"</span>";
        	 
        }
        
        return "<span>" + html + "</span>";   
    }

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

} 
