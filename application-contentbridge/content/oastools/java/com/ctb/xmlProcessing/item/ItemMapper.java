package com.ctb.xmlProcessing.item;



import java.util.List;


public interface ItemMapper {
	Item mapItem(Item item) throws IdentityMappingException;
    List getAncestorObjectiveList(String objId,Item item);
}
