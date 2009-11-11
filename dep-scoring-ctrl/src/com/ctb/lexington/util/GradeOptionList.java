package com.ctb.lexington.util;

/**
 *  @author mclemens
 */
import java.util.ArrayList;

public class GradeOptionList extends com.ctb.lexington.util.OptionList
{
    public static final String OBJECT_LABEL = "gradeOptionList";

    public static final String LONG_SALUTATION = "Select a grade...";

    public static final String SHORT_SALUTATION = "Grade...";

    public static final String SALUTATION = LONG_SALUTATION;

    private ArrayList _display_items  = null;
    private ArrayList _item_values = null;

    public GradeOptionList()
    {
        _display_items = new ArrayList();
        _item_values = new ArrayList();

        _display_items.add("K");
        _display_items.add("1");
        _display_items.add("2");
        _display_items.add("3");
        _display_items.add("4");
        _display_items.add("5");
        _display_items.add("6");
        _display_items.add("7");
        _display_items.add("8");
        _display_items.add("9");
        _display_items.add("10");
        _display_items.add("11");
        _display_items.add("12");
        _display_items.add("13");
        _display_items.add("AD");

        _item_values.add("K");
        _item_values.add("1");
        _item_values.add("2");
        _item_values.add("3");
        _item_values.add("4");
        _item_values.add("5");
        _item_values.add("6");
        _item_values.add("7");
        _item_values.add("8");
        _item_values.add("9");
        _item_values.add("10");
        _item_values.add("11");
        _item_values.add("12");
        _item_values.add("13");
        _item_values.add("AD");
 
        setNameList(_display_items);
        setValueList(_item_values);
    }

    /**
     *  @return An option list of grades.
     */
    public ArrayList getUnselectedGrades()
    {
        return getNameValueList(_item_values, _display_items);
    }

    /**
     *  @param salutation_ The first item to be listed in the combo box.
     *  @return An option list of grades with the salutation in the first position.
     */
    public ArrayList getUnselectedGradesWithSalutation(String salutation_)
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
     *               If the item does not exist in the list of display items, then&nbsp;
     *               nothing will be pre-selected.
     *  @return An option list with the item selected corresponding to the name passed in.
     */
    public ArrayList getSelectedGradesByName(String name_)
    {
        return getSelectedNameValueListByName(_item_values, _display_items, name_);
    }

    /**
     *  @param name_ The value of the display item to be selected in the drop down box.&nbsp;
     *               If the value does not exist in the list of display items, then&nbsp;
     *               nothing will be pre-selected.
     *  @return An option list with the item selected corresponding to the name passed in.
     */
    public ArrayList getSelectedGradesByValue(String value_)
    {
        return getSelectedNameValueListByValue(_item_values, _display_items, value_);
    }
}