package com.ctb.tms.bean.login;

import java.math.BigInteger;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import noNamespace.AdssvcRequestDocument.AdssvcRequest.SaveTestingSessionData.Tsd;
import noNamespace.AdssvcRequestDocument.AdssvcRequest.SaveTestingSessionData.Tsd.Ast;
import noNamespace.AdssvcRequestDocument.AdssvcRequest.SaveTestingSessionData.Tsd.Ist;
import noNamespace.AdssvcRequestDocument.AdssvcRequest.SaveTestingSessionData.Tsd.Ist.Ov;
import noNamespace.AdssvcRequestDocument.AdssvcRequest.SaveTestingSessionData.Tsd.Ist.Rv;
import noNamespace.BaseType;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.ConsolidatedRestartData;

public class ItemResponseData extends ReplicationObject {

    static Logger logger = Logger.getLogger(ItemResponseData.class);

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
    private boolean audioItem;
    private String responseType;
    private boolean sendCatSave;

    @Override
    public String toString(){
        String data = "testRosterId="+testRosterId;
        data += " itemSetId="+this.itemSetId;
        data += " itemId="+this.itemId;
        data += " itemType="+this.itemType;
        data += " itemSortOrder="+this.itemSortOrder;
        data += " responseElapsedTime="+this.responseElapsedTime;
        data += " eid="+this.eid;
        data += " score="+this.score;
        data += " audioItem="+this.audioItem;
        data += " sendCatSave="+this.sendCatSave;
        
        String ret = "<ItemResponseData "+data+">\n";
        ret += "<responseSeqNum>"+responseSeqNum+"</responseSeqNum>\n";
        ret += "<studentMarked>"+studentMarked+"</studentMarked>\n";
        ret += "<responseType>"+responseType+"</responseType>\n";
        ret += "<response>"+response+"</response>\n";
        ret += "<answerArea>"+answerArea+"</answerArea>\n";
        ret += "<constructedResponse>"+constructedResponse+"</constructedresponse>\n";
        ret += "</ItemResponseData>\n";
        return ret;
    }
    public static Tsd IrdToAdsTsd(ItemResponseData ird) {
        Tsd tsd = Tsd.Factory.newInstance();
        tsd.setScid(String.valueOf(ird.getItemSetId()));
        tsd.setLsid(String.valueOf(ird.getTestRosterId()));
        Ast ast = tsd.addNewAst();
        int maxRSN = 0;
        int totalDur = 0;
        tsd.addNewIst();
        Ist ist = tsd.getIstArray(0);
        ist.setIid(ird.getItemId());
        ist.setEid("" + ird.getEid());
        ist.setCst(Ist.Cst.UNKNOWN);
        ist.setMrk("T".equals(ird.getStudentMarked()));
        ist.setDur(ird.getResponseElapsedTime());
        ist.setAudioItem(ird.audioItem);
        ist.setSendCatSave(ird.isSendCatSave());
        tsd.setMseq(new BigInteger(ird.getResponseSeqNum()));
        totalDur = totalDur + ird.getResponseElapsedTime();
        Rv rv = ist.addNewRv();
        if ("SR".equals(ird.getItemType())) {
            rv.setT(BaseType.IDENTIFIER);
            rv.addNewV().setStringValue(ird.getResponse());
        } else {
            rv.setT(BaseType.STRING);
            rv.addNewV().setStringValue(ird.getConstructedResponse());
        }
        rv.setN("RESPONSE");

        Ov ov = ist.addNewOv();
        ov.setN("SCORE");
        ov.setT(BaseType.INTEGER);

        if ("SR".equals(ird.getItemType())) {
            ov.addNewV().setStringValue("" + ird.getScore());
        } else {
            ov.addNewV().setStringValue("");
        }

        if (Integer.valueOf(ird.getResponseSeqNum()) > maxRSN) {
            ast.setCurEid("" + ird.getEid());
            maxRSN = Integer.valueOf(ird.getResponseSeqNum());
        }
        logger.debug("\n*****  ItemResponseData: IrdToAdsTsd: converted ird to AdsTsd: " + tsd.xmlText());
        return tsd;
    }

