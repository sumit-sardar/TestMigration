package com.ctb.lexington.domain.score.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.ctb.lexington.data.ConditionCodes;
import com.ctb.lexington.data.ItemVO;
import com.ctb.lexington.db.utils.DatabaseHelper;
import com.ctb.lexington.util.IndexMap;

public class SubtestItemCollectionEvent extends SubtestEvent {
    private final IndexMap itemIndex;
    private Integer productId;

    public SubtestItemCollectionEvent(final Long testRosterId, final Integer itemSetId,
            final String itemSetName, final Collection itemDTOCollection) {
        super(testRosterId, itemSetId);
        this.itemSetName = itemSetName;

        itemIndex = new IndexMap(String.class, ItemVO.class, new IndexMap.Mapper() {
            public Object getKeyFor(final Object value) {
                return ((ItemVO) value).getItemId();
            }
        }, itemDTOCollection);
    }
    
    public SubtestItemCollectionEvent(final Long testRosterId, final Integer itemSetId,
            final String itemSetName, final Collection itemDTOCollection, final String contentArea) {
        super(testRosterId, itemSetId);
        this.itemSetName = itemSetName;
        this.contentArea = contentArea;

        itemIndex = new IndexMap(String.class, ItemVO.class, new IndexMap.Mapper() {
            public Object getKeyFor(final Object value) {
                return ((ItemVO) value).getItemId();
            }
        }, itemDTOCollection);
    }

    private ItemVO getItem(final String itemId) {
        return (ItemVO) itemIndex.get(itemId);
    }

    /**
     * Gets an unmodifiable collection of items.
     * 
     * @return
     * @see Collections#unmodifiableCollection(java.util.Collection)
     */
    public Collection getItems() {
        return Collections.unmodifiableCollection(itemIndex.values());
    }

    /**
     * Gets an unmodifiable set of items IDs (strings)
     * 
     * @return
     */
    public Set getItemIds() {
        return Collections.unmodifiableSet(itemIndex.keySet());
    }

    /**
     * @todo Smells funny
     */
    public Iterator sortedIterator() {
        final List sortedCollection = new ArrayList(getItems());
        Collections.sort(sortedCollection);
        return sortedCollection.iterator();
    }

    /**
     * @param itemId
     * @return
     */
    public String getType(final String itemId) {
        return getItem(itemId).getItemType();
    }

    /**
     * @param event
     * @return
     */
    public boolean isAttempted(final ResponseReceivedEvent event) {
        boolean attempted = isAttempted(event.getItemId(), event.getResponse(), event.getConditionCodeId());
        boolean isOnlineCr = this.isOnlineCr(event.getItemId());
	boolean isCr = 
		this.getType(event.getItemId()) != null &&
		this.getType(event.getItemId()).equals("CR");
	boolean isKeyEntered =
		event.getResponseMethod() != null &&
		event.getResponseMethod().equals("KE");
	//an item viewed online is always attempted
	boolean isOnlineCapture = !"KE".equals(event.getResponseMethod());
	boolean paperQuestionDeliveredOnline = 
        	!isOnlineCr && isCr && !isKeyEntered;
        return (attempted || isOnlineCapture) && !paperQuestionDeliveredOnline;
    }
    
    /**
     * @param event
     * @return
     */
    public boolean isMarked(final ResponseReceivedEvent event) {
        boolean attempted = isAttempted(event.getItemId(), event.getResponse(), event.getConditionCodeId());
        boolean isOnlineCr = this.isOnlineCr(event.getItemId());
	boolean isCr = 
		this.getType(event.getItemId()) != null &&
		this.getType(event.getItemId()).equals("CR");
	boolean isKeyEntered =
		event.getResponseMethod() != null &&
		event.getResponseMethod().equals("KE");
	//an item viewed online is always attempted
	boolean paperQuestionDeliveredOnline = 
        	!isOnlineCr && isCr && !isKeyEntered;
        return attempted && !paperQuestionDeliveredOnline;
    }

