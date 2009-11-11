package com.ctb.lexington.util;

/**
 *  @author mclemens
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ShortMonthOptionList extends com.ctb.lexington.util.DateOptionList
{
    public static final String OBJECT_LABEL = "monthOptionList";

    public static final String LONG_SALUTATION  = "Select a month...";

    public static final String SHORT_SALUTATION = "Month...";

    public static final String SALUTATION = LONG_SALUTATION;

    private ArrayList _display_items = null;
    private ArrayList _item_values   = null;

    public ShortMonthOptionList()
    {
        _display_items = new ArrayList();
        _item_values   = new ArrayList();

        _display_items.add("Jan");
        _display_items.add("Feb");
        _display_items.add("Mar");
        _display_items.add("Apr");
        _display_items.add("May");
        _display_items.add("Jun");
        _display_items.add("Jul");
        _display_items.add("Aug");
        _display_items.add("Sep");
        _display_items.add("Oct");
        _display_items.add("Nov");
        _display_items.add("Dec");

        _item_values.add("0");
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

        setNameList(_display_items);
        setValueList(_item_values);
    }

//*****************************************************************************
//
//  Protected methods
//
//*****************************************************************************
    /**
     *  @param date_ The value to be converted into an index for the month array.
     *  @return The index of a month value in the array for the date passed in.
     */
    protected int getIndex(Date date_)
    {
        Calendar cal = Calendar.getInstance();

        cal.setTime(date_);

        return cal.get(Calendar.MONTH);
    }

}