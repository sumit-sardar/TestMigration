/*
 * Created on Dec 5, 2003
 */
package com.ctb.contentBridge.core.publish.command;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom.Element;

import com.ctb.contentBridge.core.domain.Configuration;
import com.ctb.contentBridge.core.exception.SystemException;
import com.ctb.contentBridge.core.publish.cprocessor.CommandProcessor;
import com.ctb.contentBridge.core.publish.cprocessor.CommandProcessorFactory;
import com.ctb.contentBridge.core.publish.cprocessor.CommandProcessorFactoryUtil;
import com.ctb.contentBridge.core.publish.cprocessor.CommandProcessorImportAndMapItemsContentQA;
import com.ctb.contentBridge.core.publish.cprocessor.CommandProcessorImportAssessmentFromContentQA;
import com.ctb.contentBridge.core.publish.cprocessor.CommandProcessorValidateItemXML;
import com.ctb.contentBridge.core.publish.cprocessor.decorator.CommandProcessorReportingDecorator;
import com.ctb.contentBridge.core.publish.cprocessor.decorator.HibernateCommandProcessorTransactionDecorator;
import com.ctb.contentBridge.core.publish.dao.DBConfig;
import com.ctb.contentBridge.core.publish.dao.ProductConfig;
import com.ctb.contentBridge.core.publish.itemmap.csv.MappingProcessor;
import com.ctb.contentBridge.core.publish.itemmap.csv.MappingWriter;
import com.ctb.contentBridge.core.publish.layout.AssessmentLayoutProcessor;
import com.ctb.contentBridge.core.publish.mapping.ItemMap;
import com.ctb.contentBridge.core.publish.mapping.MapperFactory;
import com.ctb.contentBridge.core.publish.mapping.Objectives;
import com.ctb.contentBridge.core.publish.report.Report;
import com.ctb.contentBridge.core.publish.sofa.PremadeTestHolderAssembler;
import com.ctb.contentBridge.core.publish.sofa.ScorableItemConfig;
import com.ctb.contentBridge.core.publish.tools.MappingConfig;
import com.ctb.contentBridge.core.publish.tools.OpenDeployConfig;
import com.ctb.contentBridge.core.publish.xml.XMLUtils;
import com.ctb.contentBridge.core.publish.xml.item.ItemProcessor;
import com.ctb.contentBridge.core.util.FileListUtils;
import com.ctb.contentBridge.core.util.RegexUtils;


public class CIMCommandProcessor {

    public static Report processCommand(CIMCommand cIMCommand) throws Exception {
        CommandProcessor cmdProcessor = getCommandProcessor(cIMCommand);
        return cmdProcessor.process();
    }

    private static CommandProcessor getCommandProcessor(CIMCommand command) throws Exception {
        if (command instanceof CIMCommandCreateMappingHierarchy) {
            return createCommandProcessorMappingHierarchy((CIMCommandCreateMappingHierarchy) command);
        }

        if (command instanceof CIMCommandCreateFlattenedMappingHierarchy) {
            return createCommandProcessorFlattenedMappingHierarchy((CIMCommandCreateFlattenedMappingHierarchy) command);
        }

        if (command instanceof CIMCommandProductReport) {
            return createCommandProcessorProductReport((CIMCommandProductReport) command);
        }

        if (command instanceof AbstractCIMCommandTestMap) {
            return createCommandProcessorTestMap((AbstractCIMCommandTestMap) command);
        }

        if (command instanceof CIMCommandTestMapCompare) {
            return createCommandProcessorTestMapCompare((CIMCommandTestMapCompare) command);
        }

        if (command instanceof AbstractCIMCommandRoundTrip) {
            return createCommandProcessorRoundTrip((AbstractCIMCommandRoundTrip) command);
        }

        if (command instanceof CIMCommandBuildSubTestSofa) {
            return createCommandProcessorImportSubTests((CIMCommandBuildSubTestSofa) command);
        }

        if (command instanceof CIMCommandBuildAssessment) {
            return createCommandProcessorBuildAssessment((CIMCommandBuildAssessment) command);
        }

        if (command instanceof CIMCommandImportAndMapItems) {
            return createCommandProcessorImportAndMapItems((CIMCommandImportAndMapItems) command);
        }

        if (command instanceof AbstractCIMCommandItemsFromFile) {
            return createCommandProcessorItemsFromFile((AbstractCIMCommandItemsFromFile) command);
        }

        if (command instanceof CIMCommandCreatePdfThroughFile) {
            return createCommandProcessorCreatePdfThroughFile((CIMCommandCreatePdfThroughFile) command);
        }

        if (command instanceof CIMCommandGenerateMediaForEditorReview) {
            return createCommandProcessorGenerateMediaForEditorReview((CIMCommandGenerateMediaForEditorReview) command);
        }

        if (command instanceof CIMCommandGeneratePDF) {
            return null;
            //            return createCommandProcessorGeneratePDF(
            //                (CIMCommandGeneratePDF) command);
        }

        if (command instanceof CIMCommandValidateItemXML) {
            return createCommandProcessorValidateItemXML((CIMCommandValidateItemXML) command);
        }

        throw new SystemException("Don't know how to process CIMCommand: "
                + command.getCommandName());
    }

