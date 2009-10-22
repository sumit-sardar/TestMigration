package com.ctb.widgets.taglib.logic; 

import com.ctb.widgets.taglib.utils.TagLibUtils;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;


public class CaseTag extends javax.servlet.jsp.tagext.TagSupport
{ 
    private String value;
    
    public void setValue( String value ) {
        this.value = value;
    }



    public int doStartTag() {

        int result = this.SKIP_BODY;
        
        try {
            TagLibUtils tlu = new TagLibUtils( (HttpServletRequest) pageContext.getRequest(), this, this.pageContext );
            SwitchTag switchTag = (SwitchTag) this.getParent();

            if( switchTag.getDataSourceValue() == null )
                result = this.SKIP_BODY;
            else if( value == null )
                result = this.SKIP_BODY;
            else if( switchTag.getDataSourceValue().equalsIgnoreCase( this.value ) )
                result = this.EVAL_BODY_INCLUDE;
            else
                result = this.SKIP_BODY;

        } catch( JspException jspe) {
            System.err.println("JspException caught within doStartTag of CaseTag handler!");
            System.err.println(jspe);
        }
        
        return result;
    }


} 
