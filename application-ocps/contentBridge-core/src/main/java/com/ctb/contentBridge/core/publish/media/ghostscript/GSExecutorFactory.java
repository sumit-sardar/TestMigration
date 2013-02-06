package com.ctb.contentBridge.core.publish.media.ghostscript;


/**
 * User: mwshort
 * Date: Mar 21, 2004
 * Time: 12:11:55 PM
 * 
 *
 */
public class GSExecutorFactory {
    // TODO - mws - use properties files, clean up the IO and temp files created during this process
    // public static String PS2PDF_WIN = "ps2pdf12.bat";
    // public static String PS2PDF_WIN = "gswin32c.exe";
    // AFPL Ghostscript BETA RELEASE 7.33

    final public static String APPROVED_VERSION = "AFPL Ghostscript BETA RELEASE 7.33";
//    public static String GS_WIN = "gswin32c.exe";
//    public static String GS_UNIX = "/usr/local/bin/gs";
    final public static String WINDOWS_OS = "Windows";

    String ps2pdf;
    String gs;

    static public GSExecutor create() {
        // todo: use "defaults" feature of Properties instead
        String os = System.getProperty("os.name");
        GSExecutor executor = null;
        if (os.startsWith(WINDOWS_OS)) {
            executor = new WindowsGSExecutor();
        } else {
            executor = new UnixGSExecutor();
        }
        return executor;

    }


}
