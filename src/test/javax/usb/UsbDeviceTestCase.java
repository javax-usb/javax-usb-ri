package javax.usb;

/** 
 * Copyright (c) IBM Corporation, 2000
 * This software and documentation is the confidential and proprietary
 * information of IBM, Corp. ("Confidential Information").
 * Raleigh, NC USA
 */

import junit.framework.*;

import javax.usb.util.*;

/**
 * A Test for UsbDevice
 * @author Dan Streetman
 */
public class UsbDeviceTestCase extends UsbTestCase
{
	/** Constructor */
	public UsbDeviceTestCase( UsbDevice device, String name )
	{
		super( name );

		usbDevice = device;
	}

	//*************************************************************************
	// Protected methods

	/** Set up tests */
	protected void setUp()
	{
	}

	/** Tear down tests */
	protected void tearDown()
	{
	}

	//*************************************************************************
	// Public methods

	/**
	 * Create the suite of tests
	 * @return the suite of tests
	 */
	public static Test suite()
	{
		TestSuite suite = new TestSuite();

		try { rootHub = UsbHostManager.getInstance().getUsbServices().getUsbRootHub(); }
		catch ( UsbException uE ) { }

		UsbInfoIterator iterator = filterTestableDevices( rootHub ).usbInfoIterator();

		while (iterator.hasNext()) {
			UsbDevice device = (UsbDevice)iterator.nextUsbInfo();

			if (usbKnownTestDeviceRegistry.isKnownUsbDevice( device )) {
				suite.addTest( new UsbDeviceTestCase( device, "testUsbDevice" ) );

				suite.addTest( new UsbDeviceTestCase( device, "testIsUsbHub" ) );
				suite.addTest( new UsbDeviceTestCase( device, "testManufacturer" ) );
/*				suite.addTest( new UsbDeviceTestCase( device, "testSerialNumber" ) ); */
				suite.addTest( new UsbDeviceTestCase( device, "testSpeedString" ) );
				suite.addTest( new UsbDeviceTestCase( device, "testProductString" ) );
				suite.addTest( new UsbDeviceTestCase( device, "testDeviceClass" ) );
				suite.addTest( new UsbDeviceTestCase( device, "testDeviceSubClass" ) );
				suite.addTest( new UsbDeviceTestCase( device, "testDeviceProtocol" ) );
				suite.addTest( new UsbDeviceTestCase( device, "testMaxPacketSize" ) );
				suite.addTest( new UsbDeviceTestCase( device, "testNumConfigs" ) );
				suite.addTest( new UsbDeviceTestCase( device, "testVendorId" ) );
				suite.addTest( new UsbDeviceTestCase( device, "testProductId" ) );
				suite.addTest( new UsbDeviceTestCase( device, "testBcdUsb" ) );
				suite.addTest( new UsbDeviceTestCase( device, "testBcdDevice" ) );
			}
		}

		return suite;
	}

	/**
	 * Test a specific known UsbDevice
	 */
	public void testUsbDevice()
	{
		assertTrue( "The specified device is not a known UsbDevice", usbKnownTestDeviceRegistry.isKnownUsbDevice( usbDevice ) );
	}

	public void testIsUsbHub()
	{
		UsbDevice testDevice = getKnownUsbDevice();
		assertTrue( "isUsbHub not equal", usbDevice.isUsbHub() == testDevice.isUsbHub() );
	}

	public void testManufacturer()
	{
		UsbDevice testDevice = getKnownUsbDevice();
		assertTrue( "manufacturer not equal", usbDevice.getManufacturer().equals( testDevice.getManufacturer() ) );
	}

	public void testSerialNumber()
	{
		UsbDevice testDevice = getKnownUsbDevice();
		assertTrue( "serial number not equal", usbDevice.getSerialNumber() == testDevice.getSerialNumber() );
	}

	public void testSpeedString()
	{
		UsbDevice testDevice = getKnownUsbDevice();
		assertTrue( "speed string not equal", usbDevice.getSpeedString().equals( testDevice.getSpeedString() ) );
	}

