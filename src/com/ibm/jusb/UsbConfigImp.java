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
 * UsbConfig implementation.
 * @author Dan Streetman
 * @author E. Michael Maximilien
 */
public class UsbConfigImp extends AbstractUsbInfo implements UsbConfig
{
	/**
	 * Constructor.
	 * <p>
	 * The UsbDeviceImp must be {@link #setUsbDeviceImp(UsbDeviceImp) set}
	 * before use.
	 */
	public UsbConfigImp() { }

	/**
	 * Constructor.
	 * @param device The parent device.
	 */
	public UsbConfigImp( UsbDeviceImp device ) { setUsbDeviceImp( device ); }

	//**************************************************************************
	// Public methods

	/** @return name of this UsbInfo object */
	public String getName() 
	{
		if( super.getName().equals( "" ) )
			setName( USB_CONFIG_NAME_STRING + getConfigNumber() );
        
		return super.getName();
	}

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
	 * @param The number of the interface to get
	 * @return A UsbInterface with the given number
	 */
	public UsbInterface getUsbInterface( byte number ) { return getUsbInterfaceImp(number); }

	/**
	 * @param The number of the interface to get.
	 * @return A UsbInterfaceImp with the given number.
	 */
	public UsbInterfaceImp getUsbInterfaceImp( byte number )
	{
		synchronized ( interfaces ) {
			for (int i=0; i<interfaces.size(); i++) {
				UsbInterfaceImp iface = (UsbInterfaceImp)interfaces.getUsbInfo(i);

				if (number == iface.getInterfaceNumber())
					return iface;
			}
		}

		throw new UsbRuntimeException( "No UsbInterface with number " + UsbUtil.unsignedInt( number ) );
	}

	/**
	 * @param number the number of the UsbInterface to check.
	 * @return if this config contains the specified UsbInterface.
	 */
	public boolean containsUsbInterface( byte number )
	{
		try { getUsbInterface(number); }
		catch ( UsbRuntimeException urE ) { return false; }

		return true;
	}

	/** @return The parent UsbDevice */
	public UsbDevice getUsbDevice() { return getUsbDeviceImp(); }

	/** @return The parent UsbDeviceImp */
	public UsbDeviceImp getUsbDeviceImp() { return usbDeviceImp; }

	/** @param device The parent UsbDeviceImp */
	public void setUsbDeviceImp(UsbDeviceImp device) { usbDeviceImp = device; }

	/** @return the config descriptor for this config */
	public ConfigDescriptor getConfigDescriptor() { return (ConfigDescriptor)getDescriptor(); }

	/** @return the String description of this config */
	public String getConfigString()
	{
		return getUsbDeviceImp().getCachedString( getConfigDescriptor().getConfigIndex() );
	}

	/**
	 * Visitor.accept method
	 * @param visitor the UsbInfoVisitor visiting this UsbInfo
	 */
	public void accept( UsbInfoVisitor visitor ) { visitor.visitUsbConfig( this ); }

	/** @param desc the new config descriptor */
	public void setConfigDescriptor( ConfigDescriptor desc ) { setDescriptor( desc ); }

	/**
	 * Replace a UsbInterface with a new alternate setting.
	 * <p>
	 * When an interface's setting changes, this config's list of interfaces
	 * must also change - since the {@link #getUsbInterface(byte) getUsbInterface}
	 * method always should return the active alternate setting of a particular interface.
	 * This method should be called to keep the alternate setting correct.
	 * @param setting The new active alternate setting.
	 */
	public void setActiveAlternateSetting( UsbInterfaceImp setting )
	{
		synchronized ( interfaces ) {
			for (int i=0; i<interfaces.size(); i++) {
				UsbInterfaceImp iface = (UsbInterfaceImp)interfaces.getUsbInfo(i);

				if (setting.getInterfaceNumber() == iface.getInterfaceNumber()) {
					interfaces.removeUsbInfo( iface );
					break;
				}
			}

			interfaces.addUsbInfo( setting );
		}
	}

	//**************************************************************************
	// Instance variables

	private UsbDeviceImp usbDeviceImp = null;

	private UsbInfoList interfaces = new DefaultUsbInfoList();

	//**************************************************************************
	// Class constants

	public static final String USB_CONFIG_NAME_STRING = "config";
}
