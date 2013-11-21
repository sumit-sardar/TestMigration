package com.tcs.dataaccess;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.ctb.bean.Network;
import com.ctb.bean.WorkStation;

public class NetworkUpdateHelper {

	private static final Logger logger = Logger
			.getLogger(NetworkUpdateHelper.class);

	private Integer surveyId;
	private Network network;
	private WorkStation workStation;

	public NetworkUpdateHelper(Integer siteSurveyID, Network network) {
		this.surveyId = siteSurveyID;
		this.network = network;
	}

	public NetworkUpdateHelper(Integer siteSurveyID, WorkStation workStation) {
		this.surveyId = siteSurveyID;
		this.workStation = workStation;
	}

	public boolean updateSiteSurveyTable(PreparedStatement preparedStatement)
			throws SQLException {

		return false;
	}

	public int updateSiteSurveyNetowkTable(PreparedStatement preparedStatement)
			throws SQLException {
		
		preparedStatement.setInt(1, this.surveyId);
		
		preparedStatement.setString(2, network.getInternetConnectionType());
		if (!network.getUpSpeed().equals(""))
			preparedStatement.setString(3,  network.getDownSpeed());
		else
			preparedStatement.setString(3, "--");
		if (!network.getDownSpeed().equals(""))
			preparedStatement.setString(4,network.getUpSpeed());
		else
			preparedStatement.setString(4, "--");
		preparedStatement.setInt(5, this.surveyId);
		preparedStatement.setString(6, network.getInternetConnectionType());
		if (!network.getUpSpeed().equals(""))
			preparedStatement.setString(7, network.getDownSpeed());
		else
			preparedStatement.setString(7, "--");
		if (!network.getDownSpeed().equals(""))
			preparedStatement.setString(8, network.getUpSpeed());
		else
			preparedStatement.setString(8, "--");
		
		int flag = preparedStatement.executeUpdate();
		if (flag == 0)
			logger.info("Network record not updated for " + this.surveyId);
		return flag;
	}

	public int updateSiteSurveyWorkstationTable(
			PreparedStatement preparedStatement) throws SQLException {

		preparedStatement.setInt(1, this.surveyId);
		preparedStatement.setString(2, workStation.getOperatingSystem());
		preparedStatement.setString(3, workStation.getTypeOfWorkstation());
		if (!workStation.getNumberOfWorkstation().equals(""))
			preparedStatement
					.setString(4, workStation.getNumberOfWorkstation());
		else
			preparedStatement.setString(4, "0");
		if (!workStation.getProcessorSpeed().equals(""))
			preparedStatement.setString(5, workStation.getProcessorSpeed());
		else
			preparedStatement.setString(5, "--");
		if (!workStation.getMemory().equals(""))
			preparedStatement.setString(6, workStation.getMemory());
		else
			preparedStatement.setString(6, "--");

		preparedStatement.setInt(7, this.surveyId);
		if (!workStation.getOperatingSystem().equals(""))
			preparedStatement.setString(8, workStation.getOperatingSystem());
		else
			preparedStatement.setString(8, "--");

		preparedStatement.setString(9, workStation.getTypeOfWorkstation());
		if (!workStation.getNumberOfWorkstation().equals(""))
			preparedStatement.setString(10,
					workStation.getNumberOfWorkstation());
		else
			preparedStatement.setString(10, "0");
		if (!workStation.getProcessorSpeed().equals(""))
			preparedStatement.setString(11, workStation.getProcessorSpeed());
		else
			preparedStatement.setString(11, "--");
		if (!workStation.getMemory().equals(""))
			preparedStatement.setString(12, workStation.getMemory());
		else
			preparedStatement.setString(12, "--");
		int flag = preparedStatement.executeUpdate();
		if (flag == 0)
			logger.info("Workstation record not updated for " + this.surveyId
					+ " There is no workstation for this id");
		return flag;
	}

}// end of class
