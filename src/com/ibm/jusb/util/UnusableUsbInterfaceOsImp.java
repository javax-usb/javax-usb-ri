package com.ibm.jusb.util;

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
import com.ibm.jusb.os.*;

/**
 * Unusable UsbInterfaceOsImp implementation.
 * <p>
 * No methods are usable for this implementation; all throw UsbException.
 * The String contents of the UsbExceptions are settable.
 * @author Dan Streetman
 */
public class UnusableUsbInterfaceOsImp extends AbstractUsbInterfaceOsImp implements UsbInterfaceOsImp
{
	/** Constructor. */
	public UnusableUsbInterfaceOsImp() { }

	/**
	 * Constructor.
	 * @param claimed What this should return from {@link #isClaimed() isClaimed()}.
	 * @param claim The String to use in UsbExceptions thrown in {@link claim() claim()}.
	 */
	public UnusableUsbInterfaceOsImp(boolean claimed, String claim)
	{
		isClaimed = claimed;
		claimString = claim;
	}

	/**
	 * Claim this interface.
	 * @exception UsbException This cannot be claimed.
	 */
	public void claim() throws UsbException { throw new UsbException(getClaimString()); }

	/**
	 * Release this interface.
	 * <p>
	 * This is implemented as a no-op.
	 */
	public void release() { }

	/**
	 * Indicate if this interface is claimed.
	 * <p>
	 * By default this is always true.
	 * @return If this interface is claimed.
	 */
	public boolean isClaimed() { return isClaimed; }

	/**
	 * Get the String to use in UsbExceptions thrown in {@link #claim() claim()}.
	 * @return The String to use in claim() UsbExceptions.
	 */
	public String getClaimString() { return claimString; }

	public boolean isClaimed = true;
	public String claimString = CLAIM_STRING;

	public static final String CLAIM_STRING = "Cannot claim this interface.";

	public static final String HOST_CONTROLLER_CLAIM_STRING = "Cannot claim a host controller interface.";
}
