package com.ctb.utils;
import java.io.File;
import java.sql.Connection;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	private static final SessionFactory sessionFactory;
	static {
		try {
			sessionFactory = new Configuration().configure(new File("hibernate.cfg.xml"))
					.buildSessionFactory();
		} catch (Throwable ex) {
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	public static Session getSession() throws HibernateException {
		return getSessionFactory().openSession();
	}
	public static Connection getConnection() throws HibernateException {
		return getSession().connection();
	}
}