    public boolean isAttempted(final String itemId, final String response,
            final Integer conditionCodeId) {
        final int[] conditionCodeIds = getItem(itemId).getConditionCodeIds();
        return ConditionCodes.getInstance()
                .isAttempted(conditionCodeIds, response, conditionCodeId);
    }

    // TODO: Make the logic and implementation comprehensible to mortals
    public boolean isConditionCode(final String itemId, final String response) {
        final int[] conditionCodeIds = getItem(itemId).getConditionCodeIds();
        return ConditionCodes.getInstance().isConditionCode(conditionCodeIds, response);
    }

    /**
     * @param itemId
     * @return
     */
    public Integer getMaxPoints(final String itemId) {
        Integer result = new Integer(1);
        final Integer itemMaxPoints = getItem(itemId).getMaxPoints();
        if (itemMaxPoints != null) {
            result = itemMaxPoints;
        }

        return result;
    }

    /**
     * @param itemId
     * @return
     */
    public Integer getMinPoints(final String itemId) {
        Integer result = new Integer(0);

        final Integer itemMinPoints = getItem(itemId).getMinPoints();
        if (itemMinPoints != null) {
            result = itemMinPoints;
        }

        return result;
    }

    public Long getPointsPossible() {
        final Collection allItems = getItems();
        long pointsPossible = 0;
        for (Iterator iter = allItems.iterator(); iter.hasNext();) {
            final ItemVO item = (ItemVO) iter.next();
            pointsPossible += getMaxPoints(item.getItemId()).longValue();
        }
        return new Long(pointsPossible);
    }

    /**
     * @return
     */
    private String getCorrectResponse(final String itemID) {
        return getItem(itemID).getCorrectAnswer();
    }

    public boolean isOnlineCr(final String itemId) {
        return DatabaseHelper.isTrue(getItem(itemId).getOnlineCr());
    }

    public boolean isOffline(final String itemId) {
        return (!DatabaseHelper.isTrue(getItem(itemId).getOnlineCr()) &&
        		DatabaseHelper.isTrue(getItem(itemId).getCr()));
    }
    
    public boolean isCorrectResponse(final String itemID, final String response) {
        final String correctResponse = getCorrectResponse(itemID);

        if (correctResponse == null)
            throw new IllegalStateException("Item " + itemID + " has no correct response listed.");

        return correctResponse.equalsIgnoreCase(response);
    }

    /**
     * @return Returns the productId.
     */
    public Integer getProductId() {
        return productId;
    }

    /**
     * @param productId The productId to set.
     */
    public void setProductId(final Integer productId) {
        this.productId = productId;
    }

    /**
     * This method is used to determine the test completion status. A test is not complete if there
     * are are CR items that are not on-line.
     * 
     * @return
     */
    public boolean hasOfflineItems() {
        return hasMatchingItems(new OfflineMatcher());
    }

    public int countOfflineItems() {
        int count=0;
        OfflineMatcher matcher = new OfflineMatcher();
        for (final Iterator it = getItems().iterator(); it.hasNext();)
            if (matcher.isMatch((ItemVO) it.next()))
                count++;
        return count;
    }

    /**
     * This method is used to determine the subtest scoring status. Any CR item (on-line or
     * off-line) currently requires manual scoring.
     * 
     * @return
     */
    public boolean hasItemsThatRequireManualScoring() {
        return hasMatchingItems(new MatchItem() {
            public boolean isMatch(final ItemVO item) {
                return ItemVO.ITEM_TYPE_CR.equals(item.getItemType());
            }
        });
    }

    private static interface MatchItem {
        public boolean isMatch(final ItemVO item);
    }

    private boolean hasMatchingItems(final MatchItem matcher) {
        for (final Iterator it = getItems().iterator(); it.hasNext();)
            if (matcher.isMatch((ItemVO) it.next()))
                return true;

        return false;
    }

    private class OfflineMatcher implements MatchItem {
        public boolean isMatch(final ItemVO item) {
            return ItemVO.ITEM_TYPE_CR.equals(item.getItemType()) && !isOnlineCr(item.getItemId());
        }
    }
}
