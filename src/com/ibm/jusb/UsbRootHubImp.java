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
import javax.usb.util.*;

/**
 * Concrete class implementing the UsbHub interface
 * @author E. Michael Maximilien
 * @author Dan Streetman
 * @version 0.0.1 (JDK 1.1.x)
 */
class UsbRootHubImp extends UsbHubImp implements UsbRootHub
{
    //-------------------------------------------------------------------------
    // Ctor(s)
    //

    /**
     * Creates a RootUsbHub with an unspecified number of ports
     * as devices are attached to this hub the number of ports will grow
     */
    UsbRootHubImp() { super( 0 ); }

    /**
     * Creates a RootUsbHub with a fix set of ports
     * @param numberOfPorts the total number of ports for this hub
     */
    UsbRootHubImp( int numberOfPorts ) { super( numberOfPorts ); }

    //-------------------------------------------------------------------------
    // Public methods
    //

    /**
     * Attach this device to this hub at the port specified
     * @param usbDevice the UsbDevice to attach
     * @param portNumber the port that this device is attached to (0-based)
     * @exception javax.usb.UsbException if the device is being attached to 
     * a non-existing port or that the port already is occupied
     */
    public void addDevice( UsbDevice usbDevice, byte portNumber ) throws UsbException
    {
		if ( UsbUtil.unsignedInt( portNumber ) > UsbUtil.unsignedInt( getNumberOfPorts() ) )
			createUsbPorts( UsbUtil.unsignedInt( portNumber ) - UsbUtil.unsignedInt( getNumberOfPorts() ) );

		super.addDevice( usbDevice, portNumber );
    }

    /** @return true if this is the root hub */
    public boolean isUsbRootHub() { return true;  }

    //-------------------------------------------------------------------------
    // Public accept method for the Visitor pattern
    //

    /**
     * Visitor.accept method
     * @param visitor the UsbInfoVisitor visiting this UsbInfo
     */
    public void accept( UsbInfoVisitor visitor ) { visitor.visitUsbRootHub( this ); }

	//-------------------------------------------------------------------------
	// Class constants
	//

	public static final String USB_ROOT_HUB_NAME = "*hub";
}
