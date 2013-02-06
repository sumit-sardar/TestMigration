// Decompiled by DJ v3.6.6.79 Copyright 2004 Atanas Neshkov  Date: 2/22/2010 1:14:30 PM
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Base64EncodeDecode.java

package com.ctb.contentBridge.core.util.Base64;

import java.io.IOException;
import java.io.InputStream;

import com.ctb.contentBridge.core.exception.SystemException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Base64EncodeDecode {

	public Base64EncodeDecode() {
		encodedData = null;
	}

	public String base64Encoding(InputStream inputStream, int iLength)
			throws SystemException {
		byte bBuffer[] = new byte[iLength];
		try {
			iLength = inputStream.read(bBuffer, 0, iLength);
			encodedData = (new BASE64Encoder()).encode(bBuffer);
		} catch (IOException e) {
			throw new SystemException(e);
		}
		return encodedData;
	}

	public byte[] base64Decoding(String encodedData) throws SystemException {
		byte bBuffer[] = new byte[encodedData.length()];
		try {
			bBuffer = (new BASE64Decoder()).decodeBuffer(encodedData);
		} catch (IOException e) {
			throw new SystemException(e);
		}
		return bBuffer;
	}

	String encodedData;
}