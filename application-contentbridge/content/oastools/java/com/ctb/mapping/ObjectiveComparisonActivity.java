package com.ctb.mapping;

/** a summary of activity upon inspection of an objective. */
class ObjectiveComparisonActivity {
	/** item_set.ext_cms_item_set_id */
	public String extCmsId; 
	/** hierarchy level */
	public int level;
	/** whether this represents a warning about possibly bad data */
	public boolean warning;
	/** any message associated with this activity */
	public String message;
	/** whether a warning condition was corrected during this activity */
	public boolean corrected;

	public ObjectiveComparisonActivity(String extCmsId, int level, boolean warning, String message, boolean corrected) {
		this.extCmsId = extCmsId;
		this.message = message;
		this.corrected = corrected;
		this.level = level;
		this.warning = warning;
	}

	public String toString() {
		StringBuffer text = new StringBuffer(extCmsId + "\t");

		if (message != null) {
			text.append("warning:  " + message);
			if (corrected) {
				text.append(" (corrected)");
			}
		} else {
			text.append("OK");
		}
		return text.toString();
	}
}
