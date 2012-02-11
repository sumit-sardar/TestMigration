package utils; 

import data.SubtestVO;
import java.util.List;

public class LASLINKSSubtestValidation 
{ 
    // subtest names
	public static final String SPEAKING = "Speaking";
	public static final String LISTENING = "Listening";
	public static final String READING = "Reading";
	public static final String WRITING = "Writing";
    
	
    // error messages
    public static final String NO_SUBTEST_MSG = "You must select at least one subtest to continue.";
    
    public static final String NO_ERROR_MSG = "No Error";
    
    public static final String COMPREHENSION_DEPENDENCY_MSG = "Reading and Listening both need to be selected.";

    public static final String ORAL_DEPENDENCY_MSG = "Speaking and Listening both need to be selected.";
    
    public static String currentMessage = "";
    
    
    
    public static boolean validation(List subtests)
    {
    	return validationSubtests(subtests);
    }

    public static boolean validationSubtests(List subtests)
    {
        currentMessage = NO_ERROR_MSG;
        
        if (! noSubtest(subtests)) {
            currentMessage = NO_SUBTEST_MSG;
            return false;
        }
                
        if (! comprehensionDependency(subtests)) {
            currentMessage = COMPREHENSION_DEPENDENCY_MSG;
        }

        if (! oralDependency(subtests)) {
            currentMessage = ORAL_DEPENDENCY_MSG;
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
     * Comprehension (R and L together) 
    */
    public static boolean comprehensionDependency(List subtests)
    {
        if (presented(READING, subtests)) {
            if (! presented(LISTENING, subtests)) {
                return false;
            }
        }
        if (presented(LISTENING, subtests)) {
            if (! presented(READING, subtests)) {
                return false;
            }
        }
        return true;
    }

    /*
     * Comprehension (L and S together) 
    */
    public static boolean oralDependency(List subtests)
    {
        if (presented(LISTENING, subtests)) {
            if (! presented(SPEAKING, subtests)) {
                return false;
            }
        }
        if (presented(SPEAKING, subtests)) {
            if (! presented(LISTENING, subtests)) {
                return false;
            }
        }
        return true;
    }
    
} 
