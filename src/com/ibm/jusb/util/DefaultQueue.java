package com.ibm.jusb.util;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the IBM Public License.
 *
 */

import java.util.Vector;
import java.util.NoSuchElementException;

/**
 * Default implementation of the Queue interface
 * @author E. Michael Maximilien
 */
public class DefaultQueue extends Object implements Queue
{
    //-------------------------------------------------------------------------
    // Ctor(s)
    //

    /** Default ctor */
    public DefaultQueue() {}

    //-------------------------------------------------------------------------
    // Public methods
    //

    /** @return true if the queue is empty or false otherwise */
    public synchronized boolean isEmpty() { return vector.isEmpty(); }

    /** Adds the element at the end of the queue */
    public synchronized void enqueue( Object element ) { vector.addElement( element ); }

    /**
     * @return the element at the front of the queue and removes it from the queue
     * @throws java.lang.NoSuchElementException if the Queue is empty
     */
    public synchronized Object dequeue() throws NoSuchElementException
    {
        if( isEmpty() )
            throw new NoSuchElementException( "The Queue is empty!" );

        Object element = vector.elementAt( 0 );

        vector.removeElementAt( 0 );

        return element;
    }

    //-------------------------------------------------------------------------
    // Instance variables
    //

    private Vector vector = new Vector();
}
