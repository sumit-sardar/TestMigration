package com.ctb.lexington.data;

import java.util.Collection;

/**
 *
 * @author  jbecker
 * @version
 */
public class CustomerResearchNameDetailVO extends CustomerResearchNameVO
{
	private Collection values = null;
	
	public void setValues(Collection values_){
		this.values = values_;
	}
	public Collection getValues(){
		return this.values;
	}
}
