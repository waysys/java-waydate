//------------------------------------------------------------------------------
//      Compilation Unit Header
//------------------------------------------------------------------------------
//
//  Copyright (c) 2008, 2011 Waysys, Inc. All Rights Reserved.
//
//  Waysys MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
//  THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
//  TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
//  PARTICULAR PURPOSE, OR NON-INFRINGEMENT. Waysys SHALL NOT BE LIABLE FOR
//  ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
//  DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
//
//  For further information, contact Waysys LLC at waysys@waysysweb.com
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
//  Shaffer   01-May-2012   Changed substring function
//
//------------------------------------------------------------------------------
//      Package Declaration
//------------------------------------------------------------------------------

package com.waysysweb.util;

//------------------------------------------------------------------------------
//      Import Declarations
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//      Public Class Declaration
//------------------------------------------------------------------------------

/**
 * Utility to do certain string manipulations.
 * 
 * @author William A. Shaffer
 * @version 4.00 01-May-2012
 */
public class WayString  {
	/**
	 * The string being manipulated
	 */
	private String aString;

	/**
	 * Create an instance of this class
	 * 
	 */
	public WayString() {
		aString = "";
	}

	/**
	 * Create an instance of this class with a value.
	 * 
	 * @param value
	 *            the value the string should take on
	 */
	public WayString(String value) {
		aString = value;
	}

	/**
	 * Set the string value of the instance.
	 * 
	 * @param value
	 *            the value the instance should take on
	 */
	public void setString(String value) {
		aString = value;
	}

	/**
	 * Substitute the argument
	 * 
	 * @param sym
	 *            the character showing where the argument should be inserted
	 * @param arg
	 *            the argument to be substituted in the string
	 * @return the value of the string with the argument substituted
	 */
	public String substitute(char sym, String arg) {
		String message;
		int index = aString.indexOf(sym);
		if (index < 0) {
			message = aString;
		} else {
			message = aString.substring(0, index) + arg
					+ aString.substring(index + 1, aString.length());
		}
		return message;
	}
}