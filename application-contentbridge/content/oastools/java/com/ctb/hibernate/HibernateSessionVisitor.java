package com.ctb.hibernate;

import net.sf.hibernate.Session;

/**
 * @author wmli
 */
public interface HibernateSessionVisitor {
	public void visitSession(Session session);
}
