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

import com.ibm.jusb.util.*;

/**
 * UsbIrp implementation.
 * @author Dan Streetman
 */
public class UsbIrpImp implements UsbIrp,UsbPipe.SubmitResult,Recyclable
{
	/**
	 * Constructor
	 * @param factory the factory that created this
	 */
	public UsbIrpImp( RecycleFactory recycleFactory )
	{
		factory = recycleFactory;
	}

	//*************************************************************************
	// Public UsbIrp methods

	/**
	 * Accept a UsbIrpImpVisitor
	 * @param the UsbIrpImpVisitor to accept
	 */
	public void accept( UsbIrpImpVisitor visitor ) { visitor.visitUsbIrpImp( this ); }

	/**
	 * @return a unique number for this submission.
	 */
	public long getNumber() { return number; }

	/** @return the sequence number of this submission */
	public long getSequenceNumber() { return sequenceNumber; }

	/** @return the UsbPipe associated with this submission */
	public UsbPipe getUsbPipe() { return usbPipe; }

	/** @return the data associated with this submission */
	public byte[] getData() { return data; }

	/** @param data the data associated with the submission */
	public void setData( byte[] newData )
	{
		data = null == newData ? DEFAULT_DATA : newData;
	}

	/**
	 * If this UsbIrp is in progress on a UsbPipe.
	 * @return if this UsbIrp is active
	 */
	public boolean isActive() { return active; }

	/** @return true if this submit has completed */
	public boolean isCompleted() { return completed; }

	/** @return if a UsbException occured during submission */
	public boolean isInUsbException() { return ( null != getUsbException() ); }

	/** @return the number of bytes transmitted in this submission */
	public int getDataLength() { return dataLength; }

	/** Wait (block) until this submission is completed */
	public void waitUntilCompleted() { waitUntilCompleted( 0 ); }

	/** Wait (block) until this submission is completed */
	public void waitUntilCompleted( long msecs ) { waitUntilCompleted( msecs, 0 ); }

	/** Wait (block) until this submission is completed */
	public void waitUntilCompleted( long msecs, int nsecs )
	{
		synchronized ( waitLock ) {
			waitCount++;
			while (!isCompleted()) {
				try { waitLock.wait( msecs, nsecs ); }
				catch ( InterruptedException iE ) { }
			}
			waitCount--;
		}
	}

	/**
	 * Notify all Threads waiting for completion
	 * <p>
	 * This should be called <i>after</i> <code>setCompleted(true)</code>.
	 */
	public void notifyCompleted()
	{
		if (0 < waitCount) {
			synchronized ( waitLock ) {
				waitLock.notifyAll();
			}
		}
	}

	/** @return the UsbException that occured during submission */
	public UsbException getUsbException() { return usbException; }

	/**
	 * @return if a short packet during this submission should be accepted (no error)
	 */
	public boolean getAcceptShortPacket() { return acceptShortPacket; }

	/**
	 * @param accept if a short packet during this submission should be accepted (no error)
	 */
	public void setAcceptShortPacket( boolean accept ) { acceptShortPacket = accept; }

	//*************************************************************************
	// Recyclable methods

	/** Clean this object */
	public void clean()
	{
		/* assume the waitCount is accurate */

		setNumber( DEFAULT_NUMBER );
		setSequenceNumber( DEFAULT_SEQUENCE_NUMBER );
		setUsbPipe( DEFAULT_USB_PIPE );
		setData( DEFAULT_DATA );
		setActive( DEFAULT_ACTIVE );
		setCompleted( DEFAULT_COMPLETED );
		setUsbException( DEFAULT_USB_EXCEPTION );
		setDataLength( DEFAULT_DATA_LENGTH );
		setAcceptShortPacket( DEFAULT_ACCEPT_SHORT_PACKET );

	}

	/**
	 * Recycle this UsbIrpImp
	 * <p>
	 * This should be called when the UsbIrpImp is no longer needed
	 */
	public void recycle()
	{
		if (isActive())
			throw new UsbRuntimeException( "You can't recycle an active UsbIrp!" );

		factory.recycle( this );
	}

	//*************************************************************************
	// Public implementation-specific methods

	/**
	 * Sets the number for this submission
	 * @param i the new number
	 */
	public void setNumber( long l ) { number = l; }

	/** @param the seqeunce number of this submision */
	public void setSequenceNumber( long sn ) { sequenceNumber = sn; }

	/**
	 * Sets the pipe for this submission
	 * @param pipe the pipe to use
	 */
	public void setUsbPipe( UsbPipe pipe ) { usbPipe = pipe; }

	/**
	 * Set isActive.
	 * <p>
	 * This indicates whether the UsbIrp is in progress on a UsbPipe.
	 * @param b whether this is active
	 */
	public void setActive( boolean b ) { active = b; }

	/**
	 * Sets isCompleted
	 * <p>
	 * This <i>must</i> be called before {@link #notifyCompleted() notifyCompleted()}
	 * to guarantee Threads do not incorrectly wait.
	 * @param b whether this is done
	 * @see #notifyCompleted()
	 */
	public void setCompleted( boolean b ) { completed = b; }

	/**
	 * Sets the complted result
	 * @param i the result
	 */
	public void setDataLength( int i ) { dataLength = i; }

	/**
	 * Sets the UsbException that occured during submission
	 * @param exception the UsbException
	 */
	public void setUsbException( UsbException exception ) { usbException = exception; }

	//*************************************************************************
	// Instance variables

	private Object waitLock = new Object();
	private int waitCount = 0;

	private long number = DEFAULT_NUMBER;
	private long sequenceNumber = DEFAULT_SEQUENCE_NUMBER;
	private UsbPipe usbPipe = DEFAULT_USB_PIPE;
	private byte[] data = DEFAULT_DATA;
	private boolean active = DEFAULT_ACTIVE;
	private boolean completed = DEFAULT_COMPLETED;
	private boolean acceptShortPacket = DEFAULT_ACCEPT_SHORT_PACKET;
	private int dataLength = DEFAULT_DATA_LENGTH;
	private UsbException usbException = DEFAULT_USB_EXCEPTION;

	private RecycleFactory factory = null;

	//*************************************************************************
	// Class constants

	private static final long DEFAULT_NUMBER = -1;
	private static final long DEFAULT_SEQUENCE_NUMBER = -1;
	private static final UsbPipe DEFAULT_USB_PIPE = null;
	private static final byte[] DEFAULT_DATA = new byte[0];
	private static final boolean DEFAULT_ACTIVE = false;
	private static final boolean DEFAULT_COMPLETED = false;
	private static final boolean DEFAULT_ACCEPT_SHORT_PACKET = true;
	private static final int DEFAULT_DATA_LENGTH = -1;
	private static final UsbException DEFAULT_USB_EXCEPTION = null;

}
