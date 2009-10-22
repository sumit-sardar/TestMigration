package com.ctb.widgets.taglib; 
 
import com.ctb.widgets.taglib.utils.TagLibUtils;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

public class TableTabGroupTag extends WidgetsBaseTag
{ 
    private static final String STYLE_CLASS = "tableTabs";
    private String dataSource;
    private String anchorName = null;
    
    
    public void setDataSource( String dataSource ) {
        this.dataSource = dataSource;
    }

    public String getDataSource( ) {
        return this.dataSource;
    }

    public void setAnchorName( String anchorName ) {
        this.anchorName = anchorName;
    }

    public String getAnchorName( ) {
        return this.anchorName;
    }


    public int doStartTag() {
        
        try {
            TagLibUtils tlu = new TagLibUtils( (HttpServletRequest) pageContext.getRequest(), this, this.pageContext );
            
            String expression = this.getDataSource();
            String dataSourceValue = (String) tlu.performResolveVariable( expression );
            
            JspWriter out = pageContext.getOut();

            expression = "{" + this.getDataSource() + "}";
            
            out.print( hiddenElement( expression, dataSourceValue ) );
            out.print( startTable( this.STYLE_CLASS ) );
            out.print( startTableRow( this.STYLE_CLASS ) );
        } catch( IOException ioe ) {
            System.err.println("IOException caught within doStartTag of TableTabGroup handler!");
            System.err.println(ioe);
        } catch( JspException jspe ) {
            System.err.println("JspException caught within doStartTag of TableTabGroup handler!");
            System.err.println(jspe);
        }
        
        return this.EVAL_BODY_INCLUDE;
    }
    
    
    
    public int doEndTag() {

        try {
            JspWriter out = pageContext.getOut();

            out.print( endTableRow() );
            out.print( endTable() );
        }
        catch( IOException ioe ) {
            System.err.println("IOException caught within doEndTag of TableTabGroup handler!");
            System.err.println(ioe);
        }
        
        return this.SKIP_BODY;

    }

} 
