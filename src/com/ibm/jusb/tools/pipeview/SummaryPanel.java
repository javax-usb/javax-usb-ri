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

import javax.swing.*;

/**
 * Panel to construct and send submissions over a UsbPipe.
 * @author Dan Streetman
 * @version 0.0.1 (JDK 1.1.x)
 */
public class SummaryPanel extends JPanel
{
	/** Constructor */
	public SummaryPanel()
	{
		super();

		init();
	}

	//*************************************************************************
	// Public methods

	protected void init()
	{
		add( new JLabel( "Not Supported" ) );
	}
}
