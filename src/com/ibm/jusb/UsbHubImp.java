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
import javax.usb.util.*;

/**
 * Implementation of the UsbHub interface
 * @author E. Michael Maximilien
 * @author Dan Streetman
 * @version 0.0.1 (JDK 1.1.x)
 */
class UsbHubImp extends UsbDeviceAbstraction implements UsbHub
{
	//-------------------------------------------------------------------------
	// Ctor(s)
	//

	/**
	 * Creates a hub with the number of ports specified
	 * @param numberOfPorts the total number of ports for this hub
	 */
	UsbHubImp( int numberOfPorts ) { createUsbPorts( numberOfPorts ); }

	//-------------------------------------------------------------------------
	// Private/protected methods
	//

	/**
	 * Creates UsbPort objects
	 * @param n the number of UsbPort object to create
	 */
	protected void createUsbPorts( int n )
	{
		int newNumberOfPorts = UsbUtil.unsignedInt( getNumberOfPorts() ) + n;

		if ( newNumberOfPorts > USB_HUB_MAX_PORTS )
			newNumberOfPorts = USB_HUB_MAX_PORTS;

		if ( newNumberOfPorts < USB_HUB_MIN_PORTS )
			newNumberOfPorts = USB_HUB_MIN_PORTS;

		for (int i = UsbUtil.unsignedInt( getNumberOfPorts() ); i < newNumberOfPorts; i++)
			portList.addUsbInfo( new UsbPortImp( i+1, this ) );
	}

	//-------------------------------------------------------------------------
	// Package methods
	//

	/**
	 * Attach this device to this hub at the specified port.
	 * @param usbDevice the UsbDevice to attach
	 * @param portNumber the number of the port to attach the device to
	 * @exception javax.usb.UsbException if the device is being attached to 
	 * a non-existing port or that the port already is occupied
	 */
	void addDevice( UsbDevice usbDevice, byte portNumber ) throws UsbException
	{
		if ( UsbUtil.unsignedInt( portNumber ) > UsbUtil.unsignedInt( getNumberOfPorts() ) )
			throw new UsbException( USB_HUB_PORT_OUT_OF_RANGE );

		UsbPortImp usbPort =(UsbPortImp)getUsbPort( portNumber );

		usbPort.attachUsbDevice( usbDevice );

		attachedDeviceList.addUsbInfo( usbDevice );
	}

	/**
	 * Dettach this device to this hub at the specified port.
	 * @param usbDevice the UsbDevice to attach
	 * @param portNumber the number of the port the device is attached to
	 * @exception javax.usb.UsbException if the device is not already attached
	 * to the port it is being removed from
	 */
	void removeDevice( UsbDevice usbDevice, byte portNumber ) throws UsbException
	{
		if ( UsbUtil.unsignedInt( portNumber ) > UsbUtil.unsignedInt( getNumberOfPorts() ) )
			throw new UsbException( USB_HUB_PORT_OUT_OF_RANGE );

		UsbPortImp usbPort =(UsbPortImp)getUsbPort( portNumber );

		usbPort.detachUsbDevice( usbDevice );

		attachedDeviceList.removeUsbInfo( usbDevice );
	}

	//-------------------------------------------------------------------------
	// Public methods
	//

	/** @return true if this is a UsbHub and false otherwise */
	public boolean isUsbHub() { return true; }

	/** @return true if this is the root hub */
	public boolean isUsbRootHub() { return false;  }

	//-------------------------------------------------------------------------
	// Public interface methods
	//

	/** @return the number of ports for this hub */
	public byte getNumberOfPorts() { return (byte)portList.size(); }

	/** @return an iteration of UsbPort objects attached to this hub */
	public UsbInfoListIterator getUsbPorts() { return portList.usbInfoListIterator(); }

	/**
	 * @param number the number of the port to get
	 * @return the port with the specified number
	 * @throws UsbRuntimeException if the port number if not valid
	 */
	public UsbPort getUsbPort( byte number )
	{
		UsbInfoIterator iterator = getUsbPorts();

		while ( iterator.hasNext() ) {
			UsbPort usbPort = (UsbPort)iterator.nextUsbInfo();

			if (number == usbPort.getPortNumber())
				return usbPort;
		}

		throw new UsbRuntimeException( USB_HUB_PORT_OUT_OF_RANGE );
	}

	/** @return an iteration of devices currently attached to this hub */
	public UsbInfoListIterator getAttachedUsbDevices() { return attachedDeviceList.usbInfoListIterator(); }

	/**
	 * Returns a HubClassOperations object that can be used to submit
	 * standard USB hub class Request objects to this hub.
	 * @return a HubClassOperations object to use with this UsbHub
	 * @see javax.usb.Request
	 * @see javax.usb.os.UsbServices#getRequestFactory
	 */
	public HubClassOperations getHubClassOperations()
	{
		//<temp>
		throw new RuntimeException( "Not yet implemented!" );
		//</temp>
	}

	//-------------------------------------------------------------------------
	// Public accept method for the Visitor pattern
	//

	/**
	 * Visitor.accept method
	 * @param visitor the UsbInfoVisitor visiting this UsbInfo
	 */
	public void accept( UsbInfoVisitor visitor ) { visitor.visitUsbHub( this ); }

	//-------------------------------------------------------------------------
	// Instance variables
	//

	protected UsbInfoList portList = new DefaultUsbInfoList();
	protected UsbInfoList attachedDeviceList = new DefaultUsbInfoList();

	//-------------------------------------------------------------------------
	// Class constants
	//

	public static final int USB_HUB_MIN_PORTS = 0x01;
	public static final int USB_HUB_MAX_PORTS = 0xff; /* USB 1.1 spec table 11.8 - max of 255 ports */

	public static final String USB_HUB_NAME = "hub";

	private static final String USB_HUB_PORT_OUT_OF_RANGE = "No such port number on this hub";
	private static final String USB_DEVICE_NOT_ATTACHED = "The UsbDevice is not attached on the specified port";
}
