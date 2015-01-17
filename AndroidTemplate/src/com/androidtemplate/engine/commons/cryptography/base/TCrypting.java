package com.androidtemplate.engine.commons.cryptography.base;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.androidtemplate.engine.commons.cryptography.TCryptUtils;
import com.androidtemplate.engine.debug.Log;

/**
 * Crypting. An encrypted byte array can be decrypted.
 * 
 * @author Lazu Ioan-Bogdan
 *
 */
public class TCrypting {
	public static final boolean DEBUG_VIOLENT = TCryptUtils.DEBUG_VIOLENT;
	private static final boolean DEBUG = Log.DEBUG_MODE;
	
	private static final String ALGORITHM = "AES";
	private static final String CYPHER = "AES/ECB/PKCS5Padding";
	private static final String ENCODING = "UTF-8";
	
	public static byte[] obtainRandomKey() {
		SecretKey aesKey = null;
		
		try {
			KeyGenerator keygen = KeyGenerator.getInstance(ALGORITHM);
			aesKey = keygen.generateKey();
		} catch (NoSuchAlgorithmException e) {
			if(DEBUG) e.printStackTrace();
		}
		
		return aesKey.getEncoded();
	}
	
	public static byte[] decryptToByte(byte data[], byte key[]) {
		SecretKey aesKey = new SecretKeySpec(key, ALGORITHM);
	    
		byte value[]= null;
	    
		try {
	    	Cipher aesCipher = Cipher.getInstance(CYPHER);
			aesCipher.init(Cipher.DECRYPT_MODE, aesKey);
		    value = aesCipher.doFinal(data);
		} catch (Exception e) {
			if(DEBUG) e.printStackTrace();
		}
		
	    return value;
	}
	
	public static String decryptToString(byte data[], byte key[]) {
		String value = null;
		
	    try {
			value = new String(decryptToByte(data, key), ENCODING);
		} catch (UnsupportedEncodingException e) {
			if(DEBUG) e.printStackTrace();
		}
	    
	    return value;
	}
	
	public static byte[] cryptFromByte(byte data[], byte key[]) {
		SecretKey aesKey = new SecretKeySpec(key, ALGORITHM);

	    byte value[] = null;
	    
		try {
			Cipher aesCipher = Cipher.getInstance(CYPHER);
		    aesCipher.init(Cipher.ENCRYPT_MODE, aesKey);
		    value = aesCipher.doFinal(data);
		} catch (Exception e) {
			if(DEBUG) e.printStackTrace();
		}

	    return value;
	}
	
	public static byte[] cryptFromString(String data, byte key[]) {
		byte crypted[] = null;
		
	    try {
	    	crypted = cryptFromByte(data.getBytes(ENCODING), key);
		} catch (UnsupportedEncodingException e) {
			if(DEBUG) e.printStackTrace();
		}
	    
	    return crypted;
	}
}