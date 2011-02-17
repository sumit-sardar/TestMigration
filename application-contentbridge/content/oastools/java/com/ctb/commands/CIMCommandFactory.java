package com.ctb.commands;

import java.io.PrintStream;

import org.apache.commons.lang.StringUtils;
import com.ctb.common.tools.CommandLine;
import com.ctb.common.tools.SystemException;
import com.ctb.mapping.MapperFactory;
import com.ctb.xmlProcessing.item.ItemAssembler;

public class CIMCommandFactory {
    public static final String JOB_ID = "jobID";

    public static final String TGT_CSV = "tgtCsv";
    public static final String SRC_CSV = "srcCsv";

    public static final String INPUT_ITEM_IDS = "inputItemIds";
    public static final String OUTPUT_ITEM_MAP = "outputItemMap";
    public static final String OUTPUT_CSV = "outputCsv";
    public static final String INPUT_CSV = "inputCsv";
    public static final String TARGET_ENV = "targetEnv";
    public static final String SRC_ENV = "sourceEnv";
    public static final String FWK_CODE = "frameworkCode";

    public static final String MAPPING_LIST = "mapping.list";
    public static final String MAPPING_DIR = "mapping.dir";
    public static final String FILEFORMAT = "fileformat";
    public static final String MAPPINGFILE = "mappingfile";
    public static final String OBJECTIVESFILE = "objectivesfile";
    public static final String FRAMEWORKFILE = "frameworkfile";
    public static final String ITEMFILE = "itemfile";
    public static final String ENV = "env";
    public static final String VALIDATIONMODE = "validationmode";
    public static final String LOCALIMAGEAREA = "localimagearea";
    public static final String IMAGEAREA = "imagearea";

    public static final String PROPERTIES_SUFFIX = ".properties";

    public static final String _SHORT = MapperFactory.FILEFORMAT_SHORT;
    public static final String _STRICT = "strict";
    public static final String _RELAXED = "relaxed";
    public static final String _ALLOWDEFAULTS = "allowdefaults";
    public static final String _DEFAULT_ENV = "user";

    private CIMCommandFactory() {
    }

    public static int parseValidationMode(String parameter) {
        parameter = parameter.toLowerCase();
        if (parameter.equals(_STRICT)) {
            return ItemAssembler.NO_DEFAULTS_VALIDATE_STRICT;
        } else if (parameter.equals(_ALLOWDEFAULTS)) {
            return ItemAssembler.PARSE_ALLOW_DEFAULTS;
        } else if (parameter.equals(_RELAXED)) {
            return ItemAssembler.PARSE_ALLOW_DEFAULTS
                + ItemAssembler.PARSE_SKIP_EXTRA_VALIDATION;
        }
        throw new SystemException("Unknown validation mode " + parameter);
    }

