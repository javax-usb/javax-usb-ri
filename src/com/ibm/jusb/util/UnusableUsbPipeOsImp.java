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

import javax.usb.*;

import com.ibm.jusb.*;
import com.ibm.jusb.os.*;

/**
 * Unusable UsbPipeOsImp implementation.
 * <p>
 * No methods are usable for this implementation; all throw UsbExceptions.
 * The String contents of the UsbExceptions is settable.
 * @author Dan Streetman
 */
public class UnusableUsbPipeOsImp extends AbstractUsbPipeOsImp implements UsbPipeOsImp
{
	/** Constructor. */
	public UnusableUsbPipeOsImp() { }

	/**
	 * Constructor.
	 * @param open The String to use in UsbExceptions thrown in {@link #open() open()}.
	 * @param submit The String to use in UsbExceptions thrown in all submit methods.
	 */
	public UnusableUsbPipeOsImp(String open, String submit)
	{
		openString = open;
		syncSubmitListString = submit;
		asyncSubmitListString = submit;
		syncSubmitUsbIrpString = submit;
		asyncSubmitUsbIrpString = submit;
	}

	/**
	 * Constructor.
	 * @param open The String to use in UsbExceptions thrown in {@link #open() open()}.
	 * @param syncSubmitList The String to use in UsbExceptions thrown in {@link #syncSubmit(List) syncSubmit(List)}.
	 * @param asyncSubmitList The String to use in UsbExceptions thrown in {@link #asyncSubmit(List) asyncSubmit(List)}.
	 * @param syncSubmitUsbIrp The String to use in UsbExceptions thrown in {@link #syncSubmit(UsbIrp) syncSubmit(UsbIrp)}.
	 * @param asyncSubmitUsbIrp The String to use in UsbExceptions thrown in {@link #asyncSubmit(UsbIrp) asyncSubmit(UsbIrp)}.
	 */
	public UnusableUsbPipeOsImp(String open, String syncSubmitList, String asyncSubmitList, String syncSubmitUsbIrp, String asyncSubmitUsbIrp)
	{
		openString = open;
		syncSubmitListString = syncSubmitList;
		asyncSubmitListString = asyncSubmitList;
		syncSubmitUsbIrpString = syncSubmitUsbIrp;
		asyncSubmitUsbIrpString = asyncSubmitUsbIrp;
	}

	/**
	 * Open this pipe.
	 * @exception UsbException This pipe cannot be opened.
	 */
	public void open() throws UsbException { throw new UsbException(getOpenString()); }

	/**
	 * Synchronously submits a List of UsbIrpImps to the platform implementation.
	 * @param list the UsbIrpImps to use for this submission.
	 * @exception UsbException This pipe cannot be used.
	 */
	public void syncSubmit( List list ) throws UsbException { throw new UsbException(getSyncSubmitListString()); }

	/**
	 * Asynchronously submits a List of UsbIrpImps to the platform implementation.
	 * @param list The List of UsbIrpImps.
	 * @exception UsbException This pipe cannot be used.
	 */
	public void asyncSubmit( List list ) throws UsbException { throw new UsbException(getAsyncSubmitListString()); }

	/**
	 * Synchronously submits this UsbIrpImp to the platform implementation.
	 * @param irp the UsbIrpImp to use for this submission.
	 * @exception UsbException This pipe cannot be used.
	 */
	public void syncSubmit( UsbIrpImp irp ) throws UsbException { throw new UsbException(getSyncSubmitUsbIrpString()); }

	/**
	 * Asynchronously submits this UsbIrpImp to the platform implementation.
	 * @param irp the UsbIrpImp to use for this submission.
	 * @exception UsbException This pipe cannot be used.
	 */
	public void asyncSubmit( UsbIrpImp irp ) throws UsbException { throw new UsbException(getAsyncSubmitUsbIrpString()); }

	/**
	 * Stop all submissions in progress.
	 * <p>
	 * This is implemented as a no-op.
	 */
	public void abortAllSubmissions() { }

	/**
	 * Get the String to use in UsbExceptions thrown in {@link #open() open()}.
	 * @return The String to use in open() UsbExceptions.
	 */
	public String getOpenString() { return openString; }

	/**
	 * Get the String to use in UsbExceptions thrown in {@link #syncSubmit(List) syncSubmit(List)}.
	 * @return The String to use in syncSubmit(List) UsbExceptions.
	 */
	public String getSyncSubmitListString() { return syncSubmitListString; }

	/**
	 * Get the String to use in UsbExceptions thrown in {@link #asyncSubmit(List) asyncSubmit(List)}.
	 * @return The String to use in asyncSubmit(List) UsbExceptions.
	 */
	public String getAsyncSubmitListString() { return asyncSubmitListString; }

	/**
	 * Get the String to use in UsbExceptions thrown in {@link #syncSubmit(UsbIrp) syncSubmit(UsbIrp)}.
	 * @return The String to use in syncSubmit(UsbIrp) UsbExceptions.
	 */
	public String getSyncSubmitUsbIrpString() { return syncSubmitUsbIrpString; }

	/**
	 * Get the String to use in UsbExceptions thrown in {@link #asyncSubmit(UsbIrp) asyncSubmit(UsbIrp)}.
	 * @return The String to use in asyncSubmit(UsbIrp) UsbExceptions.
	 */
	public String getAsyncSubmitUsbIrpString() { return asyncSubmitUsbIrpString; }

	public String openString = OPEN_STRING;
	public String syncSubmitListString = SUBMIT_STRING;
	public String asyncSubmitListString = SUBMIT_STRING;
	public String syncSubmitUsbIrpString = SUBMIT_STRING;
	public String asyncSubmitUsbIrpString = SUBMIT_STRING;

	public static final String OPEN_STRING = "Cannot open this pipe.";
	public static final String SUBMIT_STRING = "Cannot use this pipe.";

	public static final String HOST_CONTROLLER_OPEN_STRING = "Cannot open a host controller pipe.";
	public static final String HOST_CONTROLLER_SUBMIT_STRING = "Cannot use a host controller pipe.";
}
