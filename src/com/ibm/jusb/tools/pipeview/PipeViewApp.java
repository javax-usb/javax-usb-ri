package com.ibm.jusb.tools.pipeview;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
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
System.err.println("This does not work yet.  Sorry!");
System.exit(1);

        mainFrame.setDefaultCloseOperation( javax.swing.JFrame.EXIT_ON_CLOSE );	
        mainFrame.setVisible( true );
    }

    private static MainFrame mainFrame = null;//new MainFrame( "USB Pipe View" );
}
