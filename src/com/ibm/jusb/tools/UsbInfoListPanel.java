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
import javax.usb.util.*;

/**
 * Panel to display a list of UsbInfo
 * @author E. Michael Maximilien
 * @version 0.0.1 (JDK 1.1.x)
 */
public class UsbInfoListPanel extends JPanel
{
    public UsbInfoListPanel()
    {
        setLayout( new GridLayout( 1, 1 ) );

        setBorder( BorderFactory.createLineBorder( Color.lightGray ) );

        JScrollPane jScrollPane = new JScrollPane( usbInfoList,
                                                   JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                                                   JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );

        add( jScrollPane );

        usbInfoList.setCellRenderer( renderer );
    }

    //-------------------------------------------------------------------------
    // Public methods
    //

    public void setUsbInfos( UsbInfoIterator usbInfoIterator  )
    {
        listModel.setListModelData( usbInfoIterator );
    }

    public JList getUsbInfoList() { return usbInfoList; }

    //-------------------------------------------------------------------------
    // Instance variables
    //

    private UsbInfoListCellRenderer renderer = new UsbInfoListCellRenderer();
    private UsbInfoListModel listModel = new UsbInfoListModel();
    private JList usbInfoList = new JList( listModel );
}
