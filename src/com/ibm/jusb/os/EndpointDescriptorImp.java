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
import javax.usb.util.UsbUtil;

/**
 * Concrete class implementing the EndpointDescriptor interface
 * @author E. Michael Maximilien
 * @author Dan Streetman
 * @version 0.0.1 (JDK 1.1.x)
 */
class EndpointDescriptorImp extends AbstractDescriptor implements EndpointDescriptor
{
    //-------------------------------------------------------------------------
    // USB descriptor specific public method
    //

    /** @return the address of the endpoint on the USB device described by this descriptor */
    public byte getEndpointAddress() { return endpointAddress; }

    /** @return this endpoint's attributes info */
    public byte getAttributes() { return attributes; }

    /** @returnt the maximum packet size this endpoint is capable of sending or recieving */
    public short getMaxPacketSize() { return maxPacketSize; }

    /** 
     * @returnt the interval for polling endpoint for data tansfers (in ms) 
     * NOTE: ignored for bulk and control endpoints.  Must be 1 for isochonous 
     * and 1-255 for Interrupt endpoints
     */
    public byte getInterval() { return interval; }

	/** @return this descriptor as a byte[] */
	public byte[] toBytes()
	{
		int length = UsbUtil.unsignedInt( getLength() );

		if (length < DescriptorConst.DESCRIPTOR_MIN_LENGTH_ENDPOINT)
			length = DescriptorConst.DESCRIPTOR_MIN_LENGTH_ENDPOINT;

		byte[] b = new byte[length];

		b[0] = getLength();
		b[1] = getType();
		b[2] = getEndpointAddress();
		b[3] = getAttributes();
		b[4] = (byte)getMaxPacketSize();
		b[5] = (byte)(getMaxPacketSize()>>8);
		b[6] = getInterval();

		return b;
	}

    //-------------------------------------------------------------------------
    // Public accept method for the Visitor pattern
    //

    /**
     * Accepts a DescriptorVisitor objects
     * @param visitor the DescriptorVisitor object
     */
    public void accept( DescriptorVisitor visitor ) { visitor.visitStringDescriptor( this ); }

    //-------------------------------------------------------------------------
    // Protected and package methods
    //

    /**
     * Sets this descriptor's endpointAddress
     * @param b the byte argument
     * @exception java.lang.IllegalArgumentException for a bad argument
     */
    void setEndpointAddress( byte b )
    {
        //May need to do some pre-condition checks here

        endpointAddress = b;
    }

    /**
     * Sets this descriptor's attributes value
     * @param b the byte argument
     * @exception java.lang.IllegalArgumentException for a bad argument
     */
    void setAttributes( byte b )
    {
        //May need to do some pre-condition checks here

        attributes = b;
    }

    /**
     * Sets this descriptor's maxPacketSize value
     * @param w the word (i.e. short) argument
     * @exception java.lang.IllegalArgumentException for a bad argument
     */
    void setMaxPacketSize( short w )
    {
        //May need to do some pre-condition checks here

        maxPacketSize = w;
    }

    /**
     * Sets this descriptor's interval value
     * @param b the byte argument
     * @exception java.lang.IllegalArgumentException for a bad argument
     */
    void setInterval( byte b )
    {
        //May need to do some pre-condition checks here

        interval = b;
    }

    //-------------------------------------------------------------------------
    // Instance variables
    //

    private byte endpointAddress = 0x00;
    private byte attributes = 0x00;
    private short maxPacketSize = 0x0000;
    private byte interval = 0x00;
}
