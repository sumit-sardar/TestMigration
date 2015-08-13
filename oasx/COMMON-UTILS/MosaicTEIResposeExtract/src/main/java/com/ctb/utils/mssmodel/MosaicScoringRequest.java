package com.ctb.utils.mssmodel;

import java.io.Serializable;
import java.util.List;

public class MosaicScoringRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private String ItemResponseSource;
	private String ItemSource;
	private String ItemId;
	private String ItemBankId;
	private String CandidateItemResponse;
	private String Timestamp;

	// Use to create collection of candidate response JSON object only
	private List<CandidateItemResponse> CandidateItemResponseObj;

	public MosaicScoringRequest() {
		// super();
	}

	public MosaicScoringRequest(String itemResponseSource, String itemSource, String itemId,
			String itemBankId, String timestamp) {
		ItemResponseSource = itemResponseSource;
		ItemSource = itemSource;
		ItemId = itemId;
		ItemBankId = itemBankId;
		Timestamp = timestamp;
	}

	/**
	 * @return the itemResponseSource
	 */
	public String getItemResponseSource() {
		return ItemResponseSource;
	}

	/**
	 * @param itemResponseSource
	 *            the itemResponseSource to set
	 */
	public void setItemResponseSource(String itemResponseSource) {
		ItemResponseSource = itemResponseSource;
	}

	/**
	 * @return the itemSource
	 */
	public String getItemSource() {
		return ItemSource;
	}

	/**
	 * @param itemSource
	 *            the itemSource to set
	 */
	public void setItemSource(String itemSource) {
		ItemSource = itemSource;
	}

	/**
	 * @return the itemId
	 */
	public String getItemId() {
		return ItemId;
	}

	/**
	 * @param itemId
	 *            the itemId to set
	 */
	public void setItemId(String itemId) {
		ItemId = itemId;
	}

	/**
	 * @return the itemBankId
	 */
	public String getItemBankId() {
		return ItemBankId;
	}

	/**
	 * @param itemBankId
	 *            the itemBankId to set
	 */
	public void setItemBankId(String itemBankId) {
		ItemBankId = itemBankId;
	}

	/**
	 * @return the candidateItemResponseObj
	 */
	public List<CandidateItemResponse> getCandidateItemResponseObj() {
		return CandidateItemResponseObj;
	}

	/**
	 * @param candidateItemResponseObj
	 *            the candidateItemResponseObj to set
	 */
	public void setCandidateItemResponseObj(
			List<CandidateItemResponse> candidateItemResponseObj) {
		CandidateItemResponseObj = candidateItemResponseObj;
	}

	/**
	 * @return the candidateItemResponse
	 */
	public String getCandidateItemResponse() {
		return CandidateItemResponse;
	}

	/**
	 * @param candidateItemResponse
	 *            the candidateItemResponse to set
	 */
	public void setCandidateItemResponse(String candidateItemResponse) {
		CandidateItemResponse = candidateItemResponse;
	}

	/**
	 * @return the timestamp
	 */
	public String getTimestamp() {
		return Timestamp;
	}

	/**
	 * @param timestamp
	 *            the timestamp to set
	 */
	public void setTimestamp(String timestamp) {
		Timestamp = timestamp;
	}

}
