package com.ctb.contentBridge.core.publish.report;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.ctb.contentBridge.core.publish.mapping.FrameworkInfo;
import com.ctb.contentBridge.core.publish.mapping.Objective;
import com.ctb.contentBridge.core.publish.mapping.ObjectiveVisitor;
import com.ctb.contentBridge.core.publish.mapping.Objectives;


class HierarchyReportVisitorFormatter implements Formatter, ObjectiveVisitor {
    private static Logger logger =
        Logger.getLogger(HierarchyReportVisitorFormatter.class);

    private static final String INDENT = "  ";
    private static final String UNKNOWN_OBJECTIVE_ID = "99999";

    private HierarchyReport hierarchyReport;
    private Objectives objectives;
    private FrameworkInfo frameworkInfo;
    private PrintWriter writer;

    private Map objectiveItemReportsMap;

    HierarchyReportVisitorFormatter(
        HierarchyReport hierarchyReport,
        boolean fullReport) {
        this.hierarchyReport = hierarchyReport;
        this.frameworkInfo = hierarchyReport.getObjectives().getFrameworkInfo();

        if (fullReport)
            objectives = hierarchyReport.getObjectives();
        else
            objectives = getMappedObjectives();

        if (hierarchyReport.getItemMap() != null)
            objectiveItemReportsMap = buildObjectiveItemReportMap();
        else
            objectiveItemReportsMap = new HashMap();
    }

    public void print(PrintWriter writer, boolean isSubReport) {
        this.writer = writer;

        writer.println("*** Projected Mapping Hierarchy ***");

        try {
            objectives.traverse(this);
        } catch (Exception e) {
            writer.println(e.getMessage());
        }

        // print the list of 
        List unknownMappingReport =
            (ArrayList) objectiveItemReportsMap.get(UNKNOWN_OBJECTIVE_ID);
        if (unknownMappingReport != null) {
            writer.println(
                "\n\nThe following Items have no mapping information in item_map.txt:\n");
            printItemReport(unknownMappingReport, 1);
        }

        List failedItemsReport = getFailedItemReports();
        if (!failedItemsReport.isEmpty()) {
            writer.println("\n\nThe following Items could not be mapped:\n");
            printItemReport(failedItemsReport, 1);
        }
    }

    public void visitObjective(Objective objective) {

        // write the objectives
        writer.println(
            StringUtils.repeat(INDENT, getIndent(objective))
                + frameworkInfo.getLevelName(objective.getLevel().intValue())
                + " : "
                + objective.getCurriculumID()
                + " - "
                + objective.getName()
                + getActivationStatusString(objective));

        // write the item processor report
        List objectiveItemReports =
            (ArrayList) objectiveItemReportsMap.get(
                objective.getCurriculumID());

        if (objectiveItemReports != null) {
            printItemReport(objectiveItemReports, getIndent(objective) + 1);
        }

    }

    private String getActivationStatusString(Objective objective) {
        String s = objective.getActivationStatus();
        if ((s != null) && (s.length() != 0))
            return " Activation Status [" + s + "]";
        else
            return "";
    }

    private int getIndent(Objective objective) {
        return objective.getLevel().intValue() - frameworkInfo.getRootLevel();
    }

    private void printItemReport(List objectiveItemReports, int indent) {
        int i = 0;
        for (Iterator itemReportIter = objectiveItemReports.iterator();
            itemReportIter.hasNext();
            ) {

            writer.print(
                StringUtils.repeat(INDENT, indent)
                    + StringUtils.leftPad("" + ++i + ".", 5));
            FormatterFactory.create(
                (AbstractItemReport) itemReportIter.next()).print(
                writer,
                true);
        }
    }

    private Objectives getMappedObjectives() {
        if (hierarchyReport.getItemMap() == null)
            return hierarchyReport.getObjectives();

        return hierarchyReport.getObjectives().extractObjectives(
            getMappedObjectiveIds());
    }

    private List getMappedObjectiveIds() {
        List objectiveIds = new ArrayList();

        for (Iterator itemReportIter = getSuccessItemReports().iterator();
            itemReportIter.hasNext();
            ) {
            AbstractItemReport itemReport =
                (AbstractItemReport) itemReportIter.next();

            // find the objective id for the successful mapping
            if (itemReport.isSuccess()) {
                String objectiveId =
                    hierarchyReport.getItemMap().curriculumId(
                        itemReport.getID());
                objectiveIds.add(objectiveId);
            }
        }

        return objectiveIds;
    }

    private List getSuccessItemReports() {
        return getItemReports(hierarchyReport, true);
    }

    private List getFailedItemReports() {
        return getItemReports(hierarchyReport, false);
    }

    private List getItemReports(
        HierarchyReport hierarchyReport,
        boolean status) {
        List reports = new ArrayList();

        for (Iterator subReportIter =
            hierarchyReport.getSubReports().iterator();
            subReportIter.hasNext();
            ) {
            AbstractReport report = (AbstractReport) subReportIter.next();

            if (report instanceof AbstractItemReport) {
                if (report.isSuccess() == status)
                    reports.add(report);
            } else if (report instanceof ItemSetReport) {
                for (Iterator iter =
                    ((ItemSetReport) report).getSubReports();
                    iter.hasNext();
                    ) {
                    AbstractItemReport itemReport =
                        (AbstractItemReport) iter.next();

                    if (itemReport.isSuccess() == status)
                        reports.add(itemReport);
                }
            }
        } // end for

        return reports;
    }

    private Map buildObjectiveItemReportMap() {
        Map objectiveItemReportMap = new HashMap();

        for (Iterator iter = getSuccessItemReports().iterator();
            iter.hasNext();
            ) {
            AbstractItemReport itemReport = (AbstractItemReport) iter.next();

            // get the curriculumId the the item mapped to 
            String curriculumId =
                hierarchyReport.getItemMap().curriculumId(itemReport.getID());

            if (curriculumId == null) {
                logger.info(
                    "Cannot find objective for item ["
                        + itemReport.getID()
                        + "]");
                curriculumId = UNKNOWN_OBJECTIVE_ID;
            }

            if (objectiveItemReportMap.get(curriculumId) == null) {
                objectiveItemReportMap.put(curriculumId, new ArrayList());
            }

            ((ArrayList) objectiveItemReportMap.get(curriculumId)).add(
                itemReport);
        }

        return objectiveItemReportMap;
    }
}
