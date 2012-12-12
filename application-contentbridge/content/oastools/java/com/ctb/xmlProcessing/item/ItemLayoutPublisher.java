/*
 * Created on Jan 20, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ctb.xmlProcessing.item;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.io.*;

import javax.xml.transform.*;
import javax.xml.transform.stream.*;

import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import com.ctb.common.tools.ADSConfig;
import com.ctb.common.tools.SystemException;
import com.ctb.content.layout.ItemLayoutProcessor;
import com.ctb.xmlProcessing.subtest.DeliverableUnitProcessor;
import com.enterprisedt.net.ftp.FTPClient;
import com.enterprisedt.net.ftp.FTPTransferType;
import com.stgglobal.client.CommonClient;
import com.stgglobal.client.CommonClientServiceLocator;
import com.jcraft.jsch.*;

/**
 * @author Sudha_Manimaran
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ItemLayoutPublisher {
	
	private static Logger logger = Logger.getLogger(ItemLayoutPublisher.class);

	String destinationPath = "/local/apps/oas/ads/assets/";
	String destinationPkgPath = "/local/apps/oas/ads/assets/html/";
	String sourcePackagePath = "/iwmnt/default/main/OAS/WORKAREA/highwire/images/TEAssets/";
	String statusOk = "ok";
	String status_republish = "Item cannot be re-published as state is locked.";
	String status_republish_asset = "Asset cannot be re-published as it is being used and its state is locked.";
	ArrayList unicodeList = null;
	ADSConfig adsConfig = null;
	String maxPanelWidth;
	boolean includeAcknowledgment = false;
	CommonClientServiceLocator clientLocator = null;

	public ItemLayoutPublisher(ArrayList unicodeList, ADSConfig adsConfig,
			String maxPanelWidth, String includeAcknowledgment) {
		this.unicodeList = unicodeList;
		this.adsConfig = adsConfig;
		this.maxPanelWidth = maxPanelWidth;
		if (includeAcknowledgment.equals("yes"))
			this.includeAcknowledgment = true;
		clientLocator = new CommonClientServiceLocator(adsConfig.getWsClient());

	}

	public Element publishLayout(Element rootElement) {
		//logger.info("Inside publishLayout() ....");
		String itemType = rootElement.getAttributeValue("ItemType");
		String itemId = rootElement.getAttributeValue("ID");
		Element itemLml;
		ArrayList assetList = new ArrayList();
		ArrayList htmlList = new ArrayList();
		try {
			// Call layout

			ItemLayoutProcessor itemProcessor = new ItemLayoutProcessor(
					rootElement, maxPanelWidth, 1, 12, false, unicodeList,
					includeAcknowledgment);
			itemProcessor.useStimulusDisplayID = true;
			itemLml = itemProcessor.layoutItem();
			DeliverableUnitProcessor.addSizeToContent(itemProcessor
					.getDownloadSize());
			//logger.info("Before zipfile creation ....");
			//added by for NEW item
			if ("IN".equals(itemType))
				ItemLayoutProcessor.getPackageAsset(itemLml, htmlList,itemId,adsConfig);
			else
				ItemLayoutProcessor.getAsset(itemLml, assetList);
			//end
			//logger.info("After zipfile creation ....");
			ItemLayoutProcessor.modifyItemLMLForADS_Puslishing(itemLml,
					itemType);

			// Transform layout
			XMLOutputter xmloutput = new XMLOutputter();
			String xmlStr = xmloutput.outputString(itemLml);

			xmlStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ads_publish_request><publish_item>"
					+ xmlStr + "</publish_item> </ads_publish_request>";

			xmlStr = xmlStr.replaceAll("&lt;", "<");
			xmlStr = xmlStr.replaceAll("&gt;", ">");
			xmlStr = xmlStr.replaceAll("&amp;", "&");
			xmlStr = xmlStr.replaceAll("&nbsp;", "&#160;");
			xmlStr = xmlStr.replaceAll(" & ", " &amp; ");
			String xmlStr2 = xmlStr;
			
			//logger.info("xmlStr >> "+xmlStr);
			if (assetList != null && assetList.size() > 0) {
				
				  if(adsConfig.isSftp()){ this.sendfiles_sftp(assetList); }
				 else { this.sendfiles_ftp(assetList); }
				 
				this.publishAssets(assetList);
			}
			if (htmlList != null && htmlList.size() > 0) {
				
				  if(adsConfig.isSftp()){ this.sendPkgfiles_sftp(itemId+"zip"); }
				  else { this.sendPkgfiles_ftp(itemId+"zip"); }
				 this.publishPkgAssets(htmlList,itemId);
				 //logger.info("after publishPkgAssets for TE Items...");
			}
			
			// Publish Item to ADS - publish "xml"
			CommonClient client = clientLocator.getCommonClient();
			//logger.info("before client.callUploadItem(xmlStr)....");
			String responseStr = client.callUploadItem(xmlStr);
			//logger.info("responseStr >> "+responseStr);
			if (responseStr == null) {
				throw new SystemException(
						"Error in Publishing Item. Response is null. XML :"
								+ xmlStr2);
			}
			ByteArrayInputStream bais = new ByteArrayInputStream(responseStr
					.toString().getBytes());
			org.jdom.input.SAXBuilder saxBuilder = new org.jdom.input.SAXBuilder();
			org.jdom.Document itemDoc = saxBuilder.build(bais);
			Element responseElm = itemDoc.getRootElement();

			Element response = ItemLayoutProcessor.extractSingleElement(
					".//response", responseElm);
			Attribute status = response.getAttribute("status");
			String statusStr = status.getValue();
			//logger.info("statusStr : "+statusStr);
			if (!statusStr.equals(statusOk)) {
				Element msg = ItemLayoutProcessor.extractSingleElement(
						".//msg", response);
				if (!(msg.getText()).equals(status_republish)) {
					throw new SystemException(
							"Error in Publishing item. Status = " + statusStr
									+ " Error message: " + msg.getText()
									+ "XML : " + xmlStr2);
				}
			}
			return itemLml;
		} catch (Exception e) {
			throw new SystemException("Error in ItemLayoutPublisher "
					+ e.getMessage());
		}

	}

	public void publishAssets(List inputFiles) throws Exception {

		CommonClient client = clientLocator.getCommonClient();

		String request = "";

		Iterator iter = inputFiles.iterator();
		while (iter.hasNext()) {
			String img = getImage((String) iter.next());
			String destination = destinationPath + img;

			request = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <ads_publish_request>"
					+ "<publish_asset> <asset ident= \""
					+ getImageId(img)
					+ "\" imagtype=\"image/"
					+ getImageType(img)
					+ "\">"
					+ " <file_location uri=\""
					+ destination
					+ "\" />"
					+ "</asset> </publish_asset> </ads_publish_request>";

			String responseStr = client.callUploadAsset(request);

			if (responseStr == null) {
				throw new SystemException(
						"Error in Publishing Asset. Response is null. ");
			}

			ByteArrayInputStream bais = new ByteArrayInputStream(responseStr
					.toString().getBytes());
			org.jdom.input.SAXBuilder saxBuilder = new org.jdom.input.SAXBuilder();
			org.jdom.Document itemDoc = saxBuilder.build(bais);
			Element responseElm = itemDoc.getRootElement();

			Element response = ItemLayoutProcessor.extractSingleElement(
					".//response", responseElm);
			Attribute status = response.getAttribute("status");
			String statusStr = status.getValue();
			if (!statusStr.equals(statusOk)) {
				Element msg = ItemLayoutProcessor.extractSingleElement(
						".//msg", response);
				if (!(msg.getText()).equals(status_republish_asset)) {
					throw new SystemException(
							"Error in Publishing Asset. Status = " + statusStr
									+ " Error message: " + msg.getText());
				}
			}
		}
	}
	public void publishPkgAssets(List inputFiles,String itemID) throws Exception {
		//logger.info("Inside publishPkgAssets method...");
		CommonClient client = clientLocator.getCommonClient();

		String request = "";

		Iterator iter = inputFiles.iterator();
		
			
		String destinationPkgPath = this.destinationPkgPath + itemID+"zip";
			request = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <ads_publish_request>"
					+ "<publish_asset> <asset ident= \""
					+ itemID
					+ "\" pkgtype=\"zip"					
					+ "\">"
					+ " <file_location uri=\""
					+ destinationPkgPath
					+ "\" />"
					+ "</asset> </publish_asset> </ads_publish_request>";
			//logger.info("request"+request);
			String responseStr = client.callUploadAsset(request);
			//logger.info("responseStr >> "+responseStr);
			if (responseStr == null) {
				throw new SystemException(
						"Error in Publishing Asset. Response is null. ");
			}

			ByteArrayInputStream bais = new ByteArrayInputStream(responseStr
					.toString().getBytes());
			org.jdom.input.SAXBuilder saxBuilder = new org.jdom.input.SAXBuilder();
			org.jdom.Document itemDoc = saxBuilder.build(bais);
			Element responseElm = itemDoc.getRootElement();

			Element response = ItemLayoutProcessor.extractSingleElement(
					".//response", responseElm);
			Attribute status = response.getAttribute("status");
			String statusStr = status.getValue();
			//logger.info("statusStr >> "+statusStr);
			if (!statusStr.equals(statusOk)) {
				Element msg = ItemLayoutProcessor.extractSingleElement(
						".//msg", response);
				if (!(msg.getText()).equals(status_republish_asset)) {
					throw new SystemException(
							"Error in Publishing Asset. Status = " + statusStr
									+ " Error message: " + msg.getText());
				}
			}
		
	}

	public void sendfiles_sftp(List inputFiles) throws Exception {

		JSch jsch = new JSch();
		Session session = null;
		ChannelSftp sftpChannel = null;
		Properties properties = new Properties();
		properties.put("StrictHostKeyChecking", "no");
		properties.put("compression.s2c", "none");
		properties.put("compression.c2s", "none");

		String ftpHost = adsConfig.getFtpHost();
		String ftpUser = adsConfig.getFtpUser();
		String ftpPass = adsConfig.getFtpPassword();
		int port = adsConfig.getPort();

		logger.info("Connecting to server:" + ftpHost);
		try {
			session = jsch.getSession(ftpUser, ftpHost, port);
			session.setConfig(properties);
			session.setPassword(ftpPass);
			// jsch.setKnownHosts("/export/home/iwuser/.ssh/known_hosts");
			session.connect();
			Channel channel = session.openChannel("sftp");
			channel.connect();
			sftpChannel = (ChannelSftp) channel;

			Iterator iter = inputFiles.iterator();
			while (iter.hasNext()) {
				String sourceFile = (String) (iter.next());
				try {
					String img = getImage(sourceFile);
					sourceFile = "/default/main/OAS/WORKAREA/highwire/images/"
							+ sourceFile.substring(sourceFile
									.indexOf("/images/") + 8);
					sourceFile = sourceFile.replaceAll("%20", " ");
					// sourceFile = "c:\\mappingdata\\images\\" + img;
					String destination = destinationPath + img;
					// sftpChannel.cd(destinationPath);
					sftpChannel.put(sourceFile, destination);

				} catch (SftpException e) {
					System.err.println("Exception : " + e.getMessage());
					logger.error("Exception : " + e.getMessage());
					e.printStackTrace();
				}

			}

		} finally {
			if (sftpChannel != null) {
				sftpChannel.exit();
			}
			if (session != null) {
				session.disconnect();
			}

		}

	}
	public void sendPkgfiles_sftp(String inputFile) throws Exception {

		JSch jsch = new JSch();
		Session session = null;
		ChannelSftp sftpChannel = null;
		Properties properties = new Properties();
		properties.put("StrictHostKeyChecking", "no");
		properties.put("compression.s2c", "none");
		properties.put("compression.c2s", "none");

		String ftpHost = adsConfig.getFtpHost();
		String ftpUser = adsConfig.getFtpUser();
		String ftpPass = adsConfig.getFtpPassword();
		int port = adsConfig.getPort();

		logger.info("Connecting to server: " + ftpHost);
		//logger.info("Inside sftp block....");
		//logger.info("ftpHost: "+ftpHost);
		//logger.info("ftpUser: "+ftpUser);
		//logger.info("ftpPass: "+ftpPass);
		//logger.info("inputFile: "+inputFile);
		try {
			session = jsch.getSession(ftpUser, ftpHost, port);
			session.setConfig(properties);
			session.setPassword(ftpPass);
			// jsch.setKnownHosts("/export/home/iwuser/.ssh/known_hosts");
			session.connect();
			Channel channel = session.openChannel("sftp");
			channel.connect();
			sftpChannel = (ChannelSftp) channel;

		
				try {					
					//logger.info("Current Local Path: "+sftpChannel.lpwd());
					//logger.info("Current Remote Path: "+sftpChannel.pwd());
					sftpChannel.lcd(sourcePackagePath);
					sftpChannel.cd(destinationPkgPath);
					/*inputFile = "/default/main/OAS/WORKAREA/highwire/html/"+inputFile;
							+ inputFile.substring(inputFile
									.indexOf("/html/") + 8);*/
					String sourceFile = sourcePackagePath + inputFile;
					//logger.info("sourceFile >>"+sourceFile);
