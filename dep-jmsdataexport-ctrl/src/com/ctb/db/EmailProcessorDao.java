package com.ctb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ctb.bean.testAdmin.CustomerEmail;
import com.ctb.bean.testAdmin.User;
import com.ctb.exception.CTBBusinessException;
import com.ctb.utils.SqlUtil;

public class EmailProcessorDao implements EmailProcessorSQL {

	public CustomerEmail getCustomerEmailByUserName(String userName,
			Object emailType) throws CTBBusinessException {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		CustomerEmail customerEmail = null;
		try {
			con = SqlUtil.openOASDBcon();
			ps = con.prepareStatement(GET_CUSTOMER_EMAIL_BY_USERNAME);
			ps.setString(1, userName);
			ps.setInt(2, 4);
			rs = ps.executeQuery();
			rs.setFetchSize(500);
			if (rs.next()) {
				customerEmail = new CustomerEmail();
				customerEmail.setCustomerId(rs.getInt(1));
				customerEmail.setEmailType(rs.getInt(2));
				customerEmail.setReplyTo(rs.getString(3));
				customerEmail.setSubject(rs.getString(4));
				customerEmail.setEmailBody(rs.getClob(5));
			}
			con.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CTBBusinessException("EmailProcessorDao:getCustomerEmailByUserName failed");
			
		} finally {
			SqlUtil.close(con, ps, rs);
		}
		return customerEmail;
	}

	public User getUserDetails(String userName) throws CTBBusinessException {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		User user = null;
		try {
			con = SqlUtil.openOASDBcon();
			ps = con.prepareStatement(GET_USER_DETAIL_BY_USER_NAME);
			ps.setString(1, userName);
			rs = ps.executeQuery();
			rs.setFetchSize(500);
			if (rs.next()) {
				user = new User();
				user.setUserId(rs.getInt(1));
				user.setUserName(rs.getString(2));
				user.setEmail(rs.getString(3));
			}
			con.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CTBBusinessException("EmailProcessorDao:getUserDetails failed");
		} finally {
			SqlUtil.close(con, ps, rs);
		}
		return user;

	}

	public String getCustomerEmailByUserName(String userName) throws CTBBusinessException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String customerEmail = null;
		try {
			con = SqlUtil.openOASDBcon();
			ps = con.prepareStatement(GET_CUSTOMER_EMAIL_BY_USER_NAME);
			ps.setString(1, userName);
			rs = ps.executeQuery();
			if (rs.next()) { 
				customerEmail = rs.getString(1);
			}
		}catch (SQLException e) {
			e.printStackTrace();
			throw new CTBBusinessException("EmailProcessorDao:getCustomerEmailByUserName failed");
		} finally {
			SqlUtil.close(con, ps, rs);
		}
		return customerEmail;
		
	}
	
	public boolean checkForLaslinkCustomer(String userName) throws CTBBusinessException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isLaslinkCustomer = false;
		try {
			con = SqlUtil.openOASDBcon();
			ps = con.prepareStatement(CHECK_FOR_LASLINK_CUSTOMER);
			ps.setString(1, userName);
			rs = ps.executeQuery();
			if (rs.next()) { 
				isLaslinkCustomer = new Boolean(rs.getString(1));
			}
		}catch (SQLException e) {
			e.printStackTrace();
			throw new CTBBusinessException("EmailProcessorDao:getCustomerEmailByUserName failed");
		} finally {
			SqlUtil.close(con, ps, rs);
		}
		return isLaslinkCustomer;
		
	}
	
	public User getUserDetails(Integer userId) throws CTBBusinessException {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		User user = null;
		try {
			con = SqlUtil.openOASDBcon();
			ps = con.prepareStatement(GET_USER_DETAIL_BY_USER_ID);
			ps.setInt(1, userId);
			rs = ps.executeQuery();
			rs.setFetchSize(500);
			if (rs.next()) {
				user = new User();
				user.setUserId(rs.getInt(1));
				user.setUserName(rs.getString(2));
				user.setEmail(rs.getString(3));
			}
			con.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CTBBusinessException("EmailProcessorDao:getUserDetails failed");
		} finally {
			SqlUtil.close(con, ps, rs);
		}
		return user;

	}

}
