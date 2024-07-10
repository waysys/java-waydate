//------------------------------------------------------------------------------
//      Compilation Unit Header
//------------------------------------------------------------------------------
//
//  Copyright (c) 2006, 2012 Waysys, LLC All Rights Reserved.
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
//  Shaffer   09-Jul-2000   File create
//  Shaffer   07-Nov-2006   Converted to JUnit 4
//  Shaffer   02-May-2012   Names of tests updated
//
//------------------------------------------------------------------------------
//      Package Declaration
//------------------------------------------------------------------------------

package com.waysysweb.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;

import junit.framework.JUnit4TestAdapter;

import org.easyspec.Context;
import org.easyspec.EasySpec;
import org.junit.Test;

//------------------------------------------------------------------------------
//      Public Class Declaration
//------------------------------------------------------------------------------

/**
 * This is the test harness for testing WayDate. It uses the JUnit test
 * framework developed by Erich Gamma and Kent Beck.
 * 
 * @author William A. Shaffer
 * @version 4.00
 */
@EasySpec(interest = "WayDate")
public class DateTest {

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	public static final WayDateException ERR_MESSAGE = new WayDateException(
			WayDateException.NULL_DATE, "");

	// -------------------------------------------------------------------------
	// Constructor
	// -------------------------------------------------------------------------

	/**
	 * Create an instance of this class.
	 * 
	 */
	public DateTest() {
		super();
		return;
	}

	// -------------------------------------------------------------------------
	// Support Functions
	// -------------------------------------------------------------------------

	/**
	 * Return an instance of junit.framework.Test
	 * 
	 * @return a junit test adapter
	 */
	public static junit.framework.Test suite() {
		junit.framework.Test suite = new JUnit4TestAdapter(HolidayTest.class);
		return suite;
	}

	/**
	 * Perform initial setup
	 * 
	 */
	@Context("Various WayDates")
	@org.junit.Before
	public void setUp() {
		return;
	}

	/**
	 * Perform final teardown
	 * 
	 */
	@org.junit.After
	public void tearDown() {
		return;
	}

	/**
	 * Utility routine to calculate absolute date from Gregorian date
	 * 
	 * @param date
	 *            the date to convert to absolute
	 * @return the absolute date
	 */
	public static int absoluteFromGregorian(WayDate date) throws Exception {
		WayDate epoc = new WayDate(1, 1, 1601);
		int count = date.difference(epoc) + 1;
		return count;
	}

	// -------------------------------------------------------------------------
	// Tests
	// -------------------------------------------------------------------------

	/**
	 * Test that postconditions hold for month/day/year constructor
	 */
	@Test
	public void test_maintains_post_condition_for_first_constructor()
			throws Exception {
		for (int year = WayDate.MINYEAR; year <= WayDate.MAXYEAR; year++) {
			for (int month = 1; month <= 12; month++) {
				for (int day = 1; day <= WayDate.daysInMonth(month, year); day++) {
					WayDate result = new WayDate(month, day, year);
					assertEquals(month, result.getMonth());
					assertEquals(day, result.getDay());
					assertEquals(year, result.getYear());
					assertFalse(result.isNull());
					assertTrue(result.isValid());
				}
			}
		}
		return;
	}

	/**
	 * Test that postconditions hold for day of year/year constructor
	 */
	@Test
	public void test_maintains_post_condition_for_second_constructor()
			throws Exception {
		WayDate result;
		for (int year = WayDate.MINYEAR; year <= WayDate.MAXYEAR; year++) {
			for (int dayInYear = 1; dayInYear <= WayDate.daysInYear(year); dayInYear++) {
				result = new WayDate(dayInYear, year);
				assertEquals(dayInYear, result.getDayOfYear());
			}
		}
		return;
	}

