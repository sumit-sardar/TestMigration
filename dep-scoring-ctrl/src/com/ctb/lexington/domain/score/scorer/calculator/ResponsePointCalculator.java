package com.ctb.lexington.domain.score.scorer.calculator;

import java.io.ByteArrayInputStream;
import java.net.URLDecoder;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ctb.lexington.data.ItemVO;
import com.ctb.lexington.domain.score.event.PointEvent;
import com.ctb.lexington.domain.score.event.ResponseReceivedEvent;
import com.ctb.lexington.domain.score.event.common.Channel;
import com.ctb.lexington.domain.score.scorer.Scorer;
import com.ctb.lexington.util.jsonobject.JsonContent;
import com.ctb.lexington.util.jsonobject.ScoreResponse;
import com.google.gson.Gson;

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
		
		if ("TS".equals(sicEvent.getProductType()) || "TR".equals(sicEvent.getProductType())){
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
					if(null != event.getConditionCode() || null != event.getCrResponse()){
						if(!"A".equals(event.getConditionCode())){
							final Integer attempted = sicEvent.getMaxPoints(event.getItemId());
							final Integer obtained = computePointsObtained(event);
				
							channel.send(new PointEvent(event.getTestRosterId(), event
									.getItemId(), event.getItemSetId(), attempted, obtained));
						}
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
		else if ("TC".equals(sicEvent.getProductType())){
			if (ItemVO.ITEM_TYPE_IN.equals(sicEvent.getType(event.getItemId()))) {
				HashMap<String, JsonContent> itemResponseMap = new HashMap<String, JsonContent>();
				if(null != getResponse(event, itemResponseMap) && itemResponseMap.size() > 0 ){
					final Integer attempted = computePointsAttempted(event, itemResponseMap);
					final Integer obtained = computePointsObtained(event, itemResponseMap);
					System.out.println("****** ResonsePointCalculator ****** :: ItemId ::"+event.getItemId()+" :: Obtained :: "+ obtained + " :: Attempted :: "+attempted);
					channel.send(new PointEvent(event.getTestRosterId(), event
							.getItemId(), event.getItemSetId(), attempted, obtained));
				}else{
					channel.send(new PointEvent(event.getTestRosterId(), event
							.getItemId(), event.getItemSetId(), new Integer(0),
							new Integer(0)));
				}
			}
			if(ItemVO.ITEM_TYPE_SR.equals(sicEvent.getType(event.getItemId()))){
				if(sicEvent.isAttempted(event)){
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

	private Integer computePointsAttempted(ResponseReceivedEvent event,
			HashMap<String, JsonContent> itemResponseMap) {
		
		Integer pointsAttempted = new Integer(0);
		if(itemResponseMap.containsKey(event.getItemId())) {
			pointsAttempted = itemResponseMap.get(event.getItemId()).getTotalMaxScore();
		}
		
		return pointsAttempted;
	}

	private Integer computePointsObtained(ResponseReceivedEvent event,
			HashMap<String, JsonContent> itemResponseMap) {
		
		Integer pointsObtained = event.getPointsObtained();
		if (null == pointsObtained) {
			if(itemResponseMap.containsKey(event.getItemId())) {
				pointsObtained = (null != itemResponseMap.get(event.getItemId()).getTotalScoreObtained()) 
									? itemResponseMap.get(event.getItemId()).getTotalScoreObtained()
										: sicEvent.getMinPoints(event.getItemId());
			} else{
				pointsObtained = sicEvent.getMinPoints(event.getItemId());
			}
		}
		return pointsObtained;
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
						&& ("TS".equals(sicEvent.getProductType()) || "TR"
								.equals(sicEvent.getProductType()))) {
						
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
		
	protected HashMap<String, JsonContent> getResponse(ResponseReceivedEvent event, HashMap<String, JsonContent> itemResponseMap) {
		String response = null;
		String JSONData = null;
		//itemResponseMap = new HashMap<String, JsonContent>();
		try {
			response = event.getTeItemResponse().getSubString(1, (int) event.getTeItemResponse().length());
			if(null != response) {
				response = URLDecoder.decode(response, "UTF-8");
				DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			    Document doc = builder.parse(new ByteArrayInputStream(response.getBytes()));
			    NodeList nodes = doc.getElementsByTagName(ItemVO.IN_ITEM_TAG_NAME);
			    for (int i = 0; i < nodes.getLength(); i++) {
			      Element element = (Element) nodes.item(i);
			      JSONData = getCharacterDataFromElement(element);
			    }
			    Gson gson = new Gson();
			    ScoreResponse json = null;
			    if(null != JSONData){
			    	json = gson.fromJson(JSONData, ScoreResponse.class);
				    if(null == json){
				    	json = new ScoreResponse();
				    	json.getJsonContent().setTotalScoreObtained(sicEvent.getMinPoints(event.getItemId()));
			    		json.getJsonContent().setTotalMaxScore(sicEvent.getMaxPoints(event.getItemId()));
			    		json.getJsonContent().setItemId(event.getItemId());
				    }else{
				    	if(null == json.getJsonContent() || (null == json.getJsonContent().getTotalMaxScore() || null == json.getJsonContent().getTotalScoreObtained())){
				    		json = new ScoreResponse();
					    	json.getJsonContent().setTotalScoreObtained(sicEvent.getMinPoints(event.getItemId()));
				    		json.getJsonContent().setTotalMaxScore(sicEvent.getMaxPoints(event.getItemId()));
				    		json.getJsonContent().setItemId(event.getItemId());
				    	}
				    }
			    }else{
			    	json = new ScoreResponse();
			    	json.getJsonContent().setTotalScoreObtained(sicEvent.getMinPoints(event.getItemId()));
		    		json.getJsonContent().setTotalMaxScore(sicEvent.getMaxPoints(event.getItemId()));
		    		json.getJsonContent().setItemId(event.getItemId());
			    }
			    event.setTePoints(new Integer(json.getJsonContent().getTotalScoreObtained()));
			    itemResponseMap.put(event.getItemId(), json.getJsonContent());
			}
			return itemResponseMap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private String getCharacterDataFromElement(Element e) {
		  Node child = e.getFirstChild();
		  if (child instanceof CharacterData) {
		    CharacterData cd = (CharacterData) child;
		    return cd.getData();
		  }
		  return "";
	}
}