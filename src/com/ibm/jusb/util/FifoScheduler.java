package com.ibm.jusb.util;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

/**
 * A simple Task scheduler implementing a FIFO policy (i.e. queue)
 * @author E. Michael Maximilien
 */
public class FifoScheduler extends AbstractTaskScheduler
{
    //-------------------------------------------------------------------------
    // Ctor(s)
    //

    /** Default ctor */
    public FifoScheduler() { start(); }

    //-------------------------------------------------------------------------
    // Public methods
    //

    /** @return the last Exception caused by executing a Task */
    public Exception getLastTaskException() { return lastTaskException; }

    /** @return the last Task causing an Exception  */
    public Task getLastTaskCausingException() { return lastTaskCausingException; }

    //-------------------------------------------------------------------------
    // Public overridden methods
    //

    /** Post a Task object to be schedule */
    public void post( Task task )
    {
        if( isStopped() )
            throw new RuntimeException( "Cannot post( Task ) to a stopped TaskScheduler" );

        queue.enqueue( task );

        synchronized( postLock ) { postLock.notifyAll(); }
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
        queue = new DefaultQueue();
        activeObject = this.new ActiveObject();

        synchronized( startLock ) { startLock.notifyAll(); }
    }

    //-------------------------------------------------------------------------
    // Protected methods
    //

    /** @return the name of this TaskScheduler for debugging purposes */
    protected String getName() { return FIFO_TASKSCHEDULER_NAME + taskSchedulerCount; }

    /** @return any other data String to put in the toString method*/
    protected String getOtherData() 
    {
        StringBuffer sb = new StringBuffer();

        sb.append( "    <queue.isEmpty()      = " + queue.isEmpty() + "/>\n" );
        sb.append( "    <lastTaskException.message = " + ( lastTaskException == null ? "none" : lastTaskException.getMessage() ) + "/>\n" );

        return sb.toString();
    }

    //-------------------------------------------------------------------------
    // Private methods
    //

    /**
     * Called by the FifoScheduler.ActiveObject.run method.  Executed by the 
     * FifoScheduler.ActiveObject Thread object
     */
    private void runActiveObject()
    {
        while( true )
        {
            //If we are not yet stated then wait
            while( started == false )
                synchronized( startLock )
                {
                    try{ startLock.wait(); }
                    catch( InterruptedException ie ) {}
                }

            //Check if queue is empty and wait otherwise execute tasks
            while( queue.isEmpty() )
                synchronized( postLock )
                {
                    try{ postLock.wait(); }
                    catch( InterruptedException ie ) {}
                }

            //Wait until we are no longer paused
            while( paused )
                synchronized( resumeLock )
                {
                    try{ resumeLock.wait(); }
                    catch( InterruptedException ie ) {}
                }

            executeTask( (Task)queue.dequeue() );

            //Check to see if we are stopped
            if( stopped == true )
            {
                started = false;
                return;
            }
        }
    }

    /**
     * Executes the Task argument
     * @param task the Task object
     */
    private void executeTask( Task task )
    {
        try
        {
            task.execute();
        }
        catch( Exception e )
        {
            lastTaskException = e;
            lastTaskCausingException = task;
        }
    }

    //-------------------------------------------------------------------------
    // Inner classes
    //

    /**
     * Active object that contains the Thread and implements the Runnable interface
     * @author E. Michael Maximilien
     */
    class ActiveObject extends Object implements Runnable
    {
        //---------------------------------------------------------------------
        // Ctor(s)
        //

        public ActiveObject() 
        {
            activeObjectCount++;
            thread = new Thread( this );
            thread.setName( FIFO_TASKSCHEDULER_NAME + "." + ACTIVE_OBJECT + " #" + activeObjectCount );
            thread.start();
        }

        //---------------------------------------------------------------------
        // Public methods
        //

        /** Called when the Thread is started */
        public void run() { FifoScheduler.this.runActiveObject(); }

        //---------------------------------------------------------------------
        // Instance variables
        //

        private Thread thread = null;
    }

    //-------------------------------------------------------------------------
    // Instance variable
    //

    private Exception lastTaskException = null;
    private Task lastTaskCausingException = null;

    private Object postLock = new Object();

    private Queue queue = null;
    private ActiveObject activeObject = null;

    //-------------------------------------------------------------------------
    // Class variables
    //

    static int activeObjectCount = 0;

    //-------------------------------------------------------------------------
    // Class constants
    //
    
    public static final String ACTIVE_OBJECT = "ActiveObject"; 
    public static final String FIFO_TASKSCHEDULER_NAME = "FifoScheduler";
}
