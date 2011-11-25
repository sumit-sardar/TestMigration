package com.ctb.testSessionInfo.data; 

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Iterator;

public class TestVO implements java.io.Serializable
{ 
    static final long serialVersionUID = 1L;

    private Integer id = null;
    private String testName = null;
    private String level = null;
    private String duration = null;
    private List<SubtestVO> subtests = null;
    
    private String accessCode = null;

   // private String [] forms = null;
    
    private String overrideFormAssignment = null;
    
    private Date overrideLoginStartDate = null;
        
    private String levelOrGrade = null;
    
    private String isRandomize = null; //Added for Random Distractor
   
    private String subtestsString = "";
    
    boolean hasMultipleSubtests = true;
    
    boolean isSubtestExists = true;
    
    int subtestCount = 0;
    
    public TestVO(Integer id, String testName, String level, String duration, List<SubtestVO> subtests) {
        this.id = id;
        this.testName = testName;
        this.level = level;
        this.duration = duration;
        this.subtests = subtests;
        this.subtestsString = getSubtestsString();
        this.accessCode = getFirstAccessCode();
        this.hasMultipleSubtests = hasMultipleSubtests();
        this.isSubtestExists = getSubtestExistStatus();
        this.subtestCount = getSubtestCount();
    }

   	public TestVO(TestVO src) {
        this.id = src.getId();
        this.testName = src.getTestName();
        this.level = src.getLevel();
        this.duration = src.getDuration();
        this.accessCode = src.getAccessCode();
        /*if (src.getForms() != null) {
            this.forms = new String[src.getForms().length];
            for (int i=0; i< src.getForms().length; i++) {
                this.forms[i]= src.getForms()[i];     
            }
        }
        else 
            this.forms = null;*/

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
        	this.subtestsString = getSubtestsString();
            this.accessCode = getFirstAccessCode();
            this.hasMultipleSubtests = hasMultipleSubtests();
        }
        
        this.isSubtestExists = getSubtestExistStatus();
        
    }
   	
   	
   	private int getSubtestCount() {
   		if(subtests==null ) {
			return 0;
		} else {
			return subtests.size();
		}
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
    /*
    public String [] getForms() {
        return this.forms;
    }
    public void setForms(String [] forms) {
        this.forms = forms;
    }
*/
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
    
    
} 
