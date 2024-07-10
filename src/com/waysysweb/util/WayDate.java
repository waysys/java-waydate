//------------------------------------------------------------------------------
//      Compilation Unit Header
//------------------------------------------------------------------------------
//
//  Copyright (c) 2006, 2012 Waysys LLC All Rights Reserved.
//
//  Permission to use, copy, modify, and distribute this software
//  and its documentation for NON-COMMERCIAL purposes and without
//  fee is hereby granted provided that this copyright notice
//  appears in all copies.
//
//  Waysys MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
//  THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
//  TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
//  PARTICULAR PURPOSE, OR NON-INFRINGEMENT. Waysys SHALL NOT BE LIABLE FOR
//  ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
//  DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
//
//  For further information, contact Waysys LLC at wshaffer@waysysweb.com
//  or 800-622-5315 (USA).
//
//------------------------------------------------------------------------------
//      Maintenance History
//------------------------------------------------------------------------------
//
//  Person    Date          Change
//  ------    -----------   ----------------------------------------------------
//
//  Shaffer   17-Jun-2000   File create
//  Shaffer   23-Jun-2000   Added override to Object methods
//  Shaffer   24-Jun-2000   Added constructor to return today's date
//  Shaffer   07-Nov-2006   Adapted to Eclipse environment
//  Shaffer   07-Jan-2007   Added assert statements
//  Shaffer   02-Jul-2011   Add constructor for absolute date
//  Shaffer   30-Apr-2012   Updated to build definitive version
//
//------------------------------------------------------------------------------
//      Package Declaration
//------------------------------------------------------------------------------

package com.waysysweb.util;

//------------------------------------------------------------------------------
//Import Declarations
//------------------------------------------------------------------------------

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//------------------------------------------------------------------------------
//Public Class Declaration
//------------------------------------------------------------------------------

/**
 * <p>
 * This class performs various manipulations on dates. This class addresses two
 * different types of requirements that dictate two different types of
 * implementations. Display activities such as returning the particular day of
 * the date, or displaying a string representation of the date, favor an
 * implementation in which month, day, and year are separate instance fields.
 * Calculations activities like subtracting one date from another favor a
 * representation in which the date is stored as the number of days from an
 * epoch.
 * </p>
 * 
 * <p>
 * This class is structured according to the Bridge pattern in Gamma et al. This
 * class wraps an abstract implementation class. The abstract implementation
 * class has three subclasses. One subclass is optimized for computation. The
 * second class is optimized for display purposes. The third class represents a
 * null date.
 * </p>
 * 
 * <p>
 * This WayDate class has two significant responsibilities. First, it validates
 * any inputs, thereby saving considerable validation code in the implementation
 * classes. The implementation class usually assume they are getting valid
 * inputs. Second, this class handles the switching between the display
 * implementation class and the calculation implementation class.
 * </p>
 * 
 * <p>
 * WayDate is designed to handle manipulations of dates between the designated
 * Epoc of 1-Jan-1601 and 31-Dec-3999. It uses 1,...,31 to represent the days of
 * the month. It uses 1,...,12 to represent the months of the year. It uses
 * 1601,...,3999 to represent years (century designations are always included in
 * WayDate operations involving years).
 * </p>
 * 
 * <p>
 * The hash code function returns the absolute date. The absolute date is the
 * number of days since 31-Dec-1600. The absolute date can be conveniently
 * stored in a file or database as an integer. The date can then be
 * reconstructed using the appropriate constructor.
 * </p>
 * 
 * <p>
 * The null date provides an alternative to "null" as a representation of a
 * missing date. It returns a hash value of zero. Using this value, the null
 * date can be reconstructed using the absolute date constructor.
 * </p>
 * 
 * <p>
 * WayDate instances are immutable.
 * </p>
 * 
 * @author William A. Shaffer
 * @version 4.00 30-Apr-2012
 */
public class WayDate implements Serializable, Comparable<WayDate>, Cloneable {

	// -------------------------------------------------------------------------
	// Constants
	// -------------------------------------------------------------------------

	/**
	 * The serial version unique identifier for this class
	 */
	private static final long serialVersionUID = 9700L;

	/**
	 * MAXYEAR is the largest year handled by WayDate.
	 */
	public static final int MAXYEAR = 3999;

	/**
	 * MINYEAR is the minimum year handled by WayDate. Since the Gregorian
	 * calendar did not begin until 1582, there seems little point in using this
	 * class for dates prior to 1582. 1601 is a convenient year for the epoch.
	 */
	public static final int MINYEAR = 1601;

	/** The maximum allowed date */
	public static final WayDate MaxDate = getMaxDate();

	/** The minimum allowed date */
	public static final WayDate MinDate = getMinDate();

	// -------------------------------------------------------------------------
	// Days of the week
	// -------------------------------------------------------------------------

	public static final int SUNDAY = 0;
	public static final int MONDAY = 1;
	public static final int TUESDAY = 2;
	public static final int WEDNESDAY = 3;
	public static final int THURSDAY = 4;
	public static final int FRIDAY = 5;
	public static final int SATURDAY = 6;

	/** Names of days of the week */
	private static final String[] namesOfDays = { "Sun", "Mon", "Tue", "Wed",
			"Thu", "Fri", "Sat" };

	// -------------------------------------------------------------------------
	// Months
	// -------------------------------------------------------------------------

	public static final int JANUARY = 1;
	public static final int FEBRUARY = 2;
	public static final int MARCH = 3;
	public static final int APRIL = 4;
	public static final int MAY = 5;
	public static final int JUNE = 6;
	public static final int JULY = 7;
	public static final int AUGUST = 8;
	public static final int SEPTEMBER = 9;
	public static final int OCTOBER = 10;
	public static final int NOVEMBER = 11;
	public static final int DECEMBER = 12;

	// -------------------------------------------------------------------------
	// Internal Fields
	// -------------------------------------------------------------------------

	/**
	 * The field 'date' holds an instance of WayDateCalc or WayDateDisplay.
	 * These subclasses will do the actual work.
	 */
	private WayDateImpl date;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create an instance of WayDate with the specified month, day, and year.
	 * <P>
	 * Example:
	 * </P>
	 * 
	 * <PRE>
	 * WayDate result = new WayDate(12, 22, 1998);
	 * </PRE>
	 * <p>
	 * Precondition:
	 * </p>
	 * 
	 * <pre>
	 * assert isValidDate(month, day, year)
	 * </pre>
	 * <p>
	 * Postcondition:
	 * </p>
	 * 
	 * <pre>
	 * assert result.getDay() = day and 
	 * result.getMonth() = month and 
	 * result.getYear() = year and
	 * not result.isNull()
	 * </pre>
	 * 
	 * @param month
	 *            month of the date (month in set range(1, 12))
	 * @param day
	 *            day of the date (day in set range(1, 31))
	 * @param year
	 *            year of the date (year in set range(MINYEAR, MAXYEAR))
	 * @exception WayDateException
	 *                thrown if month, day, or year are out of range
	 */
	public WayDate(int month, int day, int year) throws WayDateException {
		if (isValidDate(month, day, year)) {
			date = new WayDateDisplay(month, day, year);
		} else {
			date = null;
			String dateString = month + "-" + day + "-" + year;
			throw new WayDateException(WayDateException.ILLEGAL_DATE,
					dateString);
		}
		return;
	}

	/**
	 * Create an instance of WayDate in which the day of the year corresponds to
	 * the input to this constructor. This constructor and the getters
	 * getDayOfYear() and getYear() provide the capability to work with day of
	 * year (or Julian date) calculations.
	 * <P>
	 * Example:
	 * </P>
	 * 
	 * <PRE>
	 * WayDate date = new WayDate(dayYear, year);
	 * </PRE>
	 * <p>
	 * Precondition:
	 * </p>
	 * 
	 * <pre>
	 * assert isValidDay(day) and
	 *      isValidMonth(month) and
	 *      isValidYear(year) and
	 *      dayYear = dayOfYear(month, day, year) and
	 * 		isValidDayOfYear(dayYear, year)
	 * </pre>
	 * <p>
	 * Postcondition:
	 * </p>
	 * 
	 * <pre>
	 * assert result.getDay() = day and 
	 * result.getMonth() = month and 
	 * result.getYear() = year and
	 * not result.isNull()
	 * </pre>
	 * 
	 * @param dayYear
	 *            day of the year for the date (dayYear in set range(1,
	 *            daysInYear(year)))
	 * @param year
	 *            year of the date (year in set range(MINYEAR, MAXYEAR))
	 * @exception WayDateException
	 *                thrown if year is illegal or day of year is illegal
	 */
	public WayDate(int dayYear, int year) throws WayDateException {
		if (!isValidYear(year)) {
			throw new WayDateException(WayDateException.ILLEGAL_YEAR, year);
		}
		if (!isValidDayOfYear(dayYear, year)) {
			throw new WayDateException(WayDateException.ILLEGAL_DAY_YEAR,
					dayYear);
		}
		date = new WayDateDisplay(dayYear, year);
	}

