package com.ibm.jusb.tools.pipeview;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the IBM Public License.
 *
 */

import javax.swing.*;
import java.awt.event.*;

/**
 * USB view-style with added pipe functionality
 * @author Dan Streetman
 * @version 0.0.1 (JDK 1.1.x)
 */
public class PipeViewApp {

    private PipeViewApp() { }

    public static void main( String[] argv ) 
	{
        mainFrame.setDefaultCloseOperation( javax.swing.JFrame.EXIT_ON_CLOSE );	
        mainFrame.setVisible( true );
    }

    private static MainFrame mainFrame = new MainFrame( "USB Pipe View" );
}
