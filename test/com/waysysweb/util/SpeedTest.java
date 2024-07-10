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
//  Shaffer   07-May-2000   File create
//
//------------------------------------------------------------------------------
//      Package Declaration
//------------------------------------------------------------------------------

package com.waysysweb.util;

import static org.junit.Assert.assertEquals;
import junit.framework.JUnit4TestAdapter;

import org.junit.Test;

//------------------------------------------------------------------------------
//Public Class Declaration
//------------------------------------------------------------------------------

/**
 * This is a test of the speed of two implementations of an algorithm for
 * computing the year of an absolute date.
 * 
 * @author William A. Shaffer
 * @version 4.00
 */
public class SpeedTest {

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	static final int ITER = 100;

	// -------------------------------------------------------------------------
	// Constructor
	// -------------------------------------------------------------------------

	// -------------------------------------------------------------------------
	// Algorithms
	// -------------------------------------------------------------------------

	/**
	 * Return an instance of junit.framework.Test
	 * 
	 * @return a junit test adapter
	 */
	public static junit.framework.Test suite() {
		junit.framework.Test suite = new JUnit4TestAdapter(SpeedTest.class);
		return suite;
	}

	public static int yearFromAbsolute1(int abDate) {
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

	public static int yearFromAbsolute2(int abDate) throws Exception {
		int year = 1601;
		int numCenturies = 0;
		int daysInYear = WayDate.daysInYear(year);
		while (abDate > daysInYear) {

			if (abDate > 146097) {
				year += 400;
				abDate -= 146097;
			} else if (abDate > 36524) {
				numCenturies++;
				year += 100;
				abDate -= 36524;
			} else if (abDate > 1461) {
				year += 4;
				abDate -= 1461;
			} else if (abDate > daysInYear) {
				year += 1;
				abDate -= daysInYear;
			}
			daysInYear = WayDate.daysInYear(year);
		}
		if (numCenturies == 4)
			year--;
		return year;
	}

	// -------------------------------------------------------------------------
	// Tests
	// -------------------------------------------------------------------------

	/**
	 * Comparison of the two algorithms
	 */
	@Test
	public void test_compare_algorithms() throws Exception {
		int year1;
		int year2;
		for (int abDate = 1; abDate <= 876216; abDate++) {
			year1 = yearFromAbsolute1(abDate);
			year2 = yearFromAbsolute2(abDate);
			assertEquals(year1, year2);
		}
	}

	/**
	 * Calculate the time to compute year from absolute
	 */
	@Test
	public void test_speed_of_algorithm1() {
		int year1;
		long begin = System.currentTimeMillis();
		for (int count = 1; count < ITER; count++) {
			for (int abDate = 1; abDate <= 876216; abDate++) {
				year1 = yearFromAbsolute1(abDate);
			}
		}
		long duration = System.currentTimeMillis() - begin;
		System.out.println("Algorithm 1 duration: " + duration);
	}

	/**
	 * Calculate the time to compute year from absolute
	 */

	public void test_speed_of_algorithm2() throws Exception {
		int year1;
		long begin = System.currentTimeMillis();
		for (int count = 1; count < ITER; count++) {
			for (int abDate = 1; abDate <= 876216; abDate++) {
				year1 = yearFromAbsolute2(abDate);
			}
		}
		long duration = System.currentTimeMillis() - begin;
		System.out.println("Algorithm 2 duration: " + duration);
	}

	/**
	 * Calculate the time to compute
	 */
	@Test
	public void test_speed_of_day_of_year_to_date() throws Exception {
		WayDate date;
		long begin = System.currentTimeMillis();
		for (int count = 1; count < ITER * 1000; count++) {
			for (int dayOfYear = 1; dayOfYear <= 365; dayOfYear++) {
				date = new WayDate(dayOfYear, 2011);
			}
		}
		long duration = System.currentTimeMillis() - begin;
		System.out.println("Day of year to display date duration: " + duration);
		return;
	}
}
