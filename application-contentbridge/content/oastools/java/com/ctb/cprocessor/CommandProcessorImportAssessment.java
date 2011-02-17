/*
 * Created on Jan 20, 2004
 */
package com.ctb.cprocessor;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;


import org.jdom.Element;

import com.ctb.common.tools.ADSConfig;
import com.ctb.common.tools.SystemException;
import com.ctb.hibernate.HibernateUtils;
import com.ctb.mapping.ItemMap;
import com.ctb.mapping.Objectives;
import com.ctb.reporting.AbstractReport;
import com.ctb.reporting.Report;

import com.ctb.xmlProcessing.BuilderUtils;
import com.ctb.xmlProcessing.XMLConstants;
import com.ctb.xmlProcessing.XMLElementProcessor;
import com.ctb.xmlProcessing.XMLElementProcessors;

import com.ctb.xmlProcessing.utils.ProductConfig;
import com.ctb.sofa.ScorableItemConfig;

public class CommandProcessorImportAssessment implements CommandProcessor {

    private final Element rootElement;
    private final Session session;
    private final XMLElementProcessor processor;
    public ADSConfig adsConfig;
    
    public CommandProcessorImportAssessment(Objectives objectives, ItemMap itemMap,
            Element rootElement, Connection connection, ScorableItemConfig scoringConfig,
            ProductConfig productConfig, boolean doSubtestMedia, ArrayList unicodeList, 
            ADSConfig adsConfig, String maxPanelWidth, String includeAcknowledgment) {
        this.session = HibernateUtils.getSession(connection);
        this.rootElement = rootElement;
        this.adsConfig = adsConfig;
    
        // get the framework code for the xml
        String frameworkCode = BuilderUtils.extractAttributeOptional(rootElement,
                XMLConstants.FRAMEWORK_CODE);
        maxPanelWidth = BuilderUtils.extractAttributeOptional(rootElement, XMLConstants.MAXPANELWIDTH);
        this.processor = XMLElementProcessors.initializeAllProcessors(rootElement.getName(),
                frameworkCode, this.session, objectives, itemMap, scoringConfig, productConfig,
                doSubtestMedia, unicodeList, adsConfig, maxPanelWidth, includeAcknowledgment);
    }

    public Report process() {
        try {
            Transaction transaction = this.session.beginTransaction();
            Report r = this.processor.process(rootElement);
            if (r.isSuccess())
            {
                transaction.commit();
                doContentSizeFromADS( ( AbstractReport )r, rootElement.getAttributeValue( "ID" ) );
            }
            else
                transaction.rollback();
            return r;
        } catch (HibernateException e) {
            throw new SystemException(e.getMessage(), e);
        }
    }
    
    public void doContentSizeFromADS( AbstractReport r, String ext_tst_item_set_id )
    {
        Connection conn = null;
        CallableStatement stmt = null;
        try 
        {
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            conn =
                DriverManager.getConnection("jdbc:oracle:thin:@" + adsConfig.adsDbHost + ":1521:" + adsConfig.adsDbSid
                        					, adsConfig.adsDbUser, adsConfig.adsDbPassword );
            stmt = conn.prepareCall( "{call do_oas_td_content_size (?)}");
            stmt.setString( 1, ext_tst_item_set_id );
            stmt.execute();
	        stmt.close();
            stmt = conn.prepareCall( "{call distribute_assessment (?)}");
            stmt.setString( 1, ext_tst_item_set_id );
	        stmt.execute();
	        stmt.close();
        } 
        catch ( Exception e ) 
        {
            r.setException( e );
        }   
        if ( conn != null )
        {
            try
            {
                conn.close();
            }
            catch( Exception e){}
        }
    }
}