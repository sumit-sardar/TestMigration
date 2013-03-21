package com.ctb.contentBridge.core.publish.mapping;


import java.util.*;

import com.ctb.contentBridge.core.exception.SystemException;
import com.ctb.contentBridge.core.util.ObjectiveFileTokenizer;



public class LongFormatBuilder implements ObjectiveBuilder {

    public Objective buildFromLine(String line, Map levelNameMap, int lineNumber) { {
            ObjectiveFileTokenizer toker = new ObjectiveFileTokenizer(line);

            try {
                String name = toker.nextToken();
                String curriculumID = toker.nextToken();
                String nodeKey = toker.nextToken();
                String parentKey = toker.nextToken();
                Integer level = new Integer(toker.nextToken());

                if (toker.hasMoreTokens()) {
                    throw new SystemException("Extra items in line:  " + line);
                }
                return new Objective(name, curriculumID, nodeKey, parentKey,
                        level, levelNameMap);
            } catch (NumberFormatException e) {
                throw new SystemException("Cannot parse line(" + lineNumber + "):  " + line);
            } catch (NoSuchElementException e) {
                throw new SystemException("Cannot parse line(" + lineNumber + "):  " + line);
            }

        }
    }
}
