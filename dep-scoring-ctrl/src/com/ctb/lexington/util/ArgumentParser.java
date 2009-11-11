// ArgumentParser.java v0.9 - Unix-style commandline parsing
// Copyright (C) 1999  j.p.lewis  
// 
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Library General Public
// License as published by the Free Software Foundation; either
// version 2 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Library General Public License for more details.
// 
// You should have received a copy of the GNU Library General Public
// License along with this library; if not, write to the
// Free Software Foundation, Inc., 59 Temple Place - Suite 330,
// Boston, MA  02111-1307, USA.
//
// Primary author contact info:  www.idiom.com/~zilla  zilla@computer.org

package com.ctb.lexington.util;

import java.util.*;

import com.ctb.lexington.exception.ArgumentParserException;

/**
 *  The <code>ArgumentParser</code> class does Unix-style parsing of
 *  commandline arguments.  The single <code>args.parse()</code>
 *  method returns a hashtable containing the parsed and
 *  type-checked arguments.
 *  The hashtable can be queried for the parsed arguments,
 *  or accessor methods can be called for this purpose.
 *  If parsing fails the method prints a formatted message and returns null.
 * <p>
 * This doc page reflects version "0.9" of this code.
 * <p>
 *  Usage example:
 *  <pre>
 *  public static void main(String[] cmdline)
 *  {
 *    ArgumentParser args = new ArgumentParser(cmdline);
 *    Hashtable hash      = args(cmdline).parse("+:str:output file:" +
 *				  "-frameno:int:frame number:" +
 *				  "+:str:username:" +
 *				  "-f:int:frame rate:" +
 *				  "-v:flg:verbose");
 * <p>
 *    if (args == null) 
 *      System.exit(1);
 * <p>
 *    // first and second required arguments
 *    String Path = args.getRequiredString(0);
 *    String Username = args.getRequiredString(1);
 * <p>
 *    // optional integer argument introduced by "-framenum"
 *    Integer Framenum = args.getOptionalInteger("-framenum");
 * <p>
 *    // optional integer argument introduced by "-f".  This time
 *    // query the hashtable directly rather than using accessor function
 *    // as in the previous example.
 *    int framerate = -1;
 *    if (hash.get("-f") != null)
 *      framerate = ((Integer)hash.get("-f")).intValue();
 * <p>
 *    // optional flag
 *    Boolean Verbose = args.getFlag("-v");
 *  }
 *  </pre>
 *
 * <code>ArgumentParser</code> considers arguments to be one of three sorts:
 * <ul>
 * <li><i>Required</i> arguments
 * <li><i>Optional</i> arguments are preceeded by
 * an option string ("key") that begins with one or more hyphens. For example,
 * an optional argument to specify the number of something is
 * <tt>-n 5</tt>
 * <li><i>Flag</i>   arguments are <tt>true</tt> if the
 * flag is present, false otherwise.  The standard example of such
 * is the <tt>-verbose</tt> flag.
 * </ul>
 * Note that unlike the Unix <code>getopts</code> routine,
 * the option and flag strings are not restricted
 * to single characters; this also means that "-framenum"
 * is not the same thing as "-f -r -a -m -e -n -u -m".
 * <p>
 * The argument to the <code>parse</code> method is a string template
 * defining the desired arguments.  The string contains
 * one or more argument specifications, each of which consists of
 * a <i>key</i>, a <i>type</i>, and a <i>description</i>, each separated by a colon character.
 * The <i>key</i> is "+" for required arguments and is the desired
 * string (such as "--verbose" or "-n") for optional and flag arguments.
 * The <i>type</i> is one of the strings "int", "flt", "dbl", "str", or "flg",
 * with the last string meaning a boolean flag argument such as "-verbose".
 * The <i>description</i> is a phrase describing the argument;
 * this phrase is printed in the usage message.
 * <p>
 * For a single required string argument specifying an output file
 * the argument template string would be
 * <pre>
 *   "+:str:output file:"
 * </pre>
 * <p>
 * If we add a second optional integer argument specifying a
 * "number of passes" the argument template string would be
 * <pre>
 *   "+:str:output file:"+
 *   "-np:int:number of passes:"
 * </pre>
 * Note that the description field should be terminated with a colon.
 *
 * <h4>Usage message</h4>
 * In the example program at top, if we pass in the (incorrect) arguments
 * <pre>
 *   /usr/tmp zilla -v -f abc
 * </pre>
 * <code>ArgumentParser</code> prints a formatted error message and returns null:
 * (Note - the formatting is messed up in this html quote for
 * some reason)
 * <pre>
 * <tt>
 * error in args.parse:
 * bad argument, expected type int, got 'abc' for argument 'frame rate'
 * Usage: 
 *    <output file>                                                (string)
 *    -framenum       <frame number>                               (integer)
 *    <username>                                                   (string)
 *    -f              <framerate>                                  (integer)
 *    -v              <verbose>                                    (true|false)
 * </tt>
 * </pre>
 * <p>
 * The usage message can also be generated by calling the method
 * <pre>
 *    args.usageMessage(argtemplate)
 * </pre>
 *
 * @author jplewis
 */


