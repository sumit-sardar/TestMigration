package com.ctb.xmlProcessing.item;

import com.ctb.mapping.Objective;




import java.util.List;
import java.util.ArrayList;

import org.jdom.Element;


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
