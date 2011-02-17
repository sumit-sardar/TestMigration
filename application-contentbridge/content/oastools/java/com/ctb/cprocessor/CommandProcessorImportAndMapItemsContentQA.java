/*
 * Created on May 24, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ctb.cprocessor;

import java.sql.CallableStatement;
import java.sql.Connection;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.jdom.Element;

import com.ctb.common.tools.SystemException;
import com.ctb.hibernate.HibernateUtils;
import com.ctb.reporting.ItemImportAndMapReport;
import com.ctb.reporting.ItemProcessorReport;
import com.ctb.reporting.ItemSetReport;
import com.ctb.reporting.Report;
import com.ctb.reporting.ReportFactory;


/**
 * @author Wen-Jin_Chang
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CommandProcessorImportAndMapItemsContentQA implements CommandProcessor
{
    private final Element rootElement;
    private final Session session;

    public CommandProcessorImportAndMapItemsContentQA( Element rootElement_, Connection connection ) 
    {
        super();
        rootElement = rootElement_;
        this.session = HibernateUtils.getSession(connection);
    }
    
    public Report process() 
    {
        CallableStatement stmt = null;
        try {
            Transaction transaction = this.session.beginTransaction();
            ItemSetReport report = ReportFactory.createItemSetReport(true);
            Element[] items = CommandProcessorUtility.getItems( rootElement );
            for (int i = 0; i < items.length; i++) 
            {
                ItemImportAndMapReport r = ReportFactory.getItemImportAndMapReport();
                Element itemElement = items[ i ];
                String id = itemElement.getAttributeValue( "ID" );           
                ItemProcessorReport aItemProcessorReport = null;;
                try
                {
                    aItemProcessorReport = new ItemProcessorReport();
                    aItemProcessorReport.setSuccess(true);
                    aItemProcessorReport.setID( id );
                    aItemProcessorReport.setNewID( id );
                    aItemProcessorReport.setOperation( "map" );
                    
                    stmt = this.session.connection().prepareCall( "{call Ctpu_Push_Item_Idn (?)}");
    		        stmt.setString( 1, id );
    		        stmt.execute();
    		        stmt.close(); 
    		        report.setSuccess( true );
                } 
                catch ( Exception e )
                {
                    aItemProcessorReport.setException(e);
                    aItemProcessorReport.setSuccess( false );
                }  
                r.addItemProcessorReport( aItemProcessorReport );
                report.addSubReport( r );
            }           
            if (report.isSuccess())
                transaction.commit();
            else
                transaction.rollback();
            return report;
        } catch (HibernateException e) {
            throw new SystemException(e.getMessage(), e);
        }
    }
}
