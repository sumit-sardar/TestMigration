package com.ctb.widgets.taglib; 

import com.ctb.widgets.bean.PathListEntry;
import com.ctb.widgets.taglib.utils.TagLibUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

public class TablePathListTag extends WidgetsBaseTag
{ 
    private static final String STYLE_CLASS = "pathList";
    
    private String labelDataSource;
    private String valueDataSource;
    private String pathList;
    
    
    public void setLabelDataSource( String labelDataSource ) {
        this.labelDataSource = labelDataSource;
    }

    public void setValueDataSource( String valueDataSource ) {
        this.valueDataSource = valueDataSource;
    }
 
    public void setPathList( String pathList ) {
        this.pathList = pathList;
    }
    
    
    
    
    public int doStartTag() {
        try {
            JspWriter out = pageContext.getOut();
            TagLibUtils tlu = new TagLibUtils( (HttpServletRequest) pageContext.getRequest(), this, this.pageContext );
        
            String currentLabel = (String) tlu.performResolveVariable( this.labelDataSource );
            Integer currentValue = (Integer) tlu.performResolveVariable( this.valueDataSource );
            
            List pathListEntries = (List)tlu.performResolveVariable( pathList );
            
            if( pathListEntries == null ) {
                throw new JspException("pathList attribute not defined!");
            }
        
            String labelDS = "{" + this.labelDataSource + "}";
            String valueDS = "{" + this.valueDataSource + "}";
            
            out.print( hiddenElement( labelDS, currentLabel) );
            out.print( hiddenElement( valueDS, currentValue) );
            out.print( "<div class=\"" + STYLE_CLASS + "\" >");

            if( pathListEntries.size() > 0 ) {
                Iterator iterator = pathListEntries.iterator();
                
                while( iterator.hasNext() ) {
                    PathListEntry ple = (PathListEntry) iterator.next();
                    if( !iterator.hasNext() ) {
                        out.print( "<span>" + ple.getLabel() + "</span>" );
                    } else {
                        String js = "";
                        js += "setElementValue('" + labelDS + "', '" + tlu.escapeVariable(ple.getLabel()) + "'); ";
                        js += "setElementValueAndSubmit('" + valueDS + "', '" + ple.getValue().toString() + "'); return false;";
                        
                        out.print( "<a href=\"#\" onclick=\"" + js + "\">" + ple.getLabel() + "</a> &gt; " );
                    }
                }

            } else {
                out.print( "&nbsp;" );
            }

            out.print( "</div>" );


        } catch( IOException ioe ) {
            System.err.println("IOException caught within doStartTag of TablePagerTag handler!");
            System.err.println(ioe);
        } catch( JspException jspe ) {
            System.err.println("JspException caught within doStartTag of TablePagerTag handler!");
            System.err.println(jspe);
        }
        
        return this.SKIP_BODY;
    }
    
    
} 
