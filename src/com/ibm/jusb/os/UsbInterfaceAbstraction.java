package com.ibm.jusb.os;

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

import com.ibm.jusb.util.*;

/**
 * Abstract implementation of the UsbInterface interface
 * @author E. Michael Maximilien
 * @author Dan Streetman
 * @version 0.0.1 (JDK 1.1.x)
 */
public class UsbInterfaceAbstraction extends AbstractUsbInfo implements UsbInterface
{
	//-------------------------------------------------------------------------
	// Ctor(s)
	//

	/**
	 * Default 1-arg ctor
	 * @param usbConfig the USB device config that this interface is attached to
	 */
	UsbInterfaceAbstraction( UsbConfig config ) 
	{
		setUsbConfig( config );
	}

	//-------------------------------------------------------------------------
	// Public overridden interface methods
	//

	/** @return name of this UsbInfo object */
	public String getName() 
	{
		if( super.getName().equals( "" ) ) {
			setName( USB_INTERFACE_NAME_STRING + getInterfaceNumber() );

			if (1 < getAlternateSettings().size())
				setName( super.getName() + USB_INTERFACE_SETTING_NAME_STRING + getAlternateSettingNumber() );
		}
        
		return super.getName();
	}

	//-------------------------------------------------------------------------
	// Public interface methods
	//

	/**
	 * Claim this interface.
	 * <p>
	 * This can only be called from an
	 * {@link #isActive() active} alternate setting.
	 * @throws javax.usb.UsbException if the interface could not be claimed.
	 * @throws javax.usb.NotActiveException if the interface setting is not active.
	 */
	public void claim() throws UsbException
	{
		checkSettingActive();

		getUsbInterfaceImp().claim();
	}

	/**
	 * Release this interface.
	 * <p>
	 * This can only be called from an
	 * {@link #isActive() active} alternate setting.
	 * @throws javax.usb.UsbException if the interface could not be released.
	 * @throws javax.usb.NotActiveException if the interface setting is not active.
	 */
	public void release() throws UsbException
	{
		checkSettingActive();

		getUsbInterfaceImp().release();
	}

	/** @return if this interface is claimed (in Java). */
	public boolean isClaimed()
	{
		checkSettingActive();

		return getUsbInterfaceImp().isClaimed();
	}

	/**
	 * If this interface setting is active.
	 * @return if this UsbInterface setting is active.
	 */
	public boolean isActive()
	{
		return getUsbConfig().isActive() &&
			getAlternateSettingNumber() == activeAlternateSettingNumber;
	}

	/** @return this interface's number */
	public byte getInterfaceNumber() { return getInterfaceDescriptor().getInterfaceNumber(); }

	/** @return this interface's class */
	public byte getInterfaceClass() { return getInterfaceDescriptor().getInterfaceClass(); }

	/** @return this interface's sub-class */
	public byte getInterfaceSubClass() { return getInterfaceDescriptor().getInterfaceSubClass(); }

	/** @return this interface's protocol */
	public byte getInterfaceProtocol() { return getInterfaceDescriptor().getInterfaceProtocol(); }

	/** @return this interface's number of endpoints */
	public byte getNumEndpoints() { return getInterfaceDescriptor().getNumEndpoints(); }

	/** @return an iteration of this interface's endpoints */
	public UsbInfoListIterator getUsbEndpoints() { return endpoints.usbInfoListIterator(); }

	/**
	 * @param index the index of the UsbEndpoint to get
	 * @return a UsbEndpoint with the specified address
	 */
	public UsbEndpoint getUsbEndpoint( byte address )
	{
		UsbInfoIterator iterator = getUsbEndpoints();

		while (iterator.hasNext()) {
			UsbEndpoint ep = (UsbEndpoint)iterator.nextUsbInfo();

			if (address == ep.getEndpointAddress())
				return ep;
		}

		throw new UsbRuntimeException( "No UsbEndpoint with address " + UsbUtil.unsignedInt( address ) );
	}

	/**
	 * @param address the address of the UsbEndpoint to check.
	 * @return if this UsbInterface contains the specified UsbEndpoint.
	 */
	public boolean containsUsbEndpoint( byte address )
	{
		UsbInfoIterator iterator = getUsbEndpoints();

		while (iterator.hasNext()) {
			UsbEndpoint ep = (UsbEndpoint)iterator.nextUsbInfo();

			if (address == ep.getEndpointAddress())
				return true;
		}

		return false;
	}

	/** @return the UsbConfig that this interface belongs to */
	public UsbConfig getUsbConfig() { return usbConfig; }

	/** @return the UsbDevice that this interface belongs to */
	public UsbDevice getUsbDevice() { return getUsbConfig().getUsbDevice(); }

	/** @return the number of alternate settings */
	public byte getNumAlternateSettings() { return (byte)getAlternateSettings().size(); }

	/**
	 * Get the number of this alternate setting
	 * @return this interface's alternate setting
	 */
	public byte getAlternateSettingNumber() { return getInterfaceDescriptor().getAlternateSetting(); }

	/**
	 * Get the number of the active alternate setting for this interface
	 * @return the active setting for this interface
	 * @throws javax.usb.NotActiveException if the interface is inactive.
	 */
	public byte getActiveAlternateSettingNumber()
	{
		checkActive();

		return activeAlternateSettingNumber;
	}

	/**
	 * Get the active alternate setting.
	 * @return the active setting UsbInterface object for this interface
	 * @throws javax.usb.NotActiveException if the interface (not setting) is inactive.
	 */
	public UsbInterface getActiveAlternateSetting()
	{
		/* Active check done in getActiveAlternateSettingNumber() */

		return getAlternateSetting( getActiveAlternateSettingNumber() );
	}

