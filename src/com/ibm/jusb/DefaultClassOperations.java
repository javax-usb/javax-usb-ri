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
 * Default implementation for the ClassOperations interface
 * @author E. Michael Maximilien
 * @version 1.0.0
 */
public class DefaultClassOperations extends AbstractUsbOperations implements ClassOperations
{
    //-------------------------------------------------------------------------
    // Ctor(s)
    //

	/**
	 * 1-arg ctor
	 * @param usbDevice the UsbDevice for this operations class
	 * @param classOpsImp the ClassOpsImp object
	 * @exception javax.usb.UsbException if the RequestFactory could not be found
	 */
	public DefaultClassOperations( UsbDevice usbDevice, ClassOpsImp classOpsImp  ) throws UsbException
	{ 
		super( usbDevice ); 
		this.classOpsImp = classOpsImp;
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
		getClassOpsImp().syncSubmit( request );
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
		getClassOpsImp().syncSubmit( requestBundle );
	}

	/**
	 * Performs an asynchronous standard operation by submitting the standard request object
	 * @param request the Request object that is used for this submit
	 * @exception javax.usb.RequestException if something goes wrong sumitting the request for this operation
	 */
	public UsbOperations.SubmitResult asyncSubmit( Request request ) throws RequestException
	{
		return getClassOpsImp().asyncSubmit( request );
	}

    //-------------------------------------------------------------------------
    // Public operations methods
    //

	/**
	 * Used to submit any class request.  Note that the bmRequestType field bits 6..5
	 * must be set to 0x01 for Class type according to the USB 1.1. specification
	 * @param bmRequestType the request type bitmap
	 * @param requestType the specific request type
	 * @param wValue the word feature selector value
	 * @param wIndex Zero or Interface or Endpoint index
	 * @param data a byte array for the request Data
	 * @return a Request object that is created for this submission
	 * @exception javax.usb.RequestException if something goes wrong submitting the request for this operation
	 */
	public Request classRequest( byte bmRequestType, byte requestType, 
								 short wValue, short wIndex, byte[] data ) throws RequestException
	{
		Request classRequest = getRequestFactory().
			                   createClassRequest( bmRequestType, requestType,
												   wValue, wIndex, data );

		getClassOpsImp().syncSubmit( classRequest );

		return classRequest;
	}

	//-------------------------------------------------------------------------
	// Protected methods
	//

	/** @return the ClassOpsImp object */
	protected ClassOpsImp getClassOpsImp() { return classOpsImp; }

	//-------------------------------------------------------------------------
	// Instance variables
	//

	private ClassOpsImp classOpsImp = null;
}
