package com.ibm.jusb;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import javax.usb.UsbException;

/**
 * Defines a factory iterface to create UsbPipe objects
 * @author E. Michael Maximilien
 * @author Dan Streetman
 * @version 0.0.1 (JDK 1.1.x)
 */
public interface UsbPipeImpFactory
{
    //-------------------------------------------------------------------------
    // Public methods
    //

    /**
	 * @param abstraction a UsbPipeAbstraction object
     * @return a new UsbPipeImp
     */
    public UsbPipeImp createUsbPipeImp( UsbPipeAbstraction abstraction );

}
