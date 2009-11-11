package com.ctb.lexington.util;

/**
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class LongMonthOptionList extends com.ctb.lexington.util.DateOptionList
{
    /** DOCUMENT ME! */
    public static final String OBJECT_LABEL = "monthOptionList";

    /** DOCUMENT ME! */
    public static final String LONG_SALUTATION = "Select a month...";

    /** DOCUMENT ME! */
    public static final String SHORT_SALUTATION = "Month...";

    /** DOCUMENT ME! */
    public static final String SALUTATION = LONG_SALUTATION;
    private ArrayList _display_items = null;
    private ArrayList _item_values = null;

    /**
     * Creates a new LongMonthOptionList object.
     */
    public LongMonthOptionList()
    {
        _display_items = new ArrayList();
        _item_values = new ArrayList();

        _display_items.add("January");
        _display_items.add("February");
        _display_items.add("March");
        _display_items.add("April");
        _display_items.add("May");
        _display_items.add("June");
        _display_items.add("July");
        _display_items.add("August");
        _display_items.add("September");
        _display_items.add("October");
        _display_items.add("November");
        _display_items.add("December");

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
     * @param date_ The value to be converted into an index for the month
     *        array.
     *
     * @return The index of a month value in the array for the date passed in.
     */
    protected int getIndex(Date date_)
    {
        Calendar cal = Calendar.getInstance();

        cal.setTime(date_);

        return cal.get(Calendar.MONTH);
    }
}
