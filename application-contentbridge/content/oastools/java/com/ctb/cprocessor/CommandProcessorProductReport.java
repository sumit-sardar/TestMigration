package com.ctb.cprocessor;

import java.sql.Connection;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.ctb.common.tools.DBItemInfoGateway;
import com.ctb.common.tools.ItemInfo;
import com.ctb.cprocessor.decorator.*;
import com.ctb.xmlProcessing.item.*;
import com.ctb.mapping.ItemMap;
import com.ctb.mapping.MapperFactory;
import com.ctb.reporting.*;

public class CommandProcessorProductReport implements CommandProcessor {
    private static Logger logger =
        Logger.getLogger(CommandProcessorProductReport.class);

    private final String mappingFileName;
    private final ItemProcessor itemProcessor;
    private final Connection connection;

    public CommandProcessorProductReport(
        String mappingFileName,
        Connection connection) {
        this.connection = connection;
        this.itemProcessor =
            ItemProcessorFactory.getItemProcessorGenerateMappingHierarchy();
        this.mappingFileName = mappingFileName;
    }

    public Report process() {
        ProductReport report = new ProductReport();

        String fwkCode = MapperFactory.getFrameworkInfo().getFrameworkCode();
        report.setHierarchyReportForDBItems(
            getDBHierarchyReport(report, fwkCode));

        // The itemMap resource now has mapping file Items that are not yet in the DB
        // These can potentially be mapped in, or imported (if they belong to the Product) 
        report.setPotentialDBItemsReport(
            roundTripMapValidate(MapperFactory.getItemMap()));

        report.setSuccess(true);
        return report;
    }

    private HierarchyReport getDBHierarchyReport(
        ProductReport productReport,
        String fwkCode) {
        ItemMap allItems = new ItemMap();
        allItems.addAll(MapperFactory.getItemMap());

        ItemSetReport dBItemsReport =
            getDBItemSetReport(
                productReport,
                getDBItemInfos(fwkCode),
                allItems);

        // Although 'allItems' now has all mapping file Items + DB Items
        // with the dbItemsReport, 'dBHierarchyReport' will report only the DB Items        
        HierarchyReport dBHierarchyReport =
            new HierarchyReport(MapperFactory.getObjectives(), allItems);
        dBHierarchyReport.addSubReport(dBItemsReport);

        return dBHierarchyReport;
    }

    private List getDBItemInfos(String fwkCode) {
        List itemInfos =
            Arrays.asList(
                new DBItemInfoGateway(connection).findItemInfoByFrameworkCode(
                    fwkCode));
        return itemInfos;
    }

    private ItemSetReport getDBItemSetReport(
        ProductReport productReport,
        List itemInfos,
        ItemMap allItems) {
        int inactiveCount = 0;
        int noAnswerCount = 0;
        int stateSpecificCount = 0;
        int invisCount = 0;
        int totalCount = 0;

        ItemSetReport dBItemsReport = ReportFactory.createItemSetReport(true);

        for (Iterator iter = itemInfos.iterator(); iter.hasNext();) {
            ItemProcessorReport ipr = new ItemProcessorReport();
            dBItemsReport.addSubReport(ipr);

            // Populate ItemProcessorReport with ItemInfo. Gather statistics.
            ItemInfo itemInfo = (ItemInfo) iter.next();
            inactiveCount += setReportActivationStatus(ipr, itemInfo);
            stateSpecificCount += setReportItemIDs(ipr, itemInfo);
            ipr.setOperation("OAS database lookup");
            ipr.setSuccess(true);
            invisCount += setReportInvisibleStatus(ipr, itemInfo);
            noAnswerCount += setReportAnswerKey(ipr, itemInfo);
            totalCount++;

            // Accumulate DB Items into 'allItems'
            allItems.put(itemInfo.getDisplayID(), itemInfo.getObjectiveID());
            //remove DB Items from itemMap resource
            MapperFactory.getItemMap().remove(itemInfo.getDisplayID());
        }

        productReport.setInactiveCount(inactiveCount);
        productReport.setNoAnswerCount(noAnswerCount);
        productReport.setStateSpecificCount(stateSpecificCount);
        productReport.setInvisibleCount(invisCount);
        productReport.setTotalCount(totalCount);
        productReport.setPotentialDBItems(MapperFactory.getItemMap().size());
        return dBItemsReport;
    }

    private int setReportAnswerKey(
        ItemProcessorReport ipr,
        ItemInfo itemInfo) {
        ipr.setAnswerKey(itemInfo.getCorrectAnswer());
        if (itemInfo.getCorrectAnswer() == null
            || itemInfo.getCorrectAnswer().equalsIgnoreCase("null")
            || itemInfo.getCorrectAnswer().equals(""))
            return 1;
        return 0;
    }

    private int setReportInvisibleStatus(
        ItemProcessorReport ipr,
        ItemInfo itemInfo) {
        ipr.setInvisibleStatus(itemInfo.getInvisibleStatus());
        if (itemInfo.getInvisibleStatus().equalsIgnoreCase("T"))
            return 1;
        return 0;
    }

    private int setReportItemIDs(ItemProcessorReport ipr, ItemInfo itemInfo) {
        ipr.setID(itemInfo.getDisplayID());
        ipr.setNewID(itemInfo.getItemID());
        if (itemInfo.getDisplayID().equals(itemInfo.getItemID()))
            //imported, not mapped
            return 1;
        return 0;
    }

    private int setReportActivationStatus(
        ItemProcessorReport ipr,
        ItemInfo itemInfo) {
        ipr.setActivationStatus(itemInfo.getActivationStatus());
        if (!itemInfo.getActivationStatus().equalsIgnoreCase("AC"))
            return 1;
        return 0;

    }

    private Report roundTripMapValidate(ItemMap potentialItemsMap) {
        CommandProcessor cp =
            new CommandProcessorReportingDecorator(
                "round-trip map validate",
                mappingFileName,
                new CommandProcessorRoundTrip(
                    potentialItemsMap,
                    getRoundTripItemProcessor(potentialItemsMap),
                    connection));
        return cp.process();
    }

    private ItemProcessor getRoundTripItemProcessor(ItemMap leftOverMap) {
        return ItemProcessorFactory.getItemProcessorRoundTripValidate(
            MapperFactory.getObjectives(),
            leftOverMap,
            connection,
            connection);
    }

}
