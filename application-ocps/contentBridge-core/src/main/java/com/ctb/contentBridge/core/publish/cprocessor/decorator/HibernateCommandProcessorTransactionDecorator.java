package com.ctb.contentBridge.core.publish.cprocessor.decorator;

import java.sql.Connection;

import com.ctb.contentBridge.core.exception.SystemException;
import com.ctb.contentBridge.core.publish.cprocessor.CommandProcessor;
import com.ctb.contentBridge.core.publish.hibernate.HibernateSession;
import com.ctb.contentBridge.core.publish.hibernate.HibernateSessionVisitor;
import com.ctb.contentBridge.core.publish.hibernate.HibernateUtils;
import com.ctb.contentBridge.core.publish.report.Report;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;


/**
 * @author wmli
 */
public class HibernateCommandProcessorTransactionDecorator
    extends AbstractTransactionDecorator
    implements CommandProcessor {

    CommandProcessor nestedProcessor;

    public HibernateCommandProcessorTransactionDecorator(
        CommandProcessor nestedProcessor,
        Connection connection) {
        super(connection);
        HibernateUtils.getSession(connection);
        this.nestedProcessor = nestedProcessor;
    }

    public Report process() {
        Report report = null;
        try {
            report = nestedProcessor.process();

            if (report.isSuccess()) {
                HibernateSession.accept(new HibernateSessionVisitor() {
                    public void visitSession(Session session) {
                        try {
//                            System.out.println("Flush Session...");
                            session.flush();
                        } catch (HibernateException e) {
                            throw new SystemException(e.getMessage());
                        }
                    }
                });
            }

        } finally {
            HibernateSession.closeAllSessions();
        }

        return report;
    }
}
