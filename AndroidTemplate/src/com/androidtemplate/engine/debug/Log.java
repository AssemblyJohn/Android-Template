package com.androidtemplate.engine.debug;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;


/**
 * Replaces standard logging. Can be disabled globally.
 * 
 * @author Lazu Ioan-Bogdan
 *
 */
public class Log {
	public static final int LEVEL_WTF 		= 1;
	public static final int LEVEL_ERROR 	= 2;
	public static final int LEVEL_WARNING 	= 3;
	public static final int LEVEL_DEBUG 	= 4;
	public static final int LEVEL_VERBOSE 	= 5;
	public static final int LEVEL_INFO 		= 6;
	
	/* When we are in debug mode. Should be set to false before release. */
	public static boolean DEBUG_MODE = true;	
	
	public static final byte DEBUG_LEVEL = LEVEL_INFO;

	@SuppressWarnings("unused")
	private static final boolean INFO		= ((DEBUG_LEVEL >= 6) ? true : false) && DEBUG_MODE;
	@SuppressWarnings("unused")
	private static final boolean VERBOSE 	= ((DEBUG_LEVEL >= 5) ? true : false) && DEBUG_MODE;
	@SuppressWarnings("unused")
	private static final boolean DEBUG 		= ((DEBUG_LEVEL >= 4) ? true : false) && DEBUG_MODE;
	@SuppressWarnings("unused")
	private static final boolean WARNING 	= ((DEBUG_LEVEL >= 3) ? true : false) && DEBUG_MODE;
	@SuppressWarnings("unused")
	private static final boolean ERROR 		= ((DEBUG_LEVEL >= 2) ? true : false) && DEBUG_MODE;
	@SuppressWarnings("unused")
	private static final boolean WTF 		= ((DEBUG_LEVEL >= 1) ? true : false) && DEBUG_MODE;
	
	public static void print(int priority, String tag, String msg) {
		if(DEBUG_MODE)
			android.util.Log.println(priority, tag, msg);
	}
	
	public static void i(String tag, String msg) {
		if(INFO) {
			android.util.Log.i(tag, msg);
		}
	}
	
	public static void v(String tag, String msg) {
		if(VERBOSE) {
			android.util.Log.v(tag, msg);
		}
	}
	
	public static void d(String tag, String msg) {
		if(DEBUG)
			android.util.Log.d(tag, msg);
	}
	
	public static void w(String tag, String msg) {
		if(WARNING) {
			android.util.Log.w(tag, msg);
		}
	}
	
	public static void e(String tag, String msg) {
		if(ERROR) {
			android.util.Log.e(tag, msg);
		}
	}
	
	@SuppressLint("NewApi")
	public static void wtf(String tag, String msg) {
		if(WTF) {
			android.util.Log.wtf(tag, msg);
		}
	}
	
	public static void debugToast(Context c, String text) {
		if(DEBUG) {
			try {
				Toast.makeText(c, text, Toast.LENGTH_SHORT).show();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void debugToast(Context c, String tag, String text) {
		if(DEBUG) {
			try {
				Toast.makeText(c, tag + " " + text, Toast.LENGTH_SHORT).show();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
