package com.ctb.widgets.taglib;  
 
import com.ctb.widgets.bean.PagerSummary;
import com.ctb.widgets.taglib.utils.TagLibUtils;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

public class ShowHideSectionTag extends WidgetsBaseTag
{ 
    private static final String STYLE_CLASS         = "Collapsible";
    private static final String STYLE_CLASS_HEADER  = "CollapsibleHeader";
    private static final String STYLE_CLASS_BODY    = "CollapsibleBody";
    
    private static final String IMG_COLLAPSE = "resources/images/misc/button_collapse.gif";
    private static final String IMG_EXPAND   = "resources/images/misc/button_expand.gif";
        
    private String sectionId = "";
    private String sectionTitle = "";
    private String sectionVisible = "";
    
    private String expression = "";
    
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
         

    public int doStartTag() {
        try {
            JspWriter out = pageContext.getOut();
            TagLibUtils tlu = new TagLibUtils( (HttpServletRequest) pageContext.getRequest(), this, this.pageContext );
            
            this.expression = this.sectionVisible;
            Boolean visible;
            
            if (isOldStyle(this.sectionVisible)) {            
            	visible = (Boolean) tlu.resolveVariable( this.sectionVisible );
            }
            else {
            	visible = (Boolean) tlu.performResolveVariable( this.sectionVisible );
                this.expression = addBrackets(this.sectionVisible);
            }
            
            out.print( hiddenElement(this.expression, visible) );
            
            String hideSectionId = this.sectionId + "_hide";            
            String showSectionId = this.sectionId + "_show";     
                   
                        
            // hide div
            out.print( writeDiv( hideSectionId, ! visible.booleanValue() ) );
            
            // outter table
            out.print( startTable(this.STYLE_CLASS) );
                out.print( startTableRow(this.STYLE_CLASS) );
                    out.print( startTableData(this.STYLE_CLASS) );

                        // header
                        out.print( startTable(this.STYLE_CLASS) );
                            out.print( startTableRow(this.STYLE_CLASS) );
                                out.print( startTableData(this.STYLE_CLASS_HEADER, "25") );

                                    out.print( writeHideAnchor() );

                                out.print( endTableData() );
                                out.print( startTableData(this.STYLE_CLASS_HEADER) );

                                    out.print( "&nbsp;" + this.sectionTitle );

                                out.print( endTableData() );                        
                            out.print( endTableRow() );
                        out.print( endTable() );

                    out.print( endTableData() );                        
                out.print( endTableRow() );
            out.print( endTable() );

            out.print( "</div>" );





            // show div
            out.print( writeDiv( showSectionId, visible.booleanValue() ) );

            // outter table
            out.print( startTable(this.STYLE_CLASS) );
                out.print( startTableRow(this.STYLE_CLASS) );
                    out.print( startTableData(this.STYLE_CLASS) );

                        // header
                        out.print( startTable(this.STYLE_CLASS) );
                            out.print( startTableRow(this.STYLE_CLASS) );
                                out.print( startTableData(this.STYLE_CLASS_HEADER, "25") );

                                    out.print( writeShowAnchor() );

                                out.print( endTableData() );
                                out.print( startTableData(this.STYLE_CLASS_HEADER) );
                                    
                                    out.print( "&nbsp;" + this.sectionTitle );                                    
            
                                out.print( endTableData() );                        
                            out.print( endTableRow() );
                        out.print( endTable() );

                    out.print( endTableData() );                        
                out.print( endTableRow() );



            out.print( startTableRow(this.STYLE_CLASS) );
                out.print( startTableData(this.STYLE_CLASS) );

                // body
                out.print( startTable(this.STYLE_CLASS) );
                    out.print( startTableRow(this.STYLE_CLASS) );
                        out.print( startTableData(this.STYLE_CLASS, "25") );
                            out.print( "&nbsp;" );                
                        out.print( endTableData() );                        
                
                        out.print( startTableData(this.STYLE_CLASS_BODY, "*") );
                
                

            
        } 
        catch( IOException ioe ) {
            System.err.println(ioe);
        } 
        catch( JspException jspe ) {
            System.err.println(jspe);
        }

                    
        return this.EVAL_BODY_INCLUDE;
    }
    

    public int doEndTag() {

        try {
            JspWriter out = pageContext.getOut();

            out.print( endTableData() );                        
            out.print( endTableRow() );
            out.print( endTable() );

            out.print( endTableData() );                        
            out.print( endTableRow() );
            out.print( endTable() );
            
            out.print( "</div>" );

        }
        catch( IOException ioe ) {
            System.err.println(ioe);
        }
        
        return this.SKIP_BODY;

    }

    private String writeDiv(String section, boolean visible) {
        if ( visible ) {
            return "<div id=\"" + section + "\" style=\"display: block\" >";
        }
        else {
            return "<div id=\"" + section + "\" style=\"display: none\" >";
        }
    }
    
    private String writeHideAnchor() {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        String contextPath = request.getContextPath();
        
        String js = "showSection('" + this.sectionId + "', '" + this.expression + "');";            
        String anchor = "<a href=\"#\" onclick=\"" + js + " return false;\">";
        String img = "<img src=\"" + contextPath + "/" + IMG_COLLAPSE + "\" border=\"0\" >";
        String str = (anchor +  img + "</a>");            

        return str;
    }

    private String writeShowAnchor() {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        String contextPath = request.getContextPath();
        
        String js = "hideSection('" + this.sectionId + "', '" + this.expression + "');";
        String anchor = "<a href=\"#\" onclick=\"" + js + " return false;\">";
        String img = "<img src=\"" + contextPath + "/" + IMG_EXPAND + "\" border=\"0\" >";
        String str = (anchor +  img + "</a>");            

        return str;
    }

} 
