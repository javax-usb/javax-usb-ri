package com.ibm.jusb;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import java.util.*;

import javax.usb.*;
import javax.usb.util.*;
import javax.usb.event.*;

import com.ibm.jusb.os.*;
import com.ibm.jusb.util.*;

/**
 * ControlUsbIrp implementation.
 * <p>
 * This is the same as UsbIrpImp, except this contains Control-specific
 * setup packet information.
 * @author Dan Streetman
 */
public class ControlUsbIrpImp extends UsbIrpImp implements ControlUsbIrp
{
	/**
	 * Constructor.
	 * @param bmRequestType The bmRequestType.
	 * @param bRequest The bRequest.
	 * @param wValue The wValue.
	 * @param wIndex The wIndex.
	 */
	public ControlUsbIrpImp(byte bmRequestType, byte bRequest, short wValue, short wIndex)
	{
		super();
		this.bmRequestType = bmRequestType;
		this.bRequest = bRequest;
		this.wValue = wValue;
		this.wIndex = wIndex;
	}

	/**
	 * Constructor.
	 * @param controlUsbIrp The ControlUsbIrp this should wrap.
	 */
	public ControlUsbIrpImp(ControlUsbIrp controlUsbIrp)
	{
		super(controlUsbIrp);
		this.bmRequestType = controlUsbIrp.bmRequestType();
		this.bRequest = controlUsbIrp.bRequest();
		this.wValue = controlUsbIrp.wValue();
		this.wIndex = controlUsbIrp.wIndex();
	}

	/**
	 * Get the bmRequestType.
	 * @return The bmRequestType.
	 */
	public byte bmRequestType() { return bmRequestType; }

	/**
	 * Get the bRequest.
	 * @return The bRequest.
	 */
	public byte bRequest() { return bRequest; }

	/**
	 * Get the wValue.
	 * @return The wValue.
	 */
	public short wValue() { return wValue; }

	/**
	 * Get the wIndex.
	 * @return The wIndex.
	 */
	public short wIndex() { return wIndex; }

	/**
	 * If this is a SET_CONFIGURATION UsbIrp.
	 * @return If this is a SET_CONFIGURATION UsbIrp.
	 */
	public boolean isSetConfiguration()
	{
		return (bmRequestType() == REQUESTTYPE_SET_CONFIGURATION) && (bRequest() == UsbConst.REQUEST_SET_CONFIGURATION);
	}

	/**
	 * If this is a SET_INTERFACE UsbIrp.
	 * @return If this is a SET_INTERFACE UsbIrp.
	 */
	public boolean isSetInterface()
	{
		return (bmRequestType() == REQUESTTYPE_SET_INTERFACE) && (bRequest() == UsbConst.REQUEST_SET_INTERFACE);
	}

	protected byte bmRequestType = 0x00;
	protected byte bRequest = 0x00;
	protected short wValue = 0x0000;
	protected short wIndex = 0x0000;

	private static final byte REQUESTTYPE_SET_CONFIGURATION =
		UsbConst.REQUESTTYPE_DIRECTION_OUT | UsbConst.REQUESTTYPE_TYPE_STANDARD | UsbConst.REQUESTTYPE_RECIPIENT_DEVICE;
	private static final byte REQUESTTYPE_SET_INTERFACE =
		UsbConst.REQUESTTYPE_DIRECTION_OUT | UsbConst.REQUESTTYPE_TYPE_STANDARD | UsbConst.REQUESTTYPE_RECIPIENT_INTERFACE;
}