    public static CIMCommand createCIMCommandFromCommandLine(CommandLine cmdLine) {
        String cmd = cmdLine.getCommand();
        if (cmd.equals(CIMCommand.MAPPING_HIERARCHY)) {
            return createCIMCommandCreateMappingHierarchy(cmdLine);
        } else if (cmd.equals(CIMCommand.FLATTENED_MAPPING_HIERARCHY)) {
            return createCIMCommandCreateFlattenedMappingHierarchy(cmdLine);
        } else if (cmd.equals(CIMCommand.PRODUCT_REPORT)) {
            return createCIMCommandProductReport(cmdLine);
        } else if (cmd.equals(CIMCommand.IMPORT_ITEMS_VALIDATE)) {
            return createCIMCommandImportItemsValidate(cmdLine);
        } else if (cmd.equals(CIMCommand.IMPORT_ITEMS)) {
            return createCIMCommandImportItems(cmdLine);
        } else if (cmd.equals(CIMCommand.MAP_ITEMS_VALIDATE)) {
            return createCIMCommandMapItemsValidate(cmdLine);
        } else if (cmd.equals(CIMCommand.MAP_ITEMS)) {
            return createCIMCommandMapItems(cmdLine);
        } else if (cmd.equals(CIMCommand.BUILD_TEST)) {
            return createCIMCommandBuildSubTest(cmdLine);
        } else if (cmd.equals(CIMCommand.BUILD_ASSESSMENT)) {
            return createCIMCommandBuildAssessment(cmdLine);
        } else if (cmd.equals(CIMCommand.ROUNDTRIP_MAP)) {
            return createCIMCommandRoundTripMap(cmdLine);
        } else if (cmd.equals(CIMCommand.ROUNDTRIP_VALIDATE)) {
            return createCIMCommandRoundTripValidate(cmdLine);
        } else if (cmd.equals(CIMCommand.IMPORT_AND_MAP_ITEMS)) {
            return createCIMCommandImportAndMapItems(cmdLine);
        } else if (cmd.equals(CIMCommand.TESTMAP_INITIAL)) {
            return createCIMCommandTestMapInitial(cmdLine);
        } else if (cmd.equals(CIMCommand.TESTMAP_OBJECTIVE)) {
            return createCIMCommandTestMapObjectiveUpdate(cmdLine);
        } else if (cmd.equals(CIMCommand.TESTMAP_MERGE)) {
            return createCIMCommandTestMapMerge(cmdLine);
        } else if (cmd.equals(CIMCommand.TESTMAP_MERGE_VALIDATE)) {
            return createCIMCommandTestMapMergeValidate(cmdLine);
        } else if (cmd.equals(CIMCommand.TESTMAP_COMPARE)) {
            return createCIMCommandTestMapCompare(cmdLine);
        } else if (cmd.equals(CIMCommand.TESTMAP_ANSWERKEY)) {
            return createCIMCommandTestMapAnswerKey(cmdLine);
        } else if ( cmd.equals(CIMCommand.CREATE_PDF_THROUGH_FILE)) {
            return createCIMCommandCreatePdfThroughFile(cmdLine);
        } else if ( cmd.equals(CIMCommand.GENERATE_MEDIA_FOR_EDITOR_REVIEW)) {
            return createCIMCommandGenerateMediaForEditorReview(cmdLine);
        } else if ( cmd.equals(CIMCommand.GENERATE_PDF)) {
            return createCIMCommandGeneratePDF(cmdLine);
        } else if ( cmd.equals(CIMCommand.VALIDATE_ITEM_XML)) {
            return createCIMCommandValidateItemXML(cmdLine);
        }else {
            throw new SystemException("Unknown command: " + cmd);
        }
    }

    private static CIMCommand createCIMCommandValidateItemXML(CommandLine cmdLine) {
        return new CIMCommandValidateItemXML(
            cmdLine.getParameterValue("env"),
            cmdLine.getParameterValue("logFile"));
    }

    private static CIMCommand createCIMCommandCreatePdfThroughFile(CommandLine cmdLine) {
        return new CIMCommandCreatePdfThroughFile(
            cmdLine.getFileParameter( "infile" ),
            cmdLine.getFileParameter("xslfile"),
            cmdLine.getParameterValue( "outfile" ),
            cmdLine.getParameterValue( "type" ),
            cmdLine.getOptionalParameterValue("step","full"));
    }

    private static CIMCommand createCIMCommandGenerateMediaForEditorReview(CommandLine cmdLine) {
        return new CIMCommandGenerateMediaForEditorReview(
            cmdLine.getFileParameter( "infile" ),
            cmdLine.getParameterValue("rootdirectory"),
            cmdLine.getParameterValue("directory"),
            cmdLine.getParameterValue( "pdfoutfile" ),
            cmdLine.getParameterValue( "flashoutfile" ));
    }

    private static CIMCommand createCIMCommandGeneratePDF(CommandLine cmdLine) {
        return new CIMCommandGeneratePDF(
            cmdLine.getFileParameter( "infile" ),
            cmdLine.getParameterValue("rootdirectory"),
            cmdLine.getParameterValue("directory"),
            cmdLine.getParameterValue( "pdfoutfile" ));
    }

