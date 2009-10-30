package utils; 

import data.SubtestVO;
import java.util.List;

public class TABESubtestValidation 
{ 
    // subtest names
	public static final String READING = "TABE Reading";
	public static final String MATH_COMPUTATION = "TABE Mathematics Computation";
	public static final String APPLIED_MATH = "TABE Applied Mathematics";
	public static final String LANGUAGE = "TABE Language";
	public static final String VOCABULARY = "TABE Vocabulary";
	public static final String LANGUAGE_MECHANICS = "TABE Language Mechanics";
	public static final String SPELLING = "TABE Spelling";	
    
    // error messages
    public static final String NO_SUBTEST_MSG = "You must select at least one subtest to continue.";

    public static final String LANGUAGE_DEPENDENCY_MSG = "If you select either Language Mechanics or Spelling, you must also select Language.";

    public static final String LANGUAGE_LEVEL_MSG = "If you assign Language Mechanics or Spelling, you must also assign Language, and the same difficulty level must be selected for all.";

    public static final String READING_DEPENDENCY_MSG = "If you select Vocabulary, you must also select Reading.";

    public static final String READING_LEVEL_MSG = "If you select Reading and Vocabulary, you must assign the same difficulty level for both.";

    public static final String MATH_LEVEL_MSG = "If you select Mathematics Computation and Applied Mathematics, you must assign the same difficulty level for both."; 

    public static final String MATH_SUBTESTS_MSG = "The selected test structure does not contain both mathematics subtests. This test will not have a total math score.";

    public static final String SCORE_CALULATABLE_MSG = " Reading, Language, Mathematics Computation and Applied Mathematics must be included for this test to qualify for a total score.";
    
    public static final String NO_ERROR_MSG = "No Error";
    
    public static String currentMessage = "";
    
    public static boolean validation(List subtests, boolean validateLevels)
    {
        currentMessage = NO_ERROR_MSG;
        
        if (! noSubtest(subtests)) {
            currentMessage = NO_SUBTEST_MSG;
            return false;
        }
        
        if (! languageDependency(subtests)) {
            currentMessage = LANGUAGE_DEPENDENCY_MSG;
            return false;
        }
        
        if (validateLevels) {    
            if (! languageLevel(subtests)) {
                currentMessage = LANGUAGE_LEVEL_MSG;
                return false;
            }
        }
        
        if (! readingDependency(subtests)) {
            currentMessage = READING_DEPENDENCY_MSG;
            return false;
        }

        if (validateLevels) {    
            if (! readingLevel(subtests)) {
                currentMessage = READING_LEVEL_MSG;
                return false;
            }
        }
        
        if (validateLevels) {    
            if (! mathLevel(subtests)) {
                currentMessage = MATH_LEVEL_MSG;
                return false;
            }
        }
        
        if (! mathSubtests(subtests)) {
            currentMessage = MATH_SUBTESTS_MSG; // just warning, not error
        }

        if (! scoreCalculatable(subtests)) {
            if (currentMessage.equals(NO_ERROR_MSG))
                currentMessage = SCORE_CALULATABLE_MSG; // just warning, not error
            else {
                currentMessage += "<br/>";
                currentMessage += SCORE_CALULATABLE_MSG; // just warning, not error
            }
        }
        
        return true;
    }
    
    /*
     * You must select at least one subtest to continue.
    */
    public static boolean noSubtest(List subtests)
    {
        if (subtests.size() == 0) 
            return false;
        return true;
    }
    

    /*
     * Language must be selected if either Language Mechanics or Spelling is needed.
    */
    public static boolean languageDependency(List subtests)
    {
        if (presented(LANGUAGE_MECHANICS, subtests) || presented(SPELLING, subtests)) {
            if (! presented(LANGUAGE, subtests)) {
                return false;
            }
        }
        return true;
    }

    /*
     * If you select Language, Language Mechanics and Spelling, then they must be of the same level.
    */
    public static boolean languageLevel(List subtests)
    {
        if (presented(LANGUAGE, subtests) && presented(LANGUAGE_MECHANICS, subtests)) {
            if (! samelevel(LANGUAGE, LANGUAGE_MECHANICS, subtests)) {
                return false;
            }
        }
        if (presented(LANGUAGE, subtests) && presented(SPELLING, subtests)) {
            if (! samelevel(LANGUAGE, SPELLING, subtests)) {
                return false;
            }
        }
        return true;
    }

    /*
     * Reading must be chosen if Vocabulary is needed
    */
    public static boolean readingDependency(List subtests)
    {
        if (presented(VOCABULARY, subtests)) {
            if (! presented(READING, subtests)) {
                return false;
            }
        }
        return true;
    }
    
    /*
     * If you select Reading and Vocabulary subtests, then they must be of the same level.
    */
    public static boolean readingLevel(List subtests)
    {
        if (presented(READING, subtests) && presented(VOCABULARY, subtests)) {
            if (! samelevel(READING, VOCABULARY, subtests)) {
                return false;
            }
        }
        return true;
    }

    /*
     * The selected test structure does not contain both the math subtests. This test will not have a total math score.
    */
    public static boolean mathSubtests(List subtests)
    {
        if (presented(MATH_COMPUTATION, subtests) && presented(APPLIED_MATH, subtests)) {
            return true;
        }
        return false;
    }
    
    /*
     * If you select Math Computation and Applied Math, then they must be of the same level.
    */
    public static boolean mathLevel(List subtests)
    {
        if (presented(MATH_COMPUTATION, subtests) && presented(APPLIED_MATH, subtests)) {
            if (! samelevel(MATH_COMPUTATION, APPLIED_MATH, subtests)) {
                return false;
            }
        }
        return true;
    }


    /*
     * Reading, Language, Math Computation and Applied Mathematics must be selected for this test to qualify for a total score.
    */
    public static boolean scoreCalculatable(List subtests)
    {
        if (presented(READING, subtests) && presented(LANGUAGE, subtests) &&
            presented(MATH_COMPUTATION, subtests) && presented(APPLIED_MATH, subtests)) {
            return true;
        }
        return false;
    }


    /*
     * determine if subtestName presented in the list 
    */
    private static boolean presented(String subtestName, List subtests)
    {
        for (int i=0 ; i<subtests.size() ; i++) {
            SubtestVO subtest = (SubtestVO)subtests.get(i);
            if (subtestName.equals(subtest.getSubtestName())) 
                return true;
        }
        return false;
    }

    /*
     * determine if 2 subtests have the same level
    */
    private static boolean samelevel(String subtestName1, String subtestName2, List subtests)
    {
        String level1 = getlevel(subtestName1, subtests);
        String level2 = getlevel(subtestName2, subtests);
        
        if (level1 == null) level1 = "E";
        if (level2 == null) level2 = "E";
        
        if ((level1 != null) && (level2 != null) && (level1.equals(level2)))
            return true;
        
        return false;
    }

    /*
     * get level for given subtestName
    */
    private static String getlevel(String subtestName, List subtests)
    {
        for (int i=0 ; i<subtests.size() ; i++) {
            SubtestVO subtest = (SubtestVO)subtests.get(i);
            if (subtestName.equals(subtest.getSubtestName())) 
                return subtest.getLevel();
        }
        return null;
    }
    
} 
