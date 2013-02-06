/*
 * Created on Nov 5, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ctb.contentBridge.core.publish.roundtrip;

import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

import com.ctb.contentBridge.core.domain.Configuration;
import com.ctb.contentBridge.core.exception.SystemException;
import com.ctb.contentBridge.core.publish.dao.DBConfig;
import com.ctb.contentBridge.core.publish.dao.DBMediaGateway;
import com.ctb.contentBridge.core.publish.dao.ItemWriterOASDatabase;
import com.ctb.contentBridge.core.publish.hibernate.HibernateSession;
import com.ctb.contentBridge.core.publish.hibernate.HibernateUtils;
import com.ctb.contentBridge.core.publish.iknowxml.R2DocumentBuilder;
import com.ctb.contentBridge.core.publish.mapping.Mapper;
import com.ctb.contentBridge.core.publish.mapping.MapperFactory;
import com.ctb.contentBridge.core.publish.media.Media;
import com.ctb.contentBridge.core.publish.report.CommandReport;
import com.ctb.contentBridge.core.publish.report.HierarchyReport;
import com.ctb.contentBridge.core.publish.report.ItemProcessorReport;
import com.ctb.contentBridge.core.publish.report.Report;
import com.ctb.contentBridge.core.publish.xml.item.ItemAssembler;
import com.ctb.contentBridge.core.publish.xml.item.ItemMapper;
import com.ctb.contentBridge.core.publish.xml.item.ItemMapperMapOnly;
import com.ctb.contentBridge.core.publish.xml.item.ItemMediaCache;
import com.ctb.contentBridge.core.publish.xml.item.ItemMediaGenerator;
import com.ctb.contentBridge.core.publish.xml.item.ItemProcessorGeneric;
import com.ctb.contentBridge.core.publish.xml.item.ItemWriter;
import com.ctb.contentBridge.core.publish.xml.item.ItemWriterDummy;
import com.ctb.contentBridge.core.publish.xml.item.OpenDeployDummy;


/**
 * @author wmli
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class RoundTripProcessingCommand {
    private static Logger logger =
        Logger.getLogger(RoundTripProcessingCommand.class);
    private static final String ROOT_CURRICULUM_ID_PATH =
        ".//Hierarchy[@Type='Root']/@CurriculumID";

    public void process(
        String commandString,
        String sourceEnv,
        String targetEnv,
        String frameworkCode,
        String mappingData,
        Mapper mapper)
        throws Exception {

        HierarchyReport hierarchyReport = new HierarchyReport();
        HierarchyReport.setCurrentReport(hierarchyReport);
        hierarchyReport.setObjectives(MapperFactory.getObjectives());
        hierarchyReport.setItemMap(MapperFactory.getItemMap());

        ((CommandReport) getReport()).addSubReport(hierarchyReport);

        Connection sourceConnection =
            new DBConfig(new File(sourceEnv + ".properties")).openConnection();
        Connection targetConnection =
            new DBConfig(new File(targetEnv + ".properties")).openConnection();

        for (Iterator iter = MapperFactory.getItemMap().getAllItemIDs();
            iter.hasNext();
            ) {

            ItemProcessorReport report = new ItemProcessorReport();
            ItemProcessorReport.setCurrentReport(report);
            report.setSuccess(false);

            String itemId = (String) iter.next();
            report.setID(itemId);

            logger.info("Process item: [" + itemId + "]");

            DBMediaGateway mediaReader =
                new DBMediaGateway(
                    HibernateUtils.getSession(
                        sourceConnection,
                        HibernateSession.SOURCE_CONNECTION));
            ItemMediaCache itemMediaCache = new ItemMediaCache();

            try {
                processItem(
                    itemId,
                    frameworkCode,
                    mediaReader,
                    itemMediaCache,
                    createItemProcessorMap(
                        itemMediaCache,
                        createWriter(commandString, targetConnection),
                        createOperation(commandString)));
            } catch (Exception e) {
                report.setException(e);
                hierarchyReport.setSuccess(false);
                logger.error("Fail to process item [" + itemId + "]", e);
            }

            HierarchyReport.getCurrentReport().addSubReport(
                ItemProcessorReport.getCurrentReport());
        } //end for loop

        // setup the connection in the report for report generation.
        //roundTripReport.setConnection(new DBConnection(targetConnection));
    }

    private String createOperation(String commandString) {
        return "roundtrip " + commandString;
    }

    public Report getReport() {
        return CommandReport.getCurrentReport();
    }

    private List getItemIdListFromItemMap(
        String frameworkCode,
        String mappingData)
        throws Exception {
        final String ITEM_MAP_EXP_LONG = "\"([^\"]*)\",.*";
        final String ITEM_MAP_EXP = "([^,]*),.*";

        List itemIds = new ArrayList();

        BufferedReader reader =
            new BufferedReader(
                new FileReader(
                    new File(
                        mappingData + "/" + frameworkCode + "/item_map.txt")));
        String buffer = null;

        while ((buffer = reader.readLine()) != null) {
            String itemId = null;
            if ((itemId = getItemIdFromMapping(ITEM_MAP_EXP_LONG, buffer))
                == null) {
                itemId = getItemIdFromMapping(ITEM_MAP_EXP, buffer);
            }

            if (itemId != null) {
                itemIds.add(itemId);
            }
        }

        return itemIds;
    }

    private String getItemIdFromMapping(String mapping_exp, String buffer) {
        PatternCompiler compiler = new Perl5Compiler();
        PatternMatcher matcher = new Perl5Matcher();
        Pattern pattern = null;

        try {
            pattern =
                compiler.compile(
                    mapping_exp,
                    Perl5Compiler.CASE_INSENSITIVE_MASK);
        } catch (MalformedPatternException e) {
            throw new SystemException(
                "Cannot reconize regex: "
                    + mapping_exp
                    + " in environment properties.");
        }

        if (matcher.matches(buffer, pattern)) {
            return matcher.getMatch().group(1);
        }

        return null;
    }

    private ItemProcessorGeneric createItemProcessorMap(
        ItemMediaGenerator mediaGenerator,
        ItemWriter writer,
        String operation) {
        ItemAssembler itemAssember =
            new ItemAssembler(ItemAssembler.PARSE_ALLOW_DEFAULTS);

        ItemMapper mapper =
            new ItemMapperMapOnly(
                ItemAssembler.PARSE_ALLOW_DEFAULTS,
                MapperFactory.getObjectives(),
                MapperFactory.getItemMap());

        return new ItemProcessorGeneric(
            new OpenDeployDummy(),
            itemAssember,
            itemAssember,
            mapper,
            mediaGenerator,
            writer,
            operation,
            new ArrayList(),
            /*new ADSConfig(),*/
            null,
            new String(),
            new String());
    }

    private ItemWriter createWriter(String command, Connection connection)
        throws Exception {
        if (command.equals("map")) {
            return new ItemWriterOASDatabase(connection);
        } else {
            return new ItemWriterDummy();
        }
    }

    private void processItem(
        String itemId,
        String frameworkCode,
        MediaReader mediaReader,
        ItemMediaCache itemMediaCache,
        ItemProcessorGeneric itemProcessorMap)
        throws Exception {

        // retrieve the media from the src db
        Media media = mediaReader.readMedia(itemId);

        if (media.getXml() == null) {
            throw new SystemException("Cannot retrieve media.");
        }

        // cache the media
        itemMediaCache.addMedia(itemId, media);

        // build the media using regular builder
        SAXBuilder builder =
            new SAXBuilder(R2DocumentBuilder.SAX_PARSER_NAME);

        Document doc = null;
        doc = builder.build(new CharArrayReader(media.getXml()));

        Element rootElement = doc.getRootElement();

        // process the media
        if (!isStateItem(frameworkCode, rootElement)) {
            itemProcessorMap.process(rootElement);
            ItemProcessorReport.getCurrentReport().setSuccess(true);
        } else {
            throw new SystemException("This is a state item.");
        }

    }

    public boolean isStateItem(String frameworkCode, Element rootElement) {
        // extract the CurriculumID
        try {
            String rootCurriculumID =
                getValueForXPath(rootElement, ROOT_CURRICULUM_ID_PATH);

            if (rootCurriculumID != null) {
                return rootCurriculumID.equals(frameworkCode);
            }

            return false;
        } catch (Exception e) {
            throw new SystemException(
                "Cannot determine if the current item is a state item.");
        }
    }

    private String getValueForXPath(Element element, String path)
        throws Exception {
        String value = null;

        Object node = null;

        XPath xpath = XPath.newInstance(path);
        node = xpath.selectSingleNode(element);

        if (node != null) {
            if (node instanceof Element) {
                value = ((Element) node).getText();
            } else if (node instanceof Attribute) {
                value = ((Attribute) node).getValue();
            }
        }
        return value;
    }
}
