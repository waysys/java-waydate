//------------------------------------------------------------------------------
//      Compilation Unit Header
//------------------------------------------------------------------------------
//
//  Copyright (c) 2006 Waysys LLC. All Rights Reserved.
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
//  For further information, contact Waysys LLC at waysys@waysyweb.com
//  or 800-622-5315 (USA).
//
//------------------------------------------------------------------------------
//      Maintenance History
//------------------------------------------------------------------------------
//
//  Person    Date          Change
//  ------    -----------   ----------------------------------------------------
//
//  Shaffer   09-Jul-2006   File created
//  Shaffer   07-Jan-2007   Removed reference to WayDateException
//  Shaffer   02-May-2012   Changed positions to enumerations
//
//------------------------------------------------------------------------------
//      Imports
//------------------------------------------------------------------------------

package com.waysysweb.util;

//------------------------------------------------------------------------------
//      Public Class Declaration
//------------------------------------------------------------------------------

/**
 * This class computes a set of dates corresponding to various holidays. The
 * class generates instances of the WayDate class.
 * 
 * @see com.waysysweb.util.WayDate
 * @see <a href="http://en.wikipedia.org/wiki/Federal_holidays#List_of_Holidays"
 *      >Wikipedia</a>
 * @see <a href="http://www.opm.gov/fedhol/2006.asp" >Federal Holidays</a>
 * @see <a href="http://aa.usno.navy.mil/faq/docs/easter.html" >Easter</a>
 * 
 * @author William A. Shaffer
 * @version 4.00
 */

public class Holiday {
	
	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------
	
	/**
	 * Enumerated values indicating the position in a month.
	 */
	public enum Position {
		FIRST, SECOND, THIRD, FOURTH, LAST
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	// ------------------------------------------------------------------------------
	// Holiday Methods
	// ------------------------------------------------------------------------------

	/**
	 * Return the date for New Year's Day
	 * 
	 * @param year
	 *            the desired year (year in set range(MINYEAR, MAXYEAR))
	 * @return the date for New Year's Day
	 * @exception WayDateException
	 *         thrown if year is outside of legal values
	 */
	public static WayDate getNewYearsDay(int year)
		throws WayDateException {
		WayDate date = null;
		if (WayDate.isValidYear(year)) {
			date = new WayDate(WayDate.JANUARY, 1, year);
		}
		else {
			throw new WayDateException(WayDateException.ILLEGAL_YEAR, year);
		}
		return date;
	}

	/**
	 * Return the date for Birthday of Martin Luther King, Jr.
	 * 
	 * @param year
	 *            the desired year (year inset range(MINYEAR, MAXYEAR))
	 * @return the date for Birthday of Martin Luther King, Jr.
	 * @exception WayDateException
	 *                thrown if date is out of range
	 */
	public static WayDate getMartinLutherKingsBirthday(int year)
			throws WayDateException {
		WayDate date = null;
		if (WayDate.isValidYear(year)) {
			date = getDateFromPosition(WayDate.JANUARY, year,WayDate.MONDAY, Position.THIRD);
		}
		else {
			throw new WayDateException(WayDateException.ILLEGAL_YEAR, year);
		}
		return date;
	}

	/**
	 * Return the date for Birthday of George Washington
	 * 
	 * @param year
	 *            the desired year (year inset range(MINYEAR, MAXYEAR))
	 * @return the date for Birthday of George Washington
	 * @exception WayDateException
	 *                thrown if date is out of range
	 */
	public static WayDate getWashingtonsBirthday(int year)
			throws WayDateException {
		WayDate date = null;
		if (WayDate.isValidYear(year)) {
			date = getDateFromPosition(WayDate.FEBRUARY, year, WayDate.MONDAY, Position.THIRD);
		}
		else {
			throw new WayDateException(WayDateException.ILLEGAL_YEAR, year);
		}		
		return date;
	}