	/**
	 * Test creation of dates at beginning and end of month.
	 */
	@Test
	public void test_can_create_dates_all_months_first_and_last_days()
			throws Exception {
		int month;
		int days[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		for (month = 1; month <= 12; month++) {
			WayDate aDate1 = new WayDate(month, 1, 1997);
			WayDate aDate2 = new WayDate(month, days[month - 1], 1997);
			assertTrue("first date not before last date",
					aDate1.compareTo(aDate2) < 0);
		}
		return;
	}

	/**
	 * Test creation of dates from absolute date
	 */
	@Test
	public void test_can_create_absolute_dates() throws Exception {
		//
		// Minimum date
		//
		WayDate date1 = new WayDate(1);
		assertEquals(WayDate.MinDate, date1);
		assertFalse(date1.isNull());
		//
		// Maximum date
		//
		date1 = new WayDate(876216);
		assertEquals(WayDate.MaxDate, date1);
		assertFalse(date1.isNull());
		//
		// Null date
		//
		date1 = new WayDate(0);
		assertTrue(date1.isNull());
		//
		// Illegal absolute dates
		//
		try {
			date1 = new WayDate(-1);
		} catch (WayDateException e) {
			System.out.println(e.getMessage());
			assertEquals(WayDateException.ILLEGAL_ABSOLUTE, e.getErrorNumber());
		}
		try {
			date1 = new WayDate(876217);
		} catch (WayDateException e) {
			System.out.println(e.getMessage());
			assertEquals(WayDateException.ILLEGAL_ABSOLUTE, e.getErrorNumber());
		}
		return;
	}

	/**
	 * Test creation of dates a MINYEAR and MAXYEAR
	 */
	@Test
	public void test_can_create_min_and_max_dates() throws Exception {
		// test good years at end of range
		WayDate aDate1 = new WayDate(1, 1, 1601);
		WayDate aDate2 = new WayDate(12, 31, 3999);
		assertTrue("first data not before last date",
				aDate1.compareTo(aDate2) < 0);
		return;
	}

	/**
	 * Test leap years
	 */

	@Test
	public void test_knows_leap_year() throws Exception {
		// test of 1900 as a non-leap year
		assertTrue("1900 is not a leap year", !WayDate.isLeapYear(1900));

		// test of days in year
		assertTrue("Error in days of year 1900",
				WayDate.daysInYear(1900) == 365);

		// test of leap year 2000
		assertTrue("2000 is a leap year", WayDate.isLeapYear(2000));

		// test of number of days in leap year
		assertTrue("Error in days of year 2000",
				WayDate.daysInYear(2000) == 366);

		// test of leap year 1996
		assertTrue("1996 is a leap year", WayDate.isLeapYear(1996));

		// test of non-leap year
		assertTrue("1997 is not a leap year", !WayDate.isLeapYear(1997));

		// test of leap year 1900
		WayDate date = new WayDate(1, 1, 1900);
		assertFalse(date.isLeapYear());
		date = new WayDate(1, 1, 1991);
		assertFalse(date.isLeapYear());
		return;
	}

	/**
	 * Test days of year calculation
	 */
	@Test
	public void test_knows_day_of_year() throws Exception {
		WayDate date1 = WayDate.MinDate;
		WayDate dateMax = WayDate.MaxDate;
		int count = 1;

		while (date1.compareTo(dateMax) != 0) {
			//
			// Test conversion to day of year
			//
			assertTrue("Error in day of year for " + date1,
					count == date1.getDayOfYear());
			//
			// Test conversion from day of year to gregorian date
			//
			WayDate date2 = new WayDate(count, date1.getYear());
			assertEquals(date1, date2);
			//
			// Check static method for day of year
			//
			int month = date1.getMonth();
			int day = date1.getDay();
			int year = date1.getYear();
			assertEquals(count, WayDate.dayOfYear(month, day, year));
			// Update count and date
			//
			if (count == WayDate.daysInYear(date1.getYear()))
				count = 0;
			count++;
			date1 = date1.increment();
		}
		return;
	}

	/**
	 * Test creation of last day in year
	 */
	@Test
	public void test_can_create_last_days_of_year() throws Exception {
		WayDate date2 = new WayDate(365, 1900);
		WayDate date3 = new WayDate(366, 2000);
		assertTrue(date2.compareTo(date3) < 0);
		return;
	}

	/**
	 * Test absolute calculation
	 */
	@Test
	public void test_can_calculate_absolute_date() throws Exception {
		WayDate date1 = new WayDate(1, 1, 1601); // must be this for count = 1
		WayDate maxDate = new WayDate(12, 31, 2001);

		int count = 1;
		while (!date1.equals(maxDate)) {
			assertTrue(WayDate.isAbsoluteDate(count));
			// Case 14 -- test absolute from gregorian
			assertTrue("Error in comparison of absolute date " + date1,
					absoluteFromGregorian(date1) == count);
			date1 = date1.increment();
			count++;
		}

		// test of first absolute date
		WayDate date3 = new WayDate(1, 1, WayDate.MINYEAR);
		date3 = date3.increment();
		date3 = date3.decrement(); // forces conversion to absolute
		assertTrue("Day error converting from absolute" + date3,
				date3.getDay() == 1);
		assertTrue("Month error converting from absolute" + date3,
				date3.getMonth() == 1);
		assertTrue("Year error converting from absolute" + date3,
				date3.getYear() == WayDate.MINYEAR);

		// test of last absolute date
		date1 = new WayDate(12, 31, WayDate.MAXYEAR);
		date1 = date1.decrement(); // forces conversion to absolute
		date1 = date1.increment();
		WayDate date2 = new WayDate(12, 31, WayDate.MAXYEAR);
		assertTrue("Error converting from absolute" + date1,
				date1.equals(date2));
		return;
	}

	/**
	 * Test calculation interface
	 */
	@Test
	public void test_can_add_integer_to_date() throws Exception {
		WayDate maxDate = null;
		WayDate minDate = null;
		minDate = new WayDate(1, 1, 1800);
		maxDate = new WayDate(12, 31, 2100);
		int count;
		WayDate date1;

		try {
			while (minDate.compareTo(maxDate) != 0) {
				count = maxDate.difference(minDate);

				// test of negative add
				date1 = new WayDate(maxDate.getMonth(), maxDate.getDay(),
						maxDate.getYear());
				date1 = date1.add(-count);
				assertTrue("Error adding negative count", minDate.equals(date1));

				// test of positive add
				date1 = new WayDate(minDate.getMonth(), minDate.getDay(),
						minDate.getYear());
				date1 = date1.add(count);
				assertTrue("Error adding positive count", maxDate.equals(date1));
				maxDate = maxDate.decrement();
			}
		} catch (WayDateException e) {
			assertTrue("Unexpected WayDateException: " + e.getMessage(), false);
		}
		return;
	}

	/**
	 * Test day of week
	 */
	@Test
	public void test_knows_day_of_week() throws Exception {
		String[] namesOfDays = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri",
				"Sat" };
		WayDate date1 = new WayDate(1, 1, 1601);
		int day = 1; // 1-Jan-1601 is a monday
		WayDate maxDate = new WayDate(12, 31, 3999);
		int dow;
		while (!date1.equals(maxDate)) {
			dow = date1.getDayOfWeek();
			assertTrue(dow == (day % 7));
			assertEquals(namesOfDays[day % 7], WayDate.dayOfWeekString(day % 7));
			assertEquals(namesOfDays[day % 7], date1.getDayOfWeekString());
			day++;
			date1 = date1.increment();
		}
	}

