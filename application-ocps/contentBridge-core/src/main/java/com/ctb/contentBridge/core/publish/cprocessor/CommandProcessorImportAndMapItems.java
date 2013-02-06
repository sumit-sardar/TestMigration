/*
 * Created on Dec 1, 2003
 *
 */
package com.ctb.contentBridge.core.publish.cprocessor;

import java.sql.Connection;

import org.apache.log4j.Logger;
import org.jdom.Element;

import com.ctb.contentBridge.core.exception.BusinessException;
import com.ctb.contentBridge.core.publish.hibernate.HibernateSession;
import com.ctb.contentBridge.core.publish.mapping.ItemMap;
import com.ctb.contentBridge.core.publish.mapping.Objectives;
import com.ctb.contentBridge.core.publish.report.AbstractItemReport;
import com.ctb.contentBridge.core.publish.report.ItemImportAndMapReport;
import com.ctb.contentBridge.core.publish.report.ItemProcessorReport;
import com.ctb.contentBridge.core.publish.report.ItemSetReport;
import com.ctb.contentBridge.core.publish.report.Report;
import com.ctb.contentBridge.core.publish.report.ReportFactory;
import com.ctb.contentBridge.core.publish.tools.OpenDeployConfig;
import com.ctb.contentBridge.core.publish.xml.item.ItemMediaGeneratorCaching;
import com.ctb.contentBridge.core.publish.xml.item.ItemMediaGeneratorFactory;
import com.ctb.contentBridge.core.publish.xml.item.ItemProcessor;
import com.ctb.contentBridge.core.publish.xml.item.ItemProcessorFactory;


public class CommandProcessorImportAndMapItems implements CommandProcessor {

    private static Logger logger =
        Logger.getLogger(CommandProcessorImportAndMapItems.class);

    private final ItemMediaGeneratorCaching cachingMediaGenerator;
    private final ItemMap[] itemMaps;
    private final ItemProcessor[] mapItemProcessors;
    private final ItemProcessor importItemProcessor;
    private final Element element;
    private final Connection targetConnection;

    public CommandProcessorImportAndMapItems(
        Element rootElement,
        String localImageArea,
        String targetImageArea,
        int validationMode,
        OpenDeployConfig config,
        Objectives[] objectivesArray,
        ItemMap[] itemMaps,
        Connection targetConnection) {
        this.element = rootElement;
        this.targetConnection = targetConnection;
        this.cachingMediaGenerator =
            ItemMediaGeneratorFactory.getItemMediaGeneratorCaching();
        this.importItemProcessor =
            ItemProcessorFactory.getItemProcessorImportItemsCacheMedia(
                localImageArea,
                targetImageArea,
                validationMode,
                config,
                this.cachingMediaGenerator,
                targetConnection);
        this.itemMaps = itemMaps;
        if (objectivesArray.length != itemMaps.length)
            throw new BusinessException("Number of Objectives and ItemMaps is not equal");
        mapItemProcessors = new ItemProcessor[objectivesArray.length];
        for (int i = 0; i < objectivesArray.length; i++) {
            mapItemProcessors[i] =
                ItemProcessorFactory.getItemProcessorMapOnImport(
                    localImageArea,
                    targetImageArea,
                    config,    
                    objectivesArray[i],
                    itemMaps[i],
                    this.cachingMediaGenerator,
                    validationMode,
                    targetConnection);
        }
    }

    /**
     * This constructor is used to simplify testing
     */
    CommandProcessorImportAndMapItems(
        Element rootElement,
        ItemProcessor importProcessor,
        ItemProcessor[] mapProcessors,
        Connection targetConnection) {
        this.element = rootElement;
        this.targetConnection = targetConnection;
        this.itemMaps = null;
        this.mapItemProcessors = mapProcessors;
        this.importItemProcessor = importProcessor;
        this.cachingMediaGenerator = null;
    }

    public Report process() {
        Element[] items = CommandProcessorUtility.getItems(this.element);
        return processItems(items);
    }

    private ItemSetReport processItems(Element[] items) {
        ItemSetReport report = ReportFactory.createItemSetReport(true);
        for (int i = 0; i < items.length; i++) {
            report.addSubReport(processItem(items[i]));
        }
        if (!report.isSuccess())
            report.setException(new Exception("Some Items failed"));
        return report;
    }

    protected AbstractItemReport processItem(Element itemElement) {
        ItemImportAndMapReport r = ReportFactory.getItemImportAndMapReport();
        String id = itemElement.getAttributeValue( "ID" );
 //       ItemProcessorReport iprImport =
 //           processItemElement(itemElement, this.importItemProcessor, r);
 //       if (iprImport.isSuccess()) {
            for (int i = 0; i < mapItemProcessors.length; i++) {
                if (this.itemMaps[i].curriculumId( id ) != null)
                    processItemElement(itemElement, mapItemProcessors[i], r);
            }
            this.cachingMediaGenerator.purgeCache();
//        }
        return r;
    }

    private ItemProcessorReport processItemElement(
        Element itemElement,
        ItemProcessor ip,
        ItemImportAndMapReport r) {
        try {
            ip.process(itemElement);
			HibernateSession.currentSession().flush();
            targetConnection.commit();
        } catch (Exception e) {
            CommandProcessorUtility.handleItemException(e, logger);
        } 
 //       finally 
        {
            ItemProcessorReport ipr = ItemProcessorReport.getCurrentReport();
            r.addItemProcessorReport(ipr);
            return ipr;
        }
    }
}
