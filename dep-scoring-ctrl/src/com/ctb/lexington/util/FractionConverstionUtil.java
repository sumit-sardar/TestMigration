package com.ctb.lexington.util;

import java.text.DecimalFormat;

public class FractionConverstionUtil {

	/**
	 * ************************************************************************
	 * 395168:kingshukc
	 * 
	 * Utility to round off to 5 decimal points.
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String testString = "2/3";
        System.out.println(roundTo5DecimalPlaces(testString));
	}
	public static String roundTo5DecimalPlaces(String nonRoundedString){
		
		if (nonRoundedString.indexOf("/") > 0) {
			try{
				Double initialValue = Double.parseDouble(nonRoundedString
						.substring(0,nonRoundedString.indexOf("/")))
						/ Double.parseDouble(nonRoundedString.substring(nonRoundedString
								.indexOf("/")+1));
				System.out.println("Input:"+nonRoundedString);
				System.out.println("InitialValue:"+initialValue);
				DecimalFormat df=new DecimalFormat("###.#####");
				String roundedString=df.format(initialValue);
				System.out.println("Rounded Value:"+roundedString);
				return roundedString;
			}catch(Exception e){
				System.out.println("Problem in rounding...");
				e.printStackTrace();
			}
		}
		return nonRoundedString;
		
	}

}
