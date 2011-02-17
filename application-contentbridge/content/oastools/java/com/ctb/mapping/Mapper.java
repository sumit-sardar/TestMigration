package com.ctb.mapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.jdom.Element;


import com.ctb.xmlProcessing.item.Item;
import com.ctb.xmlProcessing.item.ItemAssembler;

public class Mapper {
    private ItemMap itemMap;
    private Objectives objectives;

    public Mapper(ItemMap itemMap, Objectives objectives) {
        this.itemMap = itemMap;
        this.objectives = objectives;
    }

    public Item map(Item originalItem) {
        return map(originalItem, ItemAssembler.NO_DEFAULTS_VALIDATE_STRICT);
    }

    public Item map(Item originalItem, int validationMode) {
        return new ItemAssembler(validationMode).parseItem(
            mapItemXML(originalItem));
    }

    /** Will not map an Item into its own Framework*/
    public Element mapItemXML(Item originalItem) {
        Element newRootElement =
            (Element) originalItem.getItemRootElement().clone();
        String originalItemId = originalItem.getId();
        String newObjectiveId = itemMap.curriculumId(originalItemId);

        if (newObjectiveId == null)
            throw new UnmappedItemIDException(originalItemId);

        rewriteItemXml(newRootElement, originalItemId, newObjectiveId);
        return newRootElement;
    }

    public void rewriteItemXml(
        Element rootElement,
        String originalItemId,
        String newObjectiveId) {
        rootElement.setAttribute("ID", mappedItemId(originalItemId));
        rootElement.setAttribute("DisplayID", originalItemId);
    }

    public Collection getMappedItemsNotInObjectives() {
        ArrayList badItems = new ArrayList();

        Iterator iter = itemMap.getAllItemIDs();

        while (iter.hasNext()) {
            String itemID = (String) iter.next();
            String curriculumID = itemMap.curriculumId(itemID);

            if (objectives.objectiveFromCurriculumId(curriculumID) == null) {
                badItems.add(itemID);
            }
        }
        return badItems;
    }

    public String mappedItemId(String originalItemId) {
        return originalItemId;
    }

    public String curriculumId(String originalItemId) {
        return itemMap.curriculumId(originalItemId);
    }

    public Objectives getObjectives() {
        return objectives;
    }

    public ItemMap getItemMap() {
        return itemMap;
    }

}
