package com.ibm.jusb;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import javax.usb.*;
import javax.usb.event.*;
import javax.usb.util.*;

import com.ibm.jusb.util.*;

/**
 * Defines abstraction for the UsbPipe bridge pattern.
 * <p>
 * Note that this will take no action on any UsbIrps passed through it,
 * except for reference counting the total number in order to maintain the correct
 * state.
 * @author E. Michael Maximilien
 * @author Dan Streetman
 * @version 0.0.1 (JDK 1.1.x)
 */
public class UsbPipeAbstraction extends Object implements UsbPipe
{
    //-------------------------------------------------------------------------
    // Ctor
    //

    /**
     * Ctor
	 * NOTE: by defaults the UsbPipeEventHelper is created and passed a FifoSchduler
	 * use other ctor to implement other kind of event delivering policy...
	 * @param endpoint this pipe's associated UsbEndpoint.
     */
    UsbPipeAbstraction( UsbEndpoint endpoint ) 
	{ this( endpoint, new FifoScheduler() ); }

    /**
     * Ctor
	 * @param endpoint this pipe's associated UsbEndpoint
	 * @param taskSchduler the TaskSchduler to use for event delivering
     */
    UsbPipeAbstraction( UsbEndpoint endpoint, TaskScheduler taskScheduler )
    { 
		usbPipeEventHelper = new UsbPipeEventHelper( this, taskScheduler );
		this.usbEndpoint = endpoint;

		initStateMachine();

		if (getUsbEndpoint().getUsbInterface().isActive())
			setActive( true );
    }

    //-------------------------------------------------------------------------
    // Package methods
    //

	/** @param the UsbPipeImp to use */
	void setUsbPipeImp( UsbPipeImp pipe ) { usbPipeImp = pipe; }

    /** @return the UsbPipeImp object */
    UsbPipeImp getUsbPipeImp() { return usbPipeImp; }

    //-------------------------------------------------------------------------
    // Protected methods
    //

	/**
	 * Initialize this UsbPipe StateMachine with following transitions:
	 *   	States                  						Transition to
	 *     INACTIVE_STATE       INACTIVE_STATE, CLOSED_STATE
	 *     CLOSED_STATE       	CLOSED_STATE, IDLE_STATE, INACTIVE_STATE
	 *     IDLE_STATE        	IDLE_STATE, BUSY_STATE, ERROR_STATE, CLOSED_STATE
	 *     BUSY_STATE        	BUSY_STATE, IDLE_STATE, ERROR_STATE
	 *     ERROR_STATE       	ERROR_STATE, IDLE_STATE
	 */
	protected void initStateMachine()
	{
		getStateMachine().addState( IDLE_STATE );
		getStateMachine().addState( BUSY_STATE );
		getStateMachine().addState( ERROR_STATE );
		getStateMachine().addState( CLOSED_STATE );

		getStateMachine().addTransition( INACTIVE_STATE, INACTIVE_STATE );
		getStateMachine().addTransition( INACTIVE_STATE, CLOSED_STATE );

		getStateMachine().addTransition( IDLE_STATE, IDLE_STATE );
		getStateMachine().addTransition( IDLE_STATE, BUSY_STATE );
		getStateMachine().addTransition( IDLE_STATE, ERROR_STATE );
		getStateMachine().addTransition( IDLE_STATE, CLOSED_STATE );

		getStateMachine().addTransition( BUSY_STATE, BUSY_STATE );
		getStateMachine().addTransition( BUSY_STATE, IDLE_STATE );
		getStateMachine().addTransition( BUSY_STATE, ERROR_STATE );

		getStateMachine().addTransition( ERROR_STATE, ERROR_STATE );
		getStateMachine().addTransition( ERROR_STATE, IDLE_STATE );

		getStateMachine().addTransition( CLOSED_STATE, CLOSED_STATE );
		getStateMachine().addTransition( CLOSED_STATE, IDLE_STATE );
		getStateMachine().addTransition( CLOSED_STATE, INACTIVE_STATE );
	}

	/** @return the UsbPipeStateMachine for this UsbPipe */
	protected UsbPipeStateMachine getStateMachine() { return stateMachine; }

	/** Get a uniquely-numbered SubmitResult */
	protected UsbIrpImp createSubmitResult()
	{
		UsbIrpImp irp = (UsbIrpImp)AbstractUsbServices.getInstance().getHelper().getUsbIrpImpFactory().take();

		irp.setNumber( ++submitResultCount );

		return irp;
	}

// FIXME
/* Change submission counting into busy-state depth counting done by 'smart'
   transition Object, for increasing and decreasing submission count */

	/** Increment the number of submissions in progress */
	protected void incrementSubmissionCount()
	{
		synchronized ( submissionCountLock ) {
			if (0 < submissionCount++)
				return;

			synchronized ( getStateMachine() ) {
				transition( UsbPipeBusyState.NAME );
			}
		}
	}

