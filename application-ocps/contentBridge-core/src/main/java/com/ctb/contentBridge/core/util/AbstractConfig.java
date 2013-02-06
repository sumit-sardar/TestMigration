package com.ctb.contentBridge.core.util;


import java.util.*;
import java.net.URL;
import java.net.MalformedURLException;

import com.ctb.contentBridge.core.exception.BusinessException;

/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Jun 3, 2004
 * Time: 10:25:15 AM
 * To change this template use File | Settings | File Templates.
 */
abstract public class AbstractConfig {

    protected Properties props;
    private Map requiredKeys = new HashMap();
    private String configName;

    protected AbstractConfig(String configName, Properties props, Map requiredKeys) {
        this.props = props;
        this.configName = configName;
        if (requiredKeys != null)
            this.requiredKeys = requiredKeys;
        validatePropsAgainstKeys(this.props,this.requiredKeys);
    }

    protected AbstractConfig(String configName, Properties props) {
        this(configName,props,null);
    }

    protected AbstractConfig(String configName) {
        this.configName = configName;
        props = getDefaultProperties();
        requiredKeys = getRequiredKeys();
        validatePropsAgainstKeys(this.props,this.requiredKeys);
    }

    abstract protected Properties getDefaultProperties();
    abstract protected Map getRequiredKeys();

    public void setProperties(Properties props) {
        validatePropsAgainstKeys(props,requiredKeys);
        this.props = props;
    }

    protected void setPropertiesAndKeys(Properties props, Map requiredKeys) {
        validatePropsAgainstKeys(props,requiredKeys);
        this.props = props;
    }

    private void validatePropsAgainstKeys(Properties props, Map requiredKeys) {
        for (Iterator iter = requiredKeys.keySet().iterator();iter.hasNext();) {
            String key = (String)iter.next();
            if (props.getProperty(key) == null)
                throw new BusinessException("Required key " + key + " not set for " + configName);
        }
    }

    protected boolean getBooleanProperty(String key) {
        return new Boolean(props.getProperty(key, (String )requiredKeys.get(key))).booleanValue();
    }

    protected List getListProperty(String key) {
        List list = new ArrayList();
        String commaDelimitedList = props.getProperty(key,(String )requiredKeys.get(key));
        StringTokenizer tokenizer = new StringTokenizer(commaDelimitedList,",");
        while (tokenizer.hasMoreTokens())
            list.add(tokenizer.nextToken());
        return list;
    }

    protected void setBooleanProperty(String key,boolean bool) {
        props.setProperty(key,new Boolean(bool).toString());
    }
    protected void setListProperty(String key, List list) {
        StringBuffer buf = new StringBuffer();
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            String current = (String)iterator.next();
            buf.append(current);
            if (iterator.hasNext())
                buf.append(",");
        }
        props.setProperty(key,buf.toString());
    }

    protected String getProperty(String key) {
        return props.getProperty(key,(String)requiredKeys.get(key));
    }

    protected String getProperty(String key, String defaultValue) {
        return props.getProperty(key,defaultValue);
    }

    protected void setProperty(String key, String value) {
        props.setProperty(key,value);
    }

    protected String getFileNameOnClassPath(String key) {
        String value = getProperty(key);
        if (value.startsWith("/"))
            return value;
        return "/" + value;
    }

    protected void setFileNameOnClassPath(String key, String value) {
        if (value.startsWith("/"))
            value = value.substring(1);
        props.setProperty(key,value);
    }

    protected URL getURLProperty(String key) {
        String urlString = props.getProperty(key,(String)requiredKeys.get(key));
        try {
            return new URL(urlString);
        } catch (MalformedURLException e) {
            throw new BusinessException(e.getMessage());
        }
    }

    protected void setURLProperty(String key, URL value) {
        String urlString = value.toExternalForm();
        setProperty(key,urlString);
    }
}
