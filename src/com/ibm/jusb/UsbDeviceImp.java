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
public class UsbDeviceImp implements UsbDevice
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

//FIXME - fix this
	/** @param requestImp The RequestImp that completed. */
	//public void requestImpCompleted(RequestImp requestImp)
	//{
	//	if (requestImp.isUsbException()) {
	//		fireErrorEvent(requestImp.getUsbException());
	//	} else {
	//		if (requestImp.isSetConfigurationRequest()) {
	//			try { setActiveUsbConfigNumber((byte)requestImp.getValue()); }
	//			catch ( Exception e ) { /* log? */ }
	//		} else if (requestImp.isSetInterfaceRequest()) {
	//			try { getActiveUsbConfigImp().getUsbInterfaceImp((byte)requestImp.getIndex()).setActiveAlternateSettingNumber((byte)requestImp.getValue()); }
	//			catch ( Exception e ) { /* log? */ }
	//		}
	//
	//		fireDataEvent(requestImp.getData(),requestImp.getDataLength());
	//	}
	//}

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
	 * @return the specified StringDescriptor.
	 */
	public StringDescriptor getCachedStringDescriptor( byte index )
	{
		return (StringDescriptor)stringDescriptors.get( new Byte( index ).toString() );
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
	public void syncSubmit( UsbIrp.ControlUsbIrp irp ) throws UsbException
	{
//FIXME - implement
throw new UsbException("not implemented");
	}

	/**
	 * Submit a ControlUsbIrp asynchronously to the Default Control Pipe.
	 * @param irp The ControlUsbIrp.
	 * @exception UsbException If an error occurrs.
	 */
	public void asyncSubmit( UsbIrp.ControlUsbIrp irp ) throws UsbException
	{
throw new UsbException("not implemented");
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
throw new UsbException("not implemented");
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
throw new UsbException("not implemented");
	}

	//**************************************************************************
	// Protected methods

	/** @return the device's default langID */
	protected synchronized short getLangId() throws UsbException
	{
//FIXME - implement
throw new UsbException("Not implemented");
//		if (0x0000 == langId) {
//			byte[] data = new byte[256];

//getStandardOperations().getDescriptor( (short)(DescriptorConst.DESCRIPTOR_TYPE_STRING << 8), (short)0x0000, data );

//			if (4 > data[0])
//				throw new UsbException("Strings not supported by device");

//			langId = (short)((data[3] << 8) | data[2]);
//		}

//		return langId;
	}

	/** Update the StringDescriptor at the specified index. */
	protected void requestStringDescriptor( byte index ) throws UsbException
	{
		byte[] data = new byte[256];

//FIXME - implement
//Request request = getStandardOperations().getDescriptor( (short)((DescriptorConst.DESCRIPTOR_TYPE_STRING << 8) | (index)), getLangId(), data );
throw new UsbException("Not implemented.");

//FIXME - this need changing!

		/* requested string not present */
//if (2 > request.getLength())
//			return;

		/* claimed length must be at least 2; length byte and type byte are mandatory. */
//		if (2 > UsbUtil.unsignedInt(data[0]))
//			throw new UsbException("String Descriptor length byte is an invalid length, minimum length must be 2, claimed length " + UsbUtil.unsignedInt(data[0]));

		/* string length (descriptor len minus 2 for header) */
//		int len = UsbUtil.unsignedInt(data[0]) - 2;

//		if (request.getLength() < (len + 2))
//			throw new UsbException("String Descriptor length byte is longer than Descriptor data");

//FIXME - fix this
//setStringDescriptor( index, new StringDescriptorImp( data[0], data[1], bytesToString(data,len) ) );
	}

	//**************************************************************************
	// Instance variables

	private UsbDeviceOsImp usbDeviceOsImp = null;

	private DeviceDescriptor deviceDescriptor = null;

	private Hashtable stringDescriptors = new Hashtable();
	private short langId = 0x0000;

	private Object speed = UsbConst.DEVICE_SPEED_UNKNOWN;
    
	private List configs = new ArrayList();
	private byte activeConfigNumber = 0;

	private UsbPortImp usbPortImp = null;

	private UsbDeviceListenerImp listenerImp = new UsbDeviceListenerImp();
}
