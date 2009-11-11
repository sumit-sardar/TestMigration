package com.ctb.lexington.db.irsdata;
import com.ctb.lexington.db.record.Persistent;

/**
 * @author Rama_Rao
 *
 */
public class IrsResponseDimData implements Persistent{
	 private Long responseid;
	 private String response;

	 public Long getResponseid() {
	        return responseid;
	    }

	 public void setResponseid(Long responseid) {
	        this.responseid = responseid;
	    }

	 public String getResponse() {
	        return response;
	    }

	 public void setResponse(String response) {
	        this.response = response;
	    }
}