	/**
	 * Return the date of Easter
	 * 
	 * @param year the desired year (year inset range(MINYEAR, MAXYEAR))
	 * @return the date of easter
	 * @exception WayDateException thrown if date is out of range
	 */
	public static WayDate getEaster(int year) throws WayDateException {
		WayDate date = null;
		if (WayDate.isValidYear(year)) {
			WayDate datePaschalMoon = paschalMoon(year);
			date = getDateOnDayOfWeekAfter(datePaschalMoon,WayDate.SUNDAY);
		}
		else {
			throw new WayDateException(WayDateException.ILLEGAL_YEAR, year);
		}
		return date;
	}
	
	/**
	 * Return the date for Memorial Day
	 * 
	 * @param year
	 *            the desired year (year in set range(MINYEAR, MAXYEAR))
	 * @return the date for Memorial day
	 * @exception WayDateException
	 *                thrown if date is out of range
	 */
	public static WayDate getMemorialDay(int year) throws WayDateException {
		WayDate date = null;
		if (WayDate.isValidYear(year)) {
			date = getDateFromPosition(WayDate.MAY, year, WayDate.MONDAY, Position.LAST);
		}
		else {
			throw new WayDateException(WayDateException.ILLEGAL_YEAR, year);
		}		
		return date;
	}

	/**
	 * Return the date for Independence Day
	 * 
	 * @param year
	 *            the desired year (year in set range(MINYEAR, MAXYEAR))
	 * @return the date for Independence Day
	 * @exception WayDateException if year is invalid
	 */
	public static WayDate getIndependenceDay(int year)
		throws WayDateException {
		WayDate date = null;
		if (WayDate.isValidYear(year)) {
			date = new WayDate(WayDate.JULY, 4, year);
		}
		else {
			throw new WayDateException(WayDateException.ILLEGAL_YEAR, year);
		}				
		return date;
	}

	/**
	 * Return the date for Labor Day
	 * 
	 * @param year
	 *            the desired year (year in set range(MINYEAR, MAXYEAR))
	 * @return the date for Labor Day
	 * @exception WayDateException
	 *                thrown if date is out of bounds
	 */
	public static WayDate getLaborDay(int year) throws WayDateException {
		WayDate date = null;
		if (WayDate.isValidYear(year)) {
			date = getDateFromPosition(WayDate.SEPTEMBER, year, WayDate.MONDAY, Position.FIRST);
		}
		else {
			throw new WayDateException(WayDateException.ILLEGAL_YEAR, year);
		}			
		return date;
	}

	/**
	 * Return the date for Columbus Day
	 * 
	 * @param year
	 *            the desired year (year in set range(MINYEAR, MAXYEAR))
	 * @return the date for Columbus Day
	 * @exception WayDateException
	 *            thrown if year is invalid
	 */
	public static WayDate getColumbusDay(int year) throws WayDateException {
		WayDate date = null;
		if (WayDate.isValidYear(year)) {
			date = getDateFromPosition(WayDate.OCTOBER, year, WayDate.MONDAY, Position.SECOND);
		}
		else {
			throw new WayDateException(WayDateException.ILLEGAL_YEAR, year);
		}					
		return date;
	}

	/**
	 * Return the date for Veterans' Day
	 * 
	 * @param year
	 *            the desired year (year in set range(MINYEAR, MAXYEAR))
	 * @return the date for Veterans' Day
	 * @exception WayDateException
	 *            thrown if year is invalid
	 */
	public static WayDate getVeteransDay(int year) 
		throws WayDateException {
		WayDate date = null;
		if (WayDate.isValidYear(year)) {
			date = new WayDate(WayDate.NOVEMBER, 11, year);
		}
		else {
			throw new WayDateException(WayDateException.ILLEGAL_YEAR, year);
		}				
		return date;
	}

	/**
	 * Return the date for Thanksgiving
	 * 
	 * @param year
	 *            the desired year (year in set range(MINYEAR, MAXYEAR))
	 * @return the date for Thanksgiving
	 * @exception WayDateException
	 *                thrown if date is out of bounds
	 */
	public static WayDate getThanksgiving(int year) throws WayDateException {
		WayDate date = null;
		if (WayDate.isValidYear(year)) {
			date = getDateFromPosition(WayDate.NOVEMBER, year, 
					WayDate.THURSDAY, Position.FOURTH);
		}
		else {
			throw new WayDateException(WayDateException.ILLEGAL_YEAR, year);
		}						
		return date;
	}

