package com.ibm.jusb.os;

/*
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 * This software is provided and licensed under the terms and conditions
 * of the IBM Public License.
 */

import javax.usb.*;

/**
 * Concrete class implementing the UsbPort interface
 * @author E. Michael Maximilien
 * @author Dan Streetman
 * @version 1.0.0
 */
class UsbPortImp extends AbstractUsbInfo implements UsbPort
{
    //-------------------------------------------------------------------------
    // Ctor(s)
    //

    /**
     * 2-args ctor
     * @param number this port number
     * @param usbHub the UsbHub device that this port belongs to
     */
    UsbPortImp( int number, UsbHub usbHub )
    {
        this.number = number;
        this.usbHub = usbHub;
    }

    //-------------------------------------------------------------------------
    // Public overridden interface methods
    //

    /** @return name of this UsbInfo object */
    public String getName() 
    {
        if( super.getName().equals( "" ) )
            setName( USB_PORT_NAME_STRING + getPortNumber() );
        
        return super.getName();
    }

    //-------------------------------------------------------------------------
    // Public interface methods
    //

	/**
	 * Return the number of this port.
	 * <p>
	 * The port number is one greater than its index in getUsbHub().getUsbPorts().
	 * It is internally represented here as an int instead of a byte, because
	 * it's really a <strong>unsigned</strong> byte.  Unfortunately, Java
	 * does not have unsigned bytes.
	 * @return the number of this port
	 */
    public byte getPortNumber() { return (byte)number; }

    /** @return USB hub that this port belongs to */
    public UsbHub getUsbHub() { return usbHub; }

    /** @return true if a device is attached to this port */
    public boolean isUsbDeviceAttached() { return (getUsbDevice() != null); }

    /** @return the attached UsbDevice to this port (null if none attached) */
    public UsbDevice getUsbDevice() { return attachedUsbDevice; }

    /** 
     * Attaches the UsbDevice to this port
     * @param usbDevice the UsbDevice to attach
	 * @throws javax.usb.UsbException if there is already a device attached
     */
    public void attachUsbDevice( UsbDevice usbDevice ) throws UsbException
    { 
		if (isUsbDeviceAttached())
			throw new UsbException( USB_PORT_DEVICE_ALREADY_ATTACHED );

        attachedUsbDevice = usbDevice; 
    }

    /** 
     * Detaches the attached UsbDevice from this port
	 * @param usbDevice the UsbDevice to detach
	 * @throws javax.usb.UsbException if the UsbDevice is not already attached
     */
    public void detachUsbDevice( UsbDevice usbDevice ) throws UsbException
	{
		try {
			if (!getUsbDevice().equals( usbDevice ))
				throw new UsbException( USB_PORT_DEVICE_NOT_ATTACHED );
		} catch ( NullPointerException npE ) {
			throw new UsbException( USB_PORT_DEVICE_NOT_ATTACHED );
		}

		attachedUsbDevice = null;
	}

    //-------------------------------------------------------------------------
    // Public accept method for the Visitor pattern
    //

    /**
     * Visitor.accept method
     * @param visitor the UsbInfoVisitor visiting this UsbInfo
     */
    public void accept( UsbInfoVisitor visitor ) { visitor.visitUsbPort( this ); }

    //-------------------------------------------------------------------------
    // Instance variables
    //

    private int number = 0;

    private UsbHub usbHub = null;
    private UsbDevice attachedUsbDevice = null;

	//-------------------------------------------------------------------------
	// Class constants
	//

    public static final String USB_PORT_NAME_STRING = "port";

	private static final String USB_PORT_DEVICE_ALREADY_ATTACHED = "UsbPort already has a UsbDevice attached";
	private static final String USB_PORT_DEVICE_NOT_ATTACHED = "Specified UsbDevice not attached";
}
