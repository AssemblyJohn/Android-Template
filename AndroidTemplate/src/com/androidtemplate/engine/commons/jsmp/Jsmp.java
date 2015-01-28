package com.androidtemplate.engine.commons.jsmp;

import java.lang.reflect.Array;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.androidtemplate.engine.commons.jsmp.JsmpReflection.FieldWrapper;
import com.androidtemplate.engine.commons.jsmp.JsmpReflection.Type;
import com.androidtemplate.engine.debug.Log;

/**
 * Lightweight class that converts a java object to JSON
 * and vice-versa. Supports primitive values, strings, enums
 * primitive arrays, objects or object arrays.
 * 
 * <p>Note: All objects that are used by this class must have an 
 * default no-arg constructor.
 * 
 * <p>Set the DEBUG to true field if debug is required.
 * 
 * @author Lazu Ioan-Bogdan
 */
public class Jsmp {
	protected static final String TAG = "JSMP";
	
	public static boolean DEBUG = true;
	
	/**
	 * Converts an JSON string to a instance of the
	 * provided class.
	 * 
	 * The provided class must have and empty constructor
	 * so we can instantiate it.
	 * 
	 * @param template
	 * 				Class type
	 * @param json
	 * 				String representing the JSON object
	 * @return	A new valid instance or null if any error occurred.
	 */
	public static <T> T fromJson(Class<T> template, String json) {
		if(json == null) {
			return null;
		}
		
		T var = null;
		JSONObject object = null;
		
		try {			
			// New instance
			var = JsmpReflection.instance(template);
			// Initialize JSON object
			object = new JSONObject(json);
		} catch (JSONException e) {
			if(DEBUG) e.printStackTrace();
			return null;
		}

		// Extract the template's fields
		List<FieldWrapper> fields = JsmpReflection.extract(template, null);
			
		FieldWrapper field;
		for (int i = 0; i < fields.size(); i++) {
			try {
				field = fields.get(i);
				
				switch(field.type) {
				case Type.TYPE_PRIMITIVE: {
					field.value = getValueForPrimitive(object, 
							field.classType, field.name);

					JsmpReflection.put(var, field);					
					break;
				}
				case Type.TYPE_PRIMITIVE_ARRAY: {
					JSONArray array = object.getJSONArray(field.name);
					Object arr = Array.newInstance(field.classType.getComponentType(), 
							array.length());
					
					for(int j = 0; j < array.length(); j++) {
						Object val = getValueForPrimitive(array, 
								field.classType.getComponentType(), j);
						Array.set(arr, j, val);
					}
					
					field.value = arr;
					JsmpReflection.put(var, field);
					break;
				}
				case Type.TYPE_OBJECT: {
					field.value = fromJson(field.classType, 
							object.getString(field.name));
					
					JsmpReflection.put(var, field);
					break;
				}
				case Type.TYPE_OBJECT_ARRAY: {
					JSONArray array = object.getJSONArray(field.name);
					Object arr = Array.newInstance(field.classType.getComponentType(), 
							array.length());
					
					for(int j = 0; j < array.length(); j++) {						
						Object val = fromJson(field.classType.getComponentType(), 
								array.getString(j));
						Array.set(arr, j, val);
					}
					
					field.value = arr;
					JsmpReflection.put(var, field);
					break;
				}
				}
			} catch(JSONException e) {
				if(DEBUG) e.printStackTrace();
			}
		}			
		
		return var;
	}	
	
	/**
	 * Serializes a object to a json string.
	 * 
	 * @param object
	 * 				Object to serialize as JSON
	 * @return	The string representing the serialized object
	 * 			or null if it couldn't be serialized.
	 */
	public static String toJson(Object object) {
		JSONObject json = innerToJson(object);
		
		return json.toString();
	}
	
	private static JSONObject innerToJson(Object object) {
		// First extract fields, names and such
		List<FieldWrapper> values = JsmpReflection.extract(object);

		if(values.size() == 0) {
			if(DEBUG) Log.w(TAG, "Object without fields provided!");
		}

		// Build the JSON object based on the value and name
		JSONObject json = new JSONObject();

		FieldWrapper field;
		for (int i = 0; i < values.size(); i++) {
			try {
				field = values.get(i);

				// Field value
				Object value = field.value;

				switch(field.type) {
				case Type.TYPE_PRIMITIVE: {
					json.put(field.name, value);
					break;
				}
				case Type.TYPE_PRIMITIVE_ARRAY: {
					// If the value is null, break, don't add
					if(value == null) break;

					JSONArray array = new JSONArray();
					final int length = Array.getLength(value);

					for (int j = 0; j < length; ++j) {
						array.put(Array.get(value, j));
					}

					json.put(field.name, array);
					break;
				}
				case Type.TYPE_OBJECT: {
					json.put(field.name, innerToJson(value));
					break;
				}
				case Type.TYPE_OBJECT_ARRAY: {
					// If value is null break, don't add
					if(value == null) break;

					JSONArray array = new JSONArray();
					final int length = Array.getLength(value);

					for (int j = 0; j < length; ++j) {
						array.put(innerToJson(Array.get(value, j)));
					}

					json.put(field.name, array);
					break;
				}
				}				
			} catch (JSONException e) {
				if(DEBUG) e.printStackTrace();
			}
		}
		
		return json;
	}
	
	private static Object getValueForPrimitive(JSONArray object, 
			Class<?> classType, int index) throws JSONException  {
		Object value = null;
		
		if(JsmpReflection.isPrimitive(classType)) {
			if(JsmpReflection.isBoolean(classType)) {
				value = object.getBoolean(index);
			} else if(JsmpReflection.isByte(classType)) {
				value = (byte)object.getInt(index);
			} else if(JsmpReflection.isShort(classType)) {
				value = (short)object.getInt(index);
			} else if(JsmpReflection.isInteger(classType)) {
				value = object.getInt(index);
			} else if(JsmpReflection.isLong(classType)) {
				value = object.getLong(index);
			} else if(JsmpReflection.isFloat(classType)) {
				value = (float)object.getDouble(index);
			} else if(JsmpReflection.isDouble(classType)) {
				value = object.getDouble(index);
			}
		} else if(JsmpReflection.isString(classType)) {
			value = object.getString(index);
		} else if(JsmpReflection.isEnum(classType)) {
			value = Enum.valueOf((Class)classType, object.getString(index)); 
		}
		
		return value;
	} 
	
	private static Object getValueForPrimitive(JSONObject object, 
			Class<?> classType, String name) throws JSONException {
		Object value = null;
		
		if(JsmpReflection.isPrimitive(classType)) {
			if(JsmpReflection.isBoolean(classType)) {
				value = object.getBoolean(name);
			} else if(JsmpReflection.isByte(classType)) {
				value = (byte)object.getInt(name);
			} else if(JsmpReflection.isShort(classType)) {
				value = (short)object.getInt(name);
			} else if(JsmpReflection.isInteger(classType)) {
				value = object.getInt(name);
			} else if(JsmpReflection.isLong(classType)) {
				value = object.getLong(name);
			} else if(JsmpReflection.isFloat(classType)) {
				value = (float)object.getDouble(name);
			} else if(JsmpReflection.isDouble(classType)) {
				value = object.getDouble(name);
			}
		} else if(JsmpReflection.isString(classType)) {
			value = object.getString(name);
		} else if(JsmpReflection.isEnum(classType)) {
			value = Enum.valueOf((Class)classType, object.getString(name)); 
		}
		
		return value;
	}
}