package com.ctb.lexington.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class YearOptionList extends DateOptionList
{
    /** DOCUMENT ME! */
    public static final String OBJECT_LABEL = "yearOptionList";

    /** DOCUMENT ME! */
    public static final String LONG_SALUTATION = "Select a year...";

    /** DOCUMENT ME! */
    public static final String SHORT_SALUTATION = "Year...";

    /** DOCUMENT ME! */
    public static final String BLANK_SALUTATION = "";

    /** DOCUMENT ME! */
    public static final String SALUTATION = LONG_SALUTATION;
    private ArrayList _display_items = null;
    private ArrayList _item_values = null;
    private int _end_year;
    private int _start_year;

    /**
     * Creates a new YearOptionList object.
     *
     * @param start_year_parameter_ DOCUMENT ME!
     * @param end_year_parameter_ DOCUMENT ME!
     */
    public YearOptionList(String start_year_parameter_,
                          String end_year_parameter_)
    {
        int start_year = buildStartYear(start_year_parameter_);
        int end_year = buildEndYear(end_year_parameter_, start_year);

        initializeList(start_year, end_year);
    }

    /**
     * Creates a new YearOptionList object.
     *
     * @param start_year_ DOCUMENT ME!
     * @param end_year_ DOCUMENT ME!
     */
    public YearOptionList(int start_year_, int end_year_)
    {
        initializeList(start_year_, end_year_);
    }

    /**
     * @param selection_ A date that should be used to pre-select the year.
     *
     * @return An option list with the item pre-selected to the year
     *         specified&nbsp; in the date object.&nbsp; If the year in the
     *         date object does not&nbsp; match a year in the list, no item
     *         will be preselected.
     */
    public ArrayList getSelectedYears(Date selection_)
    {
        return getSelectedNameValueListByIndex(_item_values, _display_items,
                                               getIndex(selection_));
    }

    //*****************************************************************************
    //
    //  Public methods to handle long month option lists
    //
    //*****************************************************************************

    /**
     * @return An option list with long month names and the numeric values.
     */
    public ArrayList getUnselectedYears()
    {
        return getUnselectedList();
    }

    /**
     * @param salutation_ A string to be included at the top of the&nbsp;
     *        option list.
     *
     * @return An option list with long month names and the numeric&nbsp;
     *         values.
     */
    public ArrayList getUnselectedYearsWithSalutation(String salutation_)
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
     * @param date_ The value to be converted into an index for the day array.
     *
     * @return The index of a day value in the array for the date passed in.
     */
    protected int getIndex(Date date_)
    {
        Calendar cal = Calendar.getInstance();

        cal.setTime(date_);

        int cal_year = cal.get(Calendar.YEAR);

        if ((cal_year >= _start_year) && (cal_year <= _end_year))
        {
            return cal_year - _start_year;
        }
        else
        {
            return OptionList.NO_INDEX;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param end_year_parameter_ DOCUMENT ME!
     * @param start_year_ DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected int buildEndYear(String end_year_parameter_, int start_year_)
    {
        int end_year = Calendar.getInstance().get(Calendar.YEAR);

        if (end_year_parameter_.startsWith(CTBConstants.ADD_YEAR_VALUE))
        {
            end_year =
                start_year_ +
                Integer.parseInt(end_year_parameter_.substring(1));
        }
        else if (end_year_parameter_.startsWith(CTBConstants.SUBTRACT_YEAR_VALUE))
        {
            end_year =
                start_year_ -
                Integer.parseInt(end_year_parameter_.substring(1));
        }
        else if (end_year_parameter_.startsWith(CTBConstants.CURRENT_VALUE) &&
                     (
                         end_year_parameter_.length() > CTBConstants.CURRENT_VALUE.length()
                     ))
        {
            if (end_year_parameter_.indexOf(CTBConstants.ADD_YEAR_VALUE) > 0)
            {
                int index =
                    end_year_parameter_.indexOf(CTBConstants.ADD_YEAR_VALUE) +
                    1;

                end_year =
                    end_year +
                    Integer.parseInt(end_year_parameter_.substring(index));
            }
            else if (end_year_parameter_.indexOf(CTBConstants.SUBTRACT_YEAR_VALUE) > 0)
            {
                int index =
                    end_year_parameter_.indexOf(CTBConstants.SUBTRACT_YEAR_VALUE) +
                    1;

                end_year =
                    end_year -
                    Integer.parseInt(end_year_parameter_.substring(index));
            }
        }
        else if (!end_year_parameter_.equalsIgnoreCase(CTBConstants.CURRENT_VALUE))
        {
            end_year = Integer.parseInt(end_year_parameter_);
        }

        return end_year;
    }

    /**
     * DOCUMENT ME!
     *
     * @param start_year_parameter_ DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected int buildStartYear(String start_year_parameter_)
    {
        int start_year = Calendar.getInstance().get(Calendar.YEAR);

        if (start_year_parameter_.startsWith(CTBConstants.ADD_YEAR_VALUE))
        {
            start_year =
                start_year +
                Integer.parseInt(start_year_parameter_.substring(1));
        }
        else if (start_year_parameter_.startsWith(CTBConstants.SUBTRACT_YEAR_VALUE))
        {
            start_year =
                start_year -
                Integer.parseInt(start_year_parameter_.substring(1));
        }
        else if (!start_year_parameter_.equalsIgnoreCase(CTBConstants.CURRENT_VALUE))
        {
            start_year = Integer.parseInt(start_year_parameter_);
        }

        return start_year;
    }

    //*****************************************************************************
    //
    //  Protected methods
    //
    //*****************************************************************************
    protected void initializeList(int start_year_, int end_year_)
    {
        _start_year = start_year_;

        _end_year = end_year_;

        _display_items = new ArrayList();
        _item_values = new ArrayList();

        if (_start_year <= _end_year)
        {
            for (int i = _start_year; i <= _end_year; i++)
            {
                _display_items.add((new Integer(i)).toString());
                _item_values.add((new Integer(i)).toString());
            }
        }
        else
        {
            for (int i = _start_year; i >= _end_year; i--)
            {
                _display_items.add((new Integer(i)).toString());
                _item_values.add((new Integer(i)).toString());
            }
        }

        setNameList(_display_items);
        setValueList(_item_values);
    }
}
