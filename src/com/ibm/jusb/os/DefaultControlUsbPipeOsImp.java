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
 * Default implementation for ControlUsbPipeOsImp.
 * <p>
 * This is identical to DefaultUsbPipeOsImp except all the methods require
 * ControlUsbIrpImps, not UsbIrpImps.  This should be driven by a {@link com.ibm.jusb.ControlUsbPipeImp ControlUsbPipeImp},
 * not a normal {@link com.ibm.jusb.UsbPipeImp UsbPipeImp}.
 * @author Dan Streetman
 */
public class DefaultControlUsbPipeOsImp extends DefaultUsbPipeOsImp implements ControlUsbPipeOsImp
{
	/** Constructor. */
	public DefaultControlUsbPipeOsImp() { super(); }

	/**
	 * Constructor.
	 * @param open The String to use in UsbExceptions thrown in {@link #open() open()}.
	 * @param submit The String to use in UsbExceptions thrown in submit methods.
	 */
	public DefaultControlUsbPipeOsImp(String open, String submit) { super(open,submit); }

	/**
	 * Constructor.
	 * <p>
	 * If this is true, opening is allowed.
	 * @param allowOpen If this should allow opening.
	 */
	public DefaultControlUsbPipeOsImp(boolean open) { super(open); }

	/**
	 * Synchronously submit a ControlUsbIrpImp.
	 * <p>
	 * This casts the UsbIrpImp to a ControlUsbIrpImp and uses the
	 * {@link #syncSubmit(ControlUsbIrpImp) syncSubmit(ControlUsbIrpImp)} method.
	 * @param irp The ControlUsbIrpImp to submit.
	 * @exception UsbException If {@link #syncSubmit(ControlUsbIrpImp) syncSubmit(ControlUsbIrpImp)} throws a UsbException.
	 */
	public void syncSubmit( UsbIrpImp irp ) throws UsbException,ClassCastException
	{
		try {
			syncSubmit((ControlUsbIrpImp)irp);
		} catch ( ClassCastException ccE ) {
			try {
				syncSubmit(new ControlUsbIrpImp((ControlUsbIrp)irp));
			} catch ( ClassCastException ccE2 ) {
				throw new UsbException("Control pipes can only handle ControlUsbIrps.");
			}
		}
	}

	/**
	 * Asynchronously submits this UsbIrpImp to the platform implementation.
	 * <p>
	 * This casts the UsbIrpImp to a ControlUsbIrpImp and uses the
	 * {@link #syncSubmit(ControlUsbIrpImp) asyncSubmit(ControlUsbIrpImp)} method.
	 * @param irp The ControlUsbIrpImp to submit.
	 * @exception UsbException If {@link #asyncSubmit(ControlUsbIrpImp) asyncSubmit(ControlUsbIrpImp)} throws a UsbException.
	 */
	public void asyncSubmit( UsbIrpImp irp ) throws UsbException,ClassCastException
	{
		try {
			asyncSubmit((ControlUsbIrpImp)irp);
		} catch ( ClassCastException ccE ) {
			try {
				asyncSubmit(new ControlUsbIrpImp((ControlUsbIrp)irp));
			} catch ( ClassCastException ccE2 ) {
				throw new UsbException("Control pipes can only handle ControlUsbIrps.");
			}
		}
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
	 * By default, this throws UsbException with the String defined by {@link #getSubmitString() getSubmitString}.
	 * The implementation should override (at least) this method.
	 * @param irp the ControlUsbIrpImp to use for this submission.
	 * @exception javax.usb.UsbException If the initial submission was unsuccessful.
	 */
	public void asyncSubmit( ControlUsbIrpImp irp ) throws UsbException
	{
		throw new UsbException(getSubmitString());
	}

}
