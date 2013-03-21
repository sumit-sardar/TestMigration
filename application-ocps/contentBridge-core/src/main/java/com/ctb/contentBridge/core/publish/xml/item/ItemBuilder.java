package com.ctb.contentBridge.core.publish.xml.item;

import org.jdom.Element;


public interface ItemBuilder {
	Item build(Element item);
}
