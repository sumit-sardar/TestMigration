package downloadtest;

import java.util.ArrayList;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import com.ctb.bean.testAdmin.CustomerTestResource;
import com.ctb.bean.testAdmin.CustomerTestResourceData;

import com.ctb.testSessionInfo.dto.FileInfo;
import com.ctb.exception.CTBBusinessException;

/**
 * @jpf:controller
 *  */
@Jpf.Controller()
public class DownloadTestController extends PageFlowController
{
    static final long serialVersionUID = 1L;

    /**
     * @common:control
     */
    @Control()
    private com.ctb.control.testAdmin.TestSessionStatus testSessionStatus;

    private String userName = null;
    
    /**
     * This method represents the point of entry into the pageflow
     * @jpf:action
     * @jpf:forward name="start" path="download_zippedFile.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "start",
                     path = "download_zippedFile.do")
    })
    protected Forward begin()
    {
        java.security.Principal principal = getRequest().getUserPrincipal();
        if (principal != null) 
            this.userName = principal.toString();
        getSession().setAttribute("userName", this.userName);
    	
        return new Forward("start");
    }
    
    /**
     * @jpf:action
     * @jpf:forward name="success" path="download_zippedFile.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "download_zippedFile.jsp")
    })
    protected Forward download_zippedFile()
    {
     	ArrayList<FileInfo> fileInfoList = new ArrayList<FileInfo>(); 
    	try {
			CustomerTestResourceData resourceData= this.testSessionStatus.getCustomerTestResources(this.userName, null, null, null);
			CustomerTestResource[] result =resourceData.getCustomerTestResource();
			for(CustomerTestResource resource:result){
				FileInfo fileInfo = new FileInfo(resource.getProductName(), resource.getResourceURI(), resource.getContentSize());
				fileInfoList.add(fileInfo);
				
			}
		} catch (CTBBusinessException e) {
			e.printStackTrace();
		} 
    	
        this.getRequest().setAttribute("fileInfoList", fileInfoList);
        this.getRequest().setAttribute("showMessage", new Boolean(fileInfoList.size() > 0));
    	 
        return new Forward("success");
    }
    
          
}