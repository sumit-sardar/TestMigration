package com.ctb.contentcreator.processor;

import com.ctb.contentcreator.bin.Configuration;
import com.ctb.contentcreator.bin.ItemSet;
import com.ctb.contentcreator.delegater.OasDelegater;
import com.ctb.contentcreator.processor.CTBQueue.Element;
import com.ctb.contentcreator.utils.FileUtil;
import com.ctb.contentcreator.utils.FtpUtil;
import com.jcraft.jsch.Session;

public class FtpProcessorThread extends StopableThread {

	private CTBQueue queue;
	private boolean finish = false;
	private Configuration configuration;
	private ItemSet itemSetTD;

	public FtpProcessorThread(Configuration configuration, ItemSet itemSetTD,
			CTBQueue queue) {
		super("FtpProcessorThread:" + itemSetTD.getAdsid());
		this.configuration = configuration;
		this.queue = queue;
		this.itemSetTD = itemSetTD;
	}

	public void run() {
		Session sftpSession = null;
		try {
			sftpSession = FtpUtil.getSFtpSession(configuration);
			while (!foreStopped) {
				Element value = queue.get();
				if (value == null && finish) {
					break;
				} else if (value == null && !finish) {
					try {
						sleep(1000);
					} catch (InterruptedException e) {

					}
				} else {
					try {
						sleep(500);
					} catch (InterruptedException e) {

					}
					String sourceFile = (String) value.getVal();
					String destinationDir = configuration.getRemoteFilePath();
					String sourceDir = configuration.getLocalFilePath();

					System.out.println("Ftp started for file ["
							+ value.getVal() + "]");
					FtpUtil.doSftp(sftpSession, destinationDir, sourceDir,
							sourceFile);
					System.out.println("Ftp completed for file ["
							+ value.getVal() + "]");
					FileUtil.deleteFile(sourceDir, sourceFile);
				}
			}

			updateItemSet();
		} catch (Exception e) {
			StopableThread.foreStopped = true;
			e.printStackTrace();
		} finally {
			if (sftpSession != null) {
				FtpUtil.closeSFtpClient(sftpSession);
			}
		}

	}

	private void updateItemSet() throws Exception {
		OasDelegater delegater = null;
		try {
			delegater = OasDelegater.getInstance(configuration);
			delegater.updateItemSet(itemSetTD.getOasid(),configuration.getRepositoryURI());
		} finally {
			if (delegater != null)
				delegater.releaseResource();
		}
	}

	/**
	 * @param finish
	 *            the finish to set
	 */
	public void setFinish(boolean finish) {
		this.finish = finish;
	}

}
