package com.ctb.util;


import java.io.*;
import java.util.*;


/**
 * build up an instance of <code>Properties</code> interactively
 * from the command prompt.
 */
public class InteractivePropertyBuilder {

    /** map of property keys to user prompts */
    private Map prompts;
    private Properties defaults;

    /**
     * Constructor
     * @param prompts a map of property keys to user prompts
     */
    public InteractivePropertyBuilder(Properties defaults, Map prompts) {
        this.prompts = prompts;
        this.defaults = defaults;
    }

    /**
     * use System.in to ask user to input all properties specified in constructor
     * @return a new instance of <code>Properties</code> with same keys as map of
     * prompts passed to the constructor.
     */
    public Properties getProperties() {
        Properties props = new Properties();

        for (Iterator iterator = prompts.keySet().iterator(); iterator.hasNext();) {
            String key = (String) iterator.next();

            if (defaults.containsKey(key)) {
                props.put(key, defaults.getProperty(key));
            } else {
                String value = promptForValueWithConfirmation((String) prompts.get(key));

                props.put(key, value);
            }
        }
        System.out.println("retrieved props:  " + props);
        return props;
    }

    /**
     * ask user for a value, confirm entered value with user
     * @param prompt a user-readable description of the property
     * @return  the new value
     */
    private String promptForValueWithConfirmation(String prompt) {
        String value = promptForValue(prompt);
        String confirmation = promptForValue("confirm " + prompt + " [" + value
                + "] [Y/N]  ");

        if (confirmation.length() == 0 || confirmation.equalsIgnoreCase("Y")
                || confirmation.equals(value)) {
            return value;
        } else {
            return promptForValueWithConfirmation(prompt);
        }
    }

    /**
     * ask user for a value without confirmation
     * @param prompt a user-readable description of the property
     * @return  the new value
     */
    private String promptForValue(String prompt) {
        System.out.print(prompt + ":  ");
        try {
            String value = new BufferedReader(new InputStreamReader(System.in)).readLine();

            checkForQuit(value);
            return value;
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
            throw new RuntimeException("unexpected error reading from input prompt");
        }
    }

    /**
     * exit if user enters 'quit' or 'exit'
     */
    private void checkForQuit(String cmd) {
        if (cmd.equalsIgnoreCase("QUIT") || cmd.equalsIgnoreCase("EXIT")) {
            System.exit(0);
        }
    }
}
