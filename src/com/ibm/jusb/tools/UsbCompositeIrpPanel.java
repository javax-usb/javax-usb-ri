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

import javax.swing.*;
import javax.swing.text.*;

/**
 * Panel to construct (and display properties of) a UsbCompositeIrp.
 * @author Dan Streetman
 * @version 0.0.1 (JDK 1.1.x)
 */
public class UsbCompositeIrpPanel extends JPanel
{
	/** Constructor */
	public UsbCompositeIrpPanel()
	{
		super();

		init();
	}

	//*************************************************************************
	// Public methods

	//*************************************************************************
	// Protected methods

	protected void init()
	{
		add( new JLabel( "Not Supported!" ) );
	}
}
