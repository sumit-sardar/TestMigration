package com.ctb.lexington.util;

/*
 * RosterStatus.java
 *
 * Still Needs to be refactored into StudentDetailVO
 *
 * Copyright CTB/McGraw-Hill, 2002
 * CONFIDENTIAL
 *
 */

// GRNDS imports
import com.ctb.lexington.domain.teststructure.RosterCaptureMethod;
import com.ctb.lexington.domain.teststructure.ScoringStatus;
import com.ctb.lexington.util.RosterTestCompletionStatus;

/**
 * @author <a href="mailto:Krys_Gawetski@ctb.com">Krys Gawetski</a>
 * @version
 * $Id$
 */
public class RosterStatus extends Object
{
    public  static final String VO_LABEL       = "com.ctb.lexington.data.RosterStatus";
    public  static final String VO_ARRAY_LABEL = VO_LABEL + ".array";

    //constants for TestRoster testCompletionStatus
    public  static final String SCHEDULED              = "Scheduled";
    public  static final String IN_PROGRESS            = "In Progress";
    public  static final String INTERRUPTED_BY_STUDENT = "System Stop";
    public  static final String INTERRUPTED_BY_NETWORK = "Student Stop";
    public  static final String SCORING_IN_PROGRESS    = "Scoring in Progress";
    public  static final String ERROR_IN_SCORING       = "Error in Scoring";
    public  static final String NOT_TAKEN              = "Not Taken";
    public  static final String INCOMPLETE             = "Incomplete";
    public  static final String COMPLETED              = "Completed";
    //the NOT_ALLOWED is a system value; should never be an output; unless there is a system/programming error!
    private static final String NOT_ALLOWED            = "X Not Allowed-contact IT";

    /**
     * Checks if the Entity Bean (Local) property is indicating that:<br>
     * 1. student HAS logged in to take a test<br>
     * NOTE: COmpleted might be without student loggin/taking a test.
     * @param captureMethod_ value of entity property.
     * @return <code>true</code> in such cases; <code>false</code> otherwise.
     */
    public static boolean hasStudentEverLoggedIn( String captureMethod_ ) {
        return ( RosterCaptureMethod.ONLINE.getCode().equals(captureMethod_) ||
             RosterCaptureMethod.MIXED.getCode().equals(captureMethod_) );
    }

    /**
     * Gives the name for the testCompletionStatus value/code.
     * @param testCompletionStatus String containing the code of the <code>TestCompletionStatus</code>,
     * @param scoringStatus String containing the code of the <code>ScoringStatus</code>,
     * @return String <code>Name</code> of testCompletionStatus.
     */
    public static String getRosterStatusName( String testCompletionStatus,
                                              String scoringStatus )
    {
        if     (testCompletionStatus.equals( RosterTestCompletionStatus.SCHEDULED ))
                return( statusNameForScheduled( scoringStatus ));
        else if (testCompletionStatus.equals( RosterTestCompletionStatus.IN_PROGRESS ))
                return( statusNameForInProgress( scoringStatus ));
        else if (testCompletionStatus.equals( RosterTestCompletionStatus.INTERRUPTED_BY_NETWORK ))
                return( statusNameForInterruptedByNetwork( scoringStatus ));
        else if (testCompletionStatus.equals( RosterTestCompletionStatus.INTERRUPTED_BY_STUDENT ))
                return( statusNameForInterruptedByStudent( scoringStatus ));
        else if (testCompletionStatus.equals( RosterTestCompletionStatus.CLOSED ))
                return( statusNameForClosed( scoringStatus ));
        else if (testCompletionStatus.equals( RosterTestCompletionStatus.ONLINE_COMPLETED ))
                return( statusNameForOnlineCompleted( scoringStatus ));
        else if (testCompletionStatus.equals( RosterTestCompletionStatus.INCOMPLETE ))
                return( statusNameForIncomplete( scoringStatus ));
        else if (testCompletionStatus.equals( RosterTestCompletionStatus.NOT_TAKEN ))
                return( statusNameForNotTaken( scoringStatus ));
        else if (testCompletionStatus.equals( RosterTestCompletionStatus.COMPLETED ))
                return( statusNameForComplete( scoringStatus ));
        else                                   //Should never be displayed; i.e.
                return "Not Supported Status."; //Indicates data/sw integrity error!!?
    }


