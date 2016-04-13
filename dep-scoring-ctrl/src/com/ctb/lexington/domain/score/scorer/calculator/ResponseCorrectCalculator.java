package com.ctb.lexington.domain.score.scorer.calculator;


import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import com.ctb.lexington.data.ItemVO;
import com.ctb.lexington.domain.score.event.CorrectResponseEvent;
import com.ctb.lexington.domain.score.event.FTResponseReceivedEvent;
import com.ctb.lexington.domain.score.event.IncorrectResponseEvent;
import com.ctb.lexington.domain.score.event.NoResponseEvent;
import com.ctb.lexington.domain.score.event.ResponseReceivedEvent;
import com.ctb.lexington.domain.score.event.common.Channel;
import com.ctb.lexington.domain.score.scorer.Scorer;
import com.ctb.lexington.util.OASLogger;
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
		OASLogger.getLogger("ResponseCorrectCalculator").info("*****OASLogger:: ResponseCorrectCalculator: onEvent(ResponseReceivedEvent event) method called :: Timestamp:: " + new Date(System.currentTimeMillis()));
		validateItemSetId(event.getItemSetId());

		final String itemId = event.getItemId();
		if (ItemVO.ITEM_TYPE_SR.equals(sicEvent.getType(itemId))) {
			OASLogger.getLogger("ResponseCorrectCalculator").info("*****OASLogger:: ResponseCorrectCalculator: onEvent:: got SR item: [" + itemId + "] :: Timestamp:: " + new Date(System.currentTimeMillis()));
			final String response = event.getResponse();
			final boolean isConditionCode = sicEvent.isConditionCode(itemId,
					response);
			OASLogger.getLogger("ResponseCorrectCalculator").info("*****OASLogger:: ResponseCorrectCalculator: onEvent:: response = ["+ response + "] : isConditionCode = [" + isConditionCode +"] :: Timestamp:: " + new Date(System.currentTimeMillis()));
			if (isConditionCode || response == null) {
				if (sicEvent.isMarked(event)) {
					channel.send(new IncorrectResponseEvent(event));
				} else {
					channel.send(new NoResponseEvent(event));
				}
			} else {
				if (sicEvent.isCorrectResponse(itemId, response)) {
					OASLogger.getLogger("ResponseCorrectCalculator").info("*****OASLogger:: ResponseCorrectCalculator: onEvent:: isCorrectResponse = [TRUE] :: Timestamp:: " + new Date(System.currentTimeMillis()));
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
					OASLogger.getLogger("ResponseCorrectCalculator").info("*****OASLogger:: ResponseCorrectCalculator: onEvent:: isCorrectResponse = [FALSE] :: Timestamp:: " + new Date(System.currentTimeMillis()));
					channel.send(new IncorrectResponseEvent(event));
				}
			}
		} else {
			if (ItemVO.ITEM_TYPE_CR.equals(sicEvent.getType(itemId))) {
				if (null != sicEvent.getProductType()
						&& ("TS".equals(sicEvent.getProductType()) || "TR".equals(sicEvent.getProductType()))) {

					if(null != sicEvent.getAnswerArea(itemId)
						&& "GRID".equals(sicEvent.getAnswerArea(itemId))) {
						OASLogger.getLogger("ResponseCorrectCalculator").info("*****OASLogger:: ResponseCorrectCalculator: onEvent:: got GR item: [" + itemId + "] :: Timestamp: " + new Date(System.currentTimeMillis()));
							
							final String actualGrResponse = event.getGrResponse();
							final String grItemRules = event.getGrItemRules();
							final String grItemCorrectAnswer = event.getGrItemCorrectAnswer();
						
							OASLogger.getLogger("ResponseCorrectCalculator").info("*****OASLogger:: ResponseCorrectCalculator: onEvent:: got below scoring parameters for GR item ID = ["+ itemId +"] \n actualGrResponse = [" + actualGrResponse+"] \n grItemRules = [" + grItemRules + "] \n grItemCorrectAnswer = [" + grItemCorrectAnswer + "]");	
						//7th Nov 2013 Not a valid production scenario uncomment this part once actual rules are defined
						//if(grItemRules != null && grItemCorrectAnswer != null) {
						if(actualGrResponse != null) {
							
							String itemsRawScore = new ValidateGRResponse().validateGRResponse(itemId, actualGrResponse, grItemRules, grItemCorrectAnswer);
							OASLogger.getLogger("ResponseCorrectCalculator").info("*****OASLogger:: ResponseCorrectCalculator: onEvent:: calculated itemsRawScore = [" + itemsRawScore + "] for GR item ID = [" + itemId + "] ::  Timestamp: " + new Date(System.currentTimeMillis()));
							if (itemsRawScore.equals("1")) {
								sicEvent.setGRItemMap(itemId, true);
								channel.send(new CorrectResponseEvent(event));
							} else if (itemsRawScore.equals("0")) {
								sicEvent.setGRItemMap(itemId, false);
								channel.send(new IncorrectResponseEvent(event));
							}
						} else {
							channel.send(new NoResponseEvent(event));
						}
					} else if (null == sicEvent.getAnswerArea(itemId)
							&& !"GRID".equals(sicEvent.getAnswerArea(itemId))){
						//  TODO: how do we handle CR item responses?
						OASLogger.getLogger("ResponseCorrectCalculator").info("*****OASLogger:: ResponseCorrectCalculator: onEvent:: got CR item: [" + itemId + "] :: Timestamp: " + new Date(System.currentTimeMillis()));
						final Integer pointsObtained = event.getCrResponse();
						OASLogger.getLogger("ResponseCorrectCalculator").info("*****OASLogger:: ResponseCorrectCalculator: onEvent:: pointsObtained = [" + pointsObtained + "] for CR item ID = [" + itemId + "] ::  Timestamp: " + new Date(System.currentTimeMillis()));
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
				else {
					final Integer pointsObtained = event.getPointsObtained();
					OASLogger.getLogger("ResponseCorrectCalculator").info("*****OASLogger:: ResponseCorrectCalculator: onEvent:: pointsObtained = [" + pointsObtained + "] for CR item ID = [" + itemId + "] ::  Timestamp: " + new Date(System.currentTimeMillis()));
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
		}
	}
	
	/**
	 * Calculation is for FT item only 
	 * Sends a <code>CorrectResponseEvent</code> event if the answer is correct and a
	 * <code>IncorrectResponseEvent</code> if the answer is incorrect. NOTE: A condition code
	 * constitutes neither a correct nor incorrect answer! An item is considered incorrect only if
	 * an incorrect choice is marked and no other choice is marked.
	 * 
	 * @param event
	 */
	public void onEvent(FTResponseReceivedEvent event) {
	}
}