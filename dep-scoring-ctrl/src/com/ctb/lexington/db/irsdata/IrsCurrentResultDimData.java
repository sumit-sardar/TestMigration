package com.ctb.lexington.db.irsdata;
import com.ctb.lexington.db.record.Persistent;
/**
 * @author Rama_Rao
 *
 */
public class IrsCurrentResultDimData implements Persistent {
	 private Long currentResultid;
	 private String name;

	    public Long getCurrentResultid() {
	        return currentResultid;
	    }
	    public void setCurrentResultid(Long currentResultid) {
	        this.currentResultid = currentResultid;
	    }
	    public String getName() {
	        return name;
	    }
	    public void setName(String name) {
	        this.name = name;
	    }
}
