package com.ctb.utils;

import java.lang.reflect.Type;
import java.util.List;

import com.ctb.utils.mssmodel.CandidateItemResponse;
import com.ctb.utils.mssmodel.MSSRequestResponse;
import com.ctb.utils.mssmodel.MosaicScoringRequest;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class CustomMosaicSerializer implements
		JsonSerializer<MSSRequestResponse> {
	
	//public final Map<String, String> mssReqJsonForAudit = new HashMap<String, String>();

	public JsonElement serialize(MSSRequestResponse mssReqResp, Type type,
			JsonSerializationContext context) {

		//JsonObject jsonObject = new JsonObject();
		JsonElement candidateJson = null;
		MosaicScoringRequest mosaicReq = mssReqResp.getMosaicScoringRequest();

		List<CandidateItemResponse> candidateItemRespList = mosaicReq.getCandidateItemResponseObj();
		if (candidateItemRespList != null && candidateItemRespList.size() > 0) {
			candidateJson = context.serialize(candidateItemRespList);
			
			// mosaicReq.setCandidateItemResponse(MSSConstantUtils.getBase64StringFromString(candidateJson.toString()));
			mosaicReq.setCandidateItemResponse(candidateJson.toString());
			
		}
		
		//jsonObject = createMSSRequestJsonObject(mssReqResp, context);
		//jsonObject.add(MSSConstantUtils.CANDIDATEITEMRESPONSE, candidateJson);
		
		return candidateJson;
	}

	/**
	 * Create final MSS request JSON object
	 * 
	 * @param mssReqResp
	 * @param context
	 * @return JSON object
	 */
	// private JsonObject createMSSRequestJsonObject(
	// MSSRequestResponse mssReqResp, JsonSerializationContext context) {
	//
	// JsonElement jsonElm = null;
	// jsonElm = context.serialize(mssReqResp.getMosaicScoringRequest());
	// JsonObject mssObject = new JsonObject();
	// mssObject.add(MSSConstantUtils.MSS_REQUEST_PARAM, jsonElm);
	// return mssObject;
	// }
	
}
