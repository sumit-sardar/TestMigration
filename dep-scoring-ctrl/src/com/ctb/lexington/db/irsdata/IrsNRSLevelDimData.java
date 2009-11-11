package com.ctb.lexington.db.irsdata;

import com.ctb.lexington.db.record.Persistent;
/**
 * @author Rama_Rao
 *
 */
public class IrsNRSLevelDimData implements Persistent{
	
	 private Long nrsLevelid;
	 private String name;

	 public Long getNrsLevelid() {
	      return nrsLevelid;
	 }

	 public void setNrsLevelid(Long nrsLevelid) {
	     this.nrsLevelid = nrsLevelid;
	    }

	 public String getName() {
	     return name;
	 }

	 public void setName(String name) {
	     this.name = name;
	 }
}
