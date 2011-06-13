package CTB;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.logging.Level;

import javax.servlet.http.HttpServletResponse;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.FormData;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.commons.fileupload.FileUpload;
import org.apache.struts.upload.FormFile;

import com.ctb.util.OASLogger;
import com.ctb.web.util.Utils;
import com.ctb.web.util.stgtms.CTBAssessmentDeliveryProcessor;
import com.ctb.web.util.stgtms.CTBLoginRequestProcessor;
import com.ctb.web.util.stgtms.CTBSaveRequestProcessor;
import com.ctb.web.util.stgtms.CTBUploadAuditFileProcessor;
import com.ctb.web.util.stgtms.CTBLoadTestRequestProcessor;
import com.ctb.control.testDelivery.LoadTest;
import com.ctb.exception.CTBBusinessException;

/**
 * @jpf:controller
 *  */
@Jpf.Controller(multipartHandler=Jpf.MultipartHandler.memory)
public class CTBController extends PageFlowController
{
    /**
     * @common:control
     */
    @Control()
    private com.ctb.control.testDelivery.AssessmentDelivery adsControl;

    /**
     * @common:control
     */
    @Control()
    private com.ctb.control.testDelivery.StudentTestDeliveryAudit auditControl;


    /**
     * @common:control
     */
    @Control()
    private com.ctb.control.testDelivery.StudentLogin loginControl;
    
    /**
     * @common:control
     */
    @Control()
    private com.ctb.control.testDelivery.StudentTestData saveControl;

	@Control
	private LoadTest loadTestControl;


