/*
 * Created on Dec 10, 2003
 *
 */
package com.ctb.contentBridge.core.publish.command;

import java.io.File;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.jdom.Element;

import com.ctb.contentBridge.core.exception.SystemException;
import com.ctb.contentBridge.core.publish.cprocessor.CommandProcessorFactoryUtil;
import com.ctb.contentBridge.core.publish.cprocessor.CommandProcessorUtility;
import com.ctb.contentBridge.core.publish.itemmap.csv.MappingProcessor;
import com.ctb.contentBridge.core.publish.itemmap.csv.MappingProcessorFactory;
import com.ctb.contentBridge.core.publish.itemmap.csv.MappingUtils;
import com.ctb.contentBridge.core.publish.itemmap.csv.MappingWriter;
import com.ctb.contentBridge.core.publish.itemmap.csv.MappingWriterDummy;
import com.ctb.contentBridge.core.publish.itemmap.csv.MappingWriterImpl;
import com.ctb.contentBridge.core.publish.itemmap.csv.MappingWriterMerge;
import com.ctb.contentBridge.core.publish.mapping.ItemMap;
import com.ctb.contentBridge.core.publish.mapping.MapperFactory;
import com.ctb.contentBridge.core.publish.mapping.Objectives;
import com.ctb.contentBridge.core.publish.tools.OpenDeployConfig;
import com.ctb.contentBridge.core.publish.xml.XMLUtils;
import com.ctb.contentBridge.core.publish.xml.item.Item;
import com.ctb.contentBridge.core.publish.xml.item.ItemAssembler;
import com.ctb.contentBridge.core.publish.xml.item.ItemBuilder;
import com.ctb.contentBridge.core.publish.xml.item.ItemBuilderFactory;
import com.ctb.contentBridge.core.publish.xml.item.ItemProcessor;
import com.ctb.contentBridge.core.publish.xml.item.ItemProcessorFactory;


public class CIMCommandProcessorUtil {

    protected static ItemProcessor getItemProcessor(
        AbstractCIMCommandItemsFromFile command,
        Connection targetConnection) {

        if (command instanceof CIMCommandImportItems) {
            CIMCommandImportItems cmd = (CIMCommandImportItems) command;
            return ItemProcessorFactory.getItemProcessorImportItems(
                cmd.getLocalImageArea(),
                cmd.getImageArea(),
                cmd.getValidationMode(),
                new OpenDeployConfig(new File(cmd.getTargetEnvironment())),
                targetConnection);
        } else if (command instanceof CIMCommandImportItemsValidate) {
            CIMCommandImportItemsValidate cmd =
                (CIMCommandImportItemsValidate) command;
            return ItemProcessorFactory.getItemProcessorImportItemsValidate(
                cmd.getValidationMode(),
                targetConnection);
        } else if (command instanceof CIMCommandImportItemsNoMedia) {
            CIMCommandImportItemsNoMedia cmd =
                (CIMCommandImportItemsNoMedia) command;
            return ItemProcessorFactory.getItemProcessorImportItemsNoMedia(
                cmd.getValidationMode(),
                targetConnection);
        }

        if (command instanceof CIMCommandMapItemsValidate) {
            CIMCommandMapItemsValidate cmd_mapvalidate =
                    (CIMCommandMapItemsValidate) command;
            CommandProcessorFactoryUtil.loadFrameworkResources(
                    cmd_mapvalidate.getFrameworkFile(),
                    cmd_mapvalidate.getObjectivesFile(),
                    cmd_mapvalidate.getMappingFile(),
                    cmd_mapvalidate.getObjectivesFileFormat());
            if (command instanceof CIMCommandMapItems) {
                CIMCommandMapItems cmd_map = (CIMCommandMapItems) command;
                return ItemProcessorFactory.getItemProcessorMapItems(
                        cmd_map.getLocalImageArea(),
                        cmd_map.getImageArea(),
                        cmd_map.getValidationMode(),
                        new OpenDeployConfig(
                                new File(cmd_map.getTargetEnvironment())),
                        MapperFactory.getObjectives(),
                        MapperFactory.getItemMap(),
                        targetConnection);
            }
            return ItemProcessorFactory.getItemProcessorMapItemsValidate(
                    cmd_mapvalidate.getValidationMode(),
                    MapperFactory.getObjectives(),
                    MapperFactory.getItemMap(),
                    targetConnection);
        } else
            throw new SystemException(
                    "Don't have Item Processor for command: "
                    + command.getCommandName());
    }

