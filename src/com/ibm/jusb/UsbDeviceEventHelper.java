package com.ibm.jusb;

/*
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import java.util.*;

import javax.usb.event.*;

import com.ibm.jusb.util.*;

/**
 * Helper class to handle multiplexing UsbDeviceEvents to listeners.
 * @author Dan Streetman
 */
public class UsbDeviceEventHelper extends EventListenerHelper
{
	/** @param event The Event to fire. */
	public void errorEventOccurred(UsbDeviceErrorEvent event)
	{
		if (!hasListeners())
			return;

		ErrorEvent eE = new ErrorEvent();
		eE.udeE = event;
		addRunnable(eE);
	}

	/** @param event The Event to fire. */
	public void dataEventOccurred(UsbDeviceDataEvent event)
	{
		if (!hasListeners())
			return;

		DataEvent dE = new DataEvent();
		dE.uddE = event;
		addRunnable(dE);
	}

	/** @param event The Event to fire. */
	public void usbDeviceDetached(UsbDeviceEvent event)
	{
		if (!hasListeners())
			return;

		DetachEvent dE = new DetachEvent();
		dE.udE = event;
		addRunnable(dE);
	}

//FIXME - event firing should use a list copy or be sync'd with add/remove of listeners

	private class ErrorEvent implements Runnable
	{
		public void run()
		{
			List list = getEventListeners();
			for (int i=0; i<list.size(); i++)
				((UsbDeviceListener)list.get(i)).errorEventOccurred(udeE);
		}

		public UsbDeviceErrorEvent udeE = null;
	}

	private class DataEvent implements Runnable
	{
		public void run()
		{
			List list = getEventListeners();
			for (int i=0; i<list.size(); i++)
				((UsbDeviceListener)list.get(i)).dataEventOccurred(uddE);
		}

		public UsbDeviceDataEvent uddE = null;
	}

	private class DetachEvent implements Runnable
	{
		public void run()
		{
			List list = getEventListeners();
			for (int i=0; i<list.size(); i++)
				((UsbDeviceListener)list.get(i)).usbDeviceDetached(udE);
		}

		public UsbDeviceEvent udE = null;
	}
}
