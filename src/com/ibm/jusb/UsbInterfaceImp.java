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

import com.ibm.jusb.os.*;
import com.ibm.jusb.util.*;

/**
 * UsbInterface platform-independent implementation.
 * @author Dan Streetman
 * @author E. Michael Maximilien
 */
public class UsbInterfaceImp extends AbstractUsbInfo implements UsbInterface
{
	/**
	 * Constructor.
	 * <p>
	 * The UsbConfigImp must be
	 * {@link #setUsbConfigImp(UsbConfigImp) set}
	 * before this is used.  Also the UsbInterfaceOsImp must be
	 * {@link #setUsbInterfaceOsImp(UsbInterfaceOsImp) set} before this is used.
	 */
	public UsbInterfaceImp() { }

	/**
	 * Constructor.
	 * <p>
	 * The UsbInterfaceOsImp must be
	 * {@link #setUsbInterfaceOsImp(UsbInterfaceOsImp) set} before this is used.
	 * @param config The parent config.
	 */
	public UsbInterfaceImp( UsbConfigImp config ) { setUsbConfigImp(config); }

	/**
	 * Constructor.
	 * <p>
	 * The UsbConfigImp must be
	 * {@link #setUsbConfigImp(UsbConfigImp) set}
	 * before this is used.
	 * @param osImp The UsbInterfaceOsImp.
	 */
	public UsbInterfaceImp( UsbInterfaceOsImp osImp ) { setUsbInterfaceOsImp(osImp); }

	/**
	 * Constructor.
	 * @param config The parent config.
	 * @param osImp The UsbInterfaceOsImp.
	 */
	public UsbInterfaceImp( UsbConfigImp config, UsbInterfaceOsImp osImp )
	{
		setUsbConfigImp(config);
		setUsbInterfaceOsImp(osImp);
	}

	//**************************************************************************
	// Public methods

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

		getUsbInterfaceOsImp().claim();
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

		getUsbInterfaceOsImp().release();
	}

	/** @return if this interface is claimed (in Java). */
	public boolean isClaimed()
	{
		checkSettingActive();

		return getUsbInterfaceOsImp().isClaimed();
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
	 * @param index The index of the UsbEndpoint to get.
	 * @return The UsbEndpoint with the specified address.
	 */
	public UsbEndpoint getUsbEndpoint( byte address ) { return getUsbEndpointImp(address); }

	/**
	 * @param index The index of the UsbEndpoint to get.
	 * @return The UsbEndpointImp with the specified address.
	 */
	public UsbEndpointImp getUsbEndpointImp( byte address )
	{
		synchronized ( endpoints ) {
			for (int i=0; i<endpoints.size(); i++) {
				UsbEndpointImp ep = (UsbEndpointImp)endpoints.get(i);

				if (address == ep.getEndpointAddress())
					return ep;
			}
		}

		throw new UsbRuntimeException( "No UsbEndpoint with address 0x" + UsbUtil.toHexString( address ) );
	}

	/**
	 * @param address the address of the UsbEndpoint to check.
	 * @return if this UsbInterface contains the specified UsbEndpoint.
	 */
	public boolean containsUsbEndpoint( byte address )
	{
		try { getUsbEndpoint(address); }
		catch ( UsbRuntimeException urE ) { return false; }

		return true;
	}

	/** @return The parent config */
	public UsbConfig getUsbConfig() { return getUsbConfigImp(); }

	/** @return The parent config */
	public UsbConfigImp getUsbConfigImp() { return usbConfigImp; }

	/** @param config The parent config */
	public void setUsbConfigImp(UsbConfigImp config) { usbConfigImp = config; }

	/** @return the UsbDevice that this interface belongs to */
	public UsbDevice getUsbDevice() { return getUsbDeviceImp(); }

	/** @return the UsbDeviceImp that this interface belongs to */
	public UsbDeviceImp getUsbDeviceImp() { return getUsbConfigImp().getUsbDeviceImp(); }

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
		synchronized ( alternateSettings ) {
			for (int i=0; i<alternateSettings.size(); i++) {
				UsbInterfaceImp iface = (UsbInterfaceImp)alternateSettings.get(i);

				if (number == iface.getAlternateSettingNumber())
					return iface;
			}
		}

		throw new UsbRuntimeException( "No Alternate Setting with number " + UsbUtil.unsignedInt(number) );
	}

	/**
	 * @param number the number of the alternate setting to check.
	 * @return if the alternate setting exists.
	 */
	public boolean containsAlternateSetting( byte number )
	{
		try { getAlternateSetting(number); }
		catch ( UsbRuntimeException urE ) { return false; }

		return true;
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
		return getUsbDeviceImp().getCachedString( getInterfaceDescriptor().getInterfaceIndex() );
	}

	/**
	 * Visitor.accept method
	 * @param visitor the UsbInfoVisitor visiting this UsbInfo
	 */
	public void accept( UsbInfoVisitor visitor ) { visitor.visitUsbInterface( this ); }

	/** @return the associated UsbInterfaceOsImp */
	public UsbInterfaceOsImp getUsbInterfaceOsImp() { return usbInterfaceOsImp; }

	/** @param the UsbInterfaceOsImp to use */
	public void setUsbInterfaceOsImp( UsbInterfaceOsImp iface ) { usbInterfaceOsImp = iface; }

	/** @param the UsbPipeBundle to use */
	public void setUsbPipes( UsbPipeBundle bundle ) { usbPipeBundle = bundle; }

	/** @param desc the new interface descriptor */
	public void setInterfaceDescriptor( InterfaceDescriptor desc ) { setDescriptor( desc ); }

	/**
	 * Set the active alternate setting number for ALL UsbInterfaces
	 * on the AlternateSettings list
	 * @param number the number of the active alternate setting
	 * @throws javax.usb.UsbRuntimeException if the specified setting does not exist in this interface.
	 */
	public void setActiveAlternateSettingNumber( byte number )
	{
		getUsbConfigImp().setActiveAlternateSetting( getAlternateSetting( number ) );

		synchronized ( alternateSettings ) {
			for (int i=0; i<alternateSettings.size(); i++) {
				UsbInterfaceImp iface = (UsbInterfaceImp)alternateSettings.getUsbInfo(i);
				iface.activeAlternateSettingNumber = number;
			}
		}
	}

	/** Connect to parent UsbConfig or active AlternateSetting */
	void connect()
	{
//FIXME...?!?
/*
		UsbInterfaceImp iface;

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
*/	}

	/** @param ep the endpoint to add */
	public void addUsbEndpointImp( UsbEndpointImp ep ) { endpoints.addUsbInfo( ep ); }

	//-------------------------------------------------------------------------
	// Private methods
	//

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

	private UsbConfigImp usbConfigImp = null;
	private UsbInterfaceImp usbInterfaceImp = null;

	private UsbInfoList endpoints = new DefaultUsbInfoList();

	private UsbInfoList alternateSettings = null;
	private byte activeAlternateSettingNumber = 0;

	private UsbPipeBundle usbPipeBundle = new UsbPipeBundleImp();

	//-------------------------------------------------------------------------
	// Class constants
	//

	public static final String USB_INTERFACE_NAME_STRING = "interface";
	public static final String USB_INTERFACE_SETTING_NAME_STRING = " setting";

}
