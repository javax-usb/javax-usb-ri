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
 * @author Dan Streetman
 */
public class UsbPipeImp implements UsbPipe,UsbIrpImp.Completion
{
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

	/** @param the UsbPipeOsImp to use */
	public void setUsbPipeOsImp( UsbPipeOsImp pipe ) { usbPipeOsImp = pipe; }

	/** @return the UsbPipeOsImp object */
	public UsbPipeOsImp getUsbPipeOsImp() { return usbPipeOsImp; }

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
	 * Opens this UsbPipe so its ready for submissions.
	 */
	public void open() throws UsbException
	{
		checkActive();

		if (!getUsbEndpointImp().getUsbInterfaceImp().isJavaClaimed())
			throw new UsbException("Cannot open UsbPipe on unclaimed UsbInterface");

		if (!isOpen()) {
			getUsbPipeOsImp().open();
			open = true;
		}
	}

	/**
	 * Closes this UsbPipe.
	 */
	public void close()
	{
		if (isActive() && isOpen()) {
//FIXME - should abort all submissions?
//abortAllSubmissions();
			getUsbPipeOsImp().close();

			open = false;
		}
	}

	/**
	 * Synchonously submits this byte[] array to the UsbPipe.
	 */
	public int syncSubmit( byte[] data ) throws UsbException
	{
		checkOpen();

		UsbIrpImp usbIrpImp = createUsbIrpImp();
		usbIrpImp.setData(data);
		usbIrpImp.setOffset(0);
		usbIrpImp.setLength(data.length);
		syncSubmit(usbIrpImp);

		return usbIrpImp.getLength();
	}

	/**
	 * Asynchonously submits this byte[] array to the UsbPipe.
	 */
	public UsbIrp asyncSubmit( byte[] data ) throws UsbException
	{
		checkOpen();

		UsbIrpImp usbIrpImp = createUsbIrpImp();
		usbIrpImp.setData(data);
		usbIrpImp.setOffset(0);
		usbIrpImp.setLength(data.length);
		asyncSubmit(usbIrpImp);

		return usbIrpImp;
	}

	/**
	 * Synchronous submission using a UsbIrp.
	 */
	public void syncSubmit( UsbIrp usbIrp ) throws UsbException
	{
		checkOpen();

		getUsbPipeOsImp().syncSubmit( usbIrpToUsbIrpImp( usbIrp ) );
	}

	/**
	 * Asynchronous submission using a UsbIrp.
	 */
	public void asyncSubmit( UsbIrp usbIrp ) throws UsbException
	{
		checkOpen();

		getUsbPipeOsImp().asyncSubmit( usbIrpToUsbIrpImp( usbIrp ) );
	}

	/**
	 * Synchronous submission using a List of UsbIrps.
	 */
	public void syncSubmit( List list ) throws UsbException
	{
		checkOpen();

		getUsbPipeOsImp().syncSubmit( usbIrpListToUsbIrpImpList( list ) );
	}

	/**
	 * Asynchronous submission using a List of UsbIrps.
	 */
	public void asyncSubmit( List list ) throws UsbException
	{
		checkOpen();

		getUsbPipeOsImp().asyncSubmit( usbIrpListToUsbIrpImpList( list ) );
	}

	/**
	 * Stop all submissions in progress.
	 */
	public void abortAllSubmissions()
	{
		if (isOpen())
			getUsbPipeOsImp().abortAllSubmissions();
	}

	/**
	 * Indicate that a specific UsbIrpImp has completed.
	 * <p>
	 * This will be called during the UsbIrpImp's complete() method.
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

	public void setupUsbIrpImp(UsbIrpImp irp) throws UsbException
	{
		if (irp.getData().length < (irp.getOffset() + irp.getLength()))
			throw new UsbException("Data buffer is smaller than offset + length");

		irp.setUsbException( null );
		irp.setActualLength( 0 );
		irp.setComplete( false );
		irp.setCompletion( this );
	}

	//**************************************************************************
	// Protected methods

	/**
	 * Convert a UsbIrp to UsbIrpImp.
	 * @param irp The UsbIrp to convert.
	 */
	protected UsbIrpImp usbIrpToUsbIrpImp(UsbIrp irp) throws UsbException
	{
		UsbIrpImp usbIrpImp = null;
		try {
			usbIrpImp = (UsbIrpImp)irp;
		} catch ( ClassCastException ccE ) {
			usbIrpImp = new UsbIrpImp(irp);
		}

		setupUsbIrpImp(usbIrpImp);

		return usbIrpImp;
	}

	protected List usbIrpListToUsbIrpImpList(List list) throws UsbException
	{
		List newlist = new ArrayList();

		for (int i=0; i<list.size(); i++)
			newlist.add(usbIrpToUsbIrpImp((UsbIrp)list.get(i)));

		return newlist;
	}

	/**
	 * Check if this pipe is active.
	 * @throws UsbException If the pipe is not active.
	 */
	protected void checkActive() throws UsbException
	{
		if (!isActive())
			throw new UsbException("UsbPipe not active");
	}

	/**
	 * Check if this pipe is open.
	 * <p>
	 * A pipe must be active to be open.
	 * @throws UsbException If the pipe is not open.
	 */
	protected void checkOpen() throws UsbException
	{
		if (!isOpen())
			throw new UsbException("UsbPipe not open");
	}

	/** Get a uniquely-numbered UsbIrpImp */
	protected UsbIrpImp createUsbIrpImp()
	{
		return new UsbIrpImp();
	}

	//**************************************************************************
	// Instance variables

	private boolean active = false;
	private boolean open = false;

	private UsbPipeOsImp usbPipeOsImp = null;

	private UsbEndpointImp usbEndpointImp = null;

	protected UsbPipeListenerImp listenerImp = new UsbPipeListenerImp();
	protected int submissionCount = 0;
}
