package com.ctb.lexington.util;

import java.util.ArrayList;

/**
 *   <P>
 *   This abstract class defines the base functionality for any class that&nbsp;
 *   returns an option list.  The derived classes will implement the four public
 *   methods.
 *   </P>
 *
 *   <P>
 *   HTML provides different ways to use the <CODE>OPTION</CODE> tag. The following table
 *   displays the <CODE>OPTION</CODE> tags supported by this abstract class.
 *   <TABLE BORDER="1">
 *
 *   <TR>
 *       <TH>Code</TH>
 *       <TH>Description</TH>
 *   </TR>
 *
 *   <TR>
 *       <TD><CODE>&lt;OPTION&gt;display value</CODE></TD>
 *       <TD>This will be an unselected option in the dropdown list.  When a user selects this option, the value displayed in the drop down list will be returned to the web server ( i.e. display value ).</TD>
 *   </TR>
 *
 *   <TR>
 *       <TD><CODE>&lt;OPTION SELECTED&gt;display value</CODE></TD>
 *       <TD>This will be the default selection in the dropdown list.  When a user selects this option, the value displayed in the drop down list will be returned to the web server ( i.e. display value ).</TD>
 *   </TR>
 *
 *   <TR>
 *       <TD><CODE>&lt;OPTION VALUE="01"&gt;display value</CODE></TD>
 *       <TD>This will be an unselected option in the dropdown list.  When a user selects this option, the value in the <CODE>VALUE</CODE> attribute will be returned to the web server ( i.e. 01 ).</TD>
 *   </TR>
 *
 *   <TR>
 *       <TD><CODE>&lt;OPTION VALUE="01"&gt;display value</CODE></TD>
 *       <TD>This will be the default selection in the dropdown list.  When a user selects this option, the value in the <CODE>VALUE</CODE> attribute will be returned to the web server ( i.e. 01 ).</TD>
 *   </TR>
 *
 *   </TABLE>
 *   </P>
 *
 *   <P>
 *   One thing to note is that the above table does not include the idea of a salutation in
 *   a drop down list.  The salutation is no different from any other selectable item in a combo
 *   or list box from the perspective of the browser.  Now from the perspective of the
 *   web tier this salutation is very important, because the web tier needs to know to validate
 *   that the salutation is not returned in a follow on requests.
 *   </P>
 *
 *   @author Matthew A. Cleemns / Accenture
 */
public class OptionList
{
    private ArrayList _item_values = new ArrayList();

    private ArrayList _display_items = new ArrayList();

    public static final int NO_INDEX = -1;

    public static final String DEFAULT_SALUTATION = "Select...";

    public static final String DEFAULT_SALUTATION_VALUE = "";

    public static final String BLANK_SALUTATION = "";


//*****************************************************************************
//
//    Public methods
//
//*****************************************************************************

    public boolean hasName(String name_)
    {
        return _display_items.contains(name_);
    }

    public boolean hasValue(String value_)
    {
        return _item_values.contains(value_);
    }

    /**
     *  @param value_ The value for the option.
     *  @return The item displayed in the option
     */
    public String getName(String value_)
    {
        int name_index = _item_values.indexOf(value_);

        if(name_index >= 0)
        {
            return (String)_display_items.get(name_index);
        }
        else
        {
            return new String("");
        }
    }

    /**
     *  @return A name/value option list
     */
    public ArrayList getUnselectedList()
    {
        return getNameValueList(_item_values, _display_items);
    }

    /**
     *  @param salutation_ The value to be inserted at the top of the option list
     *  @return A name/value option list with the salutation in the first position
     */
    public ArrayList getUnselectedListWithSalutation(String salutation_)
    {
        //the clone method on the _display_items object makes a shallow copy.
        // this shallow copy will still point to the initial values, but now
        // we can add items without affecting the original list.
        ArrayList list_clone = (ArrayList)_display_items.clone();

        // clone the value list because we will need to add a start value for
        // the salutation.
        ArrayList value_clone = (ArrayList)_item_values.clone();

        list_clone.add(0, salutation_);

        value_clone.add(0, OptionList.DEFAULT_SALUTATION_VALUE);

        return getNameValueList(value_clone, list_clone);
    }

