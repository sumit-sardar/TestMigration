package com.ctb.utils;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class CustomMosaicStrategy implements ExclusionStrategy {

	private String interactionType;

	public CustomMosaicStrategy(String interactionType) {
		this.setInteractionType(interactionType);
	}

	public boolean shouldSkipClass(Class<?> arg0) {
		return false;
	}

	public boolean shouldSkipField(FieldAttributes arg0) {
		if (MSSConstantUtils.INTERACTION_TYPE_MCQ
				.equalsIgnoreCase(this.interactionType))
			return (arg0.getName() == MSSConstantUtils.EXCLUTION_ATTR_1
			|| arg0.getName() == MSSConstantUtils.EXCLUTION_ATTR_2
			|| arg0.getName() == MSSConstantUtils.EXCLUTION_ATTR_3
			|| arg0.getName() == MSSConstantUtils.EXCLUTION_ATTR_4
			|| arg0.getName() == MSSConstantUtils.EXCLUTION_ATTR_5
			|| arg0.getName() == MSSConstantUtils.EXCLUTION_ATTR_8
			|| arg0.getName() == MSSConstantUtils.EXCLUTION_ATTR_9
			|| arg0.getName() == MSSConstantUtils.EXCLUTION_ATTR_10);
		else if (MSSConstantUtils.PARENT_DAS_INTERACTION
				.equalsIgnoreCase(this.interactionType))
			return (arg0.getName() == MSSConstantUtils.EXCLUTION_ATTR_1
					|| arg0.getName() == MSSConstantUtils.EXCLUTION_ATTR_2
					|| arg0.getName() == MSSConstantUtils.EXCLUTION_ATTR_3
					|| arg0.getName() == MSSConstantUtils.EXCLUTION_ATTR_4
					|| arg0.getName() == MSSConstantUtils.EXCLUTION_ATTR_5
					|| arg0.getName() == MSSConstantUtils.EXCLUTION_ATTR_6
					|| arg0.getName() == MSSConstantUtils.EXCLUTION_ATTR_7
					|| arg0.getName() == MSSConstantUtils.EXCLUTION_ATTR_8);
		else
			return (arg0.getName() == MSSConstantUtils.EXCLUTION_ATTR_1
			|| arg0.getName() == MSSConstantUtils.EXCLUTION_ATTR_2
			|| arg0.getName() == MSSConstantUtils.EXCLUTION_ATTR_5
			|| arg0.getName() == MSSConstantUtils.EXCLUTION_ATTR_7
			|| arg0.getName() == MSSConstantUtils.EXCLUTION_ATTR_9
			|| arg0.getName() == MSSConstantUtils.EXCLUTION_ATTR_10);
	}

	/**
	 * @return the interactionType
	 */
	public String getInteractionType() {
		return interactionType;
	}

	/**
	 * @param interactionType
	 *            the interactionType to set
	 */
	public void setInteractionType(String interactionType) {
		this.interactionType = interactionType;
	}

}
