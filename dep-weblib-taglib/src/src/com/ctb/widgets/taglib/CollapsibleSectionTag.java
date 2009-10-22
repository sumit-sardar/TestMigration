package com.ctb.widgets.taglib;  
 
import com.ctb.widgets.bean.PagerSummary;
import com.ctb.widgets.taglib.utils.TagLibUtils;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

public class CollapsibleSectionTag extends WidgetsBaseTag
{ 
    private static final String STYLE_CLASS         = "Collapsible";
    private static final String STYLE_CLASS_HEADER  = "CollapsibleHeader";
    
    private static final String IMG_COLLAPSE = "resources/images/misc/button_collapse.gif";
    private static final String IMG_EXPAND   = "resources/images/misc/button_expand.gif";
        
    private String sectionId = "";
    private String sectionTitle = "";
    private String sectionVisible = "";
    private String expandOnly = null;
    
        
    private Boolean visible;
    
    
    public void setSectionId( String sectionId ) {
        this.sectionId = sectionId;
    }
    public String getSectionId( ) {
        return this.sectionId != null ? this.sectionId : "";
    }
        
    public void setSectionTitle( String sectionTitle ) {
        this.sectionTitle = sectionTitle;
    }
    public String getSectionTitle( ) {
        return this.sectionTitle != null ? this.sectionTitle : "";
    }
        
    public void setSectionVisible( String sectionVisible ) {
        this.sectionVisible = sectionVisible;
    }
    public String getSectionVisible( ) {
        return this.sectionVisible != null ? this.sectionVisible : "";
    }

    public void setExpandOnly( String expandOnly ) {
        this.expandOnly = expandOnly;
    }
    public String getExpandOnly( ) {
        return this.expandOnly;
    }
    
    public int doStartTag() {
        try {
            JspWriter out = pageContext.getOut();
            TagLibUtils tlu = new TagLibUtils( (HttpServletRequest) pageContext.getRequest(), this, this.pageContext );
            
            String visibleStr = (String) tlu.resolveVariable( this.getSectionVisible() );
            
            this.visible = Boolean.TRUE;
            if (visibleStr == null) {
                this.visible = Boolean.TRUE;
                this.sectionVisible = Boolean.TRUE.toString();
            }
            else {
            	this.visible = new Boolean(visibleStr);
                this.sectionVisible = this.visible.toString();
            }
            
            if (this.visible == null)
                
            out.print( hiddenElement(this.sectionVisible, this.visible) );
            out.print( "<a name=\"" + this.sectionId + "\"></a>");
            
            // start section
            out.print( startTable(this.STYLE_CLASS) );

            // start title
            out.print( startTableRow(this.STYLE_CLASS) );
            out.print( startTableData(this.STYLE_CLASS) );
            
            out.print( startTable(this.STYLE_CLASS) );
            out.print( startTableRow(this.STYLE_CLASS) );
            out.print( startTableData(this.STYLE_CLASS_HEADER, "25") );
                        
            if (this.visible.booleanValue()) {
                out.print( printImage(IMG_EXPAND) );
            }
            else {
                out.print( printImage(IMG_COLLAPSE) );
            }
            
            out.print( endTableData() );
            out.print( startTableData(this.STYLE_CLASS_HEADER) );

            out.print( "&nbsp;" + this.sectionTitle );            
            
            out.print( endTableData() );
            
            out.print( endTableRow() );
            out.print( endTable() );

            out.print( endTableData() );
            out.print( endTableRow() );
            // end title
            
            // start body
            if (this.visible.booleanValue()) {
                out.print( startTableRow() );
                out.print( startTableData() );
                
                out.print( startTable(this.STYLE_CLASS) );
                out.print( startTableRow(this.STYLE_CLASS) );
                out.print( startTableData(this.STYLE_CLASS, "25") );
                out.print( "&nbsp;" );
                out.print( endTableData() );
                out.print( startTableData(this.STYLE_CLASS) );
                
                // body in jsp
                
            }            
            // end body
            
        } 
        catch( IOException ioe ) {
            System.err.println(ioe);
        } 
        catch( JspException jspe ) {
            System.err.println(jspe);
        }

                    
        if (this.visible.booleanValue()) 
            return this.EVAL_BODY_INCLUDE;
        else
            return this.SKIP_BODY;
    }
    

    public int doEndTag() {

        try {
            JspWriter out = pageContext.getOut();

            if (this.visible.booleanValue()) {
                out.print( endTableData() );
                out.print( endTableRow() );
                out.print( endTable() );
                out.print( endTableData() );
                out.print( endTableRow() );
            }
            
            out.print( endTable() );
            
        }
        catch( IOException ioe ) {
            System.err.println(ioe);
        }
        
        return this.SKIP_BODY;

    }
    
    private String printImage(String imageResource) {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        String contextPath = request.getContextPath();
    
        String result = "";
        if ((this.expandOnly != null) && this.expandOnly.equalsIgnoreCase("true") && this.visible.booleanValue()) {
            result = "<img src=\"" + contextPath + "/" + imageResource + "\" border=\"0\" >";
        }
        else {
            String js = "setElementValueAndSubmitWithAnchor('{actionForm.currentAction}', '" + this.sectionId + "', '" + this.sectionId + "');";
            String anchor = "<a href=\"#\" onclick=\"" + js + " return false;\">";
            String img = "<img src=\"" + contextPath + "/" + imageResource + "\" border=\"0\" >";
            result = (anchor +  img + "</a>");            
        }        
        return result;
    }

} 
