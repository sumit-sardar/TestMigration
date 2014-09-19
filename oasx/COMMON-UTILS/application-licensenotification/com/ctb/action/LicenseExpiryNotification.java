package com.ctb.action;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.util.Properties;
//import java.util.HashSet;
//import java.util.Iterator;
//import java.util.Set;

import com.ctb.util.DBAccess;
import com.ctb.util.EmailSender;

public class LicenseExpiryNotification {

	long availableCount;
	long reservedCount;
	long consumedCount;
	long temporaryCount;
	long totalCount;
	long usedCount;	
	int customerID;
	int productID;
	String productName;
	String productGroupName;
	String customerName;
	String contactName;
	String licensePeriodEnd;
	int licenseAfterLastPurchase;
	int actualAvailableCount;
	String orderNumber;
	String purchaseOrder;

	String storedSenderEmailId = "iknow_account_management@ctb.com";
	String env = "";
	private static DBAccess dbConnection = null;
	Properties properties = null;

	public static void main(String[] args) {
		//System.out.println("Inside main");
		LicenseExpiryNotification lenm = new LicenseExpiryNotification();
		lenm.notifyCustomerMonthly(args);
		lenm.notifyCustomerQuarterly(args);
		lenm.notifyCustomerOnLicenseExpiry(args);
	}

