package com.ctb.lexington.util;

/**
 *  @author mclemens
 */
import java.util.ArrayList;

public class BrowserTypeOptionList extends com.ctb.lexington.util.OptionList
{
    public static final String OBJECT_LABEL = "browserOptionList";

    public static final String LONG_SALUTATION = "Select Browser Type...";

    public static final String SHORT_SALUTATION = "Browser...";

    public static final String SALUTATION = LONG_SALUTATION;

    private ArrayList _display_items  = null;
    private ArrayList _item_values = null;

    public BrowserTypeOptionList()
    {
        _display_items = new ArrayList();
        _item_values = new ArrayList();

        _display_items.add("Netscape 4.7.x");
        _display_items.add("Netscape 6.x");
        _display_items.add("Internet Explorer 5.x");
        _display_items.add("Internet Explorer 6.x");
        _display_items.add("Other");
        _display_items.add("Not sure");

        _item_values.add("Netscape 4.7.x");
        _item_values.add("Netscape 6.x");
        _item_values.add("Internet Explorer 5.x");
        _item_values.add("Internet Explorer 6.x");
        _item_values.add("Other");
        _item_values.add("Not sure");

        setNameList(_display_items);
        setValueList(_item_values);
    }

}