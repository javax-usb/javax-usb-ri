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
 * UsbPipe platform-independent implementation for Control-type pipes.
 * @author Dan Streetman
 */
public class ControlUsbPipeImp extends UsbPipeImp implements UsbPipe
{
	/**
	 * Constructor.
	 * @param ep The UsbEndpointImp.
	 * @param pipe The platform-dependent pipe implementation.
	 */
	public ControlUsbPipeImp( UsbEndpointImp ep, ControlUsbPipeOsImp pipe ) { super(ep,pipe); }

	/**
	 * Control pipes cannot handle raw byte[] submissions.
	 * <p>
	 * Since Control pipes require a setup packet, raw byte[]s are disallowed.
	 * This will throw UsbException.
	 * @exception UsbException Raw byte[]s cannot be used on Control pipes.
	 */
	public int syncSubmit( byte[] data ) throws UsbException
	{
		throw new UsbException("Control pipes require a setup packet, so raw byte[] submission cannot be used.");
	}

	/**
	 * Control pipes cannot handle raw byte[] submissions.
	 * <p>
	 * Since Control pipes require a setup packet, raw byte[]s are disallowed.
	 * This will throw UsbException.
	 * @exception UsbException Raw byte[]s cannot be used on Control pipes.
	 */
	public UsbIrp asyncSubmit( byte[] data ) throws UsbException
	{
		throw new UsbException("Control pipes require a setup packet, so raw byte[] submission cannot be used.");
	}

	/**
	 * Convert a ControlUsbIrp to ControlUsbIrpImp.
	 * <p>
	 * If the UsbIrp is not a ControlUsbIrp, a UsbException is thrown.
	 * @param irp The ControlUsbIrp to convert.
	 */
	protected UsbIrpImp usbIrpToUsbIrpImp(UsbIrp irp) throws UsbException
	{
		ControlUsbIrp controlUsbIrp = null;
		try {
			controlUsbIrp = (ControlUsbIrp)irp;
		} catch ( ClassCastException ccE ) {
			throw new UsbException("Control pipes require a setup packet per submission, so only ControlUsbIrps can be submitted.");
		}

		ControlUsbIrpImp controlUsbIrpImp = null;
		try {
			controlUsbIrpImp = (ControlUsbIrpImp)controlUsbIrp;
		} catch ( ClassCastException ccE ) {
			controlUsbIrpImp = new ControlUsbIrpImp(controlUsbIrp);
		}

		setupUsbIrpImp(controlUsbIrpImp);

		return controlUsbIrpImp;
	}
}

