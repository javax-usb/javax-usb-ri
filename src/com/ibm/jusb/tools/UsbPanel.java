package com.ibm.jusb.tools;

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
import javax.usb.os.*;
import javax.usb.util.*;
import javax.usb.event.*;

/**
 * Class to display Usb info.
 * @author Dan Streetman
 */
public abstract class UsbPanel extends Box
{
	public UsbPanel()
	{
		super(BoxLayout.Y_AXIS);

		textArea.setEditable(false);
		refreshButton.addActionListener(refreshListener);

		refreshPanel.add(refreshButton);

		add(refreshPanel);
		add(textArea);
	}

	public String toString() { return string; }

	protected abstract void refresh();

	protected void clear() { textArea.replaceRange("", 0, textArea.getText().length()); }
	protected void append(String s) { textArea.append(s); }
	protected void appendln(String s) { append(s + "\n"); }

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
		initPanels();
		string = "UsbPipe";
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
		appendln("Is Active : " + usbPipe.isActive());
		appendln("Is Open : " + usbPipe.isOpen());
		appendln("Max Packet Size : " + UsbUtil.unsignedInt(usbPipe.getMaxPacketSize()));
	}

	protected void initPanels()
	{
		outputTextArea.setEditable(false);

		openButton.addActionListener(openListener);
		closeButton.addActionListener(closeListener);
		submitButton.addActionListener(submitListener);
		newPacketButton.addActionListener(newPacketListener);
		removeButton.addActionListener(removeListener);
		upButton.addActionListener(upListener);
		downButton.addActionListener(downListener);

		packetJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		packetJList.addListSelectionListener(packetListListener);

		openClosePanel.add(openButton);
		openClosePanel.add(closeButton);

		JPanel jp = new JPanel();
		jp.add(submitButton);
		submitButtonBox.add(jp);
		jp = new JPanel();
		jp.add(newPacketButton);
		submitButtonBox.add(jp);
		jp = new JPanel();
		jp.add(removeButton);
		submitButtonBox.add(jp);
		jp = new JPanel();
		jp.add(upButton);
		submitButtonBox.add(jp);
		jp = new JPanel();
		jp.add(downButton);
		submitButtonBox.add(jp);

		irpPanel.setLayout(irpLayout);

		packetPanel.add(packetJList);

		submitBox.add(submitButtonBox);
		submitBox.add(packetListScroll);

		add(openClosePanel);
		add(outputScroll);
		add(submitBox);
		add(irpPanel);
	}

	protected int getSelectedIndex()
	{
		if (packetJList.isSelectionEmpty() && !packetList.isEmpty())
			packetJList.setSelectedIndex(0);
		return packetJList.getSelectedIndex();
	}

	protected void updateSelection()
	{
		if (!packetList.isEmpty())
			irpLayout.show(irpPanel, packetList.get(getSelectedIndex()).toString());
		validate();
	}

	protected void open()
	{
		try {
			usbPipe.open();
			refresh();
		} catch ( UsbException uE ) {
			JOptionPane.showMessageDialog(null, "Could not open UsbPipe : " + uE.getMessage());
		}
	}

	protected void close()
	{
		try {
			usbPipe.close();
			refresh();
		} catch ( UsbException uE ) {
			JOptionPane.showMessageDialog(null, "Could not close UsbPipe : " + uE.getMessage());
		}
	}

	protected void submit()
	{
		UsbIrpPanel panel = null;

		try {
			for (int i=0; i<packetList.size(); i++) {
				panel = (UsbIrpPanel)packetList.get(i);
				panel.submit(usbPipe);
			}
		} catch ( UsbException uE ) {
			JOptionPane.showMessageDialog(null, "UsbException while submitting " + panel + " : " + uE.getMessage());
		} catch ( NumberFormatException nfE ) {
			JOptionPane.showMessageDialog(null, "NumberFormatException in " + panel + " : " + nfE.getMessage());
		}
	}

	protected void addPacket()
	{
		UsbIrpPanel newPanel = new UsbIrpPanel();
		packetList.add(newPanel);
		packetJList.setListData(packetList);
		irpPanel.add(newPanel, newPanel.toString());
		irpLayout.show(irpPanel, newPanel.toString());
		updateSelection();
	}

	protected void removePacket()
	{
		int index = packetJList.getSelectedIndex();
		if (0 <= index) {
			packetList.remove(index);
			packetJList.setListData(packetList);
			if (packetList.size() <= index)
					index--;
			if (0 <= index)
					packetJList.setSelectedIndex(index);
			updateSelection();
		}
	}

	protected void upPacket()
	{
		if (packetJList.isSelectionEmpty())
			return;

		int index = packetJList.getSelectedIndex();
		if (0 < index) {
			packetList.set(index, packetList.set(index-1, packetList.get(index)));
			packetJList.setListData(packetList);
			packetJList.setSelectedIndex(index-1);
			updateSelection();
		}
	}

	protected void downPacket()
	{
		if (packetJList.isSelectionEmpty())
			return;

		int index = packetJList.getSelectedIndex();
		if (packetList.size() > (index+1)) {
			packetList.set(index, packetList.set(index+1, packetList.get(index)));
			packetJList.setListData(packetList);
			packetJList.setSelectedIndex(index+1);
			updateSelection();
		}
	}

	private JPanel openClosePanel = new JPanel();
	private JTextArea outputTextArea = new JTextArea();
	private JScrollPane outputScroll = new JScrollPane(outputTextArea);
	private Vector packetList = new Vector();
	private JList packetJList = new JList();
	private JPanel packetPanel = new JPanel();
	private JScrollPane packetListScroll = new JScrollPane(packetPanel);
	private Box submitBox = new Box(BoxLayout.X_AXIS);
	private Box submitButtonBox = new Box(BoxLayout.Y_AXIS);
	private JPanel irpPanel = new JPanel();
	private CardLayout irpLayout = new CardLayout();

	private JButton openButton = new JButton("Open");
	private JButton closeButton = new JButton("Close");
	private JButton submitButton = new JButton("Submit");
	private JButton newPacketButton = new JButton("New");
	private JButton removeButton = new JButton("Remove");
	private JButton upButton = new JButton("Up");
	private JButton downButton = new JButton("Down");

	private ActionListener openListener = new ActionListener() { public void actionPerformed(ActionEvent aE) { open(); } };
	private ActionListener closeListener = new ActionListener() { public void actionPerformed(ActionEvent aE) { close(); } };
	private ActionListener submitListener = new ActionListener() { public void actionPerformed(ActionEvent aE) { submit(); } };
	private ActionListener newPacketListener = new ActionListener() { public void actionPerformed(ActionEvent aE) { addPacket(); } };
	private ActionListener removeListener = new ActionListener() { public void actionPerformed(ActionEvent aE) { removePacket(); } };
	private ActionListener upListener = new ActionListener() { public void actionPerformed(ActionEvent aE) { upPacket(); } };
	private ActionListener downListener = new ActionListener() { public void actionPerformed(ActionEvent aE) { downPacket(); } };

	private ListSelectionListener packetListListener =
		new ListSelectionListener() { public void valueChanged(ListSelectionEvent lsE) { updateSelection(); } };

	private UsbPipe usbPipe = null;
}

}
