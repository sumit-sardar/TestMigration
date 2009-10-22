package com.ctb.widgets.taglib; 

import com.ctb.widgets.taglib.utils.TagLibUtils;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

public class TableFilterToggleTag extends WidgetsBaseTag
{ 
    private static final String STYLE_CLASS_BUTTON = "button";
    
    private static final String RESOURCE_BUTTON_SHOW = "{bundle.widgets['filter.button.show']}";
    private static final String RESOURCE_BUTTON_HIDE  = "{bundle.widgets['filter.button.hide']}";
    
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
            
            String filterVisibleStr = (String) tlu.resolveVariable( this.getDataSource() );
            Boolean filterVisible = new Boolean(filterVisibleStr);
            
            String showFilter = (String) tlu.resolveVariable(this.RESOURCE_BUTTON_SHOW);
            String hideFilter = (String) tlu.resolveVariable(this.RESOURCE_BUTTON_HIDE);
            JspWriter out = pageContext.getOut();

            String divId = TableFilterTag.DIV_PREFIX + this.dataSource;

            String js = "";
            js += "if( this.value=='" + hideFilter + "' ) { ";
            js += "  setElementValue('" + this.dataSource + "', false); ";
            js += "  hideElementById('" + divId + "'); ";
            js += "  this.value='" + showFilter + "'; ";
            js += "} else { ";
            js += "  setElementValue('" + this.dataSource + "', true); ";
            js += "  showElementById('" + divId + "'); ";
            js += "  this.value = '" + hideFilter + "'; ";
            js += "}";

            String buttonText;
            if( filterVisible.booleanValue() ) {
                buttonText = hideFilter;
            } else {
                buttonText = showFilter;
            }


            out.print( buttonElement( this.dataSource, buttonText, this.STYLE_CLASS_BUTTON, null, null, js, null, false) );

        } catch( IOException ioe ) {
            System.err.println("IOException caught within doStartTag of TableFilterToggleTag handler!");
            System.err.println(ioe);
        } catch( JspException jspe ) {
            System.err.println("JspException caught within doStartTag of TableFilterToggleTag handler!");
            System.err.println(jspe);
        }
        
        return this.SKIP_BODY;
    }
    

} 
