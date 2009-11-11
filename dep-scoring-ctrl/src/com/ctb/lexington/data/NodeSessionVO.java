package com.ctb.lexington.data;

import java.util.Comparator;

/*
 * NodeSessionVO.java
 *
 * Copyright CTB/McGraw-Hill, 2005
 * CONFIDENTIAL
 *
 */

/**
 * This class extends {@link com.ctb.lexington.data.NodeVO} to provide session
 * information about a node.
 *
 * @author Ron Aikins
 * @version $Id$
 */
public class NodeSessionVO extends NodeVO
{
    public static final String VO_LABEL = "com.ctb.lexington.data.NodeSessionVO";
    public static final String VO_ARRAY_LABEL = VO_LABEL + ".array";
    private int numSessions;
    private int numSubOrgSessions;

    /**
     * Creates new NodeDetailVO
     */
    public NodeSessionVO()
    {
        super();
        this.numSessions = 0;
        this.numSubOrgSessions = 0;
    }

	public static class NodeSessionVOComparator implements Comparator {
		
		public static final int COMPARE_ORG_NODE_NAME = 1;
		public static final int COMPARE_NUM_SESSIONS = 2;
		public static final int COMPARE_NUM_SUBORG_SESSIONS = 3;
		
		int     primarySortField;
		boolean primarySortAscending;
		
		public NodeSessionVOComparator() {
			primarySortField = COMPARE_ORG_NODE_NAME;
			primarySortAscending = true;
		}
		
		/**
		 * @return Returns the primarySortField.
		 */
		public int getPrimarySortField() {
			return primarySortField;
		}
	
		/**
		 * @param primarySortField - The primarySortField to set.
		 */
		public void setPrimarySortField(int primarySortField) {
			this.primarySortField = primarySortField;
		}
	
		/**
		 * @return Returns the primarySortAscending.
		 */
		public boolean getPrimarySortAscending() {
			return primarySortAscending;
		}
	
		/**
		 * @param primarySortAscending - The primarySortAscending to set.
		 */
		public void setPrimarySortAscending(boolean primarySortAscending) {
			this.primarySortAscending = primarySortAscending;
		}
		
		public int compare(Object o1, Object o2) throws ClassCastException { 
			NodeSessionVO ns1 = null;
			NodeSessionVO ns2 = null;
			try {
				ns1 = (NodeSessionVO)o1;
				ns2 = (NodeSessionVO)o2;
			} catch (Exception e) {
				e.printStackTrace();
				ClassCastException cce = new ClassCastException();
				cce.setStackTrace(e.getStackTrace());
				throw cce;
			}
			int comp = 0;
			String str1 = null;
			String str2 = null;
			// 1st compare primary sort fields 
			switch (primarySortField) {
				case COMPARE_ORG_NODE_NAME:
				    str1 = ns1.getOrgNodeName().toLowerCase().trim();
				    str2 = ns2.getOrgNodeName().toLowerCase().trim();
					comp = str1.compareTo(str2);
					break;
				case COMPARE_NUM_SESSIONS:
					if (ns1.numSessions < ns2.numSessions) {
						comp = -1;
					}
					else if (ns1.numSessions > ns2.numSessions) {
						comp = 1;
					}
					break;
				case COMPARE_NUM_SUBORG_SESSIONS:
					if (ns1.numSubOrgSessions < ns2.numSubOrgSessions) {
						comp = -1;
					}
					else if (ns1.numSubOrgSessions > ns2.numSubOrgSessions) {
						comp = 1;
					}
					break;
			}
			// if primary fields differ, reverse comp value if descending
			if (comp != 0) {
				if (!primarySortAscending)
					comp = -comp;
			}
			else {	// primary fields equal, compare other fields in order;
					// NOTE!! ORDER IS IMPORTANT: IT IMPLEMENTS DESIGN ORDER OF SORT FIELDS
				if (primarySortField != COMPARE_ORG_NODE_NAME) {
				    str1 = ns1.getOrgNodeName().toLowerCase().trim();
				    str2 = ns2.getOrgNodeName().toLowerCase().trim();
					comp = str1.compareTo(str2);
				}
				if (comp == 0 && primarySortField != COMPARE_NUM_SESSIONS) {
					if (ns1.numSessions < ns2.numSessions) {
						comp = -1;
					}
					else if (ns1.numSessions > ns2.numSessions) {
						comp = 1;
					}
				}
				if (comp == 0 && primarySortField != COMPARE_NUM_SUBORG_SESSIONS) {
					if (ns1.numSubOrgSessions < ns2.numSubOrgSessions) {
						comp = -1;
					}
					else if (ns1.numSubOrgSessions > ns2.numSubOrgSessions) {
						comp = 1;
					}
				}
			}
			return comp;
		}
	}
	
    /**
     * Set the value of this property.
     *
     * @param numSessions_ value to set the property to.
     *
     * @return void
     */
    public void setNumSessions(int numSessions_)
    {
        this.numSessions = numSessions_;
    }

    /**
     * Get this property from this bean instance.
     *
     * @return int The value of the property.
     */
    public int getNumSessions()
    {
        return numSessions;
    }

    /**
     * Set the value of this property.
     *
     * @param numSubOrgSessions_ value to set the property to.
     *
     * @return void
     */
    public void setNumSubOrgSessions(int numSubOrgSessions_)
    {
        this.numSubOrgSessions = numSubOrgSessions_;
    }

    /**
     * Get this property from this bean instance.
     *
     * @return int The value of the property.
     */
    public int getNumSubOrgSessions()
    {
        return numSubOrgSessions;
    }
}
