package com.ctb.contentBridge.core.publish.sofa;


import java.io.File;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang.StringUtils;

import com.ctb.contentBridge.core.publish.tools.Config;

/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Jun 14, 2004
 * Time: 11:53:04 AM
 * To change this template use File | Settings | File Templates.
 */
public class ScorableItemConfig extends Config {

    private List scorableFrameworks = new ArrayList();

    public ScorableItemConfig() {
        scorableFrameworks.add("CTBS4");
    }

    public ScorableItemConfig(File file) {
        load(file);
    }

    protected void readProperties() {
        String[] frameworks =
            StringUtils.split(
                getRequiredProperty(properties, "scorable.frameworks", file),
                ",");
        scorableFrameworks.addAll(Arrays.asList(frameworks));
    }

    public List getScorableFrameworks() {
        return scorableFrameworks;
    }

    public boolean isScorableFramework(String frameworkCode) {
        return scorableFrameworks.contains(frameworkCode);
    }
}
