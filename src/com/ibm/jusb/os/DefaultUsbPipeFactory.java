package com.ibm.jusb.os;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the IBM Public License.
 *
 */

import javax.usb.*;
import javax.usb.os.*;

/**
 * Defines a factory class for creating UsbPipe objects
 * @author E. Michael Maximilien
 * @author Dan Streetman
 * @version 0.0.1 (JDK 1.1.x)
 */
class DefaultUsbPipeFactory extends Object implements UsbPipeFactory
{
    //-------------------------------------------------------------------------
    // Public methods
    //

    /**
     * @return a new UsbPipe
     * @param usbEndpoint the UsbEndpoint associated with this pipe
     */
    public UsbPipe createUsbPipe( UsbEndpoint usbEndpoint )
	{
        UsbPipeAbstraction pipe = new UsbPipeAbstraction( usbEndpoint );

		UsbPipeImp pipeImp = AbstractUsbServices.getInstance().getHelper().getUsbPipeImpFactory().createUsbPipeImp( pipe );
		pipeImp.setUsbInterfaceImp( ((UsbInterfaceAbstraction)usbEndpoint.getUsbInterface()).getUsbInterfaceImp() );

		pipe.setUsbPipeImp( pipeImp );

        return pipe;
	}

}
