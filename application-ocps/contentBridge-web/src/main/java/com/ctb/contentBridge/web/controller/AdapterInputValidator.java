package com.ctb.contentBridge.web.controller;


import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * User: mwshort
 * Date: Feb 3, 2004
 * Time: 10:37:40 AM
 * 
 *
 */
public class AdapterInputValidator {

    public static int EXPECTED_MINIMUM_ARGUMENT_SIZE = 6;

    public static boolean isValidArgList(List arguments) {

        return (
            checkForLength(arguments)
                && checkForFile((String) arguments.get(3))
                && checkForFile(
                    (String) arguments.get(3)
                        + "/"
                        + (String) arguments.get(4)));
    }

    private static boolean checkForLength(List arguments) {
        return (arguments.size() >= EXPECTED_MINIMUM_ARGUMENT_SIZE);
    }

    private static boolean checkForFile(String arg) {
        boolean validArgs = true;
        try {
            new File(arg).getCanonicalPath();
        } catch (IOException e) {
            validArgs = false;
        }
        return validArgs;
    }

}
