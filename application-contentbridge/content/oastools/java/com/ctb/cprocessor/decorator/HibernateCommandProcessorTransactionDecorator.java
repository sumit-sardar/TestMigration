package com.ctb.cprocessor.decorator;

import java.sql.Connection;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import com.ctb.common.tools.SystemException;
import com.ctb.cprocessor.CommandProcessor;
import com.ctb.hibernate.HibernateSession;
import com.ctb.hibernate.HibernateSessionVisitor;
import com.ctb.hibernate.HibernateUtils;
import com.ctb.reporting.Report;

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
                            throw new SystemException(e.getMessage(), e);
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
