package com.tcs.dataaccess;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.ctb.bean.Network;

public class NetworkUpdateHelper {

	private static final Logger logger = Logger
			.getLogger(NetworkUpdateHelper.class);

	private Integer surveyId;
	private Network network;

	public NetworkUpdateHelper(Integer siteSurveyID, Network network) {
		this.surveyId = siteSurveyID;
		this.network = network;
	}

	public boolean updateSiteSurveyTable(PreparedStatement preparedStatement)
			throws SQLException {

		return false;
	}

	public int updateSiteSurveyNetowkTable(
			PreparedStatement preparedStatement) throws SQLException {
		preparedStatement.setString(1, network.getInternetConnectionType());

		if (!network.getUpSpeed().equals(""))
			preparedStatement.setString(2, network.getUpSpeed());
		else
			preparedStatement.setString(2, "--");
		if (!network.getDownSpeed().equals(""))
			preparedStatement.setString(3, network.getDownSpeed());
		else
			preparedStatement.setString(3, "--");
		preparedStatement.setInt(4, this.surveyId);
		int flag = preparedStatement.executeUpdate();
		if(flag==0)
			logger.info("Network record not updated for "+this.surveyId+" There is no network for this id");
		return flag;
	}

	public int updateSiteSurveyWorkstationTable(
			PreparedStatement preparedStatement) throws SQLException {
		// update site_survey_workstation ws set ws.workstation_type = ? ,
		// ws.WORKSTATION_COUNT = ? ,
		// ws.OPERATING_SYSTEM = ?,
		// ws.PROCESSOR = ?
		// ws.PHYSICAL_MEMORY = ? where ws.SITE_SURVEY_ID = ?
		preparedStatement.setString(1, network.getTypeOfWorkstation());
		if(!network.getNumberOfWorkstation().equals(""))
		preparedStatement.setString(2, network.getNumberOfWorkstation());
		else
		preparedStatement.setString(2, "0");
		if(!network.getOperatingSystem().equals(""))
		preparedStatement.setString(3, network.getOperatingSystem());
		else
			preparedStatement.setString(3, "--");
		if(!network.getProcessorSpeed().equals(""))
		preparedStatement.setString(4, network.getProcessorSpeed());
		else
		preparedStatement.setString(4,"--");
		if(!network.getMemory().equals(""))
		preparedStatement.setString(5, network.getMemory());
		else
			preparedStatement.setString(5, "--");
		preparedStatement.setInt(6, this.surveyId);
		int flag = preparedStatement.executeUpdate();
		if(flag==0)
			logger.info("Workstation record not updated for "+this.surveyId+" There is no workstation for this id");
		return flag;
	}

}// end of class
