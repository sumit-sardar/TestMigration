package testWebService;
import com.bea.control.ServiceControl;

import org.apache.beehive.controls.api.events.EventSet;
import org.apache.beehive.controls.api.bean.ControlExtension;

@ServiceControl.Location(urls = {"http://nj09mhe5381-mgt.edmz.mcgraw-hill.com:22611/RosterStatusWebService/RosterStatusWS"})
@ServiceControl.HttpSoapProtocol
@ServiceControl.SOAPBinding(style = ServiceControl.SOAPBinding.Style.DOCUMENT, use = ServiceControl.SOAPBinding.Use.LITERAL, parameterStyle = ServiceControl.SOAPBinding.ParameterStyle.WRAPPED)
@ServiceControl.WSDL(resourcePath = "testWebService/RosterStatusWSService.wsdl", service = "RosterStatusWSService")
@ControlExtension
public interface RosterStatusWSServiceControl extends ServiceControl
{
    static final long serialVersionUID = 1L;

    public dto.SessionStatus getRosterStatus(dto.SecureUser user_arg,dto.SessionStatus session_arg);

   /** This event set interface provides support for the onAsyncFailure event.
    */
   @EventSet(unicast=true)
   public interface Callback extends ServiceControl.Callback {};
}