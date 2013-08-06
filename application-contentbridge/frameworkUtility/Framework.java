/**
 * Utility to format an unformated objectives.txt with indentation.
 * Also will test objectives.txt for correct format
 * and test item_map.txt objective IDs
 */

import java.io.*;
import java.util.HashSet;
import java.util.TreeSet;

public class Framework {
    // constants
    final static String CMD_FORMAT   = "FORMAT";
    final static String CMD_TEST     = "TEST";
    final static String CMD_BOTH     = "FORMATANDTEST";
    final static String ARG_PATH     = "PATH";
    final static String ARG_INPUT    = "INPUT";
    final static String ARG_OBJFILE  = "OBJFILE";
    final static String ARG_ITEMFILE = "ITEMFILE";
    final static String STR_INDENT   = "    "; // 4 consecutive spaces.

    // variables
    private static String STR_Path   = "";
    private static String STR_Input  = "objectives.txt";
    private static String STR_Output = "objectives.txt";
    private static String STR_Item   = "item_map.txt";

    private static boolean InvalidArg = false;
    private static boolean Format     = false;
    private static boolean Test       = false;

    /**
     * @param args the input command line parameters.
     */
	public static void main (String [] args) {
	    HashSet setOids = new HashSet ();

        if (args.length > 0) {
            CrackArguments (args);
        }
        if (args.length < 1 || InvalidArg) {
            Usage ();
			System.exit (1);
		}

		try {
		    // reassign error to 'error.log'
		    {
			    File Temp = new File (STR_Path + "error.log");
			    if (Temp.exists ())
			        Temp.delete ();
		    }
		    PrintStream Errors = new PrintStream (new FileOutputStream (STR_Path + "error.log"), true);
		    System.setErr (Errors);

		    // rename the input file if it conflicts with the output file
		    if (Format && STR_Input.compareTo (STR_Output) == 0) {
		        File Backup = new File (STR_Path + "Fin_" + STR_Input);
		        if (Backup.exists ())
		            Backup.delete ();
		        File Input = new File (STR_Path + STR_Input);
		        Input.renameTo (Backup);
		        STR_Input = "Fin_" + STR_Input;
		    }

		    // Test objectives file for content and structure
		    BufferedReader brObjectiveFile = new BufferedReader (new InputStreamReader (
		        new BufferedInputStream (new FileInputStream (STR_Path + STR_Input))));

		    if (Indent (setOids, brObjectiveFile)) {
		        System.out.println ("Objectives file OK");		        
		    } else {
		        System.out.println ("Objectives file contains errors");
		    	System.exit (2);
		    }

		    if (Test) {
			    // Test item map content and structure
			    BufferedReader brItemFile = new BufferedReader (new InputStreamReader (
			        new BufferedInputStream (new FileInputStream (STR_Path + STR_Item))));
	
			    if (TestItems (setOids, brItemFile)) {
			        System.out.println ("Item map file OK");		        
			    } else {
			        System.out.println ("Item map file contains errors");
			    	System.exit (3);
			    }
			}
		} catch (Exception e) {
		    e.printStackTrace ();
		    System.out.println ("ERROR: " + e);
	    	System.exit (4);
		}
	} // public static void main (String [] args)

	/**
	 * Print usage
	 */
	private static void Usage () {
	    System.out.println ("\nUsage: Framework <command> [<arguments>]");
	    System.out.println ("   <commands>");
	    System.out.println ("      Format        - Add indentation to 'objectives.txt'.");
	    System.out.println ("      Test          - Test items mapping in 'item_map.txt' against 'objectives.txt'.");
	    System.out.println ("      FormatAndTest - Perform both the Format and Test functions.");
	    System.out.println ("   <arguments>");
	    System.out.println ("      Path=<filepath>          - Path where all input and output files will be found.");
	    System.out.println ("      Input=<objectives.txt>   - Objectives input file to be formatted.");
	    System.out.println ("                                 If not specified will default to 'objectives.txt'.");
	    System.out.println ("                                 If Formating and the input file is the same as the");
	    System.out.println ("                                 objective file then the input file will be renamed.");
	    System.out.println ("      ObjFile=<objectives.txt> - Objectives output file if Formating.");
	    System.out.println ("                                 If not specified output will be to 'objectives.txt'.");
	    System.out.println ("      ItemFile=<item_map.txt>  - Item map file to test objective mapping if Testing.");
	    System.out.println ("                                 If not specified default to 'item_map.txt'.");
	} // private static void Usage ()