	/**
	 * Test the generation of a date today.
	 * 
	 * Note: update the date each day of testing.
	 * 
	 */
	@Test
	public void test_knows_current_date() throws Exception {
		WayDate date = WayDate.today();
		assertEquals(date, new WayDate(5, 6, 2012));
		Date javaDate1 = new Date();
		WayDate date1 = WayDate.asWayDate(javaDate1);
		assertEquals(date, date1);
		Date javaDate2 = date1.asDate();
		WayDate date2 = WayDate.asWayDate(javaDate2);
		assertEquals(date1, date2);
		return;
	}

	/**
	 * Test the conversion from WayDate to date
	 */
	@Test
	public void test_can_convert_waydate_to_date() throws Exception {
		WayDate aWayDate = new WayDate(5, 3, 2012);
		Date javaDate1 = aWayDate.asDate();
		Date javaDate2 = new Date(2012 - 1900, 4, 3, 0, 0, 0);
		System.out.println("1 " + javaDate1);
		System.out.println("2 " + javaDate2);
		assertTrue(javaDate1.equals(javaDate2));
		WayDate aWayDate2 = WayDate.asWayDate(javaDate2);
		assertEquals(aWayDate, aWayDate2);
	}

	/**
	 * Test handling null java date
	 */
	@Test
	public void test_can_handle_java_null_date() throws Exception {
		Date nullDate = null;
		WayDate aWayDate = WayDate.asWayDate(nullDate);
		assertTrue(aWayDate.isNull());
	}

