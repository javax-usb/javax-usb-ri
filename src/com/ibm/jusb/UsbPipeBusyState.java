package com.ibm.jusb.os;

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
public class UsbPipeBusyState extends UsbPipeOpenState
{
	public UsbPipeBusyState( UsbPipeAbstraction pipe ) { super( pipe, NAME ); }

	//*************************************************************************
	// Public methods

	/** @return if this UsbPipe is busy */
	public boolean isBusy() { return true; }

	/** @return if this UsbPipe is busy */
	public boolean isIdle() { return false; }

	/** @return if this UsbPipe is in an error state */
	public boolean isInError() { return false; }

	public void close() throws UsbException 
	{ throw new UsbException( CLOSE_MSG ); }

	/**
	 * Stop all submissions in progress
	 */
	public void abortAllSubmissions()
	{
		getUsbPipeAbstraction().getUsbPipeImp().abortAllSubmissions();
	}

	//*************************************************************************
	// Class costants

	public static final String NAME = "Busy State";

	public static final String CLOSE_MSG = "Cannot close UsbPipe while submissions are in progress";
}
