package com.androidtemplate.engine.debug;


public class TVersionInfo {
	/** The api verion of the device running our app */
	public static int THIS_MOBILE_API_VERSION  = android.os.Build.VERSION.SDK_INT;
	
	/** Android 2.2 */
	public static int API_10 = android.os.Build.VERSION_CODES.GINGERBREAD_MR1;
	/** Android 3.0, Honeycomb, has action bar */
	public static int API_11 = android.os.Build.VERSION_CODES.HONEYCOMB;
	public static int API_12 = android.os.Build.VERSION_CODES.HONEYCOMB_MR1;
	public static int API_13 = android.os.Build.VERSION_CODES.HONEYCOMB_MR2;
	/** Android 4.0 */
	public static int API_14 = android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH;
	public static int API_15 = android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1;
	public static int API_16 = android.os.Build.VERSION_CODES.JELLY_BEAN;
	public static int API_17 = android.os.Build.VERSION_CODES.JELLY_BEAN_MR1;
	public static int API_18 = android.os.Build.VERSION_CODES.JELLY_BEAN_MR2;
	public static int API_19 = android.os.Build.VERSION_CODES.KITKAT;
	
	public static boolean hasHoneycomb() {
		return THIS_MOBILE_API_VERSION >= API_11 ? true : false;
	}

	public static boolean hasHoneycombMR1() {
		return THIS_MOBILE_API_VERSION >= API_12 ? true : false;
	}

	public static boolean hasKitkat() {
		return THIS_MOBILE_API_VERSION >= API_19 ? true : false;
	}
}
