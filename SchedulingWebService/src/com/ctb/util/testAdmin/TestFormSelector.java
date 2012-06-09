package com.ctb.util.testAdmin; 

import com.ctb.bean.request.testAdmin.FormAssignmentCount;

public class TestFormSelector 
{ 
    static final long serialVersionUID = 1L;
    public static String getFormWithLowestCountAndIncrement(FormAssignmentCount [] forms) {
        int index = 0;
        int value = Integer.MAX_VALUE;
        for(int i=0;i<forms.length;i++) {
            if (forms[i].getCount().intValue() < value) {
                index = i;
                value = forms[i].getCount().intValue();
            }
        }
        forms[index].setCount(new Integer(forms[index].getCount().intValue() + 1));
        return forms[index].getForm();
    }
} 
