package com.ctb.lexington.util;

/**
 *  @author mclemens
 */
import java.util.ArrayList;

public class OSOptionList extends com.ctb.lexington.util.OptionList
{
    public static final String OBJECT_LABEL = "osOptionList";

    public static final String LONG_SALUTATION = "Select Operating System...";

    public static final String SHORT_SALUTATION = "OS...";

    public static final String SALUTATION = LONG_SALUTATION;

    private ArrayList _display_items  = null;
    private ArrayList _item_values = null;

    public OSOptionList()
    {
        _display_items = new ArrayList();
        _item_values = new ArrayList();

        _display_items.add("Mac OS 7.x");
        _display_items.add("Mac OS 8.x");
        _display_items.add("Mac OS 9.x");
        _display_items.add("Windows 95");
        _display_items.add("Windows ME");
        _display_items.add("Windows NT 4.0 SP 3");
        _display_items.add("Windows 98");
        _display_items.add("Windows 2000");
        _display_items.add("Other");
        _display_items.add("Not sure");

        _item_values.add("Mac OS 7.x");
        _item_values.add("Mac OS 8.x");
        _item_values.add("Mac OS 9.x");
        _item_values.add("Windows 95");
        _item_values.add("Windows ME");
        _item_values.add("Windows NT 4.0 SP 3");
        _item_values.add("Windows 98");
        _item_values.add("Windows 2000");
        _item_values.add("Other");
        _item_values.add("Not sure");

        setNameList(_display_items);
        setValueList(_item_values);
    }

}