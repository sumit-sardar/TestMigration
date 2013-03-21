package com.ctb.contentBridge.core.publish.hibernate;

import java.io.File;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.ctb.contentBridge.core.exception.SystemException;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.cfg.Configuration;


/**
 * @author wmli
 */
public class HibernateSession {
    public static final String TARGET_CONNECTION = "TARGET";
    public static final String SOURCE_CONNECTION = "SOURCE";

    private static final ThreadLocal sessions = new ThreadLocal();
    /*private static final String HIBERNATE_CFG_XML = "/conf/hibernate.cfg.xml";*/
    private static final String HIBERNATE_CFG_XML = System.getProperty("CONF_PATH") + "hibernate.cfg.xml";

    public static void setUp(Connection conn) {
        setUp(conn, TARGET_CONNECTION);
    }

    public static void setUp(Connection conn, String key) {
        Configuration cfg = new Configuration();
        SessionFactory sessionFactory;
        try {
            sessionFactory =
                cfg
                    .configure(new File(HIBERNATE_CFG_XML))
                    .buildSessionFactory();

        } catch (HibernateException e) {
            throw new SystemException(e.getMessage());
        }

        Session newSession =
            sessionFactory.openSession(conn, new PersistentStateInterceptor());

        if (sessions.get() == null)
            sessions.set(new HashMap());

        ((HashMap) sessions.get()).put(key, newSession);
    }

    public static Session currentSession() {
        return currentSession(TARGET_CONNECTION);
    }

    public static Session currentSession(String key) {
        if (sessions.get() == null)
            return null;

        return (Session) ((HashMap) sessions.get()).get(key);
    }

    public static void closeSession() {
        closeSession(TARGET_CONNECTION);
    }

    public static void closeSession(String key) {
        Map sessionMap = (HashMap) sessions.get();

        Session s = (Session) sessionMap.get(key);
        sessionMap.remove(TARGET_CONNECTION);
        if (s != null) {
            try {
                s.clear();
                s.close();
                s = null;
            } catch (HibernateException e) {
                throw new SystemException(e.getMessage());
            }
        }
    }

    public static void closeAllSessions() {
        if (sessions.get() == null)
            return;

        Map sessionMap = (HashMap) sessions.get();

        for (Iterator iter = sessionMap.keySet().iterator(); iter.hasNext();) {
            String key = (String) iter.next();

            Session s = (Session) sessionMap.get(key);

            if (s != null) {
                try {
                    s.clear();
                    s.close();
                    s = null;
                } catch (HibernateException e) {
                    throw new SystemException(e.getMessage());
                }
            }
        }

        sessionMap.clear();
        sessionMap = null;
        sessions.set(null);
    }

    public static void accept(HibernateSessionVisitor visitor) {
        if (sessions.get() == null)
            return;

        Map sessionMap = (HashMap) sessions.get();

        for (Iterator iter = sessionMap.values().iterator(); iter.hasNext();) {
            Session session = (Session) iter.next();
            visitor.visitSession(session);
        }
    }
}