    private static CIMCommand createCIMCommandImportAndMapItems(CommandLine cmdLine) {
        String imageArea = cmdLine.getParameterValue(IMAGEAREA);
        return new CIMCommandImportAndMapItems(
            cmdLine.getOptionalParameterValue(ENV, _DEFAULT_ENV)
                + PROPERTIES_SUFFIX,
            cmdLine.getFileParameter(ITEMFILE),
            cmdLine.getOptionalParameterValue(MAPPING_DIR, null),
            cmdLine.getOptionalParameterList(MAPPING_LIST, null),
            parseValidationMode(
                cmdLine.getOptionalParameterValue(VALIDATIONMODE, _STRICT)),
            cmdLine.getOptionalParameterValue(FILEFORMAT, _SHORT),
            imageArea,
            cmdLine.getOptionalParameterValue(LOCALIMAGEAREA, imageArea));
    }

    private static CIMCommandCreateMappingHierarchy createCIMCommandCreateMappingHierarchy(CommandLine cmdLine) {
        return new CIMCommandCreateMappingHierarchy(
            cmdLine.getFileParameter(FRAMEWORKFILE),
            cmdLine.getFileParameter(OBJECTIVESFILE),
            cmdLine.getOptionalFileParameter(MAPPINGFILE),
            cmdLine.getOptionalParameterValue(FILEFORMAT, _SHORT));
    }

    private static CIMCommandCreateFlattenedMappingHierarchy createCIMCommandCreateFlattenedMappingHierarchy(CommandLine cmdLine) {
        return new CIMCommandCreateFlattenedMappingHierarchy(
            cmdLine.getFileParameter(FRAMEWORKFILE),
            cmdLine.getFileParameter(OBJECTIVESFILE),
            cmdLine.getOptionalFileParameter(MAPPINGFILE),
            cmdLine.getOptionalParameterValue(FILEFORMAT, _SHORT));
    }

    private static CIMCommandProductReport createCIMCommandProductReport(CommandLine cmdLine) {
        return new CIMCommandProductReport(
            cmdLine.getFileParameter(FRAMEWORKFILE),
            cmdLine.getFileParameter(OBJECTIVESFILE),
            cmdLine.getOptionalFileParameter(MAPPINGFILE),
            cmdLine.getOptionalParameterValue(FILEFORMAT, _SHORT),
            cmdLine.getOptionalParameterValue(ENV, _DEFAULT_ENV)
                + PROPERTIES_SUFFIX);
    }

    private static CIMCommandImportItems createCIMCommandImportItems(CommandLine cmdLine) {
        String imageArea = cmdLine.getParameterValue(IMAGEAREA);
        return new CIMCommandImportItems(
            cmdLine.getOptionalParameterValue(ENV, _DEFAULT_ENV)
                + PROPERTIES_SUFFIX,
            cmdLine.getFileParameter(ITEMFILE),
            parseValidationMode(
                cmdLine.getOptionalParameterValue(VALIDATIONMODE, _STRICT)),
            imageArea,
            cmdLine.getOptionalParameterValue(LOCALIMAGEAREA, imageArea));
    }

    private static CIMCommandImportItemsValidate createCIMCommandImportItemsValidate(CommandLine cmdLine) {
        return new CIMCommandImportItemsValidate(
            cmdLine.getOptionalParameterValue(ENV, _DEFAULT_ENV)
                + PROPERTIES_SUFFIX,
            cmdLine.getFileParameter(ITEMFILE),
            parseValidationMode(
                cmdLine.getOptionalParameterValue(VALIDATIONMODE, _STRICT)));
    }

