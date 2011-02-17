package com.ctb.cprocessor;

import java.io.File;

import com.ctb.common.tools.SystemException;
import com.ctb.mapping.ItemMap;
import com.ctb.mapping.MapperFactory;
import com.ctb.mapping.Objectives;

public class CommandProcessorFactoryUtil {

    public static void loadFrameworkResources(
        File frameworkDefinitonFile,
        File objectivesFile,
        File mappingFile,
        String fileFormat,
        boolean allowNullMappingFile) {
        try {
            MapperFactory.loadFrameworkDefinitionFromFile(
                frameworkDefinitonFile);
            MapperFactory.loadObjectivesFromFile(
                objectivesFile,
                fileFormat,
                MapperFactory.getFrameworkInfo());
            if ((mappingFile == null) && (!allowNullMappingFile))
                throw new SystemException("Mapping File not specified");
            MapperFactory.loadItemMapFromFile(mappingFile);
        } catch (Exception e) {
            throw new SystemException(e.getMessage(), e);
        }
    }

    public static void loadFrameworkResources(
        File frameworkDefinitonFile,
        File objectivesFile,
        File mappingFile,
        String fileFormat) {
        loadFrameworkResources(
            frameworkDefinitonFile,
            objectivesFile,
            mappingFile,
            fileFormat,
            false);
    }

    public static void populateObjectiveAndItemMaps(
        String objFileFormat,
        String[] fwkCodes,
        String mappingDir,
        Objectives[] objectivesArray,
        ItemMap[] itemMapArray) {
        for (int i = 0; i < fwkCodes.length; i++) {
            loadFrameworkResources(
                getFwkFile(mappingDir, fwkCodes[i]),
                getObjFile(mappingDir, fwkCodes[i]),
                getMappingFile(mappingDir, fwkCodes[i]),
                objFileFormat);
            objectivesArray[i] = MapperFactory.getObjectives();
            itemMapArray[i] = MapperFactory.getItemMap();
        }
    }

    public static File getObjFile(String mappingDir, String frameworkCode) {
        return new File(mappingDir + "/" + frameworkCode + "/objectives.txt");
    }

    public static File getFwkFile(String mappingDir, String frameworkCode) {
        return new File(mappingDir + "/" + frameworkCode + "/levels.txt");
    }

    public static File getMappingFile(String mappingDir, String fwkCode) {
        return new File(mappingDir + "/" + fwkCode + "/item_map.txt");
    }

}
