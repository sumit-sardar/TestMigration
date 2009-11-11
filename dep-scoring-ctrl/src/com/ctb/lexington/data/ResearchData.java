/*
 * Created on Apr 1, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ctb.lexington.data;


/**
 * @author Wen-Jin_Chang
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ResearchData implements java.io.Serializable
{
	private Integer CustomerRearchNameId = null;
	private String DataName = null;
	private String DataValue = null;
	private boolean checked = false;
	
	public ResearchData()
	{
	}
	
	/**
	 * @return
	 */
	public Integer getCustomerRearchNameId()
	{
		return CustomerRearchNameId;
	}

	/**
	 * @return
	 */
	public String getDataName()
	{
		return DataName;
	}

	/**
	 * @return
	 */
	public String getDataValue()
	{
		return DataValue;
	}

	/**
	 * @param integer
	 */
	public void setCustomerRearchNameId(Integer integer)
	{
		CustomerRearchNameId = integer;
	}

	/**
	 * @param string
	 */
	public void setDataName(String string)
	{
		DataName = string;
	}

	/**
	 * @param string
	 */
	public void setDataValue(String string)
	{
		DataValue = string;
	}

	/**
	 * @return
	 */
	public boolean isChecked()
	{
		return checked;
	}

	/**
	 * @param b
	 */
	public void setChecked(boolean b)
	{
		checked = b;
	}

}
