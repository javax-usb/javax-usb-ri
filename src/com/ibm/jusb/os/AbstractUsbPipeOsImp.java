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
 * Abstract implementation for UsbPipeOsImp.
 * <p>
 * This is an optional abstract class that handles all optional methods.  Those
 * methods may be overridden by the implementation if desired.  The implementation
 * does not have to extend this abstract class.
 * @author Dan Streetman
 */
public abstract class AbstractUsbPipeOsImp implements UsbPipeOsImp
{
    /**
     * Open this pipe.
	 * <p>
	 * This is implemented as a no-op.
     */
    public void open() throws UsbException { }

    /**
     * Close the pipe.
	 * <p>
	 * This is implemented as a no-op.
     */
    public void close() { }

    /**
     * Synchonously submits this byte[] array to the platform implementation.
	 * <p>
	 * This is implemented using {@link #syncSubmit(UsbIrpImp) syncSubmit(UsbIrpImp)}.
	 * @param pipe The UsbPipeImp
     * @param data the byte[] data
	 * @return the status of the submission.
     * @exception javax.usb.UsbException If the data transfer was unsuccessful.
     */
    public int syncSubmit( UsbPipeImp pipe, byte[] data ) throws UsbException
	{
		UsbIrpImp usbIrpImp = usbIrpImpFactory.createUsbIrpImp();

		usbIrpImp.setData(data);

		pipe.setupUsbIrpImp(usbIrpImp);

		syncSubmit(usbIrpImp);

		return usbIrpImp.getDataLength();
	}

	/**
	 * Synchronously submits this UsbIrpImp to the platform implementation.
	 * <p>
	 * This is implemented using {@link #asyncSubmit(UsbIrpImp) asyncSubmit(UsbIrpImp)}.
	 * @param irp the UsbIrpImp to use for this submission.
     * @exception javax.usb.UsbException If the data transfer was unsuccessful.
	 */
    public void syncSubmit( UsbIrpImp irp ) throws UsbException
	{
		asyncSubmit(irp);

		irp.waitUntilCompleted();

		if (irp.isInUsbException())
			throw irp.getUsbException();
	}

	/**
	 * Synchronously submits a List of UsbIrpImps to the platform implementation.
	 * <p>
	 * This is implemented using {@link #syncSubmit(UsbIrpImp) syncSubmit(UsbIrpImp)}.
	 * @param list the UsbIrpImps to use for this submission.
     * @exception javax.usb.UsbException If the data transfer for any of the UsbIrpImps was unsuccessful.
	 */
    public void syncSubmit( List list ) throws UsbException
	{
		for (int i=0; i<list.size(); i++) {
			try {
				syncSubmit((UsbIrpImp)list.get(i));
			} catch ( UsbException uE ) {
				/* no action, continue submitting */
			}
		}
	}

	/**
	 * Asynchronously submits a List of UsbIrpImps to the platform implementation.
	 * <p>
	 * This is implemented using {@link #asyncSubmit(UsbIrpImp) asyncSubmit(UsbIrpImp)}.
	 * @param irp the UsbIrpImp to use for this submission
     * @exception javax.usb.UsbException If there was a problem with one or more UsbIrpImps <i>before platform submission</i>.
	 */
    public void asyncSubmit( List list ) throws UsbException
	{
		for (int i=0; i<list.size(); i++) {
			try {
				asyncSubmit((UsbIrpImp)list.get(i));
			} catch ( UsbException uE ) {
				/* ignore, continue submission */
			}
		}			
	}

	/**
	 * Asynchronously submits this UsbIrpImp to the platform implementation.
	 * <p>
	 * The OS-implementation must implement this method.
	 * @param irp the UsbIrpImp to use for this submission
     * @exception javax.usb.UsbException If the initial submission was unsuccessful.
	 */
    public abstract void asyncSubmit( UsbIrpImp irp ) throws UsbException;

	/**
	 * Stop all submissions in progress.
	 * <p>
	 * The OS-implementation must implement this method.
	 */
	public abstract void abortAllSubmissions();

	private UsbIrpImpFactory usbIrpImpFactory = new UsbIrpImpFactory();
}
