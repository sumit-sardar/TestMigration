package com.ctb.bean.request; 

import java.io.Serializable;

/**
 * Contains a list of FilterParam objects, which are
 * applied to the data set returned by a platform call
 * to produce a filtered result list.
 * 
 * Valid filter types/argument datatypes are:  
 *      CONTAINS (String)
 *      EQUALS (String, Integer, Date)
 *      STARTSWITH (String)
 *      ENDSWITH (String)
 *      BEFORE (Date)
 *      AFTER (Date)
 *      BETWEEN (Date)
 *      GREATERTHAN (String, Integer, Date)
 *      LESSTHAN (String, Integer, Date)
 * 
 * @author Nate_Cohen
 */
public class FilterParams implements Serializable
{
    static final long serialVersionUID = 1L;
    private FilterParam [] filterParams;
    
    /**
     * Contains the available filter types, to use, attach one
     * of the static internal filter types to a FilterParam object
     * 
     * @author Nate_Cohen
     */
    public static final class FilterType implements Serializable {
        private String type;
        
        private FilterType(String type) {
            this.type = type;
        }
        
        /**
         * Default constructor
         *
         */
        public FilterType() {
        }
        
        /**
         * Returns filter type description
         * @return filter type string
         */
        public String getType() {
            return this.type;
        }
        
        /**
         * Sets the filter type
         * @param type
         */
        public void setType(String type) {
            this.type = type;
        }
    
        public static final FilterType CONTAINS = new FilterType("contains");
        public static final FilterType EQUALS = new FilterType("equals");
        public static final FilterType NOTEQUAL = new FilterType("notequal");
        public static final FilterType STARTSWITH = new FilterType("startswith");
        public static final FilterType ENDSWITH = new FilterType("endswith");
        public static final FilterType BEFORE = new FilterType("before");
        public static final FilterType AFTER = new FilterType("after");
        public static final FilterType BETWEEN = new FilterType("between");
        public static final FilterType GREATERTHAN = new FilterType(">");
        public static final FilterType LESSTHAN = new FilterType("<");
        
        public boolean equals(FilterType arg) {
            return this.getType().equals(arg.getType());
        }
    }

    /**
     * Default constructor
     *
     */
    public FilterParams() {
    }
    
    /**
     * Sets the array of filter params
     * @param filterParams
     */
    public void setFilterParams(FilterParam [] filterParams) {
        this.filterParams = filterParams;
    }
    
    /**
     * Gets the array of filter params
     * @return array of filter params
     */
    public FilterParam [] getFilterParams() {
        return this.filterParams;
    }
    
    /**
     * Represents an individual filter to be applied to
     * a list of results from a platform call
     * 
     * @author Nate_Cohen
     */
    public static class FilterParam implements Serializable{
        private String field;
        private Object [] argument;
        private FilterType type;
        
        /**
         * Constructs a new FilterParam
         * @param field - the field on the data object on which to operate
         * @param argument - the values to which the field values are to be compared
         * @param type - the type of filtering to perform, one of the statics in FilterType
         */
        public FilterParam (String field, Object [] argument, FilterType type) {
            this.field = field;
            this.argument = argument;
            this.type = type;
        }
        
        /**
         * Default constructor
         *
         */
        public FilterParam() {
        }
        
        /**
         * Gets the field
         * @return field
         */
        public String getField() {
            return this.field;
        }
        
        /**
         * Sets the field
         * @param field
         */
        public void setField(String field) {
            this.field = field;
        }
        
        /**
         * Gets the type
         * @return type
         */
        public FilterType getType() {
            return this.type;
        }
        
        /**
         * Sets the type
         * @param type
         */
        public void setType(FilterType type) {
            this.type = type;
        }
        
        /**
         * Gets the argument
         * @return argument
         */
        public Object [] getArgument() {
            return this.argument;
        }
        
        /**
         * Sets the argument
         * @param argument
         */
        public void setArgument(Object [] argument) {
            this.argument = argument;
        }
    }
} 
