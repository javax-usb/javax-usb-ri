package com.ibm.jusb;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import javax.usb.*;

/**
 * Defines a factory iterface to create UsbPipe objects
 * @author E. Michael Maximilien
 * @author Dan Streetman
 * @version 0.0.1 (JDK 1.1.x)
 */
public interface UsbPipeFactory
{
    //-------------------------------------------------------------------------
    // Public methods
    //

    /**
     * @return a new UsbPipe
     * @param usbEndpoint the UsbEndpoint associated with this pipe
     */
    public UsbPipe createUsbPipe( UsbEndpoint usbEndpoint );

}
