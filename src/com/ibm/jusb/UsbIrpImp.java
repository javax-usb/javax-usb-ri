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
 * If processing is successful, the implementation will set the
 * {@link #getLength() length} via its {@link #setLength(int) setter};
 * if unsuccessful, the implementation will set the
 * {@link #getUsbException() UsbException} via its {@link #setUsbException(UsbException) setter}.
 * In either case the implementation will then set this {@link #complete() complete}.
 * <p>
 * If the user provided their own UsbIrp implementation, then the UsbPipeImp will 'wrap' their
 * implementation with this UsbIrpImp by {@link #setUsbIrp(UsbIrp) setting} the local
 * {@link #getUsbIrp() UsbIrp}.  If this has a local UsbIrp when it is
 * {@link #complete() complete}, this will set the proper fields on the wrapped UsbIrp.
 * @author Dan Streetman
 */
public class UsbIrpImp implements UsbIrp,UsbSubmission
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

	private byte[] data = null;
	private boolean complete = false;
	private boolean acceptShortPacket = true;
	private int offset = -1;
	private int length = -1;
	private UsbException usbException = null;

	//*************************************************************************
	// Inner classes

	public static class ControlUsbIrpImp extends UsbIrpImp implements UsbIrp.ControlUsbIrp
	{
		/**
		 * Get the bmRequestType.
		 * @return The bmRequestType.
		 */
		public byte getRequestType() { return bmRequestType; }

		/**
		 * Get the bRequest.
		 * @return The bRequest.
		 */
		public byte getRequest() { return bRequest; }

		/**
		 * Get the wValue.
		 * @return The wValue.
		 */
		public short getValue() { return wValue; }

		/**
		 * Get the wIndex.
		 * @return The wIndex.
		 */
		public short getIndex() { return wIndex; }

		/**
		 * Set the bmRequestType.
		 * @param bmRequestTyp The bmRequestType.
		 */
		public void setRequestType(byte bmRequestType) { this.bmRequestType = bmRequestType; }

		/**
		 * Set the bRequest.
		 * @param bRequest The bRequest.
		 */
		public void setRequest(byte bRequest) { this.bRequest = bRequest; }

		/**
		 * Set the wValue.
		 * @param wValue The wValue.
		 */
		public void setValue(short wValue) { this.wValue = wValue; }

		/** 
		 * Set the wIndex.
		 * @param wIndex The wIndex.
		 */
		public void setIndex(short wIndex) { this.wIndex = wIndex; }

		private byte bmRequestType = 0x00;
		private byte bRequest = 0x00;
		private short wValue = 0x0000;
		private short wIndex = 0x0000;
	}

}
