package com.ibm.jusb.os;

/**
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
 * Abstract implementation for ControlUsbPipeOsImp.
 * <p>
 * This is identical to AbstractUsbPipeOsImp except all the methods require
 * ControlUsbIrpImps, not UsbIrpImps.
 * @author Dan Streetman
 */
public abstract class AbstractControlUsbPipeOsImp extends AbstractUsbPipeOsImp implements ControlUsbPipeOsImp
{
	/**
	 * Synchronously submits this ControlUsbIrpImp to the platform implementation.
	 * <p>
	 * This uses {@link #asyncSubmit(ControlUsbIrpImp) asyncSubmit(ControlUsbIrpImp)}.
	 * @param irp the ControlUsbIrpImp to use for this submission.
	 * @exception UsbException If the data transfer was unsuccessful.
	 */
	public void syncSubmit( ControlUsbIrpImp irp ) throws UsbException
	{
		asyncSubmit(irp);

		irp.waitUntilComplete();

		if (irp.isUsbException())
			throw irp.getUsbException();
	}

	/**
	 * Asynchronously submits this UsbIrpImp to the platform implementation.
	 * <p>
	 * This uses {@link #asyncSubmit(ControlUsbIrpImp) asyncSubmit(ControlUsbIrpImp)},
	 * after casting the UsbIrpImp to a ControlUsbIrpImp.
	 * @param irp the ControlUsbIrpImp to use for this submission
	 * @exception UsbException If the initial submission was unsuccessful.
	 */
	public void asyncSubmit( UsbIrpImp irp ) throws UsbException
	{
		asyncSubmit((ControlUsbIrpImp)irp);
	}

	/**
	 * Asynchronously submits this ControlUsbIrpImp to the platform implementation.
	 * <p>
	 * The OS-implementation must implement this method.
	 * @param irp the ControlUsbIrpImp to use for this submission
	 * @exception UsbException If the initial submission was unsuccessful.
	 */
	public abstract void asyncSubmit( ControlUsbIrpImp irp ) throws UsbException;
}
