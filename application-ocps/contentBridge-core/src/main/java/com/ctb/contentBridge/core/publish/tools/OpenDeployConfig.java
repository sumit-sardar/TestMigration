package com.ctb.contentBridge.core.publish.tools;

import java.io.*;
import java.util.*;

import com.ctb.contentBridge.core.exception.SystemException;

/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Aug 19, 2003
 * Time: 10:58:00 AM
 * To change this template use Options | File Templates.
 */
public class OpenDeployConfig extends Config {

    /**
     * defaults to NoArt, which should stop OpenDeploy from actually running
     */

    static Map defaultMap = new HashMap();

    static {
        defaultMap.put("od.conf", "tw_filelist_deploy");
        defaultMap.put("od.working.directory", "build/test");
        defaultMap.put("od.executable", OpenDeployWrapper.EXECUTABLE);
        defaultMap.put("od.enabled", "false");
        defaultMap.put("od.dtd.systemId", "R2_Flash_UI.dtd");
    }

    String openDeployConf = "tw_filelist_deploy";
    String workingDirectory = "build/test";
    String openDeployExecutable = OpenDeployWrapper.EXECUTABLE;
    boolean enableDeployment = false;
    String dtdSystemId;

    public OpenDeployConfig() {
        loadDefaults();

    }

    public OpenDeployConfig(String openDeployConf, File workingDir) {
        this();
        this.openDeployConf = openDeployConf;
        this.workingDirectory = workingDir.getPath();
    }

    public OpenDeployConfig(File configFile) {
        load(configFile);
    }

    /**
     * @param configDir
     * @param environment
     * @deprecated DBConfig(File)
     */
    public OpenDeployConfig(File configDir, String environment) {
        load(configDir, environment);
    }

    public OpenDeployConfig(String environment) {
        this(new File(environment + ".properties"));

    }

    private void loadDefaults() {
        this.openDeployConf = (String) defaultMap.get("od.conf");
        this.workingDirectory = (String) defaultMap.get("od.working.directory");
        this.openDeployExecutable = (String) defaultMap.get("od.executable");
        this.enableDeployment =
                ((String) defaultMap.get("od.enabled")).equalsIgnoreCase("true");
        this.dtdSystemId = (String) defaultMap.get("od.dtd.systemId");
    }

    protected void readProperties() {
        this.openDeployConf = getRequiredProperty(properties, "od.conf", file);
        this.workingDirectory =
                getRequiredProperty(properties, "od.working.directory", file);
        this.openDeployExecutable =
                getRequiredProperty(properties, "od.executable", file);
        this.enableDeployment =
                properties.getProperty("od.enabled").equalsIgnoreCase("true");
        this.dtdSystemId = getRequiredProperty(properties, "od.dtd.systemId", file);
    }

    public boolean enabled() {
        return enableDeployment;
    }

    public void enable(boolean trueFalse) {
        enableDeployment = trueFalse;
    }

    public String getOpenDeployConfig() {
        return openDeployConf;
    }

    public String getExecutable() {
        return openDeployExecutable;
    }

    public File getWorkingDirectory() {

        File dir = new File(workingDirectory);

        if (dir.isDirectory()) {
            return dir;
        }
        if (dir.isFile()) {
            throw new SystemException("Can not use a file as the working directory for OpenDeploy:"
                    + dir.getAbsolutePath());
        }

        if (dir.mkdirs()) {
            return dir;
        }
        else {
            throw new SystemException("Could not create working directory for OpenDeploy"
                    + dir.getAbsolutePath());
        }
    }

    public String getSystemId() {
        return dtdSystemId;
    }
}
