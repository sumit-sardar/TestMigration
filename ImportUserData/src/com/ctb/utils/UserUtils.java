package com.ctb.utils;

import java.io.Serializable;
import java.security.MessageDigest;
import java.util.Random;
import java.util.Set;

import com.ctb.bean.UserFileRow;

public class UserUtils implements Serializable {

	static final long serialVersionUID = 1L;

	/**
	 * Converts object Array to String
	 * 
	 * @param obj
	 * @return String
	 */
	public static String convertArraytoString(Object[] obj) {
		String tempStr = "";
		for (Object value : obj) {
			tempStr = tempStr + getValue(value) + ",";
		}
		tempStr = tempStr.substring(0, tempStr.length() - 1);
		return tempStr;
	}

	/**
	 * Returns String representation of an Object
	 * 
	 * @param value
	 * @return Object
	 */
	private static Object getValue(Object value) {
		if (value instanceof String) {
			return "'" + value + "'";
		} else {
			return value;
		}
	}

	/**
	 * Generates Username from user that has to be inserted in DB.
	 * 
	 * @param user
	 * @param suffix
	 * @return String
	 */
	public static String generateBasicUsername(UserFileRow user, String suffix) {
		String firstName = convertForUserName(user.getFirstName());
		String lastName = convertForUserName(user.getLastName());
		String middleName = convertForUserName(user.getMiddleName());
		firstName = firstName.length() < 11 ? firstName : firstName.substring(
				0, 10);
		firstName = firstName.endsWith("_") ? firstName : firstName + "_";
		lastName = lastName.length() < 11 ? lastName : lastName
				.substring(0, 10);
		// lastName = lastName.endsWith("_")?lastName:lastName + "_";
		String middleInitial = middleName.length() > 0 ? middleName.substring(
				0, 1) : "";

		return new String(firstName.toLowerCase()
				+ (middleInitial.length() > 0 ? middleInitial.toLowerCase()
						+ "_" : "") + lastName.toLowerCase())
				+ suffix;
	}

	/**
	 * Generates a usernames of format <first-name>_<mi>_<last-name>_mmdd
	 * 
	 * @param student
	 * @return String
	 */
	public static String generateEscapeUsername(UserFileRow user) {
		String firstName = convertForUserName(user.getFirstName());
		String lastName = convertForUserName(user.getLastName());
		String middleName = convertForUserName(user.getMiddleName());
		firstName = firstName.length() < 11 ? firstName : firstName.substring(
				0, 10);

		firstName = firstName.endsWith("_") ? firstName : firstName + "_";
		lastName = lastName.length() < 11 ? lastName : lastName
				.substring(0, 10);
		String middleInitial = middleName.length() > 0 ? middleName.substring(
				0, 1) : "";

		String usrName = new String(firstName.toLowerCase()
				+ (middleInitial.length() > 0 ? middleInitial.toLowerCase()
						+ "_" : "") + lastName.toLowerCase());

		return usrName;
	}

	/**
	 * Generates a user's usernames of format <first-name>-<mi>-<last-name>
	 * 
	 * @param User
	 * @return String
	 */
	public static String generateUniqueUserName(Set<String> newSet,
			UserFileRow user) {

		String userName = user.getUserName();
		if (null == userName) {
			userName = user.getBasicUserName();
		}
		int count = 0;
		while (newSet.contains(userName)) {
			count++;
			String suffix = "-" + count;
			userName = userName+suffix;
		}
		newSet.add(userName);
		return userName;

	}

	/**
	 * Filtering characters
	 * 
	 * @param inString_
	 * @return String
	 */
	private static String convertForUserName(String inString_) {
		if (inString_ == null)
			return "";
		inString_ = inString_.trim();
		char prev_c = '*';
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < inString_.length(); ++i) {
			char c = inString_.charAt(i);
			if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')
					|| (c >= '0' && c <= '9')) {
				sb.append(c);
				prev_c = c;
			} else if ((c == ' ') && (prev_c != '_')) {
				sb.append('_');
				prev_c = '_';
			}
		}
		return sb.toString();
	}

	/**
	 * Converts first letter to Uppercase
	 * 
	 * @param str
	 * @return String
	 */
	public static String upperCaseFirstLetter(String str) {
		if (str != null && !str.equals("")) {
			str = str.trim();
			if (str.length() <= 1) {
				str = str.toUpperCase();
			} else {
				String firstLetter = str.substring(0, 1).toUpperCase();
				String otherLetters = str.substring(1);
				str = new StringBuffer().append(firstLetter)
						.append(otherLetters).toString();
			}
		}
		return str;
	}

	public static String generateRandomPassword(int passwordLength)
			throws Exception {
		String alphaNumArray = Constants.ALPHA_ARRAY + Constants.NUM_ARRAY;
		String password = "";
		int index = 0;
		Random rnd = new Random();
		boolean validPassword = false;
		while (!validPassword) {
			password = "";
			for (int i = 0; i < passwordLength; i++) {
				index = rnd.nextInt();
				if (index < 0) {
					index = index * -1;
				}
				// make sure the index is a value within the length of our array
				if (index != 0) {
					index = index % alphaNumArray.length();
				}
				password = password.concat(String.valueOf(alphaNumArray
						.charAt(index)));
			}
			if (verifyContainsCharFrom(Constants.ALPHA_ARRAY, password)
					&& verifyContainsCharFrom(Constants.NUM_ARRAY, password)) {
				validPassword = true;
			}
		}
		return encodePassword(password);
	}

	public static String encodePassword(String password) throws Exception {
		MessageDigest md;
		StringBuffer retval = new StringBuffer("");
		byte[] hash = new byte[] {};
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(password.getBytes());
			hash = md.digest();
		} catch (Exception e) {
			throw e;
		}
		for (int i = 0; i < hash.length; ++i) {
			if (((int) hash[i] & 0xff) < 0x10) {
				retval.append("0");
			}
			retval.append(Long.toString((int) hash[i] & 0xff, 16));
		}
		return retval.toString();
	}

	private static boolean verifyContainsCharFrom(String charArray,
			String password) {
		boolean verified = false;
		int j = 0;
		while (!verified && (j < password.length())) {
			if (charArray.indexOf(String.valueOf(password.charAt(j))) != -1) {
				verified = true;
			}
			j++;
		}
		return verified;
	}

}
