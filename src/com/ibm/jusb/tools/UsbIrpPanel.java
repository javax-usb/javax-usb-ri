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
 * Class to display UsbIrp (or raw byte[]) information.
 * @author Dan Streetman
 */
public class UsbIrpPanel extends JPanel
{
	public UsbIrpPanel()
	{
		irpCheckBox.addChangeListener(irpListener);
		refreshButton.addActionListener(refreshListener);

		packetOptionsPanel.add(syncCheckBox);
		packetOptionsPanel.add(irpCheckBox);
		packetOptionsPanel.add(acceptShortCheckBox);
		packetOptionsPanel.add(refreshButton);

		setLayout(new BorderLayout());
		add(packetOptionsPanel, BorderLayout.WEST);
		add(packetDataScroll, BorderLayout.EAST);
	}

	public void submit(UsbPipe pipe) throws UsbException,NumberFormatException
	{
		lastData = getData();

		if (irpCheckBox.isSelected()) {
			UsbIrp irp = UsbHostManager.getInstance().getUsbServices().getUsbIrpFactory().createUsbIrp();
			irp.setData(lastData);
			irp.setAcceptShortPacket(acceptShortCheckBox.isSelected());
			if (syncCheckBox.isSelected())
				pipe.syncSubmit(irp);
			else
				pipe.asyncSubmit(irp);
		} else {
			if (syncCheckBox.isSelected())
				pipe.syncSubmit(lastData);
			else
				pipe.asyncSubmit(lastData);
		}
	}

	protected byte[] getData()
	{
		java.util.List list = new ArrayList();
		StringTokenizer tokenizer = new StringTokenizer(packetDataTextArea.getText());
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			byte b;
			try {
				b = (byte)Integer.decode(token).intValue(); /* truncate without error anything greater than 1 byte */
			} catch ( NumberFormatException nfE ) {
				/* If character specified as 'X' or "X", then use the raw character specified */
				if ((3 == token.length()) && ((0x22 == token.charAt(0) && 0x22 == token.charAt(2)) || (0x27 == token.charAt(0) && 0x27 == token.charAt(2))))
					b = (byte)token.charAt(1);
				else
					throw nfE;
			}
			list.add(new Byte(b));
		}

		byte[] data = new byte[list.size()];
		for (int i=0; i<data.length; i++)
			data[i] = ((Byte)list.get(i)).byteValue();

		return data;
	}

	protected void refresh()
	{
		if (null == lastData)
			return;

		if (!Arrays.equals(lastData, getData())) {
			packetDataTextArea.replaceRange("", 0, packetDataTextArea.getText().length());
			for (int i=0; i<lastData.length; i++)
				packetDataTextArea.append("0x" + UsbUtil.toHexString(lastData[i]) + " ");
		}
	}

	private JPanel packetOptionsPanel = new JPanel();
	private JCheckBox syncCheckBox = new JCheckBox("Sync");
	private JCheckBox irpCheckBox = new JCheckBox("UsbIrp");
	private JCheckBox acceptShortCheckBox = new JCheckBox("AcceptShort");
	private JButton refreshButton = new JButton("Refresh");
	private JTextArea packetDataTextArea = new JTextArea();
	private JScrollPane packetDataScroll = new JScrollPane(packetDataTextArea);

	private byte[] lastData = null;

	private ActionListener refreshListener = new ActionListener() {
			public void actionPerformed(ActionEvent aE) { refresh(); }
		};

	private ChangeListener irpListener = new ChangeListener() {
			public void stateChanged(ChangeEvent cE) { acceptShortCheckBox.setEnabled(irpCheckBox.isSelected()); }
		};

}
