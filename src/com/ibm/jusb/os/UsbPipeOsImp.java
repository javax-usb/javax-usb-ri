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
 * Interface for Platform-specific implementation of UsbPipe.
 * <p>
 * All methods are synchronized in the Platform-Independent layer; the
 * implementation does not need to make them Thread-safe.
 * @author Dan Streetman
 */
public interface UsbPipeOsImp
{
    /**
     * Open this pipe.
	 * <p>
	 * The platform can perform whatever operations it likes.
	 * This method does not currently require the platform to guarantee
	 * anything after returning.
     * @exception javax.usb.UsbException If this pipe could not be opened.
     */
    public void open() throws UsbException;

    /**
     * Synchonously submits this byte[] array to the platform implementation.
	 * <p>
	 * Where required, the platform implementation should use defaults from
	 * {@link javax.usb.UsbIrp UsbIrp}; e.g., UsbIrp's
	 * {@link javax.usb.UsbIrp#getAcceptShortPacket() getAcceptShortPacket}
	 * defaults to true, so the platform should also accept short packets for
	 * this submission.
	 * <p>
	 * The platform implementation does not need to notify the UsbPipeImp
	 * of completion by calling UsbPipeImp.UsbIrpImpCompleted();
	 * since the operation is synchronous the pipe performs all completion
	 * activities after the method returns (or throws UsbException).
     * @param data the byte[] data
	 * @return the status of the submission.
     * @exception javax.usb.UsbException If the data transfer was unsuccessful.
     */
    public int syncSubmit( UsbPipeImp pipe, byte[] data ) throws UsbException;

	/**
	 * Synchronously submits this UsbIrpImp to the platform implementation.
	 * <p>
	 * The platform implementation does not need to notify the UsbPipeImp
	 * of completion by calling UsbPipeImp.UsbIrpImpCompleted();
	 * since the operation is synchronous the pipe performs all completion
	 * activities after the method returns (or throws UsbException).
	 * <p>
	 * If the UsbIrpImp is successful, its data length must be {@link com.ibm.jusb.UsbIrpImp#setDataLength(int) set}.
	 * If unsuccessful, simply throw the UsbException.
	 * @param irp the UsbIrpImp to use for this submission.
     * @exception javax.usb.UsbException If the data transfer was unsuccessful.
	 */
    public void syncSubmit( UsbIrpImp irp ) throws UsbException;

	/**
	 * Synchronously submits a List of UsbIrpImps to the platform implementation.
	 * <p>
	 * The platform implementation does not need to notify the UsbPipeImp
	 * of completion by calling UsbPipeImp.UsbIrpImpCompleted();
	 * since the operation is synchronous the pipe performs all completion
	 * activities after the method returns (or throws UsbException).
	 * <p>
	 * All UsbIrpImps that complete successfully must have their appropriate
	 * values set, and (if an UsbException is thrown) all unsuccessful
	 * UsbIrpImps must have their UsbException set, before throwing
	 * a UsbException for the method.
	 * @param list the UsbIrpImps to use for this submission.
     * @exception javax.usb.UsbException If the data transfer for any of the UsbIrpImps was unsuccessful.
	 */
    public void syncSubmit( List list ) throws UsbException;

	/**
	 * Asynchronously submits this UsbIrpImp to the platform implementation.
	 * <p>
	 * Since this is an asynchronous call, the platform implementation should
	 * notify the UsbPipeImp upon completion of the UsbIrpImp by
	 * calling
	 * {@link com.ibm.jusb.UsbPipeImp#UsbIrpImpCompleted(UsbIrpImp) UsbIrpImpCompleted()}
	 * <i>only if</i> the platform 'accepted' the submission by successfully
	 * returning from this method.  If the platform throws a UsbException from this
	 * method, it should <i>not</i> also call UsbIrpImpCompleted().
	 * <p>
	 * If the UsbIrpImp is successful, its data length must be {@link com.ibm.jusb.UsbIrpImp#setDataLength(int) set};
	 * if unsuccessful, its UsbException must be {@link com.ibm.jusb.UsbIrpImp#setUsbException(UsbException) set}.
	 * @param irp the UsbIrpImp to use for this submission
     * @exception javax.usb.UsbException If the initial submission was unsuccessful.
	 */
    public void asyncSubmit( UsbIrpImp irp ) throws UsbException;

	/**
	 * Asynchronously submits a List of UsbIrpImps to the platform implementation.
	 * <p>
	 * Since this is an asynchronous call, the platform implementation should
	 * notify the UsbPipeImp upon completion of the UsbIrpImp by
	 * calling
	 * {@link com.ibm.jusb.UsbPipeImp#UsbIrpImpCompleted(UsbIrpImp) UsbIrpImpCompleted()},
	 * for each UsbIrpImp in order, <i>only if</i> the platform 'accepted' the submission by successfully
	 * returning from this method.  If the platform throws a UsbException from this
	 * method, it should <i>not</i> also call UsbIrpImpCompleted(); and it does not have to set the UsbException field of each UsbIrpImp.
	 * <p>
	 * This method must return before the first UsbIrpImp completes (and should return before the first
	 * UsbIrpImp is submitted natively).  Any problems in platform submission should be reported on a per-UsbIrpImp basis (asynchronously).
	 * @param irp the UsbIrpImp to use for this submission
     * @exception javax.usb.UsbException If there was a problem with one or more UsbIrpImps <i>before platform submission</i>.
	 */
    public void asyncSubmit( List list ) throws UsbException;

	/**
	 * Stop all submissions in progress.
	 * <p>
	 * This should not return until all submissions have been aborted
	 * and <i>are no longer in progress</i> (i.e., the pipe is in a non-busy state).
	 * <p>
	 * The implementation may assume no more submissions will occur while this is executing.
	 */
	public void abortAllSubmissions();

    /**
     * Close the pipe.
     */
    public void close();
}
