// Decompiled by DJ v3.6.6.79 Copyright 2004 Atanas Neshkov  Date: 2/22/2010 1:10:30 PM
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Validation.java

package com.ctb.contentBridge.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.ctb.contentBridge.core.exception.SystemException;

// Referenced classes of package com.stgglobal.ads.common:
//            AdsCommon, ExceptionHandler

public class Validation {

	public Validation() {
	}

	public static void validationOfXMLData(ArrayList arrList, String flag)
			throws SystemException {
		String xmlResponse = "Valid";
		try {
			if (flag.equals("Item")) {
				if (((String) arrList.get(0)).trim().equals("")
						|| ((String) arrList.get(1)).trim().equals("")
						|| ((String) arrList.get(0)).length() > 32)
					throw new SystemException("Invalid publish_item request");
				else if (((String) arrList.get(0)).indexOf("'") > 0)
					throw new SystemException(
							"publish_item request xml has invalid identifier");
				else if (!((String) arrList.get(0)).equals("")
						&& !isValid((String) arrList.get(0)).equals("Valid"))
					throw new SystemException("Invalid XML");
			} else if (flag.equals("Subtest")) {
				if (((String) arrList.get(0)).trim().equals("")
						|| ((String) arrList.get(0)).length() > 32)
					throw new SystemException("invalid publish_subtest request");
				else if (((String) arrList.get(0)).indexOf("'") > 0)
					throw new SystemException(
							"publish_subtest request xml has invalid identifier");
				else if (!((String) arrList.get(0)).equals("")
						&& !isValid((String) arrList.get(0)).equals("Valid"))
					throw new SystemException("Invalid XMl");
			} else if (flag.equals("Asset")) {
				/*
				 * ZipFile file = new ZipFile(((String)arrList.get(2)).trim());
				 * ZipInputStream zinstream = new ZipInputStream( new
				 * FileInputStream(((String)arrList.get(2)).trim())); ZipEntry
				 * zentry = zinstream.getNextEntry();
				 */
				//String s = "/mappingdata/InnovativeItems/" + "FCAT_TEST.010";
				System.out.println("image value-->> "+arrList.get(2));

				File file = new File(((String) arrList.get(2)).trim());
				
				System.out.println("Absolute path image-->> "+file.getAbsolutePath());
				String[] listOfFiles = file.list();
				if (((String) arrList.get(0)).trim().equals("")
						|| ((String) arrList.get(1)).trim().equals("")
						|| ((String) arrList.get(2)).trim().equals("")
						|| ((String) arrList.get(0)).length() > 32
						|| ((String) arrList.get(1)).length() > 22)
					throw new SystemException(
							"publish_asset request xml is invalid");
				else if (((String) arrList.get(0)).indexOf("'") > 0)
					throw new SystemException(
							"publish_asset request xml has invalid identifier");
				else if (!file.exists()){
					System.out.println("exception "+file.exists());
					
					throw new SystemException(
							"Asset does not exist in the specified location.");
				}
				else if (!((String) arrList.get(0)).equals("")
						&& !isValid((String) arrList.get(0)).equals("Valid"))
					throw new SystemException("invalid XML");
			}
		} catch (Exception e) {
			throw new SystemException(e);
		}
	}

	public static String isValid(String strData) {
		for (int i = 0; i < strData.length(); i++)
			if ((strData.charAt(i) < 'A' || strData.charAt(i) > 'Z')
					&& (strData.charAt(i) < 'a' || strData.charAt(i) > 'z')
					&& (strData.charAt(i) < '0' || strData.charAt(i) > '9')
					&& strData.charAt(i) != '-' && strData.charAt(i) != '_'
					&& strData.charAt(i) != '.')
				return "Special Char";

		return "Valid";
	}
}