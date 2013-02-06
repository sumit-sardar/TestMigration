package com.ctb.contentBridge.core.publish.command;

public interface CIMCommand {

    //This is where command strings are defined for the command line interface
    public static final String BUILD_ASSESSMENT = "buildassessment";
    public static final String BUILD_TEST = "buildtest";
    public static final String MAP_ITEMS = "mapitems";
    public static final String MAP_ITEMS_VALIDATE = "mapitemsvalidate";
    public static final String IMPORT_ITEMS = "importitems";
    public static final String IMPORT_ITEMS_VALIDATE = "importitemsvalidate";
    public static final String IMPORT_AND_MAP_ITEMS = "importandmapitems";
    public static final String CREATE_PDF_THROUGH_FILE = "generatepdf";
    public static final String GENERATE_MEDIA_FOR_EDITOR_REVIEW = "generatemediaforeditorreview";
    public static final String GENERATE_PDF = "generatepdf";
    public static final String VALIDATE_ITEM_XML = "validateitemxml";

    public static final String MAPPING_HIERARCHY = "mappinghierarchy";
    public static final String FLATTENED_MAPPING_HIERARCHY = "flattenedmappinghierarchy";
    public static final String PRODUCT_REPORT = "productreport";

    //used only for testing
    public static final String IMPORT_ITEMS_NO_MEDIA = "importnomedia";

    public static final String ROUNDTRIP_MAP = "roundtrip_map";
    public static final String ROUNDTRIP_VALIDATE = "roundtrip_validate";

    public static final String TESTMAP_INITIAL = "testmap_initial";
    public static final String TESTMAP_COMPARE = "testmap_compare";
    public static final String TESTMAP_MERGE = "testmap_merge";
    public static final String TESTMAP_MERGE_VALIDATE = "testmap_merge_validate";
    public static final String TESTMAP_OBJECTIVE = "testmap_objective_update";
    public static final String TESTMAP_ANSWERKEY = "testmap_answerkey";

    public static final CommandInfo[] activeCommands =
        {
            new CommandInfo(
                BUILD_ASSESSMENT,
                "Imports an Assessment file into an OAS instance"),
            new CommandInfo(
                BUILD_TEST,
                "Imports a SubTest file into an OAS instance"),
            new CommandInfo(
                MAP_ITEMS,
                "Maps an Item/ItemSet file into an OAS instance"),
            new CommandInfo(
                MAP_ITEMS_VALIDATE,
                "Validates an Item/ItemSet file for mapping"),
            new CommandInfo(IMPORT_ITEMS, "Imports an Item/ItemSet file"),
            new CommandInfo(
                IMPORT_ITEMS_VALIDATE,
                "Validates an Item/ItemSet file for import"),
            new CommandInfo(
                MAPPING_HIERARCHY,
                "Generates a projected mapping report"),
            new CommandInfo(
                FLATTENED_MAPPING_HIERARCHY,
                "Generates a flattened projected mapping report"),
            new CommandInfo(PRODUCT_REPORT, "Generates a Product Report"),
            new CommandInfo(
                ROUNDTRIP_MAP,
                "Maps all CAB items in a source OAS instance to specified frameworks in a target OAS instance"),
            new CommandInfo(
                ROUNDTRIP_VALIDATE,
                "Validates mapping of all CAB items in a source OAS instance to specified frameworks in a target OAS instance"),
            new CommandInfo(
                IMPORT_AND_MAP_ITEMS,
                "Imports and maps an Item/ItemSet file to all frameworks in an OAS instance"),
            new CommandInfo(
                TESTMAP_INITIAL,
                "Builds initial test map from list of item Ids"),
            new CommandInfo(
                TESTMAP_COMPARE,
                "Compares the contents of two cvs files"),
            new CommandInfo(
                TESTMAP_MERGE,
                "Merges mapping in the csv file into mapping file"),
            new CommandInfo(
                TESTMAP_MERGE_VALIDATE,
                "Validates mapping in csv against the current mapping file"),
            new CommandInfo(
                TESTMAP_OBJECTIVE,
                "Updates the objective description in a csv file"),
            new CommandInfo(
                TESTMAP_ANSWERKEY,
                "Updates the csv file with the answer key from test XML"),
            new CommandInfo(
                CREATE_PDF_THROUGH_FILE,
                "Create PDF content to a file"),
            new CommandInfo(
                GENERATE_MEDIA_FOR_EDITOR_REVIEW,
                "Generate PDF and Flash media for a given XML file"),
            new CommandInfo(
                GENERATE_PDF,
                "Generate PDF media for a given XML file"
            ),
            new CommandInfo(
                VALIDATE_ITEM_XML,
                "Validate item XML"
            )
        //new CommandInfo(IMPORT_ITEMS_NO_MEDIA,), //only used for testing - not available on cmdLine
    };


    /**
     * The command name for a concrete class is defined as a static final String
     * in the interface com.ctb.commands.CIMCommand. Concrete CIMCommands
     * return their corresponding Strings.
     */
    String getCommandName();
}

class CommandInfo {
    final String name;
    final String description;

    CommandInfo(String commandName, String description) {
        this.name = commandName;
        this.description = description;
    }
}
