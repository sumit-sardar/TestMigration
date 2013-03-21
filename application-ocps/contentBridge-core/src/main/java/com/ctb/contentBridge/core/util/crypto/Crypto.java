// Decompiled by DJ v3.6.6.79 Copyright 2004 Atanas Neshkov  Date: 2/22/2010 1:15:22 PM
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Crypto.java

package com.ctb.contentBridge.core.util.crypto;

import java.io.*;
import java.security.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.apache.log4j.Logger;
import org.bouncycastle.crypto.engines.RC4Engine;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.ctb.contentBridge.core.exception.SystemException;

public class Crypto {

	public Crypto() {
		Security.insertProviderAt(new BouncyCastleProvider(), 2);
	}

	public boolean checkHash(String strManifestHash, byte byteArray[]) {
		String strHash = generateHash(byteArray);
		return strHash.equals(strManifestHash);
	}

	public String checkHashAndDecrypt(String strKey, String strManifestHash,
			byte baInputByteArrayData[], String charSet,
			boolean bUseStrongCipher, boolean bGZIP) throws SystemException {
		String strOutputStringData = new String("");
		try {
			byte baOutputByteArrayData[] = checkHashAndDecrypt(strKey,
					strManifestHash, baInputByteArrayData, bUseStrongCipher,
					bGZIP);
			if (baOutputByteArrayData != null)
				strOutputStringData = new String(baOutputByteArrayData, charSet);
		} catch (Exception exception) {
			throw new SystemException(exception);
		}
		logger.info("srtOutputStringData=" + strOutputStringData + "; ");
		return strOutputStringData;
	}

	public byte[] checkHashAndDecrypt(String strKey, String strManifestHash,
			byte baInputByteArray[], boolean bUseStrongCipher, boolean bGZIP)
			throws SystemException {
		byte baOutputByteArrayData[] = (byte[]) null;
		try {
			if (strManifestHash == null
					|| checkHash(strManifestHash, baInputByteArray)) {
				MessageDigest messageDigest = MessageDigest.getInstance("MD5",
						"BC");
				byte baKey[] = new byte[strKey.length()];
				baKey = strKey.getBytes();
				messageDigest.update(baKey);
				byte baHash[] = messageDigest.digest();
				byte baHashKey[] = (byte[]) null;
				if (bUseStrongCipher)
					baHashKey = baHash;
				else
					baHashKey = createStandardProviderCompatibleHash(baHash);
				KeyParameter hashKeyParameter = new KeyParameter(baHashKey);
				RC4Engine rc4Engine = new RC4Engine();
				rc4Engine.init(false, hashKeyParameter);
				baOutputByteArrayData = new byte[baInputByteArray.length];
				rc4Engine.processBytes(baInputByteArray, 0,
						baInputByteArray.length, baOutputByteArrayData, 0);
				rc4Engine.reset();
				if (bGZIP)
					baOutputByteArrayData = gunzip(baOutputByteArrayData);
			}
		} catch (Exception exception) {
			throw new SystemException(exception);
		}
		return baOutputByteArrayData;
	}
	public byte[] checkHashAndDecryptForXml(String strKey, String strManifestHash,
			byte baInputByteArray[], boolean bUseStrongCipher, boolean bGZIP)
			throws SystemException {
		byte baOutputByteArrayData[] = (byte[]) null;
		try {
			if (strManifestHash == null
					) {
				MessageDigest messageDigest = MessageDigest.getInstance("MD5",
						"BC");
				byte baKey[] = new byte[strKey.length()];
				baKey = strKey.getBytes();
				messageDigest.update(baKey);
				byte baHash[] = messageDigest.digest();
				byte baHashKey[] = (byte[]) null;
				if (bUseStrongCipher)
					baHashKey = baHash;
				else
					baHashKey = createStandardProviderCompatibleHash(baHash);
				KeyParameter hashKeyParameter = new KeyParameter(baHashKey);
				RC4Engine rc4Engine = new RC4Engine();
				rc4Engine.init(false, hashKeyParameter);
				baOutputByteArrayData = new byte[baInputByteArray.length];
				rc4Engine.processBytes(baInputByteArray, 0,
						baInputByteArray.length, baOutputByteArrayData, 0);
				rc4Engine.reset();
				if (bGZIP)
					baOutputByteArrayData = gunzip(baOutputByteArrayData);
			}
		} catch (Exception exception) {
			throw new SystemException(exception);
		}
		return baOutputByteArrayData;
	}

