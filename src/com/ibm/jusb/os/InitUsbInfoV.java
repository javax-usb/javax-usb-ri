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
 * Visitor class to initialize a UsbInfo object
 * <p>
 * This is not a good visitor; it kind of 'cheats'.
 * After visiting a UsbInfo, only certain methods should be
 * used; e.g. trying to setUsbRootHubInfo() after visiting a UsbInterface
 * will result in a ClassCastException.
 * @author Dan Streetman
 * @author E. Michael Maximilien
 * @version 0.0.1 (JDK 1.1.x)
 */
public class InitUsbInfoV extends DefaultUsbInfoV
{
    /** Default ctor */
    public InitUsbInfoV() {}

    //-------------------------------------------------------------------------
    // Public methods
    //

    /**
     * Set information to the UsbRootHub object passed
     * NOTE: this method should be called after visiting...
     * @param numberOfPorts the total number of ports in this UsbRootHub
     */
    public void setUsbRootHubInfo( int numberOfPorts )
    {
		UsbRootHubImp usbRootHubImp = (UsbRootHubImp)usbInfo;

        usbRootHubImp.createUsbPorts( numberOfPorts );
    }

    /**
     * @param descriptor the device descriptor to use
     * @param speedString the speed of the device
     */
    public void setUsbDeviceInfo( DeviceDescriptor descriptor, String speedString )
    {
		UsbDeviceAbstraction usbDeviceAbstraction = (UsbDeviceAbstraction)usbInfo;

		usbDeviceAbstraction.setDeviceDescriptor( descriptor );
        usbDeviceAbstraction.setSpeedString( speedString );
    }

	/**
	 * @param index the index of the string descriptor
	 * @param descriptor the string descriptor
	 */
	public void setUsbDeviceStringDescriptor( byte index, StringDescriptor descriptor )
	{
		UsbDeviceAbstraction usbDeviceAbstraction = (UsbDeviceAbstraction)usbInfo;

		usbDeviceAbstraction.setStringDescriptor( index, descriptor );
	}

	/**
	 * @param num the number of the active configuration
	 */
	public void setActiveUsbConfigNumber( byte num )
	{
		UsbDeviceAbstraction usbDeviceAbstraction = (UsbDeviceAbstraction)usbInfo;

		usbDeviceAbstraction.setActiveUsbConfigNumber( num );
	}

    /*
	 * @param descriptor the config descriptor to use
     */
    public void setUsbConfigInfo( ConfigDescriptor descriptor )
    {
		UsbConfigImp usbConfigImp = (UsbConfigImp)usbInfo;

		usbConfigImp.setConfigDescriptor( descriptor );
    }

    /**
	 * @param descriptor the interface descriptor to use
     */
    public void setUsbInterfaceInfo( InterfaceDescriptor descriptor )
    {
		UsbInterfaceAbstraction usbInterfaceAbstraction = (UsbInterfaceAbstraction)usbInfo;

		usbInterfaceAbstraction.setInterfaceDescriptor( descriptor );

		usbInterfaceAbstraction.connect();
    }

    /**
	 * @param descriptor the endpoint descriptor to use
     */
    public void setUsbEndpointInfo( EndpointDescriptor descriptor )
    {
		UsbEndpointImp usbEndpointImp = (UsbEndpointImp)usbInfo;

		usbEndpointImp.setEndpointDescriptor( descriptor );
    }

	/**
	 * @param usbHub the parent UsbHub
	 * @param port the parent port index the UsbDevice is attached to
	 */
	public void connect( UsbHub usbHub, byte port ) throws UsbException
	{
		((UsbDeviceAbstraction)usbInfo).connect( (UsbHubImp)usbHub, port );
	}

	/**
	 * Disconnect UsbDevice
	 * @exception javax.usb.UsbException if there was a problem removing the device
	 */
	public void disconnect() throws UsbException
	{
		((UsbDeviceAbstraction)usbInfo).disconnect();
	}

    //-------------------------------------------------------------------------
    // Public visitXyz visitor methods
    //

    /**
     * Default method to visit a UsbHub
     * @param usbInfo the UsbInfo object
     */
    public void visitUsbHub( UsbInfo usbInfo ) { this.usbInfo = usbInfo; }

    /**
     * Default method to visit a UsbDevice
     * @param usbInfo the UsbInfo object
     */
    public void visitUsbDevice( UsbInfo usbInfo ) { this.usbInfo = usbInfo; }

    /**
     * Default method to visit a UsbConfig
     * @param usbInfo the UsbInfo object
     */
    public void visitUsbConfig( UsbInfo usbInfo ) { this.usbInfo = usbInfo; }

    /**
     * Default method to visit a UsbInterface
     * @param usbInfo the UsbInfo object
     */
    public void visitUsbInterface( UsbInfo usbInfo ) { this.usbInfo = usbInfo; }

    /**
     * Default method to visit a UsbEndpoint
     * @param usbInfo the UsbInfo object
     */
    public void visitUsbEndpoint( UsbInfo usbInfo ) { this.usbInfo = usbInfo; }

    //-------------------------------------------------------------------------
    // Instance variables
    //

	private UsbInfo usbInfo = null;
}
