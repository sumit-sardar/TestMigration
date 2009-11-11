package com.ctb.lexington.util;


/*
 * CTBViewUtils.java
 *
 * Copyright CTB/McGraw-Hill, 2002
 * CONFIDENTIAL
 *
 */
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @author rmariott; ryan.a.mariotti@accenture.com
 * @version
 */
public class CTBViewUtils {

    //  Hierarchy Table Constants
    public static final int INDENT_WIDTH = 17;
    public static final int NOCHILD_INDENT = 20;
    public static final String OFF_CLASS = "hierarchyTableWhite";
    public static final String HOT_CLASS = "hierarchyTableBold";
    /* to replace the old contract / expand image
    public static final String GRAPHIC_FILENAME_EXPANDED_SELECTED = "contractItem_on.gif";
    public static final String GRAPHIC_FILENAME_EXPANDED = "contractItem_off.gif";
    public static final String GRAPHIC_FILENAME_CONTRACTED_SELECTED = "expandItem_on.gif";
    public static final String GRAPHIC_FILENAME_CONTRACTED = "expandItem_off.gif";
    */
    public static final String GRAPHIC_FILENAME_EXPANDED_SELECTED = "contract.gif";
    public static final String GRAPHIC_FILENAME_EXPANDED = "contract.gif";
    public static final String GRAPHIC_FILENAME_CONTRACTED_SELECTED = "expand.gif";
    public static final String GRAPHIC_FILENAME_CONTRACTED = "expand.gif";
    public static final String GRAPHIC_ERROR = "ErrorAlert.gif";

    /** Creates new CTBViewUtils */
    public CTBViewUtils() {
    }

    /** Method used within JSP pages to properly indent elements within
     * a hierarchy table.  The second parameter is used to
     * determine whether or not to display any arrow graphics.
     */
    public int getIndentPixel(int indentLevel_, boolean hasChildren_) {
        int indentPixelValue = 1 + (INDENT_WIDTH * indentLevel_);
        if (!hasChildren_) indentPixelValue = indentPixelValue + NOCHILD_INDENT;
        return indentPixelValue;
    }

    /** Method used within JSP pages to determine which graphic
     * arrow to display based on the type and properties of the
     * row.
     */
    public String getNodeGraphic(boolean isExpanded_, boolean hasChildrenSelected_) {
        String graphicFileName;
        if(isExpanded_){
            if(hasChildrenSelected_) graphicFileName = GRAPHIC_FILENAME_EXPANDED_SELECTED;
            else graphicFileName = GRAPHIC_FILENAME_EXPANDED;
        }else{
            if(hasChildrenSelected_) graphicFileName = GRAPHIC_FILENAME_CONTRACTED_SELECTED;
            else graphicFileName = GRAPHIC_FILENAME_CONTRACTED;
        }
        return graphicFileName;
    }

    /** Method used within JSP pages to format the <CODE>&lt;TD&gt;</CODE>.
     * Depending on the type and properties of a given row, this method
     * will return the <CODE>STYLE</CODE> class name as either 'hierarchyTableWhite'
     * or 'hierarchyTableBold'.
     */
    public String getClass(boolean isBold_, boolean isChecked_) {
        String className = OFF_CLASS;
        if(isBold_ || isChecked_) className = HOT_CLASS;
        return className;
    }

    public String printSelectedIfEqual(String inputValue, String listValue){
        String returnValue = "";
        if(inputValue.equals(listValue)) returnValue = "SELECTED";
        return returnValue;
    }

    /** This method is incomplete and is probably not needed, as the
     *  error formatting should be done directly within the JSP page.
     *
     public String formatError(String contextPath_, String error_) {
        System.out.println("Error is -" + error_ + "-");
        String output = "";
        if(error_ != ""){
            output = "<img src=\"" + contextPath_ + "/images/buttons/" + GRAPHIC_ERROR + "\" width=\"15\" height=\"14\" border=\"0\" alt=\"Error Alert\" hspace=\"4\" vspace=\"1\" align=\"absmiddle\"/>";
            output = output + error_;
        }else{
            // If there is no error message, return a non-breaking space
            // so that the <TD> won't collapse in Netscape.
            output = "&nbsp;";
        }
        return output;
    }
     */

