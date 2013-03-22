/*
 * Created on Jan 20, 2004
 */
package com.ctb.contentBridge.core.publish.cprocessor;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.jdom.Element;

import com.ctb.contentBridge.core.domain.Configuration;
import com.ctb.contentBridge.core.exception.SystemException;
import com.ctb.contentBridge.core.publish.bo.ContentPublishBO;
import com.ctb.contentBridge.core.publish.dao.ContentPublishDAO;
import com.ctb.contentBridge.core.publish.dao.ProductConfig;
import com.ctb.contentBridge.core.publish.hibernate.HibernateUtils;
import com.ctb.contentBridge.core.publish.mapping.ItemMap;
import com.ctb.contentBridge.core.publish.mapping.Objectives;
import com.ctb.contentBridge.core.publish.report.AbstractReport;
import com.ctb.contentBridge.core.publish.report.Report;
import com.ctb.contentBridge.core.publish.sofa.ScorableItemConfig;
import com.ctb.contentBridge.core.publish.xml.BuilderUtils;
import com.ctb.contentBridge.core.publish.xml.XMLConstants;
import com.ctb.contentBridge.core.publish.xml.XMLElementProcessor;
import com.ctb.contentBridge.core.publish.xml.XMLElementProcessors;
import com.ctb.contentBridge.core.upload.service.ContentCreatorService;

public class CommandProcessorImportAssessment implements CommandProcessor {

	private final Element rootElement;
	private final Session session;
	private final XMLElementProcessor processor;
	/* public ADSConfig adsConfig; */
	public Configuration config;
	private final Connection conn;

	public CommandProcessorImportAssessment(Objectives objectives,
			ItemMap itemMap, Element rootElement, Connection connection,
			ScorableItemConfig scoringConfig, ProductConfig productConfig,
			boolean doSubtestMedia, ArrayList unicodeList,
			/* ADSConfig adsConfig, */Configuration configuration,
			String maxPanelWidth, String includeAcknowledgment) {
		this.session = HibernateUtils.getSession(connection);
		this.rootElement = rootElement;
		/* this.adsConfig = adsConfig; */
		this.config = configuration;
		this.conn = connection;

		// get the framework code for the xml
		String frameworkCode = BuilderUtils.extractAttributeOptional(
				rootElement, XMLConstants.FRAMEWORK_CODE);
		maxPanelWidth = BuilderUtils.extractAttributeOptional(rootElement,
				XMLConstants.MAXPANELWIDTH);
		this.processor = XMLElementProcessors.initializeAllProcessors(
				rootElement.getName(), frameworkCode, this.session, objectives,
				itemMap, scoringConfig, productConfig, doSubtestMedia,
				unicodeList, /* adsConfig, */connection, maxPanelWidth,
				includeAcknowledgment);
	}

	public Report process() {
		try {
			Transaction transaction = this.session.beginTransaction();
			Report r = this.processor.process(rootElement);
			if (r.isSuccess()) {
				String extTstItemSetId = rootElement.getAttributeValue("ID");

				transaction.commit();

				doTdContentSize((AbstractReport) r, extTstItemSetId,config);
				System.out.println("under report Process");

				ContentCreatorService mvContentCreatorService = new ContentCreatorService();
				mvContentCreatorService.processExtTstItemSetId(
						(AbstractReport) r, config, /*conn,*/ extTstItemSetId);
				/*
				 * doContentSizeFromADS( ( AbstractReport )r,
				 * rootElement.getAttributeValue( "ID" ) );
				 */
			} else
				transaction.rollback();
			return r;
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new SystemException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new SystemException(e.getMessage());
		}
	}

	public void doTdContentSize(AbstractReport r, String extTstItemSetId,Configuration config) {
		try {
			ContentPublishBO.doTdContentSize(extTstItemSetId,config);
		} catch (Exception e) {
			r.setException(e);
		}
	}
	/*
	 * public void doContentSizeFromADS( AbstractReport r, String
	 * ext_tst_item_set_id ) { Connection conn = null; CallableStatement stmt =
	 * null; try { DriverManager.registerDriver(new
	 * oracle.jdbc.driver.OracleDriver()); conn =
	 * DriverManager.getConnection("jdbc:oracle:thin:@" + adsConfig.adsDbHost +
	 * ":1521:" + adsConfig.adsDbSid , adsConfig.adsDbUser,
	 * adsConfig.adsDbPassword ); stmt = conn.prepareCall(
	 * "{call do_oas_td_content_size (?)}"); stmt.setString( 1,
	 * ext_tst_item_set_id ); stmt.execute(); stmt.close(); stmt =
	 * conn.prepareCall( "{call distribute_assessment (?)}"); stmt.setString( 1,
	 * ext_tst_item_set_id ); stmt.execute(); stmt.close(); } catch ( Exception
	 * e ) { r.setException( e ); } if ( conn != null ) { try { conn.close(); }
	 * catch( Exception e){} } }
	 */
}