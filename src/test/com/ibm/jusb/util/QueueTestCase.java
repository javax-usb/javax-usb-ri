package com.ibm.jusb.util;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import java.util.*;

import junit.framework.*;

import com.ibm.jusb.test.JUnitUtility;

/**
 * A JUnit TestCase for the Queue interface and implementing class
 * @author E. Michael Maximilien
 * @author Helen Li
 */
public class QueueTestCase extends TestCase
{
	//-------------------------------------------------------------------------
	// Ctor(s)
	//

	public QueueTestCase( String name ) { super( name ); }

	//-------------------------------------------------------------------------
	// Protected overridden methods
	//

	protected void setUp()
	{
		queue = new DefaultQueue();
	}

	protected void tearDown()
	{
	}

	//-------------------------------------------------------------------------
	// Public testXyz() methods
	//
	//
    
	public void testIsEmpty()
	{
        assertTrue( "Queue is not empty", queue.isEmpty() );
    }

    public void testEnqueueDequeue()
    {
		Object element1 = "element1";
		Object element2 = "element2";
		Object element3 = "element3";

        assertTrue( "Queue is not empty", queue.isEmpty() );

		queue.enqueue( element1 );
        assertTrue( "Queue is empty", !queue.isEmpty() );

		queue.enqueue( element2 );
        assertTrue( "Queue is empty", !queue.isEmpty() );

		queue.enqueue( element3 );
        assertTrue( "Queue is empty", !queue.isEmpty() );

		assertTrue( "Queue returned elements out of order", element1 == queue.dequeue() );
		assertTrue( "Queue returned elements out of order", element2 == queue.dequeue() );
		assertTrue( "Queue returned elements out of order", element3 == queue.dequeue() );
       
		assertTrue ( "Queue is not empty", queue.isEmpty() );
       
		try
		{
			queue.dequeue();
			fail("Expected NoSuchElementException not thrown");
		}
		catch (NoSuchElementException e)
		{ 
			assertTrue( "Queue is not empty after throwing NoSuchElementException", queue.isEmpty() );
		}
    }

  	   
	//-------------------------------------------------------------------------
	// Instance variables
	//

	private DefaultQueue queue = null;
}
