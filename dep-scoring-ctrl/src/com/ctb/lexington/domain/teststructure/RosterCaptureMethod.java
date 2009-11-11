package com.ctb.lexington.domain.teststructure;

public final class RosterCaptureMethod extends StringConstant {
    public static final RosterCaptureMethod ONLINE = new RosterCaptureMethod("ON", "Online");
    public static final RosterCaptureMethod OFFLINE = new RosterCaptureMethod("OF", "Offline");
    public static final RosterCaptureMethod MIXED = new RosterCaptureMethod("MX", "Mixed");

    private RosterCaptureMethod(final String code, final String description) {
        super(code, description);
    }
}