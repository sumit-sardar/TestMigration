// Decompiled by DJ v3.6.6.79 Copyright 2004 Atanas Neshkov  Date: 2/22/2010 1:08:14 PM
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   AdsCommon.java

package com.ctb.contentBridge.core.util;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.log4j.*;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import com.ctb.contentBridge.core.exception.SystemException;

// Referenced classes of package com.stgglobal.ads.common:
//            ExceptionHandler

public class PublishCommon {

	public static String transforms(String strXML, String xsltFile)
			throws SystemException {
		StringWriter sw = new StringWriter();
		try {
			StreamSource source = new StreamSource(new StringReader(strXML));
			StreamSource stylesource = new StreamSource(new StringReader(
					xsltFile));
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer(stylesource);
			transformer.setOutputProperty("method", "xml");
			transformer.setOutputProperty("encoding", "UTF-8");
			StreamResult result = new StreamResult(sw);
			transformer.transform(source, result);
		} catch (Exception e) {
			throw new SystemException(e);
		}
		return sw.toString();
	}

	public static String randomPW() {
		int myLength = (int) Math.floor(Math.random() * 26D) + 6;
		String pwd = "";
		for (int k = 0; k < myLength; k++) {
			char c = (char) ((int) Math.floor(Math.random() * 3D) + 49);
			switch (c) {
			case 49: // '1'
				pwd = pwd + (char) ((int) Math.floor(Math.random() * 9D) + 48);
				break;

			case 50: // '2'
				pwd = pwd + (char) ((int) Math.floor(Math.random() * 25D) + 65);
				break;

			case 51: // '3'
				pwd = pwd + (char) ((int) Math.floor(Math.random() * 25D) + 97);
				break;
			}
		}

		return pwd;
	}

	public static String createXMLResposnse(String status, String statuscode,
			String strMsg, String methodName) throws SystemException {
		String xmlFile1 = "";
		try {
			Element rElement = new Element("ads_publish_response", "xsi",
					"http://www.w3.org/2001/XMLSchema-instance");
			rElement.setAttribute("method", methodName);
			Document jDOMDoc = new Document(rElement);
			Element c1_Element = new Element("response");
			c1_Element.setAttribute("method", methodName);
			Element c1_1Element = new Element("status");
			if (status.equals("ok")) {
				c1_Element.setAttribute("status", status);
				c1_1Element.setAttribute("status_code", statuscode);
				Element c1_2Element = new Element("msg");
				c1_2Element.setText(strMsg);
				c1_Element.addContent(c1_1Element);
				c1_Element.addContent(c1_2Element);
			} else if (status.equals("error")) {
				c1_Element.setAttribute("status", status);
				c1_1Element.setAttribute("status_code", statuscode);
				Element c1_2Element = new Element("msg");
				c1_2Element.setText(strMsg);
				c1_Element.addContent(c1_1Element);
				c1_Element.addContent(c1_2Element);
			}
			rElement.addContent(c1_Element);
			xmlFile1 = (new XMLOutputter()).outputString(jDOMDoc);
		} catch (Exception e) {
			throw new SystemException(e);
		}
		return xmlFile1;
	}

	public static ArrayList removeDuplicate(ArrayList arrList, String flag) {
		int i = 0;
		if (flag.equals("item"))
			i = 2;
		for (; i < arrList.size(); i++) {
			for (int j = i + 1; j < arrList.size();)
				if (((String) arrList.get(i)).equals(arrList.get(j)))
					arrList.remove(j);
				else
					j++;

		}

		return arrList;
	}

	public static String getXSLTXSDContent(File file) throws SystemException {
		String strXsltXML = "";
		try {
			/*
			 * InputStream is = new FileInputStream("./WebContent/WEB-INF/" +
			 * fileName);
			 */
			InputStream is = new FileInputStream(file);
			ByteArrayOutputStream baos = new ByteArrayOutputStream(16384);
			byte tempBa[] = new byte[1024];
			for (int read = 0; read >= 0;) {
				read = is.read(tempBa);
				if (read > 0)
					baos.write(tempBa, 0, read);
			}

			strXsltXML = baos.toString("UTF-8");
		} catch (Exception exception) {
			throw new SystemException(exception);
		}
		return strXsltXML;
	}

	public static String ReadFile(String filePath) {
		String strFileContent = null;
		try {
			FileInputStream fis = new FileInputStream(filePath);
			FileChannel fc = fis.getChannel();
			int size = (int) fc.size();
			ByteBuffer buf = ByteBuffer.allocate(size);
			fc.read(buf);
			fc.close();
			byte bArr[] = buf.array();
			strFileContent = new String(bArr, 0, bArr.length);
		} catch (FileNotFoundException e) {
			throw new SystemException(e);
		} catch (IOException e) {
			throw new SystemException(e);
		}
		return strFileContent;
	}

	public static byte[] ReadFile(Object fileName) throws Exception {
		byte buffer[] = (byte[]) null;
		try {
			File binaryFile = new File(fileName.toString());
			System.out.println("fileName.toString():	" + binaryFile.getAbsolutePath());
			FileInputStream instream = new FileInputStream(binaryFile);
			buffer = new byte[(int) binaryFile.length()];
			DataInputStream d_in = new DataInputStream(instream);
			d_in.readFully(buffer);
		} catch (Exception exception) {
			throw new SystemException(exception);
		}
		return buffer;
	}
}