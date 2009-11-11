package com.ctb.lexington.db.irsdata;

import com.ctb.lexington.db.record.Persistent;
/**
 * @author Rama_Rao
 *
 */
public class IrsProductDimData implements Persistent{

    private Long productid;
    private String name;
    private Long productTypeid;
  

    public Long getProductid() {
        return this.productid;
    }

    public void setProductid(Long productid) {
        this.productid = productid;
    }
    
    public Long getProductTypeid() {
        return this.productTypeid;
    }

    public void setProductTypeid(Long productTypeid) {
        this.productTypeid = productTypeid;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public boolean equals(Object other) {
        return
            this.getName() != null && 
            this.getName().equals(((IrsProductDimData) other).getName());
    }

    public int hashCode() {
        return (int) productid.longValue();
    }
}
