package com.ctb.lexington.db.irsdata;

import com.ctb.lexington.db.record.Persistent;
import java.util.Date;

/**
 * @author Rama_Rao
 *
 */
public class IrsProgramDimData implements Persistent{
		private Long programid;
	    private Long customerid;
	    private String name;
	    private Date progStartDate;
	    private Date progEndDate;
        private String ageCategory;
        private Long productTypeid;
        private String normsGroup;
        private String normsYear;

        /**
         * @return the normsGroup
         */
        public String getNormsGroup() {
            return normsGroup;
        }
        /**
         * @param normsGroup the normsGroup to set
         */
        public void setNormsGroup(String normsGroup) {
            this.normsGroup = normsGroup;
        }
        /**
         * @return the normsYear
         */
        public String getNormsYear() {
            return normsYear;
        }
        /**
         * @param normsYear the normsYear to set
         */
        public void setNormsYear(String normsYear) {
            this.normsYear = normsYear;
        }
        public Long getProductTypeid() {
            return this.productTypeid;
        }

        public void setProductTypeid(Long productTypeid) {
            this.productTypeid = productTypeid;
        }

	    public Long getProgramid() {
	        return programid;
	    }
	    public void setProgramid(Long programid) {
	        this.programid = programid;
	    }
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
        
        public String getAgeCategory() {
	        return ageCategory;
	    }
	    public void setAgeCategory(String ageCategory) {
	        this.ageCategory = ageCategory;
	    }

	    public Date getProgStartDate() {
	        return progStartDate;
	    }
	    public void setProgStartDate(Date progStartDate) {
	        this.progStartDate = progStartDate;
	    }

	    public Date getProgEndDate() {
	        return progEndDate;
	    }
	    public void setProgEndDate(Date progEndDate) {
	        this.progEndDate = progEndDate;
	    }
        public boolean equals(Object other) {
            return
                this.getName() != null &&
                this.getName().equals(((IrsProgramDimData) other).getName()) &&
                this.getCustomerid() != null &&
                this.getCustomerid().equals(((IrsProgramDimData) other).getCustomerid()) &&
                this.getProgStartDate() != null &&
                this.getProgStartDate().equals(((IrsProgramDimData) other).getProgStartDate()) &&
                this.getProgEndDate() != null &&
                this.getProgEndDate().equals(((IrsProgramDimData) other).getProgEndDate()) &&
                this.getNormsGroup() != null &&
                this.getNormsGroup().equals(((IrsProgramDimData) other).getNormsGroup()) &&
                this.getNormsYear() != null &&
                this.getNormsYear().equals(((IrsProgramDimData) other).getNormsYear()) &&
                this.getProductTypeid() != null &&
                this.getProductTypeid().equals(((IrsProgramDimData) other).getProductTypeid());
        }

        public int hashCode() {
            return (int) programid.longValue();
        }
}
