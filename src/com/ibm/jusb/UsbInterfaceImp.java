package com.ibm.jusb;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import java.io.*;
import java.util.*;

import javax.usb.*;
import javax.usb.util.*;

import com.ibm.jusb.os.*;
import com.ibm.jusb.util.*;

/**
 * UsbInterface platform-independent implementation.
 * <p>
 * This must be set up before use.
 * <ul>
 * <li>The UsbConfigImp must be set in the constructor or by its {@link #setUsbConfigImp(UsbConfigImp) setter}.</li>
 * <li>The InterfaceDescriptor must be set either in the constructor or by its {@link #setInterfaceDescriptor(InterfaceDescriptor) setter}.</li>
 * <li>The UsbInterfaceOsImp may optionally be set either in the constructor or by its
 *     {@link #setUsbInterfaceOsImp(UsbInterfaceOsImp) setter}.
 *     If the platform implementation does not provide any interface claiming ability,
 *     it may leave the default setting which manages in-Java claims and releases.</li>
 * <li>If the active alternate setting number is not the first added to the parent UsbConfigImp either
 *     {@link com.ibm.jusb.UsbConfigImp#addUsbInterfaceImp(UsbInterfaceImp) directly} or by
 *     {@link #setUsbConfigImp(UsbConfigImp) setUsbConfigImp}, it must be
 *     {@link #setActiveAlternateSettingNumber(byte) set} after creating the active alternate setting.</li>
 * <li>All UsbEndpointImps must be {@link #addUsbEndpointImp(UsbEndpointImp) added}.</li>
 * </ul>
 * <p>
 * When changing the active alternate setting, call the {@link #setActiveAlternateSettingNumber(byte) setActiveAlternateSettingNumber} method.
 * This will update the parent config's active interface setting map.
 * @author Dan Streetman
 */
public class UsbInterfaceImp implements UsbInterface
{
	/**
	 * Constructor.
	 * @param config The parent config.  If this is not null, the InterfaceDescriptor <i>cannot</i> be null.
	 * @param desc This interface's descriptor.
	 */
	public UsbInterfaceImp( UsbConfigImp config, InterfaceDescriptor desc )
	{
		setInterfaceDescriptor(desc);
		setUsbConfigImp(config);
	}

	/**
	 * Constructor.
	 * @param config The parent config.  If this is not null, the InterfaceDescriptor <i>cannot</i> be null.
	 * @param desc This interface's descriptor.
	 * @param osImp The UsbInterfaceOsImp.  Do not set this to null, use the other constructor.
	 */
	public UsbInterfaceImp( UsbConfigImp config, InterfaceDescriptor desc, UsbInterfaceOsImp osImp )
	{
		setInterfaceDescriptor(desc);
		setUsbConfigImp(config);
		setUsbInterfaceOsImp(osImp);
	}

	//**************************************************************************
	// Public methods

	/**
	 * Claim this interface.
	 * <p>
	 * This can only be called from an
	 * {@link #isActive() active} alternate setting.
	 * <p>
	 * All alternate settings will be claimed.
	 * @exception UsbException if the interface could not be claimed.
	 * @exception NotActiveException if the interface setting is not active.
	 */
		public void claim() throws UsbException,NotActiveException
	{
		checkSettingActive();

		getUsbInterfaceOsImp().claim();

		setClaimed(true);
	}

	/**
	 * Release this interface.
	 * <p>
	 * This can only be called from an
	 * {@link #isActive() active} alternate setting.
	 * @exception UsbException if the interface could not be released.
	 * @exception NotActiveException if the interface setting is not active.
	 */
	public void release() throws UsbException,NotActiveException
	{
		checkSettingActive();

		for (int i=0; i<endpoints.size(); i++)
			if (((UsbEndpoint)endpoints.get(i)).getUsbPipe().isOpen())
				throw new UsbException("Cannot release UsbInterface with any open UsbPipe");

		getUsbInterfaceOsImp().release();

		setClaimed(false);
	}

	/** @return if this interface is claimed. */
	public boolean isClaimed()
	{
		try { checkSettingActive(); }
		catch ( NotActiveException naE ) { return false; }

		if (claimed)
			return true;
		else
			return getUsbInterfaceOsImp().isClaimed();
	}

