package com.ibm.jusb;

/*
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import java.util.*;

import javax.usb.*;
import javax.usb.event.*;
import javax.usb.util.*;

/**
 * Abstract implementation of the UsbDevice interface
 * @author E. Michael Maximilien
 * @author Dan Streetman
 * @version 1.0.0
 */
public class UsbDeviceAbstraction extends AbstractUsbInfo implements UsbDevice
{
	//-------------------------------------------------------------------------
	// Ctor(s)
	//

	/** Constructor */
	UsbDeviceAbstraction() {}

	//-------------------------------------------------------------------------
	// Public methods
	//

	/** @return the port that this device is attached to */
	public UsbPort getUsbPort() { return usbPort; }

	/** @return true if this is a UsbHub and false otherwise */
	public boolean isUsbHub() { return false; }

	//-------------------------------------------------------------------------
	// Public interface methods
	//

	/** @return the manufacturer of this device */
	public String getManufacturer()
	{ return getCachedString( getDeviceDescriptor().getManufacturerIndex() ); }

	/** @return the serial number of this device */
	public String getSerialNumber()
	{ return getCachedString( getDeviceDescriptor().getSerialNumberIndex() ); }

	/** @return a String describing the speed of this device */
	public String getSpeedString() { return speedString; }

	/** @return a String describing this product */
	public String getProductString()
	{ return getCachedString( getDeviceDescriptor().getProductIndex() ); }

	/** @return the USB device class */
	public byte getDeviceClass() { return getDeviceDescriptor().getDeviceClass(); }

	/** @return the USB device subclass */
	public byte getDeviceSubClass() { return getDeviceDescriptor().getDeviceSubClass(); }

	/** @returnt the USB device protocol */
	public byte getDeviceProtocol() { return getDeviceDescriptor().getDeviceProtocol(); }

	/** @return the maximum packet size */
	public byte getMaxPacketSize() { return getDeviceDescriptor().getMaxPacketSize(); }

	/** @return the number of configurations */
	public byte getNumConfigs() { return getDeviceDescriptor().getNumConfigs(); }

	/** @return the vendor ID */
	public short getVendorId() { return getDeviceDescriptor().getVendorId(); }

	/** @return the product ID */
	public short getProductId() { return getDeviceDescriptor().getProductId(); }

	/** @return the BCD USB version for this device */
	public short getBcdUsb() { return getDeviceDescriptor().getBcdUsb(); }

	/** @return the BCD revision number for this device */
	public short getBcdDevice() { return getDeviceDescriptor().getBcdDevice(); }

	/** @return the UsbConfig objects associated with this UsbDevice */
	public UsbInfoListIterator getUsbConfigs() { return configs.usbInfoListIterator(); }

	/** @return the UsbConfig with the specified number as reported by getConfigNumber() */
	public UsbConfig getUsbConfig( byte number )
	{
		UsbInfoIterator iterator = getUsbConfigs();

		while (iterator.hasNext()) {
			UsbConfig config = (UsbConfig)iterator.nextUsbInfo();

			if (number == config.getConfigNumber())
				return config;
		}

		throw new UsbRuntimeException( "No UsbConfig with number " + UsbUtil.unsignedInt( number ) );
	}

	/** @return if the specified UsbConfig is contained in this UsbDevice */
	public boolean containsUsbConfig( byte number )
	{
		UsbInfoIterator iterator = getUsbConfigs();

		while (iterator.hasNext()) {
			UsbConfig config = (UsbConfig)iterator.nextUsbInfo();

			if (number == config.getConfigNumber())
				return true;
		}

		return false;
	}

	/** @return if this device is configured */
	public boolean isConfigured() { return 0 != getActiveUsbConfigNumber(); }

	/** @return the active UsbConfig number */
	public byte getActiveUsbConfigNumber() { return activeConfigNumber; }

	/** @return the active UsbConfig object */
	public UsbConfig getActiveUsbConfig() { return getUsbConfig( getActiveUsbConfigNumber() ); }

