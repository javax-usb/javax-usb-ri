package com.ibm.jusb.os;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the IBM Public License.
 *
 */

import javax.usb.UsbException;

/**
 * Interface for platform-specific interface implementations.
 * @author Dan Streetman
 * @version 0.0.1 (JDK 1.1.x)
 */
public interface UsbInterfaceImp
{
	/**
	 * Claim this interface.
	 * @exception UsbException if the interface could not be claimed.
	 */
	public void claim() throws UsbException;

	/**
	 * Release this interface.
	 * @exception UsbException if the interface could not be released.
	 */
	public void release() throws UsbException;

	/**
	 * @return if this interface is claimed (in Java).
	 */
	public boolean isClaimed();

}
