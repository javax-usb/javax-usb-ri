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
 * <li>{@link #getOffset() offset} via its {@link #setOffset(int) setter}.</li>
 * <li>{@link #getLength() length} via its {@link #setLength(int) setter}.</li>
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

	/** @return if a UsbException occured during submission */
	public boolean isUsbException() { return ( null != getUsbException() ); }

	/** @return The UsbException. */
	public UsbException getUsbException() { return usbException; }

	/** @param exception The UsbException */
	public void setUsbException( UsbException exception ) { usbException = exception; }

	/** @return If a short packet during this submission should be accepted. */
	public boolean getAcceptShortPacket() { return acceptShortPacket; }

	/** @param accept If a short packet during this submission should be accepted. */
	public void setAcceptShortPacket( boolean accept ) { acceptShortPacket = accept; }

	/** @return true if this submit has complete */
	public boolean isComplete() { return complete; }

	/** @param b If this is complete. */
	public void setComplete( boolean b ) { complete = b; }

	/**
	 * Complete this submission.
	 * <p>
	 * This will:
	 * <ul>
	 * <li>Set this {@link #isComplete() complete}.</li>
	 * <li>Notify all {@link #waitUntilComplete() waiting Threads}.</li>
	 * <li>If there is a {@link #getUsbIrp() UsbIrp},
	 *   <ul>
	 *   <li>Set its {@link javax.usb.UsbIrp#setUsbException(UsbException) UsbException}.</li>
	 *   <li>Set its {@link javax.usb.UsbIrp#setActualLength(int) actual length}.</li>
	 *   <li>Call its {@link javax.usb.UsbIrp#complete() complete} method.</li>
	 *   </ul>
	 * </li>
	 * <li>If there is a {@link #getCompletion() completion}, execute it.</li>
	 * </ul>
	 */
	public void complete()
	{
		setComplete(true);

		notifyComplete();

		completeUsbIrp();

		executeCompletion();
	}

	/** If there is a wrapped UsbIrp, set its fields. */
	protected void completeUsbIrp()
	{
//FIXME - the user's UsbIrp methods could block or generate Exception/Error which will cause problems
		try {
			UsbIrp irp = getUsbIrp();
			irp.setUsbException(getUsbException());
//FIXME - only set actual length if no UsbException?
			irp.setActualLength(getActualLength());
			irp.complete();
		} catch ( NullPointerException npE ) { }
	}

	/** Execute the Completion, if there is one. */
	protected void executeCompletion()
	{
		try { getCompletion().usbIrpImpComplete(this); }
		catch ( NullPointerException npE ) { /* No completion Runnable. */ }
		catch ( Exception e ) { /* FIXME - should ignore all completion exceptions...? */ }
	}

	/**
	 * Notify any Threads {@link #waitUntilComplete() waiting for completion}.
	 * <p>
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

	/**
	 * Get the Completion to execute on {@link #complete() completion}.
	 * @return The Completion, or null if there is none.
	 */
	public UsbIrpImp.Completion getCompletion() { return completion; }

	/**
	 * Set the Completion to execute on {@link #complete() completion}.
	 * @param c The Completion.
	 */
	public void setCompletion( UsbIrpImp.Completion c ) { completion = c; }

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

	private UsbIrp usbIrp = null;

	private Object waitLock = new Object();
	private int waitCount = 0;

	private byte[] data = null;
	private boolean complete = false;
	private boolean acceptShortPacket = true;
	private int offset = -1;
	private int length = -1;
	private int actualLength = -1;
	private UsbException usbException = null;
	private UsbIrpImp.Completion completion = null;

	public static interface Completion
	{
		public void usbIrpImpComplete(UsbIrpImp usbIrpImp);
	}
}
