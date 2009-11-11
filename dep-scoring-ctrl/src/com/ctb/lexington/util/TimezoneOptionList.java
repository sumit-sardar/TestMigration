package com.ctb.lexington.util;

/**
 * @author mclemens
 */
import java.util.ArrayList;

public class TimezoneOptionList extends com.ctb.lexington.util.OptionList
{
    public static final String OBJECT_LABEL = "timezoneOptionList";

    public static final String LONG_SALUTATION = "Select a timezone...";

    public static final String SHORT_SALUTATION = "Timezone...";

    public static final String SALUTATION = LONG_SALUTATION;

    private ArrayList _display_items  = null;
    private ArrayList _item_values = null;

    public TimezoneOptionList()
    {
        _display_items = new ArrayList();

        _item_values = new ArrayList();

        _display_items.add("HAWAII");
        _item_values.add("Pacific/Honolulu");
        
        _display_items.add("ALEUTIAN");
        _item_values.add("America/Adak");
        
        _display_items.add("ALASKA");
        _item_values.add("America/Anchorage");
        
        _display_items.add("PACIFIC");
        _item_values.add("America/Los_Angeles");

        _display_items.add("MOUNTAIN");
        _item_values.add("America/Denver");

        _display_items.add("CENTRAL");
        _item_values.add("America/Chicago");

        _display_items.add("EASTERN");
        _item_values.add("America/New_York");

        _display_items.add("GREENWICH MEAN");
        _item_values.add("GMT");

        setNameList(_display_items);
        setValueList(_item_values);
    }

    /**
     *  This method needs to be overriden in every child class
     *  @return An <CODE>ArrayList</CODE> containing options.  <CODE>null</CODE if no value match
     *          is made.
     */
    public String getTimezoneName(String value_)
    {
        int name_index = _item_values.indexOf(value_);

        if(name_index > 0)
        {
            return (String)_display_items.get(name_index);
        }
        else
        {
            return null;
        }
    }

    /**
     *  @return An <CODE>ArrayList</CODE> containing options for an HTML
     *          combo or list box.
     */
    public ArrayList getUnselectedTimezones()
    {
        return getNameValueList(_item_values, _display_items);
    }

    /**
     *  @param salutation_ The first item to be listed in the combo box.
     *  @return An <CODE>ArrayList</CODE> containing options for an HTML
     *          combo or list box with the first element being a salutation.
     */
    public ArrayList getUnselectedTimezonesWithSalutation(String salutation_)
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
     *  @param name_ The display item to be selected in the drop down box.&nbsp;
     *               If the item does not exist in the list of display items, then
     *               nothing will be preselected.
     *  @return An <CODE>ArrayList</CODE> containing options for an HTML
     *          combo or list box with a preselected item
     */
    public ArrayList getSelectedTimezonesByName(String name_)
    {
        return getSelectedNameValueListByName(_item_values, _display_items, name_);
    }

    /**
     *  This method is not used for this drop down box.
     *  @return <CODE>null</CODE>
     */
    public ArrayList getSelectedTimezonesByValue(String value_)
    {
        return getSelectedNameValueListByValue(_item_values, _display_items, value_);
    }
}