    /* ********************************************************************* */

    public static CommandProcessor decorate(String name, String file, Connection targetConnection,
            CommandProcessor cp) {
        HibernateCommandProcessorTransactionDecorator hcp = new HibernateCommandProcessorTransactionDecorator(
                cp, targetConnection);

        return decorate(name, file, hcp);
    }

    public static CommandProcessor decorate(String name, String file, CommandProcessor cp) {
        return new CommandProcessorReportingDecorator(name, file, cp);
    }

    /* ********************************************************************* */
    private static CommandProcessor createCommandProcessorValidateItemXML(
            CIMCommandValidateItemXML cmd) {
        Connection connection = buildConnectionFromEnv(cmd.getTargetEnvironment());

        CommandProcessor cp = new CommandProcessorValidateItemXML();
        return decorate(cmd.getCommandName(), cmd.getLogFile(), connection, cp);
    }

    private static CommandProcessor createCommandProcessorBuildAssessment(
            CIMCommandBuildAssessment cmd) throws Exception
    {
    	
    	ArrayList unicodeList = new ArrayList();
    	FileInputStream fis = new FileInputStream(cmd.getInputFile());
    	int len=fis.available();
    	byte[] byte_text = new byte[len];
    	fis.read(byte_text);
    	
    	String assessment = new String(byte_text);
    	AssessmentLayoutProcessor.adjustXMLSrcString(assessment, unicodeList);
    	Element rootElement = buildRootElement(cmd);
        if ( cmd.isTargetProduction() )
        {
            cmd.useCQA_ENV();
            Connection CQAconnection = buildConnectionFromEnv(cmd.getTargetEnvironment());
            return decorate(cmd.getCommandName(), cmd.getInputFile().getPath()
                    , CQAconnection
                    , new CommandProcessorImportAssessmentFromContentQA( rootElement, CQAconnection ));
        }
        else
        {
        	System.out.println(cmd.getTargetEnvironment());
        	File file = new File (cmd.getTargetEnvironment());
        	System.out.println(file.getAbsolutePath());
	        Connection connection = buildConnectionFromEnv(cmd.getTargetEnvironment());
	        System.out.println("Connection is closed-->>"+connection.isClosed());
	        ScorableItemConfig scoringConfig = new ScorableItemConfig(new File(cmd.getTargetEnvironment()));
	        ProductConfig productConfig = new ProductConfig(new File(cmd.getTargetEnvironment()));
	        /*ADSConfig adsConfig = new ADSConfig(new File(cmd.getTargetEnvironment()));*/
	        
	        String fwkCode = rootElement.getAttributeValue(PremadeTestHolderAssembler.FRAMEWORK_CODE);
	        String maxPanelWidth = rootElement.getAttributeValue(PremadeTestHolderAssembler.MAX_PANEL_WIDTH);
	        String includeAcknowledgment = rootElement.getAttributeValue(PremadeTestHolderAssembler.INCLUDE_ACKNOWLEDGMENT);
	        String mappingDir = getMappingDir(cmd.getTargetEnvironment());
	
	        CommandProcessorFactoryUtil.loadFrameworkResources(getFwkFile(mappingDir, fwkCode),
	                getObjFile(mappingDir, fwkCode), getMappingFile(mappingDir, fwkCode), cmd
	                        .getObjectivesFileFormat());
	       // String bGenPDFmedia = rootElement.getAttributeValue( "GenPDFmedia" );
	        //boolean bSubtestMedia = true;
	        //if ( bGenPDFmedia != null && bGenPDFmedia.length() > 0 
	        //       && ( bGenPDFmedia.charAt( 0 ) == 'N' || bGenPDFmedia.charAt( 0 ) == 'n' ))
	        //{
	        boolean    bSubtestMedia = false;
	       // }
	        
	        //String emd="C:\\Arindam\\OCPS\\mappingdata\\DEV.properties";
	      //String emd="C:/Arindam/OCPS/mappingdata/DEV.properties";
	        
	        String dot=null,env=null;
	      String envPath=cmd.getTargetEnvironment();
	       dot=envPath.substring(0,envPath.lastIndexOf("."));

	       env=envPath.substring(envPath.lastIndexOf("/")+1,dot.length());
	      if(dot.equals(env))
	      	env=envPath.substring(envPath.lastIndexOf("\\")+1,dot.length());
	     	
	        Configuration configuration=  new Configuration();
	        configuration.load(new File(cmd.getTargetEnvironment()));
	        configuration.setEnv(env);
	        
	        CommandProcessor cp = CommandProcessorFactory.newCommandProcessorImportAssessment(
	                connection, rootElement, MapperFactory.getObjectives(), MapperFactory.getItemMap(),
	                scoringConfig, productConfig, bSubtestMedia, unicodeList,/*adsConfig,*/configuration, maxPanelWidth, includeAcknowledgment );
	
	        return decorate(cmd.getCommandName(), cmd.getInputFile().getPath(), connection, cp);
        }
    }

