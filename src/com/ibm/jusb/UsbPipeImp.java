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
import com.ibm.jusb.event.*;

/**
 * UsbPipe platform-independent implementation.
 * <p>
 * This must be set up before use.
 * <ul>
 * <li>The {@link #getUsbEndpointImp() UsbEndpointImp} must be set either in the
 *     constructor or by its {@link #setUsbEndpointImp(UsbEndpointImp) setter}.</li>
 * <li>The {@link #getUsbPipeOsImp() UsbPipeOsImp} may optionally be set either in the
 *     constructor or by its {@link #setUsbPipeOsImp(UsbPipeOsImp) setter}.
 *     If not set, it defaults to a {@link com.ibm.jusb.os.DefaultUsbPipeOsImp DefaultUsbPipeOsImp}.</li>
 * </ul>
 * @author Dan Streetman
 */
public class UsbPipeImp implements UsbPipe,UsbIrpImp.UsbIrpImpListener
{
	/** Constructor. */
	public UsbPipeImp() { }

	/**
	 * Constructor.
	 * @param ep The UsbEndpointImp.
	 */
	public UsbPipeImp( UsbEndpointImp ep ) { setUsbEndpointImp(ep); }

	/**
	 * Constructor.
	 * @param pipe The platform-dependent pipe implementation.
	 */
	public UsbPipeImp( UsbPipeOsImp pipe ) { setUsbPipeOsImp(pipe); }

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

	/** @return the UsbPipeOsImp object */
	public UsbPipeOsImp getUsbPipeOsImp() { return usbPipeOsImp; }

	/** @param the UsbPipeOsImp to use */
	public void setUsbPipeOsImp( UsbPipeOsImp pipe )
	{
		if (null == pipe)
			usbPipeOsImp = new DefaultUsbPipeOsImp();
		else
			usbPipeOsImp = pipe;
	}

	/** @return if this UsbPipe is active */
	public boolean isActive() { return getUsbEndpoint().getUsbInterface().isActive(); }

	/** @return if this UsbPipe is open */
	public boolean isOpen() { return open; }

	/** @return the UsbEndpoint associated with this UsbPipe */
	public UsbEndpoint getUsbEndpoint() { return getUsbEndpointImp(); }

	/** @return the UsbEndpointImp associated with this UsbPipeImp */
	public UsbEndpointImp getUsbEndpointImp() { return usbEndpointImp; }

	/**
	 * Set the UsbEndpointImp.
	 * <p>
	 * This will also set this on the parent UsbEndpointImp.
	 * @param ep The UsbEndpointImp
	 */
	public void setUsbEndpointImp(UsbEndpointImp ep)
	{
		usbEndpointImp = ep;

		if (null != ep)
			ep.setUsbPipeImp(this);
	}

	/**
	 * Opens this UsbPipe using a null key.
	 */
	public void open() throws UsbPolicyDenied,UsbException,UsbNotActiveException,UsbNotClaimedException { open(null); }

	/**
	 * Opens this UsbPipe using the specified key.
	 * @param key The key to pass to the UsbInterfacePolicy.
	 */
	public void open(Object key) throws UsbPolicyDenied,UsbException,UsbNotActiveException,UsbNotClaimedException
	{
		checkActive();

		if (!getUsbEndpointImp().getUsbInterfaceImp().hasUsbInterfacePolicy()) {
			String i = UsbUtil.toHexString(getUsbEndpointImp().getUsbInterfaceImp().getUsbInterfaceDescriptor().bInterfaceNumber());
			String a = UsbUtil.toHexString(getUsbEndpointImp().getUsbInterfaceImp().getUsbInterfaceDescriptor().bAlternateSetting());
			throw new UsbNotClaimedException("UsbInterface 0x" + i + " setting 0x" + a + " is not claimed");
		}

		if (!getUsbEndpointImp().getUsbInterfaceImp().getUsbInterfacePolicy().open(this, key))
			throw new UsbPolicyDenied();

/* FIXME - create UsbOpenException ? */
		if (isOpen())
			throw new UsbException("UsbPipe is already open");

		getUsbPipeOsImp().open();
		open = true;
	}

	/** Closes this UsbPipe. */
	public void close() throws UsbException,UsbNotActiveException,UsbNotOpenException
	{
		checkOpen();

/* FIXME - keep track of # of outstanding submissions? need to throw UsbException if any */

		getUsbPipeOsImp().close();
		open = false;
	}

	/**
	 * Synchonously submits this byte[] array to the UsbPipe.
	 */
	public int syncSubmit( byte[] data ) throws UsbException,IllegalArgumentException,UsbNotActiveException,UsbNotOpenException
	{
		checkOpen();

		UsbIrpImp usbIrpImp = createUsbIrpImp();
		usbIrpImp.setData(data);
		syncSubmit(usbIrpImp);

		return usbIrpImp.getLength();
	}

	/**
	 * Asynchonously submits this byte[] array to the UsbPipe.
	 */
	public UsbIrp asyncSubmit( byte[] data ) throws UsbException,IllegalArgumentException,UsbNotActiveException,UsbNotOpenException
	{
		checkOpen();

		UsbIrpImp usbIrpImp = createUsbIrpImp();
		usbIrpImp.setData(data);
		asyncSubmit(usbIrpImp);

		return usbIrpImp;
	}

	/**
	 * Synchronous submission using a UsbIrp.
	 */
	public void syncSubmit( UsbIrp usbIrp ) throws UsbException,IllegalArgumentException,UsbNotActiveException,UsbNotOpenException
	{
		checkOpen();

		getUsbPipeOsImp().syncSubmit( usbIrpToUsbIrpImp( usbIrp ) );
	}