	/**
	 * @param args
	 */
	public void notifyCustomerMonthly(String[] args) {
		try {
			//System.out.println("Inside notifyCustomerMonthly()");
			getCommandLine(args);
			dbConnection = DBAccess.createConnection(this.env);
			this.properties = loadProperties(this.env);
			String senderEmailId = getPropertyValue("email.sender");
			if ((senderEmailId == null) || (senderEmailId.length() <= 0)) {
				senderEmailId = this.storedSenderEmailId;
			}
			DBAccess dbAccess = new DBAccess();
			/*
                      SELECT CUST.CUSTOMER_ID,
                             CUST.CUSTOMER_NAME,
                             CUST.CONTACT_NAME,
                             CUST.CONTACT_EMAIL,
                             CUST.CTB_CONTACT_EMAIL,
                             PROD.PRODUCT_ID,
                             PROD.PRODUCT_NAME,
                             TO_CHAR(CPL.LICENSE_PERIOD_END, 'dd/mon/yyyy') AS LICENSE_PERIOD_END,
                             CPL.ORDER_INDEX,
                             CPL.PO_TEXT,
                             SUM(OOL.AVAILABLE) AS TOTAL_AVAILABLE
                        FROM CUSTOMER                 CUST,
                             CUSTOMER_PRODUCT_LICENSE CPL,
                             CUSTOMER_CONFIGURATION   CUCON,
                             PRODUCT                  PROD,
                             ORGNODE_ORDER_LICENSE    OOL
                       WHERE CUST.CUSTOMER_ID = CPL.CUSTOMER_ID
                         AND CUCON.CUSTOMER_ID = CUST.CUSTOMER_ID
                         AND CPL.PRODUCT_ID = PROD.PRODUCT_ID
                         AND CUCON.CUSTOMER_CONFIGURATION_NAME = 'License_Yearly_Expiry'
                         AND OOL.ORDER_INDEX = CPL.ORDER_INDEX
                         AND TRUNC(SYSDATE + 30) = TRUNC(CPL.LICENSE_PERIOD_END)
                         AND EXISTS (SELECT 1
                              FROM CUSTOMER_CONFIGURATION CC
                              WHERE CC.CUSTOMER_ID = CUCON.CUSTOMER_ID
                              AND CUSTOMER_CONFIGURATION_NAME = 'Allow_Subscription')
                       GROUP BY CUST.CUSTOMER_ID,
                                CUST.CUSTOMER_NAME,
                                CUST.CONTACT_NAME,
                                CUST.CONTACT_EMAIL,
                                PROD.PRODUCT_NAME,
                                CUST.CTB_CONTACT_EMAIL,
                                CPL.LICENSE_PERIOD_END,
                                CPL.PO_TEXT,
                                CPL.ORDER_INDEX
                      HAVING SUM(OOL.AVAILABLE) > 0
			*/
			String query = "select cust.customer_id, cust.customer_name, cust.contact_name, cust.contact_email, cust.ctb_contact_email, PROD.PRODUCT_ID, PROD.PRODUCT_NAME, to_char(cpl.license_period_end, 'dd/mon/yyyy') as license_period_end, cpl.order_index, CPL.PO_TEXT, SUM(OOL.AVAILABLE) AS TOTAL_AVAILABLE from customer cust, customer_product_license cpl, customer_configuration cucon, PRODUCT PROD, ORGNODE_ORDER_LICENSE OOL where cust.customer_id = cpl.customer_id and cucon.customer_id = cust.customer_id AND CPL.PRODUCT_ID = PROD.PRODUCT_ID and cucon.customer_configuration_name = 'License_Yearly_Expiry' AND OOL.ORDER_INDEX = CPL.ORDER_INDEX and trunc(sysdate+30) = trunc(cpl.license_period_end) AND EXISTS (SELECT 1 FROM CUSTOMER_CONFIGURATION CC WHERE CC.CUSTOMER_ID = CUCON.CUSTOMER_ID AND CUSTOMER_CONFIGURATION_NAME = 'Allow_Subscription') GROUP BY CUST.CUSTOMER_ID, CUST.CUSTOMER_NAME, CUST.CONTACT_NAME, CUST.CONTACT_EMAIL, CUST.CTB_CONTACT_EMAIL, PROD.PRODUCT_ID, PROD.PRODUCT_NAME, CPL.LICENSE_PERIOD_END, CPL.PO_TEXT, CPL.ORDER_INDEX HAVING SUM(OOL.AVAILABLE) > 0";
			ResultSet rs = dbAccess.executeQuery(query);

			//System.out.println("Query 1 is " + query);

			while(rs.next()) {
				//System.out.println("Inside While");
				boolean isLasLink = false;
				this.productID = rs.getInt("product_id");
				if(this.productID == 7000 || this.productID == 7001 || this.productID == 7002 || this.productID == 7003 || this.productID == 7500 || this.productID == 7501 || this.productID == 7502)
					isLasLink = true;
				if(isLasLink) {
					//this.availableCount = rs.getInt("available");
					this.customerID = rs.getInt("customer_id");
					this.customerName = rs.getString("customer_name");
					this.productName = rs.getString("product_name");
					String customerEmailID = rs.getString("CONTACT_EMAIL");
					String CTBEmailID = rs.getString("CTB_CONTACT_EMAIL");
					this.licensePeriodEnd = rs.getString("license_period_end");
					//this.licenseAfterLastPurchase = rs.getInt("license_after_last_purchase");
					int orderIndex = rs.getInt("order_index");
					//this.orderNumber = rs.getString("order_number");
					this.purchaseOrder = rs.getString("po_text");
					this.actualAvailableCount = rs.getInt("total_available");

					//System.out.println("Cumulative sum of licenses in all the node is :: " + this.actualAvailableCount);

					String customerEmailSubject = "LAS Links Online - Monthly license expire status alert";
					String internalEmailSubject = "LAS Links Online - " + this.customerName.toUpperCase() + " customer monthly license expire status alert";

					/*
                     SELECT USR.FIRST_NAME,
                            USR.MIDDLE_NAME,
                            USR.LAST_NAME,
                            USR.EMAIL,
                            OOL.AVAILABLE,
                            ONC.CATEGORY_LEVEL
                       FROM ORG_NODE              ONO,
                            USER_ROLE             USRRL,
                            USERS                 USR,
                            ROLE                  RL,
                            ORGNODE_ORDER_LICENSE OOL,
                            ORG_NODE_CATEGORY     ONC
                      WHERE ONO.CUSTOMER_ID = CUSTOMERID
                        AND RL.ROLE_NAME IN ('ADMINISTRATOR', 'COORDINATOR', 'ADMINISTRATIVE COORDINATOR')
                        AND ONO.ACTIVATION_STATUS = 'AC'
                        AND USR.ACTIVATION_STATUS = 'AC'
                        AND ONO.ORG_NODE_ID = USRRL.ORG_NODE_ID
                        AND USRRL.ROLE_ID = RL.ROLE_ID
                        AND USRRL.USER_ID = USR.USER_ID
                        AND ONC.ORG_NODE_CATEGORY_ID = ONO.ORG_NODE_CATEGORY_ID
                        AND OOL.ORDER_INDEX = ORDERINDEX
                        AND OOL.ORG_NODE_ID = ONO.ORG_NODE_ID
                        AND OOL.AVAILABLE > 0
					*/

					query = "select usr.first_name,usr.middle_name,usr.last_name,usr.email,ool.available,ONC.CATEGORY_LEVEL from org_node ono, user_role usrrl, users usr, role rl, orgnode_order_license ool,ORG_NODE_CATEGORY ONC where ono.customer_id = "+ this.customerID +" and rl.role_name in ('ADMINISTRATOR', 'COORDINATOR', 'ADMINISTRATIVE COORDINATOR') AND ONO.ACTIVATION_STATUS = 'AC' AND USR.ACTIVATION_STATUS = 'AC' and ono.org_node_id = usrrl.org_node_id and usrrl.role_id = rl.role_id and usrrl.user_id = usr.user_id AND ONC.ORG_NODE_CATEGORY_ID = ONO.ORG_NODE_CATEGORY_ID and ool.order_index=" + orderIndex + " and ool.org_node_id=ono.org_node_id AND OOL.AVAILABLE > 0";

					//System.out.println("Query 2 is :: " + query);

					ResultSet rs2 = dbAccess.executeQuery(query);
					while(rs2.next()) {
						if(rs2.getString("email") != null && rs2.getString("email").length() > 0){
							String userEmailId = rs2.getString("email");
							this.contactName = "";
							if(rs2.getString("first_name") != null && rs2.getString("first_name").length() > 0){
								this.contactName += rs2.getString("first_name") + " ";
							}
							if(rs2.getString("middle_name") != null && rs2.getString("middle_name").length() > 0){
								this.contactName += rs2.getString("middle_name") + " ";
							}
							if(rs2.getString("last_name") != null && rs2.getString("last_name").length() > 0){
								this.contactName += rs2.getString("last_name");
							}
							if(rs2.getString("available") != null && rs2.getInt("available") > 0){
								if(rs2.getInt("CATEGORY_LEVEL") > 0){
									int categoryLevel = rs2.getInt("CATEGORY_LEVEL");
									if(categoryLevel == 1){
										this.availableCount = rs2.getInt("available");
										String userEmailBody = getCustomerEmailBodyForMonthlyEmail();
										//System.out.println("Top Node UserEmailBody :: \n" + customerEmailSubject + "\n" + userEmailBody);
										EmailSender.sendMail("", senderEmailId, userEmailId, "", "", customerEmailSubject, userEmailBody, null, isLasLink);
									}
									else if(categoryLevel > 1){
										this.availableCount = rs2.getInt("available");
										String userEmailBody = getUserEmailBodyForMonthlyEmail();
										//System.out.println("Lower Node UserEmailBody :: \n" + customerEmailSubject + "\n" + userEmailBody);
										EmailSender.sendMail("", senderEmailId, userEmailId, "", "", customerEmailSubject, userEmailBody, null, isLasLink);
									}
								}
							}
						}
					}
					rs2.close();

					this.contactName = rs.getString("contact_name");
					this.availableCount = actualAvailableCount;
					String customerEmailBody = getCustomerEmailBodyForMonthlyEmail();
					String internalEmailBody = getInternalEmailBodyForMonthlyEmail();

					//System.out.println("BCC email ids " + bccUserEmailIds);
					//System.out.println("CustomerEmailBody :: \n" + customerEmailSubject + "\n" + customerEmailBody);
					//System.out.println("\n\nInternalEmailBody :: \n"+ internalEmailSubject + "\n"  + internalEmailBody);

					if ((customerEmailID != null) && (customerEmailID.length() > 0)) {
						EmailSender.sendMail("", senderEmailId, customerEmailID, "", "", customerEmailSubject, customerEmailBody, null, isLasLink);
					}
					if ((CTBEmailID != null) && (CTBEmailID.length() > 0)) {
						EmailSender.sendMail("", senderEmailId, CTBEmailID, "", "", internalEmailSubject, internalEmailBody, null, isLasLink);
					}
				}
			}
			rs.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			//DBAccess.closeConnection(dbConnection.getConnection());
		}
	}

