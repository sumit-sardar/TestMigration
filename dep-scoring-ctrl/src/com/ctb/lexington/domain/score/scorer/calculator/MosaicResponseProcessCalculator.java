package com.ctb.lexington.domain.score.scorer.calculator;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.naming.NamingException;

import com.ctb.exception.CTBBusinessException;
import com.ctb.lexington.data.ItemVO;
import com.ctb.lexington.db.data.ScoringInvokeRecord;
import com.ctb.lexington.db.irsdata.irstsdata.IrsTASCItemFactData;
import com.ctb.lexington.db.mapper.TestRosterMapper;
import com.ctb.lexington.domain.score.event.FTPointEvent;
import com.ctb.lexington.domain.score.event.FTResponseReceivedEvent;
import com.ctb.lexington.domain.score.event.MosaicErrorHandleEvent;
import com.ctb.lexington.domain.score.event.ResponseReceivedEvent;
import com.ctb.lexington.domain.score.event.FTResponseReceivedEvent.ChildDASItems;
import com.ctb.lexington.domain.score.event.common.Channel;
import com.ctb.lexington.domain.score.scorer.Scorer;
import com.ctb.lexington.util.MosaicAuthentication;
import com.ctb.lexington.util.MosaicConstantUtils;
import com.ctb.lexington.util.MosaicCustomSerializer;
import com.ctb.lexington.util.MosaicCustomStrategy;
import com.ctb.lexington.util.SafeHashMap;
import com.ctb.lexington.util.jsonobject.Answer;
import com.ctb.lexington.util.jsonobject.ScoreData;
import com.ctb.lexington.util.jsonobject.ScoreResponse;
import com.ctb.lexington.util.mosaicobject.CandidateItemResponse;
import com.ctb.lexington.util.mosaicobject.MSSAuditLogVO;
import com.ctb.lexington.util.mosaicobject.MSSRequestResponse;
import com.ctb.lexington.util.mosaicobject.MSSResponseWrapper;
import com.ctb.lexington.util.mosaicobject.MosaicScoringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ibatis.sqlmap.client.SqlMapClient;

public class MosaicResponseProcessCalculator extends AbstractResponseCalculator{
	
	private final Map<String,MSSResponseWrapper> erroneousCollection = new SafeHashMap(String.class, MSSResponseWrapper.class);
	private final List<MSSAuditLogVO> mssWsAuditList = new ArrayList<MSSAuditLogVO>();
	
	public MosaicResponseProcessCalculator(Channel channel, Scorer scorer) {
		super(channel, scorer);
		
		channel.subscribe(this, MosaicErrorHandleEvent.class);
		mustPrecede(FTResponseReceivedEvent.class, MosaicErrorHandleEvent.class);
	}

	public void onEvent(ResponseReceivedEvent event) {
		// Do when operation TE Item scoring implementation needed
	}
	
