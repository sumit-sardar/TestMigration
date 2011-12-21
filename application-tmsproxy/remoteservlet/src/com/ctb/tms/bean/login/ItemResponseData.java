package com.ctb.tms.bean.login; 

import java.io.Serializable;
import java.math.BigInteger;

import noNamespace.AdssvcRequestDocument.AdssvcRequest.SaveTestingSessionData.Tsd;
import noNamespace.AdssvcRequestDocument.AdssvcRequest.SaveTestingSessionData.Tsd.Ast;
import noNamespace.AdssvcRequestDocument.AdssvcRequest.SaveTestingSessionData.Tsd.Ist;
import noNamespace.AdssvcRequestDocument.AdssvcRequest.SaveTestingSessionData.Tsd.Ist.Ov;
import noNamespace.AdssvcRequestDocument.AdssvcRequest.SaveTestingSessionData.Tsd.Ist.Rv;
import noNamespace.BaseType;


public class ItemResponseData implements Serializable, CachePreLoadObject{
	
	private static final long serialVersionUID = 1L;
	
	private boolean replicate = true;
	
	public ItemResponseData() {
	
	}
	
	private int testRosterId;
	private int itemSetId;
    private String itemId;
    private int itemSortOrder;
    private String responseSeqNum;
    private String studentMarked;
    private String itemType;
    private String response;
    private String constructedResponse;
    private int responseElapsedTime;
    private int eid;
    private int score;
    private String answerArea;
    private float duration;
    private boolean audioItem;
    private String responseType;
    
    public static Tsd IrdToTsd(ItemResponseData ird) {
    	Tsd tsd = Tsd.Factory.newInstance();        
		tsd.setScid(String.valueOf(ird.getItemSetId()));
		tsd.setLsid(String.valueOf(ird.getTestRosterId()));
		Ast ast = tsd.addNewAst();
		int maxRSN = 0;
		int totalDur = 0;
		tsd.addNewIst();
		Ist ist = tsd.getIstArray(0);
		ist.setIid(ird.getItemId());
		ist.setEid(""+ird.getEid());
		ist.setCst(Ist.Cst.UNKNOWN);
		ist.setMrk("T".equals(ird.getStudentMarked()));
		ist.setDur(ird.getResponseElapsedTime());
		tsd.setMseq(new BigInteger(ird.getResponseSeqNum()));
		totalDur = totalDur + ird.getResponseElapsedTime();
		Rv rv = ist.addNewRv();
		if ("SR".equals(ird.getItemType())) {
			rv.setT(BaseType.IDENTIFIER);
			rv.addNewV().setStringValue(ird.getResponse());
		}
		else { 
			rv.setT(BaseType.STRING);
			String crResponse = "";
			String crResponseClob = ird.getConstructedResponse();
			if (crResponseClob != null && crResponseClob.length() > 0) {
				crResponse = crResponseClob.substring(0, crResponseClob.length());
			}
			rv.addNewV().setStringValue(crResponse);
		}
		rv.setN("RESPONSE");

		Ov ov = ist.addNewOv();
		ov.setN("SCORE");
		ov.setT(BaseType.INTEGER);

		if ("SR".equals(ird.getItemType()))
			ov.addNewV().setStringValue(""+ird.getScore());
		else
			ov.addNewV().setStringValue("");

		if(Integer.valueOf(ird.getResponseSeqNum()) > maxRSN) {
			ast.setCurEid(""+ird.getEid());
			maxRSN = Integer.valueOf(ird.getResponseSeqNum());
		}
		return tsd;
    }
    
    public static ItemResponseData TsdToIrd(Tsd tsd) {
	    ItemResponseData ird = new ItemResponseData();
	    ird.setItemSetId(Integer.parseInt(tsd.getScid()));
	    ird.setResponseSeqNum(String.valueOf(tsd.getMseq()));
    	Ist[] ista = tsd.getIstArray();
		for(int j=0;j<ista.length;j++) {
	        Ist ist = ista[j];
	        ird.setItemId(ist.getIid());
	        // if(ist != null && ist.getRvArray(0) != null && ist.getRvArray(0).getVArray(0) != null) {
	        if(ist != null && ist.getRvArray() != null && ist.getRvArray().length >0 ) {
	            if( ist.getRvArray(0).getVArray() != null && ist.getRvArray(0).getVArray().length >0){
	                if(ist.getRvArray(0).getVArray(0) != null){
	                    BaseType.Enum responseType = ist.getRvArray(0).getT();
	                    ird.setResponseType(responseType.toString());
	                    String xmlResponse = ist.getRvArray(0).getVArray(0);
	                    String response = "";
	                    String studentMarked = ist.getMrk() ? "T" : "F";
	                    ird.setStudentMarked(studentMarked);
	                    String audioItem = ist.getAudioItem() ? "T" : "F";
	                    ird.setAudioItem(ist.getAudioItem());
	                    if(xmlResponse != null && xmlResponse.length() > 0) {
	                        // strip xml
	                        int start = xmlResponse.indexOf(">");
	                        if(start >= 0) {
	                            response = xmlResponse.substring(start + 1);
	                            int end = response.lastIndexOf("</");
	                            if(end != -1)
	                                response = response.substring(0, end);
	                        } else {
	                            response = xmlResponse;
	                        }
	                        // strip CDATA
	                        start = response.indexOf("[CDATA[");
	                        if(start >= 0) {
	                            response = response.substring(start + 7);
	                            int end = response.lastIndexOf("]]");
	                            if(end != -1) {
	                                response = response.substring(0, end);
	                            }
	                        }
	                    }
	                    ird.setResponse(response);
	                }
	            }
	        }
		}
		return ird;
    }

