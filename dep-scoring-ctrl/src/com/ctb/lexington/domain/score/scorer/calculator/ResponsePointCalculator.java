package com.ctb.lexington.domain.score.scorer.calculator;

import com.ctb.lexington.data.ItemVO;
import com.ctb.lexington.domain.score.event.PointEvent;
import com.ctb.lexington.domain.score.event.ResponseReceivedEvent;
import com.ctb.lexington.domain.score.event.common.Channel;
import com.ctb.lexington.domain.score.scorer.Scorer;

public class ResponsePointCalculator extends AbstractResponseCalculator {
	/**
	 * @param channel
	 * @param scorer
	 */
	public ResponsePointCalculator(Channel channel, Scorer scorer) {
		super(channel, scorer);
	}

	public void onEvent(ResponseReceivedEvent event) {
		validateItemSetId(event.getItemSetId());
		
		if ("TS".equals(sicEvent.getProductType())){
			if (ItemVO.ITEM_TYPE_CR.equals(sicEvent.getType(event.getItemId()))) {
				if (null != sicEvent.getAnswerArea(event.getItemId())
						&& "GRID".equals(sicEvent.getAnswerArea(event.getItemId()))) {
					if(null != event.getGrResponse()  && !"".equals(event.getGrResponse())){
						final Integer attempted = sicEvent.getMaxPoints(event.getItemId());
						final Integer obtained = computePointsObtained(event);
			
						channel.send(new PointEvent(event.getTestRosterId(), event
								.getItemId(), event.getItemSetId(), attempted, obtained));
					}
					else {
						channel.send(new PointEvent(event.getTestRosterId(), event
								.getItemId(), event.getItemSetId(), new Integer(0),
								new Integer(0)));
					}
				} else if (null == sicEvent.getAnswerArea(event.getItemId())
						&& !"GRID".equals(sicEvent.getAnswerArea(event.getItemId()))) {
					if(null != event.getCrResponse()  && !"".equals(event.getCrResponse())){
						final Integer attempted = sicEvent.getMaxPoints(event.getItemId());
						final Integer obtained = computePointsObtained(event);
			
						channel.send(new PointEvent(event.getTestRosterId(), event
								.getItemId(), event.getItemSetId(), attempted, obtained));
					}
					else {
						channel.send(new PointEvent(event.getTestRosterId(), event
								.getItemId(), event.getItemSetId(), new Integer(0),
								new Integer(0)));
					}
				}
			}
			if(ItemVO.ITEM_TYPE_SR.equals(sicEvent.getType(event.getItemId()))){
				if(null != event.getResponse() && !"".equals(event.getResponse()) && !"-".equals(event.getResponse())){
					final Integer attempted = sicEvent.getMaxPoints(event.getItemId());
					final Integer obtained = computePointsObtained(event);
		
					channel.send(new PointEvent(event.getTestRosterId(), event
							.getItemId(), event.getItemSetId(), attempted, obtained));
				}
				else {
					channel.send(new PointEvent(event.getTestRosterId(), event
							.getItemId(), event.getItemSetId(), new Integer(0),
							new Integer(0)));
				}
			}
		}
		else {
			if (sicEvent.isAttempted(event)) {
				final Integer attempted = sicEvent.getMaxPoints(event.getItemId());
				final Integer obtained = computePointsObtained(event);
	
				channel.send(new PointEvent(event.getTestRosterId(), event
						.getItemId(), event.getItemSetId(), attempted, obtained));
			} else { //if (!(ItemLocal.ITEM_TYPE_CR.equals(sicEvent.getType(event.getItemId())) && sicEvent.isOnlineCr(event.getItemId()))) {
				channel.send(new PointEvent(event.getTestRosterId(), event
						.getItemId(), event.getItemSetId(), new Integer(0),
						new Integer(0)));
			}
		}
	}

	private Integer computePointsObtained(ResponseReceivedEvent event) {
		Integer pointsObtained = event.getPointsObtained();
		if (pointsObtained == null) {
			// For SR items, event.pointsObtained can be null, if so - get it from the item's
			// max/min points. It need not be null, if the proctor sets a value.
			String itemType = sicEvent.getType(event.getItemId());
			if (ItemVO.ITEM_TYPE_SR.equals(itemType)) {
				if (sicEvent.isCorrectResponse(event.getItemId(), event
						.getResponse())) {
					pointsObtained = sicEvent.getMaxPoints(event.getItemId());
				} else {
					pointsObtained = sicEvent.getMinPoints(event.getItemId());
				}
			} else if (ItemVO.ITEM_TYPE_CR.equals(itemType)) {
				// Point calculation for GR items
				if (null != sicEvent.getProductType()
						&& "TS".equals(sicEvent.getProductType())) {
						
					if(null != sicEvent.getAnswerArea(event.getItemId())
						&& "GRID".equals(sicEvent.getAnswerArea(event.getItemId()))) {
					
						if (null != sicEvent.getGRItemMap(event.getItemId())
								&& sicEvent.getGRItemMap(event.getItemId()).booleanValue()) {
							
							pointsObtained = sicEvent.getMaxPoints(event.getItemId());
						} else if (null != sicEvent.getGRItemMap(event.getItemId())
								&& !sicEvent.getGRItemMap(event.getItemId()).booleanValue()) {
							
							pointsObtained = sicEvent.getMinPoints(event.getItemId());
						}
					}
					else if(null == sicEvent.getAnswerArea(event.getItemId())
							&& !"GRID".equals(sicEvent.getAnswerArea(event.getItemId()))) {
						pointsObtained = event.getCrResponse();
					}
				}
				// Point calculation for CR items
				else {
					
					if (sicEvent.isAttempted(event))
						pointsObtained = sicEvent.getMinPoints(event.getItemId());
				}
			} else {
				throw new IllegalArgumentException(
						"Item is not of recognized type: Item id - "
								+ event.getItemId() + ", Item type - "
								+ itemType);
			}
		}

		return pointsObtained;
	}
}