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
 * A JUnit TestCase for the StateMachineTestCase interface and implementing classes
 * @author E. Michael Maximilien
 */
public class StateMachineTestCase extends TestCase
{
	//-------------------------------------------------------------------------
	// Ctor(s)
	//

	public StateMachineTestCase( String name ) { super( name ); }

	//-------------------------------------------------------------------------
	// Protected overridden methods
	//

	protected void setUp() 
	{  
		sm = null;
		s1 = factory.createState( "s1" );
		s2 = factory.createState( "s2" );
		s3 = factory.createState( "s3" );
	}

	protected void tearDown() 
	{
		sm = null;
		s1 = null;
		s2 = null;
		s3 = null;
	}

	//-------------------------------------------------------------------------
	// Private methods
	//

	private void verifyValidSMTransition( StateMachine stateMachine, StateMachine.State currentState, StateMachine.State nextValidState )
	{
		try
		{ 	
			assertTrue( "StateMachine currentState == " + currentState, stateMachine.getCurrentState().equals( currentState ) );

			stateMachine.transition( nextValidState );

			assertTrue( "StateMachine can transition " + currentState + " -> " + nextValidState, true );
			assertTrue( "StateMachine currentState == " + stateMachine.getCurrentState(), stateMachine.getCurrentState().equals( nextValidState ) );
		}
		catch( StateMachineException e )
		{ assertTrue( "StateMachine should be able to transition " + currentState + " -> " + nextValidState + " but got exception with message = " + e.getMessage(), false ); }
	}

	private void verifyInvalidSMTransition( StateMachine stateMachine, StateMachine.State currentState, StateMachine.State nextInvalidState )
	{
		try
		{ 
			assertTrue( "StateMachine currentState == " + currentState, stateMachine.getCurrentState().equals( currentState ) );

			stateMachine.transition( nextInvalidState );

			assertTrue( "StateMachine can transition " + currentState + " -> " + nextInvalidState + " this is not expected", false );
		}
		catch( StateMachineException e )
		{
			assertTrue( "StateMachine currentState == " + stateMachine.getCurrentState(), stateMachine.getCurrentState().equals( currentState ) );
			assertTrue( "StateMachine could not (as expected) transition " + currentState + " -> " + nextInvalidState, true ); 
		}
	}

	//-------------------------------------------------------------------------
	// Public testXyz() methods
	//

	/** Test the creation of SM from the SMF */
	public void testStateMachineFactory()
	{
		assertTrue( true );
	}

	/**
	 * Creates a 1 state SM with transitions as follows:
	 *   States      Transitions
	 *     S1       S1->S1
	 * And verifies various aspects...
	 */
	public void testStateMachine1()
	{
		sm = factory.createStateMachine( s1 );
		sm.addTransition( s1, s1 );

		assertTrue( "StateMachine initState == s1", sm.getInitState().equals( s1 ) );
		assertTrue( "StateMachine currentStat == s1", sm.getCurrentState().equals( s1 ) );

		try
		{ 
			sm.transition( s1 );
			assertTrue( "StateMachine can transition s1 -> s1", true );
		}
		catch( StateMachineException e )
		{ assertTrue( "StateMachine should be able to transition s1 -> s1 but got exception with message = " + e.getMessage(), false ); }
	}

	/**
	 * Creates a 2 state SM with transitions as follows:
	 *   States      Transitions
	 *     S1       S1->S1, S1->S2
	 *     S2       S2->S2, S2->S1
	 * And verifies various aspects...
	 */
	public void testStateMachine2()
	{
		sm = factory.createStateMachine( s1 );
		sm.addState( s2 );
		sm.addTransition( s1, s1 );
		sm.addTransition( s1, s2 );
		sm.addTransition( s2, s2 );
		sm.addTransition( s2, s1 );

		assertTrue( "StateMachine initState == s1", sm.getInitState().equals( s1 ) );
		assertTrue( "StateMachine currentState == s1", sm.getCurrentState().equals( s1 ) );

		verifyValidSMTransition( sm, s1, s1 );
		verifyValidSMTransition( sm, s1, s2 );
		verifyValidSMTransition( sm, s2, s2 );
		verifyValidSMTransition( sm, s2, s1 );
	}

	/** 
	 * Creates a 3 states SM with transitions as follow:
	 *   States      Transitions
	 *     S1       S1->S1, S1->S2
	 *     S2       S2->S3
	 *     S3       S3->S3, S3->S2, S3->S1
	 * And verifies various aspects...
	 */
	public void testStateMachine3()
	{
		sm = factory.createStateMachine( s1 );
		sm.addState( s2 );
		sm.addState( s3 );
		sm.addTransition( s1, s1 );
		sm.addTransition( s1, s2 );
		sm.addTransition( s2, s3 );
		sm.addTransition( s3, s3 );
		sm.addTransition( s3, s2 );
		sm.addTransition( s3, s1 );

		assertTrue( "StateMachine initState == s1", sm.getInitState().equals( s1 ) );
		assertTrue( "StateMachine currentState == s1", sm.getCurrentState().equals( s1 ) );

		verifyValidSMTransition( sm, s1, s1 );
		verifyValidSMTransition( sm, s1, s2 );

		verifyInvalidSMTransition( sm, s2, s1 );

		verifyValidSMTransition( sm, s2, s3 );

		verifyValidSMTransition( sm, s3, s3 );
		verifyValidSMTransition( sm, s3, s2 );

		verifyValidSMTransition( sm, s2, s3 );

		verifyValidSMTransition( sm, s3, s1 );

		verifyInvalidSMTransition( sm, s1, s3 );
	}


	//-------------------------------------------------------------------------
	// Instance variables
	//

	private StateMachineFactory factory = DefaultStateMachineFactory.getInstance();

	private StateMachine sm = null;
	private StateMachine.State s1 = null;
	private StateMachine.State s2 = null;
	private StateMachine.State s3 = null;
}
