package com.ctb.contentBridge.core.publish.command;

abstract class AbstractCIMCommandRoundTrip extends AbstractCIMCommand {
    private final String sourceEnvironment;
    private final String targetEnvironment;
    private final String frameworkCode;

    AbstractCIMCommandRoundTrip(
        String sourceEnv,
        String targetEnv,
        String frameworkCode) {
        super(targetEnv);
        this.sourceEnvironment = sourceEnv;
        this.targetEnvironment = targetEnv;
        this.frameworkCode = frameworkCode;
    }

    public String getTargetEnvironment() {
        return targetEnvironment;
    }

    String getFrameworkCode() {
        return frameworkCode;
    }

    String getSourceEnvironment() {
        return sourceEnvironment;
    }

}
