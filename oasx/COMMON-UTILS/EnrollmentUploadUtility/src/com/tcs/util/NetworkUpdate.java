package com.tcs.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ctb.bean.Network;
import com.tcs.dataaccess.AbstractConnectionManager;
import com.tcs.dataaccess.ConnectionManager;
import com.tcs.dataaccess.NetworkUpdateHelper;

public class NetworkUpdate {
	private static final Logger logger = Logger
	.getLogger(NetworkUpdate.class);
	private String dbProperties;
	private String csv;
	private String year;
	private String marketType;
	private int rtsCustomerId;
	private Connection connection;
	private final String netWorkSql = "update site_survey_network sn SET sn.INET_CONN_TYPE = ?, sn.INET_DOWN_SPEED = ?, sn.INET_UP_SPEED = ? where sn.SITE_SURVEY_ID = ?";
	private final String workStationSql = "UPDATE site_survey_workstation  SET workstation_type = ? , WORKSTATION_COUNT = ? , OPERATING_SYSTEM = ?, PROCESSOR = ?, PHYSICAL_MEMORY = ? where SITE_SURVEY_ID = ?";

	public NetworkUpdate() {

	}

	public NetworkUpdate(String dbprop, String csv, String year,
			String marketType) {

		this.dbProperties = dbprop;
		this.csv = csv;
		this.year = year;
		this.marketType = marketType;
	}

	public void updateDatabase() {
		try {
			AbstractConnectionManager.processProperties(dbProperties);

			this.rtsCustomerId = Integer.parseInt(AbstractConnectionManager
					.getCustomerId());

			this.connection = ConnectionManager.getConnection();
			connection.setAutoCommit(false);

			PreparedStatement workStationStatement = connection
					.prepareStatement(this.workStationSql);
			PreparedStatement netWorkStatement = connection
					.prepareStatement(this.netWorkSql);
			Map<String, PreparedStatement> stamentMap = new HashMap<String, PreparedStatement>();
			stamentMap.put("network", netWorkStatement);
			stamentMap.put("workStation", workStationStatement);
			List list = CSVFileReader.getFileContent(this.csv);
			DataFormater dataFormater = new DataFormater();
			List<Network> networks = dataFormater.getNetworkList(list);
			processEachNetworkRecord(networks, stamentMap);
			connection.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.info(e);
		}
	}// end of method

	private void processEachNetworkRecord(List<Network> networks,
			Map<String, PreparedStatement> stmtMap) throws Exception {
		Integer siteSurveyId = 0;
		String customerId = null;
		Map<String, String> properties = null;

		;
		for (Network network : networks) {
			String schoolId = "";
			String distId="";
				distId = network.getDistNumber();
			if (!network.getSchoolNumber().equals(""))
				schoolId = network.getSchoolNumber();
			Map<Integer, String> map = CSVFileReader.getSiteSurveyIdAndType(
					rtsCustomerId, schoolId,distId, connection);

			if (map.containsKey(0))
				logger.info("Network And workstation update information combination does not matches in DB :district id ["
								+ network.getDistNumber()
								+ "], school id ["
								+ network.getSchoolNumber()
								+ "]. Skipping processing of this row.");
			else {
				siteSurveyId=map.keySet().iterator().next();
				String value = map.get(siteSurveyId);
				NetworkUpdateHelper helper = new NetworkUpdateHelper(
						siteSurveyId, network);
				if (value.equalsIgnoreCase("Corporation"))
					helper.updateSiteSurveyNetowkTable(stmtMap.get("network"));

				else if (value.equalsIgnoreCase("School")) {
					helper.updateSiteSurveyWorkstationTable(stmtMap
							.get("workStation"));
					helper.updateSiteSurveyNetowkTable(stmtMap.get("network"));
					
				}

			}

		}

	}
}// end of class
