package com.ctb.contentBridge.core.publish.itemmap.csv;

import java.util.Map;

import com.ctb.contentBridge.core.exception.SystemException;
import com.ctb.contentBridge.core.publish.report.ItemMapReport;
import com.ctb.contentBridge.core.publish.xml.item.Item;


/**
 * @author wmli
 */
public class MappingBuilderAKUpdateDecorator implements MappingBuilder {
    MappingBuilder builder;
    Map items;

    public MappingBuilderAKUpdateDecorator(MappingBuilder builder, Map items) {
        this.builder = builder;
        this.items = items;
    }

    public MappingEntry build(String mapping) {
        // find the item for the corresponding entry
        MappingEntry entry = builder.build(mapping);

        Item item = (Item) items.get(entry.getItemId());

        if (item != null) {
            entry.setAnswerKey(item.getCorrectAnswer());
        } else {
            ItemMapReport.getCurrentReport().setException(new SystemException(
			"Cannot find item in csv item entry: ["
				+ entry.getItemId()
				+ "]"));
			ItemMapReport.getCurrentReport().setSuccess(false);
        }

        return entry;
    }
}
