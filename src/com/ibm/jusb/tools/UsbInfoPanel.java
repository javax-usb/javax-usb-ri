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
 * Panel to display a UsbInfo
 * @author E. Michael Maximilien
 * @version 0.0.1 (JDK 1.1.x)
 */
public class UsbInfoPanel extends JPanel
{
    public UsbInfoPanel()
    {
        setLayout( new BorderLayout() );
        setBorder( BorderFactory.createLineBorder( Color.lightGray ) );

        JPanel jPanel = new JPanel( new FlowLayout( FlowLayout.CENTER ) );

        nameTextField.setEditable( false );

        jPanel.add( new JLabel( "Name:" ) );
        jPanel.add( nameTextField );

        add( jPanel, BorderLayout.NORTH );

        usbInfoTextArea.setBorder( BorderFactory.createLineBorder( Color.lightGray ) );
        usbInfoTextArea.setEditable( false );
        usbInfoTextArea.setTabSize( 4 );
        usbInfoTextArea.setFont( DEFAULT_USB_INFO_TEXT_AREA_FONT );
        JScrollPane jScrollPane = new JScrollPane( usbInfoTextArea,
                                                   JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                                                   JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );

        add( jScrollPane, BorderLayout.CENTER );
    }

    //-------------------------------------------------------------------------
    // Public methods
    //

    public void setUsbInfo( UsbInfo usbInfo )
    {
        nameTextField.setText( usbInfo.getName() );
        usbInfoTextArea.setText( usbInfo.toString() );
    }

    //-------------------------------------------------------------------------
    // Instance variables
    //

    private JTextField nameTextField = new JTextField( 15 );
    private JTextArea usbInfoTextArea = new JTextArea( 10, 20 );

    //-------------------------------------------------------------------------
    // Class constants
    //

    public static final Font DEFAULT_USB_INFO_TEXT_AREA_FONT = new Font( "Courier", Font.PLAIN, 12 );
}
