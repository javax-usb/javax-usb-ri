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

import javax.usb.*;
import javax.usb.event.*;
import javax.usb.util.*;

/**
 * Class to display the USB device topology tree using a Swing Frame.
 * @author Dan Streetman
 */
public class SwingUsbView
{
	/** Main */
	public static void main( String[] argv ) throws UsbException
	{
		UsbRootHub rootHub = UsbHostManager.getInstance().getUsbServices().getUsbRootHub();

		JFrame frame = new JFrame();

		frame.setVisible(true);
	}

	

}
