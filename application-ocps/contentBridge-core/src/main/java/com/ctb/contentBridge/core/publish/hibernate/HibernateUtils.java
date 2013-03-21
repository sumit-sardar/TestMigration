package com.ctb.contentBridge.core.publish.hibernate;

import java.io.Serializable;
import java.sql.Connection;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

/**
 * @author wmli
 */
public class HibernateUtils {
    /*private static final String HIBERNATE_CFG_XML = "conf/hibernate.cfg.xml";*/
	private static final String HIBERNATE_CFG_XML = System.getProperty("CONF_PATH") + "conf/hibernate.cfg.xml";

    public static Session getSession(Connection conn) {
        if (HibernateSession.currentSession() == null) {
            HibernateSession.setUp(conn);
        }

        return HibernateSession.currentSession();
    }

    public static Session getSession(Connection conn, String key) {
        if (HibernateSession.currentSession(key) == null) {
            HibernateSession.setUp(conn, key);
        }

        return HibernateSession.currentSession(key);
    }

    public static void safeDelete(
        Session session,
        Class recordClass,
        Serializable id)
        throws HibernateException {
        Object record = session.get(recordClass, id);
        if (record != null)
            session.delete(record);
    }
}