    /**
     * @jpf:action
     * @jpf:forward name="index" path="index.jsp"
     * @jpf:forward name="error" path="error.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "index",
                     path = "index.jsp"), 
        @Jpf.Forward(name = "error",
                     path = "error.jsp")
    })
    protected Forward begin(TestForm test)
    {   
        return handleTMSRequest(test);
    }
    
    
    @Jpf.Action(forwards = { 
            @Jpf.Forward(name = "index",
                         path = "index.jsp"), 
            @Jpf.Forward(name = "error",
                         path = "error.jsp")
        })
        protected Forward getMp3(TestForm test) throws IOException
        {   
    	
    		System.out.println("getMp3 fired");
    		HttpServletResponse resp = this.getResponse();     
    		byte [] musicFile = null;
    		resp.setContentType("audio/mpeg3");
    		
    		try {
				musicFile = loginControl.studentMusicFile(test.getMusicId());
				
				System.out.println("getMp3 fired music file size" + musicFile.length);
				resp.flushBuffer();
				OutputStream stream = resp.getOutputStream();
	    		stream.write(musicFile);
	    		stream.close();
			} catch (CTBBusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    		
    		return null;
        }

    /**
     * @jpf:action
     * @jpf:forward name="index" path="index.jsp"
     * @jpf:forward name="error" path="error.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "index",
                     path = "index.jsp"), 
        @Jpf.Forward(name = "error",
                     path = "error.jsp")
    })
    protected Forward login(TestForm test)
    {
        return handleTMSRequest(test);
    }
    
    /**
     * @jpf:action
     * @jpf:forward name="index" path="index.jsp"
     * @jpf:forward name="error" path="error.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "index",
                     path = "index.jsp"), 
        @Jpf.Forward(name = "error",
                     path = "error.jsp")
    })
    protected Forward save(TestForm test)
    {
        return handleTMSRequest(test);
    }
    
    /**
     * @jpf:action
     * @jpf:forward name="index" path="index.jsp"
     * @jpf:forward name="error" path="error.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "index",
                     path = "index.jsp"), 
        @Jpf.Forward(name = "error",
                     path = "error.jsp")
    })
    protected Forward feedback(TestForm test)
    {
        return handleTMSRequest(test);
    }
    
    /**
     * @jpf:action
     * @jpf:forward name="index" path="index.jsp"
     * @jpf:forward name="error" path="error.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "index",
                     path = "index.jsp"), 
        @Jpf.Forward(name = "error",
                     path = "error.jsp")
    })
    protected Forward getSubtest(TestForm test)
    {
        return handleTMSRequest(test);
    }
 
    /**
     * @jpf:action
     * @jpf:forward name="index" path="index.jsp"
     * @jpf:forward name="error" path="error.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "index",
                     path = "index.jsp"), 
        @Jpf.Forward(name = "error",
                     path = "error.jsp")
    })
    protected Forward downloadItem(TestForm test)
    {
        return handleTMSRequest(test);
    }
    

    /**
     * @jpf:action 
     * @jpf:forward name="index" path="index.jsp"
     * @jpf:forward name="error" path="error.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "index",
                     path = "index.jsp"), 
        @Jpf.Forward(name = "error",
                     path = "error.jsp")
    })
    protected Forward uploadAuditFile(TestForm test)
    {
        String auditFile = null;
        String requestXML = null;
        String checksum = null;
        try
        {
            // pull XML off of request
            requestXML = this.getRequest().getParameter("RequestXML");
            if (requestXML == null)
                this.getRequest().getParameter("requestXML");
            if (requestXML == null)
                requestXML = test.getRequestXML();


            // check requestXML
            if (requestXML == null)
            {
                OASLogger.getLogger("TestDelivery").debug("Invalid request: Missing requestXML");
                getRequest().setAttribute("errorMessage", "Missing requestXML");
                return new Forward("error");
            } 
            
            checksum = this.getRequest().getParameter("checksum");
            if (checksum == null)
            {
                OASLogger.getLogger("TestDelivery").debug("Invalid request: Missing checksum");
                getRequest().setAttribute("errorMessage", "Missing checksum");
                return new Forward("error");
            } 
            
            

            // make sure request contain multipart content
            if (!FileUpload.isMultipartContent(this.getRequest()))
            {
                OASLogger.getLogger("TestDelivery").debug("Not Multipart Content");
                getRequest().setAttribute("errorMessage", "Not Multipart Content");
                return new Forward("error");
            }     
            
            //Get the file item named 'auditFile'
            Hashtable fileElements = test.getMultipartRequestHandler().getFileElements();
            FormFile fileItem = (FormFile)fileElements.get("auditFile");  
            
            //return error if file item not there
            if (fileItem == null)
            {
                OASLogger.getLogger("TestDelivery").debug("Missing file 'auditFile' in upload request");
                getRequest().setAttribute("errorMessage", "Missing file 'auditFile' in upload request");
                return new Forward("error");
                
            }     
            
            //get file data                      
            int fileSize = fileItem.getFileSize();
            byte [] buf = fileItem.getFileData();
            auditFile = new String(buf);
            
            long chksum = Long.parseLong(checksum);
            
            if (Utils.getChecksum(buf) != chksum)
            {
                OASLogger.getLogger("TestDelivery").debug("Checksum doesn't match.");
                getRequest().setAttribute("errorMessage", "Checksum doesn't match.");
                return new Forward("error");
            }     
                
  
            String responseXML = null;
            responseXML = CTBUploadAuditFileProcessor.processUploadAuditFile(requestXML, auditControl, auditFile);
            // place response XML on request for use by JSP
            this.getRequest().setAttribute("responseXML", responseXML);
             
            this.getSession().invalidate();
            return new Forward("index");
        }
        catch (Exception e)
        {
            OASLogger.debugStackTrace("TestDelivery", e.getStackTrace());
            OASLogger.getLogger("TestDelivery").debug("Offending request: requestXML=" +
                                                      requestXML);
            getRequest().setAttribute("errorMessage", e.getMessage());
            return new Forward("error");
        }
    }
    
    /**
     * @jpf:action
     * @jpf:forward name="index" path="index.jsp"
     * @jpf:forward name="error" path="error.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "index",
                     path = "index.jsp"), 
        @Jpf.Forward(name = "error",
                     path = "error.jsp")
    })
    protected Forward writeToAuditFile(TestForm test)
    {

        String requestXML = null;
        try
        {
            // pull XML off of request
            requestXML = this.getRequest().getParameter("RequestXML");
            if (requestXML == null)
                this.getRequest().getParameter("requestXML");
            if (requestXML == null)
                requestXML = test.getRequestXML();

            // check requestXML
            if (requestXML == null)
            {
                OASLogger.getLogger("TestDelivery").debug("Invalid request: Missing requestXML");
                getRequest().setAttribute("errorMessage", "Missing requestXML");
                return new Forward("error");
            } 

            String responseXML = null;
            responseXML = CTBUploadAuditFileProcessor.processWriteToAuditFile(requestXML);
            // place response XML on request for use by JSP
            this.getRequest().setAttribute("responseXML", responseXML);
             
            this.getSession().invalidate();
            return new Forward("index");
        } catch (Exception e) {
            OASLogger.debugStackTrace("TestDelivery", e.getStackTrace());
            OASLogger.getLogger("TestDelivery").debug("Offending request: requestXML=" + requestXML);
            getRequest().setAttribute("errorMessage", e.getMessage());
            return new Forward("error");
        }
    }


    /**
     * @jpf:action
     * @jpf:forward name="index" path="index.jsp"
     * @jpf:forward name="error" path="error.jsp"
     */
	@Jpf.Action(
		forwards = { 
			@Jpf.Forward(name = "index", path = "index.jsp"), 
			@Jpf.Forward(name = "error", path = "error.jsp")
		}
	)
    protected Forward getStatus(TestForm test)
    {

//        String requestXML = null;
        try {

            String responseXML = null;
            responseXML = CTBUploadAuditFileProcessor.processGetStatus();
            // place response XML on request for use by JSP
            this.getRequest().setAttribute("responseXML", responseXML);
             
            this.getSession().invalidate();
            return new Forward("index");
        } catch (Exception e) {
            OASLogger.debugStackTrace("TestDelivery", e.getStackTrace());
            OASLogger.getLogger("TestDelivery").debug("Offending request:" + e.getMessage());
            getRequest().setAttribute("errorMessage", e.getMessage());
            return new Forward("error");
        }
    }

