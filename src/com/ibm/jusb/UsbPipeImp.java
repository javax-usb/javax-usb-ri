package com.ibm.jusb;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import javax.usb.*;
import javax.usb.event.*;
import javax.usb.util.*;

import com.ibm.jusb.util.*;

/**
 * UsbPipe platform-independent implementation.
 * <p>
 * This handles UsbPipe processing as much as possible and passes platform
 * specific responsibilities to its UsbPipeOsImp.
 * @author Dan Streetman
 * @author E. Michael Maximilien
 */
public class UsbPipeImp extends Object implements UsbPipe
{
    public UsbPipeImp( UsbEndpointImp ep, UsbPipeOsImp pipe )
    {
		usbEndpointImp = ep;
		setUsbPipeOsImp( pipe );

		if (getUsbEndpoint().getUsbInterface().isActive())
			setActive( true );
    }

	/** @param the UsbPipeOsImp to use */
	public void setUsbPipeOsImp( UsbPipeOsImp pipe ) { usbPipeOsImp = pipe; }

    /** @return the UsbPipeOsImp object */
    public UsbPipeOsImp getUsbPipeOsImp() { return usbPipeOsImp; }

	//**************************************************************************
    // Public methods

	/** @return if this UsbPipe is active */
	public boolean isActive() { return active; }

	/** @return if this UsbPipe is open */
	public boolean isOpen() { return open; }

	/** @return if this UsbPipe is open */
	public boolean isClosed() { return !open; }

	/** @return if this UsbPipe is busy */
	public boolean isBusy() { return 0 != submissionCount; }

	/** @return if this UsbPipe is busy */
	public boolean isIdle() { return 0 == submissionCount; }

	/** @return if this UsbPipe is in an error state */
	public boolean isInError() { return null != usbException; }

	/** @return the error code indicating the cause of the current error state */
	public int getErrorCode() { return errorCode; }

	/**
	 * Return the current sequence number.
	 * <p>
	 * The platform implementation is responsible for keeping track of the
	 * current sequence number; this allows it to only assign sequence numbers
	 * to submissions that are successfully submitted.
	 * @return the current sequence number of this UsbPipe
	 */
	public long getSequenceNumber() { return sequenceNumber; }

	/** @return the UsbDevice associated with this pipe */
	public UsbDevice getUsbDevice() { return getUsbDeviceImp(); }

	/** @return the UsbDeviceImp associated with this pipe */
	public UsbDeviceImp getUsbDeviceImp() { return getUsbEndpointImp().getUsbDeviceImp(); }

	/** @return the max packet size of this pipe's endpoint */
	public short getMaxPacketSize() { return getUsbEndpoint().getMaxPacketSize(); }

	/** @return the address of this pipe's endpoint */
	public byte getEndpointAddress() { return getUsbEndpoint().getEndpointAddress(); }

	/** @return this pipe's type */
	public byte getType() { return getUsbEndpoint().getType(); }

	/** @return the UsbEndpoint associated with this UsbPipe */
	public UsbEndpoint getUsbEndpoint() { return getUsbEndpointImp(); }

	/** @return the UsbEndpointImp associated with this UsbPipeImp */
	public UsbEndpointImp getUsbEndpointImp() { return usbEndpointImp; }

	/**
	 * Opens this UsbPipe so its ready for submissions.
	 */
	public void open() throws UsbException
	{
		checkActive();

		if (!isOpen()) {
			getUsbPipeOsImp().open();

			usbException = null;
			errorCode = 0;

			open = true;
		}
	}

	/**
	 * Closes this UsbPipe.
	 */
	public void close()
	{
		if (isActive() && isOpen()) {
			abortAllSubmissions();
			getUsbPipeOsImp().close();

			open = false;
		}
	}

	/**
	 * Synchonously submits this byte[] array to the UsbPipe.
	 */
	public int syncSubmit( byte[] data ) throws UsbException
	{
		checkOpen();

		submissionCount++;
		sequenceNumber++;

		return getUsbPipeOsImp().syncSubmit(data);

		submissionCount--;
	}

	/**
	 * Asynchonously submits this byte[] array to the UsbPipe.
	 */
	public UsbPipe.SubmitResult asyncSubmit( byte[] data ) throws UsbException
	{
		checkOpen();

		UsbIrpImp result = createSubmitResult();

		result.setData(data);

		submissionCount++;
		sequenceNumber++;

		getUsbPipeOsImp().asyncSubmit( result );

		return result;
	}

