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
 * Defines default implementation of the StateMachineFactory
 * Singleton class
 * @author E. Michael Maximilien
 */
public class DefaultStateMachineFactory extends Object implements StateMachineFactory
{
    //-------------------------------------------------------------------------
    // Ctor(s)
    //

	/** Make ctor protected to prevent construction */
	protected DefaultStateMachineFactory() {}

    //-------------------------------------------------------------------------
    // Public class methods
    //

	/** @return the unique instance of this class */
	public static StateMachineFactory getInstance()
	{
		if( instance == null )
			instance = new DefaultStateMachineFactory();

		return instance;
	}

    //-------------------------------------------------------------------------
    // Public methods
    //

	/** 
	 * @return a new StateMachine
	 * @param initState the intial StateMachine.State for the SM
	 */
	public StateMachine createStateMachine( StateMachine.State initState )
	{
		return new DefaultStateMachine( initState );
	}

	/** 
	 * @return a new StateMachine
	 * @param initState the intial State for the SM
	 * @param states an array of StateMachine.State objects
	 * @param transitions an array of StateMachine.StateTransition objects
	 */
	public StateMachine createStateMachine( StateMachine.State initState, 
											StateMachine.State[] states,
											StateMachine.StateTransition[] transitions )
	{
		return new DefaultStateMachine( initState, states, transitions );
	}

	/** 
	 * @return a new StateMachine.State object
	 * @param name the State name
	 */
	public StateMachine.State createState( String name )
	{
		return new DefaultStateMachine.DefaultState( name );
	}

	/** 
	 * @return a new StateMachine.StateTransition object
	 * @param the origState StateMachine.State object
	 * @param the nextState StateMachine.State object
	 */
	public StateMachine.StateTransition createStateTransition( StateMachine.State origState, StateMachine.State nextState )
	{
		return new DefaultStateMachine.DefaultStateTransition( origState, nextState );
	}

	//-------------------------------------------------------------------------
	// Class constants
	//

	private static StateMachineFactory instance = null; 
}
