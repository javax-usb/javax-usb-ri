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
 * Concrete class implementing the ConfigDescriptor interface
 * @author E. Michael Maximilien
 * @author Dan Streetman
 * @version 0.0.1 (JDK 1.1.x)
 */
class ConfigDescriptorImp extends AbstractDescriptor implements ConfigDescriptor
{
    //-------------------------------------------------------------------------
    // USB descriptor specific public method
    //

    /** @return the total lenght returned for this configuration */
    public short getTotalLength() { return totalLength; }

    /** @return the number of interfaces supported by this configuration */
    public byte getNumInterfaces() { return numInterfaces; }

    /** @return this configuration value */
    public byte getConfigValue() { return configValue; }

    /** @return this configuration description index */
    public byte getConfigIndex() { return configIndex; }

    /**
     * @return the attributes specifying this configuration's characteristics 
     * NOTE: this is a byte bitmap 
     */
    public byte getAttributes() { return attributes; }

    /** @return the maximum power for this configuration.  Specified in multiple of 2mA */
    public byte getMaxPower() { return maxPower; }

	/** @return this descriptor as a byte[] */
	public byte[] toBytes()
	{
		int length = UsbUtil.unsignedInt( getLength() );

		if (length < DescriptorConst.DESCRIPTOR_MIN_LENGTH_CONFIG)
			length = DescriptorConst.DESCRIPTOR_MIN_LENGTH_CONFIG;

		byte[] b = new byte[length];

		b[0] = getLength();
		b[1] = getType();
		b[2] = (byte)getTotalLength();
		b[3] = (byte)(getTotalLength()>>8);
		b[4] = getNumInterfaces();
		b[5] = getConfigValue();
		b[6] = getConfigIndex();
		b[7] = getAttributes();
		b[8] = getMaxPower();

		return b;
	}

		

    //-------------------------------------------------------------------------
    // Public accept method for the Visitor pattern
    //

    /**
     * Accepts a DescriptorVisitor objects
     * @param visitor the DescriptorVisitor object
     */
    public void accept( DescriptorVisitor visitor ) { visitor.visitConfigDescriptor( this ); }

    //-------------------------------------------------------------------------
    // Protected and package methods
    //

    /**
     * Sets this descriptor's totalLength value
     * @param w the word (i.e. short) argument
     * @exception java.lang.IllegalArgumentException for a bad argument
     */
    void setTotalLength( short w )
    {
		int len = UsbUtil.unsignedInt( w );
        if( len < 1 ) throw new IllegalArgumentException( "Illegal ConfigDescriptor.totalLength value = " + len );

        totalLength = w;
    }

    /**
     * Sets this descriptor's numInterfaces value
     * @param b the byte argument
     * @exception java.lang.IllegalArgumentException for a bad argument
     */
    void setNumInterfaces( byte b )
    {
		int n = UsbUtil.unsignedInt( b );
        if( n < 0 ) throw new IllegalArgumentException( "Illegal ConfigDescriptor.numInterfaces value = " + n );

        numInterfaces = b;
    }

    /**
     * Sets this descriptor's configValue value
     * @param b the byte argument
     * @exception java.lang.IllegalArgumentException for a bad argument
     */
    void setConfigValue( byte b )
    {
        //May need to do some pre-condition checks here

        configValue = b;
    }

    /**
     * Sets this descriptor's configIndex value
     * @param b the byte argument
     * @exception java.lang.IllegalArgumentException for a bad argument
     */
    void setConfigIndex( byte b )
    {
        //May need to do some pre-condition checks here

        configIndex = b;
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
     * Sets this descriptor's maxPower value
     * @param b the byte argument
     * @exception java.lang.IllegalArgumentException for a bad argument
     */
    void setMaxPower( byte b )
    {
        //May need to do some pre-condition checks here

        maxPower = b;
    }

    //-------------------------------------------------------------------------
    // Instance variables
    //

    private short totalLength = 0x0000;
    private byte numInterfaces = 0x00;
    private byte configValue = 0x00;
    private byte configIndex = 0x00;
    private byte attributes = 0x00;
    private byte maxPower = 0x00;
}
