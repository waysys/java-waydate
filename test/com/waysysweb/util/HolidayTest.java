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
//  Shaffer   02-May-2012   File create
//
//------------------------------------------------------------------------------
//      Package Declaration
//------------------------------------------------------------------------------

package com.waysysweb.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import junit.framework.JUnit4TestAdapter;

import org.easyspec.Context;
import org.easyspec.EasySpec;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

//------------------------------------------------------------------------------
//Public Class Declaration
//------------------------------------------------------------------------------

/**
 * This is the test harness for testing Holiday. It uses the JUnit test
 * framework developed by Erich Gamma and Kent Beck.
 * 
 * @author William A. Shaffer
 * @version 4.00
 */
@EasySpec(interest = "Holiday")
public class HolidayTest {

	// -------------------------------------------------------------------------
	// Support Functions
	// -------------------------------------------------------------------------

	/**
	 * Return an instance of junit.framework.Test
	 * 
	 * @return a junit test adapter
	 */
	public static junit.framework.Test suite() {
		junit.framework.Test suite = new JUnit4TestAdapter(DateTest.class);
		return suite;
	}

	/**
	 * Perform initial setup
	 * 
	 */
	@Context("all holidays")
	@Before
	public void setUp() {
		return;
	}

	/**
	 * Perform final teardown
	 * 
	 */
	@After
	public void tearDown() {
		return;
	}

	// -------------------------------------------------------------------------
	// Tests
	// -------------------------------------------------------------------------

	@Test
	public void testGetNewYearsDay() throws WayDateException {
		WayDate newYears1 = Holiday.getNewYearsDay(2006);
		WayDate newYears2 = new WayDate(1, 1, 2006);
		assertTrue("new years", newYears1.equals(newYears2));
	}

	@Test
	public void testGetMartinLutherKingsBirthday() throws WayDateException {
		WayDate kingDay1 = Holiday.getMartinLutherKingsBirthday(2006);
		WayDate kingDay2 = new WayDate(1, 16, 2006);
		assertTrue("Martin Luther King", kingDay1.equals(kingDay2));
	}

	@Test
	public void testGetWashingtonsBirthday() throws WayDateException {
		WayDate washington1 = Holiday.getWashingtonsBirthday(2006);
		WayDate washington2 = new WayDate(2, 20, 2006);
		assertTrue("Washington's Birthday", washington1.equals(washington2));
	}

	@Test
	public void testGetMemorialDay() throws WayDateException {
		WayDate memorial1 = Holiday.getMemorialDay(2006);
		WayDate memorial2 = new WayDate(5, 29, 2006);
		assertTrue("Memorial Day", memorial1.equals(memorial2));
	}

	@Test
	public void testGetIndependenceDay() throws WayDateException {
		WayDate independence1 = Holiday.getIndependenceDay(2006);
		WayDate independence2 = new WayDate(7, 4, 2006);
		assertTrue("Independence Day", independence1.equals(independence2));
	}

	@Test
	public void testGetLaborDay() throws WayDateException {
		WayDate labor1 = Holiday.getLaborDay(2006);
		WayDate labor2 = new WayDate(9, 4, 2006);
		assertTrue("Labor Day", labor1.equals(labor2));
	}

	@Test
	public void testGetColumbusDay() throws WayDateException {
		WayDate columbus1 = Holiday.getColumbusDay(2006);
		WayDate columbus2 = new WayDate(10, 9, 2006);
		assertTrue("Columbus Day", columbus1.equals(columbus2));
	}

	@Test
	public void testGetVeteransDay() throws WayDateException {
		WayDate veterans1 = Holiday.getVeteransDay(2006);
		WayDate veterans2 = new WayDate(11, 11, 2006);
		assertTrue("Veterans Day", veterans1.equals(veterans2));
	}

	@Test
	public void testGetThanksgiving() throws WayDateException {
		WayDate thanks1 = Holiday.getThanksgiving(2006);
		WayDate thanks2 = new WayDate(11, 23, 2006);
		assertTrue("Thanksgiving", thanks1.equals(thanks2));
	}

	@Test
	public void testGetChristmas() throws WayDateException {
		WayDate chris1 = Holiday.getChristmas(2006);
		WayDate chris2 = new WayDate(12, 25, 2006);
		assertTrue("Christmas", chris1.equals(chris2));
	}

	@Test
	public void testGetEaster() throws WayDateException {
		WayDate easter1 = Holiday.getEaster(2006);
		WayDate easter2 = new WayDate(4, 16, 2006);
		assertTrue("Easter 2006", easter1.equals(easter2));
		easter1 = Holiday.getEaster(2008);
		easter2 = new WayDate(3, 23, 2008);
		assertTrue("Easter 2008", easter1.equals(easter2));
		easter1 = Holiday.getEaster(2011);
		easter2 = new WayDate(4, 24, 2011);
		assertTrue("Easter 2011", easter1.equals(easter2));
	}

	@Test
	public void testGetObservedHoliday() throws WayDateException {
		WayDate veterans1 = Holiday.getVeteransDay(2006);
		WayDate observed1 = Holiday.getObservedHoliday(veterans1);
		WayDate observed2 = new WayDate(11, 10, 2006);
		assertTrue("Veterans Day", observed1.equals(observed2));
		WayDate july4 = Holiday.getIndependenceDay(2010);
		observed2 = Holiday.getObservedHoliday(july4);
		assertEquals(WayDate.MONDAY, observed2.getDayOfWeek());
	}

}
