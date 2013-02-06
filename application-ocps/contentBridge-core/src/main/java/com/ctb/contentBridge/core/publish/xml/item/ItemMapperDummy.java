package com.ctb.contentBridge.core.publish.xml.item;


import java.util.List;
import java.util.ArrayList;

import org.jdom.Element;

import com.ctb.contentBridge.core.publish.mapping.Objective;


public class ItemMapperDummy implements ItemMapper {

    public Item mapItem(Item item) {
         return item;
    }

    public List getAncestorObjectiveList(String objId,Item item) {
        List objectiveList = new ArrayList();
        Element root  = item.getItemRootElement();
        while (root.getChild("Hierarchy") != null) {
            root = root.getChild("Hierarchy");
            objectiveList.add(new Objective(root.getAttributeValue("Name"),root.getAttributeValue("CurriculumID")));
        }
        return objectiveList;
    }

}