public class ArgumentParser
{

  /**
   * Returns a Hashtable containing the parsed arguments.
   * The return value is not needed if the accessor functions
   * (below) are used.
   */
  public Hashtable parse(String argtemplate)
  {
    try {
      String[] argstrings = new String[cmdline.length];
      for( int i=0; i < argstrings.length; i++ )
	argstrings[i] = cmdline[i];

      StringTokenizer st = new StringTokenizer(argtemplate, ":\n");

      while( st.hasMoreTokens() ) {

	String key = st.nextToken();
	String typestr = st.nextToken();
	String usagestr = st.nextToken();

	if (verbose)
	  System.out.println("Parsing " + key +":"+ typestr +":"+ usagestr);

	Object val = argScan(argstrings, key, typestr, usagestr);

	if (val != null) {
	  if (key.equals(TOK_required)) {
	    args.put(new Integer(reqargcnt), val);
	    reqargcnt ++;
	  }
	  else
	    args.put(key, val);
	}
      } //while

      // check that all arguments were accepted
      for( int i=0; i < argstrings.length; i++ )
	if (argstrings[i] != null)
	  throw new ArgumentParserException("argument '" + argstrings[i] +
			      "' not recognized");

      args.put("#args", new Integer(reqargcnt));

      return args;
    }
    catch(ArgumentParserException ex)
    {
      System.err.println("error in ArgumentParser.parse:");
      System.err.println(ex.getMessage());
      if (verbose) ex.printStackTrace();
      usageMessage(argtemplate);
      return null;
    }

  } //parse

  //----------------------------------------------------------------

  /**
   * The <code>getRequiredXX</code> and <code>getOptionalXX</code>
   * accessor functions encapsulate the hashtable lookup for convenience.
   * 
   * The hashtable can also be queried directly.  In the case of
   * required arguments, the key is 'new Integer(<argnum>)' with the
   * argument number counting from zero.  That is, the third
   * required argument (a float in this example) could be obtained by
   * <pre>
   *    ArgumentParser args = new ArgumentParser(commandline);
   *    Hashtable h = args.parse(argtemplate);
   *    Object argval = h.get(new Integer(2));   // 3rd required arg
   *    if (argval != null)
   *       f = ((Float)argval).floatValue();
   * </pre>
   *
   * @exception ArgumentParserException a descriptive exception.
   */
  public String getRequiredString(int num)
    throws ArgumentParserException
  {
    checkArgnum(num);
    return (String)(args.get(new Integer(num)));
  }

  public String getOptionalString(String key)
  {
    return (String)(args.get(key));
  }

  public Integer getRequiredInteger(int num)
    throws ArgumentParserException
  {
    checkArgnum(num);
    return (Integer)(args.get(new Integer(num)));
  }

