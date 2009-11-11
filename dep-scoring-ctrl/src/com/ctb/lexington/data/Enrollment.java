package com.ctb.lexington.data;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * 
 * <p>
 * Company: Accenture
 * </p>
 */
import java.io.Serializable;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 * @version $Revision$
 */
public class Enrollment implements Serializable
{
    private int enrollmentCount;
    private int seedCount;
    private boolean isChanged;

    /**
     * Creates a new Enrollment object.
     *
     * @param seedC DOCUMENT ME!
     * @param enrC DOCUMENT ME!
     */
    public Enrollment(int seedC, int enrC)
    {
        this.seedCount = seedC;
        this.enrollmentCount = enrC;
    }

    /**
     * Creates a new Enrollment object.
     */
    public Enrollment() {}

    /**
     * DOCUMENT ME!
     *
     * @param enrollmentCount DOCUMENT ME!
     */
    public void setEnrollmentCount(int enrollmentCount)
    {
        this.enrollmentCount = enrollmentCount;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getEnrollmentCount()
    {
        return enrollmentCount;
    }

    /**
     * DOCUMENT ME!
     *
     * @param seedCount DOCUMENT ME!
     */
    public void setSeedCount(int seedCount)
    {
        this.seedCount = seedCount;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getSeedCount()
    {
        return seedCount;
    }
    
    public boolean isChanged() 
    {
       return isChanged;
    }
    
    public void setIsChanged(boolean isChanged_)
    {
        this.isChanged = isChanged_;
    }
}
