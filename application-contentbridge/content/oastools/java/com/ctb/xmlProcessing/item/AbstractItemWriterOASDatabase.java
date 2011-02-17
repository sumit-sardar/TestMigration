package com.ctb.xmlProcessing.item;

import java.sql.Connection;

import net.sf.hibernate.Session;

import com.ctb.common.tools.DBDatapointGateway;
import com.ctb.common.tools.DBItemGateway;
import com.ctb.common.tools.DBObjectivesGateway;
import com.ctb.common.tools.Datapoint;
import com.ctb.common.tools.OASConstants;

import com.ctb.hibernate.HibernateUtils;
import com.ctb.reporting.ItemProcessorReport;


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
        long itemSetId = ogw.getItemSetIdFromObjective( item.getObjectiveId(), item.getFrameworkCode());
        
        String[] conditionCodes = item.isCR() ? DBDatapointGateway.CR_CONDITION_CODES
                : DBDatapointGateway.SR_CONDITION_CODES;
   //     dpgw.deleteItemDatapoints( item.getId(), itemSetId );
        Datapoint theDatapoint = dpgw.getFrameworkDatapoint( item.getId(), item.getFrameworkCode() );
        if ( theDatapoint != null && theDatapoint.getItemSetId() == itemSetId )
        {
            dpgw.updateDataPoint(item.getId(), itemSetId, conditionCodes, item.getMinPoints(), item
                    .getMaxPoints());
        }
        else if ( theDatapoint != null )
        {
            ItemProcessorReport r = ItemProcessorReport.getCurrentReport();
            r.setWarning( "Item moved.");
            dpgw.updateDataPoint(item.getId(), theDatapoint.getItemSetId(), itemSetId, conditionCodes, item.getMinPoints(), item
                    .getMaxPoints());
        }
        else
        {
            dpgw.insertDatapoint(item.getId(), itemSetId,
                    conditionCodes, item.getMinPoints(), item.getMaxPoints());
        }
        ogw.linkItemToObjective(item.getId(), itemSetId
				, item.getFrameworkCode(), !item.isInvisible());
    }

    protected void setDatabaseValidator(Item item) {
        this.databaseValidator =
            new ItemValidatorOASDatabase(item, igw, ogw, dpgw);
    }

    protected ItemValidatorOASDatabase getDatabaseValidator() {
        return this.databaseValidator;
    }

    protected DBItemGateway getItemGateway() {
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
