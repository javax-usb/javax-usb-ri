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

/**
 * Concrete class implementing the UsbEndpoint interface
 * @author E. Michael Maximilien
 * @author Dan Streetman
 * @version 0.0.1 (JDK 1.1.x)
 */
class UsbEndpointImp extends AbstractUsbInfo implements UsbEndpoint
{
    //-------------------------------------------------------------------------
    // Ctor(s)
    //

    /**
     * Creates an endpoint associated with the interface passed
     * @param usbInterface the UsbInterface for this endpoint
     */
    UsbEndpointImp( UsbInterface usbInterface ) 
    {
        setUsbInterface( usbInterface );
    }

    //-------------------------------------------------------------------------
    // Private helper methods
    //

    /**
     * Sets this endpoint UsbInterface
     * @param usbInterface the UsbInterface for this endpoint
     */
    private void setUsbInterface( UsbInterface iface )
    {
        usbInterface = iface;

        ((UsbInterfaceAbstraction)usbInterface).addUsbEndpoint( this );
    }

    //-------------------------------------------------------------------------
    // Public overridden interface methods
    //

    /** @return name of this UsbInfo object */
    public String getName() 
    {
        if( super.getName().equals( "" ) )
            setName( USB_ENDPOINT_NAME_STRING + (byte)(getEndpointAddress() & UsbInfoConst.ENDPOINT_NUMBER_MASK) );
        
        return super.getName();
    }

    //-------------------------------------------------------------------------
    // Public interface methods
    //

    /** @return the unique address of this endpoint */
    public byte getEndpointAddress() { return getEndpointDescriptor().getEndpointAddress(); }

    /**
     * @return direction of this endpoint (i.e. in [from device to host] or out
     * [from host to device])
     * @see javax.usb.UsbInfoConst#ENDPOINT_DIRECTION_IN
     * @see javax.usb.UsbInfoConst#ENDPOINT_DIRECTION_OUT
     */
    public byte getDirection() { return (byte)(getEndpointAddress() & UsbInfoConst.ENDPOINT_DIRECTION_MASK); }

    /** @return the attribute of this endpoint */
    public byte getAttributes() { return getEndpointDescriptor().getAttributes(); }

    /** @return this endpoint's type */
    public byte getType() { return (byte)(getAttributes() & UsbInfoConst.ENDPOINT_TYPE_MASK); }

    /** @return the max packet size required for this endpoint */
    public short getMaxPacketSize() { return getEndpointDescriptor().getMaxPacketSize(); }

    /** @return this endpoint interval */
    public byte getInterval() { return getEndpointDescriptor().getInterval(); }

    /** @return the UsbDevice associated with this Endpoint */
    public UsbDevice getUsbDevice()
	{
		return usbInterface.getUsbConfig().getUsbDevice();
	}

    /** @return the UsbInterface associated with this Endpoint */
    public UsbInterface getUsbInterface() { return usbInterface; }

    /**
	 * @return This UsbEndpoint's UsbPipe
	 */
    public UsbPipe getUsbPipe() {
        if ( null == usbPipe )
            usbPipe = UsbUtility.getInstance().getUsbPipeFactory().createUsbPipe( this );

        return usbPipe;
    }

	/** @return the endpoint descriptor for this endpoint */
	public EndpointDescriptor getEndpointDescriptor() { return (EndpointDescriptor)getDescriptor(); }

    //-------------------------------------------------------------------------
    // Public accept method for the Visitor pattern
    //

    /**
     * Visitor.accept method
     * @param visitor the UsbInfoVisitor visiting this UsbInfo
     */
    public void accept( UsbInfoVisitor visitor ) { visitor.visitUsbEndpoint( this ); }

    //-------------------------------------------------------------------------
    // Protected and package methods
    //

	/** @param desc the endpoint descriptor */
	void setEndpointDescriptor( EndpointDescriptor desc ) { setDescriptor( desc ); }

    //-------------------------------------------------------------------------
    // Instance variables
    //

    private UsbInterface usbInterface = null;
    private UsbPipe usbPipe = null;

	//-------------------------------------------------------------------------
	// Class constants
	//

    public static final String USB_ENDPOINT_NAME_STRING = "endpoint";

}
