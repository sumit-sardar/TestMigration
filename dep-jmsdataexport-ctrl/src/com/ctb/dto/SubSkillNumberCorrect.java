package com.ctb.dto;

import com.ctb.utils.EmetricUtil;




public class SubSkillNumberCorrect
{
	
	private String speakInWords = "";
	private String speakSentences= "";
	private String makeConversations= "";
	private String tellAStory= "";
	private String listenForInformation= "";
	private String listenInTheClassroom= "";
	private String listenAndComprehend= "";
	private String analyzeWords= "";
	private String readWords= "";
	private String readForUnderStanding= "";
	private String useConventions= "";
	private String writeAbout= "";
	private String writeWhy= "";
	private String writeInDetail= "";
	private String unused= "";
	public String getSpeakInWords() {
		return speakInWords;
	}
	public void setSpeakInWords(String speakInWords) {
		this.speakInWords = speakInWords;
	}
	public String getSpeakSentences() {
		return speakSentences;
	}
	public void setSpeakSentences(String speakSentences) {
		this.speakSentences = speakSentences;
	}
	public String getMakeConversations() {
		return makeConversations;
	}
	public void setMakeConversations(String makeConversations) {
		this.makeConversations = makeConversations;
	}
	public String getTellAStory() {
		return tellAStory;
	}
	public void setTellAStory(String tellAStory) {
		this.tellAStory = tellAStory;
	}
	public String getListenForInformation() {
		return listenForInformation;
	}
	public void setListenForInformation(String listenForInformation) {
		this.listenForInformation = listenForInformation;
	}
	public String getListenInTheClassroom() {
		return listenInTheClassroom;
	}
	public void setListenInTheClassroom(String listenInTheClassroom) {
		this.listenInTheClassroom = listenInTheClassroom;
	}
	public String getListenAndComprehend() {
		return listenAndComprehend;
	}
	public void setListenAndComprehend(String listenAndComprehend) {
		this.listenAndComprehend = listenAndComprehend;
	}
	public String getAnalyzeWords() {
		return analyzeWords;
	}
	public void setAnalyzeWords(String analyzeWords) {
		this.analyzeWords = analyzeWords;
	}
	public String getReadWords() {
		return readWords;
	}
	public void setReadWords(String readWords) {
		this.readWords = readWords;
	}
	public String getReadForUnderStanding() {
		return readForUnderStanding;
	}
	public void setReadForUnderStanding(String readForUnderStanding) {
		this.readForUnderStanding = readForUnderStanding;
	}
	public String getUseConventions() {
		return useConventions;
	}
	public void setUseConventions(String useConventions) {
		this.useConventions = useConventions;
	}
	public String getWriteAbout() {
		return writeAbout;
	}
	public void setWriteAbout(String writeAbout) {
		this.writeAbout = writeAbout;
	}
	public String getWriteWhy() {
		return writeWhy;
	}
	public void setWriteWhy(String writeWhy) {
		this.writeWhy = writeWhy;
	}
	public String getWriteInDetail() {
		return writeInDetail;
	}
	public void setWriteInDetail(String writeInDetail) {
		this.writeInDetail = writeInDetail;
	}
	public String getUnused() {
		return unused;
	}
	public void setUnused(String unused) {
		this.unused = unused;
	}
	public String toString(){
		String val="";
		val += EmetricUtil.getFormatedString(speakInWords, 3)
		+EmetricUtil.getFormatedString(speakSentences, 3) 
		+EmetricUtil.getFormatedString(makeConversations, 3) 
		+EmetricUtil.getFormatedString(tellAStory, 3)
		+EmetricUtil.getFormatedString(listenForInformation, 3) 
		+EmetricUtil.getFormatedString(listenInTheClassroom, 3) 
		+EmetricUtil.getFormatedString(listenAndComprehend, 3) 
		+EmetricUtil.getFormatedString(analyzeWords, 3) 
		+EmetricUtil.getFormatedString(readWords, 3) 
		+EmetricUtil.getFormatedString(readForUnderStanding, 3) 
		+EmetricUtil.getFormatedString(useConventions, 3) 
		+EmetricUtil.getFormatedString(writeAbout, 3) 
		+EmetricUtil.getFormatedString(writeWhy, 3) 
		+EmetricUtil.getFormatedString(writeInDetail, 3) 
		+EmetricUtil.getFormatedString(unused, 9);
	

		return val;
		
	}
}