	/**
	 * Asynchronous submission using a UsbIrp.
	 */
	public void asyncSubmit( UsbIrp usbIrp ) throws UsbException,IllegalArgumentException,UsbNotActiveException,UsbNotOpenException
	{
		checkOpen();

		getUsbPipeOsImp().asyncSubmit( usbIrpToUsbIrpImp( usbIrp ) );
	}

	/**
	 * Synchronous submission using a List of UsbIrps.
	 */
	public void syncSubmit( List list ) throws UsbException,IllegalArgumentException,UsbNotActiveException,UsbNotOpenException
	{
		checkOpen();

		getUsbPipeOsImp().syncSubmit( usbIrpListToUsbIrpImpList( list ) );
	}

	/**
	 * Asynchronous submission using a List of UsbIrps.
	 */
	public void asyncSubmit( List list ) throws UsbException,IllegalArgumentException,UsbNotActiveException,UsbNotOpenException
	{
		checkOpen();

		getUsbPipeOsImp().asyncSubmit( usbIrpListToUsbIrpImpList( list ) );
	}

	/**
	 * Stop all submissions in progress.
	 */
	public void abortAllSubmissions() throws UsbNotActiveException,UsbNotOpenException
	{
		checkOpen();

		getUsbPipeOsImp().abortAllSubmissions();
	}

	/**
	 * Create a UsbIrp.
	 * @return A UsbIrp ready for use.
	 */
	public UsbIrp createUsbIrp() { return new UsbIrpImp(); }

	/**
	 * Create a UsbControlIrp.
	 * @param bmRequestType The bmRequestType.
	 * @param bRequest The bRequest.
	 * @param wValue The wValue.
	 * @param wIndex The wIndex.
	 * @return A UsbControlIrp ready for use.
	 */
	public UsbControlIrp createUsbControlIrp(byte bmRequestType, byte bRequest, short wValue, short wIndex)
	{ return new UsbControlIrpImp(bmRequestType, bRequest, wValue, wIndex); }

	/**
	 * Indicate that a specific UsbIrpImp has completed.
	 * @param irp The UsbIrpImp that completed.
	 */
	public void usbIrpImpComplete( UsbIrpImp irp )
	{
		if (irp.isUsbException())
			listenerImp.errorEventOccurred(new UsbPipeErrorEvent(this,irp.getUsbException()));
		else
			listenerImp.dataEventOccurred(new UsbPipeDataEvent(this,irp,irp.getData(),irp.getOffset(),irp.getActualLength()));
	}

	/**
	 * Register's the listener object for UsbPipeEvent
	 * @param listener the UsbPipeListener instance
	 */
	public void addUsbPipeListener( UsbPipeListener listener ) { listenerImp.addEventListener( listener ); }

	/**
	 * Removes the listener object from the listener list
	 * @param listener the UsbPipeListener instance
	 */
	public void removeUsbPipeListener( UsbPipeListener listener ) { listenerImp.removeEventListener( listener ); }

	public void setupUsbIrpImp(UsbIrpImp irp)
	{
		irp.setUsbIrpImpListener(this);
	}

	//**************************************************************************
	// Protected methods

	/**
	 * Convert a UsbIrp to UsbIrpImp.
	 * @param irp The UsbIrp to convert.
	 * @exception UsbException If the UsbIrp is not ready for submission.
	 * @exception IllegalArgumentException If the UsbIrp is not valid.
	 */
	protected UsbIrpImp usbIrpToUsbIrpImp(UsbIrp irp) throws UsbException,IllegalArgumentException
	{
		UsbIrpImp.checkUsbIrp(irp);

		UsbIrpImp usbIrpImp = null;
		try {
			usbIrpImp = (UsbIrpImp)irp;
		} catch ( ClassCastException ccE ) {
			usbIrpImp = new UsbIrpImp(irp);
		}

		setupUsbIrpImp(usbIrpImp);

		return usbIrpImp;
	}

	protected List usbIrpListToUsbIrpImpList(List list) throws UsbException,IllegalArgumentException
	{
		List newlist = new ArrayList();

		for (int i=0; i<list.size(); i++)
			newlist.add(usbIrpToUsbIrpImp((UsbIrp)list.get(i)));

		return newlist;
	}

	/**
	 * Check if this pipe is active.
	 * @throws UsbNotActiveException If the pipe is not active.
	 */
	protected void checkActive() throws UsbNotActiveException
	{
		if (!isActive())
			throw new UsbNotActiveException("UsbPipe not active");
	}

	/**
	 * Check if this pipe is open.
	 * <p>
	 * A pipe must be active to be open.
	 * @throws UsbNotActiveException If the pipe is not active.
	 * @throws UsbNotOpenException If the pipe is not open.
	 */
	protected void checkOpen() throws UsbNotActiveException,UsbNotOpenException
	{
		checkActive();

		if (!isOpen())
			throw new UsbNotOpenException("UsbPipe not open");
	}

	/** Get a uniquely-numbered UsbIrpImp */
	protected UsbIrpImp createUsbIrpImp()
	{
		return new UsbIrpImp();
	}

	//**************************************************************************
	// Instance variables

	private boolean open = false;

	private UsbPipeOsImp usbPipeOsImp = new DefaultUsbPipeOsImp();

	private UsbEndpointImp usbEndpointImp = null;

	protected UsbPipeListenerImp listenerImp = new UsbPipeListenerImp();
}
