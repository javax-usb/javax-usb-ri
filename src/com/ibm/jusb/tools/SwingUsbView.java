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
import javax.swing.event.*;

import javax.usb.*;
import javax.usb.os.*;
import javax.usb.util.*;
import javax.usb.event.*;

/**
 * Class to display the USB device topology tree using a Swing Frame.
 * @author Dan Streetman
 */
public class SwingUsbView
{
	public SwingUsbView(UsbServices services, UsbRootHub hub)
	{
		rootHub = hub;
		rootNode = new DefaultMutableTreeNode(new UsbPanel.UsbHubPanel(rootHub));
		treeModel = new DefaultTreeModel(rootNode);
		tree = new JTree(treeModel);
		treeScroll = new JScrollPane(tree);
		splitPane.setLeftComponent(treeScroll);

		frame.getContentPane().add(splitPane);

		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setEditable(false);
		tree.addTreeSelectionListener(selectionListener);

		createTree(rootHub, rootNode);

		treeModel.reload();
		frame.pack();

		services.addUsbServicesListener(topologyListener);
	}

	/** Main */
	public static void main( String[] argv ) throws Exception
	{
		UsbServices services = UsbHostManager.getInstance().getUsbServices();
		SwingUsbView s = new SwingUsbView(services, services.getUsbRootHub());
		
		s.frame.setVisible(true);
	}

	protected void createTree(UsbHub hub, DefaultMutableTreeNode node)
	{
		UsbInfoIterator iterator = hub.getUsbPorts();
		while (iterator.hasNext()) {
			UsbPort port = (UsbPort)iterator.nextUsbInfo();
			DefaultMutableTreeNode child = null;

			if (port.isUsbDeviceAttached()) {
				UsbDevice device = port.getUsbDevice();

				if (device.isUsbHub()) {
					child = getHubNode((UsbHub)device);
					createTree((UsbHub)device, child);
				} else {
					child = getDeviceNode(device);
					createDevice(device, child);
				}
			} else {
				child = getPortNode(port);
			}

			node.add(child);
		}
	}

	protected void createDevice(UsbDevice device, DefaultMutableTreeNode node)
	{
		UsbInfoIterator iterator = device.getUsbConfigs();

		while (iterator.hasNext()) {
			UsbConfig config = (UsbConfig)iterator.nextUsbInfo();
			DefaultMutableTreeNode child = getConfigNode(config);

			createConfig(config, child);

			node.add(child);
		}
	}

	protected void createConfig(UsbConfig config, DefaultMutableTreeNode node)
	{
		UsbInfoIterator iterator = config.getUsbInterfaces();

		while (iterator.hasNext()) {
			UsbInterface iface = (UsbInterface)iterator.nextUsbInfo();
			DefaultMutableTreeNode child = getInterfaceNode(iface);

			createInterface(iface, child);

			node.add(child);
		}
	}

	protected void createInterface(UsbInterface iface, DefaultMutableTreeNode node)
	{
		UsbInfoIterator iterator = iface.getUsbEndpoints();

		while (iterator.hasNext()) {
			UsbEndpoint ep = (UsbEndpoint)iterator.nextUsbInfo();
			DefaultMutableTreeNode child = getEndpointNode(ep);

			createEndpoint(ep, child);

			node.add(child);
		}
	}

	protected void createEndpoint(UsbEndpoint ep, DefaultMutableTreeNode node)
	{
		DefaultMutableTreeNode child = new DefaultMutableTreeNode(new UsbPanel.UsbPipePanel(ep.getUsbPipe()));

		node.add(child);
	}

	protected DefaultMutableTreeNode getHubNode(UsbHub hub)
	{
		return new DefaultMutableTreeNode(new UsbPanel.UsbHubPanel(hub));
	}

	protected DefaultMutableTreeNode getPortNode(UsbPort port)
	{
		return new DefaultMutableTreeNode(new UsbPanel.UsbPortPanel(port));
	}

	protected DefaultMutableTreeNode getDeviceNode(UsbDevice device)
	{
		return new DefaultMutableTreeNode(new UsbPanel.UsbDevicePanel(device));
	}

	protected DefaultMutableTreeNode getConfigNode(UsbConfig config)
	{
		return new DefaultMutableTreeNode(new UsbPanel.UsbConfigPanel(config));
	}

	protected DefaultMutableTreeNode getInterfaceNode(UsbInterface iface)
	{
		return new DefaultMutableTreeNode(new UsbPanel.UsbInterfacePanel(iface));
	}

	protected DefaultMutableTreeNode getEndpointNode(UsbEndpoint ep)
	{
		return new DefaultMutableTreeNode(new UsbPanel.UsbEndpointPanel(ep));
	}

	private UsbRootHub rootHub = null;

	private JFrame frame = new JFrame("UsbView");

	private DefaultMutableTreeNode rootNode = null;
	private DefaultTreeModel treeModel = null;
	private JTree tree = null;
	private JScrollPane treeScroll = null;

	private JScrollPane infoScroll = new JScrollPane();

	private JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, null, infoScroll);

	private UsbServicesListener topologyListener = new UsbServicesListener() {
			public void usbDeviceAttached(UsbServicesEvent usE)
			{ 
/*FIXME - implement*/ }
			public void usbDeviceDetached(UsbServicesEvent usE)
			{ 
/*FIXME - implement*/ }
		};

	private TreeSelectionListener selectionListener = new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent tsE)
			{
				if (tsE.isAddedPath())
					infoScroll.getViewport().setView((UsbPanel)((DefaultMutableTreeNode)tsE.getPath().getLastPathComponent()).getUserObject());
			}
		};

}