	/**
	 * Return the date for Christmas.
	 * 
	 * @param year
	 *            the desired year (year in set range(MINYEAR, MAXYEAR))
	 * @return the date for Christmas
	 */
	public static WayDate getChristmas(int year) 
		throws WayDateException {
		WayDate date = null;
		if (WayDate.isValidYear(year)) {
			date = new WayDate(WayDate.DECEMBER, 25, year);
		}
		else {
			throw new WayDateException(WayDateException.ILLEGAL_YEAR, year);
		}						
		return date;
	}

	// -------------------------------------------------------------------------
	// Date Support Methods
	// -------------------------------------------------------------------------

	/**
	 * Return a date on a specified day of the week on or before a specified
	 * date.
	 * 
	 * @param inputDate
	 *            the date being considered
	 * @param dayOfWeek
	 *            the day of the week (dayOfWeek in set range(0, 6))
	 * @return the date such that dayOfWeek(date) = dayOfWeek andalso
	 *         compare(inputDate, date) <> LESS
	 * @exception WayDateException
	 *                thrown if the result of adds are out of bounds
	 */
	protected static WayDate getDateOnDayOfWeekOnOrBefore(WayDate inputDate,
			int dayOfWeek) throws WayDateException {
		assert WayDate.isValidDayOfWeek(dayOfWeek) : "Invalid day of week "
				+ dayOfWeek;
		WayDate date1 = inputDate.add(-dayOfWeek);
		int priorDayOfWeek = date1.getDayOfWeek();
		WayDate date2 = inputDate.add(-priorDayOfWeek);
		return date2;
	}

	/**
	 * Return the date on a specified day of the week after a specified date
	 * 
	 * @param inputDate
	 *            the date being considered
	 * @param dayOfWeek
	 *            the day of the week (dayOfWeek in set range(0, 6))
	 * @return the date such that dayOfWeek(date) = dayOfWeek andalso
	 *         compare(inputDate, date) = GREATER
	 * @exception WayDateException
	 *                thrown if the result of adds are out of bounds
	 */
	protected static WayDate getDateOnDayOfWeekAfter(WayDate inputDate,
			int dayOfWeek) throws WayDateException {
		assert WayDate.isValidDayOfWeek(dayOfWeek) : "Invalid day of week "
				+ dayOfWeek;
		WayDate date = inputDate.add(7);
		date = getDateOnDayOfWeekOnOrBefore(date, dayOfWeek);
		return date;
	}

	/**
	 * Return the date on a specified day of the week after a specified date
	 * 
	 * @param inputDate
	 *            the date being considered
	 * @param dayOfWeek
	 *            the day of the week (dayOfWeek inset range(0, 6))
	 * @return the date such that dayOfWeek(date) = dayOfWeek andalso
	 *         compare(inputDate, date) = GREATER
	 * @exception WayDateException
	 *                thrown if the result of adds are out of bounds
	 */
	protected static WayDate getDateOnDayOfWeekBefore(WayDate inputDate,
			int dayOfWeek) throws WayDateException {
		assert WayDate.isValidDayOfWeek(dayOfWeek) : "Invalid day of week "
				+ dayOfWeek;
		WayDate date = inputDate.add(-1);
		date = getDateOnDayOfWeekOnOrBefore(date, dayOfWeek);
		return date;
	}

	/**
	 * Return the nth day that is on a specified day of the week from a
	 * specified date.
	 * 
	 * @param inputDate
	 *            the date being considered
	 * @param dayOfWeek
	 *            the day of the week (dayOfWeek inset range(0, 6))
	 * @param monthPosition
	 *            the position in the month (monthPosition inset range(0, 4)
	 * @return a date in the Nth position
	 * @exception WayDateException
	 *                thrown if adding offset puts the date out of bound
	 */
	protected static WayDate getNthDayFromDate(WayDate inputDate, int dayOfWeek,
			Position monthPosition) throws WayDateException {
		WayDate date;
		int offset = 0;
		assert WayDate.isValidDayOfWeek(dayOfWeek) : 
			"Invalid day of week " + dayOfWeek;
		//
		// Determine offset
		//
		switch (monthPosition) {
		case FIRST:
			offset = 7;
			break;
		case SECOND:
			offset = 14;
			break;
		case THIRD:
			offset = 21;
			break;
		case FOURTH:
			offset = 28;
			break;
		case LAST:
			offset = -7;
			break;
		default:
			assert false : "Illegal month position";
		}

		//
		// Get working date
		//
		if (monthPosition == Position.LAST)
			date = getDateOnDayOfWeekAfter(inputDate, dayOfWeek);
		else
			date = getDateOnDayOfWeekBefore(inputDate, dayOfWeek);

		//
		// Add the offset
		//
		date = date.add(offset);
		return date;
	}

