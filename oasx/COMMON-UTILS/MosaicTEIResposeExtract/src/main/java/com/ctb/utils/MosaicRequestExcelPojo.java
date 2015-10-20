package com.ctb.utils;

import java.io.Serializable;

public class MosaicRequestExcelPojo implements Serializable,
		Comparable<MosaicRequestExcelPojo> {

	private static final long serialVersionUID = 1L;
	private String itemresponsesource;
	private String itemsource;
	private String itemid;
	private String itemBankid;
	private String sResponse;
	private String json;
	private String studentrosterid;
	private String oasItemId;
	private String PEId;
	private String answered;

	public MosaicRequestExcelPojo(String itemid, String sResponse, String json,
			String studentrosterid, String oasItemId, String pEId,
			String answered) {
		this.itemresponsesource = MSSConstantUtils.ITEM_RESPONSE_SOURCE;
		this.itemsource = MSSConstantUtils.ITEM_SOURCE;
		this.itemid = itemid;
		this.itemBankid = MSSConstantUtils.ITEM_BANK_ID;
		this.sResponse = sResponse;
		this.json = json;
		this.studentrosterid = studentrosterid;
		this.oasItemId = oasItemId;
		this.PEId = pEId;
		this.answered = answered;
	}

	/**
	 * @return the itemresponsesource
	 */
	public String getItemresponsesource() {
		return itemresponsesource;
	}

	/**
	 * @param itemresponsesource
	 *            the itemresponsesource to set
	 */
	public void setItemresponsesource(String itemresponsesource) {
		this.itemresponsesource = itemresponsesource;
	}

	/**
	 * @return the itemsource
	 */
	public String getItemsource() {
		return itemsource;
	}

	/**
	 * @param itemsource
	 *            the itemsource to set
	 */
	public void setItemsource(String itemsource) {
		this.itemsource = itemsource;
	}

	/**
	 * @return the itemid
	 */
	public String getItemid() {
		return itemid;
	}

	/**
	 * @param itemid
	 *            the itemid to set
	 */
	public void setItemid(String itemid) {
		this.itemid = itemid;
	}

	/**
	 * @return the itemBankid
	 */
	public String getItemBankid() {
		return itemBankid;
	}

	/**
	 * @param itemBankid
	 *            the itemBankid to set
	 */
	public void setItemBankid(String itemBankid) {
		this.itemBankid = itemBankid;
	}

	/**
	 * @return the sResponse
	 */
	public String getsResponse() {
		return sResponse;
	}

	/**
	 * @param sResponse
	 *            the sResponse to set
	 */
	public void setsResponse(String sResponse) {
		this.sResponse = sResponse;
	}

	/**
	 * @return the json
	 */
	public String getJson() {
		return json;
	}

	/**
	 * @param json
	 *            the json to set
	 */
	public void setJson(String json) {
		this.json = json;
	}

	/**
	 * @return the studentrosterid
	 */
	public String getStudentrosterid() {
		return studentrosterid;
	}

	/**
	 * @param studentrosterid
	 *            the studentrosterid to set
	 */
	public void setStudentrosterid(String studentrosterid) {
		this.studentrosterid = studentrosterid;
	}

	/**
	 * @return the oasItemId
	 */
	public String getOasItemId() {
		return oasItemId;
	}

	/**
	 * @param oasItemId
	 *            the oasItemId to set
	 */
	public void setOasItemId(String oasItemId) {
		this.oasItemId = oasItemId;
	}

	/**
	 * @return the pEId
	 */
	public String getPEId() {
		return PEId;
	}

	/**
	 * @param pEId
	 *            the pEId to set
	 */
	public void setPEId(String pEId) {
		PEId = pEId;
	}

	/**
	 * @return the answered
	 */
	public String getAnswered() {
		return answered;
	}

	/**
	 * @param answered
	 *            the answered to set
	 */
	public void setAnswered(String answered) {
		this.answered = answered;
	}

	public int compareTo(MosaicRequestExcelPojo o) {
		return this.studentrosterid
				.concat(this.oasItemId)
				.concat(this.itemid)
				.compareTo(
						o.getStudentrosterid().concat(o.getOasItemId())
								.concat(o.getItemid()));
	}

	// @Override
	// public String toString() {
	// StringBuilder newString = new StringBuilder();
	// newString.append(MSSConstantUtils.wrap(itemid)).append(MSSConstantUtils.csvFileSeparator)
	// .append(MSSConstantUtils.wrap(itemsource)).append(MSSConstantUtils.csvFileSeparator)
	// .append(MSSConstantUtils.wrap(itemBankid)).append(MSSConstantUtils.csvFileSeparator)
	// .append(MSSConstantUtils.wrap(sResponse)).append(MSSConstantUtils.csvFileSeparator)
	// .append(MSSConstantUtils.wrap(studentrosterid)).append(MSSConstantUtils.csvFileSeparator)
	// .append(MSSConstantUtils.wrap(oasItemId)).append(MSSConstantUtils.csvFileSeparator)
	// .append(MSSConstantUtils.wrap(PEId)).append(MSSConstantUtils.csvFileSeparator)
	// .append(MSSConstantUtils.wrap(json));
	// return newString.toString();
	// }

}
