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
		string = "UsbInterface " + UsbUtil.unsignedInt(iface.getUsbInterfaceDescriptor().bInterfaceNumber());

		if (1 < iface.getNumSettings())
			string += " setting " + UsbUtil.unsignedInt(iface.getUsbInterfaceDescriptor().bAlternateSetting());

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
		String activeSetting = "Not Active";

		try { ifaceString = usbInterface.getInterfaceString(); } catch ( Exception e ) { ifaceString = "Error : " + e.getMessage(); }
		try { activeSetting = Integer.toString(UsbUtil.unsignedInt(usbInterface.getActiveSettingNumber())); } catch ( UsbNotActiveException unaE ) { }

		if (null == ifaceString) ifaceString = NULL_STRING;

		appendln("Interface String : " + ifaceString);
		appendln("Is Active : " + usbInterface.isActive());
		appendln("Is Claimed : " + usbInterface.isClaimed());
		appendln("Active Alternate Setting Number : " + activeSetting);
		appendln("Number of Alternate Settings : " + usbInterface.getNumSettings());

		/* Note - this relies on the IBM Reference Implementation to provide a descriptive String */
		append(usbInterface.getUsbInterfaceDescriptor().toString());
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
				catch ( UsbNotActiveException unaE ) { JOptionPane.showMessageDialog(null, "Could not claim UsbInterface : " + unaE.getMessage()); }
				refresh();
			}
		};
	private JButton releaseButton = new JButton("Release");
	private ActionListener releaseListener = new ActionListener() {
			public void actionPerformed(ActionEvent aE)
			{
				try { usbInterface.release(); }
				catch ( UsbException uE ) { JOptionPane.showMessageDialog(null, "Could not release UsbInterface : " + uE.getMessage()); }
				catch ( UsbNotActiveException unaE ) { JOptionPane.showMessageDialog(null, "Could not release UsbInterface : " + unaE.getMessage()); }
				refresh();
			}
		};
}
