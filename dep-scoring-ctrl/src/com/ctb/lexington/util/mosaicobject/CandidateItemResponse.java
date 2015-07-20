package com.ctb.lexington.util.mosaicobject;

import java.io.Serializable;

public class CandidateItemResponse implements Serializable{

	
	private static final long serialVersionUID = 1L;
	private String order;
	private String value;
	
	private String id;
	private String mathml_value;
	
	private String target; //for DND
	private String html; //for DND
	
	private String ItemOrder; //for parent DAS Item
	private String ItemScore; //for parent DAS Item
	
	/**
	 * @return the order
	 */
	public String getOrder() {
		return order;
	}
	/**
	 * @param order the order to set
	 */
	public void setOrder(String order) {
		this.order = order;
	}
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the mathml_value
	 */
	public String getMathml_value() {
		return mathml_value;
	}
	/**
	 * @param mathmlValue the mathml_value to set
	 */
	public void setMathml_value(String mathmlValue) {
		mathml_value = mathmlValue;
	}
	/**
	 * @return the target
	 */
	public String getTarget() {
		return target;
	}
	/**
	 * @param target the target to set
	 */
	public void setTarget(String target) {
		this.target = target;
	}
	/**
	 * @return the html
	 */
	public String getHtml() {
		return html;
	}
	/**
	 * @param html the html to set
	 */
	public void setHtml(String html) {
		this.html = html;
	}
	/**
	 * @return the itemOrder
	 */
	public String getItemOrder() {
		return ItemOrder;
	}
	/**
	 * @param itemOrder the itemOrder to set
	 */
	public void setItemOrder(String itemOrder) {
		ItemOrder = itemOrder;
	}
	/**
	 * @return the itemScore
	 */
	public String getItemScore() {
		return ItemScore;
	}
	/**
	 * @param itemScore the itemScore to set
	 */
	public void setItemScore(String itemScore) {
		ItemScore = itemScore;
	}
}
