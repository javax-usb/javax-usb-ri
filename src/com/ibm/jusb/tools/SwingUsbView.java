package com.ibm.jusb.tools;

/*
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import javax.swing.*;
import javax.swing.tree.*;

import javax.usb.*;
import javax.usb.os.*;
import javax.usb.util.*;

/**
 * Class to display the USB device topology tree using a Swing Frame.
 * @author Dan Streetman
 */
public class SwingUsbView
{
	/** Main */
	public static void main( String[] argv ) throws Exception
	{
		SwingUsbView s = new SwingUsbView();

		s.frame.getContentPane().add(s.scrollPane);
		s.frame.pack();
		s.frame.setVisible(true);

		s.rootHub = UsbHostManager.getInstance().getUsbServices().getUsbRootHub();

		s.createTree(s.rootHub, s.rootNode);

		s.treeModel.reload();
		s.frame.pack();
	}

	protected void createTree(UsbHub hub, DefaultMutableTreeNode node)
	{
		UsbInfoIterator iterator = hub.getUsbPorts();
		while (iterator.hasNext()) {
			UsbPort port = (UsbPort)iterator.nextUsbInfo();
			DefaultMutableTreeNode child = null;

			if (port.isUsbDeviceAttached()) {
				child = new DefaultMutableTreeNode("UsbPort " + port.getPortNumber());
			} else {
				UsbDevice device = port.getUsbDevice();

				if (device.isUsbHub()) {
					child = new DefaultMutableTreeNode("UsbHub");
					createTree((UsbHub)device, child);
				} else {
					child = new DefaultMutableTreeNode("UsbDevice");
					createDevice(device, child);
				}
			}

			node.add(child);
		}
	}

	protected void createDevice(UsbDevice device, DefaultMutableTreeNode node)
	{
		UsbInfoIterator iterator = device.getUsbConfigs();

		while (iterator.hasNext()) {
			UsbConfig config = (UsbConfig)iterator.nextUsbInfo();
			DefaultMutableTreeNode child = new DefaultMutableTreeNode("UsbConfig " + config.getConfigNumber());

			createConfig(config, child);

			node.add(child);
		}
	}

	protected void createConfig(UsbConfig config, DefaultMutableTreeNode node)
	{
		UsbInfoIterator iterator = config.getUsbInterfaces();

		while (iterator.hasNext()) {
			UsbInterface iface = (UsbInterface)iterator.nextUsbInfo();
			DefaultMutableTreeNode child = new DefaultMutableTreeNode("UsbInterface " + iface.getInterfaceNumber());

			createInterface(iface, child);

			node.add(child);
		}
	}

	protected void createInterface(UsbInterface iface, DefaultMutableTreeNode node)
	{
		UsbInfoIterator iterator = iface.getUsbEndpoints();

		while (iterator.hasNext()) {
			UsbEndpoint ep = (UsbEndpoint)iterator.nextUsbInfo();
			DefaultMutableTreeNode child = new DefaultMutableTreeNode("UsbEndpoint 0x" + UsbUtil.toHexString( ep.getEndpointAddress() ));

			createEndpoint(ep, child);

			node.add(child);
		}
	}

	protected void createEndpoint(UsbEndpoint ep, DefaultMutableTreeNode node)
	{
		DefaultMutableTreeNode child = new DefaultMutableTreeNode("UsbPipe");

		node.add(child);
	}

	private UsbRootHub rootHub = null;

	private JFrame frame = new JFrame("UsbView");

	private DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("UsbRootHub");
	private DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
	private JTree tree = new JTree(treeModel);
	private JScrollPane scrollPane = new JScrollPane(tree);
}
