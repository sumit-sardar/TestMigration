/*
 * Created on Dec 20, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ctb.content.layout;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jdom.Element;

/**
 * @author wen-jin_chang
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AssessmentLayoutProcessor 
{
    public Element root;
    public Element currentSubtest;
    public Element[] currentItems;
    
    public AssessmentLayoutProcessor( Element root_) 
    {
        super();
        root = root_;
    }
    
    public AssessmentLayoutProcessor( File rootPath ) throws Exception
    {
        org.jdom.input.SAXBuilder saxBuilder = new org.jdom.input.SAXBuilder();
        org.jdom.Document assessmentDoc = saxBuilder.build( rootPath );
        root = assessmentDoc.getRootElement();
    }
    
    public HashMap getDeliverableUnitMap() throws Exception
    {
        HashMap subtestMap = new HashMap();
        List subtestList = ItemLayoutProcessor.extractAllElement( ".//DeliverableUnit", root );
        for ( int i = 0; i < subtestList.size(); i++ )
        {
            Element subtest = ( Element )subtestList.get( i );
            String id = subtest.getAttributeValue( "ID" );
            String title = subtest.getAttributeValue( "Title" );
            subtestMap.put( id, title );
        }
        return subtestMap;
    }
    
    public void setCurrentSubtest( String subtestId ) throws Exception
    {
        List subtestList = ItemLayoutProcessor.extractAllElement( ".//DeliverableUnit", root );
        for ( int i = 0; i < subtestList.size(); i++ )
        {
            Element subtest = ( Element )subtestList.get( i );
            String id = subtest.getAttributeValue( "ID" );
            if ( subtestId.equals( id ))
            {
                currentSubtest = subtest;
                break;
            }
        }
    }
    
/*    public void processCurrentSubtest() throws Exception
    {
        List itemList = ItemLayoutProcessor.extractAllElement( ".//Item", currentSubtest );
        currentItems = new Element[ itemList.size() ];
        for ( int i = 0; i < itemList.size(); i++ )
        {
            Element currItem = ( Element )itemList.get( i );
            ItemLayoutProcessor aItemLayoutProcessor = new ItemLayoutProcessor( currItem, i + 1, 12, false );
            Element layoutItem = aItemLayoutProcessor.layoutItem();
            currentItems[ i ] = layoutItem;
        }
        processSubtestStimulus();
    }
*/    
    public void processSubtestStimulus() throws Exception
    {	// process stimulus pattern
        
    }
    
    public static String replaceUnicode( String text, List unicodeList )throws Exception
    {// unicodeList
        String returnString = text;
        int startIndex, endIndex;
        while ( ( startIndex = returnString.indexOf( "QQW" ) ) >= 0 )
        {
            endIndex = returnString.indexOf( "QQW", startIndex + 3 );
            String unicodeIndex = returnString.substring( startIndex + 3, endIndex );
            String pattern = ( String )unicodeList.get( Integer.valueOf( unicodeIndex ).intValue() - 1 );
            returnString = returnString.substring( 0, startIndex ) + pattern + returnString.substring( endIndex + 3 );
            startIndex = endIndex + 3;
        }
        return returnString;
    }
    
    public static Element generate_ads_publish_request_ForSudha( Element deliveryUnit, Long itemSetId, List unicodeList ) throws Exception
    {
        StringBuffer sb = new StringBuffer();
        sb.append( "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ads_publish_request><publish_subtest><questestinterop><assessment title=\"" );
        sb.append( deliveryUnit.getAttributeValue( "Title" ).replaceAll( "&", "&amp;" ).replaceAll("'", "&apos;") + "\" ident=\"" ); 
        sb.append( itemSetId);
        if (deliveryUnit.getAttributeValue("StartItemNumber") != null)
        	{sb.append("\" starting_question_number=\"" + deliveryUnit.getAttributeValue("StartItemNumber"));}
        sb.append("\"><section>" ); 
 
        List itemList = ItemLayoutProcessor.extractAllElement( ".//Item", deliveryUnit );
        String stimulusId = "";
        int start = 0;
        int end = 0;
        List runList = new ArrayList();
        for ( int i = 0; i < itemList.size(); i++ )
        {
            Element item = ( Element )itemList.get( i );
            Element stimulus = item.getChild( "Stimulus" );
            if ( stimulus != null )
            {
                String currentStimulusId = stimulus.getAttributeValue( "DisplayID" );
                if ( currentStimulusId == null )
                    currentStimulusId = stimulus.getAttributeValue( "ID" );
                currentStimulusId = replaceUnicode( currentStimulusId, unicodeList );
                if ( stimulusId.equals( currentStimulusId ) )
                    end = i;
                else
                {
                    if ( start != end && !"".equals( stimulusId ) )
                    {
                        StimulusGroup aRun = new StimulusGroup( stimulusId, start, end );
                        runList.add( aRun );
                        
                    }
                    stimulusId = currentStimulusId;
                    start = i;
                    end = i;
                }
            }
            else
            {
                if ( start != end && !"".equals( stimulusId ) )
                {
                    StimulusGroup aRun = new StimulusGroup( stimulusId, start, end );
                    runList.add( aRun );
                }
                stimulusId = "";
                start = i + 1;
                end = i + 1;
            }
        }
        if ( !"".equals( stimulusId ) && start != end )
        {
            StimulusGroup aRun = new StimulusGroup( stimulusId, start, end );
            runList.add( aRun );
        }
         // <section associate_items="1">
        // <itemref linkrefid="ok_demo01" />
        start = 0;
        for ( int i = 0; i < runList.size(); i++ )
        {
            StimulusGroup aRun = ( StimulusGroup )runList.get( i );
            if ( aRun.start > start )
            {
                for ( int j = start; j < aRun.start; j++ )
                {
                    Element xml = ( Element )itemList.get( j );
                    String id = xml.getAttributeValue( "ID" );
                    String itemType = xml.getAttributeValue("ItemType");
                    // For Audio Item - Las Links
                    String allowRevisit = xml.getAttributeValue("AllowRevisit","true");
                    String AllowRevisitOnRestart = xml.getAttributeValue("AllowRevisitOnRestart","true");
                    sb.append("<itemref linkrefid=\"" + id + "\" type=\"" +itemType+"\" allow_revisit=\"" +allowRevisit+"\" allow_revisit_on_restart=\"" +AllowRevisitOnRestart+"\" />\n");
                }
            }
            sb.append("<section associate_items=\"1\" >\n");
            for ( int j = aRun.start; j <= aRun.end; j++ )
            {
                Element xml = ( Element )itemList.get( j );
                String id = xml.getAttributeValue( "ID" );
                String itemType = xml.getAttributeValue("ItemType");
                // For Audio Item - Las Links
                String allowRevisit = xml.getAttributeValue("AllowRevisit","true");
                String AllowRevisitOnRestart = xml.getAttributeValue("AllowRevisitOnRestart","true");
                sb.append("<itemref linkrefid=\"" + id + "\" type=\"" +itemType+"\" allow_revisit=\"" +allowRevisit+"\" allow_revisit_on_restart=\"" +AllowRevisitOnRestart+"\" />\n");
            }
            sb.append("</section>");
            start = aRun.end + 1;
        }
        if ( start < itemList.size() )
        {
            for ( int j = start; j < itemList.size(); j++ )
            {
                Element xml = ( Element )itemList.get( j );
                String id = xml.getAttributeValue( "ID" );
                String itemType = xml.getAttributeValue("ItemType");
                // For Audio Item - Las Links
                String allowRevisit = xml.getAttributeValue("AllowRevisit","true");
                String AllowRevisitOnRestart = xml.getAttributeValue("AllowRevisitOnRestart","true");
                sb.append("<itemref linkrefid=\"" + id + "\" type=\"" +itemType+"\" allow_revisit=\"" +allowRevisit+"\" allow_revisit_on_restart=\"" +AllowRevisitOnRestart+"\" />\n");
            }
        }
        sb.append( "</section></assessment></questestinterop></publish_subtest></ads_publish_request>" );
        ByteArrayInputStream bais = new ByteArrayInputStream( sb.toString().getBytes());
        org.jdom.input.SAXBuilder saxBuilder = new org.jdom.input.SAXBuilder();
        org.jdom.Document itemDoc = saxBuilder.build( bais );
        Element returnValue = itemDoc.getRootElement();
        return returnValue;
    }
    
    public static String processSubtestStimulus( List itemLmls, boolean preview )
    {//preview = false;
        List runList = new ArrayList();
        String stimulusId = "";
        int start = 0;
        int end = 0;
        for ( int i = 0; i < itemLmls.size(); i++ )
        {
            Element lml = ( Element )itemLmls.get( i );
            lml = lml.getChild( "item_canvas" );
            Element stimulus = lml.getChild( "stimulus_tabs_panel" );
            if ( stimulus == null )
                stimulus = lml.getChild( "scrolling_text_panel" );
            if ( stimulus == null )
            {
                List panelList = lml.getChildren( "panel" );
                for ( int j = 0; j < panelList.size(); j++ )
                {
                    Element panel = ( Element )panelList.get( j );
                    if ( "stimulus".equals( panel.getAttributeValue( "stereotype" )) )
                    {
                        stimulus = panel;
                        break;
                    }
                }
            }
            if ( stimulus != null )
            {
                String currentStimulusId = stimulus.getAttributeValue( "id" );
                if ( stimulusId.equals( currentStimulusId ) )
                    end = i;
                else
                {
                    if ( start != end && !"".equals( stimulusId ) )
                    {
                        StimulusGroup aRun = new StimulusGroup( stimulusId, start, end );
                        runList.add( aRun );
                    }
                    stimulusId = currentStimulusId;
                    start = i;
                    end = i;
                }
            }
            else
            {
                if ( start != end && !"".equals( stimulusId ) )
                {
                    StimulusGroup aRun = new StimulusGroup( stimulusId, start, end );
                    runList.add( aRun );
                }
                stimulusId = "";
                start = i + 1;
                end = i + 1;
            }
        }
        if ( !"".equals( stimulusId ) && start != end )
        {
            StimulusGroup aRun = new StimulusGroup( stimulusId, start, end );
            runList.add( aRun );
        }
        for ( int i = 0; i < runList.size(); i++ )
        {
            StimulusGroup aRun = ( StimulusGroup )runList.get( i );
            for ( int j = aRun.start; j <= aRun.end; j++ )
            {
                Element lml = ( Element )itemLmls.get( j );
                replacePattern( lml, aRun.start, aRun.end );
            }
        }
        start = 0;
        StringBuffer sb = new StringBuffer();
//        sb.append("<ob_element_select_order>\n");
        for ( int i = 0; i < runList.size(); i++ )
        {
            StimulusGroup aRun = ( StimulusGroup )runList.get( i );
            if ( aRun.start > start )
            {
                for ( int j = start; j < aRun.start; j++ )
                {
                    Element lml = ( Element )itemLmls.get( j );
                    Element model = lml.getChild( "item_model" );
                    String id = null;
                    if ( preview )
                        id = model.getAttributeValue( "eid" );
                    else
                        id = model.getAttributeValue( "iid" );
                    sb.append("<e id=\"" + id + "\" />\n");
                }
            }
            sb.append("<g ai=\"1\" >\n");
            for ( int j = aRun.start; j <= aRun.end; j++ )
            {
                Element lml = ( Element )itemLmls.get( j );
                Element model = lml.getChild( "item_model" );
                String id = null;
                if ( preview )
                    id = model.getAttributeValue( "eid" );
                else
                    id = model.getAttributeValue( "iid" );
                sb.append("<e id=\"" + id + "\" />\n");
            }
            sb.append("</g>\n");
            start = aRun.end + 1;
        }
        if ( start < itemLmls.size() )
        {
            for ( int j = start; j < itemLmls.size(); j++ )
            {
                Element lml = ( Element )itemLmls.get( j );
                Element model = lml.getChild( "item_model" );
                String id = null;
                if ( preview )
                    id = model.getAttributeValue( "eid" );
                else
                    id = model.getAttributeValue( "iid" );
                sb.append("<e id=\"" + id + "\" />\n");
            }
        }
//        sb.append("</ob_element_select_order>\n");
        String orderString = sb.toString();
        return orderString;
/*        
        try
        {
            File exportFile = new File( "c:/eclipse/workspace/application-contentbridge/1.xml" );
            BufferedWriter fileOut = new BufferedWriter( new FileWriter( exportFile ));
            fileOut.write( orderString );
            fileOut.close();
        }
        catch ( Exception e1) 
        {
        }
*/        
/*        org.jdom.input.SAXBuilder saxBuilder = new org.jdom.input.SAXBuilder();
        ByteArrayInputStream bais = new ByteArrayInputStream( orderString.getBytes() );
        try
        {
            org.jdom.Document itemDoc = saxBuilder.build( bais );
            return itemDoc.getRootElement();
        }
        catch (Exception e) 
        {
            StackTraceElement [] trace = e.getStackTrace();
            e = null;
        } 
        return null; */
    }
    
    public static void replacePattern( Element lml, int start, int end )
    {
        Element item_canvas = lml.getChild( "item_canvas" );
        List panelList = item_canvas.getChildren( "panel" );
        for ( int j = 0; j < panelList.size(); j++ )
        {
            Element panel = ( Element )panelList.get( j );
            if ( "directions".equals( panel.getAttributeValue( "stereotype" ))
                || "stimulus".equals( panel.getAttributeValue( "stereotype" )) )
            {
                List textWidgets = panel.getChildren( "text_widget" );
                for ( int i = 0; i < textWidgets.size(); i++ )
                {
                    Element widget = ( Element )textWidgets.get( i );
                    String text = widget.getText();
                    text = text.replaceAll( "@startvalue", String.valueOf( start ) );
                    text = text.replaceAll( "@endvalue", String.valueOf( end ) );
                    widget.setText( text );
                }
                break;
            }
        }
    }
    
    public static String adjustXMLSrcString( String xmlSource, List unicodeReturnList ) throws Exception
    {
        HashMap patternMap = new HashMap();
        String returnString = xmlSource;
        returnString = returnString.replaceAll( "&nbsp;", "WQW" );
        returnString = returnString.replaceAll( " & ", " &amp; " );
        int startIndex, endIndex, patternIndex;
        while ( ( startIndex = returnString.indexOf( "&" ) ) >= 0 )
        {
            endIndex = returnString.indexOf( ";", startIndex );
            String pattern = returnString.substring( startIndex, endIndex + 1 );
            if ( patternMap.containsKey( pattern ) )
                patternIndex = ( ( Integer )patternMap.get( pattern ) ).intValue();
            else
            {
                unicodeReturnList.add( pattern );
                patternIndex = unicodeReturnList.size();
                patternMap.put( pattern, new Integer( patternIndex ) );
            }
            String insertString = "QQW" + patternIndex + "QQW";
            returnString = returnString.substring( 0, startIndex ) + insertString + returnString.substring( endIndex + 1 );
        }
        return returnString;
    }
}
