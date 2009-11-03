package com.ctb.content.layout; 

import com.ctb.content.layout.ItemLayoutProcessor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

public class LMLRenderer 
{ 
    public static Element renderCTBItemToLML(int id, Element sourceItem, List unicodeList, String MaxPanelWidth, boolean includeAcknowledgement ) throws Exception
    {
        Element itemElement = sourceItem;
        Element layoutItem = getItemLayout( itemElement, id, unicodeList, MaxPanelWidth, includeAcknowledgement );
        return layoutItem;
    }
    
    public static String xmlToString( Element element )
    {
        XMLOutputter outputter = new XMLOutputter();
        return outputter.outputString( element );
    }
    
    public static Element getItemLayout( Element itemElement, int order, List unicodeList, String MaxPanelWidth, boolean includeAcknowledgement ) throws Exception
    {
        ItemLayoutProcessor aItemLayoutProcessor = new ItemLayoutProcessor( itemElement, MaxPanelWidth, order, 12, false, unicodeList, includeAcknowledgement );
        aItemLayoutProcessor.useStimulusDisplayID = true;
        Element layoutItem = aItemLayoutProcessor.layoutItem(); 
        return layoutItem; 
    }
} 
