package com.androidtemplate.engine.utils;

import java.util.Collection;
import java.util.List;

/**
 * Some basic list utilitaries 
 * 
 * @author Lazu Ioan-Bogdan
 *
 */
public class TListUtils {
	public static boolean isEmpty(Collection<?> list) {
		if(list == null || list.size() <= 0)
			return true;
		else 
			return false;
	}
	
	public static boolean containsNull(List<?> list) {
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i) == null) {
				return true;
			}
		}
		
		return false;
	}
}
