package com.ctb.contentBridge.core.publish.dao;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ctb.contentBridge.core.exception.BusinessException;


public class ProductTypeInfo {
    private String name;
    private String abbreviation;
    private String title;

    private Map contentAreas;
    private List scoreTypes;

    public ProductTypeInfo(String name, String abbreviation) {
        this.name = name.toUpperCase();
        this.abbreviation = abbreviation.toUpperCase();
    }

    /**
     * @return Returns the abbreviation.
     */
    public String getAbbreviation() {
        return abbreviation;
    }

    /**
     * @param abbreviation The abbreviation to set.
     */
    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    /**
     * @return Returns the contentAreas.
     */
    public Map getContentAreas() {
        return contentAreas;
    }

    /**
     * @param contentAreas The contentAreas to set.
     */
    public void setContentAreas(Map contentAreas) {
        this.contentAreas = contentAreas;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        StringBuffer result = new StringBuffer();

        result.append("Product Type: \n");
        result.append("\t");
        result.append(getName());
        result.append(": ");
        result.append(getAbbreviation());
        result.append("\n");

        for (Iterator iterator = getContentAreas().values().iterator(); iterator.hasNext();) {
            ContentAreaInfo contentArea = (ContentAreaInfo) iterator.next();
            result.append(contentArea.toString());
            result.append("\n");

        }
        result.append("\t\tScore Type: ");
        result.append(scoreTypes);
        result.append("\n");

        return result.toString();
    }

    /**
     * @return Returns the scoreTypes.
     */
    public List getScoreTypes() {
        return scoreTypes;
    }

    /**
     * @param scoreTypes The scoreTypes to set.
     */
    public void setScoreTypes(List scoreTypes) {
        this.scoreTypes = scoreTypes;
    }

    /**
     * @param contentArea
     * @return
     */
    public ContentAreaInfo findContentArea(String contentAreaName) {
        ContentAreaInfo info = (ContentAreaInfo) contentAreas.get(contentAreaName.toUpperCase());

        if (info != null) {
            return info;
        } else {
            throw new BusinessException("Cannot find content area [" + contentAreaName
                    + "] in product [" + name + "]\n");
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

