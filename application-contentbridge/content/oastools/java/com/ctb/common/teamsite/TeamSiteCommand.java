package com.ctb.common.teamsite;

import java.io.File;

import com.ctb.reporting.Report;

/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Sep 23, 2003
 * Time: 3:09:33 PM
 * To change this template use Options | File Templates.
 */
public class TeamSiteCommand {
    public File itemSetFile;
    public File outDir;
    public String stateCode;

    public void process(TeamSiteImporter importer) throws Exception {
        importer.importCommand(this);
    }

    public Report getReport() {
        // TODO Auto-generated method stub
        return null;
    }

}
