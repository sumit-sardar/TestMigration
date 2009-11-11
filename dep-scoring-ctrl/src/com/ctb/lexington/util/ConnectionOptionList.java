package com.ctb.lexington.util;

/**
 *  @author mclemens
 */
import java.util.ArrayList;

public class ConnectionOptionList extends com.ctb.lexington.util.OptionList
{
    public static final String OBJECT_LABEL = "connectionOptionList";

    public static final String LONG_SALUTATION = "Select Connection...";

    public static final String SHORT_SALUTATION = "Connection...";

    public static final String SALUTATION = LONG_SALUTATION;

    private ArrayList _display_items  = null;
    private ArrayList _item_values = null;

    public ConnectionOptionList()
    {
        _display_items = new ArrayList();
        _item_values = new ArrayList();

        _display_items.add("T1");
        _display_items.add("ISDN");
        _display_items.add("DSL");
        _display_items.add("Cable Modem");
        _display_items.add("28.8 kbps modem");
        _display_items.add("56 kbps or faster modem");
        _display_items.add("Other");
        _display_items.add("Not Sure");
        
        _item_values.add("T1");
        _item_values.add("ISDN");
        _item_values.add("DSL");
        _item_values.add("Cable Modem");
        _item_values.add("28.8 kbps modem");
        _item_values.add("56 kbps or faster modem");
        _item_values.add("Other");
        _item_values.add("Not Sure");

        setNameList(_display_items);
        setValueList(_item_values);
    }

}