    /* ********************************************************************* */

    private static CommandProcessor createCommandProcessorImportAndMapItems(
            CIMCommandImportAndMapItems command) 
    {
        Element rootElement = buildRootElement( command );
        if ( command.isTargetProduction() )
        {
            command.useCQA_ENV();
            Connection CQAconnection = buildConnectionFromEnv( command.getTargetEnvironment());
            return decorate(command.getCommandName(), command.getInputFile().getPath()
                    , CQAconnection
                    , new CommandProcessorImportAndMapItemsContentQA( rootElement, CQAconnection ));
        }
        else
        {
	        String targetEvironment = command.getTargetEnvironment();
	        Connection connection = buildConnectionFromEnv(targetEvironment);
	        File inputFile = command.getInputFile();	
	        String objFileFormat = command.getObjectivesFileFormat();
	        String[] fwkCodes = getFrameworkCodes(command.getMappingList(), targetEvironment);
	        String mappingDir = getMappingDir(command.getMappingDir(), targetEvironment);
	
	        Objectives[] objectivesArray = new Objectives[fwkCodes.length];
	        ItemMap[] itemMapArray = new ItemMap[fwkCodes.length];
	
	        CommandProcessorFactoryUtil.populateObjectiveAndItemMaps(objFileFormat, fwkCodes,
	                mappingDir, objectivesArray, itemMapArray);
	
	        CommandProcessor cp = CommandProcessorFactory.newCommandProcessorImportAndMapItems(
	                connection, rootElement, objectivesArray, itemMapArray,
	                command.getLocalImageArea(), command.getImageArea(), command.getValidationMode(),
	                new OpenDeployConfig(new File(targetEvironment)));
	        return decorate(command.getCommandName(), inputFile.getPath(), connection, cp);
        }
    }

    /* ********************************************************************* */

    private static CommandProcessor createCommandProcessorCreatePdfThroughFile(
            CIMCommandCreatePdfThroughFile command) {
        Element rootElement = XMLUtils.buildRootElement(command.getInputFile());
        CommandProcessor cp = CommandProcessorFactory.newCommandProcessorPdfThroughFile(
                rootElement, command.getXslFile(), command.getOutputFile(), command.getPdfType(),
                command.getProcessStep());
        return decorate(command.getCommandName(), command.getInputFile().getPath(), cp);
    }

