package com.ctb.action;

import com.ctb.util.DBAccess;
import com.ctb.util.EmailSender;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
//import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class CustomerNotification
{
	long availableCount;
	long reservedCount;
	long consumedCount;
	long temporaryCount;
	long totalCount;
	long usedCount;
	long percentageThreshold = 20L;
	long percentageRemainThreshold = 80L;
	int customerID;
	int productID;
	int orgNodeID;
	String productName;
	String productGroupName;
	String customerName;
	String contactName;
	String storedSenderEmailId = "iknow_account_management@ctb.com";

	String env = "";

	Properties properties = null;

	private static DBAccess dbConnection = null;

	public static void main(String[] args)
	{
		//System.out.println("Inside Main");
		CustomerNotification cn = new CustomerNotification();
		cn.notifyCustomer(args);

		LicenseExpiryNotification lenm = new LicenseExpiryNotification();
		lenm.notifyCustomerMonthly(args);
		lenm.notifyCustomerQuarterly(args);
		lenm.notifyCustomerOnLicenseExpiry(args);
	}

	private void notifyCustomer(String[] args)
	{
		try
		{
			//System.out.println("Inside Try");
			getCommandLine(args);

			dbConnection = DBAccess.createConnection(this.env);

			this.properties = loadProperties(this.env);

			getPercentageThreshold();

			DBAccess dbAccess = new DBAccess();

			ResultSet rs = dbAccess.executeQuery(getLicenseQuantity());
			//System.out.println("Before While");
			while (rs.next())
			{
				this.availableCount = rs.getInt("AVAILABLE");
				this.reservedCount = rs.getInt("RESERVED");
				this.consumedCount = rs.getInt("CONSUMED");
				this.temporaryCount = rs.getInt("TEMPORARY_LICENSE");
				String thresholdStr = rs.getString("THRESHOLD");
				try	{
					this.percentageThreshold = new Long(thresholdStr).longValue();
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
				this.percentageRemainThreshold = (100L - this.percentageThreshold);

				if (isThreshold())
				{
					boolean isLasLink = false;
					String customerEmailID = rs.getString("CONTACT_EMAIL");
					String CTBEmailID = rs.getString("CTB_CONTACT_EMAIL");
					this.customerID = rs.getInt("CUSTOMER_ID");
					this.productID = rs.getInt("PRODUCT_ID");
					this.customerName = rs.getString("CUSTOMER_NAME");
					this.contactName = rs.getString("CONTACT_NAME");

					this.productName = rs.getString("PRODUCT_NAME");
					this.productGroupName = rs.getString("PRODUCT_GROUP_NAME");

					String s = "Framework";
					this.productName = replaceString(this.productName, s);
					this.productGroupName = replaceString(this.productGroupName, s);

					s = "framework";
					this.productName = replaceString(this.productName, s);
					this.productGroupName = replaceString(this.productGroupName, s);

					if(this.productID == 7000 || this.productID == 7001 || this.productID == 7002 || this.productID == 7003 || this.productID == 7500 || this.productID == 7501 || this.productID == 7502)
						isLasLink = true;
					String senderEmailId = getPropertiyValue("email.sender");
					if ((senderEmailId == null) || (senderEmailId.length() <= 0)) {
						senderEmailId = this.storedSenderEmailId;
					}
					/*
						SELECT *
			  			  FROM CUSTOMER_CONFIGURATION
			 			 WHERE CUSTOMER_ID = " +  this.customerID + "
			   			   AND CUSTOMER_CONFIGURATION_NAME =  'License_Yearly_Expiry'
			   			   AND EXISTS
			   				(SELECT 1
			       			   FROM CUSTOMER_CONFIGURATION
			      			  WHERE CUSTOMER_ID = " +  this.customerID + "
			        			AND CUSTOMER_CONFIGURATION_NAME = 'Allow_Subscription');
					*/
					String query ="SELECT * FROM CUSTOMER_CONFIGURATION WHERE CUSTOMER_ID = " +  this.customerID + " AND CUSTOMER_CONFIGURATION_NAME =  'License_Yearly_Expiry' AND EXISTS (SELECT 1 FROM CUSTOMER_CONFIGURATION WHERE CUSTOMER_ID = " +  this.customerID + " AND CUSTOMER_CONFIGURATION_NAME = 'Allow_Subscription')";
					//System.out.println("Query 2 is :: " + query);
					ResultSet rs1 = dbAccess.executeQuery(query);
					if(rs1.next() && isLasLink) {
						try {
							this.orgNodeID = rs.getInt("ORG_NODE_ID");
							//System.out.println("Customer ID is :: " + this.customerID +" :: Org Node ID " + this.orgNodeID + " :: Available License Count :: " + this.availableCount);
							String userEmailSubject = "LAS Links Online - License consumption status alert";
							/*
                               SELECT USR.FIRST_NAME,
                                      USR.MIDDLE_NAME,
                                      USR.LAST_NAME,
                                      USR.EMAIL,
                                      ONC.CATEGORY_LEVEL
                                 FROM ORG_NODE          ONO,
                                      USER_ROLE         USRRL,
                                      USERS             USR,
                                      ROLE              RL,
                                      ORG_NODE_CATEGORY ONC
                                WHERE USRRL.ORG_NODE_ID = THIS.ORGNODEID
                                  AND USRRL.USER_ID = USR.USER_ID
                                  AND ONO.ACTIVATION_STATUS = 'AC'
                                  AND USR.ACTIVATION_STATUS = 'AC'
                                  AND RL.ROLE_NAME IN('ADMINISTRATOR', 'COORDINATOR', 'ADMINISTRATIVE COORDINATOR')
                                  AND RL.ROLE_ID = USRRL.ROLE_ID
                                  AND USRRL.ORG_NODE_ID = ONO.ORG_NODE_ID
                                  AND ONO.ORG_NODE_CATEGORY_ID = ONC.ORG_NODE_CATEGORY_ID
							*/
							query = "SELECT USR.FIRST_NAME, USR.MIDDLE_NAME, USR.LAST_NAME, USR.EMAIL, ONC.CATEGORY_LEVEL FROM ORG_NODE ONO, USER_ROLE USRRL, USERS USR, ROLE RL, ORG_NODE_CATEGORY ONC WHERE USRRL.ORG_NODE_ID = "+ this.orgNodeID +" AND USRRL.USER_ID = USR.USER_ID AND ONO.ACTIVATION_STATUS = 'AC' AND USR.ACTIVATION_STATUS = 'AC' AND RL.ROLE_NAME IN ('ADMINISTRATOR', 'COORDINATOR','ADMINISTRATIVE COORDINATOR') AND RL.ROLE_ID = USRRL.ROLE_ID AND USRRL.ORG_NODE_ID = ONO.ORG_NODE_ID AND ONO.ORG_NODE_CATEGORY_ID = ONC.ORG_NODE_CATEGORY_ID";
							//System.out.println("Query 3 is :: " + query);
							ResultSet rs2 = dbAccess.executeQuery(query);

							while(rs2.next()) {
								if(rs2.getString("email") != null && rs2.getString("email").length() > 0) {
									String userEmailId = rs2.getString("email");
									this.contactName = "";
									if(rs2.getString("first_name") != null && rs2.getString("first_name").length() > 0) {
										this.contactName += rs2.getString("first_name") + " ";
									}
									if(rs2.getString("middle_name") != null && rs2.getString("middle_name").length() > 0) {
										this.contactName += rs2.getString("middle_name") + " ";
									}
									if(rs2.getString("last_name") != null && rs2.getString("last_name").length() > 0) {
										this.contactName += rs2.getString("last_name");
									}
									if(rs2.getInt("CATEGORY_LEVEL") > 0) {
										int categoryLevel = rs2.getInt("CATEGORY_LEVEL");
										if(categoryLevel == 1) {
											//this.availableCount = rs2.getInt("available");
											String userEmailBody = getTopNodeUserEmailBodyForLASLinkThresholdEmail();
											//System.out.println("Top Node UserEmailBody for Threshold Notification :: \n" + userEmailSubject + "\n" + userEmailBody);
											EmailSender.sendMail("", senderEmailId, userEmailId, "", "", userEmailSubject, userEmailBody, null, isLasLink);
											if ((CTBEmailID != null) && (CTBEmailID.length() > 0)) {
												String internalEmailSubject = "LAS Links Online customer license status alert";
												String internalEmailBody = getInternalEmailBodyForLASLinkThresholdEmail();
												//System.out.println("Customer Service EmailBody for Threshold Notification :: \n" + internalEmailSubject + "\n" + internalEmailBody);
												EmailSender.sendMail("", senderEmailId, CTBEmailID, "", "", internalEmailSubject, internalEmailBody, null, isLasLink);
											}
										}
										else if(categoryLevel > 1) {
											//this.availableCount = rs2.getInt("available");
											String userEmailBody = getLowerNodeUserEmailBodyForLASLinkThresholdEmail();
											//System.out.println("Lower Node UserEmailBody for Threshold Notification :: \n" + userEmailSubject + "\n" + userEmailBody);
											EmailSender.sendMail("", senderEmailId, userEmailId, "", "", userEmailSubject, userEmailBody, null, isLasLink);
										}
									}
								}
							}
							rs2.close();
							if ((customerEmailID != null) && (customerEmailID.length() > 0)) {
								/*
                                 UPDATE CUSTOMER_ORGNODE_LICENSE COL
                                    SET COL.EMAIL_NOTIFY_FLAG = 'F'
                                  WHERE COL.CUSTOMER_ID = " + this.customerID + "
                                    AND COL.PRODUCT_ID = " + this.productID + "
                                    AND COL.ORG_NODE_ID =  " + this.orgNodeID + "
								*/
								String updateQuery = "UPDATE CUSTOMER_ORGNODE_LICENSE COL SET COL.EMAIL_NOTIFY_FLAG = 'F' WHERE COL.CUSTOMER_ID = " + this.customerID + " AND COL.PRODUCT_ID = " + this.productID + " AND COL.ORG_NODE_ID = " + this.orgNodeID;
								dbAccess.updateData(updateQuery);
							}
						}catch(Exception ex){
							ex.printStackTrace();
						}
					}
					else {
						try {
							String customerEmailBody = getCustomerEmailBody();
							String internalEmailBody = getInternalEmailBody();
							String customerEmailSubject;
							String internalEmailSubject; 
							if(isLasLink)
								customerEmailSubject = "LAS Links - License consumption status alert";
							else
								customerEmailSubject = "License consumption status alert";
							internalEmailSubject = this.productGroupName + " customer license status alert";

							if ((customerEmailID != null) && (customerEmailID.length() > 0)) {
								EmailSender.sendMail("", senderEmailId, customerEmailID, "", "", customerEmailSubject, customerEmailBody, null, isLasLink);
							}
							if ((CTBEmailID != null) && (CTBEmailID.length() > 0)) {
								EmailSender.sendMail("", senderEmailId, CTBEmailID, "", "", internalEmailSubject, internalEmailBody, null, isLasLink);
							}
						}
						catch (Exception ex) {
							ex.printStackTrace();
						}
						if ((customerEmailID != null) && (customerEmailID.length() > 0)) {
							this.orgNodeID = rs.getInt("ORG_NODE_ID");
							dbAccess.updateData(updateEmailNotificationFlag());
						}
					}
				}
			}
		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private String replaceString(String originalString, String replaceString)
	{
		if (originalString.indexOf(replaceString) != -1) {
			int i = originalString.indexOf(replaceString);
			String before = originalString.substring(0, i);
			String after = originalString.substring(i + replaceString.length());
			originalString = (before + after).trim();
		}
		return originalString;
	}

	private void getCommandLine(String[] args)
	{
		if ((args.length < 1) || (args[0].indexOf('=') >= 0)) {
			System.out.println("Cannot parse command line. No command specified.");
		}
		else
			this.env = args[0].toLowerCase();
	}

	private boolean isThreshold()
	{
		this.totalCount = (this.availableCount + this.reservedCount + this.consumedCount);

		this.usedCount = (this.reservedCount + this.consumedCount);
		if (this.temporaryCount > 0L) {
			long percentageLicense = this.availableCount * 100L / this.temporaryCount;

			return percentageLicense < this.percentageThreshold;
		}

		return false;
	}

	private String getLicenseQuantity()
	{
		String sql = "select cust.customer_id, cust.customer_name, cust.contact_name, cust.contact_email, cust.ctb_contact_email, cc.default_value threshold, cpl.product_id, CPL.ORG_NODE_ID, cpl.available, cpl.reserved, cpl.consumed, cpl.license_after_last_purchase temporary_license,  parentprod.product_description product_group_name, prod.product_name from customer cust, customer_configuration cc, customer_orgnode_license cpl, product prod, product parentprod where cust.customer_id = cpl.customer_id and cpl.product_id = prod.product_id and cpl.email_notify_flag = 'T' and prod.product_type <> 'CF' and prod.parent_product_id = parentprod.product_id and cust.customer_id = cc.customer_id and cc.customer_configuration_name = 'License_Email_Notification' union  select cust.customer_id, cust.customer_name, cust.contact_name, cust.contact_email, cust.ctb_contact_email, cc.default_value threshold, cpl.product_id, CPL.ORG_NODE_ID, cpl.available, cpl.reserved, cpl.consumed, cpl.license_after_last_purchase temporary_license,  prod.product_description product_group_name, prod.product_description product_name from customer cust, customer_configuration cc, customer_orgnode_license cpl, product prod where cust.customer_id = cpl.customer_id and cpl.product_id = prod.product_id and cpl.email_notify_flag = 'T' and prod.product_type = 'CF' and cust.customer_id = cc.customer_id and cc.customer_configuration_name = 'License_Email_Notification'";

		return sql;
	}

	private String updateEmailNotificationFlag()
	{
		String sql = "update customer_orgnode_license cpl set cpl.email_notify_flag = 'F' where cpl.customer_id = " + this.customerID + " and cpl.product_id = " + this.productID + " and cpl.org_node_id = " + this.orgNodeID;

		return sql;
	}

	private static Properties loadProperties(String env)
	{
		InputStream in = null;
		Properties prop = new Properties();
		try {
			in = new FileInputStream(env);
			prop.load(in);
			return prop;
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void getPercentageThreshold()
	{
		String percentageTh = getPropertiyValue("percentage.threshold");
		if (percentageTh != "")
			try {
				this.percentageThreshold = new Long(percentageTh).intValue();
			}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private String getPropertiyValue(String name)
	{
		String value = this.properties.getProperty(name);
		if ((value == null) || (value.length() == 0)) {
			return "";
		}
		return value;
	}

	private String getInternalEmailBody()
	{
		String msg = "";
		try {
			msg = "Dear Customer Service:\n\n" + 
			this.customerName + " has used or scheduled more than " + this.percentageRemainThreshold + "% of their available license quantity after their last purchase for " + this.productName + " as follows:" + 
			"\n\n\tNumber used/allocated: " + this.usedCount +
			"\n\n\tRemaining licenses: " + this.availableCount + 
			"\n\nYou may be hearing from this customer in the near future to purchase additional licenses.";
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}

	private String getCustomerEmailBody()
	{
		String msg = "";
		try {
			msg = "Dear " + this.contactName + ":\n\n" + this.customerName + " has only " + this.availableCount + " available licenses remaining for " + this.productName + "." + " You may wish to consider calling Customer Support to" + " arrange for additional licenses in advance of actual testing dates, to ensure test" + " licenses will be available when needed.\n\n" + "Thank you for purchasing " + this.productGroupName + ". We appreciate your business and " + "are available to serve you M-F, 9:00 a.m. to 5:00 p.m. in your timezone. " + "To contact us call 800-538-9547, and press Option 1.\n\n" + "Your CTB/McGraw-Hill Companies Customer Support team.";
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return msg;
	}

	private String getInternalEmailBodyForLASLinkThresholdEmail()
	{
		String msg = "";
		try {
			msg = "Dear Customer Service:\n\n" + 
			this.customerName + " has used or scheduled more than " + this.percentageRemainThreshold + "% of their available license quantity after their last purchase for " + this.productName + " as follows:" + 
			"\n\n\tRemaining licenses: " + this.availableCount + 
			"\n\nPlease contact this customer to arrange for an additional purchase.";
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}

	private String getTopNodeUserEmailBodyForLASLinkThresholdEmail() {
		String msg = "";
		try {
			msg = "Dear " + this.contactName + ":\n\n" + this.customerName + " has fewer than " + this.percentageRemainThreshold + "% available licenses remaining for " + this.productName + "." + 
			" Please call Customer Support to arrange for additional licenses in advance of actual testing dates to ensure test licenses will be available when needed." + 
			"\n\nWe appreciate your business and value you as a LAS links Online customer. We are available to serve you M-F, 9:00 a.m. to 5:00 p.m. in your timezone." + 
			"\n\nTo contact us call 800-538-9547, and press Option 1." + 
			"\n\nYour CTB/McGraw-Hill Companies Customer Support team.";
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}

	private String getLowerNodeUserEmailBodyForLASLinkThresholdEmail(){
		String msg = "";
		try {
			msg = "Dear " + this.contactName + ":\n\n" + this.customerName + " has fewer than " + this.percentageRemainThreshold + "% available licenses remaining for " + this.productName + "." + 
			" Please call your LAS Links Online administrator to arrange for additional licenses in advance of actual testing dates to ensure test licenses will be available when needed." + 
			"\n\nWe appreciate your business and value you as a LAS links Online customer. We are available to serve you M-F, 9:00 a.m. to 5:00 p.m. in your timezone." + 
			"\n\nTo contact us call 800-538-9547, and press Option 1." + 
			"\n\nYour CTB/McGraw-Hill Companies Customer Support team.";
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}
}