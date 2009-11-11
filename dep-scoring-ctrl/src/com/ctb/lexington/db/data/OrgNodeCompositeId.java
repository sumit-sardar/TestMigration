package com.ctb.lexington.db.data;



public class OrgNodeCompositeId{
        private Long orgNodeId;
        private Long orgNodeVersionNumber;
        
        public OrgNodeCompositeId(){
            
        }
        /**
         * @return Returns the orgNodeId.
         */
        public Long getOrgNodeId() {
            return orgNodeId;
        }

        /**
         * @param orgNodeId The orgNodeId to set.
         */
        public void setOrgNodeId(Long orgNodeId) {
            this.orgNodeId = orgNodeId;
        }

        /**
         * @return Returns the orgNodeVersionNumber.
         */
        public Long getOrgNodeVersionNumber() {
            return orgNodeVersionNumber;
        }

        /**
         * @param orgNodeVersionNumber The orgNodeVersionNumber to set.
         */
        public void setOrgNodeVersionNumber(Long orgNodeVersionNumber) {
            this.orgNodeVersionNumber = orgNodeVersionNumber;
        }
    }
    