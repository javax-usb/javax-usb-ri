package com.ibm.jusb.os.linux;

/*
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import javax.usb.*;

import com.ibm.jusb.*;

/**
 * Linux implementation of the UsbDevice interface
 * @author Dan Streetman
 * @author E. Michael Maximilien
 * @version 1.0.0
 */
class LinuxDeviceImp implements UsbDeviceImp
{
	//-------------------------------------------------------------------------
	// Ctor
	//

	/**
	 * Constructor
	 * @param abstraction the UsbDeviceAbstraction to use.
	 */
	public LinuxDeviceImp( UsbDeviceAbstraction abstraction ) { usbDeviceAbstraction = abstraction; }

	//-------------------------------------------------------------------------
	// UsbDeviceImp methods
	//

	/**
	 * Get the UsbDeviceAbstraction associated with this implementation.
	 * @return the associated UsbDeviceAbstraction.
	 */
	public UsbDeviceAbstraction getUsbDeviceAbstraction() { return usbDeviceAbstraction; }

	/**
	 * Returns a StandardOpsImp object that can be used to submit
	 * standard USB Request objects to this device.
	 * @return a StandardOpsImp object to use with this UsbDevice.
	 * @see javax.usb.Request
	 */
	public StandardOpsImp getStandardOpsImp()
	{
		if( linuxStandardOpsImp == null )
			linuxStandardOpsImp = new LinuxStandardOpsImp( this );

		return linuxStandardOpsImp;
	}

	/**
	 * Returns a VendorOpsImp object that can be used to submit
	 * vendor USB Request objects to this device.
	 * @return a VendorOpsImp object to use with this UsbDevice.
	 * @see javax.usb.Request
	 */
	public VendorOpsImp getVendorOpsImp()
	{
		if( linuxVendorOpsImp == null )
			linuxVendorOpsImp = new LinuxVendorOpsImp( this );

		return linuxVendorOpsImp;
	}

	/**
	 * Returns a ClassOpsImp object that can be used to submit
	 * Class USB Request objects to this device.
	 * @return a ClassOpsImp object to use with this UsbDevice.
	 * @see javax.usb.Request
	 */
	public ClassOpsImp getClassOpsImp()
	{
		if( linuxClassOpsImp == null )
			linuxClassOpsImp = new LinuxClassOpsImp( this );

		return linuxClassOpsImp;
	}

	//-------------------------------------------------------------------------
	// Package methods
	//

	/** @return the LinuxDeviceProxy for this UsbDeviceImp */
	LinuxDeviceProxy getLinuxDeviceProxy()
	{
		if ( null == linuxDeviceProxy )
			linuxDeviceProxy = new LinuxDeviceProxy( getUsbDeviceAbstraction() );

		return linuxDeviceProxy;
	}

	//-------------------------------------------------------------------------
	// Instance variables
	//

	private UsbDeviceAbstraction usbDeviceAbstraction = null;

	private LinuxDeviceProxy linuxDeviceProxy = null;

	private LinuxStandardOpsImp linuxStandardOpsImp = null;
	private LinuxVendorOpsImp linuxVendorOpsImp = null;
	private LinuxClassOpsImp linuxClassOpsImp = null;
}
