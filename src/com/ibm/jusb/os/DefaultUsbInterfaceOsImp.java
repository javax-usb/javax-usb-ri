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
 * No methods are usable for this implementation; all throw UsbException.
 * The String contents of the UsbExceptions are settable.
 * <p>
 * The implementation may either override methods or use {@link #DefaultUsbInterfaceOsImp(boolean) this constructor}
 * to change the behavior or {@link #DefaultUsbInterfaceOsImp(String) this constructor} to
 * specify the UsbException String.
 * @author Dan Streetman
 */
public class DefaultUsbInterfaceOsImp implements UsbInterfaceOsImp
{
	/**
	 * Constructor.
	 * <p>
	 * This defaults to not allowing in-Java claiming and using the default {@link #CLAIM_STRING claim string}.
	 */
	public DefaultUsbInterfaceOsImp() { }

	/**
	 * Constructor.
	 * <p>
	 * If allowClaim is true, simple in-Java claiming is performed, and {@link #isClaimed() isClaimed}
	 * indicates the state of the in-Java claim.  If allowClaim is false, this behaves exactly as
	 * the {@link #DefaultUsbInterfaceOsImp() no-parameter constructor}.
	 * @param allowClaim If this should allow claiming or not.
	 */
	public DefaultUsbInterfaceOsImp(boolean allowClaim)
	{
		this.allowClaim = allowClaim;
	}

	/**
	 * Constructor.
	 * <p>
	 * This behaves exactly as the {@link #DefaultUsbInterfaceOsImp() no-parameter constructor}.
	 * The specified String is used in UsbExceptions thrown from the {@link #claim() claim} method.
	 * @param claim The String to use in UsbExceptions thrown in {@link #claim() claim()}.
	 */
	public DefaultUsbInterfaceOsImp(String claim)
	{
		claimString = claim;
	}

	/**
	 * Claim this interface.
	 * <p>
	 * If {@link #allowClaim() allowClaim} is true, this performs in-Java claiming.
	 * If not, this throws an UsbException with the String set to {@link #getClaimString() getClaimString}.
	 * @exception If claiming is not allowed.
	 */
	public void claim() throws UsbException
	{
		if (allowClaim())
			isClaimed = true;
		else
			throw new UsbException(getClaimString());
	}

	/**
	 * Release this interface.
	 * <p>
	 * If {@link #allowClaim() allowClaim} is true, this performs an in-Java release of the claim.
	 * If not, this does nothing.
	 */
	public void release()
	{
		if (allowClaim())
			isClaimed = false;
	}

	/**
	 * Indicate if this interface is claimed.
	 * <p>
	 * If {@link #allowClaim() allowClaim} is true, this returns the state of the in-Java claim.
	 * If not, this returns the claim specified in the {@link #DefaultUsbInterfaceOsImp(boolean) constructor}
	 * or, by default, true.
	 * @return If this interface is claimed.
	 */
	public boolean isClaimed() { return isClaimed; }

	/**
	 * Get the String to use in UsbExceptions thrown in {@link #claim() claim()}.
	 * @return The String to use in claim() UsbExceptions.
	 */
	protected String getClaimString() { return claimString; }

	/**
	 * If this allows claiming.
	 * @return If this allows claiming.
	 */
	protected boolean allowClaim() { return allowClaim; }

	protected boolean allowClaim = false;
	protected boolean isClaimed = true;
	protected String claimString = CLAIM_STRING;

	public static final String CLAIM_STRING = "Cannot claim this interface.";

	public static final String HOST_CONTROLLER_CLAIM_STRING = "Cannot claim a host controller interface.";
}
