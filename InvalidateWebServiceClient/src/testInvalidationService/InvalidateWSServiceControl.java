package testInvalidationService;
import com.bea.control.ServiceControl;

import org.apache.beehive.controls.api.events.EventSet;
import org.apache.beehive.controls.api.bean.ControlExtension;

@ServiceControl.Location(urls = {"http://localhost:7001/InvalidateWebService/InvalidateWS"})
@ServiceControl.HttpSoapProtocol
@ServiceControl.SOAPBinding(style = ServiceControl.SOAPBinding.Style.DOCUMENT, use = ServiceControl.SOAPBinding.Use.LITERAL, parameterStyle = ServiceControl.SOAPBinding.ParameterStyle.WRAPPED)
@ServiceControl.WSDL(resourcePath = "testInvalidationService/InvalidateWSService.wsdl", service = "InvalidateWSService")
@ControlExtension
public interface InvalidateWSServiceControl extends ServiceControl
{
    static final long serialVersionUID = 1L;

    public java.lang.String validateClass(com.ctb.dto.StudentValidationDetails details_arg,com.ctb.dto.SecureUser secureUser_arg);

   /** This event set interface provides support for the onAsyncFailure event.
    */
   @EventSet(unicast=true)
   public interface Callback extends ServiceControl.Callback {};
}