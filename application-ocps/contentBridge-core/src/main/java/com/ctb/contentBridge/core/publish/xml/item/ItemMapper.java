package com.ctb.contentBridge.core.publish.xml.item;



import java.util.List;

import com.ctb.contentBridge.core.exception.BusinessException;


public interface ItemMapper {
	Item mapItem(Item item) throws IdentityMappingException;
    List getAncestorObjectiveList(String objId,Item item);
}
