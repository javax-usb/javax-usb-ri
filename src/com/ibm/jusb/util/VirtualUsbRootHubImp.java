package com.ibm.jusb.util;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import javax.usb.*;

import com.ibm.jusb.*;

/**
 * Virtual UsbRootHub implementation.
 * @author Dan Streetman
 */
public class VirtualUsbRootHubImp extends UsbRootHubImp implements UsbRootHub
{
	public VirtualUsbRootHubImp()
	{
		super(virtualDeviceDescriptor, new VirtualUsbDeviceOsImp());
	}

	//**************************************************************************
	// Class constants

	public static final short VENDOR_ID = (short)0xffff;
	public static final short PRODUCT_ID = (short)0xffff;
	public static final short DEVICE_BCD = (short)0x0000;
	public static final short USB_BCD = (short)0x0101;

	public static final DeviceDescriptorImp virtualDeviceDescriptor =
		new DeviceDescriptorImp( DescriptorConst.DESCRIPTOR_MIN_LENGTH_DEVICE,
								 DescriptorConst.DESCRIPTOR_TYPE_DEVICE,
								 DescriptorConst.DEVICE_CLASS_HUB,
								 (byte)0x00, /* subclass */
								 (byte)0x00, /* protocol */
								 (byte)0x08, /* maxpacketsize */
								 (byte)0x00, /* man index */
								 (byte)0x00, /* prod index */
								 (byte)0x00, /* serial index */
								 (byte)0x01, /* n configs */
								 VENDOR_ID,
								 PRODUCT_ID,
								 DEVICE_BCD,
								 USB_BCD );

}
