/*
 * Created on May 13, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ctb.contentBridge.core.publish.xml.item;

import java.util.ArrayList;

import org.jdom.Element;

import com.ctb.contentBridge.core.domain.Configuration;
import com.ctb.contentBridge.core.publish.dao.AbstractItemWriterOASDatabase;
import com.ctb.contentBridge.core.publish.dao.DBItemGateway;
import com.ctb.contentBridge.core.publish.dao.ItemWriterOASDatabase;
import com.ctb.contentBridge.core.publish.dao.ItemWriterOASDatabaseInvisibleNoMedia;
import com.ctb.contentBridge.core.publish.report.AbstractXMLElementReport;
import com.ctb.contentBridge.core.publish.report.ItemProcessorReport;
import com.ctb.contentBridge.core.publish.tools.Datapoint;
import com.ctb.contentBridge.core.publish.tools.OASConstants;
/**
 * @author Wen-Jin_Chang
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ItemProcessorRoundTrip extends ItemProcessorGeneric {

    /**
     * @param openDeploy
     * @param builder
     * @param sourceValidator
     * @param mapper
     * @param mediaGenerator
     * @param writer
     * @param operation
     */
    public ItemProcessorRoundTrip(OpenDeploy openDeploy, ItemBuilder builder,
            ItemValidator sourceValidator, ItemMapper mapper,
            ItemMediaGenerator mediaGenerator, ItemWriter writer,
            String operation) {
        super(openDeploy, builder, sourceValidator, mapper, mediaGenerator,
                writer, operation, new ArrayList(), /*new ADSConfig(),*/null, new String(), new String());
        // TODO Auto-generated constructor stub
    }
    
    public AbstractXMLElementReport process(Element itemElement) {
        ItemProcessorReport r = ItemProcessorReport.getCurrentReport();
        r.setOperation(this.operation);
        AbstractItemWriterOASDatabase aItemWriterOASDatabase = (AbstractItemWriterOASDatabase)this.writer;
 //       Item item = this.builder.build(itemElement);
        String id = itemElement.getAttributeValue( "ID" );
        r.setNewID( itemElement.getAttributeValue( "ID" ));
        DBItemGateway aItemGateway = aItemWriterOASDatabase.getItemGateway();
        if ( !aItemGateway.itemExistsActiveOrInactive( id ) )
        {
            r.setWarning( "Item " + id + "is not in the target database." );
            return r;
        }
        if ( !aItemGateway.isItemXMLMediaExist( id ) )
        {
            r.setWarning( "Item " + id + "does not have xml media in the target database." );
            return r;
        }
        Item item = aItemGateway.getItem( id );
        item.setRootElement( itemElement );
        updateItemProcessorReport(item, r);

        this.sourceValidator.validate(item);

/*        Item mappedItem = null;
        try {
            mappedItem = this.mapper.mapItem(item);
        } catch (IdentityMappingException e) {
            r.setException(e);
            mappedItem = item;
            setAlignmentInformation(r, mappedItem);
            return r;
        } */
        ItemMapperMapOnly aMapper = ( ItemMapperMapOnly )this.mapper;
        item.setObjectiveId( aMapper.getObjectiveCode( item ));
        item.setFrameworkCode( aMapper.getFrameworkCode() );
        item.setInvisible( false ); 
        if ( item.isSR() )
        {
            item.setMinPoints( 0 );
            item.setMaxPoints( 1 );
            item.setNumAnswerChoices( 4 );
        }
        else
        {
            Datapoint aDatapoint = aItemGateway.getAnyDataPointForCRItem( id );
            item.setMinPoints( aDatapoint.getMinPoints() );
            item.setMaxPoints( aDatapoint.getMaxPoints() );
        }
        setAlignmentInformation(r, item);
        this.writer.writeDatapointConditionCode(item);
        r.setNewID( item.getId());
        
        return r;
    }
    
    public void updateItemProcessorReport(Item item, ItemProcessorReport r) 
    {      
        if ((this.writer instanceof ItemWriterOASDatabase)
            || (this.writer instanceof ItemWriterOASDatabaseInvisibleNoMedia))
            r.setActivationStatus(OASConstants.ITEM_STATUS_ACTIVE);
    }


}
