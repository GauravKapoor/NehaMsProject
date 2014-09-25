package com.election;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author prathap
 */
import org.apache.commons.codec.binary.Base64;
import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class TripleDES {
	//Chnages done by Gaurav
	public static String secretkey="5869";
	
    /*public static void main(String[] args) throws Exception {

    	String text = "vh1234";

    	byte[] codedtext = new TripleDES().encrypt(text);

        System.out.println("encoded bytes "+codedtext);
        
        String str1 = bytetostr(codedtext);
        
        //Convert String back to Byte[] and decrpt
        byte[] byteMessage = strtobyte(str1);
        
        System.out.println("ENCRYPTED MESSAGE byte Length: "+byteMessage.length);
        
        System.out.println("encoded bytes "+byteMessage);
        
    	String decodedtext = new TripleDES().decrypt(byteMessage);

    //	System.out.println(codedtext); // this is a byte array, you'll just see a reference to an array
    	System.out.println(decodedtext); // This correctly shows "kyle boon"
    }*/

    
    public static String bytetostr(byte[] ct){
       String stringMessage=null;
        try{
        byte[] encryptedMsg = Base64.encodeBase64(ct);

        System.out.println("ENCRYPTED MESSAGE byte Length: "+encryptedMsg.length);

        //Convert to String in order to send
        stringMessage = new String(encryptedMsg, "UTF-8");
        
       
       }catch(Exception e){
           e.printStackTrace();
       }
        return stringMessage;
    }
    
    public static byte[] strtobyte(String str) throws Exception{
        return Base64.decodeBase64(str.getBytes("UTF-8"));
    }
    public static byte[] encrypt(String message) throws Exception {
    	final MessageDigest md = MessageDigest.getInstance("md5");
    	final byte[] digestOfPassword = md.digest(secretkey
    			.getBytes("utf-8"));
    	final byte[] keyBytes = TripleDES.copyOf(digestOfPassword, 24);
    	for (int j = 0, k = 16; j < 8;) {
    		keyBytes[k++] = keyBytes[j++];
    	}

    	final SecretKey key = new SecretKeySpec(keyBytes, "DESede");
    	final IvParameterSpec iv = new IvParameterSpec(new byte[8]);
    	final Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
    	cipher.init(Cipher.ENCRYPT_MODE, key, iv);

    	final byte[] plainTextBytes = message.getBytes("utf-8");
    	final byte[] cipherText = cipher.doFinal(plainTextBytes);
    	// final String encodedCipherText = new sun.misc.BASE64Encoder()
    	// .encode(cipherText);

    	return cipherText;
    }
    
    public static byte[] reencrypt(String message) throws Exception {
    	final MessageDigest md = MessageDigest.getInstance("md5");
    	final byte[] digestOfPassword = md.digest(secretkey
    			.getBytes("utf-8"));
    	final byte[] keyBytes = TripleDES.copyOf(digestOfPassword, 24);
    	for (int j = 0, k = 16; j < 8;) {
    		keyBytes[k++] = keyBytes[j++];
    	}

    	final SecretKey key = new SecretKeySpec(keyBytes, "DESede");
    	final IvParameterSpec iv = new IvParameterSpec(new byte[8]);
    	final Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
    	cipher.init(Cipher.ENCRYPT_MODE, key, iv);

    	final byte[] plainTextBytes = message.getBytes("utf-8");
    	final byte[] cipherText = cipher.doFinal(plainTextBytes);
    	// final String encodedCipherText = new sun.misc.BASE64Encoder()
    	// .encode(cipherText);

    	return cipherText;
    }

    public static String decrypt(byte[] message) throws Exception {
    	final MessageDigest md = MessageDigest.getInstance("md5");
    	final byte[] digestOfPassword = md.digest(secretkey
    			.getBytes("utf-8"));
    	final byte[] keyBytes = TripleDES.copyOf(digestOfPassword, 24);
    	for (int j = 0, k = 16; j < 8;) {
    		keyBytes[k++] = keyBytes[j++];
    	}

    	final SecretKey key = new SecretKeySpec(keyBytes, "DESede");
    	final IvParameterSpec iv = new IvParameterSpec(new byte[8]);
    	final Cipher decipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
    	decipher.init(Cipher.DECRYPT_MODE, key, iv);

    	// final byte[] encData = new
    	// sun.misc.BASE64Decoder().decodeBuffer(message);
    	final byte[] plainText = decipher.doFinal(message);

    	return new String(plainText, "UTF-8");
    }
    
    public static String redecrypt(byte[] message) throws Exception {
    	final MessageDigest md = MessageDigest.getInstance("md5");
    	final byte[] digestOfPassword = md.digest(secretkey
    			.getBytes("utf-8"));
    	final byte[] keyBytes = TripleDES.copyOf(digestOfPassword, 24);
    	for (int j = 0, k = 16; j < 8;) {
    		keyBytes[k++] = keyBytes[j++];
    	}

    	final SecretKey key = new SecretKeySpec(keyBytes, "DESede");
    	final IvParameterSpec iv = new IvParameterSpec(new byte[8]);
    	final Cipher decipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
    	decipher.init(Cipher.DECRYPT_MODE, key, iv);

    	// final byte[] encData = new
    	// sun.misc.BASE64Decoder().decodeBuffer(message);
    	final byte[] plainText = decipher.doFinal(message);

    	return new String(plainText, "UTF-8");
    }
    
    public static String padding(String result) {
        String padd_result = result + "#";

        String pad = "";
        System.out.println("password length=" + padd_result.length());

        if (padd_result.length() != 200) {

            System.out.println(padd_result.length());
            for (int i = padd_result.length(); i < 200; i++) {
                pad = pad + "X";
            }
        }
        padd_result = padd_result + pad;

        System.out.println("password length=" + padd_result.length());

        return padd_result.trim();
    }
    
    public static byte[] copyOf(byte[] original, int newLength) {
        byte[] copy = new byte[newLength];
        System.arraycopy(original, 0, copy, 0,
                         Math.min(original.length, newLength));
        return copy;
    }
}
