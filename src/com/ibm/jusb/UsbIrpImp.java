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
import javax.usb.util.*;
import javax.usb.event.*;

import com.ibm.jusb.os.*;
import com.ibm.jusb.util.*;

/**
 * UsbIrp implementation.
 * <p>
 * The user must provide some fields:
 * <ul>
 * <li>{@link #getData() data} via its {@link #setData(byte[]) setter}.</li>
 * <li>{@link #getLength() length} via its {@link #setLength(int) setter}.</li>
 * <li>{@link #getOffset() offset} via its {@link #setOffset(int) setter}.</li>
 * <li>{@link #getAcceptShortPacket() short packet policy} via its {@link #setAcceptShortPacket(boolean) setter}.</li>
 * </ul>
 * <p>
 * The os implementation will then process this.
 * Once processing is complete, the implementation will set the
 * {@link #getActualLength() actual length} via its {@link #setActualLength(int) setter}.
 * If unsuccessful, the implementation will set the
 * {@link #getUsbException() UsbException} via its {@link #setUsbException(UsbException) setter}.
 * The implementation will then set this {@link #complete() complete}.
 * <p>
 * If the user provided their own UsbIrp implementation, then the UsbPipeImp will 'wrap' their
 * implementation with this UsbIrpImp by {@link #setUsbIrp(UsbIrp) setting} the local
 * {@link #getUsbIrp() UsbIrp}.  If this has a local UsbIrp when it is
 * {@link #complete() complete}, this will set the proper fields on the wrapped UsbIrp.
 * @author Dan Streetman
 */
public class UsbIrpImp implements UsbIrp
{
	/** Constructor. */
	public UsbIrpImp() { }

	/**
	 * Constructor.
	 * @param irp The UsbIrp this should wrap.
	 */
	public UsbIrpImp(UsbIrp irp) { setUsbIrp(irp); }

	//*************************************************************************
	// Public methods

	/** @return the data associated with this submission */
	public byte[] getData() { return data; }

	/** @param data the data associated with the submission */
	public void setData( byte[] newData ) { data = newData; }

	/** @return The offset. */
	public int getOffset() { return offset; }

	/** @param o The offset. */
	public void setOffset(int o) { offset = o; }

	/** @return The length. */
	public int getLength() { return length; }

	/** @param l The length. */
	public void setLength(int l) { length = l; }

	/** @return The actual length. */
	public int getActualLength() { return actualLength; }

	/** @param l The actual length. */
	public void setActualLength(int l) { actualLength = l; }

	/** @return true if this submit has complete */
	public boolean isComplete() { return complete; }

	/** @return if a UsbException occured during submission */
	public boolean isUsbException() { return ( null != getUsbException() ); }

	/** Wait (block) until this submission is complete */
	public void waitUntilComplete()
	{
		if (!isComplete()) {
			synchronized ( waitLock ) {
				waitCount++;
				while (!isComplete()) {
					try { waitLock.wait(); }
					catch ( InterruptedException iE ) { }
				}
				waitCount--;
			}
		}
	}

	/** Wait (block) until this submission is complete */
	public void waitUntilComplete( long msecs )
	{
		if (0 == msecs) {
			waitUntilComplete();
			return;
		}

		if (!isComplete()) {
			synchronized ( waitLock ) {
				waitCount++;
//FIXME - wait specified amount of time even if interrupted
				if (!isComplete()) {
					try { waitLock.wait( msecs ); }
					catch ( InterruptedException iE ) { }
				}
				waitCount--;
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

	/** @param b If this is complete. */
	public void setComplete( boolean b ) { complete = b; }

	/**
	 * Complete this submission.
	 */
	public void complete()
	{
		setComplete(true);

		notifyComplete();

//FIXME - the user's UsbIrp methods could block or generate Exception/Error which will cause problems
		try {
			UsbIrp irp = getUsbIrp();
			irp.setUsbException(getUsbException());
			irp.setLength(getLength());
			irp.complete();
		} catch ( NullPointerException npE ) { }

//FIXME - fire event somehow...?
//getUsbPipeImp().usbIrpImpComplete(this);
	}

	/**
	 * Sets the UsbException that occured during submission
	 * @param exception the UsbException
	 */
	public void setUsbException( UsbException exception ) { usbException = exception; }

	/**
	 * Set the UsbIrp to wrap.
	 * @param irp The UsbIrp.
	 */
	public void setUsbIrp(UsbIrp irp) { usbIrp = irp; }

	/**
	 * Get the UsbIrp this is wrapping.
	 * @return The UsbIrp or null.
	 */
	public UsbIrp getUsbIrp() { return usbIrp; }

	//*************************************************************************
	// Protected methods

	/**
	 * Notify any Threads {@link #waitUntilComplete() waiting for completion}.
	 * This <b>must</b> be called <i>after</i> setting this as {@link #isComplete() complete}.
	 */
	protected void notifyComplete()
	{
		if (0 < waitCount) {
			synchronized ( waitLock ) {
				waitLock.notifyAll();
			}
		}
	}

	private UsbIrp usbIrp = null;

	private Object waitLock = new Object();
	private int waitCount = 0;

	protected byte[] data = null;
	protected boolean complete = false;
	protected boolean acceptShortPacket = true;
	protected int offset = -1;
	protected int length = -1;
	protected int actualLength = -1;
	protected UsbException usbException = null;

}
