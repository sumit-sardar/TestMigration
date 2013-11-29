package com.ctb.lexington.util;

import java.text.DecimalFormat;

public class FractionConverstionUtil {

	/**
	 * ************************************************************************
	 * 395168:kingshukc
	 * 
	 * Utility to round off to 4 decimal points.
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String testString = "2/3";
        System.out.println(roundTo4DecimalPlaces(testString));
	}
	public static String roundTo4DecimalPlaces(String nonRoundedString){
		
		
			try{
				if (nonRoundedString.indexOf("/") > 0) {
					Double initialValue = Double.parseDouble(nonRoundedString
							.substring(0,nonRoundedString.indexOf("/")))
							/ Double.parseDouble(nonRoundedString.substring(nonRoundedString
									.indexOf("/")+1));
					System.out.println("Input:"+nonRoundedString);
					System.out.println("InitialValue:"+initialValue);
	//				DecimalFormat df=new DecimalFormat("###.#####");				// Fix for defect #76092
					DecimalFormat df=new DecimalFormat("###.####");
					String roundedString=df.format(initialValue);
					System.out.println("Rounded Value:"+roundedString);
					return roundedString;
				}
			}catch(Exception e){
				System.out.println("Problem in rounding...");
				//e.printStackTrace();
			}
		
		return nonRoundedString;
		
	}

}
