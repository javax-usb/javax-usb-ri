package com.ibm.jusb.tools.swing;

/*
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import java.awt.*;
import java.util.*;

import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;

import javax.usb.*;
import javax.usb.util.*;
import javax.usb.event.*;

/**
 * Class to display the USB device topology tree using a Swing Frame.
 * @author Dan Streetman
 */
public class SwingUsbView
{
	public SwingUsbView(UsbServices services, UsbHub hub)
	{
		rootHub = hub;
		rootNode = new DefaultMutableTreeNode(new UsbHubPanel(rootHub));
		treeModel = new DefaultTreeModel(rootNode);
		tree = new JTree(treeModel);
		treeScroll = new JScrollPane(tree);
		splitPane.setLeftComponent(treeScroll);

		frame.getContentPane().add(splitPane);

		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setEditable(false);
		tree.addTreeSelectionListener(selectionListener);
		tree.addTreeExpansionListener(expansionListener);

		createTree(rootHub, rootNode);

		tree.setSelectionPath(new TreePath(rootNode.getPath()));

		frame.setSize(MID_SIZE);

		services.addUsbServicesListener(topologyListener);
	}

	/** Main */
	public static void main( String[] argv ) throws Exception
	{
		UsbServices services = UsbHostManager.getUsbServices();
		SwingUsbView s = new SwingUsbView(services, services.getRootUsbHub());

		s.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		s.frame.setVisible(true);

		/* Since Swing sucks, this can't be done before it's visible, or it doesn't actually do anything. */
		s.splitPane.setDividerLocation(0.35);
	}

	protected void createTree(UsbHub hub, DefaultMutableTreeNode node)
	{
		Iterator iterator = hub.getUsbPorts().iterator();
		while (iterator.hasNext()) {
			UsbPort port = (UsbPort)iterator.next();
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

				deviceTable.put(device, child);
			} else {
				child = getPortNode(port);
			}

			node.add(child);
		}

		treeModel.reload(node);
		for (int i=0; i<node.getChildCount(); i++)
				tree.expandPath(new TreePath(((DefaultMutableTreeNode)node.getChildAt(i)).getPath()));
	}

	protected void createDevice(UsbDevice device, DefaultMutableTreeNode node)
	{
		Iterator iterator = device.getUsbConfigs().iterator();

		while (iterator.hasNext()) {
			UsbConfig config = (UsbConfig)iterator.next();
			DefaultMutableTreeNode child = getConfigNode(config);

			createConfig(config, child);

			node.add(child);
		}
	}

	protected void createConfig(UsbConfig config, DefaultMutableTreeNode node)
	{
		Iterator iterator = config.getUsbInterfaces().iterator();

		while (iterator.hasNext()) {
			UsbInterface iface = (UsbInterface)iterator.next();
			Iterator altIterator = iface.getSettings().iterator();

			while (altIterator.hasNext()) {
					UsbInterface setting = (UsbInterface)altIterator.next();

					DefaultMutableTreeNode child = getInterfaceNode(setting);

					createInterface(setting, child);

					node.add(child);
			}
		}
	}

	protected void createInterface(UsbInterface iface, DefaultMutableTreeNode node)
	{
		Iterator iterator = iface.getUsbEndpoints().iterator();

		while (iterator.hasNext()) {
			UsbEndpoint ep = (UsbEndpoint)iterator.next();
			DefaultMutableTreeNode child = getEndpointNode(ep);

			createEndpoint(ep, child);

			node.add(child);
		}
	}

	protected void createEndpoint(UsbEndpoint ep, DefaultMutableTreeNode node)
	{
		DefaultMutableTreeNode child = new DefaultMutableTreeNode(new UsbPipePanel(ep.getUsbPipe()));

		node.add(child);
	}

	protected DefaultMutableTreeNode getHubNode(UsbHub hub)
	{
		return new DefaultMutableTreeNode(new UsbHubPanel(hub));
	}

	protected DefaultMutableTreeNode getPortNode(UsbPort port)
	{
		return new DefaultMutableTreeNode(new UsbPortPanel(port));
	}

	protected DefaultMutableTreeNode getDeviceNode(UsbDevice device)
	{
		return new DefaultMutableTreeNode(new UsbDevicePanel(device));
	}

	protected DefaultMutableTreeNode getConfigNode(UsbConfig config)
	{
		return new DefaultMutableTreeNode(new UsbConfigPanel(config));
	}

	protected DefaultMutableTreeNode getInterfaceNode(UsbInterface iface)
	{
		return new DefaultMutableTreeNode(new UsbInterfacePanel(iface));
	}

	protected DefaultMutableTreeNode getEndpointNode(UsbEndpoint ep)
	{
		return new DefaultMutableTreeNode(new UsbEndpointPanel(ep));
	}

	private UsbHub rootHub = null;

	private JFrame frame = new JFrame("UsbView");

	private Hashtable deviceTable = new Hashtable();

	private DefaultMutableTreeNode rootNode = null;
	private DefaultTreeModel treeModel = null;
	private JTree tree = null;
	private JScrollPane treeScroll = null;

	private JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, null, null);

	private UsbServicesListener topologyListener = new UsbServicesListener() {
			public void usbDeviceAttached(UsbServicesEvent usE)
			{
				UsbDevice device = usE.getUsbDevice();

				if (deviceTable.containsKey(device.getParentUsbPort().getUsbHub())) {
					DefaultMutableTreeNode parent = (DefaultMutableTreeNode)deviceTable.get(device.getParentUsbPort().getUsbHub());
					DefaultMutableTreeNode node = (DefaultMutableTreeNode)parent.getChildAt(UsbUtil.unsignedInt(device.getParentUsbPort().getPortNumber()) - 1);
					node.setUserObject(new UsbDevicePanel(device));
					createDevice(device, node);
					treeModel.reload(node);
					deviceTable.put(device, node);
				}
			}
			public void usbDeviceDetached(UsbServicesEvent usE)
			{
				UsbDevice device = usE.getUsbDevice();

				if (deviceTable.containsKey(device)) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode)deviceTable.get(device);
					node.setUserObject(new UsbPortPanel(device.getParentUsbPort()));
					node.removeAllChildren();
					treeModel.reload(node);
					deviceTable.remove(device);
				}
			}
		};

	private TreeExpansionListener expansionListener = new TreeExpansionListener() {
			public void treeCollapsed(TreeExpansionEvent teE)
			{ /*splitPane.resetToPreferredSizes();*/ }
			public void treeExpanded(TreeExpansionEvent teE)
			{ /*splitPane.resetToPreferredSizes();*/ }
		};

	private TreeSelectionListener selectionListener = new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent tsE)
			{
				if (tsE.isAddedPath()) {
					/* Swing sucks.  Pretty bad. */
					int dividerLocation = splitPane.getDividerLocation();
					splitPane.setRightComponent( (UsbPanel)((DefaultMutableTreeNode)tsE.getPath().getLastPathComponent()).getUserObject() );
					splitPane.setDividerLocation(dividerLocation);
				}
			}
		};

	private static final Dimension DEFAULT_SIZE = new Dimension(640,480);
	private static final Dimension MID_SIZE = new Dimension(800,600);

}
