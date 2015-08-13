package com.ctb.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ctb.utils.datamodel.Answer;
import com.ctb.utils.datamodel.ScoreData;
import com.ctb.utils.datamodel.ScoreResponseNew;
import com.ctb.utils.mssmodel.CandidateItemResponse;
import com.ctb.utils.mssmodel.MSSRequestResponse;
import com.ctb.utils.mssmodel.MosaicScoringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MSSRequestProcessor implements Runnable {

	
	private StudentResponses response;

	final private static List<MosaicRequestExcelPojo> mosaicRequestCSVList = Collections
			.synchronizedList(new ArrayList<MosaicRequestExcelPojo>());

	public MSSRequestProcessor(StudentResponses response) {
		this.response = response;
	}

	public void run() {

		try {
			System.out.println("*** MSSRequestProcessor : run : Processing for roster "+this.response.getRosterid()+" item "+this.response.getOasItemId()+" & dasItemId "+this.response.getDasItemid());
			// Generate requested time in UTC Time-zone
			String timeStampUTC = MSSConstantUtils.getUTCTimezone();

			// : Required object initialization
			MosaicScoringRequest mssRequest = new MosaicScoringRequest(
					MSSConstantUtils.ITEM_RESPONSE_SOURCE,
					MSSConstantUtils.ITEM_SOURCE, this.response.getDasItemid(),
					MSSConstantUtils.ITEM_BANK_ID, timeStampUTC);
			MSSRequestResponse mssRequestResponse = new MSSRequestResponse(
					mssRequest);
			String studentResponse = null;

			// : Parse student response from the given response xml
			studentResponse = MSSConstantUtils.parseResponse(this.response.getClobResponse());

			if (studentResponse != null) {
				// : Process actual request JSON creation
				String processedRequest = processStudentResponse(
						studentResponse, mssRequestResponse);
				
				if(processedRequest != null && !processedRequest.isEmpty()){
					prepareCollection(new MosaicRequestExcelPojo(
							mssRequest.getItemResponseSource(),
							mssRequest.getItemSource(), this.response.getDasItemid(),
							mssRequest.getItemBankId(), processedRequest,
							studentResponse, this.response.getRosterid(), this.response.getOasItemId(), this.response.getPEId()));
				}
		    }
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Process the student response to create a MSS request JSON object
	 * @param studentResponse
	 * @param mssRequestResponse
	 * @param writer 
	 * @return
	 */
	private String  processStudentResponse(String studentResponse, MSSRequestResponse mssRequestResponse) throws Exception{

		//: Convert student response from JSON String to object
    	ScoreResponseNew scoreResponseObject = null;
    	scoreResponseObject = new Gson().fromJson(studentResponse, ScoreResponseNew.class);
    	
    	if(scoreResponseObject != null){
    		
	    	List<List<ScoreData>> outerList = scoreResponseObject.getJsonContent().getScoring();
	    	for(List<ScoreData> innerList: outerList){
	    		for(int indx=0; indx <innerList.size();indx++){ // DAS child item List
	    			ScoreData scoreJson = innerList.get(indx);
	    			if(this.response.getItemOrder()!=null && !this.response.getItemOrder().isEmpty()){
	    				int order = Integer.parseInt(this.response.getItemOrder());
	    				int currentIndx = indx+1;
	    				if(order != currentIndx){
	    					continue;
	    				}
	    			}
	    			//: Initialize
	    			List<CandidateItemResponse> candidateResponseList = new ArrayList<CandidateItemResponse>();
	    			int orderCounter = 0;
	    			String interactionType = scoreJson.getInteractionType();
	    			List<Answer> answerList = scoreJson.getAnswered();
	    			// System.out.println("Interaction Type Of Item ID '"+ mssRequestResponse.getMosaicScoringRequest().getItemId() +"' :"+interactionType);
	    			if(answerList != null && !answerList.isEmpty()){
	    				
	    				/**
		    			 * Sort the collection of Answer object by drop area name(ascending order) [ Uncomment If needed ]
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
							// System.out.println("Order count : "+orderCounter+" for item Id : "+
							// mssRequestResponse.getMosaicScoringRequest().getItemId());
		    				if(answer.getDroparea() != null){
		    					
		    					if(answer.getDragarea() != null && !answer.getDragarea().isEmpty()){
		    						//: Generate DND, DND-DND, DNDPR type item response structure
		    						orderCounter = MSSConstantUtils.processDNDTypeResponse(answer,candidateResponseList, interactionType, orderCounter);
		    					}
		    				}
		    				else{
		    					//: Generate MSR, MCQ, MCQ-MSR type item response structure
		    					orderCounter = MSSConstantUtils.processMSRTypeResponse(answer,candidateResponseList, interactionType, orderCounter);
		    				}
		    			}
		    			
		    			mssRequestResponse.getMosaicScoringRequest().setCandidateItemResponseObj(candidateResponseList);
		    			
		    			//: Prepare the Candidate response JSON only from object
		    			String finalMssRequestJson = convetObjectToJson(mssRequestResponse, scoreJson.getInteractionType());
		    			return finalMssRequestJson;// return final MSS request JSON string
	    			}
	    		}
	    	}
    	}
    	return null;
    
	}


	/**
	 * Generate JSON from Object
	 * @param mssRequest
	 * @param interactionType
	 * @return String - MSS request JSON structure
	 */
	private String convetObjectToJson(MSSRequestResponse mssRequest, final String interactionType) throws Exception{
		
		Gson gson = new GsonBuilder()
				.registerTypeAdapter(MSSRequestResponse.class,new CustomMosaicSerializer())
				.setExclusionStrategies(new CustomMosaicStrategy(interactionType))
				.serializeNulls().create();
		String json = gson.toJson(mssRequest);
		return json;
	}
	
	private void prepareCollection(MosaicRequestExcelPojo obj) {
		mosaicRequestCSVList.add(obj);
	}

	public static List<MosaicRequestExcelPojo> getfinalCollection() {
		return mosaicRequestCSVList;
	}
}
