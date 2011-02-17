package com.ctb.reporting;

import com.ctb.mapping.ObjectiveVisitor;
import com.ctb.mapping.Objective;
import com.ctb.mapping.Objectives;
import com.ctb.mapping.FrameworkInfo;

import java.io.PrintWriter;
import java.util.*;

import org.apache.log4j.Logger;





/**
 * Created by IntelliJ IDEA.
 * User: JEMarley
 * Date: Mar 30, 2004
 * Time: 1:56:05 PM
 * To change this template use Options | File Templates.
 */
public class FlattenedHierarchyReportVisitorFormatter implements Formatter, ObjectiveVisitor {
    private static Logger logger =
        Logger.getLogger(HierarchyReportVisitorFormatter.class);

    private static final String UNKNOWN_OBJECTIVE_ID = "99999";
    private static final String DELIMINATOR = " | ";

    private Objectives objectives;
    private PrintWriter writer;
    private Map objectiveItemReportsMap;
    private HierarchyReport hierarchyReport;
     private FrameworkInfo frameworkInfo;


    FlattenedHierarchyReportVisitorFormatter(HierarchyReport hierarchyReport, boolean fullReport){
        this.hierarchyReport = hierarchyReport;
         this.frameworkInfo = hierarchyReport.getObjectives().getFrameworkInfo();

        if (fullReport)   {
            objectives = hierarchyReport.getObjectives();
        }
        else
            objectives = getMappedObjectives();

        if (hierarchyReport.getItemMap() != null)
            objectiveItemReportsMap = buildObjectiveItemReportMap();
        else
            objectiveItemReportsMap = new HashMap();
    }


    public void print(PrintWriter writer, boolean isSubReport){
        this.writer = writer;

        writer.println("*** Flattened Projected Mapping Hierarchy ***");

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
            printItemReport(unknownMappingReport);
        }

        List failedItemsReport = getFailedItemReports();
        if (!failedItemsReport.isEmpty()) {
            writer.println("\n\nThe following Items could not be mapped:\n");
            printItemReport(failedItemsReport);
        }
    }

   public void visitObjective(Objective objective) {

       // write the item processor report
       List objectiveItemReports =
               (ArrayList) objectiveItemReportsMap.get(
                       objective.getCurriculumID());

       if (objectiveItemReports != null) {
           String currentObjectiveValue = buildCurrentObjectiveValue(objective);
           printItemReport(currentObjectiveValue, objectiveItemReports);
       }
   }

    private String buildCurrentObjectiveValue(Objective objective) {
        List objectiveList = new ArrayList();
        Objective currentObjective = objective;
        objectiveList.add(currentObjective);
        while(currentObjective.hasParent()) {
            currentObjective = objectives.getParent(currentObjective);
            objectiveList.add(currentObjective);
        }
        Objective[] objectiveArray = (Objective[])objectiveList.toArray(new Objective[objectiveList.size()]);
        StringBuffer buf = new StringBuffer();
        buf.append(DELIMINATOR);
        for (int i = (objectiveArray.length - 1); i >= 0; i--) {
            buf.append(objectiveArray[i].getName());
            if (i != 0)
                buf.append(DELIMINATOR);
        }
        return buf.toString();
    }

    private void printItemReport(String currentObjectiveValue, List objectiveItemReports) {

        for (Iterator itemReportIter = objectiveItemReports.iterator();
            itemReportIter.hasNext();
            ) {
                AbstractItemReport itemReport = (AbstractItemReport) itemReportIter.next();
                writer.println(itemReport.getID() + "\t" + currentObjectiveValue);
        }
    }

    private void printItemReport(List objectiveItemReports) {

        for (Iterator itemReportIter = objectiveItemReports.iterator();
            itemReportIter.hasNext();
            ) {

                AbstractItemReport itemReport = (AbstractItemReport) itemReportIter.next();
                writer.println(itemReport.getID() + "\t" + (itemReport.isSuccess()?"Succeeded":"Failed"));
        }
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
