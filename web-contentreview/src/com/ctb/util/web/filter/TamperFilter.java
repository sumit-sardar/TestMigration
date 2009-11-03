package com.ctb.util.web.filter; 

import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * HTTP Filter to prevent submission of dangerous characters on HTTP GETs
 * 
 * @author Nate Cohen
 */
public class TamperFilter implements Filter 
{ 
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException
    {
        // cast to http - this is an http only filter
        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse res = (HttpServletResponse)response;
            
        //if ( req.getMethod().equals("GET") && requestHasInvalidParameters(req) ) {
        if ( requestHasInvalidParameters(req) ) {
            throw new ServletException("Request contained invalid characters.");
        } else {
            // No invalid characters in any parameter.
            // Invoke the next item in the chain, either another filter or the
            // originally requested resource.
            filterChain.doFilter(req, res);
        }
    }
    
    private boolean requestHasInvalidParameters(HttpServletRequest req_)
    {
        boolean hasInvalidParamater = false;
        String[] invalidChars = { "<", ">", "&quot;", "&lt;", "&gt;", 
                                 "&lsquo;", "&rsquo;", "&sbquo;", "&ldquo;", 
                                 "&rdquo;", "&bdquo;", "javascript:", "'", "\"" };
        
        for (Enumeration e = req_.getParameterNames(); e.hasMoreElements(); )
        {
            String paramName = (String)e.nextElement();                  
            String paramValue = "";
            paramValue = req_.getParameter(paramName);
            for( int i=0; i<invalidChars.length; i++ )
            {
                if ( paramValue.indexOf(invalidChars[i]) != -1 )
                {
                    // Invalid character found in this parameter, set to invalid
                    hasInvalidParamater = true;                
                    break;
                }
            }
            if (hasInvalidParamater) break;
        }

        return hasInvalidParamater;
    }


    public void destroy()
    {
        
    }

    public void init(FilterConfig filterConfig) throws ServletException
    {
    }
    
} 