	/**
	 * Create an instance of WayDate based on an absolute date. If the value of
	 * the absolute date is zero, then a null date is returned.
	 * <P>
	 * Example:
	 * </P>
	 * 
	 * <PRE>
	 * WayDate date = new WayDate(100000);
	 * </PRE>
	 * 
	 * <pre>
	 * assert isValidDay(day) and
	 *      isValidMonth(month) and
	 *      isValidYear(year) and
	 * 
	 * </pre>
	 * <p>
	 * Postcondition:
	 * </p>
	 * 
	 * <pre>
	 * assert result.getDay() = day and 
	 * result.getMonth() = month and 
	 * result.getYear() = year and
	 * (
	 * </pre>
	 * 
	 * @param adate
	 *            an absolute date 0 <= adate <= MAX_ABSOLUTE
	 * @exception WayDateException
	 *                thrown if parameter is an invalid absolute date other than
	 *                zero
	 */
	public WayDate(int adate) throws WayDateException {
		if (adate == 0)
			date = new NullWayDate();
		else if (!WayDateCalc.isAbsoluteDate(adate)) {
			throw new WayDateException(WayDateException.ILLEGAL_ABSOLUTE, adate);
		} else
			date = new WayDateCalc(adate);
		return;
	}

	/**
	 * Return an instance of WayDate based on a WayDateImpl
	 * 
	 * @param dateImpl
	 *            an instance of WayDateImpl used internally by this instance
	 * 
	 */
	protected WayDate(WayDateImpl dateImpl) {
		assert dateImpl != null : "Null date implementation";
		date = dateImpl;
	}

	// -------------------------------------------------------------------------
	// Factory Methods
	// -------------------------------------------------------------------------

	/**
	 * Return the maximum date handled by WayDate, 31-Dec-3999
	 * 
	 * @return the maximum date
	 */
	public static WayDate getMaxDate() {
		WayDate date;
		try {
			date = new WayDate(WayDate.DECEMBER, 31, MAXYEAR);
		} catch (WayDateException e) {
			// this exception will never be thrown
			date = null;
		}
		return date;
	}

	/**
	 * Return the minimum date handled by WayDate, 01-Jan-1601
	 * 
	 * @return the minimum date
	 */
	public static WayDate getMinDate() {
		WayDate date;
		try {
			date = new WayDate(WayDate.JANUARY, 01, MINYEAR);
		} catch (WayDateException e) {
			// this exception will never be thrown
			date = null;
		}
		return date;
	}

	/**
	 * Return an instance of WayDate with today's date.
	 * <P>
	 * Example:
	 * </P>
	 * 
	 * <PRE>
	 * WayDate date = WayDate.today();
	 * </PRE>
	 * 
	 * @return an instance of WayDate with the current date.
	 */
	public static WayDate today() {
		WayDateImpl dt = WayDateImpl.today();
		return new WayDate(dt);
	}

	/**
	 * Return an instance of null date
	 * <P>
	 * Example:
	 * </P>
	 * 
	 * <PRE>
	 * WayDate date = WayDate.getNullDate();
	 * </PRE>
	 * 
	 * @return an instance of a null WayDate
	 */
	public static WayDate getNullDate() {
		return new WayDate(NullWayDate.NULL_DATE);
	}

	/**
	 * Return an instance of WayDate by parsing a string in the format
	 * dd-MMM-yyyy.
	 * <P>
	 * Example:
	 * </P>
	 * 
	 * <PRE>
	 * WayDate date = WayDate.asWayDate(&quot;15-Jan-2012&quot;);
	 * </PRE>
	 * 
	 * @param value
	 *            the string with a date representation
	 * @return an instance of WayDate
	 */
	public static WayDate asWayDate(String value) throws WayDateException {
		WayDate result = asWayDate("dd-MMM-yyyy", value);
		return result;
	}

	/**
	 * Return an instance of WayDate by parsing a string in ISO format: dd-MM-yy
	 * <P>
	 * Example:
	 * </P>
	 * 
	 * <PRE>
	 * WayDate date = fromISODate(&quot;2012-04-22&quot;);
	 * </PRE>
	 * 
	 * @param value
	 *            the string with a date representation
	 * @return an instance of WayDate
	 * @exception WayDateException
	 *                thrown if value is in an incorrect format.
	 */
	public static WayDate fromISODate(String value) throws WayDateException {
		WayDate result = asWayDate("yyyy-MM-dd", value);
		return result;
	}

	/**
	 * Return an instance of WayDate by parsing a string according to the
	 * specified pattern.
	 * <P>
	 * Example:
	 * </P>
	 * 
	 * <PRE>
	 * WayDate date = WayDate.asWayDate(&quot;dd/MM/yyyy&quot;, &quot;12/22/1945&quot;);
	 * </PRE>
	 * 
	 * @see "java.text.SimpleDateFormat"
	 * 
	 * @param pattern
	 *            a string with the SimpleDateFormat pattern
	 * @param value
	 *            the string with a date representation
	 * @return an instance of WayDate
	 * @exception WayDateException
	 *                thrown if the value cannot be parsed or the pattern is
	 *                incorrect
	 */
	public static WayDate asWayDate(String pattern, String value)
			throws WayDateException {
		SimpleDateFormat parser;
		Date date = null;
		if (pattern == null) {
			throw new WayDateException(WayDateException.NULL_ARGUMENT,
					"pattern");
		}
		if (value == null) {
			throw new WayDateException(WayDateException.NULL_ARGUMENT, "value");
		}
		try {
			parser = new SimpleDateFormat(pattern);
		} catch (IllegalArgumentException e) {
			String arg = pattern + "(" + e.getMessage() + ")";
			throw new WayDateException(WayDateException.PATTERN_ERROR, arg);
		}
		try {
			date = parser.parse(value);
		} catch (ParseException e) {
			String arg = value + "(" + e.getMessage() + ")";
			throw new WayDateException(WayDateException.ILLEGAL_FORMAT, arg);
		}
		return asWayDate(date);
	}

	/**
	 * Return an instance of WayDate corresponding to a Date object. If the
	 * input is null, a null date is returned.
	 * <P>
	 * Example:
	 * </P>
	 * 
	 * <PRE>
	 * import java.util.Date
	 * 
	 * Date aDate = new Date()
	 * WayDate date = WayDate.asWayDate(aDate);
	 * </PRE>
	 * 
	 * @param date
	 *            an instance of the Java Date class
	 * @return an instance of WayDate corresponding to to the input
	 * @exception WayDateException
	 *                if date is outside of range.
	 */
	public static WayDate asWayDate(Date date) throws WayDateException {
		WayDate aDate = null;
		if (date != null) {
			WayDateImpl dt = WayDateImpl.asWayDate(date);
			aDate = new WayDate(dt);
		} else {
			aDate = getNullDate();
		}
		return aDate;
	}

	// -------------------------------------------------------------------------
	// Validation Methods
	// -------------------------------------------------------------------------

	/**
	 * Check if a date is valid.
	 * <P>
	 * Example:
	 * </P>
	 * 
	 * <PRE>
	 *    if (WayDate.isValidDate(12, 22, 1945))
	 *    {
	 *      ...
	 *    }
	 * </PRE>
	 * 
	 * @param month
	 *            the month part of the date (mnth in set range(1, 12))
	 * @param day
	 *            the day part of the date (dy in set range(1, daysInMonth(mnth,
	 *            yr)))
	 * @param year
	 *            the year part of the date (yr in set range(MINYEAR, MAXYEAR))
	 * @return true if the date inputs are valid
	 */
	public static boolean isValidDate(int month, int day, int year) {
		boolean result;
		if (!isValidYear(year)) {
			result = false;
		} else if (!isValidMonth(month)) {
			result = false;
		} else if (!isValidDay(month, day, year)) {
			result = false;
		} else
			result = true;
		return result;
	}

