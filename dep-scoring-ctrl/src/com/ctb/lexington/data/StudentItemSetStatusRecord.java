package com.ctb.lexington.data;

import java.sql.Timestamp;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.ctb.lexington.db.record.Persistent;

public class StudentItemSetStatusRecord implements Persistent {
    private Long testRosterId;
    private Long itemSetId;
    private String completionStatus;
    private Timestamp startDateTime;
    private Timestamp completionDateTime;
    private String validationStatus;
    private String validationUpdatedBy;
    private Timestamp validationUpdatedDateTime;
    private String validationUpdatedNote;
    private String timeExpired;

    /**
     * Constructor for StudentItemSetStatusRecord.
     */
    public StudentItemSetStatusRecord() {
        super();
        // iBatis required constructor
    }

    /**
     * Constructs a new <code>StudentItemSetStatusRecord</code> for testing.
     *
     * @param testRosterId the test roster id
     * @param itemSetId the item set id (subtest id)
     * @param completionStatus the completion status
     * @see StudentItemSetStatusLocal
     */
    public StudentItemSetStatusRecord(final Long testRosterId, final Long itemSetId, final String completionStatus) {
        this.testRosterId = testRosterId;
        this.itemSetId = itemSetId;
        this.completionStatus = completionStatus;
    }

    public Timestamp getCompletionDateTime() {
        return completionDateTime;
    }

    public void setCompletionDateTime(final Timestamp completionDateTime) {
        this.completionDateTime = completionDateTime;
    }

    public String getCompletionStatus() {
        return completionStatus;
    }

    public void setCompletionStatus(final String completionStatus) {
        this.completionStatus = completionStatus;
    }

    public Long getItemSetId() {
        return itemSetId;
    }

    public void setItemSetId(final Long itemSetId) {
        this.itemSetId = itemSetId;
    }

    public Timestamp getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(final Timestamp startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Long getTestRosterId() {
        return testRosterId;
    }

    public void setTestRosterId(final Long testRosterId) {
        this.testRosterId = testRosterId;
    }

    public String getTimeExpired() {
        return timeExpired;
    }

    public void setTimeExpired(final String timeExpired) {
        this.timeExpired = timeExpired;
    }

    public String getValidationStatus() {
        return validationStatus;
    }

    public void setValidationStatus(final String validationStatus) {
        this.validationStatus = validationStatus;
    }

    public String getValidationUpdatedBy() {
        return validationUpdatedBy;
    }

    public void setValidationUpdatedBy(final String validationUpdatedBy) {
        this.validationUpdatedBy = validationUpdatedBy;
    }

    public Timestamp getValidationUpdatedDateTime() {
        return validationUpdatedDateTime;
    }

    public void setValidationUpdatedDateTime(final Timestamp validationUpdatedDateTime) {
        this.validationUpdatedDateTime = validationUpdatedDateTime;
    }

    public String getValidationUpdatedNote() {
        return validationUpdatedNote;
    }

    public void setValidationUpdatedNote(final String validationUpdatedNote) {
        this.validationUpdatedNote = validationUpdatedNote;
    }

    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof StudentItemSetStatusRecord)) return false;

        final StudentItemSetStatusRecord studentItemSetStatusRecord = (StudentItemSetStatusRecord) o;

        if (itemSetId != null
            ? !itemSetId.equals(studentItemSetStatusRecord.itemSetId)
            : studentItemSetStatusRecord.itemSetId != null) return false;
        if (testRosterId != null
            ? !testRosterId.equals(studentItemSetStatusRecord.testRosterId)
            : studentItemSetStatusRecord.testRosterId != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (testRosterId != null ? testRosterId.hashCode() : 0);
        result = 29 * result + (itemSetId != null ? itemSetId.hashCode() : 0);
        return result;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}