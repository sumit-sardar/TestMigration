package com.ctb.lexington.util;


/**
 *
 */
import java.util.ArrayList;
import java.util.Date;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public abstract class DateOptionList extends com.ctb.lexington.util.OptionList
{
    /**
         *
         */
    public String getDateName(Date select_)
    {
        return (String)getNameList().get(getIndex(select_));
    }

    /**
         *
         */
    public String getDateValue(Date select_)
    {
        return (String)getValueList().get(getIndex(select_));
    }

    /**
         *
         */
    public ArrayList getSelectedListByDate(Date selection_)
    {
        return getSelectedNameValueListByIndex(getValueList(), getNameList(),
                                               getIndex(selection_));
    }

    /**
         *
         */
    public ArrayList getSelectedListWithSalutationByDate(String salutation_,
                                                         Date selection_)
    {
        //the clone method on the _display_items object makes a shallow copy.
        // this shallow copy will still point to the initial values, but now
        // we can add items without affecting the original list.
        ArrayList list_clone = (ArrayList)(getNameList().clone());

        // clone the value list because we will need to add a start value for
        // the salutation.
        ArrayList value_clone = (ArrayList)(getValueList().clone());

        list_clone.add(0, salutation_);

        value_clone.add(0, OptionList.DEFAULT_SALUTATION_VALUE);

        return getSelectedNameValueListByIndex(value_clone, list_clone,
                                               getIndex(selection_) + 1);
    }

    /**
         *
         */
    public boolean hasDateName(Date selection_)
    {
        if (getIndex(selection_) == OptionList.NO_INDEX)
        {
            return false;
        }

        return true;
    }

    /**
         *
         */
    public boolean hasDateValue(Date selection_)
    {
        if (getIndex(selection_) == OptionList.NO_INDEX)
        {
            return false;
        }

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param date_ DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected abstract int getIndex(Date date_);
}
