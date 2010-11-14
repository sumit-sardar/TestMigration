package com.ctb.bean; 

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.request.FilterParams.FilterParam;
import com.ctb.bean.request.SortParams.SortParam;
import com.ctb.exception.request.InvalidFilterFieldException;
import com.ctb.exception.request.InvalidPageRequestedException;
import com.ctb.exception.request.InvalidSortFieldException;
import com.ctb.util.MathUtils;
import com.ctb.util.request.Filterer;
import com.ctb.util.request.Sorter;
import java.util.Arrays;
import java.util.Iterator;

/**
 * The base data collection object, all platform method return types
 * extend this object. Contains paging and result count information
 * and provides sorting, filtering, and paging capabilities
 * 
 * @author Nate_Cohen
 */
public abstract class CTBBeanData implements Serializable
{ 
    static final long serialVersionUID = 1L;
    
    private CTBBean [] beans;
    private Integer totalCount;
    private Integer filteredCount;
    private Integer totalPages;
    private Integer filteredPages;
    private Integer pageSize;
    
    private boolean paged = false;
    private boolean sorted = false;
    private boolean filtered = false;
    
    /**
     * Reduces the data array to a single page of results
     * according to the provided PageParams
     * 
     * @param params - paging params
     * @throws InvalidPageRequestedException
     */
    public void applyPaging(PageParams params) throws InvalidPageRequestedException{
        try {
            this.pageSize = new Integer(params.getPageSize());
            this.totalPages = MathUtils.intDiv(this.totalCount, this.pageSize);
            CTBBean [] page = new CTBBean [params.getPageSize()];
            int start = params.getPageSize() * (params.getPageRequested() - 1);
            int end = (params.getPageSize() * params.getPageRequested()) - 1;
            if(start >= totalCount.intValue()) {
                start = params.getPageSize() * (this.totalPages.intValue() - 1);
            }
            if(start < 0) {
                start = 0;
            }
            for(int i=0;i<page.length && (start+i) < beans.length;i++) {
                page[i] = beans[start + i];
            }
            this.beans = page;
            this.paged = true;
        } catch (Exception e) {
            InvalidPageRequestedException ipe = new InvalidPageRequestedException("CTBBeanData: applyPaging: page requested is invalid: " + params.getPageRequested());
            ipe.setStackTrace(e.getStackTrace());
            throw ipe;
        }
    }
    
    /**
     * Sorts the data array according to the provided SortParams
     * 
     * @param params - sorting params
     * @throws InvalidSortFieldException
     */
    public void applySorting(SortParams params) throws InvalidSortFieldException{
        SortParam [] sorts = params.getSortParams();
        List result = new ArrayList();
        for(int i=0;i<beans.length;i++) {
            result.add(beans[i]);
        }
        for(int iter=sorts.length - 1;iter>=0;iter--) {
            SortParam param = (SortParam) sorts[iter];
            try {
                Sorter.sort(result, param);
            } catch (Exception e) {
                InvalidSortFieldException ise = new InvalidSortFieldException(e.getMessage());
                ise.setStackTrace(e.getStackTrace());
                throw ise;
            }
        }
        
        Iterator iterator = result.iterator();
        CTBBean [] results = new CTBBean[result.size()];
        int i = 0;
        while (iterator.hasNext()) {
            results[i] = (CTBBean) iterator.next();
            i++;
        }
        this.beans = results;
        this.sorted = true;
    }
    
    /**
     * Filters the data array according to the provided FilterParams.
     * 
     * @param params - filtering params
     * @throws InvalidFilterFieldException
     */
    public void applyFiltering(FilterParams params) throws InvalidFilterFieldException{
        FilterParam [] filters = params.getFilterParams();
        List result = new ArrayList();
        for(int i=0;i<beans.length;i++) {
            result.add(beans[i]);
        }
        
        for(int iter=0;iter<filters.length;iter++) {
            FilterParam param = (FilterParam) filters[iter];
            try {
                 Filterer.filter(result, param);
            } catch (Exception e) {
                InvalidFilterFieldException ife = new InvalidFilterFieldException(e.getMessage());
                ife.setStackTrace(e.getStackTrace());
                throw ife;
            }
        }
        
        Iterator iterator = result.iterator();
        CTBBean [] results = new CTBBean[result.size()];
        int i = 0;
        while (iterator.hasNext()) {
            results[i] = (CTBBean) iterator.next();
            i++;
        }
        this.beans = results;

        this.filtered = true;
        
        this.filteredCount = new Integer(result.size());
        this.filteredPages =  MathUtils.intDiv(this.filteredCount, this.pageSize);
    }
    
    /**
	 * @return Returns the filteredCount.
	 */
	public Integer getFilteredCount() {
		return filteredCount;
	}
	/**
	 * @param filteredCount The filteredCount to set.
	 */
	public void setFilteredCount(Integer filteredCount) {
		this.filteredCount = filteredCount;
	}
	/**
	 * @return Returns the filteredPages.
	 */
	public Integer getFilteredPages() {
		return filteredPages;
	}
	/**
	 * @param filteredPages The filteredPages to set.
	 */
	public void setFilteredPages(Integer filteredPages) {
		this.filteredPages = filteredPages;
	}
    /**
     * @return Returns the beans as a list.
     */
    public List getBeansAsList() {
        return Arrays.asList(this.beans);
    }
    /**
	 * @param beans The beans to set as a list.
	 */
    public void setBeansWithList(List beans, Integer pageSize) {
        CTBBean [] beanArray = (CTBBean[]) beans.toArray(new CTBBean[0]);
        this.setBeans(beanArray, pageSize);
    }
    /**
	 * @return Returns the beans.
	 */
	protected CTBBean[] getBeans() {
		return this.beans;
	}
	/**
	 * @param beans The beans to set.
	 */
	protected void setBeans(CTBBean[] beans, Integer pageSize) {
		this.beans = beans;
        if(beans != null) {
            this.totalCount = new Integer(beans.length);
        } else {
            this.totalCount = new Integer(0);
        }
        if(pageSize != null) {
            this.pageSize = pageSize;
        } else {
            this.pageSize = new Integer(Integer.MAX_VALUE);
        }
        this.totalPages = MathUtils.intDiv(this.totalCount, this.pageSize);
        this.filteredCount = this.totalCount;
        this.filteredPages = this.totalPages;
	}
    
    
	/**
	 * @param beans The beans to set.
	 */
	protected void setBeans(CTBBean[] beans) {
		this.beans = beans;
        if(beans != null) {
            this.totalCount = new Integer(beans.length);
        } else {
            this.totalCount = new Integer(0);
        }
        this.pageSize = new Integer(Integer.MAX_VALUE);
        this.totalPages = MathUtils.intDiv(this.totalCount, this.pageSize);
        this.filteredCount = this.totalCount;
        this.filteredPages = this.totalPages;
	}

    
	/**
	 * @return Returns the totalCount.
	 */
	public Integer getTotalCount() {
		return totalCount;
	}
	/**
	 * @param totalCount The totalCount to set.
	 */
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}
	/**
	 * @return Returns the totalPages.
	 */
	public Integer getTotalPages() {
		return totalPages;
	}
	/**
	 * @param totalPages The totalPages to set.
	 */
	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}
} 