	 /**
     * @jpf:action
     * @jpf:forward name="index" path="index.jsp"
     * @jpf:forward name="error" path="error.jsp"
     */
	@Jpf.Action(
		forwards = { 
			@Jpf.Forward(name = "index", path = "index.jsp"), 
			@Jpf.Forward(name = "error", path = "error.jsp")
		}
	)
	public Forward getLoadTestConfig(TestForm test) {
		
		return handleTMSRequest(test);
        
	}

	/**
     * @jpf:action
     * @jpf:forward name="index" path="index.jsp"
     * @jpf:forward name="error" path="error.jsp"
     */
	@Jpf.Action(
		forwards = { 
			@Jpf.Forward(name = "index", path = "index.jsp"), 
			@Jpf.Forward(name = "error", path = "error.jsp")
		}
	)
	public Forward uploadStatistics(TestForm test) {
		return handleTMSRequest(test);
	}

	/**
     * @jpf:action
     * @jpf:forward name="index" path="index.jsp"
     * @jpf:forward name="error" path="error.jsp"
     */
	@Jpf.Action(
		forwards = { 
			@Jpf.Forward(name = "index", path = "index.jsp"), 
			@Jpf.Forward(name = "error", path = "error.jsp")
		}
	)
	public Forward uploadSystemInfo(TestForm test) {
		return handleTMSRequest(test);
	}    

    
    private Forward handleTMSRequest(TestForm test) {
    	String requestXML = null;
        try {
            // pull XML off of request
        	
        	if(test.getMusicId() != null){
        		getMp3(test);
        	}else{
           requestXML = this.getRequest().getParameter("RequestXML");
            if(requestXML == null) this.getRequest().getParameter("requestXML");
            if(requestXML == null) requestXML = test.getRequestXML();
            if(requestXML == null) requestXML = "Missing XML request";
            String responseXML = null;
            if(requestXML.indexOf("tmssvc_request") >= 0) {
            	if (requestXML.indexOf("runLoad_request") > 0){
            		responseXML = CTBLoadTestRequestProcessor.processLoadTestRequest(requestXML, loadTestControl);
            	}else if(requestXML.indexOf("upload_statistics_request") > 0){
            		responseXML = CTBLoadTestRequestProcessor.processUploadStatisticsRequest(requestXML, loadTestControl);
            	}else if(requestXML.indexOf("upload_systemInfo_request") > 0){
            		responseXML = CTBLoadTestRequestProcessor.processUploadSystemInfoRequest(requestXML, loadTestControl);
            	}else{
                    String remoteAddress = this.getRequest().getRemoteAddr();
                    responseXML = CTBLoginRequestProcessor.processLoginRequest(requestXML, loginControl, remoteAddress);
            	}
            } else if(requestXML.indexOf("get_feedback_data") >= 0) {
                responseXML = CTBSaveRequestProcessor.processFeedbackRequest(requestXML, saveControl);
            } else if(requestXML.indexOf("adssvc_request") >= 0) {
                if (requestXML.indexOf("get_subtest") >= 0) 
                    responseXML = CTBAssessmentDeliveryProcessor.processGetSubtest(requestXML, adsControl);
                else if (requestXML.indexOf("download_item") >= 0) 
                    responseXML = CTBAssessmentDeliveryProcessor.processDownloadItem(requestXML, adsControl);
                else if (requestXML.indexOf("complete_tutorial") >= 0)
                    responseXML = CTBSaveRequestProcessor.processCompleteTutorial(requestXML, saveControl);
                else                    
                    responseXML = CTBSaveRequestProcessor.processSaveRequest(requestXML, saveControl);
            } else {
                OASLogger.getLogger("TestDelivery").debug("Invalid request: " + requestXML);
                getRequest().setAttribute("errorMessage", requestXML);
                return new Forward("error");
            }      
            // place response XML on request for use by JSP
            this.getRequest().setAttribute("responseXML", responseXML);
            this.getSession().invalidate();
           
        	}
        	 return new Forward("index");
        } catch (Exception e) {
            e.printStackTrace();
            OASLogger.debugStackTrace("TestDelivery", e.getStackTrace());
            OASLogger.getLogger("TestDelivery").debug("Offending request: " + requestXML);
            getRequest().setAttribute("errorMessage", requestXML);
            return new Forward("error");
        }
    }

    /**
     * FormData get and set methods may be overwritten by the Form Bean editor.
     */
    public static class TestForm extends FormData
    {
        private String requestXML;
        private Integer musicId;
        

        public void setRequestXML(String requestXML)
        {
            this.requestXML = requestXML;
        }

        public String getRequestXML()
        {
            return this.requestXML;
        }

		/**
		 * @return the musicId
		 */
		public Integer getMusicId() {
			return musicId;
		}

		/**
		 * @param musicId the musicId to set
		 */
		public void setMusicId(Integer musicId) {
			this.musicId = musicId;
		}
    }
}

