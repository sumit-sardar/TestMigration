package utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.beehive.controls.api.bean.Control;

import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.testAdmin.RosterElement;
import com.ctb.bean.testAdmin.RosterElementData;
import com.ctb.bean.testAdmin.ScorableItem;
import com.ctb.bean.testAdmin.ScorableItemData;
import com.ctb.exception.CTBBusinessException;
import com.ctb.widgets.bean.PagerSummary;
import com.ctb.control.crscoring.TestScoring;


public class ItemScoringUtils {
	
		
	/**
     * buildItemList
     */    
    public static List<ScorableItem> buildItemList(ScorableItemData siData) 
    {
        
    	ArrayList<ScorableItem> itemList = new ArrayList<ScorableItem>();
    	if(siData != null){
    		ScorableItem[] items = siData.getScorableItems();
    		for(int i=0 ; i<items.length ; i++){
    			ScorableItem item = (ScorableItem)items[i];
    			if(item != null){
    				if(item.getItemType().equalsIgnoreCase("AI")) {
    					item.setItemType("Audio Item");
    				} else {
    					item.setItemType("Constructive Response");
    				}
    				itemList.add(item);
    			}    			
    		}    		
    	}   	
    	return itemList;
    }
    
    /**
     * buildStudentListByItem
     */    
    public static List<RosterElement> buildStudentListByItem(RosterElementData reData) 
    {
        
    	ArrayList<RosterElement> studentList = new ArrayList<RosterElement>();
    	if(reData != null){
    		RosterElement[] students = reData.getRosterElements();
    		for(int i=0 ; i<students.length ; i++){
    			RosterElement student = (RosterElement)students[i];
    			if(student != null){
    				studentList.add(student);
    			}    			
    		}    		
    	}   	
    	return studentList;
    }
    
    /**
     * buildStudentPagerSummary
     */    
    public static PagerSummary buildStudentPagerSummary(RosterElementData reData, Integer pageRequested) 
    {
        PagerSummary pagerSummary = new PagerSummary();
        pagerSummary.setCurrentPage(pageRequested);                
        pagerSummary.setTotalObjects(reData.getTotalCount());
        pagerSummary.setTotalPages(reData.getFilteredPages());
        //pagerSummary.setTotalFilteredObjects(reData.getFilteredCount());                
        return pagerSummary;
    }
    

    
    /**
     * buildItemPagerSummary
     */    
    public static PagerSummary buildItemPagerSummary(ScorableItemData siData, Integer pageRequested) 
    {
        PagerSummary pagerSummary = new PagerSummary();
        pagerSummary.setCurrentPage(pageRequested);                
        pagerSummary.setTotalObjects(siData.getTotalCount());
        pagerSummary.setTotalPages(siData.getFilteredPages());
       // pagerSummary.setTotalFilteredObjects(siData.getFilteredCount());                
        return pagerSummary;
    }
    
    /**
     * getItemsByTestSession
     */  
    
    public static ScorableItemData getItemsByTestSession(TestScoring testScoring, FilterParams filter, PageParams page, SortParams sort, Integer testAdminId)
    {   
    	ScorableItemData siData = null;
        try {    
        	siData = testScoring.getAllScorableCRItemsForItemSet(filter, page, sort, testAdminId);
        	}
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }        
        return siData;
    }
    
    /**
     * getStudentsByItem
     */  
    
    public static RosterElementData getStudentsByItem(TestScoring testScoring, FilterParams filter, PageParams page, SortParams sort, Integer testAdminId, Integer itemSetId, String itemId)
    {   
    	RosterElementData reData = null;
        try {    
        	reData = testScoring.getAllStudentForTestSessionAndTD(testAdminId, itemSetId, itemId, filter, page, sort);
        	
        }
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }        
        return reData;
    }
    
    
    
}