	public byte[] encrypt(String strKey, String strInputStringData,
			String strCharset, boolean bUseStrongCipher, boolean bGZIP)
			throws SystemException {
		byte baOutputByteArrayData[] = (byte[]) null;
		try {
			byte baInputByteArrayData[] = strInputStringData
					.getBytes(strCharset);
			baOutputByteArrayData = encrypt(strKey, baInputByteArrayData,
					bUseStrongCipher, bGZIP);
		} catch (Exception exception) {
			throw new SystemException(exception);
		}
		return baOutputByteArrayData;
	}

	public byte[] encrypt(String strKey, byte baInputByteArray[],
			boolean bUseStrongCipher, boolean bGZIP) throws SystemException {
		byte baOutputByteArray[] = (byte[]) null;
		byte baByteArrayToEncrypt[] = baInputByteArray;
		if (bGZIP)
			baByteArrayToEncrypt = gzip(baInputByteArray);
		try {
			MessageDigest messageDigest = MessageDigest
					.getInstance("MD5", "BC");
			byte baKey[] = strKey.getBytes();
			messageDigest.update(baKey);
			byte baHash[] = messageDigest.digest();
			byte baHashKey[] = (byte[]) null;
			if (bUseStrongCipher)
				baHashKey = baHash;
			else
				baHashKey = createStandardProviderCompatibleHash(baHash);
			KeyParameter hashKeyParameter = new KeyParameter(baHashKey);
			RC4Engine rc4Engine = new RC4Engine();
			rc4Engine.init(true, hashKeyParameter);
			baOutputByteArray = new byte[baByteArrayToEncrypt.length];
			rc4Engine.processBytes(baByteArrayToEncrypt, 0,
					baByteArrayToEncrypt.length, baOutputByteArray, 0);
			rc4Engine.reset();
		} catch (Exception exception) {
			throw new SystemException(exception);
		}
		return baOutputByteArray;
	}

	public static final byte[] gzip(byte baInputByteArray[])
			throws SystemException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			GZIPOutputStream zos = new GZIPOutputStream(baos);
			zos.write(baInputByteArray, 0, baInputByteArray.length);
			zos.close();
		} catch (IOException exception) {
			throw new SystemException(exception);
		}
		return baos.toByteArray();
	}

	public static final byte[] gunzip(byte baInputByteArray[])
			throws SystemException {
		ByteArrayInputStream bais = new ByteArrayInputStream(baInputByteArray);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			GZIPInputStream zis = new GZIPInputStream(bais);
			byte ba[] = new byte[gunzipChunkSize];
			while (zis.available() > 0) {
				int bytesRead = zis.read(ba, 0, gunzipChunkSize);
				if (bytesRead > 0)
					baos.write(ba, 0, bytesRead);
			}
			zis.close();
		} catch (IOException exception) {
			throw new SystemException(exception);
		}
		return baos.toByteArray();
	}

	public String generateHash(byte baInputData[]) throws SystemException {
		String strHash = null;
		try {
			MessageDigest messageDigest = MessageDigest
					.getInstance("MD5", "BC");
			messageDigest.update(baInputData);
			byte baHash[] = messageDigest.digest();
			strHash = byteArrayToHexString(baHash);
		} catch (NoSuchAlgorithmException exception) {
			throw new SystemException(exception);
		} catch (NoSuchProviderException exception) {
			throw new SystemException(exception);
		}
		return strHash;
	}

	public static String byteArrayToHexString(byte in[]) {
		byte ch = 0;
		int i = 0;
		if (in == null || in.length <= 0)
			return null;
		String hexChar[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
				"A", "B", "C", "D", "E", "F" };
		StringBuffer out = new StringBuffer(in.length * 2);
		for (; i < in.length; i++) {
			ch = (byte) (in[i] & 0xf0);
			ch >>>= 4;
			ch &= 0xf;
			out.append(hexChar[ch]);
			ch = (byte) (in[i] & 0xf);
			out.append(hexChar[ch]);
		}

		String rslt = new String(out);
		return rslt;
	}

	private static byte[] createStandardProviderCompatibleHash(byte keyBytes[]) {
		int i = 0;
		byte newKey[];
		if (keyBytes.length > 4) {
			newKey = new byte[MC_KEY_SIZE];
			for (i = 0; i <= 4; i++)
				newKey[i] = keyBytes[i];

			for (i = 5; i < MC_KEY_SIZE; i++)
				newKey[i] = 0;

		} else {
			newKey = new byte[MC_KEY_SIZE];
			for (i = 0; i <= keyBytes.length; i++)
				newKey[i] = keyBytes[i];

			for (i = keyBytes.length + 1; i < MC_KEY_SIZE; i++)
				newKey[i] = 0;

		}
		return newKey;
	}

	private static int MC_KEY_SIZE = 16;
	private static int gunzipChunkSize = 1024;
	private static Logger logger = Logger.getLogger("Crypto");

}