package com.ctb.util.web.sanitizer; 

import org.apache.beehive.netui.pageflow.FormData;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

public class SanitizedFormData extends FormData
{ 
     public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        
        ActionErrors errs = new ActionErrors();
        
        try {
            Method [] methods = this.getClass().getMethods();
            
            for(int j=0;j<methods.length;j++) {
                String methodName = methods[j].getName();
                if(methodName.startsWith("get") && methods[j].getReturnType() != null && methods[j].getReturnType().isInstance(new String())) {
                    String formval = (String) methods[j].invoke(this, null);
                    
                    if (formval != null && (formval.matches(".*script.*\\(.*") || formval.matches(".*<.*script.*>.*"))) {
                        throw new RuntimeException("XSS attack detected!");
                    }
                } 
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            errs.add(e.getMessage(), new ActionError("cannot_validate"));
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            errs.add(e.getMessage(), new ActionError("cannot_validate"));
        }
        
        return errs;
    }

} 