  public Integer getOptionalInteger(String key)
  {
    return (Integer)(args.get(key));
  }

  public Float getRequiredFloat(int num)
    throws ArgumentParserException
  {
    checkArgnum(num);
    return (Float)(args.get(new Integer(num)));
  }

  public Float getOptionalFloat(String key)
  {
    return (Float)(args.get(key));
  }


  public Double getRequiredDouble(int num)
    throws ArgumentParserException
  {
    checkArgnum(num);
    return (Double)(args.get(new Integer(num)));
  }

  public Double getOptionalDouble(String key)
  {
    return (Double)(args.get(key));
  }

  public Boolean getFlag(String key)
  {
    Object argval = args.get(key);
    return (Boolean)argval;
  }

  //----------------------------------------------------------------

  private Hashtable 	args 		= new Hashtable();
  private int 		reqargcnt;
  private String[]	cmdline;

  final static String	TOK_required 	= "+";
  final static String	TOK_flag 	= "flg";
  final static String	TOK_integer 	= "int";
  final static String	TOK_float 	= "flt";
  final static String	TOK_double 	= "dbl";
  final static String	TOK_string 	= "str";

  private static Hashtable gTypeTab;
  static {
    gTypeTab = new Hashtable();
    gTypeTab.put(TOK_string, "string");
    gTypeTab.put(TOK_integer, "integer");
    gTypeTab.put(TOK_float, "float");
    gTypeTab.put(TOK_double, "double");
    gTypeTab.put(TOK_flag, "true|false");
  }

  private final static boolean	verbose	= false;

  /**
   * pass in the command line arguments obtained from <code>main</code>
   */
  public ArgumentParser(String[] _cmdline)
  {
    cmdline = _cmdline;
    reqargcnt = 0;
  }

  /** check that requested required argument number is in range.
   */
  private void checkArgnum(int num)
    throws ArgumentParserException
  {
    if ((num < 0) || (num >= reqargcnt))
      throw new ArgumentParserException("requested argument number " + num +
			  " is outside range 0.." + (reqargcnt-1));
  }


  private static Object typeArg(String typestr, String val, String usagemsg)
    throws ArgumentParserException
  {
    if (verbose) System.out.println("typeArg " + typestr +" "+ val);
    try {
      if (typestr.equals(TOK_float))
	return new Float(val);
      else if (typestr.equals(TOK_double))
	return new Double(val);	  
      else if (typestr.equals(TOK_integer))
	return new Integer(val);	  
      else if (typestr.equals(TOK_flag))
	return new Boolean(val);	  
      else if (typestr.equals(TOK_string))
	return val;
      else
	throw new ArgumentParserException("unknown type: " + typestr);
    }
    catch(ArgumentParserException ex)
    {
      String utypestr = (String)gTypeTab.get(typestr);
      if (utypestr == null) utypestr = typestr;
      throw new ArgumentParserException("bad argument, expected type " + typestr +", got " +
			  val + " for argument '" + usagemsg + "'");
    }
  }


  private static Object argScan(String[] args,
				String key,
				String typestr,
				String usagestr)
    throws ArgumentParserException
  {
    Object val = null;

    // required argument
    if (key.equals(TOK_required)) {
      int i=0;
      while( i < args.length ) {
	// for a required argument do not match option keys or flags.
	// also do not match the arguments to the above.
	if ((args[i] != null) && !args[i].startsWith("-") &&
	    !((i > 0) && (args[i-1]!=null) && args[i-1].startsWith("-")))
	{
	  val = typeArg(typestr, args[i], usagestr);
	  args[i] = null;
	  break;
	}
	i++;
      }

      if (val == null)
	throw new ArgumentParserException("missing argument: '" + usagestr + "'");
    }

    // optional or flag argument
    // val is null if not found
    else {
      int i=0;
      while( i < args.length ) {
	if (args[i] == null) { i++; continue; }
	if (args[i].equals(key)) {

	  // optional argument
	  if (!typestr.equals(TOK_flag)) {
	    if ((i+1) == args.length)
	      throw new ArgumentParserException("missing argument for option " + key);
	    val = typeArg(typestr, args[i+1], usagestr);
	    args[i] = args[i+1] = null;
	    i++;
	  }

	  // flag argument
	  else {
	    args[i] = null;
	    val = new Boolean(true);
	  }
	}
	i++;
      }
    }

    return val;
  } //argScan