	                    /*if(responseType.equals(BaseType.IDENTIFIER)) {
	                        ird.setT testRosterId), Integer.parseInt(tsd.getScid()), ist.getIid(), response, ist.getDur(), tsd.getMseq(), studentMarked);
	                    } else if(responseType.equals(BaseType.STRING)) {
	                    	storeResponse(conn, Integer.parseInt(testRosterId), Integer.parseInt(tsd.getScid()), ist.getIid(), null, ist.getDur(), tsd.getMseq(), studentMarked);
	                        storeCRResponse(conn, Integer.parseInt(testRosterId), Integer.parseInt(tsd.getScid()), ist.getIid(), response, ist.getDur(), tsd.getMseq(), studentMarked, audioItem);
	                    }
	                    logger.debug("OASOracleSink: Stored response records in DB for roster " + testRosterId + ", mseq " + tsd.getMseq());
	                 }
	            }else{ 
	                String response = "";                   
	                String studentMarked = ist.getMrk() ? "T" : "F";                    
	                storeResponse(conn, Integer.parseInt(testRosterId), Integer.parseInt(tsd.getScid()), ist.getIid(), response, ist.getDur(), tsd.getMseq(), studentMarked);                                          
	            }       
	        }*/
    
    
	public boolean isAudioItem() {
		return audioItem;
	}
	
	public String getResponseType() {
		return responseType;
	}

	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}

	public void setAudioItem(boolean audioItem) {
		this.audioItem = audioItem;
	}
	public int getTestRosterId() {
		return testRosterId;
	}
	public void setTestRosterId(int testRosterId) {
		this.testRosterId = testRosterId;
	}
	public int getItemSetId() {
		return itemSetId;
	}
	public void setItemSetId(int itemSetId) {
		this.itemSetId = itemSetId;
	}
	public float getDuration() {
		return duration;
	}
	public void setDuration(float duration) {
		this.duration = duration;
	}
	/**
	 * @param itemId The itemId to set.
	 */
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	/**
	 * @return Returns the itemId.
	 */
	public String getItemId() {
		return itemId;
	}
	/**
	 * @param responseSeqNum The responseSeqNum to set.
	 */
	public void setResponseSeqNum(String responseSeqNum) {
		this.responseSeqNum = responseSeqNum;
	}
	/**
	 * @return Returns the responseSeqNum.
	 */
	public String getResponseSeqNum() {
		return responseSeqNum;
	}
	/**
	 * @param student_marked The student_marked to set.
	 */
	public void setStudentMarked(String studentMarked) {
		this.studentMarked = studentMarked;
	}
	/**
	 * @return Returns the student_marked.
	 */
	public String getStudentMarked() {
		return studentMarked;
	}
	/**
	 * @param itemType The itemType to set.
	 */
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	/**
	 * @return Returns the itemType.
	 */
	public String getItemType() {
		return itemType;
	}
	/**
	 * @param response The response to set.
	 */
	public void setResponse(String response) {
		this.response = response;
	}
	/**
	 * @return Returns the response.
	 */
	public String getResponse() {
		return response;
	}
	/**
	 * @param responseElapsedTime The responseElapsedTime to set.
	 */
	public void setResponseElapsedTime(int responseElapsedTime) {
		this.responseElapsedTime = responseElapsedTime;
	}
	/**
	 * @return Returns the responseElapsedTime.
	 */
	public int getResponseElapsedTime() {
		return responseElapsedTime;
	}
	/**
	 * @param itemSortOrder The itemSortOrder to set.
	 */
	public void setItemSortOrder(int itemSortOrder) {
		this.itemSortOrder = itemSortOrder;
	}
	/**
	 * @return Returns the itemSortOrder.
	 */
	public int getItemSortOrder() {
		return itemSortOrder;
	}
    /**
	 * @return Returns the eid.
	 */
	public int getEid() {
		return this.eid;
	}
	/**
	 * @param eid The eid to set.
	 */
	public void setEid(int eid) {
		this.eid = eid;
	}
	/**
	 * @return the score
	 */
	public int getScore() {
		return score;
	}
	/**
	 * @param score the score to set
	 */
	public void setScore(int score) {
		this.score = score;
	}
	/**
	 * @return the constructedResponse
	 */
	public String getConstructedResponse() {
		return constructedResponse;
	}
	/**
	 * @param constructedResponse the constructedResponse to set
	 */
	public void setConstructedResponse(String constructedResponse) {
		this.constructedResponse = constructedResponse;
	}
	
	/**
	 * @return the answerArea
	 */
	public String getAnswerArea() {
		return answerArea;
	}
	/**
	 * @param answerArea the answerArea to set
	 */
	public void setAnswerArea(String answerArea) {
		this.answerArea = answerArea;
	}

	public boolean isReplicate() {
		return replicate;
	}

	public void setReplicate(boolean replicate) {
		this.replicate = replicate;
	}
} 
