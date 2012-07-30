package com.ctb.service;
import com.bea.control.ServiceControl;

import org.apache.beehive.controls.api.events.EventSet;
import org.apache.beehive.controls.api.bean.ControlExtension;

@ServiceControl.Location(urls = {"http://192.168.14.136:8080/host/services/ScoringService"})
@ServiceControl.HttpSoapProtocol
@ServiceControl.HttpSoap12Protocol
@ServiceControl.SOAPBinding(style = ServiceControl.SOAPBinding.Style.DOCUMENT, use = ServiceControl.SOAPBinding.Use.LITERAL, parameterStyle = ServiceControl.SOAPBinding.ParameterStyle.WRAPPED)
@ServiceControl.WSDL(resourcePath = "com/ctb/service/ScoringService.wsdl", service = "ScoringService")
@ControlExtension
public interface ScoringServiceControl extends ServiceControl
{
    static final long serialVersionUID = 1L;

    public com.mhdigitallearning.terranova.scoring.vo.xsd.ScoringStatus processStudentScore(com.mhdigitallearning.terranova.scoring.vo.xsd.AuthenticatedUser user_arg,com.mhdigitallearning.terranova.scoring.vo.xsd.StudentScore score_arg);

   /** This event set interface provides support for the onAsyncFailure event.
    */
   @EventSet(unicast=true)
   public interface Callback extends ServiceControl.Callback {};
}