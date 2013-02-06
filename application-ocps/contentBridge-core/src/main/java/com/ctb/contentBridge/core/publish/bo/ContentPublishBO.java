/**
 * 
 */
package com.ctb.contentBridge.core.publish.bo;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.*;

import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import com.ctb.contentBridge.core.domain.Configuration;
import com.ctb.contentBridge.core.exception.SystemException;
import com.ctb.contentBridge.core.publish.dao.ContentPublishDAO;
import com.ctb.contentBridge.core.util.AppendXML;
import com.ctb.contentBridge.core.util.ConnectionUtil;
import com.ctb.contentBridge.core.util.PublishCommon;
import com.ctb.contentBridge.core.util.Validation;
import com.ctb.contentBridge.core.util.XMLParsing;
import com.ctb.contentBridge.core.util.crypto.Crypto;

/**
 * @author
 * 
 */
public class ContentPublishBO {
	public static void publishItem(String xmlFile)
			throws SystemException {
		XMLParsing xmlParsing = new XMLParsing();
		String XMLFILE = xmlFile;
		AppendXML appendXML;
		Crypto crypto = new Crypto();
		Connection conn = null;
		try {
			String sPropFilePath = System.getProperty("PROPERTIES_FILE_PATH");
			Configuration configuration = new Configuration();
			configuration.load(new File(sPropFilePath));
			conn = ConnectionUtil.getADSConnection(configuration);
			
			String flag = xmlParsing.set_Attribute_values(xmlFile);

			if (!"Valid".equals(flag)) {
				throw new SystemException("XML parsing failed.");
			}

			ArrayList abtValues = xmlParsing.get_Attribute_Values();
			String obItemPkgId = ContentPublishDAO.getObItemPkgId(conn,
					(String) abtValues.get(0));

			if (obItemPkgId != null && obItemPkgId.length() > 0) {
				Integer intCheckState = ContentPublishDAO.checkStateId(conn,
						new Integer(obItemPkgId), "Item");

				if (intCheckState != 1) {
					throw new SystemException(
							"Item cannot be re-published as it is being used by the subtest that is in the state of locked.");
				}
				String strAsmtState = ContentPublishDAO.checkForStateId(conn,
						obItemPkgId);
				if (!"UNLOCK".equals(strAsmtState)) {
					throw new SystemException(
							"Item cannot be re-published as state is locked.");
				}
			}

			abtValues = PublishCommon.removeDuplicate(abtValues, "item");
			Validation.validationOfXMLData(abtValues, "item");
			String xsltPath = System.getProperty("ITM_XSLT_FILE_PATH")
					+ "Item_Pkg.xslt";

			String strCfgFileURL = PublishCommon.getXSLTXSDContent(new File(
					xsltPath));
			if (strCfgFileURL == null || strCfgFileURL.length() == 0) {
				throw new SystemException("Error in parsing xslt document");
			}

			String sourcePubId = ContentPublishDAO.getSourcePublishId(conn);
			if (sourcePubId == null || sourcePubId.length() == 0) {
				throw new SystemException("Parameters are not defined in table");
			}

			xmlFile = PublishCommon.transforms(xmlFile, strCfgFileURL);
			if (xmlFile == null || xmlFile.length() == 0) {
				throw new SystemException(
						"Error has occurred transforming Item LML using xslt.");
			}

			String strChk = ContentPublishDAO.assetCheck(conn, abtValues);
			if (!(strChk.equals("Found"))) {
				throw new SystemException(strChk);
			}

			ArrayList encdKeyIDs = ContentPublishDAO.getKeyIdEncr(conn,
					(String) abtValues.get(1), sourcePubId);
			if (encdKeyIDs == null || encdKeyIDs.isEmpty()) {
				throw new SystemException(
						"Sequence is not defined in database.");
			}

			ArrayList finalValues = new ArrayList();
			finalValues.add(abtValues.get(0));
			finalValues.add(encdKeyIDs.get(0));
			finalValues.add(XMLFILE);

			String strFlag = ContentPublishDAO.checkItem(conn,
					(String) abtValues.get(0));
			appendXML = new AppendXML(xmlFile);
			String htmlWidget = appendXML.parseWithDOMHtmlWidget();

			String hash = null;
			if ("Insert".equals(strFlag)) {
				System.out.println("Insert Item...");
				appendXML = new AppendXML(xmlFile);
				obItemPkgId = ContentPublishDAO.getMaxObItmId(conn);
				if (obItemPkgId == null || obItemPkgId.length() == 0) {
					throw new SystemException(
							"Sequence is not defined in database.");
				}
				String strAppendedXML = null;
				if ("true".equals(htmlWidget)) {
					strAppendedXML = ContentPublishDAO
							.createAppendedXMLWithPakage(conn, abtValues,
									obItemPkgId, appendXML);
				} else {
					strAppendedXML = ContentPublishDAO.createAppendedXML(conn,
							abtValues, obItemPkgId, appendXML);
				}

				byte outData[] = (byte[]) null;
				outData = new byte[strAppendedXML.length()];
				outData = crypto.encrypt((String) encdKeyIDs.get(1),
						strAppendedXML, "UTF-8", true, false);

				ContentPublishDAO.insertItem(conn, finalValues);
				hash = crypto.generateHash(outData);
				ContentPublishDAO.insertObItemPkg(conn, obItemPkgId,
						(String) abtValues.get(0), hash, outData);
				decrypt(crypto, (String) encdKeyIDs.get(1), hash, outData);
				ContentPublishDAO.insertAssetItemMap(conn, abtValues);
			} else if ("Update".equals(strFlag)) {
				System.out.println("Update Item...");
				appendXML = new AppendXML(xmlFile);
				String strAppendedXML = null;
				if ("true".equals(htmlWidget)) {
					strAppendedXML = ContentPublishDAO
							.createAppendedXMLWithPakage(conn, abtValues,
									obItemPkgId, appendXML);
				} else {
					strAppendedXML = ContentPublishDAO.createAppendedXML(conn,
							abtValues, obItemPkgId, appendXML);
				}
				String decXml=null;
				byte outData[] = (byte[]) null;
				outData = new byte[strAppendedXML.length()];
				outData = crypto.encrypt((String) encdKeyIDs.get(1),
						strAppendedXML, "UTF-8", true, false);

				System.out.println("ContentPublishDAO.updateItem...");
				ContentPublishDAO.updateItem(conn, finalValues);
				decXml=ContentPublishDAO.getDecryptedItemXml(conn, finalValues.get(0).toString());
				
				
				Pattern pattern = Pattern.compile("id=\\\"widget\\d*");
				Matcher matcher =pattern.matcher(decXml);
				
			     String decTemp1Xml=matcher.replaceAll("id=\"");
			     Matcher matcherBSave =pattern.matcher(strAppendedXML);
			     String decTemp2Xml=matcherBSave.replaceAll("id=\"");
				if (decTemp1Xml.trim().equals(decTemp2Xml.trim())) {
					System.out.println("Content unchanged...");
					hash = ContentPublishDAO.getHashItemPkg(conn, finalValues
							.get(0).toString());
					System.out.println("old hash-->>"+hash);
					ContentPublishDAO.updateObItemPkg(conn,
							(String) abtValues.get(0), hash, outData);
					// decryptInputXml(crypto, (String) encdKeyIDs.get(1), hash,
					// outData);
				} else {
					System.out.println("Content changed...");
					hash = crypto.generateHash(outData);
					System.out.println("new hash-->>"+hash);
					System.out.println("ContentPublishDAO.updateObItemPkg...");
					ContentPublishDAO.updateObItemPkg(conn,
							(String) abtValues.get(0), hash, outData);
					decrypt(crypto, (String) encdKeyIDs.get(1), hash, outData);
				}
				/*hash = crypto.generateHash(outData);
				System.out.println("ContentPublishDAO.updateObItemPkg...");
				ContentPublishDAO.updateObItemPkg(conn,
						(String) abtValues.get(0), hash, outData);
				decrypt(crypto, (String) encdKeyIDs.get(1), hash, outData);*/
				System.out.println("ContentPublishDAO.updateAssetItemMap...");
				ContentPublishDAO.updateAssetItemMap(conn, abtValues);
			}

			conn.commit(); 
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException ex) {
				throw new SystemException(ex);
			}
			throw new SystemException(e);
		} finally {
			XMLParsing.arrList = new ArrayList();
			ConnectionUtil.closeADSConnection();
		}
	}

	public static void publishSubtest(String xmlFile)
			throws SystemException {
		XMLParsing xmlParsing = new XMLParsing();
		String XMLFILE = xmlFile;
		AppendXML appendXML;
		Crypto crypto = new Crypto();
		Connection conn = null;
		try {
			String sPropFilePath = System.getProperty("PROPERTIES_FILE_PATH");
			Configuration configuration = new Configuration();
			configuration.load(new File(sPropFilePath));
			conn = ConnectionUtil.getADSConnection(configuration);
			
			String flag = xmlParsing.set_Attribute_values(xmlFile);

			if (!"Valid".equals(flag)) {
				throw new SystemException("XML parsing failed.");
			}

			ArrayList abtValues = xmlParsing.get_Attribute_Values();
			String asmtId = (String) abtValues.get(0);
			ArrayList itemIdList = new ArrayList();
			Integer obAsmtId = 0;
			obAsmtId = ContentPublishDAO.getObAsmtId(conn, asmtId);

			if (obAsmtId != null && obAsmtId != 0) {
				Integer intCheckState = ContentPublishDAO.checkStateId(conn,
						obAsmtId, "Subtest");

				if (intCheckState != 1) {
					
					System.out.println("Can not republish");
					throw new SystemException(
							"Subtest cannot be re-published as state of sub-test is locked.");
				}
			}

			for (int i = 1; i < abtValues.size(); i++) {
				itemIdList.add(abtValues.get(i));
			}
			abtValues = new ArrayList(itemIdList);
			ArrayList arr = new ArrayList();
			arr.add(asmtId);

			Validation.validationOfXMLData(abtValues, "Subtest");
			abtValues = PublishCommon.removeDuplicate(abtValues, "Subtest");

			String strItemCheck = ContentPublishDAO.itemCheck(conn, itemIdList);
			if (!"Found".equals(strItemCheck)) {
				throw new SystemException("Parameters are not defined in table");
			}

			String sourcePubId = ContentPublishDAO.getSourcePublishId(conn);
			if (sourcePubId == null || sourcePubId.length() == 0) {
				throw new SystemException("Parameters are not defined in table");
			}

			String random = ContentPublishDAO.randomGeneration(2000);
			String keyRing = ContentPublishDAO.getAsmtKeyRing(conn, itemIdList);
			byte bXml[] = ContentPublishDAO.encryptedXML(keyRing, random);
			Crypto cc = new Crypto();
			String newXml = ContentPublishDAO.getAsmtManifest(conn, abtValues,
					itemIdList, XMLFILE);
			byte b[] = ContentPublishDAO.encryptedXML(newXml, random);
			String hash = cc.generateHash(b);
			ArrayList obItemPkgId = new ArrayList();
			obItemPkgId = ContentPublishDAO.getObItemId(conn, itemIdList);
			String strFlag = ContentPublishDAO.checkAsmt(conn, asmtId);

			if ("Update".equals(strFlag)) {
				System.out.println("Update subtest...");
				System.out.println("ContentPublishDAO.updateAssessment...");
				ContentPublishDAO.updateAssessment(conn, asmtId, XMLFILE);
				System.out.println("ContentPublishDAO.updateObAsmt...");
				ContentPublishDAO.updateObAsmt(conn, obAsmtId, asmtId, random,
						hash, bXml, newXml, b);
				System.out.println("ContentPublishDAO.updateAsmtItemMap...");
				ContentPublishDAO.updateAsmtItemMap(conn, asmtId, itemIdList);
				System.out.println("ContentPublishDAO.updateObItemPkg...");
				ContentPublishDAO.updateObAsmtItemMap(conn, obAsmtId,
						obItemPkgId);
			} else if ("Insert".equals(strFlag)) {
				System.out.println("Insert subtest...");
				Integer intObAsmtId = ContentPublishDAO.getMaxObAsmtId(conn);
				if (intObAsmtId == 0) {
					throw new SystemException(
							"Sequence is  not defined in database");
				}
				ContentPublishDAO.insertAssessment(conn, asmtId, XMLFILE);
				ContentPublishDAO.insertAsmtItemMap(conn, asmtId, itemIdList);
				ContentPublishDAO.insertObAsmt(conn, intObAsmtId, asmtId,
						random, hash, bXml, newXml, b);
				ContentPublishDAO.insertObAsmtItemMap(conn, intObAsmtId,
						obItemPkgId);
			}

			 conn.commit();
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException ex) {
				throw new SystemException(ex);
			}
			throw new SystemException(e);
		} finally {
			XMLParsing.arrList = new ArrayList();
			ConnectionUtil.closeADSConnection();
		}
	}

	public static void publishAsset(String xmlFile)
			throws SystemException {
		XMLParsing xmlParsing = new XMLParsing();
		String XMLFILE = xmlFile;
		AppendXML appendXML;
		Crypto crypto = new Crypto();
		Connection conn = null;
		try {
			String sPropFilePath = System.getProperty("PROPERTIES_FILE_PATH");
			Configuration configuration = new Configuration();
			configuration.load(new File(sPropFilePath));
			conn = ConnectionUtil.getADSConnection(configuration);
			
			String flag = xmlParsing.set_Attribute_values(xmlFile);

			if (!"Valid".equals(flag)) {
				throw new SystemException("XML parsing failed.");
			}

			ArrayList arrList = xmlParsing.get_Attribute_Values();
			Validation.validationOfXMLData(arrList, "Asset");
			System.out.println("image xml  request->> "+xmlFile);
			byte buffer[] = PublishCommon.ReadFile(arrList.get(2));
			System.out.println(arrList.get(2) + "has been read...");
			String strFlag = ContentPublishDAO.checkAsset(conn, (String)arrList.get(0));
			if ("Update".equals(strFlag)) {
				System.out.println("Update Asset...");
				ContentPublishDAO.updateAsset(conn, arrList, buffer);
			} else if ("Insert".equals(strFlag)) {
				System.out.println("Insert Asset...");
				ContentPublishDAO.insertAsset(conn, arrList, buffer);
			}
			conn.commit(); 
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException ex) {
				throw new SystemException(ex);
			}
			throw new SystemException(e);
		} finally {
			XMLParsing.arrList = new ArrayList();
			ConnectionUtil.closeADSConnection();
		}
	}
	
	public static void doTdContentSize(String extTstItemSetId,Configuration config) {
		Connection conn = null;
		try {
			String sPropFilePath = System.getProperty("PROPERTIES_FILE_PATH");
			Configuration configuration = new Configuration();
			configuration.load(new File(sPropFilePath));
			conn = ConnectionUtil.getADSConnection(configuration);
			ContentPublishDAO.doTdContentSize(conn, extTstItemSetId,config);
		} catch (Exception e) {
			throw new SystemException(e);
		} finally {
			ConnectionUtil.closeADSConnection();
		}
	}

	public static void decrypt(Crypto avCrypto, String key, String hash,
			byte outData[]) {
		try {
			// String key = "11";
			byte[] decryptvalue = avCrypto.checkHashAndDecrypt(key, hash,
					outData, true, false);
			SAXBuilder saxBuilder = new SAXBuilder();
			org.jdom.Document itemDoc = saxBuilder
					.build(new ByteArrayInputStream(decryptvalue));
			XMLOutputter aXMLOutputter = new XMLOutputter();
			StringWriter aStringWriter = new StringWriter();
			aXMLOutputter.output(itemDoc, aStringWriter);
			System.out.println("KEY:HASH==>" + key + ":" + hash);
			// System.out.println("Again decrypted Item LML ==>"+(aStringWriter.getBuffer().toString()));
		} catch (Exception e) {
			System.err.println(e);
		}
	}
	public static void decryptInputXml(Crypto avCrypto, String key, String hash,
			byte outData[]) {
		try {
			// String key = "11";
			byte[] decryptvalue = avCrypto.checkHashAndDecryptForXml(key, hash,
					outData, true, false);
			SAXBuilder saxBuilder = new SAXBuilder();
			org.jdom.Document itemDoc = saxBuilder
					.build(new ByteArrayInputStream(decryptvalue));
			XMLOutputter aXMLOutputter = new XMLOutputter();
			StringWriter aStringWriter = new StringWriter();
			aXMLOutputter.output(itemDoc, aStringWriter);
			System.out.println("KEY:HASH==>" + key + ":" + hash);
			// System.out.println("Again decrypted Item LML ==>"+(aStringWriter.getBuffer().toString()));
		} catch (Exception e) {
			System.err.println(e);
		}
	}
	
}