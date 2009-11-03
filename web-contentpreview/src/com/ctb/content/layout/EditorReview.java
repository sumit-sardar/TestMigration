package com.ctb.content.layout; 

import com.enterprisedt.net.ftp.FTPClient;
import com.enterprisedt.net.ftp.FTPTransferType;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.jdom.Element;
import sun.net.www.content.text.PlainTextInputStream;

public class EditorReview 
{ 
    public static boolean forReal = true;
    public static String imageFolderCQA = "/export/data/contentdemo/images";  
    public static String imageFolderLocal = "c:/contentdemo/images";  
    public static ArrayList unicodeList = null;
    
    public static String writeAssessmentFile( String assessmentFile, String ID ) throws Exception
    {
        // Location : /web/sites/content/home/editor_review
        if ( assessmentFile == null )
            return null;
    	String assessmentString = getAssessment( assessmentFile );
        writeHTM( assessmentString, "/web/sites/content/home/editor_review/", ID
                            , "http://coruscant:9000/contentPreviewWeb/upload.do\" " );
        return "http://coruscant:9000/editor_review/" + ID + ".htm";
    }
     
    //contentqa2.ctb.com
    public static void writeHTM( String assessmentString, String locaton, String testName
                        , String action ) throws Exception
    {
        StringBuffer sb = new StringBuffer();
        sb.append( "<html><head><h2>Online Content Preview</h2></head><body onLoad=\"document.forms[0].submit()\"><P>Please check the preview Window.</P><form target=\" _blank\" name=\"uploadForm\" action=\""
                    + action + "method=\"post\"><input type=\"hidden\" name=\"assessmentXML\" value=\"");
        assessmentString = URLEncoder.encode( assessmentString );
        sb.append( assessmentString );
        sb.append( "\"></form></body></html>" );
        String fileLocation = locaton + testName + ".htm";
        File exportFile = new File( fileLocation );
    	BufferedWriter fileOut = new BufferedWriter( new FileWriter( exportFile ));
        fileOut.write( sb.toString() );
        fileOut.close();
    }
    
