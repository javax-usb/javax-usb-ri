package com.ibm.jusb.os;

/** 
 * Copyright (c) IBM Corporation, 2000
 * This software and documentation is the confidential and proprietary
 * information of IBM, Corp. ("Confidential Information").
 * Raleigh, NC USA
 */

import java.util.*;

import javax.usb.*;
import javax.usb.util.*;

/**
 * Implementation of a UsbPipeBundle.
 * @author Dan Streetman
 * @since 1.0.1
 * @version 1.0.1
 */
public class UsbPipeBundleImp implements UsbPipeBundle
{
	/** Constructor */
	public UsbPipeBundleImp() { }

	//*************************************************************************
    // Public methods

    /**
     * Adds a new UsbPipe to this bundle.
     * @param usbPipe the UsbPipe to add.
	 * @throws javax.usb.UsbRuntimeException if a pipe with the same endpoint address is already contained in this.
     */
    public void addUsbPipe( UsbPipe usbPipe )
	{
		synchronized ( usbPipes ) {
			if (containsUsbPipe( usbPipe.getEndpointAddress() ))
				throw new UsbRuntimeException( "UsbPipeBundle already contains UsbPipe with address 0x" + UsbUtil.toHexString( usbPipe.getEndpointAddress() ) );

			usbPipes.put( new Byte( usbPipe.getEndpointAddress() ), usbPipe );
		}
	}

    /**
     * Removes the UsbPipe from this bundle.
	 * <p>
	 * If the specified UsbPipe is not contained in this bundle, no action
	 * is taken and false is returned.
     * @param usbPipe the UsbPipe to remove.
	 * @return if the specified UsbPipe was removed from this bundle.
     */
    public boolean removeUsbPipe( UsbPipe usbPipe ) { return (null != removeUsbPipe( usbPipe.getEndpointAddress() )); }

    /**
     * Removes the UsbPipe with the specified address from this bundle.
	 * <p>
	 * If there is no UsbPipe with the specified address contained in this bundle,
	 * no action is taken and null is returned.
     * @param address the address of the pipe to remove.
	 * @return the UsbPipe that was removed, or null if nothing was removed
     */
    public UsbPipe removeUsbPipe( byte address ) { return (UsbPipe)usbPipes.remove( new Byte( address ) ); }

    /**
	 * Get the UsbPipe with the specified UsbEndpoint address.
     * @param epAddress the address of the UsbEndpoint.
     * @return the UsbPipe belonging to the UsbEndpoint with the specified address.
	 * @throws javax.usb.UsbRuntimeException if a pipe with the specified address is not conatined in this.
     */
    public UsbPipe getUsbPipe( byte epAddress )
	{
		synchronized ( usbPipes ) {
			if (!containsUsbPipe( epAddress ))
				throw new UsbRuntimeException( "UsbPipeBundle does not contain UsbPipe with address 0x" + UsbUtil.toHexString( epAddress ) );
			else
				return (UsbPipe)usbPipes.get( new Byte( epAddress ) );
		}
	}

	/**
	 * If this contains a UsbPipe with the specified endpoint address.
	 * @return if this contains a UsbPipe with the specified address.
	 */
	public boolean containsUsbPipe( byte epAddress ) { return usbPipes.containsKey( new Byte( epAddress ) ); }

    /**
	 * Get the number of UsbPipes in the bundle.
	 * @return the current size of this bundle.
	 */
    public int size() { return usbPipes.size(); }

    /**
	 * If this bundle is empty.
	 * @return true if this bundle is empty.
	 */
    public boolean isEmpty() { return usbPipes.isEmpty(); }

    /**
	 * Get an Enumeration of the UsbPipes.
	 * @return an Enumeration of UsbPipe objects in this bundle.
	 */
    public Enumeration elements() { return usbPipes.elements(); }

	//*************************************************************************
	// Instance variables

	private Hashtable usbPipes = new Hashtable();
}
