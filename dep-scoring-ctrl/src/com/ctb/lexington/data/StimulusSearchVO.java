package com.ctb.lexington.data;

import java.io.Serializable;
import java.util.Collection;

/**
 * <p>Title: StimulusSearchVO</p>
 * <p>Description: holds stimulus search options.</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: CTB/McGraw-Hill</p>
 * @author Jon Becker
 * @version 1.0
 */

public class StimulusSearchVO implements Serializable{

    private String stimulusName;
    private String stimulusFormat;
    private Collection itemSetIds;
    
    public StimulusSearchVO(String stimulusName_, String stimulusFormat_, Collection itemSetIds_) {
    	this.stimulusName = stimulusName_;
    	this.stimulusFormat = stimulusFormat_;
    	this.itemSetIds = itemSetIds_;
    }
	public String getStimulusName() {
		return stimulusName;
	}
	public String getStimulusFormat() {
		return stimulusFormat;
	}
	public Collection getItemSetIds() {
		return itemSetIds;
	}
}