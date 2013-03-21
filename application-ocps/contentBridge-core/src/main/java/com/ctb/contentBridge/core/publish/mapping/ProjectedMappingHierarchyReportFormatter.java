/*
 * Created on Nov 6, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ctb.contentBridge.core.publish.mapping;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.ctb.contentBridge.core.publish.dao.DBConnection;
import com.ctb.contentBridge.core.publish.dao.DBItemGateway;
import com.ctb.contentBridge.core.publish.hibernate.HibernateUtils;
import com.ctb.contentBridge.core.publish.tools.ReportItem;
import com.ctb.contentBridge.core.publish.tools.ReportObjective;
import com.ctb.contentBridge.core.publish.tools.ReportWriter;


/**
 * @author wmli
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ProjectedMappingHierarchyReportFormatter {
    Mapper mapper;
    DBConnection dbconnection;
    String header = "Projected mapping Hierarchy";

    public ProjectedMappingHierarchyReportFormatter() {
    }

    public ProjectedMappingHierarchyReportFormatter(
        Mapper mapper,
        DBConnection connection) {
        this.mapper = mapper;
        this.dbconnection = connection;

    }

    public ProjectedMappingHierarchyReportFormatter(DBConnection connection) {
        this.dbconnection = connection;

    }

    public void generateProjectedMappingReport(ReportWriter writer) {
        StringBuffer buffer = new StringBuffer(1000);

        writer.writeHeader(header);
        Objective rootObjective = mapper.getObjectives().getRootObjective();
        Map statusCounts = new HashMap();

        writeObjective(writer, rootObjective, 0, statusCounts);

        String footer = "";

        // include status counts in footer if they were calculated
        if (statusCounts.size() > 0) {
            for (Iterator iterator = statusCounts.keySet().iterator();
                iterator.hasNext();
                ) {
                String status = (String) iterator.next();

                footer += (status + "\t" + statusCounts.get(status) + "\n");
            }
        }

        writer.writeFooter(footer);
    }

    private void writeObjective(
        ReportWriter writer,
        Objective objective,
        int level,
        Map statusCounts) {
        ReportObjective ro =
            new ReportObjective(
                null,
                objective.getName(),
                objective.getCurriculumID(),
                objective.getLevel(),
                "");

        writer.writeObjective(
            ro,
            mapper.getObjectives().getFrameworkInfo().getLevelNames(),
            level);

        // list all items that will be mapped to this objective
        writeMappedItem(writer, objective, level, statusCounts);

        // continue with the child objectives
        Collection children = mapper.getObjectives().getChildren(objective);

        for (Iterator iter = children.iterator(); iter.hasNext();) {
            Objective child = (Objective) iter.next();

            writeObjective(writer, child, level + 1, statusCounts);
        }
    }

    private void writeMappedItem(
        ReportWriter writer,
        Objective objective,
        int level,
        Map statusCounts) {
        for (Iterator iter = mapper.getItemMap().getAllItemIDs();
            iter.hasNext();
            ) {
            String itemId = (String) iter.next();

            if (mapper
                .curriculumId(itemId)
                .equals(objective.getCurriculumID())) {
                String itemStatus = "";
                String extraInfo = "";
                String mappedItemId = mapper.mappedItemId(itemId);

                if (dbconnection != null) {
                    itemStatus = getItemStatus(mappedItemId);
                    Integer count = (Integer) statusCounts.get(itemStatus);

                    if (count == null) {
                        count = new Integer(0);
                    }

                    statusCounts.put(
                        itemStatus,
                        new Integer(count.intValue() + 1));

                    extraInfo = getExtraInfo(dbconnection, mappedItemId);
                }

                ReportItem ri =
                    new ReportItem(
                        itemId,
                        "(" + mappedItemId + ")",
                        itemStatus,
                        extraInfo);

                writer.writeItem(ri, level + 1);
            }
        }
    }

    /**
     * Override to add extra info display for mapped items.
     * @param dbconnection
     * @param mappedItemId
     * @return Additional string that append to the end of the item
     */
    protected String getExtraInfo(
        DBConnection dbconnection,
        String mappedItemId) {
        return "";
    }

    private String getItemStatus(String itemId) {
        DBItemGateway igw =
            new DBItemGateway(
                HibernateUtils.getSession(dbconnection.getConnection()));
        String status = igw.getItem(itemId).getActivationStatus();

        return (status != null) ? status : "Unknown";
    }

    public Mapper getMapper() {
        return mapper;
    }

    public void setMapper(Mapper mapper) {
        this.mapper = mapper;
    }

}