	/**
	 * If this interface setting is active.
	 * @return if this UsbInterface setting is active.
	 */
	public boolean isActive()
	{
		try {
			return getInterfaceDescriptor().bAlternateSetting() == getActiveSettingNumber();
		} catch ( NotActiveException naE ) {
			return false;
		}
	}

	/** @return The endpoints. */
	public List getUsbEndpoints() { return endpoints; }

	/**
	 * @param index The index of the UsbEndpoint to get.
	 * @return The UsbEndpoint with the specified address.
	 */
	public UsbEndpoint getUsbEndpoint( byte address ) { return getUsbEndpointImp(address); }

	/**
	 * @param index The index of the UsbEndpoint to get.
	 * @return The UsbEndpointImp with the specified address, or null.
	 */
	public UsbEndpointImp getUsbEndpointImp( byte address )
	{
		synchronized ( endpoints ) {
			for (int i=0; i<endpoints.size(); i++) {
				UsbEndpointImp ep = (UsbEndpointImp)endpoints.get(i);

				if (address == ep.getEndpointDescriptor().bEndpointAddress())
					return ep;
			}
		}

		return null;
	}

	/**
	 * @param address the address of the UsbEndpoint to check.
	 * @return if this UsbInterface contains the specified UsbEndpoint.
	 */
	public boolean containsUsbEndpoint( byte address )
	{
		if (null != getUsbEndpoint(address))
			return true;
		else
			return false;
	}

	/** @return The parent config */
	public UsbConfig getUsbConfig() { return getUsbConfigImp(); }

	/** @return The parent config */
	public UsbConfigImp getUsbConfigImp() { return usbConfigImp; }

	/**
	 * Set the UsbConfigImp.
	 * <p>
	 * This also adds this to the parent UsbConfigImp.  The
	 * InterfaceDescriptor <i>must</i> be {@link #setInterfaceDescriptor(InterfaceDescriptor) set}
	 * before calling this.
	 * @param config The parent config
	 */
	public void setUsbConfigImp(UsbConfigImp config)
	{
		usbConfigImp = config;

		if (null != config)
			config.addUsbInterfaceImp(this);
	}

	/** @return the number of alternate settings */
	public int getNumSettings() { return getSettings().size(); }

	/**
	 * Get the number of the active alternate setting for this interface
	 * @return the active setting for this interface
	 * @exception NotActiveException if the interface is inactive.
	 */
	public byte getActiveSettingNumber() throws NotActiveException
	{
		checkActive();

		return ((UsbInterfaceImp)getUsbConfigImp().getUsbInterfaceSettingList(getInterfaceDescriptor().bInterfaceNumber()).get(0)).getInterfaceDescriptor().bAlternateSetting();
	}

	/**
	 * Get the active alternate setting.
	 * @return the active setting UsbInterface object for this interface
	 * @throws javax.usb.NotActiveException if the interface (not setting) is inactive.
	 */
	public UsbInterface getActiveSetting() throws NotActiveException { return getActiveSettingImp(); }

	/**
	 * Get the active alternate setting.
	 * @return the active setting UsbInterface object for this interface
	 * @throws javax.usb.NotActiveException if the interface (not setting) is inactive.
	 */
	public UsbInterfaceImp getActiveSettingImp() throws NotActiveException
	{
		/* Active check done in getActiveSettingNumber() */

		return getSettingImp( getActiveSettingNumber() );
	}

	/**
	 * Get the alternate setting with the specified number.
	 * @return the alternate setting with the specified number.
	 */
	public UsbInterface getSetting( byte number ) { return getSettingImp(number); }

	/**
	 * Get the alternate setting with the specified number.
	 * @return the alternate setting with the specified number, or null.
	 */
	public UsbInterfaceImp getSettingImp( byte number )
	{
		List list = getUsbConfigImp().getUsbInterfaceSettingList(getInterfaceDescriptor().bInterfaceNumber());

		synchronized (list) {
			for (int i=0; i<list.size(); i++) {
				UsbInterfaceImp setting = (UsbInterfaceImp)list.get(i);

				if (number == setting.getInterfaceDescriptor().bAlternateSetting())
					return setting;
			}
		}

		return null;
	}

	/**
	 * @param number the number of the alternate setting to check.
	 * @return if the alternate setting exists.
	 */
	public boolean containsSetting( byte number )
	{
		if (null != getSetting(number))
			return true;
		else
			return false;
	}

