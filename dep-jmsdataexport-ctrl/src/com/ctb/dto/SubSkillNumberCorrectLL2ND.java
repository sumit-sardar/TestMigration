package com.ctb.dto;

import com.ctb.utils.EmetricUtil;

public class SubSkillNumberCorrectLL2ND {
	
	private String speakingSocialInstructionalCommunication;
	private String speakingLanguageArtsScocialHistory;
	private String speakingMathematicsScienceTechnical;
	private String speakingAcademic;
	private String listeningSocialInstructionalCommunication;
	private String listeningLanguageArtsScocialHistory;
	private String listeningMathematicsScienceTechnical;
	private String listeningAcademic;
	private String readingFoundationalSkills;
	private String readingSocialInstructionalCommunication;
	private String readingLanguageArtsScocialHistory;
	private String readingMathematicsScienceTechnical;
	private String readingAcademic;
	private String writingFoundationalSkills;
	private String writingSocialInstructionalCommunication;
	private String writingLanguageArtsScocialHistory;
	private String writingMathematicsScienceTechnical;
	private String writingAcademic;
	private String subUnused;
	
	
	public String getSpeakingSocialInstructionalCommunication() {
		return speakingSocialInstructionalCommunication;
	}
	public void setSpeakingSocialInstructionalCommunication(
			String speakingSocialInstructionalCommunication) {
		this.speakingSocialInstructionalCommunication = speakingSocialInstructionalCommunication;
	}
	public String getSpeakingLanguageArtsScocialHistory() {
		return speakingLanguageArtsScocialHistory;
	}
	public void setSpeakingLanguageArtsScocialHistory(
			String speakingLanguageArtsScocialHistory) {
		this.speakingLanguageArtsScocialHistory = speakingLanguageArtsScocialHistory;
	}
	public String getSpeakingMathematicsScienceTechnical() {
		return speakingMathematicsScienceTechnical;
	}
	public void setSpeakingMathematicsScienceTechnical(
			String speakingMathematicsScienceTechnical) {
		this.speakingMathematicsScienceTechnical = speakingMathematicsScienceTechnical;
	}
	public String getSpeakingAcademic() {
		return speakingAcademic;
	}
	public void setSpeakingAcademic(String speakingAcademic) {
		this.speakingAcademic = speakingAcademic;
	}
	public String getListeningSocialInstructionalCommunication() {
		return listeningSocialInstructionalCommunication;
	}
	public void setListeningSocialInstructionalCommunication(
			String listeningSocialInstructionalCommunication) {
		this.listeningSocialInstructionalCommunication = listeningSocialInstructionalCommunication;
	}
	public String getListeningLanguageArtsScocialHistory() {
		return listeningLanguageArtsScocialHistory;
	}
	public void setListeningLanguageArtsScocialHistory(
			String listeningLanguageArtsScocialHistory) {
		this.listeningLanguageArtsScocialHistory = listeningLanguageArtsScocialHistory;
	}
	public String getListeningMathematicsScienceTechnical() {
		return listeningMathematicsScienceTechnical;
	}
	public void setListeningMathematicsScienceTechnical(
			String listeningMathematicsScienceTechnical) {
		this.listeningMathematicsScienceTechnical = listeningMathematicsScienceTechnical;
	}
	public String getListeningAcademic() {
		return listeningAcademic;
	}
	public void setListeningAcademic(String listeningAcademic) {
		this.listeningAcademic = listeningAcademic;
	}
	public String getReadingFoundationalSkills() {
		return readingFoundationalSkills;
	}
	public void setReadingFoundationalSkills(String readingFoundationalSkills) {
		this.readingFoundationalSkills = readingFoundationalSkills;
	}
	public String getReadingSocialInstructionalCommunication() {
		return readingSocialInstructionalCommunication;
	}
	public void setReadingSocialInstructionalCommunication(
			String readingSocialInstructionalCommunication) {
		this.readingSocialInstructionalCommunication = readingSocialInstructionalCommunication;
	}
	public String getReadingLanguageArtsScocialHistory() {
		return readingLanguageArtsScocialHistory;
	}
	public void setReadingLanguageArtsScocialHistory(
			String readingLanguageArtsScocialHistory) {
		this.readingLanguageArtsScocialHistory = readingLanguageArtsScocialHistory;
	}
	public String getReadingMathematicsScienceTechnical() {
		return readingMathematicsScienceTechnical;
	}
	public void setReadingMathematicsScienceTechnical(
			String readingMathematicsScienceTechnical) {
		this.readingMathematicsScienceTechnical = readingMathematicsScienceTechnical;
	}
	public String getReadingAcademic() {
		return readingAcademic;
	}
	public void setReadingAcademic(String readingAcademic) {
		this.readingAcademic = readingAcademic;
	}
	public String getWritingFoundationalSkills() {
		return writingFoundationalSkills;
	}
	public void setWritingFoundationalSkills(String writingFoundationalSkills) {
		this.writingFoundationalSkills = writingFoundationalSkills;
	}
	public String getWritingSocialInstructionalCommunication() {
		return writingSocialInstructionalCommunication;
	}
	public void setWritingSocialInstructionalCommunication(
			String writingSocialInstructionalCommunication) {
		this.writingSocialInstructionalCommunication = writingSocialInstructionalCommunication;
	}
	public String getWritingLanguageArtsScocialHistory() {
		return writingLanguageArtsScocialHistory;
	}
	public void setWritingLanguageArtsScocialHistory(
			String writingLanguageArtsScocialHistory) {
		this.writingLanguageArtsScocialHistory = writingLanguageArtsScocialHistory;
	}
	public String getWritingMathematicsScienceTechnical() {
		return writingMathematicsScienceTechnical;
	}
	public void setWritingMathematicsScienceTechnical(
			String writingMathematicsScienceTechnical) {
		this.writingMathematicsScienceTechnical = writingMathematicsScienceTechnical;
	}
	public String getWritingAcademic() {
		return writingAcademic;
	}
	public void setWritingAcademic(String writingAcademic) {
		this.writingAcademic = writingAcademic;
	}
	public String getSubUnused() {
		return subUnused;
	}
	public void setSubUnused(String subUnused) {
		this.subUnused = subUnused;
	}