	/**
	 * Return true if the year is valid.
	 * <P>
	 * Example:
	 * </P>
	 * 
	 * <PRE>
	 * if (isValidYear(year)
	 *     {
	 *     . . .
	 *     }
	 * </PRE>
	 * 
	 * @return true if year in set range(MINYEAR, MAXYEAR)
	 * @param year
	 *            the year of a date
	 */
	static public boolean isValidYear(int year) {
		boolean result;

		if ((year >= MINYEAR) && (year <= MAXYEAR))
			result = true;
		else
			result = false;
		return result;
	}

	/**
	 * Return true if month is in set range(1, 12)
	 * <P>
	 * Example:
	 * </P>
	 * 
	 * <PRE>
	 * if (isValidMonth(month)
	 *     {
	 *     . . .
	 *     }
	 * </PRE>
	 * 
	 * @return true if month in set range(1, 12)
	 * @param month
	 *            the month of a date
	 */
	static public boolean isValidMonth(int month) {
		boolean result;

		if ((month >= 1) && (month <= 12))
			result = true;
		else
			result = false;
		return result;
	}

	/**
	 * Return true if day inset range(1, dayOfMonth(month, year))
	 * 
	 * @return day inset range(1, dayOfMonth(month, year))
	 * @param month
	 *            the month of the date
	 * @param day
	 *            the day of the date
	 * @param year
	 *            the year of the date
	 * @exception WayDateException
	 *                thrown if month, day, year are not a valid date
	 */
	public static boolean isValidDay(int month, int day, int year) {
		boolean result = false;
		try {
			if (!isValidYear(year))
				result = false;
			else if (!isValidMonth(month))
				result = false;
			else if ((day >= 1) && (day <= daysInMonth(month, year)))
				result = true;
			else
				result = false;
		} catch (WayDateException e) {
			result = false;
		}
		return result;
	}

	/**
	 * Return true if the day of the year is valid.
	 * 
	 * @param dayOfYear
	 *            the day of the year
	 * @param year
	 *            the year
	 * @return dayOfYear inset range(1, daysInYear(year))
	 */
	public static boolean isValidDayOfYear(int dayOfYear, int year) {
		boolean result;
		try {
			if (!isValidYear(year))
				result = false;
			else if ((dayOfYear >= 1) && (dayOfYear <= daysInYear(year)))
				result = true;
			else
				result = false;
		} catch (WayDateException e) {
			result = false;
		}
		return result;
	}

	/**
	 * Return true if the day of the week is valid.
	 * 
	 * @param dayOfWeek
	 *            the day of the week
	 * @return dayOfWeek in set range(0, 6)
	 */
	public static boolean isValidDayOfWeek(int dayOfWeek) {
		boolean result;
		if ((dayOfWeek >= SUNDAY) && (dayOfWeek <= SATURDAY)) {
			result = true;
		} else
			result = false;
		return result;
	}

	/**
	 * Return true if the absolute date is valid.
	 * 
	 * @param adate
	 *            an absolute date
	 * @return adate in set range(1, WayDateCalc.MAX_ABSOLUTE)
	 */
	public static boolean isAbsoluteDate(int adate) {
		return WayDateCalc.isAbsoluteDate(adate);
	}

	// -------------------------------------------------------------------------
	// Static Methods
	// -------------------------------------------------------------------------

	/**
	 * Return the number of days in a month.
	 * 
	 * <P>
	 * Example:
	 * </P>
	 * 
	 * <PRE>
	 * int days = date.daysInMonth(mnth, yr);
	 * </PRE>
	 * 
	 * @return the number of days in the month
	 * @param mnth
	 *            month (month inset range(1, 12))
	 * @param yr
	 *            year (used to determine if this is a leap year) (year inset
	 *            range(MINYEAR, MAXYEAR))
	 * @exception WayDateException
	 *                thrown if year or month are invalid
	 */
	public static int daysInMonth(int mnth, int yr) throws WayDateException {
		int dy;
		if (!isValidYear(yr)) {
			throw new WayDateException(WayDateException.ILLEGAL_YEAR, yr);
		}
		if (!isValidMonth(mnth)) {
			throw new WayDateException(WayDateException.ILLEGAL_MONTH, mnth);
		}
		dy = WayDateImpl.daysInMonth(mnth, yr);
		return dy;
	}

	/**
	 * Return true if year is a leap year
	 * 
	 * <P>
	 * Example:
	 * </P>
	 * 
	 * <PRE>
	 * if (WayDate.isLeapYear(yr)) {
	 * }
	 * </PRE>
	 * 
	 * @param yr
	 *            a year (year inset range(MINYEAR, MAXYEAR))
	 * @return result true if year is leap year false if year is not leap year
	 * @exception WayDateException
	 *                thrown if year is invalid
	 */
	public static boolean isLeapYear(int yr) throws WayDateException {
		if (!isValidYear(yr)) {
			throw new WayDateException(WayDateException.ILLEGAL_YEAR, yr);
		}
		return WayDateImpl.isLeapYear(yr);
	}

	/**
	 * Return the number of days in a year.
	 * 
	 * <P>
	 * Example:
	 * </P>
	 * 
	 * <PRE>
	 *    int days = WayDate.daysInYear(yr)
	 * </PRE>
	 * 
	 * @return days number of days in the year
	 * @param year
	 *            a year (year inset range(MINYEAR, MAXYEAR))
	 * @exception WayDateException
	 *                thrown if year is invalid
	 * 
	 */
	public static int daysInYear(int year) throws WayDateException {
		if (!isValidYear(year)) {
			throw new WayDateException(WayDateException.ILLEGAL_YEAR, year);
		}
		return WayDateImpl.daysInYear(year);
	}

	/**
	 * Return a string with the abbreviated name of the month.
	 * 
	 * <P>
	 * Example:
	 * </P>
	 * 
	 * <PRE>
	 * String name = WayDate.monthString(12);
	 * </PRE>
	 * 
	 * @return name a string with the abbreviated name of the month
	 * @param month
	 *            the desired month (month inset range(1, 12))
	 */
	public static String monthString(int month) throws WayDateException {
		if (!isValidMonth(month)) {
			throw new WayDateException(WayDateException.ILLEGAL_MONTH, month);
		}
		return WayDateImpl.monthString(month);
	}

	/**
	 * Retrieve the abbreviation of the day of the week.
	 * 
	 * @param dayOfWeek
	 *            an integer dayOfWeek in set range(0, 6)
	 * @return a string with the day of the week
	 * @exception WayDateException
	 *                thrown if day of week is out of range
	 */
	public static String dayOfWeekString(int dayOfWeek) throws WayDateException {
		if (!isValidDayOfWeek(dayOfWeek)) {
			throw new WayDateException(WayDateException.ILLEGAL_DAY_OF_WEEK,
					dayOfWeek);
		}
		return namesOfDays[dayOfWeek];
	}

	/**
	 * Return the day of year for a given date
	 * <p>
	 * Example:
	 * </p>
	 * 
	 * <PRE>
	 * int dayOfYear = WayDate.dayOfYear(4, 1, 2012)
	 * </PRE>
	 * <p>
	 * Precondition:
	 * </p>
	 * 
	 * <PRE>
	 * assert isValidDate(month, day, year)
	 * </PRE>
	 * <p>
	 * Postcondition:
	 * </p>
	 * 
	 * <PRE>
	 * assert isValidDayOfYear(dayOfYear, year) and
	 *        date = new WayDate(dayOfYear, year) and
	 *        date.getDay() = day and
	 *        date.getMonth() = month and
	 *        date.getYear() = year
	 * </PRE>
	 * 
	 * @param month
	 *            the desired month (month in set range(1, 12))
	 * @param day
	 *            the desired day (day in set range(1, 31))
	 * @param year
	 *            the desired year (year in set range(MINYEAR, MAXYEAR))
	 * @return the day of the year
	 * @exception WayDateException
	 *                thrown if month, day, or year is invalid
	 */
	public static int dayOfYear(int month, int day, int year)
			throws WayDateException {
		int dayOfYear;
		if (isValidDate(month, day, year))
			dayOfYear = WayDateImpl.dayOfYear(month, day, year);
		else {
			dayOfYear = 0;
			String date = month + "-" + day + "-" + year;
			new WayDateException(WayDateException.ILLEGAL_DATE, date);
		}
		return dayOfYear;
	}