	/**
	 * Get all alternate settings for this interface.
	 * @return All of this interface's alternate settings (including this setting).
	 */
	public List getSettings()
	{
		return Collections.unmodifiableList( getUsbConfigImp().getUsbInterfaceSettingList(getInterfaceDescriptor().bInterfaceNumber()) );
	}

	/** @return the interface descriptor for this interface */
	public InterfaceDescriptor getInterfaceDescriptor() { return interfaceDescriptor; }

	/** @return the String description of this interface */
	public String getInterfaceString() throws UsbException,UnsupportedEncodingException
	{
		return getUsbConfigImp().getUsbDeviceImp().getString( getInterfaceDescriptor().iInterface() );
	}

	/** @return the associated UsbInterfaceOsImp */
	public UsbInterfaceOsImp getUsbInterfaceOsImp() { return usbInterfaceOsImp; }

	/** @param the UsbInterfaceOsImp to use */
	public void setUsbInterfaceOsImp( UsbInterfaceOsImp iface ) { usbInterfaceOsImp = iface; }

	/** @param desc the new interface descriptor */
	public void setInterfaceDescriptor( InterfaceDescriptor desc ) { interfaceDescriptor = desc; }

	/**
	 * Set the active alternate setting number for ALL UsbInterfaces
	 * on the AlternateSettings list
	 * @param number The number of the active alternate setting
	 * @throws IllegalArgumentException If the specified setting does not exist in this interface.
	 */
	public void setActiveSettingNumber( byte number ) throws IllegalArgumentException
	{
		getUsbConfigImp().setActiveUsbInterfaceImpSetting(getSettingImp(number));
	}

	/** @param ep the endpoint to add */
	public void addUsbEndpointImp( UsbEndpointImp ep )
	{
		if (!endpoints.contains(ep))
			endpoints.add( ep );
	}

	/**
	 * Compare this to an Object.
	 * @param object The Object to compare to.
	 * @return If this is equal to the Object.
	 */
	public boolean equals(Object object)
	{
		if (null == object)
			return false;

		UsbInterfaceImp iface = null;

		try { iface = (UsbInterfaceImp)object; }
		catch ( ClassCastException ccE ) { return false; }

		if (!getInterfaceDescriptor().equals(iface.getInterfaceDescriptor()))
			return false;

		List eps = getUsbEndpoints();

		for (int i=0; i<eps.size(); i++) {
			UsbEndpointImp usbEndpointImp = (UsbEndpointImp)eps.get(i);
			byte epAddress = usbEndpointImp.getEndpointDescriptor().bEndpointAddress();
			if (!iface.containsUsbEndpoint(epAddress))
				return false;
			else if (!usbEndpointImp.equals(iface.getUsbEndpoint(epAddress)))
				return false;
		}

		return false;
	}

	//**************************************************************************
	// Protected methods

	/**
	 * If this UsbInterface is claimed in Java.
	 * <p>
	 * This should be used by UsbPipeImps to verify that the open() method can be called.
	 * @return If this is claimed in Java.
	 */
	protected boolean isJavaClaimed() { return claimed; }

	//**************************************************************************
	// Private methods

	/** check if interface itself is active */
	private void checkActive() throws NotActiveException
	{
		if (!getUsbConfig().isActive())
			throw new NotActiveException( "Configuration is not active" );
	}

	/** check if this specific interface setting is active */
	private void checkSettingActive() throws NotActiveException
	{
		/* If the interface (i.e. parent config) is not active, neither are any interface settings */
		checkActive();

		if (!isActive())
			throw new NotActiveException( "Interface setting is not active" );
	}

	/**
	 * Set all alternate settings' claimed state.
	 * @param c If this interface is claimed or not.
	 */
	private void setClaimed(boolean c)
	{
		List list = getUsbConfigImp().getUsbInterfaceSettingList(getInterfaceDescriptor().bInterfaceNumber());
		for (int i=0; i<list.size(); i++)
			((UsbInterfaceImp)list.get(i)).claimed = c;
	}

	//**************************************************************************
	// Instance variables

	private UsbConfigImp usbConfigImp = null;
	private UsbInterfaceOsImp usbInterfaceOsImp = new AbstractUsbInterfaceOsImp();

	private InterfaceDescriptor interfaceDescriptor = null;

	private List endpoints = new ArrayList();

	protected boolean claimed = false;

}
