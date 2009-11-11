package com.ctb.lexington.util;

/**
 *  @author mclemens
 */
import java.util.ArrayList;

public class RAMOptionList extends com.ctb.lexington.util.OptionList
{
    public static final String OBJECT_LABEL = "ramOptionList";

    public static final String LONG_SALUTATION = "Select RAM...";

    public static final String SHORT_SALUTATION = "RAM...";

    public static final String SALUTATION = LONG_SALUTATION;

    private ArrayList _display_items  = null;
    private ArrayList _item_values = null;

    public RAMOptionList()
    {
        _display_items = new ArrayList();
        _item_values = new ArrayList();

        _display_items.add("Below 64 MB");
        _display_items.add("Between 64 MB and 128 MB");
        _display_items.add("Between 128 MB and 256 MB");
        _display_items.add("Between 256 MB and 512 MB");
        _display_items.add("Above 512 MB");
        _display_items.add("Not Sure");

         _item_values.add("Below 64 MB");
         _item_values.add("Between 64 MB and 128 MB");
         _item_values.add("Between 128 MB and 256 MB");
         _item_values.add("Between 256 MB and 512 MB");
         _item_values.add("Above 512 MB");
         _item_values.add("Not Sure");

        setNameList(_display_items);
        setValueList(_item_values);
    }

}