	// -------------------------------------------------------------------------
	// Display interface
	// -------------------------------------------------------------------------

	/**
	 * Return the day part of the date.
	 * 
	 * <P>
	 * Example:
	 * </P>
	 * 
	 * <PRE>
	 * int day = aDate.getDay();
	 * </PRE>
	 * 
	 * @return the day part of the date
	 * @exception WayDateException
	 *                thrown if date is null
	 */
	public int getDay() throws WayDateException {
		date = date.asWayDateDisplay();
		return ((WayDateDisplay) date).getDay();
	}

	/**
	 * Return the month part of the date.
	 * 
	 * <P>
	 * Example:
	 * </P>
	 * 
	 * <PRE>
	 * int month = aDate.getMonth();
	 * </PRE>
	 * 
	 * @return month month part of the date.
	 * @exception WayDateException
	 *                thrown if date is null
	 */
	public int getMonth() throws WayDateException {
		date = date.asWayDateDisplay();
		return ((WayDateDisplay) date).getMonth();
	}

	/**
	 * Return the year part of the date.
	 * 
	 * <P>
	 * Example:
	 * </P>
	 * 
	 * <PRE>
	 * int year = aDate.getYear();
	 * </PRE>
	 * 
	 * @return year the year part of the date.
	 * @exception WayDateException
	 *                thrown if date is null
	 */
	public int getYear() throws WayDateException {
		date = date.asWayDateDisplay();
		return ((WayDateDisplay) date).getYear();
	}

	/**
	 * Return the day of the year for a particular date.
	 * 
	 * <P>
	 * Example:
	 * </P>
	 * 
	 * <PRE>
	 * dayOfYear = date.getDayOfYear();
	 * </PRE>
	 * 
	 * @return dayOfYear the number of the day in the year
	 * 
	 */
	public int getDayOfYear() throws WayDateException {
		date = date.asWayDateDisplay();
		return ((WayDateDisplay) date).getDayOfYear();
	}

	/**
	 * Return true if the date is in a leap year.
	 * <P>
	 * Example:
	 * </P>
	 * 
	 * <PRE>
	 * if (date.isLeapYear()) {
	 * }
	 * </PRE>
	 * 
	 * @return true if date is in a leap year
	 */
	public boolean isLeapYear() throws WayDateException {
		date = date.asWayDateDisplay();
		return ((WayDateDisplay) date).isLeapYear();
	}

	/**
	 * Return a string representation of the date in ISO format
	 * 
	 * <P>
	 * Example:
	 * </P>
	 * 
	 * <PRE>
	 * String date = aDate.toISODate();
	 * </PRE>
	 * 
	 * @return date a string representing the date. The format is YYYY-MM-DD
	 * 
	 */
	public String toISODate() {
		String value;
		try {
			date = date.asWayDateDisplay();
			value = ((WayDateDisplay) date).toISODate();
		} catch (WayDateException e) {
			value = "";
		}
		return value;
	}

	/**
	 * Return true if the date is a valid date. The
	 * 
	 * @return true
	 * @exception WayDateException
	 *                is never actually thrown
	 */
	public boolean isValid() {
		boolean result;
		if (isNull())
			result = false;
		else {
			try {
				result = isValidDate(getMonth(), getDay(), getYear());
			} catch (WayDateException e) {
				result = false;
			}
		}
		return result;
	}

	// -------------------------------------------------------------------------
	// Calculation Interface
	// -------------------------------------------------------------------------

	/**
	 * Add a number of days to a date.
	 * 
	 * <P>
	 * Example:
	 * </P>
	 * 
	 * <PRE>
	 * aDate.add(10);
	 * </PRE>
	 * 
	 * @param value
	 *            number of days to add to the date
	 * @return a new date before or after this date
	 * @exception WayDateException
	 *                thrown if result is out of bounds
	 */
	public WayDate add(int value) throws WayDateException {
		date = date.asWayDateCalc();
		WayDateCalc dt = ((WayDateCalc) date).add(value);
		return new WayDate(dt);
	}

	/**
	 * Increases the date by one day.
	 * 
	 * <P>
	 * Example:
	 * </P>
	 * 
	 * <PRE>
	 * aDate.increment();
	 * </PRE>
	 * 
	 * @exception WayDateException
	 *                thrown if date is the maximum date
	 */
	public WayDate increment() throws WayDateException {
		if (equals(MaxDate)) {
			throw new WayDateException(WayDateException.MAX_DATE, "");
		}
		date = date.asWayDateCalc();
		WayDateCalc dt = ((WayDateCalc) date).increment();
		return new WayDate(dt);
	}

	/**
	 * Decrement date.
	 * 
	 * <P>
	 * Example:
	 * </P>
	 * 
	 * <PRE>
	 * aDate.decrement();
	 * </PRE>
	 * 
	 * @exception WayDateException
	 *                thrown if the date is the minimum date
	 */

	public WayDate decrement() throws WayDateException {
		if (equals(MinDate)) {
			throw new WayDateException(WayDateException.MIN_DATE, "");
		}
		date = date.asWayDateCalc();
		WayDateCalc dt = ((WayDateCalc) date).decrement();
		return new WayDate(dt);
	}

	/**
	 * Subtract two dates and return the number of days between the two.
	 * 
	 * <P>
	 * Example:
	 * </P>
	 * 
	 * <PRE>
	 * diff = aDate.difference(anotherDate);
	 * </PRE>
	 * 
	 * @return diff an integer representing the number of days between the
	 *         dates.
	 * @param aDate
	 *            a date
	 * @exception WayDateException
	 *                thrown if argument is null date
	 */
	public int difference(WayDate aDate) throws WayDateException {
		date = date.asWayDateCalc();
		WayDateCalc dt2 = aDate.getDate().asWayDateCalc();
		return ((WayDateCalc) date).difference(dt2);
	}

	/**
	 * Return the day of the week as an integer.
	 * 
	 * <P>
	 * Example:
	 * </P>
	 * 
	 * <PRE>
	 * dayOfWk = date.getDayOfWeek();
	 * </PRE>
	 * 
	 * @return dayOfWk 0 for Sunday, 1 for Monday, etc. (dayOfWk inset range(0,
	 *         6))
	 * @exception WayDateException
	 *                thrown if this date is null date
	 */
	public int getDayOfWeek() throws WayDateException {
		date = date.asWayDateCalc();
		return ((WayDateCalc) date).getDayOfWeek();
	}

	/**
	 * Return the day of the week as a string.
	 * 
	 * @return a string with the day of the week
	 * @exception WayDateException
	 *                thrown if this date is null date
	 */
	public String getDayOfWeekString() throws WayDateException {
		int index = getDayOfWeek();
		// assert index inset range(0,6)
		return namesOfDays[index];
	}

	/**
	 * Return the date as an instance of the Jave Date class.
	 * 
	 * @return an instance of Date class corresponding to this date
	 * @exception WayDateException
	 *                thrown if date is null date
	 */
	public Date asDate() throws WayDateException {
		int month = getMonth() - 1;
		int day = getDay();
		int year = getYear();
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.AM_PM, Calendar.AM);
		calendar.set(year, month, day, 0, 0, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date date = calendar.getTime();
		return date;
	}

	// -------------------------------------------------------------------------
	// Comparable Interface
	// -------------------------------------------------------------------------

	/**
	 * Compare a date with the current date. If the date is larger, return -1.
	 * If the date is smaller, return +1. If the date is equal, return 0.
	 * 
	 * <P>
	 * Example:
	 * </P>
	 * 
	 * <PRE>
	 * if (date1.compareTo(date2) == 0)
	 * {
	 *     ...
	 * }
	 * </PRE>
	 * 
	 * @return result -1 if less than, 0 if equal, +1 if greater than
	 * @param aDate
	 *            an instance of WayDate
	 */
	public int compareTo(WayDate aDate) {
		int result;
		if (isNull() && aDate.isNull())
			result = 0;
		else {
			try {
				date = date.asWayDateCalc();
				WayDateCalc dt2 = aDate.getDate().asWayDateCalc();
				result = ((WayDateCalc) date).compareTo(dt2);
			} catch (WayDateException e) {
				result = -1;
			}
		}
		return result;
	}

