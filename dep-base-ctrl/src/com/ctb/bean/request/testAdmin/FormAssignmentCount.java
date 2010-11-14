package com.ctb.bean.request.testAdmin; 

    import com.ctb.bean.CTBBean;

    /**
     * bean used internally by the platform to manage
     * random test form distribution within test sessions.
     */
    public class FormAssignmentCount extends CTBBean{
        static final long serialVersionUID = 1L;
        private String form;
        private Integer count;
        
        public String getForm() {
            return this.form;
        }
        
        public void setForm(String form) {
            this.form = form;
        }
        
        public Integer getCount() {
            return this.count;
        }
        
        public void setCount(Integer count) {
            this.count = count;
        }
    }
