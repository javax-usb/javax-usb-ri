package com.ibm.jusb.os;

/*
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import com.ibm.jusb.*;

/**
 * Interface for os implementation of UsbDevice.
 * @author Dan Streetman
 */
public interface UsbDeviceOsImp
{
	/**
	 * Synchronously submit a RequestImp.
	 * @param requestImp The RequestImp.
	 * @return The number of bytes transferred.
	 * @throws UsbException If the submission is unsuccessful.
	 */
	public void syncSubmit(RequestImp requestImp) throws UsbException;

	/**
	 * Synchronously submit a List of RequestImps.
	 * @param list The List.
	 * @throws UsbException If the submission is unsuccessful.
	 */
	public void syncSubmit(List list) throws UsbException;

	/**
	 * Asynchronously submit a RequestImp.
	 * @param requestImp The RequestImp.
	 * @throws UsbException If the submission is unsuccessful.
	 */
	public void asyncSubmit(RequestImp requestImp) throws UsbException;
}
