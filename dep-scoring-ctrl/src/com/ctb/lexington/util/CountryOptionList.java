package com.ctb.lexington.util;

/**
 *  @author mclemens
 */
import java.util.ArrayList;

public class CountryOptionList extends com.ctb.lexington.util.OptionList
{
    public static final String OBJECT_LABEL = "countryOptionList";

    public static final String LONG_SALUTATION = "Select a country...";

    public static final String SHORT_SALUTATION = "Country...";

    private ArrayList _display_items  = null;
    private ArrayList _item_values = null;

    public CountryOptionList()
    {
        _display_items = new ArrayList();
        _item_values = new ArrayList();
        
        _display_items.add("United States of America");
        _item_values.add("US");

        _display_items.add("Canada");
        _item_values.add("CA");

        setNameList(_display_items);
        setValueList(_item_values);
    }

    /**
     *  @return The name displayed in the drop-down list for the given value.  If a&nbsp;
     *          match is not made, then the method returns <CODE>null</CODE>.
     */
    public String getCountryName(String value_)
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
     *  @return An option list of countries.
     */
    public ArrayList getUnselectedCountries()
    {
        return getNameValueList(_item_values, _display_items);
    }

    /**
     *  @param salutation_ The first item to be listed in the combo box.
     *  @return An option list of countries with the salutation in the first position.
     */
    public ArrayList getUnselectedCountriesWithSalutation(String salutation_)
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
     *               nothing will be pre-selected.
     *  @return An option list with the item selected corresponding to the name passed in.
     */
    public ArrayList getSelectedCountriesByName(String name_)
    {
        return getSelectedNameValueListByName(_item_values, _display_items, name_);
    }

    /**
     *  @param name_ The display item to be selected in the drop down box.&nbsp;
     *               If the item does not exist in the list of display items, then
     *               nothing will be pre-selected.
     *  @return  An option list with the item selected corresponding to the value passed in.
     */
    public ArrayList getSelectedCountriesByValue(String value_)
    {
        return getSelectedNameValueListByValue(_item_values, _display_items, value_);
    }
}