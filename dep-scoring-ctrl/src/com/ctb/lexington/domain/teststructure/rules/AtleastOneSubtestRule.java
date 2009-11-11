package com.ctb.lexington.domain.teststructure.rules;

import com.ctb.lexington.domain.teststructure.Subtests;

public class AtleastOneSubtestRule implements Rule{
    public boolean validate(Subtests subtests) {
        return subtests.size() > 0;
    }

    public String message() {
        return "You must select at least one content area to continue.";
    }
}