package com.ibm.jusb.event;

/**
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
 * Implementation of UsbServicesListener.
 * @author Dan Streetman
 */
public class UsbServicesListenerImp extends EventListenerImp implements UsbServicesListener
{
	/** UsbDevices attached */
	public void usbDeviceAttached(UsbServicesEvent event)
	{
		if (!hasListeners())
			return;

		addRunnable( new AttachEvent(event) );
	}

	/** UsbDevices detached */
	public void usbDeviceDetached(UsbServicesEvent event)
	{
		if (!hasListeners())
			return;

		addRunnable( new DetachEvent(event) );
	}

	private class AttachEvent extends EventRunnable
	{
		public AttachEvent() { super(); }
		public AttachEvent(EventObject e) { super(e); }

		public void run()
		{
			List list = getEventListeners();

			synchronized (list) {
				for (int i=0; i<list.size(); i++)
					((UsbServicesListener)list.get(i)).usbDeviceAttached((UsbServicesEvent)event);
			}
		}
	}

	private class DetachEvent extends EventRunnable
	{
		public DetachEvent() { super(); }
		public DetachEvent(EventObject e) { super(e); }

		public void run()
		{
			List list = getEventListeners();

			synchronized (list) {
				for (int i=0; i<list.size(); i++)
					((UsbServicesListener)list.get(i)).usbDeviceDetached((UsbServicesEvent)event);
			}
		}
	}
}                                                                             
