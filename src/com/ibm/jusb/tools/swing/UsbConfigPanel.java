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
 * Class to display UsbConfig info.
 * @author Dan Streetman
 */
public class UsbConfigPanel extends UsbPanel
{
	public UsbConfigPanel(UsbConfig config)
	{
		super();
		usbConfig = config;
		string = "UsbConfig " + config.getConfigDescriptor().bConfigurationValue();
		
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
		String configString = null;

		try { configString = usbConfig.getConfigString(); } catch ( Exception e ) { configString = "Error : " + e.getMessage(); }

		appendln("Config Number : " + UsbUtil.unsignedInt(usbConfig.getConfigDescriptor().bConfigurationValue()));
		appendln("Is Active : " + usbConfig.isActive());
		appendln("Config String : " + configString);
		appendln("Attributes : " + UsbUtil.toHexString(usbConfig.getConfigDescriptor().bmAttributes()));
		appendln("Max Power (mA) : " + (2 * UsbUtil.unsignedInt(usbConfig.getConfigDescriptor().bMaxPower()))); /* units are 2mA */
		appendln("Number of UsbInterfaces : " + UsbUtil.unsignedInt(usbConfig.getConfigDescriptor().bNumInterfaces()));
	}

	private UsbConfig usbConfig = null;
}
