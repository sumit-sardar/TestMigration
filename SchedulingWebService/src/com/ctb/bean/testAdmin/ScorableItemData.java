package com.ctb.bean.testAdmin;

import com.ctb.bean.CTBBean;
import com.ctb.bean.CTBBeanData;

public class ScorableItemData extends CTBBeanData {
	static final long serialVersionUID = 1L;

	/**
	 * Gets the array of scorable items beans
	 * 
	 * @return array of scorable items
	 */
	public ScorableItem[] getScorableItems() {
		CTBBean[] beans = this.getBeans();
		ScorableItem[] result = new ScorableItem[beans.length];
		for (int i = 0; i < beans.length; i++)
			result[i] = (ScorableItem) beans[i];
		return result;
	}

	/**
	 * Sets the array of scorable Item beans
	 * 
	 * @param scorableItem -
	 *            the array of scorable item beans
	 * @param pageSize -
	 *            The number of beans to include in one page of data
	 */
	public void setScorableItems(ScorableItem[] scorableItem, Integer pageSize) {
		this.setBeans(scorableItem, pageSize);
	}
}