    private static String statusNameForScheduled( String scoringStatus) {
        if      (scoringStatus.equals( ScoringStatus.NOT_SCORED.getCode() ))       return( RosterStatus.SCHEDULED );
        else if( scoringStatus.equals( ScoringStatus.SCORING.getCode() ))          return( RosterStatus.SCORING_IN_PROGRESS );
        else if( scoringStatus.equals( ScoringStatus.IN_PROGRESS.getCode() ))      return( RosterStatus.SCORING_IN_PROGRESS );
        else if( scoringStatus.equals( ScoringStatus.PARTIALLY_SCORED.getCode() )) return( RosterStatus.SCORING_IN_PROGRESS );
        else if( scoringStatus.equals( ScoringStatus.SCORING_PROBLEM.getCode() ))  return( RosterStatus.ERROR_IN_SCORING );
        else if( scoringStatus.equals( ScoringStatus.SCORED.getCode() ))           return( RosterStatus.NOT_ALLOWED );
        else return( "Not Supported Status: " + scoringStatus); //Indicates data/sw integrity error!!?
    }

    private static String statusNameForInProgress( String scoringStatus) {
        if      (scoringStatus.equals( ScoringStatus.NOT_SCORED.getCode() ))       return( RosterStatus.IN_PROGRESS );
        else if( scoringStatus.equals( ScoringStatus.SCORING.getCode() ))          return( RosterStatus.IN_PROGRESS );
        else if( scoringStatus.equals( ScoringStatus.IN_PROGRESS.getCode() ))      return( RosterStatus.IN_PROGRESS );
        else if( scoringStatus.equals( ScoringStatus.PARTIALLY_SCORED.getCode() )) return( RosterStatus.IN_PROGRESS );
        else if( scoringStatus.equals( ScoringStatus.SCORING_PROBLEM.getCode() ))  return( RosterStatus.ERROR_IN_SCORING );
        else if( scoringStatus.equals( ScoringStatus.SCORED.getCode() ))           return( RosterStatus.NOT_ALLOWED );
        else return( "Not Supported Status: " + scoringStatus); //Indicates data/sw integrity error!!?
    }

    private static String statusNameForInterruptedByNetwork( String scoringStatus) {
        if      (scoringStatus.equals( ScoringStatus.NOT_SCORED.getCode() ))       return( RosterStatus.INTERRUPTED_BY_NETWORK );
        else if( scoringStatus.equals( ScoringStatus.SCORING.getCode() ))          return( RosterStatus.INTERRUPTED_BY_NETWORK );
        else if( scoringStatus.equals( ScoringStatus.IN_PROGRESS.getCode() ))      return( RosterStatus.INTERRUPTED_BY_NETWORK );
        else if( scoringStatus.equals( ScoringStatus.PARTIALLY_SCORED.getCode() )) return( RosterStatus.INTERRUPTED_BY_NETWORK );
        else if( scoringStatus.equals( ScoringStatus.SCORING_PROBLEM.getCode() ))  return( RosterStatus.ERROR_IN_SCORING );
        else if( scoringStatus.equals( ScoringStatus.SCORED.getCode() ))           return( RosterStatus.NOT_ALLOWED );
        else return( "Not Supported Status: " + scoringStatus); //Indicates data/sw integrity error!!?
    }

