package com.ibm.jusb.os;

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
import com.ibm.jusb.util.*;

/**
 * Default UsbDeviceOsImp implementation.
 * <p>
 * This is an optional default implementation that handles all methods.  Those
 * methods may be overridden by the implementation if desired.  The implementation
 * is not required to extend this class.  All methods are implemented using the
 * {@link #asyncSubmit(UsbIrpImp) asyncSubmit(UsbIrpImp)} method; this method,
 * at a minimum, must be implemented in order to provide any actual functionality.
 * <p>
 * The default action for this class is to throw a UsbException for all accesses,
 * except for Standard requests.
 * @author Dan Streetman
 */
public class DefaultUsbDeviceOsImp implements UsbDeviceOsImp
{
	public DefaultUsbDeviceOsImp() { }

	/**
	 * Synchronously submit a UsbControlIrpImp.
	 * <p>
	 * This method is implemented using {@link #asyncSubmit(UsbControlIrpImp) asyncSubmit(UsbControlIrpImp)}.
	 * @param usbControlIrpImp The UsbControlIrpImp.
	 * @throws UsbException If the submission is unsuccessful.
	 */
	public void syncSubmit(UsbControlIrpImp usbControlIrpImp) throws UsbException
	{
		asyncSubmit(usbControlIrpImp);

		usbControlIrpImp.waitUntilComplete();

		if (usbControlIrpImp.isUsbException())
			throw usbControlIrpImp.getUsbException();
	}

	/**
	 * Asynchronously submit a UsbControlIrpImp.
	 * <p>
	 * This is implemented to fail with a generic UsbException, except for Standard requests.
	 * The implementation should override (at least) this method.
	 * @param usbControlIrpImp The UsbControlIrpImp.
	 * @exception UsbException If the submission is unsucessful.
	 */
	public void asyncSubmit(UsbControlIrpImp usbControlIrpImp) throws UsbException
	{
		if (null == runnableManager)
			runnableManager = new RunnableManager();

		runnableManager.add(new AsyncRunnable(usbControlIrpImp));
	}

	/**
	 * Synchronously submit a List of UsbControlIrpImps.
	 * <p>
	 * This method is implemented using {@link #syncSubmit(UsbControlIrpImp) syncSubmit(UsbControlIrpImp)}.
	 * If an UsbException occurrs, it is thrown immediately and any remaining UsbControlIrpImps are not submitted nor modified.
	 * @param list The List.
	 */
	public void syncSubmit(List list) throws UsbException
	{
		for (int i=0; i<list.size(); i++)
			syncSubmit((UsbControlIrpImp)list.get(i));
	}

	/**
	 * Asynchronously submit a List of UsbControlIrpImps.
	 * <p>
	 * This method is implemented using {@link #asyncSubmit(UsbControlIrpImp) asyncSubmit(UsbControlIrpImp)}.
	 * If an UsbException occurrs, it is thrown immediately and any remaining UsbControlIrpImps are not submitted nor modified.
	 * @param list The List.
	 */
	public void asyncSubmit(List list) throws UsbException
	{
		for (int i=0; i<list.size(); i++)
			asyncSubmit((UsbControlIrpImp)list.get(i));
	}

	/**
	 * Internally handle a submission.
	 * <p>
	 * This is the default action, which handles Standard requests but
	 * throws UsbException for all other requests.
	 * @param usbControlIrpImp The UsbControlIrpImp.
	 * @exception UsbException If the request is not a Standard request.
	 */
	protected void internalSubmit(UsbControlIrpImp usbControlIrpImp) throws UsbException
	{
//FIXME - implement standard requests
throw new UsbException("Not implemented yet");
	}

	protected RunnableManager runnableManager = null;

	private class AsyncRunnable implements Runnable
	{
		public AsyncRunnable(UsbControlIrpImp r) { usbControlIrpImp = r; }

		public void run()
		{
			try {
				internalSubmit(usbControlIrpImp);
			} catch ( UsbException uE ) {
				usbControlIrpImp.setActualLength(0);
				usbControlIrpImp.setUsbException(uE);
				usbControlIrpImp.complete();
			}
		}

		private UsbControlIrpImp usbControlIrpImp = null;
	}

}
