package com.ctb.ltitest;


import java.io.IOException;
import java.net.URLEncoder;
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
	    	  // if(key.startsWith(OAUTH_PREFIX))
	    	   {
	    		   String[] values = param.getValue();
	    		   if(values == null || values.length<= 0){
	    			   //oauthMap.put(key, null);
	    			   continue;
	    		   }
	    		   else
	    		   {
	    			   String value = values[0];
	    			   oauthMap.put(URLEncoder.encode(key), URLEncoder.encode(value).replace("+","%20"));
	    		   }
	    	   }
	       }
	       StringBuilder baseString = new StringBuilder("POST&"+URLEncoder.encode("https://oastest.ctb.com/SessionWeb/LTIAuthentication")+"&");
	       //StringBuilder baseString = new StringBuilder("");
	       for(Map.Entry<String,String> oauthParam :oauthMap.entrySet())
	       {
	    	   baseString.append(URLEncoder.encode(oauthParam.getKey()+"="+oauthParam.getValue()+"&"));
	       }
	       if(oauthMap.size()>0 && baseString.length()>1)
	       {
	    	   String signString = baseString.substring(0,baseString.length()-3);//to remove the last %26
	    	   try {
				//String oauthSignature = HmacSha1Signature.calculateRFC2104HMAC(signString,secretKey);
	    		   System.out.println("Actual Sign string-->"+signString);
	    		//signString = "POST&https%3A%2F%2Foastest.ctb.com%2FSessionWeb%2FLTIAuthentication&context_id%3D5000007027887%26context_label%3D7th%2520Grade%2520Math%2520%2528demo%2529%26context_title%3D7th%2520Grade%2520Math%2520%2528demo%2529%26custom_appsesid%3D9000000000000163683cFvidhyn8fKT4rfrn6O71Lu1D3K5Ug1104ea7b1b7d758%26custom_districtid%3D10000000005946101%26custom_schoolid%3D10000000005946103%26lis_person_contact_email_primary%3Ddemo%2540engrade.com%26lis_person_name_full%3DLASLinks%2520Teacher%25201%26lti_message_type%3Dbasic-lti-launch-request%26lti_version%3DLTI-1p0%26oauth_callback%3Dabout%253Ablank%26oauth_consumer_key%3D14719%26oauth_nonce%3D0e6fedcc31d5f6e6a04f24d22171bdbb%26oauth_signature_method%3DHMAC-SHA1%26oauth_timestamp%3D1405453970%26oauth_version%3D1.0%26resource_link_id%3D5000007027887%26roles%3DInstructor%26user_id%3D10000000005946109";
	    		 // signString = "POST&https%3A%2F%2Foastest.ctb.com%2FSessionWeb%2FLTIAuthentication&context_id%3D5000007027887%26context_label%3D7th%2520Grade%2520Math%2520%2528demo%2529%26context_title%3D7th%2520Grade%2520Math%2520%2528demo%2529%26custom_appsesid%3D9000000000000163683cFvidhyn8fKT4rfrn6O71Lu1D3K5Ug1104ea7b1b7d758%26custom_districtid%3D10000000005946101%26custom_schoolid%3D10000000005946103%26lis_person_contact_email_primary%3Ddemo%2540engrade.com%26lis_person_name_full%3DLASLinks%2520Teacher%25201%26lti_message_type%3Dbasic-lti-launch-request%26lti_version%3DLTI-1p0%26oauth_callback%3Dabout%253Ablank%26oauth_consumer_key%3D14719%26oauth_nonce%3D88fd64654ad9a44a32e2d86be97b9e78%26oauth_signature_method%3DHMAC-SHA1%26oauth_timestamp%3D1405458214%26oauth_version%3D1.0%26resource_link_id%3D5000007027887%26roles%3DInstructor%26user_id%3D10000000005946109";
	    		//secretKey = "oasctb123&";
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
					.prepareStatement("SELECT secret_key FROM SSO_CUSTOMER_INFO WHERE CUSTOMER_ID = ? ");

			// Query for a secret key by the customer id
			secretKeyStmt.setString(1, customerID);
			ResultSet rs = secretKeyStmt.executeQuery();

			boolean exists = rs.next();
			if (exists) {
				skey = rs.getString("secret_key")+"&";
			}
			rs.close();
			secretKeyStmt.close();
			con.close();

		} catch (NamingException e) {
			
			e.printStackTrace();

		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		return skey;
	}
}