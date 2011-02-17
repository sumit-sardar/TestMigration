package com.ctb.common.tools.itemxml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

import com.ctb.util.iknowxml.R2XmlOutputter;

/**
 * @author wmli
 */

public class StimulusChecker {

    public static void main(String argv[]) {
        File cacheDir = new File(argv[0]);

		System.out.println("Start Time: " + new Date());
        // read all the stimulus from the file
        List result = new ArrayList();
		System.out.println("Read XML ... ");
        readStimulusInfoFromCache(result, cacheDir);

        // group the stimulus by index
        Map indexMap = groupStimulusByIndex(result);

        // write the group with size greater then one
        System.out.println("Write to output ...");
        writeSimilarStimuli(indexMap);
        System.out.println("Done.");
		System.out.println("End Time: " + new Date());
    }

    private static void readStimulusInfoFromCache(List result, File cacheDir) {
        String[] cacheList = cacheDir.list();

        StimulusElementComparator comparator = new StimulusElementComparator();

        
        for (int i = 0; i < cacheList.length; i++) {
            File cacheXml = new File(cacheDir, cacheList[i]);

            if (cacheXml.isFile()) {
                Element element = null;
                try {
                    element = new SAXBuilder().build(cacheXml).getRootElement();
                } catch (Exception e) {
                    System.out.println(
                        "Cannot read cached xml: " + cacheXml.getName());
                }

                try {
                    // extract stimulus
                    XPath stimulusXPath = XPath.newInstance("//Stimulus");
                    Object node = stimulusXPath.selectSingleNode(element);

                    // if stimulus existed. process stimulus information
                    if (node != null) {
                        StimulusInfo info = new StimulusChecker.StimulusInfo();
                        info.setId(((Element) node).getAttributeValue("ID"));
                        info.setXml(
                            new R2XmlOutputter().outputString((Element) node));

                        String index = comparator.getComparisonString(element);
                        info.setIndex(index);

                        result.add(info);
                    } else {
                        System.out.println(
                            "Cannot extract stimulus from: "
                                + cacheXml.getName());
                    }

                } catch (Exception e) {

                }
            }
        }
    }

    private static Map groupStimulusByIndex(List result) {
        Map indexMap = new HashMap();
        for (Iterator iter = result.iterator(); iter.hasNext();) {
            StimulusInfo info = (StimulusInfo) iter.next();

            if (indexMap.get(info.getIndex()) == null) {
                indexMap.put(info.getIndex(), new ArrayList());
            }

            ((ArrayList) indexMap.get(info.getIndex())).add(info);
        }
        return indexMap;
    }

    private static void writeSimilarStimuli(Map indexMap) {
        File outputFile = new File("StimulusChecking.txt");
        try {
            BufferedWriter writer =
                new BufferedWriter(new FileWriter(outputFile));

            int counter = 0;
            for (Iterator iter = indexMap.keySet().iterator();
                iter.hasNext();
                ) {

                String key = (String) iter.next();

                if (key.startsWith("#Stimulus[]#Heading[]"))
                    continue;
                if (key.indexOf("100x100") != -1)
                    continue;

                ArrayList infoList = (ArrayList) indexMap.get(key);

                if (infoList.size() > 1) {
                    //writer.write(key);
                    //writer.write("\n");
                    for (Iterator iterator = infoList.iterator();
                        iterator.hasNext();
                        ) {
                        StimulusInfo info = (StimulusInfo) iterator.next();
                        writer.write(info.toString());

                    }

                    writer.write("\n");
                    writer.write(
                        "____________________________________________________\n");
                    counter++;
                }
            }

            writer.write("There are [" + counter + "] similar group.\n");
            writer.flush();
            writer.close();
        } catch (Exception e) {
            System.out.println(
                "Cannot write to output file: " + outputFile.getName());
        }
    }

    static class StimulusInfo implements Comparable {
        String id;
        String xml;
        String index;
        public String getId() {
            return id;
        }

        public String getIndex() {
            return index;
        }

        public String getXml() {
            return xml;
        }

        public void setId(String string) {
            id = string;
        }

        public void setIndex(String string) {
            index = string;
        }

        public void setXml(String string) {
            xml = string;
        }

        public int compareTo(Object o) {
            return index.compareTo(((StimulusInfo) o).getIndex());
        }

        public String toString() {
            return "[" + id + "]:\n\t[" + xml + "]\n";
        }
    }
}