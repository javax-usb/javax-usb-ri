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
import javax.usb.util.*;

/**
 * ConfigDescriptor implementation.
 * @author E. Michael Maximilien
 * @author Dan Streetman
 */
public class ConfigDescriptorImp extends DescriptorImp implements ConfigDescriptor
{
	/**
	 * Constructor.
	 * @param length Descriptor length.
	 * @param type Descriptor type.
	 * @param totalLength Descriptor Total Length.
	 * @param numInterfaces Number of interfaces.
	 * @param configValue The ConfigValue.
	 * @param configIndex The ConfigIndex.
	 * @param attributes The attributes.
	 * @param maxPower The max power.
	 */
	public ConfigDescriptorImp( byte bLength, byte bType,
		short wTotalLength, byte bNumInterfaces, byte bConfigurationValue,
		byte iConfiguration, byte bmAttributes, byte bMaxPower )
	{
		super(bLength, bType);
		this.wTotalLength = wTotalLength;
		this.bNumInterfaces = bNumInterfaces;
		this.bConfigurationValue = bConfigurationValue;
		this.iConfiguration = iConfiguration;
		this.bmAttributes = bmAttributes;
		this.bMaxPower = bMaxPower;
	}

	/**
	 * Get this descriptor's wTotalLength.
	 * @return This descriptor's wTotalLength.
	 * @see javax.usb.util.UsbUtil#unsignedInt(short) This is unsigned.
	 */
	public short wTotalLength() { return wTotalLength; }

    /**
	 * Get this descriptor's bNumInterfaces.
	 * @return This descriptor's bNumInterfaces.
	 * @see javax.usb.util.UsbUtil#unsignedInt(byte) This is unsigned.
	 */
    public byte bNumInterfaces() { return bNumInterfaces; }

    /**
	 * Get this descriptor's bConfigurationValue.
	 * @return This descriptor's bConfigurationValue.
	 * @see javax.usb.util.UsbUtil#unsignedInt(byte) This is unsigned.
	 */
    public byte bConfigurationValue() { return bConfigurationValue; }

    /**
	 * Get this descriptor's iConfiguration.
	 * @return This descriptor's iConfiguration.
	 * @see javax.usb.util.UsbUtil#unsignedInt(byte) This is unsigned.
	 */
    public byte iConfiguration() { return iConfiguration; }

    /**
	 * Get this descriptor's bmAttributes.
	 * @return This descriptor's bmAttributes.
	 * @see javax.usb.util.UsbUtil#unsignedInt(byte) This is unsigned.
     */
    public byte bmAttributes() { return bmAttributes; }

    /**
	 * Get this descriptor's bMaxPower.
	 * @return This descriptor's bMaxPower.
	 * @see javax.usb.util.UsbUtil#unsignedInt(byte) This is unsigned.
	 */
    public byte bMaxPower() { return bMaxPower; }

	private short wTotalLength = 0x0000;
	private byte bNumInterfaces = 0x00;
	private byte bConfigurationValue = 0x00;
	private byte iConfiguration = 0x00;
	private byte bmAttributes = 0x00;
	private byte bMaxPower = 0x00;
}
