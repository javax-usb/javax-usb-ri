package com.ibm.jusb.util;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import java.lang.reflect.*;

import javax.usb.*;
import javax.usb.event.*;

import com.ibm.jusb.*;

/**
 * MethodHandler object - executes Methods in a seperate Thread
 * @author Dan Streetman
 * @version 0.0.1 (JDK 1.1.x)
 */
public class MethodHandler implements Recyclable
{
	/** Constructor */
	public MethodHandler( ) { this( null ); }

	/** Constructor */
	public MethodHandler( MethodHandlerFactory methodHandlerFactory )
	{
		factory = methodHandlerFactory;

		try {
			thread.setDaemon( true );
			thread.setName( getThreadName() );
		} catch ( IllegalThreadStateException itsE ) {
		} catch ( SecurityException sE ) {
		}

		try {
			alive = true;
			thread.start();
		} catch ( IllegalThreadStateException itsE ) {
			stop();
		}
	}

	//*************************************************************************
	// Public methods

	/**
	 * Call specified Method
	 * @param object the Object that owns the Method
	 * @param method the Method to invoke
	 * @param params the parameters
	 */
	public void runMethod( Object object, Method method, Object[] params )
	{
		this.object = object;
		objectMethod = method;
		objectMethodParameters = params;
		ready = true;

		synchronized ( methodLock ) { methodLock.notifyAll(); }
	}

	/**
	 * Call specified Method
	 * @param object the Object that owns the Method
	 * @param method the Method to invoke
	 * @param param a single parameter
	 */
	public void runMethod( Object object, Method method, Object param )
	{
		objectMethodSingleParameter[0] = param;

		runMethod( object, method, objectMethodSingleParameter );
	}

	/** @return true if this is active (does not belong to a factory) */
	public boolean isActive() { return active; }

	//*************************************************************************
	// Recyclable methods

	/** Clean this object */
	public void clean()
	{
//Surely some cleaning is needed...?
	}

	/** Recycle this Recyclable */
	public void recycle()
	{
		if (null != factory)
			factory.recycleMethodHandler( this );
	}

	//*************************************************************************
	// Package methods

	/** Indicates to this handler's thread to end */
	void stop() { alive = false; }

	/** @param active if this is active (does not belong to a factory) */
	void setActive( boolean active ) { this.active = active; }

	//*************************************************************************
	// Protected methods

	/** @return the name of this handler's thread */
	protected String getThreadName()
	{
		return "MethodHandler" + id++;
	}

	/** Do the work */
	protected void executeMethod()
	{
		do {
			synchronized ( methodLock ) {
				while ( alive && !ready ) {
					try { methodLock.wait(); }
					catch ( InterruptedException iE ) { }
				}
			}

			if ( alive && active && ready ) {
				try {
					objectMethod.invoke( object, objectMethodParameters );
				} catch ( Exception e ) {
/* Do something */
/* There could be a user-supplyable Command to execute in this case */
				} finally {
					ready = false;
				}
			}

			if ( alive && active )
				recycle();
		} while ( alive );
	}

	//*************************************************************************
	// Instance variables

	private Object object = null;
	private Method objectMethod = null;
	private Object[] objectMethodParameters = null;
	private Object[] objectMethodSingleParameter = new Object[1];

	private Object methodLock = new Object();

	private MethodHandlerFactory factory = null;

	private Runnable runnable = new MethodHandlerRunnable();
	private Thread thread = new Thread( runnable );

	/** true if this should continue */
	private boolean alive = false;
	/** true if this is checked out of a factory */
	private boolean active = false;
	/** true if this is ready to run a Method */
	private boolean ready = false;

	//*************************************************************************
	// Class variables

	private static int id = 1;

	//*************************************************************************
	// Inner classes

	protected class MethodHandlerRunnable implements Runnable
	{
		public void run()
		{ MethodHandler.this.executeMethod(); }
	};

}