	/**
	 * Return true if this date is after the argument
	 * 
	 * this.isAfter(date) iff this > date
	 * 
	 * @param date
	 *            the date being compared
	 * @return true if this date is after the argument
	 */
	public boolean isAfter(WayDate date) {
		boolean result;
		if (isNull())
			result = false;
		else if (date.isNull())
			result = false;
		else
			result = (compareTo(date) == 1);
		return result;
	}

	/**
	 * Return true if this date is before the argument
	 * 
	 * @param date
	 *            the date being compared
	 * @return true iff this date is before the argument
	 */
	public boolean isBefore(WayDate date) {
		boolean result;
		if (isNull())
			result = false;
		else if (date.isNull())
			result = false;
		else
			result = (compareTo(date) == -1);
		return result;
	}

	/**
	 * Return true if this date is on or before the argument
	 * 
	 * @param date
	 *            the date being compared
	 * @return true iff this date is on or before the argument
	 */
	public boolean isOnOrBefore(WayDate date) {
		return !isAfter(date);
	}

	/**
	 * Return true if this date is on or after the argument
	 * 
	 * @param date
	 *            the date being compared
	 * @return true iff this date is on or after the argument
	 */
	public boolean isOnOrAfter(WayDate date) {
		return !isBefore(date);
	}

	/**
	 * Return true if this is the null date
	 * 
	 * @return true if this date is null
	 */
	public boolean isNull() {
		return date.isNull();
	}

	// -------------------------------------------------------------------------
	// Object Methods
	// -------------------------------------------------------------------------

	/**
	 * Return a copy of date. The copy always has an instance WayDateDisplay as
	 * its implementation object. This override is necessary because the clone()
	 * method in Object would return a copy pointing to the original date
	 * implementation object.
	 * 
	 * @return an instance of WayDate
	 * 
	 */
	public Object clone() {
		return new WayDate(date);
	}

	/**
	 * Return a string representation of the date.
	 * 
	 * <P>
	 * Example:
	 * </P>
	 * 
	 * <PRE>
	 * String date = aDate.toString();
	 * </PRE>
	 * 
	 * @return date a string representing the date. The format is DD-MMM-YYYY
	 * 
	 */
	public String toString() {
		String value;
		try {
			date = date.asWayDateDisplay();
			value = ((WayDateDisplay) date).toString();
		} catch (WayDateException e) {
			value = "null date";
		}
		return value;
	}

	/**
	 * Return true if another date is equal to this date. Two dates are
	 * considered equal if they both have the same month, day, and year. This
	 * override is necessary because two separate WayDate objects can be equal,
	 * but won't be identical.
	 * 
	 * @return true if dates have same month, day, and year
	 */
	public boolean equals(Object obj) {
		boolean result;

		if (obj instanceof WayDate) {
			result = (compareTo((WayDate) obj) == 0);
		} else
			result = false;
		return result;
	}

	/**
	 * Return a hash value. This method in fact returns the absolute date
	 * corresponding to the date. This override is necessary so that two
	 * separate date objects with the same month, day, and year will return the
	 * same hash code even though they are different objects.
	 * 
	 * @return hash code
	 */

	public int hashCode() {
		int code;
		try {
			WayDateCalc dt = date.asWayDateCalc();
			code = dt.getAbsoluteDate();
		} catch (WayDateException e) {
			code = 0;
		}
		return code;
	}

	// -------------------------------------------------------------------------
	// Internal Methods
	// -------------------------------------------------------------------------

	/**
	 * Return the instance of WayDateImpl for this date
	 * 
	 * @return an instance of the WayDate implementation
	 */
	protected WayDateImpl getDate() {
		return date;
	}
}

// ------------------------------------------------------------------------------
// Private classes
// ------------------------------------------------------------------------------

/**
 * This class is an abstract class that defines the interface to the WayDate
 * implementation classes and permits a type declaration common to both
 * subclasses.
 */

abstract class WayDateImpl implements Serializable {

	/**
	 * Internal serial UID
	 */
	private static final long serialVersionUID = 9701L;

	/** an array of days in months for non-leap year */
	private static final int daysNonLeapYear[] = { 31, 28, 31, 30, 31, 30, 31,
			31, 30, 31, 30, 31 };

	/** month abbreviations */
	private static final String mnthAbbrev[] = { "Jan", "Feb", "Mar", "Apr",
			"May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

	/**
	 * Return the number of days in a month. This routine assumes month and year
	 * are valid. It does not check, because checking is done in WayDate class.
	 * 
	 * <P>
	 * Example:
	 * </P>
	 * 
	 * <PRE>
	 * int days = date.daysInMonth(mnth, yr);
	 * </PRE>
	 * 
	 * @return the number of days in the month
	 * @param mnth
	 *            month (month inset range(1, 12))
	 * @param yr
	 *            year (used to determine if this is a leap year) (yr inset
	 *            range(MINYEAR, MAXYEAR))
	 */
	public static int daysInMonth(int mnth, int yr) {
		int dy;

		if (isLeapYear(yr) && (mnth == 2))
			dy = 29;
		else
			dy = daysNonLeapYear[mnth - 1];
		return dy;
	}

	/**
	 * Return true if year is a leap year. Routine assumes a valid year, but
	 * does not check for it.
	 * 
	 * <P>
	 * Example:
	 * </P>
	 * 
	 * <PRE>
	 * if (WayDateImpl.isLeapYear(yr)) {
	 * }
	 * </PRE>
	 * 
	 * @return result true if year is leap year false if year is not leap year
	 * @param yr
	 *            a year (yr inset range(MINYEAR, MAXYEAR))
	 * 
	 */
	public static boolean isLeapYear(int yr) {
		return ((yr % 400 == 0) || ((yr % 4 == 0) && (yr % 100 != 0)));
	}

	/**
	 * Return the number of days in a year. Method assumes a valid year, but
	 * does not check for one.
	 * 
	 * <P>
	 * Example:
	 * </P>
	 * 
	 * <PRE>
	 * int days = WayDateImpl.daysInYear(yr)
	 * </PRE>
	 * 
	 * @return days number of days in the year
	 * @param year
	 *            a year (yr inset range(MINYEAR, MAXYEAR))
	 * 
	 */
	public static int daysInYear(int year) {
		int result;
		if (isLeapYear(year))
			result = 366;
		else
			result = 365;
		return result;
	}

	/**
	 * Return the day of the year for a particular date. Validity of the date is
	 * assumed, but not checked.
	 * 
	 * <P>
	 * Example:
	 * </P>
	 * 
	 * <PRE>
	 * dayInYear = WayDateImpl.dayOfYear(2, 29, 2001);
	 * </PRE>
	 * 
	 * Reference: Dershowitz, Nachum, and Edward M. Reingold, Calendrical
	 * Calculations, (Cambridge, UK: Cambridge University Press, 1997) p. 36
	 * 
	 * @return dayOfYear the number of the day in the year (dayOfYear inset
	 *         range(1,...,daysInYear(yr)))
	 * @param mnth
	 *            month part of the date (mnth inset range(1,...,12)))
	 * @param dy
	 *            day part of the date (dy inset range(1,...,daysInMonth(mnth,
	 *            dy, yr)))
	 * @param yr
	 *            year part of the date (yr inset range(MINYEAR, MAXYEAR)))
	 */
	public static int dayOfYear(int mnth, int dy, int yr) {
		int dayInYear;

		dayInYear = (367 * mnth - 362) / 12 + dy;
		if (isLeapYear(yr) && (mnth > 2))
			dayInYear -= 1;
		else if (!isLeapYear(yr) && (mnth > 2))
			dayInYear -= 2;
		return (dayInYear);
	}

	/**
	 * Return a string with the abbreviated name of the month.
	 * 
	 * <P>
	 * Example:
	 * </P>
	 * 
	 * <PRE>
	 * String name = WayDate.monthString(12);
	 * </PRE>
	 * 
	 * @return name a string with the abbreviated name of the month
	 * @param mnth
	 *            the desired month (mnth inset range(1, 12})
	 * 
	 */
	public static String monthString(int mnth) {
		assert WayDate.isValidMonth(mnth) : "Illegal month: " + mnth;
		return mnthAbbrev[mnth - 1];
	}

	/**
	 * Return an instance of WayDateDisplay with current date (as obtained from
	 * the computer)
	 * 
	 * @return an instance of WayDateDisplay
	 */
	public static WayDateImpl today() {
		WayDateImpl aDate = null;
		try {
			// get the current date and time
			Date date = new Date();
			aDate = asWayDate(date);
		} catch (WayDateException e) {
			// this exception will never happend
		}
		return aDate;
	}

	/**
	 * Return an instance of WayDate corresponding to the instance of date
	 * provided as an input.
	 * 
	 * @param date
	 *            an instance of the Java Date class
	 * @return the corresponding instance of WayDate
	 * @exception WayDateException
	 *                if the date is outside the range of supported dates
	 */
	public static WayDateImpl asWayDate(Date date) throws WayDateException {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		int year = calendar.get(Calendar.YEAR); // full four digit year
		int month = calendar.get(Calendar.MONTH) + 1; // add one since first
														// month is 0
		int day = calendar.get(Calendar.DAY_OF_MONTH); // in set {1,...,31}
		return new WayDateDisplay(month, day, year);
	}

	// -------------------------------------------------------------------------
	// Conversion methods
	// -------------------------------------------------------------------------

	/**
	 * Return an instance of the date as a WayDateCalc object.
	 * 
	 * @return an instance of WayDateCalc equal to the current object
	 * @exception WayDateException
	 *                if attempting to work with null date
	 */
	public abstract WayDateCalc asWayDateCalc() throws WayDateException;

	/**
	 * Return an instance of the date as a WayDateDisplay object
	 * 
	 * @return an instance of WayDateDisplay equal to the current object
	 * @exception WayDateException
	 *                if attempting to work with null date
	 */
	public abstract WayDateDisplay asWayDateDisplay() throws WayDateException;

	/**
	 * Return true if this is a null date
	 */
	public abstract boolean isNull();

}

// -----------------------------------------------------------------------------
// Calculation Date Class
// -----------------------------------------------------------------------------

/**
 * This class performs calculations on dates. Its field consists of a single
 * absolute date field that facilitates calculations. Absolute date calculations
 * cannot generate integer overflow, because an absolute date is always positive
 * and less than the maximum size of an int.
 * 
 */
class WayDateCalc extends WayDateImpl implements Comparable<WayDateCalc> {

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	/**
	 * The serial verion unique identifier for this class
	 */
	private static final long serialVersionUID = 9702L;