    public static ItemResponseData AdsTsdToIrd(Tsd tsd) {
        ItemResponseData ird = new ItemResponseData();
        String testRosterId = tsd.getLsid();
        testRosterId = testRosterId.substring(0, testRosterId.indexOf(":"));
        ird.setTestRosterId(Integer.parseInt(testRosterId));
        ird.setItemSetId(Integer.parseInt(tsd.getScid()));
        ird.setResponseSeqNum(String.valueOf(tsd.getMseq()));
        Ist[] ista = tsd.getIstArray();
        for (int j = 0; j < ista.length; j++) {
            Ist ist = ista[j];
            ird.setItemId(ist.getIid());
            ird.setResponseElapsedTime((int) ist.getDur());
            ird.setEid(Integer.parseInt(ist.getEid()));
            ird.setAudioItem(ist.getAudioItem());
            ird.setSendCatSave(ist.getSendCatSave());
            // if(ist != null && ist.getRvArray(0) != null && ist.getRvArray(0).getVArray(0) != null) {
            if (ist != null && ist.getRvArray() != null && ist.getRvArray().length > 0) {
                if (ist.getRvArray(0).getVArray() != null && ist.getRvArray(0).getVArray().length > 0) {
                    if (ist.getRvArray(0).getVArray(0) != null) {
                        BaseType.Enum responseType = ist.getRvArray(0).getT();
                        ird.setResponseType(responseType.toString());
                        ird.setItemType(BaseType.STRING.equals(responseType) ? "CR" : "SR");
                        String xmlResponse = ist.getRvArray(0).getVArray(0);
                        String response = "";
                        String studentMarked = ist.getMrk() ? "T" : "F";
                        ird.setStudentMarked(studentMarked);
                        String audioItem = ist.getAudioItem() ? "T" : "F";
                        ird.setAudioItem(ist.getAudioItem());
                        if (xmlResponse != null && xmlResponse.length() > 0) {
                            // strip xml
                            int start = xmlResponse.indexOf(">");
                            if (start >= 0) {
                                response = xmlResponse.substring(start + 1);
                                int end = response.lastIndexOf("</");
                                if (end != -1) {
                                    response = response.substring(0, end);
                                }
                            } else {
                                response = xmlResponse;
                            }
                            // strip CDATA
                            start = response.indexOf("[CDATA[");
                            if (start >= 0) {
                                response = response.substring(start + 7);
                                int end = response.lastIndexOf("]]");
                                if (end != -1) {
                                    response = response.substring(0, end);
                                }
                            }
                        }
                        ird.setResponse(response);
                    }
                }
            }
            if (ist != null && ist.getOvArray() != null && ist.getOvArray().length > 0) {
                if (ist.getOvArray(0).getVArray() != null && ist.getOvArray(0).getVArray().length > 0) {
                    if (ist.getOvArray(0).getVArray(0) != null && !"".equals(ist.getOvArray(0).getVArray(0).trim())) {
                        ird.setScore(Integer.parseInt(ist.getOvArray(0).getVArray(0)));
                    }
                }
            }
        }
        logger.debug("\n*****  ItemResponseData: AdsTsdToIrd: constructed restart item response " + ird.getTestRosterId() + ", seqnum: " + ird.getResponseSeqNum() + ", item type: " + ird.getItemType() + ", response type: " + ird.getResponseType() + ", elapsed time: " + ird.getResponseElapsedTime() + ", response: " + ird.getResponse() + ", CR response: " + ird.getConstructedResponse());
        return ird;
    }

    public static ItemResponseData[] TmsTsdToIrd(ConsolidatedRestartData.Tsd tsd) {
        ArrayList irds = new ArrayList();
        String testRosterId = tsd.getLsid();
        testRosterId = testRosterId.substring(0, testRosterId.indexOf(":"));
        //ird.setResponseSeqNum(String.valueOf(tsd.getMseq()));
        ConsolidatedRestartData.Tsd.Ist[] ista = tsd.getIstArray();
        for (int j = 0; j < ista.length; j++) {
            ItemResponseData ird = new ItemResponseData();
            ird.setTestRosterId(Integer.parseInt(testRosterId));
            ird.setItemSetId(Integer.parseInt(tsd.getScid()));
            ConsolidatedRestartData.Tsd.Ist ist = ista[j];
            ird.setItemId(ist.getIid());
            ird.setResponseSeqNum(String.valueOf(ist.getMseq()));
            ird.setResponseElapsedTime((int) ist.getDur());
            ird.setEid(Integer.parseInt(ist.getEid()));
            // if(ist != null && ist.getRvArray(0) != null && ist.getRvArray(0).getVArray(0) != null) {
            if (ist != null && ist.getRvArray() != null && ist.getRvArray().length > 0) {
                if (ist.getRvArray(0).getV() != null) {
                    BaseType.Enum responseType = ist.getRvArray(0).getT();
                    ird.setResponseType(responseType.toString());
                    ird.setItemType(BaseType.STRING.equals(responseType) ? "CR" : "SR");
                    String xmlResponse = ist.getRvArray(0).getV();
                    String response = "";
                    String studentMarked = ist.getMrk();
                    ird.setStudentMarked("1".equals(studentMarked) ? "T" : "F");
                    //String audioItem = ist.getAudioItem() ? "T" : "F";
                    //ird.setAudioItem(ist.getAudioItem());
                    if (xmlResponse != null && xmlResponse.length() > 0) {
                        // strip xml
                        int start = xmlResponse.indexOf(">");
                        if (start >= 0) {
                            response = xmlResponse.substring(start + 1);
                            int end = response.lastIndexOf("</");
                            if (end != -1) {
                                response = response.substring(0, end);
                            }
                        } else {
                            response = xmlResponse;
                        }
                        // strip CDATA
                        start = response.indexOf("[CDATA[");
                        if (start >= 0) {
                            response = response.substring(start + 7);
                            int end = response.lastIndexOf("]]");
                            if (end != -1) {
                                response = response.substring(0, end);
                            }
                        }
                    }
                    ird.setResponse(response);
                    if ("CR".equals(ird.getItemType())) {
                        ird.setConstructedResponse(response);
                    }
                }
            }
            if (ist != null && ist.getOvArray() != null && ist.getOvArray().length > 0) {
                if (ist.getOvArray(0).getV() != null && !"".equals(ist.getOvArray(0).getV().trim())) {
                    ird.setScore(Integer.parseInt(ist.getOvArray(0).getV()));
                }
            }
            logger.debug("\n*****  ItemResponseData: TmsTsdToIrd: constructed restart item response " + ird.getTestRosterId() + ", seqnum: " + ird.getResponseSeqNum() + ", item type: " + ird.getItemType() + ", response type: " + ird.getResponseType() + ", elapsed time: " + ird.getResponseElapsedTime() + ", response: " + ird.getResponse() + ", CR response: " + ird.getConstructedResponse());
            irds.add(ird);
        }
        return (ItemResponseData[]) irds.toArray(new ItemResponseData[0]);
    }

    public boolean isSendCatSave() {
        return sendCatSave;
    }

    public void setSendCatSave(boolean sendCatSave) {
        this.sendCatSave = sendCatSave;
    }

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
     * @param studentMarked The student_marked to set.
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
}
