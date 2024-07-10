//------------------------------------------------------------------------------
//      Compilation Unit Header
//------------------------------------------------------------------------------
//
//  Copyright (c) 2006, 2012 Waysys, LLC. All Rights Reserved.
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
//  Shaffer   09-May-1997   File create
//  Shaffer   17-Jun-2000   Package name updated and adapted to JBuilder
//  Shaffer   17-Jun-2000   Modified to add displays
//  Shaffer   12-Nov-2006   Modified to make compatible with PalmDateBench
//  Shaffer   02-Jul-2011   Added static numeric identifier
//  Shaffer   04-May-2012   Added getter for error number
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
 * Represent information about an Exception.
 * 
 * @author William A. Shaffer
 * @version 4.00 02-Jul-2011
 */
public abstract class WayException extends Exception {
	
	//--------------------------------------------------------------------------
	//  Fields
	//--------------------------------------------------------------------------
	
	protected int err = 0;
	protected String argument = "";

	/**
	 * The serial version unique identifier for this class
	 */
	private static final long serialVersionUID = 9800L;

	//--------------------------------------------------------------------------
	//  Constructors
	//--------------------------------------------------------------------------	
	
	/**
	 * Create a WayException
	 * 
	 * @param errorNum
	 *            the number identifying the error.
	 * @param arg
	 *            an argument inserted into the error message.
	 */
	public WayException(int errorNum, String arg) {
		super();
		err = errorNum;
		argument = arg;
		return;
	}

	/**
	 * Create a WayException
	 * 
	 * @param errorNum
	 *            the number identifying the error.
	 * @param arg
	 *            an integer argument inserted into the error message
	 */
	public WayException(int errorNum, int arg) {
		super();
		err = errorNum;
		argument = String.valueOf(arg);
		return;
	}

	//--------------------------------------------------------------------------
	//  Public Methods
	//--------------------------------------------------------------------------		
	
	/**
	 * Display the error message associated with this exception. To be
	 * implemented in subclasses.
	 * 
	 */
	abstract public String getMessage();

	/**
	 * Return the error number
	 * 
	 * @return the error number of this error
	 */
	public int getErrorNumber() {
		return err;
	}
	
	//--------------------------------------------------------------------------
	//  Internal Methods
	//--------------------------------------------------------------------------		
	
	/**
	 * Build and display an error message.
	 * 
	 * <P>
	 * Example:
	 * </P>
	 * 
	 * <PRE>
	 * display(errNum, errMessage);
	 * </PRE>
	 * 
	 * @param errNum
	 *            an array of integers with error numbers
	 * @param errMessage
	 *            an array of error messages
	 * @return a string with the error message
	 */

	protected String buildMessage(int[] errNum, String[] errMessage) {
		String message = findError(errNum, errMessage, err, argument);
		return message;
	}

	/**
	 * Find an error message, given an error number, and substitute the argument
	 * into the string.
	 * 
	 * <P>
	 * Example:
	 * </P>
	 * 
	 * <PRE>
	 * message = findError(errorNum, arg);
	 * </PRE>
	 * 
	 * @return the desired error message
	 * @param errNum
	 *            an array of error numbers
	 * @param errMessage
	 *            an array of error messages
	 * @param errorNum
	 *            the error number to be looked up
	 * @param arg
	 *            argument to be substituted into error message
	 */
	private static String findError(int[] errNum, String[] errMessage,
			int errorNum, String arg) {
		int i;
		int j;
		int e;
		String message;

		// precondition: MIN_VALUE <= errorNum <= MAX_VALUE

		i = 0;
		j = errNum.length - 1;
		e = (i + j) / 2;

		// Loop invariant:
		// 0 <= i, j < errNum.length
		// and
		// errNum[i] <= errorNum <= errNum[j]
		// Termination Function: j - i
		//

		while ((i + 1) < j) {
			if (errNum[e] < errorNum)
				i = e;
			else if (errNum[e] > errorNum)
				j = e;
			else
				break;
			e = (i + j) / 2;
		}

		// Proof:
		//
		// (1) Invariant holds at beginning of first iteration because of
		// initialization and precondition.
		// (2) Invariant holds at end of each iteration because:
		// 1 <= e < errNum.length
		// errNum[i] <= errorNum <= errNum[j] because of if statement
		// (3) t function always decreases since, if j > i
		// if j = e, then
		// j' - i' = (i + j)/2 - i
		// i/2 + j/2 - i = j/2 - i/2 = (j - i)/2 < j - i
		//
		// similarly if i = e, then
		// j' - i' = j - (j + i)/2 = j/2 - i/2 < j - i

		if (errNum[e] == errorNum) {
			WayString str = new WayString(errMessage[e]);
			message = str.substitute('%', arg);
		} else
			message = "Error number not found: " + errorNum;
		return message;
	}
}