package com.ibm.jusb.tools.swing;

/*
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import java.io.*;
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
 * Class to display UsbInterface info.
 * @author Dan Streetman
 */
public class UsbInterfacePanel extends UsbPanel
{
	public UsbInterfacePanel(UsbInterface iface)
	{
		super();
		usbInterface = iface;
		createClaimPanel();
		string = "UsbInterface " + iface.getUsbInterfaceDescriptor().bInterfaceNumber();
		
		// add empty space, make the UI more consistent
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
		String ifaceString = null;
		int activeSetting = -1;

		try { ifaceString = usbInterface.getInterfaceString(); } catch ( Exception e ) { ifaceString = "Error : " + e.getMessage(); }
		try { activeSetting = UsbUtil.unsignedInt(usbInterface.getActiveSettingNumber()); } catch ( NotActiveException naE ) { }

		appendln("Interface Number : " + UsbUtil.unsignedInt(usbInterface.getUsbInterfaceDescriptor().bInterfaceNumber()));
		appendln("Is Active : " + usbInterface.isActive());
		appendln("Is Claimed : " + usbInterface.isClaimed());
		appendln("Alternate Setting : " + UsbUtil.unsignedInt(usbInterface.getUsbInterfaceDescriptor().bAlternateSetting()));
		appendln("Active Alternate Setting Number : " + (0 > activeSetting ? "Not Active" : new Integer(activeSetting).toString()));
		appendln("Interface Class : " + UsbUtil.toHexString(usbInterface.getUsbInterfaceDescriptor().bInterfaceClass()));
		appendln("Interface Subclass : " + UsbUtil.toHexString(usbInterface.getUsbInterfaceDescriptor().bInterfaceSubClass()));
		appendln("Interface Protocol : " + UsbUtil.toHexString(usbInterface.getUsbInterfaceDescriptor().bInterfaceProtocol()));
		appendln("Interface String : " + ifaceString);
		appendln("Number of Alternate Settings : " + usbInterface.getNumSettings());
		appendln("Number of UsbEndpoints : " + UsbUtil.unsignedInt(usbInterface.getUsbInterfaceDescriptor().bNumEndpoints()));
	}

	protected void createClaimPanel()
	{
		claimButton.addActionListener(claimListener);
		releaseButton.addActionListener(releaseListener);

		claimPanel.add(claimButton);
		claimPanel.add(releaseButton);

		add(claimPanel);
	}

	private UsbInterface usbInterface = null;

	private JPanel claimPanel = new JPanel();
	private JButton claimButton = new JButton("Claim");
	private ActionListener claimListener = new ActionListener() {
			public void actionPerformed(ActionEvent aE)
			{
				try { usbInterface.claim(); }
				catch ( UsbException uE ) { JOptionPane.showMessageDialog(null, "Could not claim UsbInterface : " + uE.getMessage()); }
				catch ( NotActiveException naE ) { JOptionPane.showMessageDialog(null, "Could not claim UsbInterface : " + naE.getMessage()); }
				refresh();
			}
		};
	private JButton releaseButton = new JButton("Release");
	private ActionListener releaseListener = new ActionListener() {
			public void actionPerformed(ActionEvent aE)
			{
				try { usbInterface.release(); }
				catch ( UsbException uE ) { JOptionPane.showMessageDialog(null, "Could not release UsbInterface : " + uE.getMessage()); }
				catch ( NotActiveException naE ) { JOptionPane.showMessageDialog(null, "Could not release UsbInterface : " + naE.getMessage()); }
				refresh();
			}
		};
}