	public void notifyCustomerQuarterly(String[] args) {
		try {
			//System.out.println("Inside notifyCustomerQuarterly()");
			getCommandLine(args);
			dbConnection = DBAccess.createConnection(this.env);
			this.properties = loadProperties(this.env);
			String senderEmailId = getPropertyValue("email.sender");
			if ((senderEmailId == null) || (senderEmailId.length() <= 0)) {
				senderEmailId = this.storedSenderEmailId;
			}
			DBAccess dbAccess = new DBAccess();

			/*
                      SELECT CUST.CUSTOMER_ID,
                             CUST.CUSTOMER_NAME,
                             CUST.CONTACT_NAME,
                             CUST.CONTACT_EMAIL,
                             CUST.CTB_CONTACT_EMAIL,
                             PROD.PRODUCT_ID,
                             PROD.PRODUCT_NAME,
                             TO_CHAR(CPL.LICENSE_PERIOD_END, 'dd/mon/yyyy') AS LICENSE_PERIOD_END,
                             CPL.ORDER_INDEX,
                             CPL.PO_TEXT,
                             SUM(OOL.AVAILABLE) AS TOTAL_AVAILABLE
                        FROM CUSTOMER                 CUST,
                             CUSTOMER_PRODUCT_LICENSE CPL,
                             CUSTOMER_CONFIGURATION   CUCON,
                             PRODUCT                  PROD,
                             ORGNODE_ORDER_LICENSE    OOL
                       WHERE CUST.CUSTOMER_ID = CPL.CUSTOMER_ID
                         AND CUCON.CUSTOMER_ID = CUST.CUSTOMER_ID
                         AND CPL.PRODUCT_ID = PROD.PRODUCT_ID
                         AND CUCON.CUSTOMER_CONFIGURATION_NAME = 'License_Yearly_Expiry'
                         AND OOL.ORDER_INDEX = CPL.ORDER_INDEX
                         AND TRUNC(SYSDATE + 90) = TRUNC(CPL.LICENSE_PERIOD_END)
                         AND EXISTS (SELECT 1
                              FROM CUSTOMER_CONFIGURATION CC
                              WHERE CC.CUSTOMER_ID = CUCON.CUSTOMER_ID
                              AND CUSTOMER_CONFIGURATION_NAME = 'Allow_Subscription')
                       GROUP BY CUST.CUSTOMER_ID,
                                CUST.CUSTOMER_NAME,
                                CUST.CONTACT_NAME,
                                CUST.CONTACT_EMAIL,
                                CUST.CTB_CONTACT_EMAIL,
                                PROD.PRODUCT_NAME,
                                CPL.LICENSE_PERIOD_END,
                                CPL.PO_TEXT,
                                CPL.ORDER_INDEX
                      HAVING SUM(OOL.AVAILABLE) > 0
			 */

			String query = "select cust.customer_id, cust.customer_name, cust.contact_name, cust.contact_email, cust.ctb_contact_email, PROD.PRODUCT_ID, PROD.PRODUCT_NAME, to_char(cpl.license_period_end, 'dd/mon/yyyy') as license_period_end, cpl.order_index, CPL.PO_TEXT,SUM(OOL.AVAILABLE) AS TOTAL_AVAILABLE from customer cust, customer_product_license cpl, customer_configuration cucon, PRODUCT PROD, ORGNODE_ORDER_LICENSE OOL where cust.customer_id = cpl.customer_id and cucon.customer_id = cust.customer_id AND CPL.PRODUCT_ID = PROD.PRODUCT_ID and cucon.customer_configuration_name = 'License_Yearly_Expiry' AND OOL.ORDER_INDEX = CPL.ORDER_INDEX and trunc(sysdate+90) = trunc(cpl.license_period_end) AND EXISTS (SELECT 1 FROM CUSTOMER_CONFIGURATION CC WHERE CC.CUSTOMER_ID = CUCON.CUSTOMER_ID AND CUSTOMER_CONFIGURATION_NAME = 'Allow_Subscription') GROUP BY CUST.CUSTOMER_ID, CUST.CUSTOMER_NAME, CUST.CONTACT_NAME, CUST.CONTACT_EMAIL, CUST.CTB_CONTACT_EMAIL, PROD.PRODUCT_ID, PROD.PRODUCT_NAME, CPL.LICENSE_PERIOD_END, CPL.PO_TEXT, CPL.ORDER_INDEX HAVING SUM(OOL.AVAILABLE) > 0";

			//System.out.println("Query 1 is " + query);
			ResultSet rs = dbAccess.executeQuery(query);

			while(rs.next()) {
				//System.out.println("Inside While");
				boolean isLasLink = false;
				this.productID = rs.getInt("product_id");
				if(this.productID == 7000 || this.productID == 7001 || this.productID == 7002 || this.productID == 7003 || this.productID == 7500 || this.productID == 7501 || this.productID == 7502)
					isLasLink = true;
				
				if(isLasLink) {
					this.customerID = rs.getInt("customer_id");
					this.customerName = rs.getString("customer_name");
					//this.productID = rs.getInt("product_id");
					this.productName = rs.getString("product_name");
					String customerEmailID = rs.getString("CONTACT_EMAIL");
					String CTBEmailID = rs.getString("CTB_CONTACT_EMAIL");
					//this.availableCount = rs.getInt("available");
					this.licensePeriodEnd = rs.getString("license_period_end");
					int orderIndex = rs.getInt("order_index");
					//this.licenseAfterLastPurchase = rs.getInt("license_after_last_purchase");
					//this.orderNumber = rs.getString("order_number");
					this.purchaseOrder = rs.getString("po_text");
					this.actualAvailableCount = rs.getInt("total_available");

					String customerEmailSubject = "LAS Links Online - Quarterly license expire status alert";
					String internalEmailSubject = "LAS Links Online - " + this.customerName.toUpperCase() + " customer quarterly license expire status alert";

					/*
					 SELECT USR.FIRST_NAME,
                            USR.MIDDLE_NAME,
                            USR.LAST_NAME,
                            USR.EMAIL,
                            OOL.AVAILABLE,
                            ONC.CATEGORY_LEVEL
                       FROM ORG_NODE              ONO,
                            USER_ROLE             USRRL,
                            USERS                 USR,
                            ROLE                  RL,
                            ORGNODE_ORDER_LICENSE OOL,
                            ORG_NODE_CATEGORY     ONC
                      WHERE ONO.CUSTOMER_ID = CUSTOMERID
                        AND RL.ROLE_NAME IN ('ADMINISTRATOR', 'COORDINATOR', 'ADMINISTRATIVE COORDINATOR')
                        AND ONO.ACTIVATION_STATUS = 'AC'
                        AND USR.ACTIVATION_STATUS = 'AC'
                        AND ONO.ORG_NODE_ID = USRRL.ORG_NODE_ID
                        AND USRRL.ROLE_ID = RL.ROLE_ID
                        AND USRRL.USER_ID = USR.USER_ID
                        AND ONC.ORG_NODE_CATEGORY_ID = ONO.ORG_NODE_CATEGORY_ID
                        AND OOL.ORDER_INDEX = ORDERINDEX
                        AND OOL.ORG_NODE_ID = ONO.ORG_NODE_ID
                        AND OOL.AVAILABLE > 0
					 */

					query = "select USR.FIRST_NAME, USR.MIDDLE_NAME, USR.LAST_NAME, USR.EMAIL, OOL.AVAILABLE, ONC.CATEGORY_LEVEL from org_node ono, user_role usrrl, users usr, role rl, orgnode_order_license ool, ORG_NODE_CATEGORY ONC where ono.customer_id = "+ this.customerID +" and rl.role_name in ('ADMINISTRATOR', 'COORDINATOR', 'ADMINISTRATIVE COORDINATOR') AND ONO.ACTIVATION_STATUS = 'AC' AND USR.ACTIVATION_STATUS = 'AC' and ono.org_node_id = usrrl.org_node_id and usrrl.role_id = rl.role_id and usrrl.user_id = usr.user_id AND ONC.ORG_NODE_CATEGORY_ID = ONO.ORG_NODE_CATEGORY_ID and  ool.order_index=" + orderIndex + " and ool.org_node_id=ono.org_node_id AND OOL.AVAILABLE > 0";

					//System.out.println("Query 2 is :: " + query);

					ResultSet rs2 = dbAccess.executeQuery(query);
					while(rs2.next()) {
						if(rs2.getString("email") != null && rs2.getString("email").length() > 0){
							String userEmailId = rs2.getString("email");
							this.contactName = "";
							if(rs2.getString("first_name") != null && rs2.getString("first_name").length() > 0){
								this.contactName += rs2.getString("first_name") + " ";
							}
							if(rs2.getString("middle_name") != null && rs2.getString("middle_name").length() > 0){
								this.contactName += rs2.getString("middle_name") + " ";
							}
							if(rs2.getString("last_name") != null && rs2.getString("last_name").length() > 0){
								this.contactName += rs2.getString("last_name");
							}
							if(rs2.getString("available") != null && rs2.getInt("available") > 0){
								if(rs2.getInt("CATEGORY_LEVEL") > 0){
									int categoryLevel = rs2.getInt("CATEGORY_LEVEL");
									if(categoryLevel == 1) {
										this.availableCount = rs2.getInt("available");
										String userEmailBody = getCustomerEmailBodyForQuarterlyEmail();
										//System.out.println("Top Node UserEmailBody :: \n" + customerEmailSubject + "\n" + userEmailBody);
										EmailSender.sendMail("", senderEmailId, userEmailId, "", "", customerEmailSubject, userEmailBody, null, true);
									}
									else if(categoryLevel > 1) {
										this.availableCount = rs2.getInt("available");
										String userEmailBody = getUserEmailBodyForQuarterlyEmail();
										//System.out.println("Lower Node UserEmailBody :: \n" + customerEmailSubject + "\n" + userEmailBody);
										EmailSender.sendMail("", senderEmailId, userEmailId, "", "", customerEmailSubject, userEmailBody, null, true);
									}
								}
							}
						}
					}
					rs2.close();

					this.contactName = rs.getString("contact_name");
					this.availableCount = actualAvailableCount;
					String customerEmailBody = getCustomerEmailBodyForQuarterlyEmail();
					String internalEmailBody = getInternalEmailBodyForQuarterlyEmail();

					//System.out.println("CustomerEmailBody :: \n" + customerEmailSubject + "\n" + customerEmailBody);
					//System.out.println("\n\nInternalEmailBody :: \n"+ internalEmailSubject + "\n"  + internalEmailBody);

					if ((customerEmailID != null) && (customerEmailID.length() > 0)) {
						EmailSender.sendMail("", senderEmailId, customerEmailID, "", "", customerEmailSubject, customerEmailBody, null, true);
					}
					if ((CTBEmailID != null) && (CTBEmailID.length() > 0)) {
						EmailSender.sendMail("", senderEmailId, CTBEmailID, "", "", internalEmailSubject, internalEmailBody, null, true);
					}
				}
			}
			rs.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		} finally {
			//DBAccess.closeConnection(dbConnection.getConnection());
		}
	}

