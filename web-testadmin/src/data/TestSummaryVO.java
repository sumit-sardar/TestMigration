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
    private Boolean supportAccommodations = null;
    
    /* 51931 Deferred Defect For HighLighter*/
    private Integer highLighter = null;
    
    /* 51931 Deferred Defect For HighLighter*/     
    public TestSummaryVO(Integer total, 
                         Integer accommodated, 
                         Integer calculator,
                         Integer screenReader,
                         Integer colorFont,
                         Integer pause,
                         Integer untimed,
                         Integer highLighter) {
        this.total = total;
        this.accommodated = accommodated;
        this.calculator = calculator;
        this.screenReader = screenReader;
        this.colorFont = colorFont;
        this.pause = pause;
        this.untimed = untimed;
        this.highLighter = highLighter;
        this.supportAccommodations = Boolean.TRUE;
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
        this.supportAccommodations = Boolean.TRUE;
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
    
    
} 
