package com.ibm.jusb.util;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the IBM Public License.
 *
 */

import java.util.*;

/**
 * Factory to generate Recyclable Objects
 * @author Dan Streetman
 * @version 0.0.1 (JDK 1.1.x)
 */
public abstract class RecycleFactory
{
	//*************************************************************************
	// Public methods

	/** @return a 'clean' Recyclable ready for use */
	public Recyclable take()
	{
		Recyclable recyclable;

		synchronized ( supply ) {
			if (supply.size() <= getMinSize())
				growRecyclables();

			recyclable = (Recyclable)supply.remove(0);
		}

		return recyclable;
	}

	/**
	 * This will return the Recyclable to the factory.
	 * <p>
	 * This should be called by the recyclable's recycle() method.
	 * @param the Recyclable that is done with
	 */
	public void recycle( Recyclable recyclable )
	{
		cleanRecyclable( recyclable );

		if (supply.size() < getMaxSize()) {
			synchronized ( supply ) { supply.add( recyclable ); }
		} else {
			disposeRecyclable( recyclable );
		}
	}

	//*************************************************************************
	// Protected methods

	/** Grow a new generation of Recyclables */
	protected void growRecyclables()
	{
		for (int i=0; i<getGenerationSize(); i++)
			supply.add( createRecyclable() );
	}

	/**
	 * This should return a new object of the proper type
	 * @return a new Recyclable
	 */
	protected abstract Recyclable createRecyclable();

	/**
	 * This is called when the object should be 'cleaned'.
	 * The object should be ready for use with default values after this
	 * @param the Object to clean
	 */
	protected void cleanRecyclable( Recyclable recyclable ) { recyclable.clean(); }

	/**
	 * This is called when this factory will not accept the Recyclable object
	 * so it should stop all activity and prepare to be GC'ed
	 * @param the Recyclable being disposed
	 */
	protected void disposeRecyclable( Recyclable recyclable ) { }

	/** @param the minimum size of the supply */
	protected void setMinSize( int size ) { defaultMinSize = size; }

	/** @param the maximum size of the supply */
	protected void setMaxSize( int size ) { defaultMaxSize = size; }

	/** @param the size of a generation of new Objects */
	protected void setGenerationSize( int size ) { defaultGenerationSize = size; }

	//*************************************************************************
	// Public methods

	/** @return the minimum size of the supply */
	public int getMinSize() { return defaultMinSize; }

	/** @return the maximum size of the supply */
	public int getMaxSize() { return defaultMaxSize; }

	/** @return the size of a generation of new Objects */
	public int getGenerationSize() { return defaultGenerationSize; }

	//*************************************************************************
	// Instance variables

	private Vector supply = new Vector();

	private int defaultMinSize = DEFAULT_MIN_SIZE;
	private int defaultMaxSize = DEFAULT_MAX_SIZE;
	private int defaultGenerationSize = DEFAULT_GENERATION_SIZE;

	//*************************************************************************
	// Class constants

	public static final int DEFAULT_MIN_SIZE = 5;
	public static final int DEFAULT_MAX_SIZE = 500;
	public static final int DEFAULT_GENERATION_SIZE = 20;

}
