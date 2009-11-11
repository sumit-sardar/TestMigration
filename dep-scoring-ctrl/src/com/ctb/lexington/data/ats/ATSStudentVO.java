package com.ctb.lexington.data.ats;

/*
 * ATSStudentVO.java
 *
 * Copyright CTB/McGraw-Hill, 2002
 * CONFIDENTIAL
 *
 */

import java.util.Date;

/**
 * @author <a href="mailto:kstorrs@ctb.com">Ken Storrs</a>
 * @version
 * $Id$
 */
public class ATSStudentVO extends Object implements java.io.Serializable
{
    public static final String VO_LABEL       = "com.ctb.lexington.data.ats.ATSStudentVO";
    public static final String VO_ARRAY_LABEL = "com.ctb.lexington.data.ats.ATSStudentVO.array";

    private String  studentFirstName     = null;
    private String  studentLastName      = null;
    private String  studentMiddleInitial = null;
    private Date    birthdate            = null;
    private String  gender               = null;
    private String  studentIdentifier1   = null;
    private String  name                 = null;
    private Integer studentDimId         = null;
    private Integer studentDimVersionId  = null;
    private Integer orgNodeDimId         = null;
    private String  category   = null;

       /** Creates new ATSStudentVO */
    public ATSStudentVO() {}

    /**
     * Simple setters/getters...
     */

    public String  getStudentFirstName()               { return this.studentFirstName; }
    public void    setStudentFirstName(String s)       { this.studentFirstName = s; }

    public String  getStudentLastName()                { return this.studentLastName; }
    public void    setStudentLastName(String s)        { this.studentLastName = s; }

    public String  getStudentMiddleInitial()           { return this.studentMiddleInitial; }
    public void    setStudentMiddleInitial( String s ) { this.studentMiddleInitial = s; }

    public Date    getBirthdate()                      { return this.birthdate; }
    public void    setBirthdate(Date d)                { this.birthdate = d; }

    public String  getGender()                         { return this.gender; }
    public void    setGender(String s)                 { this.gender = s; }

    public String  getStudentIdentifier1()             { return this.studentIdentifier1; }
    public void    setStudentIdentifier1(String s)     { this.studentIdentifier1 = s; }

    public String  getName()                           { return this.name; }
    public void    setName(String s)                   { this.name = s; }

    public Integer getStudentDimId()                   { return this.studentDimId; }
    public void    setStudentDimId(Integer i)          { this.studentDimId = i; }

    public Integer getStudentDimVersionId()            { return this.studentDimVersionId; }
    public void    setStudentDimVersionId(Integer i)   { this.studentDimVersionId = i; }

    public Integer getOrgNodeDimId()                   { return this.orgNodeDimId; }
    public void setOrgNodeDimId(Integer orgNodeDimId)  { this.orgNodeDimId = orgNodeDimId; }

    public String  getCategory()                           { return this.category; }
    public void    setCategory(String s)                   { this.category = s; }

}
