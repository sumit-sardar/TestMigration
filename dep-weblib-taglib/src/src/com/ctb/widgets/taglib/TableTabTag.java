package com.ctb.widgets.taglib; 

import com.ctb.widgets.taglib.utils.TagLibUtils;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import org.apache.struts.action.ActionForm;

public class TableTabTag extends WidgetsBaseTag
{ 
    private final String IMG_TAB_LEFT  = "resources/images/tabs/tab_left.gif";
    private final String IMG_TAB_RIGHT = "resources/images/tabs/tab_right.gif";
    private final String STYLE_CURRENT = "tableTabsCurrent";
    private final String STYLE_NOT_CURRENT= "tableTabs";
    private String tabState;
    
    
    private String value;
    
    public void setValue( String value ) {
        this.value = value;
    }
    
    
    
    public int doStartTag() {
        
        try {
            TagLibUtils tlu = new TagLibUtils( (HttpServletRequest) pageContext.getRequest(), this, this.pageContext );
            TableTabGroupTag ttg = (TableTabGroupTag) this.getParent();
            String ttgDataSource = ttg.getDataSource();
            String ttgDataSourceValue = (String) tlu.performResolveVariable( ttgDataSource );

            JspWriter out = pageContext.getOut();
            if( this.value.equalsIgnoreCase( ttgDataSourceValue ) ) {
                this.tabState = this.STYLE_CURRENT;
            } else {
                this.tabState = this.STYLE_NOT_CURRENT;
            }

            out.print( startTableData(STYLE_NOT_CURRENT + " spacer") );
            out.print( "&nbsp;" );
            out.print( endTableData() );

            out.print( startTableData(this.tabState) );
            out.print( tabImage(IMG_TAB_LEFT) );
            out.print( endTableData() );

            out.print( startTableHeader(this.tabState) );    
            
            ttgDataSource = "{" + ttgDataSource + "}";
            
            if (ttg.getAnchorName() != null)                                
                out.print( "<a href=\"#\" onclick=\"setElementValueAndSubmitWithAnchor('" + ttgDataSource + "', '" + this.value + "', '" + ttg.getAnchorName() +  "'); return false;\">" );
            else
                out.print( "<a href=\"#\" onclick=\"setElementValueAndSubmit('" + ttgDataSource + "', '" + this.value + "'); return false;\">" );
        } catch( IOException ioe ) {
            System.err.println("IOException caught within doStartTag of TableTab handler!");
            System.err.println(ioe);
        } catch( JspException jspe) {
            System.err.println("JspException caught within doStartTag of TableTab handler!");
            System.err.println(jspe);
        }
        
        return this.EVAL_BODY_INCLUDE;
    }
    
 
    public int doEndTag() {
        
        try {
            JspWriter out = pageContext.getOut();

            out.print( "</a>");
            out.print( endTableHeader() );
            out.print( startTableData(this.tabState) );
            out.print( tabImage(IMG_TAB_RIGHT) );
            out.print( endTableData() );
        } catch( IOException ioe ) {
            System.err.println("IOException caught within doEndTag of TableTab handler!");
            System.err.println(ioe);
        }
        
        return this.SKIP_BODY;
    }




    private String tabImage(String image) {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        String contextPath = request.getContextPath();
    
        return "<img src=\"" + contextPath + "/" + image + "\" border=\"0\" width=\"8\" height=\"26\" >";
    }

} 
