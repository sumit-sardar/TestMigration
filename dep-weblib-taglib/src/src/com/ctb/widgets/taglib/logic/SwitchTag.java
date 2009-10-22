package com.ctb.widgets.taglib.logic; 

import com.ctb.widgets.taglib.WidgetsBaseTag;
import com.ctb.widgets.taglib.utils.TagLibUtils;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

public class SwitchTag extends WidgetsBaseTag
{ 

    private String dataSource;
    private String dataSourceValue;
    
    public void setDataSource( String dataSource ) {
        this.dataSource = dataSource;
    }
    
    public String getDataSource() {
        return this.dataSource;
    }

    public String getDataSourceValue() {
        return this.dataSourceValue;
    }

    
    
    public int doStartTag() {

        try {
            TagLibUtils tlu = new TagLibUtils( (HttpServletRequest) pageContext.getRequest(), this, this.pageContext );
            if (isOldStyle(this.dataSource)) {            
            	this.dataSourceValue = (String) tlu.resolveVariable( this.dataSource );
            }
            else {
            	this.dataSourceValue = (String) tlu.performResolveVariable( this.dataSource );            	
            }
            
        } catch( JspException jspe) {
            System.err.println("JspException caught within doStartTag of SwitchTag handler!");
            System.err.println(jspe);
        }

        return this.EVAL_BODY_INCLUDE;
    }

    
} 