    /**
     *  @param name_ The name of the option to be preselect
     *  @return A preselected name/value option list.&nbsp; If the name is not found in the
     *          list, the option list will not be preselected.
     */
    public ArrayList getSelectedListByName(String name_)
    {
        return getSelectedNameValueListByName(_item_values, _display_items, name_);
    }

    /**
     *  @param value_ The value of the option to be preselect
     *  @return A preselected name/value option list.&nbsp; If the value is not found in the
     *          list, the option list will not be preselected.
     */
    public ArrayList getSelectedListByValue(String value_)
    {
        return getSelectedNameValueListByValue(_item_values, _display_items, value_);
    }

    /**
     *  @param salutation_ The value to be inserted at the top of the option list.
     *  @param name_ The name of the option to be preselect
     *  @return A preselected name/value option list.&nbsp; If the name is not found in the
     *          list, the option list will not be preselected.
     */
    public ArrayList getSelectedListWithSalutationByName(String salutation_, String name_)
    {
        //the clone method on the _display_items object makes a shallow copy.
        // this shallow copy will still point to the initial values, but now
        // we can add items without affecting the original list.
        ArrayList list_clone = (ArrayList)_display_items.clone();

        // clone the value list because we will need to add a start value for
        // the salutation.
        ArrayList value_clone = (ArrayList)_item_values.clone();

        list_clone.add(0, salutation_);

        value_clone.add(0, OptionList.DEFAULT_SALUTATION_VALUE);

        return getSelectedNameValueListByName(value_clone, list_clone, name_);

    }

    /**
     *  @param salutation_ The value to be inserted at the top of the option list.
     *  @param value_ The value of the option to be preselect
     *  @return A preselected name/value option list.&nbsp; If the value is not found in the
     *          list, the option list will not be preselected.
     */
    public ArrayList getSelectedListWithSalutationByValue(String salutation_, String value_)
    {
        //the clone method on the _display_items object makes a shallow copy.
        // this shallow copy will still point to the initial values, but now
        // we can add items without affecting the original list.
        ArrayList list_clone = (ArrayList)_display_items.clone();

        // clone the value list because we will need to add a start value for
        // the salutation.
        ArrayList value_clone = (ArrayList)_item_values.clone();

        list_clone.add(0, salutation_);

        value_clone.add(0, OptionList.DEFAULT_SALUTATION_VALUE);

        return getSelectedNameValueListByValue(value_clone, list_clone, value_);
    }

//*****************************************************************************
//
//    Protected Methods
//
//*****************************************************************************

    /**
     *  @param names_ The parameters to be converted into an option list.
     *  @return An <CODE>ArrayList</CODE> containing the converted names_ list
     */
    protected ArrayList getNameList(ArrayList names_)
    {
        ArrayList option_list = new ArrayList();

        for(int i = 0 ; i < names_.size() ; i++)
        {
            option_list.add(new StringBuffer().append("<OPTION>").append((String)names_.get(i)).append("</OPTION>").toString() );
        }

        return option_list;
    }

    /**
     *  @param names_ The parameters to be converted into an option list.
     *  @param values_ The parameters to be included in the option tag that
     *                 will be returned to the web tier on a request.
     *  @return An <CODE>ArrayList</CODE> containing the converted names_ list
     */
    protected ArrayList getNameValueList(ArrayList values_, ArrayList names_)
    {
        ArrayList option_list = new ArrayList();

        for(int i = 0 ; i < names_.size() ; i++)
        {
            option_list.add(new StringBuffer().append("<OPTION VALUE=\"").append((String)values_.get(i)) .append("\">").append((String)names_.get(i)).append("</OPTION>").toString() );
        }

        return option_list;
    }

    /**
     *  @param names_ The parameters to be converted into an option list.
     *  @param select_ The display item in the option list to be the default selection
     *  @return An <CODE>ArrayList</CODE> containing the converted names_ list
     */
    protected ArrayList getSelectedNameListByName(ArrayList names_, String select_)
    {
        ArrayList option_list = new ArrayList();

        for(int i = 0 ; i < names_.size() ; i++)
        {
            StringBuffer buf = new StringBuffer();

            buf.append("<OPTION");

            if( select_.equals(names_.get(i)) )
            {
                buf.append(" SELECTED");
            }

            buf.append(">");
            buf.append( ((String)names_.get(i)) );
            buf.append("</OPTION>");

            option_list.add( buf.toString() );

            buf = null;
        }

        return option_list;
    }

