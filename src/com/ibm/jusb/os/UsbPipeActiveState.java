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

import com.ibm.jusb.util.*;

/**
 * Defines active UsbPipeState.
 * @author E. Michael Maximilien
 * @author Dan Streetman
 * @version 0.0.1 (JDK 1.1.x)
 */
public abstract class UsbPipeActiveState extends UsbPipeState
{
	/**
	 * @param pipe the UsbPipeAbstraction associated with this UsbPipeState
	 * @param name the String name
	 */
	public UsbPipeActiveState( UsbPipeAbstraction pipe, String name ) { super(pipe,name); }

	//*************************************************************************
	// Public methods

	/** @return the error code indicating the cause of the current error state */
	public int getErrorCode() { return 0; }

	/** @return if this UsbPipe is active */
	public boolean isActive() { return true; }

}
