package com.ctb.contentBridge.core.publish.mapping;


import java.util.*;


public interface ObjectiveBuilder {
    public Objective buildFromLine(String line, Map levelNameMap, int lineNumber);
}
