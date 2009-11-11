package com.ctb.lexington.util;

import com.ctb.lexington.domain.teststructure.CompletionStatus;

/*
 * RosterTestCompletionStatus.java
 *
 * Still Needs to be refactored into StudentDetailVO
 *
 * Copyright CTB/McGraw-Hill, 2002
 * CONFIDENTIAL
 *
 */

/**
 * @author <a href="mailto:Krys_Gawetski@ctb.com">Krys Gawetski</a>
 * @version
 * $Id$
 */
public class RosterTestCompletionStatus extends Object
{
    public  static final String VO_LABEL       = "com.ctb.lexington.data.RosterTestCompletionStatus";
    public  static final String VO_ARRAY_LABEL = VO_LABEL + ".array";

    // TODO: These constants should be replaced with calls to constants in CompletionStatus
    //constants for TestRoster testCompletionStatus
    public static final String SCHEDULED              = CompletionStatus.SCHEDULED.getCode();
    public static final String IN_PROGRESS            = CompletionStatus.IN_PROGRESS.getCode();
    public static final String INTERRUPTED_BY_NETWORK = CompletionStatus.SYSTEM_STOP.getCode();
    public static final String INTERRUPTED_BY_STUDENT = CompletionStatus.STUDENT_STOP.getCode();
    public static final String NOT_TAKEN              = CompletionStatus.NOT_TAKEN.getCode();
    public static final String INCOMPLETE             = CompletionStatus.INCOMPLETE.getCode();
    public static final String CLOSED                 = CompletionStatus.TEST_LOCKED.getCode(); //reportable
    public static final String ONLINE_COMPLETED       = CompletionStatus.ONLINE_COMPLETED.getCode(); //reportable
    public static final String COMPLETED              = CompletionStatus.COMPLETED.getCode(); //reportable

    /**
     * Checks if the Bean property is indicating that student has logged in to take a test.
     * @param testCompletionStatus_ String containing the code of the <code>TestCompletionStatus</code>.
     * @return <code>true</code> iin such cases; <code>false</code> otherwise.
     */
    public static boolean hasLoggedInOrCompleted(String testCompletionStatus_) {
        //this student was/is "logged on"
        return (  testCompletionStatus_.equals( COMPLETED )
           || testCompletionStatus_.equals( INCOMPLETE )
           || testCompletionStatus_.equals( ONLINE_COMPLETED )
           || testCompletionStatus_.equals( INTERRUPTED_BY_NETWORK )
           || testCompletionStatus_.equals( INTERRUPTED_BY_STUDENT )
           || testCompletionStatus_.equals( IN_PROGRESS ) );
    }

    /**
     * Checks if the Bean property is indicating that:<br>
     * 1. student HAS logged in to take a test, and/or<br>
     * 2. keyEntry recorded some "scores".
     * @param testCompletionStatus_ String containing the code of the <code>TestCompletionStatus</code>.
     * @return <code>true</code> in such cases; <code>false</code> otherwise.
     */
    public static boolean hasSomeResponses( String testCompletionStatus_ ) {
        return( hasLoggedInOrCompleted( testCompletionStatus_ ) ||
            hasKeyEntry( testCompletionStatus_ ));
    }

    /**
     * Checks if the KEY-ENTRY has recorded entires for that roster.<br>
     * @param testCompletionStatus_ String containing the code of the <code>TestCompletionStatus</code>.
     * @return <code>true</code> if <b>testCompletionStatus</b> indicates test "student online is CLOSED" category, <br>
     *         which is caused by KeyEntry; <code>false</code> otherwise.
     */
    private static boolean hasKeyEntry( String testCompletionStatus_) {
        return ( testCompletionStatus_.equals( CLOSED ) );
    }

    /**
     * Checks if the student has <b>never taken</b> the test.<br>
     * @param testCompletionStatus_ String containing the code of the <code>TestCompletionStatus</code>.
     * @return <code>true</code> if <b>testCompletionStatus</b> indicates student "never Taken Test" category; <code>false</code> otherwise.
     */
    public static boolean haveNeverTakenTest( String testCompletionStatus_) {
        return ( testCompletionStatus_.equals( SCHEDULED ) ||
            testCompletionStatus_.equals( NOT_TAKEN ));
    }

    /**
     * Checks if the student IS-LOGGED-IN to take the test.<br>
     * @param testCompletionStatus_ String containing the code of the <code>TestCompletionStatus</code>.
     * @return <code>true</code> if <b>testCompletionStatus</b> indicates test "user is online" category; <code>false</code> otherwise.
     */
    public static boolean isStudentLoggedIn( String testCompletionStatus_) {
        return ( testCompletionStatus_.equals( IN_PROGRESS ) );
    }

