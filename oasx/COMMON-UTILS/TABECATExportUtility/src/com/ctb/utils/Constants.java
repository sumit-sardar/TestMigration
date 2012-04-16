package com.ctb.utils;

import java.util.ArrayList;
import java.util.List;

public class Constants {
	public static final String USAGE ="Usage";
	public static final String SENTENCE_FORMATION = "Sentence Formation and Paragraph Development";
	public static final String CAPITALIZATION = "Capitalization, Punctuation, and Writing Conventions";
	public static final String NUMBER_OPERATIONS = "Number and Number Operations";
	public static final String COMPUTAION_ESTIMATION = "Computation and Estimation";
	public static final String MEASUREMENT = "Measurement";
	public static final String GEOMETRY_SPATIAL_SENSE = "Geometry and Spatial Sense";
	public static final String DATA_ANALYSIS = "Data Analysis, Statistics, Probability";
	public static final String PATTERNS_FUNCTIONS_ALGEBRA ="Patterns, Functions, Algebra";
	public static final String PROBLEM_SOLVING ="Problem Solving and Reasoning";
	public static final String RECALL_INFORMATION = "Recall Information";
	public static final String WORDS_IN_CONTEXT = "Words in Context/Construct Meaning";
	public static final String EVALUATE = "Evaluate/Extend Meaning and Interpret Graphic Information";
	public static final String WHOLE_NUMBERS = "Whole Numbers";
	public static final String DECIMALS_FRACTIONS = "Decimals, Fractions, Percents";
	public static final String INTEGERS = "Integers";
	public static final String ALGEBRAIC_OPERATIONS = "Algebraic Operations";
	public static final String CONSTRUCT_MEANING_IN_CONTEXT = "Construct Meaning in Context";
	public static final String INTERNET_EVALUATE_EXTEND = "Interpret, Evaluate, and Extend Meaning";
	public static final String STATISTICS_PROBABILITY = "Statistics and Probability";
	public static final String SENTENCE_PARAGRAPH = "Sentence and Paragraph Development";
	public static final String WRITING_MECHANICS_CONVENTIONS = "Writing Mechanics and Conventions";
	public static final List<String> OBJECTIVES = new ArrayList<String>(20);
	
	static {
		OBJECTIVES.add(Constants.RECALL_INFORMATION);
		OBJECTIVES.add(Constants.CONSTRUCT_MEANING_IN_CONTEXT);
		OBJECTIVES.add(Constants.INTERNET_EVALUATE_EXTEND);
		OBJECTIVES.add(Constants.WHOLE_NUMBERS);
		OBJECTIVES.add(Constants.DECIMALS_FRACTIONS);
		OBJECTIVES.add(Constants.INTEGERS);
		OBJECTIVES.add(Constants.ALGEBRAIC_OPERATIONS);
		OBJECTIVES.add(Constants.NUMBER_OPERATIONS);
		OBJECTIVES.add(Constants.COMPUTAION_ESTIMATION);
		OBJECTIVES.add(Constants.MEASUREMENT);
		OBJECTIVES.add(Constants.GEOMETRY_SPATIAL_SENSE);
		OBJECTIVES.add(Constants.STATISTICS_PROBABILITY);
		OBJECTIVES.add(Constants.PATTERNS_FUNCTIONS_ALGEBRA);
		OBJECTIVES.add(Constants.PROBLEM_SOLVING);
		OBJECTIVES.add(Constants.USAGE);
		OBJECTIVES.add(Constants.SENTENCE_PARAGRAPH);
		OBJECTIVES.add(Constants.WRITING_MECHANICS_CONVENTIONS);
	}
}