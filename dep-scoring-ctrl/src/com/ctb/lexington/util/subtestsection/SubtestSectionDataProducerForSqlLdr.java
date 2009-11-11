package com.ctb.lexington.util.subtestsection;

import java.util.List;
import java.util.Iterator;
import java.util.Arrays;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;

import com.ctb.lexington.util.Stringx;


/**
 * REDTAG
 *      This functionality should really be part of OCS, but is temporarily in OAS.
 *
 * There is a shell script to run this program in bin/createSubtestSectionData.sh
 *
 * This class runs a process to create a CSV file that contains subtest sectioning information.
 * The data is environment dependent, so this class requires run time access to the OAS DB.
 * See: oasx/database/oas/projects/pitcairn/item_set_section.ctl for the SQLLDR control file
 * that loads the data produced by this program to the DB.
 *
 * @author mnkamiya
 * @version $Id$
 */
public class SubtestSectionDataProducerForSqlLdr {
    private static final String DELIM = ",";
    private static final String NEWLINE = "\n";

    private static String sourceFile=null;
    private static String outputFile=null;
    private static List subtests;

    public static void main(String[] args) {
        parseCommandLineArgs(args);
        try {
            readSubtestDataFromXmlFile();
            retrieveSubtestDataFromDB();
            ArrayList excludedSubtests = writeOutputCsvFile();
            if (!excludedSubtests.isEmpty()) {
                System.err.println("********************************* WARNING: ***********************************************");
                System.err.println(excludedSubtests.size() + " subtests were excluded from the output file due to errors.");
                System.err.println("This may be due to incomplete content in this environment, or errors in the metadata file.");
                System.err.println("Excluded subtests:");
                for(int i=0;i<excludedSubtests.size();i++) {
                    System.err.println("\t" + excludedSubtests.get(i));
                }
                System.err.println("******************************************************************************************");
                System.exit(-1);
            }
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-2);
        }
    }

    private static void parseCommandLineArgs(String[] args) {
        if (args.length != 4 ) {
            System.err.println("Arguments passed in: " + Stringx.doubleQuote(Arrays.asList(args)));
            System.err.println("Usage: sectionExport -s <source.xml> -o <output.csv>");
            System.err.println("\t-s metadata_file\n\t\t\t\tXML file that contains the metadata for the sections.");
            System.err.println("\t-o output_file\n\t\t\t\tName of the file to output generated data set to.");
            System.exit(-2);
        }
        for (int i = 0; i < args.length; i++) {
            if ("-s".equals(args[i])) {
                sourceFile = args[++i];
            } else if ("-o".equals(args[i])) {
                outputFile = args[++i];
            }
        }
    }

    private static void readSubtestDataFromXmlFile() {
        //System.out.println("Reading meta-data from file...");
        final SectionMetaDataReader reader = new SectionMetaDataReader(sourceFile);
        subtests = reader.getSubtests();
    }

    private static void retrieveSubtestDataFromDB() {
        //System.out.println("Retrieving subtest data from database...");
        final SubtestDataRetriever retriever = new SubtestDataRetriever();
        retriever.getSubtestSectionInfo(subtests);
    }

    private static ArrayList writeOutputCsvFile() throws IOException {
        //System.out.println("Writing output CSV file...");
        final FileWriter writer = new FileWriter(outputFile);
        ArrayList excludedSubtests = new ArrayList();
        writer.write("ITEM_SET_SECTION_ID,ITEM_SET_ID,SEQUENCE,SECTION_NAME,FIRST_ITEM_ID,FIRST_ITEM_ORDER_NUM,LAST_ITEM_ID,LAST_ITEM_ORDER_NUM,TIME_LIMIT,THEME_PAGE_COUNT\n");
        int sectionCount=1;
        for (Iterator i=subtests.iterator();i.hasNext();) {
            Subtest subtest = (Subtest) i.next();
            if (!validSubtest(subtest)) {
                excludedSubtests.add(subtest.getName());
                continue;
            }
            for (Iterator j=subtest.getSections().iterator();j.hasNext();) {
                Section section = (Section) j.next();
                StringBuffer buff = new StringBuffer();
                buff.append(sectionCount++);
                buff.append(DELIM);
                buff.append(subtest.getItemSetId());
                buff.append(DELIM);
                buff.append(section.getOrder());
                buff.append(DELIM);
                buff.append(section.getName());
                buff.append(DELIM);
                buff.append(section.getFirstItemId());
                buff.append(DELIM);
                buff.append(section.getFirstItemNumber());
                buff.append(DELIM);
                buff.append(section.getLastItemId());
                buff.append(DELIM);
                buff.append(section.getLastItemNumber());
                buff.append(DELIM);
                buff.append(section.getTimeLimit());
                buff.append(DELIM);
                buff.append(section.getThemePageCount());
                buff.append(NEWLINE);
                writer.write(buff.toString());
            }
        }
        writer.flush();
        writer.close();
        return excludedSubtests;
    }

    private static boolean validSubtest(final Subtest subtest) {
        if (subtest.getItemSetId()==null)
            return false;
        else {
            for (Iterator j=subtest.getSections().iterator();j.hasNext();) {
                Section section = (Section) j.next();
                if (!validSection(section))
                    return false;
            }
            return true;
        }
    }

    private static boolean validSection(final Section section) {
        if (section.getFirstItemId()==null)
            return false;
        else if (section.getLastItemId()==null)
            return false;
        else
            return true;
    }
}
