package com.ctb.lexington.data;

/**
 * StudentDetailVO.java
 *
 * Copyright CTB/McGraw-Hill, 2002
 * CONFIDENTIAL
 */

//java imports
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author  Tai Truong
 * @version $Id$
 * @version $Id$
 * @version $Id$
 * @version $Id$
 * @version $Id$
 * @version $Id$
 */
public class StudentDetailVO extends StudentVO
{
    /**
     * This beans's static label to be used for identification.
     */
    public static final String VO_LABEL       = "com.ctb.lexington.data.StudentDetailVO";
    public static final String VO_ARRAY_LABEL =  VO_LABEL + ".array";

    /**
     * The list of users that have scored this student
    */
    private List    scoredBy         = new ArrayList();
    private String  rosterStatusName = null;//testRoster specific... ejb beware!
    private String  scoringStatus    = null;//testRoster specific... ejb beware!
    private Boolean isReportable     = null;//testRoster specific->needs RosterId
    private String scoringDescription= null;


   /**
    * Constructor.
    */
    public StudentDetailVO()
    {
    }

    /**
     * Gets the list of users <code>UserVO</code> that have scored this student in this TestAdministration.
     *
     * @return the list.
     */
    public List getScoredBy()
    {
        return this.scoredBy;
    }

   /**
    * Sets the list of users <code>UserVO</code> that have scored this student in this TestAdministration.
    *
    * @param scoredBy_ the <code>List</code>.
    */
    public void setScoredBy( List scoredBy_ )
    {
        this.scoredBy = scoredBy_;
    }

    /**
     * Returns the rosterStatusName - this is not the same as TestCompletionStatusName!
     * @return String
     */
    public String getRosterStatusName() {
        return this.rosterStatusName;
    }
    /**
     * Returns the rosterStatusName.
     * @param String with the TestRoster <code>Status</code>.
     */
    public void setRosterStatusName( String rosterStatusName_ ) {
        this.rosterStatusName = rosterStatusName_;
    }

    /**
     * Returns the scoringStatus.
     * @return String
     */
    public String getScoringStatus()
    {
        return this.scoringStatus;
    }

    /**
     * Sets the scoringStatus.
     * @param scoringStatus The scoringStatus to set
     */
    public void setScoringStatus(String scoringStatus_)
    {
        this.scoringStatus = scoringStatus_;
    }


    /**
     * readonly property to get the description of scoringStatus.
     */
    public String getScoringDescription()
    {
        return this.scoringDescription;
    }

    /** Setter for property scoringDescription.
     * @param scoringDescription New value of property scoringDescription.
     */
    public void setScoringDescription(String scoringDescription) {
        this.scoringDescription = scoringDescription;
    }


    /**
     * Returns the isReportable
     * @return String
     */
    public boolean getIsReportable() {
        return( this.isReportable.booleanValue() );
    }
    /**
     * @param Boolean with the <code>isReportable</code>.
     */
    public void setIsReportable( Boolean isReportable_ ) {
        this.isReportable = isReportable_;
    }

}
/*
 * $Log$
 * Revision 1.1  2007/01/30 01:31:41  ncohen
 * port scoring to 4.x platform
 *
 * Revision 1.1  2006/02/23 20:47:39  ncohen
 * create new module for legacy OAS app
 *
 * Revision 1.8  2005/05/03 21:26:10  ncohen
 * replace HEAD with iknow-millbarge
 *
 * Revision 1.7.16.1  2004/08/17 22:01:42  binkley
 * Globally organize imports (removed 1,000+ warnings).
 *
 * Revision 1.7  2003/04/28 17:29:42  oasuser
 * iknowR2v5 to trunk
 *
 * Revision 1.6  2003/04/17 18:50:29  oasuser
 * iknowR2v5 to trunk
 *
 * Revision 1.5  2003/04/15 16:06:51  oasuser
 *
 * Revision 1.4  2003/04/13 21:38:06  oasuser
 * iknowR2v5 to trunk
 *
 * Revision 1.3  2003/04/07 22:49:15  oasuser
 * iknowR2v5 to trunk
 *
 * Revision 1.1.2.7  2003/04/11 21:07:37  kgawetsk
 * Added isReportable().
 *
 * Revision 1.1.2.6  2003/04/11 17:34:02  sprakash
 * no message
 *
 * Revision 1.1.2.12  2003/04/25 21:29:07  kgawetsk
 * CQDTS00016450 - added isReportable property.
 *
 * Revision 1.1.2.11  2003/04/15 05:16:10  ttruong
 * no message
 *
 * Revision 1.1.2.10  2003/04/15 05:08:58  ttruong
 * no message
 *
 * Revision 1.1.2.9  2003/04/15 01:22:48  rmariott
 * added scoringDescription
 *
 * Revision 1.1.2.8  2003/04/15 00:39:02  ttruong
 * add readonly property getScoringDescription for 1.5.2
 *
 * Revision 1.1.2.7  2003/04/11 21:07:37  kgawetsk
 * Added isReportable().
 *
 * Revision 1.1.2.6  2003/04/11 17:34:02  sprakash
 * no message
 *
 * Revision 1.1.2.5  2003/04/04 23:03:06  kgawetsk
 * Added rosterStatusName property.
 *
 * Revision 1.1.2.4  2003/03/28 08:50:24  kgawetsk
 * Removed rosterId - is in StudentVO; to be is DetailVO after refactoring of VO.
 *
 * Revision 1.1.2.3  2003/03/21 04:28:46  kgawetsk
 * Added testRosterid property with setter/getter.
 *
 * Revision 1.1.2.2  2003/03/21 00:10:43  kgawetsk
 * javadoc and alignments.
 *
 */
