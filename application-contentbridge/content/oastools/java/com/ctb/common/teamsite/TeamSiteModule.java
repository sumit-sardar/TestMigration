package com.ctb.common.teamsite;


import com.ctb.common.tools.*;


/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Sep 23, 2003
 * Time: 3:05:56 PM
 * To change this template use Options | File Templates.
 */
public class TeamSiteModule {

    public static void main(String[] args) throws Exception {
        TeamSiteCommand command = null;

        command = buildImporterCommand(args);

        try {
            TeamSiteImporter importer = new TeamSiteImporter();

            command.process(importer);
            System.out.println(importer.getReport());
        } catch (Exception e) {
            System.out.println("An error occured processing the command");
            System.out.println(e.getMessage());
            System.exit(2);
        }
        System.exit(0);
    }

    static public TeamSiteCommand buildImporterCommand(String[] args) {
        CommandLine cmdLine = null;

        try {
            cmdLine = new CommandLine(args);
        } catch (SystemException e) {
            System.out.println("Cannot parse commandline");
            CommandLine.usage();
            System.out.println("Valid Commands:");
            System.out.println("importitemset - Import ItemSet into TeamSite");
        }
        TeamSiteCommand cmd = new TeamSiteCommand();

        cmd.itemSetFile = cmdLine.getFileParameter("itemsetfile");
        cmd.outDir = cmdLine.getFileParameter("outdir");
        cmd.stateCode = cmdLine.getOptionalParameterValue("statecode", null);
        return cmd;
    }

}
