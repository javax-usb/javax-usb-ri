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

import javax.usb.*;

/**
 * Panel to display a tree UsbInfo
 * @author E. Michael Maximilien
 * @version 0.0.1 (JDK 1.1.x)
 */
public class UsbInfoTreePanel extends JPanel
{
    public UsbInfoTreePanel()
    {
        setLayout( new GridLayout( 1, 1 ) );

        setBorder( BorderFactory.createLineBorder( Color.lightGray ) );

        JScrollPane jScrollPane = new JScrollPane( usbInfoTree,
                                                   JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                                                   JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );

        add( jScrollPane );
    }

    //-------------------------------------------------------------------------
    // Public methods
    //

    public void setRootUsbHub( UsbHub rootHub ) { usbInfoTree.setRootHub( rootHub ); }

    public UsbInfoTree getUsbInfoTree() { return usbInfoTree; }

    //-------------------------------------------------------------------------
    // Instance variables
    //

    private UsbInfoTree usbInfoTree = new UsbInfoTree();
}
