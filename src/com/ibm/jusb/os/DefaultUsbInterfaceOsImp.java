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

import com.ibm.jusb.*;

/**
 * Default UsbInterfaceOsImp implementation.
 * <p>
 * This provides a simple in-Java claiming implementation.
 * @author Dan Streetman
 */
public class DefaultUsbInterfaceOsImp implements UsbInterfaceOsImp
{
	/**
	 * Constructor.
	 */
	public DefaultUsbInterfaceOsImp() { }

	/**
	 * Claim this interface.
	 * <p>
	 * This performs a simple in-Java claim.
	 */
	public void claim() throws UsbException
	{
		isClaimed = true;
	}

	/**
	 * Claim this interface using a UsbInterfacePolicy.
	 * <p>
	 * @param policy The this class ignores the policy.
	 */
	public void claim(UsbInterfacePolicy policy) throws UsbException { claim(); }

	/**
	 * Release this interface.
	 * <p>
	 * This performs a simple in-Java release.
	 */
	public void release() throws UsbException
	{
		isClaimed = false;
	}

	/**
	 * Indicate if this interface is claimed.
	 * <p>
	 * This returns the state of the simple in-Java claim.
	 * @return If this interface is claimed.
	 */
	public boolean isClaimed() { return isClaimed; }

	protected boolean isClaimed = false;
}