	/**
	 * Get the alternate setting with the specified number.
	 * @return the alternate setting with the specified number.
	 */
	public UsbInterface getAlternateSetting( byte number )
	{
		UsbInfoIterator iterator = getAlternateSettings();

		while ( iterator.hasNext() ) {
			UsbInterface iface = (UsbInterface)iterator.nextUsbInfo();

			if (number == iface.getAlternateSettingNumber())
				return iface;
		}

		throw new UsbRuntimeException( "No Alternate Setting with number " + UsbUtil.unsignedInt(number) );
	}

	/**
	 * @param number the number of the alternate setting to check.
	 * @return if the alternate setting exists.
	 */
	public boolean containsAlternateSetting( byte number )
	{
		UsbInfoIterator iterator = getAlternateSettings();

		while ( iterator.hasNext() ) {
			UsbInterface iface = (UsbInterface)iterator.nextUsbInfo();

			if (number == iface.getAlternateSettingNumber())
				return true;
		}

		return false;
	}

	/**
	 * Get an iteration of all alternate settings for this interface
	 * @return an iteration of this interface's other alternate settings
	 */
	public UsbInfoListIterator getAlternateSettings() { return alternateSettings.usbInfoListIterator(); }

	/** @return the bundle of UsbPipes contained in this interface setting. */
	public UsbPipeBundle getUsbPipes() { return usbPipeBundle; }

	/** @return the interface descriptor for this interface */
	public InterfaceDescriptor getInterfaceDescriptor() { return (InterfaceDescriptor)getDescriptor(); }

	/** @return the String description of this interface */
	public String getInterfaceString()
	{
		return ((UsbDeviceAbstraction)getUsbDevice()).getCachedString( getInterfaceDescriptor().getInterfaceIndex() );
	}

	//-------------------------------------------------------------------------
	// Public accept method for the Visitor pattern
	//

	/**
	 * Visitor.accept method
	 * @param visitor the UsbInfoVisitor visiting this UsbInfo
	 */
	public void accept( UsbInfoVisitor visitor ) { visitor.visitUsbInterface( this ); }

	//-------------------------------------------------------------------------
	// Protected and package methods
	//

	/** @return the associated UsbInterfaceImp */
	UsbInterfaceImp getUsbInterfaceImp() { return usbInterfaceImp; }

	/** @param the UsbInterfaceImp to use */
	void setUsbInterfaceImp( UsbInterfaceImp interfaceImp ) { usbInterfaceImp = interfaceImp; }

	/** @param the UsbPipeBundle to use */
	void setUsbPipes( UsbPipeBundle bundle ) { usbPipeBundle = bundle; }

	/** @param desc the new interface descriptor */
	void setInterfaceDescriptor( InterfaceDescriptor desc ) { setDescriptor( desc ); }

	/**
	 * Set the active alternate setting number for ALL UsbInterfaces
	 * on the AlternateSettings list
	 * @param number the number of the active alternate setting
	 * @throws javax.usb.UsbRuntimeException if the specified setting does not exist in this interface.
	 */
	void setActiveAlternateSettingNumber( byte number )
	{
		((UsbConfigImp)getUsbConfig()).setActiveAlternateSetting( getAlternateSetting( number ) );

		UsbInfoIterator settingIterator = getAlternateSettings();

		while ( settingIterator.hasNext() )
			((UsbInterfaceAbstraction)settingIterator.nextUsbInfo()).activeAlternateSettingNumber = number;

	}

	/** Connect to parent UsbConfig or active AlternateSetting */
	void connect()
	{
		UsbInterfaceAbstraction iface;

		try {
			iface = (UsbInterfaceAbstraction)getUsbConfig().getUsbInterface( getInterfaceNumber() );

			alternateSettings = iface.alternateSettings;
			activeAlternateSettingNumber = iface.activeAlternateSettingNumber;
		} catch ( UsbRuntimeException urE ) {
			alternateSettings = new DefaultUsbInfoList();
			activeAlternateSettingNumber = getAlternateSettingNumber();
			((UsbConfigImp)getUsbConfig()).setActiveAlternateSetting( this );
		}

		alternateSettings.addUsbInfo( this );
	}

	/** @param ep the endpoint to add */
	void addUsbEndpoint( UsbEndpoint ep )
	{
		endpoints.addUsbInfo( ep );
	}

	//-------------------------------------------------------------------------
	// Private methods
	//

	/**
	 * Set the config for this interface
	 * @param config the UsbConfig
	 */
	private void setUsbConfig( UsbConfig config )
	{
		usbConfig = config;
	}

	/** check if interface itself is active */
	private void checkActive()
	{
		if (!getUsbConfig().isActive())
			throw new NotActiveException( "Configuration is not active", UsbInfoConst.USB_INFO_ERR_INACTIVE_CONFIGURATION );
	}

	/** check if this specific interface setting is active */
	private void checkSettingActive()
	{
		/* If the interface (i.e. parent config) is not active, neither are any interface settings */
		checkActive();

		if (!isActive())
			throw new NotActiveException( "Interface setting is not active", UsbInfoConst.USB_INFO_ERR_INACTIVE_INTERFACE_SETTING );
	}

	//-------------------------------------------------------------------------
	// Instance variables
	//

	private UsbInterfaceImp usbInterfaceImp = null;

	private UsbInfoList endpoints = new DefaultUsbInfoList();

	private UsbInfoList alternateSettings = null;
	private byte activeAlternateSettingNumber = 0;

	private UsbConfig usbConfig = null;

	private UsbPipeBundle usbPipeBundle = new UsbPipeBundleImp();

	//-------------------------------------------------------------------------
	// Class constants
	//

	public static final String USB_INTERFACE_NAME_STRING = "interface";
	public static final String USB_INTERFACE_SETTING_NAME_STRING = " setting";

}
