package com.ctb.contentBridge.core.upload.processor;

import com.ctb.contentBridge.core.domain.Configuration;
import com.ctb.contentBridge.core.domain.ItemSet;
import com.ctb.contentBridge.core.upload.delegater.OasDelegater;
import com.ctb.contentBridge.core.upload.processor.CTBQueue.Element;
import com.ctb.contentBridge.core.util.FileUtil;
import com.ctb.contentBridge.core.util.FtpUtil;
import com.jcraft.jsch.Session;

public class FtpProcessorThread extends StopableThread {

	private CTBQueue queue;
	private boolean finish = false;
	private Configuration configuration;
	private ItemSet itemSetTD;
	private String ftpHost = null;
	private String ftpPass = null;
	private String ftpUser = null;
	private String ftpPort = null;
	private String delimiter = ",";



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

			// sftpSession = FtpUtil.getSFtpSession(configuration);
			ftpHost = configuration.getFtpHost();
			ftpPass = configuration.getFtpPassword();
			ftpUser = configuration.getFtpUser();
			ftpPort = configuration.getFtpPort();
			
			String ftpHostCont[] = ftpHost.split(delimiter);
			String ftpPassCont[] = ftpPass.split(delimiter);
			String ftpUserCont[] = ftpUser.split(delimiter);
			String ftpPortCont[] = ftpPort.split(delimiter);
			String sourceFile=null;
			String sourceDir=null;
            int counter = 0;
      
      
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
							sleep(1000);
						} catch (InterruptedException e) {

						}
						 sourceFile = (String) value.getVal();
							String destinationDir = configuration.getRemoteFilePath();
							 sourceDir = configuration.getLocalFilePath();
						
						 for (int count = 0; count < ftpHostCont.length; count++) {								
							counter = 0;
							 do{
								 try {
									sftpSession = FtpUtil.getSFtpSession(configuration,
											ftpHostCont[count], ftpUserCont[count],
											ftpPassCont[count],
											Integer.parseInt(ftpPortCont[count]));
									System.out.println("COUNTER::"+counter);
									counter = 20;
								} catch (Exception e) {
									counter++;
									if(counter == 20){
										throw e;
									}
								}
							 }while(counter < 20);

						System.out.println("Ftp started for file ["
								+ value.getVal() + "] for ftp Host["+ftpHostCont[count]+"]");
						FtpUtil.doSftp(sftpSession, destinationDir, sourceDir,
								sourceFile);
						System.out.println("Ftp completed for file ["
								+ value.getVal() + "]");
						
					}
						 FileUtil.deleteFile(sourceDir, sourceFile);
				}
				
				 
				
			}
    
			updateItemSet();
		} catch (Exception e) {
			System.out.println("exception1 "+e.getMessage());
			//StopableThread.foreStopped = true;
			e.printStackTrace();
		} finally {
			if (sftpSession != null) {
				FtpUtil.closeSFtpClient(sftpSession);
			}
		}

	}
	/*public void run() {
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

	}*/
	private void updateItemSet() throws Exception {
		OasDelegater delegater = null;
		try {
			delegater = OasDelegater.getInstance(configuration);
			delegater.updateItemSet(itemSetTD.getOasid(),
					configuration.getRepositoryURI());
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
