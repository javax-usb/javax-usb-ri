package com.ibm.jusb.tools.pipeview;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the IBM Public License.
 *
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import javax.usb.*;
import javax.usb.os.*;
import javax.usb.event.*;
import javax.usb.util.*;
import com.ibm.jusb.tools.*;

/**
 * Panel to construct and send Requests over a device's DCP.
 * @author Dan Streetman
 * @version 0.0.1 (JDK 1.1.x)
 */
public class RequestPanel extends JPanel
{
	/** Constructor */
	public RequestPanel( UsbDevice device )
	{
		super();
		setUsbDevice( device );
		init();
	}

	//*************************************************************************
	// Protected methods

	protected void init()
	{
		add( new JLabel( "Not supported!" ) );
	}

	//*************************************************************************
	// Private methods

	/** @param device the UsbDevice to use */
	private void setUsbDevice( UsbDevice device ) { usbDevice = device; }

	/** @return the UsbDevice in use */
	private UsbDevice getUsbDevice() { return usbDevice; }

	//*************************************************************************
	// Instance variables

	private UsbDevice usbDevice = null;

}