    public static void writeXMLtoCQA( String machineHost, String user, String password
                                , Element lml, String locaton, String testName) throws Exception
    {
        String fileLocation = locaton + testName + ".xml";
        if ( forReal ) 
        {
            String tempFolder = "tmp";
            File tempfile = File.createTempFile( "src_", ".tmp", new File( tempFolder ) );
            String tempFilePath = tempfile.getPath();
            FileWriter fileOut = new FileWriter( tempfile );
            fileOut.write( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" + LMLRenderer.xmlToString( lml ) );
            fileOut.close();
            FTPClient ftpClient = new FTPClient();
            ftpClient.setRemoteHost( machineHost );   
            ftpClient.connect();
            ftpClient.login( user, password );
            ftpClient.put( tempFilePath, fileLocation );
            tempfile = new File( tempFilePath );
            tempfile.delete();
        }
        else
        {
            File exportFile = new File( fileLocation );
            BufferedWriter fileOut = new BufferedWriter( new FileWriter( exportFile ));
            fileOut.write( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" + LMLRenderer.xmlToString( lml ) );
            fileOut.close();
        }
    }
    
    public static String getAssessment( String assessmentFile ) throws Exception
    {
        if ( assessmentFile == null )
            return null;
        FileInputStream fis = new FileInputStream( new File( assessmentFile ) );
    	int len = fis.available();
    	byte[] byte_text = new byte[ len ];
    	fis.read( byte_text );
    	String assessmentString = new String( byte_text );
        fis.close();
        return assessmentString;
    }
    
    public static void sendToCQA( String assessmentFile, String testName
                                    , String testFolder, String machineHost, String user, String password
                                     ) throws Exception
    {
        unicodeList = new ArrayList();
        String assessmentString = getAssessment( assessmentFile );
        // new request now need lml
        Element lml = toLMLForm( assessmentString );
        FTPClient ftpClient = null;
        if ( forReal )
        {
            ftpClient = new FTPClient();
            ftpClient.setRemoteHost( machineHost );   
            ftpClient.connect();
            ftpClient.login( user, password );
            ftpClient.setType( FTPTransferType.BINARY );
        } 
        List imageList = ItemLayoutProcessor.extractAllElement( ".//image_widget", lml );
        for ( int i = 0; i < imageList.size(); i++ )
        {
            Element image_widget = ( Element )imageList.get( i );
            String path = image_widget.getAttributeValue( "src" );
            String destination = copyImageToCQA( ftpClient, path );
            image_widget.setAttribute( "src", destination );
        }
        // for inline images
        List textList = ItemLayoutProcessor.extractAllElement(".//text_widget", lml );
        for(int j=0;j<textList.size();j++) {
            Element text = (Element) textList.get(j);
            String originalText = new String(text.getText());
            String textString = text.getText();
            while (textString.indexOf("/images/") >= 0) {
                String imageRef = textString.substring(textString.indexOf("/default/main/OAS/WORKAREA/highwire/images"));
                imageRef = imageRef.substring(0, imageRef.indexOf("\""));
                String destination = copyImageToCQA( ftpClient, imageRef );
                textString = textString.substring(textString.indexOf("/images/") + 8);
            }
            while (originalText.indexOf("/default/main/OAS/WORKAREA/highwire/images/") >= 0) {
                String localPath = new String(originalText.substring(originalText.indexOf("/default/main/OAS/WORKAREA/highwire/images/")));
                localPath = localPath.substring(0, localPath.indexOf("\""));
                String remotePath = localPath.substring(localPath.lastIndexOf("/") + 1);
                originalText = originalText.replaceFirst(localPath, remotePath);
            }
            text.setText(originalText);
        }
        //
        if ( forReal )
        {
            writeXMLtoCQA( machineHost, user, password, lml, "/export/data/contentdemo/" + testFolder + "/", testName );
        }
        else
        {
            writeXMLtoCQA( machineHost, user, password, lml, "c:/contentdemo/" + testFolder + "/"
                        , testName );
        }
    }
    
    public static String getAttributeValue( Element element, String key ) throws Exception
    {   // unicodeList
        String value = element.getAttributeValue( key );
        if ( value != null )
            value = replaceUnicode( value );  
        return value;
    }
    
    public static String replaceUnicode( String text )throws Exception
    {
        return AssessmentLayoutProcessor.replaceUnicode( text, unicodeList );
    }
    
    public static Element toLMLForm( String assessmentString ) throws Exception
    {
        assessmentString = AssessmentLayoutProcessor.adjustXMLSrcString( assessmentString, unicodeList );
        String result = null;
        org.jdom.input.SAXBuilder saxBuilder = new org.jdom.input.SAXBuilder();
        ByteArrayInputStream bais = new ByteArrayInputStream( assessmentString.getBytes());
        org.jdom.Document assessmentDoc = saxBuilder.build( bais );
        Element inElement = assessmentDoc.getRootElement();
        String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><TestHolder></TestHolder>";
        bais = new ByteArrayInputStream( xmlString.getBytes());
        assessmentDoc = saxBuilder.build( bais );
        Element outElement = assessmentDoc.getRootElement();
        inElement.getChildren( "SchedulableUnit" );
        String MaxPanelWidth = inElement.getAttributeValue( "MaxPanelWidth" );
        
        boolean includeAcknowledgment = false;
        String includeAckFlag = getAttributeValue( inElement, "IncludeAcknowledgment" );
        if (includeAckFlag != null && "yes".equals(includeAckFlag.toLowerCase()))
            includeAcknowledgment = true;
        else 
            includeAcknowledgment = false;
        
        List SchedulableUnits = inElement.getChildren( "SchedulableUnit" );
        for ( int i = 0; i < SchedulableUnits.size(); i++ )
        {
            Element SchedulableUnit = ( Element )SchedulableUnits.get( i );
            Element outSchedulableUnit = new Element( "SchedulableUnit" );
            outSchedulableUnit.setAttribute( "ID", getAttributeValue( SchedulableUnit, "ID" ));
            outSchedulableUnit.setAttribute( "Title", getAttributeValue( SchedulableUnit, "Title" ).replaceAll( "&amp;", "&" ));
            outElement.addContent( outSchedulableUnit );
            List DeliverableUnits = SchedulableUnit.getChildren( "DeliverableUnit" );
            for ( int j = 0; j < DeliverableUnits.size(); j++ )
            {
                Element DeliverableUnit = ( Element )DeliverableUnits.get( j );
                String timeLimit = DeliverableUnit.getAttributeValue( "TimeLimit" );
                if ( timeLimit == null ) timeLimit = "0";
                Element outDeliverableUnit = new Element( "DeliverableUnit" );
                outDeliverableUnit.setAttribute( "ID", getAttributeValue( DeliverableUnit, "ID" ));
                outDeliverableUnit.setAttribute( "Title", getAttributeValue( DeliverableUnit, "Title" ).replaceAll( "&amp;", "&" ));
                outDeliverableUnit.setAttribute( "TimeLimit", timeLimit );
                String startingQuestionNumber = DeliverableUnit.getAttributeValue( "StartItemNumber" );
                if ( startingQuestionNumber != null )
                    outDeliverableUnit.setAttribute( "starting_question_number", startingQuestionNumber );
                outSchedulableUnit.addContent( outDeliverableUnit );
                List Items = ItemLayoutProcessor.extractAllElement( ".//Item", DeliverableUnit );
                List subtestLmls = new ArrayList();
                StringBuffer sb2 = new StringBuffer();
                sb2.append( "<ob_element_list>" );
                for ( int k = 0; k < Items.size(); k++ )
                {
                    Element Item = ( Element )Items.get( k );
                    String itemType = Item.getAttributeValue("ItemType");
                    if (itemType == null || "".equals(itemType))
                        itemType = "SR";
                    Element lmlItem = LMLRenderer.renderCTBItemToLML( k + 1, Item, unicodeList, MaxPanelWidth, includeAcknowledgment );
                    subtestLmls.add( lmlItem );
                    sb2.append("<f id=\"" + ( k + 1 ) + "\" h=\"F9AD5E06A6534A1374C5368CB119966F\" k=\"1\" type=\""+itemType+"\"/>\n");
                }
                sb2.append( "</ob_element_list>" );
                String orderString = AssessmentLayoutProcessor.processSubtestStimulus( subtestLmls, true );
                for ( int k = 0; k < subtestLmls.size(); k++ )
                {
                    Element Item = ( Element )subtestLmls.get( k );
                    outDeliverableUnit.addContent( Item.detach() );
                }
                orderString = "<ob_element_select_order>" + orderString + "</ob_element_select_order>";
                bais = new ByteArrayInputStream( sb2.toString().getBytes());
                outDeliverableUnit.addContent( saxBuilder.build( bais ).getRootElement().detach() );
                bais = new ByteArrayInputStream( orderString.getBytes());
                outDeliverableUnit.addContent( saxBuilder.build( bais ).getRootElement().detach() );
            }
        }
   //     result = LMLRenderer.xmlToString( outElement );
        return outElement;
    }
    
    public static String getImageDestinationPath()
    {
        String destPath;
        if ( forReal )
            destPath = imageFolderCQA;
        else
            destPath = imageFolderLocal;
        return destPath;
    }
    
    public static String copyImageToCQA( FTPClient ftpClient, String path ) throws Exception
    {
        String destImagePath = getImageDestinationPath();
        String destination = destImagePath + path.substring( path.lastIndexOf( "/" ) );
        String sourceFile = null;
        String URLDestination = null;
        if ( forReal )
        {
            sourceFile = "/default/main/OAS/WORKAREA/highwire/images/" + path.substring( path.indexOf("/images/") + 8);
            sourceFile = sourceFile.replaceAll("%20", " ");
            ftpClient.put( sourceFile, destination );
            URLDestination = path.substring( path.lastIndexOf( "/" ) + 1 ).replaceAll(" ", "%20");
        }
        else
        {
            sourceFile = "http://mcsdoas15.mhe.mhc:9000/images/" + path.substring( path.indexOf("/images/") + 8);
            String tempDest = destImagePath + path.substring( path.lastIndexOf( "/" ) );
            PlainTextInputStream fis = (PlainTextInputStream) new URL(sourceFile).getContent();
            FileOutputStream fos = new FileOutputStream( new File( tempDest  ));
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            Pipe pipe = new Pipe( fis, bos );
            pipe.run();
            fis.close();
            fos.write( bos.toByteArray() );
            fos.close();
            URLDestination = path.substring( path.lastIndexOf( "/" ) + 1 ).replaceAll(" ", "%20");
        }
        return URLDestination;
    }
} 
