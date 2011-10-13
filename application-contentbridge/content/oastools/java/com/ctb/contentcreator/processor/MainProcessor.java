package com.ctb.contentcreator.processor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.ctb.contentcreator.bin.Configuration;
import com.ctb.contentcreator.bin.ItemSet;

public class MainProcessor {

	private static Configuration configuration;
	public MainProcessor(Configuration configuration) {
		MainProcessor.configuration = configuration;
	}

	public void processor(HashMap itemSetTd) throws Exception {

		Iterator iterator = itemSetTd.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry entry = (Entry) iterator.next();
			createCompressedFile((Long) entry.getKey(), (ItemSet) (entry
					.getValue()));
		}

	}

	private static void createCompressedFile(Long itemSetTd, ItemSet itemsetTD) throws Exception {
		JobDelegater zipFileCreater = new JobDelegater(configuration, itemsetTD);
		zipFileCreater.start();

	}

}