	/** Decrement the number of submissions in progress */
	protected void decrementSubmissionCount()
	{
		synchronized ( submissionCountLock ) {
			if (0 < --submissionCount)
				return;

			synchronized ( getStateMachine() ) {
				try { transition( UsbPipeIdleState.NAME ); }
				catch ( StateMachineException smE ) {
					/* Ignore illegal transitions, which leave us in the original state - probably error state */
				}
			}
		}
	}		

    //-------------------------------------------------------------------------
    // Public methods
    //

	/** @return if this UsbPipe is active */
	public boolean isActive() { return getState().isActive(); }

	/** @return if this UsbPipe is open */
	public boolean isOpen() { return getState().isOpen(); }

	/** @return if this UsbPipe is open */
	public boolean isClosed() { return getState().isClosed(); }

	/** @return if this UsbPipe is busy */
	public boolean isBusy() { return getState().isBusy(); }

	/** @return if this UsbPipe is busy */
	public boolean isIdle() { return getState().isIdle(); }

	/** @return if this UsbPipe is in an error state */
	public boolean isInError() { return getState().isInError(); }

	/** @return the error code indicating the cause of the current error state */
	public int getErrorCode() { return getState().getErrorCode(); }

	/**
	 * Return the current sequence number.
	 * <p>
	 * The platform implementation is responsible for keeping track of the
	 * current sequence number; this allows it to only assign sequence numbers
	 * to submissions that are successfully submitted.
	 * @return the current sequence number of this UsbPipe
	 */
	public long getSequenceNumber() { return getUsbPipeImp().getSequenceNumber(); }

	/** @return the UsbDevice associated with this pipe */
	public UsbDevice getUsbDevice() { return getUsbEndpoint().getUsbDevice(); }

	/** @return the max packet size of this pipe's endpoint */
	public short getMaxPacketSize() { return getUsbEndpoint().getMaxPacketSize(); }

	/** @return the address of this pipe's endpoint */
	public byte getEndpointAddress() { return getUsbEndpoint().getEndpointAddress(); }

	/** @return this pipe's type */
	public byte getType() { return getUsbEndpoint().getType(); }

	/** @return the UsbEndpoint associated with this UsbNormalPipe */
	public UsbEndpoint getUsbEndpoint() { return usbEndpoint; }

    /** @return the current state of this UsbPipe */
    public UsbPipeState getState() { return getStateMachine().getUsbPipeState(); }

	/**
	 * Opens this UsbPipe so its ready for submissions.
	 * <p>
	 * This method gets the current State in a syncrhonized manner,
	 * but releases synchronization before actually calling the State's
	 * approprite method.
	 */
	public void open() throws UsbException { getState().open(); }

	/**
	 * Closes this UsbPipe.
	 * <p>
	 * This method gets the current State in a syncrhonized manner,
	 * but releases synchronization before actually calling the State's
	 * approprite method.
	 */
	public void close() throws UsbException { getState().close(); }

	/**
	 * Synchonously submits this byte[] array to the UsbPipe.
	 * <p>
	 * This method gets the current State in a syncrhonized manner,
	 * but releases synchronization before actually calling the State's
	 * approprite method.  The current State may change before the
	 * synchronization is released, depending on what the current state is.
	 */
	public int syncSubmit( byte[] data ) throws UsbException
	{ return getStateMachine().getUsbPipeStateForSubmission().syncSubmit( data ); }

	/**
	 * Asynchonously submits this byte[] array to the UsbPipe.
	 * <p>
	 * This method gets the current State in a syncrhonized manner,
	 * but releases synchronization before actually calling the State's
	 * approprite method.  The current State may change before the
	 * synchronization is released, depending on what the current state is.
	 */
	public UsbPipe.SubmitResult asyncSubmit( byte[] data ) throws UsbException
	{ return getStateMachine().getUsbPipeStateForSubmission().asyncSubmit( data ); }

	/**
	 * Synchronous submission using a UsbIrp.
	 * <p>
	 * This method gets the current State in a syncrhonized manner,
	 * but releases synchronization before actually calling the State's
	 * approprite method.  The current State may change before the
	 * synchronization is released, depending on what the current state is.
	 */
    public void syncSubmit( UsbIrp irp ) throws UsbException
	{ getStateMachine().getUsbPipeStateForSubmission().syncSubmit( irp ); }

	/**
	 * Asynchronous submission using a UsbIrp.
	 * <p>
	 * This method gets the current State in a syncrhonized manner,
	 * but releases synchronization before actually calling the State's
	 * approprite method.  The current State may change before the
	 * synchronization is released, depending on what the current state is.
	 */
    public void asyncSubmit( UsbIrp irp ) throws UsbException
	{ getStateMachine().getUsbPipeStateForSubmission().asyncSubmit( irp ); }

	/**
	 * Stop all submissions in progress.
	 * <p>
	 * This method gets the current State in a syncrhonized manner,
	 * but releases synchronization before actually calling the State's
	 * approprite method.
	 */
	public void abortAllSubmissions() { getState().abortAllSubmissions(); }

	/**
	 * Register's the listener object for UsbPipeEvent
	 * @param listener the UsbPipeListener instance
	 */
	public void addUsbPipeListener( UsbPipeListener listener ) { usbPipeEventHelper.addUsbPipeListener( listener ); }

