package com.redis_timeseries.utility;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class Utility {

	private static final Logger LOGGER = LogManager.getLogger();

	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

	/**
	 * <p>
	 * Checks if two dates are on the same day ignoring time.
	 * </p>
	 * 
	 * @param date1 the first date, not altered, not null
	 * @param date2 the second date, not altered, not null
	 * @return true if they represent the same day
	 * @throws IllegalArgumentException if either date is <code>null</code>
	 */
	public static Map<Integer, Integer> timingMap = new HashMap<>();

	/**
	 * <p>
	 * Checks if two dates are on the same day ignoring time.
	 * </p>
	 * 
	 * @param date1 the first date, not altered, not null
	 * @param date2 the second date, not altered, not null
	 * @return true if they represent the same day
	 * @throws IllegalArgumentException if either date is <code>null</code>
	 */

	public static float getFloat(String s) {
		float val = 0.00f;
		try {
			if (!s.trim().isEmpty()) {
				val = Float.parseFloat(s);
			}

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return val;
	}

	public static long getLong(String s) {
		long val = 0;
		if (s != null && s.length() > 0) {
			try {
				s = s.replace(".00", "");
				s = s.replace(".0", "");
				val = Long.parseLong(s.trim());
			} catch (NumberFormatException e) {
				// DFTLogContainer.getInstance().setAppLogByType(Log4jEnum.INFO,"get Long
				// NumberFormatException" +
				// e.getMessage());
			}
		}
		return val;
	}

	public static long getLongFromDate(String dateString) {
		DateFormat dfm = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		dfm.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
		long unixtime = 0;
		try {
			unixtime = dfm.parse(dateString).getTime();
			unixtime = unixtime / 1000;
		} catch (ParseException e) {
			LOGGER.error(e.getMessage());
		}
		return unixtime;
	}

	public static int getInt(String s) {
		int val = 0;
		if (!s.isEmpty()) {
			try {
				s = s.replace(".00", "");
				s = s.replace(".0", "");
				val = Integer.parseInt(s.trim());
			} catch (NumberFormatException e) {
				LOGGER.error("->can't convert String to Int NumberFormatException" + e.getMessage());
			}
		}
		return val;
	}

	public static Date getDateFromLong(long l) {
		Date dateTime = null;
		try {
			dateTime = new java.util.Date(l * 1000);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return dateTime;
	}

	public static long getLongFromOITime(String time) {
		long out = 0;
		if (!time.isEmpty() && time.contains(":")) {
			try {
				Time t = Time.valueOf(time);
				out = t.getTime();
			} catch (Exception e) {
				LOGGER.error(e.getMessage());
			}
		}
		return out;

	}

	public static Thread getThreadByName(String threadName) {
		for (Thread t : Thread.getAllStackTraces().keySet()) {
			if (t.getName().equals(threadName))
				return t;
		}
		return null;
	}

	public static String getNextValue(String feed, AtomicInteger index) {
		int start = index.get();
		int end = feed.indexOf('|', start);
		if (end == -1) {
			index.set(feed.length());
			return feed.substring(start);
		}
		index.set(end + 1);
		return feed.substring(start, end);
	}

	public static long getLongFromTime(String dateString) {
		Date dNow = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("MM/dd/yyyy");
		dateString = ft.format(dNow) + " " + dateString;
		// dateString = ft.format(dNow);
		DateFormat dfm = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		dfm.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
		long unixtime = 0;
		try {
			unixtime = dfm.parse(dateString).getTime();
			unixtime = unixtime / 1000;
		} catch (ParseException e) {
			LOGGER.error(e.getMessage());
		}
		return unixtime;
	}

	public static long getExchangeStartTime(int exchange) {
		Calendar c = Calendar.getInstance();
		switch (exchange) {
		case 11:
		case 12:
		case 21:
		case 22:
			c.set(Calendar.HOUR_OF_DAY, 9);
			c.set(Calendar.MINUTE, 15);
			c.set(Calendar.SECOND, 0);
			break;

		case 32:
		case 42:
		case 13:
		case 23:
		case 33:
			c.set(Calendar.HOUR_OF_DAY, 9);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			break;

		default:
			c.set(Calendar.HOUR_OF_DAY, 9);
			c.set(Calendar.MINUTE, 15);
			c.set(Calendar.SECOND, 0);
			break;
		}
		return c.getTimeInMillis() / 1000;
	}

	public static long getExchangeCloseTimeInMilliSec(int exchange) {
		Calendar c = Calendar.getInstance();
		switch (exchange) {
		case 11:
		case 12:
		case 21:
		case 22:
			c.set(Calendar.HOUR_OF_DAY, 15);
			c.set(Calendar.MINUTE, 30);
			c.set(Calendar.SECOND, 0);
			break;
		case 13:
		case 33:
			c.set(Calendar.HOUR_OF_DAY, 17);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			break;

		case 32:
		case 42:
			c.set(Calendar.HOUR_OF_DAY, 23);
			c.set(Calendar.MINUTE, 30);
			c.set(Calendar.SECOND, 0);
			break;

		default:
			c.set(Calendar.HOUR_OF_DAY, 15);
			c.set(Calendar.MINUTE, 30);
			c.set(Calendar.SECOND, 0);
			break;
		}
		return c.getTimeInMillis() / 1000;
	}
}
