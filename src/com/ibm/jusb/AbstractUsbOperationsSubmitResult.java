package com.ibm.jusb;

/*
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import javax.usb.*;

/**
 * Abstract superclass for all implementation UsbOperations.SubmitResult
 * @author E. Michael Maximilien
 * @version 1.0.0
 */
public abstract class AbstractUsbOperationsSubmitResult extends Object 
	   implements UsbOperations.SubmitResult
{
    //-------------------------------------------------------------------------
    // Ctor(s)
    //

	/**
	 * Creates a new UsbOperations.SubmitResult for the UsbOperations passed
	 * @param usbOperations the UsbOperations object
	 * @param request the Request object
	 */
	public AbstractUsbOperationsSubmitResult( UsbOperations usbOperations, Request request )
	{
		this.usbOperations = usbOperations;
		this.request = request;
	}

    //-------------------------------------------------------------------------
    // Abstract methods
    //

	/**
	 * This number represents a unique (per UsbDevice) sequential number for this submission.
	 * <p>
	 * The number is unique per specific UsbDevice.  The initial number is not specified,
	 * but the number will always be positive (greater than zero).  The number will be
	 * incremented by one (1) for each submission on a specific UsbPipe where a SubmitResult
	 * is generated.
	 * @return a unique (per UsbDevice) number for this submission.
	 */
	public abstract long getNumber();

	/**
	 * Wait (block) until this submission is completed.
	 * <p>
	 * It is guaranteed that the submission will be complete when this
	 * this method returns.
	 * <p>
	 * The implementation may or may not use synchronization on the SubmitResult.
	 */
	public abstract void waitUntilCompleted();

	/**
	 * Wait (block) until this submission is completed.
	 * <p>
	 * This method will return when at least one of the following
	 * conditions are met:
	 * <ul>
	 * <li>The submission is complete.</li>
	 * <li>The specified amount of time has elapsed.</li>
	 * </ul>
	 * The implementation may take some additional processing time
	 * beyond the specified timeout, but should attempt to keep the
	 * additional time to a minumim.  This method will not return
	 * due to timeout until at least the specified amount of time
	 * has passed.
	 * <p>
	 * The implementation may or may not use synchronization on the SubmitResult.
	 * @param timeout the number of milliseconds to wait before giving up
	 */
	public abstract void waitUntilCompleted( long timeout );

    //-------------------------------------------------------------------------
    // Public methods
    //

	/**
	 * Get the Request whose usbmission generated this SubmitResult.
	 * @return the Request associated with the submission
	 */
	public Request getRequest() { return request; }

	/**
	 * Get the data from the submission associated with this SubmitResult.
	 * @return the data associated with the submission
	 */
	public byte[] getData() { return request.getData(); }

	/**
	 * Get the length of the data actually transferred via the Request
	 * <p>
	 * This is only valid after the submission completes successfully,
	 * which may be determined by isCompleted() and isInUsbException().
	 * @return the amount of data transferred in this submission
	 */
	public int getDataLength() { return getData().length; }

	/**
	 * If the submission associated with this SubmitResult is complete.
	 * @return true if this submit is done
	 */
	public boolean isCompleted() { return completed; }

	/**
	 * Get the UsbException that occured during submission.
	 * @return any javax.usb.UsbException the submission may have caused
	 */
	public UsbException getUsbException() { return usbException; }

	/**
	 * If a UsbException occured.
	 * @return true if this SubmitResult has a UsbException
	 */
	public boolean isInUsbException() { return ( usbException != null ); }

	/**
	 * Recycle this SubmitResult.
	 * <p>
	 * This should be called when the SubmitResult is no longer needed.
	 * No fields or methods on this SubmitResult should be called after this method.
	 */
	public void recycle()
	{
		usbOperations = null;
		request = null;
		usbException = null;
		completed = false;
	}

    //-------------------------------------------------------------------------
    // Instance variables
    //

	protected UsbOperations usbOperations = null;
	protected Request request = null;
	protected UsbException usbException = null;
	protected boolean completed = false;
}
