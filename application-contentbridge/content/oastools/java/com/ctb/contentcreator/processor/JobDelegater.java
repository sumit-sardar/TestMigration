package com.ctb.contentcreator.processor;

import java.util.ArrayList;
import java.util.Iterator;

import com.ctb.contentcreator.bin.Configuration;
import com.ctb.contentcreator.bin.Item;
import com.ctb.contentcreator.bin.ItemSet;
import com.ctb.contentcreator.delegater.AdsDelegater;

public class JobDelegater extends StopableThread {

	private ItemSet itemSetTD;
	private Configuration configuration;
	

	public JobDelegater(Configuration configuration, ItemSet itemsetTD) {
		super( JobDelegater.class.getName()+"-"+ itemsetTD.getAdsid());
		this.itemSetTD = itemsetTD;
		this.configuration = configuration;

	}

	public void run() {

		CTBQueue queue = new CTBQueue();
		ZippedFileCreaterThread createrThread = new ZippedFileCreaterThread(configuration,
				itemSetTD, queue);
		createrThread.start();
		AdsDelegater delegater = AdsDelegater.getInstance(configuration);
		try {
			ArrayList itmsArray = itemSetTD.getChildren();
			for (Iterator it = itmsArray.iterator(); it.hasNext();) {
				Item child = (Item) it.next();
				queue.put(child.getAdsid() + ".ecp", getItem(delegater, child.getAdsid()));
				if(foreStopped)
					break;
			}
			queue.put(itemSetTD.getAdsid()+".eam", getSubTest(delegater, itemSetTD.getAdsid()) );
			createrThread.setFinish(true);
			
		} catch (Exception e) {
			ZippedFileCreaterThread.foreStopped = true;
			e.printStackTrace();
		} finally {
			delegater.releaseResource();
		}

	}

	private byte[] getItem(AdsDelegater delegater, long itemId) throws Exception {
		return delegater.getItemByteArray(itemId);
	}
	
	private byte[] getSubTest(AdsDelegater delegater, long itemSetId) throws Exception {
		return delegater.getSubtestByteArray(itemSetId);
	}

}
