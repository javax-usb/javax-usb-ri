package com.ibm.jusb.tools;

/*
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;

import javax.usb.*;
import javax.usb.os.*;
import javax.usb.util.*;
import javax.usb.event.*;

/**
 * Class to display Usb info.
 * @author Dan Streetman
 */
public abstract class UsbPanel extends JPanel
{
	public UsbPanel()
	{
		refreshPanel.add(refreshButton);
		refreshButton.addActionListener(refreshListener);
		textArea.setEditable(false);
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(refreshPanel, BorderLayout.NORTH);
		mainPanel.add(textArea, BorderLayout.SOUTH);
		setLayout(new BorderLayout());
		add(mainPanel, BorderLayout.NORTH);
	}

	public String toString() { return string; }

	protected abstract void refresh();

	protected void clear() { textArea.replaceRange("", 0, textArea.getText().length()); }
	protected void append(String s) { textArea.append(s); }
	protected void appendln(String s) { append(s + "\n"); }

	protected JPanel mainPanel = new JPanel();

	protected JPanel refreshPanel = new JPanel();
	protected JButton refreshButton = new JButton("Refresh");
	protected ActionListener refreshListener = new ActionListener() {
			public void actionPerformed(ActionEvent aE) { refresh(); }
		};

	protected JTextArea textArea = new JTextArea();
	protected String string;

public static class UsbHubPanel extends UsbDevicePanel
{
	public UsbHubPanel(UsbHub hub)
	{
		super();
		usbDevice = hub;
		usbHub = hub;
		string = hub.isUsbRootHub() ? "UsbRootHub" : "UsbHub";
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
		appendln("Number of Ports : " + usbHub.getNumberOfPorts());
		super.initText();
	}

	private UsbHub usbHub = null;
}

public static class UsbPortPanel extends UsbPanel
{
	public UsbPortPanel(UsbPort port)
	{
		super();
		usbPort = port;
		string = "UsbPort " + port.getPortNumber();
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
		appendln("Port Number : " + usbPort.getPortNumber());
		appendln("Is Device Attached : " + usbPort.isUsbDeviceAttached());
	}

	private UsbPort usbPort = null;
}

public static class UsbDevicePanel extends UsbPanel
{
	public UsbDevicePanel() { super(); }

	public UsbDevicePanel(UsbDevice device)
	{
		super();
		usbDevice = device;
		string = "UsbDevice";
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
		appendln("Vendor ID : 0x" + UsbUtil.toHexString(usbDevice.getVendorId()));
		appendln("Product ID : 0x" + UsbUtil.toHexString(usbDevice.getProductId()));
		appendln("Speed : " + usbDevice.getSpeedString());
		appendln("Manufacturer : " + usbDevice.getManufacturer());
		appendln("Product : " + usbDevice.getProductString());
		appendln("Serial Number : " + usbDevice.getSerialNumber());
		appendln("Device Class : 0x" + UsbUtil.toHexString(usbDevice.getDeviceClass()));
		appendln("Device Subclass : 0x" + UsbUtil.toHexString(usbDevice.getDeviceSubClass()));
		appendln("Device Protocol : 0x" + UsbUtil.toHexString(usbDevice.getDeviceProtocol()));
		appendln("BCD Device : " + UsbUtil.toHexString(usbDevice.getBcdDevice()));
		appendln("BCD USB : " + UsbUtil.toHexString(usbDevice.getBcdUsb()));
		appendln("Max Packet Size : " + UsbUtil.unsignedInt(usbDevice.getMaxPacketSize()));
		appendln("Is Configured : " + usbDevice.isConfigured());
		appendln("Active UsbConfig Number : " + UsbUtil.unsignedInt(usbDevice.getActiveUsbConfigNumber()));
		appendln("Number of UsbConfigs : " + UsbUtil.unsignedInt(usbDevice.getNumConfigs()));
	}

	protected UsbDevice usbDevice = null;
}

public static class UsbConfigPanel extends UsbPanel
{
	public UsbConfigPanel(UsbConfig config)
	{
		super();
		usbConfig = config;
		string = "UsbConfig " + config.getConfigNumber();
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
		appendln("Config Number : " + UsbUtil.unsignedInt(usbConfig.getConfigNumber()));
		appendln("Is Active : " + usbConfig.isActive());
		appendln("Config String : " + usbConfig.getConfigString());
		appendln("Attributes : " + UsbUtil.toHexString(usbConfig.getAttributes()));
		appendln("Max Power : " + UsbUtil.unsignedInt(usbConfig.getMaxPower()));
		appendln("Number of UsbInterfaces : " + UsbUtil.unsignedInt(usbConfig.getNumInterfaces()));
	}

