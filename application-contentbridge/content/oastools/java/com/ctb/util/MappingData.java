package com.ctb.util;

import org.apache.oro.text.regex.*;

import java.util.*;
import java.io.*;

import com.ctb.common.tools.SystemException;

/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Nov 4, 2003
 * Time: 11:37:34 AM
 * To change this template use Options | File Templates.
 */
public class MappingData {

    private Map idToObjectiveMap = new HashMap();

    private String frameworkCode;


    public MappingData(File mappingFileOrDirectory, String frameworkCode) {
        this(frameworkCode);
        File mappingFile = getFileIfDirectory(mappingFileOrDirectory);
        loadIDObjectivePairsFromFile(mappingFile);
    }

    public MappingData(String frameworkCode) {
        this.frameworkCode = frameworkCode;
    }

    public MappingData(Reader reader, String frameworkCode) {
        this(frameworkCode);
        loadIDObjectivePairsFromFile(reader);
    }
    public String getFrameworkCode() {
        return frameworkCode;
    }

    public Set getItemIdSetFromItemToObjectiveMap() {
        return idToObjectiveMap.keySet();
    }

    public String getObjectiveByItemID(String itemID) {
        return (String) idToObjectiveMap.get(itemID);
    }
    public boolean containsItemID(String itemId) {
        return idToObjectiveMap.containsKey(itemId);
    }

    private File getFileIfDirectory(File mappingFile) {
        File file = mappingFile;
        if (file.isDirectory())
            file = new File(mappingFile + "/" + frameworkCode + "/item_map.txt");
        return file;
    }

    private void loadIDObjectivePairsFromFile(File file) {
        try {
            loadIDObjectivePairsFromFile(new FileReader(file));
        } catch (FileNotFoundException e) {
            throw new SystemException(e.getMessage(),e);
        }
    }


    private void loadIDObjectivePairsFromFile(Reader inReader) {

      try {
          List itemIds = new ArrayList();
          BufferedReader reader =
              new BufferedReader(
                  inReader);
          String buffer = null;
          while ((buffer = reader.readLine()) != null) {
              if (buffer.length() > 0) {
                  IDObjectivePair pair = null;
                  try {
                  pair = readIDObjectivePairFromLineBuffer(buffer);
                  } catch (SystemException se) {
                      System.out.println(se.getMessage());
                      //todo - handle this error with log maybe?
                  }
                  this.putPair(pair);
              }
          }
      } catch (IOException e) {
          throw new SystemException(e.getMessage(),e);
      }
  }

    private void putPair(IDObjectivePair pair) {
        validatePair(pair);
        idToObjectiveMap.put(pair.getID(),pair.getObjective());
    }
    public void addIDAndObjective(String itemId, String objective) {
        putPair(new IDObjectivePair(itemId,objective));
    }

    private void validatePair(IDObjectivePair pair) {
        if (pair == null || pair.getID() == null || pair.getObjective() == null)
            throw new SystemException("Could not add an item id and objective pair with null values");
        IDObjectivePair compareTo = new IDObjectivePair(pair.getID(),(String)idToObjectiveMap.get(pair.getID()));
        if (compareTo.getObjective() == null)
            return;
        if (!compareTo.equals(pair))
            throw new SystemException("Mappings can not contain multiple objectives for one item id" +
                    ". Objectives " + compareTo.getObjective() + " and " + pair.getObjective() + " were" +
                    " referenced for ID: " + pair.getID());
    }
    private IDObjectivePair readIDObjectivePairFromLineBuffer(String buffer) {
        final String ITEM_MAP_EXP_LONG = "\"([^\"]*)\",\"([^\"]*)\"";
        final String ITEM_MAP_EXP = "([^,]*),(.*)";

        String itemId;
        String objective;
        if ((itemId = getSubStringFromLine(ITEM_MAP_EXP_LONG, buffer,1))
            == null) {
            itemId = getSubStringFromLine(ITEM_MAP_EXP, buffer,1);
        }
        if ((objective = getSubStringFromLine(ITEM_MAP_EXP_LONG, buffer,2))
            == null) {
            objective = getSubStringFromLine(ITEM_MAP_EXP, buffer,2);
        }
        if (objective == null || itemId == null)
            throw new SystemException("Could not read mapping line: " + buffer);
        return new IDObjectivePair(itemId,objective);
    }

    private String getSubStringFromLine(String mapping_exp, String buffer, int group) {
        PatternCompiler compiler = new Perl5Compiler();
        PatternMatcher matcher = new Perl5Matcher();
        Pattern pattern = null;

        try {
            pattern =
                compiler.compile(
                    mapping_exp,
                    Perl5Compiler.CASE_INSENSITIVE_MASK);
        } catch (MalformedPatternException e) {
            throw new SystemException(
                "Cannot reconize regex: "
                    + mapping_exp
                    + " in environment properties.");
        }

        if (matcher.matches(buffer, pattern)) {
            return matcher.getMatch().group(group);
        }

        return null;
    }

    private class IDObjectivePair {
        private String ID;
        private String objective;

        public IDObjectivePair(String ID, String objective) {
            this.ID = ID;
            this.objective = objective;
        }

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getObjective() {
            return objective;
        }

        public void setObjective(String objective) {
            this.objective = objective;
        }

        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof IDObjectivePair))
                return false;
            if (this == obj)
                return true;
            IDObjectivePair pair = (IDObjectivePair)obj;
            return pair.getID().equals(this.getID()) && pair.getObjective().equals(this.getObjective());
        }
    }

}
