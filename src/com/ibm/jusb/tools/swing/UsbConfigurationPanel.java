package com.ibm.jusb.tools.swing;

/*
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import java.util.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;

import javax.usb.*;
import javax.usb.util.*;
import javax.usb.event.*;

/**
 * Class to display UsbConfiguration info.
 * @author Dan Streetman
 */
public class UsbConfigurationPanel extends UsbPanel
{
	public UsbConfigurationPanel(UsbConfiguration config)
	{
		super();
		usbConfiguration = config;
		string = "UsbConfiguration " + config.getUsbConfigurationDescriptor().bConfigurationValue();
		
		add(Box.createVerticalGlue());
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(Box.createRigidArea(new Dimension(0,10)));
		add(panel);
		
		refresh();
	}

	protected void refresh()
	{
		clear();
		appendln(string);
		initText();
	}

	protected void initText()
	{
		String configurationString = null;

		try { configurationString = usbConfiguration.getConfigurationString(); } catch ( Exception e ) { configurationString = "Error : " + e.getMessage(); }

		appendln("Configuration Number : " + UsbUtil.unsignedInt(usbConfiguration.getUsbConfigurationDescriptor().bConfigurationValue()));
		appendln("Is Active : " + usbConfiguration.isActive());
		appendln("Configuration String : " + configurationString);
		appendln("Attributes : " + UsbUtil.toHexString(usbConfiguration.getUsbConfigurationDescriptor().bmAttributes()));
		appendln("Max Power (mA) : " + (2 * UsbUtil.unsignedInt(usbConfiguration.getUsbConfigurationDescriptor().bMaxPower()))); /* units are 2mA */
		appendln("Number of UsbInterfaces : " + UsbUtil.unsignedInt(usbConfiguration.getUsbConfigurationDescriptor().bNumInterfaces()));
	}

	private UsbConfiguration usbConfiguration = null;
}
