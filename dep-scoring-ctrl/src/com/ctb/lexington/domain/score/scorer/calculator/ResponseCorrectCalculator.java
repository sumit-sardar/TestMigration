package com.ctb.lexington.domain.score.scorer.calculator;


import java.util.LinkedHashMap;
import java.util.Map;

import com.ctb.lexington.data.ItemVO;
import com.ctb.lexington.domain.score.event.CorrectResponseEvent;
import com.ctb.lexington.domain.score.event.IncorrectResponseEvent;
import com.ctb.lexington.domain.score.event.NoResponseEvent;
import com.ctb.lexington.domain.score.event.ResponseReceivedEvent;
import com.ctb.lexington.domain.score.event.common.Channel;
import com.ctb.lexington.domain.score.scorer.Scorer;
import com.ctb.lexington.util.ValidateGRResponse;


/**
 * This class counts the number of correct answers for a subtest for specific roster instance.
 */
public class ResponseCorrectCalculator extends AbstractResponseCalculator {
	public ResponseCorrectCalculator(Channel channel, Scorer scorer) {
		super(channel, scorer);
	}

	/**
	 * Sends a <code>CorrectResponseEvent</code> event if the answer is correct and a
	 * <code>IncorrectResponseEvent</code> if the answer is incorrect. NOTE: A condition code
	 * constitutes neither a correct nor incorrect answer! An item is considered incorrect only if
	 * an incorrect choice is marked and no other choice is marked.
	 * 
	 * @param event
	 */
	public void onEvent(ResponseReceivedEvent event) {
		validateItemSetId(event.getItemSetId());

		final String itemId = event.getItemId();
		if (ItemVO.ITEM_TYPE_SR.equals(sicEvent.getType(itemId))) {
			final String response = event.getResponse();
			final boolean isConditionCode = sicEvent.isConditionCode(itemId,
					response);
			if (isConditionCode || response == null) {
				if (sicEvent.isMarked(event)) {
					channel.send(new IncorrectResponseEvent(event));
				} else {
					channel.send(new NoResponseEvent(event));
				}
			} else {
				if (sicEvent.isCorrectResponse(itemId, response)) {
					if (scorer.getResultHolder().getAdminData().getProductId() == 3700) {
						LinkedHashMap<String, LinkedHashMap<String, String>> caItemMap = scorer
								.getResultHolder().getCaResponseWsTv()
								.getContentAreaItems();
						Map<String, String> itemsContentArea = scorer
								.getResultHolder().getCaResponseWsTv()
								.getItemsContentArea();
						String contentArea = itemsContentArea.get(itemId);
						contentArea = (contentArea == null) ? sicEvent
								.getContentArea() : contentArea;

						LinkedHashMap<String, String> itemIdResp = new LinkedHashMap<String, String>();
						if (caItemMap.containsKey(contentArea)) {
							itemIdResp = caItemMap.get(contentArea);
							if (itemIdResp.containsKey(itemId)) {
								itemIdResp.put(itemId, "1");
							}
						}
						caItemMap.put(contentArea, itemIdResp);
					}
					channel.send(new CorrectResponseEvent(event));
				} else {
					channel.send(new IncorrectResponseEvent(event));
				}
			}
		} /*else {
			// TODO: how do we handle not SR namely GR and CR item responses?
			if (ItemVO.ITEM_TYPE_CR.equals(sicEvent.getType(itemId))) {
				// TODO: how do we handle GR item responses?
				if (null != sicEvent.getProductType()
						&& "TS".equals(sicEvent.getProductType())
						&& null != sicEvent.getAnswerArea(itemId)
						&& "GRID".equals(sicEvent.getAnswerArea(itemId))) {
					
					final String actualGrResponse = event.getGrResponse();
					final String grItemRules = event.getGrItemRules();
					final String grItemCorrectAnswer = event.getGrItemCorrectAnswer();
					
					if(actualGrResponse != null && grItemRules != null && grItemCorrectAnswer != null) {
						String itemsRawScore = ValidateGRResponse.validateGRResponse(itemId, actualGrResponse, grItemRules, grItemCorrectAnswer);
						if(itemsRawScore.equals("1")) {
							sicEvent.setGRItemMap(itemId, true);
							channel.send(new CorrectResponseEvent(event));
						} else if(itemsRawScore.equals("0")) {
							sicEvent.setGRItemMap(itemId, false);
							channel.send(new IncorrectResponseEvent(event));
						}
					} else {
						channel.send(new NoResponseEvent(event));
					}
				}
				//  TODO: how do we handle CR item responses?
				else {
					final Integer pointsObtained = event.getPointsObtained();
					if (pointsObtained != null) {
						if (pointsObtained > 0) {
							channel.send(new CorrectResponseEvent(event));
						} else {
							channel.send(new IncorrectResponseEvent(event));
						}
					} else {
						channel.send(new NoResponseEvent(event));
					}
				}
			}
		}*/
	}
}