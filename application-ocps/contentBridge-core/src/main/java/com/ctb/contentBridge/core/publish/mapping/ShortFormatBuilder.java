package com.ctb.contentBridge.core.publish.mapping;


import java.util.*;

import com.ctb.contentBridge.core.exception.BusinessException;
import com.ctb.contentBridge.core.util.ObjectiveFileTokenizer;



public class ShortFormatBuilder implements ObjectiveBuilder {

    public Objective buildFromLine(String line, Map levelNameMap, int lineNumber) { {
            ObjectiveFileTokenizer toker = new ObjectiveFileTokenizer(line);

            try {
                String name = toker.nextToken();
                String curriculumID = toker.nextToken().trim();
                String nodeKey = curriculumID.trim();
                String parentKey = toker.nextToken().trim();
                Integer level = new Integer(toker.nextToken());

                if (toker.hasMoreTokens()) {
                    throw new BusinessException("Extra items in line:  " + line);
                }
                return new Objective(name, curriculumID, nodeKey, parentKey,
                        level, levelNameMap);
            } catch (NumberFormatException e) {
                throw new BusinessException("Cannot parse line(" + lineNumber + "):  " + line);
            } catch (NoSuchElementException e) {
                throw new BusinessException("Cannot parse line(" + lineNumber + "):  " + line);
            }

        }
    }
}
