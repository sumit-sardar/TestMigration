package com.ctb.test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import com.ctb.common.tools.ADSConfig;
import com.ctb.common.tools.SystemException;
import com.ctb.content.layout.ItemLayoutProcessor;
import com.ctb.xmlProcessing.subtest.DeliverableUnitProcessor;
import com.enterprisedt.net.ftp.FTPClient;
import com.enterprisedt.net.ftp.FTPTransferType;
import com.stgglobal.client.CommonClient;
import com.stgglobal.client.CommonClientServiceLocator;

public class TestInlineImage {

	public static String statusOk = "ok";
	public static String status_republish = "Item cannot be re-published as state is locked.";
	public static String status_republish_asset = "Asset cannot be re-published as it is being used and its state is locked.";
	
	public static ADSConfig adsConfig = new ADSConfig(new File("C:\\DEV.properties"));
	
	public static void main(String[] argv) {
		try {
			SAXBuilder builder = new SAXBuilder();
			//Reader in = new StringReader("<TestItem Sample=\"no\" SuppressScore=\"no\" FieldTest=\"no\" ScaleScore=\"no\"><Item ItemType=\"SR\" Grade=\"1\" ExternalSystem=\"na\" ID=\"TD.6.0_001\">          <Stem Audible=\"yes\" location=\"across\" widgetlayout=\"vertical\">            <Text halign=\"left\" border=\"no\">This item has an in-line image that is on the second line. The anchor item is on the first<Graphic inlineAlign=\"bottom\" valign=\"top\" halign=\"left\" ID=\"Icon-Ruler_demo\"><Flash FileName=\"C:/mappingdata/images/Demo/formula2.swf\"/></Graphic> line of text. </Text>          </Stem>          <SelectedResponse NumberAnswerChoices=\"4\" Audible=\"yes\" location=\"across\" stack=\"na\" SelectorPosition=\"default\" widgetlayout=\"vertical\">            <AnswerChoice Type=\"Correct\"><Text halign=\"left\" border=\"no\">Answer A</Text></AnswerChoice>            <AnswerChoice Type=\"Distractor\"><Text halign=\"left\" border=\"no\">Answer B</Text></AnswerChoice>            <AnswerChoice Type=\"Distractor\"><Text halign=\"left\" border=\"no\">Answer C</Text></AnswerChoice>            <AnswerChoice Type=\"Distractor\"><Text halign=\"left\" border=\"no\">Answer D</Text></AnswerChoice>          </SelectedResponse>        </Item></TestItem>");
			Reader in = new StringReader("<Item ItemType=\"SR\" Grade=\"1\" ExternalSystem=\"na\" ID=\"TD.6.0_001\">          " +
					"<Stem Audible=\"yes\" location=\"across\" widgetlayout=\"vertical\">      " +
					//"<Graphic inlineAlign=\"bottom\" valign=\"top\" halign=\"left\" ID=\"Icon-Ruler_demo\">" +
					//"<Flash FileName=\"C:/mappingdata/images/Demo/formula2.swf\"/></Graphic> " +
					"<Text halign=\"left\" border=\"no\">" +
					"This item has an in-line image that is on the second line. The anchor item is on the first" +
					"<Graphic inlineAlign=\"bottom\" valign=\"top\" halign=\"left\" ID=\"Icon-Ruler_demo\">" +
					"<Flash FileName=\"C:/mappingdata/images/Demo/formula2.swf\"/></Graphic> " +
					"line of text. </Text>          </Stem>          <SelectedResponse NumberAnswerChoices=\"4\" Audible=\"yes\" location=\"across\" stack=\"na\" SelectorPosition=\"default\" widgetlayout=\"vertical\">            <AnswerChoice Type=\"Correct\"><Text halign=\"left\" border=\"no\">Answer A</Text></AnswerChoice>            <AnswerChoice Type=\"Distractor\"><Text halign=\"left\" border=\"no\">Answer B</Text></AnswerChoice>            <AnswerChoice Type=\"Distractor\"><Text halign=\"left\" border=\"no\">Answer C</Text></AnswerChoice>            <AnswerChoice Type=\"Distractor\"><Text halign=\"left\" border=\"no\">Answer D</Text></AnswerChoice>          </SelectedResponse>        </Item>");
			Document doc = null;
			Element rootElement = null;

			doc = builder.build(in);
			rootElement = doc.getRootElement();
			
			String itemType = rootElement.getAttributeValue("ItemType");
			Element itemLml ;	
			ArrayList assetList = new ArrayList();

			ItemLayoutProcessor itemProcessor = new ItemLayoutProcessor(rootElement, "500", 1, 12, false, new ArrayList(), true);
			itemProcessor.useStimulusDisplayID = true;
			itemLml = itemProcessor.layoutItem();
			DeliverableUnitProcessor.addSizeToContent( itemProcessor.getDownloadSize() );
			ItemLayoutProcessor.getAsset(itemLml, assetList);
			ItemLayoutProcessor.modifyItemLMLForADS_Puslishing(itemLml, itemType);
					
			//Transform layout
			XMLOutputter xmloutput = new XMLOutputter();
			String xmlStr = xmloutput.outputString(itemLml);
			
			xmlStr =  "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ads_publish_request><publish_item>" + xmlStr + "</publish_item> </ads_publish_request>";

			xmlStr = xmlStr.replaceAll("&lt;", "<");
			xmlStr = xmlStr.replaceAll("&gt;", ">");
			xmlStr = xmlStr.replaceAll("&amp;", "&");
			xmlStr = xmlStr.replaceAll("&nbsp;", "&#160;");
			xmlStr = xmlStr.replaceAll(" & ", " &amp; ");		
			
			System.out.println(xmlStr);
			
			String xmlStr2 = xmlStr;		
			
		     if (assetList != null) 
			 {
		     	sendfiles_ftp(assetList);
				publishAssets(assetList);
		     }
			//Publish Itemt to ADS - publish "xml"
		    CommonClientServiceLocator clientLocator = new CommonClientServiceLocator(adsConfig.getWsClient());
				
			CommonClient client = clientLocator.getCommonClient();
			
	     	String responseStr = client.callUploadItem(xmlStr);
	     	if (responseStr == null)
	    	{
	    		throw new SystemException("Error in Publishing Item. Response is null. XML :" + xmlStr2 );
	    	}
	    	
	    	ByteArrayInputStream bais = new ByteArrayInputStream( responseStr.toString().getBytes());
	        org.jdom.input.SAXBuilder saxBuilder = new org.jdom.input.SAXBuilder();
	        org.jdom.Document itemDoc = saxBuilder.build( bais );
	        Element responseElm = itemDoc.getRootElement();
	    	
	    	Element response = ItemLayoutProcessor.extractSingleElement(".//response", responseElm);
	    	Attribute status = response.getAttribute("status");
	    	String statusStr = status.getValue();
	    	if (!statusStr.equals(statusOk))
	    	{
	   			Element msg = ItemLayoutProcessor.extractSingleElement(".//msg", response);
	   			if (!(msg.getText()).equals(status_republish))
	   			{
	   				throw new SystemException("Error in Publishing item. Status = "+statusStr + " Error message: " + msg.getText()+ 
	   						"XML : " + xmlStr2);
	    		}
	    	}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void sendfiles_ftp( List inputFiles ) throws Exception
    {
		String destinationPath = "/web/sites/wldomains/ads/assets/";
		
	    FTPClient ftpClient = new FTPClient();
	    ftpClient.setRemoteHost(adsConfig.getFtpHost());   
	    ftpClient.connect();
	    if ( ftpClient.connected() )
	    {
	        ftpClient.login( adsConfig.getFtpUser(), adsConfig.getFtpPassword() );
	        ftpClient.setType( FTPTransferType.BINARY );
	        Iterator iter = inputFiles.iterator();
	        while (iter.hasNext())
	        	{
	        	String sourceFile = (String)(iter.next());
	            try
	                    {
	            			String img = getImage(sourceFile);
	            			sourceFile = "c:\\Demo\\formula2.swf";
	               			////sourceFile = "c:/images/" + sourceFile.substring(sourceFile.indexOf("/images/") + 8);
	               			//sourceFile = "c:\\mappingdata\\images\\" + img;
	            			sourceFile = sourceFile.replaceAll("%20", " ");
	            			String destination = destinationPath + img;
	            			destination = destination.replaceAll("%20", " ");
	                        ftpClient.put( sourceFile, destination );
	                    }
	                    catch( Exception e )
	                    {
	                        System.err.println("Exception : "  + e.getMessage());
	                        e.printStackTrace();
	                    }
	            }
	        }
	    }

	public static String getImage( String srcImage )
	{
		String img = srcImage;
		int pos = srcImage.lastIndexOf( "/");
		img = img.substring( pos + 1, img.length() );
		return img;
	}
	
	public static void publishAssets (List inputFiles) throws Exception
	{
		String destinationPath = "/web/sites/wldomains/ads/assets/";
		
		CommonClientServiceLocator clientLocator = new CommonClientServiceLocator(adsConfig.getWsClient());
		
		CommonClient client = clientLocator.getCommonClient();
     	 	
		String request = "";
			
			Iterator iter = inputFiles.iterator();
            while (iter.hasNext())
            	{
            	String img = getImage((String)iter.next());
            	String destination = destinationPath + img;
            	
               	request = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <ads_publish_request>" +
			 		"<publish_asset> <asset ident= \""+ getImageId(img) +
					"\" imagtype=\"image/" + getImageType(img) + "\">" +
			 		  " <file_location uri=\"" + destination + "\" />"+ 
					   "</asset> </publish_asset> </ads_publish_request>";
            	
            	String responseStr = client.callUploadAsset(request);
            	
            	if (responseStr == null)
            	{
            		throw new SystemException("Error in Publishing Asset. Response is null. " );
            	}
            	
            	ByteArrayInputStream bais = new ByteArrayInputStream( responseStr.toString().getBytes());
                org.jdom.input.SAXBuilder saxBuilder = new org.jdom.input.SAXBuilder();
                org.jdom.Document itemDoc = saxBuilder.build( bais );
                Element responseElm = itemDoc.getRootElement();
            	
            	Element response = ItemLayoutProcessor.extractSingleElement(".//response", responseElm);
            	Attribute status = response.getAttribute("status");
            	String statusStr = status.getValue();
            	if (!statusStr.equals(statusOk))
            	{
           			Element msg = ItemLayoutProcessor.extractSingleElement(".//msg", response);
           			if (!(msg.getText()).equals(status_republish_asset))
           			{
           				throw new SystemException("Error in Publishing Asset. Status = "+statusStr + " Error message: " + msg.getText());
            		}
               	}
            	}
	}
	
	public static String getImageId( String srcImage )
    {
		int pos = srcImage.lastIndexOf( ".");
		return srcImage.substring( 0, pos);
		
	}
	public static String getImageType(String srcImage)
	{
		int pos = srcImage.lastIndexOf( ".");
		return srcImage.substring(pos+1, srcImage.length());
	}
}