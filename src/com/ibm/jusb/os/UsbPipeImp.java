package com.ibm.jusb.os;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the IBM Public License.
 *
 */

import javax.usb.*;

/**
 * The implementation interface of a UsbPipe
 * @author E. Michael Maximilien
 * @author Dan Streetman
 * @version 0.0.1 (JDK 1.1.x)
 */
public interface UsbPipeImp
{
    //-------------------------------------------------------------------------
    // Public methods
    //

	/**
	 * Get the associated UsbInterfaceImp.
	 * @return the associated UsbInterfaceImp.
	 */
	public UsbInterfaceImp getUsbInterfaceImp();

	/**
	 * Set the associated UsbInterfaceImp.
	 * @param usbInterfaceImp the associated UsbIntefaceImp.
	 */
	public void setUsbInterfaceImp( UsbInterfaceImp usbInterfaceImp );

	/**
	 * Return the current sequence number.
	 * @return the current sequence number.
	 */
	public long getSequenceNumber();

    /**
     * Open this UsbPipeImp.
	 * <p>
	 * The platform can perform whatever operations it likes.
	 * This method does not currently require the platform to guarantee
	 * anything after returning.
     * @exception javax.usb.UsbException if this pipe could not be opened.
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
	 * Note that the platform implementation must allocate a sequence number for
	 * this submission unless an UsbException is thrown.
	 * <p>
	 * The platform implementation does not need to notify the UsbPipeAbstraction
	 * of completion by calling UsbPipeAbstraction.UsbIrpImpCompleted();
	 * since the operation is synchronous the pipe performs all completion
	 * activities after the method returns (or throws UsbException).
     * @param data the byte[] data
	 * @return the status of the submission.
     * @exception javax.usb.UsbException if error occurs while sending.
     */
    public int syncSubmit( byte[] data ) throws UsbException;

	/**
	 * Synchronously submits this UsbIrpImp to the platform implementation.
	 * <p>
	 * The platform implementation does not need to notify the UsbPipeAbstraction
	 * of completion by calling UsbPipeAbstraction.UsbIrpImpCompleted();
	 * since the operation is synchronous the pipe performs all completion
	 * activities after the method returns (or throws UsbException).
	 * @param irp the UsbIrpImp to use for this submission.
     * @exception javax.usb.UsbException if error occurs while sending.
	 */
    public void syncSubmit( UsbIrpImp irp ) throws UsbException;

	/**
	 * Synchronously submits this UsbCompositeIrpImp to the platform implementation.
	 * <p>
	 * The platform implementation does not need to notify the UsbPipeAbstraction
	 * of completion by calling UsbPipeAbstraction.UsbIrpImpCompleted();
	 * since the operation is synchronous the pipe performs all completion
	 * activities after the method returns (or throws UsbException).
	 * @param irp the UsbCompositeIrpImp to use for this submission
     * @exception javax.usb.UsbException if error occurs while sending
	 */
    public void syncSubmit( UsbCompositeIrpImp irp ) throws UsbException;

	/**
	 * Asynchronously submits this UsbIrpImp to the platform implementation.
	 * <p>
	 * Since this is an asynchronous call, the platform implementation should
	 * notify the UsbPipeAbstraction upon completion of the UsbIrpImp by
	 * calling
	 * {@link com.ibm.jusb.os.UsbPipeAbstraction#UsbIrpImpCompleted(UsbIrpImp) UsbIrpImpCompleted()}
	 * <i>only if</i> the platform 'accepted' the submission by successfully
	 * returning from this method.  If the platform throws a UsbException from this
	 * method, it should <i>not</i> also call UsbIrpImpCompleted().
	 * @param irp the UsbIrpImp to use for this submission
     * @exception javax.usb.UsbException if error occurs while sending
	 */
    public void asyncSubmit( UsbIrpImp irp ) throws UsbException;

	/**
	 * Asynchronously submits this UsbCompositeIrpImp to the platform implementation.
	 * <p>
	 * Since this is an asynchronous call, the platform implementation should
	 * notify the UsbPipeAbstraction upon completion of the UsbCompositeIrpImp by
	 * calling
	 * {@link com.ibm.jusb.os.UsbPipeAbstraction#UsbIrpImpCompleted(UsbIrpImp) UsbIrpImpCompleted()}
	 * <i>only if</i> the platform 'accepted' the submission by successfully
	 * returning from this method.  If the platform throws a UsbException from this
	 * method, it should <i>not</i> also call UsbIrpImpCompleted().
	 * @param irp the UsbCompositeIrpImp to use for this submission
     * @exception javax.usb.UsbException if error occurs while sending
	 */
    public void asyncSubmit( UsbCompositeIrpImp irp ) throws UsbException;

	/**
	 * Stop all submissions in progress.
	 * <p>
	 * This should not return until all submissions have been aborted
	 * and <i>are not longer in progress</i> (i.e., the pipe is in a non-busy state).
	 */
	public void abortAllSubmissions();

    /**
     * Close this UsbPipeImp.
     */
    public void close();
}
