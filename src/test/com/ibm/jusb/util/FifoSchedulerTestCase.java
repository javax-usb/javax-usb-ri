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
 * A JUnit TestCase for the FifoScheduler interface and implementing class
 * @author E. Michael Maximilien
 */
public class FifoSchedulerTestCase extends TestCase
{
	//-------------------------------------------------------------------------
	// Ctor(s)
	//

	public FifoSchedulerTestCase( String name ) { super( name ); }

	//-------------------------------------------------------------------------
	// Protected overridden methods
	//

	protected void setUp() 
    {
        scheduler = new FifoScheduler();
        
        task0 = this.new PrintTask( "PrintTask", 0 );
        task1 = this.new PrintTask( "PrintTask", 1 );
        task2 = this.new PrintTask( "PrintTask", 2 );
        task3 = this.new PrintTask( "PrintTask", 3 );
        task4 = this.new PrintTask( "PrintTask", 4 );
        sentinelTask = this.new SentinelTask();

        lastExecutedTaskNumber = -1;
        exeTaskList = new ArrayList();
        sentinelLock = new Object();
    }

	protected void tearDown() 
    { 
        scheduler = null;
        
        task0 = null;
        task1 = null;
        task2 = null;
        task3 = null;
        task4 = null;
        sentinelTask = null;

        lastExecutedTaskNumber = -1;
        exeTaskList = null;;
        sentinelLock = null;
    }

	//-------------------------------------------------------------------------
	// Private methods
	//

    private void sleep( long l ) 
    { 
        try{ Thread.currentThread().sleep( l ); }
        catch( InterruptedException e ) {}
    }

	//-------------------------------------------------------------------------
	// Public testXyz() methods
	//

	/** Verifies that the scheduler semantics work w/o any tasks (i.e. start, stop, ...) */
	public void testFifoScheduler1()
	{
        //New scheduler (default settings)
        assertTrue( "Newly created FifoScheduler is NOT stopped (or started) by default", scheduler.isStopped() == false );
        assertTrue( "Newly created FifoScheduler is NOT paused by default", scheduler.isPaused() == false );

        //Starting a alreaded started scheduler should have no impact
        scheduler.start(); sleep( 100 );
        assertTrue( "Newly created FifoScheduler is NOT stopped (or started) by default", scheduler.isStopped() == false );
        assertTrue( "Newly created FifoScheduler is NOT paused by default", scheduler.isPaused() == false );

        //Stop
        scheduler.stop(); sleep( 100 );
        assertTrue( "Expect scheduler to be stopped...", scheduler.isStopped() == true );
        assertTrue( "Expect scheduler to NOT be paused by default", scheduler.isPaused() == false );

        //Start
        scheduler.start(); sleep( 100 );
        assertTrue( "Expect scheduler to be started...", scheduler.isStopped() == false );
        assertTrue( "Expect scheduler to NOT be paused", scheduler.isPaused() == false );

        //Pause
        scheduler.pause(); sleep( 100 );
        assertTrue( "Expect scheduler to be started...", scheduler.isStopped() == false );
        assertTrue( "Expect scheduler to be paused", scheduler.isPaused() == true );

        //Resume
        scheduler.resume(); sleep( 100 );
        assertTrue( "Expect scheduler to be started...", scheduler.isStopped() == false );
        assertTrue( "Expect scheduler to be resumed", scheduler.isPaused() == false );

        //Stop
        scheduler.stop(); sleep( 100 );
        assertTrue( "Expect scheduler to be stopped...", scheduler.isStopped() == true );
        assertTrue( "Expect scheduler to NOT be paused by default", scheduler.isPaused() == false );

        //Start
        scheduler.start(); sleep( 100 );
        assertTrue( "Expect scheduler to be started...", scheduler.isStopped() == false );
        assertTrue( "Expect scheduler to NOT be paused", scheduler.isPaused() == false );
	}