	/**
	 * Removes the listener object from the listener list
	 * @param listener the UsbPipeListener instance
	 */
	public void removeUsbPipeListener( UsbPipeListener listener ) { usbPipeEventHelper.removeUsbPipeListener( listener ); }

    //-------------------------------------------------------------------------
    // Public methods (not part of UsbPipe interface)
    //

	/**
	 * Get this pipe's event helper.
	 * <p>
	 * This can be used to fire events to listeners (in a seperate Thread).
	 * <p>
	 * Note that it is the platform implementation's responsibility to fire all events.
	 * @returns a UsbPipeEventHelper object.
	 */
	public UsbPipeEventHelper getUsbPipeEventHelper() { return usbPipeEventHelper; }

	/**
	 * Transitions the UsbPipe state to the state specified
	 * @param nextStateName the String name of the next state one of:
	 * @see com.ibm.jusb.UsbPipeClosedState#NAME
	 * @see com.ibm.jusb.UsbPipeIdleState#NAME
	 * @see com.ibm.jusb.UsbPipeBusyState#NAME
	 * @see com.ibm.jusb.UsbPipeErrorState#NAME
	 * @exception javax.usb.UsbRuntimeException if the transition is invalid
	 */
	public void transition( String nextStateName )
	{
		if( UsbPipeInactiveState.NAME.equals( nextStateName ) )
			getStateMachine().transition( INACTIVE_STATE );
		else
		if( UsbPipeClosedState.NAME.equals( nextStateName ) )
			getStateMachine().transition( CLOSED_STATE );
		else
		if( UsbPipeIdleState.NAME.equals( nextStateName ) )
			getStateMachine().transition( IDLE_STATE );
		else
		if( UsbPipeBusyState.NAME.equals( nextStateName ) )
			getStateMachine().transition( BUSY_STATE );
		else
		if( UsbPipeErrorState.NAME.equals( nextStateName ) )
			getStateMachine().transition( ERROR_STATE );
		else
			throw new UsbRuntimeException( "Invalid StateMachine.State name '" + nextStateName + "'" );
	}

	/**
	 * Indicate that a specific UsbIrpImp has completed.
	 * <p>
	 * This should be called (by the platform implementation) to indicate
	 * that the specified UsbIrpImp has completed (and is not resubmitting).
	 * @param irp the UsbIrpImp that completed.
	 */
	public void UsbIrpImpCompleted( UsbIrpImp irp )
	{
		decrementSubmissionCount();
	}

	/**
	 * Set this UsbPipe's active status.
	 * @param active whether this pipe should become active or inactive.
	 * @throws javax.usb.UsbRuntimeException if the current pipe state doesn't allow the change.
	 */
	public void setActive( boolean active )
	{
		try {
			if (!active)
				transition( INACTIVE_STATE.getName() );
			else if (!isActive())
				transition( CLOSED_STATE.getName() );
		} catch ( StateMachineException smE ) {
			throw new UsbRuntimeException( "Cannot change to " + ( active ? "active" : "inactive" ) + " state from current state." );
		}
	}

    //-------------------------------------------------------------------------
    // Instance variables
    //

    private UsbPipeImp usbPipeImp = null;

	private UsbPipeEventHelper usbPipeEventHelper = null;

	private Object submissionCountLock = new Object();
	private int submissionCount = 0;

	private int submitResultCount = 0;

	private UsbEndpoint usbEndpoint = null;

    //-------------------------------------------------------------------------
    // Constants
    //

	private final UsbPipeState INACTIVE_STATE = new UsbPipeInactiveState( this );
	private final UsbPipeState CLOSED_STATE = new UsbPipeClosedState( this );
	private final UsbPipeState IDLE_STATE = new UsbPipeIdleState( this );
	private final UsbPipeState BUSY_STATE = new UsbPipeBusyState( this );
	private final UsbPipeState ERROR_STATE = new UsbPipeErrorState( this );

	private final UsbPipeStateMachine stateMachine = new UsbPipeStateMachine( INACTIVE_STATE );

	//*************************************************************************
	// Inner classes

	public class UsbPipeStateMachine extends DefaultStateMachine
	{
		/** Constructor */
		public UsbPipeStateMachine( StateMachine.State state ) { super( state ); }

		//*********************************************************************
		// Public methods

		/**
		 * Get the current state with the intent to submit.
		 * <p>
		 * This should be done synchronized on the StateMachine; the
		 * returned state should be used for the submission.
		 * <p>
		 * The appropriate states will increment the submission count during this.
		 */
		public synchronized UsbPipeState getUsbPipeStateForSubmission()
		{
			UsbPipeState state = (UsbPipeState)getCurrentState();

			if (state.shouldIncrementSubmissionCount())
				UsbPipeAbstraction.this.incrementSubmissionCount();

			return state;
		}

		/**
		 * Get the current state for an operation besides submission.
		 */
		public synchronized UsbPipeState getUsbPipeState()
		{ return (UsbPipeState)getCurrentState(); }
	};

}

