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
 * Defines a default factory class to create allow all UsbInfo object creation
 * @author E. Michael Maximilien
 * @author Dan Streetman
 * @version 0.0.1 (JDK 1.1.x)
 */
public class DefaultUsbInfoFactory extends Object implements UsbInfoFactory
{
	//-------------------------------------------------------------------------
	// Ctor
	//

	/** Default ctor */
	public DefaultUsbInfoFactory() {}

	//-------------------------------------------------------------------------
	// Public interface methods
	//

	/** Resets the numbering scheme for UsbHub and UsbDevice objects */
	public void resetCount()
	{
		usbRootHubCount = 0;
		usbHubCount = 0;
		usbDeviceCount = 0;
	}

	/** @return a UsbRootHub object that is intended to be the root USB hub */
	public UsbRootHub createUsbRootHub()
	{    
		UsbRootHubImp usbRootHubImp = new UsbRootHubImp();

		usbRootHubImp.setName( UsbRootHubImp.USB_ROOT_HUB_NAME + usbRootHubCount++ );
		usbRootHubImp.setUsbDeviceImp( getUsbInfoImpFactory().createUsbDeviceImp( usbRootHubImp ) );

		return usbRootHubImp;
	}

	/**
	 * @return a UsbRootHub object that is intended to be the root USB hub
	 * @param numberOfPorts the number of USB ports in this hub
	 */
	public UsbRootHub createUsbRootHub( int numberOfPorts )
	{   
		UsbRootHubImp usbRootHubImp = new UsbRootHubImp( numberOfPorts );

		usbRootHubImp.setName( UsbRootHubImp.USB_ROOT_HUB_NAME + usbRootHubCount++ );
		usbRootHubImp.setUsbDeviceImp( getUsbInfoImpFactory().createUsbDeviceImp( usbRootHubImp ) );

		return usbRootHubImp;
	}

	/**
	 * @return a UsbHub object
	 * @param numberOfPorts the number of USB ports in this hub  
	 */
	public UsbHub createUsbHub( int numberOfPorts )
	{    
		UsbHubImp usbHubImp = new UsbHubImp( numberOfPorts );

		usbHubImp.setName( UsbHubImp.USB_HUB_NAME + usbHubCount++ );
		usbHubImp.setUsbDeviceImp( getUsbInfoImpFactory().createUsbDeviceImp( usbHubImp ) );

		return usbHubImp;
	}

	/**
	 * @return a UsbHub object
	 * @param usbHub the parent USB hub for this hub
	 * @param portNumber the port number for this hub
	 * @param numberOfPorts the number of USB ports in this hub  
	 * @exception if the portNumber and/or usbHub are not valid
	 */
	public UsbHub createUsbHub( UsbHub usbHub, byte portNumber, int numberOfPorts ) throws UsbException
	{    
		UsbHubImp usbHubImp = new UsbHubImp( numberOfPorts );

		usbHubImp.setName( UsbHubImp.USB_HUB_NAME + usbHubCount++ );
		usbHubImp.setUsbDeviceImp( getUsbInfoImpFactory().createUsbDeviceImp( usbHubImp ) );
		usbHubImp.connect( ((UsbHubImp)usbHub), portNumber );

		return usbHubImp;
	}

	/**
	 * @return an UsbDevice object 
	 */
	public UsbDevice createUsbDevice( )
	{
		UsbDeviceAbstraction abstraction = new UsbDeviceAbstraction( );

		abstraction.setName( UsbDeviceAbstraction.USB_DEVICE_NAME + usbDeviceCount++ );
		abstraction.setUsbDeviceImp( getUsbInfoImpFactory().createUsbDeviceImp( abstraction ) );

		return abstraction;
	}

	/**
	 * @return an UsbDevice object 
	 * @param usbHub the UsbHub that this device is attached to
	 * @param portNumber the port number that this device at
	 * @exception if the portNumber and/or usbHub are not valid  
	 */
	public UsbDevice createUsbDevice( UsbHub usbHub, byte portNumber ) throws UsbException
	{
		UsbDeviceAbstraction abstraction = new UsbDeviceAbstraction( );

		abstraction.setName( UsbDeviceAbstraction.USB_DEVICE_NAME + usbDeviceCount++ );
		abstraction.setUsbDeviceImp( getUsbInfoImpFactory().createUsbDeviceImp( abstraction ) );
		abstraction.connect( ((UsbHubImp)usbHub), portNumber );

		return abstraction;
	}

	/**
	 * @return an UsbConfig object 
	 * @param usbDevice
	 */
	public UsbConfig createUsbConfig( UsbDevice usbDevice )
	{
		return new UsbConfigImp( usbDevice );
	}

	/**
	 * @return an UsbInterface object 
	 * @param usbConfig 
	 */
	public UsbInterface createUsbInterface( UsbConfig usbConfig )
	{
		UsbInterfaceAbstraction abstraction = new UsbInterfaceAbstraction( usbConfig );
		UsbDeviceImp device = ((UsbDeviceAbstraction)usbConfig.getUsbDevice()).getUsbDeviceImp();

		UsbInterfaceImp usbInterfaceImp = getUsbInfoImpFactory().createUsbInterfaceImp( abstraction, device );
		abstraction.setUsbInterfaceImp( usbInterfaceImp );

		return abstraction;
	}

	/**
	 * @return an UsbEndpoint object
	 * @param usbInterface 
	 */
	public UsbEndpoint createUsbEndpoint( UsbInterface usbInterface )
	{
		return new UsbEndpointImp( usbInterface );
	}

	//-------------------------------------------------------------------------
	// Private methods
	//

	/** @return a UsbInfoImpFactory instance */
	private UsbInfoImpFactory getUsbInfoImpFactory()
	{
		return AbstractUsbServices.getInstance().getHelper().getUsbInfoImpFactory();
	}

	//-------------------------------------------------------------------------
	// Class variables
	//

	public int usbRootHubCount = 0;
	public int usbHubCount = 0;
	public int usbDeviceCount = 0;

}
