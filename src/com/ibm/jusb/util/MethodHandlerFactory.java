package com.ibm.jusb.util;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the IBM Public License.
 *
 */


/** 
 * Copyright (c) IBM Corporation, 2000
 * This software and documentation is the confidential and proprietary
 * information of IBM, Corp. ("Confidential Information").
 * Raleigh, NC USA
 */

/**
 * Factory to generate recyclable MethodHandler objects
 * @author Dan Streetman
 * @version 0.0.1 (JDK 1.1.x)
 */
public class MethodHandlerFactory extends RecycleFactory
{
	/** Constructor */
	public MethodHandlerFactory() { }

	//*************************************************************************
	// Public methods

	/** @return a MethodHandler */
	public Recyclable take() { return takeMethodHandler(); }

	/** @return a 'clean' MethodHandler ready for use */
	public MethodHandler takeMethodHandler()
	{
		MethodHandler handler = (MethodHandler)super.take();

		handler.setActive( true );

		return handler;
	}

	/** @param the MethodHandler that is done with (which will be recycled) */
	public void recycleMethodHandler( MethodHandler handler )
	{
		handler.setActive( false );

		recycle( handler );
	}

	//*************************************************************************
	// Protected methods

	/** @param the MethodHandler to dispose */
	protected void disposeRecyclable( Recyclable recyclable )
	{
		MethodHandler handler = (MethodHandler)recyclable;

		handler.stop();
	}

	/** @return a new MethodHandler */
	protected Recyclable createRecyclable() { return new MethodHandler( this ); }

}
