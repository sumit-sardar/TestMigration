package com.ctb.bean.request; 


import java.io.Serializable;

/**
 * Contains a list of SortParam objects, which are
 * applied to the data set returned by a platform call
 * to produce a sorted result list. When multiple SortParam
 * objects are provided, multiple sorts are applied with
 * precedence corresponding to the order of the objects, eg.
 * a user list sorted by last name, first name, then middle
 * initial.
 * 
 * Valid Sort types are:
 *      ALPHAASC
 *      ALPHADESC
 *      NUMASC
 *      NUMDESC
 * 
 * @author ncohen
 */
public class SortParams implements Serializable
{
    static final long serialVersionUID = 1L;
    private SortParam [] sortParams;
    
    /**
     * Contains the available sort types, to use, attach one
     * of the static internal sort types to a SortParam object
     * 
     * @author Nate_Cohen
     */
    public static final class SortType implements Serializable{
        private String type;
        
        private SortType(String type) {
            this.type = type;
        }
        
        /**
         * Default constructor
         *
         */
        public SortType() {
        }
        
        /**
         * Get the sort type string
         * @return type
         */
        public String getType() {
            return this.type;
        }
        
        /**
         * Set the sort type string
         * @param type
         */
        public void setType(String type) {
            this.type = type;
        }
        
        public boolean equals(SortType arg) {
            return this.getType().equals(arg.getType());
        }
    
        public static final SortType ALPHAASC = new SortType("asc");
        public static final SortType ALPHADESC = new SortType("desc");
        public static final SortType NUMASC = new SortType("asc");
        public static final SortType NUMDESC = new SortType("desc");
    }

    /**
     * Default constructor
     *
     */
    public SortParams() {
    }
    
    /**
     * Represents an individual sort to be applied to
     * a list of results from a platform call
     * 
     * @author Nate_Cohen
     */
    public static class SortParam implements Serializable{
        private String field;
        private SortType type;
        
        /**
         * Constructs a new SortParam
         * 
         * @param field - the field on the data object on which to operate
         * @param type - the type of sorting to perform, one of the statics in SortType
         */
        public SortParam (String field, SortType type) {
            this.field = field;
            this.type = type;
        }
        
        public SortParam() {
        }
        
        public String getField() {
            return this.field;
        }
        
        public void setField(String field) {
            this.field = field;
        }
        
        public SortType getType() {
            return this.type;
        }
        
        public void setType(SortType type) {
            this.type = type;
        }
    }
    
    public SortParam [] getSortParams() {
        return this.sortParams;
    }
    
    public void setSortParams(SortParam [] sortParams) {
        this.sortParams = sortParams;
    }
} 