	/**
	 * Return a date with the specified month and year and day of week. The
	 * month position of the date is as specified.
	 * 
	 * @param month
	 *            the month of the date (month inset range(1, 12))
	 * @param year
	 *            the year of the date (year inset range(MINYEAR, MAXYEAR))
	 * @param dayOfWk
	 *            the day of the week (dayOfWeek inset range(SUNDAY, SATURDAY))
	 * @param monthPosition
	 *            the month position (monthPosition inset range(LAST, FOURTH))
	 * @return an instance of WayDate with the specified month, year, day of
	 *         week and month position
	 * @exception WayDateException
	 *                thrown if calculation puts date out of range
	 */
	protected static WayDate getDateFromPosition(int month, int year, int dayOfWk,
			Position monthPosition) throws WayDateException {
		assert WayDate.isValidMonth(month)       : "Illegal month: " + month;
		assert WayDate.isValidYear(year)         : "Illegal year: " + year;
		assert WayDate.isValidDayOfWeek(dayOfWk) : "Illegal day of week: "
				+ dayOfWk;
		//
		// Create initial date
		//
		WayDate date;
		if (monthPosition == Position.LAST)
			date = new WayDate(month, WayDate.daysInMonth(month, year), year);
		else
			date = new WayDate(month, 1, year);

		//
		// Move the date to the proper position
		//
		date = getNthDayFromDate(date, dayOfWk, monthPosition);
		return date;
	}
	
	/**
	 * Return the date a holiday would be observed if it were a federal holiday.
	 * 
	 * @param date
	 *            the holiday being checked
	 * @return the date a holiday will be observed
	 * @exception WayDateException thrown if date is minimum or maximum date
	 */
	public static WayDate getObservedHoliday(WayDate date) 
		throws WayDateException {
		int dayOfWeek = date.getDayOfWeek();
		switch (dayOfWeek) {
			case WayDate.SUNDAY:
				date = date.increment();
				break;
			case WayDate.SATURDAY:
				date = date.decrement();
				break;
			// default: return original date
		}
		return date;
	}

	// -------------------------------------------------------------------------
	// Easter Calculations
	// -------------------------------------------------------------------------
	
	/**
	 * Return the century of the year
	 * 
	 * @param year the year of interest
	 * @return the century of the year
	 */
	protected static int century(int year) {
		return (year / 100) + 1;
	}

	/**
	 * Return the shifted epach
	 * 
	 * @param year the year of interest
	 * @return the shifted epach
	 */
	protected static int shiftedEpach(int year) {
		int cent = century(year);
		int result = 
			(14 + (11 * (year % 19)) - ((3 * cent) / 4) + ((5 + 8 * cent) / 25)) % 30;
        return result;
	}

	/**
	 * Return the adjusted epach
	 * 
	 * @param year the year of interest
	 */
	protected static int adjustedEpach(int year) {
		int result;
		int shifted = shiftedEpach(year);
		if ((shifted == 0) ||
			(shifted == 1) && (10 < (year % 19))	
		) result = shifted + 1;
		else result = shifted;
        return result;		
	}

	/**
	 * Return the date of the Pascal Moon
	 * 
	 * @param year the year of interest
	 * @return the date of the Pascal Moon
	 */
	protected static WayDate paschalMoon(int year) 
		throws WayDateException {
		WayDate date = new WayDate(WayDate.APRIL, 19, year);
		int adjustment = adjustedEpach(year);
		date = date.add(-adjustment);
		return date;
	}
	
}