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
 * is not required to extend this abstract class.
 * @author Dan Streetman
 */
public abstract class AbstractUsbDeviceOsImp implements UsbDeviceOsImp
{
	/**
	 * Synchronously submit a UsbIrpImp.ControlUsbIrpImp.
	 * <p>
	 * This method is implemented using {@link #asyncSubmit(UsbIrpImp.ControlUsbIrpImp) asyncSubmit(UsbIrpImp.ControlUsbIrpImp)}.
	 * @param controlUsbIrpImp The UsbIrpImp.ControlUsbIrpImp.
	 * @throws UsbException If the submission is unsuccessful.
	 */
	public void syncSubmit(UsbIrpImp.ControlUsbIrpImp controlUsbIrpImp) throws UsbException
	{
		asyncSubmit(controlUsbIrpImp);

		controlUsbIrpImp.waitUntilComplete();

		if (controlUsbIrpImp.isUsbException())
			throw controlUsbIrpImp.getUsbException();
	}

	/**
	 * Synchronously submit a List of UsbIrpImp.ControlUsbIrpImps.
	 * <p>
	 * This method is implemented using {@link #syncSubmit(UsbIrpImp.ControlUsbIrpImp) syncSubmit(UsbIrpImp.ControlUsbIrpImp)}.
	 * This implementation does not throw UsbException; errors are set on a per-UsbIrpImp.ControlUsbIrpImp basis
	 * but overall execution continues.  Persistent errors will cause all remaining UsbIrpImp.ControlUsbIrpImps to
	 * fail and have their UsbException set, but no UsbException will be thrown.
	 * @param list The List.
	 */
	public void syncSubmit(List list) throws UsbException
	{
		for (int i=0; i<list.size(); i++) {
			try { syncSubmit((UsbIrpImp.ControlUsbIrpImp)list.get(i)); }
			catch ( UsbException uE ) { /* continue processing list */ }
		}
	}

	/**
	 * Asynchronously submit a UsbIrpImp.ControlUsbIrpImp.
	 * <p>
	 * The OS-implementation must implement this method.
	 * @param controlUsbIrpImp The UsbIrpImp.ControlUsbIrpImp.
	 * @throws UsbException If the submission is unsuccessful.
	 */
	public abstract void asyncSubmit(UsbIrpImp.ControlUsbIrpImp controlUsbIrpImp) throws UsbException;
}
