package com.ctb.testSessionInfo.data; 

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Iterator;

import com.ctb.bean.testAdmin.TestSession;

public class TestVO implements java.io.Serializable
{ 
    static final long serialVersionUID = 1L;

    private Integer id = null;
    private String testName = null;
    private String level = null;
    private String duration = null;
    private List<SubtestVO> subtests = null;
    
    private String accessCode = null;

    private String [] forms = null;
    
    private String overrideFormAssignment = null;
    
    private Date overrideLoginStartDate = null;
    
    private Date overrideLoginEndDate = null;
        
    private String levelOrGrade = null;
    
    private String isRandomize = null; //Added for Random Distractor
   
   // private String subtestsString = "";
    
    boolean hasMultipleSubtests = true;
    
    boolean isSubtestExists = true;
    
    int subtestCount = 0;
    
    private String startDate;
    
	private String endDate;
	
	private String minLoginEndDate;
	
	private SubtestVO locatorSubtest;
	
	private boolean autoLocator = false;
    
    private Boolean offGradeTestingDisabled = Boolean.FALSE;
    
    private String formOperand = TestSession.FormAssignment.ROUND_ROBIN;
    
    public TestVO(Integer id, String testName, String level, String duration, List<SubtestVO> subtests, SubtestVO locatorSubtest) {
        this.id = id;
        this.testName = testName;
        this.level = level;
        this.duration = duration;
        this.subtests = subtests;
        //this.subtestsString = getSubtestsString();
        //this.accessCode = getFirstAccessCode();
        this.hasMultipleSubtests = hasMultipleSubtests();
        this.isSubtestExists = getSubtestExistStatus();
        if(locatorSubtest!= null) {
        	this.locatorSubtest = locatorSubtest;
        	this.autoLocator = true;
        }
        this.subtestCount = getSubtestCount();
    }

   	public TestVO(TestVO src) {
        this.id = src.getId();
        this.testName = src.getTestName();
        this.level = src.getLevel();
        this.duration = src.getDuration();
        //this.accessCode = src.getAccessCode();
        if (src.getForms() != null && src.getForms().length>0) {
            this.forms = new String[src.getForms().length];
            for (int i=0; i< src.getForms().length; i++) {
                this.forms[i]= src.getForms()[i];     
            }
        }
        else {
        	this.forms = new String[1];
        	this.forms[0] = "";
        }
            

        if (src.getSubtests() != null) {
            Iterator<SubtestVO> it = src.getSubtests().iterator();
            this.subtests = new ArrayList<SubtestVO>();
            while (it.hasNext()) {
                this.subtests.add(new SubtestVO((SubtestVO)it.next()));  
            }
        }
        else
            this.subtests = null;
        if(this.subtests != null) {
        	//this.subtestsString = getSubtestsString();
            this.accessCode = getFirstAccessCode();
            this.hasMultipleSubtests = hasMultipleSubtests();
        }
        
        this.isSubtestExists = getSubtestExistStatus();
        
    }
   	
   	
   	public TestVO(List<SubtestVO> subtests, SubtestVO locatorSubtest) {
   		this.subtests = subtests;
   		if(locatorSubtest!= null) {
        	this.locatorSubtest = locatorSubtest;
        	this.autoLocator = true;
        }
   	   this.subtestCount = getSubtestCount();
	}

   	public int getSubtestCount() {
   		int count = 0;
   		if(subtests==null ) {
   			count = 0;
		} else {
			count =  subtests.size();
		}
   		if(isAutoLocator()){
   			count +=1;
   		}
   		return count;
	}
   	
    private boolean getSubtestExistStatus() {
		if(subtests==null || subtests.size()==0) {
			return false;
		} else {
			return true;
		}
	}

	private String getFirstAccessCode() {
    	if(subtests!=null && subtests.size()>0)
    		return subtests.get(0).getTestAccessCode();
    	else
    		return "";
	}

