package com.bubblewrap.plants.webserver.util;

import java.util.Date;

import org.joda.time.DateTime;

public class DateUtils {
	
	/**
	 * Get the date N days ahead from today
	 * 
	 * @param n
	 * @return
	 */
	public static Date getNDaysAhead(int n) {
		DateTime date = new DateTime();
		DateTime tomorrow = date.plusDays(n);
		return tomorrow.toDate();
	}
	
	public static Date getNDaysBehind(int n) {
		return getNDaysAhead(-1*n);
	}

}
