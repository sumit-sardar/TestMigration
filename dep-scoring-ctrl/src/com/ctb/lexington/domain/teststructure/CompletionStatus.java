package com.ctb.lexington.domain.teststructure;

import java.util.HashMap;

import com.ctb.lexington.util.SafeHashMap;

/**
 * Enumeration of test completion statuses
 * 
 * @author Mark Kamiya
 * @version $Id$
 */
public final class CompletionStatus extends StringConstant {
    private static final HashMap ALL_STATUSES = new SafeHashMap(String.class, CompletionStatus.class);

    public static final CompletionStatus IN_PROGRESS = new CompletionStatus("IP", "In Progress", false);
    public static final CompletionStatus SCHEDULED = new CompletionStatus("SC", "Scheduled", false);
    public static final CompletionStatus INCOMPLETE = new CompletionStatus("IC", "Incomplete", false);
    // this happens due to interruption or network problem during subtest
    public static final CompletionStatus SYSTEM_STOP = new CompletionStatus("IN", "System Stop", false);

    public static final CompletionStatus NOT_TAKEN = new CompletionStatus("NT", "Not Taken", true);
    public static final CompletionStatus COMPLETED = new CompletionStatus("CO", "Completed", true);
    // this can only be done by the end user between subtests
    public static final CompletionStatus STUDENT_STOP = new CompletionStatus("IS", "Student Stop", true);
    public static final CompletionStatus TEST_LOCKED = new CompletionStatus("CL", "Test Locked", true);
    public static final CompletionStatus ONLINE_COMPLETED = new CompletionStatus("OC", "Online Completed", true);
 
    private final boolean isSubtestEnded;

    private CompletionStatus(final String code, final String description,
            final boolean isSubtestCompleted) {
        super(code, description);
        this.isSubtestEnded = isSubtestCompleted;
        ALL_STATUSES.put(code, this);
    }

    public boolean isSubtestEnded() {
        return isSubtestEnded;
    }

    public boolean isInProgress() {
        return (this == IN_PROGRESS || this == INCOMPLETE || this == SYSTEM_STOP);
    }

    public boolean isInterrupted() {
        return this == STUDENT_STOP || this == SYSTEM_STOP;
    }

    public static CompletionStatus getByCode(final String code) {
        if (!ALL_STATUSES.containsKey(code)) {
            throw new IllegalArgumentException("No CompletionStatus found for: " + code);
        }
        return (CompletionStatus) ALL_STATUSES.get(code);
    }
}