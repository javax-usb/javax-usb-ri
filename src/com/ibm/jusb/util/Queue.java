package com.ibm.jusb.util;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the IBM Public License.
 *
 */

import java.util.NoSuchElementException;

/**
 * Defines an interface for a Queue
 * @author E. Michael Maximilien
 */
public interface Queue
{
    //-------------------------------------------------------------------------
    // Public methods
    //

    /** @return true if the queue is empty or false otherwise */
    public boolean isEmpty();

    /** Adds the element at the end of the queue */
    public void enqueue( Object element );

    /**
     * @return the element at the front of the queue and removes it from the queue
     * @throws java.util.NoSuchElementException if the Queue is empty
     */
    public Object dequeue() throws NoSuchElementException;
}
