package com.ibm.jusb.tools;

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
import javax.swing.text.*;

import javax.usb.*;

/**
 * Panel to claim or release an interface.
 * @author Dan Streetman
 * @version 0.0.1 (JDK 1.1.x)
 */
public class UsbInterfacePanel extends JPanel
{
	/** Constructor */
	public UsbInterfacePanel( UsbInterface iface )
	{
		super( new BorderLayout() );

		setUsbInterface( iface );

		init();
	}

	//*************************************************************************
	// Public methods

	//*************************************************************************
	// Protected methods

	/** @return the interface in use */
	protected UsbInterface getUsbInterface() { return usbInterface; }

	/** @param iface the interface to use */
	protected void setUsbInterface( UsbInterface iface ) { usbInterface = iface; }

	protected void init()
	{
		titleLabel = new JLabel( getTitle(), SwingConstants.CENTER );

		titleLabelPanel.add( titleLabel );

		if (getUsbInterface().isActive())
			claimButton.setSelected( getUsbInterface().isClaimed() );
		else
			claimButton.setSelected( false );
		claimButton.addActionListener( claimListener );

		claimPanel.add( claimButton );

		add( titleLabelPanel, BorderLayout.NORTH );
		add( claimPanel, BorderLayout.CENTER );
	}

	protected String getTitle()
	{
		return getUsbInterface().getUsbDevice().getName() + " "
			+ getUsbInterface().getName() + " ("
			+ getUsbInterface().getUsbDevice().getProductString() + ")";
	}

	protected void claim()
	{
		boolean claim = claimButton.isSelected();
		String claimString = claim ? "claim" : "release";

		try {
			if (claim)
				getUsbInterface().claim();
			else
				getUsbInterface().release();
		} catch ( UsbException uE ) {
			claimButton.setSelected( !claim );
			JOptionPane.showMessageDialog( this, "Could not " + claimString + " interface : " + uE.getMessage() );
		} catch ( UsbRuntimeException urE ) {
			claimButton.setSelected( !claim );
			JOptionPane.showMessageDialog( this, "Could not " + claimString + " interface : " + urE.getMessage() );
		}
	}

	//*************************************************************************
	// Instance variables

	private UsbInterface usbInterface = null;

	private JPanel titleLabelPanel = new JPanel();
		private JLabel titleLabel = null;
	private JPanel claimPanel = new JPanel();
		private JCheckBox claimButton = new JCheckBox( CLAIM_TITLE );

	private ActionListener claimListener = new ClaimListener();

	//*************************************************************************
	// Class constants

	private static final String CLAIM_TITLE = "Claim";

	//*************************************************************************
	// Inner classes

	protected class ClaimListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			UsbInterfacePanel.this.claim();
		}
	}
}
