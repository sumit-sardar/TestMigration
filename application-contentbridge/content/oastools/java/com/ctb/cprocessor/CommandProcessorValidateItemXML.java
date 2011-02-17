package com.ctb.cprocessor;

import com.ctb.cvm.oas.MediaType;
import com.ctb.hibernate.HibernateSession;
import com.ctb.hibernate.persist.ItemMediaRecord;
import com.ctb.hibernate.persist.ItemRecord;
import com.ctb.reporting.CommandReportFormatter;
import com.ctb.reporting.Report;
import com.ctb.reporting.ValidateItemXMLReport;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import org.apache.log4j.Logger;
import org.jdom.JDOMException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: JEMarley
 * Date: May 17, 2004
 */
public class CommandProcessorValidateItemXML implements CommandProcessor {
    private Session session;
    private static Logger logger =
            Logger.getLogger(CommandReportFormatter.class);
    private static final String SELECT_ALL_XML_MEDIA = "from ItemMediaRecord as item where item.mimeType=:mimeType and item.id.itemId in (:itemList)";
    private int maxSize = Integer.MAX_VALUE;

    public Report process() {
        session = HibernateSession.currentSession();
        ValidateItemXMLReport report = new ValidateItemXMLReport();
        List itemKeysList = null;
        try {
            Query query = session.createQuery("select item.itemId from " + ItemRecord.class.getName() + " as item where item.itemId = item.itemDisplayName");
            query.setMaxResults(maxSize);
            itemKeysList = new ArrayList(query.list());
        }
        catch (HibernateException e) {
            logger.error("hibernate issues", e); //TODO handle better
        }

        List arrayOfKeys = new ArrayList(itemKeysList);
        for (int i = 0; i < arrayOfKeys.size(); i += 100) {
                List itemRecordsList = null;
            try {
                itemRecordsList = getItemMediaRecordsForItemKeys(arrayOfKeys.subList(i, Math.min(i + 100, arrayOfKeys.size())));
                processRecordList(itemRecordsList, report);
            }
            catch (Exception e) {
                logger.error(e);
                report.addException(e);
                return report;
            }
            session.clear();
        }
        return report;
    }

    private void processRecordList(List itemMediaRecords, ValidateItemXMLReport report){
        for (int i = 0; i < itemMediaRecords.size(); i++) {
            ItemMediaRecord itemMediaRecord = (ItemMediaRecord) itemMediaRecords.get(i);
            try {
                itemMediaRecord.validateXML();
            }
            catch (JDOMException e) {
                report.addItemValidationFailure(itemMediaRecord.getId().getItemId(), e.getMessage(), e);
            }
            catch (IOException e) {
                report.addException(e);
            }
        }
    }

    private List getItemMediaRecordsForItemKeys(List itemKeys) throws HibernateException {
        Query query = session.createQuery(SELECT_ALL_XML_MEDIA);
        query.setParameter("mimeType", MediaType.XML_MEDIA_TYPE.getMineType());
        query.setParameterList("itemList", itemKeys);
        return query.list();
    }

    public void setRecordMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }
}
