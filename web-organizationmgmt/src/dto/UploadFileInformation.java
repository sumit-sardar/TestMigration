package dto; 


/**
 *@author Tata Consultancy Services
 * Upload File class  have the auditFileId,uploadDate,fileName,
 * recordUploaded,recordFailed and status informations. 
 */

public class UploadFileInformation implements java.io.Serializable{
    
        static final long serialVersionUID = 1L;
        
        private Integer auditFileId = new Integer(0);
	    private String uploadDate = "";
	    private String fileName = "";
	    private Integer recordUploaded = new Integer(0);
	    private Integer recordFailed = new Integer(0);
	    private String status = "";
        
       
       public UploadFileInformation() {
           this.auditFileId = new Integer(0);
           this.uploadDate = "";
           this.fileName = "";
           this.recordUploaded = new Integer(0);
           this.recordFailed = new Integer(0);
           this.status = ""; 
       }
       
       
        public UploadFileInformation(Integer auditFileId, String uploadDate,
                                     String fileName,Integer recordUploaded, 
                                     Integer recordFailed, String status){
            
           this.auditFileId = auditFileId;
           this.uploadDate = uploadDate;
           this.fileName = fileName;
           this.recordUploaded = recordUploaded;
           this.recordFailed = recordFailed;
           this.status = status;
        }
        
		/**
		 * @return Returns the auditFileId.
		 */
		public Integer getAuditFileId() {
			return auditFileId;
		}
		/**
		 * @param auditFileId The auditFileId to set.
		 */
		public void setAuditFileId(Integer auditFileId) {
			this.auditFileId = auditFileId;
		}
		/**
		 * @return Returns the fileName.
		 */
		public String getFileName() {
			return fileName;
		}
		/**
		 * @param fileName The fileName to set.
		 */
		public void setFileName(String fileName) {
			this.fileName = fileName;
		}
		/**
		 * @return Returns the recordFailed.
		 */
		public Integer getRecordFailed() {
			return recordFailed;
		}
		/**
		 * @param recordFailed The recordFailed to set.
		 */
		public void setRecordFailed(Integer recordFailed) {
			this.recordFailed = recordFailed;
		}
		/**
		 * @return Returns the recordUploaded.
		 */
		public Integer getRecordUploaded() {
			return recordUploaded;
		}
		/**
		 * @param recordUploaded The recordUploaded to set.
		 */
		public void setRecordUploaded(Integer recordUploaded) {
			this.recordUploaded = recordUploaded;
		}
		/**
		 * @return Returns the status.
		 */
		public String getStatus() {
			return status;
		}
		/**
		 * @param status The status to set.
		 */
		public void setStatus(String status) {
			this.status = status;
		}
		/**
		 * @return Returns the uploadDate.
		 */
		public String getUploadDate() {
			return uploadDate;
		}
		/**
		 * @param uploadDate The uploadDate to set.
		 */
		public void setUploadDate(String uploadDate) {
			this.uploadDate = uploadDate;
		} 
} 
