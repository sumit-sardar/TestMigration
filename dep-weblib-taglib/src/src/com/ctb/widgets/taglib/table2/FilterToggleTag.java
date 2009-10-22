package com.ctb.widgets.taglib.table2; 

import com.ctb.widgets.taglib.BaseTag;
import com.ctb.widgets.taglib.utils.TagLibUtils;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

public class FilterToggleTag extends BaseTag
{ 
    // Member variables --------------------------------------------------------
    private static final String STYLE_CLASS_BUTTON = "button";
    private static final String RESOURCE_BUTTON_SHOW = "{bundle.widgets['filter.button.show']}";
    private static final String RESOURCE_BUTTON_HIDE  = "{bundle.widgets['filter.button.hide']}";
    

    // Tag attributes --------------------------------------------------------
    private String visibleDataSource;

    // Tag attribute setters -------------------------------------------------
    public void setVisibleDataSource( String visibleDataSource ) {
        this.visibleDataSource = visibleDataSource;
    }

    
    // Life-cycle --------------------------------------------------------------
    
    public int doStartTag() {
        
        try {
            TagLibUtils tlu = new TagLibUtils( (HttpServletRequest) pageContext.getRequest(), this, this.pageContext );
            String showFilter = (String) tlu.resolveVariable( RESOURCE_BUTTON_SHOW );
            String hideFilter = (String) tlu.resolveVariable( RESOURCE_BUTTON_HIDE );
            Object visibleObject = tlu.resolveVariable( visibleDataSource);
            boolean visibleValue;

            if( visibleObject instanceof Boolean ) {
                visibleValue = ((Boolean) visibleObject).booleanValue();
            } else if( visibleObject instanceof String ) {
                visibleValue = Boolean.valueOf( visibleObject.toString() ).booleanValue();
            } else {
                visibleValue = false;
            }
 
            String divId = FilterTag.DIV_PREFIX + visibleDataSource;

            String js = "";
            js += "if( this.value=='" + hideFilter + "' ) { ";
            js += "  setElementValue('" + visibleDataSource + "', false); ";
            js += "  hideElementById('" + divId + "'); ";
            js += "  this.value='" + showFilter + "'; ";
            js += "} else { ";
            js += "  setElementValue('" + visibleDataSource + "', true); ";
            js += "  showElementById('" + divId + "'); ";
            js += "  this.value = '" + hideFilter + "'; ";
            js += "}";

            String buttonText;
            if( Boolean.valueOf(visibleValue).booleanValue() ) {
                buttonText = hideFilter;
            } else {
                buttonText = showFilter;
            }

            JspWriter out = pageContext.getOut();
            out.print( buttonElement( null, buttonText, STYLE_CLASS_BUTTON, js, false) );

        } catch( IOException ioe ) {
            System.err.println("IOException caught within doStartTag of FilterToggleTag handler!");
            System.err.println(ioe);
        } catch( JspException jspe ) {
            System.err.println("JspException caught within doStartTag of FilterToggleTag handler!");
            System.err.println(jspe);
        }
        
        return this.SKIP_BODY;
    }
    

} 
