package com.ctb.dto;

import java.io.Serializable;

import com.ctb.utils.Utility;

public class ObjectiveLevel implements Serializable {

	private static final long serialVersionUID = 1L;

	
	private String usage;
	private String sentence;
	private String capitalization;
	private String number;
	private String computation;
	private String measurement;
	private String geometry;
	private String data;
	private String patterns;	
	private String problem;
	private String recall;
	private String words;
	private String evaluate;
	private String whole;
	private String decimals;
	private String integers;
	private String algebraic;

	@Override
	public String toString(){
		String val="";
		val += Utility.getFormatedString(usage, 1) 
		+ Utility.getFormatedString(sentence, 1) 
		+ Utility.getFormatedString(capitalization, 1)
		+ Utility.getFormatedString(number, 1)
		+ Utility.getFormatedString(computation, 1)
		+ Utility.getFormatedString(measurement, 1)
		+ Utility.getFormatedString(geometry, 1)
		+ Utility.getFormatedString(data, 1)
		+ Utility.getFormatedString(patterns, 1)
		+ Utility.getFormatedString(problem, 1)
		+ Utility.getFormatedString(recall, 1)
		+ Utility.getFormatedString(words, 1)
		+ Utility.getFormatedString(evaluate, 1)
		+ Utility.getFormatedString(whole, 1)
		+ Utility.getFormatedString(decimals, 1)
		+ Utility.getFormatedString(integers, 1)
		+ Utility.getFormatedString(algebraic, 1);

		return val;
		
	}

	/**
	 * @return the usage
	 */
	public String getUsage() {
		return usage;
	}

	/**
	 * @param usage the usage to set
	 */
	public void setUsage(String usage) {
		this.usage = usage;
	}

	/**
	 * @return the sentence
	 */
	public String getSentence() {
		return sentence;
	}

	/**
	 * @param sentence the sentence to set
	 */
	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	/**
	 * @return the capitalization
	 */
	public String getCapitalization() {
		return capitalization;
	}

	/**
	 * @param capitalization the capitalization to set
	 */
	public void setCapitalization(String capitalization) {
		this.capitalization = capitalization;
	}

	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
	}

	/**
	 * @return the computation
	 */
	public String getComputation() {
		return computation;
	}

	/**
	 * @param computation the computation to set
	 */
	public void setComputation(String computation) {
		this.computation = computation;
	}

	/**
	 * @return the measurement
	 */
	public String getMeasurement() {
		return measurement;
	}

	/**
	 * @param measurement the measurement to set
	 */
	public void setMeasurement(String measurement) {
		this.measurement = measurement;
	}

	/**
	 * @return the geometry
	 */
	public String getGeometry() {
		return geometry;
	}

	/**
	 * @param geometry the geometry to set
	 */
	public void setGeometry(String geometry) {
		this.geometry = geometry;
	}

	/**
	 * @return the data
	 */
	public String getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(String data) {
		this.data = data;
	}

	/**
	 * @return the patterns
	 */
	public String getPatterns() {
		return patterns;
	}

	/**
	 * @param patterns the patterns to set
	 */
	public void setPatterns(String patterns) {
		this.patterns = patterns;
	}

	/**
	 * @return the problem
	 */
	public String getProblem() {
		return problem;
	}

	/**
	 * @param problem the problem to set
	 */
	public void setProblem(String problem) {
		this.problem = problem;
	}

	/**
	 * @return the recall
	 */
	public String getRecall() {
		return recall;
	}

	/**
	 * @param recall the recall to set
	 */
	public void setRecall(String recall) {
		this.recall = recall;
	}

	/**
	 * @return the words
	 */
	public String getWords() {
		return words;
	}

	/**
	 * @param words the words to set
	 */
	public void setWords(String words) {
		this.words = words;
	}

	/**
	 * @return the evaluate
	 */
	public String getEvaluate() {
		return evaluate;
	}

	/**
	 * @param evaluate the evaluate to set
	 */
	public void setEvaluate(String evaluate) {
		this.evaluate = evaluate;
	}

	/**
	 * @return the whole
	 */
	public String getWhole() {
		return whole;
	}

	/**
	 * @param whole the whole to set
	 */
	public void setWhole(String whole) {
		this.whole = whole;
	}

	/**
	 * @return the decimals
	 */
	public String getDecimals() {
		return decimals;
	}

	/**
	 * @param decimals the decimals to set
	 */
	public void setDecimals(String decimals) {
		this.decimals = decimals;
	}

	/**
	 * @return the integers
	 */
	public String getIntegers() {
		return integers;
	}

	/**
	 * @param integers the integers to set
	 */
	public void setIntegers(String integers) {
		this.integers = integers;
	}

	/**
	 * @return the algebraic
	 */
	public String getAlgebraic() {
		return algebraic;
	}

	/**
	 * @param algebraic the algebraic to set
	 */
	public void setAlgebraic(String algebraic) {
		this.algebraic = algebraic;
	}
	
}




 
