package webservice;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;
import org.apache.beehive.controls.api.bean.Control;
import weblogic.jws.Transactional;
import weblogic.jws.WLHttpTransport;
import weblogic.jws.soap.SOAPBinding;
import weblogic.jws.wlw.UseWLW81BindingTypes;
import weblogic.jws.wlw.WLWRollbackOnCheckedException;

/**
 * @editor-info:link autogen-style="stateless" source="ScoringControlImpl.jcs" autogen="true"
 */
@Transactional(true)
@UseWLW81BindingTypes()
@WLHttpTransport(serviceUri = "ScoringService.jws")
@WLWRollbackOnCheckedException()
@WebService(serviceName = "ScoringService",
            targetNamespace = "http://www.openuri.org/")
@javax.jws.soap.SOAPBinding(style = javax.jws.soap.SOAPBinding.Style.DOCUMENT, 
                            use = javax.jws.soap.SOAPBinding.Use.LITERAL, 
                            parameterStyle = javax.jws.soap.SOAPBinding.ParameterStyle.WRAPPED)
public class ScoringService 
{
    static final long serialVersionUID = 1L;

    /** @common:control */
    @Control()
    public com.ctb.control.scoring.ScoringControl scoringControl;

    /** @common:operation
     */
    @SOAPBinding(style = javax.jws.soap.SOAPBinding.Style.DOCUMENT, 
                 use = javax.jws.soap.SOAPBinding.Use.LITERAL, 
                 parameterStyle = javax.jws.soap.SOAPBinding.ParameterStyle.WRAPPED)
    @WebMethod()
    @WebResult(name = "scoreCompletedRostersForAdminResult")
    public java.lang.String scoreCompletedRostersForAdmin(java.lang.Integer testAdminId)
    {
        return scoringControl.scoreCompletedRostersForAdmin(testAdminId);
    }

    /** @common:operation
     */
    @SOAPBinding(style = javax.jws.soap.SOAPBinding.Style.DOCUMENT, 
                 use = javax.jws.soap.SOAPBinding.Use.LITERAL, 
                 parameterStyle = javax.jws.soap.SOAPBinding.ParameterStyle.WRAPPED)
    @WebMethod()
    @WebResult(name = "scoreCompletedRostersForProductResult")
    public java.lang.String scoreCompletedRostersForProduct(java.lang.Integer productId)
    {
        return scoringControl.scoreCompletedRostersForProduct(productId);
    }

    /** @common:operation
     */
    @SOAPBinding(style = javax.jws.soap.SOAPBinding.Style.DOCUMENT, 
                 use = javax.jws.soap.SOAPBinding.Use.LITERAL, 
                 parameterStyle = javax.jws.soap.SOAPBinding.ParameterStyle.WRAPPED)
    @WebMethod()
    @WebResult(name = "invokeScoringResult")
    public java.lang.String invokeScoring(java.lang.String testRosterId)
    {
        return scoringControl.invokeScoring(testRosterId);
    }

    /** @common:operation
     */
    @SOAPBinding(style = javax.jws.soap.SOAPBinding.Style.DOCUMENT, 
		use = javax.jws.soap.SOAPBinding.Use.LITERAL, 
		parameterStyle = javax.jws.soap.SOAPBinding.ParameterStyle.WRAPPED
	)
	@WebMethod()
	@WebResult(name = "scoreCompletedRostersForCustomerResult")
    public java.lang.String scoreCompletedRostersForCustomer(java.lang.Integer customerId)
    { return scoringControl.scoreCompletedRostersForCustomer(customerId); }

}
