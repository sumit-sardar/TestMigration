package com.ctb.xmlProcessing.subtest;

import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.ctb.hibernate.persist.ItemSetCategoryRecord;
import com.ctb.hibernate.persist.ItemSetItemRecord;
import com.ctb.hibernate.persist.ItemSetRecord;
import com.ctb.hibernate.persist.ProductRecord;
import com.ctb.xmlProcessing.AbstractDBGateway;
import com.ctb.xmlProcessing.assessment.AssessmentType;

public abstract class AbstractDBSubTestGateway extends AbstractDBGateway {
    public AbstractDBSubTestGateway(Session session) {
        super(session);
    }

    abstract public List writeSubTest(SubTestHolder subTest, AssessmentType assessmentType);
    
    public void changeItemState( String framework_code, String itemId, boolean makeInvisible ) throws HibernateException
    {
        Query query = session.createQuery("select itemSetItem from " + ItemSetItemRecord.class.getName()
                + " as itemSetItem ," + ItemSetRecord.class.getName() + " as itemSet ," +
                ItemSetCategoryRecord.class.getName() + " as itemSetCategory ," +
                ProductRecord.class.getName() + " as product " +
                "where itemSetItem.id.itemId = :itemId and itemSetItem.id.itemSetId = itemSet.itemSetId and " +
                "itemSet.itemSetType = 'RE' and itemSet.itemSetCategoryId = itemSetCategory.itemSetCategoryId " +
                "and itemSetCategory.frameworkProductId = product.productId " +
                "and product.internalDisplayName = :framework_code");
        query.setString( "itemId", itemId );
        query.setString( "framework_code", framework_code );
        for (Iterator itemSetItemAssoc = query.iterate(); itemSetItemAssoc.hasNext();) 
        {
            ItemSetItemRecord itemSetItemRecord = (ItemSetItemRecord) itemSetItemAssoc.next();
            if ( itemSetItemRecord.getIbsInvisible().equals( "F" ))
            {
                if ( makeInvisible )
                {
                    itemSetItemRecord.setIbsInvisible( "T" );
                    session.update( itemSetItemRecord );
                }   
            }
            else if ( !makeInvisible )
            {
                itemSetItemRecord.setIbsInvisible( "F" );
                session.update( itemSetItemRecord );
            }        
        }
    }
}