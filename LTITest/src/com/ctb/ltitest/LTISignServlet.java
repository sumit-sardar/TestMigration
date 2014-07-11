package com.ctb.ltitest;


import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class for Servlet: LTISignServlet
 *
 */
 public class LTISignServlet extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
   static final long serialVersionUID = 1L;
   
   static final String OAUTH_PREFIX = "oauth_";
   static final String OAUTH_SIGNATURE = "oauth_signature";
   
    /* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public LTISignServlet() {
		super();
	}   	
	
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}  	
	
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Map<String,String> oauthMap = new TreeMap<String,String>();
		//read all oauth_ parameters
		Map<java.lang.String,java.lang.String[]> reqParams = (Map<java.lang.String,java.lang.String[]>)request.getParameterMap();
	       for(Map.Entry<String,String[]> param :reqParams.entrySet())
	       {
	    	   String key = param.getKey();
	    	   //ignore if key is null or key == "oauth_signature"
	    	   if(key==null || key.equals(OAUTH_SIGNATURE))  continue;
	    	   if(key.startsWith(OAUTH_PREFIX))
	    	   {
	    		   String[] values = param.getValue();
	    		   if(values == null || values.length<= 0){
	    			   oauthMap.put(key, null);
	    		   }
	    		   else
	    		   {
	    			   String value = values[0];
	    			   oauthMap.put(key, value);
	    		   }
	    	   }
	       }
	       StringBuilder baseString = new StringBuilder("POST&");
	       for(Map.Entry<String,String> oauthParam :oauthMap.entrySet())
	       {
	    	   baseString.append(oauthParam.getKey()+"="+oauthParam.getValue()+"&");
	       }
	       if(oauthMap.size()>0 && baseString.length()>1)
	       {
	    	   String signString = baseString.substring(0,baseString.length()-1);
	    	   try {
				String oauthSignature = HmacSha1Signature.calculateRFC2104HMAC(signString,this.getInitParameter("secret-key"));
				System.out.println("OAuth signature..."+oauthSignature);
				response.getWriter().write(oauthSignature);
			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SignatureException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	       }
	       else
	       {//error
	    	   
	       }
	}   	  	    
}