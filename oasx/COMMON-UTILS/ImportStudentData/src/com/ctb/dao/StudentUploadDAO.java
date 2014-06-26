package com.ctb.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;


public class StudentUploadDAO implements IStudentUploadDAO {

	public Integer getOrgNodeIdFromSeq(Integer totalNode, String seqName) {
		// TODO Auto-generated method stub

		int sequenceNumber=0;
		Connection con=null;
		CallableStatement cstmt=null;
		ResultSet rs=null;
		
		try {
			con.setAutoCommit(false);
			String seqSQL="? = call SEQGENERATOR(?,?)";
			cstmt=con.prepareCall(seqSQL);
			cstmt.registerOutParameter(1, oracle.jdbc.OracleTypes.NUMBER);
			cstmt.setString(2, seqName);
			cstmt.setInt(3, totalNode.intValue());
			
			cstmt.executeQuery();
			
			sequenceNumber=new Integer(cstmt.getInt(1));
			
			return sequenceNumber;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sequenceNumber;
			
	}
	
}
