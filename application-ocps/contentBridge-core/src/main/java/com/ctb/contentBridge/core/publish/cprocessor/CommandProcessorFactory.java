package com.ctb.contentBridge.core.publish.cprocessor;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom.Element;

import com.ctb.contentBridge.core.domain.Configuration;
import com.ctb.contentBridge.core.publish.cprocessor.decorator.CommandProcessorHierarchyReportingDecorator;
import com.ctb.contentBridge.core.publish.dao.ProductConfig;
import com.ctb.contentBridge.core.publish.itemmap.csv.MappingProcessor;
import com.ctb.contentBridge.core.publish.itemmap.csv.MappingWriter;
import com.ctb.contentBridge.core.publish.mapping.ItemMap;
import com.ctb.contentBridge.core.publish.mapping.Objectives;
import com.ctb.contentBridge.core.publish.sofa.ScorableItemConfig;
import com.ctb.contentBridge.core.publish.tools.OpenDeployConfig;
import com.ctb.contentBridge.core.publish.xml.item.ItemProcessor;


public class CommandProcessorFactory {

    public static CommandProcessor newCommandProcessorImportAndMapItems(Connection connection,
            Element rootElement, Objectives[] objectivesArray, ItemMap[] itemMapArray,
            String localImageArea, String imageArea, int validationMode, OpenDeployConfig odConfig) {
        CommandProcessor cp = new CommandProcessorImportAndMapItems(rootElement, localImageArea,
                imageArea, validationMode, odConfig, objectivesArray, itemMapArray, connection);
        return cp;
    }

    public static CommandProcessor newCommandProcessorLoadItems(Connection connection,
            Element rootElement, ItemProcessor ip) {
        CommandProcessor cp = new CommandProcessorLoadItems(rootElement, ip, connection);
        return cp;
    }

    public static CommandProcessor newCommandProcessorImportSubTests(Connection connection,
            Element rootElement, Objectives objectives, ItemMap itemMap) {
        CommandProcessor cp = new CommandProcessorImportSubTests(objectives, itemMap, rootElement,
                connection);
        return cp;
    }

    public static CommandProcessor newCommandProcessorImportAssessment(Connection connection,
            Element rootElement, Objectives objectives, ItemMap itemMap,
            ScorableItemConfig scoringConfig, ProductConfig productConfig) {
        return CommandProcessorFactory.newCommandProcessorImportAssessment(connection, rootElement,
                objectives, itemMap, scoringConfig, productConfig, false, new ArrayList(), /*new ADSConfig(),*/new Configuration(), new String(), new String() );
    }
    
    public static CommandProcessor newCommandProcessorImportAssessment(Connection connection,
            Element rootElement, Objectives objectives, ItemMap itemMap,
            ScorableItemConfig scoringConfig, ProductConfig productConfig, boolean doSubtestMedia,
            ArrayList unicodeList, /*ADSConfig adsConfig,*/Configuration config, String maxpanelWidth, String includeAcknowledgment ) {
        CommandProcessor cp = new CommandProcessorImportAssessment(objectives, itemMap,
                rootElement, connection, scoringConfig, productConfig, doSubtestMedia, unicodeList, /*adsConfig,*/config, maxpanelWidth, includeAcknowledgment);
        return cp;
    }

    public static CommandProcessor newCommandProcessorMappingHierarchy(Objectives objectives,
            ItemMap itemMap) {
        return new CommandProcessorCreateMappingHierarchy(objectives, itemMap);
    }

    public static CommandProcessor newCommandProcessorFlattenedMappingHierarchy(
            Objectives objectives, ItemMap itemMap) {
        return new CommandProcessorCreateFlattenedMappingHierarchy(objectives, itemMap);
    }

    public static CommandProcessor newCommandProcessorProductReport(String mappingFileName,
            Connection targetConnection) {
        CommandProcessor cp = new CommandProcessorProductReport(mappingFileName, targetConnection);
        return cp;
    }

    public static CommandProcessor newCommandProcessorRoundTrip(ItemMap itemMap, ItemProcessor ip,
            Connection targetConnection) {
        CommandProcessor cp = new CommandProcessorRoundTrip(itemMap, ip, targetConnection);
        return cp;
    }

    public static CommandProcessor newCommandProcessorReoundTrip(ItemMap itemMap, ItemProcessor ip,
            Connection targetConnection) {
        return new CommandProcessorRoundTrip(itemMap, ip, targetConnection);
    }

    public static CommandProcessor newCommandProcessorMapMerge(Iterator csvEntries,
            Iterator itemMapRows, MappingProcessor mappingProcessor, MappingWriter writer,
            int numRows) {
        return new CommandProcessorTestMapMerge(csvEntries, itemMapRows, mappingProcessor, writer,
                numRows);
    }

    public static CommandProcessor newCommandProcessorMapCompare(List srcCsvEntries,
            List tgtCsvEntries) {
        return new CommandProcessorTestMapCompare(srcCsvEntries, tgtCsvEntries);

    }

    public static CommandProcessor newCommandProcessorTestMapWriteCSVDecorated(Iterator iterator,
            MappingProcessor mappingProcessor, MappingWriter writer, int numRows) {
        return new CommandProcessorHierarchyReportingDecorator(new CommandProcessorTestMapWriteCSV(
                iterator, mappingProcessor, writer, numRows));
    }

    public static CommandProcessor newCommandProcessorPdfThroughFile(Element rootElement,
            File xslFile, String outFileName, String pdfType, String processStep) {
        return new CommandProcessorPdfThroughFile(rootElement, xslFile, outFileName, pdfType,
                processStep);
    }

    public static CommandProcessor newCommandProcessorGenerateMediaForEditorReview(File infile,
            String rootDirectory, String directory, String pdfOutFile, String flashOutFile) {
        return new CommandProcessorGenerateMediaForEditorReview(infile, rootDirectory, directory,
                pdfOutFile, flashOutFile);
    }

    public static CommandProcessor newCommandProcessorGeneratePDF(File infile,
            String rootDirectory, String directory, String pdfOutFile) {
        return new CommandProcessorGeneratePDF(infile, rootDirectory, directory, pdfOutFile);
    }

}