  // string of spaces, used to do column-formatted printing
  static byte[] spaces = new byte[80];
  static {
    for( int i=0; i < 80; i++ ) spaces[i] = ' ';
  }


  /** print the argument template as a usage message
   */
  public static void usageMessage(String argtemplate)
  {
    System.err.println("Usage: ");
    StringTokenizer st = new StringTokenizer(argtemplate, ":\n");

    while( st.hasMoreTokens() ) {
      String key = st.nextToken();
      String typestr = st.nextToken();
      String usagestr = st.nextToken();

      String utypestr = (String)gTypeTab.get(typestr);
      if (utypestr == null) utypestr = typestr;

      int len = 4;
      System.err.write(spaces, 0, 4);

      if (!key.equals(TOK_required)) {
	System.err.print(key);
	len += key.length();
	if ((20-len) > 0) {
	  System.err.write(spaces, 0, 20-len);
	  len = 20;
	}
	else
	  System.err.print(" ");
      }

      System.err.print("<"+usagestr+">");
      len += (usagestr.length()+2);

      if (len < 65) 
	System.err.write(spaces, 0, 65-len);
      else {
	System.err.println("");
	System.err.write(spaces,0, 20);
      }
      System.err.println("(" + utypestr + ")");
    }
  } //usageMessage

  //----------------------------------------------------------------

  /** a sample main for testing the ArgumentParser parser
   */
  public static void main(String[] cmdline)
  {
    try {
      ArgumentParser args = new ArgumentParser(cmdline);

      Hashtable h = args.parse("+:str:path to file:" +
			       "-framenum:int:frame number:" +
			       "+:str:username:" +
			       "-f:int:framerate:" +
			       "-v:flg:verbose");

      if (h == null) 
	System.exit(1);

      {
	// get required arguments 1 and 2
	String username = (String)h.get(new Integer(1));
	String path = (String)h.get(new Integer(0));

    // get optional integer argument bound to "-framenum"
	int framenum = -1;
	if (h.get("-framenum") != null)
	  framenum = ((Integer)h.get("-framenum")).intValue();

    // get optional integer argument bound to "-f"
	int framerate = -1;
	if (h.get("-f") != null)
	  framerate = ((Integer)h.get("-f")).intValue();

    // get optional flag argument "-v"
	boolean verbose = false;
	if (h.get("-v") != null)
	  verbose = ((Boolean)h.get("-v")).booleanValue();

    // print results
	System.out.println("path = " + path);
	System.out.println("username = " + username);
	System.out.println("framenum = " + framenum);
	System.out.println("verbose = " + verbose);
      }

      {
    // also test using our access wrapper functions
	String Path = args.getRequiredString(0);
	String Username = args.getRequiredString(1);
	Integer Framenum = args.getOptionalInteger("-framenum");
	Integer Framerate = args.getOptionalInteger("-f");
	Boolean Verbose = args.getFlag("-v");
    
	System.out.println("\nrepeat using access functions");
	System.out.println("path = " + Path);
	System.out.println("username = " + Username);
	if (Framenum != null)
	  System.out.println("framenum = " + Framenum);
	if (Framerate != null)
	  System.out.println("framerate = " + Framerate);
	System.out.println("verbose = " + Verbose);
      }
    }

    catch (ArgumentParserException ex) {
      System.err.println(ex.getMessage());
    }

  } //main
}