	/** @return the device descriptor for this device */
	public DeviceDescriptor getDeviceDescriptor() { return (DeviceDescriptor)getDescriptor(); }

	/*
	 * @return the specified string descriptor
	 * @param the index of the string descriptor to get
	 * @throws javax.usb.UsbException if an error occurrs while getting the StringDescriptor.
	 */
	public StringDescriptor getStringDescriptor( byte index ) throws UsbException
	{
		StringDescriptor desc = getCachedStringDescriptor( index );

		if ( null == desc ) {
			requestStringDescriptor( index );
			desc = getCachedStringDescriptor( index );
		}

		return desc;
	}

	/**
	 * @return the String from the specified STringDescriptor
	 * @throws javax.usb.UsbException if an error occurrs while getting the StringDescriptor.
	 */
	public String getString( byte index ) throws UsbException
	{
		StringDescriptor desc = getStringDescriptor( index );

		return ( null == desc ? null : desc.getString() );
	}

	/**
	 * Returns a StandardOperations object that can be used to submit
	 * standard USB Request objects to this device.
	 * @return a StandardOperations object to use with this UsbDevice
	 * @see javax.usb.Request
	 * @see javax.usb.os.UsbServices#getRequestFactory
	 * @throw javax.usb.UsbException if the StandardOperations could not be created
	 */
	public StandardOperations getStandardOperations() throws UsbException
	{
		if( standardOperations == null )
			standardOperations = new DefaultStandardOperations( this, getUsbDeviceImp().getStandardOpsImp() );

		return standardOperations;
	}

	/**
	 * Returns a ClassOperations object that can be used to submit
	 * standard USB class Request objects to this device.
	 * @return a ClassOperations object to use with this UsbDevice
	 * @see javax.usb.Request
	 * @see javax.usb.os.UsbServices#getRequestFactory
	 * @throw javax.usb.UsbException if the ClassOperations could not be created
	 */
	public ClassOperations getClassOperations() throws UsbException
	{
		if( classOperations == null )
			classOperations = new DefaultClassOperations( this, getUsbDeviceImp().getClassOpsImp() );

		return classOperations;
	}

	/**
	 * Returns a VendorOperations object that can be used to submit
	 * standard USB vendor Request objects to this device.
	 * @return a VendorOperations object to use with this UsbDevice
	 * @see javax.usb.Request
	 * @see javax.usb.os.UsbServices#getRequestFactory
	 * @throw javax.usb.UsbException if the VendorOperations could not be created
	 */
	public VendorOperations getVendorOperations() throws UsbException
	{
		if( vendorOperations == null )
			vendorOperations = new DefaultVendorOperations( this, getUsbDeviceImp().getVendorOpsImp() );

		return vendorOperations;
	}

	/** @param the listener to add */
	public synchronized void addUsbDeviceListener( UsbDeviceListener listener ) 
	{
		listenerList.add( listener );
	}

	/** @param the listener to remove */
	public synchronized void removeUsbDeviceListener( UsbDeviceListener listener )
	{
		listenerList.remove( listener );
	}

	//-------------------------------------------------------------------------
	// Public accept method for the Visitor pattern
	//

	/**
	 * Visitor.accept method
	 * @param visitor the UsbInfoVisitor visiting this UsbInfo
	 */
	public void accept( UsbInfoVisitor visitor ) { visitor.visitUsbDevice( this ); }

	//-------------------------------------------------------------------------
	// Protected and package methods
	//

	/** @return the associated UsbDeviceImp */
	UsbDeviceImp getUsbDeviceImp() { return usbDeviceImp; }

	/** @param the UsbDeviceImp to use */
	void setUsbDeviceImp( UsbDeviceImp deviceImp ) { usbDeviceImp = deviceImp; }

	/** @param desc the new device descriptor */
	void setDeviceDescriptor( DeviceDescriptor desc ) { setDescriptor( desc ); }

	/**
	 * @param index the index of the new string descriptor
	 * @param desc the new string descriptor
	 */
	void setStringDescriptor( byte index, StringDescriptor desc )
	{
		stringDescriptors.put( new Byte( index ), desc );
	}

