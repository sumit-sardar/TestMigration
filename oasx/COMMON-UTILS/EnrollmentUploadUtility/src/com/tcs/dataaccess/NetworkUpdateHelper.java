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
		
		preparedStatement.setString(5,network.getIspName());
		preparedStatement.setString(6,network.getIspContact());
		preparedStatement.setString(7,network.getIspPhone());
		preparedStatement.setString(8,network.getFirewallVendor());
		preparedStatement.setString(9,network.getProxyVendor());
		preparedStatement.setString(10,network.getFilteringVendor());
		if(!network.getConType().equals(""))
			preparedStatement.setString(11,network.getConType());
		else
			preparedStatement.setString(11,"--");
		if(!network.getConSpeed().equals(""))
			preparedStatement.setString(12,network.getConSpeed());
		else
			preparedStatement.setString(12,"--");
		if(!network.getConCount().equals(""))
			preparedStatement.setString(13,network.getConCount());
		else
			preparedStatement.setString(13,"--");
		preparedStatement.setString(14,network.getNetworkNotes());
		
		
		
		
		
		preparedStatement.setInt(15, this.surveyId);
		preparedStatement.setString(16, network.getInternetConnectionType());
		if (!network.getUpSpeed().equals(""))
			preparedStatement.setString(17, network.getDownSpeed());
		else
			preparedStatement.setString(17, "--");
		if (!network.getDownSpeed().equals(""))
			preparedStatement.setString(18, network.getUpSpeed());
		else
			preparedStatement.setString(18, "--");
		
		preparedStatement.setString(19,network.getIspName());
		preparedStatement.setString(20,network.getIspContact());
		preparedStatement.setString(21,network.getIspPhone());
		preparedStatement.setString(22,network.getFirewallVendor());
		preparedStatement.setString(23,network.getProxyVendor());
		preparedStatement.setString(24,network.getFilteringVendor());
		if(!network.getConType().equals(""))
			preparedStatement.setString(25,network.getConType());
		else
			preparedStatement.setString(25,"--");
		
		if(!network.getConSpeed().equals(""))
			preparedStatement.setString(26,network.getConSpeed());
		else
			preparedStatement.setString(26,"--");
		if(!network.getConCount().equals(""))
			preparedStatement.setString(27,network.getConCount());
		else
			preparedStatement.setString(27,"--");
		preparedStatement.setString(28,network.getNetworkNotes());
		
		
		int flag = preparedStatement.executeUpdate();
		if (flag == 0)
			logger.info("Network record not updated for " + this.surveyId);
		return flag;
	}

	public int insertSiteSurveyWorkstationTable(
			PreparedStatement preparedStatement) throws SQLException {
		//insert into site_survey_workstation sw (sw.site_survey_id, sw.workstation_count,sw.workstation_type,sw.operating_system,sw.processor,sw.physical_memory) values (?, ?, ?, ?, ?, ?)
			preparedStatement.setInt(1, this.surveyId);
		if (!workStation.getNumberOfWorkstation().equals(""))
			preparedStatement.setString(2, workStation.getNumberOfWorkstation());
		else
			preparedStatement.setString(2, "0");		
		preparedStatement.setString(3, workStation.getTypeOfWorkstation());
		
		if (!workStation.getOperatingSystem().equals(""))
			preparedStatement.setString(4, workStation.getOperatingSystem());
		else
			preparedStatement.setString(4, "--");
		if (!workStation.getProcessorSpeed().equals(""))
		preparedStatement.setString(5, workStation.getProcessorSpeed());
		else
			preparedStatement.setString(5, "--");
		if (!workStation.getMemory().equals(""))
		    preparedStatement.setString(6, workStation.getMemory());
		else
			preparedStatement.setString(6, "--");
			int flag = preparedStatement.executeUpdate();
		if (flag == 0)
			logger.info("Workstation record not updated for " + this.surveyId
					+ " There is no workstation for this id");
		return flag;
	}

}// end of class
