package com.ibm.jusb;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import javax.usb.*;

import com.ibm.jusb.os.*;

/**
 * Virtual root UsbHub implementation.
 * @author Dan Streetman
 */
public class VirtualRootUsbHubImp extends UsbHubImp implements UsbHub
{
	public VirtualRootUsbHubImp()
	{
		super(virtualDeviceDescriptor, new DefaultUsbDeviceOsImp());
		setSpeed(UsbConst.DEVICE_SPEED_FULL);
		init();
	}

	//**************************************************************************
	// Public methods

	public void init()
	{
		UsbConfigImp virtualConfig = new UsbConfigImp(this, virtualConfigDescriptor);
		UsbInterfaceImp virtualInterface = new UsbInterfaceImp(virtualConfig, virtualInterfaceDescriptor, new DefaultUsbInterfaceOsImp());

		virtualConfig.addUsbInterfaceImp(virtualInterface);

		addUsbConfigImp(virtualConfig);
		setActiveUsbConfigNumber(CONFIG_NUM);
	}

	/** No connect operation */
	public void connect(UsbHubImp hub, byte portNumber) throws UsbException
	{
		throw new UsbException("Cannot connect Virtual Root UsbHub");
	}

	/** No disconnect */
	public void disconnect() { }

	/** No UsbPort use */
	public void setParentUsbPortImp(UsbPortImp port) { }

	/** No UsbPort use */
	public UsbPortImp getParentUsbPortImp() { return null; }

	//**************************************************************************
	// Class constants

	public static final String VIRTUAL_ROOT_HUB_MANUFACTURER = "JSR80 Reference Implementation (platform-independent section)";
	public static final String VIRTUAL_ROOT_HUB_PRODUCT = "JSR80 Virtual Root Hub";
	public static final String VIRTUAL_ROOT_HUB_SERIALNUMBER = "19741113";

	public static final short VENDOR_ID = (short)0xffff;
	public static final short PRODUCT_ID = (short)0xffff;
	public static final short DEVICE_BCD = (short)0x0000;
	public static final short USB_BCD = (short)0x0101;

	public static final byte CONFIG_NUM = (byte)0x01;
	public static final short CONFIG_TOTAL_LEN = (short)0x00ff;

	public static final byte INTERFACE_NUM = (byte)0x00;
	public static final byte SETTING_NUM = (byte)0x00;

	public static final DeviceDescriptorImp virtualDeviceDescriptor =
		new DeviceDescriptorImp( UsbConst.DESCRIPTOR_MIN_LENGTH_DEVICE,
								 UsbConst.DESCRIPTOR_TYPE_DEVICE,
								 USB_BCD,
								 UsbConst.HUB_CLASSCODE,
								 (byte)0x00, /* subclass */
								 (byte)0x00, /* protocol */
								 (byte)0x08, /* maxpacketsize */
								 VENDOR_ID,
								 PRODUCT_ID,
								 DEVICE_BCD,
								 (byte)0x00, /* man index */
								 (byte)0x00, /* prod index */
								 (byte)0x00, /* serial index */
								 (byte)0x01 /* n configs */ );

	public static final ConfigDescriptorImp virtualConfigDescriptor =
		new ConfigDescriptorImp( UsbConst.DESCRIPTOR_MIN_LENGTH_CONFIG,
								 UsbConst.DESCRIPTOR_TYPE_CONFIG,
								 (short)CONFIG_TOTAL_LEN,
								 (byte)0x01, /* n interfaces */
								 CONFIG_NUM,
								 (byte)0x00, /* config index */
								 (byte)0x80, /* attr */
								 (byte)0x00 ); /* maxpower */

	public static final InterfaceDescriptorImp virtualInterfaceDescriptor =
		new InterfaceDescriptorImp( UsbConst.DESCRIPTOR_MIN_LENGTH_INTERFACE,
									UsbConst.DESCRIPTOR_TYPE_INTERFACE,
									INTERFACE_NUM,
									SETTING_NUM,
									(byte)0x00, /* num endpoints */
									UsbConst.HUB_CLASSCODE,
									(byte)0x00, /* subclass */
									(byte)0x00, /* protocol */
									(byte)0x00 ); /* iface index */

}
