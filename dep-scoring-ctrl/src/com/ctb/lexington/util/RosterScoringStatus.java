package com.ctb.lexington.util;

/*
 * RosterScoringStatus.java
 *
 * Copyright CTB/McGraw-Hill, 2002
 * CONFIDENTIAL
 *
 */

 //java

/**
 * @author <a href="mailto:Krys_Gawetski@ctb.com">Krys Gawetski</a>
 * @version
 * $Id$
 * $Id$
 * $Id$
 * $Id$
 */
public class RosterScoringStatus extends Object
{
    public  static final String VO_LABEL       = "com.ctb.lexington.data.RosterScoringStatus";
    public  static final String VO_ARRAY_LABEL = VO_LABEL + ".array";
    private static final String TRACE_TAG      = VO_LABEL;

    //constants for TestRoster scoringStatus
    public static final String NOT_SCORED       = "NA";
    public static final String SCHEDULED        = "SC";
    public static final String IN_PROGRESS      = "IP";
    public static final String IN_ERROR         = "ER";
    public static final String PARTIALLY_SCORED = "PC";
    public static final String SCORED           = "CO";


    /**
     * Gives the name for the scoringStatus value/code.
     * @param status_ String containing the code of the <code>ScoringStatus</code>.
     * @return String <code>Name</code> of scoringStatus.
     */
    public static String getScoredStatusName( String status_ ) {
        if      (status_.equals( RosterScoringStatus.NOT_SCORED ))
                return  "Not Scored";
        else if (status_.equals( RosterScoringStatus.SCHEDULED ))
                return "Scoring";
        else if (status_.equals( RosterScoringStatus.IN_PROGRESS ))
                return "Scoring";
        else if (status_.equals( RosterScoringStatus.IN_ERROR ))
                return "Scoring Problem";
        else if (status_.equals( RosterScoringStatus.PARTIALLY_SCORED ))
                return "Partially Scored";
        else if (status_.equals( RosterScoringStatus.SCORED ))
                return "Scored";
        else                                   //Should never be displayed; i.e.
                return "Not Supported Status"; //Indicates data/sw integrity error!!?
    }


    //...................................................................helpers

    /**
     * @param scoredStatus_ String containing the code of the <code>ScoredStatus</code>.
     * @return <code>true</code> if <b>ScoredStatus</b> indicates test is in "scoring has been scheduled" category; <code>false</code> otherwise.
     */
    public static boolean isScheduled( String scoredStatus_) {
        if( scoredStatus_.equals( RosterScoringStatus.SCHEDULED ) )
        {
            return true;
        }else{
            return false;
        }
    }

    /**
     * @param scoredStatus_ String containing the code of the <code>ScoredStatus</code>.
     * @return <code>true</code> if <b>ScoredStatus</b> indicates test is in "scoring in progress" category; <code>false</code> otherwise.
     */
    public static boolean isBeingScored( String scoredStatus_) {
        if( scoredStatus_.equals( RosterScoringStatus.IN_PROGRESS ) )
        {
            return true;
        }else{
            return false;
        }
    }

    /**
     * @param scoredStatus_ String containing the code of the <code>ScoredStatus</code>.
     * @return <code>true</code> if <b>ScoredStatus</b> indicates test has "Error in scoring" category; <code>false</code> otherwise.
     */
    public static boolean isErrorInScoring( String scoredStatus_) {
        if( scoredStatus_.equals( RosterScoringStatus.IN_ERROR ) )
        {
            return true;
        }else{
            return false;
        }
    }

    /**
     * @param scoredStatus_ String containing the code of the <code>ScoredStatus</code>.
     * @return <code>true</code> if <b>ScoredStatus</b> indicates test is in "partially scored" category; <code>false</code> otherwise.
     */
    public static boolean isPartiallyScored( String scoredStatus_) {
        if( scoredStatus_.equals( RosterScoringStatus.PARTIALLY_SCORED ) )
        {
            return true;
        }else{
            return false;
        }
    }

   /**
     * @param scoredStatus_ String containing the code of the <code>ScoredStatus</code>.
     * @return <code>true</code> if <b>ScoredStatus</b> indicates test is in "scored" category; <code>false</code> otherwise.
     */
    public static boolean isScored( String scoredStatus_) {
        if( scoredStatus_.equals( RosterScoringStatus.SCORED ) )
        {
            return true;
        }else{
            return false;
        }
    }

   /**
     * @param scoredStatus_ String containing the code of the <code>ScoredStatus</code>.
     * @return <code>true</code> if <b>ScoredStatus</b> indicates test is in "nothing happend in terms of scoring" category; <code>false</code> otherwise.
     */
    public static boolean isBeforeScoring( String scoredStatus_) {
        if( scoredStatus_.equals( RosterScoringStatus.NOT_SCORED ) )
        {
            return true;
        }else{
            return false;
        }
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
 * Revision 1.6  2004/06/29 18:21:17  ncohen
 * replace trunk with latest toast
 *
 * Revision 1.5.12.1  2004/03/23 23:02:05  rmbhat
 * imports corrected. there are more on the way.
 *
 * Revision 1.5  2003/04/28 17:29:44  oasuser
 * iknowR2v5 to trunk
 *
 * Revision 1.4  2003/04/15 16:06:58  oasuser
 * iknowR2v5 to trunk
 *
 * Revision 1.3  2003/04/13 21:38:20  oasuser
 * iknowR2v5 to trunk
 *
 * Revision 1.2  2003/04/07 22:49:56  oasuser
 * iknowR2v5 to trunk
 *
 * Revision 1.1.2.6  2003/04/11 18:30:16  kgawetsk
 * Changed "names" for new 0.4 wireframe 040803.pdf version.
 *
 * Revision 1.1.2.5  2003/04/11 17:40:31  kgawetsk
 * Added isReportable().
 *
 * Revision 1.1.2.7  2003/04/25 21:29:11  kgawetsk
 * Removed isReportable().
 *
 * Revision 1.1.2.6  2003/04/11 18:30:16  kgawetsk
 * Changed "names" for new 0.4 wireframe 040803.pdf version.
 *
 * Revision 1.1.2.5  2003/04/11 17:40:31  kgawetsk
 * Added isReportable().
 *
 * Revision 1.1.2.4  2003/04/04 01:59:20  kgawetsk
 * Remove db Schemas constants.
 *
 * Revision 1.1.2.3  2003/03/25 00:42:36  kgawetsk
 * javadoc corrections.
 *
 * Revision 1.1.2.2  2003/03/22 10:10:53  kgawetsk
 * Removed "states" setters - all in RosterEJB.
 *
 * Revision 1.1.2.1  2003/03/22 09:15:25  kgawetsk
 * Creation.
 *
 */
