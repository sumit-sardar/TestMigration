package com.ctb.lexington.util;

/**
 *  @author mclemens
 */
import java.util.ArrayList;

public class CPUOptionList extends com.ctb.lexington.util.OptionList
{
    public static final String OBJECT_LABEL = "ramOptionList";

    public static final String LONG_SALUTATION = "Select a CPU...";

    public static final String SHORT_SALUTATION = "CPU...";

    public static final String SALUTATION = LONG_SALUTATION;

    private ArrayList _display_items  = null;
    private ArrayList _item_values = null;

    public CPUOptionList()
    {
        _display_items = new ArrayList();
        _item_values = new ArrayList();

        _display_items.add("Below 133 MHz");
        _display_items.add("Between 133 MHz and 300 MHz");
        _display_items.add("Between 300 MHz and 800 MHz");
        _display_items.add("Between 800 MHz and 1 GHz");
        _display_items.add("Above 1 GHz");
        _display_items.add("Not Sure");

        _item_values.add("Below 133 MHz");
        _item_values.add("Between 133 MHz and 300 MHz");
        _item_values.add("Between 300 MHz and 800 MHz");
        _item_values.add("Between 800 MHz and 1 GHz");
        _item_values.add("Above 1 GHz");
        _item_values.add("Not Sure");
        

        setNameList(_display_items);
        setValueList(_item_values);
    }

}