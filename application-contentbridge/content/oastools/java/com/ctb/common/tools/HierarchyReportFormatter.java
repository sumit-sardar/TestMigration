package com.ctb.common.tools;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.ctb.mapping.FrameworkInfo;
import com.ctb.mapping.ItemMap;
import com.ctb.mapping.Mapper;
import com.ctb.mapping.MapperFactory;
import com.ctb.mapping.Objective;
import com.ctb.mapping.Objectives;
import com.ctb.mapping.ProjectedMappingHierarchyReportFormatter;

public class HierarchyReportFormatter {

    private static Logger logger =
        Logger.getLogger(HierarchyReportFormatter.class);

    public void generateMappingHierarchyForSuccessfullyMappedItems(
        FrameworkInfo frameworkInfo,
        Objectives objectives,
        ItemMap itemMap,
        Iterator successList,
        ProjectedMappingHierarchyReportFormatter formatter,
        Writer outWriter) {

        try {
            ItemMap successMapping =
                constructItemMapFromMappingDataAndList(itemMap, successList);
            if (successMapping.isEmpty())
                return;

            Mapper mapper =
                new Mapper(
                    successMapping,
                    getMappedObjectives(
                        frameworkInfo,
                        objectives,
                        successMapping));

            ReportWriter writer = new ReportWriterPlainText();

            formatter.setMapper(mapper);
            formatter.generateProjectedMappingReport(writer);

            outWriter.write(writer.getResult());
            outWriter.flush();

        } catch (Exception e) {
            throw new SystemException(e.getMessage(), e);
        }
    }

    private ItemMap constructItemMapFromMappingDataAndList(
        ItemMap itemMap,
        Iterator ids) {
        ItemMap map = new ItemMap();

        while (ids.hasNext()) {
            String id = (String) ids.next();
            map.put(id, itemMap.curriculumId(id));
        }

        return map;
    }

    public String generateMappingHierarchy(
        File frameworkDefinitonFile,
        File objectivesFile,
        File mappingFile,
        String fileFormat,
        DBConnection conn)
        throws IOException {
        try {
            Mapper mapper =
                MapperFactory.newMapper(
                    frameworkDefinitonFile,
                    objectivesFile,
                    mappingFile,
                    fileFormat);
            Collection badItems = mapper.getMappedItemsNotInObjectives();

            if (!badItems.isEmpty()) {
                String s =
                    "Objectives for these Items were not found in objectives file ["
                        + objectivesFile.getCanonicalPath()
                        + "]:";
                for (Iterator iter = badItems.iterator(); iter.hasNext();) {
                    s += "\n  Item ID [" + (String) iter.next() + "]";
                }
                logger.error(s);
            }

            ReportWriter writer = new ReportWriterPlainText();
            ProjectedMappingHierarchyReportFormatter formatter =
                new ProjectedMappingHierarchyReportFormatter(mapper, conn);
            formatter.generateProjectedMappingReport(writer);

            String s = writer.getResult();
            logger.info(s);
            return s;

        } catch (Exception e) {
            logger.error("An error occured", e);
            throw new SystemException(e.getMessage(), e);
        }
    }

    public Objectives getMappedObjectives(
        FrameworkInfo frameworkInfo,
        Objectives objectives,
        ItemMap itemMap) {

        // building a small subset of objective for the mapped items for the report purpose
        Objectives mappedObjectives = new Objectives(frameworkInfo);

        for (Iterator mappedObjectiveIdIter = itemMap.getAllCurriculumIDs();
            mappedObjectiveIdIter.hasNext();
            ) {
            String objectiveId = (String) mappedObjectiveIdIter.next();

            List hierarchy = objectives.getHierarchyObjectiveList(objectiveId);
            for (Iterator objectiveIter = hierarchy.iterator();
                objectiveIter.hasNext();
                ) {
                Objective objective = (Objective) objectiveIter.next();

                // add the objective if the objective is not in the mapped objectives collection
                if (mappedObjectives
                    .objectiveFromCurriculumId(objective.getCurriculumID())
                    == null) {
                    mappedObjectives.add(objective);
                }
            }
        }

        return mappedObjectives;
    }
}
