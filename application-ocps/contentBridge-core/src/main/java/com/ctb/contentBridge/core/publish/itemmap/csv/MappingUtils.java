package com.ctb.contentBridge.core.publish.itemmap.csv;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.ctb.contentBridge.core.publish.mapping.FrameworkInfo;
import com.ctb.contentBridge.core.publish.mapping.ItemMap;
import com.ctb.contentBridge.core.publish.mapping.MapperFactory;
import com.ctb.contentBridge.core.publish.mapping.Objectives;
import com.ctb.contentBridge.core.util.RegexUtils;


/**
 * @author wmli
 */
public class MappingUtils {
    public static final String COLUMN_TITLE =
        "Item #,Answer Key,Item Id,Objective Id,<HIERARCHY>,Notes";

    public static int getDisplayLevelForColumnTitle(String columnTitle) {
        FrameworkInfo frameworkInfo = MapperFactory.getFrameworkInfo();

        String diff = StringUtils.difference(COLUMN_TITLE, columnTitle);

        // top level column title 
        String column = StringUtils.split(diff, ",")[0];

        // find the corresponding level for the column
        int level = frameworkInfo.getRootLevel();
        String levelName = null;

        while ((levelName = frameworkInfo.getLevelName(level)) != null) {
            if (levelName.equals(column))
                return level;
            level++;
        }

        return frameworkInfo.getRootLevel();
    }

    public static ItemMap createItemMapFromMappingEntries(
        Collection mappingEntries,
        Objectives objectives) {

        ItemMap itemMap = new ItemMap();

        for (Iterator iter = mappingEntries.iterator(); iter.hasNext();) {
            MappingEntry entry = (MappingEntry) iter.next();
            if (objectives.objectiveFromCurriculumId(entry.getObjectiveId())
                != null) {
                itemMap.add(
                    new ItemMap.Entry(
                        entry.getItemId(),
                        entry.getObjectiveId()));
            }
        }

        return itemMap;
    }

    public static List getValuesForCommaDelimitedList(String line) {
        List entry =
            RegexUtils.getAllMatchedGroups(
                "([^\",]*,?|\"[^\"]*\",?)",
                line,
                ",");

        return entry;
    }

    public static List compareValuesList(List source, List target) {
    	List result = new ArrayList();
        int numberOfValues = Math.max(source.size(), target.size());

        for (int i = 0; i < numberOfValues; i++) {
            String srcValue = getValueFromList(source, i);
            String tgtValue = getValueFromList(target, i);

            if (!srcValue.equals(tgtValue)) {
            	result.add("Column[" +
            		(i + 1) + 
            		"]: [" +
            		srcValue +
            		"] -> [" +
            		tgtValue +
            		"]");
            }
        }
        
        return result;
    }

    private static String getValueFromList(List list, int idx) {
        try {
            return StringUtils.replace((String) list.get(idx), "\"", "");
        } catch (IndexOutOfBoundsException e) {
            return "";
        }
    }
}