    public static ItemProcessor getItemProcessor(
        AbstractCIMCommandRoundTrip command,
        Connection targetConnection) {
        Connection sourceConnection =
            CIMCommandProcessor.buildConnectionFromEnv(
                command.getSourceEnvironment());

        if (command instanceof CIMCommandRoundTripMap) {
            return ItemProcessorFactory.getItemProcessorRoundTripMap(
                MapperFactory.getObjectives(),
                MapperFactory.getItemMap(),
                sourceConnection,
                targetConnection);
        } else if (command instanceof CIMCommandRoundTripValidate) {
            return ItemProcessorFactory.getItemProcessorRoundTripValidate(
                MapperFactory.getObjectives(),
                MapperFactory.getItemMap(),
                sourceConnection,
                targetConnection);
        } else
            throw new SystemException(
                "Don't have Item Processor for command: "
                    + command.getCommandName());
    }

    public static MappingProcessor getMappingProcessor(
        AbstractCIMCommandTestMap cmd,
        Objectives objectives,
        ItemMap itemMap) {
        if (cmd instanceof CIMCommandTestMapMerge) {
            return MappingProcessorFactory
                .createMappingProcessorForTestMapMerge(
                objectives,
                itemMap);
        }
        if (cmd instanceof CIMCommandTestMapMergeValidate) {
            return MappingProcessorFactory
                .createMappingProcessorForTestMapMergeValidation(
                objectives,
                itemMap);
        }
        throw new SystemException(
            "Don't have Mapping Processor for command: "
                + cmd.getCommandName());
    }

    public static MappingProcessor getMappingProcessor(
        AbstractCIMCommandTestMap cmd,
        Objectives objectives,
        ItemMap itemMap,
        String titleColumn) {

        if (cmd instanceof CIMCommandTestMapInitial)
            return MappingProcessorFactory
                .createMappingProcessorForTestMapInitial(
                objectives,
                itemMap);

        if (cmd instanceof CIMCommandTestMapObjectiveUpdate)
            return MappingProcessorFactory
                .createMappingProcessorForTestMapObjectiveUpdate(
                objectives,
                MappingUtils.getDisplayLevelForColumnTitle(titleColumn));

        if (cmd instanceof CIMCommandTestMapInitial) {
            return MappingProcessorFactory
                .createMappingProcessorForTestMapInitial(
                objectives,
                itemMap);
        }

        if (cmd instanceof CIMCommandTestMapMergeValidate) {
            return MappingProcessorFactory
                .createMappingProcessorForTestMapInitial(
                objectives,
                itemMap);
        }

        if (cmd instanceof CIMCommandTestMapAK) {
            return MappingProcessorFactory.createMappingProcessorForAKUpdate(
                buildItemMap(((CIMCommandTestMapAK) cmd).getItemFile()),
                objectives,
                itemMap);
        }

        throw new SystemException(
            "Don't have Mapping Processor for command: "
                + cmd.getCommandName());
    }

    private static Map buildItemMap(String itemFile) {
        // parse the subtest xml to obtain a map of item using the item_id as key
        Map items = new HashMap();

        // read the element form the file
        Element rootElement =
            XMLUtils.buildRootElement(new File(itemFile));

        Element[] itemElements =
            CommandProcessorUtility.getItems(
                CommandProcessorUtility.getUniqueItemSet(rootElement));

        int validationMode =
            ItemAssembler.PARSE_ALLOW_DEFAULTS
                + ItemAssembler.PARSE_SKIP_EXTRA_VALIDATION;
        ItemBuilder builder = ItemBuilderFactory.getItemBuilder(validationMode);

        // build the item for each element and add them to the item map
        for (int itemIdx = 0; itemIdx < itemElements.length; itemIdx++) {
            Element itemElement = itemElements[itemIdx];

            Item item = builder.build(itemElement);
            items.put(item.getId(), item);
        }

        return items;
    }

    public static MappingWriter getMappingWiter(AbstractCIMCommandTestMap cmd) {
        MappingWriter writer = null;
        if (cmd instanceof CIMCommandTestMapMerge)
            return new MappingWriterMerge(cmd.getOutputFileName());
        else if (cmd instanceof CIMCommandTestMapMergeValidate)
            return new MappingWriterDummy();
        else if (
            (cmd instanceof CIMCommandTestMapInitial)
                || (cmd instanceof CIMCommandTestMapObjectiveUpdate)
                || (cmd instanceof CIMCommandTestMapAK))
            return new MappingWriterImpl(cmd.getOutputFileName());
        throw new SystemException(
            "Don't have Mapping Writer for command: " + cmd.getCommandName());
    }

}
