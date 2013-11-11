package com.ctb.lexington.util;

public class RuleTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		int inputParam = Integer.parseInt(args[0]);
		// String testString="1";
		// String testString="//3.1444";
		// String testString=" . 5 ";
//		String testString = "ABCDEF.";
		// String testString="//1//2";
		// String testString="/////2//3/////434//////";
		// String testString="....123...5....55....";
		// String testString="....123...555....";
//		String testString="//./71";
//		String testString="00/000./71";
		String testString="01.9";

		switch (inputParam) {

		case 0:
			System.out.println("Dummy Rule Executed");
			System.out.println("Input String:" + testString);
			System.out
					.println("Output String:" + RuleSet.RuleDummy(testString));
			break;
		case 1:
			System.out.println("Rule1 Executed");
			System.out.println("Input String:" + testString);
			System.out.println("Output String:" + RuleSet.Rule1(testString));
			break;

		case 2:
			System.out.println("Rule2 Executed");
			System.out.println("Input String:" + testString);
			System.out.println("Output String:" + RuleSet.Rule2(testString));
			break;

		case 3:
			System.out.println("Rule3 Executed");
			System.out.println("Input String:" + testString);
			System.out.println("Output String:" + RuleSet.Rule3(testString));
			break;
		case 4:
			System.out.println("Rule4 Executed");
			System.out.println("Input String:" + testString);
			System.out.println("Output String:" + RuleSet.Rule4(testString));
			break;
		case 5:
			System.out.println("Rule5 Executed");
			System.out.println("Input String:" + testString);
			System.out.println("Output String:" + RuleSet.Rule5(testString));
			break;

		case 6:
			System.out.println("Rule6 Executed");
			System.out.println("Input String:" + testString);
			System.out.println("Output String:" + RuleSet.Rule6(testString));
			break;

		case 7:
			System.out.println("Rule7 Executed");
			System.out.println("Input String:" + testString);
			System.out.println("Output String:" + RuleSet.Rule7(testString));
			break;

		case 8:
			System.out.println("Rule8 Executed");
			System.out.println("Input String:" + testString);
			System.out.println("Output String:" + RuleSet.Rule8(testString));
			break;

		case 9:
			System.out.println("Rule9 Executed");
			System.out.println("Input String:" + testString);
			System.out.println("Output String:" + RuleSet.Rule9(testString));
			break;
		case 10:
			System.out.println("Rule10 Executed");
			System.out.println("Input String:" + testString);
			System.out.println("Output String:" + RuleSet.Rule10(testString));
			break;
		case 11:
			System.out.println("Rule11 Executed");
			System.out.println("Input String:" + testString);
			System.out.println("Output String:" + RuleSet.Rule11(testString));
			break;
		case 12:
			System.out.println("Rule12 Executed");
			System.out.println("Input String:" + testString);
			System.out.println("Output String:" + RuleSet.Rule12(testString));
			break;
		case 15:
			System.out.println("Rule15 Executed");
			System.out.println("Input String:" + testString);
			System.out.println("Output String:" + RuleSet.Rule15(testString));
			break;
		case 16:
			System.out.println("Rule16 Executed");
			System.out.println("Input String:" + testString);
			System.out.println("Output String:" + RuleSet.Rule16(testString));
			break;
		}

	}
}
