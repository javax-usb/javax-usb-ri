package com.ibm.jusb.util;

/** 
 * Copyright (c) IBM Corporation, 2000
 * This software and documentation is the confidential and proprietary
 * information of IBM, Corp. ("Confidential Information").
 * Raleigh, NC USA
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

	protected void setUp() { queue = new DefaultQueue(); }

	protected void tearDown() { queue = null; }

	//-------------------------------------------------------------------------
	// Public testXyz() methods
	//
	//
    
	public void testIsEmpty()
	{
        assertTrue( "Queue is not empty", queue.isEmpty() == true );
    }

    public void testEnqueueDequeue()
    {
		Object element1 = "element1";
		Object element2 = "element2";
		Object element3 = "element3";

        assertTrue( "Queue is not empty", queue.isEmpty() == true );

		queue.enqueue( element1 );
        assertTrue( "Queue is not empty", queue.isEmpty() == false );

		queue.enqueue( element2 );
        assertTrue( "Queue is not empty", queue.isEmpty() == false );

		queue.enqueue( element3 );
        assertTrue( "Queue is not empty", queue.isEmpty() == false );

		assertEquals ( "It did not return element1!", element1, queue.dequeue() );
		assertEquals ( "It did not return element2!", element2, queue.dequeue() );
		assertEquals ( "It did not return element3!", element3, queue.dequeue() );
       
		assertTrue ( "Queue is not empty!", queue.isEmpty() == true );
       
		try
		{
			queue.dequeue();
			assertTrue("it did not throw an exception!!!", false);
		}
		catch (NoSuchElementException e)
		{ 
			assertTrue( "Queue is not empty!", queue.isEmpty() == true );
		}
    }

  	   
	//-------------------------------------------------------------------------
	// Instance variables
	//

	private DefaultQueue queue = null;
}
