package com.ibm.jusb.event;

/*
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import java.util.*;

import com.ibm.jusb.util.*;

/**
 * Implementation of EventListener.
 * @author Dan Streetman
 */
public class EventListenerImp implements EventListener
{
	/**
	 * Add a listener.
	 * @param listener The listener to add.
	 */
	public void addEventListener( EventListener listener )
	{
		synchronized (listeners) {
			if (!listeners.containsKey(listener)) {
				EventListenerRunnableManager elrM = new EventListenerRunnableManager(listener);
				elrM.setName(getClass() + " " + hashCode() + " RunnableManager");
				elrM.setMaxSize(RunnableManager.SIZE_UNLIMITED);
				listeners.put(listener, elrM);
			}
		}
	}

	/**
	 * Remove a listener.
	 * @param listener the listener to remove.
	 */
	public void removeEventListener( EventListener listener )
	{
		synchronized (listeners) {
			if (listeners.containsKey(listener)) {
				RunnableManager rM = (RunnableManager)listeners.get(listener);
				listeners.remove(listener);
				rM.stop();
			}
		}
	}

	/**
	 * Clear all listeners.
	 */
	public void clear()
	{
		synchronized (listeners) {
			Iterator i = listeners.keySet().iterator();
			while (i.hasNext())
				removeEventListener((EventListener)i.next());
		}
	}

	/**
	 * @return If this has no listeners.
	 */
	public boolean isEmpty() { return listeners.isEmpty(); }

	protected HashMap listeners = new HashMap();

	protected class EventListenerRunnableManager extends RunnableManager
	{
		public EventListenerRunnableManager(EventListener el) { eventListener = el; }

		public EventListener getEventListener() { return eventListener; }

		protected EventListener eventListener = null;
	}
}
