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

/**
 * Defines States for UsbPipeAbstraction
 * @author E. Michael Maximilien
 * @author Dan Streetman
 * @version 0.0.1 (JDK 1.1.x)
 */
public abstract class UsbPipeOpenState extends UsbPipeActiveState
{
	public UsbPipeOpenState( UsbPipeAbstraction pipe, String name ) { super( pipe, name ); }

	//*************************************************************************
	// Public overridden methods

	/** @return if this UsbPipe is open */
	public boolean isOpen() { return true; }

	/** @return if this UsbPipe is open */
	public boolean isClosed() { return false; }

	/**
	 * Synchronous submission using a byte[].
	 * @param data the data to use for this submission
     * @exception javax.usb.UsbException if error occurs while sending
	 */
	public int syncSubmit( byte[] data ) throws UsbException
	{ 
		int result;

		try {
			result = getUsbPipeAbstraction().getUsbPipeImp().syncSubmit( data );
		} catch ( UsbException uE ) {
			throw uE;
		} finally {
			getUsbPipeAbstraction().decrementSubmissionCount();
		}

		return result;
	}

	/**
	 * Asynchronous submission using a byte[].
	 * @param data the data to use for this submission
	 * @return a UsbPipe.SubmitResult associated with this submission
     * @exception javax.usb.UsbException if error occurs while sending
	 */
	public UsbPipe.SubmitResult asyncSubmit( byte[] data ) throws UsbException
	{ 
//All this could be deferred to the platform implementation; in some cases it may
//be able to use a byte[] more efficiently than wrapping it as a UsbIrpImp here.
		UsbIrpImp irp = getUsbPipeAbstraction().createSubmitResult();

		irp.setData( data );

		asyncSubmit( irp );

		return irp;
	}

	/**
	 * Synchronous submission using a UsbIrp.
	 * <p>
	 * <b>WARNING</b>: This implementation is <i>not</i> Thread safe.
	 * @param irp the UsbIrp to use for this submission
     * @throws javax.usb.UsbException if error occurs while sending
	 * @throws java.lang.ClassCastException if the UsbIrp implementing class is not supported
	 */
    public void syncSubmit( UsbIrp irp ) throws UsbException
	{
		UsbIrpImp usbIrpImp = prepareUsbIrp( irp );

		/*
		 * The visitor does the proper submission based on UsbIrp type,
		 * and since the visitor is use across method calls, this
		 * operation is not Thread-safe (the visitor's exception is not Thread-safe).
		 */
		usbIrpImp.accept( getUsbIrpImpSyncSubmitV() );

		getUsbPipeAbstraction().decrementSubmissionCount();

		if ( getUsbIrpImpSyncSubmitV().isInUsbException() ) {
			throw getUsbIrpImpSyncSubmitV().getUsbException();
		}
	}

	/**
	 * Asynchronous submission using a UsbIrp.
	 * <p>
	 * <b>WARNING</b>: This implementation is <i>not</i> Thread safe.
	 * @param irp the UsbIrp to use for this submission
     * @throws javax.usb.UsbException if error occurs while sending
	 * @throws java.lang.ClassCastException if the UsbIrp implementing class is not supported
	 */
    public void asyncSubmit( UsbIrp irp ) throws UsbException
	{ 
		UsbIrpImp usbIrpImp = prepareUsbIrp( irp );

		/*
		 * The visitor does the proper submission based on UsbIrp type,
		 * and since the visitor is use across method calls, this
		 * operation is not Thread-safe (the visitor's exception is not Thread-safe).
		 */
		usbIrpImp.accept( getUsbIrpImpAsyncSubmitV() );

		if ( getUsbIrpImpAsyncSubmitV().isInUsbException() ) {
			getUsbPipeAbstraction().decrementSubmissionCount();

			throw getUsbIrpImpAsyncSubmitV().getUsbException();
		}
	}

	//*************************************************************************
	// Package methods

	/**
	 * Prepare a UsbIrp for submission.
	 * @param usbIrp the UsbIrp to prepare.
	 * @return the prepared UsbIrpImp.
	 * @throws javax.usb.UsbException if the UsbIrp is not a UsbIrpImp.
	 */
	UsbIrpImp prepareUsbIrp( UsbIrp usbIrp ) throws UsbException
	{
		UsbIrpImp usbIrpImp;

		try {
			usbIrpImp = (UsbIrpImp)usbIrp;
		} catch ( ClassCastException ccE ) {
			getUsbPipeAbstraction().decrementSubmissionCount();

/* There must be a better way to indicate/handle this... */
			throw new UsbException( "Cannot handle UsbIrp implementation " + usbIrp.getClass().getName() );
		}

		return usbIrpImp;
	}

	/** @return if the submission count should be incremented for this state */
	boolean shouldIncrementSubmissionCount() { return true; }

	//*************************************************************************
	// Private methods

	/**
	 * Get the visitor that performs asyncSubmits of UsbIrpImp types.
	 * <p>
	 * <b>WARNING</b>: <i>This is <b>not</b> reentrant!!!!</i>
	 * @return a UsbIrpImpPipeV to use for async submissions
	 */
	private UsbIrpImpPipeV getUsbIrpImpAsyncSubmitV()
	{
		if ( null == asyncSubmitV )
			asyncSubmitV = new UsbIrpImpAsyncSubmitV( getUsbPipeAbstraction().getUsbPipeImp() );

		return asyncSubmitV;
	}

	/**
	 * Get the visitor that performs syncSubmits of UsbIrpImp types.
	 * <p>
	 * <b>WARNING</b>: <i>This is <b>not</b> reentrant!!!!</i>
	 * @return a UsbIrpImpPipeV to use for sync submissions
	 */
	private UsbIrpImpPipeV getUsbIrpImpSyncSubmitV()
	{
		if ( null == syncSubmitV )
			syncSubmitV = new UsbIrpImpSyncSubmitV( getUsbPipeAbstraction().getUsbPipeImp() );

		return syncSubmitV;
	}

	//*************************************************************************
	// Instance variables

	private UsbIrpImpPipeV asyncSubmitV = null;
	private UsbIrpImpPipeV syncSubmitV = null;

}
