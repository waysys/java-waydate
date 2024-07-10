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
//  Shaffer   02-May-1997   File create
//  Shaffer   17-Jun-2000   Package names updated and adapted to JBuilder
//  Shaffer   12-Nov-2006   Added message for illegal day of week
//  Shaffer   07-Jan-2007   Added constructor to accept integer as second argument 
//  Shaffer   09-Jan-2007   Added error message for minimum holiday year
//  Shaffer   01-May-2012   Added error message for bad date
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
 * Represent information about an illegal date.
 * 
 * @author William A. Shaffer
 * @version 4.00 01-May-2012
 */

public class WayDateException extends WayException {
	/**
	 * The serial version unique identifier for this class
	 */
	private static final long serialVersionUID = 9704L;

	/** Minimum error value */
	public final static int MIN_ERROR = Integer.MIN_VALUE;

	/** Base error number */
	public final static int BASE_ERROR = 100;

	/** Illegal month */
	public final static int ILLEGAL_MONTH = 101;

	/** Illegal day */
	public final static int ILLEGAL_DAY = 102;

	/** Illegal year */
	public final static int ILLEGAL_YEAR = 103;

	/** Illegal day of year */
	public final static int ILLEGAL_DAY_YEAR = 104;

	/** Illegal absolute date */
	public final static int ILLEGAL_ABSOLUTE = 105;

	/** Illegal holiday year */
	public final static int ILLEGAL_HOLIDAY_YEAR = 106;

	/** Illegal day of week */
	public final static int ILLEGAL_DAY_OF_WEEK = 107;

	/** Illegal date */
	public final static int ILLEGAL_DATE = 108;

	/** Null argument */
	public final static int NULL_ARGUMENT = 109;
	
	/** Cannot increment maximum date */
	public final static int MAX_DATE = 110;
	
	/** Cannot decrement minimum date */
	public final static int MIN_DATE = 111;
	
	/** Cannot compute with null date */
	public final static int NULL_DATE = 112;
	
	/** Invalid date parsing pattern */
	public final static int PATTERN_ERROR = 113;
	
	/** Invalid date format */
	public final static int ILLEGAL_FORMAT = 114;

	/** Maximum error number */
	public final static int MAX_ERROR = Integer.MAX_VALUE;

	/** array of error numbers */
	private final static int errNum[] = { MIN_ERROR, BASE_ERROR, ILLEGAL_MONTH,
			ILLEGAL_DAY, ILLEGAL_YEAR, ILLEGAL_DAY_YEAR, ILLEGAL_ABSOLUTE,
			ILLEGAL_HOLIDAY_YEAR, ILLEGAL_DAY_OF_WEEK, ILLEGAL_DATE,
			NULL_ARGUMENT, MAX_DATE, MIN_DATE, NULL_DATE, PATTERN_ERROR, 
			ILLEGAL_FORMAT, MAX_ERROR };

	private final static String errMessage[] = {
			/* MIN_VALUE */"",
			/* 100 */"",
			/* 101 */"Illegal month: %. Must be between 1 and 12.",
			/* 102 */"Illegal day: %. Must be between 1 and the last day of the month.",
			/* 103 */"Illegal year: %. Must be between 1601 and 3999.",
			/* 104 */"Illegal day of year: %.",
			/* 105 */"Illegal absolute date: %.",
			/* 106 */"Illegal year for holidays: %. Must be between 1900 and 3999.",
			/* 107 */"Illegal day of week: %. Must be between 0 and 6.",
			/* 108 */"Illegal date: %",
			/* 109 */"Argument must not be null: ",
			/* 110 */"Cannot increment maximum date.",
			/* 111 */"Cannot decrement minimum date.",
			/* 112 */"Cannot compute with null date.",
			/* 113 */"Invalid date parsing pattern: ",
			/* 114 */"Invalid date format: ",
			/* MAX_VALUE */"" };

	/**
	 * Create a WayDateException
	 * 
	 * @param errorNum
	 *            the number identifying the error.
	 * @param arg
	 *            an argument inserted into the error message.
	 */
	public WayDateException(int errorNum, String arg) {
		super(errorNum, arg);
		return;
	}

	/**
	 * Create a WayDateException
	 * 
	 * @param errorNum
	 *            the number identifying the error.
	 * @param arg
	 *            an integer argument inserted into the error message
	 */
	public WayDateException(int errorNum, int arg) {
		super(errorNum, arg);
		return;
	}

	/**
	 * Display the error message associated with this exception. To be
	 * implemented in subclasses.
	 * 
	 * @return the error message associated with this exception
	 */
	@Override
	public String getMessage() {
		assert errNum.length == errMessage.length : 
			"error number array different size from error message array";
		String message = buildMessage(errNum, errMessage);
		return message;
	}
}