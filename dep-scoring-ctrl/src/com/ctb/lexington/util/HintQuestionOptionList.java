package com.ctb.lexington.util;

/**
 *  @author mclemens
 */
import java.util.ArrayList;

public class HintQuestionOptionList extends com.ctb.lexington.util.OptionList
{
    public static final String OBJECT_LABEL = "hintQuestionOptionList";

    public static final String LONG_SALUTATION = "Select a hint question...";

    public static final String SHORT_SALUTATION = "Hint question...";

    public static final String SALUTATION = LONG_SALUTATION;

    private ArrayList _display_items  = null;
    private ArrayList _item_values = null;

    public HintQuestionOptionList()
    {
        _display_items = new ArrayList();

        _item_values = new ArrayList();

        _display_items.add("The name of your favorite pet");
        _item_values.add("1000");

        _display_items.add("Your mother's maiden name");
        _item_values.add("1001");

        _display_items.add("The name of your childhood best friend");
        _item_values.add("1002");
        
        _display_items.add("Your birthplace");
        _item_values.add("1003");
        
        _display_items.add("Favorite teacher's name");
        _item_values.add("1004");

        setNameList(_display_items);
        setValueList(_item_values);
    }

    /**
     *  @return The name displayed in the drop-down list for the given value.  If a&nbsp;
     *          match is not made, then the method returns <CODE>null</CODE>.
     */
    public String getHitQuestion(String value_)
    {
        int name_index = _item_values.indexOf(value_);

        if(name_index > 0)
        {
            return (String)_display_items.get(name_index);
        }
        else
        {
            return null;
        }
    }

    /**
     *  @return An option list of genders.
     */
    public ArrayList getUnselectedHintQuestions()
    {
        return getNameValueList(_item_values, _display_items);
    }

    /**
     *  @param salutation_ The first item to be listed in the combo box.
     *  @return An option list of genders with the salutation in the first position.
     */
    public ArrayList getUnselectedHintQuestionsWithSalutation(String salutation_)
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
     *  @param name_ The display item to be selected in the drop down box.&nbsp;
     *               If the item does not exist in the list of display items, then&nbsp;
     *               nothing will be pre-selected.
     *  @return  An option list with the item selected corresponding to the value passed in.
     */
    public ArrayList getSelectedHintQuestionsByValue(String value_)
    {
        return getSelectedNameValueListByValue(_item_values, _display_items, value_);
    }
}