	/** Add a bunch of PrintTask in the scheduler and ask user in what order they printed */
	public void testFifoScheduler2()
	{
        //Pause the scheduler and post all the tasks (0 .. 4)  and wait on sentinel
        scheduler.pause();
        assertTrue( "Expect scheduler to be paused", scheduler.isPaused() == true );

        List expectedExeList = new ArrayList();

        scheduler.post( task0 ); expectedExeList.add( task0 );
        scheduler.post( task1 ); expectedExeList.add( task1 );
        scheduler.post( task2 ); expectedExeList.add( task2 );
        scheduler.post( task3 ); expectedExeList.add( task3 );
        scheduler.post( task4 ); expectedExeList.add( task4 );

        scheduler.post( sentinelTask );

        scheduler.resume();

        synchronized( sentinelLock )
        {
            assertTrue( "Expect scheduler should NOT be paused", scheduler.isPaused() == false );

            try{ sentinelLock.wait(); }
            catch( InterruptedException e ) {}
        }

        //At this point all task should have executed (sentinel also)
        assertTrue( "Expect scheduler should be started", scheduler.isStopped() == false );
        assertTrue( "Expect scheduler should NOT be paused", scheduler.isPaused() == false );

        assertTrue( "Expect lastExecutedTaskNumber to be 4", lastExecutedTaskNumber == 4 );
        assertTrue( "Expect executed task list and expected executed task list to be the same",
                expectedExeList.equals( exeTaskList ) );
	}

	/** Add a bunch of PrintTask in the scheduler and ask user in what order they printed */
	public void testFifoScheduler3()
	{
        //Pause the scheduler and post all the tasks (0 .. 2)  and wait on sentinel
        scheduler.pause();
        assertTrue( "Expect scheduler to be paused", scheduler.isPaused() == true );

        List expectedExeList = new ArrayList();

        scheduler.post( task2 ); expectedExeList.add( task2 );
        scheduler.post( task0 ); expectedExeList.add( task0 );
        scheduler.post( task1 ); expectedExeList.add( task1 );

        scheduler.post( sentinelTask );

        scheduler.resume();

        synchronized( sentinelLock )
        {
            assertTrue( "Expect scheduler should NOT be paused", scheduler.isPaused() == false );

            try{ sentinelLock.wait(); }
            catch( InterruptedException e ) {}

            scheduler.stop();
            assertTrue( "Expect scheduler to be stopped", scheduler.isStopped() == true );
        }

        //At this point all task should have executed (sentinel also)
        assertTrue( "Expect scheduler should be stopped", scheduler.isStopped() );

        assertTrue( "Expect lastExecutedTaskNumber to be 1", lastExecutedTaskNumber == 1 );
        assertTrue( "Expect executed task list and expected executed task list to be the same",
                expectedExeList.equals( exeTaskList ) );

        //Add task 3 and 4 and resume
        scheduler.start();
        scheduler.pause();
        assertTrue( "Expect scheduler should be started", scheduler.isStopped() == false );
        assertTrue( "Expect scheduler to be paused", scheduler.isPaused() == true );
        
        expectedExeList.clear();

        scheduler.post( task4 ); expectedExeList.add( task4 );
        scheduler.post( task3 ); expectedExeList.add( task3 );

        scheduler.post( sentinelTask );

        scheduler.resume();

        synchronized( sentinelLock )
        {
			assertTrue( "Expect scheduler should NOT be paused", scheduler.isPaused() == false );

            try{ sentinelLock.wait(); }
            catch( InterruptedException e ) {}
        }
	}

	//-------------------------------------------------------------------------
	// Instance variables
	//

	private TaskScheduler scheduler = null;
    private Task task0 = null;
    private Task task1 = null;
    private Task task2 = null;
    private Task task3 = null;
    private Task task4 = null;
    private Task sentinelTask = null;

    private int lastExecutedTaskNumber = -1;
    private List exeTaskList = null;
    private Object sentinelLock = null;

	//-------------------------------------------------------------------------
	// Inner class
	//

    protected class PrintTask extends Object implements Task
    {
        public PrintTask( String msg, int i ) 
        { this.msg = msg; number = i; }

        public void execute() 
        { 
            System.out.println( msg + " : executed task #" + number );
            lastExecutedTaskNumber = number;
            exeTaskList.add( this );
        }

        private String msg = "";
        private int number = 0;
    }

    protected class SentinelTask extends Object implements Task
    {
        public SentinelTask() {}

        public void execute() 
        { 
            synchronized( sentinelLock ) { sentinelLock.notifyAll(); }
            System.out.println( "Executed sentinel task..." ); 
        }
    }
}
