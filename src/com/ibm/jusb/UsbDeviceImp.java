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
import java.io.*;

import javax.usb.*;
import javax.usb.event.*;
import javax.usb.util.*;

import com.ibm.jusb.os.*;
import com.ibm.jusb.util.*;
import com.ibm.jusb.event.*;

/**
 * UsbDevice platform-independent implementation.
 * <p>
 * This must be set up before use and/or connection to the topology tree.
 * <ul>
 * <li>The DeviceDescriptor must be set, either in the constructor or by its {@link #setDeviceDescriptor(DeviceDescriptor) setter}.</li>
 * <li>The UsbDeviceOsImp must be set, either in the constructor or by its {@link #setUsbDeviceOsImp(UsbDeviceOsImp) setter}.</li>
 * <li>The speed must be set by its {@link #setSpeed(Object) setter}.</li>
 * <li>All UsbConfigImps must be {@link #addUsbConfigImp(UsbConfigImp) added}.</li>
 * <li>The active config number must be {@link #setActiveUsbConfigNumber(byte) set}.</li>
 * </ul>
 * After setup, this may be connected to the topology tree by using the {@link #connect(UsbHubImp,byte) connect} method.
 * If the connect method is not used, there are additional steps:
 * <ul>
 * <li>Set the parent UsbPortImp by the {@link #setParentUsbPortImp(UsbPortImp) setter}.</li>
 * <li>Set this on the UsbPortImp by its {@link com.ibm.jusb.UsbPortImp#attachUsbDeviceImp(UsbDeviceImp) setter}.</li>
 * </ul>
 * If the parent UsbHubImp is not large enough, it can be {@link com.ibm.jusb.UsbHubImp@resize(int) resized}.  This should
 * only be necessary for root hubs, or more likely the virtual root hub.  Alternately, this may be added using the UsbHubImp's
 * {@link com.ibm.jusb.UsbHubImp@addUsbDeviceImp(UsbDeviceImp,byte) addUsbDeviceImp} method, which resizes if needed and
 * sets up the UsbPortImp.
 * @author Dan Streetman
 */
public class UsbDeviceImp implements UsbDevice,UsbIrpImp.Completion
{
	/**
	 * Constructor.
	 * @param desc This device's Descriptor.
	 * @param device The UsbDeviceOsImp.
	 */
	public UsbDeviceImp(DeviceDescriptor desc, UsbDeviceOsImp device)
	{
		setDeviceDescriptor(desc);
		setUsbDeviceOsImp(device);
	}

	//**************************************************************************
	// Public methods

	/** @return the associated UsbDeviceImp */
	public UsbDeviceOsImp getUsbDeviceOsImp() { return usbDeviceOsImp; }

	/** @param the UsbDeviceOsImp to use */
	public void setUsbDeviceOsImp( UsbDeviceOsImp deviceImp ) { usbDeviceOsImp = deviceImp; }

	/** @return The port that this device is attached to */
	public UsbPort getParentUsbPort() { return getParentUsbPortImp(); }

	/** @return The port that this device is attached to */
	public UsbPortImp getParentUsbPortImp() { return usbPortImp; }

	/** @param The parent port */
	public void setParentUsbPortImp( UsbPortImp port ) { usbPortImp = port; }

	/** @return true if this is a UsbHub and false otherwise */
	public boolean isUsbHub() { return false; }

	/** @return The manufacturer string. */
	public String getManufacturerString() throws UsbException,UnsupportedEncodingException
	{
		return getString( getDeviceDescriptor().iManufacturer() );
	}

	/** @return The serial number string. */
	public String getSerialNumberString() throws UsbException,UnsupportedEncodingException
	{
		return getString( getDeviceDescriptor().iSerialNumber() );
	}

	/** @return The product string. */
	public String getProductString() throws UsbException,UnsupportedEncodingException
	{
		return getString( getDeviceDescriptor().iProduct() );
	}

	/** @return The speed of this device. */
	public Object getSpeed() { return speed; }

	/** @return the UsbConfig objects associated with this UsbDevice */
	public List getUsbConfigs() { return Collections.unmodifiableList(configs); }

	/** @return the UsbConfig with the specified number as reported by getConfigNumber() */
	public UsbConfig getUsbConfig( byte number ) { return getUsbConfigImp(number); }