	/**
	 * Crack the command aruments.
     * @param args the input command line parameters.
	 */
	private static void CrackArguments (String [] args) {
		String strParm;
        String [] ps;
		String argParm;

		// find command
		strParm = args [0];
        if (strParm.compareToIgnoreCase (CMD_FORMAT) == 0) {
            Format = true;
        } else if (strParm.compareToIgnoreCase (CMD_TEST) == 0) {
            Test = true;
        } else if (strParm.compareToIgnoreCase (CMD_BOTH) == 0) {
            Format = true;
            Test = true;
        } else {
            System.out.println ("Invalid command: " + args [0]);
		    InvalidArg = true;	            
        }

        // other arguments
        for (int nx=1; nx < args.length && !InvalidArg; nx++) {
    		strParm = args [nx];
	        ps = strParm.split ("=");
	        argParm = ps [0];
	        if (argParm.compareToIgnoreCase (ARG_PATH) == 0) {
	            if (ps.length > 1) {
		            String separator = System.getProperty ("file.separator");
		            STR_Path = ps [1];
		            if (STR_Path.compareTo ("") != 0 && !STR_Path.endsWith (separator))
		                STR_Path = STR_Path + separator;
	            }
	        } else if (ps.length < 2) {
		            System.out.println ("Error in argument: " + args [nx]);
				    InvalidArg = true;	            
	        } else if (argParm.compareToIgnoreCase (ARG_INPUT) == 0) {
	            STR_Input = ps [1];
	        } else if (argParm.compareToIgnoreCase (ARG_OBJFILE) == 0) {
	            STR_Output = ps [1];
	        } else if (argParm.compareToIgnoreCase (ARG_ITEMFILE) == 0) {
	            STR_Item = ps [1];
	        } else {
	            System.out.println ("Invalid argument: " + args [nx]);
			    InvalidArg = true;	            
	        }
        }
	} // private static void CrackArguments (String [] args)

    /**
     * Test objective for structure and content, create objective ID hash table
     * Indent objective file to output if Formatting selected
     * @param Oids a hash table to put objective IDs into for testing.
     * @param input objective file.
     */
	private static boolean Indent (HashSet Oids, BufferedReader input) throws Exception {
	    TreeSet     OIdTree = new TreeSet ();
	    PrintWriter pwOut   = null;
		String      strLine = null;
	    boolean     output  = false;
	    boolean     result  = true;

		if (Format) {
		    pwOut = new PrintWriter (new BufferedWriter (new FileWriter (STR_Path + STR_Output)));
		    output = true;
		}
		while ((strLine = input.readLine ()) != null) {
	        String [] as = strLine.split ("::");
	        if (as.length != 4) { // there must be only one objective definition per line
	            System.err.println ("Bad objective line found: " + strLine.trim ());
	            result = false;
	        } else {
		        String strObjId = as [1].replaceAll ("\"","");
		        String strParentOid = as [2].replaceAll ("\"","");
		        int    nLvl = Integer.parseInt (as [3].replaceAll ("\"",""));

		        // Parent objective must exist except framework root
		        if (!Oids.contains (strParentOid) && !Oids.isEmpty ()) {
		            System.err.println ("Parent Objective ID not found: " + strLine.trim ());
		            result = false;
	            }

		        // Parent objective must be one level up
		        if (!OIdTree.isEmpty () && !OIdTree.contains (new Objective (strParentOid, nLvl - 1))) {
		            System.err.println ("Parent Objective not found on adjacent level: " + strLine.trim ());
		            result = false;
	            }

		        // duplicate objective IDs are not allowed
		        if (Oids.contains (strObjId)) {
		            System.err.println ("Found duplicate Objective ID: " + strLine.trim ());
		            result = false;
	            } else {
		            Oids.add (strObjId);
		            OIdTree.add (new Objective (strObjId, nLvl));
		        }

		        if (Format) { // output the formatted line
			        for (int nx = 1; nx < nLvl; nx++) {
			            if (output)
			                pwOut.print (STR_INDENT);
			            else
			                System.out.print (STR_INDENT);
			        }
		            if (output)
		                pwOut.println (strLine.trim ());
		            else
		                System.out.println (strLine.trim ());
		        }
	        }
	    }

        // objectives.txt must not be empty
        if (Oids.isEmpty ()) {
            System.err.println ("No data in objectives.txt.");
            result = false;
        }

        if (output) {
            pwOut.flush ();
            pwOut.close ();
        }
	    return result;
	} // private static boolean Indent (HashSet Oids, BufferedReader input)

    /**
     * Test item map structure and content against objective ID hash table
     * @param Oids a hash table containing objective IDs for testing.
     * @param input item file.
     */
	private static boolean TestItems (HashSet Oids, BufferedReader input) throws Exception {
	    HashSet itemIds  = new HashSet ();
	    boolean result   = true;
		String strLine   = null;
        String strObjId  = null;
        String strItemId = null;

        while ((strLine = input.readLine ()) != null) {
	        String [] as = strLine.split (",");
	        if (as.length != 2) { // there must be only one item/objective pair per line
	            System.err.println ("Bad item line found: " + strLine.trim ());
	            result = false;
	        } else {
		        strItemId = as [0].replaceAll ("\"","");
		        String objList = as [1].replaceAll ("\"","");
		        String os[] = objList.split(":");
		        for(int i=0;i<os.length;i++) {
		        	strObjId = os[i];
			        if (itemIds.contains (strItemId)) { // duplicate item IDs are not allowed
			            System.err.println ("Found duplicate Item ID: " + strLine.trim ());
			            result = false;
		            } else {
		                itemIds.add (strObjId);
			        }
	
			        if (!Oids.contains (strObjId)) { // the objective ID must exist in objectives.txt
			            System.err.println ("Item Objective ID not found: " + strLine.trim ());
			            result = false;
		            }
		        }
	        }
	    }

        // item_map.txt must not be empty
        if (itemIds.isEmpty ()) {
            System.err.println ("No data in item_map.txt.");
            result = false;
        }

        return result;
    } // private static boolean TestItems (HashSet Oids, BufferedReader input)
} // public class Framework