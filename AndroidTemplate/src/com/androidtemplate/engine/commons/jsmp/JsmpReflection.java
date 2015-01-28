package com.androidtemplate.engine.commons.jsmp;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Lightweight reflection utils.
 * 
 * @author Lazu Ioan-Bogdan
 */
public class JsmpReflection {
	
	/**
	 * Types of possible data
	 */
	public static interface Type {
		public static final int TYPE_PRIMITIVE 			= 1;
		public static final int TYPE_PRIMITIVE_ARRAY 	= 2;
		public static final int TYPE_ENUM 				= 3;
		public static final int TYPE_OBJECT 			= 4;
		public static final int TYPE_OBJECT_ARRAY		= 5;
	}
	
	public static class FieldWrapper {
		// Just in case we need it
		public Class<?> classType;
		public String name;
		public Object value;
		public int type;

		public FieldWrapper() {}
		
		public FieldWrapper(Class<?> classType, String name, Object value, int type) {
			this.classType = classType;
			this.name = name;
			this.value = value;
			this.type = type;
		}		
	}
	
	protected static <T> T instance(Class<T> template) {
		try {
			// Init template
			Constructor<T> cons = template.getDeclaredConstructor((Class[])null);
			cons.setAccessible(true);
			
			return cons.newInstance();
		} catch (InstantiationException e) {
			if(Jsmp.DEBUG) e.printStackTrace();
		} catch (IllegalAccessException e) {
			if(Jsmp.DEBUG) e.printStackTrace();
		} catch (NoSuchMethodException e) {
			if(Jsmp.DEBUG) e.printStackTrace();
		} catch (IllegalArgumentException e) {
			if(Jsmp.DEBUG) e.printStackTrace();
		} catch (InvocationTargetException e) {
			if(Jsmp.DEBUG) e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Places the value in the object in the field with the provided field name. 
	 * 
	 * @param object
	 * 				Object instance
	 * @param value
	 * 				Value to set for object
	 * @param fieldName
	 * 				Field name
	 */
	protected static void put(Object object, FieldWrapper data) {
		Field field = null;
		try {
			// Get the field if it exists
			field = object.getClass().getDeclaredField(data.name);
			
			// Mark field as accessible
			field.setAccessible(true);
			
			// Set it's value
			field.set(object, data.value);			
		} catch (NoSuchFieldException e) {
			if(Jsmp.DEBUG) e.printStackTrace();
		} catch (IllegalAccessException e) {
			if(Jsmp.DEBUG) e.printStackTrace();
		} catch (IllegalArgumentException e) {
			if(Jsmp.DEBUG) e.printStackTrace();
		}
	}
	
	/**
	 * Extracts the field names and values from the object.
	 */
	protected static List<FieldWrapper> extract(Object object) {
		return extract(object.getClass(), object);
	}	
	
	/**
	 * Extracts the fields from the template and their values if the 
	 * object parameter is not null.
	 */
	protected static List<FieldWrapper> extract(Class<?> template, Object object) {
		Field fields[] = template.getDeclaredFields();
		
		if(fields.length <= 0) return Collections.emptyList();
		
		List<FieldWrapper> list = new ArrayList<FieldWrapper>();
				
		Field field;
		Class<?> fieldType;
		for (int i = 0; i < fields.length; i++) {
			field = fields[i];
			fieldType = field.getType();
			
			int type;
			
			if(isPrimitive(fieldType) || isString(fieldType) || isEnum(fieldType)) {
				type = Type.TYPE_PRIMITIVE;
			} else if(isPrimitiveArray(fieldType)) {
				type = Type.TYPE_PRIMITIVE_ARRAY;
			} else {
				if(isArray(fieldType)) {
					type = Type.TYPE_OBJECT_ARRAY;
				} else {
					type = Type.TYPE_OBJECT;
				}
			}
			
			// Get value
			field.setAccessible(true);

			try {
				Object value = null;
				
				if(object != null) {
					value = field.get(object);	
				}			
				
				list.add(new FieldWrapper(field.getType(), field.getName(), value, type));
			} catch (IllegalArgumentException e) {
				if(Jsmp.DEBUG) e.printStackTrace();
			} catch (IllegalAccessException e) {
				if(Jsmp.DEBUG) e.printStackTrace();
			}
		}
		
		return list;
	}	
	
	protected static boolean isEnum(Class<?> field) {
		return field.isEnum();
	}
	
	protected static boolean isPrimitiveArray(Class<?> field) {
		if(!isArray(field)) return false;
		
		boolean isPrimitiveArray =
				field.equals(boolean[].class) ||
				field.equals(Boolean[].class) ||
				field.equals(byte[].class) ||
				field.equals(Byte[].class) ||
				field.equals(short[].class) ||
				field.equals(Short[].class) ||
				field.equals(int[].class) ||
				field.equals(Integer[].class) ||
				field.equals(long[].class) ||
				field.equals(Long[].class) ||
				field.equals(float[].class) ||
				field.equals(Float[].class) ||
				field.equals(double[].class) ||
				field.equals(Double[].class);
				
		return isPrimitiveArray;
	}
	
	protected static boolean isArray(Class<?> field) {
		return field.isArray();
	}
	
	protected static boolean isPrimitive(Class<?> field) {
		boolean primitive = 
				field.isPrimitive() || 
				field.equals(Boolean.class) ||
				field.equals(Byte.class) ||
				field.equals(Short.class) ||
				field.equals(Integer.class) ||
				field.equals(Long.class) ||
				field.equals(Float.class) ||
				field.equals(Double.class);
		
		return primitive;
	}
	
	protected static boolean isString(Class<?> field) {
		return field.equals(String.class);
	}

	public static boolean isBoolean(Class<?> classType) {
		return classType.equals(boolean.class) ||
				classType.equals(Boolean.class);
	}
	
	public static boolean isByte(Class<?> classType) {
		return classType.equals(byte.class) ||
				classType.equals(Byte.class);
	}
	
	public static boolean isShort(Class<?> classType) {
		return classType.equals(short.class) ||
				classType.equals(Short.class);
	}
	
	public static boolean isInteger(Class<?> classType) {
		return classType.equals(int.class) ||
				classType.equals(Integer.class);
	}
	
	public static boolean isLong(Class<?> classType) {
		return classType.equals(long.class) ||
				classType.equals(Long.class);
	}
	
	public static boolean isFloat(Class<?> classType) {
		return classType.equals(float.class) ||
				classType.equals(Float.class);
	}
	
	public static boolean isDouble(Class<?> classType) {
		return classType.equals(double.class) ||
				classType.equals(Double.class);
	}
}
