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

/**
 * Abstract class for the UsbDeviceOperations expose USB device operations to the user
 * @author E. Michael Maximilien
 * @version 1.0.0
 */
public abstract class AbstractUsbOperations extends Object implements UsbOperations
{
    //-------------------------------------------------------------------------
    // Ctor(s)
    //

	/**
	 * 1 arg ctor
	 * @param usbDevice the UsbDevice that these operations will apply to	 
	 * @exception javax.usb.UsbException if the RequestFactory could not be found
	 */
	public AbstractUsbOperations( UsbDevice usbDevice ) throws UsbException
	{ 
		this.usbDevice = usbDevice; 
		requestFactory = UsbHostManager.getInstance().getUsbServices().getRequestFactory();
	}

    //-------------------------------------------------------------------------
    // Public methods
    //

	/** @return the UsbDevice associated with this UsbDeviceOperations */
	public UsbDevice getUsbDevice() { return usbDevice; }

    //-------------------------------------------------------------------------
    // Protected methods
    //

	/** @return the RequestFactory */
	protected RequestFactory getRequestFactory() { return requestFactory; }

    //-------------------------------------------------------------------------
    // Instance variables
    //

	protected UsbDevice usbDevice = null;
	protected RequestFactory requestFactory = null;
}
