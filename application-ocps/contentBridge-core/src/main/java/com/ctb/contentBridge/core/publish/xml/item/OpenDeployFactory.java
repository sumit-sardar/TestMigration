package com.ctb.contentBridge.core.publish.xml.item;

import com.ctb.contentBridge.core.publish.tools.OpenDeployConfig;
import com.ctb.contentBridge.core.publish.tools.OpenDeployWrapper;

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
