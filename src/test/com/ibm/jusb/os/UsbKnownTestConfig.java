package com.ibm.jusb.os;

/** 
 * Copyright (c) IBM Corporation, 2000
 * This software and documentation is the confidential and proprietary
 * information of IBM, Corp. ("Confidential Information").
 * Raleigh, NC USA
 */

import java.util.*;

import javax.usb.util.*;
import javax.usb.*;

/**
 * A Test for UsbConfig
 * @author Helen Li
 */

public class UsbKnownTestConfig
{
    /**
     * Create a fake UsbDevice with known values to match
     * the specified vendor and product IDs
     * @param vendorId the vendor ID of the known UsbDevice to get
     * @param productId the product ID of the known UsbDevice to get
     * @return a 'fake' UsbDevice with known values
     * @throws UsbException if the vendor and product ID pair does not match a known UsbDevice
     */
        public UsbKnownTestConfig(UsbDevice dev)
	{
		usbDevice = dev;
		if (isIBMLCD( dev.getVendorId(), dev.getProductId()) )
		{
			type=2;
                        configNum = 1;
			maxPower = 0;
			attributes = (byte)0x80;
                        configValue = 1;
			numInterfaces = 2;
		}
	
		if (isIBMKeyboard( dev.getVendorId(), dev.getProductId()) )
		{
			configNum = 1;
			maxPower = (byte)350;
			attributes = (byte)0xa0;
			numInterfaces = 4;
		}
	}


        public ConfigDescriptor getConfigDescriptor()
	{
		ConfigDescriptorImp configDescriptorImp = new ConfigDescriptorImp();

	  //      configDescriptorImp.setLength( length );
		configDescriptorImp.setType( type );
		configDescriptorImp.setNumInterfaces( numInterfaces );
		configDescriptorImp.setConfigValue( configValue );
	  //      configDescriptorImp.setConfigIndex( configIndex );
		configDescriptorImp.setAttributes( attributes );
		configDescriptorImp.setMaxPower( maxPower );

		return configDescriptorImp;
	}



	public byte getConfigNumber() { return configNum; }

	public byte getNumInterfaces() { return numInterfaces; }

	public byte getAttributes() {return attributes; }

	public byte getMaxPower() { return maxPower; }

	public UsbInfoList getUsbInterfaces() { return null; }

	public UsbInfoList getInterfaces() { return null; }

	public UsbInterface getUsbInterface (byte number) {return null; }

	public UsbDevice getUsbDevice() { return usbDevice; }

	private boolean isIBMLCD(short vendorID, short productID)
	{
		return ( 0x04b3 == vendorID && 0x4526 == productID);
	}

	private boolean isIBMKeyboard(short vendorID, short productID)
	{
		return ( 0x04b3 == vendorID && 0x4611 == productID);
	}


	//Class variables
	private byte configNum;
	private byte type;
	private byte numInterfaces;
	private byte attributes;
	private byte maxPower;
	private byte configValue;
	private UsbDevice usbDevice;
        
}

