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
 * A simple Exception class for StateMachine
 * @author E. Michael Maximilien
 */
public class StateMachineException extends RuntimeException
{
    //-------------------------------------------------------------------------
    // Ctor(s)
    //

	/** 
	 * 2-args ctor
	 * @param s the StateMachine.State object
	 * @param sm the StateMachine object
	 */
	public StateMachineException( StateMachine.State s, StateMachine sm ) { this( s, sm, "" ); }

	/** 
	 * 3-args ctor
	 * @param s the StateMachine.State object
	 * @param sm the StateMachine object
	 * @param msg the String message
	 */
	public StateMachineException( StateMachine.State s, StateMachine sm, String msg ) 
	{
		super( msg );
		state = s; 
		stateMachine = sm;
	}

    //-------------------------------------------------------------------------
    // Public methods
    //

	/** @return the State that caused this exception */
	public StateMachine.State getState() { return state; }

	/** @return the StateMachine that caused this exception */
	public StateMachine getStateMachine() { return stateMachine; }

    //-------------------------------------------------------------------------
    // Instance variables
    //

	private StateMachine.State state = null;
	private StateMachine stateMachine = null;
}
