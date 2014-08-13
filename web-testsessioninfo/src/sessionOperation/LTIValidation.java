package sessionOperation;

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

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import javax.xml.bind.DatatypeConverter;

import com.ctb.bean.testAdmin.Role;
import com.ctb.bean.testAdmin.User;

/**
 * @author d-arockiaraj_Duraira
 *
 */
/**
 * @author d-arockiaraj_Duraira
 * 
 */
public class LTIValidation {

	// private static final String OAUTH_PREFIX = "oauth_";
	private static final String OAUTH_SIGNATURE = "oauth_signature";
	private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
	private static final String DATASOURCE_NAME = "oasDataSource";

	/**
	 * @param customerID -
	 *            customer ID of the user
	 * @return the secret key for the customer ID
	 */
	public String getSecretKey(String customerID) {
		String skey = null;
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
				skey = rs.getString("secret_key");
				if (skey == null || skey.isEmpty()) {
					skey = null;
				} else {
					skey += "&";
				}
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

	/**
	 * @param customerID -
	 *            customer ID of the user
	 * @return the error URL of engrade
	 */
	public String getErrorURL(String customerID) {
		String skey = null;
		InitialContext ctx;
		try {
			ctx = new InitialContext();

			DataSource ds = null;

			ds = (DataSource) ctx.lookup(DATASOURCE_NAME);

			Connection con = ds.getConnection();
			PreparedStatement secretKeyStmt = con
					.prepareStatement("SELECT ERROR_RETURN_URL FROM SSO_CUSTOMER_INFO WHERE CUSTOMER_ID = ? ");

			// Query for a secret key by the customer id
			secretKeyStmt.setString(1, customerID);
			ResultSet rs = secretKeyStmt.executeQuery();

			boolean exists = rs.next();
			if (exists) {
				skey = rs.getString("ERROR_RETURN_URL");
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

	public boolean validateRequest(HttpServletRequest request, String secretKey) {
		boolean result = false;

		Map<String, String> oauthMap = new TreeMap<String, String>();
		// read all oauth_ parameters
		Map<java.lang.String, java.lang.String[]> reqParams = (Map<java.lang.String, java.lang.String[]>) request
				.getParameterMap();
		for (Map.Entry<String, String[]> param : reqParams.entrySet()) {
			String key = param.getKey();
			// ignore if key is null or key == "oauth_signature"
			if (key == null || key.equals(OAUTH_SIGNATURE))
				continue;

			String[] values = param.getValue();
			if (values == null || values.length <= 0) {
				oauthMap.put(key, null);
			} else {
				String value = values[0];
				oauthMap.put(URLEncoder.encode(key), URLEncoder.encode(value)
						.replace("+", "%20"));
			}

		}
		StringBuilder baseString = new StringBuilder();
		baseString
				.append("POST&"
						+ URLEncoder
								.encode("https://oastest.ctb.com/SessionWeb/LTIAuthentication")
						+ "&");
		for (Map.Entry<String, String> oauthParam : oauthMap.entrySet()) {
			baseString.append(URLEncoder.encode(oauthParam.getKey() + "="
					+ oauthParam.getValue() + "&"));
		}
		if (oauthMap.size() > 0 && baseString.length() > 1) {
			String signString = baseString
					.substring(0, baseString.length() - 3);// remove last %26
			try {
				System.out.println("sign String" + signString);
				String oauthSignature = calculateRFC2104HMAC(signString,
						secretKey);
				System.out.println("Calculated OAuth signature..."
						+ oauthSignature);

				String receivedOauthSignature = request
						.getParameter(OAUTH_SIGNATURE);
				System.out.println("Received OAuth signature..."
						+ receivedOauthSignature);
				result = oauthSignature.equals(receivedOauthSignature);
			} catch (InvalidKeyException e) {

				e.printStackTrace();
			} catch (SignatureException e) {

				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {

				e.printStackTrace();
			}
		} else {// error

		}

		return result;
	}

	private String calculateRFC2104HMAC(String data, String secretKey)
			throws SignatureException, NoSuchAlgorithmException,
			InvalidKeyException {
		SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(),
				HMAC_SHA1_ALGORITHM);
		Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
		mac.init(signingKey);
		return toHexString(mac.doFinal(data.getBytes()));
	}

	private String toHexString(byte[] bytes) {

		/*
		 * Formatter formatter = new Formatter();
		 * 
		 * for (byte b : bytes) { formatter.format("%02x", b); }
		 * 
		 * return formatter.toString();
		 */
		return DatatypeConverter.printBase64Binary(bytes);
	}

	// validate customerID(in LTI it referred as consumer)
	public boolean validateCustomer(String customerID) {
		boolean result = false;
		Connection con = null;
		InitialContext ctx;
		try {
			ctx = new InitialContext();

			DataSource ds = null;

			ds = (DataSource) ctx.lookup(DATASOURCE_NAME);

			con = ds.getConnection();
			PreparedStatement customerStmt = con
					.prepareStatement("SELECT CUSTOMER_NAME FROM CUSTOMER WHERE CUSTOMER_ID = ? AND activation_status = 'AC' ");

			// Query for a customer by the customer id
			customerStmt.setString(1, customerID);
			ResultSet rs = customerStmt.executeQuery();

			boolean exists = rs.next();
			rs.close();
			customerStmt.close();
			con.close();
			if (exists) {
				result = true;
			}

		} catch (NamingException e) {

			e.printStackTrace();

		} catch (SQLException e) {

			e.printStackTrace();
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e1) {
					result = false;
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		return result;
	}

	public User validateUser(String customerID, String userID) {
		User ltiUser = null;
		Role ltiRole = null;
		Connection con = null;
		InitialContext ctx;
		try {
			ctx = new InitialContext();

			DataSource ds = null;

			ds = (DataSource) ctx.lookup(DATASOURCE_NAME);

			con = ds.getConnection();
			PreparedStatement userStmt = con
					.prepareStatement("select u.user_id as userid, u.user_name as username, u.password as password, u.activation_status as status"
							+ "  from user_role ur, org_node org ,users u "
							+ " where ur.org_node_id = org.org_node_id and  ur.user_id = u.user_id and  "
							+ "org.customer_id = ? and u.ext_school_id =  ? ");

			// Query for a customer by the customer id
			userStmt.setString(1, customerID);
			userStmt.setString(2, userID);

			ResultSet userRS = userStmt.executeQuery();

			boolean exists = userRS.next();

			if (exists) {
				ltiUser = new User();
				ltiUser.setUserId(userRS.getInt("userid"));
				ltiUser.setUserName(userRS.getString("username"));
				ltiUser.setPassword(userRS.getString("password"));
				ltiUser.setActivationStatus(userRS.getString("status"));
			}
			userRS.close();
			userStmt.close();
			if (exists) {
				PreparedStatement roleStmt = con
						.prepareStatement("SELECT DISTINCT r.role_id as roleId,  INITCAP(r.role_name) as roleName "
								+ "FROM Role r,User_Role ur, Users us "
								+ "WHERE  ur.activation_status = 'AC'  and  r.activation_status = 'AC'  and  r.role_id = ur.role_id  and "
								+ " ur.user_id = us.user_id and us.user_id = ?");

				roleStmt.setInt(1, ltiUser.getUserId());

				ResultSet roleRS = roleStmt.executeQuery();

				exists = roleRS.next();

				if (exists) {
					ltiRole = new Role();
					ltiRole.setRoleId(roleRS.getInt("roleId"));
					ltiRole.setRoleName(roleRS.getString("roleName"));
					ltiUser.setRole(ltiRole);
				}
				roleRS.close();
				roleStmt.close();
			}

			con.close();
		} catch (NamingException e) {

			e.printStackTrace();

		} catch (SQLException e) {

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e1) {
					ltiUser = null;
					e1.printStackTrace();
				}
			}
			e.printStackTrace();
		}
		return ltiUser;
	}

	public void updateUser(Integer userID) {

		try {
			DataSource ds = null;
			InitialContext ctx = new InitialContext();

			ds = (DataSource) ctx.lookup(DATASOURCE_NAME);

			Connection con;

			con = ds.getConnection();

			con.setAutoCommit(false);
			PreparedStatement updStmt = con
					.prepareStatement("UPDATE Users set last_login_date_time = sysdate, reset_password ='F' ,"
							+ "password_expiration_date = to_date('2050/01/01','yyyy/mm/dd')  where user_id = ?");

			updStmt.setInt(1, userID.intValue());
			int rowCount = updStmt.executeUpdate();

			con.commit();
			con.close();

		} catch (SQLException e) {

			e.printStackTrace();
		} catch (NamingException e) {

			e.printStackTrace();
		}

	}
}
