package com.ibm.jusb.os;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import javax.usb.os.UsbServices;

/**
 * Default Visitor class implementation
 * @author E. Michael Maximilien
 * @version 0.0.1 (JDK 1.1.x)
 */
public class DefaultUsbServicesV extends Object implements UsbServicesVisitor
{
    //-------------------------------------------------------------------------
    // Public methods
    //

    /**
     * Called to visit a unknown platform
     * <b>NOTE:</b> this is a catch all visit for all other OSes until the
     * Visitor interface is updated
     * @param usbServices the UsbServices instance
     */
    public void visitUnknownUsbServices( UsbServices usbServices ) {}

    /**
     * Called to visit a for Win32 platform
     * @param usbServices the UsbServices instance
     */
    public void visitWin32UsbServices( UsbServices usbServices ) {}

    /**
     * Called to visit a for Linux platform
     * @param usbServices the UsbServices instance
     */
    public void visitLinuxUsbServices( UsbServices usbServices ) {}

    /**
     * Called to visit a for OS4690 platform
     * @param usbServices the UsbServices instance
     */
    public void visitOS4690UsbServices( UsbServices usbServices ) {}
}                                                                             
