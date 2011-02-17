package com.ctb.content.publish; 

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
    public static String renderCTBItemToLML(int id, String sourceItem, List unicodeList, String MaxPanelWidth, boolean includeAcknowledgement ) {
        try {
            org.jdom.input.SAXBuilder saxBuilder = new org.jdom.input.SAXBuilder();
            List assessList = new ArrayList();
            ByteArrayInputStream bais = new ByteArrayInputStream(sourceItem.getBytes());
            org.jdom.Document itemDoc = saxBuilder.build( bais );
            Element itemElement = itemDoc.getRootElement();
            Element layoutItem = getItemLayout( itemElement, id, unicodeList, MaxPanelWidth, includeAcknowledgement );
            XMLOutputter outputter = new XMLOutputter();
            return outputter.outputString(layoutItem);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static Element getItemLayout( Element itemElement, int order, List unicodeList, String MaxPanelWidth, boolean includeAcknowledgement ) throws Exception
    {
        ItemLayoutProcessor aItemLayoutProcessor = new ItemLayoutProcessor( itemElement, MaxPanelWidth, order, 12, false, unicodeList, false );
        aItemLayoutProcessor.setIncludeAcknowledgement(includeAcknowledgement);
        aItemLayoutProcessor.useStimulusDisplayID = true;
        Element layoutItem = aItemLayoutProcessor.layoutItem(); 
        return layoutItem; 
    }
} 
