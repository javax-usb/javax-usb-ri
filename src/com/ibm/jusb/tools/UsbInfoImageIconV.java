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
 * Visitor class to create an image for the a UsbInfo object
 * @author E. Michael Maximilien
 * @version 0.0.1 (JDK 1.1.x)
 */
public class UsbInfoImageIconV extends DefaultUsbInfoV
{
    /** Default ctor */
    public UsbInfoImageIconV() 
    {
        String imageFileName = JUSB_RES_IMAGES_FILEPATH + "/" + UNKNOWN_TREE_ICON_FILENAME;
        treeImageIcon = new ImageIcon( Toolkit.getDefaultToolkit().getImage( imageFileName ) );
    }

    //-------------------------------------------------------------------------
    // Public methods
    //

    /** @return an ImageIcon for the UsbInfo object passed the size of 10x10 approximately (used for JTree) */
    public ImageIcon getTreeImageIcon() { return treeImageIcon; }

    //-------------------------------------------------------------------------
    // Public visitXyz visitor methods
    //

    /**
     * Default method to visit a UsbDevice
     * @param usbInfo the UsbInfo object
     */
    public void visitUsbDevice( UsbInfo usbInfo ) 
    {
        String imageFileName = JUSB_RES_IMAGES_FILEPATH + "/" + USB_DEVICE_TREE_ICON_FILENAME;
        treeImageIcon = new ImageIcon( Toolkit.getDefaultToolkit().getImage( imageFileName ) );
    }

    /**
     * Default method to visit a UsbHub
     * @param usbInfo the UsbInfo object
     */
    public void visitUsbHub( UsbInfo usbInfo ) 
    {                           
        String imageFileName = JUSB_RES_IMAGES_FILEPATH + "/" + USB_HUB_TREE_ICON_FILENAME;
        treeImageIcon = new ImageIcon( Toolkit.getDefaultToolkit().getImage( imageFileName ) );
    }

    /**
     * Default method to visit a UsbPort
     * @param usbInfo the UsbInfo object
     */
    public void visitUsbPort( UsbInfo usbInfo ) 
    {
        String imageFileName = JUSB_RES_IMAGES_FILEPATH + "/" + USB_PORT_TREE_ICON_FILENAME;
        treeImageIcon = new ImageIcon( Toolkit.getDefaultToolkit().getImage( imageFileName ) );
    }

    /**
     * Default method to visit a UsbDeviceConfig
     * @param usbInfo the UsbInfo object
     */
    public void visitUsbDeviceConfig( UsbInfo usbInfo ) 
    {
        String imageFileName = JUSB_RES_IMAGES_FILEPATH + "/" + USB_DEVICE_CONFIG_TREE_ICON_FILENAME;
        treeImageIcon = new ImageIcon( Toolkit.getDefaultToolkit().getImage( imageFileName ) );
    }

    /**
     * Default method to visit a UsbDeviceInterface
     * @param usbInfo the UsbInfo object
     */
    public void visitUsbDeviceInterface( UsbInfo usbInfo ) 
    {
        String imageFileName = JUSB_RES_IMAGES_FILEPATH + "/" + USB_INTERFACE_TREE_ICON_FILENAME;
        treeImageIcon = new ImageIcon( Toolkit.getDefaultToolkit().getImage( imageFileName ) );
    }

    /**
     * Default method to visit a UsbEndpoint
     * @param usbInfo the UsbInfo object
     */
    public void visitUsbEndpoint( UsbInfo usbInfo ) 
    {   
        String imageFileName = JUSB_RES_IMAGES_FILEPATH + "/" + USB_ENDPOINT_TREE_ICON_FILENAME;
        treeImageIcon = new ImageIcon( Toolkit.getDefaultToolkit().getImage( imageFileName ) );
    }

    //-------------------------------------------------------------------------
    // Instance variables
    //

    private ImageIcon treeImageIcon = null;

    //-------------------------------------------------------------------------
    // Class constants
    //         

    public static final String JUSB_RES_IMAGES_FILEPATH = "com/ibm/jusb/tools/res/images";

    public static final String UNKNOWN_OFFLINE_TREE_ICON_FILENAME = "unknown_offline_tree_icon.gif";
    public static final String UNKNOWN_TREE_ICON_FILENAME = "unknown_tree_icon.gif";
    public static final String USB_DEVICE_CONFIG_OFFLINE_TREE_ICON_FILENAME = "usb_device_config_offline_tree_icon.gif";
    public static final String USB_DEVICE_CONFIG_TREE_ICON_FILENAME = "usb_device_config_tree_icon.gif";
    public static final String USB_DEVICE_OFFLINE_TREE_ICON_FILENAME = "usb_device_offline_tree_icon.gif";
    public static final String USB_DEVICE_TREE_ICON_FILENAME = "usb_device_tree_icon.gif";
    public static final String USB_ENDPOINT_OFFLINE_TREE_ICON_FILENAME = "usb_endpoint_offline_tree_icon.gif";
    public static final String USB_ENDPOINT_TREE_ICON_FILENAME = "usb_endpoint_tree_icon.gif";
    public static final String USB_HOST_TREE_ICON_FILENAME = "usb_host_tree_icon.gif";
    public static final String USB_HUB_OFFLINE_TREE_ICON_FILENAME = "usb_hub_offline_tree_icon.gif";
    public static final String USB_HUB_TREE_ICON_FILENAME = "usb_hub_tree_icon.gif";
    public static final String USB_INTERFACE_OFFLINE_TREE_ICON_FILENAME = "usb_interface_offline_tree_icon.gif";
    public static final String USB_INTERFACE_TREE_ICON_FILENAME = "usb_interface_tree_icon.gif";
    public static final String USB_PORT_OFFLINE_TREE_ICON_FILENAME = "usb_port_offline_tree_icon.gif";
    public static final String USB_PORT_TREE_ICON_FILENAME = "usb_port_tree_icon.gif";
}
