package com.ctb.oas.normsdata;

import java.io.File;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Sreenivas  Ananthakrishna sreeni@thoughtworks.com
 */
public class ScorerUtil {
    private static final String TRIMESTER_REGEX = "FALL|WINTER|SPRING";
    private static final String FORM_REGEX = "FM [A-B|a-b]";
    private static final String LEVEL_REGEX = "Level|LEVEL\\s[0-9]*";
    private static final String GRADE_REGEX = "GRADE-([a-zA-Z]|[0-9])*";
    private static final String QUARTER_MONTH_REGEX = "QUARTERMONTH\\s*-\\s*[0-9]*";
    private static final String CONTENT_AREA_ABBR_REGEX = "(?:[0-9])[a-zA-Z][a-zA-Z]";
    private static final String LEVEL_REGEX__FROM_FILENAME = "[0-9][a-zA-Z][a-zA-Z][a-zA-Z]";
    private static final String FORM_REGEX__FROM_FILENAME = "(?i)(?:[a-z]+)(\\d+)(?:.+?)\\.(?:.+?)";
    private static final String SCORE_TYPE_FROM_FILENAME_REGEX = "(?i)(?:[a-z]+)(?:\\d+)([a-z]+.+?)\\.(?:.+?)";
    private static final String LAS_LINKS_LEVEL_REGEX = "Level|LEVEL\\s[[0-9] | [a-z] | [A-Z]]*";
    private static final String LAS_LINKS_GRADE_REGEX = "GRADE-([a-zA-Z]|[a-z]|[A-Z]|[0-9])*";
     
    protected static String getTrimester(String line) {
        final String trimester = ScorerUtil.getMatchString(line, TRIMESTER_REGEX);
        if (trimester != null)
            return trimester;

        final String quarterMonthString = ScorerUtil.getMatchString(line, QUARTER_MONTH_REGEX);
        if (quarterMonthString != null) {
            int quarterMonth = Integer.parseInt(quarterMonthString.substring(quarterMonthString.indexOf("-") + 1, quarterMonthString.length()));
            switch (quarterMonth) {
                case 6:
                    return "FALL";
                case 18:
                    return "WINTER";
                case 19:
                    return "WINTER";
                case 30:
                    return "SPRING";
                case 31:
                    return "SPRING";
            }
        }

        return null;
    }

    protected static String getForm(String line) {
        String formString = getMatchString(line, FORM_REGEX);
        if (formString == null)
            return null;

        return formString.substring(formString.length() - 1);
    }

    protected static String getMatchString(String line, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        if (!matcher.find()) {
            return null;
        }
        return matcher.group();
    }

    protected static String getLevel(String code_1_line) {
        String levelString = getMatchString(code_1_line, LEVEL_REGEX);
        if (levelString == null)
            return null;

        return levelString.substring(levelString.indexOf(" ") + 1, levelString.length());
    }

    protected static String getContentArea(String instructionLine) {
        instructionLine = instructionLine.trim();
        String contentAreaCode = instructionLine.substring(instructionLine.length() - 3, instructionLine.length());
        return ContentAreaLookup.getContentArea(contentAreaCode);
    }
    protected static String getLasLinksContentArea(String instructionLine) {
        instructionLine = instructionLine.trim();
        String contentAreaCode = instructionLine.substring(instructionLine.length() - 3, instructionLine.length());
        return ContentAreaLookup.getLasLinksContentArea(contentAreaCode);
    }

    protected static String getLineForCode(ListIterator iterator, String code) {
        String result;
        while (iterator.hasNext()) {
            result = (String) iterator.next();
            if (result != null && result.trim().startsWith(code)) {
                return result;
            }
        }
        return null;
    }

    protected static boolean containsHeaderCode(String line) {
        return line != null && line.trim().startsWith(LayoutConstants.HEADER_CODE);
    }


    public static String getScoreString(String scoreLine) {
        scoreLine = scoreLine.trim();
        return scoreLine.substring(2, scoreLine.length());
    }


    public static boolean containsScoreCode(String line) {
        return line != null && line.trim().startsWith(LayoutConstants.SCORE_CODE);

    }

    protected static boolean containsInstructionCode(String line) {
        return line != null && line.trim().startsWith(LayoutConstants.INSTRUCTION_CODE);
    }

    public static String getGrade(String nceHeaderLine) {
        final String matchString = getMatchString(nceHeaderLine, GRADE_REGEX);
        if (matchString == null)
            return null;
        final String gradeString = matchString.substring(matchString.length() - 2, matchString.length());
        try {
            int grade = Integer.parseInt(gradeString);
            return String.valueOf(grade);
        }
        catch (NumberFormatException e) {
            if (gradeString.equalsIgnoreCase("KG"))
                return "K";

            return gradeString;
        }
    }

