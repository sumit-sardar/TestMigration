package com.ctb.util;

import java.util.Properties;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.io.FileInputStream;

/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Sep 2, 2004
 * Time: 10:41:38 AM
 * To change this template use File | Settings | File Templates.
 */
public class OnlineCRConfig extends AbstractConfig {

    public static final String CONFIG_NAME = "Online CR Configuration";
    private static final Map REQUIRED_KEYS = new HashMap();
    public static final String ONLINE_CR_TEMPLATES = "online.cr.templates";
    public static final String ONLINE_CR_TEMPLATES_DEFAULT = "CR3,CR4";
    public static final String CONFIG_FILE_NAME = "conf/global.properties";

    static {
        REQUIRED_KEYS.put(ONLINE_CR_TEMPLATES,ONLINE_CR_TEMPLATES_DEFAULT);
    }

    public OnlineCRConfig(Properties props) {
        super(CONFIG_NAME, props, REQUIRED_KEYS);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public OnlineCRConfig() {
        super(CONFIG_NAME);
    }

    protected Properties getDefaultProperties() {
        props = new Properties();
        try {
            props.load(new FileInputStream(CONFIG_FILE_NAME));
        }
        catch (Exception e) {
            props.clear();
            props.put(ONLINE_CR_TEMPLATES,ONLINE_CR_TEMPLATES_DEFAULT);
        }
        return props;
    }

    protected Map getRequiredKeys() {
        return REQUIRED_KEYS;
    }

    public List getOnlineCRTemplates() {
        return getListProperty(ONLINE_CR_TEMPLATES);
    }
}
