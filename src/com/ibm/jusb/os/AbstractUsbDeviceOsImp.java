package com.ibm.jusb.os;

/*
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import java.util.*;

import javax.usb.*;

import com.ibm.jusb.*;

/**
 * Abstract implementation for UsbDeviceOsImp.
 * <p>
 * This is an optional abstract class that handles all optional methods.  Those
 * methods may be overridden by the implementation if desired.  The implementation
 * does not have to extend this abstract class.
 * @author Dan Streetman
 */
public abstract class AbstractUsbDeviceOsImp implements UsbDeviceOsImp
{
	/**
	 * Synchronously submit a RequestImp.
	 * <p>
	 * This method is implemented using {@link #asyncSubmit(RequestImp) asyncSubmit(RequestImp)}.
	 * @param requestImp The RequestImp.
	 * @return The number of bytes transferred.
	 * @throws UsbException If the submission is unsuccessful.
	 */
	public void syncSubmit(RequestImp requestImp) throws UsbException
	{
//FIXME - implement
	}

	/**
	 * Synchronously submit a List of RequestImps.
	 * <p>
	 * This method is implemented using {@link #asyncSubmit(RequestImp) asyncSubmit(RequestImp)}.
	 * @param list The List.
	 * @throws UsbException If the one (or more) submissions are unsuccessful (optional).
	 */
	public void syncSubmit(List list) throws UsbException
	{
//FIXME - implement
	}

	/**
	 * Asynchronously submit a RequestImp.
	 * <p>
	 * The OS-implementation must implement this method.
	 * @param requestImp The RequestImp.
	 * @throws UsbException If the submission is unsuccessful.
	 */
	public abstract void asyncSubmit(RequestImp requestImp) throws UsbException;
}
