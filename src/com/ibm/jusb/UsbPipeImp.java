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
import javax.usb.event.*;
import javax.usb.util.*;

import com.ibm.jusb.os.*;
import com.ibm.jusb.util.*;

/**
 * UsbPipe platform-independent implementation.
 * <p>
 * This must be set up before use.
 * <ul>
 * <li>The 
 * @author Dan Streetman
 * @author E. Michael Maximilien
 */
public class UsbPipeImp extends Object implements UsbPipe
{
	/**
	 * Constructor.
	 * @param ep The UsbEndpointImp.
	 * @param pipe The platform-dependent pipe implementation.
	 */
    public UsbPipeImp( UsbEndpointImp ep, UsbPipeOsImp pipe )
    {
		setUsbEndpointImp(ep);
		setUsbPipeOsImp(pipe);
    }

	//**************************************************************************
    // Public methods

	/** @param the UsbPipeOsImp to use */
	public void setUsbPipeOsImp( UsbPipeOsImp pipe ) { usbPipeOsImp = pipe; }

    /** @return the UsbPipeOsImp object */
    public UsbPipeOsImp getUsbPipeOsImp() { return usbPipeOsImp; }

	/** @return if this UsbPipe is active */
	public boolean isActive() { return getUsbEndpoint().getUsbInterface().isActive(); }

	/** @return if this UsbPipe is open */
	public boolean isOpen() { return open; }

	/** @return if this UsbPipe is open */
	public boolean isClosed() { return !open; }

	/** @return if this UsbPipe is busy */
	public boolean isBusy() { return 0 != submissionCount; }

	/** @return if this UsbPipe is busy */
	public boolean isIdle() { return 0 == submissionCount; }

	/** @return if this UsbPipe is in an error state */
	public boolean isInError() { return 0 != getErrorCode(); }

	/** @return the error code indicating the cause of the current error state */
	public int getErrorCode() { return errorCode; }

	/** @param ec The error code */
	public void setErrorCode( int ec ) { errorCode = ec; }

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

	/** @param ep The UsbEndpointImp */
	public void setUsbEndpointImp(UsbEndpointImp ep) { usbEndpointImp = ep; }

	/**
	 * Opens this UsbPipe so its ready for submissions.
	 */
	public void open() throws UsbException
	{
		checkActive();

		if (!isOpen()) {
			getUsbPipeOsImp().open();

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

//FIXME-try-catch always decrement
		int result = getUsbPipeOsImp().syncSubmit(data);

		submissionCount--;

		return result;
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

	//**************************************************************************
	// Protected methods

	/**
	 * Check if this pipe is active.
	 * @throws UsbException If the pipe is not active.
	 */
	protected void checkActive() throws UsbException
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
	protected void checkOpen() throws UsbException
	{
		if (!isOpen())
			throw new UsbException("UsbPipe not open");
	}

	/** Get a uniquely-numbered SubmitResult */
	protected UsbIrpImp createSubmitResult()
	{
		UsbIrpImp irp = usbIrpImpFactory.createUsbIrpImp();

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
	private int errorCode = 0;

    private UsbPipeOsImp usbPipeOsImp = null;

	private UsbPipeEventHelper usbPipeEventHelper = new UsbPipeEventHelper( this, new FifoScheduler() );

	private UsbIrpImpFactory usbIrpImpFactory = new UsbIrpImpFactory();

	private long sequenceNumber = 0;
	private int submissionCount = 0;
	private int submitResultCount = 0;

	private UsbEndpointImp usbEndpointImp = null;

	//**************************************************************************
	// Inner classes

	/** Synchronized UsbPipe */
//FIXME - do this

}

