package com.ctb.lexington.util;

/**
 *  @author mclemens
 */
import java.util.ArrayList;

public class QuestionTypeOptionList extends com.ctb.lexington.util.OptionList
{
    public static final String OBJECT_LABEL = "questionTypeOptionList";

    public static final String LONG_SALUTATION  = "Select a question type...";
    public static final String SHORT_SALUTATION = "Question type...";

    public static final String SALUTATION = LONG_SALUTATION;

    private ArrayList _display_items  = null;
    private ArrayList _item_values = null;

    public QuestionTypeOptionList()
    {
        _display_items = new ArrayList();
        _item_values = new ArrayList();

        _display_items.add("New Orders/Billing");
        _item_values.add("0 - New Orders/Billing");

        _display_items.add("Adding/Changing Administrators/Coordinators");
        _item_values.add("1 - Adding/Changing Administrators/Coordinators");

        _display_items.add("Viewing/Printing Reports");
        _item_values.add("2 - Viewing/Printing Reports");

        _display_items.add("Creating Test Administration");
        _item_values.add("3 - Creating Test Administration");

        _display_items.add("Adding/Moving Students");
        _item_values.add("4 - Adding/Moving Students");

        _display_items.add("Working with Test Tickets");
        _item_values.add("5 - Working with Test Tickets");

        _display_items.add("Navigating Screens");
        _item_values.add("6 - Navigating Screens");

        _display_items.add("Administering Tests");
        _item_values.add("7 - Administering Tests");

        _display_items.add("Other");
        _item_values.add("8 - Other");

        setNameList(_display_items);
        setValueList(_item_values);
    }
}