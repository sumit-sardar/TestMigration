package com.ctb.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import com.ctb.bean.ManageStudent;

public class StudentUtils {

	static final long serialVersionUID = 1L;

	/**
	 * Generates a student usernames of format
	 * <first-name>-<mi>-<last-name>-mmdd
	 * 
	 * @param student
	 * @return String
	 */
	public static String generateUniqueStudentUserName(Set<String> newSet,
			ManageStudent student) {

		String userName = student.getLoginId();
		if (null == userName) {
			userName = StudentUtils.generateBasicStudentUsername(student, "");
		}
		int count = 0;
		while (newSet.contains(userName)) {
			count++;
			String suffix = "-" + count;
			userName = StudentUtils.generateBasicStudentUsername(student,
					suffix);
		}
		newSet.add(userName);
		return userName;

	}

	/**
	 * Generates basic UserName for student
	 * 
	 * @param student
	 * @param suffix
	 * @return String
	 */
	private static String generateBasicStudentUsername(ManageStudent student,
			String suffix) {
		String newUserName = null;
		String firstName = convertForUserName(student.getFirstName());
		String lastName = convertForUserName(student.getLastName());
		String middleName = convertForUserName(student.getMiddleName());
		firstName = firstName.length() < 11 ? firstName : firstName.substring(
				0, 10);
		firstName = firstName.endsWith("-") ? firstName : firstName + "-";
		lastName = lastName.length() < 11 ? lastName : lastName
				.substring(0, 10);
		lastName = lastName.endsWith("-") ? lastName : lastName + "-";
		String middleInitial = middleName.length() > 0 ? middleName.substring(
				0, 1) : "";
		// GACRCT2010CR007 - changed to concatenate 4 digit sequence number when
		// provide student date of birth is null.
		String seqNumber = "";
		if (student.getBirthDate() != null) {

			Date birthdate = student.getBirthDate();
			String dobDay = (new SimpleDateFormat("d")).format(birthdate);
			String dobMonth = (new SimpleDateFormat("M")).format(birthdate);
			seqNumber = padWithZero(dobMonth) + padWithZero(dobDay);

		}

		newUserName = new String(firstName.toUpperCase()
				+ (middleInitial.length() > 0 ? middleInitial.toUpperCase()
						+ "-" : "") + lastName.toUpperCase() + seqNumber)
				+ suffix;

		return newUserName;
	}

	/**
	 * Used for Username population logic
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
			} else if ((c == ' ') && (prev_c != '-')) {
				sb.append('-');
				prev_c = '-';
			}
		}
		return sb.toString();
	}

	/**
	 * Pads zero with the input string
	 * 
	 * @param number_
	 * @return String
	 */
	private static String padWithZero(String number_) {
		if (number_.length() < 2) {
			number_ = "0" + number_;
		}
		return number_;
	}

}
