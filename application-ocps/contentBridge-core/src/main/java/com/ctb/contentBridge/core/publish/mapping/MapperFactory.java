package com.ctb.contentBridge.core.publish.mapping;

import java.io.*;
import java.util.*;

import org.apache.log4j.*;

import com.ctb.contentBridge.core.exception.SystemException;

/**
 * Created by IntelliJ IDEA.
 * User: ghohpe
 * Date: Sep 5, 2003
 * Time: 1:59:36 PM
 * To change this template use Options | File Templates.
 */
public class MapperFactory {
    public static final String FILEFORMAT_SHORT = "short";
    public static final String FILEFORMAT_LONG = "long";
    private static Logger logger = Logger.getLogger(MapperFactory.class);
    private static ThreadLocal _currentFrameworkInfo = new ThreadLocal();
    private static ThreadLocal _currentObjectives = new ThreadLocal();
    private static ThreadLocal _currentItemMap = new ThreadLocal();

    public static Mapper newMapper(
        File frameworkDefinitonFile,
        File objectivesFile,
        File mappingFile,
        String objectivesFileFormat) {

        try {
            FrameworkInfo frameworkInfo =
                loadFrameworkDefinitionFromFile(frameworkDefinitonFile);

            Objectives objectives = null;
                /*loadObjectivesFromFile(
                    objectivesFile,
                    objectivesFileFormat,
                    frameworkInfo);*/

            ItemMap itemMap = loadItemMapFromFile(mappingFile);

            return newMapper(objectives, itemMap);
        } catch (IOException e) {
            throw new SystemException(e.getMessage());
        }
    }
    public static Mapper newMapper(Objectives objectives, ItemMap itemMap) {

        Mapper mapper = null;
        try {
            mapper = new Mapper(itemMap, objectives);
        } catch (Exception e) {
            throw new SystemException(e.getMessage());
        }
        Collection badItems = mapper.getMappedItemsNotInObjectives();

        if (!badItems.isEmpty()) {
            String s =
                "Objectives for these Items were not found in objectives file for ["
                    + objectives.getFrameworkCode()
                    + "] Framework";
            for (Iterator iter = badItems.iterator(); iter.hasNext();) {
                s += "\n  Item ID [" + (String) iter.next() + "]";
            }
            throw new SystemException(s);
        }
        return mapper;

    }

    public static String getDuplicateItemsList(ItemMap itemMap) {
        String s = "";
        for (Iterator iter = itemMap.getDuplicateItemIDs().iterator();
            iter.hasNext();
            ) {
            s += "\n  Item ID [" + (String) iter.next() + "]";
        }
        return s;
    }

    public static ItemMap loadItemMapFromFile(File mappingFile) {
        if (mappingFile == null) {
            throw new IllegalArgumentException("'mappingFile' parameter cannot be null");
        }

        logger.debug(
            "Loading Item Map from file [" + mappingFile.getPath() + "]");
        ItemMap itemMap = new ItemMap(mappingFile);
        if (!itemMap.getDuplicateItemIDs().isEmpty()) {
            String s =
                "Duplicate Item IDs in Item Map file: "
                    + getDuplicateItemsList(itemMap);
            throw new SystemException(s);
        }
        _currentItemMap.set(itemMap);
        return itemMap;

    }

    public static Objectives loadObjectivesFromFile(
        File objectivesFile,
        String objectivesFileFormat,
        FrameworkInfo frameworkInfo) {
        ObjectiveBuilder builder = null;

        if (objectivesFileFormat.equals(FILEFORMAT_SHORT)) {
            builder = new ShortFormatBuilder();
        } else if (objectivesFileFormat.equals(FILEFORMAT_LONG)) {
            builder = new LongFormatBuilder();
        } else {
            throw new SystemException(
                "Invalid Objectives file format specified: '"
                    + objectivesFileFormat
                    + "'. Has to be '"
                    + FILEFORMAT_SHORT
                    + "' or '"
                    + FILEFORMAT_LONG
                    + "'.");
        }
        return loadObjectivesFromFile(objectivesFile, frameworkInfo, builder);
    }

    public static Objectives loadObjectivesFromFile(
        File objectivesFile,
        FrameworkInfo frameworkInfo,
        ObjectiveBuilder builder) {
        if (objectivesFile == null) {
            throw new IllegalArgumentException("'objectivesFile' parameter cannot be null");
        }
        try {
            logger.debug(
                "Loading Objectives from file ["
                    + objectivesFile.getCanonicalPath()
                    + "]");
            Objectives objectives = new Objectives(frameworkInfo);
            ObjectivesLoader loader =
                new ObjectivesLoader(builder, new FileReader(objectivesFile));
            loader.load(objectives);

            _currentObjectives.set(objectives);
            return objectives;
        } catch (IOException e) {
        	e.printStackTrace();
            throw new SystemException(
                "Failed to load Objectives from ["
                    + objectivesFile.getPath()
                    + "]: "
                    + e.toString());
        }

    }

    public static FrameworkInfo loadFrameworkDefinitionFromFile(File frameworkDefinitonFile)
        throws IOException {
        if (frameworkDefinitonFile == null) {
            throw new IllegalArgumentException("'frameworkDefinitonFile' parameter cannot be null");
        }

        logger.debug(
            "Loading Framework Definition from file ["
                + frameworkDefinitonFile.getCanonicalPath()
                + "]");
        FrameworkInfo frameworkInfo = null;

        try {
            frameworkInfo = new FrameworkInfo(frameworkDefinitonFile);
            _currentFrameworkInfo.set(frameworkInfo);
            return frameworkInfo;
        } catch (Exception e) {
            throw new SystemException(
                "Failed to load Framework Definition from ["
                    + frameworkDefinitonFile.getCanonicalPath()
                    + "]: "
                    + e.toString());
        }

    }

    public static FrameworkInfo getFrameworkInfo() {
        return (FrameworkInfo) _currentFrameworkInfo.get();
    }

    public static Objectives getObjectives() {
        return (Objectives) _currentObjectives.get();
    }

    public static ItemMap getItemMap() {
        return (ItemMap) _currentItemMap.get();
    }
}