	/** Update the StringDescriptor at the specified index. */
	void requestStringDescriptor( byte index )
	{
		/* Do a StringDescriptor Request here, and update the cache */
	}

	/**
	 * @return the specified StringDescriptor.
	 */
	StringDescriptor getCachedStringDescriptor( byte index )
	{
		return (StringDescriptor)stringDescriptors.get( new Byte( index ) );
	}

	/**
	 * @return the String from the specified STringDescriptor
	 */
	String getCachedString( byte index )
	{
		StringDescriptor desc = getCachedStringDescriptor( index );

		return ( null == desc ? null : desc.getString() );
	}

	/**
	 * Sets the speed of this device 
	 * @param s the String argument
	 */
	void setSpeedString( String s ) { speedString = s; }

	/**
	 * Sets the active config index
	 * @param num the active config number (0 if device has been unconfigured)
	 */
	void setActiveUsbConfigNumber( byte num ) { activeConfigNumber = num; }

	/**
	 * @param usbHub the parent UsbHub
	 * @param port the parent port index the UsbDevice is attached to
	 */
	void connect( UsbHubImp usbHub, byte port ) throws UsbException
	{
		usbHub.addDevice( this, port );
		usbPort = usbHub.getUsbPort( port );
	}

	/** @param the configuration to add */
	void addUsbConfig( UsbConfig config ) { configs.addUsbInfo( config ); }

	/**
	 * Disconnect UsbDevice
	 * @exception javax.usb.UsbException if there was a problem removing the device
	 */
	void disconnect() throws UsbException
	{
		try {
			((UsbHubImp)getUsbPort().getUsbHub()).removeDevice( this, getUsbPort().getPortNumber() );
		} catch ( NullPointerException npE ) {
			throw new UsbException( "Cannot disconnect, not connected!" );
		} finally {
			/* Choose parallel or series notification */
			fireDetachEventsParallel();
		}
	}

	/** Fire detach events on all listeners in parallel */
	void fireDetachEventsParallel()
	{
		synchronized ( listenerList ) {
			for (int i=0; i<listenerList.size(); i++) {
				final UsbDeviceListener listener = (UsbDeviceListener)listenerList.elementAt(i);

				Runnable detachRunnable = new Runnable() {
					public void run() {
						listener.usbDeviceDetached( new UsbDeviceEvent( UsbDeviceAbstraction.this ) );
					}
				};

				Thread detachThread = new Thread( detachRunnable );

				detachThread.setName( getName() + " parallel detach notification Thread " + i );
				detachThread.setDaemon( true );

				detachThread.start();
			}
		}
	}

	/** Fire detach events on all listeners in series */
	void fireDetachEventsSeries()
	{
		Runnable detachRunnable = new Runnable() {
			Vector list = UsbDeviceAbstraction.this.listenerList;

			public void run() {
				synchronized ( list ) {
					for (int i=0; i<list.size(); i++) {
						UsbDeviceListener listener = (UsbDeviceListener)list.elementAt(i);

						listener.usbDeviceDetached( new UsbDeviceEvent( UsbDeviceAbstraction.this ) );
					}
				}
			}
		};

		Thread detachThread = new Thread( detachRunnable );

		detachThread.setName( getName() + " series detach notification Thread" );
		detachThread.setDaemon( true );

		detachThread.start();
	}

	//-------------------------------------------------------------------------
	// Instance variables
	//

	private UsbDeviceImp usbDeviceImp = null;

	private Vector listenerList = new Vector();

	private Hashtable stringDescriptors = new Hashtable();

	private String speedString = "";
    
	private UsbInfoList configs = new DefaultUsbInfoList();
	private byte activeConfigNumber = 0;

	private UsbPort usbPort = null;

	private StandardOperations standardOperations = null;
	private VendorOperations vendorOperations = null;
	private ClassOperations classOperations = null;

	//-------------------------------------------------------------------------
	// Class constants
	//

	public static final String USB_DEVICE_NAME = "device";
}
