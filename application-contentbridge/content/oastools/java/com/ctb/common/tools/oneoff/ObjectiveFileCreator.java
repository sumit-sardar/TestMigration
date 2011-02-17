package com.ctb.common.tools.oneoff;

import java.util.Set;
import java.util.Iterator;
import java.io.PrintStream;

/**
 * User: mwshort
 * Date: Dec 24, 2003
 * Time: 11:36:14 AM
 * 
 *
 */
public class ObjectiveFileCreator {

    private Set leafSet;
    private static final String DELIMITOR = "::";
    private static final String QUOTE = "\"";
    private static final String NEWLINE = "\n";
    private static final String INDENT = "\t";
    private PrintStream ps = System.out;

    private int TAB_COUNT = 0;
    private SourceLeaf lastLeaf = null;

    public ObjectiveFileCreator(Set leafSet) {
        this.leafSet = leafSet;
    }

    public void print() {
        for (Iterator iter = leafSet.iterator(); iter.hasNext();) {
            SourceLeaf currentLeaf = (SourceLeaf)iter.next();
            checkStateManagement(currentLeaf);
        }

    }

    public void print(PrintStream ps) {
       this.ps = ps;
       print();
    }

    public void checkStateManagement(SourceLeaf currentLeaf) {
        if (lastLeaf == null)
            writeFrameworkCode(currentLeaf);
        if ( lastLeaf == null ||!lastLeaf.getGrade().equals(currentLeaf.getGrade()))
            writeNewGrade(currentLeaf);
        if ( lastLeaf == null ||!lastLeaf.getContentArea().equals(currentLeaf.getContentArea()))
            writeNewContentArea(currentLeaf);
        if ( lastLeaf == null || !lastLeaf.getObjectiveID().equals(currentLeaf.getObjectiveID()))
            writeNewObjective(currentLeaf);
        if ( lastLeaf == null || !lastLeaf.getSubskillID().equals(currentLeaf.getSubskillID()) )
            writeNewSubskill(currentLeaf);
//        if (lastLeaf == null || !lastLeaf.getThinkID().equals(currentLeaf.getThinkID()) )
//            writeNewThinkTitle(currentLeaf);
        lastLeaf = currentLeaf;
    }

    private void writeFrameworkCode(SourceLeaf leaf) {
        writeNewLevel(1,"Terranova",leaf.getFrameworkCode(),"0");
    }
    private void writeNewGrade(SourceLeaf leaf) {
        writeNewLevel(2,"Grade " + leaf.getGrade(),leaf.getGrade(),leaf.getFrameworkCode());
    }
    private void writeNewContentArea(SourceLeaf leaf) {
        writeNewLevel(3,leaf.getContentTitle(),leaf.getContentArea(),leaf.getGrade());
    }
    public void writeNewObjective(SourceLeaf leaf) {
        writeNewLevel(4,leaf.getObjectiveTitle(),leaf.getObjectiveID(),leaf.getContentArea());
    }
    public void writeNewSubskill(SourceLeaf leaf) {
        writeNewLevel(5,leaf.getSubskillTitle(),leaf.getSubskillID(),leaf.getObjectiveID());
    }
    public void writeNewThinkTitle(SourceLeaf leaf) {
        writeNewLevel(6,leaf.getThinkTitle(),leaf.getThinkID(),leaf.getSubskillID());
    }

    private void writeNewLevel(int level, String descrip, String code, String parentCode) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < level -1 ; i++)
            buffer.append(INDENT);
        ps.println(buffer.toString() + QUOTE + descrip + QUOTE + DELIMITOR + QUOTE + code +
                QUOTE + DELIMITOR + QUOTE  + parentCode + QUOTE + DELIMITOR + QUOTE + level + QUOTE);
    }

}
