package com.ibm.jusb.tools.usbview;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the IBM Public License.
 *
 */

/**
 * Simple app to view list of USB connected devices
 * @author E. Michael Maximilien
 * @version 0.0.1
 */
public class UsbViewApp extends Object
{
    //-------------------------------------------------------------------------
    // Public class methods
    //

    /**
     * Main entry point
     * @param args the String[] arguments
     */
    public static void main( String[] args ) 
	{ 
		mainFrame.setDefaultCloseOperation( javax.swing.JFrame.EXIT_ON_CLOSE );
		mainFrame.setVisible( true ); 
	}

    //-------------------------------------------------------------------------
    // Class variables
    //

    private static MainFrame mainFrame = new MainFrame();
}
