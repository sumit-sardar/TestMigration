package com.ctb.contentBridge.core.upload.processor;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;

import com.ctb.contentBridge.core.domain.Configuration;
import com.ctb.contentBridge.core.domain.Item;
import com.ctb.contentBridge.core.domain.ItemSet;
import com.ctb.contentBridge.core.upload.dao.OasDao;
import com.ctb.contentBridge.core.upload.delegater.AdsDelegater;
import com.ctb.contentBridge.core.util.ConnectionUtil;

public class JobDelegater extends StopableThread {

	private ItemSet itemSetTD;
	private Configuration configuration;

	public JobDelegater(Configuration configuration, ItemSet itemsetTD) {
		super(JobDelegater.class.getName() + "-" + itemsetTD.getAdsid());
		this.itemSetTD = itemsetTD;
		this.configuration = configuration;
	}

	public void run() {

		CTBQueue queue = new CTBQueue();
		ZippedFileCreaterThread createrThread = new ZippedFileCreaterThread(
				configuration, itemSetTD, queue);
		createrThread.start();
		AdsDelegater delegater = null;/*AdsDelegater.getInstance(config)*/; 
		try {
			String sPropFilePath = System.getProperty("PROPERTIES_FILE_PATH");
			Configuration config = new Configuration();
			config.load(new File(sPropFilePath));
			delegater = AdsDelegater.getInstance(config); 
			ArrayList itmsArray = itemSetTD.getChildren();
			byte[] item = null;
			for (Iterator it = itmsArray.iterator(); it.hasNext();) {
				Item child = (Item) it.next();
				item = getItem( delegater, child.getAdsid());/*getItem(child.getAdsid());*/
				if(item != null) {
					queue.put(child.getAdsid() + ".ecp",item);
				}
				/*queue.put(child.getAdsid() + ".ecp",
						getItem( delegater, child.getAdsid()));*/
				if (foreStopped)
					break;
			}
			
			byte[] subtest = null;
			System.out.println("job deligator");
			subtest = getSubTest( delegater, itemSetTD.getAdsid());/*getSubTest(itemSetTD.getAdsid());*/
			if(subtest != null) {
				queue.put(itemSetTD.getAdsid() + ".eam",subtest);
			}
			/*queue.put(itemSetTD.getAdsid() + ".eam",
					getSubTest( delegater, itemSetTD.getAdsid()));*/
			createrThread.setFinish(true);

		} catch (Exception e) {
			System.out.println("exception "+e.getMessage());
			ZippedFileCreaterThread.foreStopped = true;
			e.printStackTrace();
		} 
		finally {
			if(delegater != null) {
				delegater.releaseResource();
			}
			/*ConnectionUtil.closeOASConnection();*/
		}
		 
	}

	/*private byte[] getItem( AdsDelegater delegater, long itemId)
			throws Exception {
		Connection connection = ConnectionUtil.getOASConnection(configuration);
		return OasDao.getItemBlob(connection, itemId);
	}

	private byte[] getSubTest( AdsDelegater delegater, long itemSetId)
			throws Exception {
		Connection connection = ConnectionUtil.getOASConnection(configuration);
		return OasDao.getSubtestBlob(connection, itemSetId);
	}*/
	private byte[] getItem(AdsDelegater delegater, long itemId) throws Exception {
		return delegater.getItemByteArray(itemId);
	}
	
	private byte[] getSubTest(AdsDelegater delegater, long itemSetId) throws Exception {
		return delegater.getSubtestByteArray(itemSetId);
	}
}
