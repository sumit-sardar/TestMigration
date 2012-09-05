package com.ctb.lexington.domain.score.scorer.calculator;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ctb.lexington.data.WsTvCaItemPeidVo;
import com.ctb.lexington.db.mapper.ItemMapper;
import com.ctb.lexington.db.utils.DatabaseHelper;
import com.ctb.lexington.domain.score.event.AssessmentStartedEvent;
import com.ctb.lexington.domain.score.event.SubtestItemCollectionEvent;
import com.ctb.lexington.domain.score.event.SubtestStartedEvent;
import com.ctb.lexington.domain.score.event.common.Channel;
import com.ctb.lexington.domain.score.scorer.Scorer;

public class SubtestItemCollectionCalculator extends Calculator {
    private Integer productId;
    private String currentWsTvSubject;
    private String subtestName = null;
    private String subtestIdCA = null;

    /**
     * @param channel
     * @param scorer
     */
    public SubtestItemCollectionCalculator(Channel channel, Scorer scorer) {
        super(channel, scorer);

        channel.subscribe(this, AssessmentStartedEvent.class);
        channel.subscribe(this, SubtestStartedEvent.class);
        mustPrecede(AssessmentStartedEvent.class, SubtestStartedEvent.class);
    }

    public void onEvent(AssessmentStartedEvent event) {
        productId = event.getProductId();
    }

    public void onEvent(SubtestStartedEvent event) {
        Collection items;
        Connection oasConnection = null;
        Connection oasWsTvConnection = null;
        try {
            oasConnection = scorer.getOASConnection();
            oasWsTvConnection = scorer.getOASConnection();
            if(scorer.getResultHolder().getAdminData().getProductId() == 3700 || 
            		scorer.getResultHolder().getAdminData().getProductId() == 3500) {
            	
            	if(this.subtestName != null && (event.getItemSetName().replaceAll("Part 1", "").equals(this.subtestName.replaceAll("Part 2", "")) || 
            	          event.getItemSetName().replaceAll("Part 2", "").equals(this.subtestName.replaceAll("Part 1", "")))) {
            	            this.subtestIdCA = this.subtestIdCA + "," + event.getItemSetId().toString();
            	            items = new ItemMapper(oasConnection).findItemByItemSetIdForCA(this.subtestIdCA); 	
            	            
            	 } else {
            		 this.subtestName = event.getItemSetName();
            		 this.subtestIdCA = event.getItemSetId().toString();
            		 items = new ItemMapper(oasConnection).findItemByItemSetId(DatabaseHelper.asLong(event
     	                    .getItemSetId()));
            	 }
            	
            } else {
            	items = new ItemMapper(oasConnection).findItemByItemSetId(DatabaseHelper.asLong(event
                        .getItemSetId()));
            }
            System.out.println("this.subtestIdCA -> " + this.subtestIdCA);
            
            //Added for webservice based terra nova third edition test
            //This will retrieve all the itemids and the corresponding peids for the content area.
            if(scorer.getResultHolder().getAdminData().getProductId() == 3700) {
            	
            	LinkedHashMap<String,LinkedHashMap<String,String>> contentAreaItemMap = scorer.getResultHolder().getCaResponseWsTv().getContentAreaItems();
            	System.out.println("event.getItemSetId() -> " + event.getItemSetId());
            	List<WsTvCaItemPeidVo> itemsPeids = new ItemMapper(oasWsTvConnection).findItemIdsAndPeidsByItemSetId(DatabaseHelper.asLong(event
                        .getItemSetId()));
            	
            	if (itemsPeids != null && itemsPeids.size() > 0) {
            		String contentAreaName = "";
            		String itemIdVal = "";
            		currentWsTvSubject = itemsPeids.get(0).getContentArea();
            		//Consider all items as not attempted at first so these are populated as 0.
            		// No need to consider condition for F as this is online test and student cannot click
            		// more than one option at once.
            		for(WsTvCaItemPeidVo caItemPeid : itemsPeids) {
            			contentAreaName = caItemPeid.getContentArea();
            			itemIdVal = caItemPeid.getItemId();
            			//System.out.println("itemIdVal -> " + itemIdVal);
            			if(contentAreaItemMap != null && !contentAreaItemMap.isEmpty()) {
            				if(contentAreaItemMap.containsKey(contentAreaName)) {
            					if(contentAreaItemMap.get(contentAreaName).containsKey(itemIdVal)) {
            						continue;
            					} else {
            						contentAreaItemMap.get(contentAreaName).put(itemIdVal, "0");
            					}
            				} else {
            					contentAreaItemMap.put(contentAreaName, new LinkedHashMap<String,String>());
            					contentAreaItemMap.get(contentAreaName).put(itemIdVal, "0");
            				}
            			} else {
            				contentAreaItemMap = new LinkedHashMap<String,LinkedHashMap<String,String>>();
            				contentAreaItemMap.put(contentAreaName, new LinkedHashMap<String,String>());
            				contentAreaItemMap.get(contentAreaName).put(itemIdVal, "0");
            			}
            		}
            		scorer.getResultHolder().getCaResponseWsTv().setContentAreaItems(contentAreaItemMap);
            	}
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                scorer.close(true, oasConnection);
                scorer.close(true, oasWsTvConnection);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if(items != null && items.size() > 0) {
	       
	        if(scorer.getResultHolder().getAdminData().getProductId() == 3700) {
	        	SubtestItemCollectionEvent subtestItemsEvent = new SubtestItemCollectionEvent(event
		                .getTestRosterId(), event.getItemSetId(), event.getItemSetName(), items, currentWsTvSubject);
		        subtestItemsEvent.setProductId(productId);
	        	subtestItemsEvent.setContentArea(currentWsTvSubject);
	        	channel.send(subtestItemsEvent);
	        } else {
	        	SubtestItemCollectionEvent subtestItemsEvent = new SubtestItemCollectionEvent(event
	 	                .getTestRosterId(), event.getItemSetId(), event.getItemSetName(), items);
	 	        subtestItemsEvent.setProductId(productId);
	 	        channel.send(subtestItemsEvent);
	        }
	    }
    }
}