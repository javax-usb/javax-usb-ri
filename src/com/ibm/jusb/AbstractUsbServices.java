package com.ibm.jusb;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import javax.usb.*;
import javax.usb.event.*;

/**
 * Abstract implementation of UsbServices.
 * @author Dan Streetman
 * @author E. Michael Maximilien
 */
public abstract class AbstractUsbServices implements UsbServices
{
	/** @return The UsbRootHub. */
	public UsbRootHub getUsbRootHub() throws UsbException { return getUsbRootHubImp(); }

	/** @return The UsbRootHubImp. */
	public UsbRootHubImp getUsbRootHubImp() { return usbRootHubImp; }

    /** @param listener The listener to add. */
    public synchronized void addUsbServicesListener( UsbServicesListener listener )
	{
		usbServicesEventHelper.addEventListener( listener );
	}

    /** @param listener The listener to remove. */
    public synchronized void removeUsbServicesListener( UsbServicesListener l )
	{
		usbServicesEventHelper.removeEventListener( listener );
	}

	//**************************************************************************
	// Protected methods

	/** @return The UsbServicesEventHelper */
	protected UsbServicesEventHelper getUsbServicesEventHelper() { return usbServicesEventHelper; }

    //**************************************************************************
    // Instance variables

	private UsbRootHubImp usbRootHubImp = new VirtualUsbRootHubImp();
	private UsbServicesEventHelper usbServicesEventHelper = new UsbServicesEventHelper();

}
