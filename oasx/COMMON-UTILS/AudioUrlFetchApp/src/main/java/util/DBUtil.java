package main.java.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;






import main.java.bean.ItemResponseCR;

//import com.ctb.bean.AudioResponseData;

public class DBUtil {
	
	//private static String query = "select irc.constructed_response from item_response_cr irc where test_roster_id = ? and irc.constructed_response is not null";
	
	private static String audioUrlFromItemResponseCR = "select irc.audio_url from item_response_cr irc "+
														"where irc.test_roster_id = ? "+
														"and irc.audio_url is not null "+
														"and irc.item_id = ? " ;
	
	private static String testRosterItemData =  "SELECT ISET.ITEM_SET_NAME, "+
												 "IRC.ITEM_ID, "+
												 "IRC.AUDIO_URL "+
												 "FROM ITEM_RESPONSE_CR IRC, ITEM_SET ISET,ITEM_SET_ITEM ISI,ITEM IT "+
												 "WHERE IRC.ITEM_SET_ID=ISET.ITEM_SET_ID "+
												 "AND ISET.ITEM_SET_ID=ISI.ITEM_SET_ID "+
												 "AND IRC.ITEM_ID=ISI.ITEM_ID "+
												 "AND IRC.ITEM_ID=IT.ITEM_ID "+
												 "AND UPPER(IT.ANSWER_AREA) = UPPER('AudioItem') "+
												 "AND IRC.TEST_ROSTER_ID = ? "+
												 "ORDER BY ISI.ITEM_SORT_ORDER";
	
	/*private static String testRosterItemData = "select * from item_response_cr where test_roster_id = ?";*/
	private static Connection conn = null;
	private static PreparedStatement pstmt= null;
	private static ResultSet rs = null;
	
	public static ArrayList<ItemResponseCR> fetchItemCRList(int testRosterId) {
		// TODO Auto-generated method stub
		
		ArrayList<ItemResponseCR> itemResponseList = null;
		ItemResponseCR itemResponse = null;
		try{
			conn = SQLUtil.getConnection();
			pstmt = conn.prepareStatement(testRosterItemData);
			pstmt.setInt(1, testRosterId);
			rs = pstmt.executeQuery();
			if(rs != null){
				itemResponseList = new ArrayList<ItemResponseCR>();
				while(rs.next()){
					itemResponse = new ItemResponseCR();
					itemResponse.setItemSetID(rs.getString("ITEM_SET_NAME"));
					itemResponse.setItemId(rs.getString("ITEM_ID"));
					itemResponse.setAudioUrl(rs.getString("AUDIO_URL"));
					
					itemResponseList.add(itemResponse);
					itemResponse = null;
				}
			}
			
		} catch (SQLException ex){
			System.out.println("SQL error occurred while fetching audio responses for the roster : "+ testRosterId);
			ex.printStackTrace();
		} finally{
			SQLUtil.close(conn, pstmt, rs);
		}
		
		return itemResponseList;
	
	}
}
