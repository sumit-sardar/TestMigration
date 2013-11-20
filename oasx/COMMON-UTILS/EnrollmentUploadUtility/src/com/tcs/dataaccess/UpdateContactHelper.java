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
			}else{
				preparedStatement.setString(2, okhlahama.getDistrictNumber());				
			}
			preparedStatement.setString(3, okhlahama.getSiteType());
			preparedStatement.setString(4, okhlahama.getTestCoordinatorFirstName());
			preparedStatement.setString(5, okhlahama.getTestCoordinatorLastName());
			preparedStatement.setString(6, okhlahama.getTestCoordinatorEmail());
			preparedStatement.setString(7, okhlahama.getTestPhoneNumber());
			preparedStatement.setString(8, okhlahama.getTechCoordinatorFirstName());
			preparedStatement.setString(9, okhlahama.getTechCoordinatorLastName());
			preparedStatement.setString(10, okhlahama.getTechCoordinatorEmail());
			preparedStatement.setString(11, okhlahama.getTechPhoneNumber());
			preparedStatement.setString(12,String.valueOf(siteSurveyId));
			if(okhlahama.getSiteType().equalsIgnoreCase("School")){
				preparedStatement.setString(13,okhlahama.getSchoolNumber());
			}else{
				preparedStatement.setString(13, okhlahama.getDistrictNumber());				
			}
			preparedStatement.setString(14, okhlahama.getSiteType());
			preparedStatement.setString(15, okhlahama.getTestCoordinatorFirstName());
			preparedStatement.setString(16, okhlahama.getTestCoordinatorLastName());
			preparedStatement.setString(17, okhlahama.getTestCoordinatorEmail());
			preparedStatement.setString(18, okhlahama.getTestPhoneNumber());
			preparedStatement.setString(19, okhlahama.getTechCoordinatorFirstName());
			preparedStatement.setString(20, okhlahama.getTechCoordinatorLastName());
			preparedStatement.setString(21, okhlahama.getTechCoordinatorEmail());
			preparedStatement.setString(22, okhlahama.getTechPhoneNumber());			
			preparedStatement.execute();
			
		} catch (SQLException e) {
		
			e.printStackTrace();
		}
		
		
	}
	
}//end of class
