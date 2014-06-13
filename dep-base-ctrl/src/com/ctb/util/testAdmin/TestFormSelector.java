package com.ctb.util.testAdmin; 

import java.util.Iterator;
import java.util.List;

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
    
    public static String getTestletFormWithLowestCountAndIncrement(List<FormAssignmentCount> forms, List<String> alreadyAssignedForms){
    	Iterator<FormAssignmentCount> itr = forms.iterator();
    	FormAssignmentCount retFrm = null;
    	int count = 0;
    	int value = Integer.MAX_VALUE;
    	while(itr.hasNext()){
    		FormAssignmentCount frm = itr.next();
    		if(!alreadyAssignedForms.contains(frm.getForm())){
    			if(frm.getCount().intValue()<value){
    				retFrm = frm;
    				value = frm.getCount().intValue();
    				count = frm.getCount().intValue();
    			}
    		}
    	}
    	if(retFrm == null) {
			retFrm = forms.get(0);
		}
    	int index = forms.indexOf(retFrm);
    	forms.remove(index);
    	retFrm.setCount(count+1);
    	forms.add(index, retFrm);
    	return retFrm.getForm();
    }

} 