    private static CIMCommandImportItemsNoMedia createCIMCommandImportItemsNoMedia(CommandLine cmdLine) {
        String imageArea = cmdLine.getParameterValue(IMAGEAREA);
        return new CIMCommandImportItemsNoMedia(
            parseValidationMode(
                cmdLine.getOptionalParameterValue(VALIDATIONMODE, _STRICT)),
            cmdLine.getOptionalParameterValue(ENV, _DEFAULT_ENV)
                + PROPERTIES_SUFFIX,
            cmdLine.getFileParameter(ITEMFILE));
    }

    private static CIMCommandMapItems createCIMCommandMapItems(CommandLine cmdLine) {
        String imageArea = cmdLine.getParameterValue(IMAGEAREA);
        return new CIMCommandMapItems(
            cmdLine.getOptionalParameterValue(ENV, _DEFAULT_ENV)
                + PROPERTIES_SUFFIX,
            cmdLine.getFileParameter(ITEMFILE),
            parseValidationMode(
                cmdLine.getOptionalParameterValue(VALIDATIONMODE, _STRICT)),
            imageArea,
            cmdLine.getOptionalParameterValue(LOCALIMAGEAREA, imageArea),
            cmdLine.getFileParameter(FRAMEWORKFILE),
            cmdLine.getFileParameter(OBJECTIVESFILE),
            cmdLine.getFileParameter(MAPPINGFILE),
            cmdLine.getOptionalParameterValue(FILEFORMAT, _SHORT));
    }

    private static CIMCommandMapItemsValidate createCIMCommandMapItemsValidate(CommandLine cmdLine) {
        String imageArea = cmdLine.getParameterValue(IMAGEAREA);
        return new CIMCommandMapItemsValidate(
            cmdLine.getOptionalParameterValue(ENV, _DEFAULT_ENV)
                + PROPERTIES_SUFFIX,
            cmdLine.getFileParameter(ITEMFILE),
            parseValidationMode(
                cmdLine.getOptionalParameterValue(VALIDATIONMODE, _STRICT)),
            cmdLine.getFileParameter(FRAMEWORKFILE),
            cmdLine.getFileParameter(OBJECTIVESFILE),
            cmdLine.getFileParameter(MAPPINGFILE),
            cmdLine.getOptionalParameterValue(FILEFORMAT, _SHORT));
    }

    private static CIMCommandBuildSubTestSofa createCIMCommandBuildSubTest(CommandLine cmdLine) {
        String imageArea = cmdLine.getParameterValue(IMAGEAREA);
        return new CIMCommandBuildSubTestSofa(
            cmdLine.getOptionalParameterValue(ENV, _DEFAULT_ENV)
                + PROPERTIES_SUFFIX,
            cmdLine.getFileParameter(ITEMFILE),
            cmdLine.getOptionalParameterValue(MAPPING_DIR, null),
            imageArea,
            cmdLine.getOptionalParameterValue(LOCALIMAGEAREA, imageArea),
            cmdLine.getOptionalParameterValue(FILEFORMAT, _SHORT));
    }

    private static CIMCommandBuildAssessment createCIMCommandBuildAssessment(CommandLine cmdLine) {
        String imageArea = cmdLine.getParameterValue(IMAGEAREA);
        return new CIMCommandBuildAssessment(
            cmdLine.getOptionalParameterValue(ENV, _DEFAULT_ENV)
                + PROPERTIES_SUFFIX,
            cmdLine.getFileParameter(ITEMFILE),
            cmdLine.getOptionalParameterValue(MAPPING_DIR, null),
            imageArea,
            cmdLine.getOptionalParameterValue(LOCALIMAGEAREA, imageArea),
            cmdLine.getOptionalParameterValue(FILEFORMAT, _SHORT),
            cmdLine.getOptionalParameterValue("subtestmedia", "No"));
    }

    private static CIMCommandRoundTripMap createCIMCommandRoundTripMap(CommandLine cmdLine) {
        return new CIMCommandRoundTripMap(
            cmdLine.getParameterValue(SRC_ENV) + PROPERTIES_SUFFIX,
            cmdLine.getParameterValue(TARGET_ENV) + PROPERTIES_SUFFIX,
            cmdLine.getParameterValue(FWK_CODE));
    }

