/*
 * NonTabeCTBTestVOComaparator.java
 *
 * Created on November 15, 2002, 4:35 PM
 */
package com.ctb.lexington.util;

import java.util.Comparator;

import com.ctb.lexington.data.TestVO;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 */
public class NonTabeCTBTestVOComaparator implements Comparator
{
    /**
     * DOCUMENT ME!
     *
     * @param o1 DOCUMENT ME!
     * @param o2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int compare(Object o1, Object o2)
    {
        TestVO tab1 = (TestVO)o1;
        TestVO tab2 = (TestVO)o2;
        String grade1 = tab1.getGrade();
        String grade2 = tab2.getGrade();
        String form1 = tab1.getForm();
        String form2 = tab2.getForm();
         
        if (grade1 != null && grade2 != null) {
	        int gradeResult = grade1.compareTo(grade2);
	
	        if (gradeResult == ProductDetailVOComparator.equal) {
	            if (form1 != null && form2 != null) 
	                return form1.compareTo(form2);
	            else
	                return 1;
	        }
	        else {
	            return gradeResult;
	        }
        } else {
            if (form1 != null && form2 != null) 
        		return form1.compareTo(form2);
        	else 
        		return 1;
        }
    }
}
