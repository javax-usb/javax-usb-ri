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
 * Defines inactive UsbPipeState.
 * @author Dan Streetman
 * @author E. Michael Maximilien
 * @version 0.0.1 (JDK 1.1.x)
 */
public class UsbPipeInactiveState extends UsbPipeState
{
	/**
	 * @param pipe the UsbPipeAbstraction associated with this UsbPipeState
	 * @param name the String name
	 */
	public UsbPipeInactiveState( UsbPipeAbstraction pipe )
	{
		super( pipe, NAME );
		setExceptionMessage( INACTIVE_PIPE_MESSAGE );
	}

	//*************************************************************************
	// Public methods

	/** @return if this UsbPipe is active */
	public boolean isActive() { return false; }

	/** @return if this UsbPipe is open */
	public boolean isOpen()
	{ throw new NotActiveException( getExceptionMessage(), UsbPipeConst.USB_PIPE_ERR_INACTIVE_PIPE ); }

	/** @return if this UsbPipe is open */
	public boolean isClosed()
	{ throw new NotActiveException( getExceptionMessage(), UsbPipeConst.USB_PIPE_ERR_INACTIVE_PIPE ); }

	/** @return if this UsbPipe is busy */
	public boolean isBusy()
	{ throw new NotActiveException( getExceptionMessage(), UsbPipeConst.USB_PIPE_ERR_INACTIVE_PIPE ); }

	/** @return if this UsbPipe is busy */
	public boolean isIdle()
	{ throw new NotActiveException( getExceptionMessage(), UsbPipeConst.USB_PIPE_ERR_INACTIVE_PIPE ); }

	/** @return if this UsbPipe is in an error state */
	public boolean isInError()
	{ throw new NotActiveException( getExceptionMessage(), UsbPipeConst.USB_PIPE_ERR_INACTIVE_PIPE ); }

	/** @return the error code indicating the cause of the current error state */
	public int getErrorCode()
	{ throw new NotActiveException( getExceptionMessage(), UsbPipeConst.USB_PIPE_ERR_INACTIVE_PIPE ); }

	/**
	 * @exception javax.usb.UsbException if something goes wrong opening the pipe
	 */
	public void open() throws UsbException
	{ throw new NotActiveException( getExceptionMessage(), UsbPipeConst.USB_PIPE_ERR_INACTIVE_PIPE ); }

	/**
	 * @exception javax.usb.UsbException if something goes wrong closing the pipe
	 */
	public void close() throws UsbException 
	{ throw new NotActiveException( getExceptionMessage(), UsbPipeConst.USB_PIPE_ERR_INACTIVE_PIPE ); }

	/**
	 * Synchronous submission using a byte[].
	 * @param data the byte[] data
     * @return the number of bytes transferred in the submission
	 * @exception javax.usb.UsbException if error occurs while sending
	 */
	public int syncSubmit( byte[] data ) throws UsbException
	{ throw new NotActiveException( getExceptionMessage(), UsbPipeConst.USB_PIPE_ERR_INACTIVE_PIPE ); }

	/**
	 * Synchronous submission using a byte[].
	 * @param data the byte[] data
	 * @param timeout the number of ms to wait before giving up.
     * @return the number of bytes transferred in the submission
	 * @exception javax.usb.UsbException if error occurs while sending
	 */
	public int syncSubmit( byte[] data, long timeout ) throws UsbException
	{ throw new NotActiveException( getExceptionMessage(), UsbPipeConst.USB_PIPE_ERR_INACTIVE_PIPE ); }

	/**
	 * Asynchronous submission using a byte[] and SubmitResult.
     * @return a UsbPipe.SubmitResult future object
	 * @param data the byte[] data
	 * @exception javax.usb.UsbException if error occurs while sending
	 */
	public UsbPipe.SubmitResult asyncSubmit( byte[] data ) throws UsbException
	{ throw new NotActiveException( getExceptionMessage(), UsbPipeConst.USB_PIPE_ERR_INACTIVE_PIPE ); }

	/**
	 * Synchronous submission using a UsbIrp.
	 * @param irp the UsbIrp to use for this submission
     * @exception javax.usb.UsbException if error occurs while sending
	 */
    public void syncSubmit( UsbIrp irp ) throws UsbException
	{ throw new NotActiveException( getExceptionMessage(), UsbPipeConst.USB_PIPE_ERR_INACTIVE_PIPE ); }

	/**
	 * Synchronous submission using a UsbIrp.
	 * @param irp the UsbIrp to use for this submission
	 * @param timeout the number of ms to wait before giving up.
     * @exception javax.usb.UsbException if error occurs while sending
	 */
    public void syncSubmit( UsbIrp irp, long timeout ) throws UsbException
	{ throw new NotActiveException( getExceptionMessage(), UsbPipeConst.USB_PIPE_ERR_INACTIVE_PIPE ); }

	/**
	 * Asynchronous submission using a UsbIrp.
	 * @param irp the UsbIrp to use for this submission
     * @exception javax.usb.UsbException if error occurs while sending
	 */
    public void asyncSubmit( UsbIrp irp ) throws UsbException
	{ throw new NotActiveException( getExceptionMessage(), UsbPipeConst.USB_PIPE_ERR_INACTIVE_PIPE ); }

	/**
	 * Abort the specified UsbIrp.
	 * @param irp the UsbIrp to abort
	 */
	public void abortSubmission( UsbIrp irp )
	{ throw new NotActiveException( getExceptionMessage(), UsbPipeConst.USB_PIPE_ERR_INACTIVE_PIPE ); }

	/**
	 * Abort all pending submissions on this UsbPipe.
	 */
	public void abortAllSubmissions()
	{ throw new NotActiveException( getExceptionMessage(), UsbPipeConst.USB_PIPE_ERR_INACTIVE_PIPE ); }

	//*************************************************************************
	// Class constants

	public static final String INACTIVE_PIPE_MESSAGE = "UsbPipe is not active";

	public static final String NAME = "Inactive State";

}