	/**
	 * The maximum absolute date for 31-DEC-3999
	 */
	private static final int MAX_ABSOLUTE = 876216; // absolute date for
													// 31-DEC-3999

	/**
	 * The absolute date
	 */
	final private int abDate;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Given a date, return the number of days since 12/31/1600. Absolute dates,
	 * or the number of days from the beginning range of legal dates, are used
	 * to perform computations upon dates. Thus a date calculation can be
	 * performed by converting the input dates to absolute dates, performing the
	 * calculations, and converting the result back to a gregorian date.
	 * 
	 * <P>
	 * Example:
	 * </P>
	 * 
	 * <PRE>
	 * WayDateCalc date = new WayDateCalc(month, day, year);
	 * </PRE>
	 * 
	 * @param month
	 *            the month part of the date (month inset range(1, 12))
	 * @param day
	 *            the day part of the date (day inset range(1,
	 *            daysInMonth(month, year))
	 * @param year
	 *            the year part of the date (year inset range(MINYEAR, MAXYEAR))
	 */
	public WayDateCalc(int month, int day, int year) throws WayDateException {
		abDate = dayOfYear(month, day, year) + daysInPastYears(year - 1);
		return;
	}

	/**
	 * Alternate constructor that takes the absolute date as the parameter.
	 * 
	 * Precondition: adate <= MAX_ABSOLUTE
	 * 
	 * <P>
	 * Example:
	 * </P>
	 * 
	 * <PRE>
	 * WayDateCalc date = new WayDateCalc(adate);
	 * </PRE>
	 */
	public WayDateCalc(int adate) throws WayDateException {
		if (!isAbsoluteDate(adate))
			throw new WayDateException(WayDateException.ILLEGAL_ABSOLUTE, adate);
		abDate = adate;
		return;
	}

	// -------------------------------------------------------------------------
	// Static Methods
	// -------------------------------------------------------------------------

	/**
	 * Return true if the absolute date is valid
	 * 
	 * @param adate
	 *            an absolute date
	 * @return true if the absolute date is valid
	 */
	public static boolean isAbsoluteDate(int adate) {
		boolean result;
		if (adate < 1)
			result = false;
		else if (adate > MAX_ABSOLUTE)
			result = false;
		else
			result = true;
		return result;
	}

	/**
	 * Return the number of days in a specified years and priors years.
	 * <P>
	 * The algorithm works by counting the number of days in prior years since
	 * the beginning of 1601. Multiplying the number of years by 365 gives an
	 * initial estimate, but underestimates the number of days because it does
	 * not count leap days. Therefore, we add a day for every fourth year. This
	 * over-compensates, because some years divisible by 100 are not leap years.
	 * We therefore subtract the years divisible by 100. But this is too much of
	 * a correction, since years divisible by 400 are leap years. So we finally
	 * add in the number of years divisible by 400.
	 * </P>
	 * <P>
	 * Reference: Edward M. Reingold and Nachum Dershowitz, Calendrical
	 * Calculations: The Millennium Edition (Cambridge, UK: Cambridge University
	 * Press, 2001) p. 51
	 * </P>
	 * <P>
	 * Example:
	 * </P>
	 * 
	 * <PRE>
	 * days = WayDate.daysInPastYear(yr);
	 * </PRE>
	 * 
	 * @return days number of days in years prior to yr.
	 * @param yr
	 *            a year (yr inset range(MINYEAR - 1,...,MAXYEAR))
	 * 
	 */
	protected static int daysInPastYears(int yr) throws WayDateException {
		int days;
		if (yr == (WayDate.MINYEAR - 1)) {
			days = 0;
		} else if (!WayDate.isValidYear(yr))
			throw new WayDateException(WayDateException.ILLEGAL_YEAR, yr);
		else {
			int y = yr - 1600;

			days = 365 * y // days in prior years
					+ y / 4 // plus julian leap days in prior years
					- y / 100 // minus prior century years
					+ y / 400; // plus years divisible by 400
		}
		return days;
	}

	/**
	 * Return the year implied by an absolute date. The algorithm is from:
	 * 
	 * Edward M. Reingold and Nachum Dershowitz, Calendrical Calculations: The
	 * Millennium Edition (Cambridge, UK: Cambridge University Press, 2001) p.
	 * 52
	 * 
	 * @return year year associated with an absolute date (year inset
	 *         range(MINYEAR, MAXYEAR))
	 * @param abDate
	 *            an absolute date (abDate inset range(1, MAX_ABSOLUTE})
	 * 
	 */
	public static int yearFromAbsolute(int abDate) {
		int year;
		int d0 = abDate - 1;
		int n400 = d0 / 146097; // Number of days in a 400 year cycle:
								// 400*365 + 100 - 3
		int d1 = d0 % 146097;
		int n100 = d1 / 36524; // Number of days in 100 year cycle:
								// 365*100 + 25 - 1
		int d2 = d1 % 36524;
		int n4 = d2 / 1461; // Number of days in 4 year cycle:
							// 365*4 + 1
		int d3 = d2 % 1461;
		int n1 = d3 / 365; // Number of days in 1 year:
							// 365

		//
		// If n100 = 4 orelse n1 = 4, then the date is 31-Dec of the leap
		// year so we add one less year to get the absolute year
		//
		if ((n100 == 4) || (n1 == 4))
			year = 400 * n400 + 100 * n100 + 4 * n4 + n1 + 1600;
		else
			year = 400 * n400 + 100 * n100 + 4 * n4 + n1 + 1601;
		return year;
	}

	// -------------------------------------------------------------------------
	// Conversion Methods
	// -------------------------------------------------------------------------

	/**
	 * Return an instance of WayDateDisplay.
	 * 
	 * @return the current object
	 */
	@Override
	public WayDateDisplay asWayDateDisplay() {
		int year = yearFromAbsolute(abDate);
		int dayOfYear;
		try {
			dayOfYear = abDate - daysInPastYears(year - 1);
		} catch (WayDateException e) {
			// will never get here
			dayOfYear = 0;
		}
		return new WayDateDisplay(dayOfYear, year);
	}

	/**
	 * Return an instance of WayDateCalc with an equivalent value. Since this is
	 * a WayDateCalc object, return this.
	 * 
	 * @return an instance of WayDateCalc
	 */
	@Override
	public WayDateCalc asWayDateCalc() {
		return this;
	}

	// -------------------------------------------------------------------------
	// Date Caclulation Interface
	// -------------------------------------------------------------------------

	/**
	 * Add a number of days to a date.
	 * 
	 * <P>
	 * Example:
	 * </P>
	 * 
	 * <PRE>
	 * aDate = aDate.add(10);
	 * </PRE>
	 * 
	 * @param value
	 *            number of days to add to the date
	 */
	public WayDateCalc add(int value) throws WayDateException {
		int result = abDate + value;
		if (!isAbsoluteDate(result)) {
			throw new WayDateException(WayDateException.ILLEGAL_ABSOLUTE,
					result);
		}
		return new WayDateCalc(result);
	}

	/**
	 * Increases the date by one day.
	 * 
	 * <P>
	 * Example:
	 * </P>
	 * 
	 * <PRE>
	 * aDate.increment();
	 * </PRE>
	 */
	public WayDateCalc increment() throws WayDateException {
		int result = abDate + 1;
		if (!isAbsoluteDate(result)) {
			throw new WayDateException(WayDateException.ILLEGAL_ABSOLUTE,
					result);
		}
		return new WayDateCalc(result);
	}

	/**
	 * Decrement date.
	 * 
	 * <P>
	 * Example:
	 * </P>
	 * 
	 * <PRE>
	 * aDate.decrement();
	 * </PRE>
	 */
	public WayDateCalc decrement() throws WayDateException {
		int result = abDate - 1;
		if (!isAbsoluteDate(result)) {
			throw new WayDateException(WayDateException.ILLEGAL_ABSOLUTE,
					result);
		}
		return new WayDateCalc(result);
	}

	/**
	 * Subtract two dates and return the number of days between the two.
	 * 
	 * <P>
	 * Example:
	 * </P>
	 * 
	 * <PRE>
	 * diff = aDate.difference(anotherDate);
	 * </PRE>
	 * 
	 * @return the number of days between the dates.
	 * @param anotherDate
	 *            a date
	 */
	public int difference(WayDateImpl anotherDate) throws WayDateException {
		WayDateCalc date2 = anotherDate.asWayDateCalc();
		return abDate - date2.getAbsoluteDate();
	}

	/**
	 * Return the day of the week as an integer. This method depends on the fact
	 * that 1-Jan-1601 is a Monday.
	 * 
	 * <P>
	 * Example:
	 * </P>
	 * 
	 * <PRE>
	 * dayOfWk = date.getDayOfWeek();
	 * </PRE>
	 * 
	 * @return dayOfWk 0 for Sunday, 1 for Monday, etc. (dayOfWk inset range(0,
	 *         6))
	 * 
	 */
	public int getDayOfWeek() {
		return abDate % 7;
	}

	// -------------------------------------------------------------------------
	// Comparison Interface
	// -------------------------------------------------------------------------

	/**
	 * Compare a date with the current date. If the current date is less than
	 * supplied date, return -1. If the current date is greater than supplied
	 * date, return +1. If the current date is equal to supplied date, return 0.
	 * 
	 * <P>
	 * Example:
	 * </P>
	 * 
	 * <PRE>
	 * if (date1.compareTo(date2) == 0)
	 * {
	 *     ...
	 * }
	 * </PRE>
	 * 
	 * @return result -1 if less than, 0 if equal, +1 if greater than
	 * @param aDate
	 *            a date
	 */
	public int compareTo(WayDateCalc aDate) {
		int abDate2 = aDate.getAbsoluteDate();
		int result;

		if (abDate2 > abDate) {
			result = -1;
		} else if (abDate2 < abDate) {
			result = 1;
		} else {
			result = 0;
		}
		return result;
	}

	/**
	 * Return true if this instance is an instance of the null date
	 * 
	 * @return false since this class is not the null class
	 */
	public boolean isNull() {
		return false;
	}

	/**
	 * Return the absolute date value
	 * 
	 * @return absolute date
	 */
	public int getAbsoluteDate() {
		return abDate;
	}
}

// -----------------------------------------------------------------------------
// Display Date Class
// -----------------------------------------------------------------------------

/**
 * This class handles displaying of dates. Its fields separately record the
 * month, day, and year of the date.
 * 
 */
class WayDateDisplay extends WayDateImpl {

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	/**
	 * The serial verion unique identifier for this class
	 */
	private static final long serialVersionUID = 9703L;

