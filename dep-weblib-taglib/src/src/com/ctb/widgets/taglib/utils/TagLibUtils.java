package com.ctb.widgets.taglib.utils; 

import org.apache.beehive.netui.script.el.NetUIReadVariableResolver;
import org.apache.beehive.netui.script.el.util.ParseUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.el.VariableResolver;
import javax.servlet.jsp.tagext.Tag;
import org.apache.struts.action.ActionForm;

public class TagLibUtils 
{ 
    private ActionForm actionForm;
    private Tag tag;
    private PageContext pageContext;
    
    public TagLibUtils(HttpServletRequest request, Tag tag, PageContext pageContext) throws JspException {
        Enumeration enumVar = request.getAttributeNames();
        String attributeName;
        Object object;
    
        while( enumVar.hasMoreElements() ) {
            attributeName = (String) enumVar.nextElement();
            object = request.getAttribute(attributeName);
                
            if( object instanceof ActionForm ) {
                this.actionForm = (ActionForm) object;
                break;
            }
        }

        this.tag = tag;
        this.pageContext = pageContext;
    }
    
    public Object resolveVariable(String variable) {
    	
    	NetUIReadVariableResolver vr = new NetUIReadVariableResolver(this.pageContext.getVariableResolver());
    	
        Object result = null;
        try {
            result = ParseUtils.evaluate(variable, vr);
        } catch(Exception e) {
            System.err.println( e );
            result = variable;
        }
        
        return result;
    }
    

    public String escapeVariable(String variable) {

    	String escaped = variable.replaceAll("\\'", "\\\\'");      
        return escaped;
    }

    public Object performResolveVariable(String variable) {

    	Object result = null;

    	if ( fromActionForm(variable) ) 
        	result = actionFormResolveVariable(variable);    		
    	else
        if ( fromRequest(variable) ) 
        	result = requestResolveVariable(variable);    		
        else
      	   	result = resolveVariable(variable);    
        
        return result;
    }
     
   public Object actionFormResolveVariable(String variable) {
	   if ( isSimpleObject(variable) ) 
		   return actionFormResolveSimple(variable);
	   else 
		   return actionFormResolveComplex(variable);
   }
   
   public Object actionFormResolveSimple(String variable) {
	   
	   String varName;
       if (variable.indexOf("actionForm.") == 0)
    	   varName = variable.substring("actionForm.".length(), variable.length());
       else
       if (variable.indexOf("{actionForm.") == 0)
    	   varName = variable.substring("{actionForm.".length(), variable.length()-1);
       else
    	   varName = variable.substring("${actionForm.".length(), variable.length()-1);
       
       varName = firstLetterUpper(varName);

       	Method [] methods = this.actionForm.getClass().getMethods();
        
        for (int i=0 ; i<methods.length ; i++) {
        	
            String methodName = methods[i].getName();
            if ( methods[i].getParameterTypes().length == 0 && 
            	 methodName.startsWith("get") && 
                 methods[i].getReturnType() != null ) {
            	
            	if (methodName.indexOf(varName) > 0) {
					try {
						Object formval = (Object) methods[i].invoke(this.actionForm, null);
		            	return formval;
					} catch (Exception e) {
						e.printStackTrace();
					}
            	}
            	
            }
        }
        return null;
    }

   public Object actionFormResolveComplex(String variable) {
	   
	   int lastIndex = variable.lastIndexOf(".");	   
	   String objectName = variable.substring("actionForm.".length(), lastIndex);
	   objectName = firstLetterUpper(objectName);
	   
	   Object complexObj = null;	   
       Method [] methods = this.actionForm.getClass().getMethods();        
       for (int i=0 ; i<methods.length ; i++) {        	
            String methodName = methods[i].getName();
            if ( methodName.indexOf(objectName) > 0 ) { 
				try {
					complexObj = (Object) methods[i].invoke(this.actionForm, null);
		           	break;
				} catch (Exception e) {
					e.printStackTrace();
				}
           	}
       }

	   String varName = variable.substring("actionForm.".length() + objectName.length() + 1, variable.length());
	   varName = firstLetterUpper(varName);
	   	   
       	methods = complexObj.getClass().getMethods();        
        for (int i=0 ; i<methods.length ; i++) {
        	
            String methodName = methods[i].getName();
            if ( methods[i].getParameterTypes().length == 0 && 
            	 methodName.startsWith("get") && 
                 methods[i].getReturnType() != null ) {
            	
            	if (methodName.indexOf(varName) > 0) {
					try {
						Object formval = (Object) methods[i].invoke(complexObj, null);
		            	return formval;
					} catch (Exception e) {
						e.printStackTrace();
					}
            	}            	
            }
        }
        return null;
    }
   
   public Object requestResolveVariable(String variable) {

	   String varName;
       if (variable.indexOf("request.") == 0) 
    	   varName = variable.substring("request.".length(), variable.length());
       else 
       if (variable.indexOf("{request.") == 0) 
    	   varName = variable.substring("{request.".length(), variable.length());
       else 
       if (variable.indexOf("${request.") == 0) 
    	   varName = variable.substring("${request.".length(), variable.length());
       else 
       if (variable.indexOf("requestScope.") == 0) 
    	   varName = variable.substring("requestScope.".length(), variable.length());
       else 
       if (variable.indexOf("{requestScope.") == 0) 
    	   varName = variable.substring("{requestScope.".length(), variable.length());
       else 
       if (variable.indexOf("${requestScope.") == 0) 
    	   varName = variable.substring("${requestScope.".length(), variable.length());
       else 
    	   varName = "";
	   
       return pageContext.getRequest().getAttribute( varName );
   }

   private boolean fromActionForm(String src) {
	   if ((src.indexOf("actionForm.") == 0) || (src.indexOf("{actionForm.") == 0) || (src.indexOf("${actionForm.") == 0))
		   return true;
	   else
		   return false;
   }

   private boolean fromRequest(String src) {
	   if ((src.indexOf("request.") == 0) || (src.indexOf("{request.") == 0) || (src.indexOf("${request.") == 0) ||
		   (src.indexOf("requestScope.") == 0) || (src.indexOf("{requestScope.") == 0) || (src.indexOf("${requestScope.") == 0))
		   return true;
	   else
		   return false;
   }
   
   private boolean isSimpleObject(String src) {
		int firstIndex = src.indexOf(".");
		int lastIndex = src.lastIndexOf(".");
		return (firstIndex == lastIndex);
   }

   private String firstLetterUpper(String src) {
	   String firstLetter = src.substring(0, 1).toUpperCase();
	   String theRest = src.substring(1, src.length());
	   return (firstLetter + theRest);
   }
   
} 
