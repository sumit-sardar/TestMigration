/*
 * Created on May 23, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ctb.contentBridge.core.publish.cprocessor;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import org.jdom.Element;

import com.ctb.contentBridge.core.exception.SystemException;
import com.ctb.contentBridge.core.publish.hibernate.HibernateUtils;
import com.ctb.contentBridge.core.publish.report.AssessmentProcessorReport;
import com.ctb.contentBridge.core.publish.report.DeliverableUnitReport;
import com.ctb.contentBridge.core.publish.report.ItemImportAndMapReport;
import com.ctb.contentBridge.core.publish.report.ItemProcessorReport;
import com.ctb.contentBridge.core.publish.report.Report;
import com.ctb.contentBridge.core.publish.report.ReportFactory;
import com.ctb.contentBridge.core.publish.report.SchedulableUnitReport;
import com.ctb.contentBridge.core.publish.tools.IOUtils;
import com.ctb.contentBridge.core.publish.xml.XMLConstants;
import com.ctb.contentBridge.core.publish.xml.XMLUtils;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

/**
 * @author Wen-Jin_Chang
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class CommandProcessorImportAssessmentFromContentQA implements
		CommandProcessor {
	private final Element rootElement;
	private final Session session;

	/**
     * 
     */
	public CommandProcessorImportAssessmentFromContentQA(Element rootElement_,
			Connection connection) {
		super();
		rootElement = rootElement_;
		this.session = HibernateUtils.getSession(connection);
	}

	public Report processAssessment() {
		CallableStatement stmt = null;
		try {
			Transaction transaction = this.session.beginTransaction();
			AssessmentProcessorReport aAssessmentReport = ReportFactory
					.createAssessmentReport();
			String id = rootElement.getAttributeValue("ID");
			String ProductID = rootElement.getAttributeValue("ProductID");
			aAssessmentReport.setId(id);
			aAssessmentReport.setProductId(Long.valueOf(ProductID));
			try {
				stmt = this.session.connection().prepareCall(
						"{call CTPU_HANDLE_EXT_TST_IDN (?, ?)}");
				stmt.setInt(1, Integer.valueOf(ProductID).intValue());
				stmt.setString(2, id);
				stmt.execute();
				stmt.close();
				aAssessmentReport.setSuccess(true);
				List TSList = XMLUtils
						.getSchedulableUnitInAssessment(rootElement);
				for (int j = 0; j < TSList.size(); j++) {
					SchedulableUnitReport TSReport = ReportFactory
							.createSchedulableUnitReport(false);
					Element SchedulableUnitElement = (Element) TSList.get(j);
					String SchedulableUnitId = SchedulableUnitElement
							.getAttributeValue("ID");
					TSReport.setId(SchedulableUnitId);
					String title = SchedulableUnitElement
							.getAttributeValue("Title");
					TSReport.setSubTestName(title);
					String level = SchedulableUnitElement
							.getAttributeValue("Level");
					TSReport.setSubTestLevel(level);
					List TDList = XMLUtils
							.getDeliverableUnitInSchedulableUnit(SchedulableUnitElement);
					for (int k = 0; k < TDList.size(); k++) {
						DeliverableUnitReport TDReport = ReportFactory
								.createDeliverableUnitReport(true);
						Element DeliverableUnitElement = (Element) TDList
								.get(k);
						String DeliverableUnitId = DeliverableUnitElement
								.getAttributeValue("ID");
						TDReport.setId(DeliverableUnitId);
						title = DeliverableUnitElement
								.getAttributeValue("Title");
						TDReport.setSubTestName(title);
						level = DeliverableUnitElement
								.getAttributeValue("Level");
						TDReport.setSubTestLevel(level);
						String form = DeliverableUnitElement
								.getAttributeValue("Form");
						TDReport.setSubTestForm(form);
						List item_list = CommandProcessorUtility
								.getItemsUnderTD(DeliverableUnitElement);
						Object[] items = (Object[]) item_list.toArray();
						for (int i = 0; i < items.length; i++) {
							ItemImportAndMapReport r = ReportFactory
									.getItemImportAndMapReport();
							Element itemElement = (Element) items[i];
							String itemId = itemElement.getAttributeValue("ID");
							ItemProcessorReport aItemProcessorReport = new ItemProcessorReport();
							aItemProcessorReport.setSuccess(true);
							aItemProcessorReport.setID(itemId);
							aItemProcessorReport.setNewID(itemId);
							aItemProcessorReport.setOperation("map");
							aItemProcessorReport.setSuccess(true);
							r.addItemProcessorReport(aItemProcessorReport);
							r.setSuccess(true);
							TDReport.addSubReport(r);
						}
						TDReport.setSuccess(true);
						TSReport.addSubReport(TDReport);
					}
					TSReport.setSuccess(true);
					aAssessmentReport.addSubReport(TSReport);
				}
			} catch (Exception e) {
				aAssessmentReport.setException(e);
				aAssessmentReport.setSuccess(false);
			}
			if (aAssessmentReport.isSuccess()) {
				writePushInfo(id, aAssessmentReport);
				if (aAssessmentReport.isSuccess()) {
					transaction.commit();
					if (!callPushScript()) {
						aAssessmentReport.setSuccess(false);
						aAssessmentReport
								.setException(new Exception(
										"Perl script failed. Please contact Puru for details."));
					}
				} else
					transaction.rollback();
			} else
				transaction.rollback();
			return aAssessmentReport;
		} catch (HibernateException e) {
			throw new SystemException(e.getMessage());
		}
	}

	public void writePushInfo(String ext_tst_item_set_id,
			AssessmentProcessorReport aAssessmentReport) {
		String sql = "delete from INFA_PARAMETERS where JOB_NAME = 'ADS_ETL' and PARAM_NAME = 'EXT_TST_ITEM_SET_ID'";
		try {
			PreparedStatement pstatement = this.session.connection()
					.prepareStatement(sql);
			pstatement.executeUpdate();
			pstatement.close();
			sql = "insert into INFA_PARAMETERS( JOB_NAME, PARAM_NAME, PARAM_VALUE ) "
					+ "values( 'ADS_ETL', 'EXT_TST_ITEM_SET_ID', '"
					+ ext_tst_item_set_id + "')";
			pstatement = this.session.connection().prepareStatement(sql);
			pstatement.executeUpdate();
			pstatement.close();
		} catch (Exception e) {
			aAssessmentReport.setException(e);
			aAssessmentReport.setSuccess(false);
		}
	}

	public boolean callPushScript() {
		IOUtils.ExecInfo info;
		String[] commandArray = { "ads_etl.pl" };
		try {
			info = IOUtils.exec(commandArray);
			if (info.exitValue == 0)
				return true;
			else
				return false;
		} catch (Exception e) {
			return false;
		}
	}

	public Report process() {
		if (rootElement.getName().equals(XMLConstants.ELEMENT_NAME_ASSESSMENT))
			return processAssessment();
		else
			throw new SystemException("Root element has to be Assessment.");
	}

}
