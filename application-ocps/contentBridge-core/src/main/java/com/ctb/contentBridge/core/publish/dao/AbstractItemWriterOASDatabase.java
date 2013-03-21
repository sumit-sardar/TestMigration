package com.ctb.contentBridge.core.publish.dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ctb.contentBridge.core.publish.dao.DBDatapointGateway;
import com.ctb.contentBridge.core.publish.dao.DBItemGateway;
import com.ctb.contentBridge.core.publish.dao.DBObjectivesGateway;
import com.ctb.contentBridge.core.publish.hibernate.HibernateUtils;
import com.ctb.contentBridge.core.publish.report.ItemProcessorReport;
import com.ctb.contentBridge.core.publish.tools.Datapoint;
import com.ctb.contentBridge.core.publish.tools.OASConstants;
import com.ctb.contentBridge.core.publish.xml.item.Item;
import com.ctb.contentBridge.core.publish.xml.item.ItemWriter;
import com.ctb.contentBridge.core.util.ObjectiveUtil;

import net.sf.hibernate.Session;



abstract public class AbstractItemWriterOASDatabase implements ItemWriter {
    private final Session session;
    private final DBItemGateway igw;
    private final DBObjectivesGateway ogw;
    private final DBDatapointGateway dpgw;
    private ItemValidatorOASDatabase databaseValidator;
    public static final long userId = OASConstants.CREATED_BY;

    public AbstractItemWriterOASDatabase(Connection trueConnection) {
        this(HibernateUtils.getSession(trueConnection));
    }

    public AbstractItemWriterOASDatabase(Session session) {
        igw = new DBItemGateway(session);
        ogw = new DBObjectivesGateway(session);
        dpgw = new DBDatapointGateway(session);
        this.session = session;
    }
    
    public void writeDatapointConditionCode( Item item )
    {
        //START SPRINT 10: TO SUPPORT MAPPING AN ITEM TO MULTIPLE OBJECTIVE
        List datapointList ;
        List itemSetIdList = new ArrayList();
        List updatedItemSetIdList= new ArrayList();
        final String OriginalObjectiveId =  item.getObjectiveId();
        String [] objectiveIdArray = ObjectiveUtil.getArrayFromString(OriginalObjectiveId,ObjectiveUtil.ObjectiveSeperatore);
		for (int i=0; i<objectiveIdArray.length; i++) {
			long itemSetId = ogw.getItemSetIdFromObjective(objectiveIdArray[i], item.getFrameworkCode());
			itemSetIdList.add(new Long(itemSetId));
 		 }
        
        String[] conditionCodes = item.isCR() ? DBDatapointGateway.CR_CONDITION_CODES
                : DBDatapointGateway.SR_CONDITION_CODES;
   //     dpgw.deleteItemDatapoints( item.getId(), itemSetId );
        datapointList = dpgw.getFrameworkDatapoint( item.getId(), item.getFrameworkCode() );
        

		for (Iterator it= datapointList.iterator(); it.hasNext();){
       	long itemSetId= ((Datapoint)it.next()).getItemSetId();
       	if( itemSetIdList.contains(new Long (itemSetId)) ){
       		dpgw.updateDataPoint(item.getId(), itemSetId, conditionCodes, item.getMinPoints(), item
                 .getMaxPoints());
       		updatedItemSetIdList.add(new Long(itemSetId));
       	} else {
       		dpgw.deleteItemDatapoints( item.getId(), itemSetId );
       	}
       }

       if(!updatedItemSetIdList.containsAll(itemSetIdList)){
       	 for (Iterator it= itemSetIdList.iterator(); it.hasNext();){
           	 long itemSetId= ((Long)it.next()).longValue();
           	if( !updatedItemSetIdList.contains(new Long (itemSetId)) ){
           		dpgw.insertDatapoint(item.getId(), itemSetId,
                        conditionCodes, item.getMinPoints(), item.getMaxPoints());
           	} 
           }
       }
        
       ogw.linkItemToObjective(item.getId(), itemSetIdList
				, item.getFrameworkCode(), !item.isInvisible());
	 //END SPRINT 10: TO SUPPORT MAPPING AN ITEM TO MULTIPLE OBJECTIVE
   }

    protected void setDatabaseValidator(Item item) {
        this.databaseValidator =
            new ItemValidatorOASDatabase(item, igw, ogw, dpgw);
    }

    protected ItemValidatorOASDatabase getDatabaseValidator() {
        return this.databaseValidator;
    }

    public DBItemGateway getItemGateway() {
        return this.igw;
    }

    protected Session getSession() {
        return this.session;
    }

    void setItemIdFromDatabase(Item item) {
 /*
        String originalItemID = item.getHistory();
        String id = null;
        String frameworkCode = item.getFrameworkCode();
        if ((originalItemID == null) || (originalItemID.equals("")))
            id = item.getId();
        else 
        {
            if ( this.igw.activeItemExists(originalItemID, frameworkCode ))
            {
                id = this.igw.getItemId( originalItemID, frameworkCode );
                item.getItemRootElement().setAttribute( "ID", id );
                Media media = item.getMedia();
                if (media != null)
                    item.getMedia().setXml(new R2XmlOutputter().outputString(item.getItemRootElement()).toCharArray());
            }
            else
                id = item.getId();
        }
        
        item.setId(id); */
    }
}
