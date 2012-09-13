/*
 * Created on Nov 15, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ctb.content.layout;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

import org.jdom.Attribute;
import org.jdom.CDATA;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.ProcessingInstruction;
import org.jdom.Text;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import sun.net.www.content.text.PlainTextInputStream;

/**
 * @author wen-jin_chang 
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ItemLayoutProcessor 
{
    public static boolean developement = true;
    public static final int LAYOUT_MAX_WIDTH = 786;
    public Element itemElement;
    public Element studentDirection;
    public Element stimulus;
    public Element stem;
    public Element answer;
    public Element accommodation;
    public static HashMap manipulateMapping = null;
    public static HashMap allManipulateMapping = null;
    public static int tableCellTopBottomPadding = 3;
    public static int tableCellLeftRightPadding = 6;
    
//    public static int tableTitleFontSize = 14; 
    public static int defaultSpacing = 12;
    public static int pageIconWidth = 40;
    public int startYAfterDirection;
    public int order;
    public static int fontLeading = 7;
    public static int startY = 74 + defaultSpacing - fontLeading;
	public static String text_widget = "text_widget";
	public static String passage_widget = "scrolling_text_widget";
	public static String footnote_widget = "footnote";
	public static String table_widget = "table";
	public static String rationale_widget = "rationale";
	public static String multi_line_answer_widget = "multi_line_answer";
	public static String single_line_answer_widget = "single_line_answer";
    
    public static int scrolling_text_border = 15;
    public static int tab_title_height = 33;
    public static int defaultPassageHeight = 350;
    public static int defaultPassageWidth = 378;
	public static String image_widget = "image_widget";
	public static int textBoxExtraWidth = 10; // was 8 before
	public static int textBoxExtraHeight = 10; // was 10 before
	public static int maxLayoutBottom = 532;
	public static int scrollbarWidth = 14;
    public static int rightIndent = 6;
	public int uniqueId;
    public long timestamp;	
	public int pointSize;
	public int totalDownloadSize;
    public boolean addCData;
	public static String teamSite = "file://MCSDOAS15.mhe.mhc";
    public List unicodeList;
    public boolean useStimulusDisplayID;
    boolean stimulusLeftLocated;
    boolean stemRightLocated;
    boolean answerChoiceRightLocated;
    public int m_MaxPanelWidth;
    
    private boolean includeAcknowledgement = false;
    
    private boolean insideAcknowledgement = false;
    private boolean insideTable = false;
    
    private List acknowledgementList;
    
    public ItemLayoutProcessor( Element itemElement_, String MaxPanelWidth, int order_, int pointSize_, boolean addCData_, List unicodeList_, boolean includeAcknowledgement) {
        super();
        this.includeAcknowledgement = includeAcknowledgement;
        itemElement = itemElement_;
        studentDirection = null;
        stimulus = null;
        stem = null;
        answer = null;
        accommodation = null;
        startYAfterDirection = startY;
        order = order_;
        timestamp = System.currentTimeMillis()%1000000*10000;
        uniqueId = 1000;
        pointSize = pointSize_;
        totalDownloadSize = 0;
        addCData = addCData_;
     //   addCData = true;
        unicodeList = unicodeList_;
        useStimulusDisplayID = false;
        stimulusLeftLocated = false;
        stemRightLocated = false;
        answerChoiceRightLocated = false;
        createManipulateMapping();     
        m_MaxPanelWidth = 0;   
        
        if ( MaxPanelWidth != null && MaxPanelWidth.length() > 0 )
        {
            try
            {
                m_MaxPanelWidth = Integer.valueOf( MaxPanelWidth ).intValue();
            }
            catch( Exception e )
            {
                m_MaxPanelWidth = 0;
            }
        }
        if ( m_MaxPanelWidth == 0 )
            m_MaxPanelWidth = LAYOUT_MAX_WIDTH - rightIndent;
    }
    
    public void assignIdToElements( Element element )
    {
        List childList = element.getChildren();
        for ( int i = 0; i < childList.size(); i++ )
        {
            Object object = ( Object )childList.get( i );
            if ( object instanceof Element )
            {
                Element thisElement = ( Element )object;
                if ( thisElement.getAttributeValue( "id" ) == null )
                {
                    thisElement.setAttribute( "id", getUniqueId());
                }
                if ("text_widget".equals(thisElement.getName()) && "yes".equals(thisElement.getAttributeValue("input"))) {
                    List contents = thisElement.getContent();
                    for (int j = 0; j < contents.size(); j++) {
                        if (contents.get(j) instanceof Text) {
                            Text textContent = (Text) contents.get(j); 
                            String text = textContent.getValue();
                            contents.set(j, new Text(changeWidgetID(text)));
                        }
                        else if (contents.get(j) instanceof CDATA) {
                            CDATA textContent = (CDATA) contents.get(j); 
                            String text = textContent.getText();
                            contents.set(j, new CDATA(changeWidgetID(text)));
                        }
                    }
                }
                assignIdToElements( thisElement );
            }
        }    
    }
    
    private String changeWidgetID(String text) {
        final String CR_PATTERN = "<single_line_answer id=\"";
        int startIndex = text.indexOf(CR_PATTERN);
        String newText;
        if (startIndex>=0) {
            newText = text.substring(0, startIndex+CR_PATTERN.length()) + getUniqueId() + text.substring(startIndex+CR_PATTERN.length()+11, text.length());
        }
        else
            newText = text;
        
        return newText;
    }
    
    public String getUniqueId()
    {
        String value = "widget" + String.valueOf( timestamp+uniqueId );
        uniqueId++;
        return value;
    }
    
    public int getDownloadSize()
    {
        return totalDownloadSize;
    }
    
    public boolean checkGridElement() throws Exception
    {
        return extractAllElement( ".//Grid", itemElement ).size() > 0;
    }
     /* For Audio Item - Las Links
      * Method cheks Item is AudioItem or not.
      */
    private boolean checkAudioItemElement()throws Exception{
    	 return extractAllElement( ".//AudioItem", itemElement ).size() > 0;
	}
	
    public Element layoutItem() throws Exception
    {
        Element layout = getDummy();
        updateNavigationalConstraint(layout); // For Audio Item - Las Links: To add attributes AllowRevisit and AllowRevisitOnRestart to Item
        boolean isFakeCR = checkGridElement( );
        boolean isAudioItem= checkAudioItemElement( ); // For Audio Item - Las Links: 
        boolean isNICR = false;
        boolean isNISR = false;
        
        handleLeadIn();
        extractStudentDirection( null );
        handleStimulusDirection();
        extractStimulus();
        extractStem();
        if("NI".equals(itemElement.getAttributeValue( "ItemType"))) { //FOR NI ITEM : TO ADD RESPONSE AREA
        	
        	isNICR = (extractSingleElement( ".//ConstructedResponse", itemElement ) != null);
        	isNISR =  ( extractSingleElement( ".//SelectedResponse", itemElement ) !=null);
        	if(isNICR)
        		extractCRArea( isFakeCR, isAudioItem );
        	else if (isNISR)
        		extractSRChoice();
        } 
        else if ( "CR".equals( itemElement.getAttributeValue( "ItemType" )))
            extractCRArea( isFakeCR, isAudioItem ); // For Audio Item - Las Links
        else
            extractSRChoice();
        extractAccommodation( isFakeCR ); 
        doStudentDirection();
        doStimulus();
        doStem();
                
        if("NI".equals(itemElement.getAttributeValue( "ItemType"))) { //FOR NI ITEM : TO ADD RESPONSE AREA
        	if ( isFakeCR )
                doFakeCRArea();
        	else if(isNISR)
        		 doAnswerChoice();
            else
                doCRArea();
        } 
        else if ( "CR".equals( itemElement.getAttributeValue( "ItemType" )))
        {
            if ( isFakeCR )
                doFakeCRArea();
            else
                doCRArea();
        }
        else
            doAnswerChoice();
        removeUselessHeight();
        reDoScrollWidgetTextWidthHeight();
        putAllPiecesTogether( layout, !isFakeCR && !isAudioItem && "CR".equals( itemElement.getAttributeValue( "ItemType" )) ); // For Audio Item - Las Links
        fillDefaultIDs();
        totalDownloadSize += 1000 + Math.abs( Math.random() ) % 999 ; // xml size
        if ( stimulus != null )
        {
            if ( stimulus.getName().equals( "scrolling_text_panel" ) 
                    || stimulus.getName().equals( "stimulus_tabs_panel" ))
            {
                stimulus.removeAttribute( "left_padding" );
                stimulus.removeAttribute( "spacing" );
                stimulus.removeAttribute( "halign" );
                stimulus.removeAttribute( "layout" );
            }
        }
        removeTempAttributes( layout );
        removeWidthIfCenterAlign( layout );
    //    removeGraphicAlignIfHorizontalAlign( studentDirection );
    //    removeGraphicAlignIfHorizontalAlign( stem );
    //    removeGraphicAlignIfHorizontalAlign( stimulus );
        return layout;
    }
    
    public void removeGraphicAlignIfHorizontalAlign( Element element )throws Exception
    {
        if ( element != null && "horizontal".equals( element.getAttributeValue( "layout")))
        {
            List allImageWidgets = extractAllElement( ".//image_widget", element );
            for ( int i = 0; i < allImageWidgets.size(); i++ )
            {
                Element imageWidget = ( Element )allImageWidgets.get( i );
                imageWidget.removeAttribute( "halign" );
                imageWidget.removeAttribute( "valign" );
            }
        }
    }
    
    public void removeTempAttributes( Element layout ) throws Exception
    {
        List allTextWidgets = extractAllElement( ".//" + text_widget, layout );
        for ( int i = 0; i < allTextWidgets.size(); i++ )
        {
            Element text_widget = ( Element )allTextWidgets.get( i );
            text_widget.removeAttribute( "numberOfBR" );
        }
        List answer_choice_widgets= extractAllElement( ".//answer_choice_widget", layout );
        for ( int i = 0; i < answer_choice_widgets.size(); i++ )
        {
            Element answer_choice_widget = ( Element )answer_choice_widgets.get( i );
            answer_choice_widget.removeAttribute( "id" );
        }
        List selector_widgets= extractAllElement( ".//selector_widget", layout );
        for ( int i = 0; i < selector_widgets.size(); i++ )
        {
            Element selector_widget = ( Element )selector_widgets.get( i );
            selector_widget.removeAttribute( "id" );
        }
    }
    
    public void handleLeadIn() throws Exception
    {
        Element LeadIn = extractSingleElement( ".//LeadIn", itemElement );
        Element Stem = extractSingleElement( ".//Stem", itemElement );
        if ( LeadIn != null )
        {
            LeadIn.detach();
            if ( Stem != null )
            {
                List children = Stem.getContent();
                ArrayList aList = new ArrayList();
                aList.add( LeadIn );
                for ( int i = 0; i < children.size(); i++ )
                    aList.add( children.get( i ) );
                for ( int i = 1; i < aList.size(); i++ )
                {
                    Object thisObject = ( Object )aList.get( i );
                    if ( thisObject instanceof Element )
                    {
                        Element thisElement = ( Element )thisObject;
                        thisElement.detach();
                    }
                    else if (thisObject instanceof Text )
                    {
                        Text aText = ( Text )thisObject;
//                        aText.getParent().setText(null);
                    }
                }
                Stem.setContent( aList );
            }
        }
    }
    
    public void fillDefaultIDs()
    {
        if ( studentDirection != null )
            assignIdToElements( studentDirection );
        if ( stimulus != null )
            assignIdToElements( stimulus );
        if ( stem != null )
            assignIdToElements( stem );
        if ( answer != null )
            assignIdToElements( answer );
    }
    
    public void removeWidthIfCenterAlign( Element layout )throws Exception
    {
        List textWidgetList = extractAllElement( ".//" + text_widget, layout );
        for ( int i = 0; i < textWidgetList.size(); i++ )
        {
            Element theWidget = ( Element )textWidgetList.get( i );
            String align = theWidget.getAttributeValue( "halign" );
            if ( align != null && "center".equals( align ) )
                theWidget.removeAttribute( "width" );
        }
    }    
    
    public void reDoScrollWidgetTextWidthHeight()throws Exception
    {
        int text_to_scrollbar_margin = 3;
        if ( stimulus != null )
        {
            List passage_widgetList = extractAllElement( ".//" + passage_widget, stimulus );  
            if ( passage_widgetList.size() == 0 && stimulus.getName().equals( "scrolling_text_panel" ))
                passage_widgetList.add( stimulus );
            for ( int j = 0; j < passage_widgetList.size(); j++ )
            {
                Element passageWidget = ( Element ) passage_widgetList.get( j );
                int width = Integer.valueOf( passageWidget.getAttributeValue( "width" )).intValue();
                int desiredWith = width - scrollbarWidth - text_to_scrollbar_margin;
                List textWidgetList = extractAllElement( ".//" + text_widget, passageWidget );
                for ( int i = 0; i < textWidgetList.size(); i++ )
                {
                    Element theWidget = ( Element )textWidgetList.get( i );
                    if (!"cell".equals(theWidget.getParentElement().getName())) {
                        theWidget.removeAttribute( "height" );
                        theWidget.setAttribute( "width", String.valueOf( desiredWith ));
                    }
                }
            }
        }
    }
    
    public void removeUselessHeight()throws Exception
    {
        if ( studentDirection != null )
            removeElementUselessHeightIfVertical( studentDirection );
        if ( stimulus != null )
            removeElementUselessHeightIfVertical( stimulus );
        if ( stem != null )
            removeElementUselessHeightIfVertical( stem );
        if ( answer != null )  // remove this block next week
            removeTextElementHeight( answer );
    }
    
    public void removeTextElementHeight( Element element )throws Exception
    {
        List textWidgetList = extractAllElement( ".//" + text_widget, element );
        for ( int i = 0; i < textWidgetList.size(); i++ )
        {
            Element theWidget = ( Element )textWidgetList.get( i );
            theWidget.removeAttribute( "height" );
        }
    }
    
    public void removeElementUselessHeightIfVertical( Element element )throws Exception
    {
        String elementLayout = element.getAttributeValue( "layout" );
        if ( elementLayout.equals( "vertical" ))
        {
            List textWidgetList = extractAllElement( ".//" + text_widget, element );
            for ( int i = 0; i < textWidgetList.size(); i++ )
            {
                Element theWidget = ( Element )textWidgetList.get( i );
                theWidget.removeAttribute( "height" );
            }
        }
    }
    
    public void putAllPiecesTogether( Element layout, boolean isCR ) throws Exception
    {
        layout.setAttribute( "iid", getAttributeValue( itemElement, "ID" ));
        layout.setAttribute( "security_classification_id", "alg112" );
        Element canvas = new Element( "item_canvas" );
        canvas.setAttribute( "id", "IC" );
//        canvas.setAttribute( "features", "img-tag" );
        layout.addContent( canvas );
        if ( accommodation != null )
            layout.addContent( accommodation );
        if ( studentDirection != null )
            canvas.addContent( studentDirection );
        if ( stimulus != null )
            canvas.addContent( stimulus );
        if ( stem != null )
            canvas.addContent( stem );
        if ( answer != null )
        {
            canvas.addContent( answer );
            if ( isCR )
            {
                /* Element constructed_response_panel = new Element( "constructed_response_panel" );
                constructed_response_panel.setAttribute( "identifier", "obj_con_response");
                setAllDefaultAttributes( constructed_response_panel, answer );
                constructed_response_panel.removeAttribute( "spacing" );
                layout.addContent( constructed_response_panel ); */
                Element constructed_response_panel = new Element( "constructed_response_panel" );
                constructed_response_panel.setAttribute( "identifier", "obj_con_response");
                setAllDefaultAttributes( constructed_response_panel, answer );
                constructed_response_panel.removeAttribute( "spacing" );
                canvas.addContent( constructed_response_panel );  
            }
        }
    }
   
    public void doStudentDirection() throws Exception
    {
        if ( studentDirection != null )
        {
            PanelAttributes aPanelAttributes = new PanelAttributes();
            Element src = extractSingleElement( ".//StudentDirections", itemElement );
            aPanelAttributes.readAttributes( studentDirection );
            if ( src != null )
                aPanelAttributes.readAttributes( src );
            int width = m_MaxPanelWidth;
            if ( aPanelAttributes.width == null )
                studentDirection.setAttribute( "width", String.valueOf( width ));
            else
                width = Integer.valueOf( aPanelAttributes.width ).intValue();
            int height = calculateHeight( aPanelAttributes.widgetLayout.equals( "vertical"), width
        					, studentDirection, Integer.valueOf( aPanelAttributes.spacing ).intValue() );
            if ( aPanelAttributes.height == null )
                studentDirection.setAttribute( "height", String.valueOf( height ));
            if ( aPanelAttributes.x == null )
                studentDirection.setAttribute( "x", "0" );
            if ( aPanelAttributes.y == null )
                studentDirection.setAttribute( "y", String.valueOf( startYAfterDirection ));
            startYAfterDirection = Integer.valueOf( studentDirection.getAttributeValue( "y" ) ).intValue() +
            				Integer.valueOf( studentDirection.getAttributeValue( "height") ).intValue();
            if ( studentDirection.getAttributeValue( "layout").equals( "horizontal" ))
                specialHandlingForHorizontal( studentDirection );
        }
    }
    
    public void specialHandlingForHorizontal( Element element ) throws Exception
    {
        int text_to_scrollbar_margin = 3;
        List children = element.getChildren();
        if ( children.size() == 2 )
        {
            Element firstBorn = ( Element )children.get( 0 );
            if ( firstBorn.getName().equals( image_widget ) )
            {
                Element lastBorn = ( Element )children.get( 1 );
                if ( lastBorn.getName().equals( text_widget ) )
                {
                    int panelWidth = Integer.valueOf( element.getAttributeValue( "width" )).intValue();
                    int left_padding = Integer.valueOf( element.getAttributeValue( "left_padding" )).intValue();
                    int panelSpacing = Integer.valueOf( element.getAttributeValue( "spacing" )).intValue();
                    int imageWidth = Integer.valueOf( firstBorn.getAttributeValue( "width" )).intValue();
                    lastBorn.setAttribute( "width", String.valueOf( panelWidth - left_padding - panelSpacing - imageWidth - scrollbarWidth - text_to_scrollbar_margin ));
                }
            }
        }
    }
    
    public void doStimulus() throws Exception
    {
        if ( stimulus != null )
        {	// only do height
            PanelAttributes aPanelAttributes = new PanelAttributes();
            aPanelAttributes.readAttributes( stimulus );
            if ( aPanelAttributes.y == null )
            {
                if ( startY == startYAfterDirection )
                    stimulus.setAttribute( "y", String.valueOf( startYAfterDirection ) );
                else
                    stimulus.setAttribute( "y", String.valueOf( startYAfterDirection + fontLeading ) );
            }
            if ( tabStimulus || hasPassage )
            { 
                int y = Integer.valueOf( stimulus.getAttributeValue( "y" ) ).intValue();
     //           int stimulusHeight = Integer.valueOf( stimulus.getAttributeValue( "height" ) ).intValue();
     //           int bottom = maxLayoutBottom - scrolling_text_border;
                int stimulusHeight = maxLayoutBottom - y;
                String heightStr = stimulus.getAttributeValue("height");
                if (heightStr == null || heightStr.equals("")) {
                    stimulus.setAttribute( "height", String.valueOf( stimulusHeight ));
                }
                else {
                    stimulusHeight = Integer.valueOf(heightStr).intValue();
                }
                List scrollable_txt_widget_List = extractAllElement( ".//" + passage_widget, stimulus );
                int textWidth = Integer.valueOf(aPanelAttributes.width ).intValue();
                textWidth += defaultSpacing;
         //       textWidth = textWidth - defaultSpacing / 2 - scrollbarWidth;
                for ( int i = 0; i < scrollable_txt_widget_List.size(); i++ )
                {
                    calculateHeight( true, textWidth
                                            , ( Element)scrollable_txt_widget_List.get( i ), Integer.valueOf( aPanelAttributes.spacing ).intValue() );
                }
                int x = Integer.valueOf( stimulus.getAttributeValue( "x" ) ).intValue();
                int stimulusWidth = Integer.valueOf( stimulus.getAttributeValue( "width" ) ).intValue();
                if ( x == 0 )
                {
                    x = defaultSpacing;
                    stimulusWidth -= defaultSpacing;
                    stimulus.setAttribute( "width", String.valueOf( stimulusWidth ));
                    stimulus.setAttribute( "x", String.valueOf( x ));
         //           stimulus.setAttribute( "left_padding", String.valueOf( defaultSpacing ) );
                }
                if ( tabStimulus )
                {
                    stimulusHeight -= tab_title_height;
                    int panelWidth = Integer.valueOf( stimulus.getAttributeValue( "width" )).intValue();
                    // panelWidth = panelWidth - scrollbarWidth;
                    for ( int i = 0; i < scrollable_txt_widget_List.size(); i++ )
                    {
                        Element scrollable_panel = ( Element )scrollable_txt_widget_List.get( i );
                        scrollable_panel.setAttribute( "width", String.valueOf( panelWidth ));
                        scrollable_panel.setAttribute( "height", String.valueOf( stimulusHeight ));
                    }         
       /*             List allTabPanels = extractAllElement( ".//stimulus_tab", stimulus );    
                    for ( int i = 0; i < allTabPanels.size(); i++ )    
                    {
                        Element TabPanel = ( Element )allTabPanels.get( i );
                        calculateHeight( true, panelWidth, TabPanel, 20 );
                    }  */
                }  
            }
            else if ( stimulusLeftLocated && stemRightLocated && answerChoiceRightLocated )
            {
                int y = Integer.valueOf( stimulus.getAttributeValue( "y" ) ).intValue();
         //       int stimulusHeight = Integer.valueOf( stimulus.getAttributeValue( "height" ) ).intValue();
                int stimulusHeight = maxLayoutBottom - y;
                stimulus.setAttribute( "height", String.valueOf( stimulusHeight ));
            }
            int height = calculateHeight( aPanelAttributes.widgetLayout.equals( "vertical"), Integer.valueOf(aPanelAttributes.width ).intValue()
    				, stimulus, Integer.valueOf( aPanelAttributes.spacing ).intValue() );
            if ( stimulus.getAttributeValue( "height" ) == null )
                stimulus.setAttribute( "height", String.valueOf( height ));
            String myHeight = stimulus.getAttributeValue( "height" );
            String myY = stimulus.getAttributeValue( "y" );
            int bottom = Integer.valueOf( myY ).intValue() + Integer.valueOf( myHeight ).intValue();
            if ( bottom > maxLayoutBottom )
            {
                int newHeight = maxLayoutBottom - Integer.valueOf( myY ).intValue();
                stimulus.setAttribute( "height", String.valueOf( newHeight ) );
            } 
            if ( stimulus.getAttributeValue( "layout").equals( "horizontal" ))
                specialHandlingForHorizontal( stimulus );
            PanelAttributes bPanelAttributes = new PanelAttributes();
            bPanelAttributes.readAttributes( stimulus );
            calculateHeight( bPanelAttributes.widgetLayout.equals( "vertical")
                            , Integer.valueOf( stimulus.getAttributeValue( "width") ).intValue()
                            , stimulus, Integer.valueOf( bPanelAttributes.spacing ).intValue() );
        }
    }
    
    public int getStemY( String stemX ) throws Exception
    {
        int startY = startYAfterDirection;
        if ( stimulus != null )
        {
            Element Stem = extractSingleElement( ".//Stem", itemElement );
            String location = Stem.getAttributeValue( "location" );
            if ( "across".equals( location ))
            {
                String y = stimulus.getAttributeValue( "y" );
                String height = stimulus.getAttributeValue( "height" );
                startY = Integer.valueOf( y ).intValue() +       
                				Integer.valueOf( height ).intValue();
            }
        }
        return startY;
    }
    
    public boolean noPanelBelowStem() throws Exception
    {
        boolean result = false;
        if ( !stemRightLocated )
        {
            if ( stimulus != null )
                result = Integer.valueOf( stimulus.getAttributeValue( "x" ) ).intValue() > 45;
            if ( !result && answer != null )
                result = Integer.valueOf( answer.getAttributeValue( "x" ) ).intValue() > 45;
        }
        return result;
    }
    
    public void doFormulaCard(Element accommodation) throws Exception
    {
       List formulaCard = extractAllElement( ".//FormulaCard", itemElement );
        if ( formulaCard != null && formulaCard.size() > 0 )
        {
            Element thisElement = ( Element )formulaCard.get( 0 );
            String thisResource = thisElement.getAttributeValue( "resource" );
            if ( thisResource != null && !thisResource.equals("")){
                    accommodation.setAttribute("formula_card_resource", thisResource );
            }
            
            
        }
    }
    
    public void doStem() throws Exception
    {
        if ( stem != null )
        {	// only do height
            PanelAttributes aPanelAttributes = new PanelAttributes();
            aPanelAttributes.readAttributes( stem );
            if ( aPanelAttributes.y == null )
            {
                int startY = getStemY( aPanelAttributes.x );
                if ( startY == startYAfterDirection )
                    stem.setAttribute( "y", String.valueOf( startY ) );
                else
                    stem.setAttribute( "y", String.valueOf( startY + fontLeading ) );
            }
            int height = calculateHeight( aPanelAttributes.widgetLayout.equals( "vertical"), Integer.valueOf(aPanelAttributes.width ).intValue()
							, stem, Integer.valueOf( aPanelAttributes.spacing ).intValue() );
            if ( aPanelAttributes.height == null )
                stem.setAttribute( "height", String.valueOf( height ));
            if ( noPanelBelowStem() )
            {
                int y = Integer.valueOf( stem.getAttributeValue( "y" ) ).intValue();
                int stemHeight = maxLayoutBottom - y;
                stem.setAttribute( "height", String.valueOf( stemHeight ));
            }
            String myHeight = stem.getAttributeValue( "height" );
            String myY = stem.getAttributeValue( "y" );
            int bottom = Integer.valueOf( myY ).intValue() + Integer.valueOf( myHeight ).intValue();
            if ( bottom > maxLayoutBottom )
            {
                int newHeight = maxLayoutBottom - Integer.valueOf( myY ).intValue();
                stem.setAttribute( "height", String.valueOf( newHeight ) );
            } 
        }
    }
    
    public void adjustGridHeightIfNeeded() throws Exception
    {
        int myX = Integer.valueOf( answer.getAttributeValue( "x" ) ).intValue();
        int myY = Integer.valueOf( answer.getAttributeValue( "y" ) ).intValue();
        int myWidth = Integer.valueOf( answer.getAttributeValue( "width" ) ).intValue();
        int myHeight = Integer.valueOf( answer.getAttributeValue( "height" ) ).intValue();
        int top_padding = 12;
        // gridcol entry
        List gridcolList = extractAllElement( ".//gridcol", answer );
        Element gridcol = ( Element )gridcolList.get( 0 );
        List entryList = gridcol.getChildren( "entry" );
        int heightNeeded = 0;
        for ( int i = 0; i < entryList.size(); i++ )
        {
            Element theEntry = ( Element )entryList.get( i );
            String value = theEntry.getAttributeValue( "value" );
            if ( "---".equals( value ) )
                heightNeeded += 10;
            else
                heightNeeded += 17 + 6;
        }
        heightNeeded += top_padding + 36 + 5;
        if ( heightNeeded > myHeight )
        {
            int newY = myY - (  heightNeeded - myHeight );
            answer.setAttribute( "y", String.valueOf( newY ));
            answer.setAttribute( "height", String.valueOf( heightNeeded ));
            if ( stimulus != null )
                adjustPanelHeight( stimulus, myX + 10, newY );
            if ( stem != null )
                adjustPanelHeight( stem, myX + 10, newY );
        }
        answer.setAttribute( "height", String.valueOf( heightNeeded ));
//        int bottom = maxLayoutBottom - myY;
//        answer.setAttribute( "height", String.valueOf( bottom ));
    }
    
    public void adjustPanelHeight( Element panel, int x, int y )
    {
        int myX = Integer.valueOf( panel.getAttributeValue( "x" ) ).intValue();
        int myY = Integer.valueOf( panel.getAttributeValue( "y" ) ).intValue();
        int myWidth = Integer.valueOf( panel.getAttributeValue( "width" ) ).intValue();
        int myHeight = Integer.valueOf( panel.getAttributeValue( "height" ) ).intValue();
        if ( ( myY + myHeight ) > y )
        {
            int right = myX + myWidth;
            if ( x > myX && x < right )
            {
                int newHeight = y - myY;
                panel.setAttribute( "height", String.valueOf( newHeight ));
            }
        }
    }
    
    public void doFakeCRArea() throws Exception
    {
        String myWidth = answer.getAttributeValue( "height" );
        doCRArea();
        if ( myWidth == null )
            adjustGridHeightIfNeeded();
    }
    
    public void doCRArea() throws Exception
    {
        if ( answer != null )
        {
            String myX = answer.getAttributeValue( "x" );
            String myY = answer.getAttributeValue( "y" );
            int answerY;
            if ( myY == null )
            {
                answerY = getAnswerY( Integer.valueOf( myX ).intValue() );
                    answerY += fontLeading;
                answer.setAttribute( "y", String.valueOf( answerY ) );
            }
            else
                answerY = Integer.valueOf( myY ).intValue();
            String myHeight = answer.getAttributeValue( "height" );
            String myWidth = answer.getAttributeValue( "width" );
            int height = maxLayoutBottom - answerY;
            if ( myHeight == null )
                answer.setAttribute( "height", String.valueOf( height ));
            myHeight = answer.getAttributeValue( "height" );
            myY = answer.getAttributeValue( "y" );
            int bottom = Integer.valueOf( myY ).intValue() + Integer.valueOf( myHeight ).intValue();
            if ( bottom > maxLayoutBottom )
            {
                int newHeight = maxLayoutBottom - Integer.valueOf( myY ).intValue();
                answer.setAttribute( "height", String.valueOf( newHeight ) );
            } 
        }
    }
    
    public void doAnswerChoice() throws Exception
    {
        if ( answer != null )
        {
            String myX = answer.getAttributeValue( "x" );
            String myY = answer.getAttributeValue( "y" );
            int answerY;
            if ( myY == null )
            {
                answerY = getAnswerY( Integer.valueOf( myX ).intValue() );
 //               if ( answerY == startYAfterDirection )
                    answerY += fontLeading;
                answer.setAttribute( "y", String.valueOf( answerY ) );
            }
            else
                answerY = Integer.valueOf( myY ).intValue();
            String myHeight = answer.getAttributeValue( "height" );
            String myWidth = answer.getAttributeValue( "width" );
            int height = calculateAnswerHeight( "vertical".equals( answer.getAttributeValue( "layout" ))
                    							, Integer.valueOf( myWidth ).intValue(), answer );
            if ( myHeight == null )
                answer.setAttribute( "height", String.valueOf( height ));
            myHeight = answer.getAttributeValue( "height" );
            myY = answer.getAttributeValue( "y" );
            int bottom = Integer.valueOf( myY ).intValue() + Integer.valueOf( myHeight ).intValue();
            if ( bottom > maxLayoutBottom )
            {
                int newHeight = maxLayoutBottom - Integer.valueOf( myY ).intValue();
                answer.setAttribute( "height", String.valueOf( newHeight ) );
            } 
        }
    }
    
    public int getAnswerY( int startX )
    {
        boolean set = false;
        int answerY = startYAfterDirection;
        if ( stem != null )
        {
            int stemX = Integer.valueOf( stem.getAttributeValue( "x" )).intValue();
            if ( ( startX - stemX ) <= defaultSpacing )
            {
                String stemY = stem.getAttributeValue( "y" );
                String stemHeight = stem.getAttributeValue( "height" );
                answerY = Integer.valueOf( stemY ).intValue() + Integer.valueOf( stemHeight ).intValue();
                set = true;
            }
        }
        if ( !set && stimulus != null )
        {
            int width = Integer.valueOf( stimulus.getAttributeValue( "width" ) ).intValue();
//            if ( width > 600 )
//            {
                int stimulusX = Integer.valueOf( stimulus.getAttributeValue( "x" )).intValue();
                if ( ( startX - stimulusX ) <= defaultSpacing )
                {
                    String stimulusY = stimulus.getAttributeValue( "y" );
                    String stimulusHeight = stimulus.getAttributeValue( "height" );
                    answerY = Integer.valueOf( stimulusY ).intValue() + Integer.valueOf( stimulusHeight ).intValue();
                }
//            }
        }
        return answerY;
    }
    
    public int calculateAnswerHeight( boolean vertical, int width, Element panel ) throws Exception
    {
        int text_to_scrollbar_margin = 3;
        int i, height = 0;
        int widgetMaxWidthAllowed = width - defaultSpacing - 6 - 24 - scrollbarWidth - text_to_scrollbar_margin;
        List content = extractAllElement( ".//" + text_widget, panel );
        List content2 = extractAllElement( ".//" + image_widget, panel );
        content.addAll( content2 );
        int spacing = Integer.valueOf( answer.getAttributeValue( "spacing" ) ).intValue();
        if ( vertical || "answer_choices_absolute_positioned_panel".equals( panel.getName() ))
        {
            for ( i = 0; i < content.size(); i++ )
            {
                Element widget = ( Element )content.get( i );
                 if(widget.getAttributeValue( "width" ) != null)
                {
	                int widgetWidth = Integer.valueOf( widget.getAttributeValue( "width" ) ).intValue();
	                int widgetHeight = Integer.valueOf( widget.getAttributeValue( "height" ) ).intValue();
	                if ( widgetWidth > widgetMaxWidthAllowed )
	                {
	                    widget.setAttribute( "width", String.valueOf( widgetMaxWidthAllowed ));
	                    widgetHeight = widgetHeight * ( ( widgetWidth - 1 ) / widgetMaxWidthAllowed + 1 );
	                    if(widgetHeight < ChoiceLayoutMaster.radioButtonSize)
	                        widgetHeight = ChoiceLayoutMaster.radioButtonSize;
	                    height += widgetHeight;
	                    widget.setAttribute( "height", String.valueOf( widgetHeight ));
	                }
	                else
	                {
	                    if(widgetHeight < ChoiceLayoutMaster.radioButtonSize)
	                        widgetHeight = ChoiceLayoutMaster.radioButtonSize;
	                    height += widgetHeight;
	                    widget.setAttribute( "height", String.valueOf( widgetHeight ));
	                    if ( (text_widget.equals( widget.getName() ) || "text".equals( widget.getName() ) )
	                        && !"cell".equals(widget.getParentElement().getName()))
	                        widget.setAttribute( "width", String.valueOf( widgetMaxWidthAllowed ));
	                }
                }
                else
                {
                    height += ChoiceLayoutMaster.radioButtonSize;
                    widget.setAttribute( "height", String.valueOf( ChoiceLayoutMaster.radioButtonSize ));
                    if ( (text_widget.equals( widget.getName() ) || "text".equals( widget.getName() ) )
                        && !"cell".equals(widget.getParentElement().getName()))
                        widget.setAttribute( "width", String.valueOf( widgetMaxWidthAllowed ));
                }
                
            }
            height += ( spacing * ( content.size() ) );
        }
        else 
        {
            for ( i = 0; i < content.size(); i++ )
            {
                Element widget = ( Element )content.get( i );
                int widgetHeight = widget.getAttributeValue( "height" ) == null? 1:Integer.valueOf( widget.getAttributeValue( "height" ) ).intValue();
                if ( widgetHeight > height )
                    height = widgetHeight;
            }
            height += spacing;
        }
        return height;
    }
    
    public int addBRHeight( Element widget, int lineHeight )
    {
        int returnHeight = 0;
        String numberBR = widget.getAttributeValue( "numberOfBR" );
        if ( numberBR != null )
        {
            returnHeight = ( Integer.valueOf( numberBR ).intValue() - 1 ) * lineHeight;
            if ( returnHeight < 0 )
                returnHeight = 0;
            if ( numberBR.equals( "1" ) && returnHeight == 0 )
               returnHeight = lineHeight;
        }
        return returnHeight;
    }
    
 public void getImageHeight(List imageH, String inlineImage) {
        
        int startIndex = inlineImage.indexOf("<image_widget");
        int imageHeight = 0;
        if (startIndex != -1) {
            int heightIndex = inlineImage.indexOf("height",startIndex);
            if (heightIndex != -1) {
                
                String subText = inlineImage.substring(heightIndex,inlineImage.indexOf("width",startIndex));
                String [] result = subText.trim().split("=");
                String image_H = result[1].substring(1,(result[1].length() - 1));
                imageHeight = Integer.valueOf(image_H).intValue();
                int tempImageHeight = ((Integer)imageH.get(0)).intValue();
                imageHeight += tempImageHeight +24;
                imageH.remove(0);
                imageH.add(0, new Integer(imageHeight));
            }
            getImageHeight (imageH,inlineImage.substring(inlineImage.indexOf("/>",startIndex)+2));
        }
        
    }
    
    public boolean isInlineImageExist (String text) {
        int index = text.indexOf("<image_widget");
        if (index != -1) {
            return true;
        } 
        return false;   
        
    }
    
    public int calculateHeight( boolean vertical, int width, Element panel, int spacing ) 
    {
        int text_to_scrollbar_margin = 3;
        int i, height = 0;
        int newWidth = width - scrollbarWidth - defaultSpacing - text_to_scrollbar_margin;
        List content = panel.getContent();
        if ( vertical )
        {
            for ( i = 0; i < content.size(); i++ )
            {
                Element widget = ( Element )content.get( i );
                if ( widget.getAttributeValue( "width" ) == null 
                                    || widget.getAttributeValue( "height" ) == null )
                    continue;
                int widgetWidth = Integer.valueOf( widget.getAttributeValue( "width" ) ).intValue();
                int widgetHeight = Integer.valueOf( widget.getAttributeValue( "height" ) ).intValue();
                int lineHeight = widgetHeight;
                int imageHeight = 0;
                
                String text = widget.getText();
                if (text != null && text.length() > 0) {
                    
                    if (isInlineImageExist (text)) {
                        List imageH = new ArrayList();
                        imageH.add(new Integer(0));
                        getImageHeight (imageH, text); 
                        imageHeight = ((Integer)imageH.get(0)).intValue();
                        
                    }
           
                    
                }
                
                if ( widgetWidth > newWidth )
                {
                    if ( text_widget.equals( widget.getName()) || "text".equals( widget.getName()) )
                    {
                    		if (imageHeight > 0 && imageHeight > widgetHeight ) {
                            
	                            widget.setAttribute( "width", String.valueOf( newWidth ));
	                            double val = ( widgetWidth - 1 ) / newWidth;
	                            try {
	                                
	                                //widgetHeight = widgetHeight * (new Double(Math.ceil(val)).intValue()  + 1);
	                                widgetHeight = widgetHeight * (new Double(val).intValue()  + 1);
	                            } catch (Exception e) {
	                                e.printStackTrace();
	                            }
                            
                            
                            
	                            widgetHeight += imageHeight;
	                            widgetHeight += addBRHeight( widget, lineHeight );
	                            widget.setAttribute( "height", String.valueOf( widgetHeight ));
	                            height += widgetHeight;
                        } else {
			                    widget.setAttribute( "width", String.valueOf( newWidth ));
			                    widgetHeight = widgetHeight * ( ( widgetWidth - 1 ) / newWidth + 1 );
			                    widgetHeight += addBRHeight( widget, lineHeight );
			                    widget.setAttribute( "height", String.valueOf( widgetHeight ));
		                        height += widgetHeight;
                        }
                    }
                    else
                        height += widgetHeight;
                }
                else
                {
                	if (imageHeight > 0 && imageHeight > widgetHeight ) {
                        
                        widgetHeight = widgetHeight * ( ( widgetWidth - 1 ) / newWidth + 1);
                        widgetHeight += imageHeight;
                        height += widgetHeight;
            		} else {
            			height += widgetHeight;
            		}
                    if ( text_widget.equals( widget.getName() ) || "text".equals( widget.getName() ) )
                    {
                        widget.setAttribute( "width", String.valueOf( newWidth ));
                        height += addBRHeight( widget, lineHeight );
                    }
                }
            }
            height += ( spacing * ( content.size() - 1 ) ) + text_to_scrollbar_margin;
        }
        else
        {
            for ( i = 0; i < content.size(); i++ )
            {
                Element widget = ( Element )content.get( i );
                String thisheight = widget.getAttributeValue( "height" );
                if ( thisheight != null )
                {
                    int widgetHeight = Integer.valueOf( thisheight ).intValue();
                    if ( widgetHeight > height )
                        height = widgetHeight;
                }
            }
            height += spacing;
        }
        return height;
    }
    
    public static void setElementAttribute( Element element, String key, String value )
    {
        if ( key != null && value != null )
            element.setAttribute( key, value );
    }
    
    public void setAllDefaultAttributes( Element lml, Element src )
    {
        setElementAttribute( lml, "x", src.getAttributeValue( "x" ));
        setElementAttribute( lml, "y", src.getAttributeValue( "y" ));
        setElementAttribute( lml, "width", src.getAttributeValue( "width" ));
        setElementAttribute( lml, "height", src.getAttributeValue( "height" ));
        setElementAttribute( lml, "layout", src.getAttributeValue( "widgetlayout" ));
	//	setElementAttribute( lml, "alt", src.getAttributeValue( "Alt" ));
        String spacing = src.getAttributeValue( "widgetspacing" );
        if ( spacing == null )
            spacing = String.valueOf( defaultSpacing );
        setElementAttribute( lml, "spacing", spacing );
    }
    
    public Font ctbFont = null;
    public Font ctbBoldFont = null;
    
    public Font getFont( boolean developement ) throws Exception
    {
        if ( ctbFont == null )
        {
            Font newFont = Font.createFont( Font.TRUETYPE_FONT, this.getClass().getResourceAsStream("/fonts/OASmathv3.ttf"));
	        ctbFont = newFont.deriveFont( (float) pointSize );
        }
        return ctbFont;
    }
    
    public Font getBoldFont() throws Exception
    {
        if ( ctbBoldFont == null )
        {
            Font newFont = Font.createFont( Font.TRUETYPE_FONT, this.getClass().getResourceAsStream("/fonts/OASmathv3 Bold.ttf"));
	        ctbBoldFont = newFont.deriveFont( (float) pointSize );
        }
        return ctbBoldFont;
    }
    
    StringBuffer textBuffer = null;
    Element boundingElement = null;
    boolean bInTextElement;
    boolean isInlineImage;
    boolean isInlineImageSet;
    
    public int numberOfBR( String text )
    {
        int number = 0;
        int index = 0;
        while ( index != -1 )
        {
            index = text.indexOf( "<br/>", index );
            if ( index >= 0 )
            {
                index++;
                number++;
            }
        }
        return number;
    }
    
    public String replaceUnicode( String text )throws Exception
    {
        return AssessmentLayoutProcessor.replaceUnicode( text, unicodeList );
    }
    
    public String generatePattern( String pattern, int numberOfTime )
    {
        StringBuffer sb = new StringBuffer();
        for ( int i = 0; i < numberOfTime; i++ )
            sb.append( pattern );
        return sb.toString();
    }
    
    public String replaceSpacePattern( String src ) 
    {
        String pattern = "space#";
        String returnString = src;
        int startIndex, numberIndex, numberOfDigit;
        while ( ( startIndex = returnString.indexOf( pattern ) ) >= 0 )
        {
            numberIndex = startIndex + pattern.length();
            numberOfDigit = 0;
            while ( numberIndex < returnString.length() )
            {
                char ch = returnString.charAt( numberIndex );
                if ( Character.isDigit( ch ) )
                {
                    numberOfDigit++;
                    numberIndex++;
                }
                else
                    break;
            }
            if ( numberOfDigit > 0 )
            {
                String number = returnString.substring( startIndex + pattern.length()
                                            , startIndex + pattern.length() + numberOfDigit );
                returnString = returnString.substring( 0, startIndex ) 
                                    + generatePattern( "&nbsp;", Integer.valueOf( number ).intValue() ) 
                                    + returnString.substring( startIndex + pattern.length() + numberOfDigit );
            }
            else
            {
                returnString = returnString.substring( 0, startIndex ) + returnString.substring( startIndex + pattern.length() );
            }
        }
        return returnString;
    }
    
    public static String replaceAll( String src, String toBeReplace, String replaceWith )
    {
    	String result = src;
    	int index = 0;
    	int difference = replaceWith.length();
    	while ( ( index = result.indexOf( toBeReplace, index )) >= 0 )
    	{
    		result = result.substring( 0, index ) + replaceWith + result.substring( index + toBeReplace.length() );
    		index += difference;
    	}
    	return result;
    }

    public void cleanTextBuffer( List childSrcList ) throws Exception
    {
        if ( textBuffer.length() > 0 )
        {
//        	if (textBuffer.length() == 1 && (textBuffer.indexOf(" ")!= -1)) {
//        		textBuffer.delete(0, 1);
//        	}
            Element widgetElement;
            if ( boundingElement == null )
            {
                widgetElement = new Element( text_widget );
                widgetElement.setAttribute( "halign", "left" );
            }
            else
                widgetElement = boundingElement;
            String text = textBuffer.toString();
            text = text.replaceAll( "\n", " ");
            text = text.trim();
            text = text.replaceAll( "WQW", "&nbsp;" );
            text = replaceAll( text, "+", "&#x002B;" );
            text = replaceUnicode( text );
            text = replaceSpacePattern( text );
            int numberBR = numberOfBR( text );
            Font font = null;
            boolean hasBoldSetting = text.indexOf( "<b>" ) >= 0;
            if ( hasBoldSetting )
                font = getBoldFont();
            else
                font = getFont( developement );
            String tempText = text.replaceAll( "&quot;", "\"");
            tempText = tempText.replaceAll( "&nbsp;", "!"); // was space " "  here before
            tempText = tempText.replaceAll( "&apos;", "'");
            tempText = tempText.replaceAll( "<b>", "");
            tempText = tempText.replaceAll( "</b>", "");
            tempText = tempText.replaceAll( "<i>", "");
            tempText = tempText.replaceAll( "</i>", "");
            tempText = tempText.replaceAll( "<u>", "");
            tempText = tempText.replaceAll( "</u>", "");
            tempText = tempText.replaceAll( "&amp;#x00", "");
            tempText = tempText.replaceAll( "&amp;#x", "");
            if(tempText != null && tempText.length()>0){
	            TextLayout layout = new TextLayout( tempText, font, new FontRenderContext( new AffineTransform(), false, false ));
	            Rectangle2D bounds = layout.getBounds();
	            
	//            int adjustWidth = 0;
	//            if (this.insideTable) {
	//                adjustWidth = 5; //todo: remove hardcode
	//            }
	            
	            int height = ( int )(bounds.getHeight()) + textBoxExtraHeight;
	            int width = ( int )(bounds.getWidth()+textBoxExtraWidth);
	            
	            if ( widgetElement.getAttributeValue( "width" ) == null )
	                widgetElement.setAttribute( "width", String.valueOf( width ));
	            if ( widgetElement.getAttributeValue( "height" ) == null )
	            {
	                widgetElement.setAttribute( "height", String.valueOf( height ));
	/*                if ( height > 25 )
	                    widgetElement.setAttribute( "height", String.valueOf( height ));
	                else
	                    widgetElement.setAttribute( "height", "25" ); */
	            }
            }
            if ( addCData )
            {
                CDATA element = new CDATA( text );
                widgetElement.addContent( element );
            }
            else
                widgetElement.setText( text );
            widgetElement.setAttribute( "numberOfBR", String.valueOf( numberBR ));
            if (this.insideAcknowledgement) {
                if (acknowledgementList != null)
                    acknowledgementList.add(widgetElement);
            }
            else
                childSrcList.add( widgetElement );
            textBuffer = new StringBuffer();
            boundingElement = null;
        }
    }
    
    public void copyImage( String srcFile, String destFile ) throws Exception
    {
        PlainTextInputStream fis = (PlainTextInputStream) new URL(srcFile).getContent();
        FileOutputStream fos = new FileOutputStream( new File( destFile ) );
//        FileInputStream fis = new FileInputStream( new File( srcFile ) );
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Pipe pipe = new Pipe( fis, bos );
        pipe.run();
        fis.close();
        fos.write( bos.toByteArray() );
        fos.close();
    }
    
    public String copyThisImage( String path ) throws Exception
    {
        String src = path;        
        String dest = "c:/eclipse/workspace/application-contentpreview/contentPreviewWeb/PresentationCanvas/images/"
                        + path.substring( path.lastIndexOf( "/" ) + 1 );
        copyImage( src, dest );
        return "images/" + path.substring( path.lastIndexOf( "/" ) + 1 );
    }

    public void getProcessableChildInOrder( Element element, List childSrcList)throws Exception
    {
        List childList = element.getContent();
        for ( int i = 0; i < childList.size(); i++ )
        {
            Object thisObject = ( Object )childList.get( i );
            if ( thisObject instanceof Element )
            {
                Element thisElement = ( Element )thisObject;
                String name = thisElement.getName();
                if ( name.equals( "Graphic" ))
                {
                    cleanTextBuffer( childSrcList );
                    if ( processingStimulus && tabStimulus && thisElement.getAttributeValue( "TabTitle" ) != null)
                    {
                        Element stimulus_tab = new Element( "stimulus_tab" );
                        stimulus_tab.setAttribute( "title", getAttributeValue( thisElement, "TabTitle" ));
                        List childLMLList = new ArrayList();
                        getProcessableChildInOrder( thisElement, childLMLList );
                        cleanTextBuffer( childLMLList );
                        for ( int j = 0; j < childLMLList.size(); j++ )
                            stimulus_tab.addContent( ( Element )childLMLList.get( j ));
                        childSrcList.add( stimulus_tab );
                    }
                    else              
                        getProcessableChildInOrder( thisElement, childSrcList );
                }
                else if ( name.equals( "Flash" ) )
                {
                    Element widgetElement = null;
					String inlineImage = "";
                   	Element textWidget = null;
                    if ( bInTextElement )
                    {
                    	if (childSrcList.size() != 0 ) {
                    		textWidget = (Element)childSrcList.get(childSrcList.size()-1);
                    	} else {
                    		                    		
                    		if ( boundingElement == null )
                            {
                                widgetElement = new Element( text_widget );
                                widgetElement.setAttribute( "halign", "left" );
                            }
                            else
                                widgetElement = boundingElement;
                    		
                    		CDATA element1 = new CDATA( " " );
                            widgetElement.addContent( element1 );
                            childSrcList.add( widgetElement );
                            //widgetElement = null;
                            textWidget = (Element)childSrcList.get(childSrcList.size()-1);
                            boundingElement = null;
                    	}
                    	
                        widgetElement = new Element( "image_widget" );
                        Element parent = thisElement.getParentElement();
                        if (parent.getAttributeValue( "inlineAlign" ) != null) {
                        	widgetElement.setAttribute("valign", parent.getAttributeValue( "inlineAlign") );
                        }
                        if (parent.getAttributeValue( "height" ) != null) {
                        	widgetElement.setAttribute("height", parent.getAttributeValue( "height") );
                        }
                        if(parent.getAttributeValue( "width" ) != null) {
                        	widgetElement.setAttribute("width", parent.getAttributeValue( "width") );
                        }
                       
                      isInlineImage = true;
                      isInlineImageSet = true;
                    }
                    else
                    {
                        widgetElement = new Element( image_widget );
                        Element parent = thisElement.getParentElement();
                        widgetElement.setAttribute( "halign", parent.getAttributeValue( "halign" ) );
                    }
                    
               		widgetElement.setAttribute("isaudio", thisElement.getAttributeValue( "isaudio") );
               		widgetElement.setAttribute("autoplay", thisElement.getAttributeValue( "autoplay") );
               		widgetElement.setAttribute("playorder", thisElement.getAttributeValue( "playorder") );
               		widgetElement.setAttribute("playonce", thisElement.getAttributeValue( "playonce") );

                    String srcImage = getAttributeValue( thisElement, "FileName" );
                    widgetElement.setAttribute( "id", getImageId( srcImage ) );
                    //widgetElement.setAttribute( "src", getImagePath( srcImage ) );
                    //SM   - Needed for publishing but not for previewer
                    srcImage = "http://mcsdoas15.mhe.mhc:9000/images/" + srcImage.substring(srcImage.indexOf("/images/") + 8).replaceAll(" ", "%20");
           //         srcImage = "C:/mappingdata/DEMO/inline_images/formula2.swf";
                    //SM Commented the first line and added the second
                    //widgetElement.setAttribute( "src", getImagePath( srcImage ) );
                    widgetElement.setAttribute( "src", srcImage );
                    handleImageAttributes( thisElement, widgetElement, true );
       //             widgetElement.setAttribute( "src", tempImage );
                    String altText = getAttributeValue( thisElement.getParentElement(), "Alt" );
                    if ( altText != null )
                        widgetElement.setAttribute( "alt_text", altText.replaceAll( "&quot;", "\"" ) );
                      
                    
                    //srcImage = "http://coruscant:9000/images/" + srcImage.substring(srcImage.indexOf("/images/") + 8);
                    //widgetElement.setAttribute( "src", getImagePath( srcImage ) );
                                    
                    if (isInlineImage) {
                        //Change ElementtoString
                        XMLOutputter xmloutput = new XMLOutputter();
                        inlineImage = xmloutput.outputString(widgetElement);
                        if(textWidget.getAttribute("width") == null)
                        {
                        	String s = widgetElement.getAttribute("width").getValue();
                        	textWidget.setAttribute("width", s);
                    	}
                        if(textWidget.getAttribute("height") == null)
                        {
                        	String s = widgetElement.getAttribute("height").getValue();
                        	textWidget.setAttribute("height", s);
                    	}
                        
                        List Cdata = textWidget.getContent();
                        Text cdata = (Text)Cdata.get(0);
                        String textCada = cdata.getText();
                        //add inline_image to CDATA
                        textCada = textCada.concat(" "+inlineImage+" ");
                        //remove existing CDATA
                        textWidget.removeContent(cdata);
                        //add updated CDATA
                      //  textWidget.addContent(new CDATA(textCada));
                      //add updated text with inline image
                        textWidget.addContent(textCada);
                    } else {
                        childSrcList.add( widgetElement );
                    }
                }
                else if ( name.equals( "Table" ) )
                {
                    this.insideTable = true;
                    Element tableElement = new Element(table_widget);
                    translateTableElement(thisElement, tableElement);                  
                    
                    List rowList = thisElement.getChildren("Row");    
                    Iterator rowIt = rowList.iterator();
                    
                    int tableWidth = 0; //todo: remove hardcode
                    int tableHeight = 60; //todo: remove hardcode
                    while (rowIt.hasNext())
                    {
                        Element currentRow = (Element) rowIt.next();
                        Element rowElement = new Element("row");
                        
                        translateRowElement(currentRow, rowElement);

                        List cellList = currentRow.getChildren("Cell");    
                        Iterator cellIt = cellList.iterator();
                        
                        int rowHeight = 0;
                        int rowWidth = 0;
                        while (cellIt.hasNext())
                        {
                            Element currentCell = (Element) cellIt.next();
                            Element cellElement = new Element("cell");
                            translateCellElement(currentCell, cellElement);  
                            
                            cleanTextBuffer( childSrcList );
                            List childLMLList = new ArrayList();
                            getProcessableChildInOrder( currentCell, childLMLList );
                            cleanTextBuffer( childLMLList );
                            int cellWidth = 0;
                            if (cellElement.getAttribute("width") != null)
                                cellWidth = Integer.parseInt((String)cellElement.getAttributeValue("width"));
                            
                            int cellHeight = tableCellTopBottomPadding*2;
                            for ( int j = 0; j < childLMLList.size(); j++ ) {
                                Element childElement = ( Element )childLMLList.get( j );
                                int width = tableCellLeftRightPadding*2+getIntFromString(childElement.getAttributeValue("width"));
                                int height = getIntFromString(childElement.getAttributeValue("height"));
                                if (cellElement.getAttribute("width") == null && width > cellWidth) {
                                    cellWidth = width;  
                                }
                                if (cellElement.getAttribute("width") != null && width > cellWidth) {
                                    height = (int) ((height+5) * Math.round(Math.ceil(1.0*width/cellWidth)));
                                    childElement.setAttribute("width", String.valueOf(cellWidth));
                                }
                                cellHeight = cellHeight + height;                              

                                cellElement.addContent(childElement);

                            }
                            if (cellElement.getAttribute("width") == null)
                                cellElement.setAttribute("width", String.valueOf(cellWidth));
                            rowWidth = rowWidth + cellWidth;    
                            if (cellHeight > rowHeight )
                                rowHeight = cellHeight;
                            rowElement.addContent(cellElement);
                        }
                        if (rowElement.getAttribute("height") == null)
                            rowElement.setAttribute("height", String.valueOf(rowHeight));
                        else 
                            rowHeight = Integer.parseInt((String)rowElement.getAttributeValue("height"));
                        
                        if (tableWidth < rowWidth)
                            tableWidth = rowWidth;
                        tableHeight = tableHeight + rowHeight;    
                        tableElement.addContent(rowElement);
                    }
                    
                    tableWidth = tableWidth + 30; //todo: remove hardcode here
                    tableElement.setAttribute("height", String.valueOf(tableHeight));
                    tableElement.setAttribute("width", String.valueOf(tableWidth));
                    childSrcList.add( tableElement );
                    this.insideTable = false;
                }
                
                else if ( name.equals( "BR" ))
                    textBuffer.append( "<br/>");
                else if ( name.equals( "B" ) || name.equals( "U" ) || name.equals( "I" ) )
                {
                    textBuffer.append( "<" + name.toLowerCase()+ ">" );
                    getProcessableChildInOrder( thisElement, childSrcList );
                    textBuffer.append( "</" + name.toLowerCase()+ ">" );
                }
                else if ( name.equals( "Passage" ))
                {
                    acknowledgementList = new ArrayList();
                    cleanTextBuffer( childSrcList );
                    Element passageElement = null;
                    List childLMLList = new ArrayList();
                    if ( tabStimulus )
                    {
                        Element stimulus_tab = new Element( "stimulus_tab" );
                        stimulus_tab.setAttribute( "title", getAttributeValue( thisElement, "TabTitle" ));

                        String newID = getAttributeValue( thisElement, "ID" );
                        if (newID == null) { // if ID not in Passage, then get it from parent stimulus
                            Element stimulus = (Element) thisElement.getParentElement();
                            newID = stimulus.getAttributeValue( "DisplayID" );
                            if ( newID == null )
                                newID = getAttributeValue( stimulus, "ID" );
                        }

                        passageElement = new Element( passage_widget );
                        passageElement.setAttribute( "id", newID );
                        stimulus_tab.addContent( passageElement );
                        childSrcList.add( stimulus_tab );
                    }
                    else {
                        passageElement = this.stimulus;
                        for ( int j = 0; j < childSrcList.size(); j++ ) {
                            passageElement.addContent( ( Element )childSrcList.get( j ));
                            childSrcList.remove(j);
                        }
                    }
                    passageElement.setAttribute( "halign", "left" );
                    getProcessableChildInOrder( thisElement, childLMLList );
                    cleanTextBuffer( childLMLList );
                    for ( int j = 0; j < childLMLList.size(); j++ )
                        passageElement.addContent( ( Element )childLMLList.get( j ));
                    hasPassage = true;
                    
                    Element lineBreaker;
                    
                    // add footnotes at the end of passages
                    List footnoteList = extractAllElement(".//Footnote", thisElement);
                    
                    if (footnoteList.size() > 0) {
                        lineBreaker = new Element(text_widget);
                        lineBreaker.addContent("&nbsp;");
                        lineBreaker.setAttribute("alt_text", "");
                        lineBreaker.setAttribute("halign", "left");
                        passageElement.addContent(lineBreaker);
                        
                    }
                    Iterator it = footnoteList.iterator();
                    while (it.hasNext()) {
                        Element srcFootnote = (Element) it.next();
                        
                        String header = srcFootnote.getAttributeValue( "header" );
                        if ( header != null && header.trim().length() > 0 )
                        {
                            header = replaceUnicode( header );
                        }
                        String detail = srcFootnote.getAttributeValue( "detail" );
                        if ( detail != null && detail.trim().length() > 0 )
                        {
                            detail = replaceUnicode( detail );
                        }

                        
                        
                        Element footnote = new Element( text_widget );
                        StringBuffer buf = new StringBuffer();
                        buf.append("<b>").append(header).append("</b>&nbsp;&nbsp;");
                        buf.append(detail);
                        footnote.addContent(buf.toString());
                        String altText = srcFootnote.getParentElement().getAttributeValue("AltText");
                        if (altText != null && "".equals(altText.trim()))
                            footnote.setAttribute("alt_text", altText);
                        footnote.setAttribute("text_magnification","0.83");
                        footnote.setAttribute("halign", "left");
                        passageElement.addContent(footnote);
                    }
                    
                    if (this.includeAcknowledgement)
                    {
                    
                        if (this.acknowledgementList.size() > 0) {
                            lineBreaker = new Element(text_widget);
                            lineBreaker.addContent("&nbsp;");
                            lineBreaker.setAttribute("alt_text", "");
                            lineBreaker.setAttribute("halign", "left");
                            passageElement.addContent(lineBreaker);
                        }
                        
                        //add acknowledgements at the end of passage
                        it = this.acknowledgementList.iterator();
                        while (it.hasNext()) {
                            Element ackText = (Element) it.next();
                            ackText.setAttribute("text_magnification","0.83");
                            ackText.setAttribute("halign", "left");
                            passageElement.addContent(ackText);
                        }
                    
                    }                    
                }
                else if ( name.equals( "P" ) )
                {
                    if ( bInTextElement )
                    {
                        textBuffer.append( "<p>" );
                        getProcessableChildInOrder( thisElement, childSrcList );
                        textBuffer.append( "</p>" );
                    }
                    else
                    {
                        cleanTextBuffer( childSrcList );
                        boundingElement = new Element( text_widget );
                        handleParaAttributes( thisElement, boundingElement );
                        getProcessableChildInOrder( thisElement, childSrcList );
                    }
                }
                else if ( name.equals( "Byline" ) || name.equals( "Heading" ) )
                {
                    String haligh = thisElement.getAttributeValue( "halign" );
                    getProcessableChildInOrder( thisElement, childSrcList );
                    cleanTextBuffer( childSrcList );
                    setTextWidgetAttribute( childSrcList, "halign", haligh );
                }
                else if ( name.equals( "Text" ) )
                {
                    cleanTextBuffer( childSrcList );
                    if ( processingStimulus && tabStimulus 
                                    && thisElement.getAttributeValue( "TabTitle" ) != null )
                    {
                        Element stimulus_tab = new Element( "stimulus_tab" );
                        stimulus_tab.setAttribute( "title", getAttributeValue( thisElement, "TabTitle" ));
              //          Element tabElement = new Element( passage_widget );
              //          stimulus_tab.addContent( tabElement );
                        List childLMLList = new ArrayList();
                        bInTextElement = true;
                        boundingElement = new Element( text_widget );
                        boundingElement.setAttribute( "halign", thisElement.getAttributeValue( "halign" ) );
                        //halign
                        String altText = getAttributeValue( thisElement, "Alt" );
                        if ( altText != null )
                            boundingElement.setAttribute( "alt_text", altText.replaceAll( "&quot;", "\"" ) );
       //                 boundingElement.setAttribute( "multiline", "true" );
                        setElementAttribute( boundingElement, "width", thisElement.getAttributeValue( "width" ));
                        setElementAttribute( boundingElement, "height", thisElement.getAttributeValue( "height" ));
                        getProcessableChildInOrder( thisElement, childLMLList );
                        cleanTextBuffer( childLMLList );
                        for ( int j = 0; j < childLMLList.size(); j++ )
                            stimulus_tab.addContent( ( Element )childLMLList.get( j ));
                        childSrcList.add( stimulus_tab );
                        bInTextElement = false;
                    }
                    else
                    {
                        bInTextElement = true;
                        String text = thisElement.getText();
                        // boundingElement = new Element( "text" );
                        boundingElement = new Element( text_widget );
                        boundingElement.setAttribute( "halign", thisElement.getAttributeValue( "halign" ) );
                        String altText = getAttributeValue( thisElement, "AltText" );
                        if ( altText != null )
                            boundingElement.setAttribute( "alt_text", altText.replaceAll( "&quot;", "\"" ) );
      //                  boundingElement.setAttribute( "multiline", "true" );
                        setElementAttribute( boundingElement, "width", thisElement.getAttributeValue( "width" ));
                        setElementAttribute( boundingElement, "height", thisElement.getAttributeValue( "height" ));
                        getProcessableChildInOrder( thisElement, childSrcList );
                        if (!isInlineImage) {
                        	// this function create new <text_widget> and CDATA but in Inline_Image it is not required.
                            cleanTextBuffer( childSrcList );
                        } else {
                        	String restText = "";
                            if (textBuffer != null ) {
                                //add rest string in CDATA
                                
                                restText = textBuffer.toString();
                                restText = restText.replaceAll( "\n", " ");
                                restText = restText.trim();
                                restText = restText.replaceAll( "WQW", "&nbsp;" );
                                restText = replaceAll( restText, "+", "&#x002B;" );
                                restText = replaceUnicode( restText );
                                restText = replaceSpacePattern( restText );  
                            }
                            
                                Element textWidget = (Element)childSrcList.get(childSrcList.size()-1); 
                                textWidget.setAttribute("inline_image","yes");
                                List Cdata = textWidget.getContent();
                                Text cdata = (Text)Cdata.get(0);
                                String textCada = cdata.getText();
                                textCada = textCada.concat(restText);
                                textWidget.removeContent(cdata);
                                textWidget.addContent(new CDATA(textCada));
                                textBuffer = new StringBuffer();
                                isInlineImage = false;
                                                        
                        //childSrcList.add( widgetElement );
                        
                            
                        }
                        
                        bInTextElement = false;
                    }
                }
                else if ( name.equals( "Acknowledgment" ) )
                {
                    insideAcknowledgement = true;
                    getProcessableChildInOrder( thisElement, childSrcList );
                    insideAcknowledgement = false;
                    
                }
                else if ( name.equals( "Footnote" ) )
                {
/*                    cleanTextBuffer( childSrcList );
                    boundingElement = new Element( footnote_widget );
                    handleFootnoteParaAttributes( thisElement, boundingElement );
                    getProcessableChildInOrder( thisElement, childSrcList );
*/
                
                    String header = thisElement.getAttributeValue( "header" );
                    if ( header != null && header.trim().length() > 0 )
                    {
                        header = replaceUnicode( header );
                    }
                    String detail = thisElement.getAttributeValue( "detail" );
                    if ( detail != null && detail.trim().length() > 0 )
                    {
                        detail = replaceUnicode( detail );
                    }
                    
                    String readable = "true";
                    String altText = thisElement.getParentElement().getAttributeValue("AltText");
                    
                    if (altText != null && "".equals(altText.trim()))
                        readable = "false";
                    
                    textBuffer.append( "<footnote header=\"").append(header);
                    textBuffer.append("\" detail=\"").append(detail);
                    textBuffer.append("\" readable=\"").append(readable);
                    textBuffer.append("\"><a href=\"javascript:showFootnote('").append(URLEncoder.encode(header)).append("');\">");
                    getProcessableChildInOrder( thisElement, childSrcList );
                    textBuffer.append( "</a></footnote>" );
                }
                else if ( name.equals( "DistractorRationale" ) )
                {
                    Element textElement = (Element) thisElement.getChild("Text");
                    String text = textElement.getText();
                    text = replaceUnicode(text);
                    
                    Element rationaleElement = new Element(rationale_widget);
                    rationaleElement.setText(text);
                    childSrcList.add( rationaleElement );
                    
                }
                else if ( name.equals( "CRResponseArea" ) )
                {
                    boolean isMultiLine = false;
                    String type = thisElement.getAttributeValue( "Type" );
                    if ("multiLine".equals(type))
                        isMultiLine = true;
                    boolean inline = false;    
                    if (!isMultiLine && "Text".equals(thisElement.getParentElement().getName()))
                        inline = true;
                    
                    Element crElement = null;
                    if ( isMultiLine ) {
                        crElement = new Element(multi_line_answer_widget);
                        String width = thisElement.getAttributeValue("width");
                        String height = thisElement.getAttributeValue("height");
                        String accommodatedWidth = thisElement.getAttributeValue("accommodatedWidth");
                        String accommodatedHeight = thisElement.getAttributeValue("accommodatedHeight");
                        String charLimit = thisElement.getAttributeValue("charLimit");
                        
                        if (width != null)
                            crElement.setAttribute("width", width);
                        if (height != null)
                            crElement.setAttribute("height", height);
                        if (accommodatedWidth != null)
                            crElement.setAttribute("acc_width", accommodatedWidth);
                        if (accommodatedHeight != null)
                            crElement.setAttribute("acc_height", accommodatedHeight);
                        if (charLimit != null)
                            crElement.setAttribute("char_limit", charLimit);
                    } 
                    else {
                        String width = thisElement.getAttributeValue("width");
                        String height = thisElement.getAttributeValue("height");
                        if (height == null)
                            height = "20"; //default height for single line
                        String accommodatedWidth = thisElement.getAttributeValue("accommodatedWidth");
                        String accommodatedHeight = thisElement.getAttributeValue("accommodatedHeight");
                        String charLimit = thisElement.getAttributeValue("charLimit");
                        if (!inline) {
                            crElement = new Element(single_line_answer_widget);
                            crElement.setAttribute("type", "standalone");
                            
                            if (width != null)
                                crElement.setAttribute("width", width);
                            if (height != null)
                                crElement.setAttribute("height", height);
                            if (accommodatedWidth != null)
                                crElement.setAttribute("acc_width", accommodatedWidth);
                            if (accommodatedHeight != null)
                                crElement.setAttribute("acc_height", accommodatedHeight);
                            if (charLimit != null)
                                crElement.setAttribute("char_limit", charLimit);
                        }                            
                        else {
                            String crId = "widget"+Math.round(Math.random()*89999+10000);
                            textBuffer.append( " <single_line_answer id=\"").append(crId).append("\" type=\"inline\" ");
                            if (width != null)
                                textBuffer.append("width=\"").append(width).append("\" ");
                            if (height != null)
                                textBuffer.append("height=\"").append(height).append("\" ");
                            if (accommodatedWidth != null)
                                textBuffer.append("acc_width=\"").append(accommodatedWidth).append("\" ");
                            if (accommodatedHeight != null)
                                textBuffer.append("acc_height=\"").append(accommodatedHeight).append("\" ");
                            if (charLimit != null)
                                textBuffer.append("char_limit=\"").append(charLimit).append("\" ");
                            
                            textBuffer.append("/> ");
                            
                            this.boundingElement.setAttribute("input", "yes");
                        }
                                                    
                    }                 
                    if (!inline)
                        childSrcList.add( crElement );
                    
                }
                
                else 
                    getProcessableChildInOrder( thisElement, childSrcList );
            }
            else if ( thisObject instanceof ProcessingInstruction ) {
                // do nothing
            } 
            else if ( thisObject instanceof Text ) 
            {
                Text thisText = ( Text )thisObject;
                if(isInlineImage){
                	if (childSrcList.size() != 0 ) {
                		Element textWidget = (Element)childSrcList.get(childSrcList.size()-1);
                		List Cdata = textWidget.getContent();
                		Text cdata = (Text)Cdata.get(0);
                		int restTextHeight = 0;
                        int restTextWidth  = 0;
                        String textCada = cdata.getText();
                        //add inline_image to CDATA
                        if(thisText.getText() != null && thisText.getText().length()>0){
                        	
                        	
                        	textCada = textCada.concat(" "+thisText.getText()+" ");
                        }
                        //remove existing CDATA
                        textWidget.removeContent(cdata);
                        //add updated CDATA
                      //  textWidget.addContent(new CDATA(textCada));
                      //add updated text with inline image
                        textWidget.addContent(textCada);
                	}
                	else{
                		textBuffer.append( thisText.getValue() );
                	}
                }
                else{
                	textBuffer.append( thisText.getValue() );
                }
            } 
            else 
            {
                if(bInTextElement) 
                {
                    String text = (String) thisObject;
                    text = parseJDOMString( text );
                    textBuffer.append( text );
                }
            }
        }
    }
    
    private int getIntFromString(String str) {
        int result = 0;
        try {
            if (str != null) 
                result = Integer.parseInt(str);
        }
        catch (NumberFormatException e) {
            result = 0;
        }
        return result;
        
    }
    
    private void translateTableElement(Element thisElement, Element tableElement) throws Exception
    {
        String halign = getAttributeValue(thisElement, "halign");
        if (halign!=null)
            tableElement.setAttribute("halign", halign);
        String title = getAttributeValue(thisElement, "title");
        if (title!=null)
            tableElement.setAttribute("title", title);
        else
            tableElement.setAttribute("title", "");
        
        String titlewidth = getAttributeValue(thisElement, "titlewidth");
        if (titlewidth!=null)
            tableElement.setAttribute("titlewidth", titlewidth);
//        else
//            tableElement.setAttribute("titlewidth", title==null?"0":String.valueOf(title.length()*tableTitleFontSize));
        
        String border = getAttributeValue(thisElement, "border");
        if (border!=null)
            tableElement.setAttribute("border", border);
        String showvlines = getAttributeValue(thisElement, "showvlines");
        if (showvlines!=null)
            tableElement.setAttribute("showvlines", showvlines);
        String showhlines = getAttributeValue(thisElement, "showhlines");
        if (showhlines!=null)
            tableElement.setAttribute("showhlines", showhlines);
        String enlargeable = getAttributeValue(thisElement, "enlargeable");
        if (enlargeable!=null)
            tableElement.setAttribute("enlargeable", enlargeable);
        String autoenlargeable = getAttributeValue(thisElement, "autoenlargeable");
        if (autoenlargeable!=null)
            tableElement.setAttribute("autoenlargeable", autoenlargeable);
        
    }
    
    private void translateRowElement(Element currentRow, Element rowElement) throws Exception
    {
/*
        String bgcolor = getAttributeValue(currentRow, "bgcolor");
        if (bgcolor!=null) 
            rowElement.setAttribute("bgcolor", fixColor(bgcolor));
        String fgcolor = getAttributeValue(currentRow, "fgcolor");
        if (fgcolor!=null)
            rowElement.setAttribute("fgcolor", fixColor(fgcolor));
        String rowhalign = getAttributeValue(currentRow, "rowhalign");
        if (rowhalign!=null)
            rowElement.setAttribute("rowhalign", rowhalign);
        String rowvalign = getAttributeValue(currentRow, "rowvalign");
        if (rowvalign!=null)
            rowElement.setAttribute("rowvalign", rowvalign);
*/            
        String height = getAttributeValue(currentRow, "height");
        if (height!=null)
            rowElement.setAttribute("height", height);
        
    }
    
    private void translateCellElement(Element currentCell, Element cellElement) throws Exception
    {
        String bgcolor = getAttributeValue(currentCell, "bgcolor");
        if (bgcolor!=null)
            cellElement.setAttribute("bgcolor", fixColor(bgcolor));
        String fgcolor = getAttributeValue(currentCell, "fgcolor");
        if (fgcolor!=null)
            cellElement.setAttribute("fgcolor", fixColor(fgcolor));
        String halign = getAttributeValue(currentCell, "halign");
        if (halign!=null)
            cellElement.setAttribute("halign", halign);
        String valign = getAttributeValue(currentCell, "valign");
        if (valign!=null)
            cellElement.setAttribute("valign", valign);
        String width = getAttributeValue(currentCell, "width");
        if (width!=null)
            cellElement.setAttribute("width", width);
    
    }
    
    private String fixColor(String color) {
        if (color.indexOf("0x") < 0 && color.indexOf("0X") < 0)
            color = "0x"+color;
        return color;
    }
    
    public void setTextWidgetAttribute( List childSrcList, String key, String value )
    {
        if ( key != null && value != null )
        {
            for ( int i = 0; i < childSrcList.size(); i++ )
            {
                Element element = ( Element )childSrcList.get( i );
                if ( element.getName().equals( text_widget ) )
                    element.setAttribute( key, value );           
            }
        }
    }
    
    public static String startPattern = "\n      ";
    
    public String parseJDOMString( String text ) throws Exception
    {
        String returnString = text;
        if ( returnString.startsWith( startPattern ) )
        {
            returnString = returnString.substring( startPattern.length() );
            if ( returnString.length() > 0 )
            {
                char ch = returnString.charAt( 0 );
                if ( Character.isDigit( ch ) || Character.isLetter( ch ) )
                {
                    boolean fromBR = false;
                    if ( textBuffer.length() > 0 && textBuffer.toString().endsWith( "<br/>" ) )
                        fromBR = true;
                    if ( !fromBR )
                        returnString = " " + returnString;
                }
            }
        }
        return returnString;
    }
    
    public List extractBlocktext( Element element ) throws Exception
    {
        List childLMLList = new ArrayList();      
        textBuffer = new StringBuffer();
        bInTextElement = false;
        getProcessableChildInOrder( element, childLMLList );
        if (!isInlineImageSet) {
            cleanTextBuffer( childLMLList );
        } else {
            isInlineImageSet = false;
            isInlineImage = false;
        }
        
        return childLMLList;
    }

    public void handleImageAttributes( Element para, Element lml, boolean calculateSize ) throws Exception
    {
        String width = para.getAttributeValue( "width" );
        if ( width != null )
            lml.setAttribute( "width", width );
        String height = para.getAttributeValue( "height" );
        if ( height != null )
            lml.setAttribute( "height", height );
        String filePath = lml.getAttributeValue( "src" );

        String enlargeable = para.getAttributeValue( "Enlargeable" );
        if ( enlargeable != null )
            lml.setAttribute( "enlargeable", enlargeable );
        
        String autoenlargeable = para.getAttributeValue( "autoenlargeable" );
        if ( autoenlargeable != null )
            lml.setAttribute( "autoenlargeable", autoenlargeable );
        
        // this is ugly
/*        PlainTextInputStream asset = (PlainTextInputStream) new URL(filePath).getContent();
        int size = asset.available();
        asset.close(); 
           
        totalDownloadSize += size;  */
        totalDownloadSize += 1000;
        
        if ( calculateSize && ( width == null || height == null ))
        {
            boolean isJPGorBMP = false;
            boolean isSWF = false;
            String ext = filePath.substring( filePath.length() - 3 ).toLowerCase();
            if ( ext.equals( "bmp" ) || ext.equals( "jpg" ))
                isJPGorBMP = true;
            else if ( ext.equals( "swf" ))
                isSWF = true;
            if ( isJPGorBMP )
            {
                PlainTextInputStream asset = (PlainTextInputStream) new URL(filePath).getContent();
                BufferedImage image = ImageIO.read( asset );
                Integer widthINT = new Integer( image.getWidth() );
                Integer heightINT = new Integer( image.getHeight() );
                lml.setAttribute( "height", heightINT.toString() );
                lml.setAttribute( "width", widthINT.toString() );
                asset.close();
            }
            else if ( isSWF )
            {
                SWFImageSizeDeterminer aImageDeterminer = new SWFImageSizeDeterminer( filePath );
               aImageDeterminer.checkSize();
                lml.setAttribute( "height", String.valueOf( aImageDeterminer.getHeight() ) );
                lml.setAttribute( "width", String.valueOf( aImageDeterminer.getWidth() ) );                   
            }
        }  
    }
    
    public void handleParaAttributes( Element para, Element lml ) throws Exception
    {
        String align = para.getAttributeValue( "ALIGN" );
        if ( align != null )
        {
            if ( align.equals( "CENTER" ))
                lml.setAttribute( "halign", "center" );
            else if ( align.equals( "RIGHT" ))
                lml.setAttribute( "halign", "right" );
            else
                lml.setAttribute( "halign", "left" );
        }
    }
    

	public String getImageId( String srcImage )
    {
		String id = srcImage;
		int pos = srcImage.lastIndexOf( "/");
		id = id.substring( pos + 1, id.length() - 4 );
		return id;
	}
    
    public String getImagePath( String srcImage )
    {
        return srcImage;
        /**if ( developement )
        {
            int pos = srcImage.lastIndexOf( '/' );
            if ( pos  < 0 )
                pos = srcImage.lastIndexOf( '\\' );
            if ( pos >= 0 )
            {
                String filePath = srcImage.substring( pos + 1 );
                return "./images/" + filePath;
            }
            else
                return srcImage;
        }
        else
        {
            String filePath = teamSite + srcImage.replaceFirst( "iwts:", "/iwmnt/default/main/MultiBranch/WORKAREA/common");
            return filePath;
        }*/
    }
    
    public void removeStimulusDirection() throws Exception
    {
        Element stimulusDir = extractSingleElement( ".//StimulusDirections", itemElement );
        if ( stimulusDir != null )
            stimulusDir.detach();
    }
    
    public void handleStimulusDirection() throws Exception
    {
        if ( studentDirection == null )
        {
            Element Stimulus = extractSingleElement( ".//StimulusDirections", itemElement );
            if ( Stimulus != null )
                extractStudentDirection( Stimulus );
        }
        removeStimulusDirection();
        Element Stimulus = extractSingleElement( ".//Stimulus", itemElement );
        if ( Stimulus != null )
        {
            if ( Stimulus.getChildren().size() == 0 )
            {
                if ( useStimulusDisplayID )
                {
                    String newID = Stimulus.getAttributeValue( "DisplayID" );
                    if ( newID == null )
                        newID = getAttributeValue( Stimulus, "ID" );
                    studentDirection.setAttribute( "id", newID );
                }
                else
                    studentDirection.setAttribute( "id", getAttributeValue( Stimulus, "ID" ) );
                studentDirection.setAttribute( "stereotype", "stimulus" );
                Stimulus.detach();
            }
        }
    }
    public boolean processingStimulus = false;
    public boolean tabStimulus = false;
    public boolean hasPassage = false;
    public boolean stimulusContainsOnlyPassage = false;
    
    public void extractStimulus() throws Exception
    {
        Element Stimulus = extractSingleElement( ".//Stimulus", itemElement );
        if ( Stimulus != null )
        {
            List StimuluschildList = Stimulus.getChildren();
            Element firstChild = ( Element )StimuluschildList.get( 0 );
            stimulusContainsOnlyPassage = StimuluschildList.size() == 1 && firstChild.getName().equals( "Passage" );
            if ( "tab".equals( Stimulus.getAttributeValue( "widgetlayout" )) )
            {    
                stimulus = new Element( "stimulus_tabs_panel" );
                stimulus.setAttribute( "selectedTabNumber", "1" );
                tabStimulus = true;
            }
            else if ( stimulusContainsOnlyPassage )
                stimulus = new Element( "scrolling_text_panel" );
            else
                stimulus = new Element( "panel" );
            stimulus.setAttribute( "left_padding", String.valueOf( defaultSpacing ) );
            extractPanelAlt( Stimulus, stimulus );
            stimulus.setAttribute( "stereotype", "stimulus" );
            if ( useStimulusDisplayID )
            {
                String newID = Stimulus.getAttributeValue( "DisplayID" );
                if ( newID == null )
                    newID = getAttributeValue( Stimulus, "ID" );
                stimulus.setAttribute( "id", newID );
            }
            else
                stimulus.setAttribute( "id", getAttributeValue( Stimulus, "ID" ) );
            setAllDefaultAttributes( stimulus, Stimulus );
            processingStimulus = true;
            List childList = extractBlocktext( Stimulus );
            for ( int i = 0; i < childList.size(); i++ )
                stimulus.addContent( ( Element )childList.get( i ));
            String location = Stimulus.getAttributeValue( "location" );
            if ( "across".equals( location ))
            {
                String x = stimulus.getAttributeValue( "x" );
                if ( x == null )
                {
                    stimulus.setAttribute( "x", String.valueOf( 0 ) );
                    x = String.valueOf( 0 );
                }
                String width = stimulus.getAttributeValue( "width" );
                if ( width == null )
                 	stimulus.setAttribute( "width", String.valueOf( m_MaxPanelWidth ) );
            }
            else if ( "left".equals( location ))
            {
                String x = stimulus.getAttributeValue( "x" );
                if ( x == null )
                {
                    stimulus.setAttribute( "x", String.valueOf( 0 ) );
                    x = String.valueOf( 0 );
                }
                String widthS = stimulus.getAttributeValue( "width" );
                if ( widthS == null )
                {
	                int width = LAYOUT_MAX_WIDTH - 0 - 0;
	                width = width / 2;
	                width = getMaxWidth( stimulus, width );
	                stimulus.setAttribute( "width", String.valueOf( width ) );
                }
                stimulusLeftLocated = true;
            }
            else // right_column
            {
                String widthS = stimulus.getAttributeValue( "width" );
                if ( widthS == null )
                {
	                int width = LAYOUT_MAX_WIDTH - 0 - 0;
	                int x = width;
	                width = width / 2;
	                width = getMaxWidth( stimulus, width );
	                stimulus.setAttribute( "width", String.valueOf( width ) );
                }
                String x = stimulus.getAttributeValue( "x" );
                if ( x == null )
                {
                    widthS = stimulus.getAttributeValue( "width" );
	                stimulus.setAttribute( "x", String.valueOf( LAYOUT_MAX_WIDTH - rightIndent - Integer.valueOf( widthS ).intValue() ) );
                }
            }   
            processingStimulus = false;
        }
    }
    
    public void extractStem()throws Exception
    {
        Element Stem = extractSingleElement( ".//Stem", itemElement );
        if ( Stem != null )
        {
            stem = new Element( "panel" );
            stem.setAttribute( "left_padding", String.valueOf( defaultSpacing ) );
            extractPanelAlt( Stem, stem );
            stem.setAttribute( "stereotype", "stem" );
            stem.setAttribute( "id", "stem1" );
            setAllDefaultAttributes( stem, Stem );
            if ( stem.getAttributeValue( "layout" ) == null )
                stem.setAttribute( "layout", "vertical" );
            List childList = extractBlocktext( Stem );
            for ( int i = 0; i < childList.size(); i++ )
                stem.addContent( ( Element )childList.get( i ));
            String location = Stem.getAttributeValue( "location" );
            if ( "across".equals( location ))
            {
                String x = stem.getAttributeValue( "x" );
                if ( x == null )
                {
                    stem.setAttribute( "x", String.valueOf( 0 ) );
                    x = String.valueOf( 0 );
                }
                String width = stem.getAttributeValue( "width" );
                if ( width == null )
                    stem.setAttribute( "width", String.valueOf( m_MaxPanelWidth ) );
            }
            else if ( "left".equals( location ))
            {
                String x = stem.getAttributeValue( "x" );
                if ( x == null )
                {
                    stem.setAttribute( "x", String.valueOf( 0 ) );
                    x = String.valueOf( 0 );
                }
                String widthS = stem.getAttributeValue( "width" );
                if ( widthS == null )
                {
	                if ( stimulus != null )
	                {
	                    String stimulusX = stimulus.getAttributeValue( "x" );
	                    int width = Integer.valueOf( stimulusX ).intValue() - Integer.valueOf( x ).intValue() - 0;
	                    stem.setAttribute( "width", String.valueOf( width ) );
	                }
	                else
	                {
	                    int width = LAYOUT_MAX_WIDTH;
	                    width = width / 2;
	                    width = getMaxWidth( stem, width );
	                    stem.setAttribute( "width", String.valueOf( width ) );
	                }     
                }
            }
            else // right_column
            {
                String widthS = stem.getAttributeValue( "width" );
                if ( widthS == null )
                {
                    if ( stimulus != null )
                    {
                        String stimulusX = stimulus.getAttributeValue( "x" );
                        String stimulusWidth = stimulus.getAttributeValue( "width" );
                        int width = LAYOUT_MAX_WIDTH - ( Integer.valueOf( stimulusX ).intValue() + Integer.valueOf( stimulusWidth ).intValue() ) - rightIndent;
                        stem.setAttribute( "width", String.valueOf( width ) );
                    }
                    else
                    {
		                int width = LAYOUT_MAX_WIDTH;
		                int x = width;
		                width = width / 2;
		                width = getMaxWidth( stem, width );
		                stem.setAttribute( "width", String.valueOf( width ) );
                    }
                }
                String x = stem.getAttributeValue( "x" );
                if ( x == null )
                {
                    widthS = stem.getAttributeValue( "width" );
                    stem.setAttribute( "x", String.valueOf( LAYOUT_MAX_WIDTH - Integer.valueOf( widthS ).intValue() - rightIndent ) );
                }
                stemRightLocated = true;
            }   
        }
    }
    
    public int getMaxWidth( Element stimulus, int width )
    {
        List content = stimulus.getContent();
        int newWidth = width;
        for ( int i = 0; i < content.size(); i++ )
        {
            Element widget = ( Element )content.get( i );
            if ( !( widget.getName().equals( text_widget ) || widget.getName().equals( "text" ) ))
            {
                String thiswidth = widget.getAttributeValue( "width" );
                if ( thiswidth != null )
                {
                    int widgetWidth = Integer.valueOf( thiswidth ).intValue() + scrollbarWidth + defaultSpacing;
                    if ( widgetWidth > newWidth )
                        newWidth = widgetWidth;
                }
            }
        }
        return newWidth;
    }
    
    public void extractPanelAlt( Element src, Element dest )throws Exception
    {
        String alt = getAttributeValue( src, "Alt" );
        if ( alt != null )
        {
            Element alt_text = new Element( "alt_text" );
            alt_text.setText( alt );
            dest.addContent( alt_text );
        }
    }
    
    public void extractStudentDirection( Element element )throws Exception
    {
        Element studDir = null;
        if ( element == null )
            studDir = extractSingleElement( ".//StudentDirections", itemElement );
        else
            studDir = element;
        if ( studDir != null )
        {
            studentDirection = new Element( "panel" );
            studentDirection.setAttribute( "stereotype", "directions" );
            studentDirection.setAttribute( "id", "sd1" );
            studentDirection.setAttribute( "left_padding", String.valueOf( defaultSpacing ) );
            extractPanelAlt( studDir, studentDirection);
            setAllDefaultAttributes( studentDirection, studDir );
            List childList = extractBlocktext( studDir );
            for ( int i = 0; i < childList.size(); i++ )
                studentDirection.addContent( ( Element )childList.get( i ));
        }
    }
    
    public void replaceGridDisplayValue( Element gridcol ) throws Exception
    {
        List entries = gridcol.getChildren( "entry" );
        for ( int i = 0; i < entries.size(); i++ )
        {
            Element entry = ( Element) entries.get( i );
            String display = entry.getAttributeValue( "display" );
            if ( display != null && display.trim().length() > 0 )
            {
                display = replaceUnicode( display );
                entry.setAttribute( "display", display );
            }
            String value = entry.getAttributeValue( "value" );
            if (value == null)
            {
                entry.setAttribute( "value", "");
            }
            else if ( value.trim().length() > 0 )
            {
                value = replaceUnicode( value );
                entry.setAttribute( "value", value );
            }
        }
    }
    
    public void extractCRArea( boolean isFakeCR, boolean isAudioItem )throws Exception    // For Audio Item - Las Links
    {
        Element constructedResponse = extractSingleElement( ".//ConstructedResponse", itemElement );
        if (constructedResponse == null)
            return;
        String originalWidth = constructedResponse.getAttributeValue( "width" );
        Element answerArea = extractSingleElement( ".//AnswerArea", constructedResponse );
        if (answerArea == null)
            return;
        
        if ( !isFakeCR && !isAudioItem && answerArea.getAttributeValue( "OnlineVisible").equals( "no" ))  // For Audio Item - Las Links
            return;
        int numberOfGridCol = 0;
        if ( isFakeCR )
        {
            answer = new Element( "gridded_response_panel" );
            answer.setAttribute( "top_padding", "5" );
            answer.setAttribute( "left_padding", "0" );
            answer.setAttribute( "stereotype", "answerArea" );
            Element gridresponse = new Element( "gridresponse" );
            answer.addContent( gridresponse );
            List gridcols = extractAllElement( ".//gridcol", itemElement );
            XMLOutputter outputter = new XMLOutputter();
            org.jdom.input.SAXBuilder saxBuilder = new org.jdom.input.SAXBuilder();
            numberOfGridCol = gridcols.size();
            for ( int i = 0; i < gridcols.size(); i++ )
            {
                Element gridcol = ( Element ) gridcols.get( i );
                String gridcolString = outputter.outputString( gridcol );
                ByteArrayInputStream bais = new ByteArrayInputStream( gridcolString.getBytes());
                org.jdom.Document gridcolDoc = saxBuilder.build( bais );
                Element newElement = gridcolDoc.getRootElement();
                newElement.setAttribute( "identifier", (new Integer( i + 1 )).toString() );
                newElement.detach();
                replaceGridDisplayValue( newElement );
                gridresponse.addContent( newElement );
            }
        } else if (isAudioItem){     //  For Audio Item - Las Links: Extracts all attributes from AudioItem and creats new element for recorder_widget  
        	 answer = new Element( "panel" );
             answer.setAttribute( "stereotype", "answerArea" );
             Element recordedResponse = extractSingleElement( ".//AudioItem", itemElement );
             if (recordedResponse == null)
             	return;
             Element recorder_wid = new Element( "recorder_widget" );
             setAllDefaultAttributes( recorder_wid, recordedResponse );
             recorder_wid.setAttribute( "totalTime", recordedResponse.getAttributeValue("totalTime") );
             recorder_wid.setAttribute( "pausable", recordedResponse.getAttributeValue("pausable","false" )  );
             recorder_wid.setAttribute( "playable", recordedResponse.getAttributeValue("playable","false" )  );
             recorder_wid.setAttribute( "timer", recordedResponse.getAttributeValue("timer","true" )  );
             recorder_wid.setAttribute( "progressBar", recordedResponse.getAttributeValue("progressBar","true" )  );
             recorder_wid.setAttribute( "showActivityLevel", recordedResponse.getAttributeValue("showActivityLevel","true" )  );
             answer.addContent(recorder_wid);
             
        }
        else
        {
            answer = new Element( "answer_area_panel" );
            answer.setAttribute( "stereotype", "answerArea" );
        }
        answer.setAttribute( "id", "A1" );
        setAllDefaultAttributes( answer, constructedResponse );
        if ( isFakeCR || isAudioItem)     //  For Audio Item - Las Links : For GR and Audio Item
            answer.setAttribute( "spacing", "0" );
        String location = constructedResponse.getAttributeValue( "location" );
        if ( "across".equals( location ))
        {
            if ( answer.getAttributeValue( "width" ) == null )
                answer.setAttribute( "width", String.valueOf( m_MaxPanelWidth - defaultSpacing ) );
            if ( answer.getAttributeValue( "x" ) == null )
                answer.setAttribute( "x", String.valueOf( defaultSpacing ) ); 
        }
        else if ( "left".equals( location ))
        {
            if ( answer.getAttributeValue( "x" ) == null )
                answer.setAttribute( "x", String.valueOf( defaultSpacing ) );
            int answerWidth = 0;
            String stemWidth = null;
            String stemX = null;
            if ( stem == null )
            {
                stemWidth = stimulus.getAttributeValue( "width" );
                stemX = stimulus.getAttributeValue( "x" );
            }
            else
            {
                stemWidth = stem.getAttributeValue( "width" );
                stemX = stem.getAttributeValue( "x" );
            }            
            if ( stemX.equals( String.valueOf( 0 )))
                answerWidth = Integer.valueOf( stemWidth ).intValue();
            else 
                answerWidth = Integer.valueOf( stemX ).intValue();
            if ( answer.getAttributeValue( "width" ) == null )
                answer.setAttribute( "width", String.valueOf( answerWidth ) );
        }
        else // right_column
        {
            String stemWidth = null;
            String stemX = null;
            if ( stem == null )
            {
                stemWidth = stimulus.getAttributeValue( "width" );
                if ( Integer.valueOf( stemWidth ).intValue() == LAYOUT_MAX_WIDTH )
                    stemWidth = String.valueOf( LAYOUT_MAX_WIDTH - defaultSpacing );
                stemX = stimulus.getAttributeValue( "x" );
            }
            else
            {
                stemWidth = stem.getAttributeValue( "width" );
                stemX = stem.getAttributeValue( "x" );
                if ( Integer.valueOf( stemX ).intValue() + Integer.valueOf( stemWidth ).intValue() >= LAYOUT_MAX_WIDTH )
                    stemWidth = String.valueOf( Integer.valueOf( stemWidth ).intValue() - defaultSpacing );
            }            
            int answerWidth;
            int startX;
            if ( stemX.equals( String.valueOf( 0 )))
            {
                startX = Integer.valueOf( stemX ).intValue() + Integer.valueOf( stemWidth ).intValue();
                answerWidth = LAYOUT_MAX_WIDTH - Integer.valueOf( stemWidth ).intValue() - defaultSpacing;
            }
            else
            {
                startX = Integer.valueOf( stemX ).intValue();
                answerWidth = Integer.valueOf( stemWidth ).intValue();
                if ( startX + answerWidth >= LAYOUT_MAX_WIDTH )
                    answerWidth -= defaultSpacing;
            }
            if ( answer.getAttributeValue( "width" ) == null )
                answer.setAttribute( "width", String.valueOf( answerWidth ) );
            if ( answer.getAttributeValue( "x" ) == null )
            {
                if ( isFakeCR )
                    answer.setAttribute( "x", String.valueOf( startX + defaultSpacing ) );
                else
                    answer.setAttribute( "x", String.valueOf( startX ) );
            }
            answerChoiceRightLocated = true;
        }   
        if ( numberOfGridCol != 0 && originalWidth == null )
            alignGridHorizontalToMiddle( numberOfGridCol );
    }
    
    public void alignGridHorizontalToMiddle( int numberOfGridCol )
    {
        int gridsWidth = numberOfGridCol * 28 + 120;
        int x = Integer.valueOf( answer.getAttributeValue( "x" ) ).intValue();
        if ( ( x + gridsWidth ) > LAYOUT_MAX_WIDTH )
            gridsWidth = LAYOUT_MAX_WIDTH - x;
        answer.setAttribute( "width", String.valueOf( gridsWidth ) );
/*       int width = Integer.valueOf( answer.getAttributeValue( "width" ) ).intValue();
        int x = Integer.valueOf( answer.getAttributeValue( "x" ) ).intValue();
        int gridsWidth = numberOfGridCol * 28;
        if ( width > gridsWidth )
        {
            int left_padding = ( width - gridsWidth ) / 2;
            answer.setAttribute( "left_padding", String.valueOf( left_padding ) );
        } 
        else
            answer.removeAttribute( "left_padding" );
*/
    }
    
    public void extractSRChoice()throws Exception
    {
        Element SelectedResponse = extractSingleElement( ".//SelectedResponse", itemElement );
        String stack = SelectedResponse.getAttributeValue( "stack" );
        if ( !"na".equals( stack ))
            answer = new Element( "answer_choices_absolute_positioned_panel" );
        else
        {
            answer = new Element( "answer_area_panel" );
            answer.setAttribute( "left_padding", String.valueOf(defaultSpacing) );
        }
        extractPanelAlt( SelectedResponse, answer );
        answer.setAttribute( "id", "A1" );
        Element Stem = extractSingleElement( ".//Stem", itemElement );
        answer.setAttribute( "stereotype", "answerArea" );
        setAllDefaultAttributes( answer, SelectedResponse );
        String layout = SelectedResponse.getAttributeValue( "widgetlayout" );
        if ( !( layout.equals( "vertical" ) || layout.equals( "horizontal" ) ))
            answer.setAttribute( "layout", "vertical" );
        String location = SelectedResponse.getAttributeValue( "location" );
        String spacing = SelectedResponse.getAttributeValue( "widgetspacing" );
        int spacingInt = defaultSpacing;
        if ( spacing != null )
            spacingInt = Integer.valueOf( spacing ).intValue();
        else
            answer.setAttribute( "spacing", String.valueOf( defaultSpacing ) );
        if ( "across".equals( location ))
        {
            if ( answer.getAttributeValue( "width" ) == null )
                answer.setAttribute( "width", String.valueOf( m_MaxPanelWidth - defaultSpacing ) );
            if ( answer.getAttributeValue( "x" ) == null )
                answer.setAttribute( "x", String.valueOf( defaultSpacing ) ); 
        }
        else if ( "left".equals( location ))
        {
            if ( answer.getAttributeValue( "x" ) == null )
                answer.setAttribute( "x", String.valueOf( defaultSpacing ) );
            int answerWidth = 0;
            String stemWidth = null;
            String stemX = null;
            if ( stem == null )
            {
                stemWidth = stimulus.getAttributeValue( "width" );
                stemX = stimulus.getAttributeValue( "x" );
            }
            else
            {
                stemWidth = stem.getAttributeValue( "width" );
                stemX = stem.getAttributeValue( "x" );
            }            
            if ( stemX.equals( String.valueOf( 0 )))
                answerWidth = Integer.valueOf( stemWidth ).intValue();
            else 
                answerWidth = Integer.valueOf( stemX ).intValue();
            if ( answer.getAttributeValue( "width" ) == null )
                answer.setAttribute( "width", String.valueOf( answerWidth ) );
        }
        else // right_column
        {
            String stemWidth = null;
            String stemX = null;
            if ( stem == null )
            {
                stemWidth = stimulus.getAttributeValue( "width" );
//                if ( Integer.valueOf( stemWidth ).intValue() == ( LAYOUT_MAX_WIDTH - rightIndent ) )
                if ( Integer.valueOf( stemWidth ).intValue() > 600 )
                    stemWidth = String.valueOf( LAYOUT_MAX_WIDTH - rightIndent );
                stemX = stimulus.getAttributeValue( "x" );
            }
            else
            {
                stemWidth = stem.getAttributeValue( "width" );
                stemX = stem.getAttributeValue( "x" );
                if ( Integer.valueOf( stemX ).intValue() + Integer.valueOf( stemWidth ).intValue() >= LAYOUT_MAX_WIDTH )
                    stemWidth = String.valueOf( Integer.valueOf( stemWidth ).intValue() - defaultSpacing );
            }            
            int answerWidth;
            int startX;
            if ( stemX.equals( String.valueOf( 0 )))
            {
                startX = Integer.valueOf( stemX ).intValue() + Integer.valueOf( stemWidth ).intValue() + defaultSpacing;
                answerWidth = LAYOUT_MAX_WIDTH - Integer.valueOf( stemWidth ).intValue() - rightIndent;
            }
            else
            {
                startX = Integer.valueOf( stemX ).intValue();
                startX += defaultSpacing;
                answerWidth = Integer.valueOf( stemWidth ).intValue();
                if ( startX + answerWidth >= ( LAYOUT_MAX_WIDTH - rightIndent ) )
                    answerWidth = LAYOUT_MAX_WIDTH - rightIndent - startX;
            }
            if ( answer.getAttributeValue( "width" ) == null )
                answer.setAttribute( "width", String.valueOf( answerWidth ) );
            if ( answer.getAttributeValue( "x" ) == null )
                answer.setAttribute( "x", String.valueOf( startX ) );
            answerChoiceRightLocated = true;
        }   
        boolean containImage = extractAllElement( ".//Graphic", itemElement ).size() > 0;
        layout = answer.getAttributeValue( "layout" );		
        ArrayList choices = new ArrayList();
        String haligh = SelectedResponse.getAttributeValue( "halign" );
        List selectedResponses = extractAllElement( ".//AnswerChoice", itemElement );
        for ( int i = 0; i < selectedResponses.size(); i++ )
        {
            Element choice = ( Element )selectedResponses.get( i );
            List childList = extractBlocktext( choice );
            setTextWidgetAttribute( childList, "halign", haligh );
            choices.add( childList );
        }
        ChoiceLayoutMaster aGuessor 
        					= new ChoiceLayoutMaster( SelectedResponse, answer, containImage, stack, layout, choices, spacingInt );
        aGuessor.doIt();
    }
    
    public Element getDummy()throws Exception
    {
        String dummyStr = "<element_package iid=\"ok_demo01.v01\" security_classification_id=\"alg112\"></element_package>";
        ByteArrayInputStream bais = new ByteArrayInputStream(dummyStr.getBytes());
        org.jdom.input.SAXBuilder saxBuilder = new org.jdom.input.SAXBuilder();
        org.jdom.Document itemDoc = saxBuilder.build( bais );
        Element dummy = itemDoc.getRootElement();
        return dummy;
    }

    public static void createManipulateMapping()
    {
        if ( manipulateMapping == null )
        {
            manipulateMapping = new HashMap();
            manipulateMapping.put( "straight_edge", "straight_edge" );
            manipulateMapping.put( "oneeighth_inch_ruler", "oneeighth_inch_ruler" );
            manipulateMapping.put( "cm_ruler", "cm_ruler" );
            manipulateMapping.put( "mm_ruler", "mm_ruler" );
            manipulateMapping.put( "half_inch_ruler", "half_inch_ruler" );
            manipulateMapping.put( "protractor", "protractor" );
            manipulateMapping.put( "standard_calculator", "standard_calculator" );
//          manipulateMapping.put( "formula_card", "formula_card" );       
            manipulateMapping.put( "TN_formula_card", "TN_formula_card" );       
            manipulateMapping.put( "FCAT_formula_card", "FCAT_formula_card" );       
            manipulateMapping.put( "scientific_calculator", "scientific_calculator" );
            manipulateMapping.put( "scratchpad", "scratchpad" );
            manipulateMapping.put( "required", "required" );
            manipulateMapping.put( "accommodated", "accommodated" );
            manipulateMapping.put( "masking_ruler", "masking_ruler" );
            manipulateMapping.put("ti_scientific_calculator", "ti_scientific_calculator");
            manipulateMapping.put("ti_scientific_graphic_calculator","ti_scientific_graphic_calculator");
        }
        if ( allManipulateMapping == null )
        {
            allManipulateMapping = new HashMap();
            allManipulateMapping.put( "oneeighth_inch_ruler", "not provisioned" );
            allManipulateMapping.put( "cm_ruler", "not provisioned" );
            allManipulateMapping.put( "mm_ruler", "not provisioned" );
            allManipulateMapping.put( "half_inch_ruler", "not provisioned" );
            allManipulateMapping.put( "protractor", "not provisioned" );
            allManipulateMapping.put( "standard_calculator", "not provisioned" );
//          allManipulateMapping.put( "formula_card", "not provisioned" );       
            allManipulateMapping.put( "TN_formula_card", "not provisioned" );       
            allManipulateMapping.put( "FCAT_formula_card", "not provisioned" );       
            allManipulateMapping.put( "scientific_calculator", "not provisioned" );
            allManipulateMapping.put( "option_eliminator", "required" );
            allManipulateMapping.put( "eraser", "required" );
            allManipulateMapping.put( "highlighter", "required" );
            allManipulateMapping.put( "straight_edge", "not provisioned" );
            allManipulateMapping.put( "scratchpad", "not provisioned" );
            allManipulateMapping.put( "masking_ruler", "required" );
            allManipulateMapping.put("ti_scientific_calculator", "not provisioned");
            allManipulateMapping.put("ti_scientific_graphic_calculator", "not provisioned");
        }
    }
    
    public void extractAccommodation( boolean isFakeCR ) throws Exception
    {
        List manipulatives = extractAllElement( ".//Manipulative", itemElement );
        accommodation = new Element( "toolbar" );
        HashMap accommodationMap = new HashMap();
        if ( manipulatives != null && manipulatives.size() > 0 )
        {
            for ( int i = 0; i < manipulatives.size(); i++ )
            {
                Element thisElement = ( Element )manipulatives.get( i );
                String thisOne = thisElement.getAttributeValue( "Type" );
                String attributeName = ( String )manipulateMapping.get( thisOne );
                String thisValue = thisElement.getAttributeValue( "Provision" );
                String attributeValue = ( String )manipulateMapping.get( thisValue );
                if ( attributeName != null && attributeValue != null )
                    accommodationMap.put( attributeName, attributeValue );
            }
        }
        Set set = allManipulateMapping.keySet();
        Iterator it = set.iterator();
        while ( it.hasNext() )
        {
            String key = ( String )it.next();
            String value = ( String )accommodationMap.get( key );
            if ( value != null )
                accommodation.setAttribute( key, value );
            else
                accommodation.setAttribute( key, ( String )allManipulateMapping.get( key ) );
        }
        
        doFormulaCard(accommodation);
        
        Element item_model = new Element( "item_model" );
        item_model.setAttribute( "iid", getAttributeValue( itemElement, "ID" ) );
        item_model.setAttribute( "eid", String.valueOf( order ) );
        item_model.setAttribute( "marked", "0" );
        item_model.setAttribute( "answered", "0" );
        item_model.setAttribute( "number", "0" );
        
        if ( accommodation != null )
            item_model.addContent( accommodation );
        if ( itemElement.getAttributeValue( "ItemType" ).equals( "SR" ) ||
                itemElement.getAttributeValue( "ItemType" ).equals( "RQ" ))
        {
            populateAnswerInfo( item_model );
        }
        else if ( itemElement.getAttributeValue( "ItemType" ).equals( "NI" )){  //FOR NI ITEM : TO ADD RESPONSE AREA
        	if(item_model!=null && extractAllElement( ".//AnswerChoice", itemElement ).size()>0 ){
        		populateAnswerInfo( item_model );
        	} else if(item_model!=null && extractAllElement( ".//gridcol", itemElement ).size()>0){
        		populateAnswerAreaInfo( item_model );
        	}
        }
        else if ( isFakeCR )
            populateAnswerAreaInfo( item_model );
        accommodation = item_model;
    }
    
    public void populateAnswerAreaInfo( Element item_model ) throws Exception
    {        
        List gridcols = extractAllElement( ".//gridcol", itemElement );
        item_model.setAttribute( "type", "question" );
        Element interaction = new Element( "interaction" );
        interaction.setAttribute( "type", "grid" );
        for ( int i = 0; i < gridcols.size(); i++ )
        {
            Element destGrid = new Element( "response" );
            destGrid.setAttribute( "identifier", String.valueOf( i + 1 ) );
            destGrid.setAttribute( "user_action", " " );
            interaction.addContent( destGrid );
        }     
        item_model.addContent( interaction );
    }

    public void populateAnswerInfo( Element item_model ) throws Exception
    {
        List selectedResponses = extractAllElement( ".//AnswerChoice", itemElement );
        item_model.setAttribute( "type", "question" );
//        item_model.setAttribute( "eid", itemElement.getAttributeValue( "ID" ) );
//        item_model.setAttribute( "iid", itemElement.getAttributeValue( "ID" ) );
        Element interaction = new Element( "interaction" );
        interaction.setAttribute( "type", "choice" );
        interaction.setAttribute( "max_choices", "1" );
        for ( int i = 0; i < selectedResponses.size(); i++ )
        {
            Element srcChoice = ( Element )selectedResponses.get( i );
            Element destChoice = new Element( "selector" );
            String key = getChoiceKey( i );
            destChoice.setAttribute( "identifier", key );
            destChoice.setAttribute( "obj_id_ref", key );
            destChoice.setAttribute( "user_action", "n" );
            if ( srcChoice.getAttributeValue( "Type" ).equals( "Correct" ))
                item_model.setAttribute( "correct", key );
            interaction.addContent( destChoice );
        }     
        item_model.addContent( interaction );
    }
    
    public String getChoiceKey( int i )
    {
        switch ( i )
        {
        	case 0: return "A";
        	case 1: return "B";
        	case 2: return "C";
        	case 3: return "D";
        	case 4: return "E";
        	default: return "N";
        }
    }
    
    public static Element extractSingleElement( String pattern, Element element ) throws Exception
    {
        // TO-DO: this will only work with simple './/name' queries as is . . .
        pattern = pattern.substring(pattern.indexOf(".//") + 3);
        List children = element.getChildren();
        Iterator iterator = children.iterator();
        while(iterator.hasNext()) {
            Element elem = (Element) iterator.next();
            if(pattern.equals(elem.getName())) {
                return elem;
            } else {
                Element child = extractSingleElement(".//" + pattern, elem);
                if(child != null) return child;
            }
        }
        return null;
        
        // This doesn't work with current JDOM
        /** XPath assetXPath = XPath.newInstance( pattern );
        List elementList = assetXPath.selectNodes( element );
        if ( elementList != null && elementList.size() > 0 )
        {
            Element aElement = ( Element )elementList.get( 0 );
            return aElement;
        }
        else
            return null; */
    }
    
    public static List extractAllElement(String pattern, Element element ) throws Exception
    {
        // TO-DO: this will only work with simple './/name' queries as is . . .
        ArrayList results = new ArrayList();
        pattern = pattern.substring(pattern.indexOf(".//") + 3);
        List children = element.getChildren();
        Iterator iterator = children.iterator();
        while(iterator.hasNext()) {
            Element elem = (Element) iterator.next();
            if(pattern.equals(elem.getName())) {
                results.add(elem);
            }
            results.addAll(extractAllElement(".//" + pattern, elem));
        }
        return results;
        
        // This doesn't work with current JDOM
        /** XPath assetXPath = XPath.newInstance( pattern );
        List elementList = assetXPath.selectNodes( element );
        return elementList;*/
    }
    
    public String getAttributeValue( Element element, String key ) throws Exception
    {   // unicodeList
        String value = element.getAttributeValue( key );
        if ( value != null )
            value = replaceUnicode( value );  
        return value;
    }
    
    public static void getAsset( Element layoutItem, List assetList ) throws Exception
    {
        List localAssetList = extractAllElement( ".//" + image_widget, layoutItem );
        for ( int i = 0; i < localAssetList.size(); i++ ) 
        {
            Element image_widget = ( Element )localAssetList.get( i );
            String path = image_widget.getAttributeValue( "src" );
            assetList.add( path );
        }
        localAssetList = extractAllElement( ".//" + text_widget, layoutItem );
        for ( int i = 0; i < localAssetList.size(); i++ ) 
        {
        	SAXBuilder builder = new SAXBuilder();
        	Element text_widget = ( Element )localAssetList.get( i );
        	String textContent = text_widget.getContent(0).toString();
        	while(textContent.indexOf("image_widget") >= 0) {
        		String text = textContent.substring(textContent.indexOf("image_widget") - 1, textContent.indexOf("/>") + 2);
	        	Reader in = new StringReader(text);
	        	Document doc = builder.build(in);
				Element rootElement = doc.getRootElement();
				String path = rootElement.getAttributeValue( "src" );
				assetList.add( path );
				textContent = textContent.substring(textContent.indexOf("/>") + 2);
        	}
        }
        localAssetList = extractAllElement( ".//img", layoutItem );
        for ( int i = 0; i < localAssetList.size(); i++ ) 
        {
            Element image_widget = ( Element )localAssetList.get( i );
            String path = image_widget.getAttributeValue( "src" );
            assetList.add( path );
        }
    }
    
    public static void adjustJDOMDifference( Element itemLML ) throws Exception
    {
        List gridcolList = ItemLayoutProcessor.extractAllElement( ".//gridcol", itemLML );
        for ( int j = 0; j < gridcolList.size(); j++ )
        {
            Element gridcol = ( Element )gridcolList.get( j );
            List entryList = gridcol.getChildren( "entry" );
            for ( int i = 0; i < entryList.size(); i++ )
            {
                Element theEntry = ( Element )entryList.get( i );
                String value = theEntry.getAttributeValue( "value" );
                if ( value == null || value.trim().length() == 0 )
                {
                    theEntry.setAttribute( "value", "" );
                    theEntry.setAttribute( "display", "" );
                    break;
                }
            }
        }
    }

    public static void modifyItemLMLForADS_Puslishing( Element itemLML , String itemType) throws Exception
    {
        ItemLayoutProcessor.adjustJDOMDifference( itemLML );
    	itemLML.setName("item_delivery_package");
    	
    	List imageList = ItemLayoutProcessor.extractAllElement( ".//image_widget", itemLML );
    	for ( int i = 0; i < imageList.size(); i++ )
        {
    		Element image = (Element)imageList.get(i);
            String id = image.getAttributeValue( "id" );
            image.setAttribute( "src", id );
            image.removeAttribute( "id" );
        }
    	
    	imageList = extractAllElement( ".//" + text_widget, itemLML );
        for ( int i = 0; i < imageList.size(); i++ ) 
        {
        	SAXBuilder builder = new SAXBuilder();
        	Element text_widget = ( Element )imageList.get( i );
        	String textContent = text_widget.getContent(0).getValue();
        	if(textContent.indexOf("image_widget") >= 0) {
        		textContent = textContent.replaceAll("src=\"", "origpath=\"");
        		textContent = textContent.replaceAll("id=\"", "src=\"");
        		text_widget.removeContent(0);
        		text_widget.addContent(textContent);
        	}
        }
    	

        Element item_model = ItemLayoutProcessor.extractSingleElement( ".//item_model", itemLML );
        if ( item_model != null )
        {
            String correctAnswer = item_model.getAttributeValue( "correct" );
            Element toolbar = item_model.getChild( "toolbar" );
            boolean isSR = ItemLayoutProcessor.extractSingleElement( ".//selector_widget", itemLML ) != null;
            // For Audio Item - Las Links
            boolean isAudioItem = (ItemLayoutProcessor.extractSingleElement( ".//recorder_widget", itemLML ) != null);
            Element interaction = null;
            if ( isSR )
            {
                interaction = item_model.getChild( "interaction" );
                interaction.setName( "choiceInteraction" );
                Element choiceInteraction = interaction;
                choiceInteraction.removeAttribute( "type" );
                choiceInteraction.removeAttribute( "max_choices" );
                choiceInteraction.setAttribute( "responseIdentifier", "RESPONSE" );
                choiceInteraction.setAttribute( "maxChoices", "1" );
                List selects = interaction.getChildren();
                for ( int i = 0; i < selects.size(); i++ )
                {
                    Element select = ( Element )selects.get( i );
                    select.setName( "simpleChoice" );
                    select.removeAttribute( "user_action" );
                }
                interaction.detach();
            }
            List attributes = toolbar.getAttributes();
            for ( int i = 0; i < attributes.size(); i++ )
            {
                Attribute attribute = ( Attribute )attributes.get( i );
                item_model.setAttribute( attribute.getName(), attribute.getValue() );
            }
            toolbar.detach();
            if ( isSR )
            {
                item_model.removeAttribute( "type" );
                item_model.removeAttribute( "correct" );
            }
            item_model.removeAttribute( "iid" );
            item_model.removeAttribute( "eid" );
            item_model.removeAttribute( "number" );
            item_model.removeAttribute( "answered" );
            item_model.removeAttribute( "marked" );
            
            Element responseDeclaration = new Element( "responseDeclaration" );
            responseDeclaration.setAttribute( "identifier", "RESPONSE" );
            responseDeclaration.setAttribute( "cardinality", "single" );
            responseDeclaration.setAttribute( "baseType", "identifier" );
            Element correctResponse = new Element( "correctResponse" );
            Element value = new Element( "value" );
            if ( isSR )
                value.setText( correctAnswer );
            correctResponse.addContent( value );
            responseDeclaration.addContent( correctResponse );
            item_model.addContent( responseDeclaration );
            if ( (!isSR && item_model!=null) ) // FOR NI ITEM : TO ADD RESPONSE AREA
            {
                Element textEntryInteraction = new Element( "textEntryInteraction" );
                textEntryInteraction.setAttribute( "responseIdentifier", "RESPONSE" );
                textEntryInteraction.setAttribute( "obj_id_ref", "obj_con_response" );
                item_model.addContent( textEntryInteraction );
            }
            else if ( isSR ) // to avoid unnecessary comparison "else if" is introduced
            {
                Element outcomeDeclaration = new Element( "outcomeDeclaration" );
                outcomeDeclaration.setAttribute( "identifier", "SCORE" );
                outcomeDeclaration.setAttribute( "cardinality", "single" );
                outcomeDeclaration.setAttribute( "baseType", "float" );
                Element defaultValue = new Element( "defaultValue" );
                value = new Element( "value" );
                value.setText( "0" );
                defaultValue.addContent( value );
                outcomeDeclaration.addContent( defaultValue );
                item_model.addContent( outcomeDeclaration );
                item_model.addContent( interaction );
            }
            
            Element interactionElement = item_model.getChild( "interaction" );
            if ( interactionElement != null && "grid".equals( interactionElement.getAttributeValue( "type" )) )
            {
                item_model.removeChild( "textEntryInteraction" );
                interactionElement.detach();
                item_model.addContent( interactionElement );
            }
        }    
    }
    
    public void setIncludeAcknowledgement(boolean includeAcknowledgement)
    {
        this.includeAcknowledgement = includeAcknowledgement;
    }

    public boolean getIncludeAcknowledgement()
    {
        return this.includeAcknowledgement;
    }
    /*
     *  For Audio Item - Las Links
     *  Method is used to add attributes AllowRevisit and AllowRevisitOnRestart to Item.
     *  If attribute is not defined in Assessment XML, method will add default value  
     */
    private void updateNavigationalConstraint(Element layout) {
    	layout.setAttribute("allow_revisit", itemElement.getAttributeValue("AllowRevisit","true") );
    	layout.setAttribute("allow_revisit_on_restart", itemElement.getAttributeValue("AllowRevisitOnRestart","true") );
	}
}

