package com.ibm.jusb;

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
import javax.usb.util.*;
import javax.usb.event.*;

import com.ibm.jusb.os.*;
import com.ibm.jusb.util.*;

/**
 * UsbIrp implementation.
 * <p>
 * If the user provided their own UsbIrp implementation, then the UsbPipeImp will 'wrap' their
 * implementation with this UsbIrpImp by {@link #setUsbIrp(UsbIrp) setting} the local
 * {@link #getUsbIrp() UsbIrp}.  If this has a local UsbIrp when it is
 * {@link #complete() complete}, this will set the proper fields on the wrapped UsbIrp.
 * @author Dan Streetman
 */
public class UsbIrpImp extends DefaultUsbIrp implements UsbIrp
{
	/** Constructor. */
	public UsbIrpImp()
	{
		super();
	}

	/**
	 * Constructor.
	 * @param irp The UsbIrp this should wrap.
	 */
	public UsbIrpImp(UsbIrp irp)
	{
		this();
		setUsbIrp(irp);
	}

	/**
	 * Complete this submission.
	 * <p>
	 * This calls the superclass's complete().
	 * Then, if there is a {@link #getUsbIrp() UsbIrp},
	 * the UsbException and actual length are set, and its complete method is called.
	 * Then if there is a {@link #getCompletion() completion}, execute it.
	 */
	public void complete()
	{
		/* Set wrapped UsbIrp fields before completing ourself */
		completeUsbIrp();

		/* set wrapper as complete and notify waiters */
		super.complete();

		/* Let device or pipe fire event(s) */
		executeCompletion();
	}

	/** If there is a wrapped UsbIrp, set its fields. */
	protected void completeUsbIrp()
	{
//FIXME - the user's UsbIrp methods could block or generate Exception/Error which will cause problems
		try {
			UsbIrp irp = getUsbIrp();
			irp.setUsbException(getUsbException());
			irp.setActualLength(getActualLength());
			irp.complete();
		} catch ( NullPointerException npE ) { }
	}

	/** Execute the Completion, if there is one. */
	protected void executeCompletion()
	{
		try { getCompletion().usbIrpImpComplete(this); }
		catch ( NullPointerException npE ) { /* No completion Runnable. */ }
		catch ( Exception e ) { /* FIXME - should ignore all completion exceptions...? */ }
	}

	/**
	 * Get the Completion to execute on {@link #complete() completion}.
	 * @return The Completion, or null if there is none.
	 */
	public UsbIrpImp.Completion getCompletion() { return completion; }

	/**
	 * Set the Completion to execute on {@link #complete() completion}.
	 * @param c The Completion.
	 */
	public void setCompletion( UsbIrpImp.Completion c ) { completion = c; }

	/**
	 * Set the UsbIrp to wrap.
	 * @param irp The UsbIrp.
	 */
	public void setUsbIrp(UsbIrp irp)
	{
		usbIrp = irp;
		setData(irp.getData(),irp.getOffset(),irp.getLength());
		setAcceptShortPacket(irp.getAcceptShortPacket());
		setUsbException(irp.getUsbException());
		setComplete(irp.isComplete());
	}

	/**
	 * Get the UsbIrp this is wrapping.
	 * @return The UsbIrp or null.
	 */
	public UsbIrp getUsbIrp() { return usbIrp; }

	/**
	 * Check the specified UsbIrp.
	 * <p>
	 * This may be used to check the validity of an UsbIrp.
	 * This will throw an IllegalArgumentException if the UsbIrp
	 * does not behave as specified in the UsbIrp interface documentation.
	 * This will throw an UsbException if the UsbIrp is in a state not
	 * ready for submission, such as being complete or having a UsbException.
	 * @exception IllegalArgumentException If the UsbIrp is not valid.
	 * @exception UsbException If the UsbIrp is not ready for submission.
	 */
	public static void checkUsbIrp(UsbIrp irp) throws IllegalArgumentException,UsbException
	{
		int datalen, offset, length;

		if (null == irp.getData())
			throw new IllegalArgumentException("UsbIrp data cannot be null.");

		datalen = irp.getData().length;
		offset = irp.getOffset();
		length = irp.getLength();

		if (0 > offset)
			throw new IllegalArgumentException("UsbIrp offset ("+offset+") cannot be negative.");

		if (0 > length)
			throw new IllegalArgumentException("UsbIrp length ("+length+") cannot be negative.");

		if (datalen < (offset + length))
			throw new UsbException("Data buffer (length "+datalen+") is smaller than offset ("+offset+") + length ("+length+") = "+(offset+length));

		if (irp.isComplete())
			throw new UsbException("UsbIrp cannot be used while isComplete() is true.");

		if (irp.isUsbException())
			throw new UsbException("UsbIrp cannot be used while isUsbException() is true.");
	}

	protected UsbIrp usbIrp = null;
	protected UsbIrpImp.Completion completion = null;

	public static interface Completion
	{
		public void usbIrpImpComplete(UsbIrpImp usbIrpImp);
	}
}