    /* ********************************************************************* */

    private static CommandProcessor createCommandProcessorGenerateMediaForEditorReview(
            CIMCommandGenerateMediaForEditorReview command) {
        Element rootElement = XMLUtils.buildRootElement(command.getInputFile());
        CommandProcessor cp = CommandProcessorFactory
                .newCommandProcessorGenerateMediaForEditorReview(command.getInputFile(), command
                        .getRootDirectory(), command.getDirectory(), command.getPdfOutputFile(),
                        command.getFlashOutfile());
        return decorate(command.getCommandName(), command.getInputFile().getName(), cp);
    }

    /* ********************************************************************* */

    //    private static CommandProcessor createCommandProcessorGeneratePDF
    //                                (CIMCommandGeneratePDF command) {
    //        Element rootElement = XMLUtils.buildRootElement( command.getInputFile());
    //        CommandProcessor cp =
    //            CommandProcessorFactory.newCommandProcessorGeneratePDF(
    //                command.getInputFile(),command.getRootDirectory(),command.getDirectory(),command.getPdfOutputFile());
    //        return decorate(
    //            command.getCommandName(),
    //            command.getInputFile().getName(),
    //            cp);
    //    }
    //
    /* ********************************************************************* */

    private static CommandProcessor createCommandProcessorItemsFromFile(
            AbstractCIMCommandItemsFromFile command) {
        Connection connection = buildConnectionFromEnv(command.getTargetEnvironment());
        AbstractCIMCommandItemsFromFile cmd = (AbstractCIMCommandItemsFromFile) command;
        Element rootElement = buildRootElement(cmd);
        ItemProcessor ip = CIMCommandProcessorUtil.getItemProcessor(cmd, connection);
        CommandProcessor cp = CommandProcessorFactory.newCommandProcessorLoadItems(connection,
                rootElement, ip);
        return decorate(cmd.getCommandName(), cmd.getInputFile().getPath(), connection, cp);
    }

    /* ********************************************************************* */

    private static CommandProcessor createCommandProcessorImportSubTests(
            CIMCommandBuildSubTestSofa cmd) 
    {
        Element rootElement = buildRootElement(cmd);
        if ( cmd.isTargetProduction() )
        {
            cmd.useCQA_ENV();
            Connection CQAconnection = buildConnectionFromEnv(cmd.getTargetEnvironment());
            return decorate(cmd.getCommandName(), cmd.getInputFile().getPath()
                    , CQAconnection
                    , new CommandProcessorImportAssessmentFromContentQA( rootElement, CQAconnection ));
        }
        else
        {
	        Connection connection = buildConnectionFromEnv(cmd.getTargetEnvironment());
	        String fwkCode = rootElement.getAttributeValue(PremadeTestHolderAssembler.FRAMEWORK_CODE);
	        String mappingDir = getMappingDir(cmd.getTargetEnvironment());
	
	        CommandProcessorFactoryUtil.loadFrameworkResources(getFwkFile(mappingDir, fwkCode),
	                getObjFile(mappingDir, fwkCode), getMappingFile(mappingDir, fwkCode), cmd
	                        .getObjectivesFileFormat());
	
	        CommandProcessor cp = CommandProcessorFactory.newCommandProcessorImportSubTests(connection,
	                rootElement, MapperFactory.getObjectives(), MapperFactory.getItemMap());
	
	        return decorate(cmd.getCommandName(), cmd.getInputFile().getPath(), connection, cp);
        }
    }

    /* ********************************************************************* */

    private static CommandProcessor createCommandProcessorMappingHierarchy(
            CIMCommandCreateMappingHierarchy cmd) {
        String inputFile = (cmd.getMappingFile() != null) ? cmd.getMappingFile().getPath() : cmd
                .getFrameworkFile().getPath();

        CommandProcessorFactoryUtil.loadFrameworkResources(cmd.getFrameworkFile(), cmd
                .getObjectivesFile(), cmd.getMappingFile(), cmd.getObjectivesFileFormat(), true);

        CommandProcessor cp = CommandProcessorFactory.newCommandProcessorMappingHierarchy(
                MapperFactory.getObjectives(), MapperFactory.getItemMap());

        return decorate(cmd.getCommandName(), inputFile, cp);
    }

