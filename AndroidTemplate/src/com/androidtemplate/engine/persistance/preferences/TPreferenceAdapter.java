package com.androidtemplate.engine.persistance.preferences;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

import com.androidtemplate.engine.commons.jsmp.Jsmp;
import com.androidtemplate.engine.debug.Log;

/**
 * The adapter must be initialized at some point. If 
 * it is not, it will throw null pointers around.
 * 
 * @author Lazu Ioan-Bogdan
 *
 */
public abstract class TPreferenceAdapter {
	private static final String TAG = TPreferenceAdapter.class.getSimpleName().toUpperCase();
	
	private Context mContext;
	private String mPreferencesName;
	private SharedPreferences mPreferences;

	// We have to use this list here because the shared preferences will have
	// weak pointers to them. That's why we need strong pointers to them here...
	private List<OnSharedPreferenceChangeListener> mListeners;

	private boolean mInitialized = false;

	public TPreferenceAdapter(Context context) {
		mContext = context;
	}

	public boolean isInitialized() {
		return mInitialized;
	}

	/**
	 * Init the preferences
	 */
	public void init(String name) {
		if(mInitialized) {
			Log.w(TAG, "Preferences already initialized! Deinitializing!");
			deinit();
		}

		mPreferencesName = name;

		mPreferences = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
		mListeners = new ArrayList<OnSharedPreferenceChangeListener>();

		mInitialized = true;
	}

	public void deinit() {
		mPreferencesName = null;
		mPreferences = null;

		if(mListeners != null) mListeners.clear();
		mListeners = null;

		mInitialized = false;
	}

	public String getPreferencesName() {
		return mPreferencesName;
	}

	public synchronized void registerPreferencesListener(
			OnSharedPreferenceChangeListener listener) {

		mListeners.add(listener);
		mPreferences.registerOnSharedPreferenceChangeListener(listener);
	}

	public synchronized void unregisterPreferencesListener(
			OnSharedPreferenceChangeListener listener) {

		mListeners.remove(listener);
		mPreferences.unregisterOnSharedPreferenceChangeListener(listener);
	}

	/**
	 * Persist an object in JSMP form. It's class
	 * simple name will be used as a key.
	 * 
	 * @param data
	 * 				Object to persist in JSMP form
	 */
	public void putJsmpObjectAutocommit(Object data) {
		String key = data.getClass().getSimpleName();
		String json = Jsmp.toJson(data);

		// Persist it
		putStringAutocommit(json, key);
	}

	/**
	 * Retrieve an JSMP object from the preferences that was previously
	 * persisted.
	 *  
	 * @param clazz
	 * 				Class representing this object
	 * @return	The object from the preferences or null if no mapping could be found.
	 */
	public <T> T getJsmpObject(Class<T> clazz) {
		String key = clazz.getSimpleName();
		String json = mPreferences.getString(key, null);

		if(json != null) {
			return Jsmp.fromJson(clazz, json);
		}

		return null;
	}

	public <T> boolean hasObject(Class<T> clazz) {
		String key = clazz.getSimpleName();

		return (mPreferences.getString(key, null) != null) ? true : false;
	}

	/**
	 * Places in the preferences a value that is either
	 * a int, long, float, boolean or string and commits.
	 *  
	 * @param value
	 * 				Primitive value to put
	 * @param key
	 * 				Key for the value
	 */
	public void putPrimitiveAutocommit(Object value, String key) {
		Class<?> clazz = value.getClass();

		Editor mEditor = getEditor();
		
		if(clazz.equals(int.class) || clazz.equals(Integer.class)) {
			mEditor.putInt(key, (Integer)value);
		} else if(clazz.equals(long.class) || clazz.equals(Long.class)) {
			mEditor.putLong(key, (Long)value);
		} else if(clazz.equals(float.class) || clazz.equals(float.class)) {
			mEditor.putFloat(key, (Float)value);
		} else if(clazz.equals(boolean.class) || clazz.equals(Boolean.class)) {
			mEditor.putBoolean(key, (Boolean)value);
		} else if(clazz.equals(String.class)) {
			mEditor.putString(key, (String)value);
		} else {
			throw new IllegalArgumentException("Invalid primitive type! Must be int, long, float or boolean!");
		}
		
		mEditor.commit();
	}

	/** Returns a the int or -1 in case it was not found */
	public int getInt(String key) {
		return mPreferences.getInt(key, -1);
	}
	
	public void putFloatAutocommit(float value, String key) {
		getEditor().putFloat(key, value).commit();
	}

	public void putBooleanAutocommit(boolean value, String key) {
		getEditor().putBoolean(key, value).commit();
	}

	public void putLongAutocommit(long value, String key) {
		getEditor().putLong(key, value).commit();
	}

	public void putIntAutocommit(int value, String key) {
		getEditor().putInt(key, value).commit();
	}

	public void putStringAutocommit(String value, String key) {
		getEditor().putString(key, value).commit();
	}
	
	private Editor getEditor() {
		return mPreferences.edit();
	}
}