	// Fields

	/**
	 * The day portion of the date
	 */
	protected final int day;

	/**
	 * The month portion of the date
	 */
	protected final int month;

	/**
	 * The year portion of the date
	 */
	protected final int year;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a WayDate object with the specified month, day, and year.
	 * 
	 * <P>
	 * Example:
	 * </P>
	 * 
	 * <PRE>
	 * WayDate aDate = new WayDate(12, 22, 1998);
	 * </PRE>
	 * 
	 * @param _month
	 *            month of the date (_month inset range(1, 12))
	 * @param _day
	 *            day of the date (_day inset range(1, daysInMonth(_month)))
	 * @param _year
	 *            year of the date (_year inset range(MINYEAR, MAXYEAR))
	 * @exception WayDateException
	 *                thrown if the month, day, and year are not a proper date
	 * 
	 */
	public WayDateDisplay(int _month, int _day, int _year)
			throws WayDateException {
		if (WayDate.isValidDate(_month, _day, _year)) {
			month = _month;
			day = _day;
			year = _year;
		} else {
			String dateString = _month + "-" + _day + "-" + _year;
			throw new WayDateException(WayDateException.ILLEGAL_DATE,
					dateString);
		}
	}

	/**
	 * Return an instance of WayDateDisplay the day of the year of which
	 * corresponds to the input to this constructor. This constructor and the
	 * accessors getDayOfYear() and getYear() provide the capability to work
	 * with day of year (or Julian date) calculations.
	 * 
	 * <P>
	 * Example:
	 * </P>
	 * 
	 * <PRE>
	 * WayDate date = new WayDate(dayOfYear, yr);
	 * </PRE>
	 * 
	 * @param dayYear
	 *            day of the year for the date (dayOfYear inset
	 *            range(1,daysInYear(year))
	 * @param yr
	 *            year of the date (yr inset range(MINYEAR, MAXYEAR))
	 */
	public WayDateDisplay(int dayYear, int yr) {
		int mnth = 1;
		int dy = 1;
		year = yr;
		int diff = dayYear - 1; // dayOfYear(1, 1, year) = 1

		/*
		 * <p>Precondition:</p> isValidYear(yr) andalso
		 * isValidDayOfYear(dayYear) Loop Invariant: diff = dayYear -
		 * dayOfYear(mnth, dy, yr) andalso mnth inset range(1, 12) andalso dy
		 * inset range(1, daysInMonth(mnth,yr))
		 * 
		 * Bound Function: fun diff(dayYear : int, mnth : Month, dy : Day, year
		 * : Year) : int= dayYear - dayOfYear(mnth, dy, year);
		 * 
		 * Termination: diff = 0 Postcondition: dayOfYear = dayOfYear(mnth, dy,
		 * yr) andalso isValidDate(mnth, dy, yr)
		 */

		/*
		 * Theorem 1: Changes in dayOfYear, month > 1
		 * 
		 * Precondition: month inset range(2, 12) Postcondition:
		 * daysInMonth(month, year) = dayOfYear(month, day, year) -
		 * dayOfYear(month-1, day, year) Proof: From definiton of dayOfYear()
		 */

		/*
		 * Theorem 2: Changes in dayOfYear, month = 1
		 * 
		 * Precondition: month = 1 andalso day = 31 Postcondition:
		 * daysInMonth(month, year) = dayOfYear(month, day, year)
		 * 
		 * Proof: From definition of dayOfYear()
		 */

		/*
		 * Theorem 3: Change in day of month
		 * 
		 * <pre> Precondition: diff < daysInMonth(month, year) Postcondition:
		 * diff = dayOfYear(month, diff + 1, year) - dayOfYear(month, 1, year)
		 * </pre>
		 * 
		 * Proof: From definition of dayOfYear()
		 */

		/*
		 * Proof Obligation 1: the invariant holds at the beginning
		 * 
		 * Proof: From: dayOfYear(1, 1, yr) = 1 1. diff = dayYear - dayOfYear(1,
		 * 1, yr) 2. 1 inset range(1, 12) 3. 1 inset range(1,
		 * daysInMonth(mnth,yr)) Infer: invariant holds
		 */
		
		while (diff > 0) {
			if (diff >= daysInMonth(mnth, year)) {
				diff -= daysInMonth(mnth, year);
				mnth++;

				/*
				 * Proof Obligation 2: the invariant still holds
				 * 
				 * Proof: From: diff = diff' - daysInMonth(mnth, year) 1. diff =
				 * diff' - dayOfYear(mnth, year) + dayOfYear(mnth-1, year)
				 * {Theorem 1 and 2} 2. diff' = dayYear - dayOfYear(mnth-1, 1,
				 * year) 3. diff = dayYear - dayOfYear(mnth , 1, year) 4. dy = 1
				 * 5. mnth inset range(1, 12) {Precondition} Infer: diff =
				 * dayYear - dayOfYear(mnth, 1, yr) andalso mnth inset range(1,
				 * 12) andalso dy inset range(1, daysInMonth(mnth,yr))
				 */
			} else {
				dy = diff + 1;
				diff = 0;

				/*
				 * Proof Obligation 2: the invariant still holds
				 * 
				 * Proof: From: dy = diff + 1 andalso diff = 0 1. mnth inset
				 * range(1,12) {invariant prior iteration} 2. dy inset range(1,
				 * daysInMonth(mnth,yr)) {1st guard command} 3. diff' = dayYear
				 * - dayOfYear(mnth, 1, yr) 4. diff' = dayYear - dayOfYear(mnth,
				 * diff' + 1, yr) + diff' {Theorem 3} 5. 0 = dayYear -
				 * dayOfYear(mnth, dy, yr) 6. diff = 0 7. diff = dayYear -
				 * dayOfYear(mnth, dy, yr) Infer: diff = dayYear -
				 * dayOfYear(mnth, dy, yr) andalso mnth inset range(1, 12)
				 * andalso dy inset range(1, daysInMonth(mnth,yr))
				 */
			}
		}

		day = dy;
		month = mnth;

		/*
		 * Proof Obligation 5: Each iteration reduces bound function
		 * 
		 * Proof: Under first guard, diff is reduced by dayInMonth which is
		 * always positive.
		 * 
		 * Under second guard, diff is set to 0 terminating iteration
		 */

		/*
		 * Proof Obligation 4: Bound function is positive if loop has not
		 * terminated.
		 * 
		 * Proof: diff must be positive else loop terminates
		 */

		/*
		 * Proof Obligation 3: Postcondition is true
		 * 
		 * From: 0 = dayYear - dayOfYear(mnth, dy, yr) andalso mnth inset
		 * range(1, 12) andalso dy inset range(1, daysInMonth(mnth,yr)) 1.
		 * dayYear = dayOfYear(mnth, dy, yr) andalso mnth inset range(1, 12)
		 * andalso dy inset range(1, daysInMonth(mnth, yr)) Infer: postcondition
		 * holds
		 */
		return;
	}

