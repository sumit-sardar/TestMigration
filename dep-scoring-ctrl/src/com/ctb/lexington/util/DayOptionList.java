package com.ctb.lexington.util;

/**
 *  @author mclemens
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DayOptionList extends com.ctb.lexington.util.DateOptionList
{
    public static final String OBJECT_LABEL = "dayOptionList";

    public static final String LONG_SALUTATION = "Select a day...";

    public static final String SHORT_SALUTATION = "Day...";

    public static final String SALUTATION = LONG_SALUTATION;

    private ArrayList _display_items  = null;
    private ArrayList _item_values = null;

    public DayOptionList()
    {
        _item_values = new ArrayList();
        _display_items = new ArrayList();

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
        _item_values.add("14");
        _item_values.add("15");
        _item_values.add("16");
        _item_values.add("17");
        _item_values.add("18");
        _item_values.add("19");
        _item_values.add("20");
        _item_values.add("21");
        _item_values.add("22");
        _item_values.add("23");
        _item_values.add("24");
        _item_values.add("25");
        _item_values.add("26");
        _item_values.add("27");
        _item_values.add("28");
        _item_values.add("29");
        _item_values.add("30");
        _item_values.add("31");

        _display_items.add("01");
        _display_items.add("02");
        _display_items.add("03");
        _display_items.add("04");
        _display_items.add("05");
        _display_items.add("06");
        _display_items.add("07");
        _display_items.add("08");
        _display_items.add("09");
        _display_items.add("10");
        _display_items.add("11");
        _display_items.add("12");
        _display_items.add("13");
        _display_items.add("14");
        _display_items.add("15");
        _display_items.add("16");
        _display_items.add("17");
        _display_items.add("18");
        _display_items.add("19");
        _display_items.add("20");
        _display_items.add("21");
        _display_items.add("22");
        _display_items.add("23");
        _display_items.add("24");
        _display_items.add("25");
        _display_items.add("26");
        _display_items.add("27");
        _display_items.add("28");
        _display_items.add("29");
        _display_items.add("30");
        _display_items.add("31");

        setNameList(_display_items);
        setValueList(_item_values);
    }

//*****************************************************************************
//
//  Public methods to handle long month option lists
//
//*****************************************************************************

    /**
     *  @return An option list with long month names and the numeric
     *          values.
     */
    public ArrayList getUnselectedDays()
    {
        return getNameValueList(_item_values, _display_items);
    }

    /**
     *  @param salutation_ A string to be included at the top of the&nbsp;
     *                     option list.
     *  @return An option list with long month names and the numeric&nbsp;
     *          values.
     */
    public ArrayList getUnselectedDaysWithSalutation(String salutation_)
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
     *  @param selection_ A date that should be used to pre-select the month
     *  @return An option list with long month names and the numeric&nbsp;
     *          values with the month specified in the date object pre-selected.
     */
    public ArrayList getSelectedDays(Date selection_)
    {
        return getSelectedNameValueListByIndex( _item_values, _display_items, getIndex(selection_) );
    }

//*****************************************************************************
//
//  Protected methods
//
//*****************************************************************************

    /**
     *  @param date_ The value to be converted into an index for the day array.
     *  @return The index of a day value in the array for the date passed in.
     */
    protected int getIndex(Date date_)
    {
        Calendar cal = Calendar.getInstance();

        cal.setTime(date_);

        return cal.get(Calendar.DAY_OF_MONTH) - 1;
    }

}