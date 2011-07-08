package com.ctb.bean.dataExportManagement;

import com.ctb.bean.CTBBean;
import com.ctb.bean.CTBBeanData;

public class ManageJobData extends CTBBeanData
{
	static final long serialVersionUID = 1L;
    /**
	 * Gets the array of ManageJob beans
	 * @return array of ManageJob
	 */
	public ManageJob[] getManageJobs() {
        CTBBean [] beans = this.getBeans();
        ManageJob[] result = new ManageJob[beans.length];
        for(int i=0;i<beans.length;i++)
            result[i] = (ManageJob) beans[i];
        return result;
	}
	
    /**
     * Sets the array of ManageJob beans
     * @param jobs - the array of ManageJob beans
     * @param pageSize - The number of beans to include in one page of data
     */
    public void setManageJobs(ManageJob [] jobs, Integer pageSize) {
        this.setBeans(jobs, pageSize);
    }
}
