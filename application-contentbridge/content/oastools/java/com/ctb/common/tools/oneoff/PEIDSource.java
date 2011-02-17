package com.ctb.common.tools.oneoff;

import com.ctb.common.tools.SystemException;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * User: mwshort
 * Date: Dec 24, 2003
 * Time: 10:40:53 AM
 * 
 *
 */
public class PEIDSource {

    Map leafMap = new HashMap();

    public PEIDSource(File file) {
        loadFile(file);
    }

    private void loadFile(File file) {
        CSVLineReader lineReader = new CSVLineReader(",","|",",");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while(reader.ready()) {
                SourceLeaf leaf = new SourceLeaf(lineReader.readLine(reader.readLine()));
                leafMap.put(leaf.getThinkColumn(),leaf);
            }
            reader.close();

        } catch (IOException e) {
            throw new SystemException(e.getMessage(),e);
        }
    }
    public Map getLeafMap() {
        return leafMap;
    }


    public Set getLeafSet() {
        Set set = new TreeSet(new SourceLeafComparator());
        set.addAll(leafMap.values());
        return set;
    }

    public class SourceLeafComparator implements Comparator {

        public int compare(Object o1, Object o2) {
            return compareLeafs((SourceLeaf)o1,(SourceLeaf)o2);
        }

        private int compareLeafs(SourceLeaf leaf1,SourceLeaf leaf2) {
            return leaf1.getThinkColumn().compareTo(leaf2.getThinkColumn());
        }
    }
}
