package com.tcs.dataaccess;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.ctb.bean.Contects;

public class UpdateContactHelper 
{
	private static final Logger logger = Logger.getLogger(UpdateContactHelper.class);
	private PreparedStatement preparedStatement;
	
	public UpdateContactHelper(PreparedStatement preparedStatement) {
		if(preparedStatement!=null)
			this.preparedStatement=preparedStatement;
	}
	
	public UpdateContactHelper() {
		// TODO Auto-generated constructor stub
	}
	
	public void processEachRecordForOkhlahama(Integer siteSurveyId,Contects okhlahama,String year){		
		try {
			preparedStatement.setString(1, String.valueOf(siteSurveyId));
			if(okhlahama.getSiteType().equalsIgnoreCase("School")){
				preparedStatement.setString(2,okhlahama.getSchoolNumber());
				preparedStatement.setString(3, okhlahama.getSchoolName());
			}else{
				preparedStatement.setString(2, okhlahama.getDistrictNumber());
				preparedStatement.setString(3, okhlahama.getDistrictName());
			}
			preparedStatement.setString(4, okhlahama.getSiteType());
			preparedStatement.setString(5, okhlahama.getTestCoordinatorFirstName());
			preparedStatement.setString(6, okhlahama.getTestCoordinatorLastName());
			preparedStatement.setString(7, okhlahama.getTestCoordinatorEmail());
			preparedStatement.setString(8, okhlahama.getTestPhoneNumber());
			preparedStatement.setString(9, okhlahama.getTechCoordinatorFirstName());
			preparedStatement.setString(10, okhlahama.getTechCoordinatorLastName());
			preparedStatement.setString(11, okhlahama.getTechCoordinatorEmail());
			preparedStatement.setString(12, okhlahama.getTechPhoneNumber());
			preparedStatement.setString(13,String.valueOf(siteSurveyId));
			if(okhlahama.getSiteType().equalsIgnoreCase("School")){
				preparedStatement.setString(14,okhlahama.getSchoolNumber());
				preparedStatement.setString(15, okhlahama.getSchoolName());
			}else{
				preparedStatement.setString(14, okhlahama.getDistrictNumber());
				preparedStatement.setString(15, okhlahama.getDistrictName());
			}
			preparedStatement.setString(16, okhlahama.getSiteType());
			preparedStatement.setString(17, okhlahama.getTestCoordinatorFirstName());
			preparedStatement.setString(18, okhlahama.getTestCoordinatorLastName());
			preparedStatement.setString(19, okhlahama.getTestCoordinatorEmail());
			preparedStatement.setString(20, okhlahama.getTestPhoneNumber());
			preparedStatement.setString(21, okhlahama.getTechCoordinatorFirstName());
			preparedStatement.setString(22, okhlahama.getTechCoordinatorLastName());
			preparedStatement.setString(23, okhlahama.getTechCoordinatorEmail());
			preparedStatement.setString(24, okhlahama.getTechPhoneNumber());			
			preparedStatement.execute();
			
		} catch (SQLException e) {
		
			e.printStackTrace();
		}
		
		
	}
	
}//end of class