    /* ********************************************************************* */

    private static CommandProcessor createCommandProcessorFlattenedMappingHierarchy(
            CIMCommandCreateFlattenedMappingHierarchy cmd) {
        String inputFile = (cmd.getMappingFile() != null) ? cmd.getMappingFile().getPath() : cmd
                .getFrameworkFile().getPath();

        CommandProcessorFactoryUtil.loadFrameworkResources(cmd.getFrameworkFile(), cmd
                .getObjectivesFile(), cmd.getMappingFile(), cmd.getObjectivesFileFormat(), true);

        CommandProcessor cp = CommandProcessorFactory.newCommandProcessorFlattenedMappingHierarchy(
                MapperFactory.getObjectives(), MapperFactory.getItemMap());

        return decorate(cmd.getCommandName(), inputFile, cp);
    }

    /* ********************************************************************* */

    private static CommandProcessor createCommandProcessorProductReport(
            CIMCommandProductReport command) {
        File mappingFile = command.getMappingFile();
        String inputFile = (mappingFile != null) ? mappingFile.getPath() : command
                .getFrameworkFile().getPath();
        String mappingFileName = (mappingFile == null) ? "" : mappingFile.getPath();
        Connection targetConnection = buildConnectionFromEnv(command.getTargetEnvironment());

        CommandProcessorFactoryUtil.loadFrameworkResources(command.getFrameworkFile(), command
                .getObjectivesFile(), command.getMappingFile(), command.getObjectivesFileFormat(),
                true);

        CommandProcessor cp = CommandProcessorFactory.newCommandProcessorProductReport(
                mappingFileName, targetConnection);
        return decorate(command.getCommandName(), "ProductReport.log", targetConnection, cp);
    }

    /* ********************************************************************* */

    private static CommandProcessor createCommandProcessorRoundTrip(
            AbstractCIMCommandRoundTrip command) {
        AbstractCIMCommandRoundTrip cmd = (AbstractCIMCommandRoundTrip) command;

        String fwkCode = cmd.getFrameworkCode();
        String mappingDir = getMappingDir(cmd.getTargetEnvironment());
        File mappingFile = getMappingFile(mappingDir, fwkCode);
        CommandProcessorFactoryUtil.loadFrameworkResources(getFwkFile(mappingDir, fwkCode),
                getObjFile(mappingDir, fwkCode), mappingFile, CIMCommandFactory._SHORT);
        ItemMap itemMap = MapperFactory.getItemMap();
        Connection targetConnection = buildConnectionFromEnv(cmd.getTargetEnvironment());

        ItemProcessor ip = CIMCommandProcessorUtil.getItemProcessor(command, targetConnection);
        CommandProcessor cp = CommandProcessorFactory.newCommandProcessorReoundTrip(itemMap, ip,
                targetConnection);
        return decorate(cmd.getCommandName(), "RoundTrip.log", targetConnection, cp);
    }

    /* ********************************************************************* */

    private static CommandProcessor createCommandProcessorTestMapCompare(
            CIMCommandTestMapCompare cmd) {
        List srcCsvEntries = FileListUtils.fileToList(cmd.getInputFileName());
        srcCsvEntries.remove(0);

        List tgtCsvEntries = FileListUtils.fileToList(cmd.getOutputFileName());
        tgtCsvEntries.remove(0);

        CommandProcessor cp = CommandProcessorFactory.newCommandProcessorMapCompare(srcCsvEntries,
                tgtCsvEntries);
        return decorate(cmd.getCommandName(), cmd.getInputFileName(), cp);
    }

    /* ********************************************************************* */

