package com.ibm.jusb.os;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import javax.usb.*;

import com.ibm.jusb.util.*;

/**
 * Defines States for UsbPipeAbstraction
 * @author E. Michael Maximilien
 * @author Dan Streetman
 * @version 0.0.1 (JDK 1.1.x)
 */
public abstract class UsbPipeState extends DefaultStateMachine.DefaultState implements StateMachine.State
{
	/**
	 * @param pipe the UsbPipeAbstraction associated with this UsbPipeState
	 * @param name the String name
	 */
	public UsbPipeState( UsbPipeAbstraction pipe, String name )
	{
		super( name );
		usbPipe = pipe;
	}

	//*************************************************************************
	// Public methods

	/** @return if this UsbPipe is active */
	public abstract boolean isActive();

	/** @return if this UsbPipe is open */
	public abstract boolean isOpen();

	/** @return if this UsbPipe is open */
	public abstract boolean isClosed();

	/** @return if this UsbPipe is busy */
	public abstract boolean isBusy();

	/** @return if this UsbPipe is busy */
	public abstract boolean isIdle();

	/** @return if this UsbPipe is in an error state */
	public abstract boolean isInError();

	/** @return the error code indicating the cause of the current error state */
	public abstract int getErrorCode();

	/**
	 * @exception javax.usb.UsbException if something goes wrong opening the pipe
	 */
	public void open() throws UsbException
	{ return; }

	/**
	 * @exception javax.usb.UsbException if something goes wrong closing the pipe
	 */
	public void close() throws UsbException 
	{ return; }

	/**
	 * Synchronous submission using a byte[].
	 * @param data the byte[] data
     * @return the number of bytes transferred in the submission
	 * @exception javax.usb.UsbException if error occurs while sending
	 */
	public int syncSubmit( byte[] data ) throws UsbException
	{ throw new UsbException( getExceptionMessage() ); }

	/**
	 * Asynchronous submission using a byte[] and SubmitResult.
     * @return a UsbPipe.SubmitResult future object
	 * @param data the byte[] data
	 * @exception javax.usb.UsbException if error occurs while sending
	 */
	public UsbPipe.SubmitResult asyncSubmit( byte[] data ) throws UsbException
	{ throw new UsbException( getExceptionMessage() ); }

	/**
	 * Synchronous submission using a UsbIrp.
	 * @param irp the UsbIrp to use for this submission
     * @exception javax.usb.UsbException if error occurs while sending
	 */
    public void syncSubmit( UsbIrp irp ) throws UsbException
	{ throw new UsbException( getExceptionMessage() ); }

	/**
	 * Asynchronous submission using a UsbIrp.
	 * @param irp the UsbIrp to use for this submission
     * @exception javax.usb.UsbException if error occurs while sending
	 */
    public void asyncSubmit( UsbIrp irp ) throws UsbException
	{ throw new UsbException( getExceptionMessage() ); }

	/**
	 * Abort all pending submissions on this UsbPipe.
	 */
	public void abortAllSubmissions() { }

	//*************************************************************************
	// Package methods

// Perhaps this could be turned into a pre and post Command for States thing?
	/** @return if the submission count should be incremented for this state */
	boolean shouldIncrementSubmissionCount() { return false; }

	//*************************************************************************
	// Protected methods

	/** @return the exception message that should be used */
	protected String getExceptionMessage() { return exceptionMessage; }

	/** @param message the exception message to use */
	protected void setExceptionMessage( String message ) { exceptionMessage = message; }

	/** @return the pipe associated with this state */
	protected UsbPipeAbstraction getUsbPipeAbstraction() { return usbPipe; }

	//*************************************************************************
	// Instance variables

	protected String exceptionMessage = "";

	protected UsbPipeAbstraction usbPipe = null;

}
