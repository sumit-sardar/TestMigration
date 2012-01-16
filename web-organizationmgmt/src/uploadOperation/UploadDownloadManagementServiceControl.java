package uploadOperation;
import com.bea.control.ServiceControl;

import org.apache.beehive.controls.api.events.EventSet;
import org.apache.beehive.controls.api.bean.ControlExtension;

@ServiceControl.Location(urls = {"http://localhost:7001/platform-webservices/UploadDownloadManagement"})
@ServiceControl.HttpSoapProtocol
@ServiceControl.SOAPBinding(style = ServiceControl.SOAPBinding.Style.DOCUMENT, use = ServiceControl.SOAPBinding.Use.LITERAL, parameterStyle = ServiceControl.SOAPBinding.ParameterStyle.WRAPPED)
@ServiceControl.WSDL(resourcePath = "uploadOperation/UploadDownloadManagementService.wsdl", service = "UploadDownloadManagementService")
@ControlExtension
public interface UploadDownloadManagementServiceControl extends ServiceControl
{
    static final long serialVersionUID = 1L;

    public void uploadFile(java.lang.String userName_arg,java.lang.String serverFilePath_arg,java.lang.Integer uploadDataFileId_arg) throws com.ctb.webservices.CTBBusinessException;

   /** This event set interface provides support for the onAsyncFailure event.
    */
   @EventSet(unicast=true)
   public interface Callback extends ServiceControl.Callback {};
}