package com.androidtemplate.engine.commons.cryptography;

import com.androidtemplate.engine.commons.cryptography.base.THashing;
import com.androidtemplate.engine.commons.cryptography.base.TCrypting;

/**
 * Cryptography utils.
 * 
 * @author Lazu Ioan-Bogdan
 *
 */
public class TCryptUtils {
	// If the should throw exceptions in case of error, or if we should only print the stack trace.
	public static final boolean DEBUG_VIOLENT = false;
	
	public static byte[] secureKey() {
		return TCrypting.obtainRandomKey();
	}
	
	public static String decryptToString(byte data[], byte key[]) {
		return TCrypting.decryptToString(data, key);
	}
	
	public static byte[] decryptToByte(byte data[], byte key[]) {
		return TCrypting.decryptToByte(data, key);
	}
	
	public static byte[] cryptFromString(String data, byte key[]) {
		return TCrypting.cryptFromString(data, key);
	}
	
	public static byte[] cryptFromByte(byte data[], byte key[]) {
		return TCrypting.cryptFromByte(data, key);
	}
	
	public static String byteToHexString(byte data[]) {
		return THashing.byteToHexString(data);
	}

	
}