	/** @return the UsbConfigImp with the specified number as reported by getConfigNumber() */
	public UsbConfigImp getUsbConfigImp( byte number )
	{
		synchronized ( configs ) {
			for (int i=0; i<configs.size(); i++) {
				UsbConfigImp config = (UsbConfigImp)configs.get(i);

				if (number == config.getConfigDescriptor().bConfigurationValue())
					return config;
			}
		}

		return null;
	}

	/** @return if the specified UsbConfig is contained in this UsbDevice */
	public boolean containsUsbConfig( byte number )
	{
		if (null == getUsbConfig( number ))
			return false;
		else
			return true;
	}

	/** @return if this device is configured */
	public boolean isConfigured() { return 0 != getActiveUsbConfigNumber(); }

	/** @return the active UsbConfig number */
	public byte getActiveUsbConfigNumber() { return activeConfigNumber; }

	/** @return the active UsbConfig object */
	public UsbConfig getActiveUsbConfig() { return getActiveUsbConfigImp(); }

	/** @return the active UsbConfigImp object */
	public UsbConfigImp getActiveUsbConfigImp() { return getUsbConfigImp( getActiveUsbConfigNumber() ); }

	/** @return the device descriptor for this device */
	public DeviceDescriptor getDeviceDescriptor() { return deviceDescriptor; }

	/*
	 * @return the specified string descriptor
	 * @param the index of the string descriptor to get
	 * @throws javax.usb.UsbException if an error occurrs while getting the StringDescriptor.
	 */
	public StringDescriptor getStringDescriptor( byte index ) throws UsbException
	{
		/* There is no StringDescriptor for index 0 */
		if (0 == index)
			return null;

		StringDescriptor desc = getCachedStringDescriptor( index );

		if ( null == desc ) {
			requestStringDescriptor( index );
			desc = getCachedStringDescriptor( index );
		}

		return desc;
	}

	/**
	 * @return the String from the specified STringDescriptor
	 * @exception UsbException if an error occurrs while getting the StringDescriptor.
	 * @exception UnsupportedEncodingException If the string encoding is not supported.
	 */
	public String getString( byte index ) throws UsbException,UnsupportedEncodingException
	{
		StringDescriptor desc = getStringDescriptor( index );

		try {
			return desc.getString();
		} catch ( NullPointerException npE ) {
			return null;
		}
	}

	/**
	 * Indicate that a specific UsbIrpImp has completed.
	 * @param irp The UsbIrpImp that completed.
	 */
	public void usbIrpImpComplete( UsbIrpImp irp )
	{
		try { usbIrpImpComplete( (ControlUsbIrpImp)irp ); }
		catch ( ClassCastException ccE ) { /* shouldn't happen */ }
	}

	/**
	 * Indicate that a specific ControlUsbIrpImp has completed.
	 * <p>
	 * This will be called during the ControlUsbIrpImp's complete() method.
	 * @param irp The ControlUsbIrpImp that completed.
	 */
	public void usbIrpImpComplete( ControlUsbIrpImp irp )
	{
		if (irp.isUsbException()) {
			listenerImp.errorEventOccurred(new UsbDeviceErrorEvent(this,irp.getUsbException()));
		} else {
			if (irp.isSetConfiguration()) {
				try { setActiveUsbConfigNumber((byte)irp.wValue()); }
				catch ( Exception e ) { /* FIXME - log? */ }
			} else if (irp.isSetInterface()) {
				try { getActiveUsbConfigImp().getUsbInterfaceImp((byte)irp.wIndex()).setActiveSettingNumber((byte)irp.wValue()); }
				catch ( Exception e ) { /* FIXME - log? */ }
			}
			listenerImp.dataEventOccurred(new UsbDeviceDataEvent(this,irp,irp.getData(),irp.getOffset(),irp.getActualLength()));
		}
	}

	/** @param the listener to add */
	public void addUsbDeviceListener( UsbDeviceListener listener ) 
	{
		listenerImp.addEventListener(listener);
	}

	/** @param the listener to remove */
	public void removeUsbDeviceListener( UsbDeviceListener listener )
	{
		listenerImp.removeEventListener(listener);
	}

	/** @param desc the new device descriptor */
	public void setDeviceDescriptor( DeviceDescriptor desc ) { deviceDescriptor = desc; }

	/**
	 * Get a cached StringDescriptor.
	 * @param index The index of the StringDescriptor.
	 * @return The specified StringDescriptor, or null.
	 */
	public StringDescriptor getCachedStringDescriptor( byte index )
	{
		return (StringDescriptor)stringDescriptors.get( new Byte(index).toString() );
	}