	public String toString(){
		String val="";
		val += EmetricUtil.getFormatedString(speakingSocialInstructionalCommunication, 3)
		+EmetricUtil.getFormatedString(speakingLanguageArtsScocialHistory, 3) 
		+EmetricUtil.getFormatedString(speakingMathematicsScienceTechnical, 3) 
		+EmetricUtil.getFormatedString(speakingAcademic, 3)
		+EmetricUtil.getFormatedString(listeningSocialInstructionalCommunication, 3) 
		+EmetricUtil.getFormatedString(listeningLanguageArtsScocialHistory, 3) 
		+EmetricUtil.getFormatedString(listeningMathematicsScienceTechnical, 3) 
		+EmetricUtil.getFormatedString(listeningAcademic, 3) 
		+EmetricUtil.getFormatedString(readingFoundationalSkills, 3) 
		+EmetricUtil.getFormatedString(readingSocialInstructionalCommunication, 3) 
		+EmetricUtil.getFormatedString(readingLanguageArtsScocialHistory, 3) 
		+EmetricUtil.getFormatedString(readingMathematicsScienceTechnical, 3) 
		+EmetricUtil.getFormatedString(readingAcademic, 3) 
		+EmetricUtil.getFormatedString(writingFoundationalSkills, 3)
		+EmetricUtil.getFormatedString(writingSocialInstructionalCommunication, 3)
		+EmetricUtil.getFormatedString(writingLanguageArtsScocialHistory, 3)
		+EmetricUtil.getFormatedString(writingMathematicsScienceTechnical, 3)
		+EmetricUtil.getFormatedString(writingAcademic, 3)
		+EmetricUtil.getFormatedString(subUnused, 12);
	
		return val;
		
	}
	
}
