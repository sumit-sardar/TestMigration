package com.ctb.contentBridge.core.publish.mapping;


import java.io.*;
import java.util.*;

import org.apache.commons.lang.*;

import com.ctb.contentBridge.core.exception.SystemException;


public class FrameworkInfo {

    private static final Map DEFAULT_LEVEL_NAME_MAP_0 = new HashMap();
    private static final Map DEFAULT_LEVEL_NAME_MAP_1 = new HashMap();
    private static final Map WV_LEVEL_NAME_MAP = new HashMap();
    public static final Integer NO_GRADE = new Integer(0);
    static {
        DEFAULT_LEVEL_NAME_MAP_0.put(new Integer(0), "Root");
        DEFAULT_LEVEL_NAME_MAP_0.put(new Integer(1), "Grade");
        DEFAULT_LEVEL_NAME_MAP_0.put(new Integer(2), "Content Area");
        DEFAULT_LEVEL_NAME_MAP_0.put(new Integer(3), "Strand");
        DEFAULT_LEVEL_NAME_MAP_0.put(new Integer(4), "Standard");
        DEFAULT_LEVEL_NAME_MAP_0.put(new Integer(5), "Benchmark");
        DEFAULT_LEVEL_NAME_MAP_0.put(new Integer(6), "Skill");

        DEFAULT_LEVEL_NAME_MAP_1.put(new Integer(1), "Root");
        DEFAULT_LEVEL_NAME_MAP_1.put(new Integer(2), "Grade");
        DEFAULT_LEVEL_NAME_MAP_1.put(new Integer(3), "Content Area");
        DEFAULT_LEVEL_NAME_MAP_1.put(new Integer(4), "Strand");
        DEFAULT_LEVEL_NAME_MAP_1.put(new Integer(5), "Standard");
        DEFAULT_LEVEL_NAME_MAP_1.put(new Integer(6), "Benchmark");
        DEFAULT_LEVEL_NAME_MAP_1.put(new Integer(7), "Skill");
        DEFAULT_LEVEL_NAME_MAP_1.put(new Integer(8), "Subskill");

        WV_LEVEL_NAME_MAP.put(new Integer(1), "Root");
        WV_LEVEL_NAME_MAP.put(new Integer(2), "Content Area");
        WV_LEVEL_NAME_MAP.put(new Integer(3), "Grade");
        WV_LEVEL_NAME_MAP.put(new Integer(4), "Standard");
        WV_LEVEL_NAME_MAP.put(new Integer(5), "Benchmark");
    }

    public static final FrameworkInfo WV = new FrameworkInfo("WV",
            WV_LEVEL_NAME_MAP, new Integer(3), new Integer(2));
    public static final FrameworkInfo CAB = new FrameworkInfo("CTB",
            DEFAULT_LEVEL_NAME_MAP_1, new Integer(2), new Integer(3));
    public static final FrameworkInfo CO = new FrameworkInfo("CO",
            DEFAULT_LEVEL_NAME_MAP_1, new Integer(2), new Integer(3));
    public static final FrameworkInfo TX = new FrameworkInfo("TX",
            DEFAULT_LEVEL_NAME_MAP_1, new Integer(2), new Integer(3));
    public static final FrameworkInfo CCSUNIT = new FrameworkInfo("CCSUNIT",
            DEFAULT_LEVEL_NAME_MAP_1, new Integer(2), new Integer(3));
    public static final FrameworkInfo CCSUNITWV = new FrameworkInfo("CCSUNITWV",
            WV_LEVEL_NAME_MAP, new Integer(3), new Integer(2));

    public static final FrameworkInfo CO_TESTDATA = new FrameworkInfo("CO",
            DEFAULT_LEVEL_NAME_MAP_0, new Integer(1), new Integer(2));
    public static final FrameworkInfo TX_TESTDATA = new FrameworkInfo("TX",
            DEFAULT_LEVEL_NAME_MAP_0, new Integer(1), new Integer(2));

    private Map levelNames;
    private String frameworkCode;
    private Integer gradeLevel;
    private Integer internalProductNameLevel;