    public Integer getId() {
        return this.id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getTestName() {
        return this.testName;
    }
    public void setTestName(String testName) {
        this.testName = testName;
    }   
     public String getLevelOrGrade() {
        return this.levelOrGrade;
    }
    public void setLevelOrGrade(String levelOrGrade) {
        this.levelOrGrade = levelOrGrade;
    }   
     public String getLevel() {
        return this.level;
    }
    public void setLevel(String level) {
        this.level = level;
    }   
   public String getDuration() {
        return this.duration;
    }
    public void setDuration(String duration) {
        this.duration = duration;
    }   
    public List<SubtestVO> getSubtests() {
        return this.subtests;
    }
    public void setSubtests(List<SubtestVO> subtests) {
        this.subtests = subtests;
    }   

    public String getAccessCode() {
        return this.accessCode;
    }
    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }
    
    public String [] getForms() {
        return this.forms;
    }
    public void setForms(String [] forms) {
        this.forms = forms;
    }

    public String getOverrideFormAssignment() {
        return this.overrideFormAssignment;
    }
    public void setOverrideFormAssignment(String overrideFormAssignment) {
        this.overrideFormAssignment = overrideFormAssignment;
    }

    public Date getOverrideLoginStartDate() {
        return this.overrideLoginStartDate;
    }
    public void setOverrideLoginStartDate(Date overrideLoginStartDate) {
        this.overrideLoginStartDate = overrideLoginStartDate;
    }


    public boolean hasMultipleSubtests() {
        if (this.subtests.size() > 1)
            return true;
        else
            return false;
    }             
    
    public String getSubtestsString() {
        Iterator it = this.subtests.iterator();
        StringBuffer buf = new StringBuffer();
        
        if (this.subtests.size()<=1) 
            return "--";
        while (it.hasNext())
        {
            String subtestName = ((SubtestVO)it.next()).getSubtestName();
            buf.append(subtestName);
            if (it.hasNext())
            	buf.append(",");
               // buf.append("<br/>");
        }
        return buf.toString();
    }
    
   /**
	 * @return Returns the IsRandomize.
	 */
    
    public String getIsRandomize() {
		return isRandomize;
	}
    
    /**
	 * @param IsRandomize The IsRandomize to set.
	 */
	public void setIsRandomize(String isRandomize) {
		this.isRandomize = isRandomize;
	}

	
	/**
	 * @param offGradeTestingDisabled the offGradeTestingDisabled to set
	 */
	public void setOffGradeTestingDisabled(Boolean offGradeTestingDisabled) {
		this.offGradeTestingDisabled = offGradeTestingDisabled;
	}

	
	/**
	 * @param formOperand the formOperand to set
	 */
	public void setFormOperand(String formOperand) {
		this.formOperand = formOperand;
	}

	
	/**
	 * @return the formOperand
	 */
	public String getFormOperand() {
		return formOperand;
	}

	
	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}

	
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	
	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}

	
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	
	/**
	 * @return the overrideLoginEndDate
	 */
	public Date getOverrideLoginEndDate() {
		return overrideLoginEndDate;
	}

	
	/**
	 * @param overrideLoginEndDate the overrideLoginEndDate to set
	 */
	public void setOverrideLoginEndDate(Date overrideLoginEndDate) {
		this.overrideLoginEndDate = overrideLoginEndDate;
	}

	
	/**
	 * @return the minLoginEndDate
	 */
	public String getMinLoginEndDate() {
		return minLoginEndDate;
	}

	
	/**
	 * @param minLoginEndDate the minLoginEndDate to set
	 */
	public void setMinLoginEndDate(String minLoginEndDate) {
		this.minLoginEndDate = minLoginEndDate;
	}

	
	/**
	 * @return the locatorSubtest
	 */
	public SubtestVO getLocatorSubtest() {
		return locatorSubtest;
	}

	
	/**
	 * @param locatorSubtest the locatorSubtest to set
	 */
	public void setLocatorSubtest(SubtestVO locatorSubtest) {
		this.locatorSubtest = locatorSubtest;
	}

	
	/**
	 * @return the autoLocator
	 */
	public boolean isAutoLocator() {
		return autoLocator;
	}

	
	/**
	 * @param autoLocator the autoLocator to set
	 */
	public void setAutoLocator(boolean autoLocator) {
		this.autoLocator = autoLocator;
	}
    
    
} 
