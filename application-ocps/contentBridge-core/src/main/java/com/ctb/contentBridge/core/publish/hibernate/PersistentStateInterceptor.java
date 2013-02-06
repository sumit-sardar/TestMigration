package com.ctb.contentBridge.core.publish.hibernate;

import java.io.Serializable;
import java.util.Iterator;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Interceptor;
import net.sf.hibernate.type.Type;

/**
 * @author wmli
 */
public class PersistentStateInterceptor implements Interceptor {

    public boolean onLoad(
        Object entity,
        Serializable id,
        Object[] state,
        String[] propertyNames,
        Type[] types)
        throws CallbackException {
        if (entity instanceof Persistent)
             ((Persistent) entity).onLoad();
        return false;

    }

    public boolean onFlushDirty(
        Object entity,
        Serializable id,
        Object[] currentState,
        Object[] previousState,
        String[] propertyNames,
        Type[] types)
        throws CallbackException {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean onSave(
        Object entity,
        Serializable id,
        Object[] state,
        String[] propertyNames,
        Type[] types)
        throws CallbackException {
        if (entity instanceof Persistent)
             ((Persistent) entity).onSave();
        return false;

    }

    public void onDelete(
        Object entity,
        Serializable id,
        Object[] state,
        String[] propertyNames,
        Type[] types)
        throws CallbackException {
        // TODO Auto-generated method stub

    }

    public void preFlush(Iterator entities) throws CallbackException {
        // TODO Auto-generated method stub

    }

    public void postFlush(Iterator entities) throws CallbackException {
        // TODO Auto-generated method stub

    }

    public Boolean isUnsaved(Object entity) {
        if (entity instanceof Persistent) {
            return new Boolean(!((Persistent) entity).isSaved());
        } else {
            return null;
        }
    }

    public int[] findDirty(
        Object entity,
        Serializable id,
        Object[] currentState,
        Object[] previousState,
        String[] propertyNames,
        Type[] types) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object instantiate(Class clazz, Serializable id)
        throws CallbackException {
        // TODO Auto-generated method stub
        return null;
    }

}
