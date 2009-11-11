package com.ctb.lexington.util;

/**
 *  @author mclemens
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class LoginTimeOptionList extends com.ctb.lexington.util.DateOptionList
{
    public static final String OBJECT_LABEL = "loginOptionList";

    public static final String LONG_SALUTATION = "Select a login time...";

    public static final String SHORT_SALUTATION = "Login time...";

    public static final String SALUTATION = LONG_SALUTATION;

    private ArrayList _display_items = null;
    private ArrayList _item_values = null;

    private Calendar _start_time;

    private Calendar _end_time;

    private int _step_minutes;

    public LoginTimeOptionList(String start_time_, String end_time_, String step_time_)
    {
        initializeList( convertStringToCalendar(start_time_), convertStringToCalendar(end_time_), Integer.parseInt(step_time_) );
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
    public ArrayList getUnselectedLoginTimes()
    {
        return getNameValueList(_item_values, _display_items);
    }

    /**
     *  @param salutation_ A string to be included at the top of the&nbsp;
     *                     option list.
     *  @return An option list with long month names and the numeric&nbsp;
     *          values.
     */
    public ArrayList getUnselectedLoginTimesWithSalutation(String salutation_)
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
    public ArrayList getSelectedLoginTimes(Date selection_)
    {
        return getSelectedNameValueListByName(_item_values, _display_items, buildTimeString(selection_) );
    }
    
    public ArrayList getSelectedLoginTimeByValue(Calendar selection_)
    {
       return getSelectedNameValueListByName(_item_values, _display_items, buildTimeString(selection_) );
    }

//*****************************************************************************
//
//  Protected methods
//
//*****************************************************************************

    protected void initializeList(Calendar start_time_, Calendar end_time_, int step_time_)
    {
        _start_time = start_time_;

        Calendar roll_cal = (Calendar)_start_time.clone();

        _end_time = end_time_;

        _step_minutes = step_time_;

        _display_items = new ArrayList();
        _item_values = new ArrayList();

        while( roll_cal.before(_end_time) || roll_cal.equals(_end_time) )
        {
            _display_items.add(buildTimeString(roll_cal));
            _item_values.add(buildValueString(roll_cal));

            roll_cal.add(Calendar.MINUTE, _step_minutes);
        }

        setNameList(_display_items);
        setValueList(_item_values);
    }

    protected int getIndex( Date selection_ )
    {
        Calendar cal = Calendar.getInstance();

        cal.setTime(selection_);

        String time_string = buildTimeString(cal);

        if( getNameList().contains(time_string) )
        {
            return getNameList().indexOf(time_string);
        }
        else
        {
            return OptionList.NO_INDEX;
        }
    }

    /**
     *
     */
    public String buildTimeString(Date date_)
    {
        Calendar cal = Calendar.getInstance();

        cal.setTime(date_);

        return buildTimeString(cal);
    }

    /**
     *
     */
    public String buildTimeString(Calendar cal_)
    {
        StringBuffer buf = new StringBuffer();
        
        if((cal_.get(Calendar.HOUR) < 10) && (cal_.get(Calendar.HOUR) > 0))
        {
            buf.append("0");
        }
        
                
        if(cal_.get(Calendar.HOUR) == 0 )
        {
            buf.append("12");
        }
        else 
        {
            buf.append( Integer.toString(cal_.get(Calendar.HOUR)) );
            
        }
        
        
        buf.append(":");

        if(cal_.get(Calendar.MINUTE) < 10)
        {
            buf.append("0");
        }

        buf.append( Integer.toString(cal_.get(Calendar.MINUTE)) );

        if(cal_.get(Calendar.AM_PM) == Calendar.AM)
        {
            buf.append(" AM");
        }
        else
        {
            buf.append(" PM");
        }

        return buf.toString();
    }

    protected String buildValueString(Date date_)
    {
        Calendar cal = Calendar.getInstance();

        cal.setTime(date_);

        return buildValueString(cal);
    }

    /**
     * Returns the 'value' string of the calendar passed. 
     */
    public String buildValueString(Calendar cal_)
    {
        StringBuffer buf = new StringBuffer();

        if ( cal_.get(Calendar.HOUR_OF_DAY) < 10)
        {
            buf.append( "0" ).append(cal_.get(Calendar.HOUR_OF_DAY));
        }
        else
        {
            buf.append(cal_.get(Calendar.HOUR_OF_DAY));
        }

        if( cal_.get(Calendar.MINUTE) < 10)
        {
            buf.append("0").append(cal_.get(Calendar.MINUTE));
        }
        else
        {
            buf.append(cal_.get(Calendar.MINUTE));
        }

        return buf.toString();
    }

    protected Calendar convertStringToCalendar(String time_)
    {
        Calendar cal = Calendar.getInstance();

        int hour = 1;
        int minute = 0;
        int sec = 0;

        int length = time_.length();

        if(length == 3)
        {
            hour = Integer.parseInt(time_.substring(0,1));
            minute = Integer.parseInt(time_.substring(1,3));

        }
        else if(length == 4)
        {
            hour = Integer.parseInt(time_.substring(0,2));
            minute = Integer.parseInt(time_.substring(2,4));
        }

        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, sec);

        return cal;
    }
}