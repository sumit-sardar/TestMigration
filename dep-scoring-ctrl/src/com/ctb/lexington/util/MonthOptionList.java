package com.ctb.lexington.util;

/**
 *  @author mclemens
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MonthOptionList extends com.ctb.lexington.util.DateOptionList
{
    public static final String OBJECT_LABEL = "monthOptionList";

    public static final String LONG_SALUTATION  = "Select a month...";

    public static final String SHORT_SALUTATION = "Month...";

    public static final String SALUTATION = LONG_SALUTATION;

    private ArrayList _long_month_items    = null;
    private ArrayList _short_month_items   = null;
    private ArrayList _numeric_month_items = null;
    private ArrayList _item_values         = null;

    public MonthOptionList()
    {
        _long_month_items    = new ArrayList();
        _short_month_items   = new ArrayList();
        _numeric_month_items = new ArrayList();
        _item_values         = new ArrayList();

        _long_month_items.add("January");
        _long_month_items.add("February");
        _long_month_items.add("March");
        _long_month_items.add("April");
        _long_month_items.add("May");
        _long_month_items.add("June");
        _long_month_items.add("July");
        _long_month_items.add("August");
        _long_month_items.add("September");
        _long_month_items.add("October");
        _long_month_items.add("November");
        _long_month_items.add("December");

        _short_month_items.add("Jan");
        _short_month_items.add("Feb");
        _short_month_items.add("Mar");
        _short_month_items.add("Apr");
        _short_month_items.add("May");
        _short_month_items.add("Jun");
        _short_month_items.add("Jul");
        _short_month_items.add("Aug");
        _short_month_items.add("Sep");
        _short_month_items.add("Oct");
        _short_month_items.add("Nov");
        _short_month_items.add("Dec");

        _numeric_month_items.add("01");
        _numeric_month_items.add("02");
        _numeric_month_items.add("03");
        _numeric_month_items.add("04");
        _numeric_month_items.add("05");
        _numeric_month_items.add("06");
        _numeric_month_items.add("07");
        _numeric_month_items.add("08");
        _numeric_month_items.add("09");
        _numeric_month_items.add("10");
        _numeric_month_items.add("11");
        _numeric_month_items.add("12");

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

        setNameList(_short_month_items);
        setValueList(_item_values);
    }

//*****************************************************************************
//
//  Public methods to handle long month option lists
//
//*****************************************************************************
    /**
     *  @param selection_ The date used to select the long month.
     *  @return The long month displayed in the drop-down list
     *          for the given date.
     */
    public String getLongMonthName(Date selection_)
    {
        return (String)_long_month_items.get( getIndex(selection_) );
    }

    /**
     *  @return An option list with long month names and the numeric
     *          values.
     */
    public ArrayList getUnselectedLongMonths()
    {
        return getNameValueList(_item_values, _long_month_items);
    }

    /**
     *  @param salutation_ A string to be included at the top of the&nbsp;
     *                     option list.
     *  @return An option list with long month names and the numeric&nbsp;
     *          values.
     */
    public ArrayList getUnselectedLongMonthsWithSalutation(String salutation_)
    {
        //the clone method on the _display_items object makes a shallow copy.
        // this shallow copy will still point to the initial values, but now
        // we can add items without affecting the original list.
        ArrayList list_clone = (ArrayList)_long_month_items.clone();

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
    public ArrayList getSelectedLongMonths(Date selection_)
    {
        return getSelectedNameValueListByIndex(_item_values, _long_month_items, getIndex(selection_) );
    }

    /**
     *  @param selection_ A date that should be used to pre-select the month
     *  @return An option list with long month names and the numeric&nbsp;
     *          values with the month specified in the date object pre-selected.
     */
    public ArrayList getSelectedLongMonthsWithSalutation(String salutation_, Date selection_)
    {
        //the clone method on the _display_items object makes a shallow copy.
        // this shallow copy will still point to the initial values, but now
        // we can add items without affecting the original list.
        ArrayList list_clone = (ArrayList)_long_month_items.clone();

        // clone the value list because we will need to add a start value for
        // the salutation.
        ArrayList value_clone = (ArrayList)_item_values.clone();

        list_clone.add(0, salutation_);

        value_clone.add(0, OptionList.DEFAULT_SALUTATION_VALUE);

        return getSelectedNameValueListByIndex(value_clone, list_clone, getIndex(selection_) + 1 );
    }

//*****************************************************************************
//
//  Public methods to handle short month option lists
//
//*****************************************************************************

    /**
     *  @param selection_ The date used to select the short month.
     *  @return The short month displayed in the drop-down list
     *          for the given date.
     */
    public String getShortMonthName(Date selection_)
    {
        return (String)_short_month_items.get( getIndex(selection_) );
    }

    /**
     *  @return An option list with short month names and the numeric
     *          values.
     */
    public ArrayList getUnselectedShortMonths()
    {
        return getNameValueList(_item_values, _short_month_items);
    }

    /**
     *  @param salutation_ A string to be included at the top of the&nbsp;
     *                     option list.
     *  @return An option list with short month names and the numeric&nbsp;
     *          values.
     */
    public ArrayList getUnselectedShortMonthsWithSalutation(String salutation_)
    {
        //the clone method on the _display_items object makes a shallow copy.
        // this shallow copy will still point to the initial values, but now
        // we can add items without affecting the original list.
        ArrayList list_clone = (ArrayList)_short_month_items.clone();

        // clone the value list because we will need to add a start value for
        // the salutation.
        ArrayList value_clone = (ArrayList)_item_values.clone();

        list_clone.add(0, salutation_);

        value_clone.add(0, OptionList.DEFAULT_SALUTATION_VALUE);

        return getNameValueList(value_clone, list_clone);
    }

    /**
     *  @param selection_ A date that should be used to pre-select the month
     *  @return An option list with short month names and the numeric&nbsp;
     *          values with the month specified in the date object pre-selected.
     */
    public ArrayList getSelectedShortMonths(Date selection_)
    {
        return getSelectedNameValueListByIndex(_item_values, _short_month_items, getIndex(selection_) );
    }

    /**
     *  @param selection_ A date that should be used to pre-select the month
     *  @return An option list with short month names and the numeric&nbsp;
     *          values with the month specified in the date object pre-selected.
     */
    public ArrayList getSelectedShortMonthsWithSalutation(String salutation_, Date selection_)
    {
        //the clone method on the _display_items object makes a shallow copy.
        // this shallow copy will still point to the initial values, but now
        // we can add items without affecting the original list.
        ArrayList list_clone = (ArrayList)_short_month_items.clone();

        // clone the value list because we will need to add a start value for
        // the salutation.
        ArrayList value_clone = (ArrayList)_item_values.clone();

        list_clone.add(0, salutation_);

        value_clone.add(0, OptionList.DEFAULT_SALUTATION_VALUE);

        return getSelectedNameValueListByIndex(value_clone, list_clone, getIndex(selection_) + 1 );
    }

//*****************************************************************************
//
//  Public methods to handle numeric month option lists
//
//*****************************************************************************

    /**
     *  @return An option list with numeric values representing the months.
     */
    public ArrayList getUnselectedNumericMonths()
    {
        return getNameList(_item_values);
    }

    /**
     *  @param salutation_ A string to be included at the top of the&nbsp;
     *                     option list.
     *  @return An option list with numeric values representing the months.
     */
    public ArrayList getUnselectedNumericMonthsWithSalutation(String salutation_)
    {
        //the clone method on the _display_items object makes a shallow copy.
        // this shallow copy will still point to the initial values, but now
        // we can add items without affecting the original list.
        ArrayList list_clone = (ArrayList)_numeric_month_items.clone();

        list_clone.add(0, salutation_);

        // clone the value list because we will need to add a start value for
        // the salutation.
        ArrayList value_clone = (ArrayList)_item_values.clone();

        list_clone.add(0, salutation_);

        value_clone.add(0, OptionList.DEFAULT_SALUTATION_VALUE);

        return getNameValueList(value_clone, list_clone);
    }

    /**
     *  @param selection_ A date that should be used to pre-select the month
     *  @return An option list with numeric values representing the months&nbsp;
     *          with the month specified in the date object pre-selected.
     */
    public ArrayList getSelectedNumericMonths(Date selection_)
    {
        return getSelectedNameValueListByIndex(_item_values, _numeric_month_items, getIndex(selection_) );
    }

    /**
     *  @param selection_ A date that should be used to pre-select the month
     *  @return An option list with numeric values representing the months&nbsp;
     *          with the month specified in the date object pre-selected.
     */
    public ArrayList getSelectedNumericMonthsWithSalutation(String salutation_, Date selection_)
    {
        //the clone method on the _display_items object makes a shallow copy.
        // this shallow copy will still point to the initial values, but now
        // we can add items without affecting the original list.
        ArrayList list_clone = (ArrayList)_numeric_month_items.clone();

        list_clone.add(0, salutation_);

        // clone the value list because we will need to add a start value for
        // the salutation.
        ArrayList value_clone = (ArrayList)_item_values.clone();

        list_clone.add(0, salutation_);

        value_clone.add(0, OptionList.DEFAULT_SALUTATION_VALUE);

        return getSelectedNameValueListByIndex(_item_values, _numeric_month_items, getIndex(selection_) + 1 );
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