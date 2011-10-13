package com.ctb.contentcreator.processor;

import java.io.File;
import java.util.zip.ZipOutputStream;

import com.ctb.contentcreator.bin.Configuration;
import com.ctb.contentcreator.bin.ItemSet;
import com.ctb.contentcreator.processor.CTBQueue.Element;
import com.ctb.contentcreator.utils.ZipUtil;

public class ZippedFileCreaterThread extends StopableThread {
	private ItemSet itemSetTD;
	private CTBQueue queue;
	private boolean finish = false;
	private ZippedFileSeparatorThread fileSeparatorThread;
	private Configuration configuration ;

	public ZippedFileCreaterThread(Configuration configuration, ItemSet itemSetTD, CTBQueue queue) {
		super("ZippedFileCreaterThread:"+itemSetTD.getAdsid());
		this.itemSetTD = itemSetTD;
		this.queue = queue;
		this.configuration = configuration;
		fileSeparatorThread = new ZippedFileSeparatorThread(configuration, itemSetTD);
	}

	public void run() {
		boolean success = false;
		ZipOutputStream out = null;
		String tempFileName = configuration.getLocalFilePath()+File.separator+itemSetTD.getAdsid() + "$" + itemSetTD.getHash() + ".zip.tmp";
		String fileName = configuration.getLocalFilePath()+File.separator+itemSetTD.getAdsid() + "$" + itemSetTD.getHash() + ".zip";
		try {
			out = ZipUtil.createZipFile(tempFileName);
		
			while (!foreStopped) {
				Element value = queue.get();
				if (value == null && finish) {
					
					success = true;
					break;
				} else if(value == null && !finish) {
					try {
						sleep(1000);
					} catch (InterruptedException e) {
						
					}
				} else {
					ZipUtil.appendFileInZipFile(out, value.getId(), (byte[]) value.getVal());
				}
				
				
			}

		} catch (Exception e) {
			StopableThread.foreStopped = true;
			e.printStackTrace();
		} finally {
			ZipUtil.closeZipFile(out);
			if(!success) {
				new File(tempFileName).delete();
			} else {
				File dest = new File(fileName);
				new File(tempFileName).renameTo(dest);
				System.out.println("File ["+fileName+"] completed successfulley.");
				fileSeparatorThread.start();
			}
		}

	}

	/**
	 * @param stopped the stopped to set
	 */
	public void setFinish(boolean stopped) {
		this.finish = stopped;
	}
}
