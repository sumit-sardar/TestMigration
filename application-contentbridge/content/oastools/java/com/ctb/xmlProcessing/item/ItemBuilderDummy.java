package com.ctb.xmlProcessing.item;

import org.jdom.Element;


public class ItemBuilderDummy implements ItemBuilder{

     public Item build(Element item) {
     	return new Item();
    }
}
