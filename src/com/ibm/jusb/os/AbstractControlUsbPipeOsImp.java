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
 * <p>
 * This implementation must be driven by a ControlUsbPipeImp; all methods require ControlUsbIrpImps and
 * cannot accept non-Control UsbIrpImps.
 * @author Dan Streetman
 */
public abstract class AbstractControlUsbPipeOsImp extends AbstractUsbPipeOsImp implements ControlUsbPipeOsImp
{
	/**
	 * Synchronously submit a ControlUsbIrpImp.
	 * <p>
	 * This casts the UsbIrpImp to a ControlUsbIrpImp and uses the
	 * {@link #syncSubmit(ControlUsbIrpImp) syncSubmit(ControlUsbIrpImp)} method.
	 * @param irp The ControlUsbIrpImp to submit.
	 * @exception UsbException If {@link #syncSubmit(ControlUsbIrpImp) syncSubmit(ControlUsbIrpImp)} throws a UsbException.
	 * @exception ClassCastException If the UsbIrpImp is not a ControlUsbIrpImp.
	 */
	public void syncSubmit( UsbIrpImp irp ) throws UsbException,ClassCastException
	{
		syncSubmit((ControlUsbIrpImp)irp);
	}

	/**
	 * Asynchronously submits this UsbIrpImp to the platform implementation.
	 * <p>
	 * This casts the UsbIrpImp to a ControlUsbIrpImp and uses the
	 * {@link #syncSubmit(ControlUsbIrpImp) asyncSubmit(ControlUsbIrpImp)} method.
	 * @param irp The ControlUsbIrpImp to submit.
	 * @exception UsbException If {@link #asyncSubmit(ControlUsbIrpImp) asyncSubmit(ControlUsbIrpImp)} throws a UsbException.
	 * @exception ClassCastException If the UsbIrpImp is not a ControlUsbIrpImp.
	 */
	public void asyncSubmit( UsbIrpImp irp ) throws UsbException,ClassCastException
	{
		asyncSubmit((ControlUsbIrpImp)irp);
	}

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
	 * Asynchronously submits this ControlUsbIrpImp to the platform implementation.
	 * <p>
	 * The OS-implementation must implement this method.
	 * @param irp the ControlUsbIrpImp to use for this submission
	 * @exception UsbException If the initial submission was unsuccessful.
	 */
	public abstract void asyncSubmit( ControlUsbIrpImp irp ) throws UsbException;

}
