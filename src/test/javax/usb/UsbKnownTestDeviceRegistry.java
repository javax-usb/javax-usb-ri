package javax.usb;

/** 
 * Copyright (c) IBM Corporation, 2000
 * This software and documentation is the confidential and proprietary
 * information of IBM, Corp. ("Confidential Information").
 * Raleigh, NC USA
 */

import java.util.*;

import javax.usb.event.*;
import javax.usb.util.*;

import javax.usb.UsbTestCase;

/**
 * Factory which creates known UsbDevice objects
 * which match known connected 'real' UsbDevice objects
 * @author Dan Streetman
 */
public class UsbKnownTestDeviceRegistry extends UsbTestCase
{

	//-------------------------------------------------------------------------
	// Ctor(s)
	//

	/** 
	 * Constructor
	 * @param name the name of the test case
	 */
	public UsbKnownTestDeviceRegistry( String name ) 
	{ 
		super( name ); 
		createKnownUsbDeviceList();
	}

	/** Constructor */
	public UsbKnownTestDeviceRegistry()
	{
		super( "UsbKnownTestDeviceRegistry" );
		createKnownUsbDeviceList();
	}

	//*************************************************************************
	// Public methods

	/**
	 * Check if a vendor and product ID pair matches a known UsbDevice
	 * @param vendorId the vendor ID of the known UsbDevice
	 * @param productId the product ID of the known UsbDevice
	 * @return true if the vendor and product ID pair match a known UsbDevice
	 */
	public boolean isKnownUsbDevice( short vendorId, short productId )
	{
		return knownDevices.contains( getKey( vendorId, productId ) );
	}

	/**
	 * Check if a UsbDevice Object matches a known UsbDevice
	 * @return true if the vendor and product ID pair match a known UsbDevice
	 */
	public boolean isKnownUsbDevice( UsbDevice device )
	{
		return isKnownUsbDevice( device.getVendorId(), device.getProductId() );
	}

	/**
	 * Create a fake UsbDevice with known values to match
	 * the specified vendor and product IDs
	 * @param vendorId the vendor ID of the known UsbDevice to get
	 * @param productId the product ID of the known UsbDevice to get
	 * @return a 'fake' UsbDevice with known values
	 * @throws UsbException if the vendor and product ID pair does not match a known UsbDevice
	 */
	public UsbDevice getKnownUsbDevice( short vendorId, short productId ) throws UsbException
	{
		if (0x04b3 == vendorId && 0x4526 == productId)
			return ibmLcd;

		throw new UsbException( "Not implemented!" );
	}

	//*************************************************************************
	// Private methods

	/**
	 * Change a vendor and product ID pair into an Object usable as a Hashtable key.
	 * <p>
	 * For any given ID pair, the returned Objects may not be == equal,
	 * but they will be equal() equal.
	 * @param vendorId the vendor ID
	 * @param productId the product ID
	 * @return an Object representing the vendor/product ID pair
	 */
	private Object getKey( short vendorId, short productId )
	{
		return new Integer( (vendorId << 16) | productId );
	}

	/**
	 * Change a device's vendor and product ID pair into an Object usable as a Hashtable key.
	 * <p>
	 * For any given ID pair, the returned Objects may not be == equal,
	 * but they will be equal() equal.
	 * @param device the device
	 * @return an Object representing the vendor/product ID pair
	 */
	private Object getKey( UsbDevice device )
	{
		return new Integer( (device.getVendorId() << 16) | device.getProductId() );
	}

	/**
	 * Initialize the list of known UsbDevices
	 */
	private void createKnownUsbDeviceList()
	{
		knownDevices.add( getKey( ibmLcd ) );
	}

	//*************************************************************************
	// Instance variables

	private Vector knownDevices = new Vector();

	//*************************************************************************
	// Class constants

	private UsbDevice ibmLcd = new UsbDevice() {
		public boolean isUsbHub() { return false; }
		public UsbPort getUsbPort() { return null; }
		public StandardOperations getStandardOperations() { return null; }
		public VendorOperations getVendorOperations() { return null; }
		public ClassOperations getClassOperations() { return null; }
		public String getManufacturer() { return "(c) Copyright IBM Corp. 1999"; }
		public String getSerialNumber() { return "19991109 123708 A 001108 25L5510"; }
		public String getSpeedString() { return "12 Mbps"; }
		public String getProductString() { return "IBM Retail USB 40 Character Liquid Crystal Display"; }
		public byte getDeviceClass() { return 0; }
		public byte getDeviceSubClass() { return 0; }
		public byte getDeviceProtocol() { return 0; }
		public byte getMaxPacketSize() { return 16; }
		public byte getNumConfigs() { return 1; }
		public short getVendorId() { return 0x04b3; }
		public short getProductId() { return 0x4526; }
		public short getBcdUsb() { return 0x0100; }
		public short getBcdDevice() { return 0x0101; }
		public boolean isConfigured() { return true; }
		public UsbInfoListIterator getUsbConfigs() { return null; }
		public UsbConfig getUsbConfig( byte number ) { return null; }
		public boolean containsUsbConfig( byte number ) { return false; }
		public UsbConfig getActiveUsbConfig() { return null; }
		public byte getActiveUsbConfigNumber() { return (byte)0; }
		public DeviceDescriptor getDeviceDescriptor() { return null; }
		public String getName() { return ""; }
		public Descriptor getDescriptor() { return null; }
		public StringDescriptor getStringDescriptor( byte index ) { return null; }
		public String getString( byte index ) { return ""; }
		public void addUsbDeviceListener( UsbDeviceListener listener ) { }
		public void removeUsbDeviceListener( UsbDeviceListener listener ) { }
		public void accept( UsbInfoVisitor v ) { }
	};

}