	// -------------------------------------------------------------------------
	// Conversion Routines
	// -------------------------------------------------------------------------

	/**
	 * Return an instance of WayDateDisplay. Since this object is a
	 * WayDateDisplay, just return the current object.
	 * 
	 * @return the current object
	 */
	public WayDateDisplay asWayDateDisplay() {
		return this;
	}

	/**
	 * Return an instance of WayDateCalc
	 * 
	 * @return an instance of WayDateCalc
	 */
	public WayDateCalc asWayDateCalc() {
		WayDateCalc date;
		try {
			date = new WayDateCalc(month, day, year);
		} catch (WayDateException e) {
			// will never get here
			date = null;
		}
		return date;
	}

	// -------------------------------------------------------------------------
	// Accessor Functions
	// -------------------------------------------------------------------------

	/**
	 * Return the day part of the date.
	 * 
	 * <P>
	 * Example:
	 * </P>
	 * 
	 * <PRE>
	 * int day = aDate.getDay();
	 * </PRE>
	 * 
	 * @return day the day part of the date
	 */
	public int getDay() {
		return day;
	}

	/**
	 * Return the month part of the date.
	 * 
	 * <P>
	 * Example:
	 * </P>
	 * 
	 * <PRE>
	 * int month = aDate.getMonth();
	 * </PRE>
	 * 
	 * @return month month part of the date.
	 */

	public int getMonth() {
		return month;
	}

	/**
	 * Return the year part of the date.
	 * 
	 * <P>
	 * Example:
	 * </P>
	 * 
	 * <PRE>
	 * int year = aDate.getYear();
	 * </PRE>
	 * 
	 * @return year the year part of the date.
	 */
	public int getYear() {
		return year;
	}

	/**
	 * Return true if the date is in a leap year.
	 * <P>
	 * Example:
	 * </P>
	 * 
	 * <PRE>
	 * if (date.isLeapYear()) {
	 * }
	 * </PRE>
	 * 
	 * @return true if date is in a leap year
	 */
	public boolean isLeapYear() {
		return isLeapYear(year);
	}

	/**
	 * Return the day of the year for a date.
	 * 
	 * <P>
	 * Example:
	 * </P>
	 * 
	 * <PRE>
	 * int dayYear = date.getDayOfYear();
	 * </PRE>
	 * 
	 * @return the day of the year for the date.
	 */
	public int getDayOfYear() {
		return dayOfYear(month, day, year);
	}

	/**
	 * Return a string representation of the date.
	 * 
	 * <P>
	 * Example:
	 * </P>
	 * 
	 * <PRE>
	 * String date = aDate.toString();
	 * </PRE>
	 * 
	 * @return date a string representing the date. The format is DD-MMM-YYYY
	 * 
	 */
	public String toString() {
		String dayString = String.valueOf(day);
		if (dayString.length() == 1)
			dayString = "0" + dayString;
		String dateString = dayString + "-" + monthString(month) + "-"
				+ String.valueOf(year);
		return dateString;
	}

	/**
	 * Return a string representation of the date in the ISO format.
	 * 
	 * <P>
	 * Example:
	 * </P>
	 * 
	 * <PRE>
	 * String date = aDate.toISODate();
	 * </PRE>
	 * 
	 * @return date a string representing the date. The format is YYYY-MM-DD
	 * 
	 */
	public String toISODate() {
		//
		// Format the day
		//
		String dayString = String.valueOf(day);
		if (dayString.length() == 1)
			dayString = "0" + dayString;
		//
		// Format the month
		//
		String monthString = String.valueOf(month);
		if (monthString.length() == 1)
			monthString = "0" + monthString;
		//
		// Format the year
		//
		String yearString = String.valueOf(year);
		//
		// Format the date string
		//
		String dateString = yearString + "-" + monthString + "-" + dayString;
		return dateString;
	}

	/**
	 * Return true if this instance is an instance of the null date
	 * 
	 * @return false since this class is not the null class
	 */
	public boolean isNull() {
		return false;
	}
}

// -----------------------------------------------------------------------------
// Null WayDate Class
// -----------------------------------------------------------------------------

/**
 * This class is a null class that indicates that
 */
class NullWayDate extends WayDateImpl {

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	/**
	 * The serial verion unique identifier for this class
	 */
	private static final long serialVersionUID = 9704L;

	public static final WayDateImpl NULL_DATE = new NullWayDate();

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Return an instance of this class
	 */
	public NullWayDate() {

	}

	// -------------------------------------------------------------------------
	// Conversion Functions
	// -------------------------------------------------------------------------
	/**
	 * Return true if this instance is an instance of the null date
	 * 
	 * @return true since this class is the null class
	 */
	public boolean isNull() {
		return true;
	}

	/**
	 * Return an instance of WayDateDisplay. Since this object is a
	 * WayDateDisplay, just return the current object.
	 * 
	 * @return the current object
	 */
	public WayDateDisplay asWayDateDisplay() throws WayDateException {
		throw new WayDateException(WayDateException.NULL_DATE, "");
	}

	/**
	 * Return an instance of WayDateCalc
	 * 
	 * @return an instance of WayDateCalc
	 */
	public WayDateCalc asWayDateCalc() throws WayDateException {
		throw new WayDateException(WayDateException.NULL_DATE, "");
	}
}
