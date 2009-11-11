package com.ctb.lexington.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ctb.lexington.util.CTBConstants;

public class ItemResponseList implements Serializable {
	private ArrayList sourceList = null;
	private ArrayList filterList = null;
	private ArrayList pageList = null;
	private String itemType = CTBConstants.ITEM_TYPE_ALL;
	private int pageNumber = 1;
    private int pageSize = 20;
	
	public ItemResponseList(List sourceList, String itemType) {
	    this.sourceList = (ArrayList)sourceList;
	    this.itemType = itemType;
	    this.pageNumber = 1;
	    this.pageSize = 20;
	}
    public ArrayList getFilterList() {
        return filterList;
    }
    public void setFilterList(ArrayList filterList) {
        this.filterList = filterList;
    }
    public ArrayList getSourceList() {
        return sourceList;
    }
    public void setSourceList(ArrayList sourceList) {
        this.sourceList = sourceList;
    }	
    public ArrayList getPageList() {
        return pageList;
    }
    public void setPageList(ArrayList pageList) {
        this.pageList = pageList;
    }
    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }
    public int getPageNumber() {
        return this.pageNumber;
    }
    public int getPageSize() {
        return pageSize;
    }
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    public String getItemType() {
        return this.itemType;
    }
    public void setItemType(String itemType) {
        this.itemType = itemType;
    }
    public int getSourceListSize() {
        return this.sourceList.size();
    }
    public int getFilterListSize() {
        return this.filterList.size();
    }
    public int getPageListSize() {
        return this.pageList.size();
    }
    
    public ArrayList filterByItemType(String itemType) {
        ArrayList result = new ArrayList();
        for (Iterator iter = this.sourceList.iterator(); iter.hasNext();) {
            ItemResponseDetailVO item = (ItemResponseDetailVO) iter.next();
            if (itemType.equals(CTBConstants.ITEM_TYPE_ALL)) 
                result.add(item.clone());
            else
            if (itemType.equals(item.getItemType()))
                result.add(item.clone());
        }
        return result;
    }   
    
    public ArrayList getCurrentPage() {
        this.filterList = filterByItemType(this.itemType);
        this.pageList = new ArrayList();
        int index = (this.pageNumber - 1) * this.pageSize;
        if ((index >= 0) && (index < filterList.size())) {
            int count = 0;
	        for (int i=index ; i<this.filterList.size() ; i++) {
	            ItemResponseDetailVO item = (ItemResponseDetailVO) this.filterList.get(i);
	            this.pageList.add(item.clone());
	            count++;
	            if (count == this.pageSize)
	                break;
	        }
        }
        return this.pageList;
    }    
      
    public void computeCurrentPage() {
        this.pageList = getCurrentPage();
    }    
    
    public void copyAttributes(ArrayList itemList) {
        for (Iterator iter1 = itemList.iterator(); iter1.hasNext();) {
            ItemResponseDetailVO item = (ItemResponseDetailVO) iter1.next();
        }        
    }
    
    public void updateEnteredItemResponses(Map parametersMap, boolean isStudentLoggedIn) {
        String key = null;
        String[] value = null;
        String itemId = null;
        Iterator keys = parametersMap.keySet().iterator();
        
        // Annoyed.  Filter out the CR scores if a CC condition code is 
        // present for that item.  You can't have it all, bud.  Condition
        // code trumps CR score since JavaScript logic should prevent this
        // from occurring.
        Hashtable filteredMap = new Hashtable();
        while( keys.hasNext() ) {
        	key = (String) keys.next();
            value = (String[]) parametersMap.get(key);
            itemId = key.substring(3);
        	if( key.startsWith("SR_") || key.startsWith("CT_") ) {
        		// put these in the filtered parameter map
        		filteredMap.put(key, value);
        	} else if( key.startsWith("CR_") ) {
        		// place only if no corresponding CC
        		if( !filteredMap.containsKey("CC_" + itemId) )
        			filteredMap.put(key, value);
	    	} else if( key.startsWith("CC_") ) {
	    		// place as well as remove the corresponding CR
    			filteredMap.put(key, value);
	    		if( filteredMap.containsKey("CR_" + itemId) ) {
	    			filteredMap.remove("CR_" + itemId);
	    		}
	    	}
        		
        }
        
        keys = filteredMap.keySet().iterator();
        while (keys.hasNext()) {
            key = (String) keys.next();
            value = (String[]) parametersMap.get(key);
            itemId = key.substring(3);
            if (key.startsWith("SR_")) {
                // only update SR items if the student has NOT logged in
                if (!isStudentLoggedIn) {
                    setKeyEnteredResponseInList(itemId, value[0]);
                }
            } else if (key.startsWith("CR_") || key.startsWith("CC_")) {
                setKeyEnteredResponseInList(itemId, value[0]);
            } else if (key.startsWith("CT_")) {
                setKeyEnteredCommentInList(itemId, value[0]);
            }
        }
    }
    
    private void setKeyEnteredResponseInList(String itemId, String response) {
        List pointsList = null;
        ItemResponsePointsVO points = null;
        ItemResponseDetailVO currentResponse = null;
        Iterator it = this.sourceList.iterator();

        while (it.hasNext()) {
            currentResponse = (ItemResponseDetailVO) it.next();
            if (currentResponse.getItemId().equals(itemId)) {
                String value = response.substring(3);
                if (response.startsWith("RS_")) {
                    currentResponse.setResponse(value);
                    pointsList = currentResponse.getItemResponsePointsVO();
                    if (pointsList.size() > 0) {
                        points = (ItemResponsePointsVO) pointsList.get(0);
                        points.setPoints(null);
                        points.setConditionCodeId(null);
                    }
                } else if (response.startsWith("SC_")) {
                    pointsList = currentResponse.getItemResponsePointsVO();
                    if (pointsList.size() > 0) {
                        points = (ItemResponsePointsVO) pointsList.get(0);
                        points.setPoints(new Integer(value));
                        points.setConditionCodeId(null);
                    }
                    currentResponse.setResponse(null);
                } else if (response.startsWith("CC_")) {
                    pointsList = currentResponse.getItemResponsePointsVO();
                    if (pointsList.size() > 0) {
                        points = (ItemResponsePointsVO) pointsList.get(0);
                        points.setConditionCodeId(new Integer(value));
                        points.setPoints(null);
                    }
                    currentResponse.setResponse(null);
                }

                break;
            }
        }
    }

    private void setKeyEnteredCommentInList(String itemId_, String comment_) {
        List pointsList = null;
        ItemResponsePointsVO points = null;
        ItemResponseDetailVO currentResponse = null;
        Iterator it = this.sourceList.iterator();

        while (it.hasNext()) {
            currentResponse = (ItemResponseDetailVO) it.next();
            if (currentResponse.getItemId().equals(itemId_)) {
                pointsList = currentResponse.getItemResponsePointsVO();
                points = (ItemResponsePointsVO) pointsList.get(0);
                points.setComments(comment_);
                break;
            }
        }
    }

    public int getSubtestItemUnscored() {
        return getItemUnscored(this.sourceList);
    }
    
    public int getItemScored(List theList) {
        Iterator it = theList.iterator();
        int itemScoredCount = 0;
        while (it.hasNext()) {
            ItemResponseDetailVO item = (ItemResponseDetailVO) it.next();
            if (isItemScored(item))
                itemScoredCount++;
        }
        return itemScoredCount; 
    }

    public int getItemUnscored(List theList) {
        int itemScoredCount = getItemScored(theList);
        return (theList.size() - itemScoredCount); 
    }
    
    public boolean isItemScored(ItemResponseDetailVO item) {
        boolean isScored = false;
        List pointsList = null;
        ItemResponsePointsVO points = null;

        if (item.getItemType().equals("SR")) {
            if (item.getResponse() != null) {
                isScored = true;
            }
            else {
                pointsList = item.getItemResponsePointsVO();
                if (pointsList.size() > 0) {
                    points = (ItemResponsePointsVO) pointsList.get(0);
                    if (points.getConditionCodeId() != null)
                        isScored = true;
                }
            }
        }
        if (item.getItemType().equals("CR")) {
            pointsList = item.getItemResponsePointsVO();
            if (pointsList.size() > 0) {
                points = (ItemResponsePointsVO) pointsList.get(0);
                if ((points.getPoints() != null) || (points.getConditionCodeId() != null))
                    isScored = true;
            }
        }
        return isScored;
    }
    
    public int findFirstUnScoredItemInFilter() {
        int index = 0;
        for (Iterator iter = this.filterList.iterator(); iter.hasNext();) {
            ItemResponseDetailVO item = (ItemResponseDetailVO) iter.next();
            if (isItemScored(item))
                index++;
            else
                break;
        }
        if (index == this.filterList.size())
            index = -1;
        return index;
    }
    
    
    public void sortQuestionOrder(String ascending) {
        if (ascending.equals("true")) 
            Collections.sort(this.sourceList, new OrderAscendingComparator());
        else
            Collections.sort(this.sourceList, new OrderDescendingComparator());
    }    

    public void sortQuestionType(String ascending) {
        if (ascending.equals("true")) 
            Collections.sort(this.sourceList, new TypeAscendingComparator());
        else
            Collections.sort(this.sourceList, new TypeDescendingComparator());
    }
    
	private class OrderAscendingComparator implements Comparator{
		public int compare(Object o1, Object o2) {
			int result = 0;
			ItemResponseDetailVO left = (ItemResponseDetailVO) o1;
			ItemResponseDetailVO right = (ItemResponseDetailVO) o2;
			result = compareOrders(left, right);
			return result;
		}
	}

	private class OrderDescendingComparator implements Comparator{
		public int compare(Object o1, Object o2) {
			int result = 0;
			ItemResponseDetailVO left = (ItemResponseDetailVO) o1;
			ItemResponseDetailVO right = (ItemResponseDetailVO) o2;
			result = compareOrders(right, left);
			return result;
		}
	}
	
	private class TypeAscendingComparator implements Comparator{
		public int compare(Object o1, Object o2) {
			int result = 0;
			ItemResponseDetailVO left = (ItemResponseDetailVO) o1;
			ItemResponseDetailVO right = (ItemResponseDetailVO) o2;
			result = compareTypes(left, right);
			if(result == 0)
				result = compareOrders(left, right);
			return result;
		}
	}

	private class TypeDescendingComparator implements Comparator{
		public int compare(Object o1, Object o2) {
			int result = 0;
			ItemResponseDetailVO left = (ItemResponseDetailVO) o1;
			ItemResponseDetailVO right = (ItemResponseDetailVO) o2;
			result = compareTypes(right, left);
			if(result == 0)
				result = compareOrders(left, right);
			return result;
		}
	}
	
	private int compareOrders(ItemResponseDetailVO left, ItemResponseDetailVO right){
		Integer leftOrder = left.getItemSetPosition() != null ? left.getItemSetPosition() : new Integer(0);		
		Integer rightOrder = right.getItemSetPosition() != null ? right.getItemSetPosition() : new Integer(0);	
		return leftOrder.compareTo(rightOrder);
	}
	
	private int compareTypes(ItemResponseDetailVO left, ItemResponseDetailVO right){
		String leftType = left.getItemType() != null ? left.getItemType() : "";		
		String rightType = right.getItemType() != null ? right.getItemType() : "";
		return leftType.toLowerCase().compareTo(rightType.toLowerCase());
	}
	
}