    private static CommandProcessor createCommandProcessorTestMap(AbstractCIMCommandTestMap cmd) {
        String fwkCode = cmd.getFrameworkCode();
        String mappingDir = getMappingDir(cmd.getTargetEnvironment());
        File mappingFile = getMappingFile(mappingDir, fwkCode);
        CommandProcessorFactoryUtil.loadFrameworkResources(getFwkFile(mappingDir, fwkCode),
                getObjFile(mappingDir, fwkCode), mappingFile, CIMCommandFactory._SHORT, true);
        ItemMap itemMap = MapperFactory.getItemMap();
        Objectives objectives = MapperFactory.getObjectives();

        List csvEntries = FileListUtils.fileToList(cmd.getInputFileName());
        int _NUM_ROWS = 40;
        MappingWriter writer = CIMCommandProcessorUtil.getMappingWiter(cmd);

        MappingProcessor mappingProcessor = null;
        if ( (cmd instanceof CIMCommandTestMapInitial)
                || (cmd instanceof CIMCommandTestMapObjectiveUpdate)
                || (cmd instanceof CIMCommandTestMapAK)) {
            String titleColumn = (String) csvEntries.remove(0);

            mappingProcessor = CIMCommandProcessorUtil.getMappingProcessor(cmd, objectives,
                    itemMap, titleColumn);

            CommandProcessor cp = CommandProcessorFactory
                    .newCommandProcessorTestMapWriteCSVDecorated(csvEntries.iterator(),
                            mappingProcessor, writer, _NUM_ROWS);
            return decorate(cmd.getCommandName(), cmd.getInputFileName(), cp);
        }

        if ( (cmd instanceof CIMCommandTestMapMerge)
                || (cmd instanceof CIMCommandTestMapMergeValidate)) {

            // check the number of column in the source file
            if (getNumberOfColumns((String) csvEntries.get(0)) > 3) {
                csvEntries.remove(0);
                mappingProcessor = CIMCommandProcessorUtil.getMappingProcessor(cmd, objectives,
                        itemMap);
            } else {
                String titleColumn = (String) csvEntries.remove(0);
                mappingProcessor = CIMCommandProcessorUtil.getMappingProcessor(cmd, objectives,
                        itemMap, titleColumn);
            }

            Iterator itemMapRows = FileListUtils.fileToList(mappingFile.getPath()).iterator();

            writer = CIMCommandProcessorUtil.getMappingWiter(cmd);
            CommandProcessor cp = CommandProcessorFactory.newCommandProcessorMapMerge(csvEntries
                    .iterator(), itemMapRows, mappingProcessor, writer, _NUM_ROWS);
            return decorate(cmd.getCommandName(), cmd.getInputFileName(), cp);
        }
        throw new SystemException("Don't know how to process CIMCommand: " + cmd.getCommandName());
    }

    /* ********************************************************************* */

    private static int getNumberOfColumns(String csvEntry) {
        List entry = RegexUtils.getAllMatchedGroups("([^\",]*,?|\"[^\"]*\",?)", csvEntry, ",");
        return entry.size();
    }

    protected static String getMappingDir(String mappingDir, String environment) {
        return (mappingDir == null) ? new MappingConfig(environment).getMappingDir() : mappingDir;
    }

    private static String getMappingDir(String environment) {
        return getMappingDir(null, environment);
    }

    static Connection buildConnectionFromEnv(String environment) {
        return new DBConfig(new File(environment)).openConnection();
    }

    private static Element buildRootElement(AbstractCIMCommandContentFromFile command) {
        if (command instanceof InterfaceGeneratesMedia) {
            InterfaceGeneratesMedia cmd = (InterfaceGeneratesMedia) command;
            return XMLUtils.buildRootElement(command.getInputFile(), cmd.getImageArea(), cmd
                    .getLocalImageArea());
        } else {
            return XMLUtils.buildRootElement(command.getInputFile());
        }
    }

    private static File getObjFile(String mapDir, String fwkCode) {
        return CommandProcessorFactoryUtil.getObjFile(mapDir, fwkCode);
    }

    private static File getFwkFile(String mapDir, String fwkCode) {
        return CommandProcessorFactoryUtil.getFwkFile(mapDir, fwkCode);
    }

    private static File getMappingFile(String mapDir, String fwkCode) {
        return CommandProcessorFactoryUtil.getMappingFile(mapDir, fwkCode);
    }

    private static String[] getFrameworkCodes(String[] mappingList, String environment) {
        return (mappingList == null) ? new MappingConfig(environment).getFrameworkCodes()
                : MappingConfig.removeDuplicates(mappingList);
    }

}