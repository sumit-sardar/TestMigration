package com.ctb.common.teamsite;


import java.io.*;
import java.util.*;

import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;
import org.jdom.xpath.*;

import com.ctb.common.tools.*;
import com.ctb.util.iknowxml.*;


/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Sep 23, 2003
 * Time: 3:45:54 PM
 * To change this template use Options | File Templates.
 */
public class TSItemWriter {

    List list = new ArrayList();
    private File outDir;

    public TSItemWriter(File outDir) {
        this.outDir = outDir;
    }

    public TeamSiteImporterReportData writeSetOfItems(Collection setOfItems) {
        TeamSiteImporterReportData data = new TeamSiteImporterReportData();

        try {
            addInfoToReportData(data, setOfItems);
            writeItemSet(data, setOfItems);
        } catch (Exception e) {
            data.setError(e);
        }
        return data;
    }

    private void writeItemSet(TeamSiteImporterReportData data, Collection setOfItems) {
        File file = new File(outDir, data.getFileName());
        Element rootElement = ElementNester.getDefaultItemSetElement();
        Document document = new Document(rootElement,
                new DocType(rootElement.getName(), IOUtils.DTD_NAME));

        for (Iterator iter = setOfItems.iterator(); iter.hasNext();) {
            Element element = (Element) iter.next();

            element.detach();
            list.add(element.getAttributeValue("ID"));
            rootElement.addContent(element);
        }

        XMLOutputter outputter = new XMLOutputter();

        try {
            OutputStream fos = new FileOutputStream(file);

            outputter.output(document, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            throw new SystemException(e.getMessage(), e);
        }
        validateFileAgainstDTD(file);

    }

    public TeamSiteImporterReportData writeDuplicates(Collection setOfItems) {
        TeamSiteImporterReportData data = new TeamSiteImporterReportData();

        try {
            addInfoToReportData(data, setOfItems);
            writeItemSet(data, setOfItems);
        } catch (Exception e) {
            data.setError(e);
        }
        data.setCritieriaString("Duplicates");
        data.setFileName("Duplicates.xml");
        return data;
    }

    private boolean validateFileAgainstDTD(File file) {
        SAXBuilder builder = new SAXBuilder("org.apache.xerces.parsers.SAXParser",
                true);

        try {
            builder.build(new InputStreamReader(new FileInputStream(file)));
        } catch (JDOMException e) {
            throw new SystemException("INVALID XML WRITE:" + e.getMessage(), e);
        } catch (IOException e) {
            throw new SystemException(e.getMessage(), e);
        }
        return true;
    }

    public void addInfoToReportData(TeamSiteImporterReportData data, Collection items) {
        List itemIDs = new ArrayList();
        Element ele = null;

        for (Iterator iter = items.iterator(); iter.hasNext();) {
            ele = (Element) iter.next();
            itemIDs.add(ele.getAttributeValue("ID"));
        }

        String criteriaString = getContentArea(ele) + "\t" + getGradeLevel(ele);

        data.setCritieriaString(criteriaString);
        data.setFileName(data.getCritieriaString().replace('\t', '_').replace(' ',
                '-')
                + ".xml");
        data.setItemIDList(itemIDs);
        data.setNumOfItems(items.size());
    }

    private String getContentArea(Element ele) {
        final String contentAreaExp = ".//Hierarchy[@Type= \"Content Area\"]/@Name";

        return getAttributeValue(ele, contentAreaExp);
    }

    private String getGradeLevel(Element ele) {
        final String gradeLevelExp = ".//Hierarchy[@Type= \"Grade\"]/@Name";

        return getAttributeValue(ele, gradeLevelExp);
    }

    private String getAttributeValue(Element ele, String xpathExp) {
        try {
            XPath query = XPath.newInstance(xpathExp);
            Attribute att = (Attribute) query.selectSingleNode(ele);

            return att.getValue();
        } catch (JDOMException e) {
            throw new SystemException("Could not get attribute with query: "
                    + xpathExp);
        }
    }

}