	public void notifyCustomerOnLicenseExpiry(String []args) {
		try {
			//System.out.println("Inside notifyCustomerOnLicenseExpiry()");
			getCommandLine(args);
			dbConnection = DBAccess.createConnection(this.env);
			this.properties = loadProperties(this.env);
			String senderEmailId = getPropertyValue("email.sender");
			if ((senderEmailId == null) || (senderEmailId.length() <= 0)) {
				senderEmailId = this.storedSenderEmailId;
			}
			DBAccess dbAccess = new DBAccess();

			/*
                      SELECT CUST.CUSTOMER_ID,
                             CUST.CUSTOMER_NAME,
                             CUST.CONTACT_NAME,
                             CUST.CONTACT_EMAIL,
                             CUST.CTB_CONTACT_EMAIL,
                             PROD.PRODUCT_ID,
                             PROD.PRODUCT_NAME,
                             TO_CHAR(CPL.LICENSE_PERIOD_END, 'dd/mon/yyyy') AS LICENSE_PERIOD_END,
                             CPL.ORDER_INDEX,
                             CPL.PO_TEXT,
                             SUM(OOL.AVAILABLE) AS TOTAL_AVAILABLE
                        FROM CUSTOMER                 CUST,
                             CUSTOMER_PRODUCT_LICENSE CPL,
                             CUSTOMER_CONFIGURATION   CUCON,
                             PRODUCT                  PROD,
                             ORGNODE_ORDER_LICENSE    OOL
                       WHERE CUST.CUSTOMER_ID = CPL.CUSTOMER_ID
                         AND CUCON.CUSTOMER_ID = CUST.CUSTOMER_ID
                         AND CPL.PRODUCT_ID = PROD.PRODUCT_ID
                         AND CUCON.CUSTOMER_CONFIGURATION_NAME = 'License_Yearly_Expiry'
                         AND OOL.ORDER_INDEX = CPL.ORDER_INDEX
                         AND TRUNC(SYSDATE - 1) = TRUNC(CPL.LICENSE_PERIOD_END)
                         AND EXISTS (SELECT 1
                              FROM CUSTOMER_CONFIGURATION CC
                              WHERE CC.CUSTOMER_ID = CUCON.CUSTOMER_ID
                              AND CUSTOMER_CONFIGURATION_NAME = 'Allow_Subscription')
                       GROUP BY CUST.CUSTOMER_ID,
                                CUST.CUSTOMER_NAME,
                                CUST.CONTACT_NAME,
                                CUST.CONTACT_EMAIL,
                                CUST.CTB_CONTACT_EMAIL,
                             	PROD.PRODUCT_NAME,
                                CPL.LICENSE_PERIOD_END,
                                CPL.PO_TEXT,
                                CPL.ORDER_INDEX
                      HAVING SUM(OOL.AVAILABLE) > 0
			 */

			String query = "select cust.customer_id, cust.customer_name, cust.contact_name, cust.contact_email, cust.ctb_contact_email, PROD.PRODUCT_ID, PROD.PRODUCT_NAME, to_char(cpl.license_period_end, 'dd/mon/yyyy') as license_period_end, cpl.order_index, CPL.PO_TEXT, SUM(OOL.AVAILABLE) AS TOTAL_AVAILABLE from customer cust, customer_product_license cpl, customer_configuration cucon, PRODUCT PROD, ORGNODE_ORDER_LICENSE OOL where cust.customer_id = cpl.customer_id and cucon.customer_id = cust.customer_id AND CPL.PRODUCT_ID = PROD.PRODUCT_ID and cucon.customer_configuration_name = 'License_Yearly_Expiry' AND OOL.ORDER_INDEX = CPL.ORDER_INDEX and trunc(sysdate-1) = trunc(cpl.license_period_end) AND EXISTS (SELECT 1 FROM CUSTOMER_CONFIGURATION CC WHERE CC.CUSTOMER_ID = CUCON.CUSTOMER_ID AND CUSTOMER_CONFIGURATION_NAME = 'Allow_Subscription') GROUP BY CUST.CUSTOMER_ID, CUST.CUSTOMER_NAME, CUST.CONTACT_NAME, CUST.CONTACT_EMAIL, CUST.CTB_CONTACT_EMAIL, PROD.PRODUCT_ID, PROD.PRODUCT_NAME, CPL.LICENSE_PERIOD_END, CPL.PO_TEXT, CPL.ORDER_INDEX HAVING SUM(OOL.AVAILABLE) > 0";
			ResultSet rs = dbAccess.executeQuery(query);

			//System.out.println("Query 1 is " + query);

			while(rs.next()) {
				//System.out.println("Inside while");
				boolean isLasLink = false;
				this.productID = rs.getInt("product_id");
				if(this.productID == 7000 || this.productID == 7001 || this.productID == 7002 || this.productID == 7003 || this.productID == 7500 || this.productID == 7501 || this.productID == 7502)
					isLasLink = true;
				
				if(isLasLink) {
					this.customerID = rs.getInt("customer_id");
					this.customerName = rs.getString("customer_name");
					//this.productID = rs.getInt("product_id");
					this.productName = rs.getString("product_name");
					this.contactName = rs.getString("contact_name");
					String customerEmailID = rs.getString("CONTACT_EMAIL");
					String CTBEmailID = rs.getString("CTB_CONTACT_EMAIL");
					//this.availableCount = rs.getInt("available");
					this.licensePeriodEnd = rs.getString("license_period_end");
					int orderIndex = rs.getInt("order_index");
					//this.licenseAfterLastPurchase = rs.getInt("license_after_last_purchase");
					//this.orderNumber = rs.getString("order_number");
					this.purchaseOrder = rs.getString("po_text");
					this.actualAvailableCount = rs.getInt("total_available");

					String customerEmailSubject = "LAS Links Online - license expire status alert";
					String internalEmailSubject = "LAS Links Online - " + this.customerName.toUpperCase() + " customer license expire status alert";

					/*
                     SELECT USR.FIRST_NAME,
                            USR.MIDDLE_NAME,
                            USR.LAST_NAME,
                            USR.EMAIL,
                            OOL.AVAILABLE,
                            ONC.CATEGORY_LEVEL
                       FROM ORG_NODE              ONO,
                            USER_ROLE             USRRL,
                            USERS                 USR,
                            ROLE                  RL,
                            ORGNODE_ORDER_LICENSE OOL,
                            ORG_NODE_CATEGORY     ONC
                      WHERE ONO.CUSTOMER_ID = CUSTOMERID
                        AND RL.ROLE_NAME IN ('ADMINISTRATOR', 'COORDINATOR', 'ADMINISTRATIVE COORDINATOR')
                        AND ONO.ACTIVATION_STATUS = 'AC'
                        AND USR.ACTIVATION_STATUS = 'AC'
                        AND ONO.ORG_NODE_ID = USRRL.ORG_NODE_ID
                        AND USRRL.ROLE_ID = RL.ROLE_ID
                        AND USRRL.USER_ID = USR.USER_ID
                        AND ONC.ORG_NODE_CATEGORY_ID = ONO.ORG_NODE_CATEGORY_ID
                        AND OOL.ORDER_INDEX = ORDERINDEX
                        AND OOL.ORG_NODE_ID = ONO.ORG_NODE_ID
                        AND OOL.AVAILABLE > 0
					*/

					query = "select USR.FIRST_NAME, USR.MIDDLE_NAME, USR.LAST_NAME, USR.EMAIL, OOL.AVAILABLE, ONC.CATEGORY_LEVEL from org_node ono, user_role usrrl, users usr, role rl, orgnode_order_license ool, org_node_category onc where ono.customer_id = "+ this.customerID +" and rl.role_name in ('ADMINISTRATOR', 'COORDINATOR', 'ADMINISTRATIVE COORDINATOR') AND ONO.ACTIVATION_STATUS = 'AC' AND USR.ACTIVATION_STATUS = 'AC' and ono.org_node_id = usrrl.org_node_id and usrrl.role_id = rl.role_id and usrrl.user_id = usr.user_id and onc.org_node_category_id = ono.org_node_category_id and ool.order_index=" + orderIndex + " and ool.org_node_id=ono.org_node_id AND OOL.AVAILABLE > 0";
					//System.out.println("Query 2 is :: " + query);
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
							if(rs2.getString("available") != null && rs2.getInt("available") > 0) {
								if(rs2.getInt("CATEGORY_LEVEL") > 0) {
									int categoryLevel = rs2.getInt("CATEGORY_LEVEL");
									if(categoryLevel == 1) {
										this.availableCount = rs2.getInt("available");
										String userEmailBody = getCustomerEmailBodyOnLicenseExpiry();
										//System.out.println("Top Node UserEmailBody :: \n" + customerEmailSubject + "\n" + userEmailBody);
										EmailSender.sendMail("", senderEmailId, userEmailId, "", "", customerEmailSubject, userEmailBody, null, true);
									}
									else if(categoryLevel > 1) {
										this.availableCount = rs2.getInt("available");
										String userEmailBody = getOtherNodeUserEmailBodyOnLicenseExpiry();
										//System.out.println("Lower Node UserEmailBody :: \n" + customerEmailSubject + "\n" + userEmailBody);
										EmailSender.sendMail("", senderEmailId, userEmailId, "", "", customerEmailSubject, userEmailBody, null, true);
									}
								}
							}
						}
					}
					rs2.close();

					this.contactName = rs.getString("contact_name");
					this.availableCount = actualAvailableCount;
					String customerEmailBody = getCustomerEmailBodyOnLicenseExpiry();
					String internalEmailBody = getInternalEmailBodyOnLicenseExpiry();

					//System.out.println("\nCustomerEmailBody :: \n" + customerEmailSubject + "\n" + customerEmailBody);
					//System.out.println("\nInternalEmailBody :: \n"+ internalEmailSubject + "\n"  + internalEmailBody);

					if ((customerEmailID != null) && (customerEmailID.length() > 0)) {
						EmailSender.sendMail("", senderEmailId, customerEmailID, "", "", customerEmailSubject, customerEmailBody, null, true);
					}
					if ((CTBEmailID != null) && (CTBEmailID.length() > 0)) {
						EmailSender.sendMail("", senderEmailId, CTBEmailID, "", "", internalEmailSubject, internalEmailBody, null, true);
					}
				}
			}
			rs.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}finally{
			//DBAccess.closeConnection(dbConnection.getConnection());
		}
	}

	private void getCommandLine(String[] args) {
		if ((args.length < 1) || (args[0].indexOf('=') >= 0)) {
			System.out.println("Cannot parse command line. No command specified.");
		}
		else
			this.env = args[0].toLowerCase();
	}

	private static Properties loadProperties(String env) {
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

	private String getInternalEmailBodyForMonthlyEmail() {
		String msg = "";
		try {
			msg = "Dear Customer Service:\n\n  For " + this.customerName + " Customer " + this.actualAvailableCount + " Licenses for "+ this.productName + " Product and " + this.purchaseOrder + " Purchase Order are going to expire on " + this.licensePeriodEnd + "." +
			"\n\nYou may wish to contact this customer in the near future to arrange for an additional purchase.";
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}

	private String getInternalEmailBodyForQuarterlyEmail(){
		String msg = "";
		try {
			msg = "Dear Customer Service:\n\n  For " + this.customerName + " Customer " + this.actualAvailableCount + " Licenses for "+ this.productName + " Product and " + this.purchaseOrder + " Purchase Order are going to expire on " + this.licensePeriodEnd + "." +
			"\n\nYou may be hearing from this customer in the near future to purchase additional licenses.";
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}

	private String getCustomerEmailBodyForMonthlyEmail() {
		String msg = "";
		try {
			msg = "Dear " + this.contactName + ":\n\n  For " + this.customerName + " Customer " + this.availableCount + " Licenses for "+ this.productName + " Product and " + this.purchaseOrder + " Purchase Order are going to expire on " + this.licensePeriodEnd  + "." +
			"\nPlease use these licenses before this date and call Customer Support to ensure you have sufficient licenses for your next test session." + 
			"\n\nWe appreciate your business and value you as a LAS links Online customer. We are available to serve you M-F, 9:00 a.m. to 5:00 p.m. in your timezone." + 
			"\nTo contact us call 800-538-9547, and press Option 1." + 
			"\n\nYour CTB/McGraw-Hill Companies Customer Support team.";
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}

	private String getCustomerEmailBodyForQuarterlyEmail() {
		String msg = "";
		try {
			msg = "Dear " + this.contactName + ":\n\n  For " + this.customerName + " Customer " + this.availableCount + " Licenses for "+ this.productName + " Product and " + this.purchaseOrder + " Purchase Order are going to expire on " + this.licensePeriodEnd  + "." +
			"\nPlease use these licenses before this date and call Customer Support if you need additional licenses for your next test session." + 
			"\n\nWe appreciate your business and value you as a LAS links Online customer. We are available to serve you M-F, 9:00 a.m. to 5:00 p.m. in your timezone." + 
			"\nTo contact us call 800-538-9547, and press Option 1." + 
			"\n\nYour CTB/McGraw-Hill Companies Customer Support team.";
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}

	private String getUserEmailBodyForMonthlyEmail() {
		String msg = "";
		try {
			msg = "Dear " + this.contactName + ":\n\n  For " + this.customerName + " Customer " + this.availableCount + " Licenses for "+ this.productName + " Product and " + this.purchaseOrder + " Purchase Order are going to expire on " + this.licensePeriodEnd  + "." +
			"\nPlease use these licenses before this date and call your LAS Links Online administrator to ensure you have sufficient licenses for your next test session." + 
			"\n\nWe appreciate your business and value you as a LAS links Online customer. We are available to serve you M-F, 9:00 a.m. to 5:00 p.m. in your timezone." + 
			"\nTo contact us call 800-538-9547, and press Option 1." + 
			"\n\nYour CTB/McGraw-Hill Companies Customer Support team.";
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}

	private String getUserEmailBodyForQuarterlyEmail(){
		String msg = "";
		try {
			msg = "Dear " + this.contactName + ":\n\n  For " + this.customerName + " Customer " + this.availableCount + " Licenses for "+ this.productName + " Product and " + this.purchaseOrder + " Purchase Order are going to expire on " + this.licensePeriodEnd  + "." +
			"\nPlease use these licenses before this date and call your LAS Links Online administrator if you need additional licenses for your next test session." + 
			"\n\nWe appreciate your business and value you as a LAS links Online customer. We are available to serve you M-F, 9:00 a.m. to 5:00 p.m. in your timezone." + 
			"\nTo contact us call 800-538-9547, and press Option 1." + 
			"\n\nYour CTB/McGraw-Hill Companies Customer Support team.";
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}

	private String getCustomerEmailBodyOnLicenseExpiry() {
		String msg = "";
		try {
			msg = "Dear " + this.contactName + ":\n\n  For " + this.customerName + " Customer " + this.availableCount + " Licenses for "+ this.productName + " Product and " + this.purchaseOrder + " Purchase Order expired Yesterday (" + this.licensePeriodEnd  + ")." +
			"\nPlease contact your LAS Links Administrator or CTB Customer Service if you are the LAS Links Administrator to arrange for additional licenses for your next test session." +
			"\n\nWe appreciate your business and value you as a LAS links Online customer. We are available to serve you M-F, 9:00 a.m. to 5:00 p.m. in your timezone." + 
			"\nTo contact us call 800-538-9547, and press Option 1." +
			"\n\nYour CTB/McGraw-Hill Companies Customer Support team.";
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}

	private String getOtherNodeUserEmailBodyOnLicenseExpiry() {
		String msg = "";
		try {
			msg = "Dear " + this.contactName + ":\n\n  For " + this.customerName + " Customer " + this.availableCount + " Licenses for "+ this.productName + " Product and " + this.purchaseOrder + " Purchase Order expired Yesterday (" + this.licensePeriodEnd  + ")." +
			"\nPlease contact your LAS Links Administrator or CTB Customer Service if you are the LAS Links Administrator to arrange for additional licenses for your next test session." +
			"\n\nWe appreciate your business and value you as a LAS links Online customer. We are available to serve you M-F, 9:00 a.m. to 5:00 p.m. in your timezone." + 
			"\nTo contact us call 800-538-9547, and press Option 1." +
			"\n\nYour CTB/McGraw-Hill Companies Customer Support team.";
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}

	private String getInternalEmailBodyOnLicenseExpiry() {
		String msg = "";
		try {
			msg = "Dear Customer Service:\n\n  For " + this.customerName + " Customer " + this.actualAvailableCount + " Licenses for "+ this.productName + " Product and " + this.purchaseOrder + " Purchase Order expired Yesterday (" + this.licensePeriodEnd + ")." +
			"\n\nPlease contact this customer to arrange for an additional purchase.";
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}

	private String getPropertyValue(String name) {
		String value = this.properties.getProperty(name);
		if ((value == null) || (value.length() == 0)) {
			return "";
		}
		return value;
	}
}