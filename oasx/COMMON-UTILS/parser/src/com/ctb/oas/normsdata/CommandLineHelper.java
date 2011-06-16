package com.ctb.oas.normsdata;

import com.ctb.oas.normsdata.parser.ItemPValuesReader;
import com.ctb.oas.normsdata.parser.PValuesReader;

import java.io.IOException;

/**
 * @author Sreenivas  Ananthakrishna
 */
public class CommandLineHelper {
    private static final String TN_SURVEY_COMMAND = "TNSurvey";
    private static final String TN_OBJ_P_VALUES_COMMAND = "TNObjectivePValues";
    private static final String TN_ITEM_P_VALUES_COMMAND = "TNItemPValues";
    private static final String TABE_COMMAND = "Tabe";

    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            printUsage();
            System.exit(2);
        }

        String[] fileArgs = new String[]{args[1], args[2]};

        if (args[0].equals(TN_SURVEY_COMMAND)) {
            TerraNovaLoader.main(fileArgs);
        }
        else if (args[0].equals(TN_OBJ_P_VALUES_COMMAND)) {
            PValuesReader.main(fileArgs);
        }
        else if (args[0].equals(TN_ITEM_P_VALUES_COMMAND)) {
            ItemPValuesReader.main(fileArgs);
        }
        else if (args[0].equals(TABE_COMMAND))
            TABELoader.main(fileArgs);
        else
            printUsage();
    }

    private static void printUsage() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("run  bin/loadNorms <norms_type> <input> <output> \n");
        buffer.append("where <norms_type> can be one of the following:- \n");
        buffer.append(TN_SURVEY_COMMAND + " - for loading terranova survey or battery data \n");
        buffer.append(TN_OBJ_P_VALUES_COMMAND + "- for loading TN Objective P Values \n");
        buffer.append(TN_ITEM_P_VALUES_COMMAND + " - for loading TN Item P Values \n");
        buffer.append(TABE_COMMAND + " - for loading TABE norms data \n");
        System.out.println(buffer.toString());
    }
}
