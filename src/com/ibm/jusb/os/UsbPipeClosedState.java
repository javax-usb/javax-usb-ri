package com.ibm.jusb.os;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the IBM Public License.
 *
 */

import javax.usb.*;

/**
 * Defines UsbPipe's Closed State
 * @author E. Michael Maximilien
 * @author Dan Streetman
 * @see javax.usb.UsbPipe#syncSubmit
 * @see javax.usb.UsbPipe#asyncSubmit
 * @version 0.0.1 (JDK 1.1.x)
 */
public class UsbPipeClosedState extends UsbPipeActiveState
{
	public UsbPipeClosedState( UsbPipeAbstraction pipe ) 
	{ 
		super( pipe, NAME ); 
		exceptionMessage = USE_MSG;
	}

	//*************************************************************************
	// Public methods

	/** @return if this UsbPipe is open */
	public boolean isOpen() { return false; }

	/** @return if this UsbPipe is open */
	public boolean isClosed() { return true; }

	/** @return if this UsbPipe is busy */
	public boolean isBusy() { return false; }

	/** @return if this UsbPipe is busy */
	public boolean isIdle() { return false; }

	/** @return if this UsbPipe is in an error state */
	public boolean isInError() { return false; }

	public void open() throws UsbException
	{
		getUsbPipeAbstraction().getUsbPipeImp().open();
		getUsbPipeAbstraction().transition( UsbPipeIdleState.NAME );
	}

	//*************************************************************************
	// Class constants

	public static final String NAME = "Closed State";

	public static final String USE_MSG = "Cannot use UsbPipe while closed";

}
