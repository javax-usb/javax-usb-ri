package com.ibm.jusb.os;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the IBM Public License.
 *
 */

import javax.usb.*;

/**
 * Defines a factory class to create allow all UsbInfo object creation
 * @author E. Michael Maximilien
 * @author Dan Streetman
 * @version 0.0.1 (JDK 1.1.x)
 */
public interface UsbInfoFactory
{
	//-------------------------------------------------------------------------
	// Public methods
	//

	/** Reset the count of devices */
	public void resetCount();

	/** @return a UsbRootHub object that is intended to be the root USB hub */
	public UsbRootHub createUsbRootHub();

	/**
	 * @return a UsbRootHub object that is intended to be the root USB hub
	 * @param numberOfPorts the number of USB ports in this hub
	 */
	public UsbRootHub createUsbRootHub( int numberOfPorts );

	/**
	 * The UsbHub created must be connected to its parent
	 * @return a UsbHub object
	 * @param numberOfPorts the number of USB ports in this hub  
	 */
	public UsbHub createUsbHub( int numberOfPorts );

	/**
	 * @return a UsbHub object
	 * @param usbHub the parent USB hub for this hub
	 * @param portNumber the port number for this hub
	 * @param numberOfPorts the number of USB ports in this hub  
	 * @exception if the portNumber and/or usbHub are not valid
	 */
	public UsbHub createUsbHub( UsbHub usbHub, byte portNumber, int numberOfPorts ) throws UsbException;

	/**
	 * The UsbDevice created must be connected to its parent
	 * @return an UsbDevice object 
	 */
	public UsbDevice createUsbDevice( );

	/**
	 * @return an UsbDevice object 
	 * @param usbHub the UsbHub that this device is attached to
	 * @param portNumber the port number that this device at
	 * @exception if the portNumber and/or usbHub are not valid  
	 */
	public UsbDevice createUsbDevice( UsbHub usbHub, byte portNumber ) throws UsbException;

	/**
	 * @return an UsbConfig object 
	 * @param usbDevice
	 */
	public UsbConfig createUsbConfig( UsbDevice usbDevice );

	/**
	 * @return an UsbInterface object 
	 * @param usbConfig 
	 */
	public UsbInterface createUsbInterface( UsbConfig usbConfig );

	/**
	 * @return an UsbEndpoint object
	 * @param usbInterface 
	 */
	public UsbEndpoint createUsbEndpoint( UsbInterface usbInterface );

}

