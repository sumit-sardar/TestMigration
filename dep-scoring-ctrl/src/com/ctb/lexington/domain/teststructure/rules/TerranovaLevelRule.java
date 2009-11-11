package com.ctb.lexington.domain.teststructure.rules;

import com.ctb.lexington.domain.teststructure.Subtests;

import java.util.List;

public class TerranovaLevelRule extends LevelRule {
	public boolean validate(Subtests subtests) {
        List subtestNamesList = subtests.subtestNames();
        return validate(subtests, (String[])subtestNamesList.toArray(new String[subtestNamesList.size()]));
	}
    
    public String message() {
        return "Both subtests should be of the same level";
    }
}