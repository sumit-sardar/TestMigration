package com.ctb.lexington.util;

/**
 *  @author mclemens
 */
import java.util.ArrayList;

public class ContactTypeOptionList extends com.ctb.lexington.util.OptionList
{
    public static final String OBJECT_LABEL = "contactTypeOptionList";

    public static final String LONG_SALUTATION  = "Select a contact type...";
    public static final String SHORT_SALUTATION = "Contact type...";

    public static final String SALUTATION = "Select a contact type...";

    private ArrayList _display_items  = null;
    private ArrayList _item_values = null;

    public ContactTypeOptionList()
    {
        _display_items = new ArrayList();

        _item_values = new ArrayList();

        _display_items.add("Parent");
        _item_values.add("P");

        _display_items.add("Family Member");
        _item_values.add("F");

        _display_items.add("Guardian");
        _item_values.add("G");

        _display_items.add("Other");
        _item_values.add("O");
        
        _display_items.add("Unknown");
        _item_values.add("U");

        setNameList(_display_items);
        setValueList(_item_values);
    }


//*****************************************************************************
//
//  Detailed methods for the contact option list should be removed.
//
//*****************************************************************************

    /**
     *  @return An option list of contact types.
     */
    public ArrayList getUnselectedContactTypes()
    {
        return getNameValueList(_item_values, _display_items);
    }

    /**
     *  @param salutation_ The first item to be listed in the combo box.
     *  @return An option list of contact types with the salutation in the first position.
     */
    public ArrayList getUnselectedContactTypesWithSalutation(String salutation_)
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
    public ArrayList getSelectedContactTypesByName(String name_)
    {
        return getSelectedNameValueListByName(_item_values, _display_items, name_);
    }

    /**
     *  @param name_ The display item to be selected in the drop down box.&nbsp;
     *               If the item does not exist in the list of display items, then
     *               nothing will be pre-selected.
     *  @return  An option list with the item selected corresponding to the value passed in.
     */
    public ArrayList getSelectedContactTypesByValue(String value_)
    {
        return getSelectedNameValueListByValue(_item_values, _display_items, value_);
    }
}