package com.ibm.jusb.util;

/*
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import java.util.*;

import javax.usb.*;

import com.ibm.jusb.*;
import com.ibm.jusb.os.*;

/**
 * Virtual UsbDeviceOsImp implementation.
 * @author Dan Streetman
 */
public class VirtualUsbDeviceOsImp implements UsbDeviceOsImp
{
	public VirtualUsbDeviceOsImp() { }

	/**
	 * Synchronously submit a RequestImp.
	 * @param requestImp The RequestImp.
	 * @return The number of bytes transferred.
	 * @throws UsbException If the submission is unsuccessful.
	 */
	public void syncSubmit(RequestImp requestImp) throws UsbException
	{
		byte[] data = requestImp.getData();
		byte bmRequestType = data[0];
		byte bRequest = data[1];
		short wValue = (short)((data[3] << 8) | data[2]);
		short wIndex = (short)((data[5] << 8) | data[4]);

		/* handle requests */

		throw new UsbException("Not implemented");
	}

	/**
	 * Synchronously submit a List of RequestImps.
	 * @param list The List.
	 * @throws UsbException If the submission is unsuccessful.
	 */
	public void syncSubmit(List list) throws UsbException
	{
		for (int i=0; i<list.size(); i++)
			syncSubmit( (RequestImp)list.get(i) );
	}

	/**
	 * Asynchronously submit a RequestImp.
	 * @param requestImp The RequestImp.
	 * @throws UsbException If the submission is unsuccessful.
	 */
	public void asyncSubmit(final RequestImp requestImp) throws UsbException
	{
		Runnable r = new Runnable() {
				public void run()
				{
					try {
						syncSubmit(requestImp);
					} catch ( UsbException uE ) {
						requestImp.setUsbException(uE);
						requestImp.setCompleted(true);
					}
				}
			};

		Thread t = new Thread(r);

		t.setDaemon(true);
		t.start();
	}

}