	/**
	 * Test the equals function.
	 * 
	 */
	@Test
	public void test_can_determine_if_two_dates_equal() throws Exception {
		WayDate date1 = WayDate.today();
		WayDate date2 = (WayDate) date1.clone();
		assertEquals(date1, date2);
		assertEquals(date1, date1);
		assertTrue("Problem with equivalence of clone", !(date1 == date2));
		date1 = date1.increment();
		assertTrue("Problem with unequal", !date1.equals(date2));
		assertTrue("Problem with null", !date1.equals(null));
		assertTrue("Problem with object", !date1.equals(new Object()));
	}

	/**
	 * Test of compareTo function
	 * 
	 */

	@Test
	public void test_can_compare_two_dates() throws Exception {
		WayDate date1 = new WayDate(3, 3, 2000);
		WayDate date2 = new WayDate(2, 2, 2001);
		WayDate date3 = new WayDate(1, 1, 2003);
		WayDate date4 = new WayDate(3, 3, 2000);

		assertTrue("Compare less than", date1.compareTo(date2) < 0);
		assertTrue("Compare equal", date1.compareTo(date4) == 0);
		assertTrue("Compare greater", date3.compareTo(date2) > 0);
		assertTrue(date1.isBefore(date2));
		assertFalse(date1.isBefore(date1));
		assertTrue(date2.isAfter(date1));
		assertFalse(date2.isAfter(date2));
		assertTrue(date1.isOnOrBefore(date2));
		assertTrue(date1.isOnOrBefore(date1));
		assertFalse(date2.isOnOrBefore(date1));
		assertTrue(date2.isOnOrAfter(date1));
		assertTrue(date2.isOnOrAfter(date2));
		assertFalse(date1.isOnOrAfter(date2));
	}

	/**
	 * Test hashCode function
	 * 
	 */
	@Test
	public void test_knows_hash_code() throws Exception {
		WayDate date1 = new WayDate(3, 3, 2000);
		WayDate date2 = new WayDate(2, 2, 2001);
		WayDate date3 = new WayDate(1, 1, 2003);
		WayDate date4 = new WayDate(3, 3, 2000);

		assertEquals(date1.hashCode(), date4.hashCode());
		assertEquals(date1.hashCode(), date1.hashCode());
		assertTrue("not equals", date1.hashCode() != date2.hashCode());
		assertTrue("second not equals", date4.hashCode() != date3.hashCode());
	}

	/**
	 * Test of exception with illegal date
	 */
	@Test
	public void test_fails_on_invalid_date() {
		try {
			WayDate date = new WayDate(13, 02, 2900);
			fail("did not detect illegal date: " + date);
		} catch (WayDateException e) {
			System.out.println(e.getMessage());
			assertTrue("", true);
		}
		return;
	}

	/**
	 * Test of null date
	 */
	@Test
	public void test_handles_null_date() throws Exception {
		WayDate nullDate1 = WayDate.getNullDate();
		WayDate nullDate2 = WayDate.getNullDate();
		assertTrue(nullDate1.isNull());
		// two null dates are equal
		assertTrue(nullDate1.equals(nullDate2));
		// null date never equals another date
		assertTrue(!nullDate2.equals(WayDate.MinDate));
		// the hash code for a null date is 0
		assertTrue(nullDate1.hashCode() == 0);
		assertEquals("null date", nullDate1.toString());
		//
		// Absolute date constructor
		//
		nullDate1 = new WayDate(0);
		assertTrue(nullDate1.isNull());
		return;
	}

	/**
	 * Test of add
	 */
	@Test
	public void test_handles_add_with_null_date() {
		WayDate nullDate1 = WayDate.getNullDate();
		try {
			WayDate result = nullDate1.add(10);
			fail("adding to a null date should throw an exception");
		} catch (WayDateException e) {
			assertEquals(ERR_MESSAGE.getMessage(), e.getMessage());
		}
	}

	/**
	 * Test of difference
	 */
	@Test
	public void test_handles_difference_with_null_date() {
		WayDate nullDate1 = WayDate.getNullDate();
		try {
			int result = nullDate1.difference(WayDate.MinDate);
			fail("difference involving null date should throw exception");
		} catch (WayDateException e) {
			assertEquals(ERR_MESSAGE.getMessage(), e.getMessage());
		}
		try {
			int result = WayDate.MinDate.difference(nullDate1);
			fail("difference involving null date should throw exception");
		} catch (WayDateException e) {
			assertEquals(ERR_MESSAGE.getMessage(), e.getMessage());
		}
	}

