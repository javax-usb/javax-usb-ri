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
 * Abstract implementation of the TaskScheduler (assumes the Scheduler automatically 
 * starts after its constructed)
 * @author E. Michael Maximilien
 */
public abstract class AbstractTaskScheduler extends Object implements TaskScheduler
{
    //-------------------------------------------------------------------------
    // Public ctor(s)
    //               

    /** Increments the taskSchedulerCount */
    public AbstractTaskScheduler() { taskSchedulerCount++; }

    //-------------------------------------------------------------------------
    // Public abstract methods
    //                

    /** Post a Task object to be schedule */
    public abstract void post( Task task );

    //-------------------------------------------------------------------------
    // Public overridden methods
    //

    /** @return a String representation of this TaskScheduler */
    public String toString()
    {
        StringBuffer sb = new StringBuffer();

        sb.append( "<TaskScheduler name = " + getName() +">\n" );
        sb.append( "    <state started = " + started + 
                   " paused = " + paused + "/>\n" );
        sb.append( getOtherData() );
        sb.append( "</TaskScheduler>\n" );

        return sb.toString();
    }

    //-------------------------------------------------------------------------
    // Public methods
    //

    /**
     * @return true if this scheduler is paused otherwise the scheduler 
     * is resumed or running 
     */
    public boolean isPaused() { return paused; }

    /** @return true if this scheduler is stopped */
    public boolean isStopped() { return ( started == false ); }

    /** Pauses the scheduler */
    public void pause()
    {
        if( paused ) return;

        paused = true;
    }

    /** Resumes the scheduler if paused otherwise do nothing */
    public void resume()
    {
        if( paused == false ) return;

        paused = false;
        
        synchronized( resumeLock ) { resumeLock.notifyAll(); }
    }

    /**
     * Starts the scheduler (the task list is assume empty at this time)
     * If the scheduler already started then this call does nothing
     */
    public void start()
    {
        if( started ) return;

        started = true;
        stopped = false;
        paused = false;
        
        synchronized( startLock ) { startLock.notifyAll(); }
    }

    /**
     * Stops the scheduler which finishes executing current task (if any)
     * and clears the task list.  If the scheduler is already stopped then
     * this call does nothing
     * NOTE: all task pending will be cleared from scheduler list
     */
    public void stop()
    {
        if( stopped ) return;

        stopped = true;
        started = false;
        paused = false;
        
        synchronized( stopLock ) { stopLock.notifyAll(); }
    }

    //-------------------------------------------------------------------------
    // Protected methods
    //

    /** @return the name of this TaskScheduler for debugging purposes */
    protected String getName() { return DEFAULT_TASK_SCHEDULER_NAME + taskSchedulerCount; }

    /** @return any other data String to put in the toString method*/
    protected String getOtherData() { return ""; }

    //-------------------------------------------------------------------------
    // Instance variable
    //

    protected boolean started = false;
    protected boolean stopped = true;
    protected boolean paused = false;

    protected Object startLock = new Object();
    protected Object stopLock = new Object();

    protected Object resumeLock = new Object();

    //-------------------------------------------------------------------------
    // Class variables
    //

    protected static int taskSchedulerCount = 0;

    //-------------------------------------------------------------------------
    // Class constants
    //

    public static final String DEFAULT_TASK_SCHEDULER_NAME = "TaskScheduler";
}
