package com.ctb.common.tools.oneoff.peid;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class ObjectiveBuilder {

    public static void main(String[] args) throws IOException {
        build("TB", "TABE", "/csv/TABE9_SV.csv");
    }

    public static void build(String root, String rootName, String fileName)
            throws IOException {
        // read row from file
        Map objectives = new HashMap();

        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        
        File outputFile = new File("./TABE_SURVEY.csv");       
        BufferedWriter outputter = new BufferedWriter(new FileWriter(
                outputFile));
        
        while (reader.ready()) {
            PEIDExportRecord record = new PEIDExportRecord(root, rootName,
                    CSVLineReader.readLine(reader.readLine()));

            //            System.out.println("\"" + record.getItemId() + "\",\""
            //                    + record.getMap().getId() + "\"");

          

            outputter.write(record.getItemId());
            outputter.write(",");
            outputter.write(record.getMap().getId());
            outputter.write(",");

            for (Iterator iter = record.getObjectiveHierarchy().iterator(); iter
                    .hasNext();) {
                PEIDObjective objective = (PEIDObjective) iter.next();
                outputter.write(objective.getId());
                outputter.write(",\"");
                outputter.write(objective.getDescription());
                outputter.write("\",");
            }

            outputter.write(record.getAnswerKey());
            outputter.write("\n");
            
            
            List hierarchy = record.getObjectiveHierarchy();

            PEIDObjective parent = null;

            for (Iterator iter = hierarchy.iterator(); iter.hasNext();) {
                PEIDObjective objective = (PEIDObjective) iter.next();

                if (objectives.get(objective.getId()) != null) {
                    objective = (PEIDObjective) objectives.get(objective
                            .getId());
                } else {
                    objectives.put(objective.getId(), objective);
                    objective.setParent(parent);
                }

                parent = objective;
            }
        }
        
        outputter.close();

        //        PEIDObjective rootCurr = (PEIDObjective) objectives.get(root);
        //        rootCurr.traverse(new ObjectiveWriter());

    }

    static class ObjectiveWriter implements ObjectiveVisitor {
        public void visitObjective(PEIDObjective objective) {

            System.out.print(StringUtils.repeat("\t", objective
                    .getCategoryLevel() - 1));
            System.out.print("\"");
            System.out.print(objective.getDescription());
            System.out.print("\"::\"");
            System.out.print(objective.getId());
            System.out.print("\"::\"");
            System.out.print(objective.getParent() == null ? "0" : objective
                    .getParent().getId());
            System.out.print("\"::\"");
            System.out.print(objective.getCategoryLevel());
            System.out.println("\"");
        }
    }
}