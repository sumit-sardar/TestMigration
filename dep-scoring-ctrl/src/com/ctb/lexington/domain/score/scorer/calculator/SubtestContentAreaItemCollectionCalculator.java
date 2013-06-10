package com.ctb.lexington.domain.score.scorer.calculator;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ctb.lexington.data.ItemContentArea;
import com.ctb.lexington.db.mapper.ItemMapper;
import com.ctb.lexington.db.utils.DatabaseHelper;
import com.ctb.lexington.domain.score.event.SubtestContentAreaItemCollectionEvent;
import com.ctb.lexington.domain.score.event.SubtestItemCollectionEvent;
import com.ctb.lexington.domain.score.event.common.Channel;
import com.ctb.lexington.domain.score.scorer.Scorer;
import com.ctb.lexington.util.IndexMap;

public class SubtestContentAreaItemCollectionCalculator extends Calculator {
    /**
     * Constructor for SubtestContentAreaItemCollectionCalculator.
     * 
     * @param channel
     * @param scorer
     */
    public SubtestContentAreaItemCollectionCalculator(Channel channel, Scorer scorer) {
        super(channel, scorer);
        channel.subscribe(this, SubtestItemCollectionEvent.class);
    }

    public void onEvent(final SubtestItemCollectionEvent event) {
        List<ItemContentArea> itemsByContentAreaQueryResults = getItemsByContentArea(event.getItemSetId(),
                event.getProductId());
        if(event.getProductId() == 7501 || event.getProductId() == 7502){
        	final Map<String,Long> contentMap = getAllVirtualContentArea(event.getItemSetId(),event.getProductId());
        	itemsByContentAreaQueryResults = populateContentListWithVirtual(itemsByContentAreaQueryResults, contentMap);
        }
        final IndexMap itemsByContentArea = new IndexMap(String.class, ItemContentArea.class,
                new IndexMap.Mapper() {
                    public Object getKeyFor(final Object value) {
                        return (((ItemContentArea) value).getItemId()+ ((ItemContentArea) value).getContentAreaName());
                    }
                });

        itemsByContentArea.addAll(itemsByContentAreaQueryResults);
        if(scorer.getResultHolder().getAdminData().getProductId() != 8000) {
        	channel.send(new SubtestContentAreaItemCollectionEvent(event.getTestRosterId(),
                    DatabaseHelper.asLong(event.getItemSetId()), itemsByContentArea));
        } else {

        channel.send(new SubtestContentAreaItemCollectionEvent(event.getTestRosterId(),
                DatabaseHelper.asLong(event.getItemSetId()), itemsByContentArea, event.getItemSetName()));
        }
    }

