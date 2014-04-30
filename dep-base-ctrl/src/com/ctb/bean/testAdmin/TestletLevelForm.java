package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import java.util.Date;

/**
 * Data bean representing the contents of query to filter students for assigning testlets
 * 
 * @author Nate_Cohen
 */
public class TestletLevelForm extends CTBBean
{
    static final long serialVersionUID = 1L;
    private String subject;
    private String TABELevel;
    private String TestletForm;
    
    /**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	/**
	 * @return Returns the TABELevel.
	 */
	public String getTABELevel() {
		return TABELevel;
	}
	/**
	 * @param TABELevel The TABELevel to set.
	 */
	public void setTABELevel(String TABELevel) {
		this.TABELevel = TABELevel;
	}
	/**
	 * @return the TestletForm
	 */
	public String getTestletForm() {
		return TestletForm;
	}
	/**
	 * @param TestletForm the TestletForm to set
	 */
	public void setTestletForm(String TestletForm) {
		this.TestletForm = TestletForm;
	}
	
} 
