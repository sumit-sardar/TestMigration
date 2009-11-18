package com.ctb.util.web.sanitizer; 

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;

import org.apache.beehive.netui.pageflow.FormData;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

public class SanitizedFormData extends FormData implements java.io.Serializable
{ 
	private static final long serialVersionUID = 1L;

	public SanitizedFormData() {
		super();
	}
	
     public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        
        ActionErrors errs = new ActionErrors();
      
        try {
            Method [] methods = this.getClass().getMethods();
            
            for(int j=0;j<methods.length;j++) {
            	
                String methodName = methods[j].getName();
                
                
                if(methods[j].getParameterTypes().length == 0 && 
                   methodName.startsWith("get") && 
                   methods[j].getReturnType() != null && 
                   methods[j].getReturnType().isInstance(new String())) {
                
                	//System.out.println("*****  " + this.getClass().getName() + " :  " + methods[j].getName());
                	
                	String formval = (String) methods[j].invoke(this, null);
                	
                    if (formval != null && (
                    	formval.indexOf("demo.testfire.net") >= 0 || 
                    	formval.indexOf("WF_XSRF.html") >= 0 || 
                        formval.indexOf("%27+%2B+%27") >= 0 || 
                        formval.indexOf("'+'") >= 0 ||
                        formval.indexOf("'%2B'") >= 0 ||
                        formval.indexOf("%27%2B%27") >= 0 ||
                        formval.indexOf("%27+%27") >= 0 ||
                        formval.indexOf("+and+") >= 0 || 
                        formval.indexOf("%2Band%2B") >= 0 || 
                        formval.indexOf("+%7C%7C+") >= 0 || 
                        formval.indexOf("%2B%7C%7C%2B") >= 0 || 
                        formval.indexOf("%2B||%2B") >= 0 || 
                        formval.indexOf("+||+") >= 0 ||
                        formval.indexOf("javascript:") >= 0 ||
                        formval.indexOf("javascript%3A") >= 0 ||
                        formval.matches(".*script.*\\(.*") || 
                        formval.matches(".*<.*script.*>.*"))) {
                        throw new RuntimeException("XSS or SQL Injection attack detected!");
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