    public static String getContentAreafromFileName(String string) {
        String caAbbr = getMatchString(string, CONTENT_AREA_ABBR_REGEX);
        if (caAbbr != null)
            return ContentAreaLookup.getContentArea(caAbbr.substring(1, caAbbr.length()));

        return null;
    }

    public static String getFormFromFileName(String fileName) {
        if (fileName != null) {
            //return fileName.substring(2, 3);
            Pattern pattern = Pattern.compile(FORM_REGEX__FROM_FILENAME);
            Matcher matcher = pattern.matcher(fileName);
            if (!matcher.find()) {
                return null;
            }
            return matcher.group(1); // Group 0 is always entire expression       
        }

        return null;
    }

    public static String getScoreTypeFromFileName(String fileName) {
    	if( fileName != null ) {
            Pattern pattern = Pattern.compile(SCORE_TYPE_FROM_FILENAME_REGEX);
            Matcher matcher = pattern.matcher(fileName);
            if (!matcher.find()) {
                return null;
            }
            return matcher.group(1); // Group 0 is always entire expression       
    	}
    	
    	return null;
    }
    
    
    public static String getLevelFromFileName(String name) {
        String token = getMatchString(name, LEVEL_REGEX__FROM_FILENAME);
        if (token == null)
            return null;

        return token.substring(token.length() - 1, token.length());
    }

    public static void validateArgs(String[] args) {
        if (args.length != 2) {
            printUsage();
            System.exit(2);
        }
    }

    public static File getInputFileFromArgs(String[] args) {
        validateArgs(args);
        File file = new File(args[0]);
        if (!file.exists()) {
            System.out.println("Input path is incorrect! ");
            System.exit(2);
        }
        return file;
    }

    public static File getOutputFileFromArgs(String[] args) {
        validateArgs(args);
        File file = new File(args[1]);
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
        }
        catch (IOException e) {
            throw new RuntimeException("Cannot write to output file ", e);
        }

        return file;
    }

    private static void printUsage() {
        System.out.println("Usage: load <input file > <output file>");
    }

    public static String getProductFromFileName(String name) {
    	String productAbbrev = name.substring(0,2);
    	String productForm = getFormFromFileName(name);
    	
    	if( productAbbrev.equalsIgnoreCase("cb") ) {
    		
    		if( productForm.equals("9") )
    			return ScoreRecord.TABE9_BATTERY_NAME.trim();
    		else if( productForm.equals("10") )
    			return ScoreRecord.TABE10_BATTERY_NAME.trim();
    		
    	} else if( productAbbrev.equalsIgnoreCase("sv") ) {

    		if( productForm.equals("9") )
    			return ScoreRecord.TABE9_SURVEY_NAME.trim();
    		else if( productForm.equals("10") )
    			return ScoreRecord.TABE10_SURVEY_NAME.trim();

    	}
    	
    	/*
    	String productString = name.substring(0, 2);
        if (productString.equalsIgnoreCase("cb"))
            return ScoreRecord.TABE_BATTERY_NAME.trim();

        if (productString.equalsIgnoreCase("sv"))
            return ScoreRecord.TABE_SURVEY_NAME.trim();
        */
        return null;
    }

    public static void advanceReader(LineNumberReader reader, int count) {
        for (int i = 0; i < count; i++) {
            try {
                reader.readLine();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

	public static String getLasLinkLevel(String code_1_line) {
        String levelString = getMatchString(code_1_line, LAS_LINKS_LEVEL_REGEX);
        if (levelString == null)
            return null;

        return levelString.substring(levelString.indexOf(" ") + 1, levelString.length());
    }
	
	   public static String getLasLinksGrade(String nceHeaderLine) {
	        final String matchString = getMatchString(nceHeaderLine, LAS_LINKS_GRADE_REGEX);
	        if (matchString == null)
	            return null;
	        final String gradeString;
	        if(matchString.indexOf("-")==matchString.length()-2)
	        	gradeString = matchString.substring(matchString.length() - 1, matchString.length());
	        else
	        	gradeString = matchString.substring(matchString.length() - 2, matchString.length());
	        try {
	            int grade = Integer.parseInt(gradeString.trim());
	            return String.valueOf(grade);
	        }
	        catch (NumberFormatException e) {
	            if (gradeString.equalsIgnoreCase("KG"))
	                return "K";
	            else if (nceHeaderLine.indexOf("*")>-1){
	            	String allGradeString =handleAllGrade(nceHeaderLine);
	            	if(allGradeString.equals("*"));
	            	  return allGradeString;
	            }
	            return gradeString.trim();
	        }
	    }

	private static String handleAllGrade(String nceHeaderLine) {
		try{
			int index = nceHeaderLine.indexOf("GRADE-");
			String gradeString=nceHeaderLine.substring(index+6, index+8);
			return gradeString.trim();
		} catch (Exception e){
			return "";
		}
	}
		
	
}
