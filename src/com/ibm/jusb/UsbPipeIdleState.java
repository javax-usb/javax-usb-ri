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
public class UsbPipeIdleState extends UsbPipeOpenState
{
	public UsbPipeIdleState( UsbPipeAbstraction pipe ) { super( pipe, NAME ); }

	//*************************************************************************
	// Public methods

	/** @return if this UsbPipe is busy */
	public boolean isBusy() { return false; }

	/** @return if this UsbPipe is busy */
	public boolean isIdle() { return true; }

	/** @return if this UsbPipe is in an error state */
	public boolean isInError() { return false; }

	public void close() throws UsbException 
	{ 
		getUsbPipeAbstraction().getUsbPipeImp().close();
		getUsbPipeAbstraction().transition( UsbPipeClosedState.NAME );
	}

	//*************************************************************************
	// Class Constants

	public static final String NAME = "Idle State";
}
