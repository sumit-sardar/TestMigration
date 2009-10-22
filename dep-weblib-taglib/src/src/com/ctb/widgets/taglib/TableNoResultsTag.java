package com.ctb.widgets.taglib; 

import com.ctb.widgets.bean.PagerSummary;
import com.ctb.widgets.taglib.utils.TagLibUtils;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import org.apache.struts.action.ActionForm;

public class TableNoResultsTag extends WidgetsBaseTag
{ 
    private String dataSource;
    
    public void setDataSource( String dataSource ) {
        this.dataSource = dataSource;
    }
    
    
    
    public int doStartTag() {
        int results = this.SKIP_BODY;
        
        try {
            TagLibUtils tlu = new TagLibUtils( (HttpServletRequest) pageContext.getRequest(), this, this.pageContext );
            
            List dataList;
            
            if (isOldStyle(this.dataSource)) {            
            	dataList = (List)tlu.resolveVariable( this.dataSource );
            }
            else {
            	dataList = (List)tlu.performResolveVariable( this.dataSource );            	
            }

            if( (dataList != null) && (dataList.size() == 0) ) {
            	results = this.EVAL_BODY_INCLUDE;
            } 

        } catch( JspException jspe ) {
            System.err.println("JSPException caught within doStartTag of TableNoResultsTag handler!");
            System.err.println(jspe);
        }
        
        return results;
    }
} 