    /**
     * Checks if the student has <b>taken</b>, <b>finished</b>, and
     * got <b><u>scored</u></b>the test.<br>
     * (i.e. All <b>responses</b> are available, and <b>scoring</b> is at least in progress (if not completed).
     *
     * NOTE:
     *      this method may need to be change to denote COmpleted SCORING phase!?
     *
     * @param testCompletionStatus_ String containing the code of the <code>TestCompletionStatus</code>.
     * @return <code>true</code> if <b>testCompletionStatus</b> indicates test "scoring CAN be completed" category; <code>false</code> otherwise.
     */
    public static boolean haveCompletedTest( String testCompletionStatus_) {
        return ( testCompletionStatus_.equals( COMPLETED ) );
    }

}
/**
 * $Log$
 * Revision 1.1  2007/01/30 01:31:45  ncohen
 * port scoring to 4.x platform
 *
 * Revision 1.1  2006/02/23 20:48:28  ncohen
 * create new module for legacy OAS app
 *
 * Revision 1.7  2005/05/03 21:25:21  ncohen
 * replace HEAD with iknow-millbarge
 *
 * Revision 1.6.16.5  2004/10/15 15:01:45  vraravam
 * Inlined RosterTestCompletionStatus.getTestCompletionStatusName since it was just calling a method on CompletionStatus.
 *
 * Revision 1.6.16.4  2004/10/15 13:39:56  vraravam
 * Added TODO comments.
 *
 * Revision 1.6.16.3  2004/09/02 23:45:26  vraravam
 * Removed duplicated cvs commit tags.
 *
 * Revision 1.6.16.2  2004/09/02 23:44:30  vraravam
 * CompletionStatus now uses proper descriptions. RosterTestCompletionStatus references codes defined in CompletionStatus.
 *
 * Revision 1.6.16.1  2004/08/11 20:30:33  ncohen
 * last batch of toast changes
 *
 * Revision 1.6.12.1  2004/08/04 20:22:30  wchang
 * Change status label to "Online Completed" for code "OC"
 *
 * Revision 1.6  2003/04/17 18:50:36  oasuser
 * iknowR2v5 to trunk
 *
 * Revision 1.5  2003/04/15 16:06:58  oasuser
 * iknowR2v5 to trunk
 *
 * Revision 1.4  2003/04/13 21:38:21  oasuser
 * iknowR2v5 to trunk
 *
 * Revision 1.3  2003/04/07 22:49:57  oasuser
 * iknowR2v5 to trunk
 *
 * Revision 1.2  2003/03/11 20:14:31  oasuser
 * iknowR2v5 to trunk
 *
 * Revision 1.1.4.20  2003/04/15 16:46:35  kgawetsk
 * "Test Locked" for CL.
 *
 * Revision 1.1.4.19  2003/04/11 18:27:21  kgawetsk
 * Changed "names" for new 0.4 wireframe 040803.pdf version.
 *
 * Revision 1.1.4.18  2003/04/11 17:40:34  kgawetsk
 * Moved isReportable() to RosterScoringStatus.
 *
 * Revision 1.1.4.17  2003/04/11 05:39:19  kgawetsk
 * Added isReportable(), getReportableList().
 *
 * Revision 1.1.4.16  2003/04/10 06:20:39  kgawetsk
 * Renaming methods.
 *
 * Revision 1.1.4.15  2003/04/10 02:12:51  kgawetsk
 * Renamed haveLoggedResponses() to haveSomeResponses().
 *
 * Revision 1.1.4.14  2003/04/09 08:46:38  kgawetsk
 * Documentation for "reportable" category.
 *
 * Revision 1.1.4.13  2003/04/09 03:50:56  kgawetsk
 * Added INCOMPLETE to haveLoggedInToTakeTest "category".
 *
 * Revision 1.1.4.12  2003/04/08 08:08:19  kgawetsk
 * Added haveLoggedResponses().
 *
 * Revision 1.1.4.11  2003/04/08 07:02:14  kgawetsk
 * Added new isInKeyEntry().
 *
 * Revision 1.1.4.10  2003/04/02 18:09:46  sprakash
 * added Not_APPLICABLE
 *
 * Revision 1.1.4.9  2003/04/01 02:35:59  kgawetsk
 * Changed name for ONLINE_COMPLETED.
 *
 * Revision 1.1.4.8  2003/03/28 08:50:09  kgawetsk
 * Added isStudentLoggedIn().
 *
 * Revision 1.1.4.7  2003/03/26 22:10:48  kgawetsk
 * changed getTestCompletionStatusName() for OC, CL.
 *
 * Revision 1.1.4.6  2003/03/22 09:15:37  kgawetsk
 * Removed extrenous imports.
 *
 * Revision 1.1.4.5  2003/03/22 01:58:16  kgawetsk
 * javadoc and cleanup.
 *
 * Revision 1.1.4.4  2003/03/22 00:46:19  kgawetsk
 * Commented out haveFinishedTest() as it is not used.
 *
 * Revision 1.1.4.3  2003/03/21 18:26:12  ggennaro
 * Removed the deprecated SP (SCORING_IN_PROGRESS) test
 * completion status code.  Introduced references to new CL (CLOSED)
 * and OC (ONLINE_COMPLETED) test completion status codes with
 * regards to Test Delivery.
 *
 * Revision 1.1.4.2  2003/03/21 06:19:14  kgawetsk
 * <No Comment Entered>
 *
 * Revision 1.1.4.1  2003/03/10 22:08:46  kgawetsk
 * Creation.
 */