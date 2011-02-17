package com.ctb.common.teamsite;


import java.io.*;
import java.util.*;

import org.jdom.*;
import org.jdom.input.*;

import com.ctb.common.tools.*;
import com.ctb.util.*;


/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Sep 23, 2003
 * Time: 3:08:26 PM
 * To change this template use Options | File Templates.
 */
public class TeamSiteImporter {
    final static public String ITEM_SET = "ItemSet";
    private TSItemWriter tsWriter = null;
    private TeamSiteImporterReport report;
    private TeamSiteCommand tsi = null;

    public void importCommand(TeamSiteCommand tsi) {
        tsWriter = new TSItemWriter(tsi.outDir);
        this.tsi = tsi;
        importFile(tsi.itemSetFile);

    }

    public void importFile(File itemSetFile) {
        try {
            importFromReader(new InputStreamReader(new FileInputStream(itemSetFile)));
        } catch (FileNotFoundException e) {
            throw new SystemException(e.getMessage(), e);
        }
    }

    public void importFromReader(Reader reader) {

        SAXBuilder builder = new SAXBuilder("org.apache.xerces.parsers.SAXParser",
                false);
        // todo: what ignores and settings does bobby need?
        Document document = null;

        try {
            document = builder.build(reader);
        } catch (JDOMException e) {
            throw new SystemException(e.getMessage(), e);
        } catch (IOException e) {
            throw new SystemException(e.getMessage(), e);
        }
        Element rootElement = document.getRootElement();

        if (!rootElement.getName().equals(ITEM_SET)) {
            throw new SystemException("XML File must have " + ITEM_SET
                    + " as the root element");
        }
        writeItems(rootElement);
    }

    private void writeItems(Element itemSetElement) {
        report = new TeamSiteImporterReport();
        List partitionedSets = getItemPartitionsFromItemSet(itemSetElement);
        int i = 0;

        for (Iterator iter = partitionedSets.iterator(); iter.hasNext();) {
            Set setOfItems = (Set) iter.next();
            TeamSiteImporterReportData data = tsWriter.writeSetOfItems(setOfItems);

            report.addData(data);
        }
    }

    public TeamSiteImporterReport getReport() {
        return report;
    }

    private List getItemPartitionsFromItemSet(Element itemSetElement) {
        // now partition them

        final String contentAreaExp = ".//Hierarchy[@Type= \"Content Area\"]/@Name";
        final String gradeLevelExp = ".//Hierarchy[@Type= \"Grade\"]/@Name";
        final String elementsOfType = "Item";
        final String uniquenessAttribute = "ID";
        String[] expressions = {contentAreaExp, gradeLevelExp};
        JDOMElementComparator comparator = new JDOMElementComparator(expressions);
        com.ctb.util.JDOMElementPartitioner ip = new com.ctb.util.JDOMElementPartitioner(uniquenessAttribute,
                comparator);

        ip.applyUniquenessConstraintFirst(true);
        NodeFilterChain filterChain = new NodeFilterChain();

        filterChain.addFilter(new ElementNodeFilter());
        if (tsi != null && tsi.stateCode != null) {
            filterChain.addFilter(new StateNumberCodeFilter(tsi.stateCode));
        }
        ip.setNodeFilter(filterChain);
        List partitionedSets = ip.getPartitionedSets(itemSetElement,
                elementsOfType);

        report.addData(tsWriter.writeDuplicates(ip.getDuplicates()));
        return partitionedSets;
    }

}
