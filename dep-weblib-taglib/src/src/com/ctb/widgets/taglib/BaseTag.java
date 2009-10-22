package com.ctb.widgets.taglib; 


/**
 * 
 */
public class BaseTag extends javax.servlet.jsp.tagext.TagSupport
{ 
    /**
     * 
     */
    protected String safeDataSourceName( String dataSource ) {
        String name = dataSource;
        
        name = name.replaceAll("\\{", "");
        name = name.replaceAll("\\}", "");
        name = name.replaceAll("\\.", "");
        
        return name;
    }
    
    
    
    /**
     * 
     */
    protected String hiddenElement( String id, Object value ) {
        String html = "";
        
        html += "<input";
        html += " type=\"hidden\" ";
        html += " id=\"" + id + "\" ";
        html += " name=\"" + id + "\" ";
        if( value != null )
            html += " value=\"" + value.toString() + "\" ";
        else
            html += " value=\"\" ";
        html += "/>";
               
        return html;
    }


    /**
     * 
     */
    protected String buttonElement( String id, Object value, String styleClass, String onClick, boolean disabled ) {
        String html = "";
        
        html += "<input";
        html += " type=\"button\" ";
        
        if( id != null ) {
            html += "id=\"" + id + "\" ";
            html += "name=\"" + id + "\" ";
        }
        
        if( value != null )
            html += "value=\"" + value.toString() + "\" ";
        else
            html += "value=\"\" ";
        
        if( styleClass != null )
            html += "class=\"" + styleClass + "\" ";
            
        if( onClick != null )
            html += "onclick=\"" + onClick + "\" ";
        
        if( disabled )
            html += "disabled ";
        
        html += "/>";
               
        return html;
    }

    /**
     * 
     */
    protected String textElement( String id, Object value, String styleClass, String onKeyPress, boolean disabled ) {
        String html = "";
        
        html += "<input";
        html += " type=\"text\" ";
        
        if( id != null ) {
            html += "id=\"" + id + "\" ";
            html += "name=\"" + id + "\" ";
        }
        
        if( value != null )
            html += "value=\"" + value.toString() + "\" ";
        else
            html += "value=\"\" ";
        
        if( styleClass != null )
            html += "class=\"" + styleClass + "\" ";
            
        if( onKeyPress != null )
            html += "onkeypress=\"" + onKeyPress + "\" ";
        
        if( disabled )
            html += "disabled ";
        
        html += "/>";
               
        return html;
    }



    /**
     * 
     */
    protected String startTable(String styleClass) {
        String html = "";
        
        html += "<table";
        if( styleClass != null );
            html += " class=\"" + styleClass + "\"";
        html += ">";

        return html;
    }
    
    /**
     * 
     */
    protected String startTableRow(String styleClass) {
        String html = "";
        
        html += "<tr";
        if( styleClass != null );
            html += " class=\"" + styleClass + "\"";
        html += ">";

        return html;
    }

    /**
     * 
     */
    protected String startTableHeader(String styleClass) {
        String html = "";
    
        html += "<th";
        if( styleClass != null );
            html += " class=\"" + styleClass + "\"";
        html += ">";

        return html;
    }

    
    /**
     * 
     */
    protected String endTableHeader() {
        return "</th>";
    }

    /**
     * 
     */
    protected String startTableData(String styleClass) {
        String html = "";
    
        html += "<td";
        if( styleClass != null );
            html += " class=\"" + styleClass + "\"";
        html += ">";

        return html;
    }

    
    /**
     * 
     */
    protected String endTableData() {
        return "</td>";
    }

    /**
     * 
     */
    protected String endTableRow() {
        return "</tr>";
    }
    
    /**
     * 
     */
    protected String endTable() {
        return "</table>";
    }
} 
