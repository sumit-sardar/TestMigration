package com.ctb.xmlProcessing.item;

import org.jdom.Element;


public interface ItemBuilder {
	Item build(Element item);
}
