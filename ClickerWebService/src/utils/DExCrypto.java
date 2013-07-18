package utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import sun.misc.BASE64Encoder;


 

/**
 * -----------------------------------------------------------------------------
 * The following example implements a class for encrypting and decrypting
 * strings using several Cipher algorithms. The class is created with a key and
 * can be used repeatedly to encrypt and decrypt strings using that key.
 * Some of the more popular algorithms are:
 *      Blowfish
 *      DES
 *      DESede
 *      PBEWithMD5AndDES
 *      PBEWithMD5AndTripleDES
 *      TripleDES
 * 
 * @version 1.0
 * @author  Tata Consultancy Services Ltd.
 * -----------------------------------------------------------------------------
 */

public class DExCrypto {
	/**
	 * Cipher object used for encrypting the string
	 */
    private Cipher ecipher;
	/**
	 * Cipher object used for decrypting the string
	 */    
    private Cipher dcipher;

    /**
     * Constructor used to create this object.  Responsible for setting
     * and initializing this object's encrypter and decrypter Chipher instances
     * given a Secret Key and algorithm.
     * @param key        Secret Key used to initialize both the encrypter and
     *                   decrypter instances.
     * @param algorithm  Which algorithm to use for creating the encrypter and
     *                   decrypter instances.
     */
    DExCrypto(SecretKey key, String algorithm) {
        try {
            ecipher = Cipher.getInstance(algorithm);
            dcipher = Cipher.getInstance(algorithm);
            ecipher.init(Cipher.ENCRYPT_MODE, key);
            dcipher.init(Cipher.DECRYPT_MODE, key);
        } catch (NoSuchPaddingException e) {
        	e.printStackTrace();
            //System.out.println("EXCEPTION: NoSuchPaddingException");
        } catch (NoSuchAlgorithmException e) {
        	e.printStackTrace();
            //System.out.println("EXCEPTION: NoSuchAlgorithmException");
        } catch (InvalidKeyException e) {
        	e.printStackTrace();
            //System.out.println("EXCEPTION: InvalidKeyException");
        }
    }

    /**
     * Constructor used to create this object.  Responsible for setting
     * and initializing this object's encrypter and decrypter Chipher instances
     * given a Pass Phrase and algorithm.
     * @param passPhrase Pass Phrase used to initialize both the encrypter and
     *                   decrypter instances.
     * @throws Exception , Throws Base Exception if business logic failed.
     */
    DExCrypto(String passPhrase) throws Exception {
        // 8-bytes Salt
        byte[] salt = {
            (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32,
            (byte) 0x56, (byte) 0x34, (byte) 0xE3, (byte) 0x03
        };
        // Iteration count
        int iterationCount = 19;
        try {
            KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);
            SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
            ecipher = Cipher.getInstance(key.getAlgorithm());
            dcipher = Cipher.getInstance(key.getAlgorithm());
            // Prepare the parameters to the cipthers
            AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
            ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
            dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
        } catch (InvalidAlgorithmParameterException e) {
        	    throw new Exception(e.getMessage());
        } catch (InvalidKeySpecException e) {
        	    throw new Exception(e.getMessage());
        } catch (NoSuchPaddingException e) {
        	    throw new Exception(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
        	    throw new Exception(e.getMessage());
        } catch (InvalidKeyException e) {
        	    throw new Exception(e.getMessage());
        }
    }

    /**
     * Takes a single String as an argument and returns an Encrypted version
     * of that String.
     * @param str String to be encrypted
     * @return <code>String</code> Encrypted version of the provided String
     * @throws Exception , Throws Exception if business logic fails.
     */
    private String encrypt(String str) throws Exception {
        try {
            // Encode the string into bytes using utf-8
            byte[] utf8 = str.getBytes("UTF8");
            // Encrypt
            byte[] enc = ecipher.doFinal(utf8);
            // Encode bytes to base64 to get a string
            return new sun.misc.BASE64Encoder().encode(enc);
        } catch (BadPaddingException e) {
        	    throw new Exception(e.getMessage());
        } catch (IllegalBlockSizeException e) {
        	    throw new Exception(e.getMessage());
        } catch (UnsupportedEncodingException e) {
        	    throw new Exception(e.getMessage());
        } catch (Exception e) {
        	    throw new Exception(e.getMessage());
        }

    }

    /**
     * Takes a encrypted String as an argument, decrypts and returns the 
     * decrypted String.
     * @param str Encrypted String to be decrypted
     * @return <code>String</code> Decrypted version of the provided String
     * @throws Exception , Throws Exception if business logic fails.
     */
    private String decrypt(String str) throws Exception {
        try {
            // Decode base64 to get bytes
            byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(str);
            // Decrypt
            byte[] utf8 = dcipher.doFinal(dec);
            // Decode using utf-8
            return new String(utf8, "UTF8");
        } catch (BadPaddingException e) {
        	    throw new Exception(e.getMessage());
        } catch (IllegalBlockSizeException e) {
        	    throw new Exception(e.getMessage());
        } catch (UnsupportedEncodingException e) {
        	    throw new Exception(e.getMessage());
        } catch (IOException e) {
        	    throw new Exception(e.getMessage());
        }
    }
    /**
     * This method encrypt the string using PassPhrase two way algorithm
     * @param secretString origional string which we have to encrypt
     * @return encrypt string
     * @throws Exception , Throws Exception if business logic fails.
     */
    public static String encryptUsingPassPhrase(String secretString) throws Exception {
    	  String passPhrase   = "CTBGROWDUT123&&";
    	  DExCrypto dexEncrypter = new DExCrypto(passPhrase);
    	  return dexEncrypter.encrypt(secretString);
    }
		/**
		 * This method decrypt the string using Pass phrase two way algorithm 
		 * @param secretString encrypted string which we have to decrypt
		 * @return original string
		 * @throws Exception , Throws Exception if business logic fails.
		 */    
    public static String decryptUsingPassPhrase(String secretString) throws Exception {
    	  String passPhrase   = "CTBGROWDUT123&&";
    	  DExCrypto dexEncrypter = new DExCrypto(passPhrase);
    	  return dexEncrypter.decrypt(secretString);
    }    
		/**
		 * This method encrypt the string using MD5 one-way algorithm
		 * @param source String which we have to encrypt
		 * @return encrypted string
		 * @throws Exception , Throws Exception if business logic fails.
		 */
    public static String encryptUsingMD5(String source) throws Exception {
        try {
            MessageDigest md = MessageDigest.getInstance ("MD5");
            byte[] bytes = md.digest (source.getBytes());
            return (new BASE64Encoder()).encode(bytes);
        } catch (Exception e) {
      	    throw new Exception(e.getMessage());
        }
    }
		/**
		 * This method encrypt the string using SHA one-way algorithm
		 * @param source String which we have to encrypt
		 * @return encrypted string
		 * @throws Exception
		 */
    public static String encryptUsingSHA (String source) {
        try {
            MessageDigest md = MessageDigest.getInstance ("SHA");
            byte[] bytes = md.digest (source.getBytes());
            return (new BASE64Encoder()).encode(bytes);       
        } catch (Exception e) {
      		  e.printStackTrace();
      		  return null;
        }
    }    
    
      
}

