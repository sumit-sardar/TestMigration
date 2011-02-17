package com.ctb.cprocessor;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.ctb.common.tools.DBConfig;
import com.ctb.common.tools.DBConnection;
import com.ctb.common.tools.IOUtils;
import com.ctb.mapping.ItemMap;
import com.ctb.mapping.Objectives;

public abstract class CommandProcessorTestAbstract extends TestCase {

    protected static Logger logger =
        Logger.getLogger(CommandProcessorTestAbstract.class);
    static {
        PropertyConfigurator.configure("conf/log4j.properties");
    }

    public static final File runtimeTestDir = new File("build/test");
    public static final File dtdMasterDir = new File("etc/");
    public static final String dtd = IOUtils.DTD_NAME;
    public static final String entityLat1 = "xhtml-lat1.ent";
    public static final String entitySpecial = "xhtml-special.ent";
    public static final String entitySymbol = "xhtml-symbol.ent";

    private DBConnection dbConn;
    private Connection conn;
    private DBConfig dbconfig;

    public CommandProcessorTestAbstract(String s) {
        super(s);
    }

    protected void setUp() throws Exception {
        setupDtdAndEntityFiles();
        dbconfig = new DBConfig();
        conn = dbconfig.openNoCommitConnection();
//        conn = dbconfig.openConnection();
        dbConn = new DBConnection(conn);
        dbConn.setDebug(true);
    }

    protected void tearDown() throws SQLException {
        if (conn != null) {
            conn.rollback();
//            conn.commit();
            conn.close();
        }
    }

    protected Connection getTrueConnection() {
        return conn;
    }

    protected DBConnection getDBConnection() throws IOException, SQLException {
        return dbConn;
    }

    protected void setupDtdAndEntityFiles() {
        copyDtdFilesToDir(runtimeTestDir);
    }

    protected void copyDtdFilesToDir(File targetDir) {
        copyFileToDir(new File(dtdMasterDir, dtd), targetDir);
        copyFileToDir(new File(dtdMasterDir, entityLat1), targetDir);
        copyFileToDir(new File(dtdMasterDir, entitySpecial), targetDir);
        copyFileToDir(new File(dtdMasterDir, entitySymbol), targetDir);
    }
    public static File copyFileToDir(File source, File targetDir) {
        targetDir.mkdirs();
        File target = new File(targetDir, source.getName());

        return copyFile(source, target);
    }

    private static File copyFile(File source, File target) {
        return IOUtils.copyFile(source, target);
    }

    public static void loadFrameworkResources(
        File frameworkDefinitonFile,
        File objectivesFile,
        File mappingFile,
        String fileFormat,
        boolean allowNullMappingFile) {
        CommandProcessorFactoryUtil.loadFrameworkResources(
            frameworkDefinitonFile,
            objectivesFile,
            mappingFile,
            fileFormat,
            allowNullMappingFile);
    }

    public static void loadFrameworkResources(
        File frameworkDefinitonFile,
        File objectivesFile,
        File mappingFile,
        String fileFormat) {
        CommandProcessorFactoryUtil.loadFrameworkResources(
            frameworkDefinitonFile,
            objectivesFile,
            mappingFile,
            fileFormat,
            false);
    }

    protected void populateObjectiveAndItemMaps(
        String objFileFormat,
        String[] fwkCodes,
        String mappingDir,
        Objectives[] objectivesArray,
        ItemMap[] itemMapArray) {
        CommandProcessorFactoryUtil.populateObjectiveAndItemMaps(
            objFileFormat,
            fwkCodes,
            mappingDir,
            objectivesArray,
            itemMapArray);

    }

}