    private static String statusNameForInterruptedByStudent( String scoringStatus) {
        if      (scoringStatus.equals( ScoringStatus.NOT_SCORED.getCode() ))       return( RosterStatus.INTERRUPTED_BY_STUDENT );
        else if( scoringStatus.equals( ScoringStatus.SCORING.getCode() ))          return( RosterStatus.INTERRUPTED_BY_STUDENT );
        else if( scoringStatus.equals( ScoringStatus.IN_PROGRESS.getCode() ))      return( RosterStatus.INTERRUPTED_BY_STUDENT );
        else if( scoringStatus.equals( ScoringStatus.PARTIALLY_SCORED.getCode() )) return( RosterStatus.INTERRUPTED_BY_STUDENT );
        else if( scoringStatus.equals( ScoringStatus.SCORING_PROBLEM.getCode() ))  return( RosterStatus.ERROR_IN_SCORING );
        else if( scoringStatus.equals( ScoringStatus.SCORED.getCode() ))           return( RosterStatus.NOT_ALLOWED );
        else return( "Not Supported Status: " + scoringStatus); //Indicates data/sw integrity error!!?
    }

    private static String statusNameForClosed( String scoringStatus) {
        if      (scoringStatus.equals( ScoringStatus.NOT_SCORED.getCode() ))       return( RosterStatus.NOT_ALLOWED );
        else if( scoringStatus.equals( ScoringStatus.SCORING.getCode() ))          return( RosterStatus.SCORING_IN_PROGRESS );
        else if( scoringStatus.equals( ScoringStatus.IN_PROGRESS.getCode() ))      return( RosterStatus.SCORING_IN_PROGRESS );
        else if( scoringStatus.equals( ScoringStatus.PARTIALLY_SCORED.getCode() )) return( RosterStatus.SCORING_IN_PROGRESS );
        else if( scoringStatus.equals( ScoringStatus.SCORING_PROBLEM.getCode() ))  return( RosterStatus.ERROR_IN_SCORING );
        else if( scoringStatus.equals( ScoringStatus.SCORED.getCode() ))           return( RosterStatus.NOT_ALLOWED );
        else return( "Not Supported Status: " + scoringStatus); //Indicates data/sw integrity error!!?
    }

    private static String statusNameForOnlineCompleted( String scoringStatus) {
        if      (scoringStatus.equals( ScoringStatus.NOT_SCORED.getCode() ))       return( RosterStatus.NOT_ALLOWED );
        else if( scoringStatus.equals( ScoringStatus.SCORING.getCode() ))          return( RosterStatus.SCORING_IN_PROGRESS );
        else if( scoringStatus.equals( ScoringStatus.IN_PROGRESS.getCode() ))      return( RosterStatus.SCORING_IN_PROGRESS );
        else if( scoringStatus.equals( ScoringStatus.PARTIALLY_SCORED.getCode() )) return( RosterStatus.SCORING_IN_PROGRESS );
        else if( scoringStatus.equals( ScoringStatus.SCORING_PROBLEM.getCode() ))  return( RosterStatus.ERROR_IN_SCORING );
        else if( scoringStatus.equals( ScoringStatus.SCORED.getCode() ))           return( RosterStatus.NOT_ALLOWED );
        else return( "Not Supported Status: " + scoringStatus); //Indicates data/sw integrity error!!?
    }

    private static String statusNameForIncomplete( String scoringStatus) {
        if      (scoringStatus.equals( ScoringStatus.NOT_SCORED.getCode() ))       return( RosterStatus.INCOMPLETE );
        else if( scoringStatus.equals( ScoringStatus.SCORING.getCode() ))          return( RosterStatus.SCORING_IN_PROGRESS );
        else if( scoringStatus.equals( ScoringStatus.IN_PROGRESS.getCode() ))      return( RosterStatus.SCORING_IN_PROGRESS );
        else if( scoringStatus.equals( ScoringStatus.PARTIALLY_SCORED.getCode() )) return( RosterStatus.SCORING_IN_PROGRESS );
        else if( scoringStatus.equals( ScoringStatus.SCORING_PROBLEM.getCode() ))  return( RosterStatus.ERROR_IN_SCORING );
        else if( scoringStatus.equals( ScoringStatus.SCORED.getCode() ))           return( RosterStatus.NOT_ALLOWED );
        else return( "Not Supported Status: " + scoringStatus); //Indicates data/sw integrity error!!?
    }

