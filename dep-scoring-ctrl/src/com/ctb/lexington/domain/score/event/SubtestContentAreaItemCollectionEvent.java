package com.ctb.lexington.domain.score.event;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.ctb.lexington.data.ItemContentArea;
import com.ctb.lexington.domain.score.event.common.Event;
import com.ctb.lexington.util.IndexSetMap;

public class SubtestContentAreaItemCollectionEvent extends Event {
    private final Long itemSetId;
    private final Map itemContentAreasByItemId;
    private final IndexSetMap contentAreasIndex;
    private final String itemSetName; //Added for tabe adaptive

    public SubtestContentAreaItemCollectionEvent(final Long testRosterId, final Long itemSetId,
            final Map itemContentAreasByItemId) {
        super(testRosterId);
        this.itemSetId = itemSetId;
        this.itemContentAreasByItemId = itemContentAreasByItemId;
        this.contentAreasIndex = createContentAreasIndex(itemContentAreasByItemId.values());
        this.itemSetName = null;
    }
    
    public SubtestContentAreaItemCollectionEvent(final Long testRosterId, final Long itemSetId,
            final Map itemContentAreasByItemId, final String itemSetName) {
        super(testRosterId);
        this.itemSetId = itemSetId;
        this.itemContentAreasByItemId = itemContentAreasByItemId;
        this.contentAreasIndex = createContentAreasIndex(itemContentAreasByItemId.values());
        this.itemSetName = itemSetName;
    }

    private static IndexSetMap createContentAreasIndex(final Collection contentAreas) {
        return new IndexSetMap(String.class, ItemContentArea.class, new IndexSetMap.Mapper() {
            public Object getKeyFor(final Object value) {
                return ((ItemContentArea) value).getContentAreaName();
            }
        }, contentAreas);
    }

    public Map getItemContentAreasByItemId() {
        return itemContentAreasByItemId;
    }

    public Long getItemSetId() {
        return itemSetId;
    }

    public Set getContentAreaNames() {
        return contentAreasIndex.keySet();
    }

    public Set getItemContentAreasFor(final String contentArea) {
        return contentAreasIndex.getAll(contentArea);
    }

	public String getItemSetName() {
		return itemSetName;
	}
}