	private UsbConfig usbConfig = null;
}

public static class UsbInterfacePanel extends UsbPanel
{
	public UsbInterfacePanel(UsbInterface iface)
	{
		super();
		usbInterface = iface;
		createClaimPanel();
		string = "UsbInterface " + iface.getInterfaceNumber();
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
		appendln("Interface Number : " + UsbUtil.unsignedInt(usbInterface.getInterfaceNumber()));
		appendln("Is Active : " + usbInterface.isActive());
		appendln("Is Claimed : " + usbInterface.isClaimed());
		appendln("Alternate Setting : " + UsbUtil.unsignedInt(usbInterface.getAlternateSettingNumber()));
		appendln("Active Alternate Setting Number : " + UsbUtil.unsignedInt(usbInterface.getActiveAlternateSettingNumber()));
		appendln("Interface Class : " + UsbUtil.toHexString(usbInterface.getInterfaceClass()));
		appendln("Interface Subclass : " + UsbUtil.toHexString(usbInterface.getInterfaceSubClass()));
		appendln("Interface Protocol : " + UsbUtil.toHexString(usbInterface.getInterfaceProtocol()));
		appendln("Interface String : " + usbInterface.getInterfaceString());
		appendln("Number of Alternate Settings : " + UsbUtil.unsignedInt(usbInterface.getNumAlternateSettings()));
		appendln("Number of UsbEndpoints : " + UsbUtil.unsignedInt(usbInterface.getNumEndpoints()));
	}

	protected void createClaimPanel()
	{
		claimButton.addActionListener(claimListener);
		claimPanel.add(claimButton);
		releaseButton.addActionListener(releaseListener);
		claimPanel.add(releaseButton);
		add(claimPanel, BorderLayout.SOUTH);
	}

	private UsbInterface usbInterface = null;

	private JPanel claimPanel = new JPanel();
	private JButton claimButton = new JButton("Claim");
	private ActionListener claimListener = new ActionListener() {
			public void actionPerformed(ActionEvent aE)
			{
				try { usbInterface.claim(); }
				catch ( UsbException uE ) { JOptionPane.showMessageDialog(null, "Could not claim UsbInterface : " + uE.getMessage()); }
				refresh();
			}
		};
	private JButton releaseButton = new JButton("Release");
	private ActionListener releaseListener = new ActionListener() {
			public void actionPerformed(ActionEvent aE)
			{
				try { usbInterface.release(); }
				catch ( UsbException uE ) { JOptionPane.showMessageDialog(null, "Could not release UsbInterface : " + uE.getMessage()); }
				refresh();
			}
		};
}

public static class UsbEndpointPanel extends UsbPanel
{
	public UsbEndpointPanel(UsbEndpoint ep)
	{
		super();
		usbEndpoint = ep;
		string = "UsbEndpoint 0x" + UsbUtil.toHexString(ep.getEndpointAddress());
	}

	protected void refresh()
	{
		clear();
		appendln(string);
		initText();
	}

	protected void initText()
	{
		String type = null;
		switch (usbEndpoint.getType()) {
		case UsbInfoConst.ENDPOINT_TYPE_CONTROL: type = "Control"; break;
		case UsbInfoConst.ENDPOINT_TYPE_BULK: type = "Bulk"; break;
		case UsbInfoConst.ENDPOINT_TYPE_INT: type = "Interrupt"; break;
		case UsbInfoConst.ENDPOINT_TYPE_ISOC: type = "Isochronous"; break;
		default: type = "Unknown"; break;
		}

		String direction = null;
		switch (usbEndpoint.getDirection()) {
		case UsbInfoConst.ENDPOINT_DIRECTION_IN: direction = "IN"; break;
		case UsbInfoConst.ENDPOINT_DIRECTION_OUT: direction = "OUT"; break;
		default: direction = "Unknown"; break;
		}

		appendln("Endpoint Address : 0x" + UsbUtil.toHexString(usbEndpoint.getEndpointAddress()));
		appendln("Type : " + UsbUtil.toHexString(usbEndpoint.getType()) + " (" + type + ")");
		appendln("Direction : " + direction);
		appendln("Interval : " + UsbUtil.unsignedInt(usbEndpoint.getInterval()));
		appendln("Max Packet Size : " + UsbUtil.unsignedInt(usbEndpoint.getMaxPacketSize()));
		appendln("Attributes : " + UsbUtil.toHexString(usbEndpoint.getAttributes()));
	}

	private UsbEndpoint usbEndpoint = null;
}

public static class UsbPipePanel extends UsbPanel
{
	public UsbPipePanel(UsbPipe pipe)
	{
		super();
		usbPipe = pipe;
		string = "UsbPipe";
	}

	protected void refresh()
	{
		clear();
		appendln(string);
		initText();
	}

	protected void initText()
	{
		appendln("Is Active : " + usbPipe.isActive());
		appendln("Is Open : " + usbPipe.isOpen());
		appendln("Max Packet Size : " + UsbUtil.unsignedInt(usbPipe.getMaxPacketSize()));
	}

	private UsbPipe usbPipe = null;
}

}
