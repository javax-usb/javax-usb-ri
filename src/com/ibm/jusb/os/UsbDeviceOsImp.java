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
 * Interface for Platform-specific implementation of UsbDevice.
 * <p>
 * All methods are synchronized in the Platform-Independent layer; the
 * implementation does not need to make them Thread-safe.
 * @author Dan Streetman
 */
public interface UsbDeviceOsImp
{
	/**
	 * Synchronously submit a RequestImp.
	 * <p>
	 * If a UsbException is thrown, the implementation should first
	 * {@link com.ibm.jusb.RequestImp#setUsbException(UsbException) set} the
	 * {@link com.ibm.jusb.RequestImp#getUsbException() UsbException}.
	 * If the operation completes successfully, the
	 * {@link com.ibm.jusb.RequestImp#getDataLength() data length} must be
	 * {@link com.ibm.jusb.RequestImp#setDataLength(int) set}.
	 * The implementation should throw the UsbException if one occurs.
	 * In either case no more action is required, the UsbDeviceImp will handle
	 * all other operations.
	 * @param requestImp The RequestImp.
	 * @return The number of bytes transferred.
	 * @throws UsbException If the submission is unsuccessful.
	 */
	public void syncSubmit(RequestImp requestImp) throws UsbException;

	/**
	 * Synchronously submit a List of RequestImps.
	 * <p>
	 * The List is guaranteed to contains all RequestImps.  Every RequestImp
	 * should be handled as if passed via
	 * {@link #syncSubmit(RequestImp) single RequestImp syncSubmit},
	 * with the only difference being an error for one of the RequestImps does
	 * not require the implementation to throw a UsbException for the entire List.
	 * The implementation may, at its option, simply set the RequestImp's UsbException
	 * and continue processing RequestImps.  If a UsbException is thrown, all
	 * RequestImps in the List must have either their data length or UsbException set,
	 * depending on whether that specific RequestImp was successful or not.  RequestImps
	 * not yet processed must have their UsbException set.  No action (besides setting
	 * each RequestImp's data length or UsbException) is required, regardless of
	 * whether a UsbException is thrown or not.
	 * @param list The List.
	 * @throws UsbException If the one (or more) submissions are unsuccessful (optional).
	 */
	public void syncSubmit(List list) throws UsbException;

	/**
	 * Asynchronously submit a RequestImp.
	 * <p>
	 * This should return as soon as possible.  If a UsbException is
	 * thrown, no other action is required.  Otherwise, the RequestImp is
	 * accepted for processing.  The RequestImp should then be handled the same as
	 * {@link #syncSubmit(RequestImp) single RequestImp syncSubmit}, with the only
	 * difference is no UsbException is thrown (if unsuccessful) but it should be set
	 * on the RequestImp.  In either a successful or unsuccessful case the RequestImp
	 * must be passed to the UsbDeviceImp's
	 * {@link com.ibm.jusb.UsbDeviceImp#requestImpCompleted(RequestImp) requestImpCompleted}.
	 * No other action is required.
	 * @param requestImp The RequestImp.
	 * @throws UsbException If the submission is unsuccessful.
	 */
	public void asyncSubmit(RequestImp requestImp) throws UsbException;
}
