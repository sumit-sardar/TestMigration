package com.ctb.lexington.data;

/**
 * @author Tai Truong
 * @created Jan 31, 2005
 */

////////////////////////////////////////////////////////////////////////////////////////////////
// This a transfer data object used to display the wizard buttons  
/////////////////////////////////////////////////////////////////////////////////////////////////
public class ImageButtonVO implements java.io.Serializable
{
    private String name = null;
    private String imagePathName = null;
    private String alt = null;
    private String script = null;
    private boolean hidden = false;
    private String disableImagePathName = null;
    
    /**
     * Creates new ImageButtonVO
     */
    public ImageButtonVO(String name, String imagePathName, String alt, String script) {
	    this.name = name;
	    this.imagePathName = imagePathName;
	    this.alt = alt;
	    this.script = script;
    }

    public ImageButtonVO(String name, String imagePathName, String disableImagePathName, String alt, String script) {
	    this.name = name;
	    this.imagePathName = imagePathName;
	    this.disableImagePathName = disableImagePathName;
	    this.alt = alt;
	    this.script = script;
    }
    
    /**
     * @return Returns the alt.
     */
    public String getAlt() {
        return this.alt;
    }
    /**
     * @param alt The alt to set.
     */
    public void setAlt(String alt) {
        this.alt = alt;
    }
    /**
     * @return Returns the imagePathName.
     */
    public String getImagePathName() {
        return this.imagePathName;
    }
    /**
     * @param imagePathName The imagePathName to set.
     */
    public void setImagePathName(String imagePathName) {
        this.imagePathName = imagePathName;
    }
    /**
     * @return Returns the name.
     */
    public String getName() {
        return this.name;
    }
    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return Returns the script.
     */
    public String getScript() {
        return this.script;
    }
    /**
     * @param script The script to set.
     */
    public void setScript(String script) {
        this.script = script;
    }
    /**
     * @return Returns the hidden.
     */
    public boolean isHidden() {
        return this.hidden;
    }
    /**
     * @param hidden The hidden to set.
     */
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
    public String getDisableImagePathName() {
        return this.disableImagePathName;
    }
    public void setDisableImagePathName(String disableImagePathName) {
        this.disableImagePathName = disableImagePathName;
    }
}
 