     /**
      *  This method will take the output valued from the option lists and convert
      *  then into a date object.
      */
     public static Calendar buildDate(String time_, String month_, String day_, String year_, String timezone_)
     {
         TimeZone tz = TimeZone.getTimeZone(timezone_);
         
         java.util.Calendar cal = java.util.Calendar.getInstance(tz);
         
         cal.set( java.util.Calendar.MONTH, Integer.parseInt(month_.trim()) );
         cal.set( java.util.Calendar.DAY_OF_MONTH, Integer.parseInt(day_.trim()) );
         cal.set( java.util.Calendar.YEAR, Integer.parseInt(year_.trim()) );
         cal.set( java.util.Calendar.HOUR_OF_DAY,Integer.valueOf(time_.substring(0,2)).intValue());
         cal.set( java.util.Calendar.MINUTE,Integer.valueOf(time_.substring(2)).intValue());

         return cal;
     }
     
     public static Date convertTime(Calendar cal_)
     {
         
         if (cal_ == null)
         {
            return null;
         }         
         
         int year = cal_.get (java.util.Calendar.YEAR);
         int month = cal_.get (java.util.Calendar.MONTH) + 1;
         int day = cal_.get (java.util.Calendar.DAY_OF_MONTH);
         int hour = cal_.get (java.util.Calendar.HOUR_OF_DAY);
         int minute = cal_.get (java.util.Calendar.MINUTE);
         int second = cal_.get (java.util.Calendar.SECOND);
         int milli = cal_.get (java.util.Calendar.MILLISECOND);
         
         SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy HH:mm");
         
         
         String dateStr = new StringBuffer().append(month).append("/").append(day)
                                            .append("/").append(year).append(" ")
                                            .append(hour).append(":").append(minute).toString();
         
         Date myDate = new Date();
         
         try
         {
             myDate = sdf.parse(dateStr);             
         }
         catch(ParseException pe) 
         {
             pe.getMessage();
         }
 
         return myDate;
         
     }   
         
     public static Date convertyearTime(Date cal_)
     {
         
         if (cal_ == null)
         {
            return null;
         }         
         
         int year = cal_.getYear();
         int month = cal_.getMonth();
         int day = cal_.getDay();
         int hour = cal_.getHours();
         int minute = cal_.getMinutes();
         int second = cal_.getSeconds();
         
         SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
         
         String dateStr = new StringBuffer().append(month).append("/").append(day)
                                            .append("/").append(year).append(" ")
                                            .append(hour).append(":").append(minute).toString();
         
         Date myyearDate = new Date();
         
         try
         {
             myyearDate = sdf.parse(dateStr);             
         }
         catch(ParseException pe) 
         {
             pe.getMessage();
         }
 
         return myyearDate;
        
        
         //cal_.clear();
         
         //java.util.TimeZone tz = java.util.TimeZone.getTimeZone(timezoneId_);
         
         //cal_.setTimeZone(tz);
         
         //cal_.set (year, month - 1, day, hour, minute, second);
         
         //cal_.set (Calendar.MILLISECOND, milli);
         
         
     }
     
        public static Date convertyearTime(Calendar cal_)
     {
         
         if (cal_ == null)
         {
            return null;
         }         
         
         int year = cal_.get (java.util.Calendar.YEAR);
         int month = cal_.get (java.util.Calendar.MONTH) + 1;
         int day = cal_.get (java.util.Calendar.DAY_OF_MONTH);
         int hour = cal_.get (java.util.Calendar.HOUR_OF_DAY);
         int minute = cal_.get (java.util.Calendar.MINUTE);
         int second = cal_.get (java.util.Calendar.SECOND);
         int milli = cal_.get (java.util.Calendar.MILLISECOND);
         
         SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
         
         String dateStr = new StringBuffer().append(month).append("/").append(day)
                                            .append("/").append(year).append(" ")
                                            .append(hour).append(":").append(minute).toString();
         
         Date myyearDate = new Date();
         
         try
         {
             myyearDate = sdf.parse(dateStr);             
         }
         catch(ParseException pe) 
         {
             pe.getMessage();
         }
 
         return myyearDate;
        
        
         //cal_.clear();
         
         //java.util.TimeZone tz = java.util.TimeZone.getTimeZone(timezoneId_);
         
         //cal_.setTimeZone(tz);
         
         //cal_.set (year, month - 1, day, hour, minute, second);
         
         //cal_.set (Calendar.MILLISECOND, milli);
         
         
     }



      /**
      *  This method will take turn a string into a string of start.
      *  Used for masking passwords in HTML when getting value from form.
      */

     public String createStars(String pswd){
        int pswdLength = pswd.length();
        StringBuffer stars =  new StringBuffer();

        for (int i=0; i <pswdLength; i++){
            stars.append("*");
        }

        return stars.toString();
    }
}