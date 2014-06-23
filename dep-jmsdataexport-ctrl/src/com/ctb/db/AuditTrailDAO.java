package com.ctb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import com.ctb.exception.CTBBusinessException;
import com.ctb.utils.Configuration;
import com.ctb.utils.SqlUtil;

public class AuditTrailDAO implements AuditTrailSQL {

	public void updateIntermideateAuditTrail( int jobStatus ,String message , int jobId) {

		Connection con = null;
		PreparedStatement ps = null;
		//int recordCount = 0;
		// CustomerEmail customerEmail = null;
		try {
			con = SqlUtil.openOASDBcon();
			ps = con.prepareStatement(UPDATE_DATAEXPORT_INTERMIDEATE_JOB_STATUS);
			ps.setInt(1, jobStatus);
			ps.setString(2, message);
			ps.setInt(3, jobId);

			ps.executeUpdate();
			con.commit();

		} catch (Exception e) {
			System.out.println("Audittrail updation failed:"+e.getMessage());
			e.printStackTrace();
			//throw new CTBBusinessException("getCustomerEmailByUserName:failed");

		} finally {
			SqlUtil.close(con, ps);
		}

	}

	public void updateAuditTrail( int finalJobStatus, int intirmJobStatus ,String message , int jobId) {

		Connection con = null;
		PreparedStatement ps = null;
		//int recordCount = 0;
		// CustomerEmail customerEmail = null;
		try {
			con = SqlUtil.openOASDBcon();
			ps = con.prepareStatement(UPDATE_DATAEXPORT_JOB_STATUS);
			ps.setInt(1, finalJobStatus);
			ps.setInt(2, intirmJobStatus);
			ps.setString(3, message);
			ps.setInt(4, jobId);
			ps.executeUpdate();
			con.commit();
		} catch (Exception e) {
			System.out.println("Audittrail updation failed:"+e.getMessage());
			e.printStackTrace();
			//throw new CTBBusinessException("getCustomerEmailByUserName:failed");

		} finally {
			SqlUtil.close(con, ps);
		}

	}
	
	
	public void updateTestRoster(List<String> allRosterlist) throws CTBBusinessException {

		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = SqlUtil.openOASDBcon();
			for (String rosterlist : allRosterlist) {
				try {
					String UPDATE_TEST_ROSTER1 = UPDATE_TEST_ROSTER.replace(
							"<<ROSTER_ID_LIST>>", rosterlist);
					ps = con.prepareStatement(UPDATE_TEST_ROSTER1);
					ps.executeUpdate();
				} finally {
					SqlUtil.close(ps);
				}

			}
		   con.commit();
		} catch (Exception e) {
			System.out.println("Testroster updation failed:" + e.getMessage());
			e.printStackTrace();
			throw new CTBBusinessException("updateTestRoster:Testroster updation failed.");
		} finally {
			SqlUtil.close(con);
		}

	}
	
	public boolean isENGRADEcustomer(Integer customerId) throws CTBBusinessException {
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String customerConfig = Configuration.getCustomerConfigurationName();
		try {
			con = SqlUtil.openOASDBcon();
			String sql = HAS_ENGRADE_CUSTOMER_CONFIGURATION;
			ps = con.prepareStatement(sql);
			ps.setInt(1, customerId);
			ps.setString(2, customerConfig);
			rs = ps.executeQuery();
			while(rs.next()){
				String value = rs.getString(1);
				if("T".equals(value)){
					return true;
				}else{
					return false;
				}
			}
			return false;
		} catch (Exception e) {
			System.out.println("Verify isENGRADEcustomer failed:" + e.getMessage());
			e.printStackTrace();
			throw new CTBBusinessException("isLAUSDcustomer:Verify isENGRADEcustomer failed.");
		} finally {
			SqlUtil.close(con,ps,rs);
		}
	}

}
