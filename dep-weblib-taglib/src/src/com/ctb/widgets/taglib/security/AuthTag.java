package com.ctb.widgets.taglib.security; 

import com.ctb.widgets.taglib.utils.TagLibUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import org.apache.struts.action.ActionForm;

public class AuthTag extends javax.servlet.jsp.tagext.TagSupport
{ 
    // Member variables --------------------------------------------------------


    // Tag attributes ----------------------------------------------------------
    private String roles; 
    
    // Tag attribute setters ---------------------------------------------------
    public void setRoles( String roles ) {
        this.roles = roles;
    }
    
    
    // Tag life cycle ----------------------------------------------------------
    public int doStartTag() {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        int results = this.SKIP_BODY;
    
        ArrayList roleNames = parseRoles(roles);
        for( Iterator i = roleNames.iterator(); i.hasNext();  ) {
            if( request.isUserInRole( (String) i.next() ) ) {
                results = this.EVAL_BODY_INCLUDE;
                break;
            }
        }
        
        return results;
    }


    // Methods -----------------------------------------------------------------
    private ArrayList parseRoles( String rolesList ) {
        ArrayList roleNames = new ArrayList();
        
        if( roles != null && roles.length() > 0 ) {
            StringTokenizer st = new StringTokenizer(roles, ",");
            String roleName;
            while( st.hasMoreTokens() ) {
                roleName = (String) st.nextToken();
                roleNames.add( roleName.trim() );
            }
        }
        
        return roleNames;
    }


} 