	public void testProductString()
	{
		UsbDevice testDevice = getKnownUsbDevice();
		assertTrue( "product string not equal", usbDevice.getProductString().equals( testDevice.getProductString() ) );
	}

	public void testDeviceClass()
	{
		UsbDevice testDevice = getKnownUsbDevice();
		assertTrue( "device class not equal", usbDevice.getDeviceClass() == testDevice.getDeviceClass() );
	}

	public void testDeviceSubClass()
	{
		UsbDevice testDevice = getKnownUsbDevice();
		assertTrue( "device subclass not equal", usbDevice.getDeviceSubClass() == testDevice.getDeviceSubClass() );
	}

	public void testDeviceProtocol()
	{
		UsbDevice testDevice = getKnownUsbDevice();
		assertTrue( "device protocol not equal", usbDevice.getDeviceProtocol() == testDevice.getDeviceProtocol() );
	}

	public void testMaxPacketSize()
	{
		UsbDevice testDevice = getKnownUsbDevice();
		assertTrue( "max packet size not equal", usbDevice.getMaxPacketSize() == testDevice.getMaxPacketSize() );
	}

	public void testNumConfigs()
	{
		UsbDevice testDevice = getKnownUsbDevice();
		assertTrue( "num configs not equal", usbDevice.getNumConfigs() == testDevice.getNumConfigs() );
	}

	public void testVendorId()
	{
		UsbDevice testDevice = getKnownUsbDevice();
		assertTrue( "vendor Id not equal", usbDevice.getVendorId() == testDevice.getVendorId() );
	}

	public void testProductId()
	{
		UsbDevice testDevice = getKnownUsbDevice();
		assertTrue( "product Id not equal", usbDevice.getProductId() == testDevice.getProductId() );
	}

	public void testBcdUsb()
	{
		UsbDevice testDevice = getKnownUsbDevice();

		assertTrue( "bcd usb not equal", usbDevice.getBcdUsb() == testDevice.getBcdUsb() );
	}

	public void testBcdDevice()
	{
		UsbDevice testDevice = getKnownUsbDevice();

		//EMM: Since the UsbDevice could be a latter release than the test device then use >= rather than ==
		assertTrue( "bcd device not equal", usbDevice.getBcdDevice() >= testDevice.getBcdDevice() );
	}

	//*************************************************************************
	// Private methods

	/** @return a 'fake' test device for this.usbDevice */
	private UsbDevice getKnownUsbDevice()
	{
		UsbDevice testDevice = null;

		try { testDevice = usbKnownTestDeviceRegistry.getKnownUsbDevice( usbDevice.getVendorId(), usbDevice.getProductId() ); }
		catch ( UsbException uE ) { fail( uE.getMessage() ); }

		return testDevice;
	}

	/**
	 * Filter testable devices out of a UsbInfoList of devices and hubs
	 * @param list the list of candidates
	 * @return a new list of the testable UsbDevices
	 */
	private static UsbInfoList filterTestableDevices( UsbDevice dev )
	{
		UsbInfoList newList = new DefaultUsbInfoList();

		if (null == dev)
			return newList;

		if (dev.isUsbHub()) {
			UsbInfoIterator iterator = ((UsbHub)dev).getAttachedUsbDevices();

			while (iterator.hasNext())
				newList.addUsbInfoList( filterTestableDevices( (UsbDevice)iterator.nextUsbInfo() ) );
		}

		if (usbKnownTestDeviceRegistry.isKnownUsbDevice( dev.getVendorId(), dev.getProductId() ))
			newList.addUsbInfo( dev );

		return newList;
	}

	//*************************************************************************
	// Instance variables

	private UsbDevice usbDevice = null;

	//*************************************************************************
	// Class variables

	private static UsbHub rootHub = null;
	private static UsbKnownTestDeviceRegistry usbKnownTestDeviceRegistry = new UsbKnownTestDeviceRegistry();
}
