package com.ctb.forms; 

import org.apache.beehive.netui.pageflow.FormData;
import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;

public class FormFieldValidator 
{ 
    public static boolean validateFilterForm(FormData form, HttpServletRequest request) 
    {
        boolean valid = true;
        try {
            String[] invalidChars = { "!", "@", "#", "$", "%", "^" };            
            Method [] methods = form.getClass().getMethods();
            Object filterObject = null;
            Method [] filterMethods = null;
            for(int j=0;j<methods.length;j++) {
                String methodName = methods[j].getName();
                String returnType = methods[j].getReturnType().getName();
                if(methodName.startsWith("get") && returnType.indexOf("Filter") >= 0) {
                    filterObject = methods[j].invoke(form, null);
                    filterMethods = filterObject.getClass().getMethods();
                }
            }
            if(filterObject != null && filterMethods != null) {
                for(int j=0;j<filterMethods.length;j++) {
                    String methodName = filterMethods[j].getName();
                    String returnType = "";
                    if (filterMethods[j].getReturnType().isInstance(new String()))
                        returnType = filterMethods[j].getReturnType().getName();
                    
                    if(methodName.startsWith("get") && returnType.indexOf("String") >= 0) {
                        String formval = (String) filterMethods[j].invoke(filterObject, null);
                        for( int i=0; i<invalidChars.length; i++ ) {
                            if ( formval.indexOf(invalidChars[i]) != -1 ) {
                              request.setAttribute("formIsClean",new Boolean(false));
                              valid = false;
                            }
                        }  
                    } 
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            valid = false;
        }
        return valid;
    }
} 
