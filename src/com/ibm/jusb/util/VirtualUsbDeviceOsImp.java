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
public class VirtualUsbDeviceOsImp extends AbstractUsbDeviceOsImp implements UsbDeviceOsImp
{
	public VirtualUsbDeviceOsImp() { }

	/**
	 * Synchronously submit a ControlUsbIrpImp.
	 * @param controlUsbIrpImp The ControlUsbIrpImp.
	 * @return The number of bytes transferred.
	 * @throws UsbException If the submission is unsuccessful.
	 */
	public void syncSubmit(ControlUsbIrpImp controlUsbIrpImp) throws UsbException
	{
		throw new UsbException("Not implemented");
	}

	/**
	 * Asynchronously submit a ControlUsbIrpImp.
	 * @param controlUsbIrpImp The ControlUsbIrpImp.
	 */
	public void asyncSubmit(ControlUsbIrpImp controlUsbIrpImp) throws UsbException
	{
		runnableManager.add(new AsyncRunnable(controlUsbIrpImp));
	}

	//**************************************************************************
	// Instance methods

	private RunnableManager runnableManager = new RunnableManager();

	//**************************************************************************
	// Class constants

	public static final String VIRTUAL_ROOT_HUB_MANUFACTURER = "JSR80 Reference Implementation (platform-independent section)";
	public static final String VIRTUAL_ROOT_HUB_PRODUCT = "JSR80 Virtual Root Hub";
	public static final String VIRTUAL_ROOT_HUB_SERIALNUMBER = "19741113";

	//**************************************************************************
	// Inner classes

	private class AsyncRunnable implements Runnable
	{
		public AsyncRunnable(ControlUsbIrpImp r) { controlUsbIrpImp = r; }

		public void run()
		{
			try {
				syncSubmit(controlUsbIrpImp);
			} catch ( UsbException uE ) {
				controlUsbIrpImp.setUsbException(uE);
				controlUsbIrpImp.complete();
			}
		}

		private ControlUsbIrpImp controlUsbIrpImp = null;
	}

}
