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
 * Concrete class implementing the StringDescriptor interface
 * @author E. Michael Maximilien
 * @author Dan Streetman
 * @version 0.0.1 (JDK 1.1.x)
 */
class StringDescriptorImp extends AbstractDescriptor implements StringDescriptor
{
    //-------------------------------------------------------------------------
    // USB descriptor specific public method
    //

    /** @return the UNICODE encoded string for this descriptor */
    public String getString() { return string; }

	/**
	 * WARNING: This depends on the platform's default encoding for String.getBytes() to work correctly!
	 *          Also, this depends on String.getBytes() returning its bytes in a little-endian format
	 * @return this descriptor as a byte[]
	 */
	public byte[] toBytes()
	{
		int length = UsbUtil.unsignedInt( getLength() );

		if (length < DescriptorConst.DESCRIPTOR_MIN_LENGTH_STRING)
			length = DescriptorConst.DESCRIPTOR_MIN_LENGTH_STRING;

		byte[] b = new byte[length];
		byte[] s = getString().getBytes();

		b[0] = getLength();
		b[1] = getType();

		// If String.getBytes() is not little-endian, this is wrong...
		for (int i=2; i<length; i++)
			b[length] = s[length-2];

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
     * Sets this descriptor's String value
     * @param s the String argument
     */
    void setString( String s ) 
    {
        string = ( null == s ? "" : s );
    }

    //-------------------------------------------------------------------------
    // Instance variables
    //

    private String string = "";
}
