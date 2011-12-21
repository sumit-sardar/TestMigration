package data; 

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Iterator;

public class TestSummaryVO implements java.io.Serializable
{ 
    static final long serialVersionUID = 1L;

    private Integer total = null;
    private Integer accommodated = null;
    private Integer calculator = null;
    private Integer screenReader = null;
    private Integer colorFont = null;
    private Integer pause = null;
    private Integer untimed = null;
    //Start- added for student pacing
    private Integer extendedTimeAccom = null;
    private Boolean supportAccommodations = null;
    
    /* 51931 Deferred Defect For HighLighter*/
    private Integer highLighter = null;
    
    /* 51931 Deferred Defect For HighLighter*/
    // Start: For MQC defect 66844
    private Integer maskingRular = null;
    private Integer maskingTool = null;
    private Integer magnifyingGlass = null;
    private Integer musicPlayerAccom = null;
    // End: For MQC defect 66844
    // For MQC defect 66844
    public TestSummaryVO(Integer total, 
                         Integer accommodated, 
                         Integer calculator,
                         Integer screenReader,
                         Integer colorFont,
                         Integer pause,
                         Integer untimed,
                         Integer highLighter,
                         Integer extendedTimeAccom, 
                         Integer maskingRular, 
                         Integer maskingTool, 
                         Integer magnifyingGlass, 
                         Integer musicPlayerAccom) {
        this.total = total;
        this.accommodated = accommodated;
        this.calculator = calculator;
        this.screenReader = screenReader;
        this.colorFont = colorFont;
        this.pause = pause;
        this.untimed = untimed;
        this.highLighter = highLighter;
        this.extendedTimeAccom = extendedTimeAccom; 
        // end- added for student pacing
        this.supportAccommodations = Boolean.TRUE;
         // Start: For MQC defect 66844
        this.maskingRular = maskingRular; 
        this.maskingTool = maskingTool; 
        this.magnifyingGlass = magnifyingGlass; 
        this.musicPlayerAccom = musicPlayerAccom; 
         // End: For MQC defect 66844
    }
    /* 51931 Deferred Defect For HighLighter*/     
    public TestSummaryVO(TestSummaryVO src) {
        this.total = src.getTotal();
        this.accommodated = src.getAccommodated();
        this.calculator = src.getCalculator();
        this.screenReader = src.getScreenReader();
        this.colorFont = src.getColorFont();
        this.pause = src.getPause();
        this.untimed = src.getUntimed();
        this.highLighter = src.highLighter;
        //added for student pacing
        this.extendedTimeAccom = src.extendedTimeAccom;
        this.supportAccommodations = Boolean.TRUE;
         // Start: For MQC defect 66844
        this.maskingRular = src.maskingRular; 
        this.maskingTool = src.maskingTool; 
        this.magnifyingGlass = src.magnifyingGlass; 
        this.musicPlayerAccom = src.musicPlayerAccom; 
         // End: For MQC defect 66844
    }

    public Integer getTotal() {
        return this.total;
    }
    public void setTotal(Integer total) {
        this.total = total;
    }
    public Integer getAccommodated() {
        return this.accommodated;
    }
    public void setAccommodated(Integer accommodated) {
        this.accommodated = accommodated;
    }
    public Integer getCalculator() {
        return this.calculator;
    }
    public void setCalculator(Integer calculator) {
        this.calculator = calculator;
    }
    public Integer getScreenReader() {
        return this.screenReader;
    }
    public void setScreenReader(Integer screenReader) {
        this.screenReader = screenReader;
    }
    public Integer getColorFont() {
        return this.colorFont;
    }
    public void setColorFont(Integer colorFont) {
        this.colorFont = colorFont;
    }
    public Integer getPause() {
        return this.pause;
    }
    public void setPause(Integer pause) {
        this.pause = pause;
    }    
    public Integer getUntimed() {
        return this.untimed;
    }
    public void setUntimed(Integer untimed) {
        this.untimed = untimed;
    }
    public Boolean getSupportAccommodations() {
        return this.supportAccommodations;
    }
    public void setSupportAccommodations(Boolean supportAccommodations) {
        this.supportAccommodations = supportAccommodations;
    }    
    /* 51931 Deferred Defect For HighLighter*/
    public Integer getHighLighter() {
        return this.highLighter;
    }
    public void setHighLighter(Integer highLighter) {
        this.highLighter = highLighter;
    }
    
    //Start- added for student pacing
	/**
	 * @return the extendedTimeAccom
	 */
	public Integer getExtendedTimeAccom() {
		return extendedTimeAccom;
	}
	/**
	 * @param extendedTimeAccom the extendedTimeAccom to set
	 */
	public void setExtendedTimeAccom(Integer extendedTimeAccom) {
		this.extendedTimeAccom = extendedTimeAccom;
	}
    //end- added for student pacing
	public Integer getMaskingRular() {
		return maskingRular;
	}
	public void setMaskingRular(Integer maskingRular) {
		this.maskingRular = maskingRular;
	}
	public Integer getMaskingTool() {
		return maskingTool;
	}
	public void setMaskingTool(Integer maskingTool) {
		this.maskingTool = maskingTool;
	}
	public Integer getMagnifyingGlass() {
		return magnifyingGlass;
	}
	public void setMagnifyingGlass(Integer magnifyingGlass) {
		this.magnifyingGlass = magnifyingGlass;
	}
	public Integer getMusicPlayerAccom() {
		return musicPlayerAccom;
	}
	public void setMusicPlayerAccom(Integer musicPlayerAccom) {
		this.musicPlayerAccom = musicPlayerAccom;
	}
    
} 
