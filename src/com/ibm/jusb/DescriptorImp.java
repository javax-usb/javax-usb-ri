package com.ibm.jusb;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import javax.usb.Descriptor;

/**
 * Descriptor implementation.
 * @author Dan Streetman
 */
public class DescriptorImp implements Descriptor
{
	public DescriptorImp( byte bLength, byte bDescriptorType )
	{
		this.bLength = bLength;
		this.bDescriptorType = bDescriptorType;
	}
	
    /**
	 * Get this descriptor's bLength.
	 * @return This descriptor's bLength.
	 * @see javax.usb.util.UsbUtil#unsignedInt(byte) This is unsigned.
	 */
    public byte bLength() { return bLength; }

    /**
	 * Get this descriptor's bDescriptorType.
	 * @return This descriptor's bDescriptorType.
	 * @see javax.usb.util.UsbUtil#unsignedInt(byte) This is unsigned.
	 */
    public byte bDescriptorType() { return bDescriptorType; }

	/**
	 * Compare this to an Object.
	 * @param object The Object to compare to.
	 * @return If this is equal to the Object.
	 */
	public boolean equals(Object object)
	{
		if (null == object)
			return false;

		DescriptorImp desc = null;

		try { desc = (DescriptorImp)object; }
		catch ( ClassCastException ccE ) { return false; }

		return
			bLength() == desc.bLength() &&
			bDescriptorType() == desc.bDescriptorType();
	}

	private byte bLength = 0x00;
	private byte bDescriptorType = 0x00;
}
