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
 * Interface for platform-specific UsbDevice implementations
 * @author Dan Streetman
 * @author E. Michael Maximilien
 * @version 1.0.0
 */
public interface UsbDeviceImp
{
	/**
	 * Returns a StandardOpsImp object that can be used to submit
	 * standard USB Request objects to this device.
	 * @return a StandardOpsImp object to use with this UsbDevice.
	 * @see javax.usb.Request
	 */
	public StandardOpsImp getStandardOpsImp();

	/**
	 * Returns a VendorOpsImp object that can be used to submit
	 * vendor USB Request objects to this device.
	 * @return a VendorOpsImp object to use with this UsbDevice.
	 * @see javax.usb.Request
	 */
	public VendorOpsImp getVendorOpsImp();

	/**
	 * Returns a ClassOpsImp object that can be used to submit
	 * Class USB Request objects to this device.
	 * @return a ClassOpsImp object to use with this UsbDevice.
	 * @see javax.usb.Request
	 */
	public ClassOpsImp getClassOpsImp();
}