    private static String statusNameForNotTaken( String scoringStatus) {
        if      (scoringStatus.equals( ScoringStatus.NOT_SCORED.getCode() ))       return( RosterStatus.NOT_TAKEN );
        else if( scoringStatus.equals( ScoringStatus.SCORING.getCode() ))          return( RosterStatus.SCORING_IN_PROGRESS );
        else if( scoringStatus.equals( ScoringStatus.IN_PROGRESS.getCode() ))      return( RosterStatus.SCORING_IN_PROGRESS );
        else if( scoringStatus.equals( ScoringStatus.PARTIALLY_SCORED.getCode() )) return( RosterStatus.SCORING_IN_PROGRESS );
        else if( scoringStatus.equals( ScoringStatus.SCORING_PROBLEM.getCode() ))  return( RosterStatus.ERROR_IN_SCORING );
        else if( scoringStatus.equals( ScoringStatus.SCORED.getCode() ))           return( RosterStatus.NOT_ALLOWED );
        else return( "Not Supported Status: " + scoringStatus); //Indicates data/sw integrity error!!?
    }

    private static String statusNameForComplete( String scoringStatus) {
        if      (scoringStatus.equals( ScoringStatus.NOT_SCORED.getCode() ))       return( RosterStatus.COMPLETED );
        else if( scoringStatus.equals( ScoringStatus.SCORING.getCode() ))          return( RosterStatus.NOT_ALLOWED );
        else if( scoringStatus.equals( ScoringStatus.IN_PROGRESS.getCode() ))      return( RosterStatus.NOT_ALLOWED );
        else if( scoringStatus.equals( ScoringStatus.PARTIALLY_SCORED.getCode() )) return( RosterStatus.NOT_ALLOWED );
        else if( scoringStatus.equals( ScoringStatus.SCORING_PROBLEM.getCode() ))  return( RosterStatus.NOT_ALLOWED );
        else if( scoringStatus.equals( ScoringStatus.SCORED.getCode() ))           return( RosterStatus.COMPLETED );
        else return( "Not Supported Status: " + scoringStatus); //Indicates data/sw integrity error!!?
    }
}
/**
 * $Log$
 * Revision 1.1  2007/01/30 01:31:46  ncohen
 * port scoring to 4.x platform
 *
 * Revision 1.1  2006/02/23 20:48:29  ncohen
 * create new module for legacy OAS app
 *
 * Revision 1.4  2005/05/03 21:25:19  ncohen
 * replace HEAD with iknow-millbarge
 *
 * Revision 1.3.16.3  2004/10/15 17:51:59  vraravam
 * Renamed CaptureMethod to RosterCaptureMethod.
 *
 * Revision 1.3.16.2  2004/10/15 14:53:06  vraravam
 * RosterCaptureMethod has been renamed to RosterCaptureMethod and also implements StringConstant.It has been moved to the package where other StringConstants live.
 *
 * Revision 1.3.16.1  2004/10/15 14:34:38  vraravam
 * Pointed references from RosterScoringStatus to ScoringStatus.
 *
 * Revision 1.3  2003/04/17 18:50:36  oasuser
 * iknowR2v5 to trunk
 *
 * Revision 1.2  2003/04/13 21:38:20  oasuser
 * iknowR2v5 to trunk
 *
 * Revision 1.1.2.5  2003/04/15 15:56:09  ggennaro
 * Made changes to support the correct capture method codes (ON, OF, MX)
 * as well as methods to use these codes.
 *
 * Revision 1.1.2.4  2003/04/10 03:41:27  kgawetsk
 * Added hasStudentEverLoggedIn().
 *
 * Revision 1.1.2.3  2003/04/09 17:58:08  kgawetsk
 * Corrections for NotTaken, Incomplete.
 *
 * Revision 1.1.2.2  2003/04/04 18:46:08  kgawetsk
 * Fully implemented "III.	Test Roster Status Name" based on design doc"Roster Status - Scenarios...".
 *
 * Revision 1.1.2.1  2003/04/04 03:19:33  kgawetsk
 * Creation.
 *
 */
