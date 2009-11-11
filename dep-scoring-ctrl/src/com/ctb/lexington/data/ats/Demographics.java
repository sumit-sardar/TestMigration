package com.ctb.lexington.data.ats;

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
 * Copyright: Copyright (c) CTB McGraw-Hill 2002
 * </p>
 * 
 * <p>
 * Company: Accenture
 * </p>
 *
 * @author Vishal Saxena
 * @version 1.0
 */
public class Demographics implements java.io.Serializable
{
    private String englishSecondLanguage;
    private String ethnicity;
    private String lepLevel;
    private String socioEconomicStatus;
    private String specialEducation;
    private boolean hasChanged = false;
    private int demographicsID;

    /**
     * Creates a new Demographics object.
     */
    public Demographics() {}

    /**
     * DOCUMENT ME!
     *
     * @param demographicsID DOCUMENT ME!
     */
    public void setDemographicsID(int demographicsID)
    {
        this.demographicsID = demographicsID;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDemographicsID()
    {
        return demographicsID;
    }

    /**
     * DOCUMENT ME!
     *
     * @param englishSecondLanguage DOCUMENT ME!
     */
    public void setEnglishSecondLanguage(String englishSecondLanguage)
    {
        this.englishSecondLanguage = englishSecondLanguage;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getEnglishSecondLanguage()
    {
        return englishSecondLanguage;
    }

    /**
     * DOCUMENT ME!
     *
     * @param b DOCUMENT ME!
     */
    public void setHasChanged(boolean b)
    {
        this.hasChanged = b;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getHasChanged()
    {
        return this.hasChanged;
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args)
    {
        Biographics biographics1 = new Biographics();
    }

    /**
     * DOCUMENT ME!
     *
     * @param ethnicity DOCUMENT ME!
     */
    public void setEthnicity(String ethnicity)
    {
        this.ethnicity = ethnicity;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getEthnicity()
    {
        return ethnicity;
    }

    /**
     * DOCUMENT ME!
     *
     * @param lepLevel DOCUMENT ME!
     */
    public void setLepLevel(String lepLevel)
    {
        this.lepLevel = lepLevel;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getLepLevel()
    {
        return lepLevel;
    }

    /**
     * DOCUMENT ME!
     *
     * @param socioEconomicStatus DOCUMENT ME!
     */
    public void setSocioEconomicStatus(String socioEconomicStatus)
    {
        this.socioEconomicStatus = socioEconomicStatus;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getSocioEconomicStatus()
    {
        return socioEconomicStatus;
    }

    /**
     * DOCUMENT ME!
     *
     * @param specialEducation DOCUMENT ME!
     */
    public void setSpecialEducation(String specialEducation)
    {
        this.specialEducation = specialEducation;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getSpecialEducation()
    {
        return specialEducation;
    }
}
