package com.ibm.jusb;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import javax.usb.*;

/**
 * Defines States for UsbPipeAbstraction
 * @author E. Michael Maximilien
 * @author Dan Streetman
 * @version 0.0.1 (JDK 1.1.x)
 */
public class UsbPipeErrorState extends UsbPipeActiveState
{
	public UsbPipeErrorState( UsbPipeAbstraction pipe )
	{
		super( pipe, NAME );
		exceptionMessage = USE_MSG;
	}

	//*************************************************************************
	// Public methods

	/**
	 * Note that while this state is technically 'open' it does not extend UsbPipeOpenState.
	 * @return if this UsbPipe is open
	 */
	public boolean isOpen() { return true; }

	/** @return if this UsbPipe is open */
	public boolean isClosed() { return false; }

	/** @return if this UsbPipe is busy */
	public boolean isBusy() { return false; }

	/** @return if this UsbPipe is busy */
	public boolean isIdle() { return false; }

	/** @return if this UsbPipe is in an error state */
	public boolean isInError() { return true; }

	/** @return the error code indicating the cause of the current error state */
	public int getErrorCode() { return errorCode; }

	/** @param err the error code */
	public void setErrorCode( int err ) { errorCode = err; }

	/** Close the pipe */
	public void close() throws UsbException 
	{ throw new UsbException( CLOSE_MSG ); }

	//*************************************************************************
	// Instance variables

	private int errorCode = -1;

	//*************************************************************************
	// Class constants

	public static final String NAME = "Error State";

	public static final String CLOSE_MSG = "Cannot close UsbPipe while in error";

	public static final String USE_MSG = "Cannot use UsbPipe while in error";

}
