package com.ibm.jusb;

/*
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import javax.usb.*;
import javax.usb.util.*;
import javax.usb.os.*;

/**
 * Default implementation for the VendorOperations interface
 * @author E. Michael Maximilien
 * @version 1.0.0
 */
public class DefaultVendorOperations extends AbstractUsbOperations implements VendorOperations
{
    //-------------------------------------------------------------------------
    // Ctor(s)
    //

	/**
	 * 1-arg ctor
	 * @param usbDevice the UsbDevice for this operations class
	 * @param vendorOpsImp the VendorOpsImp object
	 * @exception javax.usb.UsbException if the RequestFactory could not be found
	 */
	public DefaultVendorOperations( UsbDevice usbDevice, UsbOpsImp ops  ) throws UsbException
	{ 
		super( usbDevice ); 
		opsImp = ops;
	}

    //-------------------------------------------------------------------------
    // Public request methods
    //

	/**
	 * Performs a synchronous standard operation by submitting the standard request object
	 * @param request the Request object that is used for this submit
	 * @exception javax.usb.RequestException if something goes wrong sumitting the request for this operation
	 */
	public synchronized void syncSubmit( Request request ) throws RequestException
	{
		getOpsImp().syncSubmit( request );
	}

	/**
	 * Performs a synchronous operation by submitting all the Request objects in the bundle.
	 * No other request submission can be overlapped.  This means that the Request object in the
	 * bundle are guaranteed to be sent w/o interruption.
	 * @param requestBundle the RequestBundle object that is used for this submit
	 * @exception javax.usb.RequestException if something goes wrong submitting the request for this operation
	 */
	public synchronized void syncSubmit( RequestBundle requestBundle ) throws RequestException
	{
		getOpsImp().syncSubmit( requestBundle );
	}

	/**
	 * Performs an asynchronous standard operation by submitting the standard request object
	 * @param request the Request object that is used for this submit
	 * @exception javax.usb.RequestException if something goes wrong sumitting the request for this operation
	 */
	public UsbOperations.SubmitResult asyncSubmit( Request request ) throws RequestException
	{
		return getOpsImp().asyncSubmit( request );
	}

    //-------------------------------------------------------------------------
    // Public operations methods
    //

	/**
	 * Used to submit any vendor request.  Note that the bmRequestType field bits 6..5
	 * must be set to 0x02 for Vendor type according to the USB 1.1. specification
	 * @param bmRequestType the request type bitmap
	 * @param requestType the specific request type
	 * @param wValue the word feature selector value
	 * @param wIndex Zero or Interface or Endpoint index
	 * @param data a byte array for the request Data
	 * @return a Request object that is created for this submission
	 * @exception javax.usb.RequestException if something goes wrong submitting the request for this operation
	 */
	public Request vendorRequest( byte bmRequestType, byte requestType, 
								  short wValue, short wIndex, byte[] data ) throws RequestException
	{
		Request vendorRequest = getRequestFactory().
			                    createVendorRequest( bmRequestType, requestType,
													 wValue, wIndex, data );

		getOpsImp().syncSubmit( vendorRequest );

		return vendorRequest;
	}

	//-------------------------------------------------------------------------
	// Protected methods
	//

	/** @return the UsbOpsImp object */
	protected UsbOpsImp getOpsImp() { return opsImp; }

	//-------------------------------------------------------------------------
	// Instance variables
	//

	private UsbOpsImp opsImp = null;
}
