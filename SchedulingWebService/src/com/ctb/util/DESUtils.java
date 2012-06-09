package com.ctb.util; 
/*
 * DESUtils.java
 *
 * @author John_Wang
 */



import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import weblogic.utils.encoders.BASE64Encoder;

public class DESUtils 
{ 
    
    public static String encrypt(String input, String key) {
        String output = null;
        try {  
            // Create Key 
            KeySpec keySpec = new DESKeySpec(key.getBytes());
            SecretKey secretKey = SecretKeyFactory.getInstance("DES").generateSecret(keySpec);
            
              // Create Cipher 
            Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding"); 
            desCipher.init(Cipher.ENCRYPT_MODE, secretKey); 
            
            // Encrypt
            String outputStr = new BASE64Encoder().encodeBuffer(desCipher.doFinal(input.getBytes()));
            output = URLEncoder.encode(outputStr);
            OASLogger.getLogger("TestAdmin").debug("DESUtils.encrypt input: " + input + ", output: " + output);       
        } catch (NoSuchPaddingException e) { 
        System.err.println("Padding problem: " + e); 
        } catch (NoSuchAlgorithmException e) { 
        System.err.println("Invalid algorithm: " + e); 
        } catch (InvalidKeyException e) { 
        System.err.println("Invalid key: " + e); 
        } catch (IllegalStateException e) {
        System.err.println("Illegal State Problem: " + e);
        } catch (IllegalBlockSizeException e) {
        System.err.println("Block Size Problem: " + e);
        } catch (BadPaddingException e) {
        System.err.println("Padding Problem: " + e);
        } catch (InvalidKeySpecException e) {
        System.err.println("KeySpec Problem: " + e);
        }   
        return output;     
        
    }
} 
