package com.ibm.jusb.util;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import java.util.Vector;
import java.util.Enumeration;

/**
 * Defines a default implementation of the StateMachine interface
 * @author E. Michael Maximilien
 */
public class DefaultStateMachine extends Object implements StateMachine
{
    //-------------------------------------------------------------------------
    // Ctor
    //

    /**
     * Default 1-arg ctor
     * @param initS the intial state for this SM
     */
    public DefaultStateMachine( StateMachine.State initS )  
	{ 
		initState = initS;
		currentState = initState;
		diGraph.addVertex( initState );
	}

    /**
     * 2-args ctor
     * @param initS the intial State of this SM
     * @param states the StateMachine.State objects
     * @param stateTransitions the StateMachine.StateTransition objects
     */
    public DefaultStateMachine( StateMachine.State initS, StateMachine.State[] states,
						 StateMachine.StateTransition[] stateTransitions ) 
    {
		this( initS );
		initStates( states );
        initTransitions( stateTransitions );
    }

    //-------------------------------------------------------------------------
    // Private/Protected methods
    //

    /**
     * Initialize the StateMachine.State objects
     * @param states the StateMachine.StateTransition objects
     */
    protected void initStates( StateMachine.State[] states )
	{
		Vector vector = new Vector();

		for( int i = 0; i < states.length; ++i )
        {
			vector.addElement( states[ i ] );
			diGraph.setVertices( vector.elements() );
        }
    }

    /**
     * Initialize the StateTransition objects
     * @param stateTransitions the StateTransition objects
     */
    protected void initTransitions( StateMachine.StateTransition[] stateTransitions )
    {
		Vector vector = new Vector();

        for( int i = 0; i < stateTransitions.length; ++i )
        {
			vector.addElement( stateTransitions[ i ] );
			diGraph.setEdges( vector.elements() );
        }
    }

    //-------------------------------------------------------------------------
    // Public methods
    //

    /** @return the intitial state */
    public StateMachine.State getInitState() { return initState; }

	/** @return the current state of this SM */
	public StateMachine.State getCurrentState() { return currentState; }

    /**
     * Transition to the state specified if a valid transition exists
     * @throws com.ibm.jusb.util.StateMachineException if the trasition is invalid
     */
    public void transition( StateMachine.State nextState ) throws StateMachineException
    {
		if( diGraph.getAdjacentList( getCurrentState() ).containsVertex( nextState ) )
			currentState = nextState;
		else
			throw new StateMachineException( getCurrentState(), this, 
											 "Cannot transition from state = " + getCurrentState() + 
											 " to nextState = " + nextState );
    }

	/** 
	 * Adds a StateMachine.State in this StateMachine
	 * @param state the StateMachine.State to add
	 */
	public void addState( StateMachine.State state ) 
	{ 
		state.setStateMachine( this );

		diGraph.addVertex( state ); 
	}

    /** 
     * Adds a transition for the states specified
     * @param origState the current (original) state
     * @param nextState the next state
     */
    public void addTransition( StateMachine.State origState, StateMachine.State nextState )
    {
		origState.setStateMachine( this );
		nextState.setStateMachine( this );

		StateMachine.StateTransition transition = new DefaultStateMachine.DefaultStateTransition( origState, nextState );
		diGraph.addEdge( transition );
    }

    /** @return the StateTransition objects for this StateMachine */
    public Enumeration getTransitions() 
	{ 
		return diGraph.getAdjacentList( (Graph.Vertex)getCurrentState() ).getAdjVertices();
	}

	/** @return the DiGraph that is used to implement this StateMachine */
	public DiGraph getDiGraph() { return diGraph; }
    
    //-------------------------------------------------------------------------
    // Implementation of the StateMachine.State inner interface
    //

	/**
	 * Default implementation of the State interface
     * @author E. Michael Maximilien
     */
	public static class DefaultState extends Object implements StateMachine.State
	{
		//-------------------------------------------------------------------------
		// Ctor
		//

		/**
	     * Default 1-arg ctor
	     * @param s the unique String name
	     */
		public DefaultState( String s ) { name = s; }

		/**
	     * 2-arg ctor
	     * @param s the unique String name
		 * @param sm the StateMachine associated with this State
	     */
		public DefaultState( String s, StateMachine sm ) 
		{ 
			this( s );
			stateMachine = sm;
		}

		//-------------------------------------------------------------------------
		// Package methods
		//

		/** 
		 * Sets the StateMachine of this State object
		 * @param sm the StateMachine associated with this state
		 */
		public void setStateMachine( StateMachine sm ) { stateMachine = sm; }

		//-------------------------------------------------------------------------
		// Public methods
		//

		/** @return the unique name of that state for a StateMachine */
		public String getName() { return name; }

        /** @return the Object that this vertex constains */
        public Object getObject() { return this; }

		/** @return the StateMachine that this state is a part of */
		public StateMachine getStateMachine() { return stateMachine; }

		/** @return an Enumeration of State objects that can become the next state after this one */
		public Enumeration getNextStates() 
		{ 
			return getStateMachine().getDiGraph().getAdjacentList( this ).getAdjVertices();
		}

		//-------------------------------------------------------------------------
		// Instance variables
		//

		private String name = "";
		private StateMachine stateMachine = null;
	}

    //-------------------------------------------------------------------------
    // Implementation of the StateMachine.StateTransition inner interface
    //

	/**
	 * Default implementation of the StateTransition interface
     * Useful for implementing the State pattern
     * @author E. Michael Maximilien
	 */
	public static class DefaultStateTransition extends Object implements StateMachine.StateTransition
	{
		//-------------------------------------------------------------------------
		// Ctor
		//

		/**
	     * Default 2-args ctor
	     * @param source the source State
	     * @param next the next State
	     */
		public DefaultStateTransition( StateMachine.State source, StateMachine.State next )
		{
			sourceState = source;
			nextState = next;
		}

		//-------------------------------------------------------------------------
		// Public methods
		//

		/** @return the source state */
		public StateMachine.State getSourceState() { return sourceState; }

		/** @return the next state */
		public StateMachine.State getNextState() { return nextState; }

        /** @return the source Vertex that this edge constains */
        public Graph.Vertex getSourceVertex() { return (Graph.Vertex)getSourceState(); }

        /** @return the sink Vertex that this edge constains */
        public Graph.Vertex getSinkVertex() { return (Graph.Vertex)getNextState(); }

		//-------------------------------------------------------------------------
		// Instance variables
		//

		private StateMachine.State sourceState = null;
		private StateMachine.State nextState = null;
	}

    //-------------------------------------------------------------------------
    // Instance variables
    //

    private StateMachine.State initState = null;
	private StateMachine.State currentState = null;
	private DiGraph diGraph = new DefaultDiGraph();
}
