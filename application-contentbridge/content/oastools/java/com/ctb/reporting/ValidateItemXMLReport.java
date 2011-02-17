package com.ctb.reporting;

import org.jdom.JDOMException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sreenivas  Ananthakrishna sreeni@thoughtworks.com
 */
public class ValidateItemXMLReport extends AbstractReport {
    private List exceptionsList = new ArrayList();
    private List validationFailures = new ArrayList();

    public void addException(Exception e) {
        exceptionsList.add(e);
    }

    public void addItemValidationFailure(String itemId, String message, JDOMException e) {
        validationFailures.add(new ItemValidationFailure(itemId, message, e));
    }

    public Exception getException() {
        return new Exception("Items have failed Validation");
    }

    public boolean isSuccess() {
        return validationFailures.isEmpty();
    }

    public String toString() {
        return super.toString();
    }

    public String toString(boolean isSubReport) {
        return super.toString(isSubReport);
    }

    public class ItemValidationFailure {
        String itemId;
        String message;
        JDOMException e;

        public ItemValidationFailure(String itemId, String message, JDOMException e) {
            this.itemId = itemId;
            this.message = message;
            this.e = e;
        }

        public String getItemId() {
            return itemId;
        }

        public String getMessage() {
            return message;
        }

        public JDOMException getException() {
            return e;
        }
    }

    public List getExceptions() {
        return exceptionsList;
    }

    public List getValidationFailures() {
        return validationFailures;
    }
}
