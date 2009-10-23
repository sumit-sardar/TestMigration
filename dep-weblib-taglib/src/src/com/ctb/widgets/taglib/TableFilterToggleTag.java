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
            
            String showFilter = (String) tlu.resolveVariable(this.RESOURCE_BUTTON_SHOW);
            String hideFilter = (String) tlu.resolveVariable(this.RESOURCE_BUTTON_HIDE);
            JspWriter out = pageContext.getOut();

            String divId = TableFilterTag.DIV_PREFIX + expression;

            String js = "";
            js += "if( this.value=='" + hideFilter + "' ) { ";
            js += "  setElementValue('" + expression + "', false); ";
            js += "  hideElementById('" + divId + "'); ";
            js += "  this.value='" + showFilter + "'; ";
            js += "} else { ";
            js += "  setElementValue('" + expression + "', true); ";
            js += "  showElementById('" + divId + "'); ";
            js += "  this.value = '" + hideFilter + "'; ";
            js += "}";

            String buttonText;
            if( filterVisible.booleanValue() ) {
                buttonText = hideFilter;
            } else {
                buttonText = showFilter;
            }


            out.print( buttonElement( expression, buttonText, this.STYLE_CLASS_BUTTON, null, null, js, null, false) );

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
