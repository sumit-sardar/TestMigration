package com.ctb.lexington.util;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class MosaicCustomStrategy implements ExclusionStrategy {

	private String interactionType;

	public MosaicCustomStrategy(String interactionType) {
		this.setInteractionType(interactionType);
	}

	@Override
	public boolean shouldSkipClass(Class<?> arg0) {
		return false;
	}

	@Override
	public boolean shouldSkipField(FieldAttributes arg0) {
		if (MosaicConstantUtils.INTERACTION_TYPE_MCQ
				.equalsIgnoreCase(this.interactionType))
			return (arg0.getName() == MosaicConstantUtils.EXCLUTION_ATTR_1
					|| arg0.getName() == MosaicConstantUtils.EXCLUTION_ATTR_2
					|| arg0.getName() == MosaicConstantUtils.EXCLUTION_ATTR_3
					|| arg0.getName() == MosaicConstantUtils.EXCLUTION_ATTR_4
					|| arg0.getName() == MosaicConstantUtils.EXCLUTION_ATTR_5);
		else if (MosaicConstantUtils.PARENT_DAS_INTERACTION
				.equalsIgnoreCase(this.interactionType))
			return (arg0.getName() == MosaicConstantUtils.EXCLUTION_ATTR_1
					|| arg0.getName() == MosaicConstantUtils.EXCLUTION_ATTR_2
					|| arg0.getName() == MosaicConstantUtils.EXCLUTION_ATTR_3
					|| arg0.getName() == MosaicConstantUtils.EXCLUTION_ATTR_4
					|| arg0.getName() == MosaicConstantUtils.EXCLUTION_ATTR_5
					|| arg0.getName() == MosaicConstantUtils.EXCLUTION_ATTR_6
					|| arg0.getName() == MosaicConstantUtils.EXCLUTION_ATTR_7);
		else
			return (arg0.getName() == MosaicConstantUtils.EXCLUTION_ATTR_1
					|| arg0.getName() == MosaicConstantUtils.EXCLUTION_ATTR_2
					|| arg0.getName() == MosaicConstantUtils.EXCLUTION_ATTR_5);
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