    /**
     *  @param names_ The parameters to be converted into an option list.
     *  @param select_ The display item in the option list to be the default selection
     *  @return An <CODE>ArrayList</CODE> containing the converted names_ list
     */
    protected ArrayList getSelectedNameListByIndex(ArrayList names_, int index_)
    {
        ArrayList option_list = new ArrayList();

        for(int i = 0 ; i < names_.size() ; i++)
        {
            StringBuffer buf = new StringBuffer();

            buf.append("<OPTION");

            if( i == index_ )
            {
                buf.append(" SELECTED");
            }

            buf.append(">");
            buf.append( ((String)names_.get(i)) );
            buf.append("</OPTION>");

            option_list.add( buf.toString() );

            buf = null;
        }

        return option_list;
    }

    /**
     *  @param names_ The parameters to be converted into an option list.
     *  @param values_ The parameters to be included in the option tag that
     *                 will be returned to the web tier on a request.
     *  @param select_ The display item in the option list to be the default selection
     *  @return An <CODE>ArrayList</CODE> containing the converted names_ list
     */
    protected ArrayList getSelectedNameValueListByName(ArrayList values_, ArrayList names_, String select_)
    {
        ArrayList option_list = new ArrayList();

        for(int i = 0 ; i < names_.size() ; i++)
        {
            StringBuffer buf = new StringBuffer();

            buf.append("<OPTION VALUE=\"");
            buf.append( values_.get(i) );
            buf.append("\"");

            if( select_.equals(names_.get(i)) )
            {
                buf.append(" SELECTED");
            }

            buf.append(">");
            buf.append( ((String)names_.get(i)) );
            buf.append("</OPTION>");

            option_list.add( buf.toString() );

            buf = null;
        }

        return option_list;
    }

    /**
     *  @param names_ The parameters to be converted into an option list.
     *  @param values_ The parameters to be included in the option tag that
     *                 will be returned to the web tier on a request.
     *  @param select_ The value of the display item in the option list to be the default selection
     *  @return An <CODE>ArrayList</CODE> containing the converted names_ list
     */
    protected ArrayList getSelectedNameValueListByValue(ArrayList values_, ArrayList names_, String select_)
    {
        ArrayList option_list = new ArrayList();
        if (select_ == null)
            return option_list;
        
        for(int i = 0 ; i < names_.size() ; i++)
        {
            StringBuffer buf = new StringBuffer();

            buf.append("<OPTION VALUE=\"");
            buf.append( values_.get(i) );
            buf.append("\"");

            if( select_.equals(values_.get(i)) )
            {
                buf.append(" SELECTED");
            }

            buf.append(">");
            buf.append( ((String)names_.get(i)) );
            buf.append("</OPTION>");

            option_list.add( buf.toString() );

            buf = null;
        }

        return option_list;
    }

    /**
     *  @param names_ The parameters to be converted into an option list.
     *  @param values_ The parameters to be included in the option tag that
     *                 will be returned to the web tier on a request.
     *  @param index_ The index of the display item in the option list to be the default selection
     *  @return An <CODE>ArrayList</CODE> containing the converted names_ list
     */
    protected ArrayList getSelectedNameValueListByIndex(ArrayList values_, ArrayList names_, int index_)
    {
        ArrayList option_list = new ArrayList();

        for(int i = 0 ; i < names_.size() ; i++)
        {
            StringBuffer buf = new StringBuffer();

            buf.append("<OPTION VALUE=\"");
            buf.append( values_.get(i) );
            buf.append("\"");

            if( i == index_ )
            {
                buf.append(" SELECTED");
            }

            buf.append(">");
            buf.append( ((String)names_.get(i)) );
            buf.append("</OPTION>");

            option_list.add( buf.toString() );

            buf = null;
        }

        return option_list;
    }

    protected ArrayList getNameList()
    {
        return _display_items;
    }

    protected void setNameList(ArrayList names_)
    {
        _display_items = names_;
    }

    protected ArrayList getValueList()
    {
        return _item_values;
    }

    protected void setValueList(ArrayList values_)
    {
        _item_values = values_;
    }
}
