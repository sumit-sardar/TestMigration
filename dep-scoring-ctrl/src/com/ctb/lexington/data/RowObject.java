package com.ctb.lexington.data;

/*
 * RowObject.java
 *
 * Copyright CTB/McGraw-Hill, 2003 CONFIDENTIAL
 *
 * @author <a href="mailto:Tai_Truong@ctb.com">Tai Truong</a>
 * @author <a href="mailto:jonathan_becker@ctb.com">Jon Becker</a>
 * @created September 26, 2003
 */

// java imports
import java.util.ArrayList;
import java.util.Map;

/*
 * RowObject class
*/
public abstract class RowObject implements java.io.Serializable, Comparable
{
    private int objectId = 0;
    private String name = null;
    private ArrayList textList = null;
    private Map properties = null;
    private Object domainObject = null;
    private String sort = null;

    // -- constructor --
    RowObject() { }

    // -- constructor --
    RowObject(int objectId_, String name_, ArrayList textList_, Map properties_, Object domainObject_)
    {
        this.objectId = objectId_;
        this.name = name_;
        this.textList = textList_;
        this.properties = properties_;
        this.domainObject = domainObject_;
    }

    // set objectId
    public void setObjectId(int objectId_)
    {
        this.objectId = objectId_;
    }

    // get objectId
    public int getObjectId()
    {
        return this.objectId;
    }

	// set name-
	public void setName(String name_)
	{
		this.name = name_;
	}

	// get name
	public String getName()
	{
		return this.name;
	}

    // set textList
    public void setTextList(ArrayList textList_)
    {
        this.textList = textList_;
    }

    // get textList
    public ArrayList setTextList()
    {
        return this.textList;
    }

    // set properties
    public void setProperties(Map properties_)
    {
        this.properties = properties_;
    }

    // get properties
    public Map getProperties()
    {
        return this.properties;
    }

    // set domainObject
    public void setDomainObject(Object domainObject_)
    {
        this.domainObject = domainObject_;
    }

    // get domainObject
    public Object setDomainObject()
    {
        return this.domainObject;
    }
    
    // set the sort string
    public void setSort(String sort_){
    	this.sort = sort_;
    }
    
    // compare to another row object - required for Comparable interface
    public int compareTo(Object obj_){
    	RowObject other = (RowObject) obj_;
    	String otherSortString = other.getSortString(this.sort);
    	String thisSortString = this.getSortString(this.sort);
    	return(thisSortString.compareTo(otherSortString));
    }
    
    public abstract String getSortString(String sort_);
    
    public abstract String getDisplayString(String sort_);
}