	/**
	 * Test of increment and decrement
	 */
	@Test
	public void test_handles_decrement_increment_with_null_date() {
		WayDate nullDate1 = WayDate.getNullDate();
		try {
			WayDate result = nullDate1.increment();
			fail("increment should throw exception");
		} catch (WayDateException e) {
			assertEquals(ERR_MESSAGE.getMessage(), e.getMessage());
		}
		try {
			WayDate result = nullDate1.decrement();
			fail("decrement should throw exception");
		} catch (WayDateException e) {
			assertEquals(ERR_MESSAGE.getMessage(), e.getMessage());
		}
	}

	/**
	 * Test of accessors
	 */
	@Test
	public void test_handles_accessors_with_null_date() {
		WayDate nullDate1 = WayDate.getNullDate();
		try {
			int result = nullDate1.getDay();
			fail("accessor should throw exception");
		} catch (WayDateException e) {
			assertEquals(ERR_MESSAGE.getMessage(), e.getMessage());
		}
		return;
	}

	/**
	 * Test of isAfter.
	 */
	@Test
	public void test_knows_is_after() throws WayDateException {
		WayDate nullDate1 = WayDate.getNullDate();
		WayDate lesser = new WayDate(1, 1, 1800);
		WayDate greater = new WayDate(1, 1, 1900);
		assertFalse(lesser.equals(greater));
		assertTrue(greater.isAfter(lesser));
		assertFalse(lesser.isAfter(greater));
		assertFalse(nullDate1.isAfter(greater));
		assertFalse(greater.isAfter(nullDate1));
	}

	/**
	 * Test of string conversion
	 */
	@Test
	public void test_can_convert_to_string() throws Exception {
		//
		// Normal date
		//
		WayDate date1 = new WayDate(1, 4, 2020);
		assertEquals("04-Jan-2020", date1.toString());
		assertEquals("2020-01-04", date1.toISODate());
		//
		// Null date
		//
		WayDate nullDate1 = WayDate.getNullDate();
		assertEquals("null date", nullDate1.toString());
		assertEquals("", nullDate1.toISODate());
		return;
	}

	/**
	 * Test of parsing strings
	 */
	@Test
	public void test_can_parse_from_string() throws Exception {
		//
		// Valid dates
		//
		WayDate date1 = WayDate.asWayDate("04-Mar-2012");
		assertEquals(new WayDate(3, 4, 2012), date1);
		//
		// Invalid date
		//
		try {
			date1 = WayDate.asWayDate("31-Dec-1600");
			fail("Should not have converted date");
		} catch (WayDateException e) {
			assertEquals(WayDateException.ILLEGAL_DATE, e.getErrorNumber());
		}
		//
		// Bad characters
		//
		try {
			date1 = WayDate.asWayDate("xx");
			fail("Should not have converted date");
		} catch (WayDateException e) {
			assertEquals(WayDateException.ILLEGAL_FORMAT, e.getErrorNumber());
		}
		//
		// Edge conditions
		//
		date1 = WayDate.asWayDate("1-Dec-1900");
		assertEquals(new WayDate(12, 1, 1900), date1);
		return;
	}

	/**
	 * Test of the ISO conversion
	 */
	@Test
	public void test_can_parse_from_iso_string() throws Exception {
		//
		// Valid dates
		//
		WayDate date1 = WayDate.fromISODate("2012-04-01");
		assertEquals(new WayDate(4, 1, 2012), date1);
		//
		// Null date
		//
		String nullString = null;
		try {
			date1 = WayDate.fromISODate(nullString);
		} catch (WayDateException e) {
			assertEquals(WayDateException.NULL_ARGUMENT, e.getErrorNumber());
		}
	}

	/**
	 * Test of month abbreviations
	 */
	@Test
	public void test_knows_month_abbreviations() throws Exception {
		String[] months = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
				"Aug", "Sep", "Oct", "Nov", "Dec" };
		for (int month = 1; month <= 12; month++) {
			assertEquals(months[month - 1], WayDate.monthString(month));
		}
		return;
	}
}