//					inputFile = inputFile.replaceAll("%20", " ");
					// sourceFile = "c:\\mappingdata\\images\\" + img;
					String destination = destinationPkgPath + inputFile;
					//logger.info("destination >>"+destination);
					// sftpChannel.cd(destinationPath);
					sftpChannel.put(sourceFile, destination);

				} catch (SftpException e) {
					System.err.println("Exception : " + e.getMessage());
					logger.error("Exception : " + e.getMessage());
					e.printStackTrace();
				}

		

		} finally {
			if (sftpChannel != null) {
				sftpChannel.exit();
			}
			if (session != null) {
				session.disconnect();
			}

		}

	}

	public void sendfiles_ftp(List inputFiles) throws Exception {

		FTPClient ftpClient = new FTPClient();
		ftpClient.setRemoteHost(adsConfig.getFtpHost());
		ftpClient.connect();
		if (ftpClient.connected()) {
			ftpClient.login(adsConfig.getFtpUser(), adsConfig.getFtpPassword());
			ftpClient.setType(FTPTransferType.BINARY);
			Iterator iter = inputFiles.iterator();
			while (iter.hasNext()) {
				String sourceFile = (String) (iter.next());
				try {
					String img = getImage(sourceFile);
					sourceFile = "/default/main/OAS/WORKAREA/highwire/images/"
							+ sourceFile.substring(sourceFile
									.indexOf("/images/") + 8);
					// //sourceFile = "c:/images/" +
					// sourceFile.substring(sourceFile.indexOf("/images/") + 8);
					// sourceFile = "c:\\mappingdata\\images\\" + img;
					sourceFile = sourceFile.replaceAll("%20", " ");
					String destination = destinationPath + img;
					destination = destination.replaceAll("%20", " ");
					ftpClient.put(sourceFile, destination);
				} catch (Exception e) {
					System.err.println("Exception : " + e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}
	public void sendPkgfiles_ftp(String inputFile) throws Exception {

		FTPClient ftpClient = new FTPClient();
		//logger.info("Inside ftp block....");
		//logger.info("ftpHost: "+adsConfig.getFtpHost());
		//logger.info("ftpHost: "+adsConfig.getFtpPassword());
		ftpClient.setRemoteHost(adsConfig.getFtpHost());
		ftpClient.connect();
		if (ftpClient.connected()) {
			ftpClient.login(adsConfig.getFtpUser(), adsConfig.getFtpPassword());
			ftpClient.setType(FTPTransferType.BINARY);
			String sourceFile=null;
			try{
				/*inputFile = "/default/main/OAS/WORKAREA/highwire/html/"+inputFile;
				+ inputFile.substring(inputFile
						.indexOf("/html/") + 8);*/
					//sourceFile = "/default/main/OAS/WORKAREA/highwire/images/TEAsset/"+inputFile;
					sourceFile = sourcePackagePath + inputFile;
					// //sourceFile = "c:/images/" +
					// sourceFile.substring(sourceFile.indexOf("/images/") + 8);
					// sourceFile = "c:\\mappingdata\\images\\" + img;
					//sourceFile = inputFile.replaceAll("%20", " ");
					String destination = destinationPkgPath + inputFile;
					destination = destination.replaceAll("%20", " ");
					ftpClient.put(sourceFile, destination);
				} catch (Exception e) {
					System.err.println("Exception : " + e.getMessage());
					e.printStackTrace();
				}
		
		}
	}

	public void transform(Reader xml, Reader xsl, Writer out)
			throws TransformerException {
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer(new StreamSource(xsl));
		transformer.transform(new StreamSource(xml), new StreamResult(out));
	}

	public String getImage(String srcImage) {
		String img = srcImage;
		int pos = srcImage.lastIndexOf("/");
		img = img.substring(pos + 1, img.length());
		return img;
	}

	public String getImageId(String srcImage) {
		int pos = srcImage.lastIndexOf(".");
		return srcImage.substring(0, pos);

	}

	public String getImageType(String srcImage) {
		int pos = srcImage.lastIndexOf(".");
		return srcImage.substring(pos + 1, srcImage.length());
	}
}