	public void onEvent(FTResponseReceivedEvent event) {
		validateItemSetId(event.getItemSetId());
		final String itemId = event.getItemId();
		if (ItemVO.ITEM_TYPE_IN.equals(sicEvent.getType(itemId))) {
			try {
				processScore(event);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void onEvent(MosaicErrorHandleEvent event) {
		
		if(erroneousCollection != null && !erroneousCollection.isEmpty()){
			StringBuilder itemIds = new StringBuilder();
			StringBuilder messages = new StringBuilder();
			for(String itemId : erroneousCollection.keySet()){
				MSSResponseWrapper erroResponse = erroneousCollection.get(itemId);
				String errorMsg = erroResponse.getErrorMessage();
				itemIds.append(itemId).append(",");
				messages.append(itemId).append(":").append(errorMsg).append(",");
			}
			if(!itemIds.toString().isEmpty()&& !messages.toString().isEmpty()){
				if(event.isRetryFTProcess()){ // In case of Retry Failed
					updateMosaicErrorRecord(event, itemIds.toString(), messages.toString());
				} else{
					insertMosaicErrorRecord(event, itemIds.toString(), messages.toString());
				}
				
			}
		} else { // In case of Successful retry
			if(event.isRetryFTProcess()){
				updateMosaicErrorRecordSuccess(event);
			}
		}
		
		// Delete from TMP_MSS_ERROR_LOG table
		if(event.isRetryFTProcess()){
			deleteMosaicErrorRecordTemp(event);
		}
		
		System.out.println("!!!Started Writing Audit Log!!!");
		if(mssWsAuditList != null && !mssWsAuditList.isEmpty()){
			for(int i=0;i<mssWsAuditList.size();i++) {
				mssWsAuditList.get(i).setRosterId(event.getTestRosterId());
				mssWsAuditList.get(i).setStudentId(event.getStudentId());
				mssWsAuditList.get(i).setSessionId(event.getSessionId());
				mssWsAuditList.get(i).setRetryProcess((event.isRetryFTProcess()?"Yes":"No"));
				mssWsAuditList.get(i).setRetryErrorLogId(event.getInvokeKey());
			}
			
			insertMSSAuditLog();
		}
		System.out.println("!!!Audit Log Writing completed!!!");
	}
	
	public void processScore(FTResponseReceivedEvent event) throws Exception{
		
		List<ChildDASItems> childitems = null;
		if(!event.getChildItems().isEmpty()){
			childitems = event.getChildItems();
		}
		
		// Generate requested time in UTC Time-zone
		String timeStampUTC = MosaicConstantUtils.getUTCTimezone();
		
		//: Required object initialization
		MosaicScoringRequest mssRequest = new MosaicScoringRequest(
				MosaicConstantUtils.ITEM_RESPONSE_SOURCE, 
				MosaicConstantUtils.ITEM_SOURCE,
				event.getDasItemId(),
				MosaicConstantUtils.ITEM_BANK_ID,
				timeStampUTC);
		MSSRequestResponse mssRequestResponse = new MSSRequestResponse(mssRequest);
		String studentResponse = null;		
		
		
	    //: Parse student response from the given response clob xml
	    studentResponse = MosaicConstantUtils.parseResponse(MosaicConstantUtils.clobToString(event.getTeItemResponse()));
	    
	    	//: Process actual request JSON creation 
	    if(studentResponse != null && !studentResponse.isEmpty()){
	    	Map<String, Integer> itemOrderMap = new SafeHashMap(String.class, Integer.class);
	    	
	    	Map<String, String> requests = processStudentResponse(childitems, studentResponse, mssRequestResponse, itemOrderMap);
	    	
	    	if(!requests.isEmpty()){
	    		if(childitems!= null){
		    		List<CandidateItemResponse> candidateResponseList = new ArrayList<CandidateItemResponse>();
			    	Iterator<String> it = requests.keySet().iterator();
			    	while(it.hasNext()){
			    		String DASitemId = it.next();
			    		//: get response from Mosaic web service for child item of a compound item
			    		candidateResponseList.add(getMosaicResponse(event, requests.get(DASitemId), timeStampUTC, itemOrderMap.get(DASitemId), DASitemId));
			    	}
			    	
			    	if(!candidateResponseList.isEmpty()){
			    		mssRequestResponse.getMosaicScoringRequest().setItemId(event.getDasItemId());
			    		mssRequestResponse.getMosaicScoringRequest().setCandidateItemResponseObj(candidateResponseList);
			    		String parentRequest = convetObjectToJson(mssRequestResponse,MosaicConstantUtils.PARENT_DAS_INTERACTION);
			    		//: get response from Mosaic web service for parent item of a compound item
			    		processMosaicResponse(event, parentRequest, timeStampUTC);
			    	}
		    	} else {
		    		//: get response from Mosaic web service for simple item
		    		processMosaicResponse(event, requests.get(event.getDasItemId()), timeStampUTC);
		    	}
	    	}
	    }
	}
	
	private CandidateItemResponse getMosaicResponse(FTResponseReceivedEvent event, String processedrequset, String timeStampUTC, int childItemOrder, String DASitemId) {

		//: Authenticate response now from MSS.
    	MosaicAuthentication mssAuthentication = new MosaicAuthentication();
    	
    	Timestamp reqTimestamp=new Timestamp(new GregorianCalendar(TimeZone.getTimeZone("UTC")).getTimeInMillis());
    	
    	String mssResponse = mssAuthentication.authenticateResponseFromMSS(processedrequset , timeStampUTC);

    	// Add to MSS WS Audit Log list
    	Timestamp respTimestamp=new Timestamp(new GregorianCalendar(TimeZone.getTimeZone("UTC")).getTimeInMillis());
    	mssWsAuditList.add(new MSSAuditLogVO(reqTimestamp,respTimestamp,event.getItemId(),DASitemId,processedrequset,mssResponse));
    	
    	//: process if response is processed successfully.
		if (mssResponse != null && !mssResponse.isEmpty()) {
			
			MSSResponseWrapper mssResponseBody = new Gson().fromJson(mssResponse, MSSResponseWrapper.class);
			System.out.println("***onEvent(FTResponseReceivedEvent)*** Mosaic response for item : "+event.getItemId()+" : child DAS Item : "+DASitemId+" :: "+ mssResponse);
			//: scoring is done and score is received from MSS
			if (mssResponseBody.getResponseCode() == null) {
				final Integer obtained = computeMosaicPointsObtained(event, mssResponseBody);
				
				CandidateItemResponse response = new CandidateItemResponse();
				response.setItemOrder(String.valueOf(childItemOrder));
				response.setItemScore(obtained.toString());
				return response;
			} else {
				storeErroneousData(event.getItemId().concat(event.getDasItemId()), mssResponseBody);
			}
		} else {
			storeErroneousData(event.getItemId().concat(event.getDasItemId()), new MSSResponseWrapper());
		}
		return null;
	}

	
	private void processMosaicResponse(FTResponseReceivedEvent event, String processedrequset, String timeStampUTC) {

		//: Authenticate response now from MSS.
    	MosaicAuthentication mssAuthentication = new MosaicAuthentication();
    	
    	Timestamp reqTimestamp=new Timestamp(new GregorianCalendar(TimeZone.getTimeZone("UTC")).getTimeInMillis());
    	
    	String mssResponse = mssAuthentication.authenticateResponseFromMSS(processedrequset , timeStampUTC);
    	
    	// Add to MSS WS Audit Log list
    	Timestamp respTimestamp=new Timestamp(new GregorianCalendar(TimeZone.getTimeZone("UTC")).getTimeInMillis());
    	mssWsAuditList.add(new MSSAuditLogVO(reqTimestamp,respTimestamp,event.getItemId(),event.getDasItemId(),processedrequset,mssResponse));
    	
    	//: process if response is processed successfully.
		if (mssResponse != null && !mssResponse.isEmpty()) {
			
			MSSResponseWrapper mssResponseBody = new Gson().fromJson(mssResponse, MSSResponseWrapper.class);
			System.out.println("***onEvent(FTResponseReceivedEvent)*** Mosaic response for item : "+event.getItemId()+" : DAS Item : "+event.getDasItemId()+" :: "+ mssResponse);
			//: scoring is done and score is received from MSS
			if (mssResponseBody.getResponseCode() == null) {
				final Integer attempted = computeMosaicPointsAttempted(event, mssResponseBody);
				final Integer obtained = computeMosaicPointsObtained(event, mssResponseBody);
				channel.send(new FTPointEvent(event.getTestRosterId(), event
						.getItemId(), event.getItemSetId(), attempted, obtained));
			} else {
				final Integer attempted = sicEvent.getMaxPoints(event.getItemId());
				storeErroneousData(event.getItemId(), mssResponseBody);
				channel.send(new FTPointEvent(event.getTestRosterId(), event
						.getItemId(), event.getItemSetId(), attempted, null));
			}
		} else {
			storeErroneousData(event.getItemId().concat(event.getDasItemId()), new MSSResponseWrapper());
		}
	}
	
	
	/**
	 * Process the student response to create a MSS request JSON object
	 * @param studentResponse
	 * @param mssRequestResponse
	 * @return Map<String, String>
	 */
	private Map<String, String>  processStudentResponse(List<ChildDASItems> childitems, String studentResponse,
			MSSRequestResponse mssRequestResponse, Map<String, Integer> itemOrderMap)
			throws CTBBusinessException {

		Map<String, String> responseMap = new HashMap<String, String>();
		
		//: Convert student response from JSON String to object
    	ScoreResponse scoreResponseObject = null;
    	scoreResponseObject = new Gson().fromJson(studentResponse, ScoreResponse.class);
    	
    	if(scoreResponseObject != null){
    		
	    	List<List<ScoreData>> outerList = scoreResponseObject.getJsonContent().getScoring();
	    	for(List<ScoreData> innerList: outerList){
	    		for(ScoreData scoreJson: innerList){ // DAS child item List
	    			
	    			//: Initialize
	    			List<CandidateItemResponse> candidateResponseList = new ArrayList<CandidateItemResponse>();
	    			int orderCounter = 0;
	    			String interactionType = scoreJson.getInteractionType();
	    			List<Answer> answerList = scoreJson.getAnswered();
	    			
	    			//: get child item Id if available
	    			if(childitems != null){
	    				for(ChildDASItems child: childitems){
	    					if(scoreJson.getSubquestionIndex().intValue()==child.getItemOrder().intValue()){
	    						mssRequestResponse.getMosaicScoringRequest().setItemId(child.getChildDasItemId());
	    						itemOrderMap.put(child.getChildDasItemId(), child.getItemOrder());
	    					}
	    				}
	    			}
	    			
	    			if(!answerList.isEmpty()){
	    				
	    				/**
		    			 * Sort the collection of Answer object by drop area name(ascending order)
		    			 */
		    			/*Collections.sort(answerList, new Comparator<Answer>() {
							@Override
							public int compare(Answer o1, Answer o2) {
								if(o1.getDroparea() != null && o2.getDroparea() != null){
									return o1.getDroparea().getName().compareTo(o2.getDroparea().getName());
								}else return 0;
							}
		    				
						});*/
	    				
		    			for(Answer answer: answerList){
		    				if(answer.getDroparea() != null){
		    					
		    					if(answer.getDragarea() != null && !answer.getDragarea().isEmpty()){
		    						//: Generate DND, DND-DND, DNDPR type item response structure
		    						orderCounter = MosaicConstantUtils.processDNDTypeResponse(answer,candidateResponseList, interactionType, orderCounter);
		    					}
		    				}
		    				else{
		    					//: Generate MSR, MCQ, MCQ-MSR type item response structure
		    					orderCounter = MosaicConstantUtils.processMSRTypeResponse(answer,candidateResponseList, interactionType, orderCounter);
		    				}
		    			}
		    			mssRequestResponse.getMosaicScoringRequest().setCandidateItemResponseObj(candidateResponseList);
		    			
		    			//: Prepare the MSS final request JSON object from object and return as String
		    			responseMap.put(mssRequestResponse.getMosaicScoringRequest().getItemId(),
								convetObjectToJson(mssRequestResponse,scoreJson.getInteractionType()));
	    			}
	    		}
	    	}
    	}
    	return responseMap;
	}
	
	/**
	 * calculate points obtained from mosaic response
	 * @param event
	 * @param response
	 * @return
	 */
	private Integer computeMosaicPointsObtained(FTResponseReceivedEvent event,
			MSSResponseWrapper response) {

		Integer pointsObtained = event.getPointsObtained();
		if (pointsObtained == null) {
			if (response != null) {
				String score = response.getMosaicScoringResponse()
						.getItemScore();
				Double value = (score != null && !score.isEmpty()) ? Double
						.parseDouble(score) : sicEvent.getMinPoints(event
						.getItemId());
				pointsObtained = Integer.valueOf(value.intValue());
			} else {
				pointsObtained = sicEvent.getMinPoints(event.getItemId());
			}
		}
		return pointsObtained;
	}

	/**
	 * calculate points attempted from mosaic response
	 * @param event
	 * @param response
	 * @return
	 */
	private Integer computeMosaicPointsAttempted(FTResponseReceivedEvent event,
			MSSResponseWrapper response) {
		
		Integer pointsAttempted = new Integer(0);
		if (response != null) {
			pointsAttempted = (!response.getMosaicScoringResponse()
					.getItemScore().isEmpty()) ? sicEvent.getMaxPoints(event
					.getItemId()) : pointsAttempted;

		}
		return pointsAttempted;
	}
	
	/**
	 * Generate JSON from Object
	 * @param mssRequest
	 * @param interactionType
	 * @return String - MSS request JSON structure
	 */
	private String convetObjectToJson(MSSRequestResponse mssRequest, final String interactionType) throws CTBBusinessException{
		Gson gson = new GsonBuilder()
				.registerTypeAdapter(MSSRequestResponse.class,new MosaicCustomSerializer())
				.setExclusionStrategies(new MosaicCustomStrategy(interactionType))
				.serializeNulls().create();
		String json = gson.toJson(mssRequest);
		System.out.println("****onEvent(FTResponseReceivedEvent) ******* Request Mosaic JSON :: "+ json);
		return json;
	}

	private void storeErroneousData(String oasItemId, MSSResponseWrapper mssResponseBody){
		if(!erroneousCollection.containsKey(oasItemId)){
			erroneousCollection.put(oasItemId, mssResponseBody);
		}
	}
	
	/**
	 * insert error record with the invoke count 0 in scoring invoke log table
	 * @param event
	 * @param string
	 * @param string2
	 */
	private void insertMosaicErrorRecord(MosaicErrorHandleEvent event, String itemIds, String messages) {
		
		String additionInfo = itemIds.substring(0, itemIds.lastIndexOf(","));
		String message = messages.substring(0, messages.lastIndexOf(","));
		ScoringInvokeRecord record = new ScoringInvokeRecord(event.getTestRosterId(),
				event.getStudentId(), event.getSessionId(), MosaicConstantUtils.INVOKE_STATUS_IP, new Integer(0), scorer
						.getClass().getSimpleName(), additionInfo, message, null);
		final Connection conn = getOASConnection();
        try {
        	new TestRosterMapper(conn).insertMosaicErrorRecord(record);
        } finally {
            closeConnection(conn);
        }
	}
	
	/**
	 * update error record against the invoke key in scoring invoke log table up to invoke count 5
	 */
	private void updateMosaicErrorRecord(MosaicErrorHandleEvent event, String itemIds, String messages) {
		
		String additionInfo = itemIds.substring(0, itemIds.lastIndexOf(","));
		String message = messages.substring(0, messages.lastIndexOf(","));
		ScoringInvokeRecord record = new ScoringInvokeRecord(event.getTestRosterId(),
				event.getStudentId(), event.getSessionId(), MosaicConstantUtils.INVOKE_STATUS_IP, null, null, additionInfo, message, event.getInvokeKey());
		final Connection conn = getOASConnection();
        try {
        	new TestRosterMapper(conn).updateMosaicErrorRecord(record);
        } finally {
            closeConnection(conn);
        }
	}
	
	/**
	 * update error record against the invoke key in scoring invoke log table to success
	 */
	private void updateMosaicErrorRecordSuccess(MosaicErrorHandleEvent event) {
		
		ScoringInvokeRecord record = new ScoringInvokeRecord(event.getTestRosterId(),
				event.getStudentId(), event.getSessionId(), MosaicConstantUtils.INVOKE_STATUS_SUCCESS, null, null, null, null, event.getInvokeKey());
		final Connection conn = getOASConnection();
        try {
        	new TestRosterMapper(conn).updateMosaicErrorRecordSuccess(record);
        } finally {
            closeConnection(conn);
        }
	}
	
	/**
	 * Delete error record from TMP_MSS_ERROR_LOG table
	 */
	private void deleteMosaicErrorRecordTemp(MosaicErrorHandleEvent event) {
		PreparedStatement pst = null;
		Connection con = null;
		try {
			con = getOASConnection();
			pst  = con.prepareStatement("DELETE FROM TMP_MSS_ERROR_LOG WHERE INVOKE_LOG_KEY = ?");
			pst.setLong(1, event.getInvokeKey());
			pst.execute();
		} catch (Exception e) {
			System.err.println("Error in the tmp_mss_error_log deletion");
			e.printStackTrace();
		} finally {
			closeConnection(con);
		}
	}
	
	/**
	 * insert audit log data
	 */
	private void insertMSSAuditLog() {
		PreparedStatement pst = null;
		Connection con = null;
		try {
			con = getOASConnection();
			pst = con.prepareStatement("INSERT INTO MSS_WS_AUDIT_LOG " +
		  "(INVOKE_ID, " +
		  "ROSTER_ID, " +
		  "STUDENT_ID, " +
		  "SESSION_ID, " +
		  "WS_INVOKE_TIMESTAMP, " +
		  "WS_RESPONSE_TIMESTAMP, " +
		  "RETRY_PROCESS, " +
		  "RETRY_ERROR_LOG_ID, " +
		  "OAS_ITEM_ID, " +
		  "DAS_ITEM_ID, " +
		  "WS_REQUEST, " +
		  "WS_RESPONSE) " +
		"VALUES " +
		  "(SEQ_MSS_WS_AUDIT_LOG_ID.nextval,?,?,?,?,?,?,?,?,?,?,?)");
			
			for (MSSAuditLogVO logdata : mssWsAuditList) {
				pst.setLong(1, logdata.getRosterId());
				pst.setLong(2, logdata.getStudentId());
				pst.setLong(3, logdata.getSessionId());
				pst.setTimestamp(4, logdata.getWsInvokeTimestamp());
				pst.setTimestamp(5, logdata.getWsResponseTimestamp());
				pst.setString(6, logdata.getRetryProcess());
				if(logdata.getRetryErrorLogId()!=null) {
					pst.setLong(7, logdata.getRetryErrorLogId());
				} else {
					pst.setObject(7, null);
				}
				pst.setString(8, logdata.getOasItemId());
				pst.setString(9, logdata.getDasItemId());
				pst.setString(10, logdata.getWsRequest());
				pst.setString(11, logdata.getWsResponse());
				
				pst.addBatch();
			}
			
			pst.executeBatch();
			
		} catch (Exception e) {
			System.err.println("Error in the audit log insertion ");
			e.printStackTrace();
		} finally {
			closeConnection(con);
		}
	}
	
	protected Connection getOASConnection() {
        try {
            return scorer.getOASConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

    protected void closeConnection(Connection connection) {
        try {
            scorer.close(true, connection);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
	
}