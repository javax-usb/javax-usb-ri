
package com.ibm.jusb.os;

/** 
 * Copyright (c) IBM Corporation, 2000
 * This software and documentation is the confidential and proprietary
 * information of IBM, Corp. ("Confidential Information").
 * Raleigh, NC USA
 */

import junit.framework.*;
import javax.usb.util.*;
import javax.usb.*;

/**
 * A Test for UsbConfig
 * @author Helen Li
 */

public class UsbConfigImpTestCase extends TestCase
{
        /** Constructor */
	public UsbConfigImpTestCase( String name ) { super( name ); }

	//*************************************************************************
	// Protected methods

	/** Set up tests */
	protected void setUp()
	{
		
            UsbHub rootHub = null;

            try { rootHub = UsbHostManager.getInstance().getUsbServices().getUsbRootHub(); }
            catch ( UsbException uE ) { }


            UsbInfoIterator iterator = filterTestableDevices( rootHub ).usbInfoListIterator();

            while (iterator.hasNext()) {
                    usbDevice = (UsbDevice)iterator.nextUsbInfo();

                    if (usbKnownTestDeviceRegistry.isKnownUsbDevice( usbDevice )) 

                        usbConfig = new UsbConfigImp( usbDevice );
            }
        }

	/** Tear down tests */
	protected void tearDown()
	{
	    usbDevice = null;
            usbConfig = null;
	}


	//*************************************************************************
	// Public methods
      
        

//        public String getName() 



	/** @return the list of USB device interfaces for this configuration */
//        public UsbInfoList getUsbInterfaces() { return interfaces; }


	/** @return the String description of this config */
//	public String getConfigString()
	//-------------------------------------------------------------------------
	// Public accept method for the Visitor pattern
	//

	/**
	 * Visitor.accept method
	 * @param visitor the UsbInfoVisitor visiting this UsbInfo
	 */
//	public void accept( UsbInfoVisitor visitor ) { visitor.visitUsbConfig( this ); }



        /** @test configuration's index number */
        public void testGetConfigNumber()
        {
            UsbKnownTestConfig testConfig = new UsbKnownTestConfig(usbDevice);
            assertTrue( "num configs not equal", (usbConfig.getConfigNumber() == testConfig.getConfigNumber()) );
        }


        /** @test configuration's number of UsbInterface */
        public void testGetNumInterfaces()
        {
            UsbKnownTestConfig testConfig = new UsbKnownTestConfig(usbDevice);
            assertTrue( "num configs not equal", (usbConfig.getNumInterfaces() == testConfig.getNumInterfaces()) );
        }


        /** @test the attributes code for this configuration */
        public void testtGetAttributes()
        {
            UsbKnownTestConfig testConfig = new UsbKnownTestConfig(usbDevice);
            assertTrue( "num configs not equal", (usbConfig.getAttributes() == testConfig.getAttributes() ));
        }


        /** @test the maximum power needed for this configuration */
        public void testGetMaxPower()
        {
            UsbKnownTestConfig testConfig = new UsbKnownTestConfig(usbDevice);
            assertTrue( "num configs not equal", (usbConfig.getMaxPower() == testConfig.getMaxPower() ));
        }


        /** @test the list of USB device interfaces for this configuration */
   /*     public void testGetUsbInterfaces()
        {
            UsbKnownTestConfig testConfig = new UsbKnownTestConfig(usbDevice);
            assertTrue( "num configs not equal", (usbConfig).getUsbInterfaces().equals (testConfig.getUsbInterfaces() )));
        }
   */

        /** @test the list of USB device interfaces for this configuration */
   /*     public void testGetInterfaces()
        {
            UsbKnownTestConfig testConfig = new UsbKnownTestConfig(usbDevice);
            assertTrue( "num configs not equal", (usbConfig.getInterfaces().equals (testConfig.getInterfaces() )));
        }
   */

        /** @test the list of USB device interfaces for this configuration */
   /*     public void testGetUsbInterface()
        {
            UsbKnownTestConfig testConfig = new UsbKnownTestConfig(usbDevice);
            assertTrue( "usb interface not equal", (usbConfig.getUsbInterface((byte)0).equals (testConfig.getUsbInterface((byte)0) )));
        }
   */
        /** @test the UsbDevice that has this config */
        public void testGetUsbDevice()
        {
            UsbKnownTestConfig testConfig = new UsbKnownTestConfig(usbDevice);
            assertTrue( "usb device not equal", (usbConfig.getUsbDevice().equals ( testConfig.getUsbDevice() )));
        }


        /** @test the descriptor for this UsbConfig */
        public void testConfigDescriptor()
        {
            UsbKnownTestConfig testConfig = new UsbKnownTestConfig(usbDevice);
            usbConfig.setConfigDescriptor( testConfig.getConfigDescriptor() );
            assertTrue( "num configs not equal", (usbConfig.getConfigDescriptor().equals ( testConfig.getConfigDescriptor()) ));
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

	private UsbDevice usbDevice;
        private UsbConfigImp usbConfig;

	//*************************************************************************
	// Class variables
        private static UsbKnownTestDeviceRegistry usbKnownTestDeviceRegistry = new UsbKnownTestDeviceRegistry();
}

