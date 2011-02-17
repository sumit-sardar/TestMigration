/*
 * Created on Nov 25, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ctb.content.layout;

import java.util.List;

import org.jdom.Element;

/**
 * @author Wen-Jin_Chang
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ChoiceLayoutMaster 
{
    boolean containImage;
    String stack;
    String layout;
    List selectedResponses;
    int spacing;
    Element answerRoot;
    Element src;
    public static int scrollbarWidth = 14;
    public static int defaultSpacing = 12;
    public static int radioButtonSize = 24;
    public static String choiceContainer = "answer_choice_widget";
    public static String selectorSpacing = "6";
    public static int padding = 12;
    public static String paddingString = String.valueOf( padding );

    public ChoiceLayoutMaster( Element SelectedResponse, Element answerRoot_, boolean containImage_, String stack_, String layout_, List selectedResponses_, int spacing_) 
    {
        super();
        answerRoot = answerRoot_;
        containImage = containImage_;
        stack = stack_;
        layout = layout_;
        selectedResponses = selectedResponses_;
        spacing = spacing_;
        src = SelectedResponse;
    }
    
    public void doIt() throws Exception
    {
        if ( stack.equals( "na" ))
        {
            if ( layout.equals( "vertical" ) )
                doStraightVertical();
            else
                doStraightHorizontal();
            answerRoot.setAttribute( "top_padding", paddingString ); 
            answerRoot.setAttribute( "left_padding", paddingString ); 
        }
        else if ( stack.equals( "Z" ))
        {
            int numberChoices = selectedResponses.size();
            switch ( numberChoices )
            {
	            case 3: 
	            case 4: doZ3Or4();
	                	break;
	            case 5: doZ5();
	                	break;
            }
            answerRoot.setAttribute( "layout", "absolute" ); 
            answerRoot.setAttribute( "spacing", "0" );
        }
        else // "N"
        {
            int numberChoices = selectedResponses.size();
            switch ( numberChoices )
			{
			case 3: 
			case 4: doN3Or4();
			    	break;
			case 5: doN5();
			    	break;
			}
            answerRoot.setAttribute( "layout", "absolute" );
            answerRoot.setAttribute( "spacing", "0" );
        }   
        doAllSelectorIds();
    }
    
    public void doAllSelectorIds() throws Exception
    {
        List selectors = ItemLayoutProcessor.extractAllElement( ".//selector_widget", answerRoot );
        char id = 'A';
        for ( int i = 0; i < selectors.size(); i++ )
        {
            Element element = ( Element )selectors.get( i );
            if ( element.getAttributeValue( "identifier" ) != null )
                break;
            element.setAttribute( "identifier",  String.valueOf( id ) );
            id++;
        }
    }
    
    public void doZ5()
    {
        boolean selectorFirst = true;
        String layout = "horizontal";
        String selectorLoaction = src.getAttributeValue( "SelectorPosition" );
        String selectorKey = "valign";
        if ( selectorLoaction.equals( "bottomcenter" ) )
        {
            selectorLoaction = "center";
            selectorFirst = false;
            layout = "vertical";
            selectorKey = "halign";
        }
        else if ( selectorLoaction.equals( "lefttop" ) )
        {
            selectorLoaction = "top";
        }
        else // left center
        {
            selectorLoaction = "middle";
        }
        
        int x = padding; 
        int y = padding;
        int maxHeight = radioButtonSize;
        String totalWidth = answerRoot.getAttributeValue( "width" );
        int oneField = ( Integer.valueOf( totalWidth ).intValue() - padding ) / 3;
        for ( int i = 0; i < 3; i++ )
        {
            Element answer_choice_widget = new Element( choiceContainer );
            answer_choice_widget.setAttribute( "layout", layout );
            answer_choice_widget.setAttribute( "spacing", selectorSpacing );
            List childList = ( List )selectedResponses.get( i );
            Element selector = new Element( "selector_widget" );
            selector.setAttribute( selectorKey, selectorLoaction );
            if (selectorFirst)
                answer_choice_widget.addContent( selector );
            for ( int j = 0; j < childList.size(); j++ )
            {
                Element child = ( Element )childList.get( j );
                answer_choice_widget.addContent( child );
                if (ItemLayoutProcessor.rationale_widget.equals( child.getName())) {
                    continue;
                }
                
                int heightInt = Integer.valueOf( child.getAttributeValue( "height" )).intValue();

                if ( selectorFirst )
                {
                    if ( heightInt > maxHeight )
                        maxHeight = heightInt;
                }
                else
                {
                    if ( ( heightInt + 6 + radioButtonSize ) > maxHeight )
                        maxHeight = heightInt + 6 + radioButtonSize;
                    child.setAttribute( "halign", "center" );
                    answer_choice_widget.setAttribute( "width", child.getAttributeValue( "width" ));
                }
            }
            if (!selectorFirst)
                answer_choice_widget.addContent( selector );
            answer_choice_widget.setAttribute( "x", String.valueOf( x ) );
            answer_choice_widget.setAttribute( "y", String.valueOf( y ) );
            answer_choice_widget.setAttribute( "width", String.valueOf( oneField ) );
            answer_choice_widget.setAttribute( "height", String.valueOf( maxHeight ) );
            answerRoot.addContent( answer_choice_widget );
            x += oneField;
        }
        x = padding;
        y += maxHeight + spacing;
        for ( int i = 3; i < selectedResponses.size(); i++ )
        {
            Element answer_choice_widget = new Element( choiceContainer );
            answer_choice_widget.setAttribute( "layout", layout );
            answer_choice_widget.setAttribute( "spacing", selectorSpacing );
            List childList = ( List )selectedResponses.get( i );
            Element selector = new Element( "selector_widget" );
            selector.setAttribute( selectorKey, selectorLoaction );
            if (selectorFirst)
                answer_choice_widget.addContent( selector );
            for ( int j = 0; j < childList.size(); j++ )
            {
                Element child = ( Element )childList.get( j );
                answer_choice_widget.addContent( child );
                if (ItemLayoutProcessor.rationale_widget.equals( child.getName())) {
                    continue;
                }
                
                int heightInt = Integer.valueOf( child.getAttributeValue( "height" )).intValue();
                if ( selectorFirst )
                {
                    if ( heightInt > maxHeight )
                        maxHeight = heightInt;
                }
                else
                {
                    if ( ( heightInt + 6 + radioButtonSize ) > maxHeight )
                        maxHeight = heightInt + 6 + radioButtonSize;
                    child.setAttribute( "halign", "center" );
                    answer_choice_widget.setAttribute( "width", child.getAttributeValue( "width" ));
                }
            }
            if (!selectorFirst)
                answer_choice_widget.addContent( selector );
            answer_choice_widget.setAttribute( "x", String.valueOf( x ) );
            answer_choice_widget.setAttribute( "y", String.valueOf( y ) );
            answer_choice_widget.setAttribute( "width", String.valueOf( oneField ) );
            answer_choice_widget.setAttribute( "height", String.valueOf( maxHeight ) );
            answerRoot.addContent( answer_choice_widget );
            x += oneField;
        }
        y += maxHeight + spacing;
        String totalHeight = answerRoot.getAttributeValue( "height" );
        if ( totalHeight == null )
            answerRoot.setAttribute( "height", String.valueOf( y ) );
    }
    
    public void doN5()
    {
        boolean selectorFirst = true;
        String layout = "horizontal";
        String selectorLoaction = src.getAttributeValue( "SelectorPosition" );
        String selectorKey = "valign";
        if ( selectorLoaction.equals( "bottomcenter" ) )
        {
            selectorLoaction = "center";
            selectorFirst = false;
            layout = "vertical";
            selectorKey = "halign";
        }
        else if ( selectorLoaction.equals( "lefttop" ) )
        {
            selectorLoaction = "top";
        }
        else // left center
        {
            selectorLoaction = "middle";
        }
        Element[] queue = new Element[ selectedResponses.size() ];
        int x = padding; 
        int y = padding;
        int maxHeight = radioButtonSize;
        String totalWidth = answerRoot.getAttributeValue( "width" );
        int oneField = ( Integer.valueOf( totalWidth ).intValue() - padding ) / 2;
        char id = 'A';
        for ( int i = 0; i < 5; i += 3 )
        {
            Element answer_choice_widget = new Element( choiceContainer );
            answer_choice_widget.setAttribute( "layout", layout );
            answer_choice_widget.setAttribute( "spacing", selectorSpacing );
            List childList = ( List )selectedResponses.get( i );
            Element selector = new Element( "selector_widget" );
            selector.setAttribute( selectorKey, selectorLoaction );
            if ( selectorFirst )
                answer_choice_widget.addContent( selector );
            for ( int j = 0; j < childList.size(); j++ )
            {
                Element child = ( Element )childList.get( j );
                answer_choice_widget.addContent( child );
                if (ItemLayoutProcessor.rationale_widget.equals( child.getName())) {
                    continue;
                }
                
                int heightInt = Integer.valueOf( child.getAttributeValue( "height" )).intValue();
                if ( selectorFirst )
                {
                    if ( heightInt > maxHeight )
                        maxHeight = heightInt;
                }
                else
                {
                    if ( ( heightInt + 6 + radioButtonSize ) > maxHeight )
                        maxHeight = heightInt + 6 + radioButtonSize;
                    child.setAttribute( "halign", "center" );
                }
            }
            if ( !selectorFirst )
                answer_choice_widget.addContent( selector );
            answer_choice_widget.setAttribute( "x", String.valueOf( x ) );
            answer_choice_widget.setAttribute( "y", String.valueOf( y ) );
            answer_choice_widget.setAttribute( "width", String.valueOf( oneField ) );
            answer_choice_widget.setAttribute( "height", String.valueOf( maxHeight ) );
            selector.setAttribute( "identifier",  String.valueOf( id ) );
            queue[ id - 'A' ] = answer_choice_widget;
            id += 3;
            // answerRoot.addContent( answer_choice_widget );
            x += oneField;
        }
        x = padding;
        y += maxHeight + spacing;
        if ( !selectorFirst )
            y += defaultSpacing;
        id = 'B';
        for ( int i = 1; i < selectedResponses.size(); i += 3 )
        {
            Element answer_choice_widget = new Element( choiceContainer );
            answer_choice_widget.setAttribute( "layout", layout );
            answer_choice_widget.setAttribute( "spacing", selectorSpacing );
            List childList = ( List )selectedResponses.get( i );
            Element selector = new Element( "selector_widget" );
            selector.setAttribute( selectorKey, selectorLoaction );
            if ( selectorFirst )
                answer_choice_widget.addContent( selector );
            for ( int j = 0; j < childList.size(); j++ )
            {
                Element child = ( Element )childList.get( j );
                answer_choice_widget.addContent( child );
                if (ItemLayoutProcessor.rationale_widget.equals( child.getName())) {
                    continue;
                }
                
                int heightInt = Integer.valueOf( child.getAttributeValue( "height" )).intValue();
                if ( selectorFirst )
                {
                    if ( heightInt > maxHeight )
                        maxHeight = heightInt;
                }
                else
                {
                    if ( ( heightInt + 6 + radioButtonSize ) > maxHeight )
                        maxHeight = heightInt + 6 + radioButtonSize;
                    child.setAttribute( "halign", "center" );
                }
            }
            if ( !selectorFirst )
                answer_choice_widget.addContent( selector );
            answer_choice_widget.setAttribute( "x", String.valueOf( x ) );
            answer_choice_widget.setAttribute( "y", String.valueOf( y ) );
            answer_choice_widget.setAttribute( "width", String.valueOf( oneField ) );
            answer_choice_widget.setAttribute( "height", String.valueOf( maxHeight ) );
            selector.setAttribute( "identifier",  String.valueOf( id ) );
            queue[ id - 'A' ] = answer_choice_widget;
            id += 3;
            // answerRoot.addContent( answer_choice_widget );
            x += oneField;
        }
        x = padding;
        y += maxHeight + spacing;
        if ( !selectorFirst )
            y += defaultSpacing;
        for ( int i = 2; i < selectedResponses.size(); i += 3 )
        {
            Element answer_choice_widget = new Element( choiceContainer );
            answer_choice_widget.setAttribute( "layout", layout );
            answer_choice_widget.setAttribute( "spacing", selectorSpacing );
            List childList = ( List )selectedResponses.get( i );
            Element selector = new Element( "selector_widget" );
            selector.setAttribute( selectorKey, selectorLoaction );
            if ( selectorFirst )
                answer_choice_widget.addContent( selector );
            for ( int j = 0; j < childList.size(); j++ )
            {
                Element child = ( Element )childList.get( j );
                answer_choice_widget.addContent( child );
                if (ItemLayoutProcessor.rationale_widget.equals( child.getName())) {
                    continue;
                }
                
                int heightInt = Integer.valueOf( child.getAttributeValue( "height" )).intValue();
                if ( selectorFirst )
                {
                    if ( heightInt > maxHeight )
                        maxHeight = heightInt;
                }
                else
                {
                    if ( ( heightInt + 6 + radioButtonSize ) > maxHeight )
                        maxHeight = heightInt + 6 + radioButtonSize;
                    child.setAttribute( "halign", "center" );
                }
            }
            if ( !selectorFirst )
                answer_choice_widget.addContent( selector );
            answer_choice_widget.setAttribute( "x", String.valueOf( x ) );
            answer_choice_widget.setAttribute( "y", String.valueOf( y ) );
            answer_choice_widget.setAttribute( "width", String.valueOf( oneField ) );
            answer_choice_widget.setAttribute( "height", String.valueOf( maxHeight ) );
            selector.setAttribute( "identifier",  "C" );
            queue[ 2 ] = answer_choice_widget;
            // answerRoot.addContent( answer_choice_widget );
            x += oneField;
        }
        y += maxHeight + spacing;
        String totalHeight = answerRoot.getAttributeValue( "height" );
        if ( totalHeight == null )
            answerRoot.setAttribute( "height", String.valueOf( y ) );
        for ( int i = 0; i < queue.length; i++ )
            answerRoot.addContent( queue[ i ] );
    }
    
    public void doN3Or4()
    {
        boolean selectorFirst = true;
        String layout = "horizontal";
        String selectorLoaction = src.getAttributeValue( "SelectorPosition" );
        String selectorKey = "valign";
        if ( selectorLoaction.equals( "bottomcenter" ) )
        {
            selectorLoaction = "center";
            selectorFirst = false;
            layout = "vertical";
            selectorKey = "halign";
        }
        else if ( selectorLoaction.equals( "lefttop" ) )
        {
            selectorLoaction = "top";
        }
        else // left center
        {
            selectorLoaction = "middle";
        }
        
        Element[] queue = new Element[ selectedResponses.size() ];
        int x = padding; 
        int y = padding;
        int maxHeight = radioButtonSize;
        String totalWidth = answerRoot.getAttributeValue( "width" );
        int oneField = ( Integer.valueOf( totalWidth ).intValue() - padding ) / 2;
        char id = 'A';
        for ( int i = 0; i < 4; i += 2 )
        {
            Element answer_choice_widget = new Element( choiceContainer );
            answer_choice_widget.setAttribute( "layout", layout );
            answer_choice_widget.setAttribute( "spacing", selectorSpacing );
            List childList = ( List )selectedResponses.get( i );
            Element selector = new Element( "selector_widget" );
            selector.setAttribute( selectorKey, selectorLoaction );
            if ( selectorFirst )
                answer_choice_widget.addContent( selector );
            for ( int j = 0; j < childList.size(); j++ )
            {
                Element child = ( Element )childList.get( j );
                answer_choice_widget.addContent( child );
                if (ItemLayoutProcessor.rationale_widget.equals( child.getName())) {
                    continue;
                }
                
                int heightInt = Integer.valueOf( child.getAttributeValue( "height" )).intValue();
                if ( selectorFirst )
                {
                    if ( heightInt > maxHeight )
                        maxHeight = heightInt;
                }
                else
                {
                    if ( ( heightInt + 6 + radioButtonSize ) > maxHeight )
                        maxHeight = heightInt + 6 + radioButtonSize;
                    child.setAttribute( "halign", "center" );
                    answer_choice_widget.setAttribute( "width", child.getAttributeValue( "width" ));
                }
                if ( child.getName().equals( "text_widget" ) )
                {
                    child.setAttribute( "valign", "middle" );
                    child.removeAttribute( "halign" );
                }
                
            }
            if ( !selectorFirst )
                answer_choice_widget.addContent( selector );
            answer_choice_widget.setAttribute( "x", String.valueOf( x ) );
            answer_choice_widget.setAttribute( "y", String.valueOf( y ) );
            if ( selectorFirst )
                answer_choice_widget.setAttribute( "width", String.valueOf( oneField ) );
            answer_choice_widget.setAttribute( "height", String.valueOf( maxHeight ) );
            selector.setAttribute( "identifier",  String.valueOf( id ) );
            queue[ id - 'A' ] = answer_choice_widget;
            id += 2;
            // answerRoot.addContent( answer_choice_widget );
            x += oneField;
        }
        x = padding;
        y += maxHeight + spacing;
        if ( !selectorFirst )
            y += defaultSpacing;
        id = 'B';
        for ( int i = 1; i < selectedResponses.size(); i += 2 )
        {
            Element answer_choice_widget = new Element( choiceContainer );
            answer_choice_widget.setAttribute( "layout", layout );
            answer_choice_widget.setAttribute( "spacing", selectorSpacing );
            List childList = ( List )selectedResponses.get( i );
            Element selector = new Element( "selector_widget" );
            selector.setAttribute( selectorKey, selectorLoaction );
            if ( selectorFirst )
                answer_choice_widget.addContent( selector );
            for ( int j = 0; j < childList.size(); j++ )
            {
                Element child = ( Element )childList.get( j );
                answer_choice_widget.addContent( child );
                if (ItemLayoutProcessor.rationale_widget.equals( child.getName())) {
                    continue;
                }
                
                int heightInt = Integer.valueOf( child.getAttributeValue( "height" )).intValue();
                if ( selectorFirst )
                {
                    if ( heightInt > maxHeight )
                        maxHeight = heightInt;
                }
                else
                {
                    if ( ( heightInt + 6 + radioButtonSize ) > maxHeight )
                        maxHeight = heightInt + 6 + radioButtonSize;
                    child.setAttribute( "halign", "center" );
                    answer_choice_widget.setAttribute( "width", child.getAttributeValue( "width" ));
                }
                if ( child.getName().equals( "text_widget" ) )
                {
                    child.setAttribute( "valign", "middle" );
                    child.removeAttribute( "halign" );
                }
            }
            if ( !selectorFirst )
                answer_choice_widget.addContent( selector );
            answer_choice_widget.setAttribute( "x", String.valueOf( x ) );
            answer_choice_widget.setAttribute( "y", String.valueOf( y ) );
            if ( selectorFirst )
                answer_choice_widget.setAttribute( "width", String.valueOf( oneField ) );
            answer_choice_widget.setAttribute( "height", String.valueOf( maxHeight ) );
            selector.setAttribute( "identifier",  String.valueOf( id ) );
            queue[ id - 'A' ] = answer_choice_widget;
            id += 2;
            // answerRoot.addContent( answer_choice_widget );
            x += oneField;
        }
        y += maxHeight + spacing;
        String totalHeight = answerRoot.getAttributeValue( "height" );
        if ( totalHeight == null )
            answerRoot.setAttribute( "height", String.valueOf( y ) );
        for ( int i = 0; i < queue.length; i++ )
            answerRoot.addContent( queue[ i ] );
    }
    
    public void doZ3Or4()
    {
        
        boolean selectorFirst = true;
        String layout = "horizontal";
        String selectorLoaction = src.getAttributeValue( "SelectorPosition" );
        String selectorKey = "valign";
        if ( selectorLoaction.equals( "bottomcenter" ) )
        {
            selectorLoaction = "center";
            selectorFirst = false;
            layout = "vertical";
            selectorKey = "halign";
        }
        else if ( selectorLoaction.equals( "lefttop" ) )
        {
            selectorLoaction = "top";
        }
        else // left center
        {
            selectorLoaction = "middle";
        }
        
        int x = padding; 
        int y = padding;
        int maxHeight = radioButtonSize;
        String totalWidth = answerRoot.getAttributeValue( "width" );
        int oneField = ( Integer.valueOf( totalWidth ).intValue() - padding ) / 2;
        for ( int i = 0; i < 2; i++ )
        {
            Element answer_choice_widget = new Element( choiceContainer );
            answer_choice_widget.setAttribute( "spacing", selectorSpacing );
            answer_choice_widget.setAttribute( "layout", layout );
            List childList = ( List )selectedResponses.get( i );
            Element selector = new Element( "selector_widget" );
            selector.setAttribute( selectorKey, selectorLoaction );
            if ( selectorFirst )
                answer_choice_widget.addContent( selector );
            for ( int j = 0; j < childList.size(); j++ )
            {
                Element child = ( Element )childList.get( j );
                answer_choice_widget.addContent( child );
                if (ItemLayoutProcessor.rationale_widget.equals( child.getName())) {
                    continue;
                }
                
                int heightInt = Integer.valueOf( child.getAttributeValue( "height" )).intValue();
                if ( selectorFirst )
                {
                    if ( heightInt > maxHeight )
                        maxHeight = heightInt;
                }
                else
                {
                    if ( ( heightInt + 6 + radioButtonSize ) > maxHeight )
                        maxHeight = heightInt + 6 + radioButtonSize;
                    child.setAttribute( "halign", "center" );
                    answer_choice_widget.setAttribute( "width", child.getAttributeValue( "width" ));
                }
            }
            if ( !selectorFirst )
                answer_choice_widget.addContent( selector );
            
            answer_choice_widget.setAttribute( "x", String.valueOf( x ) );
            answer_choice_widget.setAttribute( "y", String.valueOf( y ) );
            answer_choice_widget.setAttribute( "width", String.valueOf( oneField ) );
            answer_choice_widget.setAttribute( "height", String.valueOf( maxHeight ) );
            answerRoot.addContent( answer_choice_widget );
            x += oneField;
        }
        x = padding;
        y += maxHeight + spacing;
        for ( int i = 2; i < selectedResponses.size(); i++ )
        {
            Element answer_choice_widget = new Element( choiceContainer );
            answer_choice_widget.setAttribute( "spacing", selectorSpacing );
            answer_choice_widget.setAttribute( "layout", layout );
            List childList = ( List )selectedResponses.get( i );
            Element selector = new Element( "selector_widget" );
            selector.setAttribute(selectorKey, selectorLoaction );
            if ( selectorFirst )
                answer_choice_widget.addContent( selector );
            for ( int j = 0; j < childList.size(); j++ )
            {
                Element child = ( Element )childList.get( j );
                answer_choice_widget.addContent( child );
                if (ItemLayoutProcessor.rationale_widget.equals( child.getName())) {
                    continue;
                }
                int heightInt = Integer.valueOf( child.getAttributeValue( "height" )).intValue();
                if ( selectorFirst )
                {
                    if ( heightInt > maxHeight )
                        maxHeight = heightInt;
                }
                else
                {
                    if ( ( heightInt + 6 + radioButtonSize ) > maxHeight )
                        maxHeight = heightInt + 6 + radioButtonSize;
                    child.setAttribute( "halign", "center" );
                    answer_choice_widget.setAttribute( "width", child.getAttributeValue( "width" ));
                }
            }
            if ( !selectorFirst )
                answer_choice_widget.addContent( selector );
            answer_choice_widget.setAttribute( "x", String.valueOf( x ) );
            answer_choice_widget.setAttribute( "y", String.valueOf( y ) );
            answer_choice_widget.setAttribute( "width", String.valueOf( oneField ) );
            answer_choice_widget.setAttribute( "height", String.valueOf( maxHeight ) );
            answerRoot.addContent( answer_choice_widget );
            x += oneField;
        }
        y += maxHeight + spacing;
        String totalHeight = answerRoot.getAttributeValue( "height" );
        if ( totalHeight == null )
            answerRoot.setAttribute( "height", String.valueOf( y ) );
    }
    
    public void doStraightHorizontal()
    {
        String selectorLoaction = src.getAttributeValue( "SelectorPosition" );
        if ( selectorLoaction.equals( "bottomcenter" ) )
            selectorLoaction = "center";
        else
            selectorLoaction = "left";
        int x = padding; 
        int width = padding;
        int maxHeight = radioButtonSize;
        String height = answerRoot.getAttributeValue( "height" );
        String totalWidth = answerRoot.getAttributeValue( "width" );//spacing
        int thisField, oneField = ( Integer.valueOf( totalWidth ).intValue() - padding - ( spacing * ( selectedResponses.size() - 1 )) ) / selectedResponses.size();
        for ( int i = 0; i < selectedResponses.size(); i++ )
        {
            thisField = oneField;
            Element answer_choice_widget = new Element( choiceContainer );
            answer_choice_widget.setAttribute( "layout", "vertical" );
            answer_choice_widget.setAttribute( "spacing", "6" );
            List childList = ( List )selectedResponses.get( i );
            Element selector = new Element( "selector_widget" );
            selector.setAttribute( "halign", selectorLoaction );
            int answerChoiceWidgetWidth=0;
            for ( int j = 0; j < childList.size(); j++ )
            {
                Element child = ( Element )childList.get( j );
                answer_choice_widget.addContent( child );
                if (ItemLayoutProcessor.rationale_widget.equals( child.getName())) {
                    continue;
                }
                
                int heightInt = child.getAttributeValue( "height")==null?0:Integer.valueOf( child.getAttributeValue( "height" )).intValue();
                if ( heightInt > maxHeight )
                    maxHeight = heightInt;
                if ( child.getName().equals( "text_widget" ) )
                {
                    child.setAttribute( "width", String.valueOf( thisField ) );
           //         child.setAttribute( "halign", "center" );
                }
                else if ( containImage )
                {
                    thisField = child.getAttributeValue( "width")==null?0:Integer.valueOf( child.getAttributeValue( "width" )).intValue();
                }
                
                answerChoiceWidgetWidth = answerChoiceWidgetWidth + thisField;
            }
            answer_choice_widget.addContent( selector );
            answer_choice_widget.setAttribute( "x", String.valueOf( width ) );
            answer_choice_widget.setAttribute( "y", String.valueOf( 0 ) );
            answer_choice_widget.setAttribute( "width", String.valueOf( answerChoiceWidgetWidth ) );
      //      selector.setAttribute( "width", String.valueOf( thisField ) );
            answerRoot.addContent( answer_choice_widget );
            width += spacing + thisField;
        }
        maxHeight += padding + 6 + radioButtonSize + padding;
        if ( height == null )
            answerRoot.setAttribute( "height", String.valueOf( maxHeight + 6 ) );
    }

    public void doStraightVertical()
    {//SelectorPosition (leftcenter|bottomcenter|lefttop|default) "default"
        int text_to_scrollbar_margin = 3;
        String selectorLoaction = src.getAttributeValue( "SelectorPosition" );
        if ( selectorLoaction.equals( "leftcenter" ) )
            selectorLoaction = "middle";
        else
            selectorLoaction = "top";
        int x = padding; 
        int height = padding * 2;
        int thisHeight = 0;
        int width = Integer.valueOf( answerRoot.getAttributeValue( "width")).intValue();
        int boundWidth = width - padding;
        int widgetMaxWidthAllowed = width - padding - Integer.valueOf( selectorSpacing ).intValue() - radioButtonSize - scrollbarWidth - text_to_scrollbar_margin;
        for ( int i = 0; i < selectedResponses.size(); i++ )
        {
            Element answer_choice_widget = new Element( choiceContainer );
            answer_choice_widget.setAttribute( "spacing", selectorSpacing );
            answer_choice_widget.setAttribute( "layout", "horizontal" );
            List childList = ( List )selectedResponses.get( i );
            Element selector = new Element( "selector_widget" );
            answer_choice_widget.addContent( selector );
            selector.setAttribute( "valign", selectorLoaction );
            for ( int j = 0; j < childList.size(); j++ )
            {
                Element child = ( Element )childList.get( j );
                if (ItemLayoutProcessor.rationale_widget.equals( child.getName())) {
                    answer_choice_widget.addContent( child );
                    continue;
                }
                    
                else if ( ItemLayoutProcessor.text_widget.equals( child.getName() ))
                {
                	if(child.getAttributeValue( "height" ) != null){
                		thisHeight = Integer.valueOf( child.getAttributeValue( "height" ) ).intValue();
                	}
                	if(child.getAttributeValue( "width" ) != null)
                	{
                		int widgetWidth = Integer.valueOf( child.getAttributeValue( "width" ) ).intValue();
	                    if ( widgetWidth > widgetMaxWidthAllowed )
	                    {
	                        child.setAttribute( "width", String.valueOf( widgetMaxWidthAllowed ));
	                        thisHeight = thisHeight * ( ( widgetWidth - 1 ) / widgetMaxWidthAllowed + 1 );
	//                        child.setAttribute( "height", String.valueOf( thisHeight ));
	                    }
                	}
                }
                else
                    thisHeight = child.getAttributeValue( "height")==null?0:Integer.valueOf( child.getAttributeValue( "height")).intValue();
                if ( thisHeight < radioButtonSize)
                    thisHeight = radioButtonSize;
                answer_choice_widget.addContent( child );
                height += thisHeight + spacing;
            }
//            answer_choice_widget.setAttribute( "x", String.valueOf( x ) );
//            answer_choice_widget.setAttribute( "y", String.valueOf( height ) );
            answer_choice_widget.setAttribute( "width", String.valueOf( boundWidth ) );
//            answer_choice_widget.setAttribute( "height", String.valueOf( maxHeight ) );
            answerRoot.addContent( answer_choice_widget );
        }
        if ( answerRoot.getAttributeValue( "height" ) == null )
            answerRoot.setAttribute( "height", String.valueOf( height - spacing ) );
    }
}
