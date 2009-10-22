package com.ctb.widgets.taglib.logic; 

import com.ctb.widgets.taglib.utils.TagLibUtils;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import org.apache.struts.action.ActionForm;

public class NoResultsTag extends javax.servlet.jsp.tagext.TagSupport
{ 
    private String dataSource;
    
    public void setDataSource( String dataSource ) {
        this.dataSource = dataSource;
    }
    
    
    
    public int doStartTag() {
        int results = this.SKIP_BODY;
        
        try {
            TagLibUtils tlu = new TagLibUtils( (HttpServletRequest) pageContext.getRequest(), this, this.pageContext );
            Object dataSourceValue = tlu.resolveVariable( this.dataSource );

            if( dataSourceValue instanceof List ) {
                List list = (List) dataSourceValue;
                if( list.size() <= 0 )
                    results = this.EVAL_BODY_INCLUDE;
            } 

        } catch( JspException jspe ) {
            System.err.println("JSPException caught within doStartTag of NoResultsTag handler!");
            System.err.println(jspe);
        }
        
        return results;
    }
} 
