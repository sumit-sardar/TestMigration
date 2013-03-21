package com.ctb.contentBridge.core.upload.processor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.ctb.contentBridge.core.domain.Configuration;
import com.ctb.contentBridge.core.domain.ItemSet;

public class MainProcessor {

	private Configuration configuration;

	public MainProcessor(Configuration configuration) {
		this.configuration = configuration;
	}

	public void processor(HashMap itemSetTd) throws Exception {
		System.out.println("inside process-->>"+itemSetTd.size());

		Iterator iterator = itemSetTd.entrySet().iterator();
		System.out.println("inside main process"+itemSetTd.entrySet().size());
		while (iterator.hasNext()) {
			Map.Entry entry = (Entry) iterator.next();
			ItemSet itemSet = (ItemSet) (entry.getValue());
			System.out.println("inside main process1"+itemSet.hashCode()+"\thash "+itemSet.getHash());
			if(itemSet.getHash() != null) {
				createCompressedFile((Long) entry.getKey(),itemSet);
			}
			/*createCompressedFile((Long) entry.getKey(),
					(ItemSet) (entry.getValue()));*/
		}

	}

	private void createCompressedFile(Long itemSetTd, ItemSet itemsetTD)
			throws Exception {
		System.out.println("inside compressed file");
		JobDelegater zipFileCreater = new JobDelegater(configuration, itemsetTD);
		zipFileCreater.start();
	}

}