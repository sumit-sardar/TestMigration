package com.ctb.lexington.domain.teststructure;

import com.ctb.lexington.domain.teststructure.rules.SubtestRuleFactory;

public class TerraNovaSubtestRules extends SubtestRules {

    public TerraNovaSubtestRules(Subtests subtests) {
        this.subtests = subtests;
        errorRules.add(SubtestRuleFactory.terranovaLevelRule());
        errorRules.add(SubtestRuleFactory.atleastOneSubtestRule());
    }


}
