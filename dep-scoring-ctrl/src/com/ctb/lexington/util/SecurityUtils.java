package com.ctb.lexington.util;

/*
 * SecurityUtils.java
 *
 * Copyright CTB/McGraw-Hill, 2002
 * CONFIDENTIAL
 */

import java.security.MessageDigest;

/**
 * <P>
 * This utility class will be used to encapsulate methods for hashing
 * passwords and validating them.
 * </P>
 *
 * @author Dave Fuller
 * @version $Id$
 */
public final class SecurityUtils
{

   /**
    * Used to encode passwords using the SHA-1 message digest algorithm.
    *
    * @param password  The <code>String</code> to get the hash value of.
    * @return      A <code>String</code> that is the SHA-1 message digest of
    * the password parameter.
    */
    public static String encodePassword( String password ) {
        MessageDigest md;
        StringBuffer retval = new StringBuffer( "" );
        byte[] hash = new byte[]{};
        try 
        {
            md = MessageDigest.getInstance("MD5");
            md.update( password.getBytes() );
            hash = md.digest();
        }
        catch (Exception e ) 
        {
            e.printStackTrace();
        }
        for (int i=0; i < hash.length; ++i )
        {
            //retval.append( Integer.toHexString( hash[i] ) );
            if (((int) hash[i] & 0xff) < 0x10) 
            {
                retval.append("0");
            }
            retval.append(Long.toString((int) hash[i] & 0xff, 16));

        }

        return retval.toString();
    }
}

/*
 * $Log$
 * Revision 1.1  2007/01/30 01:31:45  ncohen
 * port scoring to 4.x platform
 *
 * Revision 1.1  2006/02/23 20:48:28  ncohen
 * create new module for legacy OAS app
 *
 * Revision 1.5  2002/09/05 20:27:20  oasuser
 * Clobbered trunk with code from ATSR1 branch.
 *
 * Revision 1.3.4.1  2002/08/22 18:32:01  jshields
 * Fixed control-M problems.
 *
 * Revision 1.3  2002/08/20 22:51:09  oasuser
 * Merged build-R1-20020813-161424
 *
 * Revision 1.2  2002/05/21 18:38:56  oasuser
 * Committing to the trunk
 *
 * Revision 1.1.2.2  2002/04/09 18:05:15  dfuller
 * Updated to better handle hash to String conversion.
 *
 * Revision 1.1.2.1  2002/03/27 05:24:08  dfuller
 * Initial check-in.
 *
 */
