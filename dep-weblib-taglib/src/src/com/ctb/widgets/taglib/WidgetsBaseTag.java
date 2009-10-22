package com.ctb.widgets.taglib; 


public class WidgetsBaseTag extends javax.servlet.jsp.tagext.TagSupport
{ 
    private static final String INPUT_SUBMIT   = "submit";
    private static final String INPUT_RESET    = "reset";
    private static final String INPUT_BUTTON   = "button";
    private static final String INPUT_CHECKBOX = "checkbox";
    private static final String INPUT_HIDDEN   = "hidden";
    private static final String INPUT_RADIO    = "radio";
    private static final String INPUT_TEXT     = "text";
    
    
    
        
    protected String startTable( ) {
        return "<table>";
    }

    protected String startTable( String styleClass ) {
        return "<table class=\"" + styleClass + "\">";
    }

    protected String startTableRow( ) {
        return "<tr>";
    }

    protected String startTableRow( String styleClass ) {
        return "<tr class=\"" + styleClass + "\">";
    }

    protected String startTableHeader( String styleClass ) {
        return "<th class=\"" + styleClass + "\">";
    }

    protected String startTableHeader( ) {
        return "<th>";
    }

    protected String endTableHeader() {
        return "</th>";
    }

    protected String startTableData( String styleClass ) {
        return "<td class=\"" + styleClass + "\">";
    }

    protected String startTableData( String styleClass, String width ) {
        return "<td class=\"" + styleClass + "\" width=\"" + width + "\" >";
    }

    protected String startTableData( ) {
        return "<td>";
    }

    protected String endTableData() {
        return "</td>";
    }

    protected String endTableRow() {
        return "</tr>";
    }

    protected String endTable() {
        return "</table>";    
    }
    



    /**
     * 
     */
    protected String buttonElement( String id, Object value, String styleClass, 
                                    String onMouseOver, String onMouseOut, String onClick, 
                                    String onKeyPress, boolean disabled) {

        return inputElement( INPUT_BUTTON, id, value, styleClass, 
                             onMouseOver, onMouseOut, onClick, onKeyPress, disabled );
    }


    /**
     * 
     */
    protected String hiddenElement( String id, Object value ) {

        return inputElement( INPUT_HIDDEN, id, value, null, null, null, null, null, false);
    }


    /**
     * 
     */
    protected String submitElement( String id, Object value, String styleClass,
                                  String onMouseOver, String onMouseOut, String onClick,
                                  String onKeyPress, boolean disabled ) {
        return inputElement( INPUT_SUBMIT, id, value, styleClass, onMouseOver, onMouseOut, onClick, onKeyPress, disabled);
    }
    

    /**
     * 
     */
    protected String textElement( String id, Object value, String styleClass, 
                                    String onMouseOver, String onMouseOut, String onClick, String onKeyPress,
                                    boolean disabled) {

        return inputElement( INPUT_TEXT, id, value, styleClass, 
                             onMouseOver, onMouseOut, onClick, onKeyPress, disabled );
    }





    /**
     * 
     */
    private String inputElement( String type, String id, Object value, String styleClass,
                                 String onMouseOver, String onMouseOut, String onClick,
                                 String onKeyPress, boolean disabled ) {
        String html = "";
    
        html += "<input ";

        if( type != null ) {
            html += "type=\"" + type + "\" ";
        } else {
            html += "type=\"" + INPUT_HIDDEN + "\" ";
        }

        if( id != null ) {
            html += "id=\"" + id + "\" ";
            html += "name=\"" + id + "\" ";
        }

        if( value != null ) {
            if( type.equalsIgnoreCase(INPUT_BUTTON) ) {
                // Don't sanitize buttons since they should not be user supplied
                html += "value=\"" + value.toString() + "\" ";
            } else {
                html += "value=\"" + value.toString() + "\" ";
            }
        } else {
            html += "value=\"\" ";
        }
    
        if( styleClass != null ) {
            html += "class=\"" + styleClass + "\" ";
        }

        if( onMouseOver != null ) {
            html += "onmouseover=\"" + onMouseOver + "\" ";
        }

        if( onMouseOut != null ) {
            html += "onmouseout=\"" + onMouseOut + "\" ";
        }

        if( onClick != null ) {
            html += "onclick=\"" + onClick + "\" ";
        }

        if( onKeyPress != null ) {
            html += "onkeypress=\"" + onKeyPress + "\" ";
        }

        if( disabled ) {
            html += "disabled ";
        }
    
        html += " />";
        return html.toString();
    }
    
} 
