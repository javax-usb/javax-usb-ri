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
 * Concrete class implementing the UsbConfig interface
 * @author E. Michael Maximilien
 * @author Dan Streetman
 * @version 0.0.1 (JDK 1.1.x)
 */
class UsbConfigImp extends AbstractUsbInfo implements UsbConfig
{
	//-------------------------------------------------------------------------
	// Ctor(s)
	//

	/** 
	 * Creates a configuration for the UsbDevice specified
	 * @param usbDevice the UsbDevice that this config belongs to
	 */
	UsbConfigImp( UsbDevice usbDevice ) 
	{
		setUsbDevice( usbDevice );
	}

	//-------------------------------------------------------------------------
	// Public overridden interface methods
	//

	/** @return name of this UsbInfo object */
	public String getName() 
	{
		if( super.getName().equals( "" ) )
			setName( USB_CONFIG_NAME_STRING + getConfigNumber() );
        
		return super.getName();
	}

	//-------------------------------------------------------------------------
	// Public interface methods
	//

	/** @return this configuration's number */
	public byte getConfigNumber() { return getConfigDescriptor().getConfigValue(); }

	/** @return this configuration's number of UsbInterface */
	public byte getNumInterfaces() { return getConfigDescriptor().getNumInterfaces(); }

	/** @return the attributes code for this configuration */
	public byte getAttributes() { return getConfigDescriptor().getAttributes(); }

	/** @return the maximum power needed for this configuration */
	public byte getMaxPower() { return getConfigDescriptor().getMaxPower(); }

	/** @return if this UsbConfig is active */
	public boolean isActive() { return getUsbDevice().getActiveUsbConfigNumber() == getConfigNumber(); }

	/** @return an iteration of USB device interfaces for this configuration */
	public UsbInfoListIterator getUsbInterfaces() { return interfaces.usbInfoListIterator(); }

	/**
	 * @param the number of the interface to get
	 * @return a UsbInterface with the given number
	 */
	public UsbInterface getUsbInterface( byte number )
	{
		UsbInfoIterator iterator = getUsbInterfaces();

		while ( iterator.hasNext() ) {
			UsbInterface iface = (UsbInterface)iterator.nextUsbInfo();

			if (number == iface.getInterfaceNumber())
				return iface;
		}

		throw new UsbRuntimeException( "No UsbInterface with number " + UsbUtil.unsignedInt( number ) );
	}

	/**
	 * @param number the number of the UsbInterface to check.
	 * @return if this config contains the specified UsbInterface.
	 */
	public boolean containsUsbInterface( byte number )
	{
		UsbInfoIterator iterator = getUsbInterfaces();

		while ( iterator.hasNext() ) {
			UsbInterface iface = (UsbInterface)iterator.nextUsbInfo();

			if (number == iface.getInterfaceNumber())
				return true;
		}

		return false;
	}

	/** @return the UsbDevice that has this config */
	public UsbDevice getUsbDevice() { return usbDevice; }

	/** @return the config descriptor for this config */
	public ConfigDescriptor getConfigDescriptor() { return (ConfigDescriptor)getDescriptor(); }

	/** @return the String description of this config */
	public String getConfigString()
	{
		return ((UsbDeviceAbstraction)getUsbDevice()).getCachedString( getConfigDescriptor().getConfigIndex() );
	}

	//-------------------------------------------------------------------------
	// Public accept method for the Visitor pattern
	//

	/**
	 * Visitor.accept method
	 * @param visitor the UsbInfoVisitor visiting this UsbInfo
	 */
	public void accept( UsbInfoVisitor visitor ) { visitor.visitUsbConfig( this ); }

	//-------------------------------------------------------------------------
	// Protected and package methods
	//

	/** @param desc the new config descriptor */
	void setConfigDescriptor( ConfigDescriptor desc ) { setDescriptor( desc ); }

	/** @param setting the new active alternate setting */
	void setActiveAlternateSetting( UsbInterface setting )
	{
		synchronized ( interfaces ) {
			UsbInfoIterator iterator = getUsbInterfaces();

			while ( iterator.hasNext() ) {
				UsbInterface iface = (UsbInterface)iterator.nextUsbInfo();

				if (setting.getInterfaceNumber() == iface.getInterfaceNumber())
					interfaces.removeUsbInfo( iface );
			}

			interfaces.addUsbInfo( setting );
		}
	}

	//-------------------------------------------------------------------------
	// Private helper methods
	//

	/**
	 * Sets this config to its UsbDevice
	 * @param device the UsbDevice
	 */
	private void setUsbDevice( UsbDevice device )
	{
		usbDevice = device;

		((UsbDeviceAbstraction)usbDevice).addUsbConfig( this );
	}

	//-------------------------------------------------------------------------
	// Instance variables
	//

	private UsbInfoList interfaces = new DefaultUsbInfoList();

	private UsbDevice usbDevice = null;

	//-------------------------------------------------------------------------
	// Class constants
	//

	public static final String USB_CONFIG_NAME_STRING = "config";
}
