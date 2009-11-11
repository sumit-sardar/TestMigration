package com.ctb.lexington.data;

/**
 * @author Tai Truong
 */

import java.io.Serializable;

public class TemplateVO implements Serializable 
{
	private String id = null;
	private String displayName = null;
	private String imageName = null;
	private String imageNamePreview = null;
	private String stimulusType = null;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public String getImageName() {
        return imageName;
    }
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
    public String getImageNamePreview() {
        return imageNamePreview;
    }
    public void setImageNamePreview(String imageNamePreview) {
        this.imageNamePreview = imageNamePreview;
    }
    public String getStimulusType() {
        return stimulusType;
    }
    public void setStimulusType(String stimulusType) {
        this.stimulusType = stimulusType;
    }
}