	/**
	 * Set a cached StringDescriptor.
	 * @param index The index.
	 * @param desc The StringDescriptor.
	 */
	public void setCachedStringDescriptor( byte index, StringDescriptor desc )
	{
		stringDescriptors.put( new Byte(index).toString(), desc );
	}

	/**
	 * Sets the speed of this device.
	 * @param o The speed.
	 * @see javax.usb.UsbConst#DEVICE_SPEED_UNKNOWN
	 * @see javax.usb.UsbConst#DEVICE_SPEED_LOW
	 * @see javax.usb.UsbConst#DEVICE_SPEED_FULL
	 * @exception IllegalArgumentException If the speed is not one of the defined speeds.
	 */
	public void setSpeed( Object o )
	{
		if (UsbConst.DEVICE_SPEED_UNKNOWN == o || UsbConst.DEVICE_SPEED_LOW == o || UsbConst.DEVICE_SPEED_FULL == o)
			speed = o;
		else
			throw new IllegalArgumentException("Device speed must be DEVICE_SPEED_UNKNOWN, DEVICE_SPEED_LOW, or DEVICE_SPEED_FULL.");
	}

	/**
	 * Sets the active config index
	 * @param num the active config number (0 if device has been unconfigured)
	 */
	public void setActiveUsbConfigNumber( byte num ) { activeConfigNumber = num; }

	/** @param the configuration to add */
	public void addUsbConfigImp( UsbConfigImp config )
	{
		if (!configs.contains(config))
			configs.add( config );
	}

	/**
	 * Connect to the parent UsbHubImp.
	 * @param hub The parent.
	 * @param portNumber The port on the parent this is connected to.
	 */
	public void connect(UsbHubImp hub, byte portNumber) throws UsbException
	{
		hub.addUsbDeviceImp( this, portNumber );

		setParentUsbPortImp(hub.getUsbPortImp(portNumber));
	}

	/**
	 * Disconnect UsbDeviceImp.
	 * <p>
	 * Only call this if the device was connected to the topology tree;
	 * i.e. the UsbPortImp has been {@link #setParentUsbPortImp(UsbPortImp) set}.
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
		getParentUsbPortImp().detachUsbDeviceImp( this );

		listenerImp.usbDeviceDetached(new UsbDeviceEvent(this));

//FIXME - turn off all subcomponents; close pipes, release ifaces, etc.
	}

	/** Compare this to another UsbDeviceImp */
	public boolean equals(Object object)
	{
		UsbDeviceImp device = null;

		try { device = (UsbDeviceImp)object; }
		catch ( ClassCastException ccE ) { return false; }

		if (!getSpeed().equals(device.getSpeed()))
			return false;

		if (!getDeviceDescriptor().equals(device.getDeviceDescriptor()))
			return false;

//FIXME - check config/interface/endpoints too
		return true;
	}

	/**
	 * Submit a ControlUsbIrp synchronously to the Default Control Pipe.
	 * @param irp The ControlUsbIrp.
	 * @exception UsbException If an error occurrs.
	 */
	public void syncSubmit( ControlUsbIrp irp ) throws UsbException
	{
		synchronized (submitLock) {
			getUsbDeviceOsImp().syncSubmit( controlUsbIrpToControlUsbIrpImp( irp ) );
		}
	}

	/**
	 * Submit a ControlUsbIrp asynchronously to the Default Control Pipe.
	 * @param irp The ControlUsbIrp.
	 * @exception UsbException If an error occurrs.
	 */
	public void asyncSubmit( ControlUsbIrp irp ) throws UsbException
	{
		synchronized (submitLock) {
			getUsbDeviceOsImp().asyncSubmit( controlUsbIrpToControlUsbIrpImp( irp ) );
		}
	}

	/**
	 * Submit a List of ControlUsbIrps synchronously to the Default Control Pipe.
	 * <p>
	 * All ControlUsbIrps are guaranteed to be atomically (with respect to other clients
	 * of this API) submitted to the Default Control Pipe.  Atomicity on a native level
	 * is implementation-dependent.
	 * @param list The List of ControlUsbIrps.
	 * @exception UsbException If an error occurrs.
	 */
	public void syncSubmit( List list ) throws UsbException
	{
		synchronized (submitLock) {
			getUsbDeviceOsImp().syncSubmit( controlUsbIrpListToControlUsbIrpImpList( list ) );
		}
	}

