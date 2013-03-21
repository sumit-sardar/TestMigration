/*
 * Created on Nov 21, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ctb.contentBridge.core.publish.layout;

import org.jdom.Element;

/**
 * @author wen-jin_chang
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PanelAttributes 
{
    public String x;
    public String y;
    public String width;
    public String height;
    public String spacing;
    public String location;
    public String widgetLayout;
    
    public PanelAttributes() 
    {
        super();
        x = null;
        y = null;
        width = null;
        height = null;
        spacing = null;
        location = null;
        widgetLayout = null;
    }
    
    public void readAttributes( Element element )
    {
        if ( x == null )
            x = element.getAttributeValue( "x" );
        if ( y == null )
            y = element.getAttributeValue( "y" );
        if ( width == null )
            width = element.getAttributeValue( "width" );
        if ( height == null )
            height = element.getAttributeValue( "height" );
        if ( spacing == null )
            spacing = element.getAttributeValue( "spacing" );
        if ( widgetLayout == null )
            widgetLayout = element.getAttributeValue( "layout" );
        if ( location == null )
            location = element.getAttributeValue( "location" );  
    }
}
