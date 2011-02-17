package com.ctb.pico.examples;

import java.io.File;
import java.sql.Connection;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.cfg.Configuration;

import com.ctb.common.tools.SystemException;
import com.ctb.hibernate.PersistentStateInterceptor;

/**
 * @author wmli
 */
public class HSession {
    private static final String HIBERNATE_CFG_XML = "conf/hibernate.cfg.xml";
    private Session session;

    public HSession(Connection connection) {
        Configuration cfg = new Configuration();
        SessionFactory sessionFactory;
        try {
            sessionFactory =
                cfg
                    .configure(new File(HIBERNATE_CFG_XML))
                    .buildSessionFactory();

        } catch (HibernateException e) {
            throw new SystemException(e.getMessage(), e);
        }

        session =
            sessionFactory.openSession(
                connection,
                new PersistentStateInterceptor());
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

}
