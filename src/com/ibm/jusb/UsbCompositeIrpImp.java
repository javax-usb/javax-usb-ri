package com.ibm.jusb;

/*
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import java.util.*;

import javax.usb.*;
import javax.usb.util.*;

import com.ibm.jusb.util.*;

/**
 * Defines a Recyclable Usb Composite IRP (I/O Request Packet) 
 * @author Dan Streetman
 * @version 1.0.0
 */
public class UsbCompositeIrpImp extends UsbIrpImp implements UsbCompositeIrp, 
												  com.ibm.jusb.util.Recyclable
{
	/**
	 * Constructor
	 * @param factory the factory that created this
	 */
	public UsbCompositeIrpImp( RecycleFactory factory )
	{
		super( factory );

		setEventCommand( nullCompositeEventCommand );
	}

	//*************************************************************************
	// Public methods

	/**
	 * Accept a UsbIrpImpVisitor
	 * @param the UsbIrpImpVisitor to accept
	 */
	public void accept( UsbIrpImpVisitor visitor ) { visitor.visitUsbCompositeIrpImp( this ); }

	public UsbIrpList getUsbIrps() { return usbIrpList; }

	/**
	 * Get the CompositeErrorCommand currently in use.
	 * @return the CompositeErrorCommand in use.
	 */
	public UsbCompositeIrp.CompositeErrorCommand getCompositeErrorCommand() { return compositeErrorCommand; }

	/**
	 * Set the CompositeErrorCommand currently in use.
	 * @param command the CompositeErrorCommand to use.
	 */
	public void setCompositeErrorCommand( UsbCompositeIrp.CompositeErrorCommand command )
	{
		if (null == command)
			command = nullCompositeErrorCommand;

		compositeErrorCommand = command;
	}

	/**
	 * Set the EventCommand currently in use.
	 * @param the EventCommand to use.
	 */
	public void setEventCommand( UsbIrp.EventCommand command )
	{
		if (null == command)
			command = nullCompositeEventCommand;

		super.setEventCommand( command );
	}

	//*************************************************************************
	// Public overridden methods

	/** Clean this object */
	public void clean()
	{
		super.clean();

		getUsbIrps().clear();

		setCompositeErrorCommand( null );
		setEventCommand( null );
	}

	//*************************************************************************
	// Instance variables

	private UsbIrpList usbIrpList = new CompositeUsbIrpList();

	private UsbCompositeIrp.CompositeErrorCommand compositeErrorCommand = nullCompositeErrorCommand;

	//*************************************************************************
	// Class variables

	public static final UsbCompositeIrp.CompositeErrorCommand nullCompositeErrorCommand = new UsbCompositeIrp.CompositeErrorCommand() {
		public boolean continueSubmissions( UsbCompositeIrp composite, UsbIrp irp ) { return false; }
	};

	public static final UsbIrp.EventCommand nullCompositeEventCommand = new UsbIrp.EventCommand() {
		public boolean shouldFireEvent( UsbIrp irp ) { return false; }
	};

	//*************************************************************************
	// Inner classes

/* Hmm..mabye we should consider *allowing* user-supplied UsbIrps? */
	public class CompositeUsbIrpList extends DefaultUsbIrpList implements UsbIrpList
	{
		private void checkUsbIrp( UsbIrp usbIrp )
		{
			if (!(usbIrp instanceof UsbIrpImp))
				throw new UsbRuntimeException( "Cannot handle UsbIrp implementation " + usbIrp.getClass().getName() );
		}

		public void addUsbIrp( int index, UsbIrp usbIrp )
		{
			checkUsbIrp( usbIrp );
			super.addUsbIrp( index, usbIrp );
		}

		public void addUsbIrp( UsbIrp usbIrp )
		{
			checkUsbIrp( usbIrp );
			super.addUsbIrp( usbIrp );
		}

		public UsbIrp setUsbIrp( int index, UsbIrp usbIrp )
		{
			checkUsbIrp( usbIrp );
			return super.setUsbIrp( index, usbIrp );
		}
	}
}
