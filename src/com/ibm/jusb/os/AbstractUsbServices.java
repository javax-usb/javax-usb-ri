package com.ibm.jusb.os;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import java.util.Vector;

import javax.usb.*;
import javax.usb.os.*;
import javax.usb.util.*;
import javax.usb.event.*;

import com.ibm.jusb.util.*;

/**
 * Basic abstract class implementing the UsbServices interface
 * @author E. Michael Maximilien
 * @version 0.0.1
 */
public abstract class AbstractUsbServices extends Object implements UsbServices
{
    //-------------------------------------------------------------------------
    // Public ctor
    //

    /** Creates a default UsbServices with default TaskScheduler */
    public AbstractUsbServices() { this( new FifoScheduler() ); }

    /** 
     * Creates a UsbServices using the TaskScheduler passed
     * @param eventTaskScheduler the TaskScheduler for the TopologyHelper
     */
    public AbstractUsbServices( TaskScheduler eventTaskScheduler ) 
    { 
		if (null != abstractUsbServices)
			throw new UsbRuntimeException( "Cannot create multiple instances of UsbServices" );

		abstractUsbServices = this;
        this.eventTaskScheduler = eventTaskScheduler;
        topologyHelper = new UsbTopologyServicesHelper( this, eventTaskScheduler  );  
    }

    //-------------------------------------------------------------------------
    // Public abstract methods
    //

	/**
	 * @return a UsbInfoIterator of UsbDevice in breadth-first search (BFS) order
	 * @param usbHub the UsbHub object whose children will be queried
	 * <i>NOTE: since UsbHub are UsbDevice then they are also included in return list</i>
	 */
	public UsbInfoIterator bfsUsbDevices( UsbHub usbHub )
	{
		return getUsbServicesHelper().bfsUsbDevices( usbHub );
	}

	/**
	 * @return a UsbInfoIterator of UsbDevice in depth-first search (DFS) order
	 * @param usbHub the UsbHub object whose children will be queried
	 * <i>NOTE: since UsbHub are UsbDevice then they are also included in return list</i>
	 */
	public UsbInfoIterator dfsUsbDevices( UsbHub usbHub )
	{
		return getUsbServicesHelper().dfsUsbDevices( usbHub );
	}

    //-------------------------------------------------------------------------
    // Public abstract methods
    //

    /**
     * Accepts a DescriptorVisitor objects
     * @param visitor the OSServicesVisitor object
     */
    public abstract void accept( UsbServicesVisitor visitor );

	/** @return the UsbServices.Helper object */
	public abstract AbstractUsbServices.AbstractHelper getHelper();

    //-------------------------------------------------------------------------
    // Public registration methods
    //

    /**
     * Adds a new UsbServicesListener object to receive events when the USB host
     * has changes.  For instance a new device is plugged in or unplugged.
     * @param l the UsbServicesListener to register     
     */
    public synchronized void addUsbServicesListener( UsbServicesListener l )
    {
        getTopologyHelper().addUsbServicesListener( l );
    }

    /**
     * Adds a new UsbServicesListener object to receive events when the USB host
     * has changes.  For instance a new device is plugged in or unplugged.
     * @param l the UsbServicesListener to register     
     */
    public synchronized void removeUsbServicesListener( UsbServicesListener l )
    {
        getTopologyHelper().removeUsbServicesListener( l );
    }

	/**
	 * @return the RequestFactory used to create Request object for the USB operations
	 * @see javax.usb.Request
	 * @see javax.usb.StandardOperations
	 */ 
	public RequestFactory getRequestFactory()
	{
		if( requestFactory == null )
			requestFactory = new DefaultRequestFactory();

		return requestFactory;
	}

	/** @return a new instance of a RequestFactory */
	public RequestFactory getNewRequestFactory()
	{
		return new DefaultRequestFactory();
	}

	/** @return the current UsbIrpFactory being used */
	public UsbIrpFactory getUsbIrpFactory()
	{
		if ( null == usbIrpFactory )
			usbIrpFactory = new DefaultUsbIrpFactory();

		return usbIrpFactory;
	}

	/** @return a new instance of a UsbIrpFactory */
	public UsbIrpFactory getNewUsbIrpFactory()
	{
		return new DefaultUsbIrpFactory();
	}

    //-------------------------------------------------------------------------
    // Protected methods
    //

    /** @return the UsbTopologyServicesHelper object */
    protected UsbTopologyServicesHelper getTopologyHelper() { return topologyHelper; }

    /** @return the TaskScheduler object */
    protected TaskScheduler getEventTaskScheduler() { return eventTaskScheduler; }

	/** @return a lazily created UsbServicesHelper used for the BFS/DFS methods */
	protected UsbServicesHelper getUsbServicesHelper()
	{
		if( usbServicesHelper == null )
			usbServicesHelper = new UsbServicesHelper();

		return usbServicesHelper;
	}

	//-------------------------------------------------------------------------
	// Package methods
	//

	/** @return an instance of this class */
	static AbstractUsbServices getInstance()
	{
		return abstractUsbServices;
	}

    //-------------------------------------------------------------------------
    // Inner interfaces
    //

	/**
	 * Defines a helper interface for all UsbServices implementation "communication"
	 * @author E. Michael Maximilien
	 */
	public abstract class AbstractHelper extends Object
	{
		//---------------------------------------------------------------------
		// Public methods
		//

		/** @return the current UsbInfoFactory being used */
		public UsbInfoFactory getUsbInfoFactory() { return UsbUtility.getInstance().getUsbInfoFactory(); }

		/** @return the current DescriptorFactory being used */
		public DescriptorFactory getDescriptorFactory() { return UsbUtility.getInstance().getDescriptorFactory(); }

		/** @return the current UsbIrpImpFactory being used */
		public RecycleFactory getUsbIrpImpFactory() { return UsbUtility.getInstance().getUsbIrpImpFactory(); }

		/** @return the current UsbCompositeIrpImpFactory being used */
		public RecycleFactory getUsbCompositeIrpImpFactory() { return UsbUtility.getInstance().getUsbCompositeIrpImpFactory(); }

		//---------------------------------------------------------------------
		// Abstract public methods
		//

		/** @return the current UsbPipeImpFactory being used */
		public abstract UsbPipeImpFactory getUsbPipeImpFactory();

		/** @return the current UsbInfoImpFactory being used */
		public abstract UsbInfoImpFactory getUsbInfoImpFactory();

	}

    //-------------------------------------------------------------------------
    // Instance variables
    //

    private TaskScheduler eventTaskScheduler = null;
    private UsbTopologyServicesHelper topologyHelper = null;
	private RequestFactory requestFactory = null;
	private UsbIrpFactory usbIrpFactory = null;
	private UsbServicesHelper usbServicesHelper = null;

	//-------------------------------------------------------------------------
	// Class variables
	//

	private static AbstractUsbServices abstractUsbServices = null;
}
