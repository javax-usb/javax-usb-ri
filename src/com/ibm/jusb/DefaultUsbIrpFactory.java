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

/**
 * Defines default Usb IRP (I/O Request Packet) Factory
 * @author Dan Streetman
 * @version 0.0.1 (JDK 1.1.x)
 */
public class DefaultUsbIrpFactory implements UsbIrpFactory
{
	//*************************************************************************
	// Public methods

	/**
	 * Create a UsbIrp
	 * @return a UsbIrp
	 */
	public UsbIrp createUsbIrp()
	{
		return (UsbIrp)AbstractUsbServices.getInstance().getHelper().getUsbIrpImpFactory().take();
	}

	/**
	 * Create a UsbCompositeIrp
	 * @return a UsbCompositeIrp
	 */
	public UsbCompositeIrp createUsbCompositeIrp()
	{
		return (UsbCompositeIrp)AbstractUsbServices.getInstance().getHelper().getUsbCompositeIrpImpFactory().take();
	}

}
