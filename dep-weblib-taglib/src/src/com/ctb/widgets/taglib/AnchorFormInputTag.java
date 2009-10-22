package com.ctb.widgets.taglib; 

import com.ctb.widgets.taglib.utils.TagLibUtils;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import org.apache.struts.action.ActionForm;

public class AnchorFormInputTag extends WidgetsBaseTag
{ 
    private String dataSource;
    private String inputId;
    private String submit;
    
    public void setDataSource( String dataSource ) {
        this.dataSource = dataSource;
    }
    
    public void setInputId( String inputId ) {
        this.inputId = inputId;
    }
    
    public void setSubmit( String submit ) {
        this.submit = submit;
    }
    
    
    
    public int doStartTag() {
        
        try {
            TagLibUtils tlu = new TagLibUtils( (HttpServletRequest) pageContext.getRequest(), this, this.pageContext );
            Object elementValue = tlu.resolveVariable( this.dataSource );
            String js;
            boolean submitForm = Boolean.valueOf(this.submit).booleanValue();
            
            if( submitForm )
                js = "setElementValueAndSubmit('" + this.inputId + "', '" + elementValue.toString() + "'); ";
            else
                js = "setElementValue('" + this.inputId + "', '" + elementValue.toString() + "'); ";
            
            JspWriter out = pageContext.getOut();
            out.print( "<a href=\"#\" onclick=\"" + js + " return false;\">" );

        } catch( JspException jspe ) {
            System.err.println("JSPException caught within doStartTag of AnchorFormInput handler!");
            System.err.println(jspe);
        } catch( IOException ioe ) {
            System.err.println("IOException caught within doStartTag of AnchorFormInput handler!");
            System.err.println(ioe);
        }
        
        return this.EVAL_BODY_INCLUDE;
    }
    

    public int doEndTag() {
        
        try {
            JspWriter out = pageContext.getOut();
            out.print( "</a>");
        } catch( IOException ioe ) {
            System.err.println("IOException caught within doEndTag of AnchorFormInput handler!");
            System.err.println(ioe);
        }
        
        return this.SKIP_BODY;
    }

} 
