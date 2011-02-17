package com.ctb.xmlProcessing.item;

import com.ctb.common.tools.OpenDeployConfig;
import com.ctb.common.tools.OpenDeployWrapper;

public class OpenDeployFactory {

    private OpenDeployFactory() {
    }

    public static OpenDeploy getOpenDeployDummy() {
        return new OpenDeployDummy();
    }

    public static OpenDeploy getOpenDeploy(
        String localImageArea,
        String targetImageArea,
        OpenDeployConfig config) {
        return new OpenDeployWrapper(localImageArea, targetImageArea, config);
    }
}