	/**
	 * Synchronous submission using a UsbIrp.
	 */
    public void syncSubmit( UsbIrp irp ) throws UsbException
	{
		checkOpen();

/* transform UsbIrp -> UsbIrpImp */

		submissionCount++;
		sequenceNumber++;

/* try-catch and finally decrement submissionCount */
//		getUsbPipeOsImp().syncSubmit( irpImp );

		submissionCount--;
	}

	/**
	 * Asynchronous submission using a UsbIrp.
	 */
    public void asyncSubmit( UsbIrp irp ) throws UsbException
	{
		checkOpen();

/* transform */

//		getUsbPipeOsImp().asyncSubmit( irpImp );
	}

	/**
	 * Synchronous submission using a List of UsbIrps.
	 */
    public void syncSubmit( List list ) throws UsbException
	{
		checkOpen();

/* transform */

//		getUsbPipeOsImp().syncSubmit( newList );
	}

	/**
	 * Asynchronous submission using a List of UsbIrps.
	 */
    public void asyncSubmit( List list ) throws UsbException
	{
		checkOpen();

/* transform */

//		getUsbPipeOsImp().asyncSubmit( newList );
	}

	/**
	 * Stop all submissions in progress.
	 */
	public void abortAllSubmissions()
	{
		if (isOpen())
			getUsbPipeOsImp().abortAllSubmissions();
	}

	/**
	 * Indicate that a specific UsbIrpImp has completed.
	 * <p>
	 * This should be called (by the platform implementation) to indicate
	 * that the specified UsbIrpImp has completed.
	 * @param irp the UsbIrpImp that completed.
	 */
	public void UsbIrpImpCompleted( UsbIrpImp irp )
	{
		decrementSubmissionCount();
	}

	/**
	 * Register's the listener object for UsbPipeEvent
	 * @param listener the UsbPipeListener instance
	 */
	public void addUsbPipeListener( UsbPipeListener listener ) { usbPipeEventHelper.addUsbPipeListener( listener ); }

	/**
	 * Removes the listener object from the listener list
	 * @param listener the UsbPipeListener instance
	 */
	public void removeUsbPipeListener( UsbPipeListener listener ) { usbPipeEventHelper.removeUsbPipeListener( listener ); }

	/**
	 * Get this pipe's event helper.
	 * <p>
	 * This can be used to fire events to listeners (in a seperate Thread).
	 * @returns a UsbPipeEventHelper object.
	 */
	public UsbPipeEventHelper getUsbPipeEventHelper() { return usbPipeEventHelper; }

	/**
	 * Set this UsbPipe's active status.
	 * @param status whether this pipe should become active or inactive.
	 */
	public synchronized void setActive( boolean status )
	{
		if (status == active)
			return;

//FIXME
/* Check this behavior!  Is this correct/enough? */
		if (!status) {
			abortAllSubmissions();
			close();
		}

		active = status;
	}

	//**************************************************************************
	// Protected methods

	/**
	 * Check if this pipe is active.
	 * @throws UsbException If the pipe is not active.
	 */
	protected void checkActive()
	{
		if (!isActive())
			throw new UsbException("UsbPipe not active");
	}

	/**
	 * Check if this pipe is open.
	 * <p>
	 * A pipe must be active to be open.
	 * @throws UsbException If the pipe is not open.
	 */
	protected void checkOpen()
	{
		if (!isOpen())
			throw new UsbException("UsbPipe not open");
	}

	/** Get a uniquely-numbered SubmitResult */
	protected UsbIrpImp createSubmitResult()
	{
		UsbIrpImp irp = (UsbIrpImp)AbstractUsbServices.getInstance().getHelper().getUsbIrpImpFactory().take();

		irp.setNumber( ++submitResultCount );

		return irp;
	}

	/** Increment the number of submissions in progress */
	protected void incrementSubmissionCount()
	{
		submissionCount++;
	}

	/** Decrement the number of submissions in progress */
	protected void decrementSubmissionCount()
	{
		submissionCount--;
	}		

	//**************************************************************************
	// Instance variables

	private boolean active = false;
	private boolean open = false;
	private UsbException usbException = null;
	private int errorCode = 0;

    private UsbPipeImp usbPipeImp = null;

	private UsbPipeEventHelper usbPipeEventHelper = new UsbPipeEventHelper( this, new FifoScheduler() );

	private long sequenceNumber = 0;
	private int submissionCount = 0;
	private int submitResultCount = 0;

	private UsbEndpoint usbEndpoint = null;

	//**************************************************************************
	// Inner classes

	/** Synchronized UsbPipe */

}

