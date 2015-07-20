package com.ctb.lexington.util;

import java.lang.reflect.Type;
import java.util.List;

import com.ctb.lexington.util.mosaicobject.CandidateItemResponse;
import com.ctb.lexington.util.mosaicobject.MSSRequestResponse;
import com.ctb.lexington.util.mosaicobject.MosaicScoringRequest;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class MosaicCustomSerializer implements
		JsonSerializer<MSSRequestResponse> {

	@Override
	public JsonElement serialize(MSSRequestResponse mssReqResp, Type type,
			JsonSerializationContext context) {

		JsonObject jsonObject = new JsonObject();
		JsonElement candidateJson = null;
		MosaicScoringRequest mosaicReq = mssReqResp.getMosaicScoringRequest();

		List<CandidateItemResponse> candidateItemRespList = mosaicReq.getCandidateItemResponseObj();
		if (candidateItemRespList != null && candidateItemRespList.size() > 0) {
			candidateJson = context.serialize(candidateItemRespList);
			mosaicReq.setCandidateItemResponse(MosaicConstantUtils.getBase64StringFromString(candidateJson.toString()));
		}
		jsonObject = createMSSRequestJsonObject(mssReqResp, context);
		return jsonObject;
	}

	
	/**
	 * Create final MSS request JSON object
	 * @param mssReqResp
	 * @param context
	 * @return JSON object
	 */
	private JsonObject createMSSRequestJsonObject(
			MSSRequestResponse mssReqResp, JsonSerializationContext context) {

		JsonElement jsonElm = null;
		jsonElm = context.serialize(mssReqResp.getMosaicScoringRequest());
		JsonObject mssObject = new JsonObject();
		mssObject.add(MosaicConstantUtils.MSS_REQUEST_PARAM, jsonElm);
		return mssObject;
	}

}
