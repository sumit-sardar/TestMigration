package com.ctb.mapping;


import java.util.*;


public interface ObjectiveBuilder {
    public Objective buildFromLine(String line, Map levelNameMap, int lineNumber);
}
