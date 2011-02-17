package com.ctb.xmlProcessing.utils;

import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.HibernateException;

import java.util.*;



import com.ctb.hibernate.persist.ScoreLookupRecord;
import com.ctb.common.tools.SystemException;


public class ContentAreaInfo {
    private String name;
    private String abbreviation;
    private boolean scoringEnabled;
    private List scoreLookup;

    public ContentAreaInfo(String name, String abbreviation) {
        this(name,abbreviation,true);
    }

    public ContentAreaInfo(String name,String abbreviation,boolean scoringEnabled) {
        this.name = name;
        this.abbreviation = abbreviation;
        this.scoringEnabled = scoringEnabled;
    }
    /**
     * @return Returns the abbreviation.
     */
    public String getAbbreviation() {
        return abbreviation;
    }

    /**
     * @param abbreviation The abbreviation to set.
     */
    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Returns the scoreLookup.
     */
    public List getScoreLookup() {
        return scoreLookup;
    }
    /**
     * @return Returns the scoreLookup.
     */
    public Collection getScoreLookupCodes(Session session, String frameworkCode,String level,String form,
                                    String productInternalDisplayName) {
        Set resultSet = new HashSet();
        if (!scoringEnabled || scoreLookup == null )
            return resultSet;
        try {
            String hql = "from " + ScoreLookupRecord.class.getName() + " as rec " +
                    "where rec.frameworkCode = :fCode " +
                    "and rec.contentArea in (:myAreas) " +
                    "and (rec.testForm is null or rec.testForm = :myForm) " +
                    "and rec.productInternalDisplayName = :myProductInternalDisplayName " +
                    "and (rec.testLevel is null or rec.testLevel = :myLevel)";


            Query query = session.createQuery(hql);
            query.setParameter("fCode",frameworkCode);
            query.setParameter("myLevel",level);
            query.setParameter("myProductInternalDisplayName",productInternalDisplayName);
            query.setParameter("myForm",form);
            query.setParameterList("myAreas",getScoreLookup());
            resultSet.addAll(query.list());

        } catch (HibernateException e) {
            throw new SystemException(e.getMessage(),e);
        }

        if (resultSet.isEmpty())
        {
/*            throw new SystemException("No Scoring information was found in OAS for " +
                    productInternalDisplayName + " Level:" + level + " Form:" + form +
                    " Content Areas:" + getScoreLookup()); */
        }
        return resultSet;
    }

    /**
     * @param scoreLookup The scoreLookup to set.
     */
    public void setScoreLookup(List scoreLookup) {
        this.scoreLookup = scoreLookup;
    }

    public String toString() {
        StringBuffer result = new StringBuffer();

        result.append("\t\t");
        result.append(getName());
        result.append(": ");
        result.append(getAbbreviation());
        result.append("\n");

        result.append("\t\tScore Lookup: " + scoreLookup);

        return result.toString();
    }
}