    private static CIMCommandRoundTripValidate createCIMCommandRoundTripValidate(CommandLine cmdLine) {
        return new CIMCommandRoundTripValidate(
            cmdLine.getParameterValue(SRC_ENV) + PROPERTIES_SUFFIX,
            cmdLine.getParameterValue(TARGET_ENV) + PROPERTIES_SUFFIX,
            cmdLine.getParameterValue(FWK_CODE));
    }

    private static CIMCommandTestMapInitial createCIMCommandTestMapInitial(CommandLine cmdLine) {
        return new CIMCommandTestMapInitial(
            cmdLine.getOptionalParameterValue(ENV, _DEFAULT_ENV)
                + PROPERTIES_SUFFIX,
            cmdLine.getParameterValue(FWK_CODE),
            cmdLine.getParameterValue(INPUT_CSV),
            cmdLine.getParameterValue(OUTPUT_CSV));
    }

    private static CIMCommandTestMapObjectiveUpdate createCIMCommandTestMapObjectiveUpdate(CommandLine cmdLine) {
        return new CIMCommandTestMapObjectiveUpdate(
            cmdLine.getOptionalParameterValue(ENV, _DEFAULT_ENV)
                + PROPERTIES_SUFFIX,
            cmdLine.getParameterValue(FWK_CODE),
            cmdLine.getParameterValue(INPUT_CSV),
            cmdLine.getParameterValue(OUTPUT_CSV));
    }

    private static CIMCommandTestMapMerge createCIMCommandTestMapMerge(CommandLine cmdLine) {
        return new CIMCommandTestMapMerge(
            cmdLine.getOptionalParameterValue(ENV, _DEFAULT_ENV)
                + PROPERTIES_SUFFIX,
            cmdLine.getParameterValue(FWK_CODE),
            cmdLine.getParameterValue(INPUT_CSV),
            cmdLine.getParameterValue(OUTPUT_ITEM_MAP));
    }

    private static CIMCommandTestMapMergeValidate createCIMCommandTestMapMergeValidate(CommandLine cmdLine) {
        return new CIMCommandTestMapMergeValidate(
            cmdLine.getOptionalParameterValue(ENV, _DEFAULT_ENV)
                + PROPERTIES_SUFFIX,
            cmdLine.getParameterValue(FWK_CODE),
            cmdLine.getParameterValue(INPUT_CSV),
            cmdLine.getParameterValue(OUTPUT_ITEM_MAP));
    }

    private static CIMCommandTestMapCompare createCIMCommandTestMapCompare(CommandLine cmdLine) {
        return new CIMCommandTestMapCompare(
            cmdLine.getParameterValue(SRC_CSV),
            cmdLine.getParameterValue(TGT_CSV));
    }

    private static CIMCommandTestMapAK createCIMCommandTestMapAnswerKey(CommandLine cmdLine) {
        return new CIMCommandTestMapAK(
            cmdLine.getOptionalParameterValue(ENV, _DEFAULT_ENV)
                + PROPERTIES_SUFFIX,
            cmdLine.getParameterValue(FWK_CODE),
            cmdLine.getParameterValue(ITEMFILE),
            cmdLine.getParameterValue(INPUT_CSV),
            cmdLine.getParameterValue(OUTPUT_CSV));
    }

    public static void printAvailableCIMCommandsInfo(PrintStream s) {
        String output = getAvailableCIMCommandsInfo();
        s.println(output);
    }

    public static String getAvailableCIMCommandsInfo() {
        String output = "\nThe following are valid 'commands':";
        
        for (int i = 0; i < CIMCommand.activeCommands.length; i++) {
            output += format(CIMCommand.activeCommands[i].name)
                + CIMCommand.activeCommands[i].description + "\n";
        }
        return output;
    }

    private static String format(String cmdName) {
        return StringUtils.leftPad(cmdName + ": ", 27);
    }
}
