package com.ctb.util.web.filter; 

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * HTTP Filter to add headers specifying not to cache URLs.  Headers include:
 * <ul>
 *   <li>Expires</li>
 *   <li>Cache-Control</li>
 *   <li>Pragma</li>
 * </ul>
 * 
 * @author Giuseppe Gennaro
 */
public class NoCacheFilter implements Filter 
{ 
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException
    {
        // Added the no-cache headers
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        httpResponse.setHeader("Pragma", "no-cache");
        httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        httpResponse.setDateHeader("Expires", -1);

        filterChain.doFilter(request, response);
    }


    public void destroy()
    {
        
    }

    public void init(FilterConfig filterConfig) throws ServletException
    {
    }
} 
