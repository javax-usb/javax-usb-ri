package com.ibm.jusb.tools.pipeview;

/**
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
import javax.swing.event.*;
import javax.swing.tree.*;

import javax.usb.*;
import javax.usb.os.*;
import javax.usb.event.*;
import javax.usb.util.*;
import com.ibm.jusb.tools.*;

/**
 * Panel to construct and send submissions over a UsbPipe.
 * @author Dan Streetman
 * @version 0.0.1 (JDK 1.1.x)
 */
public class UsbPipePanel extends JPanel
{
	/** Constructor */
	public UsbPipePanel( UsbEndpoint endpoint )
	{
		super( new BorderLayout() );
		setUsbEndpoint( endpoint );
		init();
	}

	//*************************************************************************
	// Protected methods

	protected void init()
	{
		abortAllButtonPanel.add( abortAllButton );
		abortTabPanel.add( SUMMARY_ABORT_TITLE, summaryAbortPanel );

		abortPanel.add( abortAllButtonPanel, BorderLayout.NORTH );
		abortPanel.add( abortTabPanel, BorderLayout.CENTER );

		submitButtonPanel.add( syncSubmitButton );
		submitButtonPanel.add( asyncSubmitButton );
		submitTabPanel.add( BYTE_ARRAY_SUBMIT_TITLE, byteArraySubmitPanel );
		submitTabPanel.add( USB_IRP_SUBMIT_TITLE, usbIrpSubmitPanel );
		submitTabPanel.add( USB_COMPOSITE_IRP_SUBMIT_TITLE, usbCompositeIrpSubmitPanel );

		submitPanel.add( submitButtonPanel, BorderLayout.NORTH );
		submitPanel.add( submitTabPanel, BorderLayout.CENTER );

		submitAbortPanel = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT, submitPanel, abortPanel );

		contentPanel = new JSplitPane( JSplitPane.VERTICAL_SPLIT, submitAbortPanel, dataTextArea );

		openButtonPanel.add( openButton );

		openPanel.add( openButtonPanel, BorderLayout.NORTH );
		openPanel.add( contentPanel, BorderLayout.CENTER );

		titleLabel = new JLabel( getTitle(), SwingConstants.CENTER );
		titleLabelPanel.add( titleLabel );

		add( titleLabelPanel, BorderLayout.NORTH );
		add( openPanel, BorderLayout.CENTER );

		addListeners();
	}

	protected void open()
	{
		boolean open = openButton.isSelected();
		String openString = open ? "open" : "close";

		try {
			if (open) {
				getUsbEndpoint().getUsbPipe().open();
			} else {
				getUsbEndpoint().getUsbPipe().close();
			}
		} catch ( UsbException uE ) {
			openButton.setSelected( !open );
			JOptionPane.showMessageDialog( this, "Could not " + openString + " UsbPipe : " + uE.getMessage() );
		} catch ( UsbRuntimeException urE ) {
			openButton.setSelected( !open );
			JOptionPane.showMessageDialog( this, "Could not " + openString + " UsbPipe : " + urE.getMessage() );
		}
	}

	//*************************************************************************
	// Protected methods

	protected void addListeners()
	{
		openButton.addActionListener( openListener );
	}

	//*************************************************************************
	// Private methods

	/** @param endpoint the UsbEndpoint to use */
	private void setUsbEndpoint( UsbEndpoint endpoint ) { usbEndpoint = endpoint; }

	/** @return the UsbEndpoint in use */
	private UsbEndpoint getUsbEndpoint() { return usbEndpoint; }

	/** @return the title of this panel */
	private String getTitle()
	{
		return getUsbEndpoint().getUsbDevice().getName() + " "
			+ getUsbEndpoint().getName() + " ("
			+ getUsbEndpoint().getUsbDevice().getProductString() + ")";
	}

	//*************************************************************************
	// Instance variables

	private UsbEndpoint usbEndpoint = null;

	private JPanel titleLabelPanel = new JPanel();
		private JLabel titleLabel = null;
	private JPanel openPanel = new JPanel( new BorderLayout() );
		private JPanel openButtonPanel = new JPanel();
			private JCheckBox openButton = new JCheckBox( OPEN_TITLE );
		private JSplitPane contentPanel = null;
			private JSplitPane submitAbortPanel = null;
				private JPanel submitPanel = new JPanel( new BorderLayout() );
					private JPanel submitButtonPanel = new JPanel();
						private JButton syncSubmitButton = new JButton( SYNCSUBMIT_TITLE );
						private JButton asyncSubmitButton = new JButton( ASYNCSUBMIT_TITLE );
					private JTabbedPane submitTabPanel = new JTabbedPane();
				private JPanel abortPanel = new JPanel( new BorderLayout() );
					private JPanel abortAllButtonPanel = new JPanel();
						private JButton abortAllButton = new JButton( ABORTALL_TITLE );
					private JTabbedPane abortTabPanel = new JTabbedPane();
			private JTextArea dataTextArea = new JTextArea( DATA_TEXTAREA_DEFAULT_ROWS, DATA_TEXTAREA_DEFAULT_COLS );

	/* submitTabPanel */
	private JPanel byteArraySubmitPanel = new ByteArrayPanel();
	private JPanel usbIrpSubmitPanel = new UsbIrpPanel();
	private JPanel usbCompositeIrpSubmitPanel = new UsbCompositeIrpPanel();

	/* abortTabPanel */
	private JPanel summaryAbortPanel = new SummaryPanel();

	/* Listeners */
	private ActionListener openListener = new OpenListener();

	//*************************************************************************
	// Class constants

	private static final String SYNCSUBMIT_TITLE = "Sync Submit";
	private static final String ASYNCSUBMIT_TITLE = "Async Submit";
	private static final String ABORTALL_TITLE = "Abort All";
	private static final String OPEN_TITLE = "Open";

	private static final String BYTE_ARRAY_SUBMIT_TITLE = "byte[]";
	private static final String USB_IRP_SUBMIT_TITLE = "irp";
	private static final String USB_COMPOSITE_IRP_SUBMIT_TITLE = "composite";
	private static final String SUMMARY_ABORT_TITLE = "summary";

	private static final int DATA_TEXTAREA_DEFAULT_ROWS = 5;
	private static final int DATA_TEXTAREA_DEFAULT_COLS = 40;

	//*************************************************************************
	// Inner classes

	public class OpenListener implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			UsbPipePanel.this.open();
		}
	}
}
