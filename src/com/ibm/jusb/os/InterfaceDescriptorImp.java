package com.ibm.jusb.os;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import javax.usb.*;
import javax.usb.util.UsbUtil;

/**
 * Concrete class implementing the InterfaceDescriptor interface
 * @author E. Michael Maximilien
 * @author Dan Streetman
 * @version 0.0.1 (JDK 1.1.x)
 */
class InterfaceDescriptorImp extends AbstractDescriptor implements InterfaceDescriptor
{
    //-------------------------------------------------------------------------
    // USB descriptor specific public method
    //

    /** @return the interface number */
    public byte getInterfaceNumber() { return interfaceNumber; }

    /** @return the alternate setting for this interface */
    public byte getAlternateSetting() { return alternateSetting; }

    /** @return the number of endpoints used by this interface (excludes endpoint 0) */
    public byte getNumEndpoints() { return numEndpoints; }

    /** @return the interface class code */
    public byte getInterfaceClass() { return interfaceClass; }

    /** @return the interface subclass code */
    public byte getInterfaceSubClass() { return interfaceSubClass; }

    /** @return the interface protocol code */
    public byte getInterfaceProtocol() { return interfaceProtocol; }

    /** @return the interface StringDescriptor index code */
    public byte getInterfaceIndex() { return interfaceIndex; }

	/** @return this descriptor as a byte[] */
	public byte[] toBytes()
	{
		int length = UsbUtil.unsignedInt( getLength() );

		if (length < DescriptorConst.DESCRIPTOR_MIN_LENGTH_INTERFACE)
			length = DescriptorConst.DESCRIPTOR_MIN_LENGTH_INTERFACE;

		byte[] b = new byte[length];

		b[0] = getLength();
		b[1] = getType();
		b[2] = getInterfaceNumber();
		b[3] = getAlternateSetting();
		b[4] = getNumEndpoints();
		b[5] = getInterfaceClass();
		b[6] = getInterfaceSubClass();
		b[7] = getInterfaceProtocol();
		b[8] = getInterfaceIndex();

		return b;
	}

    //-------------------------------------------------------------------------
    // Public accept method for the Visitor pattern
    //

    /**
     * Accepts a DescriptorVisitor objects
     * @param visitor the DescriptorVisitor object
     */
    public void accept( DescriptorVisitor visitor ) { visitor.visitInterfaceDescriptor( this ); }

    //-------------------------------------------------------------------------
    // Protected and package methods
    //

    /**
     * Sets this descriptor's interfaceNumber value
     * @param b the byte argument
     * @exception java.lang.IllegalArgumentException for a bad argument
     */
    void setInterfaceNumber( byte b )
    {
        //May need to do some pre-condition checks here

        interfaceNumber = b;
    }

    /**
     * Sets this descriptor's alternateSetting value
     * @param b the byte argument
     * @exception java.lang.IllegalArgumentException for a bad argument
     */
    void setAlternateSetting( byte b )
    {
        //May need to do some pre-condition checks here

        alternateSetting = b;
    }

    /**
     * Sets this descriptor's numEndpoints value
     * @param b the byte argument
     * @exception java.lang.IllegalArgumentException for a bad argument
     */
    void setNumEndpoints( byte b )
    {
        //May need to do some pre-condition checks here

        numEndpoints = b;
    }

    /**
     * Sets this descriptor's interfaceClass value
     * @param b the byte argument
     * @exception java.lang.IllegalArgumentException for a bad argument
     */
    void setInterfaceClass( byte b )
    {
        //May need to do some pre-condition checks here

        interfaceClass = b;
    }

    /**
     * Sets this descriptor's interfaceSubClass value
     * @param b the byte argument
     * @exception java.lang.IllegalArgumentException for a bad argument
     */
    void setInterfaceSubClass( byte b )
    {
        //May need to do some pre-condition checks here

        interfaceSubClass = b;
    }

    /**
     * Sets this descriptor's interfaceProtocol value
     * @param b the byte argument
     * @exception java.lang.IllegalArgumentException for a bad argument
     */
    void setInterfaceProtocol( byte b )
    {
        //May need to do some pre-condition checks here

        interfaceProtocol = b;
    }

    /**
     * Sets this descriptor's interfaceIndex value
     * @param b the byte argument
     * @exception java.lang.IllegalArgumentException for a bad argument
     */
    void setInterfaceIndex( byte b )
    {
        //May need to do some pre-condition checks here

        interfaceIndex = b;
    }

    //-------------------------------------------------------------------------
    // Instance variables
    //

    private byte interfaceNumber = 0x00;
    private byte alternateSetting = 0x00;
    private byte numEndpoints = 0x00;
    private byte interfaceClass = 0x00;
    private byte interfaceSubClass = 0x00;
    private byte interfaceProtocol = 0x00;
    private byte interfaceIndex = 0x00;
}
