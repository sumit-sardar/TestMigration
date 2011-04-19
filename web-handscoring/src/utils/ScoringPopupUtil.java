package utils;

import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ctb.bean.testAdmin.ScorableCRAnswerContent;
import com.ctb.control.crscoring.TestScoring;
import com.ctb.exception.CTBBusinessException;

/**
 * This class contains common method for displaying CR response in pop up. 
 * @author TCS
 *
 */
public class ScoringPopupUtil {

	/**
	 * This method reads Audio response from Database and display. 
	 * @param req
	 * @param resp
	 * @param userName
	 * @param testScoring
	 * @throws Exception
	 */
	public static String CONTENT_TYPE_JSON = "application/json";
	public static void processCRResponseDisplay(HttpServletRequest req,
			HttpServletResponse resp, String userName, TestScoring testScoring, String contentType) throws Exception {

		String jsonResponse = "";
		String itemType = req.getParameter("itemType");
		String itemId = req.getParameter("itemId");
		OutputStream stream = null;
		Integer itemSetId = Integer.valueOf(req.getParameter("itemSetId"));
		Integer testRosterId = Integer.valueOf(req.getParameter("rosterId"));
		ScorableCRAnswerContent scr = getIndividualCRResponse(testScoring,
				userName, testRosterId, itemSetId, itemId, itemType);

		try {
			jsonResponse = JsonUtils.getJson(scr, "answer", scr.getClass());
			resp.setContentType(contentType);
			resp.flushBuffer();
			stream = resp.getOutputStream();
			stream.write(jsonResponse.getBytes());
		} finally{
			if (stream!=null){
				stream.close();
			}
		}
	}

	private static ScorableCRAnswerContent getIndividualCRResponse(
			TestScoring testScoring, String userName, Integer testRosterId,
			Integer deliverableItemSetId, String itemId, String itemType) {
		ScorableCRAnswerContent answerArea = new ScorableCRAnswerContent();
		try {

			answerArea = testScoring.getCRItemResponseForScoring(userName,
					testRosterId, deliverableItemSetId, itemId, itemType);
		} catch (CTBBusinessException be) {
			be.printStackTrace();
		}

		return answerArea;
	}

}