    protected List<ItemContentArea> getItemsByContentArea(Integer itemSetId, Integer productId) {
        Connection conn = null;
        try {
            conn = scorer.getOASConnection();
            ItemMapper mapper = new ItemMapper(conn);
            return mapper.findItemGroupByContentAreaForItemSetAndProduct(DatabaseHelper
                    .asLong(itemSetId), DatabaseHelper.asLong(productId));
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                scorer.close(true, conn);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    protected List<ItemContentArea> populateContentListWithVirtual(List<ItemContentArea> iContentAreaResults, Map<String,Long> vContentAreaMap){
    	List<ItemContentArea> tempVirtualContentList = new ArrayList<ItemContentArea>();
    	Iterator iter = vContentAreaMap.keySet().iterator();
        while (iter.hasNext()) {
        	String vContentArea = (String)iter.next();
        	if("Comprehension".equals(vContentArea)){
		        for(ItemContentArea ica : iContentAreaResults){
		    		if("Listening".equals(ica.getContentAreaName())){
		    			ItemContentArea newIca = new ItemContentArea();
		    			newIca.setContentAreaId(vContentAreaMap.get(vContentArea));
		    			newIca.setContentAreaName(vContentArea);
		    			newIca.setProductId(ica.getProductId());
		    			newIca.setItemId(ica.getItemId());
		    			newIca.setItemSetId(ica.getItemSetId());
		    			newIca.setMaxPoints(ica.getMaxPoints());
		    			tempVirtualContentList.add(newIca);
		    		}else if("Reading".equals(ica.getContentAreaName())){
		    			ItemContentArea newIca = new ItemContentArea();
		    			newIca.setContentAreaId(vContentAreaMap.get(vContentArea));
		    			newIca.setContentAreaName(vContentArea);
		    			newIca.setProductId(ica.getProductId());
		    			newIca.setItemId(ica.getItemId());
		    			newIca.setItemSetId(ica.getItemSetId());
		    			newIca.setMaxPoints(ica.getMaxPoints());
		    			tempVirtualContentList.add(newIca);
		    		}
		    	}
        	}else if("Oral".equals(vContentArea)){
		        for(ItemContentArea ica : iContentAreaResults){
		    		if("Listening".equals(ica.getContentAreaName())){
		    			ItemContentArea newIca = new ItemContentArea();
		    			newIca.setContentAreaId(vContentAreaMap.get(vContentArea));
		    			newIca.setContentAreaName(vContentArea);
		    			newIca.setProductId(ica.getProductId());
		    			newIca.setItemId(ica.getItemId());
		    			newIca.setItemSetId(ica.getItemSetId());
		    			newIca.setMaxPoints(ica.getMaxPoints());
		    			tempVirtualContentList.add(newIca);
		    		}else if("Speaking".equals(ica.getContentAreaName())){
		    			ItemContentArea newIca = new ItemContentArea();
		    			newIca.setContentAreaId(vContentAreaMap.get(vContentArea));
		    			newIca.setContentAreaName(vContentArea);
		    			newIca.setProductId(ica.getProductId());
		    			newIca.setItemId(ica.getItemId());
		    			newIca.setItemSetId(ica.getItemSetId());
		    			newIca.setMaxPoints(ica.getMaxPoints());
		    			tempVirtualContentList.add(newIca);
		    		}
		    	}
        	}else if("Productive".equals(vContentArea)){
		        for(ItemContentArea ica : iContentAreaResults){
		    		if("Speaking".equals(ica.getContentAreaName())){
		    			ItemContentArea newIca = new ItemContentArea();
		    			newIca.setContentAreaId(vContentAreaMap.get(vContentArea));
		    			newIca.setContentAreaName(vContentArea);
		    			newIca.setProductId(ica.getProductId());
		    			newIca.setItemId(ica.getItemId());
		    			newIca.setItemSetId(ica.getItemSetId());
		    			newIca.setMaxPoints(ica.getMaxPoints());
		    			tempVirtualContentList.add(newIca);
		    		}else if("Writing".equals(ica.getContentAreaName())){
		    			ItemContentArea newIca = new ItemContentArea();
		    			newIca.setContentAreaId(vContentAreaMap.get(vContentArea));
		    			newIca.setContentAreaName(vContentArea);
		    			newIca.setProductId(ica.getProductId());
		    			newIca.setItemId(ica.getItemId());
		    			newIca.setItemSetId(ica.getItemSetId());
		    			newIca.setMaxPoints(ica.getMaxPoints());
		    			tempVirtualContentList.add(newIca);
		    		}
		    	}
        	}else if("Literacy".equals(vContentArea)){
		        for(ItemContentArea ica : iContentAreaResults){
		    		if("Reading".equals(ica.getContentAreaName())){
		    			ItemContentArea newIca = new ItemContentArea();
		    			newIca.setContentAreaId(vContentAreaMap.get(vContentArea));
		    			newIca.setContentAreaName(vContentArea);
		    			newIca.setProductId(ica.getProductId());
		    			newIca.setItemId(ica.getItemId());
		    			newIca.setItemSetId(ica.getItemSetId());
		    			newIca.setMaxPoints(ica.getMaxPoints());
		    			tempVirtualContentList.add(newIca);
		    		}else if("Writing".equals(ica.getContentAreaName())){
		    			ItemContentArea newIca = new ItemContentArea();
		    			newIca.setContentAreaId(vContentAreaMap.get(vContentArea));
		    			newIca.setContentAreaName(vContentArea);
		    			newIca.setProductId(ica.getProductId());
		    			newIca.setItemId(ica.getItemId());
		    			newIca.setItemSetId(ica.getItemSetId());
		    			newIca.setMaxPoints(ica.getMaxPoints());
		    			tempVirtualContentList.add(newIca);
		    		}
		    	}
        	}
        }
        iContentAreaResults.addAll(tempVirtualContentList);
    	return iContentAreaResults;
    }
    
    protected Map<String,Long> getAllVirtualContentArea(Integer itemSetId, Integer productId) {
        Connection conn = null;
        try {
            conn = scorer.getOASConnection();
            ItemMapper mapper = new ItemMapper(conn);
            return mapper.findAllVirtuContent(DatabaseHelper
                    .asLong(itemSetId), DatabaseHelper.asLong(productId));
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                scorer.close(true, conn);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}