	/**
	 * Submit a List of ControlUsbIrps asynchronously to the Default Control Pipe.
	 * <p>
	 * All ControlUsbIrps are guaranteed to be atomically (with respect to other clients
	 * of this API) submitted to the Default Control Pipe.  Atomicity on a native level
	 * is implementation-dependent.
	 * @param list The List of ControlUsbIrps.
	 * @exception UsbException If an error occurrs.
	 */
	public void asyncSubmit( List list ) throws UsbException
	{
		synchronized (submitLock) {
			getUsbDeviceOsImp().asyncSubmit( controlUsbIrpListToControlUsbIrpImpList( list ) );
		}
	}

	//**************************************************************************
	// Protected methods

	/** @return the device's default langID */
	protected synchronized short getLangId() throws UsbException
	{
		if (0x0000 == langId) {
			byte[] data = new byte[256];

			int len = StandardRequest.getDescriptor( this, UsbConst.DESCRIPTOR_TYPE_STRING, (byte)0, (short)0, data );

			if (4 > len || 4 > UsbUtil.unsignedInt(data[0]))
				throw new UsbException("Strings not supported by device");

			langId = (short)((data[3] << 8) | data[2]);
		}

		return langId;
	}

	/** Update the StringDescriptor at the specified index. */
	protected void requestStringDescriptor( byte index ) throws UsbException
	{
		byte[] data = new byte[256];

		int len = StandardRequest.getDescriptor( this, UsbConst.DESCRIPTOR_TYPE_STRING, index, getLangId(), data );

		/* requested string not present or invalid */
		if (2 > len || 2 > UsbUtil.unsignedInt(data[0]))
			return;

		/* String claims to be longer than actual data transferred */
		if (UsbUtil.unsignedInt(data[0]) > len)
			throw new UsbException("String Descriptor length byte is longer than Descriptor data");

		/* string length (descriptor len minus 2 for header) */
		int strLen = UsbUtil.unsignedInt(data[0]) - 2;

		byte[] bString = new byte[strLen];
		System.arraycopy(data, 2, bString, 0, strLen);

		setCachedStringDescriptor( index, new StringDescriptorImp( data[0], data[1], bString ) );
	}

	/**
	 * Setup a ControlUsbIrpImp.
	 * @param controlUsbIrpImp The ControlUsbIrpImp to setup.
	 */
	protected void setupControlUsbIrpImp( ControlUsbIrpImp controlUsbIrpImp )
	{
		controlUsbIrpImp.setCompletion( this );
	}

	/**
	 * Convert a ControlUsbIrp to a ControlUsbIrpImp.
	 * @param controlUsbIrp The ControlUsbIrp.
	 * @return A ControlUsbIrpImp that corresponds to the controlUsbIrp.
	 */
	protected ControlUsbIrpImp controlUsbIrpToControlUsbIrpImp(ControlUsbIrp controlUsbIrp)
	{
		ControlUsbIrpImp controlUsbIrpImp = null;

		try {
			controlUsbIrpImp = (ControlUsbIrpImp)controlUsbIrp;
		} catch ( ClassCastException ccE ) {
			controlUsbIrpImp = new ControlUsbIrpImp(controlUsbIrp);
		}

		setupControlUsbIrpImp( controlUsbIrpImp );

		return controlUsbIrpImp;
	}

	/**
	 * Convert a List of ControlUsbIrps to a List of ControlUsbIrpImps.
	 * @param list The List of ControlUsbIrps.
	 * @return A List of ControlUsbIrpImps that correspond to the ControlUsbIrp List.
	 */
	protected List controlUsbIrpListToControlUsbIrpImpList(List list) throws ClassCastException
	{
		List newList = new ArrayList();

		for (int i=0; i<list.size(); i++)
			newList.add(controlUsbIrpToControlUsbIrpImp((ControlUsbIrp)list.get(i)));

		return newList;
	}

	//**************************************************************************
	// Instance variables

	private UsbDeviceOsImp usbDeviceOsImp = null;

	private Object submitLock = new Object();

	private DeviceDescriptor deviceDescriptor = null;

	private Hashtable stringDescriptors = new Hashtable();
	private short langId = 0x0000;

	private Object speed = UsbConst.DEVICE_SPEED_UNKNOWN;
    
	private List configs = new ArrayList();
	private byte activeConfigNumber = 0;

	private UsbPortImp usbPortImp = null;

	private UsbDeviceListenerImp listenerImp = new UsbDeviceListenerImp();
}
