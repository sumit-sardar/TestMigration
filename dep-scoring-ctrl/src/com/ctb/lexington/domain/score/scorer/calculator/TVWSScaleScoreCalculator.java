package com.ctb.lexington.domain.score.scorer.calculator;

import gscode.GridSearch;
import gscode.Item;
import gscode.Score;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TVWSScaleScoreCalculator {
	
	double loss = 260;
	double hoss = 650;
	double threshold = 0.8;
	int numGridPoints = 89;
	
	@SuppressWarnings("unchecked")
	public BigDecimal getTVWSScaleScoreCalculator(String pTestLevel, String contentAreaName, Integer productId,
			LinkedHashMap<String,LinkedHashMap<String,String>> contentAreaResponse, Connection irsCon) {
		
		String fileName = getFileNameForPar(pTestLevel, contentAreaName, productId, irsCon);
		String parFile = "";
		String genResponseString = generateResponseString(contentAreaResponse, contentAreaName);
		/**
		 * The below path given is the relative path, which will not be able to read if we make ear of the file.
		 * It is because this java file is present inside dep-scoring-ctrl, which in turn becomes a jar file.
		 * So, it will not be able to get the proper path of the file. Hence while checking in, uncomment
		 * the relative path and comment the absolute path.
		 */
		String path = getClass().getProtectionDomain().getCodeSource().getLocation().getPath()+File.separator+"..";
		//String path = "C:\\WorkspaceLocal94\\application-scoring\\EarContent\\APP-INF\\lib";
		//The parFile will remain as it is.
		parFile = path+File.separator+"tvWsScoringFiles/"+fileName;
		BigDecimal scaleScore = null;
		try {
			System.out.println("parFile:"+parFile);
			List<Item> items = Item.load(parFile);
			if(items.size() > contentAreaResponse.get(contentAreaName).size()) {
				scaleScore = null;
			} else {
				GridSearch g = new GridSearch(items,loss,hoss,numGridPoints);
				int scale_score = score_dat(g,items,1.0, genResponseString);
				scaleScore = new BigDecimal(scale_score);
			}
			return scaleScore;
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return new BigDecimal(0);
	}
	
	private static int score_dat(GridSearch g, List<Item> items, double tolerance, String responseString) throws Exception {
		int numItems = items.size();
		int[] item_responses = new int[numItems] ;
		BufferedReader r = null ;
		BufferedWriter out = null, err = null ;
		Score result = new Score() ;
		try {
			String response_string = responseString;
			char[] responses = response_string.toCharArray() ;
			for(int i=0;i<numItems;i++) {
				if( responses[i] == 'F' ) {
					item_responses[i] = Item.LIKELIHOOD_IGNORE_RESPONSE ;
				} else {
					item_responses[i] = responses[i]-'0';
				}
			}
			g.score(item_responses,GridSearch.SCORE_TIEBREAK_HI,result) ;

		}catch(Exception e) {
			e.printStackTrace() ;
		}finally{
			if( r != null ) r.close() ;
			if( out != null ) {
				out.flush() ;
				out.close() ;
			}
			if( err != null ) {
				err.flush() ;
				err.close() ;
			}
		}
		return (int)result.score;
	}
	
	private String getFileNameForPar(String pTestLevel, String contentAreaName, Integer productId, Connection con) {
		
		String parFileName = "";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String sqlQuery = "SELECT FILE_NAME FROM IRT_SCORE_LOOKUP_FILES WHERE CONTENT_AREA = ? AND TEST_LEVEL = ? AND PRODUCT_ID = ?";
			ps = con.prepareStatement(sqlQuery);
			ps.setString(1, contentAreaName);
			ps.setString(2, pTestLevel);
			ps.setString(3, productId.toString());
			rs = ps.executeQuery();
			while(rs.next()) {
				parFileName = rs.getString("FILE_NAME");
			}
		} catch (SQLException se) {
			throw new RuntimeException(se);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if(con != null) {
					con.close();
				}
				if(ps != null) {
					ps.close();
				}
				if(rs != null) {
					rs.close();
				}
			} catch(Exception e) {
				throw new RuntimeException(e);
			}
		}
		
		return parFileName;		
	}
	
	private String generateResponseString(LinkedHashMap<String,LinkedHashMap<String,String>> contentAreaResponse, String contentAreaName) {
		String responseString = "";
		LinkedHashMap<String,String> itemResponses = contentAreaResponse.get(contentAreaName);
		for(Map.Entry<String, String> entry : itemResponses.entrySet()) {
			responseString = responseString + entry.getValue();
		}
		return responseString;
	}
	
	//Retrieve the loss and hoss value
	public void getLossHossValue (Long subtestId, String contentAreaName, String pTestLevel, Connection con) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sqlQuery = "SELECT MIN(SL.SOURCE_SCORE_VALUE) AS MINSRCVAL, MIN(SL.DEST_SCORE_VALUE) AS MINDESTVAL, MAX(SL.SOURCE_SCORE_VALUE) AS MAXSRCVAL, MAX(SL.DEST_SCORE_VALUE) AS MAXDESTVAL " +
				"FROM SCORE_LOOKUP SL, SCORE_LOOKUP_ITEM_SET SLIS " +
				"WHERE SL.SCORE_LOOKUP_ID = SLIS.SCORE_LOOKUP_ID AND SLIS.ITEM_SET_ID = ? AND SL.SOURCE_SCORE_TYPE_CODE = 'NSC' AND SL.DEST_SCORE_TYPE_CODE = 'SCL' AND SL.CONTENT_AREA = ? AND SL.TEST_LEVEL = ?";
		try {
			ps = con.prepareStatement(sqlQuery);
			ps.setInt(1, subtestId.intValue());
			ps.setString(2, contentAreaName);
			ps.setString(3, pTestLevel);
			rs = ps.executeQuery();
			Double lossVal = null;
			Double hossVal = null;
			while (rs.next()) {
				lossVal = new Double(rs.getInt("MINDESTVAL"));
				hossVal = new Double(rs.getInt("MAXDESTVAL"));
			}
			if(lossVal != null && lossVal != 0) {
				loss = lossVal;
			}
			if(hossVal != null && hossVal != 0) {
				hoss = hossVal;
			}
			System.out.println("loss -> " + loss);
			System.out.println("hoss -> " + hoss);
		} catch (SQLException se) {
			throw new RuntimeException(se);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
