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
 * Abstract super-class for all USB descriptors
 * @author E. Michael Maximilien
 * @author Dan Streetman
 * @version 0.0.1 (JDK 1.1.x)
 */
public abstract class AbstractDescriptor extends Object implements Descriptor
{
    //-------------------------------------------------------------------------
    // USB descriptor specific public method
    //

    /** @return the length of this descriptor */
    public byte getLength() { return length; }

    /** @return the type of this descriptor */
    public byte getType() { return type; }

    //-------------------------------------------------------------------------
    // Public abstract methods
    //

    /**
     * Accepts a DescriptorVisitor objects
     * @param visitor the DescriptorVisitor object
     */
    public abstract void accept( DescriptorVisitor visitor );

    //-------------------------------------------------------------------------
    // Protected and package methods
    //

    /**
     * Sets this descriptor's length
     * @param b the > 0 lenght
     * @exception java.lang.IllegalArgumentException for a bad argument
     */
    void setLength( byte b ) 
    {
        //Precondition check
		int len = UsbUtil.unsignedInt( b );
        if( len < DescriptorConst.DESCRIPTOR_MIN_LENGTH )
			throw new IllegalArgumentException( "Illegal Descriptor.length value = " + len );

        length = b; 
    }

    /**
     * Sets this descriptor's type
     * @param b the type of this descriptor (see constants)
     * @exception java.lang.IllegalArgumentException for a bad argument
     */
    void setType( byte b ) throws IllegalArgumentException
    {
        //May need to do some validation or checking

        type = b;
    }

    //-------------------------------------------------------------------------
    // Instance variables
    //

    private byte length = 0x00;
    private byte type = 0x00;
}
