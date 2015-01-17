package com.androidtemplate.engine.commons.cryptography.base;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

import com.androidtemplate.engine.commons.cryptography.TCryptUtils;
import com.androidtemplate.engine.debug.Log;

/**
 * Hashing. An hashed byte array cannot be de-hashed.
 * 
 * @author Lazu Ioan-Bogdan
 *
 */
public class THashing {
	private static final boolean DEBUG_VIOLENT = TCryptUtils.DEBUG_VIOLENT; 
	private static boolean DEBUG = Log.DEBUG_MODE;
	
	private static final String HASH = "SHA1";
	private static final String ENCODING = "UTF-8";
	
	
	protected static byte[] digestString(String string) {
		byte data[];
		try {
			data = string.getBytes(ENCODING);
			byte sha[] = digestBytes(data);
			
			return sha;
		} catch (UnsupportedEncodingException e) {
			if(DEBUG) e.printStackTrace();
			throw new RuntimeException("Invalid encoding!");
		}
	}
	
	/**
	 * Will digest the provided bytes.
	 */
	protected static byte[] digestBytes(byte data[]) {
		try {
			MessageDigest digester = MessageDigest.getInstance(HASH);
			byte sha[] = digester.digest(data);
			
			return sha;
		} catch (Exception e) {
			if(DEBUG) e.printStackTrace();
		}
		
		return null;
	}
	
	public static String byteToHexString(byte data[]) {
		StringBuilder builder = new StringBuilder(20);
		
		for (int i = 0; i < data.length; i++) {
			builder.append(String.format("%02x", data[i]));
		}
	    
	    return builder.toString();
	}	
}
