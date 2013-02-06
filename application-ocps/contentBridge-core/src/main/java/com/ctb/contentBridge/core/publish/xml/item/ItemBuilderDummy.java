package com.ctb.contentBridge.core.publish.xml.item;

import org.jdom.Element;


public class ItemBuilderDummy implements ItemBuilder{

     public Item build(Element item) {
     	return new Item();
    }
}
