package com.tcs.upload;

import java.io.FileInputStream;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.tcs.dataaccess.AbstractConnectionManager;
import com.tcs.dataaccess.ConnectionManager;
import com.tcs.util.CSVFileReader;
import com.tcs.util.ContactsUpdate;
import com.tcs.util.NetworkUpdate;

public class MainUpload {
	private static int CUSTOMER_ID_RTS = 0;
	private static final String updateTypeNetowrk = "NT";
	private static final String updateTypeContact = "CON";
	private static final String updateTypeWork = "WOR";
	private static final Logger logger = Logger.getLogger(MainUpload.class);

	/**
	 * @param args
	 */
	public static void main(String... args) throws Exception {
		// TODO Auto-generated method stub
		Connection con = null;
		String dbprop = null;
		String csv = null;
		String year = null;
		String marketType = null;
		String updateType = null;
		if (args.length != 3) {
			if (args.length == 5) {				
				dbprop = args[0];// getting db properties
				csv = args[1];
				year = args[2];
				marketType = args[3];
				updateType = args[4];
				logger.info("Update type :--"+updateType);
				if (updateType.equalsIgnoreCase(updateTypeContact)) {
					ContactsUpdate contactsUpdate = new ContactsUpdate(dbprop,
							csv, year, marketType);
					contactsUpdate.processContactForOkhlahama();
				} else if (updateType.equalsIgnoreCase(updateTypeNetowrk)) {
					NetworkUpdate networkUpdate = new NetworkUpdate(dbprop,
							csv, year, marketType);
					networkUpdate.updateNetworkInformation();
				}
				else if(updateType.equalsIgnoreCase(updateTypeWork)){
					NetworkUpdate workNetworkUpdate=new NetworkUpdate(dbprop, csv, year, marketType);
					workNetworkUpdate.insertWorkstatinInformation();
				}
			} else {
				logger.info("Incorrect argument....");
				logger.info("Please provide the argument with order: <properties file> <csv file> <year>");
				throw new Exception("Incorrect argument....");
			}
		} else {
			dbprop = args[0];
			csv = args[1];
			year = args[2];
			if (!dbprop.substring(dbprop.indexOf(".") + 1).equals("properties")) {
				logger.info("First Argument is not a properties file...");
				logger.info("Please provide the three argument with order: <properties file> <csv file> <year>");
				throw new Exception();
			} else if (!csv.substring(csv.indexOf(".") + 1).equals("csv")) {

				logger.info("Secound Argument is not a CSV file...");
				logger.info("Please provide the three argument with order: <properties file> <csv file> <year>");
				throw new Exception();

			} else {
				try {
					Integer.valueOf(year);
				} catch (Exception e) {
					logger.info("Third Argument is not a valid year ...");
					logger.info("Please provide the three argument with order: <properties file> <csv file> <year>");
					throw new Exception();
				}

				AbstractConnectionManager.processProperties(dbprop);
			}

			try {
				Properties prop = new Properties();
				prop.load(new FileInputStream(dbprop));
				CUSTOMER_ID_RTS = Integer.parseInt(prop
						.getProperty("rts.customerId"));
				// Integer(ExtractUtil.getDetail("rts.customerId"));

				con = ConnectionManager.getConnection();
				con.setAutoCommit(false);

				List list = CSVFileReader.getFileContent(csv);
				Integer siteSurverId = 0;
				Boolean isSubject = false;
				String customerId = null;
				Map<String, String> properties = null;
				String enrollmentTableName = "site_survey_enrollment";
				String testSessionPerday = "3";
				String noTestingDays = "10";

				String testSessionPerday_wv = "2";
				String noTestingDays_wv = "20";

				String customerType = "";
				for (int i = 1; i < list.size(); i++) {
					logger.info("Processing started for row==>"
							+ (i + 1));
					logger.info("list.size()=" + list.size());
					String rowData[] = (String[]) list.get(i);
					logger.info("Size : " + list.size() + "  "
							+ rowData[4] + "   " + rowData[5]);
					siteSurverId = CSVFileReader
							.getSiteSurveyIDBySchoolAndDistrictNo(
									CUSTOMER_ID_RTS, rowData[4], rowData[5],
									con);
					logger.info("siteSurverId===>" + siteSurverId);

					if (siteSurverId == 0) {
						logger.info("Given information combination does not matches in DB :district id ["
										+ rowData[4]
										+ "], school id ["
										+ rowData[5]
										+ "]. Skipping processing of this row.");

					} else {
						customerId = CSVFileReader.getCustomerId(rowData[4],
								rowData[5], con);
						if (null == customerId) {
							logger.info("Given information combination does not matches in DB :district id ["
											+ rowData[4]
											+ "], school id ["
											+ rowData[5]
											+ "]. Skipping processing of this row.");
							continue;
						}
						properties = CSVFileReader.getCustomerPropValuesById(
								customerId, con);
						logger.info("Properties for cusomerid["
								+ customerId + "] are " + properties);
						if (null == properties) {
							isSubject = false; // old corporation/district might
												// not have properties
						} else {
							if (properties.get("ENROLLMENTBYSUBJECT") != null
									&& properties.get("ENROLLMENTBYSUBJECT")
											.equalsIgnoreCase("TRUE")) {
								isSubject = true;
								enrollmentTableName = "site_survey_enrollment_subject";
							} else
								isSubject = false;
						}
						customerType = properties.get("CUSTOMERTYPE");
						logger.info("customerType=" + customerType);
						if (customerType.equalsIgnoreCase("WV")) {
							testSessionPerday_wv = properties
									.get("TESTSESSIONPERDAY") == null ? testSessionPerday_wv
									: properties.get("TESTSESSIONPERDAY");
							noTestingDays_wv = properties
									.get("TOTALTESTINGDAYS") == null ? noTestingDays_wv
									: properties.get("TOTALTESTINGDAYS");
						} else {
							testSessionPerday = properties
									.get("TESTSESSIONPERDAY") == null ? testSessionPerday
									: properties.get("TESTSESSIONPERDAY");
							noTestingDays = properties.get("TOTALTESTINGDAYS") == null ? noTestingDays
									: properties.get("TOTALTESTINGDAYS");
						}
						if (!isSubject)
							if (!customerType.equalsIgnoreCase("WV"))
								CSVFileReader.saveOrUpdateSiteSurveyEnrollMent(
										rowData, siteSurverId, year, con,
										enrollmentTableName, false,
										testSessionPerday, noTestingDays);
							else
								CSVFileReader
										.saveOrUpdateSiteSurveyEnrollMent_WV(
												rowData, siteSurverId, year,
												con, enrollmentTableName,
												testSessionPerday_wv,
												noTestingDays_wv);
						else
							CSVFileReader.saveOrUpdateSiteSurveyEnrollMent(
									rowData, siteSurverId, year, con,
									enrollmentTableName, true,
									testSessionPerday, noTestingDays);
					}
				}

			} catch (Exception e) {
				logger.info(e);
			} finally {

				try {

					con.commit();
					ConnectionManager.close(con);
				} catch (Exception e) {
					logger.info(e);
				}

				logger.info("Finished....");
			}

		}
		logger.info("Finished....");
	}
	
}
