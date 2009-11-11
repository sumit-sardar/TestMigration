package com.ctb.lexington.db.irsdata;

/**
 * @author Rama_Rao
 *
 */
import com.ctb.lexington.db.record.Persistent;

public class IrsCustomerDimData implements Persistent {
	 private Long customerid;
	 private String name;
     private String key;

	    public Long getCustomerid() {
	        return customerid;
	    }

	    public void setCustomerid(Long customerid) {
	        this.customerid = customerid;
	    }

	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }
        
        public String getKey() {
	        return key;
	    }

	    public void setKey(String key) {
	        this.key = key;
	    }

        public boolean equals(Object other) {
            return
                this.getCustomerid().equals(((IrsCustomerDimData) other).getCustomerid()) &&
                this.getName().equals(((IrsCustomerDimData) other).getName());
        }

        public int hashCode() {
            return (int) customerid.longValue();
        }

}
