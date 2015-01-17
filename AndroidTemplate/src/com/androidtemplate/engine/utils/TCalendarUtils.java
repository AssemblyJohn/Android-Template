package com.androidtemplate.engine.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.text.format.DateFormat;

/**
 * Basic calendar utilitaries.
 * 
 * @author Lazu Ioan-Bogdan
 *
 */
public class TCalendarUtils {
	private static final int MILLIINNANO = 1000000;
	
	public static long nanoToMili(long nano) {
		return (nano / MILLIINNANO);
	}
	
	public static long secondToMilli(long seconds) {
		return seconds * 1000;
	}
	
	public static int getDaysInThisMonth() {
		Calendar c = getCalendarInstance();
		
		return c.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	public static CharSequence getFormatedDate(long milliseconds) {
		return DateFormat.format("yyyy-MM-dd hh:mm:ss", milliseconds);
	}
	
	/**
	 * Get calendar instance.
	 * 
	 * @return A instance of calendar with locale and local timezone settings 
	 */
	public static Calendar getCalendarInstance() {
		return Calendar.getInstance();
	}
	
	/**
	 * @return timezone instance
	 */
	public static TimeZone getTimeZone() {
		return Calendar.getInstance().getTimeZone();
	}
	
	/**
	 * @return Returns the UTC offset in minutes
	 */
	public static int getUTCOffset() {
		return (getUTCOffsetMilli() / 1000) / 60;
	}
	
	/**
	 * @return Returns the UTC offset in seconds
	 */
	public static int getUTCOffsetMilli() {
		return Calendar.getInstance().getTimeZone().getOffset(getTimeMillis());
	}
	
	/**
	 * @return Returns if this zone is in daylight saving.
	 */
	public static boolean inDaylightSaving() {
		return Calendar.getInstance().getTimeZone().inDaylightTime(new Date(getTimeMillis()));
	}
	
	/**
	 * Get current time in MS.
	 * @return Current time in milliseconds, based on locale and default timezone 
	 */
	public static long getTimeMillis() {
		return Calendar.getInstance().getTimeInMillis();
	}
	
	/**
	 * Returns the time in UTC. To be used for text messages.
	 */
	public static long getTimeUTC() {
		return (getTimeMillis() - getUTCOffsetMilli());
	}
}
