package com.ibm.jusb;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import javax.usb.UsbDeviceDescriptor;

/*
 * UsbDeviceDescriptor implementation.
 * @author Dan Streetman
 */
public class UsbDeviceDescriptorImp extends UsbDescriptorImp implements UsbDeviceDescriptor
{
	/**
	 * Constructor.
	 * @param bLength This descriptor's bLength.
	 * @param bDescriptorType This descriptor's bDescriptorType.
	 * @param bcdUSB This descriptor's bcdUSB.
	 * @param bDeviceClass This descriptor's bDeviceClass.
	 * @param bDeviceSubClass This descriptor's bDeviceSubClass.
	 * @param bDeviceProtocol This descriptor's bDeviceProtocol.
	 * @param bMaxPacketSize0 This descriptor's bMaxPacketSize0.
	 * @param idVendor This descriptor's idVendor.
	 * @param idProduct This descriptor's idProduct.
	 * @param bcdDevice This descriptor's bcdDevice.
	 * @param iManufacturer This descriptor's iManufacturer.
	 * @param iProduct This descriptor's iProduct.
	 * @param iSerialNumber This descriptor's iSerialNumber.
	 * @param bNumConfigurations This descriptor's bNumConfigurations.
	 */
	public UsbDeviceDescriptorImp( byte bLength, byte bDescriptorType,
		short bcdUSB, byte bDeviceClass, byte bDeviceSubClass, byte bDeviceProtocol,
		byte bMaxPacketSize0, short idVendor, short idProduct, short bcdDevice,
		byte iManufacturer, byte iProduct, byte iSerialNumber, byte bNumConfigurations )
	{
		super(bLength, bDescriptorType);
		this.bcdUSB = bcdUSB;
		this.bDeviceClass = bDeviceClass;
		this.bDeviceSubClass = bDeviceSubClass;
		this.bDeviceProtocol = bDeviceProtocol;
		this.bMaxPacketSize0 = bMaxPacketSize0;
		this.idVendor = idVendor;
		this.idProduct = idProduct;
		this.bcdDevice = bcdDevice;
		this.iManufacturer = iManufacturer;
		this.iProduct = iProduct;
		this.iSerialNumber = iSerialNumber;
		this.bNumConfigurations = bNumConfigurations;
	}

    /**
	 * Get this descriptor's bcdUSB.
	 * @return This descriptor's bcdUSB.
	 * @see javax.usb.util.UsbUtil#unsignedInt(short) This is unsigned.
	 */
    public short bcdUSB() { return bcdUSB; }

    /**
	 * Get this descriptor's bDeviceClass.
	 * @return This descriptor's bDeviceClass.
	 * @see javax.usb.util.UsbUtil#unsignedInt(byte) This is unsigned.
	 */
    public byte bDeviceClass() { return bDeviceClass; }

	/**
	 * Get this descriptor's bDeviceSubClass.
	 * @return This descriptor's bDeviceSubClass.
	 * @see javax.usb.util.UsbUtil#unsignedInt(byte) This is unsigned.
	 */
    public byte bDeviceSubClass() { return bDeviceSubClass; }

    /**
	 * Get this descriptor's bDeviceProtocol.
	 * @return This descriptor's bDeviceProtocol.
	 * @see javax.usb.util.UsbUtil#unsignedInt(byte) This is unsigned.
	 */
    public byte bDeviceProtocol() { return bDeviceProtocol; }

    /**
	 * Get this descriptor's bMaxPacketSize.
	 * @return This descriptor's bMaxPacketSize.
	 * @see javax.usb.util.UsbUtil#unsignedInt(byte) This is unsigned.
	 */
    public byte bMaxPacketSize0() { return bMaxPacketSize0; }

    /**
	 * Get this descriptor's idVendor.
	 * @return This descriptor's idVendor.
	 * @see javax.usb.util.UsbUtil#unsignedInt(short) This is unsigned.
	 */
    public short idVendor() { return idVendor; }

    /**
	 * Get this descriptor's idProduct.
	 * @return This descriptor's idProduct.
	 * @see javax.usb.util.UsbUtil#unsignedInt(short) This is unsigned.
	 */
    public short idProduct() { return idProduct; }

    /**
	 * Get this descriptor's bcdDevice.
	 * @return This descriptor's bcdDevice.
	 * @see javax.usb.util.UsbUtil#unsignedInt(short) This is unsigned.
	 */
    public short bcdDevice() { return bcdDevice; }

    /**
	 * Get this descriptor's iManufacturer.
	 * @return This descriptor's iManufacturer.
	 * @see javax.usb.util.UsbUtil#unsignedInt(byte) This is unsigned.
	 */
    public byte iManufacturer() { return iManufacturer; }

    /**
	 * Get this descriptor's iProduct.
	 * @return This descriptor's iProduct.
	 * @see javax.usb.util.UsbUtil#unsignedInt(byte) This is unsigned.
	 */
    public byte iProduct() { return iProduct; }

    /**
	 * Get this descriptor's iSerialNumber.
	 * @return This descriptor's iSerialNumber.
	 * @see javax.usb.util.UsbUtil#unsignedInt(byte) This is unsigned.
	 */
    public byte iSerialNumber() { return iSerialNumber; }

    /**
	 * Get this descriptor's bNumConfigurations.
	 * @return This descriptor's bNumConfigurations.
	 * @see javax.usb.util.UsbUtil#unsignedInt(byte) This is unsigned.
	 */
    public byte bNumConfigurations() { return bNumConfigurations; }

	/**
	 * Compare this to an Object.
	 * @param object The Object.
	 * @return If this is equal to the Object.
	 */
	public boolean equals(Object object)
	{
		if (!super.equals(object))
			return false;

		UsbDeviceDescriptorImp desc = null;

		try { desc = (UsbDeviceDescriptorImp)object; }
		catch ( ClassCastException ccE ) { return false; }

		return
			bcdUSB() == desc.bcdUSB() &&
			bDeviceClass() == desc.bDeviceClass() &&
			bDeviceSubClass() == desc.bDeviceSubClass() &&
			bDeviceProtocol() == desc.bDeviceProtocol() &&
			bMaxPacketSize0() == desc.bMaxPacketSize0() &&
			idVendor() == desc.idVendor() &&
			idProduct() == desc.idProduct() &&
			bcdDevice() == desc.bcdDevice() &&
			iManufacturer() == desc.iManufacturer() &&
			iProduct() == desc.iProduct() &&
			iSerialNumber() == desc.iSerialNumber() &&
			bNumConfigurations() == desc.bNumConfigurations();
	}

    private short bcdUSB = 0x0000;
    private byte bDeviceClass = 0x00;
    private byte bDeviceSubClass = 0x00;
    private byte bDeviceProtocol = 0x00;
    private byte bMaxPacketSize0 = 0x00;
    private short idVendor = 0x0000;
    private short idProduct = 0x0000;
    private short bcdDevice = 0x0000;
    private byte iManufacturer = 0x00;
    private byte iProduct = 0x00;
    private byte iSerialNumber = 0x00;
    private byte bNumConfigurations = 0x00;
}
