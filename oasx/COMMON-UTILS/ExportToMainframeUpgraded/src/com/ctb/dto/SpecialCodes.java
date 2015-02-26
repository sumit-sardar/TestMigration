package com.ctb.dto;

public class SpecialCodes {

	private String specialCodeK = new String(" ");
	private String specialCodeL = new String(" ");
	private String specialCodeM = new String(" ");
	private String specialCodeN = new String(" ");
	private String specialCodeO = new String(" ");
	private String specialCodeP = new String(" ");
	private String specialCodeQ = new String(" ");
	private String specialCodeR = new String(" ");
	private String specialCodeS = new String(" ");
	private String specialCodeT = new String(" ");

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(getSpecialCodeK()).append(getSpecialCodeL()).append(getSpecialCodeM())
				.append(getSpecialCodeN()).append(getSpecialCodeO()).append(getSpecialCodeP())
				.append(getSpecialCodeQ()).append(getSpecialCodeR()).append(getSpecialCodeS())
				.append(getSpecialCodeT());
		
		return sb.toString();
	}

	/**
	 * @return the specialCodeK
	 */
	public String getSpecialCodeK() {
		
		return specialCodeK;
	}

	/**
	 * @param specialCodeK
	 *            the specialCodeK to set
	 */
	public void setSpecialCodeK(String specialCodeK) {
		this.specialCodeK = specialCodeK;
	}

	/**
	 * @return the specialCodeL
	 */
	public String getSpecialCodeL() {
		
		return specialCodeL;
	}

	/**
	 * @param specialCodeL
	 *            the specialCodeL to set
	 */
	public void setSpecialCodeL(String specialCodeL) {
		this.specialCodeL = specialCodeL;
	}

	/**
	 * @return the specialCodeM
	 */
	public String getSpecialCodeM() {
		
		return specialCodeM;
	}

	/**
	 * @param specialCodeM
	 *            the specialCodeM to set
	 */
	public void setSpecialCodeM(String specialCodeM) {
		this.specialCodeM = specialCodeM;
	}

	/**
	 * @return the specialCodeN
	 */
	public String getSpecialCodeN() {
		
		return specialCodeN;
	}

	/**
	 * @param specialCodeN
	 *            the specialCodeN to set
	 */
	public void setSpecialCodeN(String specialCodeN) {
		this.specialCodeN = specialCodeN;
	}

	/**
	 * @return the specialCodeO
	 */
	public String getSpecialCodeO() {
		
		return specialCodeO;
	}

	/**
	 * @param specialCodeO
	 *            the specialCodeO to set
	 */
	public void setSpecialCodeO(String specialCodeO) {
		this.specialCodeO = specialCodeO;
	}

	/**
	 * @return the specialCodeP
	 */
	public String getSpecialCodeP() {
		
		return specialCodeP;
	}

	/**
	 * @param specialCodeP
	 *            the specialCodeP to set
	 */
	public void setSpecialCodeP(String specialCodeP) {
		this.specialCodeP = specialCodeP;
	}

	/**
	 * @return the specialCodeQ
	 */
	public String getSpecialCodeQ() {
		
		return specialCodeQ;
	}

	/**
	 * @param specialCodeQ
	 *            the specialCodeQ to set
	 */
	public void setSpecialCodeQ(String specialCodeQ) {
		this.specialCodeQ = specialCodeQ;
	}

	/**
	 * @return the specialCodeR
	 */
	public String getSpecialCodeR() {
		
		return specialCodeR;
	}

	/**
	 * @param specialCodeR
	 *            the specialCodeR to set
	 */
	public void setSpecialCodeR(String specialCodeR) {
		this.specialCodeR = specialCodeR;
	}

	/**
	 * @return the specialCodeS
	 */
	public String getSpecialCodeS() {
		
		return specialCodeS;
	}

	/**
	 * @param specialCodeS
	 *            the specialCodeS to set
	 */
	public void setSpecialCodeS(String specialCodeS) {
		
		this.specialCodeS = specialCodeS;
	}

	/**
	 * @return the specialCodeT
	 */
	public String getSpecialCodeT() {
		
		return specialCodeT;
	}

	/**
	 * @param specialCodeT
	 *            the specialCodeT to set
	 */
	public void setSpecialCodeT(String specialCodeT) {
		this.specialCodeT = specialCodeT;
	}

}
