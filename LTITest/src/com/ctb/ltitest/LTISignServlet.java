package com.ctb.ltitest;


import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class for Servlet: LTISignServlet
 *
 */
 public class LTISignServlet extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
   static final long serialVersionUID = 1L;
   
   static final String OAUTH_PREFIX = "oauth_";
   static final String OAUTH_SIGNATURE = "oauth_signature";
   private static final String DATASOURCE_NAME = "oasDataSource";
   
  
   
    /* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public LTISignServlet() {
		super();
	}   	
	
	
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}  	
	
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 
		String secretKey = null;
		
		secretKey = this.getSecretKey(request);
		if(secretKey==null || secretKey.isEmpty())
		{
			response.getWriter().write("Secret key not defined for consumer key");
			return;
		}
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
				String oauthSignature = HmacSha1Signature.calculateRFC2104HMAC(signString,secretKey);
				System.out.println("Sign string-->"+signString);
				System.out.println("!!!!!!!!->"+secretKey);
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
	private String getSecretKey(HttpServletRequest request)
	{
		String skey = null;
		String customerID = request.getParameter("oauth_consumer_key");
		if (customerID == null || customerID.isEmpty()) {
			System.out.println("Customer ID cannot blank");
			request.setAttribute("message","Consumer key cannot be blank");	
		}
		InitialContext ctx;
		try {
			ctx = new InitialContext();

			DataSource ds = null;

			ds = (DataSource) ctx.lookup(DATASOURCE_NAME);

			Connection con = ds.getConnection();
			PreparedStatement secretKeyStmt = con
					.prepareStatement("SELECT secret_key FROM ENGRADE_CUSTOMER_KEY WHERE CUSTOMER_ID = ? ");

			// Query for a secret key by the customer id
			secretKeyStmt.setString(1, customerID);
			ResultSet rs = secretKeyStmt.executeQuery();

			boolean exists = rs.next();
			if (exists) {
				skey = rs.getString("secret_key");
			}
			rs.close();
			secretKeyStmt.close();
			con.close();

		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return skey;
	}
}