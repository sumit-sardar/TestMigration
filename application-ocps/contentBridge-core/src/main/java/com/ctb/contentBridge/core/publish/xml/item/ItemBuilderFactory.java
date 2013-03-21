package com.ctb.contentBridge.core.publish.xml.item;

import com.ctb.contentBridge.core.publish.roundtrip.MediaReader;

public class ItemBuilderFactory {

	private ItemBuilderFactory() {
	}

	public static ItemBuilder getItemBuilderDummy() {
		return new ItemBuilderDummy();
	}

	public static ItemBuilder getItemBuilder(int validationMode) {
		return new ItemAssembler(validationMode);
	}

	public static ItemBuilder getItemBuilderRoundTrip(int validationMode,
			MediaReader mediaReader, ItemMediaCache itemMediaCache) {
		return new ItemBuilderRoundTrip(validationMode, mediaReader,
				itemMediaCache);
	}
}
