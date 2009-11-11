package com.ctb.lexington.data;

import java.io.Serializable;

/**
 * Value Object for information relating to reporting.
 *
 * @author Jon Becker, jonathan_becker@ctb.com
 * @version $Id$
 */
public class ReportDetailVO implements Serializable {

	private String type;
	private String name;
	private String description;
	private boolean available;
	
    public static final String VO_LABEL = "com.ctb.lexington.data.ReportDetailVO";

    public ReportDetailVO() {}

    public String getType() {
        return type;
    }

    public void setType(String type_) {
        this.type = type_;
    }

    public String getName() {
        return name;
    }

    public void setName(String name_) {
        this.name = name_;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description_) {
        this.description = description_;
    }

    public boolean getAvailable() {
        return available;
    }

    public void setAvailable(boolean available_) {
        this.available = available_;
    }

}