    FrameworkInfo(String frameworkCode, Map levelNames, Integer gradeLevel, Integer internalProductNameLevel) {
        this.frameworkCode = frameworkCode;
        this.levelNames = levelNames;
        this.gradeLevel = gradeLevel;
        this.internalProductNameLevel = internalProductNameLevel;
    }

    public FrameworkInfo(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));

        loadFile(reader);
    }

    FrameworkInfo(BufferedReader reader) throws IOException {
        loadFile(reader);
    }

    private void loadFile(BufferedReader reader) throws IOException {
        frameworkCode = null;
        gradeLevel = null;
        internalProductNameLevel = null;

        Map map = new HashMap();
        String line;
        int rowcount = 1;

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.length() == 0) {
                continue;
            }
            StringTokenizer variableToker = new StringTokenizer(line, "=");
            StringTokenizer levelToker = new StringTokenizer(line, ",\"");

            // see whether it is a variable line
            if (variableToker.countTokens() == 2) {
                String varName = variableToker.nextToken().toLowerCase();
                String varValue = variableToker.nextToken();

                if (varName.equals("framework")) {
                    frameworkCode = varValue;
                } else if (varName.equals("gradelevel")) {
                    if (StringUtils.isNumeric(varValue)) {
                        gradeLevel = new Integer(varValue);
                    } else {
                        throw new SystemException("Variable " + varName
                                + " requires a numeric value");
                    }
                } else if (varName.equals("productlevel")) {
                    if (StringUtils.isNumeric(varValue)) {
                        internalProductNameLevel = new Integer(varValue);
                    } else {
                        throw new SystemException("Variable " + varName
                                + " requires a numeric value");
                    }
                }
            } // or a level definition line
            else if (levelToker.countTokens() == 2) {
                String levelToken = levelToker.nextToken();
                String levelName = levelToker.nextToken();

                if (StringUtils.isNumeric(levelToken)) {
                    Integer levelNumber = new Integer(levelToken);

                    if (map.containsKey(levelNumber)) {
                        throw new SystemException("Duplicate level definition: "
                                + line);
                    }
                    map.put(levelNumber, levelName);
                } else {
                    throw new SystemException("Level number has to be numeric value: "
                            + line);
                }

            } else {
                throw new SystemException("Invalid row in line " + rowcount
                        + ": " + line + " (" + levelToker.countTokens() + " "
                        + variableToker.countTokens() + ")");
            }
            rowcount++;
        }
        if (frameworkCode == null) {
            throw new SystemException("Framework code not defined in framework definition file. The file requires a line of the format \"Framework=xyz\"");
        }
        if (gradeLevel == null) {
            throw new SystemException("Grade level not defined in framework definition file. The file requires a line of the format \"GradeLevel=n\"");
        }
        if (internalProductNameLevel == null) {
            throw new SystemException("Product level not defined in framework definition file. The file requires a line of the format \"ProductLevel=n\"");
        }
        if (!gradeLevel.equals(NO_GRADE) && !map.containsKey(gradeLevel)) {
            throw new SystemException("The specified grade level " + gradeLevel
                    + " does not match a level definition");
        }
        if (!map.containsKey(internalProductNameLevel)) {
            throw new SystemException("The specified product level "
                    + internalProductNameLevel
                    + " does not match a level definition");
        }
        levelNames = map;
        if (!levelNames.get(new Integer(1)).equals("Root"))
            throw new SystemException("The first level entry in levels.txt must be 1,Root");
    }

    public Integer getInternalProductNameLevel() {
        return internalProductNameLevel;
    }

    public Map getLevelNames() {
        return levelNames;
    }

    public String getLevelName(int level) {
        if (!levelNames.containsKey(new Integer(level))) {
            return null;
        } else {
            return (String) levelNames.get(new Integer(level));
        }
    }

    public int getRootLevel() {
        if (!levelNames.isEmpty()) {
            Object minObject = Collections.min(levelNames.keySet());

            return ((Integer) minObject).intValue();
        } else {
            throw new SystemException("Cannot get the root level of an empty collection ");
        }
    }

    public Integer getGradeLevel() {
        return gradeLevel;
    }

    public String getFrameworkCode() {
        return frameworkCode;
    }

}
