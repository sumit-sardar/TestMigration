package webservice;

import javax.jws.*;
import org.apache.beehive.controls.api.bean.Control;
import com.ctb.control.scoring.ScoringControl;

@WebService
public class ScoringService {

	@Control
	private ScoringControl scoringControl;

	@WebMethod
	public String invokeScoring(String testRosterId) {
		return scoringControl.invokeScoring(testRosterId);
	}

	@WebMethod
	public String scoreCompletedRostersForAdmin(Integer testAdminId) {
		return scoringControl.scoreCompletedRostersForAdmin(testAdminId);
	}

	@WebMethod
	public String scoreCompletedRostersForProduct(Integer productId)
    {
        return scoringControl.scoreCompletedRostersForProduct(productId);
    }

	@WebMethod
	public String scoreCompletedRostersForCustomer(Integer customerId)
    { 
		return scoringControl.scoreCompletedRostersForCustomer(customerId); 
    }

	/*
	 * Commenting out TASC TE FT Items Scoring for story OAS-3944
	 */
	/*@WebMethod
	public String rertyInvokeFTTEScoring(String testRosterId, String invokeKey) {
		return scoringControl.rertyInvokeFTTEScoring(testRosterId, invokeKey);
	}*/
	
}