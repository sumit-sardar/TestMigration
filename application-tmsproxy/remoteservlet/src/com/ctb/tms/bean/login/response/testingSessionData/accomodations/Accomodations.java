package com.ctb.tms.bean.login.response.testingSessionData.accomodations; 

import java.util.List;

public class Accomodations 
{ 
    private int calculator;
    private int magnifier;
    private int screenReader;
    private int untimed;
    private int testPause;
    private List stereotypeStyles;
    
    /**
	 * @return Returns the testPause.
	 */
	public int getTestPause() {
		return testPause;
	}
	/**
	 * @param testPause The testPause to set.
	 */
	public void setTestPause(int testPause) {
		this.testPause = testPause;
	}
    /**
	 * @return Returns the calculator.
	 */
	public int getCalculator() {
		return calculator;
	}
	/**
	 * @param calculator The calculator to set.
	 */
	public void setCalculator(int calculator) {
		this.calculator = calculator;
	}
	/**
	 * @return Returns the magnifier.
	 */
	public int getMagnifier() {
		return magnifier;
	}
	/**
	 * @param magnifier The magnifier to set.
	 */
	public void setMagnifier(int magnifier) {
		this.magnifier = magnifier;
	}
	/**
	 * @return Returns the screenReader.
	 */
	public int getScreenReader() {
		return screenReader;
	}
	/**
	 * @param screenReader The screenReader to set.
	 */
	public void setScreenReader(int screenReader) {
		this.screenReader = screenReader;
	}
	/**
	 * @return Returns the stereotypeStyles.
	 */
	public List getStereotypeStyles() {
		return stereotypeStyles;
	}
	/**
	 * @param stereotypeStyles The stereotypeStyles to set.
	 */
	public void setStereotypeStyles(List stereotypeStyles) {
		this.stereotypeStyles = stereotypeStyles;
	}
	/**
	 * @return Returns the untimed.
	 */
	public int getUntimed() {
		return untimed;
	}
	/**
	 * @param untimed The untimed to set.
	 */
	public void setUntimed(int untimed) {
		this.untimed = untimed;
	}
} 
