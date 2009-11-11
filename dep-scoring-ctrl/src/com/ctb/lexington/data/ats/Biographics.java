package com.ctb.lexington.data.ats;

import java.util.Date;


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
 * Copyright: Copyright (c) CTB McGraw-Hill2002
 * </p>
 * 
 * <p>
 * Company: Accenture
 * </p>
 *
 * @author Vishal Saxena
 * @version 1.0
 */
public class Biographics implements java.io.Serializable
{
    private Date birthdate;
    private String firstName;
    private String gender;
    private String lastName;
    private String middleInitial;
    private String studentTestNumber;
    private String withdrawReason;

    /**
     * Creates a new Biographics object.
     */
    public Biographics() {}

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args)
    {
        Demographics demographics1 = new Demographics();
    }

    /**
     * DOCUMENT ME!
     *
     * @param birthdate DOCUMENT ME!
     */
    public void setBirthdate(Date birthdate)
    {
        this.birthdate = birthdate;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Date getBirthdate()
    {
        return birthdate;
    }

    /**
     * DOCUMENT ME!
     *
     * @param firstName DOCUMENT ME!
     */
    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getFirstName()
    {
        return firstName;
    }

    /**
     * DOCUMENT ME!
     *
     * @param gender DOCUMENT ME!
     */
    public void setGender(String gender)
    {
        this.gender = gender;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getGender()
    {
        return gender;
    }

    /**
     * DOCUMENT ME!
     *
     * @param lastName DOCUMENT ME!
     */
    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getLastName()
    {
        return lastName;
    }

    /**
     * DOCUMENT ME!
     *
     * @param middleInitial DOCUMENT ME!
     */
    public void setMiddleInitial(String middleInitial)
    {
        this.middleInitial = middleInitial;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getMiddleInitial()
    {
        return middleInitial;
    }

    /**
     * DOCUMENT ME!
     *
     * @param studentTestNumber DOCUMENT ME!
     */
    public void setStudentTestNumber(String studentTestNumber)
    {
        this.studentTestNumber = studentTestNumber;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getStudentTestNumber()
    {
        return studentTestNumber;
    }

    /**
     * DOCUMENT ME!
     *
     * @param withdrawReason DOCUMENT ME!
     */
    public void setWithdrawReason(String withdrawReason)
    {
        this.withdrawReason = withdrawReason;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getWithdrawReason()
    {
        return withdrawReason;
    }
}
