package com.ctb.commands;

class CIMCommandRoundTripMap extends AbstractCIMCommandRoundTrip {	
    
    CIMCommandRoundTripMap(
        String sourceEnv,
        String targetEnv,
        String frameworkCode) {
        super(sourceEnv, targetEnv, frameworkCode);
    }

    public String getCommandName() {
        return ROUNDTRIP_MAP;
    }

}
