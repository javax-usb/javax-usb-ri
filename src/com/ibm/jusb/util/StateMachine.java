package com.ibm.jusb.util;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the IBM Public License.
 *
 */

import java.util.Enumeration;

/**
 * Defines an interface for a StateMachine
 * Useful for implementing the State pattern
 * @author E. Michael Maximilien
 */
public interface StateMachine
{
    //-------------------------------------------------------------------------
    // Public methods
    //

	/** @return the intitial state */
	public StateMachine.State getInitState();

	/** @return the current state of this SM */
	public StateMachine.State getCurrentState();

	/**
	 * Transition to the state specified if a valid transition exists
	 * @throws com.ibm.jusb.util.StateMachineException if the trasition is invalid
	 */
	public void transition( StateMachine.State nextState ) throws StateMachineException;

	/** 
	 * Adds a StateMachine.State in this StateMachine
	 * @param state the StateMachine.State to add
	 */
	public void addState( StateMachine.State state );

	/** 
	 * Adds a transition for the states specified
	 * @param origState the current (original) state
	 * @param nextState the next state
	 */
	public void addTransition( StateMachine.State origState, StateMachine.State nextState );

	//-------------------------------------------------------------------------
	// Package methods
	//

	/** @return the DiGraph used to implement this StateMachine */
	DiGraph getDiGraph();

	//-------------------------------------------------------------------------
	// Public inner interfaces
	//

	/**
	 * Defines an interface for a State
     * Useful for implementing the State pattern
     * @author E. Michael Maximilien
     */
	public interface State extends Graph.Vertex
	{
		//-------------------------------------------------------------------------
		// Public methods
		//

		/** @return the unique name of that state for a StateMachine */
		public String getName();

		/** @return the StateMachine that this state is a part of */
		public StateMachine getStateMachine();

		/** @return an Enumeration of State objects that can become the next state after this one */
		public Enumeration getNextStates();

		//-------------------------------------------------------------------------
		// Package methods
		//

		/** 
		 * Sets the StateMachine of this State object
		 * @param sm the StateMachine associated with this state
		 */
		void setStateMachine( StateMachine sm );
	}

	/**
	 * Defines an interface for a StateTransition
     * Useful for implementing the State pattern
	 * @author E. Michael Maximilien
	 */
	public interface StateTransition extends Graph.Edge
	{
		//-------------------------------------------------------------------------
		// Public methods
		//

		/** @return the source state */
		public StateMachine.State getSourceState();

		/** @return the next state */
		public StateMachine.State getNextState();
	}
}
