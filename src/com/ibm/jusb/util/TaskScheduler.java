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
 * Defines an interface for scheduling Task objects
 * @author E. Michael Maximilien
 */
public interface TaskScheduler
{
    //-------------------------------------------------------------------------
    // Public methods
    //                        

    /** Post a Task object to be schedule */
    public void post( Task task );

    /**
     * @return true if this scheduler is paused otherwise the scheduler 
     * is resumed or running 
     */
    public boolean isPaused();

    /** @return true if this scheduler is stopped */
    public boolean isStopped();

    /** Pauses the scheduler */
    public void pause();

    /** Resumes the scheduler if paused otherwise do nothing */
    public void resume();

    /**
     * Starts the scheduler (the task list is assume empty at this time)
     * If the scheduler already started then this call does nothing
     */
    public void start();

    /**
     * Stops the scheduler which finishes executing current task (if any)
     * and clears the task list.  If the scheduler is already stopped then
     * this call does nothing
     * NOTE: all task pending will be cleared from scheduler list
     */
    public void stop();
}
