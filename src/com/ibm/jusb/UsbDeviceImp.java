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

import com.ibm.jusb.os.*;

/**
 * UsbDevice platform-independent implementation.
 * @author Dan Streetman
 * @author E. Michael Maximilien
 */
public class UsbDeviceImp extends AbstractUsbInfo implements UsbDevice
{
	/**
	 * Constructor.
	 * <p>
	 * The UsbDeviceOsImp must be {@link #setUsbDeviceOsImp(UsbDeviceOsImp) set} before
	 * connecting this device to the topology tree.
	 */
	public UsbDeviceImp() {}

	/**
	 * Constructor.
	 * @param device The UsbDeviceOsImp.
	 */
	public UsbDeviceImp(UsbDeviceOsImp device) { setUsbDeviceOsImp(device); }

	//**************************************************************************
	// Public methods

	/** @return the associated UsbDeviceImp */
	public UsbDeviceOsImp getUsbDeviceOsImp() { return usbDeviceOsImp; }

	/** @param the UsbDeviceOsImp to use */
	public void setUsbDeviceOsImp( UsbDeviceOsImp deviceImp ) { usbDeviceOsImp = deviceImp; }

	/** @return the port that this device is attached to */
	public UsbPort getUsbPort() { return getUsbPortImp(); }

	/** @return the port that this device is attached to */
	public UsbPortImp getUsbPortImp() { return usbPortImp; }

	/** @param The parent port */
	public void setUsbPortImp( UsbPortImp port ) { usbPortImp = port; }

	/** @return true if this is a UsbHub and false otherwise */
	public boolean isUsbHub() { return false; }

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
	public UsbConfig getUsbConfig( byte number ) { return getUsbConfigImp(number); }

	/** @return the UsbConfigImp with the specified number as reported by getConfigNumber() */
	public UsbConfigImp getUsbConfigImp( byte number )
	{
		synchronized ( configs ) {
			for (int i=0; i<configs.size(); i++) {
				UsbConfigImp config = (UsbConfigImp)configs.get(i);

				if (number == config.getConfigNumber())
					return config;
			}
		}

		throw new UsbRuntimeException( "No UsbConfig with number " + UsbUtil.unsignedInt( number ) );
	}

	/** @return if the specified UsbConfig is contained in this UsbDevice */
	public boolean containsUsbConfig( byte number )
	{
		try { getUsbConfig( number ); }
		catch ( UsbRuntimeException urE ) { return false; }

		return true;
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

	/** @return A UsbOperationsImp object */
	public UsbOperationsImp getUsbOperationsImp() { return usbOperationsImp; }

	/** @return A StandardOperations object */
	public StandardOperations getStandardOperations() { return getUsbOperationsImp(); }

	/** @return A ClassOperations object */
	public ClassOperations getClassOperations() { return getUsbOperationsImp(); }

	/** @return A VendorOperations object */
	public VendorOperations getVendorOperations() { return getUsbOperationsImp(); }

	/** @param the listener to add */
	public void addUsbDeviceListener( UsbDeviceListener listener ) 
	{
		listenerList.add( listener );
	}

	/** @param the listener to remove */
	public void removeUsbDeviceListener( UsbDeviceListener listener )
	{
		listenerList.remove( listener );
	}

	/**
	 * Visitor.accept method
	 * @param visitor the UsbInfoVisitor visiting this UsbInfo
	 */
	public void accept( UsbInfoVisitor visitor ) { visitor.visitUsbDevice( this ); }

	/** @param desc the new device descriptor */
	public void setDeviceDescriptor( DeviceDescriptor desc ) { setDescriptor( desc ); }

	/**
	 * @param index the index of the new string descriptor
	 * @param desc the new string descriptor
	 */
	public void setStringDescriptor( byte index, StringDescriptor desc )
	{
		stringDescriptors.put( new Byte( index ), desc );
	}

	/** Update the StringDescriptor at the specified index. */
	public void requestStringDescriptor( byte index )
	{
//FIXME!!
		/* Do a StringDescriptor Request here, and update the cache */
	}

	/**
	 * @return the specified StringDescriptor.
	 */
	public StringDescriptor getCachedStringDescriptor( byte index )
	{
		return (StringDescriptor)stringDescriptors.get( new Byte( index ) );
	}

	/**
	 * @return the String from the specified STringDescriptor
	 */
	public String getCachedString( byte index )
	{
		StringDescriptor desc = getCachedStringDescriptor( index );

		return ( null == desc ? null : desc.getString() );
	}

	/**
	 * Sets the speed of this device 
	 * @param s the String argument
	 */
	public void setSpeedString( String s ) { speedString = s; }

	/**
	 * Sets the active config index
	 * @param num the active config number (0 if device has been unconfigured)
	 */
	public void setActiveUsbConfigNumber( byte num ) { activeConfigNumber = num; }

	/** @param the configuration to add */
	public void addUsbConfig( UsbConfig config ) { configs.addUsbInfo( config ); }

	/**
	 * Disconnect UsbDeviceImp.
	 * <p>
	 * Only call this if the device was connected to the topology tree;
	 * i.e. the UsbPortImp has been {@link #setUsbPortImp(UsbPortImp) set}.
	 * This will fire
	 * {@link javax.usb.event.UsbDeviceListener#usbDeviceDetached(UsbDeviceEvent) usbDeviceDetached}
	 * events to all listeners.
	 * <p>
	 * The implementation does not have to call this method, it is only a convienience method
	 * to disconnect this device and fire events to listeners; the implementation can do those
	 * things itself instead of this method, if desired.
	 */
	public void disconnect()
	{
		getUsbPortImp().detachUsbDeviceImp( this );

		/* Choose parallel or series notification */
		fireDetachEventsParallel();
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

	private UsbDeviceOsImp usbDeviceOsImp = null;

	private Vector listenerList = new Vector();

	private Hashtable stringDescriptors = new Hashtable();

	private String speedString = "";
    
	private UsbInfoList configs = new DefaultUsbInfoList();
	private byte activeConfigNumber = 0;

	private UsbPortImp usbPortImp = null;

	private UsbOperationsImp usbOperationsImp = new UsbOperationsImp();

	//-------------------------------------------------------------------------
	// Class constants
	//

	public static final String USB_DEVICE_NAME = "device";
}
