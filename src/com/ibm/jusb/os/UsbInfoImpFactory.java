package com.ibm.jusb.os;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the IBM Public License.
 *
 */

/**
 * Defines a factory iterface to create UsbInfo implementation objects
 * @author Dan Streetman
 * @version 0.0.1 (JDK 1.1.x)
 */
public interface UsbInfoImpFactory
{
	/**
	 * Create a UsbDeviceImp object.
	 * @param abstraction the UsbDeviceAbstraction to associate the implementation with.
	 */
	public UsbDeviceImp createUsbDeviceImp( UsbDeviceAbstraction abstraction );

	/**
	 * Create a UsbInterfaceImp object.
	 * @param abstraction the UsbInterfaceAbstraction to associate the implementation with.
	 * @param device the 'parent' UsbDeviceImp (created previously by this factory).
	 */
	public UsbInterfaceImp createUsbInterfaceImp( UsbInterfaceAbstraction abstraction, UsbDeviceImp device );

}
